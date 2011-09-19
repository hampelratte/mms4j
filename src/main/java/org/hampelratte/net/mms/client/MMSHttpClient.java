package org.hampelratte.net.mms.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.hampelratte.net.mms.MMSObject;
import org.hampelratte.net.mms.asf.objects.ASFFilePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.http.request.HttpRequest;
import org.hampelratte.net.mms.http.request.Play;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.server.ReportStreamSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MMSHttpClient extends IoHandlerAdapter implements IClient {
    private static transient Logger logger = LoggerFactory.getLogger(MMSHttpClient.class);

    /** Connect timeout in millisecods */
    public static int CONNECT_TIMEOUT = 30000;

    private List<MMSMessageListener> messageListeners = new ArrayList<MMSMessageListener>();
    private List<MMSPacketListener> packetListeners = new ArrayList<MMSPacketListener>();

    private List<IoHandler> additionalIoHandlers = new ArrayList<IoHandler>();

    private String host;
    private File path;
    private int port = 80;
    private SocketConnector connector;
    private IoSession session;

    private long lastUpdate = 0;

    private long packetCount = -1;
    private long packetsReceived = 0;
    private long packetsreceivedAtLastLog = 0;

    public MMSHttpClient(URI mmsUri) throws URISyntaxException {
        host = mmsUri.getHost();
        path = new File(mmsUri.getPath());

        connector = new NioSocketConnector();
        // connector.getFilterChain().addFirst("logger", new RawInputStreamDumpFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientHttpProtocolCodecFactory()));
        connector.setHandler(this);
    }

    @Override
    public void connect(IoFutureListener<ConnectFuture> listener) throws Exception {
        openConnection(listener);
    }

    private void openConnection(IoFutureListener<ConnectFuture> ioFutureListener) throws Exception {
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));

        if (connectFuture != null) {
            connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
            try {
                session = connectFuture.getSession();
                session.setAttribute("client.guid", UUID.randomUUID().toString());
                session.getConfig().setThroughputCalculationInterval(1);
                if (ioFutureListener != null) {
                    connectFuture.addListener(ioFutureListener);
                }
                connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
                    @Override
                    public void operationComplete(ConnectFuture cf) {
                        if (cf.isConnected()) {
                            logger.debug("Connection successful, firing rss");
                            fireMessageReceived(new ReportStreamSwitch());
                        } else {
                            logger.error("Couldn't connect to server", cf.getException());
                        }
                    }
                });
            } catch (RuntimeException e) {
                if (e.getCause() != null) {
                    exceptionCaught(session, e.getCause());
                } else {
                    exceptionCaught(session, e);
                }
            }
        } else {
            exceptionCaught(session, new IOException("Connect to host failed"));
        }
    }

    @Override
    public void disconnect(IoFutureListener<IoFuture> listener) {
        // sendRequest(new CancelProtocol());

        // cancel protocol doesn't work -> kill the connection
        if (session != null) {
            CloseFuture future = session.close(true);
            if (listener != null) {
                future.addListener(listener);
            }
        }

        if (connector != null) {
            connector.dispose();
        }
    }

    public void sendRequest(HttpRequest request) {
        if (session == null) {
            throw new RuntimeException("Not connected");
        } else {
            session.write(request);
            logger.debug("--OUT--> " + request.toString());
        }
    }

    @Override
    public void messageReceived(final IoSession session, Object message) throws Exception {
        for (IoHandler handler : additionalIoHandlers) {
            handler.messageReceived(session, message);
        }

        if (message instanceof MMSMessage) {
            logger.debug("<--IN-- {}", message);
            fireMessageReceived((MMSObject) message);
        } else if (message instanceof MMSPacket) {
            logger.trace("<--IN-- {}, Throughput: {} KiB/s", message, getSpeed());
            if ((++packetsReceived - packetsreceivedAtLastLog) >= 100) {
                packetsreceivedAtLastLog = packetsReceived;
                logger.debug("{} data packets received. {}%", packetsReceived, getProgress());
            }
            firePacketReceived((MMSObject) message);
        }
    }

    private void firePacketReceived(MMSObject mmso) {
        for (MMSPacketListener listener : packetListeners) {
            listener.packetReceived((MMSPacket) mmso);
        }
    }

    private void fireMessageReceived(MMSObject mmso) {
        for (MMSMessageListener listener : messageListeners) {
            listener.messageReceived((MMSMessage) mmso);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn("Exception occured", cause);
        for (IoHandler handler : additionalIoHandlers) {
            handler.exceptionCaught(session, cause);
        }
    }

    @Override
    public void sessionClosed(IoSession iosession) throws Exception {
        super.sessionClosed(iosession);
        logger.info("MMS over HTTP connection closed");
        for (IoHandler handler : additionalIoHandlers) {
            handler.sessionClosed(iosession);
        }
    }

    @Override
    public void addMessageListener(MMSMessageListener listener) {
        messageListeners.add(listener);
    }

    @Override
    public void removeMessageListener(MMSMessageListener listener) {
        messageListeners.remove(listener);
    }

    @Override
    public void addPacketListener(MMSPacketListener listener) {
        packetListeners.add(listener);
    }

    @Override
    public void removePacketListener(MMSPacketListener listener) {
        packetListeners.remove(listener);
    }

    @Override
    public void addAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.add(handler);
    }

    @Override
    public void removeAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.remove(handler);
    }

    @Override
    public double getSpeed() {
        if (session != null) {
            if ((System.currentTimeMillis() - lastUpdate) > 1000) {
                lastUpdate = System.currentTimeMillis();
                session.updateThroughput(System.currentTimeMillis(), true);
            }
            return session.getReadBytesThroughput() / 1024;
        }

        return 0;
    }

    @Override
    public boolean isPauseSupported() {
        // TODO implement this
        // if (session != null) {
        // @SuppressWarnings("unchecked")
        // List<String> features = (List<String>) session.getAttribute("features");
        // return features != null && features.contains("seekable");
        // } else {
        // return false;
        // }
        return false;
    }

    @Override
    public int getProgress() {
        // try to determine the packet count
        if (packetCount == -1) {
            ASFToplevelHeader header = (ASFToplevelHeader) session.getAttribute("asf.top.level.header");
            if (header != null) {
                ASFFilePropertiesObject props = (ASFFilePropertiesObject) header.getNestedHeader(ASFFilePropertiesObject.class);
                if (props != null) {
                    packetCount = props.getDataPacketCount();
                }
            }
        }

        if (packetCount == -1) {
            return -1;
        } else {
            return (int) (((double) packetsReceived / (double) packetCount) * 100);
        }
    }

    @Override
    public void messageSent(IoSession iosession, Object obj) throws Exception {
        super.messageSent(iosession, obj);
        for (IoHandler handler : additionalIoHandlers) {
            handler.messageSent(iosession, obj);
        }
    }

    @Override
    public void sessionCreated(IoSession iosession) throws Exception {
        super.sessionCreated(iosession);
        for (IoHandler handler : additionalIoHandlers) {
            handler.sessionCreated(iosession);
        }
    }

    @Override
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus) throws Exception {
        super.sessionIdle(iosession, idlestatus);
        for (IoHandler handler : additionalIoHandlers) {
            handler.sessionIdle(iosession, idlestatus);
        }
    }

    @Override
    public void sessionOpened(IoSession iosession) throws Exception {
        super.sessionOpened(iosession);
        for (IoHandler handler : additionalIoHandlers) {
            handler.sessionOpened(iosession);
        }
    }

    /**
     * Starts the streaming
     * 
     * @param startPacket
     *            the packetNumber from which the streaming should start
     */
    @Override
    public void startStreaming(long startPacket) {
        logger.info("Staring streaming from packet {}", startPacket);
        packetsReceived = startPacket;
        Play play = new Play((String) session.getAttribute("client.guid"));
        play.setPath(path.getAbsolutePath());
        play.setHost(host);
        play.setPacketNum(startPacket);
        sendRequest(play);
    }
}

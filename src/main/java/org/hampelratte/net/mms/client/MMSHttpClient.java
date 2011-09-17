package org.hampelratte.net.mms.client;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
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
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.http.request.HttpRequest;
import org.hampelratte.net.mms.http.request.Play;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.StartPlaying;
import org.hampelratte.net.mms.messages.server.ReportOpenFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MMSHttpClient extends IoHandlerAdapter {
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

    private long packetsReceived = 0;
    private long packetsreceivedAtLastLog = 0;

    public MMSHttpClient(String mmsUri) throws URISyntaxException {
        URI uri = new URI(mmsUri);
        host = uri.getHost();
        path = new File(uri.getPath());
        // String filepath = path.getParentFile().getPath();
        // if (filepath.startsWith("/")) {
        // filepath = filepath.substring(1);
        // }
        // final String filename = path.getName();

        connector = new NioSocketConnector();
        // connector.getFilterChain().addFirst("logger", new RawInputStreamDumpFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientHttpProtocolCodecFactory()));
        connector.setHandler(this);
    }

    public void connect() throws Exception {
        openConnection(new IoFutureListener<ConnectFuture>() {
            @Override
            public void operationComplete(ConnectFuture cf) {
                if (cf.isConnected()) {
                    // set throughput calculation interval
                    session.getConfig().setThroughputCalculationInterval(1);

                    // set the first sequence number
                    session.setAttribute("mms.sequence", 0);

                    // get stream info
                    // Describe desc = new Describe((String) session.getAttribute("client.guid"));
                    // desc.setPath(path.getAbsolutePath());
                    // desc.setHost(host);
                    // sendRequest(desc);

                    Play play = new Play((String) session.getAttribute("client.guid"));
                    play.setPath(path.getAbsolutePath());
                    play.setHost(host);
                    sendRequest(play);
                } else {
                    try {
                        exceptionCaught(session, cf.getException());
                    } catch (Exception e) {
                        logger.error("Couldn't propagate exception", e);
                    }
                }
            }
        });
    }

    private void openConnection(IoFutureListener<ConnectFuture> ioFutureListener) throws Exception {
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));

        if (connectFuture != null) {
            connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
            try {
                session = connectFuture.getSession();
                session.setAttribute("client.guid", UUID.randomUUID().toString());
                connectFuture.addListener(ioFutureListener);
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

    public void disconnect(IoFutureListener<IoFuture> listener) {
        // sendRequest(new CancelProtocol());

        // cancel protocol doesn't work -> kill the connection
        if (session != null) {
            CloseFuture future = session.close(true);
            future.addListener(listener);
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

        logger.debug("<--IN-- {}", message);
        // if (message instanceof ASFObject) {
        // // close the connection and reinitialize a new one
        // logger.debug("Closing connection");
        // disconnect(new IoFutureListener<IoFuture>() {
        // @Override
        // public void operationComplete(IoFuture future) {
        // logger.debug("Connection closed");
        // // closing the connection finished, now open a new one
        // // first create a new connector
        // connector = new NioSocketConnector();
        // // connector.getFilterChain().addFirst("logger", new RawInputStreamDumpFilter());
        // connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientHttpProtocolCodecFactory()));
        // connector.setHandler(MMSHttpClient.this);
        // logger.debug("New connector created");
        //
        // // now open the connection
        // try {
        // logger.debug("Open new connection");
        // openConnection(new IoFutureListener<ConnectFuture>() {
        // @Override
        // public void operationComplete(ConnectFuture cf) {
        // if (cf.isConnected()) {
        // logger.debug("Connection opened");
        // // set throughput calculation interval
        // session.getConfig().setThroughputCalculationInterval(1);
        //
        // // set the first sequence number
        // session.setAttribute("mms.sequence", 0);
        //
        // // start streaming
        // Play play = new Play((String) session.getAttribute("client.guid"));
        // play.setPath(path.getAbsolutePath());
        // play.setHost(host);
        // sendRequest(play);
        // } else {
        // logger.debug("Failed to open connection");
        // try {
        // exceptionCaught(session, cf.getException());
        // } catch (Exception e) {
        // logger.error("Couldn't propagate exception", e);
        // e.printStackTrace();
        // }
        // }
        // }
        // });
        // } catch (Exception e) {
        // try {
        // exceptionCaught(session, e);
        // } catch (Exception e1) {
        // logger.error("Couldn't propagate exception", e);
        // e1.printStackTrace();
        // }
        // }
        // }
        // });
        // }

        if (message instanceof MMSMessage) {
            logger.debug("Message received");
            fireMessageReceived((MMSObject) message);
        } else if (message instanceof MMSPacket) {
            logger.debug("Packet received");
            if ((++packetsReceived - packetsreceivedAtLastLog) >= 100) {
                packetsreceivedAtLastLog = packetsReceived;
                logger.debug("{} data packets received", packetsReceived);
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
        if (cause instanceof ConnectException) {
            logger.error("Couldn't connect to server. Now trying HTTP rollover");
            logger.trace("Connection exception", cause);
            this.port = 80;
            connect();
        } else {
            logger.warn("Exception occured", cause);
            for (IoHandler handler : additionalIoHandlers) {
                handler.exceptionCaught(session, cause);
            }
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

    public void addMessageListener(MMSMessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MMSMessageListener listener) {
        messageListeners.remove(listener);
    }

    public void addPacketListener(MMSPacketListener listener) {
        packetListeners.add(listener);
    }

    public void removePacketListener(MMSPacketListener listener) {
        packetListeners.remove(listener);
    }

    public void addAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.add(handler);
    }

    public void removeAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.remove(handler);
    }

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
    public void startStreaming(long startPacket) {
        StartPlaying sp = new StartPlaying();
        ReportOpenFile rof = (ReportOpenFile) session.getAttribute(ReportOpenFile.class);
        sp.setOpenFileId(rof.getOpenFileId());

        /*
         * this confuses me: we use the packet number to seek the start of streaming. in my opinion we should have to use setLocationId for packet numbers, but
         * it only works correctly with setAsfOffset ?!? maybe the sequence of the values is wrong in the spec
         */
        sp.setPosition(Double.MAX_VALUE);
        sp.setLocationId(0xFFFFFFFF);
        if (startPacket > 0) {
            sp.setAsfOffset(startPacket);
        }
        // sendRequest(sp);
    }
}

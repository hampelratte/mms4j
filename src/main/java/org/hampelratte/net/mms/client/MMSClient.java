package org.hampelratte.net.mms.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.hampelratte.net.mms.asf.objects.ASFFilePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.hampelratte.net.mms.data.MMSHeaderPacket;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.CancelProtocol;
import org.hampelratte.net.mms.messages.client.Connect;
import org.hampelratte.net.mms.messages.client.ConnectFunnel;
import org.hampelratte.net.mms.messages.client.MMSRequest;
import org.hampelratte.net.mms.messages.client.OpenFile;
import org.hampelratte.net.mms.messages.client.ReadBlock;
import org.hampelratte.net.mms.messages.client.StartPlaying;
import org.hampelratte.net.mms.messages.client.StreamSwitch;
import org.hampelratte.net.mms.messages.server.ReportOpenFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MMSClient extends IoHandlerAdapter implements IClient {
    private static transient Logger logger = LoggerFactory.getLogger(MMSClient.class);

    /** Connect timeout in millisecods */
    public static int CONNECT_TIMEOUT = 30000;

    private List<MMSMessageListener> messageListeners = new ArrayList<MMSMessageListener>();
    private List<MMSPacketListener> packetListeners = new ArrayList<MMSPacketListener>();

    private List<IoHandler> additionalIoHandlers = new ArrayList<IoHandler>();

    private URI mmsUri;
    private String host;
    private int port = 1755;
    private SocketConnector connector;
    private IoSession session;
    private MMSNegotiator negotiator;

    private long lastUpdate = 0;

    private long packetCount = -1;
    private long packetsReceived = 0;
    private long packetsreceivedAtLastLog = 0;

    public MMSClient(URI mmsUri) {
        this.mmsUri = mmsUri;
        createNegotiater();

        connector = new NioSocketConnector();
        // connector.getFilterChain().addFirst("logger", new RawInputStreamDumpFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientMmsProtocolCodecFactory()));
        connector.setHandler(this);
        this.addMessageListener(negotiator);
        this.addPacketListener(negotiator);
    }

    private void createNegotiater() {
        host = mmsUri.getHost();
        File path = new File(mmsUri.getPath());
        String filepath = path.getParentFile().getPath();
        if (filepath.startsWith("/")) {
            filepath = filepath.substring(1);
        }
        final String filename = path.getName();

        negotiator = new MMSNegotiator();
        negotiator.setClient(this);

        // configure the negotiator
        // connect
        Connect connect = new Connect();
        connect.setPlayerInfo("NSPlayer/12.0.7724.0");
        connect.setGuid(UUID.randomUUID().toString());
        connect.setHost(host);
        negotiator.setConnect(connect);
        // connect funnel
        ConnectFunnel cf = new ConnectFunnel();
        cf.setIpAddress("192.168.0.1");
        cf.setProtocol("TCP");
        cf.setPort("1037");
        negotiator.setConnectFunnel(cf);
        // open file
        OpenFile of = new OpenFile();
        String remoteFile = filepath + '/' + filename;
        logger.info("Remote file is {}", remoteFile);
        of.setFileName(remoteFile);
        negotiator.setOpenFile(of);
        // read block
        ReadBlock rb = new ReadBlock();
        negotiator.setReadBlock(rb);
        // stream switch
        StreamSwitch ss = new StreamSwitch();
        ss.addStreamSwitchEntry(ss.new StreamSwitchEntry(0xFFFF, 1, 0));
        ss.addStreamSwitchEntry(ss.new StreamSwitchEntry(0xFFFF, 2, 0));
        negotiator.setStreamSwitch(ss);
    }

    public MMSNegotiator getNegotiator() {
        return negotiator;
    }

    @Override
    public void connect(IoFutureListener<ConnectFuture> listener) throws Exception {
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));

        if (connectFuture != null) {
            try {
                connectFuture.awaitUninterruptibly(CONNECT_TIMEOUT);
                session = connectFuture.getSession();
                if (listener != null) {
                    connectFuture.addListener(listener);
                }
                connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
                    @Override
                    public void operationComplete(ConnectFuture cf) {
                        if (cf.isConnected()) {
                            // set throughput calculation interval
                            session.getConfig().setThroughputCalculationInterval(1);

                            // set the first sequence number
                            session.setAttribute("mms.sequence", 0);

                            // start the streaming negotiation
                            negotiator.start(session);
                        } else {
                            try {
                                exceptionCaught(session, cf.getException());
                            } catch (Exception e) {
                                logger.error("Couldn't propagate exception", e);
                            }
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
        sendRequest(new CancelProtocol());

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

    public void sendRequest(MMSRequest request) {
        if (session == null) {
            throw new RuntimeException("Not connected");
        } else {
            session.write(request);
            logger.debug("--OUT--> " + request.toString());
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        for (IoHandler handler : additionalIoHandlers) {
            handler.messageReceived(session, message);
        }

        MMSObject mmso = (MMSObject) message;
        if (mmso instanceof MMSMessage) {
            logger.debug("<--IN-- " + mmso.toString());
            fireMessageReceived(mmso);
        } else {
            if ((++packetsReceived - packetsreceivedAtLastLog) >= 100) {
                packetsreceivedAtLastLog = packetsReceived;
                logger.debug("{} data packets received", packetsReceived);
            }
            firePacketReceived(mmso);
        }
    }

    private void firePacketReceived(MMSObject mmso) {
        if (mmso instanceof MMSHeaderPacket) {
            MMSHeaderPacket hp = (MMSHeaderPacket) mmso;
            try {
                ByteArrayInputStream bin = new ByteArrayInputStream(hp.getData());
                ASFInputStream asfin = new ASFInputStream(bin);
                ASFObject asfo = asfin.readASFObject();
                if (asfo instanceof ASFToplevelHeader) {
                    ASFToplevelHeader asfHeader = (ASFToplevelHeader) asfo;
                    session.setAttribute("asf.top.level.header", asfHeader);
                    logger.debug("ASF header: {}", asfHeader);
                    ASFFilePropertiesObject fileprops = (ASFFilePropertiesObject) asfHeader.getNestedHeader(ASFFilePropertiesObject.class);
                    if (fileprops != null) {
                        packetCount = fileprops.getDataPacketCount();
                    } else {
                        packetCount = -1;
                    }
                }
            } catch (Exception e) {
                logger.warn("Ignoring unknown ASF header object", e);
            }
        }

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
        for (IoHandler handler : additionalIoHandlers) {
            handler.exceptionCaught(session, cause);
        }
    }

    @Override
    public void sessionClosed(IoSession iosession) throws Exception {
        super.sessionClosed(iosession);
        logger.info("MMS connection closed");
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
        return negotiator != null && negotiator.isResumeSupported();
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
        StartPlaying sp = new StartPlaying();
        ReportOpenFile rof = (ReportOpenFile) session.getAttribute(ReportOpenFile.class);
        sp.setOpenFileId(rof.getOpenFileId());

        // try to request a higher bandwidth for the first 30 seconds, to quickly fill up buffers
        sp.setDwLinkBandwidth(6000000);
        sp.setDwAccelBandwidth(6000000);
        long duration = TimeUnit.HOURS.toMillis(12);
        sp.setDwAccelDuration(duration);

        /*
         * this confuses me: we use the packet number to seek the start of streaming. in my opinion we should have to use setLocationId for packet numbers, but
         * it only works correctly with setAsfOffset ?!? maybe the sequence of the values is wrong in the spec
         */
        sp.setPosition(Double.MAX_VALUE);
        sp.setLocationId(0xFFFFFFFF);
        if (startPacket > 0) {
            sp.setAsfOffset(startPacket);
        }
        sendRequest(sp);
    }

    @Override
    // TODO introduce abstract client class to reduce code redundancies like in MMSClient.getProgress() and MMSHttpClient.getProgress()
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
}

package org.hampelratte.net.mms.client;

import java.net.ConnectException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends IoHandlerAdapter implements IClient {
    private static transient Logger logger = LoggerFactory.getLogger(Client.class);

    private List<MMSMessageListener> messageListeners = new ArrayList<MMSMessageListener>();
    private List<MMSPacketListener> packetListeners = new ArrayList<MMSPacketListener>();
    private List<IoHandler> additionalIoHandlers = new ArrayList<IoHandler>();

    private IClient delegate;
    private URI mmsUri;
    private IoFutureListener<ConnectFuture> connectListener;

    public Client(URI mmsUri) {
        this.mmsUri = mmsUri;
        delegate = new MMSClient(mmsUri);
        delegate.addAdditionalIoHandler(this);
    }

    @Override
    public void connect(final IoFutureListener<ConnectFuture> listener) throws Exception {
        this.connectListener = listener;
        delegate.connect(new IoFutureListener<ConnectFuture>() {
            @Override
            public void operationComplete(ConnectFuture future) {
                if (listener != null) {
                    listener.operationComplete(future);
                }
            }
        });
    }

    @Override
    public void disconnect(IoFutureListener<IoFuture> listener) {
        delegate.disconnect(listener);
    }

    /**
     * Returns the throughput of the current transmission in KiB/s.
     * 
     * @return the throughput in KiB/s
     */
    @Override
    public double getSpeed() {
        return delegate.getSpeed();
    }

    @Override
    public int getProgress() {
        return delegate.getProgress();
    }

    @Override
    public boolean isPauseSupported() {
        return delegate.isPauseSupported();
    }

    /**
     * Starts the streaming
     * 
     * @param startPacket
     *            the packetNumber from which the streaming should start
     */
    @Override
    public void startStreaming(long startPacket) {
        delegate.startStreaming(startPacket);
    }

    @Override
    public void addMessageListener(MMSMessageListener listener) {
        messageListeners.add(listener);
        delegate.addMessageListener(listener);
    }

    @Override
    public void removeMessageListener(MMSMessageListener listener) {
        messageListeners.remove(listener);
        delegate.removeMessageListener(listener);
    }

    @Override
    public void addPacketListener(MMSPacketListener listener) {
        packetListeners.add(listener);
        delegate.addPacketListener(listener);
    }

    @Override
    public void removePacketListener(MMSPacketListener listener) {
        packetListeners.remove(listener);
        delegate.addPacketListener(listener);
    }

    @Override
    public void addAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.add(handler);
        delegate.addAdditionalIoHandler(handler);
    }

    @Override
    public void removeAdditionalIoHandler(IoHandler handler) {
        additionalIoHandlers.remove(handler);
        delegate.removeAdditionalIoHandler(handler);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (cause instanceof ConnectException) {
            logger.warn("Couldn't connect to server. Now trying MMS over HTTP", cause);
            delegate = new MMSHttpClient(mmsUri);

            // transfer all listeners from the old delegate to the new
            for (MMSMessageListener listener : messageListeners) {
                delegate.addMessageListener(listener);
            }
            for (MMSPacketListener listener : packetListeners) {
                delegate.addPacketListener(listener);
            }
            for (IoHandler listener : additionalIoHandlers) {
                delegate.addAdditionalIoHandler(listener);
            }

            delegate.connect(connectListener);
        }
    }
}

package org.hampelratte.net.mms.client;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.hampelratte.net.mms.client.listeners.MMSMessageListener;
import org.hampelratte.net.mms.client.listeners.MMSPacketListener;

public interface IClient extends IoHandler {

    public void connect(IoFutureListener<ConnectFuture> listener) throws Exception;

    public void disconnect(IoFutureListener<IoFuture> listener);

    public void startStreaming(long startPacket);

    public int getProgress();

    public double getSpeed();

    public void addMessageListener(MMSMessageListener listener);

    public void removeMessageListener(MMSMessageListener listener);

    public void addPacketListener(MMSPacketListener listener);

    public void removePacketListener(MMSPacketListener listener);

    public void addAdditionalIoHandler(IoHandler handler);

    public void removeAdditionalIoHandler(IoHandler handler);
}

package org.hampelratte.net.mms.client.listeners;

import org.apache.mina.core.session.IoSession;

public interface MMSErrorListener {
    public void mmsErrorOccured(Throwable t, IoSession session);
}

package org.hampelratte.net.mms.client.listeners;

import org.hampelratte.net.mms.data.MMSPacket;

public interface MMSPacketListener {
    public void packetReceived(MMSPacket packet);
}

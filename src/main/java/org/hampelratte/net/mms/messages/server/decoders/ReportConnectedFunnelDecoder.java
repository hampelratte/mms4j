package org.hampelratte.net.mms.messages.server.decoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportConnectedFunnel;

/**
 * Decoder for {@link ReportConnectedFunnel} objects.
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportConnectedFunnelDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws Exception {
        ReportConnectedFunnel rce = new ReportConnectedFunnel();
        rce.setHr(b.getInt());
        rce.setPlayIncarnation(b.getInt());
        rce.setPacketPayloadSize(b.getInt());

        // read the "Funnel Of The Gods" or "Funnel Of The " crap
        int funnelNameLength = header.getMessageLength() == 80 ? 36 : 28; 
        b.order(ByteOrder.BIG_ENDIAN);
        byte[] stringData = new byte[funnelNameLength];
        b.get(stringData);
        String text = new String(stringData, "UTF-16LE");
        rce.setFunnelName(text);
        
        return rce;
    }

}

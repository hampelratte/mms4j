package org.hampelratte.net.mms.data.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hampelratte.net.mms.data.MMSHeaderPacket;
import org.hampelratte.net.mms.data.MMSMediaPacket;
import org.hampelratte.net.mms.data.MMSPacket;
import org.hampelratte.net.mms.data.PacketSizeMissingException;
import org.hampelratte.net.mms.io.UnknownHeaderException;
import org.hampelratte.net.mms.messages.server.ReportOpenFile;

/**
 * Decoder for packets containing streaming data
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class MMSPacketDecoder {
    /**
     * Decodes streaming data
     * @param session
     * @param b
     * @param out
     * @throws Exception
     */
    public void decode(IoSession session, IoBuffer b, ProtocolDecoderOutput out) throws Exception {
        int position = b.position();
        
        long locationId = b.getUnsignedInt();
        byte playIncarnation = b.get();
        byte afFlags = b.get();
        int length = b.getUnsignedShort();
        
        MMSPacket packet;
        if(playIncarnation == MMSHeaderPacket.PACKET_ID) {
            packet = new MMSHeaderPacket(locationId, afFlags, length);
        } else if(playIncarnation == MMSMediaPacket.PACKET_ID) {
            packet = new MMSMediaPacket(locationId, afFlags, length);
        } else {
            byte[] header = new byte[8];
            b.position(position);
            b.get(header);
            throw new UnknownHeaderException(header);
        }
        
        // read the data
        byte[] data = new byte[length - 8];
        b.get(data);
        packet.setData(data);
        
        // add padding for data packets
        if(playIncarnation == MMSMediaPacket.PACKET_ID) {
            ReportOpenFile rof = (ReportOpenFile) session.getAttribute(ReportOpenFile.class);
            if(rof == null) {
                throw new PacketSizeMissingException();
            }
            int padding = (int) (rof.getFilePacketSize() - (length - 8));
            MMSMediaPacket mmp = (MMSMediaPacket) packet;
            mmp.addPadding(padding);
        }
        
        out.write(packet);
    }
}

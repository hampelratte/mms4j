package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportReadBlock;

/**
 * Decoder for {@link ReportReadBlock} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportReadBlockDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws Exception {
        ReportReadBlock rrb = new ReportReadBlock();
        rrb.setHr(b.getInt());
        rrb.setPlayIncarnation(b.getInt());
        rrb.setPlaySequence(b.getInt());
        
        return rrb;
    }

}

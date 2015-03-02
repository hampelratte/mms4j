package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportReadBlock;

/**
 * Decoder for {@link ReportReadBlock} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportReadBlockDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws RemoteException {
        ReportReadBlock rrb = new ReportReadBlock();
        rrb.setHr(b.getInt());
        if (rrb.getHr() != 0) {
            throw new RemoteException(rrb.getHr());
        }
        rrb.setPlayIncarnation(b.getInt());
        rrb.setPlaySequence(b.getInt());

        return rrb;
    }

}

package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportEndOfStream;

/**
 * Decoder for {@link ReportEndOfStream} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportEndOfStreamDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws RemoteException {
        ReportEndOfStream eos = new ReportEndOfStream();
        eos.setHr(b.getInt());
        if (eos.getHr() != 0) {
            throw new RemoteException(eos.getHr());
        }
        eos.setPlayIncarnation(b.getInt());
        return eos;
    }

}

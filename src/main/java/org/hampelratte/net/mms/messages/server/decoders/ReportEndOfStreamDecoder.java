package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportEndOfStream;

/**
 * Decoder for {@link ReportEndOfStream} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportEndOfStreamDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws Exception {
        ReportEndOfStream eos = new ReportEndOfStream();
        eos.setHr(b.getInt());
        if(eos.getHr() != 0) {
            throw new RemoteException(HRESULT.hrToHumanReadable(eos.getHr()));
        }
        eos.setPlayIncarnation(b.getInt());
        return eos;
    }

}

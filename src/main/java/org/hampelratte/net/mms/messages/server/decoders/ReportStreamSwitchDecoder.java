package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportStreamSwitch;

/**
 * Decoder for {@link ReportStreamSwitch} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportStreamSwitchDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws RemoteException {
        ReportStreamSwitch rss = new ReportStreamSwitch();
        rss.setHr(b.getInt());
        if (rss.getHr() != 0) {
            throw new RemoteException(rss.getHr());
        }
        return rss;
    }

}

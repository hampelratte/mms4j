package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportStartedPlaying;

/**
 * Decoder for {@link ReportStartedPlaying} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportStartedPlayingDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws Exception {
        ReportStartedPlaying rsp = new ReportStartedPlaying();
        rsp.setHr(b.getInt());
        rsp.setPlayIncarnation(b.getInt());
        rsp.setTigerFileId(b.getInt());
        b.skip(16); // skip 4 + 12 bytes of unused1 and unused2
        return rsp;
    }
}

package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportOpenFile;

/**
 * Decoder for {@link ReportOpenFile} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportOpenFileDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws Exception {
        ReportOpenFile rof = new ReportOpenFile();
        rof.setHr(b.getInt());
        if(rof.getHr() != 0) {
            throw new RemoteException(HRESULT.hrToHumanReadable(rof.getHr()));
        }
        rof.setPlayIncarnation(b.getInt());
        rof.setOpenFileId(b.getInt());
        rof.setPadding(b.getInt());
        rof.setFileName(b.getInt());
        rof.setFileAttributes(b.getInt());
        rof.setFileDuration(b.getDouble());
        rof.setFileBlocks(b.getUnsignedInt());
        
        // skip 16 reserverd bytes
        b.skip(16);
        
        rof.setFilePacketSize(b.getUnsignedInt());
        rof.setFilePacketCount(b.getLong()); // this is an unsigned long, which
                                            // could result in a negative value
                                            // due to two's complement. for
                                            // simplicity, we read a signed long
                                            // and risk a negative value
        rof.setFileBitrate(b.getUnsignedInt());
        rof.setFileHeaderSize(b.getUnsignedInt());
        
        // skip 36 reserved bytes
        b.skip(36);
        
        return rof;
    }

}

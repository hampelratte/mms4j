package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
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
        ReportOpenFile md = new ReportOpenFile();
        md.setHr(b.getInt());
        md.setPlayIncarnation(b.getInt());
        md.setOpenFileId(b.getInt());
        md.setPadding(b.getInt());
        md.setFileName(b.getInt());
        md.setFileAttributes(b.getInt());
        md.setFileDuration(b.getDouble());
        md.setFileBlocks(b.getUnsignedInt());
        
        // skip 16 reserverd bytes
        b.skip(16);
        
        md.setFilePacketSize(b.getUnsignedInt());
        md.setFilePacketCount(b.getLong()); // this is an unsigned long, which
                                            // could result in a negative value
                                            // due to two's complement. for
                                            // simplicity, we read a signed long
                                            // and risk a negative value
        md.setFileBitrate(b.getUnsignedInt());
        md.setFileHeaderSize(b.getUnsignedInt());
        
        // skip 36 reserved bytes
        b.skip(36);
        
        return md;
    }

}

package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.hampelratte.net.mms.messages.client.OpenFile;

/**
 * Encoder for {@link OpenFile} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class OpenFileEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception {
        OpenFile of = (OpenFile) command;
        
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int)of.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        b.putInt(of.getPlayIncarnation());
        b.putInt(of.getSpare());
        b.putUnsignedInt(of.getToken());
        b.putUnsignedInt(of.getCbToken());
        b.putString(of.getFileName(), StringUtils.getEncoder("UTF-16LE"));
        b.putShort((short)0); // UTF-16 NULL-byte // NOTE diff from spec. this should only be written, if cbToken > 0
        if(of.getCbToken() > 0) {
            b.put(of.getTokenData());
        }
        
        b.flip();
        out.write(b);
    }
}

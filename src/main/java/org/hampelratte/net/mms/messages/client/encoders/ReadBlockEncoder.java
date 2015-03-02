package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.messages.client.ReadBlock;

/**
 * Encoder for {@link ReadBlock} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReadBlockEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        ReadBlock rb = (ReadBlock) message;
        
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int)rb.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        b.putInt(1); //b.putInt(rb.getOpenFileId());
        b.putLong(0);
        b.putLong(0);
        b.putLong(0);
        b.putLong(0);
        b.putInt(0);
//        b.putInt(rb.getFileBlockId());
//        b.putInt(rb.getOffset());
//        b.putInt(rb.getLength());
//        b.putInt(rb.getFlags());
//        b.putInt(rb.getPadding());
//        b.putDouble(rb.getTEarliest());
//        b.putDouble(rb.getTDeadline());
        b.putInt(rb.getPlayIncarnation());
        b.putInt(rb.getPlaySequence());
        
        b.flip();
        out.write(b);
    }

}

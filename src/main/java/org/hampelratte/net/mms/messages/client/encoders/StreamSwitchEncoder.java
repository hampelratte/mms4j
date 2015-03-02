package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.messages.client.StreamSwitch;
import org.hampelratte.net.mms.messages.client.StreamSwitch.StreamSwitchEntry;

/**
 * Encoder for {@link StreamSwitch} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class StreamSwitchEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        StreamSwitch ss = (StreamSwitch) message;
        
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int)ss.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        b.putUnsignedInt(ss.getStreamSwitchEntries().size());
        for (StreamSwitchEntry sse : ss.getStreamSwitchEntries()) {
            b.putUnsignedShort(sse.getSrcStreamNumber());
            b.putUnsignedShort(sse.getDstStreamNumber());
            b.putUnsignedShort(sse.getThinningLevel());
        }
        
        b.flip();
        out.write(b);
    }
}
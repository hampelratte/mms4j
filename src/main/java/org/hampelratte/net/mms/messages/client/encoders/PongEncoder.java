package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.messages.client.Pong;

/**
 * Encoder for {@link Pong} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class PongEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        Pong pe = (Pong) message;
        
        IoBuffer b = IoBuffer.allocate((int)pe.getBodyLength());
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putInt(pe.getDwParam1());
        b.putInt(pe.getDwParam2());
        
        b.flip();
        out.write(b);
    }
}

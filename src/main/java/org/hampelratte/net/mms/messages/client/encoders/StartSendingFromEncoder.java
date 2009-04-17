package org.hampelratte.net.mms.messages.client.encoders;

import java.math.BigInteger;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.messages.client.StartSendingFrom;

public class StartSendingFromEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception {
        StartSendingFrom ssf = (StartSendingFrom) command;
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int) ssf.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        // NOTE these two ints are not part of the spec
        b.putInt(1);
        b.putInt(0x0001FFFF);
        
        // seektime
        b.putUnsignedLong(new BigInteger(Long.toString(ssf.getSeekTime())));
        
        // unknown
        b.putInt(0xFFFFFFFF); // FF FF FF FF
        
        // start packet sequence number (start at the beginning)
        b.putUnsignedInt(ssf.getStartPacket());
        
        // max stream time (set to infinite)
        b.putInt(0); // 00 00 00 00
        
        // packet id type
        b.putInt(4); // 04 00 00 00
        
        b.flip();
        out.write(b);
    }
}

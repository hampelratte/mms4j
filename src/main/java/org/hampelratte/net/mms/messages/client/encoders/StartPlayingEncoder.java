package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.messages.client.StartPlaying;

public class StartPlayingEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception {
        StartPlaying sp = (StartPlaying) command;
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int) sp.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        // openFileId
        b.putInt(sp.getOpenFileId());
        
        // padding
        b.putInt(sp.getPadding());
        
        // position in secs
        b.putDouble(sp.getPosition());
        
        // location id (start packet)
        b.putUnsignedInt(sp.getLocationId());
        
        // asf offset in bytes
        b.putUnsignedInt(sp.getAsfOffset());
        
        // frame offset
        b.putInt(sp.getFrameOffset());
        
        // playincarnation
        b.putInt(sp.getPlayIncarnation());
        
        if(sp.getDwAccelBandwidth() > 0 && sp.getDwAccelDuration() > 0 && sp.getDwLinkBandwidth() > 0) {
            b.putUnsignedInt(sp.getDwAccelBandwidth());
            b.putUnsignedInt(sp.getDwAccelDuration());
            b.putUnsignedInt(sp.getDwLinkBandwidth());
        }
        
        b.flip();
        out.write(b);
    }
}

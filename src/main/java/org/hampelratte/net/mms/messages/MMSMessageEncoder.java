package org.hampelratte.net.mms.messages;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;

/**
 * Encoder for all objects sent to the server
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public abstract class MMSMessageEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        MMSMessage msg = (MMSMessage) message;
        
        // determine the size of the message and store it in the header
        TcpMessageHeader header = msg.getHeader();
        header.setMessageLength(msg.getMessageLength());
        
        // determine the sequence number and store it in the header
        int sequence = (Integer) session.getAttribute("mms.sequence");
        header.setSeq(sequence++);
        session.setAttribute("mms.sequence", sequence);
        
        // create a buffer for the header
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate(TcpMessageHeader.SIZE + 8));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        // header
        b.putByte(header.getRep());                            // 1
        b.putByte(header.getVersion());                        // 0
        b.putByte(header.getVersionMinor());                   // 0
        b.putByte(header.getPadding());                        // 0
        b.putInt(header.getSessionId());                       // CE FA B0 0B (0xb00bface)
        b.putUnsignedInt(header.getMessageLength());           // length
        b.putInt(header.getSeal());                            // 4d 4d 53 20 ("MMS ")
        b.putUnsignedInt(header.getChunkCount());              // total size in multiples of 8 bytes
        b.putUnsignedShort(header.getSeq());                   // sequence
        b.putUnsignedShort(header.getMbz());                   // MBZ
        b.putLong(header.getTimeSent());                       // timestamp 
        
        // now write chunkLen and MID. all messages have these two values in common
        b.putInt(msg.getChunkLength());
        b.putInt(msg.getMID());
        
        b.order(ByteOrder.BIG_ENDIAN);
        
        // write the buffer to the output
        b.flip();
        out.write(b);
        
        // delegate writing of the msg body to the specialized encoder
        doEncode(session, message, out);
        
        // add padding, if necessary
        if(msg.getAdditionalPadding() > 0) {
            IoBuffer padding = IoBuffer.allocate(8);
            for (int i = 0; i < msg.getAdditionalPadding(); i++) {
                padding.put((byte) 0);
            }
            padding.flip();
            out.write(padding);
        }
    }

    @Override
    public void dispose(IoSession iosession) throws Exception {
        // huh? nothing to dispose
        doDispose(iosession);
    }

    protected abstract void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception;
    
    protected abstract void doDispose(IoSession iosession);
}
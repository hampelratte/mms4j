package org.hampelratte.net.mina.nio;

import java.math.BigInteger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferWrapper;

/**
 * Extends the mina IoBuffer with support for 
 * unsigned numbers (short, int, long)
 */
public class ExtendedIoBuffer extends IoBufferWrapper {

    public ExtendedIoBuffer(IoBuffer buf) {
        super(buf);
    }

    public IoBuffer putUnsignedShort(int s) {
        putByte((int) ((s >>> 0)  & 0xff));
        putByte((int) ((s >>> 8)  & 0xff));
        return this;
    }
    
    public IoBuffer putUnsignedInt(long i) {
        putByte((int) ((i >>> 0)  & 0xff));
        putByte((int) ((i >>> 8)  & 0xff));
        putByte((int) ((i >>> 16) & 0xff));
        putByte((int) ((i >>> 24) & 0xff));
        return this;
    }
    
    public IoBuffer putUnsignedLong(BigInteger l) {
        byte[] b = l.toByteArray();
        for (int i = 0; i < 8; i++) {
            if(b.length > i) {
                put(b[i]);
            } else {
                put((byte) 0);
            }
        }
        return this;
    }
    
    
    /**
     * Writes the specified byte (the low eight bits of the argument 
     * <code>b</code>) to the buffer.
     * @param   b   the <code>byte</code> to be written.
     * @return this buffer for method chaining
     */
    public IoBuffer putByte(int b) {
        byte _byte = (byte) (b & 0xFF);
        put(_byte);
        return this;
    }
}

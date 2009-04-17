package org.hampelratte.net.mms.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianEnabledOutputStream extends DataOutputStream {
    
    public LittleEndianEnabledOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Writes a 16-bit short to the output stream in little-endian byte order.
     * @param s the short to write to the stream
     */
    public void writeLEShort(int s) throws IOException {
        out.write((s >>> 0) & 0xff);
        out.write((s >>> 8) & 0xff);
    }

    /**
     * Writes a 32-bit int to the output stream in little-endian byte order.
     * @param i the integer to write to the stream
     */
    public void writeLEInt(long i) throws IOException {
        out.write((int) ((i >>> 0) & 0xff));
        out.write((int) ((i >>> 8) & 0xff));
        out.write((int) ((i >>> 16) & 0xff));
        out.write((int) ((i >>> 24) & 0xff));
    }
    
    /**
     * Writes a 64-bit long to the output stream in little-endian byte order.
     * @param i the long to write to the stream
     */
    public void writeLELong(long i) throws IOException {
        out.write((int) ((i >>> 0) & 0xff));
        out.write((int) ((i >>> 8) & 0xff));
        out.write((int) ((i >>> 16) & 0xff));
        out.write((int) ((i >>> 24) & 0xff));
        out.write((int) ((i >>> 32) & 0xff));
        out.write((int) ((i >>> 40) & 0xff));
        out.write((int) ((i >>> 48) & 0xff));
        out.write((int) ((i >>> 56) & 0xff));
    }
}

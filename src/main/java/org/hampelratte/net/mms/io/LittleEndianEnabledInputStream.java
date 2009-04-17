package org.hampelratte.net.mms.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianEnabledInputStream extends DataInputStream {

    public LittleEndianEnabledInputStream(InputStream in) {
        super(in);
    }
    
    public int readLEShort() throws IOException {
        int i = readUnsignedShort();
        int number = 0;
        // swap the bytes
        number += (i & 0xff00) >> 8;
        number += (i & 0x00ff) << 8;
        return number;
    }
    
    public long readLEInt() throws IOException {
        InputStream in = this.in;
        int a = in.read();
        int b = in.read();
        int c = in.read();
        int d = in.read();
        if ((a | b | c | d) < 0)
            throw new EOFException();
        return ((d << 24) + (c << 16) + (b << 8) + (a << 0));
    }
    
    public long readLELong() throws IOException {
        InputStream in = this.in;
        int[] bytes = new int[8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = in.read();
            if(bytes[i] < 0) {
                throw new EOFException();
            }
        }

        long number = 0;
        for (int i = 0; i < bytes.length; i++) {
            number += bytes[i] << (i*8);
        }
        return number;
    }
}
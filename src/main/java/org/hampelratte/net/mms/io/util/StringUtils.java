package org.hampelratte.net.mms.io.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class StringUtils {
    public static String toHexString(byte[] bytes, int bytesPerRow) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i+=bytesPerRow) {
            int length = bytes.length - i >= bytesPerRow ? bytesPerRow : bytes.length % bytesPerRow;
            byte[] row = new byte[bytesPerRow];
            System.arraycopy(bytes, i, row, 0, length);
            
            for (int j = 0; j < length; j++) {
                sb.append('0'); sb.append('x');
                sb.append(toHexString(row[j]));
                sb.append(' ');
            }
            
            for (int j = 0; j < bytesPerRow - length; j++) {
                sb.append("     ");
            }
            
            for (int j = 0; j < Math.min(length, bytesPerRow); j++) {
                if(row[j] >= 32 && row[j] <= 127) {
                    sb.append((char)row[j]);
                } else {
                    sb.append('.');
                }
            }
            
            if(i+bytesPerRow < bytes.length) {
                sb.append('\n'); sb.append(' '); sb.append(' ');
            }
        }
        return sb.toString();
    }
    
    /**
     * Converts one byte to its hex representation with leading zeros.
     * E.g. 255 -> FF
     * @param b
     * @return
     */
    public static String toHexString(int b) {
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }
    
    public static String toCharString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            char c = (char)(bytes[i] & 0xFF);
            sb.append(c);
            sb.append(' ');
            if(i % 8 == 7) {
                sb.append('\n'); sb.append(' '); sb.append(' ');
            }
        }
        return sb.toString();
    }
    
    public static CharsetEncoder getEncoder(String charset) {
        return Charset.forName(charset).newEncoder();
    }
    
    public static CharsetDecoder getDecoder(String charset) {
        return Charset.forName(charset).newDecoder();
    }
}

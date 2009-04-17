package org.hampelratte.net.mms.io;

import java.io.IOException;

import org.hampelratte.net.mms.io.util.StringUtils;

public class UnknownHeaderException extends IOException {
    
    private byte[] bytes;
    
    public UnknownHeaderException(byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public String toString() {
        if(bytes == null) {
            return super.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Found unknown header:");
            sb.append(StringUtils.toHexString(bytes, 8));
            return sb.toString();
        }
    }
}

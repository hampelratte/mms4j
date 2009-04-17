package org.hampelratte.net.mina.nio;

import org.apache.mina.core.buffer.IoBuffer;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferDebug {
    private static transient Logger logger = LoggerFactory.getLogger(BufferDebug.class);
    
    public static int TRUNCATE_AT = 250;
    
    public static void printBufferContents(IoBuffer b) {
        int position = b.position();
        int size = Math.min(b.remaining(), TRUNCATE_AT);
        byte[] buffer = new byte[size];
        b.get(buffer);
        b.position(position);
        logger.trace("Buffer contents ({} bytes truncated to {} bytes):\n  {}", new Object[] {b.remaining(), TRUNCATE_AT, StringUtils.toHexString(buffer, 8)});
    }
}

package org.hampelratte.net.mms.io;

import java.io.FileOutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class RawInputStreamDumpFilter extends IoFilterAdapter {

    private FileOutputStream fos;
    
    @Override
    public void filterClose(NextFilter nextfilter, IoSession iosession) throws Exception {
        fos.close();
        nextfilter.filterClose(iosession);
    }

    @Override
    public void messageReceived(NextFilter nextfilter, IoSession iosession, Object obj) throws Exception {
        if(fos == null) {
            fos = new FileOutputStream("/tmp/mms.raw.stream");
        }
        
        if(obj instanceof IoBuffer) {
            IoBuffer b = (IoBuffer) obj;
            fos.write(b.array(), b.position(), b.limit());
        }
        nextfilter.messageReceived(iosession, obj);
    }
}

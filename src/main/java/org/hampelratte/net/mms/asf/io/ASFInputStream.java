package org.hampelratte.net.mms.asf.io;

import java.io.IOException;
import java.io.InputStream;

import org.hampelratte.net.mms.asf.ASFObjectFactory;
import org.hampelratte.net.mms.asf.UnknownAsfObjectException;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.io.LittleEndianEnabledInputStream;

import unclealex.mms.GUID;

public class ASFInputStream extends LittleEndianEnabledInputStream {

    public ASFInputStream(InputStream in) {
        super(in);
    }

    public ASFObject readASFObject() throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        // read the guid
        GUID guid = readGUID();
        
        // create the ASFObject
        ASFObject asfo = ASFObjectFactory.createObjectFromGUID(guid);

        // read the size
        asfo.setSize(readLELong());
        
        // read the data
        byte[] data = readData((int)asfo.getSize() - 24);
        asfo.setData(data);
        
        return asfo;
    }
    
    public GUID readGUID() throws IOException {
        byte[] guidBytes = new byte[16];
        for (int i = 0; i<guidBytes.length; i++) {
            guidBytes[i] = (byte) in.read();
        }
        return new GUID(guidBytes);
    }
    
    private byte[] readData(int count) throws IOException {
        byte[] data = new byte[count];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) in.read();
        }
        return data;
    }
    
    /**
     * Reads a NULL terminated UTF-16LE string
     * @param size size in bytes including the NULL byte
     * @return
     * @throws IOException
     */
    public String readUTF16LE(int size) throws IOException {
        if(size <= 0) {
            return "";
        }
        
        byte[] bytes = readData(size);
        return new String(bytes, 0, size-2, "UTF-16LE");
    }
}

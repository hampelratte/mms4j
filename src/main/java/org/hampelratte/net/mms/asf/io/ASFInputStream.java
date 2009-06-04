package org.hampelratte.net.mms.asf.io;

import java.io.IOException;
import java.io.InputStream;

import org.hampelratte.net.mms.asf.ASFObjectFactory;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.io.LittleEndianEnabledInputStream;

import unclealex.mms.GUID;

public class ASFInputStream extends LittleEndianEnabledInputStream {

    public ASFInputStream(InputStream in) {
        super(in);
    }

    public ASFObject readASFObject() throws IOException, InstantiationException, IllegalAccessException {
        // read the guid
        GUID guid = readGUID();
        
        // create the ASFObject
        ASFObject asfo = ASFObjectFactory.createObjectFromGUID(guid);

        // read the size
        asfo.setSize(readLELong());
        
        // read the data
        byte[] data;
        if(asfo.getSize() > 24) {
            data = readData((int)asfo.getSize() - 24); // -24 = 16(GUID) + 8(Size)
        } else {
            data = new byte[0];
        }
        asfo.setData(data);
        
        return asfo;
    }
    
    public GUID readGUID() throws IOException {
        byte[] guidBytes = readData(16);
        return new GUID(guidBytes);
    }
    
    private byte[] readData(int count) throws IOException {
        byte[] data = new byte[count];
        in.read(data);
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

package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unclealex.mms.GUID;

// TODO parse as described in chapter 4 of the asf spec
public class ASFHeaderExtensionObject extends ASFHeaderObject {

    private static transient Logger logger = LoggerFactory.getLogger(ASFHeaderExtensionObject.class);
    
    private GUID reserved1;

    private int reserved2;

    private long dataSize;
    
    private List<ASFObject> nestedHeaders = new ArrayList<ASFObject>();

    @Override
    public void setData(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        reserved1 = asfin.readGUID();
        reserved2 = asfin.readLEShort();

        dataSize = asfin.readLEInt();
        byte[] d = new byte[(int) dataSize];
        asfin.read(d);
        super.setData(d);
        
        bin = new ByteArrayInputStream(d);
        asfin = new ASFInputStream(bin);
        
        try {
            ASFObject asfo;
            long remaining = dataSize;
            while( remaining > 0 && (asfo = asfin.readASFObject()) != null ) {
                nestedHeaders.add(asfo);
                remaining -= asfo.getSize() + 24;
            }
        } catch(Exception e) {
            logger.warn("Couldn't read nested ASF Header Extension Object", e);
        }
    }

    public GUID getReserved1() {
        return reserved1;
    }

    public void setReserved1(GUID reserved1) {
        this.reserved1 = reserved1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    @Override
    public String toString() {
        String s = super.toString() + " [Reserved GUID:" + getReserved1() + ",Reserved Short:" + getReserved2() + ",Data Size:" + getDataSize();
        for (ASFObject asfo : nestedHeaders) {
            s += "\n    nested Header Extension Object: " + asfo.toString();
        }
        
        return s;
    }
}

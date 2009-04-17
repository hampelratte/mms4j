package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.hampelratte.net.mms.asf.UnknownAsfObjectException;
import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ASFToplevelHeader extends ASFObject {

    private static transient Logger logger = LoggerFactory.getLogger(ASFToplevelHeader.class);
    
    private ASFHeaderObject[] nestedHeaders;
    
    private long objectCount;
    
    @Override
    public void setData(byte[] data) throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        super.setData(data);
        
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);
        
        // read the header object count
        objectCount = asfin.readLEInt();
        
        // read 2 reserved bytes
        asfin.read();
        asfin.read();
        
        // read the nested header objects
        nestedHeaders = new ASFHeaderObject[(int) objectCount];
        for (int i = 0; i < nestedHeaders.length; i++) {
            try {
                nestedHeaders[i] = (ASFHeaderObject) asfin.readASFObject();
                System.err.println(nestedHeaders[i]);
            } catch (UnknownAsfObjectException e) {
                logger.warn("Found a nested unknown ASF object", e);
            }
        }
    }

    public long getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(long objectCount) {
        this.objectCount = objectCount;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" [Object count:");
        sb.append(getObjectCount());
        sb.append(']');
        
        for (int i = 0; i < nestedHeaders.length; i++) {
            if(nestedHeaders[i] != null) {
                sb.append("\n  nested Header: ");
                sb.append(nestedHeaders[i].toString());
            } else {
                sb.append("\n  unknown nested Header");
            }
        }
        
        return sb.toString();
    }
    
    public ASFHeaderObject getNestedHeader(Class<?> headerClass) {
        for (int i = 0; i < nestedHeaders.length; i++) {
            if(nestedHeaders[i] != null && nestedHeaders[i].getClass() == headerClass) {
                return nestedHeaders[i];
            }
        }
        
        return null;
    }
}
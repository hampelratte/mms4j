package org.hampelratte.net.mms.asf.objects;

import java.io.IOException;

import org.hampelratte.net.mms.asf.UnknownAsfObjectException;

import unclealex.mms.GUID;

public abstract class ASFObject {
    private GUID guid;

    /** The size of the ASF object. 
     *  Should be unsigned and thus an BigInteger, 
     *  but i doubt that any object will be so large,
     *  so long will be ok and much more comfortable.
     */
    private long size;

    private byte[] data;

    public GUID getGuid() {
        return guid;
    }

    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        this.data = data;
    }
    
    public String getName() {
        return getClass().getSimpleName();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append(" [Size:");
        sb.append(getSize());
        sb.append(",GUID:");
        sb.append(getGuid());
        sb.append(']');
        return sb.toString();
    }
}

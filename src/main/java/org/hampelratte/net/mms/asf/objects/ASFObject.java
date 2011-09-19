package org.hampelratte.net.mms.asf.objects;

import java.io.IOException;

import unclealex.mms.GUID;

public abstract class ASFObject {
    private GUID guid;

    /**
     * The size of the ASF object. Should be unsigned and thus an BigInteger, but i doubt that any object will be so large, so long will be ok and much more
     * comfortable.
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

    public void setData(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
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

    public byte[] toByteArray() throws IOException {
        byte[] b = new byte[(int) (24 + getSize())];
        System.arraycopy(getGuid().toByteArray(), 0, b, 0, 16);
        b[23] = (byte) ((getSize() >> 56) & 0xFF);
        b[22] = (byte) ((getSize() >> 48) & 0xFF);
        b[21] = (byte) ((getSize() >> 40) & 0xFF);
        b[20] = (byte) ((getSize() >> 32) & 0xFF);
        b[19] = (byte) ((getSize() >> 24) & 0xFF);
        b[18] = (byte) ((getSize() >> 16) & 0xFF);
        b[17] = (byte) ((getSize() >> 8) & 0xFF);
        b[16] = (byte) (getSize() & 0xFF);
        System.arraycopy(getData(), 0, b, 24, getData().length);
        return b;
    }
}

package org.hampelratte.net.mms.data;

import org.hampelratte.net.mms.MMSObject;

public abstract class MMSPacket extends MMSObject {

    protected long sequence;

    protected byte flags;

    protected long length;
    
    protected byte[] data;
    
    public MMSPacket() {}
    
    public MMSPacket(long sequence, byte flags, int length) {
        this.sequence = sequence;
        this.flags = flags;
        this.length = length;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    public abstract String getPacketName();
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPacketName());
        sb.append(" length[");
        sb.append(data.length);
        sb.append("]");
        //sb.append("\n  ");
        //sb.append(StringUtils.toHexString(data));
        return sb.toString();
    }

    @Override
    public long getMessageLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
    
    @Override
    public long getByteLength() {
        return getMessageLength();
    }
}

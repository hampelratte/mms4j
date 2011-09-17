package org.hampelratte.net.mms.http;

public class FramingHeader {
    private boolean b = false;

    private int frame;

    private int packetId;

    private int packetLength;

    private int reason;

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(short frame) {
        this.frame = frame;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return (char) getPacketId() + " Length:" + getPacketLength() + " B:" + isB() + " Frame:0x" + Integer.toHexString(frame);
    }
}

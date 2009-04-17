package org.hampelratte.net.mms.data;


public class MMSMediaPacket extends MMSPacket {

    public static final int PACKET_ID = 0x04;
    
    public MMSMediaPacket(long sequence, byte flags, int length) {
        super(sequence, flags, length);
    }

    @Override
    public String getPacketName() {
        return "MMS media packet";
    }

    public void addPadding(int padding) {
        byte[] d = new byte[data.length + padding];
        System.arraycopy(data, 0, d, 0, data.length);
        this.data = d;
    }
}

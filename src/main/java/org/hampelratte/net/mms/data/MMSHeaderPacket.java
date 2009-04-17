package org.hampelratte.net.mms.data;


public class MMSHeaderPacket extends MMSPacket {

    public static final int PACKET_ID = 0x02;
    
    public MMSHeaderPacket(long sequence, byte flags, int length) {
        super(sequence, flags, length);
    }

    @Override
    public String getPacketName() {
        return "MMS header packet";
    }
}

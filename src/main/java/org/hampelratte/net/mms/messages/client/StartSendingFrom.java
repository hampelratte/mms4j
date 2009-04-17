package org.hampelratte.net.mms.messages.client;


public class StartSendingFrom extends MMSRequest {
    
    public static final long FIRST_PACKET = 0xFFFFFFFFL; 
    
    private long seekTime;
    
    private long startPacket = FIRST_PACKET;
    
    public StartSendingFrom() {
        super();
        setMID(0x00030007);
    }
    
    @Override
    public String getMessageName() {
        return "Start sending from packet";
    }

    @Override
    public long getBodyLength() {
        return 32 + 8; // NOTE 8 bytes come from two ints, which are not part of the spec, see the note in StartSendingFromEncoder
    }
    
    public long getSeekTime() {
        return seekTime;
    }

    public void setSeekTime(long seekTime) {
        this.seekTime = seekTime;
    }

    public long getStartPacket() {
        return startPacket;
    }

    public void setStartPacket(long startPacket) {
        this.startPacket = startPacket;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------\n  Seek time in sec: ");
        sb.append(seekTime);
        sb.append("\n  Start packet: ");
        sb.append(startPacket);
        return sb.toString();
    }
}

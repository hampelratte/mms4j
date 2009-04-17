package org.hampelratte.net.mms.messages.client;

/**
 * The Pong message is sent by the client to inform the server
 * that it is still active. It sends this message in response to a
 * Ping (section 2.2.4.1) message from the server.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class Pong extends MMSRequest {

    private int dwParam1 = 0;
    
    private int dwParam2 = 0;
    
    /**
     * The Pong message is sent by the client to inform the
     * server that it is still active. It sends this message in response to a
     * Ping (section 2.2.4.1) message from the server.
     */
    public Pong() {
        super();
        setMID(0x0003001B);
    }
    
    @Override
    public String getMessageName() {
        return "Pong";
    }

    @Override
    public long getBodyLength() {
        return 16;
    }

    /**
     * @return dwParam1 (4 bytes): A 32-bit field. It SHOULD be set to
     *         0x00000000 and MUST be ignored by the receiver.
     */
    public int getDwParam1() {
        return dwParam1;
    }

    /**
     * @param dwParam1
     *            (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000 and
     *            MUST be ignored by the receiver.
     */
    public void setDwParam1(int dwParam1) {
        this.dwParam1 = dwParam1;
    }

    /**
     * @return dwParam2 (4 bytes): A 32-bit field. It SHOULD be set to
     *         0x00000000 and MUST be ignored by the receiver.
     */
    public int getDwParam2() {
        return dwParam2;
    }

    /**
     * @param dwParam2
     *            (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000 and
     *            MUST be ignored by the receiver.
     */
    public void setDwParam2(int dwParam2) {
        this.dwParam2 = dwParam2;
    }
}

package org.hampelratte.net.mms.messages.server;

/**
 * The Ping message is sent by the server to verify that a
 * particular client is still active.
 * 
 * @author <a
 *         href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios
 *         .de</a>
 */
public class Ping extends MMSResponse {

    private int dwParam1 = 0;

    private int dwParam2 = 0;

    /**
     * @return dwParam1 (4 bytes): A 32-bit field. SHOULD be set to 0x00000000
     *         and MUST be ignored by the receiver.
     */
    public int getDwParam1() {
        return dwParam1;
    }

    /**
     * @param dwParam1
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000 and
     *            MUST be ignored by the receiver.
     */
    public void setDwParam1(int dwParam1) {
        this.dwParam1 = dwParam1;
    }

    /**
     * @return dwParam2 (4 bytes): A 32-bit field. SHOULD be set to 0x00000000
     *         and MUST be ignored by the receiver.
     */
    public int getDwParam2() {
        return dwParam2;
    }

    /**
     * @param dwParam2
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000 and
     *            MUST be ignored by the receiver.
     */
    public void setDwParam2(int dwParam2) {
        this.dwParam2 = dwParam2;
    }

    @Override
    public String getMessageName() {
        return "Ping";
    }

    @Override
    public long getBodyLength() {
        return 16;
    }
}

package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.client.ConnectFunnel;

/**
 * The ReportConnectedFunnel message is sent by the server in response to a {@link ConnectFunnel} (section 2.2.4.18) message sent by the client.
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportConnectedFunnel extends MMSResponse {

    private int hr = 0;

    private int playIncarnation = 0;

    private int packetPayloadSize = 0;

    private String funnelName;

    /** @return A 32-bit field. MUST be set to 0x00000000. */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            A 32-bit field. MUST be set to 0x00000000.
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It MUST be ignored by the receiver.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It MUST be ignored by the receiver.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return packetPayloadSize (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It MUST be ignored by the receiver.
     */
    public int getPacketPayloadSize() {
        return packetPayloadSize;
    }

    /**
     * @param packetPayloadSize
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It MUST be ignored by the receiver.
     */
    public void setPacketPayloadSize(int packetPayloadSize) {
        this.packetPayloadSize = packetPayloadSize;
    }

    /**
     * @return funnelName (variable): A variable size array of Unicode characters. MUST be set to the Unicode character string "Funnel Of The Gods".
     */
    public String getFunnelName() {
        return funnelName;
    }

    /**
     * @param funnelName
     *            (variable): A variable size array of Unicode characters. MUST be set to the Unicode character string "Funnel Of The Gods".
     */
    public void setFunnelName(String funnelName) {
        this.funnelName = funnelName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  HRESULT: ").append(HRESULT.hrToHumanReadable(getHr()));
        sb.append("\n  funnelName: ");
        sb.append(funnelName);
        return sb.toString();
    }

    @Override
    public String getMessageName() {
        return "Report Connected Funnel";
    }

    @Override
    public long getBodyLength() {
        return 20 + funnelName.length() * 2;
    }
}

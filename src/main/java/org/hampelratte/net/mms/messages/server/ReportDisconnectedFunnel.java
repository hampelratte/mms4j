package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.HRESULT;

/**
 * The ReportDisconnectedFunnel message is sent by the server in
 * response to a ConnectFunnel (section 2.2.4.18) message from
 * the client when there is an error processing the request.
 * 
 * @author <a
 *         href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios
 *         .de</a>
 */
public class ReportDisconnectedFunnel extends MMSResponse {

    private int hr;

    private int playIncarnation;

    /**
     * @return hr (4 bytes): Result of processing the
     *         ConnectFunnel (section 2.2.4.18) message. For
     *         HRESULT codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): Result of processing the
     *            ConnectFunnel (section 2.2.4.18) message. For
     *            HRESULT codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. SHOULD be set to
     *         0x00000000. It MUST be ignored by the receiver.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It
     *            MUST be ignored by the receiver.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    @Override
    public String getMessageName() {
        return "Report Disconnected Funnel";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\n  --------");
        sb.append("\n  HRESULT: ").append(HRESULT.hrToHumanReadable(getHr()));
        return sb.toString();
    }

    @Override
    public long getBodyLength() {
        return 16;
    }
}

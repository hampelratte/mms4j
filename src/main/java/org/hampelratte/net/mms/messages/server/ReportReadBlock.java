package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.client.ReadBlock;

/**
 * The ReportReadBlock message is sent by the server in response
 * to a {@link ReadBlock} (section 2.2.4.23) message from the
 * client.
 * 
 * @author <a
 *         href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios
 *         .de</a>
 */
public class ReportReadBlock extends MMSResponse {

    private int hr;

    private int playIncarnation;

    private int playSequence;

    /**
     * @return hr (4 bytes): The result of processing the client
     *         ReadBlock (section 2.2.4.23) request. For HRESULT
     *         codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): The result of processing the client
     *            ReadBlock (section 2.2.4.23) request. For
     *            HRESULT codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. This field MUST be set
     *         to the value of the playIncarnation field in the
     *         ReadBlock (section 2.2.4.23) message that this
     *         ReportReadBlock message is a response to. A value
     *         that specifies the type of packet-pair bandwidth estimation that
     *         should be used, if any.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. This field MUST be set to the value
     *            of the playIncarnation field in the ReadBlock
     *            (section 2.2.4.23) message that this
     *            ReportReadBlock message is a response to. A
     *            value that specifies the type of packet-pair bandwidth
     *            estimation that should be used, if any.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return playSequence (4 bytes): A 32-bit field. This field MUST be set to
     *         0x00000000.
     */
    public int getPlaySequence() {
        return playSequence;
    }

    /**
     * @param playSequence
     *            (4 bytes): A 32-bit field. This field MUST be set to
     *            0x00000000.
     */
    public void setPlaySequence(int playSequence) {
        this.playSequence = playSequence;
    }

    @Override
    public String getMessageName() {
        return "Report Read Block";
    }

    @Override
    public long getBodyLength() {
        return 20;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  HRESULT: ");
        sb.append(HRESULT.hrToHumanReadable(getHr()));
        return sb.toString();
    }
}

package org.hampelratte.net.mms.messages.server;


/**
 * The ReportStreamSwitch message is used by the server to
 * respond to a {@link StreamSwitch} (section 2.2.4.28) message
 * from the client.
 * 
 * @author <a
 *         href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios
 *         .de</a>
 */
public class ReportStreamSwitch extends MMSResponse {

    private int hr;

    /**
     * @return hr (4 bytes): HRESULT. Result of processing the client
     *         StreamSwitch (section 2.2.4.28) message. For
     *         HRESULT codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr (4 bytes): HRESULT. Result of processing the client
     *         StreamSwitch (section 2.2.4.28) message. For
     *         HRESULT codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    @Override
    public String getMessageName() {
        return "Report Stream Switch";
    }

    @Override
    public long getBodyLength() {
        return 12;
    }
}

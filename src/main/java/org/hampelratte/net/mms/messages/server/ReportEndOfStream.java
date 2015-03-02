package org.hampelratte.net.mms.messages.server;

/**
 * The ReportEndOfStream message is sent by the server to notify the client that the end of the current playlist entry has been reached.
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportEndOfStream extends MMSResponse {

    private int hr;

    private int playIncarnation;

    /**
     * @return hr (4 bytes): HRESULT. The following HRESULT codes have a special meaning (any other HRESULT code has the meaning specified in [MS-ERREF]):
     *         <ul>
     *         <li>
     *         0x00000000 The end of the current playlist entry is reached, or the server successfully processed the LinkViewerToMacStopPlaying (section
     *         2.2.4.27) message.</li>
     *         <li>0x00000001 The end of the current playlist entry is reached, and the server sends a LinkMacToViewerReportStreamChange (section 2.2.4.12)
     *         message.</li>
     *         </ul>
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): HRESULT. The following HRESULT codes have a special meaning (any other HRESULT code has the meaning specified in [MS-ERREF]):
     *            <ul>
     *            <li>
     *            0x00000000 The end of the current playlist entry is reached, or the server successfully processed the LinkViewerToMacStopPlaying (section
     *            2.2.4.27) message.</li>
     *            <li>0x00000001 The end of the current playlist entry is reached, and the server sends a LinkMacToViewerReportStreamChange (section 2.2.4.12)
     *            message.</li>
     *            </ul>
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. If this ReportEndOfStream message is sent in response to a LinkViewerToMacStopPlaying (section
     *         2.2.4.27) message, the playIncarnation field MUST be set to the value of the playIncarnation field in the LinkViewerToMacStopPlaying (section
     *         2.2.4.27) message. Otherwise, the playIncarnation field MUST be set to the value of the playIncarnation field in the most recently received
     *         message of the following message types: LinkViewerToMacStartPlaying and LinkViewerToMacStartStriding.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. If this ReportEndOfStream message is sent in response to a LinkViewerToMacStopPlaying (section 2.2.4.27) message,
     *            the playIncarnation field MUST be set to the value of the playIncarnation field in the LinkViewerToMacStopPlaying (section 2.2.4.27) message.
     *            Otherwise, the playIncarnation field MUST be set to the value of the playIncarnation field in the most recently received message of the
     *            following message types: LinkViewerToMacStartPlaying and LinkViewerToMacStartStriding.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    @Override
    public String getMessageName() {
        return "Report End of Stream";
    }

    @Override
    public long getBodyLength() {
        return 16;
    }
}

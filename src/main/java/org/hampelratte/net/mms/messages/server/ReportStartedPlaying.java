package org.hampelratte.net.mms.messages.server;

/**
 * The ReportStartedPlaying message is sent by the server in
 * response to a LinkViewerToMacStartPlaying (section 2.2.4.25) message.
 * 
 * @author <a
 *         href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios
 *         .de</a>
 */
public class ReportStartedPlaying extends MMSResponse {

    private int hr;

    private int playIncarnation;

    private int tigerFileId;

    /**
     * @return hr (4 bytes): HRESULT. The result of processing the client
     *         LinkViewerToMacStartPlaying (section 2.2.4.25) message. For
     *         HRESULT codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): HRESULT. The result of processing the client
     *            LinkViewerToMacStartPlaying (section 2.2.4.25) message. For
     *            HRESULT codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. It MUST be the value
     *         of the playIncarnation field in the LinkViewerToMacStartPlaying
     *         (section 2.2.4.25) message that this
     *         ReportStartedPlaying message is a response to.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. It MUST be the value of the
     *            playIncarnation field in the LinkViewerToMacStartPlaying
     *            (section 2.2.4.25) message that this
     *            ReportStartedPlaying message is a response to.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return tigerFileId (4 bytes): A 32-bit field. This field SHOULD <11> be
     *         set to the value of the openFileId field in the
     *         ReportOpenFile (section 2.2.4.7) message sent
     *         previously by the server. The field MUST be ignored by the
     *         receiver.
     */
    public int getTigerFileId() {
        return tigerFileId;
    }

    /**
     * @param tigerFileId
     *            (4 bytes): A 32-bit field. This field SHOULD <11> be set to
     *            the value of the openFileId field in the
     *            ReportOpenFile (section 2.2.4.7) message sent
     *            previously by the server. The field MUST be ignored by the
     *            receiver.
     */
    public void setTigerFileId(int tigerFileId) {
        this.tigerFileId = tigerFileId;
    }

    @Override
    public String getMessageName() {
        return "Report Started Playing";
    }

    @Override
    public long getBodyLength() {
        return 36;
    }
}
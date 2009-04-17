package org.hampelratte.net.mms.messages.client;

/**
 * The ReadBlock message is sent by the client to request the ASF
 * file header. The ASF file header contains all of the codec initialization
 * parameters needed by the client to decompress the multimedia data.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReadBlock extends MMSRequest {

    private int openFileId;
    
    private int fileBlockId = 0;
    
    private int offset = 0;
    
    private int length = 0x00008000;
    
    private int flags = 0xFFFFFFFF;
    
    private int padding = 0;
    
    private double tEarliest = 0;
    
    private double tDeadline = 3600;
    
    private int playIncarnation = 2;
    
    private int playSequence = 0;
    
    /**
     * The ReadBlock message is sent by the client to request the ASF
     * file header. The ASF file header contains all of the codec initialization
     * parameters needed by the client to decompress the multimedia data.
     */
    public ReadBlock() {
        super();
        setMID(0x00030015);
    }
    
    @Override
    public String getMessageName() {
        return "Read Block";
    }

    @Override
    public long getBodyLength() {
        return 56;
    }

    /**
     * @return openFileId (4 bytes): A 32-bit field. This field MUST be the
     *         value of the openFileId field in the
     *         ReportOpenFile (section 2.2.4.7) message that the
     *         client previously received from the server.
     */
    public int getOpenFileId() {
        return openFileId;
    }

    /**
     * @param openFileId (4 bytes): A 32-bit field. This field MUST be the
     *         value of the openFileId field in the
     *         ReportOpenFile (section 2.2.4.7) message that the
     *         client previously received from the server.
     */
    public void setOpenFileId(int openFileId) {
        // TODO set this value during negotiation
        this.openFileId = openFileId;
    }

    /**
     * @return fileBlockId (4 bytes): A 32-bit field. It MUST be set to
     *         0x00000000.
     */
    public int getFileBlockId() {
        return fileBlockId;
    }

    /**
     * @param fileBlockId
     *            (4 bytes): A 32-bit field. It MUST be set to 0x00000000.
     */
    public void setFileBlockId(int fileBlockId) {
        this.fileBlockId = fileBlockId;
    }

    /** @return offset (4 bytes): A 32-bit field. It MUST be set to 0x00000000. */
    public int getOffset() {
        return offset;
    }

    /** @param offset (4 bytes): A 32-bit field. It MUST be set to 0x00000000. */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /** @return length (4 bytes): A 32-bit field. It SHOULD be set to 0x00008000. */
    public int getLength() {
        return length;
    }

    /** @param length (4 bytes): A 32-bit field. It SHOULD be set to 0x00008000. */
    public void setLength(int length) {
        this.length = length;
    }

    /** @return flags (4 bytes): A 32-bit field. It MUST be set to 0xFFFFFFFF. */
    public int getFlags() {
        return flags;
    }

    /** @param flags (4 bytes): A 32-bit field. It MUST be set to 0xFFFFFFFF. */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @return padding (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000
     *         and MUST be ignored by the receiver.
     */
    public int getPadding() {
        return padding;
    }

    /**
     * @param padding (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000
     *         and MUST be ignored by the receiver.
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * @return tEarliest (8 bytes): A DOUBLE data type field. This field MUST be
     *         set to the value 0 expressed as a double precision floating point
     *         number.
     */
    public double getTEarliest() {
        return tEarliest;
    }

    /**
     * @param earliest (8 bytes): A DOUBLE data type field. This field MUST be
     *         set to the value 0 expressed as a double precision floating point
     *         number.
     */
    public void setTEarliest(double earliest) {
        tEarliest = earliest;
    }

    /**
     * @return tDeadline (8 bytes): A DOUBLE data type field. It MUST be set to
     *         the value 3600 expressed as a double precision floating-point
     *         number.
     */
    public double getTDeadline() {
        return tDeadline;
    }

    /**
     * @param deadline
     *            (8 bytes): A DOUBLE data type field. It MUST be set to the
     *            value 3600 expressed as a double precision floating-point
     *            number.
     */
    public void setTDeadline(double deadline) {
        tDeadline = deadline;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. It MUST be set to a
     *         value in the range 0x00000001 to 0x000000FE, inclusive.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. It MUST be set to a value in the
     *            range 0x00000001 to 0x000000FE, inclusive.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return playSequence (4 bytes): A 32-bit field. It MUST be set to
     *         0x00000000.
     */
    public int getPlaySequence() {
        return playSequence;
    }

    /**
     * @param playSequence (4 bytes): A 32-bit field. It MUST be set to
     *         0x00000000.
     */
    public void setPlaySequence(int playSequence) {
        this.playSequence = playSequence;
    }
    
    
}

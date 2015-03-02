package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.client.OpenFile;


/**
 * The ReportOpenFile message is sent by the server in response
 * to a {@link OpenFile} (section 2.2.4.21) message by the client.
 * 
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportOpenFile extends MMSResponse {

    /** Specifies that the server supports fast forward and rewinding of content. */
    public static final int FILE_ATTRIBUTE_MMS_CANSTRIDE = 0x00800000;
    
    /** Specifies that the server supports seeking in content. */
    public static final int FILE_ATTRIBUTE_MMS_CANSEEK = 0x01000000;
    
    /**
     * Specifies that the content is a broadcast stream (the same stream may be
     * shared by multiple clients).
     */
    public static final int FILE_ATTRIBUTE_MMS_BROADCAST = 0x02000000;
    
    /** Specifies that the content is a live stream. */
    public static final int FILE_ATTRIBUTE_MMS_LIVE = 0x04000000;
    
    /**
     * Specifies that the content is part of a multiple item server-side
     * playlist.
     */
    public static final int FILE_ATTRIBUTE_MMS_PLAYLIST = 0x40000000;
    
    private int hr;
    
    private int playIncarnation;
    
    private int openFileId;
    
    private int padding = 0;
    
    private int fileName = 0;
    
    private int fileAttributes;
    
    private double fileDuration;
    
    private long fileBlocks;
    
    private long filePacketSize;
    
    private long filePacketCount;
    
    private long fileBitrate;
    
    private long fileHeaderSize;
    
    /**
     * @return hr (4 bytes): HRESULT. Result of processing the client
     *         OpenFile (section 2.2.4.21) request. For HRESULT
     *         codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): HRESULT. Result of processing the client
     *            OpenFile (section 2.2.4.21) request. For
     *            HRESULT codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. It MUST be set to the
     *         value of the playIncarnation field in the message received from
     *         the client that triggered the transmission of this
     *         ReportOpenFile message. The message is either a
     *         OpenFile (section 2.2.4.21) message or a
     *         LinkViewerToMacSecurityResponse (section 2.2.4.24) message.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. It MUST be set to the value of the
     *            playIncarnation field in the message received from the client
     *            that triggered the transmission of this
     *            ReportOpenFile message. The message is either a
     *            OpenFile (section 2.2.4.21) message or a
     *            LinkViewerToMacSecurityResponse (section 2.2.4.24) message.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return openFileId (4 bytes): A 32-bit field. It SHOULD be set by the
     *         server in such a way that when the client echoes the value of
     *         this field in the RequestPacketListResend (section 2.2.5) packet
     *         (or in other messages that contain an openFileId field), the
     *         server can identify the file referred to in the message.
     */
    public int getOpenFileId() {
        return openFileId;
    }

    /**
     * @param openFileId
     *            (4 bytes): A 32-bit field. It SHOULD be set by the server in
     *            such a way that when the client echoes the value of this field
     *            in the RequestPacketListResend (section 2.2.5) packet (or in
     *            other messages that contain an openFileId field), the server
     *            can identify the file referred to in the message.
     */
    public void setOpenFileId(int openFileId) {
        this.openFileId = openFileId;
    }

    /**
     * @return padding (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000
     *         and MUST be ignored by the receiver.
     */
    public int getPadding() {
        return padding;
    }

    /**
     * @param padding
     *            (4 bytes): A 32-bit field. It SHOULD be set to 0x00000000 and
     *            MUST be ignored by the receiver.
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /** @return fileName (4 bytes): A 32-bit field. It MUST be set to 0x00000000. */
    public int getFileName() {
        return fileName;
    }

    /** @param fileName (4 bytes): A 32-bit field. It MUST be set to 0x00000000. */
    public void setFileName(int fileName) {
        this.fileName = fileName;
    }

    /**
     * @return fileAttributes (4 bytes): A 32-bit field. It MUST specify the
     *         attributes of the content by using a bitwise OR combination of
     *         the flags in the following table.
     * @see #FILE_ATTRIBUTE_MMS_BROADCAST
     * @see #FILE_ATTRIBUTE_MMS_CANSEEK
     * @see #FILE_ATTRIBUTE_MMS_CANSTRIDE
     * @see #FILE_ATTRIBUTE_MMS_LIVE
     * @see #FILE_ATTRIBUTE_MMS_PLAYLIST
     */
    public int getFileAttributes() {
        return fileAttributes;
    }

    /**
     * @param fileAttributes (4 bytes): A 32-bit field. It MUST specify the
     *         attributes of the content by using a bitwise OR combination of
     *         the flags in the following table.
     * @see #FILE_ATTRIBUTE_MMS_BROADCAST
     * @see #FILE_ATTRIBUTE_MMS_CANSEEK
     * @see #FILE_ATTRIBUTE_MMS_CANSTRIDE
     * @see #FILE_ATTRIBUTE_MMS_LIVE
     * @see #FILE_ATTRIBUTE_MMS_PLAYLIST
     */
    public void setFileAttributes(int fileAttributes) {
        this.fileAttributes = fileAttributes;
    }

    /**
     * @return fileDuration (8 bytes): A DOUBLE data type field. It MUST be set
     *         to the duration, in seconds, of the current playlist entry,
     *         specified as a double precision floating point number. If the
     *         duration is unknown (for example, for live content, the
     *         fileDuration field MUST be set to 0).
     */
    public double getFileDuration() {
        return fileDuration;
    }

    /**
     * @param fileDuration
     *            (8 bytes): A DOUBLE data type field. It MUST be set to the
     *            duration, in seconds, of the current playlist entry, specified
     *            as a double precision floating point number. If the duration
     *            is unknown (for example, for live content, the fileDuration
     *            field MUST be set to 0).
     */
    public void setFileDuration(double fileDuration) {
        this.fileDuration = fileDuration;
    }

    /**
     * @return fileBlocks (4 bytes): An unsigned 32-bit integer. It MUST be set
     *         to the duration, in seconds, of the current playlist entry. If
     *         the duration is not an integer number of seconds, it MUST be
     *         rounded to the nearest higher integer value.
     */
    public long getFileBlocks() {
        return fileBlocks;
    }

    /**
     * @param fileBlocks
     *            (4 bytes): An unsigned 32-bit integer. It MUST be set to the
     *            duration, in seconds, of the current playlist entry. If the
     *            duration is not an integer number of seconds, it MUST be
     *            rounded to the nearest higher integer value.
     */
    public void setFileBlocks(long fileBlocks) {
        this.fileBlocks = fileBlocks;
    }

    /**
     * @return filePacketSize (4 bytes): An unsigned 32-bit integer. It MUST be
     *         set to the maximum size, in bytes, of the ASF data packets to be
     *         transmitted by the server for this playlist entry.
     */
    public long getFilePacketSize() {
        return filePacketSize;
    }

    /**
     * @param filePacketSize (4 bytes): An unsigned 32-bit integer. It MUST be
     *         set to the maximum size, in bytes, of the ASF data packets to be
     *         transmitted by the server for this playlist entry.
     */
    public void setFilePacketSize(long filePacketSize) {
        this.filePacketSize = filePacketSize;
    }

    /**
     * @return filePacketCount (8 bytes): An unsigned 64-bit integer. It MUST be
     *         set to the number of ASF data packets in the content. If this
     *         information is unavailable, the field MUST be set to
     *         0x0000000000000000.
     */
    public long getFilePacketCount() {
        return filePacketCount;
    }

    /**
     * @param filePacketCount
     *            (8 bytes): An unsigned 64-bit integer. It MUST be set to the
     *            number of ASF data packets in the content. If this information
     *            is unavailable, the field MUST be set to 0x0000000000000000.
     */
    public void setFilePacketCount(long filePacketCount) {
        this.filePacketCount = filePacketCount;
    }

    /**
     * @return fileBitrate (4 bytes): An unsigned 32-bit integer. It MUST be the
     *         sum of the maximum bit rates (in bits per second) of all streams
     *         in the current playlist entry.
     */
    public long getFileBitrate() {
        return fileBitrate;
    }

    /**
     * @param fileBitrate
     *            (4 bytes): An unsigned 32-bit integer. It MUST be the sum of
     *            the maximum bit rates (in bits per second) of all streams in
     *            the current playlist entry.
     */
    public void setFileBitrate(long fileBitrate) {
        this.fileBitrate = fileBitrate;
    }

    /**
     * @return fileHeaderSize (4 bytes): An unsigned 32-bit integer. It MUST be
     *         set to the size, in bytes, of the ASF file header for the current
     *         playlist entry.
     */
    public long getFileHeaderSize() {
        return fileHeaderSize;
    }

    /**
     * @param fileHeaderSize
     *            (4 bytes): An unsigned 32-bit integer. It MUST be set to the
     *            size, in bytes, of the ASF file header for the current
     *            playlist entry.
     */
    public void setFileHeaderSize(long fileHeaderSize) {
        this.fileHeaderSize = fileHeaderSize;
    }

    @Override
    public long getBodyLength() {
        return 116;
    }

    @Override
    public String getMessageName() {
        return "Report Open File";
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  HRESULT: ").append(HRESULT.hrToHumanReadable(getHr()));
        sb.append("\n  Play incarnation: ").append(getPlayIncarnation());
        sb.append("\n  Open file ID: ").append(openFileId);
        sb.append("\n  Padding: ").append(getPadding());
        sb.append("\n  File name: ").append(getFileName());
        sb.append("\n  File attributes: 0x"); sb.append(Integer.toHexString(getFileAttributes()));
        sb.append("\n  File duration: ").append(getFileDuration());
        sb.append("\n  File blocks: ").append(getFileBlocks());
        sb.append("\n  File packet size: ").append(getFilePacketSize());
        sb.append("\n  File packet count: ").append(getFilePacketCount());
        sb.append("\n  File bitrate: ").append(getFileBitrate());
        sb.append("\n  File header size: ").append(getFileHeaderSize());
        return sb.toString();
    }
}

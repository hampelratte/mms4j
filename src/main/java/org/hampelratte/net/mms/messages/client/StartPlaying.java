package org.hampelratte.net.mms.messages.client;

/**
 * The StartPlaying message is sent by the client to request that the server
 * start streaming the content for playback at the normal (real-time) rate.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class StartPlaying extends MMSRequest {

    private int openFileId;
    
    private int padding = 0;
    
    private double position;
    
    private long asfOffset;
    
    private long locationId;
    
    private int frameOffset;
    
    private int playIncarnation = 4;
    
    private long dwAccelBandwidth;
    
    private long dwAccelDuration;
    
    private long dwLinkBandwidth;

    public static final long FIRST_PACKET = 0xFFFFFFFFL;

    /**
     * The StartPlaying message is sent by the client to request that the server
     * start streaming the content for playback at the normal (real-time) rate.
     */
    public StartPlaying() {
        super();
        setMID(0x00030007);
    }

    /**
     * @return openFileId (4 bytes): A 32-bit field. This field MUST be the
     *         value of the openFileId field in the
     *         LinkMacToViewerReportOpenFile (section 2.2.4.7) message that the
     *         client previously received from the server.
     */
    public int getOpenFileId() {
        return openFileId;
    }

    /**
     * @param openFileId
     *            (4 bytes): A 32-bit field. This field MUST be the value of the
     *            openFileId field in the LinkMacToViewerReportOpenFile (section
     *            2.2.4.7) message that the client previously received from the
     *            server.
     */
    public void setOpenFileId(int openFileId) {
        this.openFileId = openFileId;
    }

    /**
     * @return padding (4 bytes): A 32-bit field. This field SHOULD be set to
     *         0x00000000 and MUST be ignored by the receiver.
     */
    public int getPadding() {
        return padding;
    }
    
    /**
     * @param padding (4 bytes): A 32-bit field. This field SHOULD be set to
     *         0x00000000 and MUST be ignored by the receiver.
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }
    
    /**
     * @return position (8 bytes): A DOUBLE data type field. This field MUST be
     *         set to either the time offset in seconds from which the server is
     *         requested to start streaming the content, or to the maximum
     *         positive number for data type DOUBLE (DBL_MAX). The content is
     *         assumed to begin at time 0. For example, setting this field to 0
     *         requests the server to stream the content from the beginning. If
     *         this field is set to the maximum positive number for data type
     *         DOUBLE, either the asfOffset field or the locationId field MUST
     *         be used to specify the starting point for playback.
     */
    public double getPosition() {
        return position;
    }

    /**
     * @param position
     *            (8 bytes): A DOUBLE data type field. This field MUST be set to
     *            either the time offset in seconds from which the server is
     *            requested to start streaming the content, or to the maximum
     *            positive number for data type DOUBLE (DBL_MAX). The content is
     *            assumed to begin at time 0. For example, setting this field to
     *            0 requests the server to stream the content from the
     *            beginning. If this field is set to the maximum positive number
     *            for data type DOUBLE, either the asfOffset field or the
     *            locationId field MUST be used to specify the starting point
     *            for playback.
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * @return asfOffset (4 bytes): A 32-bit unsigned integer. This field MUST
     *         be set to the offset, in bytes, counted from the beginning of the
     *         ASF file from which the server is requested to start streaming,
     *         or set to either 0xFFFFFFFF or 0x00000000. If this field is set
     *         to 0xFFFFFFFF or 0x00000000, then either the position or
     *         locationId field MUST be used to specify the starting point for
     *         playback.
     */
    public long getAsfOffset() {
        return asfOffset;
    }

    /**
     * @param asfOffset
     *            (4 bytes): A 32-bit unsigned integer. This field MUST be set
     *            to the offset, in bytes, counted from the beginning of the ASF
     *            file from which the server is requested to start streaming, or
     *            set to either 0xFFFFFFFF or 0x00000000. If this field is set
     *            to 0xFFFFFFFF or 0x00000000, then either the position or
     *            locationId field MUST be used to specify the starting point
     *            for playback.
     */
    public void setAsfOffset(long asfOffset) {
        this.asfOffset = asfOffset;
    }

    /**
     * @return locationId (4 bytes): A 32-bit unsigned integer. This field MUST
     *         be set to the ASF data packet number from which the server is
     *         requested to start streaming the content, or set to either
     *         0xFFFFFFFF or 0x00000000. If this field is set to 0xFFFFFFFF or
     *         0x00000000, then either the position field or the asfOffset field
     *         MUST be used to specify the starting point for playback.
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     *            (4 bytes): A 32-bit unsigned integer. This field MUST be set
     *            to the ASF data packet number from which the server is
     *            requested to start streaming the content, or set to either
     *            0xFFFFFFFF or 0x00000000. If this field is set to 0xFFFFFFFF
     *            or 0x00000000, then either the position field or the asfOffset
     *            field MUST be used to specify the starting point for playback.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return frameOffset (4 bytes): A 32-bit field. If the server is requested
     *         to stream the content until its end, this field MUST be set to
     *         0x00000000. Otherwise, the low-order 31 bits MUST be treated as
     *         an unsigned 31-bit integer. The low-order 31 bits MUST be set to
     *         the time position in the content at which the server is requested
     *         to stop streaming, in millisecond time units. The time position
     *         MUST be expressed either relative to the start of the content
     *         (time 0), in which case the most significant bit in the
     *         frameOffset field MUST be 0, or relative to the start position
     *         requested in this LinkViewerToMacStartPlaying message, in which
     *         case the most significant bit in the frameOffset field MUST be 1.
     */
    public int getFrameOffset() {
        return frameOffset;
    }

    /**
     * @param frameOffset
     *            (4 bytes): A 32-bit field. If the server is requested to
     *            stream the content until its end, this field MUST be set to
     *            0x00000000. Otherwise, the low-order 31 bits MUST be treated
     *            as an unsigned 31-bit integer. The low-order 31 bits MUST be
     *            set to the time position in the content at which the server is
     *            requested to stop streaming, in millisecond time units. The
     *            time position MUST be expressed either relative to the start
     *            of the content (time 0), in which case the most significant
     *            bit in the frameOffset field MUST be 0, or relative to the
     *            start position requested in this LinkViewerToMacStartPlaying
     *            message, in which case the most significant bit in the
     *            frameOffset field MUST be 1.
     */
    public void setFrameOffset(int frameOffset) {
        this.frameOffset = frameOffset;
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
     * @return dwAccelBandwidth (4 bytes): A 32-bit unsigned integer. If this
     *         field is present, the dwAccelDuration field MUST also be present.
     *         The dwAccelBandwidth field specifies a transmission rate, in bits
     *         per second, that the client is requesting the server to use when
     *         transmitting the amount of multimedia data specified by the
     *         dwAccelDuration field.
     */
    public long getDwAccelBandwidth() {
        return dwAccelBandwidth;
    }

    /**
     * @param dwAccelBandwidth
     *            (4 bytes): A 32-bit unsigned integer. If this field is
     *            present, the dwAccelDuration field MUST also be present. The
     *            dwAccelBandwidth field specifies a transmission rate, in bits
     *            per second, that the client is requesting the server to use
     *            when transmitting the amount of multimedia data specified by
     *            the dwAccelDuration field.
     */
    public void setDwAccelBandwidth(long dwAccelBandwidth) {
        this.dwAccelBandwidth = dwAccelBandwidth;
    }

    /**
     * @return dwAccelDuration (4 bytes): A 32-bit unsigned integer. If this
     *         field is present, the dwAccelBandwidth field MUST also be
     *         present. The dwAccelDuration field specifies an amount of
     *         multimedia data, in millisecond units, that the client is
     *         requesting the server to transmit at the bandwidth specified by
     *         the dwAccelBandwidth field.
     */
    public long getDwAccelDuration() {
        return dwAccelDuration;
    }

    /**
     * @param dwAccelDuration
     *            (4 bytes): A 32-bit unsigned integer. If this field is
     *            present, the dwAccelBandwidth field MUST also be present. The
     *            dwAccelDuration field specifies an amount of multimedia data,
     *            in millisecond units, that the client is requesting the server
     *            to transmit at the bandwidth specified by the dwAccelBandwidth
     *            field.
     */
    public void setDwAccelDuration(long dwAccelDuration) {
        this.dwAccelDuration = dwAccelDuration;
    }

    /**
     * @return dwLinkBandwidth (4 bytes): A 32-bit unsigned integer. If this
     *         field is present, the dwAccelBandwidth and dwAccelDuration fields
     *         MUST also be present. The dwLinkBandwidth field specifies the
     *         client's connection bandwidth, in bits per second.
     */
    public long getDwLinkBandwidth() {
        return dwLinkBandwidth;
    }

    /**
     * @param dwLinkBandwidth
     *            (4 bytes): A 32-bit unsigned integer. If this field is
     *            present, the dwAccelBandwidth and dwAccelDuration fields MUST
     *            also be present. The dwLinkBandwidth field specifies the
     *            client's connection bandwidth, in bits per second.
     */
    public void setDwLinkBandwidth(long dwLinkBandwidth) {
        this.dwLinkBandwidth = dwLinkBandwidth;
    }

    @Override
    public String getMessageName() {
        return "Start Playing";
    }

    @Override
    public long getBodyLength() {
        long length = 40;
        if(getDwAccelBandwidth() > 0 && getDwAccelDuration() > 0 && getDwLinkBandwidth() > 0) {
            length += 12;
        }
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------\n  Position in sec: ");
        sb.append(position == Double.MAX_VALUE ? "not set" : position);
        sb.append("\n  ASF offset (start packet): ");
        sb.append(asfOffset);
        return sb.toString();
    }
}

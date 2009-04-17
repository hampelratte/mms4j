package org.hampelratte.net.mms.messages;

/**
 * TcpMessageHeader packet encapsulates MMS messages when they are sent over
 * TCP. A single TcpMessageHeader packet can contain multiple MMS messages.
 * TcpMessageHeader is not used for MMS messages that are sent over UDP.
 */
public class TcpMessageHeader {
    /**
     * Constant size of TcpMessageHeader
     */
    public static final int SIZE = 32;
    
    private byte rep = 1;
    
    private byte version = 0;
    
    private byte versionMinor = 0;
    
    private byte padding = 0;
    
    private int sessionId = 0xb00bface;
    
    private long messageLength;
    
    private int seal = 0x20534D4D;
    
    private int seq;
    
    private int mbz = 0;
    
    private long timeSent = 0;
    
    /** @return rep (1 byte): An 8-bit field. It MUST be set to 0x1. */
    public byte getRep() {
        return rep;
    }

    /** @param rep (1 byte): An 8-bit field. It MUST be set to 0x1. */
    public void setRep(byte rep) {
        this.rep = rep;
    }

    /** @return version (1 byte): An 8-bit field. It MUST be set to 0x0. */
    public byte getVersion() {
        return version;
    }

    /** @param version (1 byte): An 8-bit field. It MUST be set to 0x0. */
    public void setVersion(byte version) {
        this.version = version;
    }

    /** @return versionMinor (1 byte): An 8-bit field. It MUST be set to 0x0. */
    public byte getVersionMinor() {
        return versionMinor;
    }

    /** @param versionMinor (1 byte): An 8-bit field. It MUST be set to 0x0. */
    public void setVersionMinor(byte versionMinor) {
        this.versionMinor = versionMinor;
    }

    /**
     * @return padding (1 byte): An 8-bit field. It SHOULD be set to 0x0, and
     *         SHOULD be ignored by receivers.
     */
    public byte getPadding() {
        return padding;
    }

    /**
     * @param padding (1 byte): An 8-bit field. It SHOULD be set to 0x0, and SHOULD be
     * ignored by receivers.
     */
    public void setPadding(byte padding) {
        this.padding = padding;
    }

    /**
     * @return sessionId (4 bytes): A 32-bit field. It MUST be set to 2953575118.
     * 2953575118 = 0xb00bface. boobface ?!?, they must be smoking pot at M$ ;-)
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId (4 bytes): A 32-bit field. It MUST be set to 2953575118.
     * 2953575118 = 0xb00bface. boobface ?!?, they must be smoking pot at M$ ;-)
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    /**
     * @return messageLength (4 bytes): An unsigned 32-bit integer that specifies the
     * size, in bytes, of the MMS message field plus 16.
     */
    public long getMessageLength() {
        return messageLength;
    }

    /**
     * @param messageLength (4 bytes): An unsigned 32-bit integer that specifies the
     * size, in bytes, of the MMS message field plus 16.
     */
    public void setMessageLength(long messageLength) {
        this.messageLength = messageLength;
    }
    
    /** @return seal (4 bytes): A 32-bit field. It MUST be set to 0x20534D4D = "MMS ". */
    public int getSeal() {
        return seal;
    }
    
    /** @param seal (4 bytes): A 32-bit field. It MUST be set to 0x20534D4D = "MMS ". */
    public void setSeal(int seal) {
        this.seal = seal;
    }
    
    /**
     * @return chunkCount (4 bytes): An unsigned 32-bit integer. It MUST be set to the
     * total size of the TcpMessageHeader packet in multiples of 8 bytes.
     */
    public long getChunkCount() {
        return getMessageLength() / 8;
    }
    
    /**
     * @return seq (2 bytes): An unsigned 16-bit integer. The TcpMessageHeader
     *         packet sequence number. The value of this field MUST increment by
     *         1 for each TcpMessageHeader packet that is sent. The first packet
     *         MUST have a seq value of 0x0000.
     * 
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param sequence
     *            (2 bytes): An unsigned 16-bit integer. The TcpMessageHeader
     *            packet sequence number. The value of this field MUST increment
     *            by 1 for each TcpMessageHeader packet that is sent. The first
     *            packet MUST have a seq value of 0x0000.
     */
    public void setSeq(int sequence) {
        this.seq = sequence;
    }
    
    /** @return MBZ (2 bytes): A 16-bit field. It MUST be set to 0x0000. */
    public int getMbz() {
        return mbz;
    }
    
    /** @param mbz (2 bytes): A 16-bit field. It MUST be set to 0x0000. */
    public void setMbz(int mbz) {
        this.mbz = mbz;
    }

    /**
     * @return timeSent (8 bytes): An unsigned 64-bit integer. The value of this field
     * SHOULD be set to the elapsed time since the transmission of the first
     * TcpMessageHeader packet (measured at the time the current TcpMessageHeader
     * packet is sent), expressed in millisecond units. The value of this field MAY
     * be chosen arbitrarily by the sender. Receivers SHOULD ignore this field.
     */
    public long getTimeSent() {
        return timeSent;
    }

    /**
     * @param timeSent (8 bytes): An unsigned 64-bit integer. The value of this field
     * SHOULD be set to the elapsed time since the transmission of the first
     * TcpMessageHeader packet (measured at the time the current TcpMessageHeader
     * packet is sent), expressed in millisecond units. The value of this field MAY
     * be chosen arbitrarily by the sender. Receivers SHOULD ignore this field.
     */
    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Header");
        sb.append("\n  MMS message length: "); sb.append(getMessageLength());
        sb.append("\n  MMS body length: "); sb.append(getMessageLength() - 16);
        sb.append("\n  Sequence: "); sb.append(getSeq());
        return sb.toString();
    }
}

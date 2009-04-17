package org.hampelratte.net.mms.messages;

import org.hampelratte.net.mms.MMSObject;

/**
 * Parent class for all MMS messages described in paragraph 2.2.4
 * of the specification 
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public abstract class MMSMessage extends MMSObject {
    /**
     * TcpMessageHeader of this message
     */
    protected TcpMessageHeader header;
    
    /**
     * Message ID
     */
    protected int MID;
    
    private byte additionalPadding = 0;
    
    /**
     * Default constructor. Initializes a message with a default
     * TcpMessageHeader
     */
    public MMSMessage() {
        header = new TcpMessageHeader();
    }
    
    /**
     * @return the TcpMessageHeader of this message
     */
    public TcpMessageHeader getHeader() {
        return header;
    }

    /**
     * @param header the TcpMessageHeader of this message
     */
    public void setHeader(TcpMessageHeader header) {
        this.header = header;
    }

    /**
     * @return a message name, which can be used for logging etc.
     */
    public abstract String getMessageName();
    
    @Override
    public long getMessageLength() {
        /* from spec: messageLength (4 bytes): An unsigned 32-bit integer that
         * specifies the size, in bytes, of the MMS message field plus 16. */
        long length = 16;
       
        // the message, which can be raw 8bit or 16bit unicode
        length += getBodyLength();
        
        /* last thing is the padding
         * the length of the command may not be a multiple of 8
         * in that case we have to add a padding */
        if(length % 8 != 0) {
            additionalPadding = (byte)(8 - (length % 8));
            length += additionalPadding;
        }
        
        return length;
    }
    
    /**
     * @return the length in bytes of the message body (the part after the
     *         TcpMessageHeader) without padding
     */
    public abstract long getBodyLength();
    
    @Override
    public long getByteLength() {
        return getMessageLength() + 16;
    }
    
    /**
     * @return the length of the message body in chunks. A chunk is 8 bytes.
     */
    public int getChunkLength() {
        return (int) ((getMessageLength() - 16) / 8);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessageName());
        sb.append("\n  ");
        sb.append(getHeader().toString());
        return sb.toString();
    }

    /**
     * Returns the padding of this message
     * @return the number of bytes, which have to be added to the message body
     *         to make it divisible by 8
     */
    protected byte getAdditionalPadding() {
        return additionalPadding;
    }
    
    /**
     * Returns the message ID of this MMS message
     * @return the message ID of this MMS message
     */
    public int getMID() {
        return MID;
    }

    /**
     * Sets the message ID of this MMS message
     * @param mid the message ID of this MMS message (MID) 
     */
    public void setMID(int mid) {
        MID = mid;
    }
}

package org.hampelratte.net.mms;

/**
 * Parent class for all sent objects (messages and data packets) 
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public abstract class MMSObject {
    /**
     * @return the logical length used in the protocol. E.g. a mms message
     *         length leaves out the first 16 bytes
     */
    public abstract long getMessageLength();
    
    /**
     * @return the number of bytes it would take to write this object to a
     *         stream
     */
    public abstract long getByteLength();
}

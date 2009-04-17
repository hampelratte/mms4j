package org.hampelratte.net.mms.data;

public class PacketSizeMissingException extends Exception {
    public PacketSizeMissingException() {
        super("Couldn't find a MediaDetails object in session" +
        		" to determine the data packet size");
    }
}

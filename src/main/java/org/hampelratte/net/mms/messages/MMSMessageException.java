package org.hampelratte.net.mms.messages;


public class MMSMessageException extends Exception {
    public MMSMessageException(String errorCode, String msg) {
        super("["+errorCode+"] " + msg);
    }
}

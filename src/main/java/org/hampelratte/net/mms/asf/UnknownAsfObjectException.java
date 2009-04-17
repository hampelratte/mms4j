package org.hampelratte.net.mms.asf;

import unclealex.mms.GUID;

public class UnknownAsfObjectException extends Exception {

    private GUID guid;
    
    public UnknownAsfObjectException(GUID guid) {
        super(guid.toString());
        this.guid = guid;
    }

    public GUID getGuid() {
        return guid;
    }

    public void setGuid(GUID guid) {
        this.guid = guid;
    }

}

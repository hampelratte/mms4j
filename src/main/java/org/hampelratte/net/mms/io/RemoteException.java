package org.hampelratte.net.mms.io;

import org.hampelratte.net.mms.io.util.HRESULT;

public class RemoteException extends Exception {

    private int hr;
    
    public RemoteException(int hr) {
        this.hr = hr;
    }
    
    public int getHr() {
        return hr;
    }
    
    public void setHr(int hr) {
        this.hr = hr;
    }
    
    @Override
    public String toString() {
        return "Remote exception - HRESULT 0x" + Integer.toHexString(getHr()) + " " + HRESULT.hrToHumanReadable(hr);
    }

}

package org.hampelratte.net.mms.io;

public class RemoteException extends Exception {

    public RemoteException(String msg) {
        super("Remote exception - HRESULT: " + msg);
    }

}

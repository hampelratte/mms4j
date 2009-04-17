package org.hampelratte.net.mms.messages.client;

public class CancelProtocol extends MMSRequest {

    @Override
    public String getMessageName() {
        return "Cancel protocol";
    }

    @Override
    public long getBodyLength() {
        return 0;
    }

}

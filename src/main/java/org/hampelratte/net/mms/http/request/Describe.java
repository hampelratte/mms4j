package org.hampelratte.net.mms.http.request;

public class Describe extends HttpRequest {

    private String clientGuid;

    public Describe(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    @Override
    public String toString() {
        StringBuilder req = new StringBuilder(super.toString());
        req.append("Accept: */*").append(CRLF);
        req.append("Pragma: xClientGUID={").append(clientGuid).append('}').append(CRLF);
        // TODO make pragmas configurable
        req.append("Pragma: no-cache,rate=1.000000,stream-time=0,stream-offset=0:0,request-context=1,max-duration=0").append(CRLF);
        req.append("Connection: close").append(CRLF);
        return req.append(CRLF).toString();
    }
}

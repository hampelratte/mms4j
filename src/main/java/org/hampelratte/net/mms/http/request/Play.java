package org.hampelratte.net.mms.http.request;

public class Play extends HttpRequest {

    private String clientGuid;

    public Play(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    @Override
    public String toString() {
        StringBuilder req = new StringBuilder(super.toString());

        req.append("Accept: */*").append(CRLF);
        req.append("Pragma: xClientGUID={").append(clientGuid).append('}').append(CRLF);
        // TODO make pragmas configurable
        req.append("Pragma: no-cache,rate=1.000000,stream-time=0,stream-offset=0:0,request-context=2,max-duration=0").append(CRLF);
        req.append("Pragma: xPlayStrm=1").append(CRLF);
        req.append("Pragma: stream-switch-entry=ffff:1:0 ffff:2:0").append(CRLF);
        req.append("Pragma: stream-switch-count=2").append(CRLF);
        req.append("Connection: close").append(CRLF);

        return req.append(CRLF).toString();
    }
}

package org.hampelratte.net.mms.http.request;

import java.util.concurrent.TimeUnit;

public class Play extends HttpRequest {

    private String clientGuid;

    private long packetNum;

    public Play(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public long getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(long packetNum) {
        this.packetNum = packetNum;
    }

    @Override
    public String toString() {
        StringBuilder req = new StringBuilder(super.toString());

        req.append("Accept: */*").append(CRLF);
        req.append("Pragma: xClientGUID={").append(clientGuid).append('}').append(CRLF);
        // TODO make pragmas configurable
        req.append("Pragma: no-cache,rate=1.000,packet-num=").append(packetNum).append(",max-duration=0").append(CRLF);
        // Try to increase the download speed. We should get at least a few seconds of accelerated download, so that the buffer
        // gets filled quickly and no buffering has to be done while watching the video.
        // The server will respond with a http header with the bandwidth and the duration it has accepted
        // In this case we try to get 6 MBit for 12 hours. ZDFmediathek answers with 3.5 MBit for 20 seconds
        long accelDuration = TimeUnit.HOURS.toMillis(12);
        req.append("Pragma: LinkBW=6000000, AccelBW=6000000, AccelDuration=").append(accelDuration).append(", Speed=10000").append(CRLF);
        req.append("Pragma: xPlayStrm=1").append(CRLF);
        req.append("Pragma: stream-switch-entry=ffff:1:0 ffff:2:0").append(CRLF);
        req.append("Pragma: stream-switch-count=2").append(CRLF);
        req.append("Connection: close").append(CRLF);

        return req.append(CRLF).toString();
    }
}

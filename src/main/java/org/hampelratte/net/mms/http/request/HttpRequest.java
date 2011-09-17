package org.hampelratte.net.mms.http.request;

public abstract class HttpRequest {
    public static final String CRLF = "\r\n";

    protected String host;
    protected int port = 80;
    protected String path;
    protected String userAgent = "NSPlayer/4.1.0.3856"; // alternative user agent like "MMS4J/1.2-SNAPSHOT"; do not work ?!?

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        StringBuilder request = new StringBuilder("GET ");
        request.append(getPath()).append(" HTTP/1.1").append(CRLF);
        request.append("Host: ").append(getHost()).append(':').append(getPort()).append(CRLF);
        request.append("User-Agent: ").append(getUserAgent()).append(CRLF);
        return request.toString();
    }
}

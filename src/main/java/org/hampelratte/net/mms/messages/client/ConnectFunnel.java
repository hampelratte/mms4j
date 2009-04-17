package org.hampelratte.net.mms.messages.client;

/**
 * The ConnectFunnel message is sent by the client to request
 * that Data (section 2.2.2) packets be streamed by the server using a specific
 * protocol and to a specific port.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ConnectFunnel extends MMSRequest {

    private String ipAddress;
    
    private String protocol;
    
    private String port;
    
    private int playIncarnation = 0;
    
    private int maxBlockBytes = 0xFFFFFFFF;
    
    private int maxFunnelBytes = 0;
    
    private int maxBitRate = 0x00989680; // = 10000000 = 10000 kbit
    
    private int funnelMode = 2;
    
    /**
     * The ConnectFunnel message is sent by the client to request
     * that Data (section 2.2.2) packets be streamed by the server using a specific
     * protocol and to a specific port.
     */
    public ConnectFunnel() {
        super();
        setMID(0x00030002);
    }
    
    @Override
    public long getBodyLength() {
        byte length = 28;
        
        long stringLength = 5; // \\, \, \, NULL-byte 
        stringLength += ipAddress.length();
        stringLength += protocol.length();
        stringLength += port.length();
        stringLength *= 2; // factor 2 due to UTF-16
        
        return length + stringLength;
    }
    
    /** @return the ip-addr part of the funnelName */
    public String getIpAddress() {
        return ipAddress;
    }

    /** @param ipAddress the ip-addr part of the funnelName */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /** @return the proto part of the funnelName */
    public String getProtocol() {
        return protocol;
    }

    /** @param protocol the proto part of the funnelName */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /** @return the port part of the funnelName */
    public String getPort() {
        return port;
    }

    /** @param port the port part of the funnelName */
    public void setPort(String port) {
        this.port = port;
    }
    
    /**
     * @return playIncarnation (4 bytes): A 32-bit field. SHOULD be set to
     *         0x00000000. It MUST be ignored by the receiver.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation (4 bytes): A 32-bit field. SHOULD be set to
     *         0x00000000. It MUST be ignored by the receiver.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /** @return maxBlockBytes (4 bytes): A 32-bit field. SHOULD be set to 0xFFFFFFFF.*/
    public int getMaxBlockBytes() {
        return maxBlockBytes;
    }

    /** @param maxBlockBytes (4 bytes): A 32-bit field. SHOULD be set to 0xFFFFFFFF.*/
    public void setMaxBlockBytes(int maxBlockBytes) {
        this.maxBlockBytes = maxBlockBytes;
    }

    /** @return maxFunnelBytes (4 bytes): A 32-bit field. SHOULD be set to 0x00000000.*/
    public int getMaxFunnelBytes() {
        return maxFunnelBytes;
    }

    /** @param maxFunnelBytes (4 bytes): A 32-bit field. SHOULD be set to 0x00000000.*/
    public void setMaxFunnelBytes(int maxFunnelBytes) {
        this.maxFunnelBytes = maxFunnelBytes;
    }

    /** @return maxBitRate (4 bytes): A 32-bit field. SHOULD be set to 0x00989680.*/
    public int getMaxBitRate() {
        return maxBitRate;
    }

    /** @param maxBitRate (4 bytes): A 32-bit field. SHOULD be set to 0x00989680.*/
    public void setMaxBitRate(int maxBitRate) {
        this.maxBitRate = maxBitRate;
    }

    /** @return funnelMode (4 bytes): A 32-bit field. It MUST be set to 0x00000002.*/
    public int getFunnelMode() {
        return funnelMode;
    }

    /** @param funnelMode (4 bytes): A 32-bit field. It MUST be set to 0x00000002.*/
    public void setFunnelMode(int funnelMode) {
        this.funnelMode = funnelMode;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  IP: ");
        sb.append(ipAddress);
        sb.append("\n  Protocol: ");
        sb.append(protocol);
        sb.append("\n  Port: ");
        sb.append(port);
        return sb.toString();
    }

    @Override
    public String getMessageName() {
        return "Connect Funnel";
    }
}

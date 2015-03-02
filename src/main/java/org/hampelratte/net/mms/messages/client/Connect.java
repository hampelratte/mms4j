package org.hampelratte.net.mms.messages.client;

/**
 * Request a connection to a server. Specification paragraph 2.2.4.17
 * For simplicity this class doesn't provide all fields of the subscriberName,
 * but only fields of the client-token and the host-info
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class Connect extends MMSRequest {
    /** Packet-pair bandwidth estimation not requested. */
    public static final int MMS_DISABLE_PACKET_PAIR = 0xf0f0f0ef;
    /** Requests that packet-pair bandwidth estimation be performed. */
    public static final int MMS_USE_PACKET_PAIR = 0xf0f0f0f0;
    
    private String playerInfo;
    
    private String guid;
    
    private String host;
    
    private int playIncarnation = MMS_USE_PACKET_PAIR;
    
    private int macToViewerProtocolRevision = 0x0004000B;
    
    private int viewerToMacProtocolRevision = 0x0003001C;
    
    /**
     * Request a connection to a server. Specification paragraph 2.2.4.17
     */
    public Connect() {
        super();
        setMID(0x00030001);
    }

    /** @return the player part of the client-token (e.g. NSPlayer/7.0.0.1956) */
    public String getPlayerInfo() {
        return playerInfo;
    }

    /** @param playerInfo the player part of the client-token (e.g. NSPlayer/7.0.0.1956) */
    public void setPlayerInfo(String playerInfo) {
        this.playerInfo = playerInfo;
    }

    /** @return the guid part of the client-token */
    public String getGuid() {
        return guid;
    }

    /** @param guid the guid part of the client-token */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /** @return the host value of the subscriberName value */ 
    public String getHost() {
        return host;
    }

    /** @param host the host value of the subscriberName value */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * @return playIncarnation (4 bytes): A 32-bit field. The field specifies
     *         the type of packet-pair bandwidth estimation that should be used,
     *         if any.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. The field specifies the type of
     *            packet-pair bandwidth estimation that should be used, if any.
     *            Should be {@link #MMS_USE_PACKET_PAIR} or
     *            {@link #MMS_DISABLE_PACKET_PAIR}
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return MacToViewerProtocolRevision (4 bytes): A 32-bit field. It MUST be
     *         set to 0x0004000B.
     */
    public int getMacToViewerProtocolRevision() {
        return macToViewerProtocolRevision;
    }

    /**
     * @param macToViewerProtocolRevision
     *            MacToViewerProtocolRevision (4 bytes): A 32-bit field. It MUST
     *            be set to 0x0004000B.
     */
    public void setMacToViewerProtocolRevision(int macToViewerProtocolRevision) {
        this.macToViewerProtocolRevision = macToViewerProtocolRevision;
    }

    /**
     * @return ViewerToMacProtocolRevision (4 bytes): A 32-bit field. It MUST be
     *         set to 0x0003001C.
     */
    public int getViewerToMacProtocolRevision() {
        return viewerToMacProtocolRevision;
    }

    /**
     * @param viewerToMacProtocolRevision
     *            ViewerToMacProtocolRevision (4 bytes): A 32-bit field. It MUST
     *            be set to 0x0003001C.
     */
    public void setViewerToMacProtocolRevision(int viewerToMacProtocolRevision) {
        this.viewerToMacProtocolRevision = viewerToMacProtocolRevision;
    }
    
    @Override
    public String getMessageName() {
        return "Connect";
    }

    @Override
    public long getBodyLength() {
        long length = 20;       // constant message parts
        long stringLength = 13; // constant string parts (inclusive the terminating 0 byte)
        stringLength += playerInfo.length();
        stringLength += guid.length();
        stringLength += host.length();
        length += stringLength * 2; // factor 2 due to UTF-16LE
        return length;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  Player info: ");
        sb.append(playerInfo);
        sb.append("\n  GUID: ");
        sb.append(guid);
        sb.append("\n  Host: ");
        sb.append(host);
        return sb.toString();
    }
}

package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.HRESULT;
import org.hampelratte.net.mms.messages.client.Connect;


/**
 * The ReportConnectedEX message is sent by the server in
 * response to a {@link Connect} (section 2.2.4.17) message from
 * the client.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ReportConnectedEX extends MMSResponse {

    /** The server cannot perform a packet-pair experiment. */
    public static final int MMS_DISABLE_PACKET_PAIR = 0xf0f0f0ef;

    /** The server can perform a packet-pair experiment. */
    public static final int MMS_USE_PACKET_PAIR = 0xf0f0f0f0;

    private int hr;

    private int playIncarnation;

    private int macToViewerProtocolRevision = 0x0004000B;

    private int viewerToMacProtocolRevision = 0x0003001C;

    private double blockGroupPlayTime = 1;

    private int blockGroupBlocks = 1;

    private int maxOpenFiles = 1;

    private int blockMaxBytes = 0x00008000;

    private int maxBitrate = 0x00989680;

    private String serverVersionInfo = "";

    private String versionInfo = "";

    private String versionUrl = "";

    private String authenPackage = "";

    /**
     * @return hr (4 bytes): The result of processing the client
     *         Connect (section 2.2.4.17) message. For HRESULT
     *         codes, see [MS-ERREF].
     */
    public int getHr() {
        return hr;
    }

    /**
     * @param hr
     *            (4 bytes): The result of processing the client
     *            Connect (section 2.2.4.17) message. For HRESULT
     *            codes, see [MS-ERREF].
     */
    public void setHr(int hr) {
        this.hr = hr;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field that specifies if the
     *         server can perform a packet-pair experiment.
     * @see #MMS_USE_PACKET_PAIR
     * @see #MMS_DISABLE_PACKET_PAIR
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field that specifies if the server can
     *            perform a packet-pair experiment. It MUST be set to one of the
     *            following values: {@link #MMS_USE_PACKET_PAIR},
     *            {@link #MMS_DISABLE_PACKET_PAIR}
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /** @return A 32-bit field. It MUST be set to 0x0004000B. */
    public int getMacToViewerProtocolRevision() {
        return macToViewerProtocolRevision;
    }

    /**
     * @param macToViewerProtocolRevision
     *            (4 bytes): A 32-bit field. It MUST be set to 0x0004000B.
     */
    public void setMacToViewerProtocolRevision(int macToViewerProtocolRevision) {
        this.macToViewerProtocolRevision = macToViewerProtocolRevision;
    }

    /** @return A 32-bit field. It MUST be set to 0x0003001C. */
    public int getViewerToMacProtocolRevision() {
        return viewerToMacProtocolRevision;
    }

    /**
     * @param viewerToMacProtocolRevision
     *            (4 bytes): A 32-bit field. It MUST be set to 0x0003001C.
     */
    public void setViewerToMacProtocolRevision(int viewerToMacProtocolRevision) {
        this.viewerToMacProtocolRevision = viewerToMacProtocolRevision;
    }

    /**
     * @return blockGroupPlayTime (8 bytes): A DOUBLE (floating-point) field. It
     *         MUST be set to 1.0.
     */
    public double getBlockGroupPlayTime() {
        return blockGroupPlayTime;
    }

    /**
     * @param blockGroupPlayTime
     *            (8 bytes): A DOUBLE (floating-point) field. It MUST be set to
     *            1.0.
     */
    public void setBlockGroupPlayTime(double blockGroupPlayTime) {
        this.blockGroupPlayTime = blockGroupPlayTime;
    }

    /**
     * @return blockGroupBlocks (4 bytes): A 32-bit field. It MUST be set to
     *         0x00000001.
     */
    public int getBlockGroupBlocks() {
        return blockGroupBlocks;
    }

    /**
     * @param blockGroupBlocks
     *            (4 bytes): A 32-bit field. It MUST be set to 0x00000001.
     */
    public void setBlockGroupBlocks(int blockGroupBlocks) {
        this.blockGroupBlocks = blockGroupBlocks;
    }

    /**
     * @return maxOpenFiles (4 bytes): A 32-bit field. It MUST be set to
     *         0x00000001.
     */
    public int getMaxOpenFiles() {
        return maxOpenFiles;
    }

    /**
     * @param maxOpenFiles
     *            (4 bytes): A 32-bit field. It MUST be set to 0x00000001.
     */
    public void setMaxOpenFiles(int maxOpenFiles) {
        this.maxOpenFiles = maxOpenFiles;
    }

    /**
     * @return blockMaxBytes (4 bytes): A 32-bit field. It MUST be set to
     *         0x00008000.
     */
    public int getBlockMaxBytes() {
        return blockMaxBytes;
    }

    /**
     * @param blockMaxBytes
     *            (4 bytes): A 32-bit field. It MUST be set to 0x00008000.
     */
    public void setBlockMaxBytes(int blockMaxBytes) {
        this.blockMaxBytes = blockMaxBytes;
    }

    /**
     * @return maxBitrate (4 bytes): A 32-bit field. It MUST be set to
     *         0x00989680.
     */
    public int getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * @param maxBitrate
     *            (4 bytes): A 32-bit field. It MUST be set to 0x00989680.
     */
    public void setMaxBitrate(int maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    /**
     * @return ServerVersionInfo (variable): A variable-size array of Unicode
     *         characters. The major and minor version numbers of the software
     *         product (specified in section 2.2.4.17) that is sending the
     *         ReportConnectedEX message.
     */
    public String getServerVersionInfo() {
        return serverVersionInfo;
    }

    /**
     * @param serverVersion
     *            (variable): A variable-size array of Unicode characters. It
     *            MUST be set to the major and minor version numbers of the
     *            software product (specified in section 2.2.4.17) that is
     *            sending the ReportConnectedEX message and MUST
     *            adhere to the following Augmented Backus-Naur Form (ABNF) (as
     *            specified in [RFC4234]) syntax:
     * 
     *            <pre>
     * major = 1*2DIGIT
     * minor = 1*2DIGIT [&quot;.&quot; 1*4DIGIT &quot;.&quot; 1*4DIGIT]
     * server-version-info = major &quot;.&quot; minor %x0000
     * </pre>
     */
    public void setServerVersionInfo(String serverVersion) {
        this.serverVersionInfo = serverVersion;
    }

    /**
     * @return versionInfo (variable): A variable-size array of Unicode
     *         characters. If the server wants the client to upgrade to a new
     *         version, it SHOULD include a nonzero size VersionInfo field. When
     *         the VersionInfo field has a nonzero size, it MUST specify the
     *         major and minor version numbers of the client software that the
     *         client should upgrade to.
     */
    public String getVersionInfo() {
        return versionInfo;
    }

    /**
     * @param versionInfo
     *            (variable): A variable-size array of Unicode characters. If
     *            the server wants the client to upgrade to a new version, it
     *            SHOULD include a nonzero size VersionInfo field. When the
     *            VersionInfo field has a nonzero size, it MUST specify the
     *            major and minor version numbers of the client software that
     *            the client should upgrade to, and the VersionInfo field MUST
     *            adhere to the following ABNF syntax:
     * 
     *            <pre>
     * major = 1*2DIGIT
     * minor = 1*2DIGIT [&quot;.&quot; 1*4DIGIT &quot;.&quot; 1*4DIGIT]
     * version-info = major &quot;.&quot; minor %x0000
     * </pre>
     */
    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    /**
     * @return VersionUrl (variable): A variable-size array of Unicode
     *         characters. If the VersionInfo field has a nonzero size, the
     *         VersionUrl field MUST specify the URL from which the client can
     *         download a software update of the version specified by the
     *         VersionInfo field. The URL MUST adhere to the URI syntax (as
     *         specified in [RFC3986]), it MUST be expressed using Unicode
     *         characters, and it MUST include a terminating null character.
     */
    public String getVersionUrl() {
        return versionUrl;
    }

    /**
     * @param versionUrl
     *            (variable): A variable-size array of Unicode characters. If
     *            the VersionInfo field has a nonzero size, the VersionUrl field
     *            MUST specify the URL from which the client can download a
     *            software update of the version specified by the VersionInfo
     *            field. The URL MUST adhere to the URI syntax (as specified in
     *            [RFC3986]), it MUST be expressed using Unicode characters, and
     *            it MUST include a terminating null character.
     */
    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    /**
     * @return AuthenPackage (variable): A variable-size array of Unicode
     *         characters. If the server supports authentication, and if it
     *         sends a LinkMacToViewerSecurityChallenge (section 2.2.4.14)
     *         message to this client, it MUST include a nonzero size
     *         AuthenPackage field. The AuthenPackage field MUST specify the
     *         authentication scheme the server uses, according to the following
     *         ABNF syntax: auth-scheme= ( "BASIC" / "NTLM" ) %x0000
     */
    public String getAuthenPackage() {
        return authenPackage;
    }

    /**
     * @param authenPackage
     *            (variable): A variable-size array of Unicode characters. If
     *            the server supports authentication, and if it sends a
     *            LinkMacToViewerSecurityChallenge (section 2.2.4.14) message to
     *            this client, it MUST include a nonzero size AuthenPackage
     *            field. The AuthenPackage field MUST specify the authentication
     *            scheme the server uses, according to the following ABNF
     *            syntax: auth-scheme= ( "BASIC" / "NTLM" ) %x0000
     */
    public void setAuthenPackage(String authenPackage) {
        this.authenPackage = authenPackage;
    }
    
    /**
     * @return The number of Unicode characters in the {@link #getServerVersionInfo} field,
     *         including the terminating null character.
     */
    public long getServerVersionInfoLength() {
        /* + 1 due to the terminating NULL-byte
         * factor 2 due to UTF-16 encoding */
        return (getServerVersionInfo().length() + 1) * 2;
    }
    
    /**
     * @return The number of Unicode characters in the {@link #getVersionInfo} field,
     *         including the terminating null character.
     */
    public long getVersionInfoLength() {
        /* + 1 due to the terminating NULL-byte
         * factor 2 due to UTF-16 encoding */
        return (getVersionInfo().length() + 1) * 2;
    }
    
    /**
     * @return The number of Unicode characters in the {@link #getVersionUrl} field,
     *         including the terminating null character.
     */
    public long getVersionUrlLength() {
        /* + 1 due to the terminating NULL-byte
         * factor 2 due to UTF-16 encoding */
        return (getVersionUrl().length() + 1) * 2;
    }
    
    /**
     * @return The number of Unicode characters in the {@link #getAuthenPackage} field,
     *         including the terminating null character.
     */
    public long getAuthenPackageLength() {
        /* + 1 due to the terminating NULL-byte
         * factor 2 due to UTF-16 encoding */
        return (getAuthenPackage().length() + 1) * 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  HRESULT: ").append(HRESULT.hrToHumanReadable(getHr()));
        sb.append("\n  Server version info: ");
        sb.append(serverVersionInfo);
        sb.append("\n  Version info: ");
        sb.append(versionInfo);
        sb.append("\n  Version URL: ");
        sb.append(versionUrl);
        sb.append("\n  AuthenPackage: ");
        sb.append(authenPackage);
        return sb.toString();
    }

    @Override
    public String getMessageName() {
        return "Report Connected EX";
    }

    @Override
    public long getBodyLength() {
        long length = 64;
        length += getServerVersionInfoLength(); 
        length += versionInfo != null ? versionInfo.length() * 2 + 2 : 0;
        length += versionUrl != null ? versionUrl.length() * 2 + 2 : 0;
        length += authenPackage != null ? authenPackage.length() * 2 + 2 : 0;
        return length;
    }
}

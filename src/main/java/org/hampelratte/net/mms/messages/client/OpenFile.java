package org.hampelratte.net.mms.messages.client;

/**
 * The OpenFile message is sent by the client to specify the name
 * of the resource on the server that is to be streamed.
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class OpenFile extends MMSRequest {

    private int playIncarnation = 1;
    
    private int spare = 0;
    
    private String fileName;
    
    private byte[] tokenData;
    
    /**
     * The OpenFile message is sent by the client to specify the name
     * of the resource on the server that is to be streamed.
     */
    public OpenFile() {
        super();
        setMID(0x00030005);
    }
    
    @Override
    public String getMessageName() {
        return "OpenFile";
    }

    @Override
    public long getBodyLength() {
        long length = 24;
        length += fileName.length() * 2; // factor 2 due to UTF-16LE
            
        // one UTF-16 NULL-byte for the fileName 
        // NOTE Diff from spec. The NULL-byte should only be written, if tokenData is set
        length += 2;
        
        if(tokenData != null && tokenData.length > 0) {
            length += tokenData.length;
        }
        return length;
    }

    /**
     * @return playIncarnation (4 bytes): A 32-bit field. It MUST be set to a
     *         value in the range 0x00000001 to 0x000000FE, inclusive.
     */
    public int getPlayIncarnation() {
        return playIncarnation;
    }

    /**
     * @param playIncarnation
     *            (4 bytes): A 32-bit field. It MUST be set to a value in the
     *            range 0x00000001 to 0x000000FE, inclusive.
     */
    public void setPlayIncarnation(int playIncarnation) {
        this.playIncarnation = playIncarnation;
    }

    /**
     * @return spare (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It
     *         MUST be ignored by the receiver.
     */
    public int getSpare() {
        return spare;
    }

    /**
     * @param spare
     *            (4 bytes): A 32-bit field. SHOULD be set to 0x00000000. It
     *            MUST be ignored by the receiver.
     */
    public void setSpare(int spare) {
        this.spare = spare;
    }

    /**
     * @return token (4 bytes): A 32-bit unsigned integer. If the
     *         OpenFile message contains a nonzero size tokenData
     *         field, the token field MUST specify the offset to the start of
     *         the tokenData field, counted in bytes from the start of the
     *         fileName field. If the size of the tokenData field is zero, the
     *         value of the token field MUST be 0x00000000. Example: If the
     *         fileName field is abc.asf, and the size of the tokenData field is
     *         nonzero, the value of the token field must be 0x00000010.
     */
    public long getToken() {
        if(tokenData != null && tokenData.length > 0) {
            return (getFileName().length() + 1) * 2;
        } else {
            return 0;
        }
    }

    /**
     * @return cbtoken (4 bytes): A 32-bit unsigned integer. It MUST be the size,
     *         in bytes, of the tokenData field.
     */
    public long getCbToken() {
        if(tokenData != null && tokenData.length > 0) {
            return tokenData.length;
        } else {
            return 0;
        }
    }

    /**
     * @return fileName (variable): A variable size array of Unicode characters
     *         specifying the path component of the URL of the content. See
     *         definition of path as specified in [RFC3986] section 3.3.
     *         Percent-encoding (as specified in [RFC3986] section 2.1) MUST NOT
     *         be used on the characters in the path. The fileName field SHOULD
     *         NOT include a terminating null Unicode character, unless the
     *         value of the cbtoken field is greater than 0, in which case the
     *         fileName field MUST include a terminating null Unicode character.
     *         Example: media/mediastream.asf
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            (variable): A variable size array of Unicode characters
     *            specifying the path component of the URL of the content. See
     *            definition of path as specified in [RFC3986] section 3.3.
     *            Percent-encoding (as specified in [RFC3986] section 2.1) MUST
     *            NOT be used on the characters in the path. The fileName field
     *            SHOULD NOT include a terminating null Unicode character,
     *            unless the value of the cbtoken field is greater than 0, in
     *            which case the fileName field MUST include a terminating null
     *            Unicode character. Example: media/mediastream.asf
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return tokenData (variable): A variable size array of bytes. It MUST
     *         contain authentication data of the size given by the cbtoken
     *         field. Thus, if the cbtoken field is 0x00000000, the tokenData
     *         field is not included in the message. If the authentication data
     *         is for the Basic authentication scheme, as specified in
     *         [RFC2617], the authentication data MUST be the user name and
     *         password, according to the syntax for basic-credentials, as
     *         specified in [RFC2617]. The basic-credentials authentication data
     *         MUST be stored in the tokenData field as a null-terminated ASCII
     *         string. If the authentication data is for the NTLM authentication
     *         scheme, as specified in [MS-NLMP], the tokenData consists of an
     *         NTLM_AUTH structure (section 2.2.4.14.1).
     */
    public byte[] getTokenData() {
        return tokenData;
    }

    /**
     * @param tokenData
     *            (variable): A variable size array of bytes. It MUST contain
     *            authentication data of the size given by the cbtoken field.
     *            Thus, if the cbtoken field is 0x00000000, the tokenData field
     *            is not included in the message. If the authentication data is
     *            for the Basic authentication scheme, as specified in
     *            [RFC2617], the authentication data MUST be the user name and
     *            password, according to the syntax for basic-credentials, as
     *            specified in [RFC2617]. The basic-credentials authentication
     *            data MUST be stored in the tokenData field as a
     *            null-terminated ASCII string. If the authentication data is
     *            for the NTLM authentication scheme, as specified in [MS-NLMP],
     *            the tokenData consists of an NTLM_AUTH structure (section
     *            2.2.4.14.1).
     */
    public void setTokenData(byte[] tokenData) {
        this.tokenData = tokenData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\n  --------");
        sb.append("\n  File: ");
        sb.append(fileName);
        return sb.toString();
    }
}

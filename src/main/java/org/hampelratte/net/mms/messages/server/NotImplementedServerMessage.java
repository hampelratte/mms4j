package org.hampelratte.net.mms.messages.server;

import org.hampelratte.net.mms.io.util.StringUtils;

/**
 * This is a dummy message for messages coming from the server, which are not yet implemented, or which we don't know
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class NotImplementedServerMessage extends MMSResponse {

    private byte[] data;

    @Override
    public String getMessageName() {
        return "Not implemented server message";
    }

    /**
     * @param bytes
     *            the message body as raw byte array
     */
    public void setMessage(byte[] bytes) {
        // unknown server command, don't know what to do with the data
        this.data = bytes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\n  Data:\n  ");
        sb.append(StringUtils.toHexString(data, 8));
        return sb.toString();
    }

    @Override
    public long getBodyLength() {
        return data.length;
    }
}

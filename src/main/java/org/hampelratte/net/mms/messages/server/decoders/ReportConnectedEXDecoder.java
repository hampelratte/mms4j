package org.hampelratte.net.mms.messages.server.decoders;

import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.ReportConnectedEX;

/**
 * Decoder for {@link ReportConnectedEX} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ReportConnectedEXDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) throws RemoteException, CharacterCodingException {
        ReportConnectedEX rce = new ReportConnectedEX();
        rce.setHr(b.getInt());
        if (rce.getHr() != 0) {
            throw new RemoteException(rce.getHr());
        }
        rce.setPlayIncarnation(b.getInt());
        rce.setMacToViewerProtocolRevision(b.getInt());
        rce.setViewerToMacProtocolRevision(b.getInt());
        rce.setBlockGroupPlayTime(b.getDouble());
        rce.setBlockGroupBlocks(b.getInt());
        rce.setMaxOpenFiles(b.getInt());
        rce.setBlockMaxBytes(b.getInt());
        rce.setMaxBitrate(b.getInt());

        // * 2 because its Unicode 16bit
        long serverVersionInfoLength = b.getUnsignedInt() * 2;
        long versionInfoLength = b.getUnsignedInt() * 2;
        long versionUrlLength = b.getUnsignedInt() * 2;
        long authenPackageLength = b.getUnsignedInt() * 2;

        b.order(ByteOrder.BIG_ENDIAN);
        if (serverVersionInfoLength > 0) {
            // length - 2, because we don't want to read the UTF-16 NULL-byte
            rce.setServerVersionInfo(b.getString((int) (serverVersionInfoLength - 2), StringUtils.getDecoder("UTF-16LE")));
            b.skip(2); // skip 16bit nullbyte
        }
        if (versionInfoLength > 0) {
            // length - 2, because we don't want to read the UTF-16 NULL-byte
            rce.setVersionInfo(b.getString((int) (versionInfoLength - 2), StringUtils.getDecoder("UTF-16LE")));
            b.skip(2); // skip 16bit nullbyte
        }
        if (versionUrlLength > 0) {
            // length - 2, because we don't want to read the UTF-16 NULL-byte
            rce.setVersionUrl(b.getString((int) (versionUrlLength - 2), StringUtils.getDecoder("UTF-16LE")));
            b.skip(2); // skip 16bit nullbyte
        }
        if (authenPackageLength > 0) {
            // length - 2, because we don't want to read the UTF-16 NULL-byte
            rce.setAuthenPackage(b.getString((int) (authenPackageLength - 2), StringUtils.getDecoder("UTF-16LE")));
            b.skip(2); // skip 16bit nullbyte
        }

        return rce;
    }
}

package org.hampelratte.net.mms.messages.server.decoders;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hampelratte.net.mms.io.RemoteException;
import org.hampelratte.net.mms.messages.TcpMessageHeader;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for all decoders of messages returned by the server
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public abstract class MMSResponseDecoder {

    private static transient Logger logger = LoggerFactory.getLogger(MMSResponseDecoder.class);

    protected TcpMessageHeader header;

    /**
     * Decodes received data to objects
     *
     * @param session
     *            the Mina {@link IoSession}
     * @param b
     *            the buffer, which contains the received data
     * @param out
     *            the decoded objects has to be written to this output
     * @throws RemoteException
     *             if we received a HRESULT error
     * @throws UnsupportedEncodingException
     *             if the java platform does not support UTF-16LE
     * @throws CharacterCodingException
     *             if the java platform does not support UTF-16LE
     */
    public void decode(IoSession session, IoBuffer b, ProtocolDecoderOutput out) throws CharacterCodingException, UnsupportedEncodingException, RemoteException {
        int positionBeforeBodyDecoding = b.position();

        // skip chunkLen
        b.getInt();

        // decode message id (MID)
        int mid = b.getInt();

        // decode the rest of the message
        MMSResponse resp = doDecode(session, b);

        resp.setMID(mid);
        resp.setHeader(header);

        // there could be some padding, which we have to skip now
        int positionAfterBodyDecoding = b.position();
        int bytesRead = positionAfterBodyDecoding - positionBeforeBodyDecoding;
        int padding = (int) (header.getMessageLength() - 16 - bytesRead);
        if (padding > 0) {
            logger.trace("Decoder {} didn't read whole message. " + "{} bytes padding or unknown data remaining, which will be skipped now", getClass()
                    .getSimpleName(), padding);
            b.skip(padding);
        }

        out.write(resp);
    }

    /**
     * Decodes received data to objects
     *
     * @param session
     *            the Mina {@link IoSession}
     * @param b
     *            the buffer, which contains the received data
     * @return the decoded MMS message as {@link MMSResponse} object
     * @throws UnsupportedEncodingException
     *             if the java platform does not support UTF-16LE
     * @throws CharacterCodingException
     *             if the java platform does not support UTF-16LE
     * @throws RemoteException
     *             if we received a HRESULT error
     */
    public abstract MMSResponse doDecode(IoSession session, IoBuffer b) throws RemoteException, CharacterCodingException, UnsupportedEncodingException;

    /**
     * @return the {@link TcpMessageHeader}, which belongs to the received MMS message
     */
    public TcpMessageHeader getHeader() {
        return header;
    }

    /**
     * Sets the {@link TcpMessageHeader}, which belongs to the received MMS message
     *
     * @param header
     *            the {@link TcpMessageHeader}, which belongs to the received MMS message
     */
    public void setHeader(TcpMessageHeader header) {
        this.header = header;
    }
}

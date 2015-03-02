package org.hampelratte.net.mms.messages.server.decoders;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.messages.server.MMSResponse;
import org.hampelratte.net.mms.messages.server.NotImplementedServerMessage;

/**
 * Decoder for {@link NotImplementedServerMessage} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class NotImplementedServerMessageDecoder extends MMSResponseDecoder {

    @Override
    public MMSResponse doDecode(IoSession session, IoBuffer b) {
        NotImplementedServerMessage nisc = new NotImplementedServerMessage();

        byte[] data = new byte[(int) (header.getMessageLength() - 16)];
        b.get(data);
        nisc.setMessage(data);

        return nisc;
    }
}

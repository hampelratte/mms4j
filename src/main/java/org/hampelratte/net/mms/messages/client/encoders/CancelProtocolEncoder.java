package org.hampelratte.net.mms.messages.client.encoders;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class CancelProtocolEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception {
        // nothing to encode
    }
}

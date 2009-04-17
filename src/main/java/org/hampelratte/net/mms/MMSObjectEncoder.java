package org.hampelratte.net.mms;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.messages.client.MMSRequest;
import org.hampelratte.net.mms.messages.client.encoders.MMSRequestEncoderFactory;

/**
 * Encoder for all objects sent to a server
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class MMSObjectEncoder implements ProtocolEncoder {

    private ProtocolEncoder encoder;
    
    public void dispose(IoSession arg0) throws Exception {
        encoder.dispose(arg0);
    }

    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
        if(obj instanceof MMSRequest) {
            encoder = MMSRequestEncoderFactory.createEncoder((MMSRequest)obj);
            encoder.encode(session, obj, out);
        } else {
            throw new UnsupportedOperationException("Encoding of objects other than MMS messages is not implemented yet");
        }
    } 
}
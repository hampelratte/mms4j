package org.hampelratte.net.mms.http;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.http.request.HttpRequest;
import org.hampelratte.net.mms.http.request.encoder.HttpRequestEncoder;

/**
 * Encoder for all http requests sent to a server
 * 
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class MMSHttpRequestEncoder implements ProtocolEncoder {

    private ProtocolEncoder encoder = new HttpRequestEncoder();

    @Override
    public void dispose(IoSession arg0) throws Exception {
        encoder.dispose(arg0);
    }

    @Override
    public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
        if (obj instanceof HttpRequest) {
            encoder.encode(session, obj, out);
        } else {
            throw new UnsupportedOperationException("Encoding of objects other than MMS over HTTP requests is not implemented yet");
        }
    }
}
package org.hampelratte.net.mms.http.request.encoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.http.request.HttpRequest;

public class HttpRequestEncoder extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        HttpRequest req = (HttpRequest) message;
        IoBuffer b = IoBuffer.wrap(req.toString().getBytes("UTF-8"));
        out.write(b);
    }
}

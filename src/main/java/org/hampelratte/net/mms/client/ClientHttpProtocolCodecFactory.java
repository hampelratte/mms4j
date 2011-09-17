package org.hampelratte.net.mms.client;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.hampelratte.net.mms.http.MMSHttpRequestEncoder;
import org.hampelratte.net.mms.http.MMSHttpResponseDecoder;

public class ClientHttpProtocolCodecFactory implements ProtocolCodecFactory {

    private ProtocolDecoder decoder = new MMSHttpResponseDecoder();
    private ProtocolEncoder encoder = new MMSHttpRequestEncoder();

    @Override
    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        return encoder;
    }

}

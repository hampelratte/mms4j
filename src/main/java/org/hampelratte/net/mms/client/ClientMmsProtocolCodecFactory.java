package org.hampelratte.net.mms.client;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.hampelratte.net.mms.MMSObjectDecoder;
import org.hampelratte.net.mms.MMSObjectEncoder;

public class ClientProtocolCodecFactory implements ProtocolCodecFactory {

    private ProtocolDecoder decoder = new MMSObjectDecoder();
    private ProtocolEncoder encoder = new MMSObjectEncoder();
    
    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        return decoder;
    }

    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        return encoder;
    }

}

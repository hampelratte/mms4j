package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.hampelratte.net.mms.messages.client.ConnectFunnel;

/**
 * Encoder for {@link ConnectFunnel} objects
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class ConnectFunnelEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object command, ProtocolEncoderOutput out) throws Exception {
        ConnectFunnel cf = (ConnectFunnel) command;
        IoBuffer b = IoBuffer.allocate((int) cf.getBodyLength());
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        b.putInt(cf.getPlayIncarnation());
        b.putInt(cf.getMaxBlockBytes());
        b.putInt(cf.getMaxFunnelBytes());
        b.putInt(cf.getMaxBitRate());
        b.putInt(cf.getFunnelMode());
        
        StringBuilder funnelName = new StringBuilder();
        funnelName.append('\\').append('\\');
        funnelName.append(cf.getIpAddress());
        funnelName.append('\\');
        funnelName.append(cf.getProtocol());
        funnelName.append('\\');
        funnelName.append(cf.getPort());
        b.putString(funnelName, StringUtils.getEncoder("UTF-16LE"));
        
        // one UTF-16 NULL-byte
        b.putShort((short)0);

        b.flip();
        out.write(b);
    }
}

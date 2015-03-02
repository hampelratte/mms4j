package org.hampelratte.net.mms.messages.client.encoders;

import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hampelratte.net.mina.nio.ExtendedIoBuffer;
import org.hampelratte.net.mms.io.util.StringUtils;
import org.hampelratte.net.mms.messages.client.Connect;

/**
 * Encoder for {@link Connect} objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class ConnectEncoder extends MMSRequestEncoder {

    @Override
    protected void doEncode(IoSession session, Object message, ProtocolEncoderOutput out) throws CharacterCodingException {
        Connect connect = (Connect) message;
        
        ExtendedIoBuffer b = new ExtendedIoBuffer(IoBuffer.allocate((int)connect.getBodyLength()));
        b.order(ByteOrder.LITTLE_ENDIAN);
        
        b.putInt(connect.getPlayIncarnation());
        b.putInt(connect.getMacToViewerProtocolRevision());
        b.putInt(connect.getViewerToMacProtocolRevision());

        // construct subscriberName
        StringBuilder subscriberName = new StringBuilder();
        subscriberName.append(connect.getPlayerInfo());
        subscriberName.append("; {");
        subscriberName.append(connect.getGuid());
        subscriberName.append("}; Host: ");
        subscriberName.append(connect.getHost());
        b.putString(subscriberName, StringUtils.getEncoder("UTF-16LE")); 
        
        // one UTF-16 NULL-byte
        b.putShort((short)0);
        
        // write the buffer to the output
        b.flip();
        out.write(b);
    }
}

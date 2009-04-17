package org.hampelratte.net.mms.messages.client.encoders;

import org.apache.mina.core.session.IoSession;
import org.hampelratte.net.mms.messages.MMSMessageEncoder;

/**
 * Parent class for encoders of messages sent to the server
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public abstract class MMSRequestEncoder extends MMSMessageEncoder {
    
    @Override
    protected void doDispose(IoSession iosession) {
        // most encoders don't have anything to dispose
        // that's why we handle this method here inside
    }
}

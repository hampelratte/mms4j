package org.hampelratte.net.mms.client.listeners;

import org.hampelratte.net.mms.messages.MMSMessage;

/**
 * Listener for arriving MMS messages
 *
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public interface MMSMessageListener {
    /**
     * Called, when an MMS message has arrived
     * @param message the decoded MMS message
     */
    public void messageReceived(MMSMessage message);
}

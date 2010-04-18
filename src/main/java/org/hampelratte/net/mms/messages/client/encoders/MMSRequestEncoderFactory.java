package org.hampelratte.net.mms.messages.client.encoders;

import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.MMSRequest;

/**
 * Factory class to create {@link MMSRequestEncoder} objects for message objects
 * 
 * @author <a href="mailto:hampelratte@users.berlios.de">hampelratte@users.berlios.de</a>
 */
public class MMSRequestEncoderFactory {

    /**
     * @param request
     *            The MMS message, which shall be encoded
     * @return a {@link MMSRequestEncoder} for the given {@link MMSRequest}
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static MMSRequestEncoder createEncoder(MMSMessage request) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String encoderClsName = "org.hampelratte.net.mms.messages.client.encoders."
                + request.getClass().getSimpleName() + "Encoder";
        Class<?> encoderClass = Class.forName(encoderClsName);
        MMSRequestEncoder encoder = (MMSRequestEncoder) encoderClass.newInstance();
        return encoder;
    }
}

package org.hampelratte.net.mms.messages.client.encoders;

import org.hampelratte.net.mms.messages.MMSMessage;
import org.hampelratte.net.mms.messages.client.MMSRequest;

/**
 * Factory class to create {@link MMSRequestEncoder} objects for message objects
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class MMSRequestEncoderFactory {

    /**
     * @param request
     *            The MMS message, which shall be encoded
     * @return a {@link MMSRequestEncoder} for the given {@link MMSRequest}
     * @throws InstantiationException
     *             if the encoder or its constructor for this request is not accessible (shouldn't happen)
     * @throws IllegalAccessException
     *             if the encoder's class for this request is not a regular class (shouldn't happen)
     * @throws ClassNotFoundException
     *             if the encoder's class for this request is not in the class path (shouldn't happen)
     */
    public static MMSRequestEncoder createEncoder(MMSMessage request) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String encoderClsName = "org.hampelratte.net.mms.messages.client.encoders." + request.getClass().getSimpleName() + "Encoder";
        Class<?> encoderClass = Class.forName(encoderClsName);
        MMSRequestEncoder encoder = (MMSRequestEncoder) encoderClass.newInstance();
        return encoder;
    }
}

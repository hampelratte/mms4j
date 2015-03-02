package org.hampelratte.net.mms.messages.server.decoders;

import java.util.HashMap;
import java.util.Map;

import org.hampelratte.net.mms.MMSObjectDecoder;

/**
 * Factory class to create MMSResponseDecoder objects from message IDs (MID)
 *
 * @author <a href="mailto:henrik.niehaus@gmx.de">henrik.niehaus@gmx.de</a>
 */
public class MMSResponseDecoderFactory extends MMSObjectDecoder {

    private static Map<Integer, Class<?>> msgClasses = new HashMap<Integer, Class<?>>();

    static {
        msgClasses.put(0x00040001, ReportConnectedEXDecoder.class);
        msgClasses.put(0x00040002, ReportConnectedFunnelDecoder.class);
        msgClasses.put(0x00040003, ReportDisconnectedFunnelDecoder.class);
        msgClasses.put(0x00040005, ReportStartedPlayingDecoder.class);
        msgClasses.put(0x00040006, ReportOpenFileDecoder.class);
        msgClasses.put(0x00040011, ReportReadBlockDecoder.class);
        msgClasses.put(0x0004001B, PingDecoder.class);
        msgClasses.put(0x0004001E, ReportEndOfStreamDecoder.class);
        msgClasses.put(0x00040021, ReportStreamSwitchDecoder.class);
    }

    /**
     * Returns a {@link MMSResponseDecoder} for a given message ID (MID)
     *
     * @param MID
     *            a message ID of a MMS message
     * @return a {@link MMSResponseDecoder}, which is able to decode a message of the given type
     * @throws InstantiationException
     *             if the decoder or its constructor for this message ID is not accessible (shouldn't happen)
     * @throws IllegalAccessException
     *             if the decoder's class for this message ID is not a regular class (shouldn't happen)
     */
    public static MMSResponseDecoder createDecoder(int MID) throws InstantiationException, IllegalAccessException {
        Class<?> encoderClass = msgClasses.get(MID);
        if (encoderClass == null) {
            return new NotImplementedServerMessageDecoder();
        }

        MMSResponseDecoder decoder = (MMSResponseDecoder) encoderClass.newInstance();
        return decoder;
    }
}

package org.hampelratte.net.mms.messages.client;

import java.util.ArrayList;
import java.util.List;

/**
 * The StreamSwitch message is sent by the client to select,
 * unselect, thin, unthin, or replace individual streams that it is currently
 * receiving.
 */
public class StreamSwitch extends MMSRequest {

    private List<StreamSwitchEntry> sseList = new ArrayList<StreamSwitchEntry>(); 
    
    /**
     * The StreamSwitch message is sent by the client to select,
     * unselect, thin, unthin, or replace individual streams that it is currently
     * receiving.
     */
    public StreamSwitch() {
        super();
        setMID(0x00030033);
    }
    
    @Override
    public String getMessageName() {
        return "Stream Switch";
    }
    
    @Override
    public long getBodyLength() {
        return 12 + sseList.size() * 6;
    }
    
    /**
     * Adds a {@link StreamSwitchEntry} to the message
     * @param sse
     */
    public void addStreamSwitchEntry(StreamSwitchEntry sse) {
        sseList.add(sse);
    }
    
    /**
     * @return a list of all {@link StreamSwitchEntry} objects
     */
    public List<StreamSwitchEntry> getStreamSwitchEntries() {
        return sseList;
    }
    
    /**
     * Used by StreamSwitch message to select, unselect, thin,
     * unthin, or replace individual streams that it is currently receiving.
     */
    public class StreamSwitchEntry {
        /**
         * All ASF media objects for the stream given by dstStreamNumber are to
         * be transmitted.
         */
        public static final int EVERYTHING = 0;
        
        /**
         * Only ASF media objects for the stream given by dstStreamNumber (that
         * are marked as containing key frame data) are to be transmitted.
         */
        public static final int KEYFRAMES_ONLY = 1;
        
        /**
         * No ASF media objects for the stream given by dstStreamNumber are to
         * be transmitted.
         */
        public static final int NOTHING = 2;
        
        private int srcStreamNumber;

        private int dstStreamNumber;

        private int thinningLevel = EVERYTHING;
        
        /**
         * Convenience constructor
         * @param srcStreamNumber
         * @param dstStreamNumber
         * @param thinningLevel
         */
        public StreamSwitchEntry(int srcStreamNumber, int dstStreamNumber, int thinningLevel) {
            this.srcStreamNumber = srcStreamNumber;
            this.dstStreamNumber = dstStreamNumber;
            this.thinningLevel = thinningLevel;
        }

        /**
         * @return srcStreamNumber: A 16-bit unsigned integer. This field MUST
         *         be set to the ASF stream number (of the stream that the
         *         server is requested to replace with the stream specified by
         *         the wDstStreamNumber field) or 0xFFFF if wDstStreamNumber is
         *         not supposed to be replaced by any other stream.
         */
        public int getSrcStreamNumber() {
            return srcStreamNumber;
        }

        /**
         * @param srcStreamNumber
         *            : A 16-bit unsigned integer. This field MUST be set to the
         *            ASF stream number (of the stream that the server is
         *            requested to replace with the stream specified by the
         *            wDstStreamNumber field) or 0xFFFF if wDstStreamNumber is
         *            not supposed to be replaced by any other stream.
         */
        public void setSrcStreamNumber(int srcStreamNumber) {
            this.srcStreamNumber = srcStreamNumber;
        }

        /**
         * @return dstStreamNumber: A 16-bit unsigned integer. This field MUST
         *         be set to the ASF stream number (of the stream that the
         *         server is requested to replace with the stream specified by
         *         the wSrcStreamNumber field) or 0xFFFF if wSrcStreamNumber is
         *         not supposed to be replaced by any other stream.
         */
        public int getDstStreamNumber() {
            return dstStreamNumber;
        }

        /**
         * @param dstStreamNumber
         *            : A 16-bit unsigned integer. This field MUST be set to the
         *            ASF stream number (of the stream that the server is
         *            requested to replace with the stream specified by the
         *            wSrcStreamNumber field) or 0xFFFF if wSrcStreamNumber is
         *            not supposed to be replaced by any other stream.
         */
        public void setDstStreamNumber(int dstStreamNumber) {
            this.dstStreamNumber = dstStreamNumber;
        }

        /**
         * @return thinningLevel: A 16-bit unsigned integer. This field MUST be
         *         set to one of the values in the following table.
         */
        public int getThinningLevel() {
            return thinningLevel;
        }

        /**
         * @param thinningLevel
         *            : A 16-bit unsigned integer. This field MUST be set to one
         *            of the values in the following table.
         */
        public void setThinningLevel(int thinningLevel) {
            this.thinningLevel = thinningLevel;
        }
    }

}

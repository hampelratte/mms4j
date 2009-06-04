package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hampelratte.net.mms.asf.io.ASFInputStream;

public class ASFStreamBitratePropertiesObject extends ASFHeaderObject {

    private List<BitrateRecord> bitrateRecords = new ArrayList<BitrateRecord>();
    
    @Override
    public void setData(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);
        
        int count = asfin.readLEShort();
        for (int i = 0; i < count; i++) {
            int flags = asfin.readLEShort();
            short streamNumber = (short) (flags & 0x7F);
            long bitrate = asfin.readLEInt();
            bitrateRecords.add(new BitrateRecord(streamNumber, bitrate));
        }
    }

    public List<BitrateRecord> getBitrateRecords() {
        return bitrateRecords;
    }
    
    public void setBitrateRecords(List<BitrateRecord> bitrateRecords) {
        this.bitrateRecords = bitrateRecords;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(super.toString());
        s.append(" [");
        for (Iterator<BitrateRecord> iterator = bitrateRecords.iterator(); iterator.hasNext();) {
            BitrateRecord br = iterator.next();
            s.append(br.toString());
            s.append(iterator.hasNext() ? "," : "");
        }
        s.append("]");
        return s.toString();
    }
    
    public class BitrateRecord {
        private short streamNumber;
        private long averageBitrate;
        
        public BitrateRecord(short streamNumber, long averageBitrate) {
            this.streamNumber = streamNumber;
            this.averageBitrate = averageBitrate;
        }

        public short getStreamNumber() {
            return streamNumber;
        }

        public void setStreamNumber(short streamNumber) {
            this.streamNumber = streamNumber;
        }
        
        public long getAverageBitrate() {
            return averageBitrate;
        }
        
        public void setAverageBitrate(long averageBitrate) {
            this.averageBitrate = averageBitrate;
        }
        
        @Override
        public String toString() {
            return "BitrateRecord:[Stream number:"+streamNumber+",Average Bitrate:"+averageBitrate+"]";
        }
    }
}

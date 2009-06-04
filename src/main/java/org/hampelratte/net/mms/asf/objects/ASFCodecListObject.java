package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unclealex.mms.GUID;

public class ASFCodecListObject extends ASFHeaderObject {

    private static transient Logger logger = LoggerFactory.getLogger(ASFCodecListObject.class);
    
    private GUID reserved;

    private int codecEntriesCount;

    private List<CodecEntry> codecEntries;

    @Override
    public void setData(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        reserved = asfin.readGUID();
        codecEntriesCount = (int) asfin.readLEInt();
        codecEntries = new ArrayList<CodecEntry>(codecEntriesCount);
        
        for (int i = 0; i < codecEntriesCount; i++) {
            try {
                CodecEntry entry = new CodecEntry();
                entry.setType(asfin.readLEShort());
                int nameLength = asfin.readLEShort() * 2;
                entry.setName(asfin.readUTF16LE(nameLength));
                int descriptionLength = asfin.readLEShort() * 2;
                entry.setDescription(asfin.readUTF16LE(descriptionLength));
                int infoLength = asfin.readLEShort();
                byte[] info = new byte[infoLength];
                asfin.read(info);
                entry.setCodecInfo(info);
                codecEntries.add(entry);
            } catch (Exception e) {
                logger.warn("Couldn't read codec entry", e);
            }
        }
    }
    
    public GUID getReserved() {
        return reserved;
    }


    public void setReserved(GUID reserved) {
        this.reserved = reserved;
    }

    public List<CodecEntry> getCodecEntries() {
        return codecEntries;
    }

    public void setCodecEntries(List<CodecEntry> codecEntries) {
        this.codecEntries = codecEntries;
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += " [Reserved GUID:" + reserved + ",Entry count:" + codecEntriesCount;
        if (codecEntries != null) {
            s += ",Entries:[";
            for (Iterator<CodecEntry> iterator = codecEntries.iterator(); iterator.hasNext();) {
                CodecEntry entry = iterator.next();
                s += entry;
                s += iterator.hasNext() ? "," : "";
            }
            s += "]";
        }
        s += "]";
        return s;
    }

    public class CodecEntry {
        private int type;

        private String name;

        private String description;

        private byte[] codecInfo;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public byte[] getCodecInfo() {
            return codecInfo;
        }

        public void setCodecInfo(byte[] codecInfo) {
            this.codecInfo = codecInfo;
        }
        
        @Override
        public String toString() {
            String typeString = type == 1 ? "Video" : type == 2 ? "Audio" : "Unknown";
            return "CodecEntry[Type:"+typeString+",Name:"+name+",Description:"+description+",Data:...]";
        }
    }
}

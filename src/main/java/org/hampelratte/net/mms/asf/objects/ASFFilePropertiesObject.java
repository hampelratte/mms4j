package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import org.hampelratte.net.mms.asf.UnknownAsfObjectException;
import org.hampelratte.net.mms.asf.io.ASFInputStream;

import unclealex.mms.GUID;

public class ASFFilePropertiesObject extends ASFHeaderObject {

    private GUID fileId;

    private long size;

    private Calendar creationDate;
    
    private long dataPacketCount;

    private long playDuration;

    private long sendDuration;

    private long preRoll;

    private long flags;

    private long minDataPacketSize;

    private long maxDataPacketSize;

    private long maxBitrate;

    @Override
    public void setData(byte[] data) throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        fileId = asfin.readGUID();
        size = asfin.readLELong();
        
        creationDate = Calendar.getInstance();
        creationDate.setTimeInMillis(asfin.readLELong());
        
        dataPacketCount = asfin.readLELong();
        playDuration = asfin.readLELong();
        sendDuration = asfin.readLELong();
        preRoll = asfin.readLELong();
        flags = asfin.readLEInt();
        minDataPacketSize = asfin.readLEInt();
        maxDataPacketSize = asfin.readLEInt();
        maxBitrate = asfin.readLEInt();
    }

    public GUID getFileId() {
        return fileId;
    }

    public void setFileId(GUID fileId) {
        this.fileId = fileId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public long getDataPacketCount() {
        return dataPacketCount;
    }

    public void setDataPacketCount(long dataPacketCount) {
        this.dataPacketCount = dataPacketCount;
    }

    public long getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(long playDuration) {
        this.playDuration = playDuration;
    }

    public long getSendDuration() {
        return sendDuration;
    }

    public void setSendDuration(long sendDuration) {
        this.sendDuration = sendDuration;
    }

    public long getPreRoll() {
        return preRoll;
    }

    public void setPreRoll(long preRoll) {
        this.preRoll = preRoll;
    }

    public long getFlags() {
        return flags;
    }

    public void setFlags(long flags) {
        this.flags = flags;
    }

    public long getMinDataPacketSize() {
        return minDataPacketSize;
    }

    public void setMinDataPacketSize(long minDataPacketSize) {
        this.minDataPacketSize = minDataPacketSize;
    }

    public long getMaxDataPacketSize() {
        return maxDataPacketSize;
    }

    public void setMaxDataPacketSize(long maxDataPacketSize) {
        this.maxDataPacketSize = maxDataPacketSize;
    }

    public long getMaxBitrate() {
        return maxBitrate;
    }

    public void setMaxBitrate(long maxBitrate) {
        this.maxBitrate = maxBitrate;
    }
    
    public boolean isSeekable() {
        return (getFlags() & 0x02) == 0x02;
    }
    
    public void setSeekable(boolean seekable) {
        if(seekable) {
            setFlags(getFlags() | 0x02);
        } else {
            setFlags(getFlags() & 0xFFFFFFFD);
        }
    }
    
    public boolean isBroadcast() {
        return (getFlags() & 0x01) == 0x01;
    }
    
    public void setBroadcast(boolean broadcast) {
        if(broadcast) {
            setFlags(getFlags() | 0x01);
        } else {
            setFlags(getFlags() & 0xFFFFFFFE);
        }
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" [ID:");
        sb.append(getFileId());
        sb.append(",size:");
        sb.append(getSize() / 1024 / 1024);
        sb.append(" mb");
        if(getCreationDate() != null) {
            sb.append(",creation date:");
            sb.append(DateFormat.getDateTimeInstance().format(getCreationDate().getTime()));
        }
        sb.append(",packet count:");
        sb.append(getDataPacketCount());
        sb.append(",play duration:");
        sb.append(getPlayDuration());
        sb.append(",send duration:");
        sb.append(getSendDuration());
        sb.append(",preroll:");
        sb.append(getPreRoll());
        sb.append(",seekable:");
        sb.append(isSeekable());
        sb.append(",broadcast:");
        sb.append(isBroadcast());
        sb.append(",min packet size:");
        sb.append(getMinDataPacketSize());
        sb.append(",max packet size:");
        sb.append(getMaxDataPacketSize());
        sb.append(",max bitrate:");
        sb.append(getMaxBitrate());
        sb.append(']');
        return sb.toString();
    }
    
}

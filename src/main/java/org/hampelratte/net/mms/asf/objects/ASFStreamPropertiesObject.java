package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hampelratte.net.mms.asf.io.ASFInputStream;

import unclealex.mms.GUID;

public class ASFStreamPropertiesObject extends ASFHeaderObject {

    private GUID streamType;

    private GUID errorCorrection;

    private long timeOffset;

    private int dataLength;

    private int errorCorrectionDataLength;

    private short streamNumber;

    private boolean encrypted;

    private byte[] errorCorrectionData;

    @Override
    public void setData(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
        super.setData(data);
        
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        streamType = asfin.readGUID();
        errorCorrection = asfin.readGUID();
        timeOffset = asfin.readLELong();
        dataLength = (int) asfin.readLEInt();
        errorCorrectionDataLength = (int) asfin.readLEInt();
        int flags = asfin.readLEShort();
        streamNumber = (short) (flags & 0x7F);
        encrypted = (flags & 0x8000) == 1;
        asfin.skip(4);
        
        byte[] d = new byte[dataLength];
        asfin.read(d);
        super.setData(d);
        
        errorCorrectionData = new byte[errorCorrectionDataLength];
        asfin.read(errorCorrectionData);
    }

    public GUID getStreamType() {
        return streamType;
    }

    public void setStreamType(GUID streamType) {
        this.streamType = streamType;
    }

    public GUID getErrorCorrection() {
        return errorCorrection;
    }

    public void setErrorCorrection(GUID errorCorrection) {
        this.errorCorrection = errorCorrection;
    }

    public long getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getErrorCorrectionDataLength() {
        return errorCorrectionDataLength;
    }

    public void setErrorCorrectionDataLength(int errorCorrectionDataLength) {
        this.errorCorrectionDataLength = errorCorrectionDataLength;
    }

    public short getStreamNumber() {
        return streamNumber;
    }

    public void setStreamNumber(short streamNumber) {
        this.streamNumber = streamNumber;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public byte[] getErrorCorrectionData() {
        return errorCorrectionData;
    }

    public void setErrorCorrectionData(byte[] errorCorrectionData) {
        this.errorCorrectionData = errorCorrectionData;
    }
    
    @Override
    public String toString() {
        String type = guidToStreamType.get(streamType);
        if(type == null && streamType != null) type = streamType.toString();
        
        String error = guidToErrorCorrection.get(errorCorrection);
        if(error == null && errorCorrection != null) error = errorCorrection.toString();
        
        return super.toString() + " [Type:"+type+",Error Correction:"+error+",Time Offset:"+timeOffset+",Stream Number:"+streamNumber+",Encrypted:"+encrypted+"]";
    }

    public static final Map<GUID, String> guidToStreamType = new HashMap<GUID, String>();
    static {
        guidToStreamType.put(new GUID("F8699E40-5B4D-11CF-A8FD-00805F5C442B"), "ASF_Audio_Media");
        guidToStreamType.put(new GUID("BC19EFC0-5B4D-11CF-A8FD-00805F5C442B"), "ASF_Video_Media");
        guidToStreamType.put(new GUID("59DACFC0-59E6-11D0-A3AC-00A0C90348F6"), "ASF_Command_Media");
        guidToStreamType.put(new GUID("B61BE100-5B4E-11CF-A8FD-00805F5C442B"), "ASF_JFIF_Media");
        guidToStreamType.put(new GUID("35907DE0-E415-11CF-A917-00805F5C442B"), "ASF_Degradable_JPEG_Media");
        guidToStreamType.put(new GUID("91BD222C-F21C-497A-8B6D-5AA86BFC0185"), "ASF_File_Transfer_Media");
        guidToStreamType.put(new GUID("3AFB65E2-47EF-40F2-AC2C-70A90D71D343"), "ASF_Binary_Media");
    }

    public static final Map<GUID, String> guidToErrorCorrection = new HashMap<GUID, String>();
    static {
        guidToErrorCorrection.put(new GUID("20FB5700-5B55-11CF-A8FD-00805F5C442B"), "ASF_No_Error_Correction");
        guidToErrorCorrection.put(new GUID("BFC3CD50-618F-11CF-8BB2-00AA00B4E220"), "ASF_Audio_Spread");
    }
}

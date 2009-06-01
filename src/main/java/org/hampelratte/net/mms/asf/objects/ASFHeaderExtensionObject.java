package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.hampelratte.net.mms.asf.UnknownAsfObjectException;
import org.hampelratte.net.mms.asf.io.ASFInputStream;
import org.hampelratte.net.mms.io.util.StringUtils;

import unclealex.mms.GUID;

// TODO parse as described in chapter 4 of the asf spec
public class ASFHeaderExtensionObject extends ASFHeaderObject {

    private GUID reserved1;

    private int reserved2;

    private long dataSize;

    @Override
    public void setData(byte[] data) throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        reserved1 = asfin.readGUID();
        reserved2 = asfin.readLEShort();

        dataSize = asfin.readLEInt();
        byte[] d = new byte[(int) dataSize];
        asfin.read(d);
        super.setData(d);
    }

    public GUID getReserved1() {
        return reserved1;
    }

    public void setReserved1(GUID reserved1) {
        this.reserved1 = reserved1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    @Override
    public String toString() {
        String s = super.toString() + " [Reserved GUID:" + getReserved1() + ",Reserved Short:" + getReserved2() + ",Data Size:" + getDataSize();
        if (getData() != null) {
            s += ",Data:\n  " + StringUtils.toHeadHexString(getData());
        }
        s += "]";
        return s;
    }
}

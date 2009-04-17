package org.hampelratte.net.mms.asf;

import java.util.HashMap;

import org.hampelratte.net.mms.asf.objects.ASFContentDescriptionObject;
import org.hampelratte.net.mms.asf.objects.ASFFilePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;

import unclealex.mms.GUID;

public class ASFObjectFactory {
    private static HashMap<GUID, Class<?>> guid2Class = new HashMap<GUID, Class<?>>();
    static {
        guid2Class.put(new GUID("75B22630-668E-11CF-A6D9-00AA0062CE6C"), ASFToplevelHeader.class);
        guid2Class.put(new GUID("75B22633-668E-11CF-A6D9-00AA0062CE6C"), ASFContentDescriptionObject.class);
        guid2Class.put(new GUID("8CABDCA1-A947-11CF-8EE4-00C00C205365"), ASFFilePropertiesObject.class);
        // TODO implement the other header objects ?!?
        //guid2Class.put(new GUID("5FBF03B5-A92E-11CF-8EE3-00C00C205365"), ASFHeaderExtensionObject.class);
    }
    
    public static ASFObject createObjectFromGUID(GUID guid) throws UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        Class<?> objectClass = guid2Class.get(guid);
        if(objectClass != null) {
            ASFObject asfo = (ASFObject) objectClass.newInstance();
            asfo.setGuid(guid);
            return asfo;
        } else {
            throw new UnknownAsfObjectException(guid);
        }
    }
}

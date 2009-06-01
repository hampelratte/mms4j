package org.hampelratte.net.mms.asf;

import java.util.HashMap;

import org.hampelratte.net.mms.asf.objects.ASFCodecListObject;
import org.hampelratte.net.mms.asf.objects.ASFContentDescriptionObject;
import org.hampelratte.net.mms.asf.objects.ASFExtendedContentDescriptionObject;
import org.hampelratte.net.mms.asf.objects.ASFFilePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFHeaderExtensionObject;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.asf.objects.ASFStreamBitratePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFStreamPropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unclealex.mms.GUID;

public class ASFObjectFactory {
    
    private static transient Logger logger = LoggerFactory.getLogger(ASFObjectFactory.class);
    
    private static HashMap<GUID, Class<?>> guid2Class = new HashMap<GUID, Class<?>>();
    static {
        guid2Class.put(new GUID("75B22630-668E-11CF-A6D9-00AA0062CE6C"), ASFToplevelHeader.class);
        guid2Class.put(new GUID("75B22633-668E-11CF-A6D9-00AA0062CE6C"), ASFContentDescriptionObject.class);
        guid2Class.put(new GUID("8CABDCA1-A947-11CF-8EE4-00C00C205365"), ASFFilePropertiesObject.class);
        
        // TODO implement these header objects ?!? at the moment they are only skeletons without functionality
        guid2Class.put(new GUID("5FBF03B5-A92E-11CF-8EE3-00C00C205365"), ASFHeaderExtensionObject.class);
        guid2Class.put(new GUID("D2D0A440-E307-11D2-97F0-00A0C95EA850"), ASFExtendedContentDescriptionObject.class);
        guid2Class.put(new GUID("86D15240-311D-11D0-A3A4-00A0C90348F6"), ASFCodecListObject.class);
        guid2Class.put(new GUID("B7DC0791-A9B7-11CF-8EE6-00C00C205365"), ASFStreamPropertiesObject.class);
        guid2Class.put(new GUID("7BF875CE-468D-11D1-8D82-006097C9A2B2"), ASFStreamBitratePropertiesObject.class);
    }
    
    public static ASFObject createObjectFromGUID(GUID guid) throws UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        Class<?> objectClass = guid2Class.get(guid);
        if(objectClass != null) {
            ASFObject asfo = (ASFObject) objectClass.newInstance();
            asfo.setGuid(guid);
            logger.debug("Created ASF object of type {}", asfo.getClass().getSimpleName());
            return asfo;
        } else {
            throw new UnknownAsfObjectException(guid);
        }
    }
}

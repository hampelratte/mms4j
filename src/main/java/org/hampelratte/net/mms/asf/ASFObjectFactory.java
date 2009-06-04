package org.hampelratte.net.mms.asf;

import java.util.HashMap;

import org.hampelratte.net.mms.asf.objects.ASFAdvancedMutualExclusionObject;
import org.hampelratte.net.mms.asf.objects.ASFCodecListObject;
import org.hampelratte.net.mms.asf.objects.ASFContentDescriptionObject;
import org.hampelratte.net.mms.asf.objects.ASFExtendedContentDescriptionObject;
import org.hampelratte.net.mms.asf.objects.ASFExtendedStreamPropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFFilePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFHeaderExtensionObject;
import org.hampelratte.net.mms.asf.objects.ASFLanguageListObject;
import org.hampelratte.net.mms.asf.objects.ASFMetadataObject;
import org.hampelratte.net.mms.asf.objects.ASFObject;
import org.hampelratte.net.mms.asf.objects.ASFStreamBitratePropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFStreamPriorizationObject;
import org.hampelratte.net.mms.asf.objects.ASFStreamPropertiesObject;
import org.hampelratte.net.mms.asf.objects.ASFToplevelHeader;
import org.hampelratte.net.mms.asf.objects.ASFUnknownObject;

import unclealex.mms.GUID;

public class ASFObjectFactory {
    
    private static HashMap<GUID, Class<?>> guid2Class = new HashMap<GUID, Class<?>>();
    static {
        guid2Class.put(new GUID("75B22630-668E-11CF-A6D9-00AA0062CE6C"), ASFToplevelHeader.class);
        guid2Class.put(new GUID("75B22633-668E-11CF-A6D9-00AA0062CE6C"), ASFContentDescriptionObject.class);
        guid2Class.put(new GUID("8CABDCA1-A947-11CF-8EE4-00C00C205365"), ASFFilePropertiesObject.class);
        guid2Class.put(new GUID("5FBF03B5-A92E-11CF-8EE3-00C00C205365"), ASFHeaderExtensionObject.class);
        guid2Class.put(new GUID("D2D0A440-E307-11D2-97F0-00A0C95EA850"), ASFExtendedContentDescriptionObject.class);
        guid2Class.put(new GUID("86D15240-311D-11D0-A3A4-00A0C90348F6"), ASFCodecListObject.class);
        guid2Class.put(new GUID("B7DC0791-A9B7-11CF-8EE6-00C00C205365"), ASFStreamPropertiesObject.class);
        guid2Class.put(new GUID("7BF875CE-468D-11D1-8D82-006097C9A2B2"), ASFStreamBitratePropertiesObject.class);

        // TODO implement these header objects ?!? at the moment they are only skeletons without functionality
        guid2Class.put(new GUID("14E6A5CB-C672-4332-8399-A96952065B5A"), ASFExtendedStreamPropertiesObject.class);
        guid2Class.put(new GUID("7C4346A9-EFE0-4BFC-B229-393EDE415C85"), ASFLanguageListObject.class);
        guid2Class.put(new GUID("C5F8CBEA-5BAF-4877-8467-AA8C44FA4CCA"), ASFMetadataObject.class);
        guid2Class.put(new GUID("A08649CF-4775-4670-8A16-6E35357566CD"), ASFAdvancedMutualExclusionObject.class);
        guid2Class.put(new GUID("D4FED15B-88D3-454F-81F0-ED5C45999E24"), ASFStreamPriorizationObject.class);
    }
    
    public static ASFObject createObjectFromGUID(GUID guid) throws InstantiationException, IllegalAccessException {
        Class<?> objectClass = guid2Class.get(guid);
        ASFObject asfo;
        if(objectClass != null) {
            asfo = (ASFObject) objectClass.newInstance();
        } else {
            asfo = new ASFUnknownObject();
        }
        
        asfo.setGuid(guid);
        return asfo;
    }
}

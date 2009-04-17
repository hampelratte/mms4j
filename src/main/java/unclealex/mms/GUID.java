/* GUID.java
 * Part of the Java MMS Downloader - distributed under the GNU Public License.
 * See www.gnu.org for details.
 */
package unclealex.mms;


/** This class represents 128 bit integers.
 * @author Alex Jones
 */
public class GUID implements java.io.Serializable {
    private static int[] byteOrder = {  3,  2,  1,  0,  5,  4,  7,  6,
                                        8,  9, 10, 11, 12, 13, 14, 15 };
    private static String hex = "0123456789ABCDEF";
    private String ascii;
    private byte[] data = new byte[16];
    
    public GUID() {
    }
    
    public GUID(byte[] data)
    {
        if (data.length != 16) throw new IllegalArgumentException("Invalid GUID length.");
        for (int i = 0; i < 16; i++) this.data[i] = data[i];
        createAscii();
    }
    
    /** Create a GUID from a string.
     * @param guid A string of the form XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX.
     */    
    public GUID(String guid)
    {
        String g = guid.toUpperCase();
        if (!g.matches("[0-9 A-F]{8}-[0-9 A-F]{4}-[0-9 A-F]{4}-[0-9 A-F]{4}-[0-9 A-F]{12}"))
            throw new IllegalArgumentException(guid + " is not a valid GUID.");
        
        g = g.substring(0, 8) + g.substring(9, 13) + g.substring(14, 18) +
            g.substring(19, 23) + g.substring(24, 36);
        
        byte[] data = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            data[i] = (byte) ((hex.indexOf(g.substring(i * 2, i * 2 + 1)) << 4) +
                              hex.indexOf(g.substring(i * 2 + 1, i * 2 + 2)));
        }
        setData(data);
    }
    
    private void setData(byte[] newData)
    {
        for (int i = 0; i < 16; i++)
        {
            data[byteOrder[i]] = newData[i];
        }
        createAscii();
    }
    
    /** Convert the GUID to a string of length 36.
     * @return A string of the form XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX.
     */    
    public String toString()
    {
        return ascii;
    }

    private void createAscii()
    {
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < 16; i++)
        {
            byte b = data[byteOrder[i]];
            int msn = (b & 0xF0) >>> 4;
            int lsn = b & 0x0F;
            sb.append(hex.substring(msn, msn+1));
            sb.append(hex.substring(lsn, lsn+1));
        }
        String guid = sb.toString();
        ascii = guid.substring(0, 8) + "-" + guid.substring(8, 12) + "-" + guid.substring(12, 16) +
                "-" + guid.substring(16, 20) + "-" + guid.substring(20);
    }
    
    public int compareTo(Object o)
    {
        GUID other = (GUID) o;
        return ascii.compareTo(other.ascii);
    }
    
    public boolean equals(Object o) { return compareTo(o)==0; }
    
    public int hashCode() { return ascii.hashCode(); }
    
    public byte[] toByteArray() { return data; }
    
    public static void main(String[] args)
    {
        String s = "75B22630-668E-11CF-A6D9-00AA0062CE6C";
        GUID g = new GUID(s);
        
        System.out.println(s);
        System.out.println(g.toString());
        for (int i = 0; i < 16; i++)
        {
            byte b = g.toByteArray()[i];
            int ub = b<0?b+256:b;
            System.out.print(new java.math.BigInteger(""+ub).toString(16));
            if (i % 2 != 0) System.out.print(" ");
        }
        System.out.println();
        System.out.println(new GUID(g.toByteArray()).toString());
    }
}

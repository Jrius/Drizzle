/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class Version extends tdsobj
{
    public int version;
    
    private Version(){}
    
    public static Version create(int version)
    {
        Version result = new Version();
        result.version = version;
        return result;
    }
    public Typeid type(){return Typeid.version;}
    public void innercompile(IBytedeque c)
    {
        c.writeInt(version);
    }
}

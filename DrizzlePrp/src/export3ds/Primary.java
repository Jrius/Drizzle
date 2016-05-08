/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class Primary extends tdsobj
{
    public Version version;
    public Meshdata meshdata;
    
    private Primary(){}
    public static Primary createNull()
    {
        Primary result = new Primary();
        result.version = Version.create(3); //this is the Version Blender exports as.
        result.meshdata = Meshdata.createNull();
        return result;
    }
    public Typeid type(){return Typeid.primary;}
    public void innercompile(IBytedeque c)
    {
        version.compile(c);
        meshdata.compile(c);
    }
}

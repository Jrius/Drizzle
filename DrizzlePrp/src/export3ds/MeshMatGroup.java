/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class MeshMatGroup extends tdsobj
{
    public Ntstring name;
    public short facecount;
    public short[] faces;
    
    private MeshMatGroup(){}
    
    public static MeshMatGroup create(String name, short[] facesToApplyTo)
    {
        MeshMatGroup result = new MeshMatGroup();
        result.name = Ntstring.createFromString(name);
        if(facesToApplyTo.length>30000)
        {
            throw new uncaughtexception("MeshMatGroup: too many faces, todo: set this number a little higher.");
        }
        result.facecount = (short)facesToApplyTo.length;
        result.faces = facesToApplyTo;
        return result;
    }
    public Typeid type(){return Typeid.meshmatgroup;}
    public void innercompile(IBytedeque c)
    {
        name.compile(c);
        c.writeShort(facecount);
        c.writeShorts(faces);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;
import java.util.Vector;

public class FaceArray extends tdsobj
{
    public short facecount;
    public tdsface[] faces;
    //public MeshMatGroup mat;
    public Vector<MeshMatGroup> mats = new Vector<MeshMatGroup>();
    
    private FaceArray(){}
    
    public static FaceArray createNull()
    {
        FaceArray result = new FaceArray();
        return result;
    }
    public static FaceArray create(ShortTriplet[] faces, String matname)
    {
        FaceArray result = new FaceArray();
        result.facecount = check.intToShort(faces.length);
        result.faces = new tdsface[result.facecount];
        for(int i=0;i<faces.length;i++)
        {
            short v1 = faces[i].p;
            short v2 = faces[i].q;
            short v3 = faces[i].r;
            short flags = 0;
            result.faces[i] = new tdsface(v1,v2,v3,flags);
        }
        short[] facesToApplyTo = new short[result.facecount];
        for(int i=0;i<result.facecount;i++)
        {
            facesToApplyTo[i] = (short)i;
        }
        result.mats.add(MeshMatGroup.create(matname, facesToApplyTo));
        return result;
    }
    public Typeid type(){return Typeid.facearray;}
    public void innercompile(IBytedeque c)
    {
        c.writeShort(facecount);
        c.writeArray(faces);
        //mat.compile(c);
        c.writeVector(mats);
    }
    
    public static class tdsface implements ICompilable
    {
        short v1;
        short v2;
        short v3;
        short flags;
        
        public tdsface(short v1, short v2, short v3, short flags)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.flags = flags;
        }
        public void compile(IBytedeque c)
        {
            c.writeShort(v1);
            c.writeShort(v2);
            c.writeShort(v3);
            c.writeShort(flags);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public enum Typeid
{
   
    unknown,
    primary,
    version,
    meshdata,
    material,
    matname,
    matambient,
    matdiffuse,
    matspecular,
    color24,
    namedobj,
    namedtriobj,
    pointarray,
    facearray,
    meshmatgroup,
    texturemap,
    texturefilename,
    uvcoords,
    uoffset,
    voffset,
    uscale,
    vscale,
    ;
    public static pair[] pairs = {
        p((short)0xffff, unknown),
        p((short)0x4d4d, primary),
        p((short)0x0002, version),
        p((short)0x3d3d, meshdata),
        p((short)0xafff, material),
        p((short)0xa000, matname),
        p((short)0xa010, matambient),
        p((short)0xa020, matdiffuse),
        p((short)0xa030, matspecular),
        p((short)0x0011, color24),
        p((short)0x4000, namedobj),
        p((short)0x4100, namedtriobj),
        p((short)0x4110, pointarray),
        p((short)0x4120, facearray),
        p((short)0x4130, meshmatgroup),
        p((short)0x4140, uvcoords),
        p((short)0xa200, texturemap),
        p((short)0xa300, texturefilename),
        p((short)0xa358, uoffset),
        p((short)0xa35a, voffset),
        p((short)0xa356, uscale),
        p((short)0xa354, vscale),
    };
    
    public static void verify()
    {
        for(pair p1: pairs)
        {
            boolean foundint = false;
            boolean foundenum = false;
            for(pair p2: pairs)
            {
                if(p1.i==p2.i)
                {
                    if(!foundint) foundint = true;
                    else m.err("3ds typeid programming error.");
                }
                if(p1.type==p2.type)
                {
                    if(!foundenum) foundenum = true;
                    else m.err("3ds typeid programming error.");
                }
            }
        }
    }
    public static Typeid read(IBytestream c)
    {
        short data = c.readShort();
        Typeid result = getType(data);
        if(result==Typeid.unknown)
        {
            int error=0;
        }
        return result;
    }
    public static boolean has(short i)
    {
        for(pair p: pairs)
        {
            if(p.i==i) return true;
        }
        return false;
    }
    public static pair p(short i, Typeid type)
    {
        return new pair(i,type);
    }
    public static class pair
    {
        short i;
        Typeid type;
        
        public pair(short i, Typeid type)
        {
            this.i = i;
            this.type = type;
        }
    }
    
    public short getNum()
    {
        for(pair p: pairs)
        {
            if(p.type==this)
            {
                return p.i;
            }
        }
        return unknown.getNum();
    }
    public static short getNum(Typeid type)
    {
        return type.getNum();
    }
    public static Typeid getType(short i)
    {
        for(pair p: pairs)
        {
            if(p.i==i)
            {
                return p.type;
            }
        }
        m.warn("Unhandled type in Typeid.");
        return unknown;
    }
    public void compile(IBytedeque c)
    {
        c.writeShort(this.getNum());
    }
}

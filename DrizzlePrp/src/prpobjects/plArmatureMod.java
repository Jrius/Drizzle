/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plArmatureMod extends uruobj
{
    plAGMasterMod parent;
    Uruobjectref ref;
    Urustring s1;
    int count;
    PrpTaggedObject[] brains;
    byte b1;
    Uruobjectref xref2;
    int u1;
    byte b2;
    Uruobjectref xref3;
    //offset 48???
    Vertex mins;
    Vertex maxs;
    Flt f1;
    Flt f2;
    
    PlArmatureModBase xbase;
    int xu1;
    byte xb1;
    Uruobjectref xref;
    
    public plArmatureMod(context c) throws readexception
    {
        if(c.readversion==4)
        {
            xbase = new PlArmatureModBase(c);
            s1 = new Urustring(c);
            xu1 = c.readInt();
            xb1 = c.readByte();
            if(xb1!=0)
            {
                xref = new Uruobjectref(c);
            }
            throw new shared.readwarningexception("PlArmatureMod: can read okay, but throwing exception to ignore, since I haven't implemented conversion.");
        }
        else if(c.readversion==3)
        {
            parent = new plAGMasterMod(c);
            //c.readInt();
            ref = new Uruobjectref(c);
            s1 = new Urustring(c);
            count = c.readInt();
            brains = c.readArray(PrpTaggedObject.class, count);
            b1 = c.readByte();
            if(b1!=0)
            {
                xref2 = new Uruobjectref(c);
            }
            u1 = c.readInt();
            b2 = c.readByte();
            if(b2!=0)
            {
                xref3 = new Uruobjectref(c);
            }
            mins = new Vertex(c);
            maxs = new Vertex(c);
            m.msg("v1="+mins.toString()+"  v2="+maxs.toString());
            f1 = new Flt(c);
            f2 = new Flt(c);
        }
        else if(c.readversion==6)
        {
            parent = new plAGMasterMod(c);
            //c.readInt();
            ref = new Uruobjectref(c);
            s1 = new Urustring(c);
            count = c.readInt();
            brains = c.readArray(PrpTaggedObject.class, count);
            b1 = c.readByte();
            if(b1!=0)
            {
                xref2 = new Uruobjectref(c);
            }
            u1 = c.readInt();
            b2 = c.readByte();
            if(b2!=0)
            {
                xref3 = new Uruobjectref(c);
            }
            //v1 = new Vertex(c);
            //v2 = new Vertex(c);
            mins = Vertex.createFromFloats(0, 0, 0);
            maxs = Vertex.createFromFloats(0, 0, 0);
            //v1 = Vertex.createFromFloats(0, 0, 2);
            //v2 = Vertex.createFromFloats(0, 0, 4);
            //v1 = Vertex.createFromFlts(Flt.createFromData(0x0), Flt.createFromData(0xb3bbc0ac), Flt.createFromData(0x40000262));
            //v2 = Vertex.createFromFlts(Flt.createFromData(0x0), Flt.createFromData(0xb43a3090), Flt.createFromData(0x407de32c));
            m.warn("Using identity vectors in PlArmatureMod.");
            f1 = new Flt(c);
            f2 = new Flt(c);
            Urustring s1 = new Urustring(c); //kg,Male,Female,Quab
            Urustring s2 = new Urustring(c); //CustomAvatars
            Urustring s3 = new Urustring(c); //Audio
            int dummy=0;
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        ref.compile(c);
        s1.compile(c);
        c.writeInt(count);
        c.writeArray2(brains);
        c.writeByte(b1);
        if(b1!=0)
        {
            xref2.compile(c);
        }
        c.writeInt(u1);
        c.writeByte(b2);
        if(b2!=0)
        {
            xref3.compile(c);
        }
        mins.compile(c);
        maxs.compile(c);
        f1.compile(c);
        f2.compile(c);
    }
    
    public static class PlArmatureModBase extends uruobj
    {
        plAGMasterMod parent;
        int count1;
        subPlArmatureModBase[] subs;
          //uruobjectref
          //int count
          //uruobjectref[count]
        int count2;
          //??? * count2
        PrpTaggedObject[] objects;
        Uruobjectref ref1;
        
        public PlArmatureModBase(context c) throws readexception
        {
            parent = new plAGMasterMod(c);
            count1 = c.readInt();
            subs = c.readArray(subPlArmatureModBase.class, count1);
            count2 = c.readInt();
                //Urustring str = new Urustring(c);
            objects = c.readArray(PrpTaggedObject.class, count2);
            ref1 = new Uruobjectref(c);
        }
        
        public static class subPlArmatureModBase extends uruobj
        {
            Uruobjectref ref;
            int count;
            Uruobjectref[] refs;
            
            public subPlArmatureModBase(context c) throws readexception
            {
                ref = new Uruobjectref(c);
                count = c.readInt();
                refs = c.readArray(Uruobjectref.class, count);
            }
        }
    }
    
    
    
}

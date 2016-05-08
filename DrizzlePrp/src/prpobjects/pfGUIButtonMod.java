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


public class pfGUIButtonMod extends uruobj
{
    pfGUIControlMod parent;
    int refcount;
    Uruobjectref[] refs;
    Urustring str;
    int refcount2;
    Uruobjectref[] refs2;
    Urustring str2;
    int u1;
    byte b1;
    int xu1;
    int xu2;
    int xu3;
    Uruobjectref xref3;
    
    public pfGUIButtonMod(context c) throws readexception
    {
        parent = new pfGUIControlMod(c);
        refcount = c.readInt();
        refs = c.readArray(Uruobjectref.class, refcount); //really uruobjectrefs?
        str = new Urustring(c);
        refcount2 = c.readInt();
        refs2 = c.readArray(Uruobjectref.class, refcount2); //really uruobjectrefs?
        str2 = new Urustring(c);
        if(c.readversion==3||c.readversion==6)
        {
            //b1 is always 0
            //u1: 1(x1) 2(x4) 0(x~230)
            //so, we could just write 0,0
            u1 = c.readInt();
            //b1 = c.readByte(); //no, this is an Uruobjectref
            xref3 = new Uruobjectref(c);
            //m.msg("pfguibuttonmod: delme: "+Integer.toString(u1)+":"+Byte.toString(b1));
        }
        else if(c.readversion==4) //does 6 go here? no, it goes in the above one.
        {
            //these have no clear relation to the pots flags above.
            //xu1 = c.readInt(); //maybe this is u1?
            u1 = c.readInt();
            xu2 = c.readInt();
            xu3 = c.readInt();
            xref3 = new Uruobjectref(c);
            //m.msg("pfguibuttonmod: delme: "+Integer.toString(xu1)+":"+Integer.toString(xu2)+":"+Integer.toString(xu3)+":"+xref3.toString());
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(refcount);
        c.writeArray2(refs);
        str.compile(c);
        c.writeInt(refcount2);
        c.writeArray2(refs2);
        str2.compile(c);

        //these next 2 lines are justified by the fact that they are usually 0, and I couldn't see a relation between them and the new flags.
        //c.writeInt(0);
        c.writeInt(u1);
        //c.writeByte((byte)0);
        xref3.compile(c);
    }
    public static class PfGUIDialogMod extends uruobj
    {
        plSingleModifier parent;
        Uruobjectref ref;
        byte[] bs1;
        int refcount;
        Uruobjectref[] refs;
        int u1;
        Uruobjectref ref2;
        int u2;
        //wacky thing
        pfGUIControlMod.pfGUIColorScheme wha1;
        Uruobjectref ref3;
        
        public PfGUIDialogMod(context c) throws readexception
        {
            parent = new plSingleModifier(c);
            ref = new Uruobjectref(c);
            bs1 = c.readBytes(128);
            refcount = c.readInt();
            refs = c.readArray(Uruobjectref.class, refcount);
            u1 = c.readInt();
            ref2 = new Uruobjectref(c);
            u2 = c.readInt();
            wha1 = new pfGUIControlMod.pfGUIColorScheme(c);
            ref3 = new Uruobjectref(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            ref.compile(c);
            c.writeBytes(bs1);
            c.writeInt(refcount);
            c.writeArray2(refs);
            c.writeInt(u1);
            ref2.compile(c);
            c.writeInt(u2);
            wha1.compile(c);
            ref3.compile(c);
        }
            
    }
    
    public static class PfGUIDragBarCtrl extends uruobj
    {
        pfGUIControlMod parent;
        
        public PfGUIDragBarCtrl(context c) throws readexception
        {
            parent = new pfGUIControlMod(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
        }
    }
    
    
    public static class PfGUIKnobCtrl extends uruobj
    {
        PfGUIValueCtrl parent;
        int count;
        Uruobjectref[] refs;
        Urustring str;
        Vertex v1;
        Vertex v2;
        
        public PfGUIKnobCtrl(context c) throws readexception
        {
            parent = new PfGUIValueCtrl(c);
            count = c.readInt();
            refs = c.readArray(Uruobjectref.class, count);
            str = new Urustring(c);
            v1 = new Vertex(c);
            v2 = new Vertex(c);
            if(c.readversion==4)
            {
                int u1 = c.readInt();
                int u2 = c.readInt();
                int u3 = c.readInt();
                int u4 = c.readInt();
                Flt f1 = new Flt(c);
                Flt f2 = new Flt(c);
                Flt f3 = new Flt(c);
                Flt f4 = new Flt(c);
            }
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(count);
            c.writeArray2(refs);
            str.compile(c);
            v1.compile(c);
            v2.compile(c);
        }
    }
    
    public static class PfGUIValueCtrl extends uruobj
    {
        pfGUIControlMod parent;
        Flt f1;
        Flt f2;
        Flt f3;
        
        public PfGUIValueCtrl(context c) throws readexception
        {
            parent = new pfGUIControlMod(c);
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            f1.compile(c);
            f2.compile(c);
            f3.compile(c);
        }
    }
    
}

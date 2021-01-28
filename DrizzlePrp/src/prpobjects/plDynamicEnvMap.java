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


public class plDynamicEnvMap extends uruobj
{
    ithinkthisisPlCubicRenderTarget u1;
    public Vertex fPos;
    Flt[] u3; //8
    byte u4;
    int refcount;
    Uruobjectref[] refs;
    int xsubcount;
    Urustring[] xsubs;
    Uruobjectref xref2;
    
    public plDynamicEnvMap(context c) throws readexception
    {
        u1 = new ithinkthisisPlCubicRenderTarget(c);
        fPos = new Vertex(c);
        u3 = c.readArray(Flt.class, 8);
        u4 = c.readByte();
        refcount = c.readInt();
        refs = c.readArray(Uruobjectref.class, refcount);
        if(c.readversion==6||c.readversion==4||c.readversion==7)
        {
            xsubcount = c.readInt();
            xsubs = c.readArray(Urustring.class, xsubcount);
            xref2 = new Uruobjectref(c);
        }
    }
    
    public void compile(Bytedeque c)
    {
        u1.compile(c);
        fPos.compile(c);
        c.writeArray(u3);
        c.writeByte(u4);
        c.writeInt(refcount);
        c.writeArray2(refs);
        //skip this next block, since it's moul-only.
        /*if(c.readversion==6)
        {
            xsubcount = c.readInt();
            xsubs = c.readVector(Urustring.class, xsubcount);
            xref2 = new Uruobjectref(c);
        }*/
    }
    public static class ithinkthisisPlCubicRenderTarget extends uruobj //plCubicRenderTarget probably.
    {
        plRenderTarget parent; //sub_566C80
        plRenderTarget child1;
        plRenderTarget child2;
        plRenderTarget child3;
        plRenderTarget child4;
        plRenderTarget child5;
        plRenderTarget child6;
        
        public ithinkthisisPlCubicRenderTarget(context c) throws readexception
        {
            //sub4e0580 in hexisle
            parent = new plRenderTarget(c);
            if(c.readversion==3||c.readversion==6)
            {
                child1 = new plRenderTarget(c);
                child2 = new plRenderTarget(c);
                child3 = new plRenderTarget(c);
                child4 = new plRenderTarget(c);
                child5 = new plRenderTarget(c);
                child6 = new plRenderTarget(c);
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Uruobjectref ref1 = new Uruobjectref(c);
                child1 = new plRenderTarget(c);
                Uruobjectref ref2 = new Uruobjectref(c);
                child2 = new plRenderTarget(c);
                Uruobjectref ref3 = new Uruobjectref(c);
                child3 = new plRenderTarget(c);
                Uruobjectref ref4 = new Uruobjectref(c);
                child4 = new plRenderTarget(c);
                Uruobjectref ref5 = new Uruobjectref(c);
                child5 = new plRenderTarget(c);
                Uruobjectref ref6 = new Uruobjectref(c);
                child6 = new plRenderTarget(c);
            }
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            child1.compile(c);
            child2.compile(c);
            child3.compile(c);
            child4.compile(c);
            child5.compile(c);
            child6.compile(c);
        }
    }
    
    //sub_566C80
    public static class plRenderTarget extends uruobj
    {
        x0003Bitmap parent;
        short u1;
        short u2;
        byte u3;
        Flt xu4;
        Flt xu5;
        Flt xu6;
        Flt xu7;
        short xu8;
        short xu9;
        short xu10;
        short xu11;
        byte u12;
        byte u13;
        
        public plRenderTarget(context c)
        {
            parent = new x0003Bitmap(c);
            u1 = c.readShort();
            u2 = c.readShort();
            if(c.readversion==7)
            {
                //this extra byte must be here, because the next one is used to determine what data to read next.
                byte u4 = c.readByte();
            }
            u3 = c.readByte();
            if(u3!=0)
            {
                xu4 = new Flt(c);
                xu5 = new Flt(c);
                xu6 = new Flt(c);
                xu7 = new Flt(c);
            }
            else
            {
                xu8 = c.readShort();
                xu9 = c.readShort();
                xu10 = c.readShort();
                xu11 = c.readShort();
            }
            u12 = c.readByte();
            u13 = c.readByte();
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeShort(u1);
            c.writeShort(u2);
            c.writeByte(u3);
            if(u3!=0)
            {
                xu4.compile(c);
                xu5.compile(c);
                xu6.compile(c);
                xu7.compile(c);
            }
            else
            {
                c.writeShort(xu8);
                c.writeShort(xu9);
                c.writeShort(xu10);
                c.writeShort(xu11);
            }
            c.writeByte(u12);
            c.writeByte(u13);
        }
    }
    /*public static class subdynamicenvmap extends uruobj
    {
        Urustring u1;
        
        public subdynamicenvmap(context c)
        {
            m.msg("dynamicenvmap: untested.");
            u1 = new Urustring(c);
        }
    }*/
}

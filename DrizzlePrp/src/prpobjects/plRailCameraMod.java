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

import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;

//incomplete and untested. Do not use!
public class plRailCameraMod extends uruobj
{
    //Objheader xheader;
    
    plMultiModifier parent;
    public PrpTaggedObject u1a;
    //PrpController.plTMController u1b;
    Uruobjectref ref1;
    Uruobjectref ref2;
    int count;
    Uruobjectref[] refs;
    int u2;
    Flt xu3;
    Flt xu4;
    Flt xu5;
    
    public plRailCameraMod(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plMultiModifier(c);//,false);
        u1a = new PrpTaggedObject(c);
        ref1 = new Uruobjectref(c);
        ref2 = new Uruobjectref(c);
        count = c.readInt();
        refs = c.readArray(Uruobjectref.class, count);
        u2 = c.readInt();
        if(((u2)&0x60000)!=0)
        {
            xu3 = new Flt(c);
        }
        if((u2&0x8)!=0)
        {
            xu4 = new Flt(c);
        }
        if((u2&0x20)!=0)
        {
            xu5 = new Flt(c);
        }
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        u1a.compile(c);
        ref1.compile(c);
        ref2.compile(c);
        c.writeInt(count);
        c.writeArray2(refs);
        c.writeInt(u2);
        if(((u2)&0x60000)!=0)
        {
            xu3.compile(c);
        }
        if((u2&0x8)!=0)
        {
            xu4.compile(c);
        }
        if((u2&0x20)!=0)
        {
            xu5.compile(c);
        }
    }
    
    public static class plAnimPath extends uruobj
    {
        int u1;
        PrpTaggedObject xu2a;
        public PrpController.plTMController xu2b;
        public PrpController.uk u3;
        Transmatrix u4;
        Transmatrix u5;
        Flt u6;
        Flt u7;
        
        int readversion;
        public plAnimPath(context c) throws readexception
        {
            readversion = c.readversion;
            
            u1 = c.readInt();
            if(c.readversion==6||c.readversion==4)
            {
                xu2a = new PrpTaggedObject(c);
                e.ensure(xu2a.type==Typeid.plCompoundController); //It shouldn't be anything else.
            }
            else if(c.readversion==3)
            {
                xu2b = new PrpController.plTMController(c);
            }
            u3 = new PrpController.uk(c);
            u4 = new Transmatrix(c);
            u5 = new Transmatrix(c);
            u6 = new Flt(c);
            u7 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(u1);
            if(readversion==6||readversion==4)
            {
                xu2a.compileWithoutTypeid(c); //really a compileSpecial. This compiles it without the typeid, since pots has a naked plTMController. This must be a plCompoundController, which is ensured while reading.
            }
            else if(readversion==3)
            {
                xu2b.compile(c);
            }
            u3.compile(c);
            u4.compile(c);
            u5.compile(c);
            u6.compile(c);
            u7.compile(c);
        }
    }
    
}

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

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;

//I reverse-engineered part of this myself.
//works but I don't know how to convert from moul to pots.
public class plAGMasterMod extends uruobj
{
    //Objheader xheader;
    
    //int u1;
    plSynchedObject parent;
    Bstr u2;
    //short u1;
    public int count;
    public Uruobjectref[] ATCAnim;
    byte u3;
    byte u4;
    Uruobjectref xu5;
    
    int xxrefscount;
    Uruobjectref[] xxrefs;
    
    
    public plAGMasterMod(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        //u1 = c.readInt();
        parent = new plSynchedObject(c);
        if(c.readversion==6||c.readversion==3)
        {
            u2 = new Bstr(c);
        }
        else if(c.readversion==4||c.readversion==7)
        {
            u2 = Bstr.createFromNothing();
        }
        //u1 = c.readShort(); e.ensure(u1==0); //I think this is 0.
        count = c.readInt();
        ATCAnim = c.readArray(Uruobjectref.class, count);
        
        if(c.readversion==6)
        {
            u3 = c.readByte();
            u4 = c.readByte();
            
            if(u4!=0)
            {
                xu5 = new Uruobjectref(c);
            }
        }
        else if(c.readversion==4||c.readversion==7)
        {
            xxrefscount = c.readInt();
            //if(ucount!=0) throw new readexception("PlAGMasterMod: unhandled case.");
            //I think this is an Uruobjectref count.
            xxrefs = c.readArray(Uruobjectref.class, xxrefscount);
            if(xxrefscount!=0) m.warn("PlAGMasterMod: ignoring some refs.");
        }
        //u4 = new Uruobjectref(c);
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        u2.compile(c);
        c.writeInt(count);
        c.writeArray2(ATCAnim);
        //skip the last part, which is moul-only.
    }
}

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

//untested / incomplete
public class plShadowCaster extends uruobj
{
    //Objheader xheader;
    
    plMultiModifier parent;
    byte u1; //ignore this comment! //actually something else, probably Uruobjectref or Transmatrix (offset 56 in the factory)
    Flt f1;
    Flt f2;
    Flt f3;
    
    public plShadowCaster(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plMultiModifier(c);//,false);
        u1 = c.readByte(); //ignore this comment! //e.ensure(u1==0); //doesn't need to be zero, but it's something else otherwise.
        f1 = new Flt(c);
        f2 = new Flt(c);
        f3 = new Flt(c);
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeByte(u1);
        f1.compile(c);
        f2.compile(c);
        f3.compile(c);
    }
}

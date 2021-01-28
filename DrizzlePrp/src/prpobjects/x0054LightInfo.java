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

/**
 *
 * @author user
 */
public class x0054LightInfo extends uruobj
{
    //Objheader xheader;
    plObjInterface parent;
    Flt u1a;
    Flt u1b;
    Flt u1c;
    Flt u2;
    Flt lightcolor1;
    Flt lightcolor2;
    Flt lightcolor3;
    Flt u3a;
    Flt u3b;
    Flt u3c;
    Flt u3d;
    Flt u3e;
    Transmatrix u4;
    Transmatrix u5;
    Transmatrix u6;
    Transmatrix u7;
    Uruobjectref u8;
    Uruobjectref softvolume;
    Uruobjectref scenenode;
    int count;
    Uruobjectref[] visregion;
    
    public x0054LightInfo(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plObjInterface(c);//,false);
        u1a = new Flt(c);
        u1b = new Flt(c);
        u1c = new Flt(c);
        u2 = new Flt(c);
        lightcolor1 = new Flt(c);
        lightcolor2 = new Flt(c);
        lightcolor3 = new Flt(c);
        u3a = new Flt(c);
        u3b = new Flt(c);
        u3c = new Flt(c);
        u3d = new Flt(c);
        u3e = new Flt(c);
        u4 = new Transmatrix(c);
        u5 = new Transmatrix(c);
        u6 = new Transmatrix(c);
        u7 = new Transmatrix(c);
        u8 = new Uruobjectref(c);
        softvolume = new Uruobjectref(c);
        scenenode = new Uruobjectref(c);
        count = data.readInt();
        visregion = c.readArray(Uruobjectref.class, count);
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        u1a.compile(data);
        u1b.compile(data);
        u1c.compile(data);
        u2.compile(data);
        lightcolor1.compile(data);
        lightcolor2.compile(data);
        lightcolor3.compile(data);
        u3a.compile(data);
        u3b.compile(data);
        u3c.compile(data);
        u3d.compile(data);
        u3e.compile(data);
        u4.compile(data);
        u5.compile(data);
        u6.compile(data);
        u7.compile(data);
        u8.compile(data);
        softvolume.compile(data);
        scenenode.compile(data);
        data.writeInt(count);
        data.writeArray2(visregion);
        
    }
}

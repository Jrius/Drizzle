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

//apparently different from pots. Used for the great zero light, for example.
public class plLimitedDirLightInfo extends uruobj
{
    PlLightInfo parent;
    Flt u1;
    Flt u2;
    Flt u3;
    
    
    public plLimitedDirLightInfo(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new PlLightInfo(c);
        u1 = new Flt(c);
        u2 = new Flt(c);
        u3 = new Flt(c);
        //floats2 = c.readVector(Flt.class,3);
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        u1.compile(c);
        u2.compile(c);
        u3.compile(c);
    }
    
    public static class PlLightInfo extends uruobj
    {
        plRegionBase parent;
        //int u1;
        //Uruobjectref sceneobject;
        //int u2;
        //int u3;
        Flt[] floats;
        Transmatrix m1;
        Transmatrix m2;
        Transmatrix m3;
        Transmatrix m4;
        Uruobjectref layer;
        Uruobjectref softvolume;
        Uruobjectref scenenode;
        int count;
        Uruobjectref[] visRegion;
        Flt[] floats2;

        public PlLightInfo(context c) throws readexception
        {
            //u1 = c.readInt();
            //sceneobject = new Uruobjectref(c);
            //u2 = c.readInt();
            //u3 = c.readInt();
            parent = new plRegionBase(c);
            //floats = c.readVector(Flt.class, 76); //76=12+16+16+16+16
            floats = c.readArray(Flt.class, 12);
            m1 = new Transmatrix(c);
            m2 = new Transmatrix(c);
            m3 = new Transmatrix(c);
            m4 = new Transmatrix(c);
            layer = new Uruobjectref(c);
            softvolume = new Uruobjectref(c);
            scenenode = new Uruobjectref(c);
            count = c.readInt();
            visRegion = c.readArray(Uruobjectref.class, count);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeArray(floats);
            m1.compile(c);
            m2.compile(c);
            m3.compile(c);
            m4.compile(c);
            layer.compile(c);
            softvolume.compile(c);
            scenenode.compile(c);
            c.writeInt(count);
            c.writeArray2(visRegion);
        }
    }
}

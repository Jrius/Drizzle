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


public class plMobileOccluder extends uruobj
{
    plOccluder parent;
    Transmatrix m1;
    Transmatrix m2;
    
    int u1;
    int u2;
    Vertex v1;
    Vertex v2;
    Flt[] conglomerate;
    
    public plMobileOccluder(context c) throws readexception
    {
        parent = new plOccluder(c);
        m1 = new Transmatrix(c);
        m2 = new Transmatrix(c);
        
        //same as in PlOccluder:
        //in pots: sub_48a930
        u1 = c.readInt();
        u2 = c.readInt();
        v1 = new Vertex(c);
        v2 = new Vertex(c);
        if((u1&0x1)==0)
        {
            //if u1&1==0 then vertex, 3*(vertex, flt, flt)
            //m.warn("untested case in PlMobileOccluder.");
            conglomerate = c.readArray(Flt.class, 18);
        }
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        m1.compile(c);
        m2.compile(c);
        
        //same as in PlOccluder:
        c.writeInt(u1);
        c.writeInt(u2);
        v1.compile(c);
        v2.compile(c);
        if((u1&0x1)==0)
        {
            c.writeArray(conglomerate);
        }
    }
    
}

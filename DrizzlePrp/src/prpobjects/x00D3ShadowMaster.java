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
public class x00D3ShadowMaster extends uruobj
{
    //Objheader xheader;
    public plObjInterface parent;
    Flt attendist;
    Flt maxdist;
    Flt mindist;
    int maxsize;
    int minsize;
    Flt power;
    
    
    public x00D3ShadowMaster(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plObjInterface(c);//,false);
        attendist = new Flt(c);
        maxdist = new Flt(c);
        mindist = new Flt(c);
        maxsize = c.in.readInt();
        minsize = c.in.readInt();
        power = new Flt(c);
        
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        attendist.compile(data);
        maxdist.compile(data);
        mindist.compile(data);
        data.writeInt(maxsize);
        data.writeInt(minsize);
        power.compile(data);
    }
}

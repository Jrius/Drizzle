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
//Not working!!!!!!!!!!
public class x000CBoundInterface extends uruobj
{
    //Objheader xheader;
    plObjInterface parent;
    //int u1;
    //Uruobjectref parentobject; //should be a scene node.
    //int u2;

    //short u3;
    //int count;
    //Flt[][] u4 = new Flt[4][];

    PrpTaggedObject convexvolume;
    
    
    public x000CBoundInterface(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plObjInterface(c);//,false); //I don't think this is the problem, since x0010 is the parent for other classes which work perfectly.(some use the array and some don't, but they all work.)
        //u1 = data.readInt();
        //parentobject = new Uruobjectref(c);
        //u2 = data.readInt();
        
        //this is an embedded object.  Check the type and implement it.
        /*u3 = data.readShort();
        count = data.readInt(); //number of planes, each plane has 4 flts: a vertex(the normal) and W
        for(int i=0;i<4;i++)
        {
            u4[i] = new Flt[count];
            for(int j=0;j<count;j++)
            {
                u4[i][j] = new Flt(c);
            }
        }*/

        convexvolume = new PrpTaggedObject(c);
        
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);

        //deque.writeShort(u3);
        //deque.writeInt(count);
        //for(int i=0;i<4;i++)
        //{
        //    for(int j=0;j<count;j++)
        //    {
        //        u4[i][j].compile(deque);
        //    }
        //}

        convexvolume.compile(deque);
    }
}

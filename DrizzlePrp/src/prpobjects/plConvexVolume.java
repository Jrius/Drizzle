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
import shared.*;


public class plConvexVolume extends uruobj
{
    //just used by plBoundsInterface, it appears.
    int count;
    Plane[] planes;
    
    public plConvexVolume(context c) throws readexception
    {
        count = c.readInt(); //number of planes, each plane has 4 flts: a vertex(the normal) and W
        //for(int i=0;i<4;i++)
        //{
            //u4[i] = new Flt[count];
            //for(int j=0;j<count;j++)
            //{
            //    u4[i][j] = new Flt(c);
            //}
        //}
        planes = c.readArray(plConvexVolume.Plane.class, count);
    }
    
    public void compile(Bytedeque c)
    {
        c.writeInt(count);
        c.writeArray2(planes);
    }
    public static class Plane extends uruobj
    {
        Vertex v;
        float f;
        public Plane(context c)
        {
            v = new Vertex(c);
            f = c.readFloat();
        }
        public void compile(Bytedeque c)
        {
            v.compile(c);
            c.writeFloat(f);
        }
    }
    
}

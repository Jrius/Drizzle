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
//import java.util.Vector;

/**
 *
 * @author user
 */
public class PrpVolumeIsect // extends uruobj
{
    /*Typeid vitype;
    public uruobj isect;
    
    public PrpVolumeIsect(context c) throws readexception
    {
        vitype = Typeid.Read(c);
        switch(vitype)
        {
            case plVolumeIsect:
                isect = new plVolumeIsect(c);
                break;
            case plConvexIsect:
                isect = new plConvexIsect(c);
                break;
            default:
                m.err("prpvolumeisect: unhandled type");
                break;
        }
    }
    public void compile(Bytedeque c)
    {
        vitype.compile(c);
        switch(vitype)
        {
            case plVolumeIsect:
                isect.compile(c);
                break;
            case plConvexIsect:
                isect.compile(c);
                break;
            default:
                m.err("prpvolumeisect: unhandled type");
                break;
        }
    }*/
    
    
    
    public static class PlConvexPlane extends uruobj
    {
        public Vertex normal; //norm
        public Vertex point; //pos
        public Flt distance; //dist, the dot product of normal and point.
        public Vertex scaledNormal; //worldnorm
        public Flt scaledDist; //worlddist, the dot product of scaledNormal and point.
        
        public PlConvexPlane(context c) throws readexception
        {
            normal = new Vertex(c);
            point = new Vertex(c);
            distance = new Flt(c);
            scaledNormal = new Vertex(c);
            scaledDist = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            normal.compile(c);
            point.compile(c);
            distance.compile(c);
            scaledNormal.compile(c);
            scaledDist.compile(c);
        }
    }
}

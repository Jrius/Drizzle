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

//aka hsBounds3Ext
public class BoundingBox extends uruobj
{
    public int extFlags; //was flags
    public int flags; //was mode
    public Vertex min;
    public Vertex max;
    public Vertex xboundingboxcorner;
    public Vertex xdiff0;
    public Flt xdot0;
    public Flt xmag20;
    public Vertex xdiff1;
    public Flt xdot1;
    public Flt xmag21;
    public Vertex xdiff2;
    public Flt xdot2;
    public Flt xmag22;

    public BoundingBox(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        extFlags = data.readInt();
        flags = data.readInt();
        min = c.readObj(Vertex.class);
        max = c.readObj(Vertex.class);
        if((extFlags&0x00000001)==0)
        {
            xboundingboxcorner = c.readObj(Vertex.class);
            xdiff0 = c.readObj(Vertex.class); //axis
            xdot0 = c.readObj(Flt.class); //x
            xmag20 = c.readObj(Flt.class); //y
            xdiff1 = c.readObj(Vertex.class); //axis
            xdot1 = c.readObj(Flt.class); //x
            xmag21 = c.readObj(Flt.class); //y
            xdiff2 = c.readObj(Vertex.class); //axis
            xdot2 = c.readObj(Flt.class); //x
            xmag22 = c.readObj(Flt.class); //y
        }
    }
    public static BoundingBox createEmpty()
    {
        BoundingBox r = new BoundingBox();
        return r;
    }
    public static BoundingBox createFromMinMax(float minx, float miny, float minz, float maxx, float maxy, float maxz)
    {
        BoundingBox r = createEmpty();
        r.extFlags = 0x1; //on-axis
        r.flags = 0x0;
        r.min = Vertex.createFromFloats(minx, miny, minz);
        r.max = Vertex.createFromFloats(maxx, maxy, maxz);
        return r;
    }
    public static BoundingBox createWithZeroes()
    {
        return createFromMinMax(0,0,0,0,0,0);
    }
    public void transform(Transmatrix mat)
    {
        m.warn("BoundingBox transform may not be correct.");
        min = mat.mult(min);
        max = mat.mult(max);
        if((extFlags&0x00000001)==0)
        {
            xboundingboxcorner = mat.mult(xboundingboxcorner);
        }
    }
    public void compile(Bytedeque data)
    {
        data.writeInt(extFlags);
        data.writeInt(flags);
        min.compile(data);
        max.compile(data);

        if((extFlags&0x00000001)==0)
        {
            xboundingboxcorner.compile(data);
            xdiff0.compile(data);
            xdot0.compile(data);
            xmag20.compile(data);
            xdiff1.compile(data);
            xdot1.compile(data);
            xmag21.compile(data);
            xdiff2.compile(data);
            xdot2.compile(data);
            xmag22.compile(data);

        }

    }
    BoundingBox(){}
    public BoundingBox deepClone()
    {
        BoundingBox r = new BoundingBox();
        r.extFlags = this.extFlags;
        r.flags = this.flags;
        r.min = this.min.deepClone();
        r.max = this.max.deepClone();
        r.xboundingboxcorner = this.xboundingboxcorner.deepClone();
        r.xdiff0 = this.xdiff0.deepClone();
        r.xdot0 = this.xdot0.deepClone();
        r.xmag20 = this.xmag20.deepClone();
        r.xdiff1 = this.xdiff1.deepClone();
        r.xdot1 = this.xdot1.deepClone();
        r.xmag21 = this.xmag21.deepClone();
        r.xdiff2 = this.xdiff2.deepClone();
        r.xdot2 = this.xdot2.deepClone();
        r.xmag22 = this.xmag22.deepClone();
        return r;
    }

}

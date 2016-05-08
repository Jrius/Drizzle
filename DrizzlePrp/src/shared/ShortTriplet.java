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

package shared;

import prpobjects.*;
import uru.Bytestream;
import uru.Bytedeque;
import uru.context; import shared.readexception;

/**
 *
 * @author user
 */
public class ShortTriplet extends uruobj
{
    public short p;
    public short q;
    public short r;

    public ShortTriplet(context c)
    {
        p = c.readShort();
        q = c.readShort();
        r = c.readShort();
    }
    public ShortTriplet(int p, int q, int r)
    {
        this.p = (short)p;
        this.q = (short)q;
        this.r = (short)r;
    }
    private ShortTriplet(){}
    public static ShortTriplet createFromShorts(short p, short q, short r)
    {
        ShortTriplet result = new ShortTriplet();
        result.p = p;
        result.q = q;
        result.r = r;
        return result;
    }
    public void compile(Bytedeque data)
    {
        data.writeShort(p);
        data.writeShort(q);
        data.writeShort(r);
    }
    
    public String toString()
    {
        return "face1="+Short.toString(p)+" face2="+Short.toString(q)+" face3="+Short.toString(r);
    }
}
    

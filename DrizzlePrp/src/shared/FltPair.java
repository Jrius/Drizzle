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

//import prpobjects.*;
//import shared.Flt;
//import uru.Bytestream;
//import uru.Bytedeque;
//import uru.context; import shared.readexception;
//import shared.readexception;

/**
 *
 * @author user
 */
public strictfp class FltPair implements ICompilable
{
    public Flt u;
    public Flt v;
    
    public FltPair(IBytestream c)
    {
        u = new Flt(c);
        v = new Flt(c);
    }
    
    private FltPair(){}
    public static FltPair createFromFloats(float u, float v)
    {
        FltPair result = new FltPair();
        result.u = Flt.createFromJavaFloat(u);
        result.v = Flt.createFromJavaFloat(v);
        return result;
    }
    public static FltPair createFromFlts(Flt u, Flt v)
    {
        FltPair result = new FltPair();
        result.u = u;
        result.v = v;
        return result;
    }
    public void compile(IBytedeque data)
    {
        u.compile(data);
        v.compile(data);
    }
    public String toString()
    {
        //return x.toString()               +":"+y.toString()                +":"+z.toString();
        String s = "  ";
        return "("+u.toString()+s+v.toString()+")";
    }

}

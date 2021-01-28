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

import uru.Bytestream;
import uru.Bytedeque;
import uru.context; import shared.readexception;
import shared.m;
import shared.b;

/**
 *
 * @author user
 */
public class Double64 extends uruobj
{
    byte[] rawdata = new byte[8];
    
    public Double64(context c)
    {
        rawdata = c.readBytes(8);
    }
    /*public Double64(int intToConvert)
    {
        switch(intToConvert)
        {
            case 0:
                rawdata = 0x00000000;
                break;
            case 1:
                rawdata = 0x3F800000;
                break;
            default:
                m.err("Conversion from this int is not currently supported.");
                break;
        }
    }*/
    
    public void compile(Bytedeque deque)
    {
        deque.writeBytes(rawdata);
    }
    public String toString()
    {
        //float value = java.lang.Float.intBitsToFloat(rawdata);
        return "Double=" + java.lang.Double.toString(this.toJavaDouble());
    }
    /*public static String toString(int intvalue)
    {
        float fl = java.lang.Float.intBitsToFloat(intvalue);
        String result = java.lang.Float.toString(fl);
        return result;
    }*/
    public double toJavaDouble()
    {
        long value = b.BytesToInt64(rawdata,0);
        double result = java.lang.Double.longBitsToDouble(value);
        return result;
    }

}

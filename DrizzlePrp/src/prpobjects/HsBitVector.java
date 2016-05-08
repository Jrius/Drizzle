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
import uru.context;
import shared.readexception;
import shared.m;
import shared.*;

/**
 *
 * @author user
 */
public class HsBitVector extends uruobj
{
    public int count;
    public int[] values;

    public HsBitVector(context c)
    {
        count = c.readInt();
        values = c.in.readInts(count);
    }
    public HsBitVector(IBytestream c)
    {
        count = c.readInt();
        values = c.readInts(count);
    }
    public HsBitVector(int... newvalues)
    {
        count = newvalues.length;
        values = new int[count];
        for(int i=0;i<count;i++)
        {
            values[i] = newvalues[i];
        }
    }
    
    private HsBitVector(){}
    
    public static HsBitVector createWithValues(int... newvalues)
    {
        HsBitVector result = new HsBitVector(newvalues);
        return result;
    }
    public static HsBitVector createDefault()
    {
        HsBitVector result = new HsBitVector();
        result.count = 0;
        result.values = new int[0];
        return result;
    }
    
    public void compile(Bytedeque data)
    {
        data.writeInt(count);
        data.writeInts(values);
    }
    
    public int get(int index)// throws readexception
    {
        if(index<count)
        {
            return values[index];
        }
        else
        {
            m.err("HsBitVector: something read out of bounds.");
            //throw new readexception("HsBitVector: something read out of bounds.");
            return 0;
        }
    }
    
    public boolean flag(int flag)
    {
        //offset 12 is pointer to HsBitVector elements
        //offset 16 is the HsBitVector length
        int pos = flag >>> 5;
        if(pos<this.count) //if we don't have that many bits, return false.
        {
            int val = this.get(pos);
            boolean bit = ((1 << (flag&0x1F)) & val)!=0;
            return bit;
        }
        return false;
    }

    public String toString()
    {
        String result = "";
        for(int i=0;i<count;i++)
        {
            result += Integer.toHexString(values[i])+",";
        }
        return result;
    }
}
    

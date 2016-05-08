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
import shared.m;
import shared.e;
import shared.b;
import uru.Bytedeque;
import shared.IBytestream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

/**
 *
 * @author user
 */
public class Urustring16 extends uruobj implements java.io.Serializable
{
    short[] unencryptedString; //should be null-terminated

    private Urustring16(){}
    public Urustring16(IBytestream c, int readversion)
    {
        if(readversion!=6) m.throwUncaughtException("Urustring16 just reads moul right now");
        if(readversion==6)// || readversion==3 || readversion==8)
        {
            short flags = c.readShort();
            int length = (flags & 0x0FFF);
            //unencryptedString = new short[length+1];
            unencryptedString = c.readShorts(length+1);
            for(int i=0;i<length;i++)
            {
                unencryptedString[i] = (short)((~unencryptedString[i]) & 0xFFFF);
            }
            //last byte is not changed:
            //unencryptedString[length] = unencryptedString[length];
        }
    }
    public Urustring16(context c)
    {
        this(c.in,c.readversion);
    }
    public void compile(Bytedeque deque)
    {
        m.throwUncaughtException("Urustring16 compile not implemented.");
    }
    
    public String toString()
    {
        return b.ShortsToString(unencryptedString,0,unencryptedString.length-1);
    }
    
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Urustring16)) return false;
        return o.toString().equals(this.toString());
    }
    
}


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
import shared.b;
import uru.Bytedeque;
import uru.context;
import shared.*;

/**
 *
 * @author user
 */
public class Wpstr extends uruobj
{
    short strlen;
    byte[] string;
    
    public Wpstr(context data)
    {
        strlen = data.readShort();
        string = data.readBytes(b.Int16ToInt32(strlen));
    }
    public Wpstr(IBytestream data)
    {
        strlen = data.readShort();
        string = data.readBytes(b.Int16ToInt32(strlen));
    }
    private Wpstr(){}
    public static Wpstr create(String s)
    {
        byte[] str = b.StringToBytes(s);
        Wpstr result = new Wpstr();
        if(str.length > 65535) throw new shared.uncaughtexception("String size too large for Wpstr.");
        result.strlen = (short)str.length;
        result.string = str;
        return result;
    }
    
    public String toString()
    {
        return new String(string);
    }
    public void compile(Bytedeque deque)
    {
        deque.writeShort(strlen);
        deque.writeBytes(string);
    }
    public Wpstr deepClone()
    {
        Wpstr result = new Wpstr();
        result.strlen = strlen;
        result.string = b.CopyBytes(string);
        return result;
    }

}

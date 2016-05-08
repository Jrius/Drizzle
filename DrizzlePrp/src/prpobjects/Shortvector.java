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
import shared.IBytestream;

/**
 *
 * @author user
 */
public class Shortvector extends uruobj
{
    int length;
    short[] elements;
    
    public Shortvector(IBytestream data)
    {
        length = data.readInt();
        elements = new short[length];
        for(int i=0;i<length;i++)
        {
            elements[i] = data.readShort();
        }
    }
    public void compile(Bytedeque data)
    {
        data.writeInt(length);
        for(int i=0;i<length;i++)
        {
            data.writeShort(elements[i]);
        }
    }

}

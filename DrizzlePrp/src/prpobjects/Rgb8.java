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
import shared.Flt;
import uru.context;
import shared.*;

/**
 *
 * @author user
 */
public class Rgb8 extends uruobj
{
    public byte r;
    public byte g;
    public byte b;
    
    public Rgb8(context c)
    {
        r = c.readByte();
        g = c.readByte();
        b = c.readByte();
    }
    public void compile(Bytedeque c)
    {
        c.writeByte(r);
        c.writeByte(g);
        c.writeByte(b);
    }

    private Rgb8(){}
}

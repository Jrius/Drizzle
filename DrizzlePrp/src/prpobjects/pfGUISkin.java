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


public class pfGUISkin extends uruobj
{
    short itemMargin;
    short borderMargin;
    pfSRect[] elements;
    Uruobjectref texture;
    
    public pfGUISkin(context c) throws readexception
    {
        itemMargin = c.readShort();
        borderMargin = c.readShort();
        int count = c.readInt();
        elements = new pfSRect[count];
        for(int i=0;i<count;i++)
        {
            elements[i] = new pfSRect(c);
        }
        texture = new Uruobjectref(c);
    }
    
    public void compile(Bytedeque c)
    {
        c.writeShort(itemMargin);
        c.writeShort(borderMargin);
        c.writeInt(elements.length);
        for(int i=0;i<elements.length;i++)
        {
            elements[i].compile(c);
        }
        texture.compile(c);
    }

    public static class pfSRect
    {
        short x;
        short y;
        short width;
        short height;

        public pfSRect(context c)
        {
            x = c.readShort();
            y = c.readShort();
            width = c.readShort();
            height = c.readShort();
        }
        public void compile(Bytedeque c)
        {
            c.writeShort(x);
            c.writeShort(y);
            c.writeShort(width);
            c.writeShort(height);
        }
    }
}

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

package realmyst;

import shared.*;

//45a950
public class Flagsen
{
    Bstr name;
    byte type;
    
    public Flagsen(IBytestream c)
    {
        //45a950
        name = new Bstr(c);
        type = c.readByte();
        switch(type)
        {
            case 16:
                int u1 = c.readInt();
                int u2 = c.readInt();
                int u3 = c.readInt();
                int u4 = c.readInt();
                Bstr str2 = new Bstr(c);
                break;
            default:
                m.err("Unhandled flag in flagsen.");
                break;
        }
    }
}

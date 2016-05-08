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

public class X5c3e0f00
{
    int u1;
    int u2;
    int u3;
    int u4;
    int u5;
    int u6;
    int u7;
    int u8;
    int u9;
    int u10;
    Bstr name;
    //byte b11;
    int u12;
    int u13;
    int u14;
    int u15;
    int u16;
    int u17;
    
    public X5c3e0f00(IBytestream c)
    {
        u1 = c.readInt();
        u2 = c.readInt();
        u3 = c.readInt();
        u4 = c.readInt();
        u5 = c.readInt();
        u6 = c.readInt();
        u7 = c.readInt();
        u8 = c.readInt();
        u9 = c.readInt();
        u10 = c.readInt();
        name = new Bstr(c);
        //b11 = c.readByte();
        u12 = c.readInt();
        u13 = c.readInt();
        u14 = c.readInt();
        u15 = c.readInt();
        u16 = c.readInt();
        u17 = c.readInt();
    }
}

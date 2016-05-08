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

import shared.*;

public class Face
{
    public int v1;
    public int v2;
    public int v3;
    
    public Face(IBytestream c)
    {
        v1 = c.readInt();
        v2 = c.readInt();
        v3 = c.readInt();
    }
    
    public String toString()
    {
        String s = "  ";
        String result = "("+Integer.toString(v1)+s+Integer.toString(v2)+s+Integer.toString(v3)+s+")";
        return result;
    }
}

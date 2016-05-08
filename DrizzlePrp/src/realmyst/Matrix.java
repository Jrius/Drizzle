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

public class Matrix
{
    public Flt[][] values = new Flt[4][4];
    
    public Matrix(IBytestream c)
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                values[i][j] = new Flt(c);
            }
        }
    }
    public Matrix()
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                values[i][j] = new Flt(0f);
            }
        }
        values[0][0] = new Flt(1f);
        values[1][1] = new Flt(1f);
        values[2][2] = new Flt(1f);
        values[3][3] = new Flt(1f);
    }
    
    public String toString()
    {
        String result = "( ";
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                result += values[i][j].toString()+"  ";
            }
            result += "; ";
        }
        result += ")";
        return result;
    }
}

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
import shared.m;

/**
 *
 * @author user
 */
public class Uruvector<T extends uruobj> extends uruobj
{
    int length;
    T[] elements;

    public Uruvector(Bytestream data, Class objclass)
    {
        length = data.readInt();
        elements = (T[])uru.generics.makeArray(objclass, length);
        for(int i=0;i<length;i++)
        {
            elements[i] = (T)uru.generics.constructUruObject(objclass, data);
        }
        
    }
    /*static public Uruvector create(Bytestream data)
    {
        return new Uruvector(data);
    }*/
    public void compile(Bytedeque data)
    {
        data.writeInt(length);
        for(int i=0;i<length;i++)
        {
            elements[i].compile(data);
        }
    }

}

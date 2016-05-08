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
import uru.Bytedeque;
import shared.e;
import shared.m;

/**
 *
 * @author user
 */
public class Objheader extends uruobj
{
    //short objecttype;
    public Typeid objecttype;
    byte u1;
    public Uruobjectdesc desc;
    
    public Objheader(context c) throws readexception
    {
        //objecttype = data.readShort();
        objecttype = Typeid.Read(c);
        if(c.readversion==6)
        {
            u1 = c.in.readByte();
        }
        else if(c.readversion==3||c.readversion==4||c.readversion==7)
        {
            //do nothing.
        }
        desc = new Uruobjectdesc(c);
    }
    private Objheader(){}
    public static Objheader createFromDesc(Uruobjectdesc desc)
    {
        Objheader result = new Objheader();
        result.desc = desc;
        result.objecttype = desc.objecttype;
        return result;
    }
    public void compile(Bytedeque deque)
    {
        //this could be wrong.
        objecttype.compile(deque);
        desc.compile(deque);
    }
    public String toString()
    {
        return desc.toString();
    }
}

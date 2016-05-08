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

public class TaggedObj
{
    Typeid type;
    //X5c3e0f00 x1;
    Object val;
    
    public TaggedObj(IBytestream c)
    {
        type = Typeid.read(c);
        val = readwithtype(type,c);

    }

    public static Object readwithtype(Typeid type, IBytestream c)
    {
        switch(type)
        {
            //case occref:
            //    x1 = new X5c3e0f00(c);
            //    break;
            //case ref:
            //    return new Count9.Ref1(c);
            //case ref2:
            //    return new Count9.Ref2(c);
            case occref:
                return new Count10.occref.suboccref(c);
            case count9ref:
                return new Count9.Subref2(c);
            default:
                m.err("Unhandled type in TaggedObj.");
                return null;
        }
    }
}

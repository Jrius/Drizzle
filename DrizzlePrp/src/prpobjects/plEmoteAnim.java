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
import shared.b;
import shared.readexception;
//import java.util.Vector;
import shared.*;

public class plEmoteAnim extends uruobj
{
    public plATCAnim parent;
    public Flt f1;
    public Flt f2;
    public byte b3;
    
    public plEmoteAnim(context c) throws readexception
    {
        parent = new plATCAnim(c);
        f1 = new Flt(c);
        f2 = new Flt(c);
        b3 = c.readByte();
        
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        f1.compile(c);
        f2.compile(c);
        c.writeByte(b3);
    }
    
}

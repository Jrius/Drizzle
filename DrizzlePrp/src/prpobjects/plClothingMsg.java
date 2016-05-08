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
//import java.util.Vector;
import shared.*;


public class plClothingMsg extends uruobj
{
    plMessage parent;
    int u1; //1
    byte hasref;
    Uruobjectref ref; //a plClothingItem
    float f1; //1.0
    float f2; //1.0
    float f3; //1.0
    float f4; //1.0
    byte b5; //0
    byte b6; //0
    float f7; //0.0
    
    public plClothingMsg(context c) throws readexception //moul format
    {
        parent = new plMessage(c);
        u1 = c.readInt();
        hasref = c.readByte();
        if(hasref!=0)
        {
            ref = new Uruobjectref(c);
        }
        f1 = c.readFloat();
        f2 = c.readFloat();
        f3 = c.readFloat();
        f4 = c.readFloat();
        b5 = c.readByte();
        b6 = c.readByte();
        f7 = c.readFloat();
    }
    
    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("compile not implemented.",this.toString());
    }
    
}

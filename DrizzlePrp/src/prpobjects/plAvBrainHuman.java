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


public class plAvBrainHuman extends uruobj
{
    plArmatureBrain parent;
    //int u1;
    //byte b2;
    //Uruobjectref xref;
    //int u3;
    //Flt f4;
    //Double64 d5;
    
    public plAvBrainHuman(context c) throws readexception
    {
        //u1 = c.readInt();
        //b2 = c.readByte();
        //if(b2!=0)
        //{
        //    xref = new Uruobjectref(c);
        //}
        //u3 = c.readInt();
        //f4 = new Flt(c);
        //d5 = new Double64(c);
        parent = new plArmatureBrain(c);
        if(c.readversion==6)
        {
            byte b6 = c.readByte();
        }
        else if(c.readversion==3)
        {
            //do nothing.
        }
    }
    
    public void compile(Bytedeque c)
    {
        //c.writeInt(u1);
        //c.writeByte(b2);
        //if(b2!=0)
        //{
        //    xref.compile(c);
        //}
        //c.writeInt(u3);
        //f4.compile(c);
        //d5.compile(c);
        parent.compile(c);
    }

    public static class plArmatureBrain extends uruobj
    {
        int u1;
        byte b2;
        Uruobjectref xref;
        int u3;
        float f4;
        Double64 d5;

        public plArmatureBrain(context c) throws readexception
        {
            u1 = c.readInt();
            b2 = c.readByte();
            if(b2!=0)
            {
                xref = new Uruobjectref(c);
            }
            u3 = c.readInt();
            f4 = c.readFloat();
            d5 = new Double64(c);
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(u1);
            c.writeByte(b2);
            if(b2!=0)
            {
                xref.compile(c);
            }
            c.writeInt(u3);
            c.writeFloat(f4);
            d5.compile(c);
        }
    }
    
}

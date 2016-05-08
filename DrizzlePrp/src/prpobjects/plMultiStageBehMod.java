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

//unfinished, do not use.
public class plMultiStageBehMod extends uruobj
{
    plSingleModifier parent;
    byte u1;
    byte u2;
    byte u3;
    int count1;
    plAnimStage[] animStages;
    int count2;
    Uruobjectref[] refs;
    
    public plMultiStageBehMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        u1 = c.readByte();
        u2 = c.readByte();
        u3 = c.readByte();
        count1 = c.readInt();
        animStages = c.readArray(plAnimStage.class, count1);
        count2 = c.readInt();
        refs = c.readArray(Uruobjectref.class, count2);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeByte(u1);
        c.writeByte(u2);
        c.writeByte(u3);
        c.writeInt(count1);
        c.writeArray2(animStages);
        c.writeInt(count2);
        c.writeArray2(refs);
    }
    
    public static class plAnimStage extends uruobj
    {
        Urustring u1;
        byte u2;
        int u3;
        int u4;
        int u5;
        int u6;
        int u7;
        byte u8;
        int u9;
        byte u10;
        int u11;
        
        public plAnimStage(context c)
        {
            u1 = new Urustring(c);
            u2 = c.readByte();
            u3 = c.readInt();
            u4 = c.readInt();
            u5 = c.readInt();
            u6 = c.readInt();
            u7 = c.readInt();
            u8 = c.readByte();
            u9 = c.readInt();
            u10 = c.readByte();
            u11 = c.readInt();
        }
        
        public void compile(Bytedeque c)
        {
            u1.compile(c);
            c.writeByte(u2);
            c.writeInt(u3);
            c.writeInt(u4);
            c.writeInt(u5);
            c.writeInt(u6);
            c.writeInt(u7);
            c.writeByte(u8);
            c.writeInt(u9);
            c.writeByte(u10);
            c.writeInt(u11);
        }
    }
}

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
import shared.Flt;
//import java.util.Vector;

public class plDynamicTextMap extends uruobj
{
    //Objheader xheader;
    
    x0003Bitmap parent; //cobbs lists this as MipMap, but it's actually a Bitmap.
    int visWidth;  //was u1
    int visHeight;  //was u2
    byte hasAlpha; //was u3
    //byte u4;
    //byte u5;
    //byte u6;
    //byte u7;
    int initBufferLen; //was u4to7
    byte[] buffer;
    
    public plDynamicTextMap(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new x0003Bitmap(c);//,false);
        visWidth = c.readInt();
        visHeight = c.readInt();
        hasAlpha = c.readByte();
        if(c.readversion==7)
        {
            //I'm not sure if alpha goes before these are between or after.
            m.warn("Not sure if reading PlDynamicTextMap correctly.");
            byte b1 = c.readByte();
            byte b2 = c.readByte();
            Flt f3 = new Flt(c);
            Flt f4 = new Flt(c);
            Flt f5 = new Flt(c);
            Flt f6 = new Flt(c);
        }
        //u4 = c.readByte();
        //u5 = c.readByte();
        //u6 = c.readByte();
        //u7 = c.readByte();
        initBufferLen = c.readInt();
        buffer = c.readBytes(initBufferLen*4); //are these ints?
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(visWidth);
        c.writeInt(visHeight);
        c.writeByte(hasAlpha);
        //c.writeByte(u4);
        //c.writeByte(u5);
        //c.writeByte(u6);
        //c.writeByte(u7);
        c.writeInt(initBufferLen);
        c.writeBytes(buffer);
    }
    private plDynamicTextMap(){}
    public static plDynamicTextMap createBlank(int visWidth, int visHeight)
    {
        plDynamicTextMap result = new plDynamicTextMap();
        
        result.parent = x0003Bitmap.createForDynamicTextMap();
        result.visWidth = visWidth; //1024;
        result.visHeight = visHeight; //1024;
        result.hasAlpha = 0;
        result.initBufferLen = 0;
        result.buffer = new byte[0];
        
        return result;
    }
}

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


public class plAvAnimTask extends uruobj
{
    Urustring animName;
    float initialBlend;
    float targetBlend;
    float fadeSpeed;
    float setTime;
    byte start;
    byte loop;
    byte attach;
    
    public plAvAnimTask(context c) throws readexception
    {
        //moul format, not necessarily pots.
        animName = new Urustring(c);
        initialBlend = c.readFloat();
        targetBlend = c.readFloat();
        fadeSpeed = c.readFloat();
        setTime = c.readFloat();
        start = c.readByte();
        loop = c.readByte();
        attach = c.readByte();
    }
    
    public void compile(Bytedeque c)
    {
        if(c.format!=Format.moul) m.throwUncaughtException("unsure");
        
        animName.compile(c);
        c.writeFloat(initialBlend);
        c.writeFloat(targetBlend);
        c.writeFloat(fadeSpeed);
        c.writeFloat(setTime);
        c.writeByte(start);
        c.writeByte(loop);
        c.writeByte(attach);
    }
    
}

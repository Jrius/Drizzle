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

public class Count2
{
    public Typeid type;
    public int size;
    
    public int u1;
    public StringAndByte sb2;
    public int u3;
    public Bstr s4;
    public byte b5;
    public Bstr textureFilename;
    public int u7;
    
    //texture
    public Count2(IBytestream c) //has hsm filename.
    {
        type = Typeid.read(c);
        size = c.readInt();
        
        //skip the rest.
        //c.readBytes(size-8);
        u1 = c.readInt(); e.ensure(u1,1);
        sb2 = new StringAndByte(c);
        u3 = c.readInt(); e.ensure(u3,0);
        //StringAndByte sb4 = new StringAndByte(c);
        s4 = new Bstr(c);
        b5 = c.readByte(); e.ensure((int)b5,0);
        textureFilename = new Bstr(c);
        u7 = c.readInt(); //e.ensure(u7,0x800000,0x18800000,0x10800000,0x10900000,0x20800000,0x900000);
        int dummy=0;
    }
}

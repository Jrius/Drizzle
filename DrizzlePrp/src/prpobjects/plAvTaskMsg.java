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


public class plAvTaskMsg extends uruobj
{
    plAvatarMsg parent;
    byte hastask;
    PrpTaggedObject task;
    
    public plAvTaskMsg(context c) throws readexception
    {
        parent = new plAvatarMsg(c);
        hastask = c.readByte();
        if(hastask!=0)
        {
            task = new PrpTaggedObject(c);
        }
    }
    
    public void compile(Bytedeque c)
    {
        if(c.format!=Format.moul) m.throwUncaughtException("unsure");

        parent.compile(c);
        c.writeByte(hastask);
        if(hastask!=0)
        {
            task.compile(c);
        }
    }

    public static class plAvatarMsg extends uruobj
    {
        plMessage parent;

        public plAvatarMsg(context c) throws readexception
        {
            parent = new plMessage(c);
        }

        public void compile(Bytedeque c)
        {
            m.throwUncaughtException("compile not implemented.",this.toString());
        }
    }
}

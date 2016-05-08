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


public class plNetMsgPagingRoom extends uruobj
{
    public static final int kPagingOut = 0x1;
    public static final int kResetList = 0x2;
    public static final int kRequestState = 0x4;
    public static final int kFinalRoomInAge = 0x8;
    
    public plNetMsgRoomsList parent;
    public byte pageFlags;
    
    public plNetMsgPagingRoom(context c) throws readexception
    {
        parent = new plNetMsgRoomsList(c);
        pageFlags = c.readByte();
    }

    public boolean isPagingOut()
    {
        return (pageFlags&kPagingOut)!=0;
    }
    
    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("compile not implemented.",this.toString());
    }
    
}

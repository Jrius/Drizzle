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


public class plNetMsgRoomsList extends uruobj
{
    public plNetMessage parent;
    private int count;
    public Room[] rooms;
    boolean extended = true;

    public plNetMsgRoomsList(context c) throws readexception
    {
        parent = new plNetMessage(c);
        if(c.areBytesKnownToBeAvailable())
        {
            count = c.readInt();
            rooms = c.readArray(Room.class, count);
        }
        else extended = false;
    }

    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("compile not implemented.",this.toString());
    }

    public static class Room extends uruobj
    {
        public Location location;
        public Wpstr name; //e.g. AvatarCustomization_Closet

        public Room(context c) throws readexception
        {
            location = new Location(c);
            name = new Wpstr(c);
        }
    }

}

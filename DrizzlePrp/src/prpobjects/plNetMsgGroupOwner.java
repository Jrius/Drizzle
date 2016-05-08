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


public class plNetMsgGroupOwner extends uruobj
{
    public plNetMessage parent;
    private int size;
    public GroupInfo[] groups;
    
    public plNetMsgGroupOwner(context c) throws readexception
    {
        parent = new plNetMessage(c);
        size = c.readInt();
        groups = new GroupInfo[size];
        for(int i=0;i<size;i++)
        {
            groups[i] = new GroupInfo(c);
        }
    }
    private plNetMsgGroupOwner(){}
    public static plNetMsgGroupOwner createWithInfo(Location location, boolean isOwner)
    {
        plNetMsgGroupOwner r = new plNetMsgGroupOwner();
        r.parent = plNetMessage.createDefault();
        r.groups = new GroupInfo[1];
        r.groups[0] = new GroupInfo();
        r.groups[0].groupId = plNetGroupId.createWithLocation(location);
        r.groups[0].ownIt = b.BooleanToByte(isOwner);
        return r;
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(groups.length);
        for(int i=0;i<groups.length;i++)
        {
            groups[i].compile(c);
        }
    }

    public static class GroupInfo
    {
        plNetGroupId groupId;
        byte ownIt;

        public GroupInfo(){}

        public GroupInfo(context c) throws readexception
        {
            groupId = new plNetGroupId(c);
            ownIt = c.readByte();
        }

        public void compile(Bytedeque c)
        {
            groupId.compile(c);
            c.writeByte(ownIt);
        }
    }

    public static class plNetGroupId
    {
        public static final int kNetGroupConstant = 0x01;
        public static final int kNetGroupLocal = 0x02;

        Location id;
        byte flags;

        public plNetGroupId(context c) throws readexception
        {
            id = new Location(c);
            flags = c.readByte();
        }

        public void compile(Bytedeque c)
        {
            id.compile(c);
            c.writeByte(flags);
        }

        private plNetGroupId(){}
        public static plNetGroupId createWithLocation(Location loc)
        {
            plNetGroupId r = new plNetGroupId();
            r.id = loc;
            r.flags = 0;
            return r;
        }
    }

}

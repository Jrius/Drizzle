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


public class plNetMsgMemberUpdate extends uruobj
{
    public plNetMessage parent;
    private short size;
    public plNetMsgMembersList.plNetMsgMemberInfoHelper[] members;
    
    public plNetMsgMemberUpdate(context c) throws readexception
    {
        parent = new plNetMessage(c);
        size = c.readShort();
        int size2 = b.Int16ToInt32(size);
        members = new plNetMsgMembersList.plNetMsgMemberInfoHelper[size2];
        for(int i=0;i<size2;i++)
        {
            members[i] = new plNetMsgMembersList.plNetMsgMemberInfoHelper(c);
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        short size2 = (short)members.length;
        c.writeShort(size2);
        for(plNetMsgMembersList.plNetMsgMemberInfoHelper member: members)
        {
            member.compile(c);
        }
    }
    
}

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


//doesn't have to be abstract, but I'm lazy for now.
public abstract class plLoadCloneMsg extends inhuruobj
{
    public plMessage parent;
    public Uruobjectref cloneKey;
    public Uruobjectref requestorKey;
    public int originatingPlayerId;
    public int userData;
    public byte validMsg;
    public byte isLoading;
    public PrpTaggedObject triggerMsg;

    public plLoadCloneMsg(context c) throws readexception
    {
        parent = new plMessage(c);
        cloneKey = new Uruobjectref(c);
        requestorKey = new Uruobjectref(c);
        originatingPlayerId = c.readInt();
        userData = c.readInt();
        validMsg = c.readByte();
        isLoading = c.readByte();
        triggerMsg = new PrpTaggedObject(c);
    }
    //public Typeid typeid(){return Typeid.pllo}
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        cloneKey.compile(c);
        requestorKey.compile(c);
        c.writeInt(originatingPlayerId);
        c.writeInt(userData);
        c.writeByte(validMsg);
        c.writeByte(isLoading);
        triggerMsg.compile(c);
    }
}


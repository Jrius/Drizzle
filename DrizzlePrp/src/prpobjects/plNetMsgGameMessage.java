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


public class plNetMsgGameMessage extends uruobj
{
    //public plNetMsgLoadClone.plNetMessage parent;
    //public PrpTaggedObject obj;
    public plNetMsgStream parent;
    public Byte hasTimestamp;
    public Timestamp time;

    boolean extended = true;

    //sub6b62b0
    public plNetMsgGameMessage(context c) throws readexception
    {
        parent = new plNetMsgStream(c);
        if(c.areBytesKnownToBeAvailable())
        {
            hasTimestamp = c.readByte();
            if(hasTimestamp!=0)
            {
                time = new Timestamp(c.in);
            }
        }
        else extended = false;
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        if(extended)
        {
            c.writeByte(hasTimestamp);
            if(hasTimestamp!=0)
            {
                time.compile(c);
            }
        }
    }

    private plNetMsgGameMessage(){}
    public static plNetMsgGameMessage createWithObjPlayeridx(PrpTaggedObject obj, int playerIdx)
    {
        plNetMsgGameMessage r = new plNetMsgGameMessage();
        r.parent = plNetMsgStream.createWithObjPlayeridx(obj, playerIdx);
        r.hasTimestamp = 0;
        return r;
    }

    public static class plNetMsgStream extends uruobj
    {
        public plNetMessage parent;
        public PrpTaggedObject obj;
        boolean extended=true;

        public plNetMsgStream(context c) throws readexception
        {
            parent = new plNetMessage(c);

            if(c.areBytesKnownToBeAvailable())
            {
                plNetMsgLoadClone.plNetMsgStreamHelper nms = new plNetMsgLoadClone.plNetMsgStreamHelper(c,PrpTaggedObject.class,true/*,2*/);
                obj = (PrpTaggedObject)nms.obj;
                //m.msg("GameMessage: "+obj.type.toString());
            }
            else extended = false;
        }
        private plNetMsgStream(){}
        public static plNetMsgStream createWithObjPlayeridx(PrpTaggedObject obj, int playerIdx)
        {
            plNetMsgStream r = new plNetMsgStream();
            r.parent = plNetMessage.createWithPlayeridx(playerIdx);
            r.obj = obj;
            return r;
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            if(extended)
            {
                //this isn't bytewise exactly the same as the original, but we shouldn't care, because this is intended for net use anyway.
                plNetMsgLoadClone.plNetMsgStreamHelper helper = plNetMsgLoadClone.plNetMsgStreamHelper.createFromObj(c,obj);
                helper.compile(c);
            }
        }
    }
}


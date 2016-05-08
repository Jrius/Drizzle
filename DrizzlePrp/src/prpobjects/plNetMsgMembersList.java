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
import java.util.ArrayList;


public class plNetMsgMembersList extends uruobj
{
    public plNetMessage parent;
    private short size;
    public ArrayList<plNetMsgMemberInfoHelper> members;
    
    public plNetMsgMembersList(context c) throws readexception
    {
        parent = new plNetMessage(c);
        size = c.readShort();
        int size2 = b.Int16ToInt32(size);
        members = new ArrayList(size2);
        for(int i=0;i<size2;i++)
        {
            members.add(new plNetMsgMemberInfoHelper(c));
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        short size2 = (short)members.size();
        c.writeShort(size2);
        for(plNetMsgMemberInfoHelper member: members)
        {
            member.compile(c);
        }
    }

    private plNetMsgMembersList(){}
    public static plNetMsgMembersList createWithPlayeridx(int playerIdx)
    {
        plNetMsgMembersList r = new plNetMsgMembersList();
        r.parent = plNetMessage.createWithPlayeridx(playerIdx);
        r.members = new ArrayList();
        return r;
    }

    public static class plNetMsgMemberInfoHelper extends uruobj
    {
        public int flags; //should this be set to clientGuid.flags?
        public plClientGuid clientGuid;
        public Uruobjectdesc avatarDesc;

        public plNetMsgMemberInfoHelper(context c) throws readexception
        {
            flags = c.readInt();
            clientGuid = new plClientGuid(c);
            avatarDesc = new Uruobjectdesc(c);
        }
        public void compile(Bytedeque c)
        {
            if(c.format!=Format.moul) m.throwUncaughtException("untested");
            c.writeInt(flags);
            clientGuid.compile(c);
            avatarDesc.compile(c);
        }
        private plNetMsgMemberInfoHelper(){}
        public static plNetMsgMemberInfoHelper createWithAvatardesc(Uruobjectdesc avatarDesc)
        {
            plNetMsgMemberInfoHelper r = new plNetMsgMemberInfoHelper();
            r.flags = 0;
            r.clientGuid = plClientGuid.createEmpty();
            r.avatarDesc = avatarDesc;
            return r;
        }
    }

    public static class plClientGuid extends uruobj
    {

        public static final int kAcctUuid = 0x1;
        public static final int kPlayerID = 0x2;
        public static final int kTempPlayerID = 0x4;
        public static final int kCCRLevel = 0x8;
        public static final int kProtectedLogin = 0x10;
        public static final int kBuildType = 0x20;
        public static final int kPlayerName = 0x40;
        public static final int kSrcAddr = 0x80;
        public static final int kSrcPort = 0x100;
        public static final int kReserved = 0x200;
        public static final int kClientKey = 0x400;

        //don't access these directly, use the 'set' methods.
        short flags;
        Guid acctGuid;
        int playerIdx;
        Wpstr playerName;
        byte CCRLevel;
        byte protectedLogin;
        byte buildType;
        int srcAddr;
        short srcPort;
        short reserved;
        Wpstr clientKey;

        public plClientGuid(context c)
        {
            flags = c.readShort();

            if((flags&kAcctUuid)!=0)
            {
                acctGuid = new Guid(c);
            }
            if((flags&kPlayerID)!=0)
            {
                playerIdx = c.readInt();
            }
            if((flags&kPlayerName)!=0)
            {
                playerName = new Wpstr(c);
            }
            if((flags&kCCRLevel)!=0)
            {
                CCRLevel = c.readByte();
            }
            if((flags&kProtectedLogin)!=0)
            {
                protectedLogin = c.readByte();
            }
            if((flags&kBuildType)!=0)
            {
                buildType = c.readByte();
            }
            if((flags&kSrcAddr)!=0)
            {
                srcAddr = c.readInt();
            }
            if((flags&kSrcPort)!=0)
            {
                srcPort = c.readShort();
            }
            if((flags&kReserved)!=0)
            {
                reserved = c.readShort();
            }
            if((flags&kClientKey)!=0)
            {
                clientKey = new Wpstr(c);
            }
        }

        public void compile(Bytedeque c)
        {
            c.writeShort(flags);

            if((flags&kAcctUuid)!=0)
            {
                acctGuid.write(c);
            }
            if((flags&kPlayerID)!=0)
            {
                c.writeInt(playerIdx);
            }
            if((flags&kPlayerName)!=0)
            {
                playerName.compile(c);
            }
            if((flags&kCCRLevel)!=0)
            {
                c.writeByte(CCRLevel);
            }
            if((flags&kProtectedLogin)!=0)
            {
                c.writeByte(protectedLogin);
            }
            if((flags&kBuildType)!=0)
            {
                c.writeByte(buildType);
            }
            if((flags&kSrcAddr)!=0)
            {
                c.writeInt(srcAddr);
            }
            if((flags&kSrcPort)!=0)
            {
                c.writeShort(srcPort);
            }
            if((flags&kReserved)!=0)
            {
                c.writeShort(reserved);
            }
            if((flags&kClientKey)!=0)
            {
                clientKey.compile(c);
            }
        }
        
        private plClientGuid(){}
        public static plClientGuid createEmpty()
        {
            plClientGuid r = new plClientGuid();
            return r;
        }

        public void setPlayerId(int playerIdx)
        {
            this.playerIdx = playerIdx;
            this.flags |= kPlayerID;
        }
        public void setPlayerName(String playerName)
        {
            this.playerName = Wpstr.create(playerName);
            this.flags |= kPlayerName;
        }
        public void setCCRLevel(byte CCRLevel)
        {
            this.CCRLevel = CCRLevel;
            this.flags |= kCCRLevel;
        }
        public void setAcctGuid(Guid acctGuid)
        {
            this.acctGuid = acctGuid;
            this.flags |= kAcctUuid;
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import shared.*;
import uru.context;
import uru.Bytedeque;

public class plNetMessage extends uruobj
{
    public static final int kHasTimeSent = 0x1;
    public static final int kHasGameMsgRecvrs = 0x2;
    public static final int kEchoBackToSender = 0x4;
    public static final int kRequestP2P = 0x8;
    public static final int kAllowTimeOut = 0x10;
    public static final int kIndirectMember = 0x20;
    public static final int kPublicIPClient = 0x40;
    public static final int kHasContext = 0x80;
    public static final int kAskVaultForGameState = 0x100;
    public static final int kHasTransactionID = 0x200;
    public static final int kNewSDLState = 0x400;
    public static final int kInitialAgeStateRequest = 0x800;
    public static final int kHasPlayerID = 0x1000;
    public static final int kUseRelevanceRegions = 0x2000;
    public static final int kHasAcctUuid = 0x4000;
    public static final int kInterAgeRouting = 0x8000;
    public static final int kHasVersion = 0x10000;
    public static final int kIsSystemMessage = 0x20000;
    public static final int kNeedsReliableSend = 0x40000;
    public static final int kRouteToAllPlayers = 0x80000;

    //short type; //present, but we'll treat this like a PrpTaggedObject, so we'll read it there for now.
    public int flags;
    public byte xProtocolMajorVersion; //12
    public byte xProtocolMinorVersion; //6
    public Timestamp xTimeSent;
    public Integer xContext;
    public Integer xTransId;
    public Integer xPlayerId;
    public byte[] xAccountGuid;

    //sub6b60c0
    public plNetMessage(context c) throws readexception
    {
        //type = c.readShort();
        flags = c.readInt();
        if((flags&kHasVersion)!=0)
        {
            xProtocolMajorVersion = c.readByte();
            xProtocolMinorVersion = c.readByte();
        }
        if((flags&kHasTimeSent)!=0)
        {
            xTimeSent = new Timestamp(c.in);
        }
        if((flags&kHasContext)!=0)
        {
            xContext = c.readInt();
        }
        if((flags&kHasTransactionID)!=0)
        {
            xTransId = c.readInt();
        }
        if((flags&kHasPlayerID)!=0)
        {
            xPlayerId = c.readInt();
        }
        if((flags&kHasAcctUuid)!=0)
        {
            xAccountGuid = c.readBytes(16);
        }
    }
    public void compile(Bytedeque c)
    {
        c.writeInt(flags);
        if((flags&kHasVersion)!=0)
        {
            c.writeByte(xProtocolMajorVersion);
            c.writeByte(xProtocolMinorVersion);
        }
        if((flags&kHasTimeSent)!=0)
        {
            xTimeSent.compile(c);
        }
        if((flags&kHasContext)!=0)
        {
            c.writeInt(xContext);
        }
        if((flags&kHasTransactionID)!=0)
        {
            c.writeInt(xTransId);
        }
        if((flags&kHasPlayerID)!=0)
        {
            c.writeInt(xPlayerId);
        }
        if((flags&kHasAcctUuid)!=0)
        {
            c.writeBytes(xAccountGuid);
        }
    }

    private plNetMessage(){}
    public static plNetMessage createDefault()
    {
        plNetMessage r = new plNetMessage();
        r.flags = 0; //nothing turned on.
        return r;
    }
    public static plNetMessage createWithPlayeridx(int playerIdx)
    {
        plNetMessage r = new plNetMessage();
        r.flags = kHasPlayerID;
        r.xPlayerId = playerIdx;
        return r;
    }
}

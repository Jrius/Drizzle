/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import prpobjects.Guid;
import shared.*;
import uru.server.*;
import prpobjects.uruobj;
import uru.Bytedeque;
//import moulserver.ConnectServer.ServerThread;
import java.util.Queue;
import moulserver.Comm.*;
//import moulserver.NetServer.ConnectionState;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.ref.WeakReference;

public class GameMainServer extends Thread
{

    //The purpose of the GameMainServer is simply to redirect messages to the correct GameServer.

    public static final int kCli2Game_PingRequest = 0;
    public static final int kCli2Game_JoinAgeRequest = 1;
    public static final int kCli2Game_PropagateBuffer = 2;
    public static final int kCli2Game_GameMgrMsg = 3;
    public static final int kCli2Game_LastMessage = 4;

    public static final int kGame2Cli_PingReply = 0;
    public static final int kGame2Cli_JoinAgeReply = 1;
    public static final int kGame2Cli_PropagateBuffer = 2;
    public static final int kGame2Cli_GameMgrMsg = 3;
    public static final int kGame2Cli_LastMessage = 4;

    Manager manager;
    Queue<Comm.CommItem> items = Concurrent.getConcurrentQueue();

    private AtomicInteger nextGameServerNum = new AtomicInteger();
    ConcurrentHashMap<Guid,GameServer> gameservers = Concurrent.getConcurrentHashMap(); //ageInstanceGuid->gameserver
    ConcurrentHashMap<Integer,GameServer> players = Concurrent.getConcurrentHashMap(); //playerIdx->gameserver

    public GameMainServer(Manager manager)
    {
        this.manager = manager;
    }

    public void run()
    {
        m.msg("Starting GameMainServer...");

        while(true)
        {
            CommItem item = items.poll();
            if(item!=null)
            {
                if(item.type==CommItemType.HandleMessage)
                {
                    HandleMessage(item);
                }
                else
                {
                    m.throwUncaughtException("unhandled");
                }
            }
            else
            {
                try{
                    Thread.sleep(Manager.msToSleep);
                }catch(Exception e){}
            }
        }
    }


    public synchronized GameServer requestGameserverFromGuid(Guid instanceGuid, String ageFilename, int playerIdx)
    {
        GameServer gameserver = gameservers.get(instanceGuid);
        if(gameserver==null)
        {
            //create gameserver
            int nextnum = nextGameServerNum.incrementAndGet();
            gameserver = new GameServer(nextnum, ageFilename, instanceGuid, manager);
            gameserver.start();
            gameservers.put(instanceGuid, gameserver);
        }
        gameserver.expectPlayer(playerIdx);
        players.put(playerIdx, gameserver);
        //return new WeakReference(gameserver);
        //return gameserver.gameServerNumber;
        return gameserver;
    }


    public static GameMsg ReadMessage(IBytestream c, boolean isServer)
    {

        short msgId = c.readShort();

        if(isServer)
        {
            switch(msgId)
            {
                case kCli2Game_JoinAgeRequest:
                    return new JoinAgeRequest(c);
                case kCli2Game_PropagateBuffer:
                    return new PropagateBuffer(c);
                default:
                    throw new shared.uncaughtexception("Unread GameMainServer packet: "+Short.toString(msgId));
            }
        }
        else
        {
            switch(msgId)
            {
                case kGame2Cli_JoinAgeReply:
                    return new JoinAgeReply(c);
                case kGame2Cli_PropagateBuffer:
                    return new PropagateBuffer(c); //same class for both client and server.
                default:
                    throw new shared.uncaughtexception("Unread GameMainServer packet: "+Short.toString(msgId));
            }
        }

    }

    //public static void HandleGameServerPacket(ConnectServer.ServerThread sock)

    public void HandleMessageDirect(CommItem item)
    {
        HandleMessage(item);
    }

    private void HandleMessage(CommItem item)
    {
        //Comm.CommItem_Old item = item2.cast();
        //pass off the message to the correct gameserver.
        Object msg = item.msg;
        Class klass = msg.getClass();
        ConnectionState cs = item.connstate;

        if(klass==JoinAgeRequest.class)
        {
            m.msg("GameMainServer JoinAgeRequest");
            JoinAgeRequest request = (JoinAgeRequest)msg;

            cs.playerId = request.playerIdx;
        }

        //get the player's GameServer.
        if(item.connstate.currentGameServer==null)
        {
            if(cs.playerId==null)
            {
                m.err("Player doesn't have a GameServer assigned, because they haven't done a JoinAgeRequest.");
                return;
            }
            GameServer gameserver = players.get(item.connstate.playerId);
            item.connstate.currentGameServer = new WeakReference(gameserver);
        }

        //pass the message onto their gameserver
        GameServer gameserver = item.connstate.currentGameServer.get();
        if(gameserver==null)
        {
            m.err("GameServer seems to have died.");
        }
        else
        {
            gameserver.items.add(item);
        }

    }





    /*public static class PropagateBuffer_serverToClient extends GameMsg
    {
        int messageType;
        int size;
        byte[] data;

        public PropagateBuffer_serverToClient(IBytestream c)
        {
            messageType = c.readInt();
            size = c.readInt();
            data = c.readBytes(size);
        }
        public PropagateBuffer_serverToClient(){}
        public short type(){return kGame2Cli_PropagateBuffer;}
        public void write(IBytedeque c)
        {
            c.writeInt(messageType);
            c.writeInt(data.length);
            c.writeBytes(data);
        }
    }*/

    public static class PropagateBuffer extends GameMsg
    {
        int messageType;
        int size;
        byte[] data;

        prpobjects.PrpTaggedObject obj;

        public PropagateBuffer(IBytestream c)
        {
            messageType = c.readInt();
            size = c.readInt();
            data = c.readBytes(size);

            parse();
        }
        public PropagateBuffer(){}
        public short type(){return kCli2Game_PropagateBuffer;}
        public void write(IBytedeque c)
        {
            c.writeInt(messageType);
            c.writeInt(data.length);
            c.writeBytes(data);
        }
        public void parse()
        {
            IBytestream stream = ByteArrayBytestream.createFromByteArray(data);
            uru.context ctxt = uru.context.createFromBytestream(stream);
            ctxt.readversion = 6;
            try{
                obj = new prpobjects.PrpTaggedObject(ctxt);
                //m.msg("GameServer PropagateBuffer: "+obj.type.toString());
                if(ctxt.in.getBytesRemaining()!=0)
                    m.throwUncaughtException("did not read all");
            }catch(Exception e){
                e.printStackTrace();
                m.throwUncaughtException("error reading object.");
            }
        }
        public static PropagateBuffer createWithObj(prpobjects.PrpTaggedObject obj)
        {
            PropagateBuffer r = new PropagateBuffer();
            r.messageType = b.Int16ToInt32(obj.type.getAsShort(Format.moul));
            r.data = obj.compileAlone(Format.moul);
            return r;
        }
        public static PropagateBuffer createWithObj(prpobjects.Typeid type, uruobj obj2)
        {
            prpobjects.PrpTaggedObject obj = prpobjects.PrpTaggedObject.createWithTypeidUruobj(type, obj2);
            return createWithObj(obj);
        }
        public String dump()
        {
            return super.dump() + " : " + obj.dump();
        }
    }

    public static class JoinAgeReply extends GameMsg
    {
        int transId;
        int result;

        public JoinAgeReply(){}
        public JoinAgeReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
        }
        public short type(){return kGame2Cli_JoinAgeReply;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
        }
    }

    public static class JoinAgeRequest extends GameMsg
    {
        int transId;
        int ageMcpId; //matches the value we game them earlier.
        Guid accountGuid;
        int playerIdx;

        public JoinAgeRequest(IBytestream c)
        {
            transId = c.readInt();
            ageMcpId = c.readInt();
            accountGuid = new Guid(c);
            playerIdx = c.readInt();
        }
        public JoinAgeRequest(){}
        public short type(){return GameMainServer.kCli2Game_JoinAgeRequest;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(ageMcpId);
            accountGuid.write(c);
            c.writeInt(playerIdx);
        }
    }

    public static abstract class GameMsg extends Server.ServerMsg
    {
        public abstract short type();

        public byte[] GetMsgBytes()
        {
            IBytedeque c = new Bytedeque(Format.moul);
            c.writeShort(this.type());
            this.write(c);
            byte[] data = c.getAllBytes();
            return data;
        }
    }

    static void SendMsg(ConnectionState cs, GameMsg msg)
    {
        //if(ConnectServer.allatonce)
        //{
            Bytedeque c = new uru.Bytedeque(Format.moul);
            c.writeShort(msg.type());
            msg.write(c);
            byte[] data = c.getAllBytes();

            //sock.out.writeBytes(data);
            //sock.out.flush();
            cs.sendMsgBytes(data);
        //}
        //else
        //{
        //    m.throwUncaughtException("deprecated");
        //    //sock.out.writeShort(msg.type());
        //    //msg.write(sock.out);
        //    //sock.out.flush();
        //}
    }

}

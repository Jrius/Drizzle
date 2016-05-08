/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import shared.*;
import uru.server.*;
import prpobjects.uruobj;
import uru.Bytedeque;
//import moulserver.ConnectServer.ServerThread;
import java.util.Queue;
import moulserver.Comm.*;
//import moulserver.NetServer.ConnectionState;

public class GateServer extends Thread
{
    public static final int kCli2GateKeeper_PingRequest = 0;
    public static final int kCli2GateKeeper_FileSrvIpAddressRequest = 1;
    public static final int kCli2GateKeeper_AuthSrvIpAddressRequest = 2;
    public static final int kCli2GateKeeper_LastMessage = 3;

    public static final int kGateKeeper2Cli_PingReply = 0;
    public static final int kGateKeeper2Cli_FileSrvIpAddressReply = 1;
    public static final int kGateKeeper2Cli_AuthSrvIpAddressReply = 2;
    public static final int kGateKeeper2Cli_LastMessage = 3;

    //public static void HandleGateServer(ServerThread sock)
    //{
    //    m.throwUncaughtException("deprecated");
    //}
    public void HandleMessage(Object msg, ConnectionState connstate)
    {
        //IBytestream c = sock.in;

        //short type = c.readShort();

        Class klass = msg.getClass();

        if(klass==FileSrvIpAddressRequest.class)
        {
            m.msg("GateServer_FileSrvIpAddressRequest");
            FileSrvIpAddressRequest request = (FileSrvIpAddressRequest)msg;

            FileSrvIpAddressReply reply = new FileSrvIpAddressReply();
            reply.transId = request.transId;
            String domainname = manager.settings.getDomainName();
            reply.address = new Str(domainname);
            SendMsg(connstate,reply);
        }
        else
        {
            m.throwUncaughtException("Unhandled GateServer msg: "+klass.getSimpleName());
        }
    }


    Queue<Comm.CommItem> items = Concurrent.getConcurrentQueue();
    Manager manager;
    public GateServer(Manager manager)
    {
        this.manager = manager;
    }

    public void run()
    {
        m.msg("Starting GateServer...");

        while(true)
        {
            CommItem item = items.poll();
            if(item!=null)
            {
                if(item.type==CommItemType.HandleMessage)
                {
                    HandleMessage(item.msg,item.connstate);
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

    public static GateServerMsg ReadMessage(IBytestream c, boolean isServer)
    {

        short type = c.readShort();

        if(isServer)
        {
            switch(type)
            {
                case GateServer.kCli2GateKeeper_FileSrvIpAddressRequest:
                    return new FileSrvIpAddressRequest(c);
                case GateServer.kCli2GateKeeper_AuthSrvIpAddressRequest:
                    return new AuthSrvIpAddressRequest(c);
                default:
                    throw new shared.uncaughtexception("Unread GateServer msg: "+Short.toString(type));
            }
        }
        else
        {
            switch(type)
            {
                case GateServer.kGateKeeper2Cli_FileSrvIpAddressReply:
                    return new FileSrvIpAddressReply(c);
                case GateServer.kGateKeeper2Cli_AuthSrvIpAddressReply:
                    return new AuthSrvIpAddressReply(c);
                default:
                    throw new shared.uncaughtexception("Unread GateServer msg: "+Short.toString(type));
            }
        }

    }

    public static void SendMsg(ConnectionState connstate, GateServerMsg msg)
    {
        IBytedeque c = new Bytedeque(Format.moul);
        c.writeShort(msg.type());
        msg.write(c);
        byte[] data = c.getAllBytes();
        
        //out.writeBytes(data);
        //out.flush();
        connstate.sendMsgBytes(data);
    }

    public abstract static class GateServerMsg extends Server.ServerMsg
    {
        abstract public short type();

        public byte[] GetMsgBytes()
        {
            IBytedeque c = new Bytedeque(Format.moul);
            c.writeShort(this.type());
            this.write(c);
            byte[] data = c.getAllBytes();
            return data;
        }

    }

    public static class FileSrvIpAddressReply extends GateServerMsg
    {
        int transId;
        //Utf16.Sized16 address;
        Str address;

        public FileSrvIpAddressReply(){}

        public FileSrvIpAddressReply(IBytestream c)
        {
            transId = c.readInt();
            //address = new Utf16.Sized16(c);
            address = Str.readAsUtf16Sized16(c);
        }
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            //address.write(c);
            address.writeAsUtf16Sized16(c);
        }

        public short type(){ return kGateKeeper2Cli_FileSrvIpAddressReply; }
        public Integer transid(){return transId;}
    }

    public static class FileSrvIpAddressRequest extends GateServerMsg
    {
        int transId; //1
        byte u1; //1

        public FileSrvIpAddressRequest(){}
        public FileSrvIpAddressRequest(IBytestream c)
        {
            transId = c.readInt();
            u1 = c.readByte();
        }
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeByte(u1);
        }
        public short type(){ return kCli2GateKeeper_FileSrvIpAddressRequest; }
        public Integer transid(){return transId;}
    }

    //you can't actually use these auth server requests on Cyan's servers.
    public static class AuthSrvIpAddressRequest extends GateServerMsg
    {
        int transId; //1

        public AuthSrvIpAddressRequest(){}
        public AuthSrvIpAddressRequest(IBytestream c)
        {
            transId = c.readInt();
        }
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
        }
        public short type(){ return kCli2GateKeeper_AuthSrvIpAddressRequest; }
        public Integer transid(){return transId;}
    }
    public static class AuthSrvIpAddressReply extends GateServerMsg
    {
        int transId;
        Str address;

        public AuthSrvIpAddressReply(){}

        public AuthSrvIpAddressReply(IBytestream c)
        {
            transId = c.readInt();
            address = Str.readAsUtf16Sized16(c);
        }
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            address.writeAsUtf16Sized16(c);
        }

        public short type(){ return kGateKeeper2Cli_AuthSrvIpAddressReply; }
        public Integer transid(){return transId;}
    }
}

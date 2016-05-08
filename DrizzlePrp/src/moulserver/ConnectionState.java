/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.net.SocketException;
import java.util.Queue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import shared.*;
import uru.Bytedeque;
import prpobjects.uruobj;
import prpobjects.Guid;
import moulserver.Comm.*;
import moulserver.Server.*;
import java.lang.ref.WeakReference;

//had MessageReaderThread
public class ConnectionState extends Thread
{
    AbstractManager manager;

    //Socket things
    Socket sock;
    ServerType serverType;
    //MessageReaderThread reader;
    byte[] ipAddress;
    int port;
    InputStream _in2;
    OutputStream _out2;
    IBytestream in;
    IBytedeque out;
    //Queue<Object> msgQueue = Concurrent.getConcurrentQueue();
    //Queue<Comm.CommItem> items = new ImmediateQueue();
    //Queue<Comm.CommItem> items = new java.util.ArrayDeque();
    final java.util.concurrent.LinkedBlockingQueue<CommItem> items = Concurrent.getConcurrentBlockingQueue();
    public final WeakReference<Queue<CommItem>> weakitems = new WeakReference(items);
    NetworkHeader header;
    //Object extra;
    WorkerThread worker;

    //server things
    ChunkSendHandler chunksendhandler = new ChunkSendHandler(); //used for file and auth servers. They will each have a separate ConnectionState, so this is fine.
    Integer serverchallenge = null; // set at Cli2Auth_ClientRegisterRequest, read at Cli2Auth_AcctLoginRequest
    Database.accountinfo account = null;
    Integer playerId = null; //0 means in-game, but unset player. I.e. the StartUp Age.
    Integer playerInfoIdx = null;
    Node.PlayerNode player = null;
    Comm.Listener listener = null; //only used by AuthServer
    NodeManager nodemgr = null;
    //Guid currentGameServer = null; //used by AuthServer and GameMainServer (the guid is the ageInstanceGuid.
    java.lang.ref.WeakReference<GameServer> currentGameServer;


    public class WorkerThread extends Thread
    {
        public void run()
        {
            while(true)
            {
                //blocks here:
                try{
                    CommItem item = items.take();
                    //m.msg("item taken");

                    //handle the item:
                    if(item.type==CommItemType.SendMessage)
                    {
                        m.msg("WorkerThread sending message: "+item.msg.dump());
                        ConnectionState.this.sendMsgBytes(item.msg.GetMsgBytes());
                    }
                    else if(item.type==CommItemType.Terminate)
                    {
                        m.throwUncaughtException("Unhandled terminate command.");
                        break;
                    }
                    else
                    {
                        m.throwUncaughtException("Unable to handle CommItem: "+item.type.toString());
                    }
                }
                catch(InterruptedException e)
                {
                    //that's fine; do nothing.
                }
            }
        }
    }

    public class ImmediateQueue extends java.util.ArrayDeque<CommItem>
    {
        public boolean add(CommItem e)
        {
            if(e.type==CommItemType.NodeChange)
            {
                //send msg
                AuthServer.AuthMsg msg = (AuthServer.AuthMsg)e.msg;
                AuthServer.SendMsg(ConnectionState.this, msg);
            }
            sendMsgBytes(new byte[]{});
            return true;
        }
    }
    public void terminate()
    {
        try{
            sock.close();
        }catch(Exception e){
            throw new shared.nested(e);
        }
    }

    public ConnectionState(Socket sck, AbstractManager manager)
    {
        this.manager = manager;
        sock = sck;
        serverType = ServerType.Connect;
        try{
            _in2 = sock.getInputStream();
            _out2 = sock.getOutputStream();
            in = new StreamBytestream(_in2);
            out = new StreamBytedeque(_out2);
        }catch(IOException e){
            throw new shared.nested(e);
        }
        //reader = new MessageReaderThread(this);
        ipAddress = sck.getInetAddress().getAddress();
        port = sck.getPort();
        worker = new WorkerThread();
        worker.start();
    }

    private ConnectionState(){}
    public static ConnectionState dummy()
    {
        return new ConnectionState();
    }

    public void startReader()
    {
        //reader.start();
        this.start();
    }

    public void setAccount(Database.accountinfo acct)
    {
        //todo: map this ip to this account guid.
        account = acct;
    }

    public void setPlayerIdx(int playerIdx2)
    {
        //todo: map this ip to this playerIdx
        playerId = playerIdx2;
    }

    public synchronized void sendMsgBytes(byte[] msgBytes)
    {
        try{
            out.writeBytes(msgBytes);
            out.flush();
        }catch(Exception e){
            Exception root = nested.getRootOfException(e);
            if(root instanceof SocketException && root.getMessage().equals("Connection reset"))
            {
                manager.removeConnectionState(this);
                try{
                    this.sock.close();
                }catch(Exception e2){
                    m.msg("Error while closing connection.");
                    e2.printStackTrace();
                }
                m.msg("Client disconnected.");
            }
            else
            {
                e.printStackTrace();
            }
        }
    }

    //ConnectionState connstate;

    //public MessageReaderThread(ConnectionState connstate)
    //{
    //    this.connstate = connstate;
    //}

    public void run()
    {
        try
        {
            //do initial work with finding out the server type and setting up encryption
            HandleConnect();

            //main loop for this server type:
            while(true)
            {
                ServerMsg msg = ReadMsg();
                //m.msg(msg.toString());
                manager.HandleMessage(serverType, msg, this);
                //connstate.msgQueue.add(msg);
            }

        }
        catch(Exception e)
        {
            //todo: check if this is a nested exception with a IOException for a connection reset
            //and if so, remove this ConnectionState from the list, and break out of this loop.
            Exception root = nested.getRootOfException(e);
            if(root instanceof SocketException && root.getMessage().equals("Connection reset"))
            {
                manager.removeConnectionState(this);
                try{
                    this.sock.close();
                }catch(Exception e2){
                    m.msg("Error while closing connection.");
                    e2.printStackTrace();
                }
                m.msg("Client disconnected.");
            }
            else if(root instanceof SocketException && root.getMessage().equals("socket closed"))
            {
                manager.removeConnectionState(this);
                try{
                    this.sock.close();
                }catch(Exception e2){
                    m.msg("Error while closing connection.");
                    e2.printStackTrace();
                }
                m.msg("Server disconnected.");
            }
            else
            {
                e.printStackTrace();
            }
        }
    }

    private ServerMsg ReadMsg()
    {
        switch(serverType)
        {
            //case Connect:
            //    HandleConnect();
            //    break;
            //case NetCli:
            //    HandleNetCliPacket(in);
            //    break;
            case GateServer:
                return GateServer.ReadMessage(in,true);
            case FileServer:
                return FileServer.ReadMessage(in,true);
            case AuthServer:
                return AuthServer.ReadMessage(in,true);
            case GameServer:
                return GameMainServer.ReadMessage(in,true);
            default:
                throw new shared.uncaughtexception("Unhandled state");
        }

    }

    private void HandleConnect()
    {
        header = new NetworkHeader(in);

        //int size = in.readInt();
        //we could read the whole thing in here, if we wanted. byte[size-4]

        if(header.type==NetworkHeader.kConnTypeCliToFile)
        {
            //File server header:
            //FileConnHeader connheader = new FileConnHeader(in);

            serverType = ServerType.FileServer;
        }
        else if(header.type==NetworkHeader.kConnTypeCliToGate)
        {
            //Gate server header:
            //GateConnHeader connheader = new GateConnHeader(in);

            //set up encryption:
            HandleNetCliPacket(in,Version.talcum.gateserver_B);

            serverType = ServerType.GateServer;
        }
        else if(header.type==NetworkHeader.kConnTypeCliToAuth)
        {
            //Auth server header:
            //int size = in.readInt(); //20
            //byte[] nullguid = in.readBytes(16); //should be all 0. redirection ip address and port and something else?
            //AuthConnHeader connheader = new AuthConnHeader(in);

            HandleNetCliPacket(in,Version.talcum.authserver_B);
            serverType = ServerType.AuthServer;
        }
        else if(header.type==NetworkHeader.kConnTypeCliToGame)
        {
            //Game server header:
            //GameConnHeader gameheader = new GameConnHeader(in);

            HandleNetCliPacket(in,Version.talcum.gameserver_B);
            serverType = ServerType.GameServer;
        }
        else
        {
            m.throwUncaughtException("unhandled server type: ",Byte.toString(header.type));
        }
    }

    public void HandleNetCliPacket(IBytestream c, String B)
    {

        //receive encryption info:
        byte type = c.readByte();
        byte msgsize = c.readByte(); //e.ensure(msgsize==66);//always 66=0x42

        //this works perfectly, so we don't need this debugging feature, which is slower
        //if(readfirst)
        //{
        //    byte[] msg = c.readBytes(b.ByteToInt32(msgsize)-2);
        //    c = shared.ByteArrayBytestream.createFromByteArray(msg);
        //}

        if(type==NetServer.kNetCliCli2SrvConnect)
        {
            NetCliConnectMsg request = new NetCliConnectMsg(c);

            //calculate encryption info
            //_a is client private key.
            //_b is server private key.
            //_base is the base.
            //_A is base^a (the client public key)
            //_B is base^b (the server public key)
            //_mod is the modulo.
            //_key is base^ab (the shared secret key)

            //client calculations:
            //LargeInteger _B = new LargeInteger(enc_x_moul);
            //LargeInteger _mod = new LargeInteger(enc_n_moul);
            //LargeInteger _a = new LargeInteger(512,rng);
            //LargeInteger _base = new LargeInteger(4);
            //LargeInteger _key = _B.modPow(_a, _mod);
            //LargeInteger _A = _base.modPow(_a, _mod);

            //server calculations:
            LargeInteger _mod = new LargeInteger(Version.talcum.gateserver_mod);
            //LargeInteger _base = new LargeInteger(base);
            //LargeInteger _B = new LargeInteger(B); //(ServerShared.enc_B_talcum);
            LargeInteger _A = new LargeInteger(request.A_bytes);
            LargeInteger _b = new LargeInteger(Version.enc_b_talcum);//SharedServer.enc_b_talcum);
            LargeInteger _key = _A.modPow(_b, _mod);
            byte[] dhkey = _key.toBytes(64);

            //prepare the message with the key.
            byte[] rc4key = new byte[]{0,0,0,0,0,0,0}; //great key :D  But we could pick anything here, i.e. a random key.
            //rc4key = new byte[]{4,3,9,1,9,8,1};
            byte[] keymsg = new byte[7];
            for(int i=0;i<7;i++)
            {
                keymsg[i] = (byte)(rc4key[i]^dhkey[i]);
            }


            //respond with encryption info:
            NetCliEncryptMsg reply = new NetCliEncryptMsg();
            reply.keymsg = keymsg;
            //byte len = (byte)(keymsg.length+2);
            //out.writeByte(msg); //-1 would mean can't connect or something. 1 means can connect.
            //out.writeByte(len);
            //out.writeBytes(keymsg); //7 bytes
            reply.SendNetCliMsg(out);

            //encrypt our connection now:
            org.bouncycastle.crypto.engines.RC4Engine rc4in = new org.bouncycastle.crypto.engines.RC4Engine();
            org.bouncycastle.crypto.engines.RC4Engine rc4out = new org.bouncycastle.crypto.engines.RC4Engine();
            org.bouncycastle.crypto.params.KeyParameter keyparams = new org.bouncycastle.crypto.params.KeyParameter(rc4key);
            rc4in.init(true, keyparams);
            rc4out.init(true, keyparams);
            InputStream in_sub = in.getChildStreamIfExists();
            OutputStream out_sub = out.getChildStreamIfExists();
            in = new CryptoBytestream(in_sub,rc4in);
            out = new CryptoBytedeque(out_sub,rc4out);

            //state = Handler.GateServer;
            m.msg("ConnectServer: now encrypted with rc4key: "+b.BytesToHexString(rc4key));

        }
        else
        {
            m.err("Unhandled NetCliServer msg: ",Byte.toString(type));
        }

    }

    //public static class ConnectionState
    //{
//
    //}
    //Connection header messages
    public static abstract class ConnHeader extends uruobj
    {
        /*public void SendMsg(Sock sock)
        {
            Bytedeque c = new Bytedeque(Format.moul);
            c.writeInt(0); //overwrite
            this.write(c);
            byte[] data = c.getAllBytes();
            b.loadInt32IntoBytes(data.length, data, 0); //set length
            sock.out.writeBytes(data);
            sock.out.flush();
        }*/
        public byte[] getbytes()
        {
            Bytedeque c = new Bytedeque(Format.moul);
            //c.writeInt(0); //overwrite
            this.write(c);
            byte[] data = c.getAllBytes();
            //b.loadInt32IntoBytes(data.length, data, 0); //set length

            return data;
        }
    }

    public static class GameConnHeader extends ConnHeader
    {
        Guid accountGuid;
        Guid ageGuid;

        public GameConnHeader(IBytestream c)
        {
            accountGuid = new Guid(c);
            ageGuid = new Guid(c);
        }
        public GameConnHeader(){}
        public static GameConnHeader createWithInfo(Guid accountGuid, Guid ageInstanceGuid)
        {
            GameConnHeader r = new GameConnHeader();
            r.accountGuid = accountGuid;
            r.ageGuid = ageInstanceGuid;
            return r;
        }
        public void write(IBytedeque c)
        {
            accountGuid.write(c);
            ageGuid.write(c);
        }
    }

    public static class AuthConnHeader extends ConnHeader
    {
        //int size; //20
        byte[] nullguid; //byte[16] //should be all 0. redirection ip address and port and something else?

        public AuthConnHeader(IBytestream c)
        {
            //size = c.readInt();
            nullguid = c.readBytes(16);
        }
        public AuthConnHeader(){}
        public static AuthConnHeader createDefault()
        {
            AuthConnHeader r = new AuthConnHeader();
            r.nullguid = Guid.none();
            return r;
        }

        public void write(IBytedeque c)
        {
            if(nullguid.length!=16) m.throwUncaughtException("unexpected");
            c.writeBytes(nullguid);
        }
    }
    public static class FileConnHeader extends ConnHeader
    {
        //int size; //12
        int u2; //0 //redirection ip address and port? No.
        int u3; //0

        public FileConnHeader(IBytestream c)
        {
            //size = c.readInt();
            //super(c);
            u2 = c.readInt();
            u3 = c.readInt();
        }
        public void write(IBytedeque c)
        {
            //super.write(c);
            //c.writeInt(size);
            c.writeInt(u2);
            c.writeInt(u3);
        }
        private FileConnHeader(){}
        public static FileConnHeader createDefault()
        {
            FileConnHeader r = new FileConnHeader();
            r.u2 = 0;
            r.u3 = 0;
            return r;
        }
        public byte[] GetMsgBytes()
        {
            Bytedeque c = new Bytedeque(Format.moul);
            write(c);
            return c.getAllBytes();
            //set the size
        }
    }
    public static class GateConnHeader extends ConnHeader
    {
        //int size; //20
        public Guid nullguid; //should be all 0. redirection ip address and port and something else?

        public GateConnHeader(IBytestream c)
        {
            //size = c.readInt();
            //nullguid = c.readBytes(16);
            nullguid = new Guid(c);
        }
        private GateConnHeader(){}
        public static GateConnHeader createDefault()
        {
            GateConnHeader r = new GateConnHeader();
            r.nullguid = Guid.none2();
            return r;
        }
        public void write(IBytedeque c)
        {
            //c.writeInt(0); //overwrite this later
            //if(nullguid.length!=16) m.throwUncaughtException("unexpected");
            //c.writeBytes(nullguid);
            nullguid.write(c);
        }

    }
    public static class NetworkHeader extends Server.ServerMsg
    {

        //possible values of type:
        static final byte kConnTypeNil = (byte)0;
        static final byte kConnTypeDebug = (byte)1;
        static final byte kConnTypeCliToAuth = (byte)10;
        static final byte kConnTypeCliToGame = (byte)11;
        static final byte kConnTypeCliToFile = (byte)16;
        static final byte kConnTypeCliToGate = (byte)22;
        static final byte kConnTypeSrvToVault = (byte)100;
        static final byte kConnTypeSrvToLookup = (byte)101;
        static final byte kConnTypeCliToAdmin = (byte)102;

        byte type;
        short sockHeaderSize;
        int buildID;
        int buildType;
        int branchId;
        Guid productID;

        ConnHeader subheader;

        public NetworkHeader(IBytestream c)
        {
            type = c.readByte();
            sockHeaderSize = c.readShort(); //size of this object
            buildID = c.readInt(); //0 for Moul //893 for Moulagain 1.893
            buildType = c.readInt(); //50 for Moul //50 for Moulagain 1.893
            branchId = c.readInt(); //9 for Moul //1 for Moulagain 1.893
            //productID = c.readBytes(16);
            productID = new Guid(c);
            //m.msg(b.BytesToHexString(productID));

            if(type==kConnTypeCliToGate)
            {
                int size = c.readInt();
                subheader = new ConnectionState.GateConnHeader(c);
            }
            else if(type==kConnTypeCliToFile)
            {
                int size = c.readInt();
                subheader = new ConnectionState.FileConnHeader(c);
            }
            else if(type==kConnTypeCliToAuth)
            {
                int size = c.readInt();
                subheader = new ConnectionState.AuthConnHeader(c);
            }
            else if(type==kConnTypeCliToGame)
            {
                int size = c.readInt();
                subheader = new ConnectionState.GameConnHeader(c);
            }
            else
            {
                m.throwUncaughtException("unhandled");
            }
        }

        public static NetworkHeader createGateHeader(Version ver)
        {
            NetworkHeader r = new NetworkHeader();
            r.type = kConnTypeCliToGate;
            r.buildID = ver.buildId;
            r.buildType = ver.buildType;
            r.branchId = ver.branchId;
            r.productID = ver.productId;
            r.subheader = GateConnHeader.createDefault();
            return r;
        }
        public static NetworkHeader createFileHeader(Version ver)
        {
            NetworkHeader r = new NetworkHeader();
            r.type = kConnTypeCliToFile;
            r.buildID = ver.buildId;
            r.buildType = ver.buildType;
            r.branchId = ver.branchId;
            r.productID = ver.productId;
            r.subheader = FileConnHeader.createDefault();
            return r;
        }
        public static NetworkHeader createAuthHeader(Version ver)
        {
            NetworkHeader r = new NetworkHeader();
            r.type = kConnTypeCliToAuth;
            r.buildID = ver.buildId;
            r.buildType = ver.buildType;
            r.branchId = ver.branchId;
            r.productID = ver.productId;
            r.subheader = AuthConnHeader.createDefault();
            return r;
        }
        public static NetworkHeader createGameHeader(Version ver, Guid accountGuid, Guid ageInstanceGuid)
        {
            NetworkHeader r = new NetworkHeader();
            r.type = kConnTypeCliToGame;
            r.buildID = ver.buildId;
            r.buildType = ver.buildType;
            r.branchId = ver.branchId;
            r.productID = ver.productId;
            r.subheader = GameConnHeader.createWithInfo(accountGuid, ageInstanceGuid);
            return r;
        }

        public void write(IBytedeque c)
        {
            final short size = 31; //=1+2+4+4+4+16;  It's fixed, but I don't like this :P

            c.writeByte(type);
            //c.writeShort((short)0); //overwrite this
            c.writeShort(size);
            c.writeInt(buildID);
            c.writeInt(buildType);
            c.writeInt(branchId);
            //c.writeBytes(productID);
            productID.write(c);
            
            //subheader
            byte[] subheaderbs = subheader.getbytes(); //without the size.
            int size2 = subheaderbs.length+4;
            c.writeInt(size2); //size of subheader
            c.writeBytes(subheaderbs);

        }

        public NetworkHeader(){}
        public byte[] GetMsgBytes()
        {
            Bytedeque c = new Bytedeque(Format.moul);
            this.write(c);
            return c.getAllBytes();

            /*c.writeByte(type);
            c.writeShort((short)0); //overwrite this
            c.writeInt(buildID);
            c.writeInt(buildType);
            c.writeInt(branchId);
            //c.writeBytes(productID);
            productID.write(c);

            byte[] data = c.getAllBytes();
            b.loadInt16IntoBytes((short)data.length, data, 1); //set size

            return data;*/
        }

    }


    //Encryption related messages
    static abstract class CliNetServerMsg extends Server.ServerMsg
    {
        abstract byte type();

        public void SendNetCliMsg(IBytedeque out)
        {
            final boolean allatonce = true;
            if(allatonce)
            {
                Bytedeque c = new uru.Bytedeque(Format.moul);
                c.writeByte(this.type());
                c.writeByte((byte)0);
                this.write(c);
                byte[] data = c.getAllBytes();
                data[1] = (byte)(data.length);
                out.writeBytes(data);
                out.flush();
            }
            else
            {
                Bytedeque c = new uru.Bytedeque(Format.moul);
                this.write(c);
                byte[] data = c.getAllBytes();
                byte type = this.type();
                int len2 = data.length+2;
                byte len = (byte)len2;

                out.writeByte(type);
                out.writeByte(len);
                out.writeBytes(data);
                out.flush();
            }

        }

        public byte[] GetMsgBytes()
        {
            Bytedeque c = new Bytedeque(Format.moul);
            c.writeByte(this.type());
            //c.writeInt(0); //overwrite this size
            c.writeByte((byte)0); //overwrite this size
            this.write(c);
            byte[] r = c.getAllBytes();
            //b.Int32IntoBytes(r.length, r, 0);
            r[1] = (byte)(r.length);
            return r;
        }
    }
    public static class NetCliConnectMsg extends CliNetServerMsg
    {
        byte[] A_bytes; //64 byte public client key.

        public NetCliConnectMsg(){}

        public NetCliConnectMsg(IBytestream c)
        {
            A_bytes = c.readBytes(64);
        }

        public void write(IBytedeque c)
        {
            if(A_bytes.length!=64) m.throwUncaughtException("unexpected");
            c.writeBytes(A_bytes);
        }

        public byte type() { return NetServer.kNetCliCli2SrvConnect; }
    }
    public static class NetCliEncryptMsg extends CliNetServerMsg
    {
        byte[] keymsg; //7 byte dh-encrypted rc4 key
        public NetCliEncryptMsg(IBytestream c)
        {
            keymsg = c.readBytes(7);
        }
        public NetCliEncryptMsg(){}
        public void write(IBytedeque c)
        {
            c.writeBytes(keymsg);
        }
        public byte type() { return NetServer.kNetCliSrv2CliEncrypt; }

    }

}


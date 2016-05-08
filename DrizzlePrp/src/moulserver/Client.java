/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import shared.Str;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import moulserver.Comm.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.InterruptedException;
import java.io.InputStream;
import java.io.OutputStream;
import moulserver.Server.*;
import shared.*;
import moulserver.ConnectionState.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedQueue;
import moulserver.GateServer.*;
import moulserver.AuthServer.*;
import moulserver.FileServer.*;
import moulserver.GameMainServer.*;
import java.util.ArrayList;
import java.net.SocketException;
import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class Client
{
    AcctLoginReply logindetails;
    //MsgHandler msghandler;

    public static interface MsgHandler
    {
        void HandleMsg(ServerMsg msg, Client.ClientConnection caller);
    }
    public class AuthConnection extends ClientConnection
    {
        public List<AcctPlayerInfo> players = null;

        public AuthConnection(Version ver, String hostname)
        {
            super(ver,ServerType.AuthServer,hostname);
            msghandler = new Client.MsgHandler() {
                public void HandleMsg(ServerMsg msg, Client.ClientConnection caller) {
                    //m.msg("HandleMsg: "+msg.getClass().getName());
                    if(msg instanceof AuthServer.AcctPlayerInfo)
                    {
                        players.add((AcctPlayerInfo)msg);
                    }
                }
            };
            header = ConnectionState.NetworkHeader.createAuthHeader(ver);
            doEncrypt = true;
            server_B = ver.authserver_B;
            server_mod = ver.authserver_mod;
            server_base = ver.authserver_base;

            Connect();
        }
        //public void HandleMsg(ServerMsg msg)
        //{
        //}
        public boolean Login(String username, String password)
        {
            ClientRegisterRequest reg = new ClientRegisterRequest();
            reg.buildId = ver.buildId;
            ClientRegisterReply regreply = SendMsgAndWaitForClass(reg, ClientRegisterReply.class); //it has no transid, so we just wait for any reply of this class.
            //we also get sent a ServerAddr here.

            AcctLoginRequest request = new AcctLoginRequest();
            request.transId = Client.this.getNextTransid();
            request.clientchallenge = 0;
            request.accountName = new Str(username);
            request.setPassword(username,password,regreply.serverchallenge,request.clientchallenge);
            request.authToken = new Str("");
            request.OS = new Str("win");
            //players = new ArrayList();
            players = Concurrent.getThreadsafeList();
            AcctLoginReply logreply = SendMsgAndWaitForClass(request,AcctLoginReply.class); //this has a transid, but the transid is also used for the playerinfo messages.  We *should* make a WaitForTransidAndClass function.
            
            Client.this.logindetails = logreply;
            if(logreply.result==ENetError.kNetSuccess) return true;
            else return false;
            //return true;
        }
        public void GetNodeWithTransidCallback(int nodeIdx, Concurrent.Callback<ServerMsg> callback) //returns immediately
        {
            AuthServer.VaultNodeFetch request = new AuthServer.VaultNodeFetch();
            request.transId = Client.this.getNextTransid();
            request.nodeId = nodeIdx;
            SendMsgAndRegisterTransidCallback(request,callback);
        }
        public ClientNodesHelper.NodesInfo GetNodes(int rootNodeIdx)
        {
            return ClientNodesHelper.GetNodes(this, rootNodeIdx);
        }
        public ArrayList<byte[]> GetRawNodes(int rootNodeIdx)
        {
            return ClientNodesHelper.GetRawNodes(this, rootNodeIdx);
        }
        public <T extends Node> T GetNode(int nodeIdx)
        {
            AuthServer.VaultNodeFetch request = new AuthServer.VaultNodeFetch();
            request.transId = Client.this.getNextTransid();
            request.nodeId = nodeIdx;
            AuthServer.VaultNodeFetched reply = SendMsgAndWaitForTransid(request);
            T node = (T)Node.getNode(reply.data);
            return node;
        }
        public ArrayList<Node.Ref> GetNodeRefs(int nodeIdx)
        {
            AuthServer.VaultFetchNodeRefs req = new AuthServer.VaultFetchNodeRefs();
            req.transId = Client.this.getNextTransid();
            req.nodeId = nodeIdx;
            AuthServer.VaultNodeRefsFetched reply = SendMsgAndWaitForTransid(req);
            return reply.refs;
        }
        public AcctPlayerInfo GetPlayer(String playername)
        {
            playername = playername.toLowerCase();
            for(AcctPlayerInfo player: players)
            {
                if(player.playerName.toString().toLowerCase().equals(playername)) return player;
            }
            return null;
        }
        public ArrayList<moulserver.SecureDownloadManifest.Entry> GetDirList(String dir, String ext)
        {
            //dir is e.g. sdl or python.
            AuthServer.FileListRequest request = new AuthServer.FileListRequest();
            request.transId = Client.this.getNextTransid();
            request.dir = new Str(dir);
            request.extension = new Str(ext);
            AuthServer.FileListReply reply = SendMsgAndWaitForTransid(request);
            return reply.manifest.entries;
        }
        public byte[] GetFile(String filename)
        {
            return GetFile(filename, Client.this.logindetails.encryptionKey);
        }
        public byte[] GetFile(String filename, int[] encryptionKey)
        {
            byte[] encdata = GetFileRaw(filename);
            byte[] data = uru.UruCrypt.DecryptNotthedroids(encdata, encryptionKey);
            return data;
        }
        public byte[] GetFileRaw(String filename)
        {
            AuthServer.FileDownloadRequest request = new AuthServer.FileDownloadRequest();
            request.transId = Client.this.getNextTransid();
            if(gui.Version.debug) m.msg("Transid="+Integer.toString(request.transId));
            request.filename = new Str(filename);
            AuthServer.FileDownloadChunk reply = SendMsgAndWaitForTransid(request);
            if(gui.Version.debug) m.msg("Transid="+Integer.toString(reply.transId));
            
            //copy bytes to result
            byte[] result = new byte[reply.filesize];
            int curpos = 0;
            b.CopyBytes(reply.buffer, result, reply.chunkOffset);
            curpos += reply.buffer.length;

            float percentage = 100.0f*((float)curpos / (float)result.length);
            m.msg(String.format("%6.1f%% done. (%d bytes out of %d)", percentage,curpos,result.length));

            //send ack
            while(curpos!=result.length)
            {
                AuthServer.FileDownloadChunkAck ack = new AuthServer.FileDownloadChunkAck();
                ack.transId = reply.transId; //keep using the same transid
                AuthServer.FileDownloadChunk reply2 = SendMsgAndWaitForTransid(ack);
                if(gui.Version.debug) m.msg("Transid="+Integer.toString(reply2.transId));

                //copy bytes into result
                b.CopyBytes(reply2.buffer, result, reply2.chunkOffset);
                curpos += reply2.buffer.length;

                percentage = 100.0f*((float)curpos / (float)result.length);
                m.msg(String.format("%6.1f%% done. (%d bytes out of %d)", percentage,curpos,result.length));
            }

            AuthServer.FileDownloadChunkAck finalack = new AuthServer.FileDownloadChunkAck();
            finalack.transId = reply.transId;
            SendMsg(finalack);

            return result;
        }
    }

    public class GateConnection extends ClientConnection
    {
        public GateConnection(Version ver)
        {
            super(ver,ServerType.GateServer,ver.gateserver);
            header = ConnectionState.NetworkHeader.createGateHeader(ver);
            doEncrypt = true;
            server_B = ver.gateserver_B;
            server_mod = ver.gateserver_mod;
            server_base = ver.gateserver_base;

            Connect();
        }
        public String GetFileSrvIp()
        {
            GateServer.FileSrvIpAddressRequest request = new GateServer.FileSrvIpAddressRequest();
            request.transId = Client.this.getNextTransid();
            request.u1 = 1;
            //SendMsg(request);

            //Trigger trigger = new Trigger();
            //this.waiters.put(transid, trigger);

            m.msg("waiting for filesrvip");
            //ServerMsg msg = trigger.await();
            //FileSrvIpAddressReply reply = (FileSrvIpAddressReply)msg;
            FileSrvIpAddressReply reply = SendMsgAndWaitForTransid(request);
            return reply.address.toString();
        }
        @Deprecated public String GetAuthSrvIp() //this doesn't work on Plasma servers.
        {
            GateServer.AuthSrvIpAddressRequest request = new GateServer.AuthSrvIpAddressRequest();
            request.transId = Client.this.getNextTransid();
            AuthSrvIpAddressReply reply = SendMsgAndWaitForTransid(request);
            return reply.address.toString();
        }
        //public void HandleMsg(ServerMsg msg)
        //{
        //}
    }
    public class GameConnection extends ClientConnection
    {
        public GameConnection(Version ver, String gameserveraddress, prpobjects.Guid accountGuid, prpobjects.Guid ageInstanceGuid)
        {
            super(ver,ServerType.GameServer,gameserveraddress);
            header = ConnectionState.NetworkHeader.createGameHeader(ver, accountGuid, ageInstanceGuid);
            doEncrypt = true;
            server_B = ver.gameserver_B;
            server_mod = ver.gameserver_mod;
            server_base = ver.gameserver_base;

            Connect();
        }
    }

    public class FileConnection extends ClientConnection
    {
        public FileConnection(Version ver, String hostname)
        {
            super(ver,ServerType.FileServer,hostname);
            header = ConnectionState.NetworkHeader.createFileHeader(ver);
            doEncrypt = false;
            
            Connect();
        }
        //public void HandleMsg(ServerMsg msg)
        //{
            //Class klass = msg.getClass();
            //if(msg.transid()!=null)
            //{
            //    //Server.Transaction tmsg = (Server.Transaction)msg;
            //    TriggerTransid(msg,msg.transid());
            //}
            //if(klass==ManifestReply.class)
            //{
            //    ManifestReply reply = msg.cast();
            //    TriggerTransid(reply, reply.transId);
            //}
        //}
        public byte[] GetFile(uru.server.MoulFileInfo mfi)
        {
            byte[] filedata = GetRawFile(mfi.Downloadname.toString());
            //String filepath = outputpath+"/"+mfi.filename.toString();
            if(!mfi.Hash.toString().equals(mfi.CompressedHash.toString()))
            {
                filedata = shared.zip.decompressGzip(filedata); //decompress
            }
            //FileUtils.WriteFile(filepath, filedata, true, true); //create dirs and throw exception
            return filedata;
        }
        private byte[] GetRawFile(String filename)
        {
            //Note: the transids are probably messed up in here.  It doesn't seem to cause a problem though.

            //e.g. filename="game_clients\drcExplorer\ReleaseNotes.txt.gz"
            FileServer.FileDownloadRequest request = new FileServer.FileDownloadRequest();
            request.transId = Client.this.getNextTransid();
            request.filename = new Str(filename);
            request.buildId = 0;
            FileDownloadReply reply = SendMsgAndWaitForTransid(request);
            
            //copy bytes to result
            byte[] result = new byte[reply.filesize];
            int curpos = 0;
            b.CopyBytes(reply.buffer, result, curpos);
            curpos += reply.buffer.length;

            //send ack
            while(curpos!=result.length)
            {
                FileServer.FileDownloadChunkAck ack = new FileServer.FileDownloadChunkAck();
                ack.transId = Client.this.getNextTransid();
                ack.readerId = reply.readerId;
                FileDownloadReply reply2 = SendMsgAndWaitForTransid(ack);

                //copy bytes to result
                b.CopyBytes(reply2.buffer, result, curpos);
                curpos += reply2.buffer.length;

                //check if done
                //if(curpos==result.length)
                //{
                    //should we send an ack anyway here?  I think we should.
                    //FileServer.FileDownloadChunkAck finalack = new FileServer.FileDownloadChunkAck();
                    //finalack.transId = Client.this.getNextTransid();
                    //finalack.readerId = reply.readerId;
                    //SendMsg(finalack);

                //    break;
                //}
            }

            FileServer.FileDownloadChunkAck finalack = new FileServer.FileDownloadChunkAck();
            finalack.transId = Client.this.getNextTransid();
            finalack.readerId = reply.readerId;
            SendMsg(finalack);

            return result;
        }
        public ArrayList<uru.server.MoulFileInfo> GetManifest(String group)
        {
            //options are: "ExternalPatcher","ThinExternal","External".
            //ExternalPatcher is just UruLauncher.exe, ThinExternal is minimal files, and External is all files.
            if(!group.equals("ExternalPatcher")&&!group.equals("ThinExternal")&&!group.equals("External")) m.throwUncaughtException("invalid group");

            ArrayList<uru.server.MoulFileInfo> files = new ArrayList();

            //get the first manifest part
            FileServer.ManifestRequest request = new FileServer.ManifestRequest();
            request.transId = Client.this.getNextTransid();
            request.group = new Str(group);
            request.buildId = 0;
            ManifestReply reply = SendMsgAndWaitForTransid(request);
            int nummanifestparts = 1;

            //add the entries to our list
            files.addAll(reply.manifest.getFiles());
            if(files.size()==reply.manifest.count) return files;

            //FileServer.ManifestRequest request2 = new FileServer.ManifestRequest();
            //request2.transId = Client.this.getNextTransid();
            //request2.group = new Str(group);
            //request2.buildId = reply.readerId;
            //ManifestReply reply2 = SendMsgAndWaitForTransid(request2,request2.transId);
            while(true)
            {
                //ack this manifest part, and the server will send us the next part.
                FileServer.ManifestEntryAck ack = new ManifestEntryAck();
                ack.transId = Client.this.getNextTransid();
                ack.readerId = reply.readerId;
                ManifestReply reply2 = SendMsgAndWaitForTransid(ack);
                nummanifestparts++;

                //add the entries to our list
                files.addAll(reply2.manifest.getFiles());
                if(files.size()==reply.manifest.count) return files;
            }

            //return reply.manifest;
        }
    }

    public abstract static class ClientConnection
    {
        public MsgHandler msghandler = null;

        protected Version ver;
        protected ServerType type;
        protected Socket sock;
        //protected SocketChannel sockchan;
        protected InputStream _in2;
        protected OutputStream _out2;
        protected IBytestream in;
        protected IBytedeque out;
        protected ClientConnectionReadThread readthread;
        protected ClientConnectionWriteThread writethread;

        protected ConnectionState.NetworkHeader header;
        protected Boolean doEncrypt;
        protected String server_B;
        protected String server_mod;
        protected Integer server_base;
        protected byte[] dhkey;
        protected byte[] rc4key;
        protected boolean isConnected;


        public final LinkedBlockingQueue<CommItem> items = Concurrent.getConcurrentBlockingQueue();
        //public final Queue<CommItem> items = Concurrent.getConcurrentQueue();
        //public final ConcurrentHashMap<Class,Runnable> handlers = Concurrent.getConcurrentHashMap();
        //public final ConcurrentHashMap<Integer,Trigger> waiters = Concurrent.getConcurrentHashMap(); //Transid->trigger
        //public final ConcurrentHashMap<Object,Trigger> waiters = Concurrent.getConcurrentHashMap(); //Transid->trigger
        public final ConcurrentHashMap<Object,Waiter> waiters = Concurrent.getConcurrentHashMap(); //Transid->trigger

        //public abstract void HandleMsg(ServerMsg msg);
        public void Connect()
        {
            //start the threads
            readthread.start();
            writethread.start();

            //send the header
            SendMsg(header);
            //items.add(CommItem.SendMessage(header, null));

            if(doEncrypt)
            {
                SendEncryption();
                //Trigger trigger = new Trigger();
                //waiters.put(0, trigger);
                //m.msg("waiting for connection");
                //trigger.await();
            }

            m.msg("connected!");
        }

        public void Disconnect()
        {
            //kill the threads
            try{
                sock.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
            //try{
                //in.close();
            //}catch(Exception e){e.printStackTrace();}
            //try{
            //    out.close();
            //}catch(Exception e){e.printStackTrace();}
            try{
                _in2.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
            try{
                _out2.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
            try{
                readthread.join(400);
                if(readthread.isAlive())
                {
                    m.err("Thread hasn't died. Killing it...");
                    readthread.stop();
                }
            }catch(Exception e){
                //e.printStackTrace();
            }
            try{
                items.add(CommItem.Terminate());
                writethread.join(400);
                if(writethread.isAlive())
                {
                    m.err("Write thread hasn't died. Killing it...");
                    writethread.stop();
                }
            }catch(Exception e){
                //e.printStackTrace();
            }
        }

        public ClientConnection(Version ver, ServerType type, String hostname)
        {
            try{
                int port = ver.port;
                this.ver = ver;
                this.type = type;
                //sock = new Socket();
                //sock.bind(new java.net.InetSocketAddress(hostname, port));
                sock = new Socket(hostname,port);
                //sock.getInputStream().r
                //sockchan = SocketChannel.open(new java.net.InetSocketAddress(hostname, port));
                //sockchan.configureBlocking(true);
                //sockchan.
                //sock = new Socket(hostname, port);
                //sock.connect(new java.net.InetSocketAddress(hostname, port));
                _in2 = sock.getInputStream();
                _out2 = sock.getOutputStream();
                int av = _in2.available();
                in = new shared.StreamBytestream(_in2);
                out = new shared.StreamBytedeque(_out2);
                readthread = new ClientConnectionReadThread();
                writethread = new ClientConnectionWriteThread();
                //readthread.start();
                //writethread.start();
            }catch(Exception e){
                //could have java.net.ConnectException: "Connection refused"
                Exception e2 = nested.getRootOfException(e);
                if(e2 instanceof ConnectException && e2.getMessage().equals("Connection refused: connect"))
                {
                    m.err("Server refuses to connect.");
                }
                else
                {
                    throw new shared.nested(e);
                }
            }
        }
        protected void SendEncryption()
        {
            NetCliConnectMsg msg = new NetCliConnectMsg();

            //encryption calculations:
            LargeInteger _B = new LargeInteger(server_B);
            LargeInteger _mod = new LargeInteger(server_mod);
            LargeInteger _a = new LargeInteger(512,RandomUtils.rng); //this could just be a fixed number, it doesn't matter.
            LargeInteger _base = new LargeInteger(server_base);
            LargeInteger _key = _B.modPow(_a, _mod);
            LargeInteger _A = _base.modPow(_a, _mod);
            dhkey = _key.toBytes(64);

            msg.A_bytes = _A.toBytes(64);

            //header.SendMsg(this);
            //connheader.SendMsg(this);
            //byte[] b1 = header.getbytes();
            //byte[] b2 = connheader.getbytes();
            //byte[] data = b.appendBytes(b1,b2);
            //out.writeBytes(data);
            //out.flush();
            //ConnectServer.SendNetCliMsg(this, msg);

           // state = state.WaitForKeyAuth;

            //byte[] header2 = header.GetMsgBytes();
            //byte[] enc = msg.GetMsgBytes();
            //byte[] full = b.appendBytes(header2,enc);
            //this.SendBytes(full);

            //items.add(CommItem.SendMessage(msg,null));
            m.msg("waiting for connection");
            NetCliEncryptMsg request = this.SendMsgAndWaitForClass(msg, NetCliEncryptMsg.class);
            /*rc4key = new byte[7];
            for(int i=0;i<7;i++)
            {
                rc4key[i] = (byte)(dhkey[i]^request.keymsg[i]);
            }
            EncryptConnection();*/

        }
        public class ClientConnectionReadThread extends Thread
        {
            public ClientConnectionReadThread()
            {
                //this.setDaemon(true);
                //this.setName("ClientConnectionReadThread");
            }
            public void run()
            {
                //int count=0;
                if(doEncrypt)
                {
                    ServerMsg encmsg = HandleConnectMsg();
                    if(gui.Version.debug) m.msg("Read Msg: "+encmsg.dump());
                    //HandleMsg(encmsg);
                    if(ClientConnection.this.msghandler!=null) ClientConnection.this.msghandler.HandleMsg(encmsg,ClientConnection.this);
                    HandleMsg2(encmsg);
                }

                try
                {
                    while(true)
                    {
                        //if(_in2.available()!=0 || count==2)
                        //{
                            //m.msg("reading");
                            //if(!isConnected)
                            //{
                                //HandleConnectMsg();
                            //}
                            //else
                            //{
                                ServerMsg msg = ReadMessage();
                                if(gui.Version.debug) m.msg("Read Msg: "+msg.dump());
                                //HandleMsg(msg);
                                if(ClientConnection.this.msghandler!=null) ClientConnection.this.msghandler.HandleMsg(msg,ClientConnection.this);
                                HandleMsg2(msg);
                                //Class msgklass = msg.getClass();
                                //Runnable handler = handlers.get(msgklass);
                            //}
                        //}

                        /*CommItem item = items.poll();
                        if(item!=null)
                        {
                            m.msg("item taken");

                            //handle the item:
                            if(item.type==CommItemType.SendMessage)
                            {
                                count++;
                                SendMsg2(item.msg);
                            }
                            else
                            {
                                m.err("Unable to handle CommItem: "+item.type.toString());
                            }
                        }

                        Thread.sleep(100);*/

                    }
                }
                catch(Exception e)
                {
                    //could have SocketException "Connection reset"
                    Exception e2 = nested.getRootOfException(e);
                    if(e2 instanceof SocketException && e2.getMessage().equals("Connection reset"))
                    {
                        m.msg("Server disconnected from us.");
                    }
                    else if(e2 instanceof SocketException && e2.getMessage().equals("socket closed"))
                    {
                        m.msg("Disconnected from server.");
                    }
                    else
                    {
                        throw new nested(e);
                    }
                }
            }
            private void HandleMsg2(ServerMsg msg)
            {
                //we must do this in this thread, as it modifies the streams.
                if(msg instanceof NetCliEncryptMsg)
                {
                    NetCliEncryptMsg request = msg.cast();
                    rc4key = new byte[7];
                    for(int i=0;i<7;i++)
                    {
                        rc4key[i] = (byte)(dhkey[i]^request.keymsg[i]);
                    }
                    EncryptConnection();
                }

                //Transid triggers
                Integer transid = msg.transid();
                if(transid!=null)
                {
                    /*Waiter trigger = waiters.get(transid);
                    if(trigger!=null)
                    {
                        trigger.trigger(msg);
                        //Object o1 = waiters.remove(trigger);
                        waiters.remove(transid);
                        //int dummy=0;
                    }
                    //TriggerTransid(msg,msg.transid());*/
                    Waiter trigger = waiters.remove(transid);
                    if(trigger!=null)
                    {
                        trigger.trigger(msg);
                    }
                }

                //Class trigers
                Class klass = msg.getClass();
                /*Waiter klassTrigger = waiters.get(klass);
                if(klassTrigger!=null)
                {
                    klassTrigger.trigger(msg);
                    //Object o2 = waiters.remove(klassTrigger);
                    waiters.remove(klass);
                    //int dummy=0;
                }*/
                Waiter klassTrigger = waiters.remove(klass);
                if(klassTrigger!=null)
                {
                    klassTrigger.trigger(msg);
                }

            }
            private ServerMsg ReadMessage()
            {
                switch(type)
                {
                    case GateServer:
                        return GateServer.ReadMessage(in,false);
                    case FileServer:
                        return FileServer.ReadMessage(in,false);
                    case AuthServer:
                        return AuthServer.ReadMessage(in,false);
                    case GameServer:
                        return GameMainServer.ReadMessage(in,false);
                    default:
                        throw new shared.uncaughtexception("Unhandled state");
                }
            }

            private ServerMsg HandleConnectMsg()
            {
                IBytestream c = in;

                //receive encryption info:
                //try{
                //while(_in2.available()==0)
                //{
                //    try{
                //    Thread.sleep(1000);
                //    }catch(Exception e){}
                //}
                //}catch(Exception e){}
                byte type = c.readByte();
                byte msgsize = c.readByte(); //e.ensure(msgsize==66);//always 66=0x42

                //works but isn't as efficient and we don't need it.
                //if(ConnectServer.readfirst)
                //{
                //    byte[] msg = c.readBytes(b.ByteToInt32(msgsize)-2);
                //    c = shared.ByteArrayBytestream.createFromByteArray(msg);
                //}

                if(type==NetServer.kNetCliSrv2CliEncrypt)
                {
                    return new NetCliEncryptMsg(c);

                    //rc4key = new byte[7];
                    //for(int i=0;i<7;i++)
                    //{
                    //    rc4key[i] = (byte)(dhkey[i]^request.keymsg[i]);
                    //}

                    //EncryptConnection();

                    //Trigger trigger = waiters.get(0);
                    //if(trigger!=null) trigger.trigger(request);
                    //m.msg("encrypted");
                }
                else
                {
                    //int dummy=0;
                    throw new shared.uncaughtexception("Unhandled NetCliServer msg: "+Byte.toString(type));
                    //m.err("Unhandled NetCliServer msg: ",Byte.toString(type));
                }

            }
            /*private void SendMsg2(ServerMsg msg)
            {
                m.msg("Sending Msg: "+msg.getClass().toString());
                //try{
                byte[] msgdata = msg.GetMsgBytes();
                out.writeBytes(msgdata);
                out.flush();
                //}catch(Exception e){
                //    throw new nested(e);
                //}
            }*/
        }
        private void EncryptConnection()
        {
            if(rc4key==null) throw new shared.uncaughtexception("Empty key");
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

            m.msg("ConnectServer: now encrypted.");
        }
        public class ClientConnectionWriteThread extends Thread
        {
            public ClientConnectionWriteThread()
            {
                //this.setDaemon(true);
                //this.setName("ClientConnectionWriteThread");
            }
            public void run()
            {
                try
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
                                SendMsg2(item.msg);
                            }
                            else if(item.type==CommItemType.Terminate)
                            {
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
                catch(Exception e)
                {
                    //could be a SocketException "Connection reset"
                    Exception e2 = nested.getRootOfException(e);
                    if(e2 instanceof SocketException && e2.getMessage().equals("Connection reset"))
                    {
                        m.msg("Server disconnected from us.");
                    }
                    else
                    {
                        throw new nested(e);
                    }
                }
            }
            private void SendMsg2(ServerMsg msg)
            {
                if(gui.Version.debug) m.msg("Sending Msg: "+msg.dump());
                //try{
                byte[] msgdata = msg.GetMsgBytes();
                out.writeBytes(msgdata);
                out.flush();
                //}catch(Exception e){
                //    throw new nested(e);
                //}
            }
        }
        //public void SendBytes(byte[] msg)
        //{
        //    out.writeBytes(msg);
        //    out.flush();
        //}

        public void SendMsg(ServerMsg msg)
        {
            //m.msg("sendmsgA");
            items.add(CommItem.SendMessage(msg));
        }
        /*public <T extends ServerMsg> T SendMsgAndWaitForTransidAndClass(ServerMsg msg, Class klassToWaitFor)
        {
            Trigger trigger = new Trigg
        }*/
        public <T extends ServerMsg> T SendMsgAndWaitForClass(ServerMsg msg, Class klassToWaitFor)
        {
            Trigger trigger = new Trigger();
            waiters.put(klassToWaitFor, trigger);

            SendMsg(msg);

            ServerMsg reply = trigger.await();
            T r = (T) reply;
            //waiters.remove(klassToWaitFor);

            return r;
        }
        public <T extends ServerMsg> T SendMsgAndWaitForTransid(ServerMsg msg)
        {
            //ServerMsg msg2 = (ServerMsg)msg;
            if(msg.transid()==null) m.throwUncaughtException("Class does not have a transid(): "+msg.getClass().getName());
            T r = (T)SendMsgAndWaitForTransid(msg,msg.transid());
            return r;
        }
        private <T extends ServerMsg> T SendMsgAndWaitForTransid(ServerMsg msg, int transid)
        {
            Trigger trigger = new Trigger();
            waiters.put(transid, trigger);

            SendMsg(msg);

            ServerMsg reply = trigger.await();
            T r = (T) reply;
            //waiters.remove(transid); //freeing up this trigger is especially important so that the messages can be garbage collected.

            return r;
        }
        public void SendMsgAndRegisterTransidCallback(ServerMsg msg, Concurrent.Callback<ServerMsg> callback)
        {
            if(msg.transid()==null) m.throwUncaughtException("Class does not have a transid(): "+msg.getClass().getName());
            CallbackWaiter waiter = new CallbackWaiter(callback);
            waiters.put(msg.transid(), waiter);

            SendMsg(msg);
        }
        /*public void TriggerTransid(ServerMsg reply, int transid)
        {
            Trigger trigger = this.waiters.get(transid);
            if(trigger!=null) trigger.trigger(reply);
        }*/
    }

    public static abstract class Waiter
    {
        public void trigger(ServerMsg msg)
        {
            throw new shared.uncaughtexception("Trigger is unimplemented in subclass: "+this.getClass().toString());
        }
    }
    public static class CallbackWaiter extends Waiter
    {
        Concurrent.Callback<ServerMsg> callback;
        public CallbackWaiter(Concurrent.Callback<ServerMsg> callback)
        {
            this.callback = callback;
        }
        public void trigger(ServerMsg msg)
        {
            callback.callback(msg);
        }
    }
    public static class Trigger extends Waiter
    {
        private CountDownLatch latch;
        private ServerMsg msg;

        public Trigger()
        {
            latch = new CountDownLatch(1);
        }

        public ServerMsg await()
        {
            while(true)
            {
                try{
                    latch.await();
                    break; //we'll only return if the trigger has been called properly.
                }catch(InterruptedException e){}
            }
            return msg;
        }

        public void trigger(ServerMsg msg)
        {
            this.msg = msg;
            latch.countDown();
        }
    }




    private java.util.concurrent.atomic.AtomicInteger curtransid = new java.util.concurrent.atomic.AtomicInteger();
    public int getNextTransid()
    {
        return curtransid.incrementAndGet();
    }



    /*public void StartClientInThisThread()
    {

    }
    public void StartClientInItsOwnThread()
    {

    }*/



}

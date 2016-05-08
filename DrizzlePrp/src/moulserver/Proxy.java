/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import moulserver.Server.ServerMsg;
import shared.*;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;

public class Proxy
{
    //ClientListener client;
    //ServerListener server;

    //ProxyManager manager;
    String uruPath;
    String proxyServerAddress;
    int proxyServerAddress2;
    int[] ourNotthedroidsKey = {0,0,0,0};

    NetServer netserver;
    String fileserveraddress;
    Integer currentGameServerAddress;
    int[] notthedroidsKey;
    ConcurrentHashMap<ConnectionState,ServerConnection> c2s;
    ConcurrentHashMap<Client.ClientConnection,ConnectionState> s2c;

    public static void start(String ProxyUruFolder)
    {
        //Proxy proxy = new Proxy("h:/DontBackup/prps/TalcumProxy/");
        Proxy proxy = new Proxy(ProxyUruFolder);
        proxy.StartServers();
    }

    public static int StringAddressToIntAddress(String strAddress)
    {
        try{
            java.net.InetAddress addr = java.net.Inet4Address.getByName(strAddress);
            java.net.Inet4Address addr4 = (java.net.Inet4Address)addr; //it needs to be ipv4 for the server.
            byte[] addrbs = addr4.getAddress();
            int bigEndianAddress = b.BytesToInt32(addrbs, 0);
            int intAddress = b.reverseEndianness(bigEndianAddress);
            return intAddress;
        }catch(Exception e){
            throw new shared.uncaughtexception("unabled to get address from server name for some reason: "+strAddress);
        }
    }
    public static String IntAddressToStringAddress(int intAddress)
    {
        try{
            int bigEndianAddress = b.reverseEndianness(intAddress);
            byte[] addrbs = b.Int32ToBytes(bigEndianAddress);
            java.net.InetAddress addr = java.net.InetAddress.getByAddress(addrbs);
            String strAddress = addr.getHostAddress();
            return strAddress;
        }catch(Exception e){
            throw new shared.uncaughtexception("unable to get string address from int address: "+Integer.toString(intAddress));
        }
    }

    public Proxy(String uruPath2)
    {
        uruPath = uruPath2;
        proxyServerAddress = "127.0.0.1";
        proxyServerAddress2 = StringAddressToIntAddress(proxyServerAddress);

        //client = new ClientListener();
        //server = new ServerListener();
    }
    public void StartServers()
    {
        //SuperManager.SetAgeInfoFolder(uruPath);
        SuperManager.SetAgeInfoFolder(uruPath+"/dat/", uruPath+"/SDL/"); //changed in Drizzle29

        c2s = Concurrent.getConcurrentHashMap();
        s2c = Concurrent.getConcurrentHashMap();

        //proxy server stuff
        //manager = new ProxyManager();
        AbstractManager manager = new moulserver.AbstractManager() {
            public void HandleMessage(ServerType servertype, ServerMsg msg, ConnectionState cs) {
                //get current connection
                ServerConnection serverconn = c2s.get(cs);
                Class kls = msg.getClass();
                //create connection to server if we don't have one already:
                if(serverconn==null)
                {
                    serverconn = new ServerConnection();
                    serverconn.connect(servertype,cs);
                    c2s.put(cs, serverconn);
                    s2c.put(serverconn.cc,cs);
                }

                //do modifications or whatever:

                //intercept secure download queue requests and use cached version if available.
                if(kls==AuthServer.FileDownloadRequest.class)
                {
                    AuthServer.FileDownloadRequest msg2 = (AuthServer.FileDownloadRequest)msg;

                    try{

                        File f = FileServer.GetFile(msg2.filename.toString(), uruPath);

                        //if we don't already have it, get it.
                        if(f==null)
                        {
                            Client.AuthConnection authcc = (Client.AuthConnection)serverconn.cc;
                            byte[] filedata = authcc.GetFile(msg2.filename.toString(),notthedroidsKey);
                            String filepath = uruPath+"/"+msg2.filename.toString();
                            //encrypt it with our key:
                            FileUtils.WriteFile(filepath, filedata, true, true); //create dirs and throw exception
                            MemUtils.GarbageCollect();
                        }
                        f = FileServer.GetFile(msg2.filename.toString(), uruPath);

                        //encrypt it
                        byte[] filedata = FileUtils.ReadFile(f);
                        byte[] encfiledata = uru.UruCrypt.EncryptNotthedroids(filedata, ourNotthedroidsKey);

                        //use it
                        //int filesize = (int)f.length();
                        //java.io.FileInputStream fis = new java.io.FileInputStream(f);
                        int filesize = encfiledata.length;
                        java.io.InputStream fis = new java.io.ByteArrayInputStream(encfiledata);
                        ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.startfile(msg2.filename.toString(), filesize, msg2.transId, fis, true);

                        AuthServer.FileDownloadChunk reply = new AuthServer.FileDownloadChunk();
                        reply.transId = msg2.transId;
                        reply.result = ENetError.kNetSuccess;
                        //byte[] data = FileUtils.ReadFile(f);
                        reply.filesize = filesize;
                        //reply.chunkOffset = 0;
                        reply.chunkOffset = chunk.offset();
                        //reply.buffer = data;
                        reply.buffer = chunk.read();

                        //send to client
                        cs.sendMsgBytes(reply.GetMsgBytes());
                        //SendMsg(cs,reply);

                    }catch(Exception e){
                        throw new nested(e);
                    }
                    return; //don't forward this message; we'll handle it ourselves.
                }
                else if(kls==AuthServer.FileDownloadChunkAck.class)
                {
                    AuthServer.FileDownloadChunkAck ack = (AuthServer.FileDownloadChunkAck)msg;

                    //Thank ya' kindly, yet again!
                    ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.ack(ack.transId);
                    if(!chunk.done)
                    {
                        //send another!
                        AuthServer.FileDownloadChunk reply = new AuthServer.FileDownloadChunk();
                        reply.transId = ack.transId;
                        reply.result = ENetError.kNetSuccess;
                        reply.filesize = chunk.filesize;
                        reply.chunkOffset = chunk.offset();
                        reply.buffer = chunk.read();

                        cs.sendMsgBytes(reply.GetMsgBytes());
                        //SendMsg(cs,reply);
                    }
                    else
                    {
                        cs.chunksendhandler.clearfile(ack.transId);
                    }
                    return;
                }

                //send msg:
                serverconn.send(msg);
            }
            public void removeConnectionState(ConnectionState cs)
            {
                m.msg("Proxy disconnect");
            }
        };
        netserver = new NetServer(manager);
        netserver.start();


    }

    /*public static class ClientConnection
    {
        ServerConnection serverconn;
    }*/
    public class ServerConnection extends Client
    {
        //ClientConnection clientconn;
        Client.ClientConnection cc;
        //Str filesrvaddress;

        public void connect(ServerType servertype, ConnectionState clientconn)
        {
            Version ver = Version.currentMoulagain;
            switch(servertype)
            {
                case GateServer:
                    cc = new Client.GateConnection(ver);
                    break;
                case FileServer:
                    //fileserveraddress = filesrvaddress.toString(); //we should have intercepted this before.
                    cc = new Client.FileConnection(ver, fileserveraddress);
                    break;
                case AuthServer:
                    cc = new Client.AuthConnection(ver, ver.authserver);
                    break;
                case GameServer:
                    //we will need to intercept this and implement a client for it.
                    //m.throwUncaughtException("unimplemented");
                    String addr = Proxy.IntAddressToStringAddress(currentGameServerAddress);
                    ConnectionState.GameConnHeader gheader = (ConnectionState.GameConnHeader)clientconn.header.subheader;
                    cc = new Client.GameConnection(ver, addr, gheader.accountGuid, gheader.ageGuid);
                    break;
                default:
                    throw new shared.uncaughtexception("unexpected");
            }
            cc.msghandler = new Client.MsgHandler() {
                public void HandleMsg(ServerMsg msg, Client.ClientConnection caller) {
                    //m.msg("HandleMsg: "+msg.getClass().getName());
                    Class kls = msg.getClass();

                    //change file server address
                    if(kls==GateServer.FileSrvIpAddressReply.class)
                    {
                        GateServer.FileSrvIpAddressReply msg2 = (GateServer.FileSrvIpAddressReply)msg;
                        fileserveraddress = msg2.address.toString();
                        msg2.address = new Str("127.0.0.1"); //redirect them here.
                    }
                    //intercept the manifests that would change the UruLauncher and UruExplorer.
                    else if(kls==FileServer.ManifestReply.class)
                    {
                        FileServer.ManifestReply msg2 = (FileServer.ManifestReply)msg;
                        for(uru.server.MoulFileInfo mfi: msg2.manifest.getFiles())
                        {
                            String fn = mfi.filename.toString();
                            if(fn.equals("UruLauncher.exe")||fn.equals("UruExplorer.exe"))
                            {
                                //msg2.manifest.
                                File f = new File(uruPath+"/"+fn);
                                if(!f.exists()) m.throwUncaughtException("File not found: "+f.getAbsolutePath());
                                mfi.Filesize = (int)f.length();
                                mfi.Hash = new Str(b.BytesToHexString(shared.CryptHashes.GetHash(f.getAbsolutePath(), CryptHashes.Hashtype.md5)));
                            }
                        }
                    }
                    //change the game server addresses
                    else if(kls==AuthServer.AgeReply.class)
                    {
                        AuthServer.AgeReply msg2 = (AuthServer.AgeReply)msg;
                        currentGameServerAddress = msg2.gameServerAddress;
                        msg2.gameServerAddress = proxyServerAddress2;
                    }
                    //replace the notthedroidsKey
                    else if(kls==AuthServer.AcctLoginReply.class)
                    {
                        AuthServer.AcctLoginReply msg2 = (AuthServer.AcctLoginReply)msg;
                        notthedroidsKey = msg2.encryptionKey;
                        msg2.encryptionKey = ourNotthedroidsKey;
                    }

                    //forward the message
                    ConnectionState cs = s2c.get(caller);
                    if(cs==null) m.throwUncaughtException("this shouldn't happen");
                    cs.sendMsgBytes(msg.GetMsgBytes());
                }
            };
        }
        public void send(ServerMsg msg)
        {
            cc.SendMsg(msg);
        }
    }

    /*public static class ProxyManager extends AbstractManager
    {
        public void HandleMessage(ServerType servertype, Server.ServerMsg msg, ConnectionState cs)
        {
            //get the ClientConnection
            ServerConnection serverconn = c2s
            if(clientconn==null)
            {
                //set it up!
            }
        }
    }*/
}

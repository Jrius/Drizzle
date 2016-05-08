/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import shared.Str;
import shared.*;
import uru.server.*;
//import moulserver.ConnectServer.ServerThread;
import uru.Bytedeque;
import prpobjects.uruobj;
import java.io.File;
import moulserver.Comm.*;
import java.util.Queue;
//import moulserver.NetServer.ConnectionState;

public class FileServer extends SharedServer
{

    static final int kCli2File_PingRequest = 0;
    static final int kCli2File_BuildIdRequest = 10;
    static final int kCli2File_ManifestRequest = 20;
    static final int kCli2File_FileDownloadRequest = 21;
    static final int kCli2File_ManifestEntryAck = 22;
    static final int kCli2File_FileDownloadChunkAck = 23;

    static final int kFile2Cli_PingReply = 0;
    static final int kFile2Cli_BuildIdReply = 10;
    static final int kFile2Cli_BuildIdUpdate = 11;
    static final int kFile2Cli_ManifestReply = 20;
    static final int kFile2Cli_FileDownloadReply = 21;

    Queue<CommItem> items = Concurrent.getConcurrentQueue();
    Manager manager;

    public FileServer(Manager manager)
    {
        this.manager = manager;
    }

    public void run()
    {
        m.msg("Starting FileServer...");

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

    //static public void HandleFileServerPacket(ServerThread sock)
    //{
    //    m.throwUncaughtException("deprecated");
    //}
    public void HandleMessage(Object msg, ConnectionState cs)
    {
        //IBytestream c = cs.in;
        
        //int msgSize = c.readInt(); //size of msg including this two ints.
        //int msgId = c.readInt();
        //Class msgId = Cli2File.read(c);
        Class klass = msg.getClass();

        //works, but less efficient
        //if(ConnectServer.readfirst)
        //{
        //    byte[] msg = c.readBytes(msgSize-8);
        //    c = shared.ByteArrayBytestream.createFromByteArray(msg);
        //}

        //if(msgId==kCli2File_PingRequest)
        if(klass==PingRequest.class)
        {
            m.msg("FileServer_PingRequest");
            PingRequest pr = (PingRequest)msg;

            PingReply reply = new PingReply();
            reply.time = pr.time;
            SendMsg(cs,reply);
        }
        //else if(msgId==FileServer.kCli2File_ManifestRequest)
        else if(klass==ManifestRequest.class)
        {
            m.msg("FileServer_ManifestRequest");
            ManifestRequest mr = (ManifestRequest)msg;

            String group = mr.group.toString();
            m.msg("Manifest requested: "+group);
            ManifestReply reply = new ManifestReply();
            reply.transId = mr.transId;
            reply.result = ENetError.kNetSuccess;
            reply.readerId = mr.transId; //is this right?
            if(group.equals("ExternalPatcher")) //Just to update UruLauncher.exe
            {
                IBytestream manstream = shared.SerialBytestream.createFromFilename(manager.settings.getPathToExternalPatcherManifest());
                reply.manifest = new Manifest(manstream,true);
                SendMsg(cs,reply);
            }
            else if(group.equals("ThinExternal"))
            {
                //Always called, just updates smaller, more important files: .fni, .p2f, .dll, .exe, .loc, .age, .csv, etc.
                //Doesn't actually download these files, so they must be in "External" too!  But not UruLauncher.exe
                IBytestream manstream = shared.SerialBytestream.createFromFilename(manager.settings.getPathToMainManifest());
                reply.manifest = new Manifest(manstream,true);
                SendMsg(cs,reply);
            }
            else if(group.equals("External"))
            {
                //Not always called (It seems if there's something updated in the earlier ones, then it will run this.)  .prp, .ogg, .bik, even global prps.
                //Contains all the files from ThinExternal too.  But not UruLauncher.exe
                //IBytestream manstream = shared.SerialBytestream.createFromFilename(Manager.settings.getPathToAgeManifests()+"GUI.mfs_moul");
                IBytestream manstream = shared.SerialBytestream.createFromFilename(manager.settings.getPathToMainManifest());
                reply.manifest = new Manifest(manstream,true);
                SendMsg(cs,reply);
            }
            else
            {
                //It's an Age name.
                //In order: GlobalAnimations,GlobalAvatars,GlobalClothing,GlobalMarkers,GUI,CustomAvatars,
                if(group.contains("/")||group.contains("\\"))  m.throwUncaughtException("None of that!");
                File manfile = new File(manager.settings.getPathToAgeManifests()+"/"+group+".mfs_moul");
                if(!manfile.exists())
                {
                    m.throwUncaughtException("unhandled group: "+group);
                }

                IBytestream manstream = shared.SerialBytestream.createFromFile(manfile);
                reply.manifest = new Manifest(manstream,true);
                SendMsg(cs,reply);
            }
            //reply.
        }
        //else if(msgId==FileServer.kCli2File_ManifestEntryAck)
        else if(klass==ManifestEntryAck.class)
        {
            m.msg("FileServer_ManifestEntryAck");
            ManifestEntryAck ack = (ManifestEntryAck)msg;
            //try{
            //    sock.close();
            //}catch(Exception e){
            //    e.printStackTrace();
            //}
            int dummy=0;
        }
        //else if(msgId==FileServer.kCli2File_FileDownloadRequest)
        else if(klass==FileDownloadRequest.class)
        {
            FileDownloadRequest request = (FileDownloadRequest)msg;
            try{
            String filename = request.filename.toString();
            m.msg("FileServer_FileDownloadRequest: ",filename);
            //File f = GetFile(filename,false,manager); //inefficient use of memory and causes the client to wait a few seconds before downloading for large files.  We could do this better with many smaller chunks. The largest file is just under 40MB.
            File f = GetFile(filename,manager.settings.getFileserverPath());
            int filesize = (int)f.length();
            java.io.FileInputStream fis = new java.io.FileInputStream(f);
            ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.startfile(request.filename.toString(), filesize, request.transId, fis, false);

            //we could break this into chunks to be downloaded, but... this is tcp, so why?
            FileDownloadReply reply = new FileDownloadReply();
            reply.transId = request.transId;
            reply.result = ENetError.kNetSuccess;
            reply.readerId = request.transId; //good enough for now.
            //byte[] filedata = FileUtils.ReadFile(f);
            //reply.totalsize = filedata.length;
            reply.filesize = filesize;
            //reply.buffersize = filedata.length;
            //reply.buffer = filedata;
            reply.buffer = chunk.read();
            SendMsg(cs,reply);
            }catch(Exception e){throw new shared.nested(e);}

        }
        //else if(msgId==FileServer.kCli2File_FileDownloadChunkAck)
        else if(klass==FileDownloadChunkAck.class)
        {
            m.msg("FileServer_FileDownloadChunkAck");
            FileDownloadChunkAck ack = (FileDownloadChunkAck)msg;

            //thank ya' kindly!
            ChunkSendHandler.ChunkFile chunk = cs.chunksendhandler.ack(ack.transId);
            if(!chunk.done)
            {
                //send another!
                FileDownloadReply reply = new FileDownloadReply();
                reply.transId = ack.transId;
                reply.result = ENetError.kNetSuccess;
                reply.readerId = ack.transId;
                reply.filesize = chunk.filesize;
                reply.buffer = chunk.read();
                SendMsg(cs,reply);
            }
            else
            {
                cs.chunksendhandler.clearfile(ack.transId);
            }

        }
        else
        {
            m.throwUncaughtException("Unhandled FileServer msg type: "+klass.getSimpleName());
        }


    }

    static public FileServerMsg ReadMessage(IBytestream c, boolean isServer)
    {

        int msgSize = c.readInt(); //size of msg including this two ints.
        int msgId = c.readInt();

        //this works, but its just for debugging, and faster without it.
        //if(ConnectServer.readfirst)
        //{
        //    byte[] msg = c.readBytes(msgSize-8);
        //    c = shared.ByteArrayBytestream.createFromByteArray(msg);
        //}

        if(isServer)
        {
            switch(msgId)
            {
                case FileServer.kCli2File_PingRequest:
                    return new PingRequest(c);
                case FileServer.kCli2File_ManifestRequest:
                    return new ManifestRequest(c);
                case FileServer.kCli2File_ManifestEntryAck:
                    return new ManifestEntryAck(c);
                case FileServer.kCli2File_FileDownloadRequest:
                    return new FileDownloadRequest(c);
                case FileServer.kCli2File_FileDownloadChunkAck:
                    return new FileDownloadChunkAck(c);
                default:
                    throw new shared.uncaughtexception("Unread FileServer msg type: "+Integer.toString(msgId));
            }
        }
        else
        {
            switch(msgId)
            {
                case FileServer.kFile2Cli_PingReply:
                    return new PingReply(c);
                case FileServer.kFile2Cli_ManifestReply:
                    return new ManifestReply(c);
                case FileServer.kFile2Cli_FileDownloadReply:
                    return new FileDownloadReply(c);
                default:
                    throw new shared.uncaughtexception("Unread FileServer msg type: "+Integer.toString(msgId));
            }
        }

    }

    public static class FileDownloadChunkAck extends FileServerMsg
    {
        int transId;
        int readerId;

        public FileDownloadChunkAck(IBytestream c)
        {
            transId = c.readInt();
            readerId = c.readInt();
        }
        public int type(){return FileServer.kCli2File_FileDownloadChunkAck;}
        public Integer transid(){return transId;}
        public FileDownloadChunkAck(){}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(readerId);
        }
    }

    //public static File GetFile(String filename, boolean isAuthServer, Manager manager)
    public static File GetFile(String filename, String root)
    {
        //String root = isAuthServer?manager.settings.getAuthFileserverPath():manager.settings.getFileserverPath();
        File f = new File(root+"/"+filename);
        //if(!f.exists() || !f.isFile()) throw new uncaughtexception("File does not exist: "+filename);
        if(!f.exists()) return null;
        if(!f.isFile()) throw new uncaughtexception("Expected file but found folder: "+filename);
        String[] pathparts = filename.replace("\\", "/").split("/");

        //security wise, we can't just let them give us any filename.
        //security checks:
        for(String s: pathparts)
        {
            if(s.trim().equals("..")) throw new uncaughtexception("File path contained ..: "+filename);
        }
        try{
            String realpath = f.getCanonicalPath();
            String realroot = new File(root).getCanonicalPath();
            if(!realpath.startsWith(realroot)) throw new uncaughtexception("File path is not under root: "+filename);
        }catch(Exception e){
            throw new nested(e);
        }

        return f;
    }
    static public class FileDownloadReply extends FileServerMsg
    {
        int transId;
        int result;
        int readerId;
        int filesize;
        private int buffersize;
        byte[] buffer;

        public FileDownloadReply(){}
        public FileDownloadReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            readerId = c.readInt();
            filesize = c.readInt();
            buffersize = c.readInt();
            buffer = c.readBytes(buffersize);
        }

        public int type(){ return FileServer.kFile2Cli_FileDownloadReply; }
        public Integer transid(){return transId;}

        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(readerId);
            c.writeInt(filesize);
            c.writeInt(buffer.length);
            c.writeBytes(buffer);
        }

    }
    static public class FileDownloadRequest extends FileServerMsg
    {
        int transId;
        //Utf16.FixedNT filename;
        Str filename;
        int buildId;

        public FileDownloadRequest(IBytestream c)
        {
            transId = c.readInt();
            //filename = new Utf16.FixedNT(c, 260);
            filename = Str.readAsUtf16FixedAndNT(c, 260);
            buildId = c.readInt();
        }
        public int type(){return FileServer.kCli2File_FileDownloadRequest;}
        public Integer transid(){return transId;}
        public FileDownloadRequest(){}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            filename.writeAsUtf16FixedAndNT(c,260);
            c.writeInt(buildId);
        }
    }

    static private void SendMsg(ConnectionState cs, FileServerMsg msg)
    {
        //if(ConnectServer.allatonce)
        //{
            Bytedeque c = new uru.Bytedeque(Format.moul);
            c.writeInt(0); //set this later.
            c.writeInt(msg.type());
            msg.write(c);
            byte[] data = c.getAllBytes();
            byte[] len = b.Int32ToBytes(data.length);
            b.CopyBytes(len, data, 0); //put the length in the array.

            //sock.out.writeBytes(data);
            //sock.out.flush();
            cs.sendMsgBytes(data);
        //}
        //else
        //{
        //    m.throwUncaughtException("deprecated");
        //    Bytedeque c = new uru.Bytedeque(Format.moul);
        //    msg.write(c);
        //    byte[] data = c.getAllBytes();
        //    //int type = File2Cli.get(msg.getClass());
        //    int type = msg.type();
        //
        //    //sock.out.writeInt(data.length+8);
        //    //sock.out.writeInt(type);
        //    //sock.out.writeBytes(data);
        //    //sock.out.flush();
        //}
    }

    static abstract class FileServerMsg extends Server.ServerMsg
    {
        abstract int type();

        public byte[] GetMsgBytes()
        {
            Bytedeque c = new uru.Bytedeque(Format.moul);
            c.writeInt(0); //set this later.
            c.writeInt(this.type());
            this.write(c);
            byte[] data = c.getAllBytes();
            b.Int32IntoBytes(data.length, data, 0);
            return data;
        }
    }

    public static class PingReply extends FileServerMsg
    {
        int time; //in ms.  unsigned.  GetTickCount() from windows api.  The # of ms since the system was started.  It loops around after about 49 days.

        public PingReply(IBytestream c)
        {
            time = c.readInt();
        }
        public PingReply(){}
        public void write(IBytedeque c)
        {
            c.writeInt(time);
        }
        public int type(){ return FileServer.kFile2Cli_PingReply; }
    }

    public static class ManifestRequest extends FileServerMsg
    {
        int transId; //client increases by 1 each time it is needed.
        //group may = "ExternalPatcher", and it is null-terminated, and all the chars after that are random garbage from elsewhere.
        //Utf16 group; //520 bytes of null-terminated UTF-16 or some such thing.
        Str group;
        int buildId; //0, at least sometimes.  Or perhaps readerid?

        public ManifestRequest(IBytestream c)
        {
            transId = c.readInt();
            //group = new Utf16(c,260);
            group = Str.readAsUtf16FixedAndNT(c, 260);
            buildId = c.readInt();
        }
        public int type(){return FileServer.kCli2File_ManifestRequest;}
        public Integer transid(){return transId;}
        public ManifestRequest(){}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            //group.write(c);
            group.writeAsUtf16FixedAndNT(c, 260);
            c.writeInt(buildId);
        }

    }

    public static class ManifestReply extends FileServerMsg
    {
        int transId; //2, 4
        int result; //an ENetError code, 0 for success.
        int readerId; //2, 4.  Does this just match transid?  It seems so.

        //int totalFiles;
        //int numchars;
        //Vector<Utf16> files;

        //We're just reading the .mfs_moul files, which contain totalFiles, numchars, and all the manifest data.
        Manifest manifest;

        public ManifestReply(){}
        public Integer transid(){return transId;}
        public ManifestReply(IBytestream c)
        {
            transId = c.readInt();
            result = c.readInt();
            readerId = c.readInt();
            manifest = new Manifest(c,false);
        }
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(result);
            c.writeInt(readerId);
            manifest.write(c);
        }

        public int type(){ return FileServer.kFile2Cli_ManifestReply; }

    }
    public static class ManifestEntryAck extends FileServerMsg
    {
        int transId;
        int readerId;

        public ManifestEntryAck(IBytestream c)
        {
            transId = c.readInt();
            readerId = c.readInt();
        }
        public ManifestEntryAck(){}
        public int type(){return FileServer.kCli2File_ManifestEntryAck;}
        public Integer transid(){return transId;}
        public void write(IBytedeque c)
        {
            c.writeInt(transId);
            c.writeInt(readerId);
        }
    }
    public static class PingRequest extends FileServerMsg
    {
        int time; //in ms.  GetTickCount() from windows api.  The # of ms since the system was started.  It loops around after about 49 days.

        //static{
        //    type = 3;
        //}

        public PingRequest(IBytestream c)
        {
            time = c.readInt();
        }
        public PingRequest(){}
        public int type(){return FileServer.kCli2File_PingRequest;}
        public void write(IBytedeque c)
        {
            c.writeInt(time);
        }
    }

}

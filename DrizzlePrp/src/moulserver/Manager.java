/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.net.ServerSocket;
import java.net.Socket;
import shared.*;
import java.io.InputStream;
import uru.server.*;
import java.util.Vector;

public class Manager extends AbstractManager
{

    //some settings
    static final int msToSleep = 4; //was 40

    //notthedroids: {0x9a17342d,0xe40bc816,0x7b2ef65d,0xaa9d5539}
    //whatdoyousee: {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2}

    public static void test(String input)
    {
        try{
            //java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-0");
            //byte[] hash = md.digest(new byte[]{0});

            //jonelo.jacksum.algorithm.AbstractChecksum cs = jonelo.jacksum.JacksumAPI.getChecksumInstance("sha-0");
            //cs.update(new byte[]{0});
            //byte[] hash = cs.getByteArray();
            String username = "name@example.com";
            String password = "password";
            int serverchallenge = 0;
            int clientchallenge = 0x62be8659;//0x36877e74;
            jonelo.jacksum.adapt.gnu.crypto.hash.Sha0 sha = new jonelo.jacksum.adapt.gnu.crypto.hash.Sha0();
            byte[] pwbs = b.Utf16ToBytes(password/*+(char)0*/);
            byte[] unbs = b.Utf16ToBytes(username.toLowerCase()/*+(char)0*/);
            byte[] both = new byte[pwbs.length+unbs.length];
            b.CopyBytes(pwbs, both, 0);
            b.CopyBytes(unbs, both, pwbs.length);
            both[pwbs.length-1] = 0;
            both[pwbs.length-2] = 0;
            both[both.length-1] = 0;
            both[both.length-2] = 0;
            jonelo.jacksum.adapt.gnu.crypto.hash.Sha0 sha0 = new jonelo.jacksum.adapt.gnu.crypto.hash.Sha0();
            sha0.update(both, 0, both.length);
            byte[] UnAndPwHash = sha0.digest();
            //UnAndPwHash = shared.CryptHashes.GetHash(both, CryptHashes.Hashtype.sha1);

            byte[] all = new byte[4+4+UnAndPwHash.length];
            b.loadInt32IntoBytes(clientchallenge, all, 0);
            b.loadInt32IntoBytes(serverchallenge, all, 4);
            b.CopyBytes(UnAndPwHash, all, 8);
            sha0.update(all, 0, all.length);
            byte[] hash = sha0.digest();
            //hash = shared.CryptHashes.GetHash(all, CryptHashes.Hashtype.sha1);

            if(true) return;

            m.msg("start");
            /*//java.util.Date d = new java.util.Date();
            //System.c
            String bytes = "219848EA356CD04B9DAEBB17C585E680";
            byte[] bs = b.HexStringToBytes(bytes);
            //key from sub452428
            //0x67452301  0xEFCDAB89  0x98BADCFE  0x10325476  0x00000000
            byte[] key = b.HexStringToBytes("0123456789ABCDEFFEDCBA987654321000000000");
            org.bouncycastle.crypto.engines.RC4Engine rc4e = new org.bouncycastle.crypto.engines.RC4Engine();
            org.bouncycastle.crypto.CipherParameters cp = new org.bouncycastle.crypto.params.KeyParameter(key);
            rc4e.init(false, cp);
            byte[] output = new byte[16];
            rc4e.processBytes(bs, 0, bs.length, output, 0);
            //rc4e.returnByte(in); //don't need this.*/

            byte[] data = FileUtils.ReadFile(input);
            IBytestream c = shared.ByteArrayBytestream.createFromByteArray(data);
            Vector<MoulFileInfo> mfis = new Vector();
            int i = 0;
            StringBuilder r = new StringBuilder();
            while(true)
            {
                i++;
                if(c.getBytesRemaining()<1000)
                {
                    int dummy=0;
                }
                if(c.getBytesRemaining()==0)
                {
                    break;
                }
                //if(i>=2000) break;
                MoulFileInfo mfi = new MoulFileInfo(c);
                mfis.add(mfi);
                String msg = mfi.toString();
                r.append(msg+"\n");
            //if(true)return;
            }
            m.msg(r.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        //int dummy=0;
    }

    static Manager manager;

    //static Thread t;
    public Settings settings;
    public Database database;
    public int address;
    public AgesInfo agesinfo;
    public Comm comm;
    private NetServer netserver;
    private HttpServer httpserver;
    private GateServer gateserver;
    private FileServer fileserver;
    public AuthServer authserver;
    public GameMainServer gamemainserver;
    public VaultListeners vaultlistener;

    public static void StartServers(String mainpassword, String domainname, String datapath)
    {
        moulserver.Settings settings = new moulserver.Settings();
        settings.setDomainName(domainname);
        settings.setMainPassword(mainpassword);
        settings.setDatapath(datapath);
        moulserver.Manager mgr = new moulserver.Manager();
        mgr.StartServers(settings);
    }

    public void StartServers(Settings stngs)
    {
        Manager.manager = this;
        settings = stngs;
        //SuperManager.SetAgeInfoFolder(settings.getPathToCleanFiles());
        SuperManager.SetAgeInfoFolder(settings.getPathToAgeFiles(), settings.getPathToSdlFiles());

        /*final boolean threadstart = false;
        if(threadstart)
        {
            if(true) return;
            t = new Thread(new java.lang.Runnable() {
                public void run() {
                    start2();
                }
            });
            t.start();
        }
        else
        {
            start2();
        }*/
        //Settings settings = new Settings();

        comm = new Comm();

        vaultlistener = new VaultListeners();

        try{
            java.net.InetAddress addr = java.net.Inet4Address.getByName(settings.getDomainName());
            java.net.Inet4Address addr4 = (java.net.Inet4Address)addr; //it needs to be ipv4 for the server.
            byte[] addrbs = addr4.getAddress();
            int bigEndianAddress = b.BytesToInt32(addrbs, 0);
            address = b.reverseEndianness(bigEndianAddress);
        }catch(Exception e){
            throw new shared.uncaughtexception("unabled to get address from server name for some reason: "+settings.getDomainName());
        }

        //agesinfo = new AgesInfo(this);
        //agesinfo.initialize();
        //agesinfo = new AgesInfo(this.getPathToCleanFiles());
        agesinfo = new AgesInfo(settings.getPathToAgeFiles(),settings.getPathToSdlFiles());

        database = new Database();
        database.initialise(settings.getDatabasefile());

        //ConnectServer fileserver = new ConnectServer(settings);
        //fileserver.start();

        gateserver = new GateServer(this);
        gateserver.start();

        fileserver = new FileServer(this);
        fileserver.start();

        authserver = new AuthServer(this);
        authserver.start();

        gamemainserver = new GameMainServer(this);
        gamemainserver.start();

        netserver = new NetServer(this);
        netserver.start();

        httpserver = new HttpServer(this);
        httpserver.start();

    }


    public void HandleMessage(ServerType servertype, Server.ServerMsg msg, ConnectionState cs)
    {
        switch(servertype)
        {
            case GateServer:
                gateserver.items.add(Comm.CommItem.HandleMessage(msg, cs));
                break;
            case FileServer:
                fileserver.items.add(Comm.CommItem.HandleMessage(msg, cs));
                break;
            case AuthServer:
                authserver.items.add(Comm.CommItem.HandleMessage(msg, cs));
                break;
            case GameServer:
                //The HandleMessageDirect will have the gamemainserver just pass on the message, without using the GameMainServer's thread. Which should save some time, instead of waiting through two queues.
                //Manager.gamemainserver.items.add(Comm.CommItem.HandleMessage(msg, connstate));
                gamemainserver.HandleMessageDirect(Comm.CommItem.HandleMessage(msg, cs));
                break;
            default:
                throw new shared.uncaughtexception("Unhandled state");
        }
    }
    /*public String getPathToCleanFiles()
    {
        return settings.getPathToCleanFiles();
    }*/
    public void removeConnectionState(ConnectionState cs)
    {
        this.netserver.removeConnectionState(cs);
    }
    public AgesInfo getagesinfo()
    {
        return agesinfo;
    }
    /*public static void testinit()
    {
        Manager.settings = new Settings();
        
        agesinfo = new AgesInfo();
        agesinfo.initialize();
    }*/
    public static void StartClient(Settings settings)
    {
    }
    /*public static void start2()
    {
        m.msg("listening");
        int port = 14617;
        //int port = 4165;
        int msToSleep = 40;
        try{
            ServerSocket server = new ServerSocket(port);
            while(true)
            {
                Socket sock = server.accept();
                InputStream in = sock.getInputStream();
                while(!sock.isClosed())
                {
                    int avail = in.available();
                    if(avail!=0)
                    {
                        byte[] bytes = new byte[avail];
                        in.read(bytes);
                        String data = b.BytesToString(bytes);
                        handlepacket(bytes);
                    }
                    Thread.sleep(msToSleep);
                }
                int curb;
                m.msg("Done with socket!");
                break;
            }
            server.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

    /*public static void handlepacket(byte[] packet)
    {
        //dumppacket(packet);

        IBytestream c = shared.ByteArrayBytestream.createFromByteArray(packet);
        FileServer fileserver = new FileServer();
        fileserver.start();

        c.close();
    }*/
    public static void dumppacket(byte[] packet)
    {
        String folder = "H:/DontBackup/prps/moulserver/packets/";
        int i=0;
        String curname;
        while(true)
        {
            curname = folder+"/packet"+Integer.toString(i)+".hex";
            if(!FileUtils.Exists(curname))
            {
                break;
            }
            i++;
        }
        FileUtils.WriteFile(curname, packet);
    }


    //*************************** stability checks ***************************************
    public static void ensurePlayerIsOnline(int playerIdx)
    {
        //todo
    }
}

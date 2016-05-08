/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import moulserver.GateServer.*;
import shared.*;
import java.util.ArrayList;
import java.io.File;

public class Client_SaveSecureDownloadFiles extends Client
{
    //String username;
    //String password;
    //String outputpath;
    //public Client_SaveSecureDownloadFiles(String username, String password, String outputpath)
    //{
    //    this.username = username;
    //    this.password = password;
    //    this.outputpath = outputpath;
    //}

    public static void SaveSecureDownloadQueue(String username, String password, String outputpath)
    {
        Client_SaveSecureDownloadFiles client = new Client_SaveSecureDownloadFiles();
        client.work(username, password, outputpath);
    }

    private void work(String username, String password, String outputpath)
    {
        //the shard version
        Version ver = Version.moulagain_1_893;

        //connect to gateway server
        //Client.GateConnection gateconn = new Client.GateConnection(ver);

        //get the file server address
        //m.msg("waiting at start");
        //String filesrvaddress = gateconn.GetFileSrvIp();
        //m.msg("done! "+filesrvaddress);

        //connect to the file server
        //Client.FileConnection fileconn = new Client.FileConnection(ver, filesrvaddress);
        //m.msg("connected to file server!");
        
        //get the auth server address
        //String authsrvaddr = gateconn.GetAuthSrvIp();

        //connect to the auth server
        Client.AuthConnection authconn = new Client.AuthConnection(ver, ver.authserver);

        //login
        boolean loginsucceeded = authconn.Login(username,password);
        if(!loginsucceeded)
        {
            m.err("Login did not succeed.  The username or password was probably incorrect.");
            return;
        }

        //get list of all files
        ArrayList<moulserver.SecureDownloadManifest.Entry> pythonfiles = authconn.GetDirList("Python","pak");
        ArrayList<moulserver.SecureDownloadManifest.Entry> sdlfiles = authconn.GetDirList("SDL","sdl");
        pythonfiles.addAll(sdlfiles);

        int curfile = 0;
        int numfiles = pythonfiles.size();
        for(moulserver.SecureDownloadManifest.Entry entry: pythonfiles)
        {
            //download and save the file.
            curfile++;
            m.msg("Downloading file ",Integer.toString(curfile)," of ",Integer.toString(numfiles));
            byte[] filedata = authconn.GetFile(entry.filename.toString());
            String filepath = outputpath+"/"+entry.filename.toString().replace("\\", "/"); //fix problem where linux creates the filename with a blackslash in it rather than in a folder.
            FileUtils.WriteFile(filepath, filedata, true, true); //create dirs and throw exception
            MemUtils.GarbageCollect();
        }

        //close them down
        authconn.Disconnect();

        //all done!
        m.msg("All done!");

    }
}

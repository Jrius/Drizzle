/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import moulserver.GateServer.*;
import shared.*;
import java.util.ArrayList;
import java.io.File;

public class Client_SaveFileServerFiles extends Client
{
    //String outputpath;
    //public Client_SaveFileServerFiles(String outputpath)
    //{
    //    this.outputpath = outputpath;
    //}

    public static void SaveSecureDownloadQueue(String outputpath)
    {
        Client_SaveFileServerFiles client = new Client_SaveFileServerFiles();
        client.work(outputpath);
    }

    private void work(String outputpath)
    {
        //the shard version
        Version ver = Version.moulagain_1_893;

        //connect to gateway server
        Client.GateConnection gateconn = new Client.GateConnection(ver);

        //get the file server address
        m.msg("key="+b.BytesToHexString(gateconn.rc4key));
        m.msg(gateconn.in.getClass().getName());
        m.msg("waiting at start");
        String filesrvaddress = gateconn.GetFileSrvIp();
        m.msg("done! "+filesrvaddress);

        //connect to the file server
        Client.FileConnection fileconn = new Client.FileConnection(ver, filesrvaddress);
        m.msg("connected to file server!");

        //download the two manifests
        ArrayList<uru.server.MoulFileInfo> externalFiles = fileconn.GetManifest("External");
        m.msg("got external manifest!");
        ArrayList<uru.server.MoulFileInfo> externalPatcherFiles = fileconn.GetManifest("ExternalPatcher");
        m.msg("got externalpatcher manifest!");
        externalFiles.addAll(externalPatcherFiles);

        //download the files
        for(uru.server.MoulFileInfo mfi: externalFiles)
        {
            String filepath = outputpath+"/"+mfi.filename.toString();
            File filepath2 = new File(filepath);
            if(filepath2.exists())
            {
                byte[] curfiledata = FileUtils.ReadFile(filepath);
                String curmd5 = b.BytesToHexString(shared.CryptHashes.GetMd5(curfiledata)).toLowerCase();
                String correctmd5 = mfi.Hash.toString().toLowerCase();
                if(curmd5.equals(correctmd5)) continue; //skip this file.
            }
            //download and save the file.
            byte[] filedata = fileconn.GetFile(mfi);
            FileUtils.WriteFile(filepath, filedata, true, true); //create dirs and throw exception
            MemUtils.GarbageCollect();
        }

        gateconn.Disconnect();
        fileconn.Disconnect();

        //all done!
        m.msg("All done!");

    }
}

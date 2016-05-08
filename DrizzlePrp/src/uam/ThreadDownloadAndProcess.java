/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

import shared.*;
import java.io.ByteArrayInputStream;
import java.io.File;

public class ThreadDownloadAndProcess extends Thread
{
    /*Jobs job = Jobs.none;
    
    String age;
    String ver;
    String mir;
    String potsfolder;
    String whirlpool;
    
    String server;
    gui.Gui guiform;
    shared.delegate callback;
    boolean doDownload;
    
    boolean wasSuccessful = false;
    
    public static enum Jobs
    {
        none,
        downloadConfig,
        downloadAge7z,
    }*/

    private static batching.Queue queue;
    private static batching.Queue getQueue()
    {
        if(queue==null) queue = new batching.Queue();
        return queue;
    }
    public static void downloadConfig(String server, String potsfolder, shared.delegate callback)
    {
        /*ThreadDownloadAndProcess thread = new ThreadDownloadAndProcess();
        thread.job = Jobs.downloadConfig;
        thread.server = server;
        thread.callback = callback;
        thread.potsfolder = potsfolder;
        thread.doDownload = true;
        thread.start();*/
        final String server2 = server;
        final String potsfolder2 = potsfolder;
        batching.Queue queue = getQueue();
        //final shared.Monitor trigger = new shared.Monitor();
        //queue.AddItem(new batching.QueueItem() {
            boolean success;

            //@Override
            //public void run() {
                //InvisibleModal modal = InvisibleModal.createAndShow();
                try{

                String file = server2+"/"+Uam.statusFilename;//"uam.status.txt";
                byte[] result = ThreadDownloader.downloadAsBytes(file);
                if(result==null)
                {
                    //failed
                    success = false;
                    return;
                }

                if(potsfolder2!=null)
                {
                    FileUtils.CreateFolder(potsfolder2+uam.Uam.ageArchivesFolder);
                    //if(new File(potsfolder+uam.Uam.ageArchivesFolder).getUsableSpace()>result.length+1000000)
                    if(FileUtils.HasFreeSpace(potsfolder2+uam.Uam.ageArchivesFolder, result.length))
                    {
                        FileUtils.WriteFile(potsfolder2+uam.Uam.ageArchivesFolder+uam.Uam.statusFilename, result);
                    }
                    else
                    {
                        m.err("There isn't enough free space to save the Age list to disk.");
                    }
                }



                //callback
                //callback.callback(result);//(ageList);
                //byte[] result = (byte[])arg;
                ByteArrayInputStream in = new ByteArrayInputStream(result);
                //uam.UamConfigNew wha = new uam.UamConfigNew(in);
                uam.UamConfigNew ageList = new uam.UamConfigNew(in);
                //list Ages...
                //return ageList.getAllAgeNames();
                //uam.UamConfig config = (uam.UamConfig)arg;
                //uam.Uam.ageList = config;
                uam.Uam.ageList = ageList;

                gui.UamGui.RefreshInfo(potsfolder2);

                success = true;

                }catch(Exception e){
                    //int dummy=0;
                    e.printStackTrace();
                }finally{
                    //modal.hideInvisibleModal();
                    //trigger.notifyCorrectly();
                }
            //}
        //});
        //java.util.concurrent.atomic.AtomicBoolean a;
        //java.util.concurrent.locks.ReentrantLock b;
        //java.util.concurrent.
        //synchronized(trigger){
            //try{
            //    trigger.wait();
            //}catch(Exception e){
            //    int i = 0;
            //}
        //}
        //queue.waitFor();
        //trigger.waitCorrectly();
        //m.msg("doneskis!");
    }
    public static void downloadAge(String age,String ver,String mir,String potsfolder,String whirlpool)
    {
        /*ThreadDownloadAndProcess thread = new ThreadDownloadAndProcess();
        thread.job = Jobs.downloadAge7z;
        thread.age = age;
        thread.ver = ver;
        thread.mir = mir;
        thread.potsfolder = potsfolder;
        thread.whirlpool = whirlpool;
        thread.doDownload = true;
        thread.start();*/
        downloadOrExtractAge(age,ver,mir,potsfolder,whirlpool,true);
    }
    public static void downloadOrExtractAge(String age, String ver, String mir, String potsfolder, String whirlpool, boolean doDownload)
    {
        final String potsfolder2 = potsfolder;
        final String age2 = age;
        final String ver2 = ver;
        final String mir2 = mir;
        final String whirlpool2 = whirlpool;
        final boolean doDownload2 = doDownload;
        final shared.Monitor trigger = new shared.Monitor();
        getQueue().AddItem(new batching.QueueItem() {

            boolean success;

            @Override
            public void run() {
                InvisibleModal modal = InvisibleModal.createAndShow();
                try{

                //prepare output file.
                String outputfolder = potsfolder2+Uam.ageArchivesFolder;
                FileUtils.CreateFolder(outputfolder);
                String outputfile = outputfolder+age2+uam.Uam.versionSep+ver2+".7z";

                //download file.
                if(doDownload2)
                {
                    String tempoutputfile = outputfile;//+".part";
                    if(!ThreadDownloader.downloadAsFile(mir2, tempoutputfile))
                    {
                        //failed
                        FileUtils.DeleteFile(tempoutputfile);
                        success = false;
                        return;
                    }
                    else
                    {
                        //success
                        //File f = new File(tempoutputfile);
                        // f.
                    }
                }


                //check integrity.
                m.status("Checking integrity...");
                //byte[] hash = shared.CryptHashes.GetWhirlpool(outputfile);
                  //FileUtils.DeleteFile(outputfile);
                byte[] hash = shared.CryptHashes.GetHash(outputfile, shared.CryptHashes.Hashtype.sha1);
                String hashstr = b.BytesToHexString(hash);
                boolean isgood = whirlpool2.equals(hashstr);
                if(!isgood)
                {
                    m.err("Bad file integrity. The Age downloaded wasn't what was expected, perhaps because the version on the server is corrupted.");
                    FileUtils.DeleteFile(outputfile);
                    success = false;
                    modal.hideInvisibleModal();
                    return;
                }
                m.status("File integrity is good!");

                //extract.
                if(!shared.sevenzip.extract(outputfile, potsfolder2))
                {
                    success = false;
                    modal.hideInvisibleModal();
                    return;
                }

                //callback
                //callback.callback(null);
                m.status("Age installed!");

                //javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                //        public void run() {
                            gui.UamGui.RefreshInfo(potsfolder2);
                //        }
                //    });
                //gui.UamGui.RefreshInfo(potsfolder);

                success = true;

                }finally{
                modal.hideInvisibleModal();
                    trigger.notifyCorrectly();
                }
            }
        });
        trigger.waitCorrectly();
    }
    public static void extractAge(String age,String ver,String potsfolder,String whirlpool)
    {
        /*ThreadDownloadAndProcess thread = new ThreadDownloadAndProcess();
        thread.job = Jobs.downloadAge7z;
        thread.age = age;
        thread.ver = ver;
        thread.potsfolder = potsfolder;
        thread.whirlpool = whirlpool;
        thread.doDownload = false;
        thread.start();*/
        downloadOrExtractAge(age,ver,null,potsfolder,whirlpool,false);
    }
    
    public ThreadDownloadAndProcess()
    {
        
    }
    
    /*@Override public void run()
    {
        if(job==Jobs.none)
        {
            //do nothing.
            wasSuccessful = true;
        }
        else if(job==Jobs.downloadAge7z)
        {
            //prepare output file.
            String outputfolder = potsfolder+Uam.ageArchivesFolder;
            FileUtils.CreateFolder(outputfolder);
            String outputfile = outputfolder+age+uam.Uam.versionSep+ver+".7z";

            //download file.
            if(doDownload)
            {
                String tempoutputfile = outputfile;//+".part";
                if(!ThreadDownloader.downloadAsFile(mir, tempoutputfile))
                {
                    //failed
                    FileUtils.DeleteFile(tempoutputfile);
                    wasSuccessful = false;
                    return;
                }
                else
                {
                    //success
                    //File f = new File(tempoutputfile);
                    // f.
                }
            }
            
            InvisibleModal modal = InvisibleModal.createAndShow();
            try{

            //check integrity.
            m.status("Checking integrity...");
            //byte[] hash = shared.CryptHashes.GetWhirlpool(outputfile);
              //FileUtils.DeleteFile(outputfile);
            byte[] hash = shared.CryptHashes.GetHash(outputfile, shared.CryptHashes.Hashtype.sha1);
            String hashstr = b.BytesToHexString(hash);
            boolean isgood = whirlpool.equals(hashstr);
            if(!isgood)
            {
                m.err("Bad file integrity. The Age downloaded wasn't what was expected, perhaps because the version on the server is corrupted.");
                FileUtils.DeleteFile(outputfile);
                wasSuccessful = false;
                modal.hideInvisibleModal();
                return;
            }
            m.status("File integrity is good!");
            
            //extract.
            if(!shared.sevenzip.extract(outputfile, potsfolder))
            {
                wasSuccessful = false;
                modal.hideInvisibleModal();
                return;
            }
            
            //callback
            //callback.callback(null);
            m.status("Age installed!");

            javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
                    public void run() {
                        gui.UamGui.RefreshInfo(potsfolder);
                    }
                });
            //gui.UamGui.RefreshInfo(potsfolder);
            
            wasSuccessful = true;
            
            }finally{
            modal.hideInvisibleModal();
            }
        }
        else if(job==Jobs.downloadConfig)
        {
            String file = server+"/"+Uam.statusFilename;//"uam.status.txt";
            byte[] result = ThreadDownloader.downloadAsBytes(file);
            if(result==null)
            {
                //failed
                wasSuccessful = false;
                return;
            }
            
            if(potsfolder!=null)
            {
                FileUtils.CreateFolder(potsfolder+uam.Uam.ageArchivesFolder);
                //if(new File(potsfolder+uam.Uam.ageArchivesFolder).getUsableSpace()>result.length+1000000)
                if(FileUtils.HasFreeSpace(potsfolder+uam.Uam.ageArchivesFolder, result.length))
                {
                    FileUtils.WriteFile(potsfolder+uam.Uam.ageArchivesFolder+uam.Uam.statusFilename, result);
                }
                else
                {
                    m.err("There isn't enough free space to save the Age list to disk.");
                }
            }
            
            InvisibleModal modal = InvisibleModal.createAndShow();
            try{
            
        
            //callback
            callback.callback(result);//(ageList);

            wasSuccessful = true;
            
            }finally{
            modal.hideInvisibleModal();
            }
        }
        
    }*/
    
}

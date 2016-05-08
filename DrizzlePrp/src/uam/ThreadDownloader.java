/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

//import java.util.concurrent.atomic.AtomicBoolean;
import java.io.OutputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.IOException;
import java.net.SocketTimeoutException;
import shared.m;
import java.lang.InterruptedException;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;

public class ThreadDownloader extends Thread
{
    String urlstr;
    OutputStream out;
    //AtomicBoolean doCancel;
    boolean doCancel = false;
    boolean doPause = false;
    boolean wasSuccessful = false;
    //long freespace = -1;
    String outputfilename = null;
    GuiModal window;

    GuiModal getwindow()
    {
        if(window==null) window = new GuiModal(null, true, this);
        return window;
    }

    public static boolean downloadAsFile(String url, String filename)
    {
        FileOutputStream out = null;
        try
        {
            File f = new File(filename);
            String folder = f.getParent();
            shared.FileUtils.CreateFolder(folder);
            out = new FileOutputStream(filename);
            ThreadDownloader td = new ThreadDownloader(url, out);
            //td.freespace = f.getUsableSpace();
            //td.outputfilename = filename;
            td.outputfilename = new File(filename).getParent();
            shared.FileUtils.CreateFolder(td.outputfilename);
            td.start();
            td.joinEvenIfInterrupted();
            return td.wasSuccessful;
        }
        catch(FileNotFoundException e)
        {
            //throw new DownloadErrorException("Error opening file for saving: "+e.getMessage());
            m.err("Error opening file for saving: ",e.getMessage());
            return false;
        }
        finally      
        {        
            try{
                if(out!=null) out.close();
            }catch(IOException e){
                m.err("Unable to close FileOutputStream.");
            }
        }
        
    }
    public static byte[] downloadAsBytes(String url)
    {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
                
        ThreadDownloader td = new ThreadDownloader(url, baout);
        td.start();
        td.joinEvenIfInterrupted();
        
        byte[] result = baout.toByteArray();
        try{
            baout.close();
        }catch(IOException e){
            m.err("Unable to close ByteArrayOutputStream.");
        }
        if(td.wasSuccessful) return result;
        else return null;
    }
    
    public void joinEvenIfInterrupted()
    {
        boolean joined = false;
        while(!joined)
        {
            try
            {
                this.join();
                joined = true;
            }
            catch(InterruptedException e){}
        }
    }
    public void cancel()
    {
        synchronized(this)
        {
            doCancel = true;
            if(doPause) unpause();
            //this.interrupt();
            //this.stop();
            //this.window.setVisible(false);
        }
    }
    public void unsafecancel()
    {
        synchronized(this)
        {
            doCancel = true;
            //if(doPause) unpause();
            //try{
            //    this.wait(1000);
            //}catch(InterruptedException e){}
            this.stop();
            getwindow().setVisible(false);
            m.warn("You shouldn't use 'Unsafe Cancel' if at all possible.");
        }
    }
    public void pause()
    {
        synchronized(this)
        {
            doPause = true;
        }
    }
    public void unpause()
    {
        synchronized(this)
        {
            doPause = false;
            this.notifyAll();
        }
    }
    public ThreadDownloader(String urlstr, OutputStream out)
    {
       this.setName("ThreadDownloader:"+this.getName());
        this.urlstr = urlstr;
        this.out = out;
        //doCancel = new AtomicBoolean();
        //doCancel.set(false);
        //window = new GuiModal(null, true, this);
        getwindow();
        //window.setVisible(true);
        //window.sho
        //java.awt.Dialog.ModalityType.
        //java.util.concurrent.atomic.AtomicBoolean b;
        //synchronized(this){}
        
    }
    
    @Override public void run()
    {
        //download.
        getwindow().showButDontBlock();
        getwindow().finishedShowingMonitor.waitCorrectly();
        //try{
        //window.finishedShowingMonitor.wait(1000);
        //}catch(Exception e){}
        
        int sConnectTimeout = 10;
        int sReadTimeout = 10;
        int buffersize = 4096; //1024; //4096 doesn't seem to be a load problem on the cpu at all.
        
        try
        {
            URL url;
            int contentlength;
            try
            {
                urlstr = urlstr.replace(" ", "%20");
                url = new URL(urlstr);
            }
            catch(MalformedURLException e)
            {
                throw new DownloadErrorException("The server address is invalid.");
            }
            if(!url.getProtocol().equals("http") && !url.getProtocol().equals("ftp"))
            {
                throw new DownloadErrorException("The server has an unhandled protocol; it should be either http or ftp.");
            }
            URLConnection conn;
            InputStream in;
            try
            {
                conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "Java/"+gui.Version.GetJavaVersion()+" ("+gui.Version.GetVersion()+")");
                //sun.net.www.http.HttpClient httpclient;
                conn.setConnectTimeout(sConnectTimeout*1000);
                conn.setReadTimeout(sReadTimeout*1000);
                conn.setAllowUserInteraction(false); //otherwise it might popup a password question.
                in = conn.getInputStream();
                contentlength = conn.getContentLength();
                if(contentlength==-1)
                {
                    m.msg("Server doesn't support progress status.");
                }
                //else if(this.freespace!=-1)
                else if(this.outputfilename!=null)
                {
                    //if(contentlength+10000000>this.freespace) //give 10MB slack.
                    if(!shared.FileUtils.HasFreeSpace(outputfilename, contentlength))
                    {
                        throw new DownloadErrorException("There doesn't appear to be enough disk space to download this.");
                    }
                }
            }
            catch(IOException e)
            {
                if(e instanceof SocketTimeoutException)
                {
                    throw new DownloadErrorException("Unable to open connection.");
                    //choose another server?
                }
                else if(e instanceof java.io.FileNotFoundException)
                {
                    //happens when you try to download a file that doesn't exist.
                    throw new DownloadErrorException("The server doesn't have the expected file, check the address.");
                }
                else if(e instanceof java.net.UnknownHostException)
                {
                    //happens when you have a server that doesn't respond.
                    throw new DownloadErrorException("The server is either not running, or the name of the server is wrong, or your network connection is down.");
                }
                else
                {
                    throw new DownloadErrorException(e.getMessage());
                }
            }
            byte[] buffer = new byte[buffersize];
            int read=0;
            int transferred=0;
            while(read!=-1)
            {
                //update progress...
                if(contentlength!=-1 && contentlength!=0)
                {
                    int prog = (int)((1000L*transferred)/contentlength);
                    //Double prog = (100.0*transferred)/contentlength;
                    getwindow().setProgress(prog);
                }

                //check for cancel...
                synchronized(this)
                {
                    if(doCancel)
                    {
                        break;
                    }
                }

                //check for pause...
                synchronized(this)
                {
                    while(doPause)
                    {
                        try{
                            this.wait();
                        }catch(InterruptedException e){} //normal for this to be thrown.
                    }
                }

                try
                {
                    try
                    {
                        read = in.read(buffer);
                    }
                    catch(SocketTimeoutException e)
                    {
                        read = 0;
                        //ask the user to continue?
                        //window.handleReadTimeout();
                        //or:
                        //synchronized(this)
                        //{
                        //    doPause = true;
                        //}
                    }
                    if(read>0)
                    {
                        out.write(buffer, 0, read);
                        transferred += read;
                    }
                }
                catch(IOException e)
                {
                    if(e instanceof SocketException)
                    {
                        //can be "Connection reset". e.g. if the network card is pulled out :P
                        throw new DownloadErrorException("Connection reset; possible problem with network connection or server.");
                    }
                    else if(e instanceof IOException && e.getMessage().equals("There is not enough space on the disk"))
                    {
                        throw new DownloadErrorException("Not enough free disk space.  Free some up, then try again.");
                    }
                    else
                    {
                        throw new DownloadErrorException("Error while reading.");
                    }
                }
            }
            try
            {
                out.flush();
                in.close();
            }
            catch(IOException e)
            {
                throw new DownloadErrorException("Unable to close connection.");
            }
            
            wasSuccessful = read==-1; //did we finish the normal way?

            /*for(int i=0;i<40;i++)
            {
                synchronized(this)
                {
                    if(doCancel)
                    {
                        break;
                    }
                }
                try{
                sleep(1000);
                }catch(InterruptedException e){}
                window.setProgress(i*10);
            }*/

            //try{
            //sleep(10000);
            //}catch(InterruptedException e){}
        }
        catch(DownloadErrorException e)
        {
            //do nothing, but return?
            wasSuccessful = false;
        }
        
        getwindow().setVisible(false);
    }
}

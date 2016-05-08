/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checker;

import java.net.URL;
//import java.net.HttpURLConnection;
//import sun.net.www.protocol.http.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import shared.*;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Date;

public class nettimer
{

    private static java.util.Timer timer;

    /*public static void timer(String address, double interval, String search)
    {
        runner r = new runner(interval,address,search);
        r.start();
        //we lose the reference, but since it's a thread, it doesn't get garbage collected.
    }
    public static class runner extends Thread
    {
        double interval;
        String address;
        String search;
        //boolean stop = false;
        
        public runner(double interval, String address, String search)
        {
            this.interval = interval;
            this.address = address;
            this.search = search;
        }
        
        public void run()
        {
            while(true)
            {
                check(address,search);
                long msToSleep = (long)(interval*1000);
                try
                {
                    Thread.sleep(msToSleep);
                }
                catch(Exception e)
                {
                    //interrupted.
                }
            }
        }
    }
    static void check(String address, String search)
    {
        try {
            m.msg("Checking...");
            URL url = new URL(address);
            Object content = url.getContent();
            InputStream in = new BufferedInputStream(url.openConnection().getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
            in.close();
            byte[] bytes = out.toByteArray();
            String result = b.BytesToString(bytes);
            if (result.indexOf(search) != -1) {
                GuiUtils.showTrayIcon("/gui/Pterosaur2b4-16.png");
                GuiUtils.DisplayTrayMessage("Found", "The query '"+search+"' has been found.");
            }
            int dummy = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void timer(final String address, double periodInSecs, final String search)
    {
        CancelTimers();

        long period = (long)(periodInSecs*1000);
        timer = new java.util.Timer(true);
        timer.schedule(new java.util.TimerTask(){
            public void run()
            {
                m.msg("Checking...");
                byte[] data = shared.HttpUtils.geturl(address);
                String result = b.BytesToString(data);
                if (result.indexOf(search) != -1) {
                    String msg = "The query '"+search+"' has been found.";
                    m.msg(msg);
                    GuiUtils.DisplayTrayMessage("Found", msg);
                }
            }
        }, 0, period);

    }

    public static void SavePageAtTime(final String address, double periodInSecs, final String outfolder, final String prefix, final String suffix)
    {
        CancelTimers();

        long period = (long)(periodInSecs*1000);
        timer = new java.util.Timer(true);
        timer.schedule(new java.util.TimerTask(){
            public void run()
            {
                m.msg("Reading from ",address);
                byte[] data = shared.HttpUtils.geturl(address);
                //String outfile = FileUtils.GetNextFilename(outfolder, "save", suffix);
                String outfile = outfolder+"/"+prefix+FileUtils.SanitizeFilename(shared.DateTimeUtils.GetSortableCurrentDate())+suffix;
                m.msg("Saving to ",outfile);
                FileUtils.WriteFile(outfile, data);
                //GuiUtils.DisplayTrayMessage("File Saved", "The page "+address+" has been saved to "+outfile);
            }
        }, 0, period);
        
    }
    static Vector<Timer> timers;
    public static void SavePageAtSpecificTime(final String address, final String outfolder, String datetime, final String prefix, final String suffix)
    {
        Timer newtimer = new Timer(true);
        if (timers==null) timers = new Vector();
        synchronized(timers)
        {
            timers.add(newtimer);
        }
        long d = Date.parse(datetime);
        Date date = new Date(d);
        newtimer.schedule(new TimerTask(){
            public void run()
            {
                m.msg("Reading from ",address);
                byte[] data = shared.HttpUtils.geturl(address);
                String outfile = outfolder+"/"+prefix+FileUtils.SanitizeFilename(shared.DateTimeUtils.GetSortableCurrentDate())+suffix;
                m.msg("Saving to ",outfile);
                FileUtils.WriteFile(outfile, data);
            }
        }, date);
    }
    public static void CancelTimers()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer = null;
        }
        if(timers!=null)
        {
            synchronized(timers)
            {
                for(Timer t: timers)
                {
                    t.cancel();
                }
                timers.removeAllElements();
            }
        }
    }

}

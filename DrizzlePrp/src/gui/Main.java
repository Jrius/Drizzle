/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package gui;


import javax.swing.UIManager;
import shared.m;
import java.io.File;
import shared.FileUtils;

public class Main extends javax.swing.JFrame {

    /**
     * @param args the command line arguments
     */
    static public Gui guiform;
    static String javaversion = "";
    static double javaversion2 = 0.0;
    static String os = "";
    static String osversion = "";
    static double osversion2 = 0.0;
    static long maxmemory = 0;
    static long requiredmemory;

    static public Runnable debugcheck;
    static String jarpath;
    static File thisJarsFile;
    static long id = shared.RandomUtils.rng.nextLong(); //just a random id for this instance of Drizzle

    //settings
    static final boolean updateenabled = true;
    static int requestedheapsize;
    static final boolean debugupdate = false;


    public static boolean isVistaPlus()
    {
        if(Main.os.toLowerCase().startsWith("windows") && Main.osversion2>5.1)
        {
            return true;
        }
        return false;
    }
    
    public static void main(String[] args)
    {
        //This will work for other threads as well as this one.
        //System.out might be redirected, and we can't unset it, because this might not be terminating the app.
        //For thread names, "main" is used while initializing, and "AWT-EventQueue-<<something>>" may be used if initialized by a button press.
        //If unset, it simply prints to stderr, so let's do that anyway.
        java.lang.Thread.setDefaultUncaughtExceptionHandler(new java.lang.Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                String threadname = t.getName(); //"main" is the main thread.
                String estring = shared.ExceptionUtils.ExceptionToString(e);
                String msg = "Exception in thread: \""+threadname+"\"\n"+estring;
                //System.err.println(msg);
                m.err(msg);
                if(threadname.equals("main"))
                {
                    javax.swing.JOptionPane.showMessageDialog(null, msg, "Uncaught Exception", javax.swing.JOptionPane.ERROR_MESSAGE, null);
                }
            }
        });
        
        //try
        //{
            main2(args);
        //}
        //catch(Exception e)
        //{
        //    System.out.println("exception 3 caught!");
        //    javax.swing.JOptionPane.showMessageDialog(null, "message 3", "title", javax.swing.JOptionPane.ERROR_MESSAGE, null);
        //}
    }
    public static void main2(String[] args)
    {
        
        //exception text:
        //try{
            
        //if(true) throw new RuntimeException("hi");
        //}catch(Exception e){
        //    System.out.println("exception 1 caught!");
        //    javax.swing.JOptionPane.showMessageDialog(null, "message 1", "title", javax.swing.JOptionPane.ERROR_MESSAGE, null);
        //}
        
        debug("New Drizzle instance started...");
        
        //find memory info:
        //int requiredheapsize = 800; //900;  //all Simplicity works with 400 on Win32.
        requiredmemory = 600*1024*1024; //We need 600MB to start normally.
        requestedheapsize = 800; //We will ask for this many MB when launching Drizzle.
        
        try{
            maxmemory = Runtime.getRuntime().maxMemory();
        }catch(Exception e){}
        //m.msg("Heapsize="+Long.toString(maxmemory));

        //find launcher info:
        String islauncherstr = System.getProperty("Drizzle.IsLauncher","true");
        boolean isLauncher = Boolean.parseBoolean(islauncherstr);
        if(maxmemory>=requiredmemory)
        {
            isLauncher = false;
        }
        //m.msg("IsLauncher="+Boolean.toString(isLauncher));

        //find updater info:
        String isupdaterstr = System.getProperty("Drizzle.IsUpdater","false");
        boolean isUpdater = Boolean.parseBoolean(isupdaterstr);
        debug("Maxmemory: "+Long.toString(maxmemory)+" Requiredmemory: "+Long.toString(requiredmemory));
        debug("isLauncher: "+Boolean.toString(isLauncher)+" isUpdater: "+Boolean.toString(isUpdater));

        //find path to this jar:
        jarpath = shared.JarUtils.GetJarPath(Main.class);
        if(jarpath==null) m.err("Jarpath is null.");
        thisJarsFile = new File(jarpath);
        debug("thisJarFile: "+jarpath);

        //check if a new version has been downloaded, and install it, if so.
        PerformUpdate(args,thisJarsFile.getParent(),false); //will halt the JVM if updated.




        if(isLauncher) //then relaunch with correct memsize, etc.
        {
            Main.LaunchDrizzle(jarpath, args, requestedheapsize);
        }
        else if(isUpdater)
        {
            int numretries = 10;
            int mstowait = 2000; //2 seconds
            boolean success = false;
            String genericJar = thisJarsFile.getParent()+"/Drizzle.jar";
            int numtries = 0;
            while(true)
            //for(int i=0;i<numretries;i++)
            {
                try{

                    //copy self to Drizzle.jar
                    debug("Trying to overwrite Drizzle.jar from main...");
                    shared.FileUtils.DeleteFile(genericJar, true);
                    if(shared.FileUtils.Exists(genericJar)) throw new shared.uncaughtexception("Drizzle.jar isn't deleted yet.");
                    shared.FileUtils.CopyFile(thisJarsFile.getAbsolutePath(), genericJar, true, false, true);
                    success = true;
                    break;

                }catch(Exception e){
                    debug("Error while updating Drizzle. It seems Drizzle.jar did not close.");
                    m.err("Error while updating Drizzle. It seems Drizzle.jar did not close.");
                    e.printStackTrace();
                }
                numtries++;

                if(numtries>numretries)
                {
                    debug("Giving up on Drizzle.jar overwriting...");
                    boolean ok = shared.GuiUtils.getOKorCancelFromUserDos(m.trans("Please make sure there are no other copies of Drizzle running, and hit OK to try again."), m.trans("Problem updating")); //This never gets displayed for some reason.
                    if(!ok) break;
                }

                //wait
                try{
                    Thread.sleep(mstowait);
                }catch(Exception e){}
            }

            if(success)
            {
                //launch Drizzle.jar
                Main.LaunchDrizzle(genericJar, args, requestedheapsize);
            }
        }
        else  //start normally
        {
            debug("Normal start...");
            try{
                //javaversion = System.getProperty("java.version");
                javaversion = System.getProperty("java.specification.version");
                System.out.println("Using JRE version: "+javaversion);
                javaversion2 = Double.parseDouble(javaversion);
                /*String[] verparts = version.split(".");
                String verstr = verparts[0]+"."+verparts[1];
                float jreversion = Float.parseFloat(verstr);
                float minver = 1.6f;
                if(jreversion<minver)
                {
                    System.out.println("Your JRE version is too old.");
                }*/
                //System.out.println("Written with JRE version 1.6.0");

                //get jar name:
                //String source = Gui.class.getProtectionDomain().getCodeSource().getLocation().toString();
                //shared.m.msg(source);
            }catch(Exception e){}
            try{
                os = System.getProperty("os.name");
            }catch(Exception e){}
            try{
                osversion = System.getProperty("os.version");
                osversion2 = Double.parseDouble(osversion);
            }catch(Exception e){}

            //initialise plugins for all interfaces.
            Plugins.initialise();

            if(args.length>0)
            {
                //command-line mode.
                
                System.out.println("Using the commandline interface!");

                Main.CloseSplashScreen();
                
                gui.CommandLine.HandleArguments(args);
            }
            else
            {
                //GUI mode.

                //select whether to match the native widgets or use the Swing appearance.
                try
                {
                    //javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                    //javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
                    //javax.swing.UIManager.setLookAndFeel(new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel());
                    //javax.swing.UIManager.setLookAndFeel( sun.java.swing.plaf.gtk.GTKLooktAndFeel());
                    //javax.swing.UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
                    //javax.swing.JDialog j = new javax.swing.JDialog();
                    //j.
                    //javax.swing.UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
                    //javax.swing.UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceModerateLookAndFeel());
                    //javax.swing.UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceBusinessLookAndFeel());
                    //org.jvnet.substance.SubstanceLookAndFeel.setSkin(new org.jvnet.substance.skin.BusinessSkin());
                    //javax.swing.LookAndFeel laf = new com.jgoodies.looks.windows.WindowsLookAndFeel();
                    //javax.swing.LookAndFeel laf = new com.jgoodies.looks.plastic.PlasticXPLookAndFeel();
                    javax.swing.LookAndFeel laf;
                    //laf = new com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel();
                    laf = new com.sun.java.swing.plaf.motif.MotifLookAndFeel();
                    javax.swing.UIManager.setLookAndFeel(laf);

                    shared.GuiUtils.setCrossPlatformFonts(true);
                    //shared.GuiUtils.setBackgroundColour(java.awt.Color.CYAN);

                }
                catch(Exception e){
                    int dummy=0;
                }

                //Run the main form.
                java.awt.EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        //exception test:
                        //try{
                            //if(true) throw new RuntimeException("hi");
                        //}catch(Exception e){
                        //    System.out.println("exception 2 caught!");
                        //    javax.swing.JOptionPane.showMessageDialog(null, "message 2", "title", javax.swing.JOptionPane.ERROR_MESSAGE, null);
                        //}
                        
                        //org.jvnet.substance.SubstanceLookAndFeel.setSkin(new org.jvnet.substance.skin.ModerateSkin());
                        guiform = new Gui();
                        if(debugcheck!=null) debugcheck.run();
                        //java.net.URL url = this.getClass().getResource("Pterosaur2b4-16.png");
                        //javax.swing.ImageIcon image = new javax.swing.ImageIcon(url,"");
                        //java.awt.Image img = image.getImage();
                        //java.awt.Image img = shared.GetResource.getResourceAsImage("/gui/Pterosaur2b4-16.png");
                        //gui.setIconImage(img);
                        guiform.setVisible(true);
                    }
                });
            }
        }
    }
    
    /*public static void message(String msg)
    {
        gui.message(msg);
    }
    public static void message(int msg)
    {
        message(Integer.toString(msg));
    }
    public static void warning(String msg)
    {
        message("Warning: "+msg);
    }
    
    public static void errror(String msg)
    {
        message("Error: "+msg);
    }*/

    public static void PerformUpdate(String[] args, String installDir, boolean warnAboutRestart)
    {
        //Called when the program starts up and whenever Uam downloads something.

        //detect if this is the first installation into this folder
        boolean firstInstall = !(new File(installDir+"/Drizzle.jar").exists());

        boolean thisIsGenericDrizzle = Main.thisJarsFile.getName().equals("Drizzle.jar");

        //find version info:
        //String launchUpdater = FindUpdatedDrizzleJar(new File(installDir),true);
        VersionsInfo info = FindUpdatedDrizzleJar(new File(installDir));

        boolean doupdate = updateenabled && (info.launchUpdater!=null) && (firstInstall || thisIsGenericDrizzle);

        //create the Drizzle.jar file if it doesn't exist, even when we are not doing an update:  (to fix the problem where installing DrizzleY from DrizzleY results in it not being considered an update, and thus Drizzle.jar not getting created.)
        //if launchUpdater is null, then we're not dealing with an 'installed' Drizzle situation anyway.
        /*if( firstInstall && !doupdate && launchUpdater!=null )
        {
            //create Drizzle.jar
            String from = launchUpdater;
            String to = installDir+"/Drizzle.jar";
            shared.FileUtils.CopyFile(from, to, false, false, true);
        }*/
        
        debug("Performing Update...");
        debug("firstInstall: "+Boolean.toString(firstInstall));
        debug("doupdate: "+Boolean.toString(doupdate));
        debug("hasDrizzleExe: "+Boolean.toString(info.hasDrizzleExe));
        debug("maxjar: "+info.maxjar);
        debug("installDir: "+installDir);
        debug("launchUpdater: "+info.launchUpdater);
        

        //save the settings file if applicable.  (Drizzle28 added this save)
        if(doupdate)
        {
            //we're going to terminate, so save settings if settings are set to be saved.
            if(guiform!=null) guiform.SaveSettingsIfApplicable();
        }

        //if we do have an installation, but not Drizzle.jar file, create it.  (Drizzle28 moved this here, so that it is done whether there is an update or not.)
        if(info.hasDrizzleExe && info.maxjar!=null && firstInstall)
        {
            //create the Drizzle.jar file if it doesn't exist, even when we are not doing an update:  (to fix the problem where installing DrizzleY from DrizzleY results in it not being considered an update, and thus Drizzle.jar not getting created.)
            //if launchUpdater is null, then we're not dealing with an 'installed' Drizzle situation anyway.
            debug("Attempting to overwrite Drizzle.jar...");
            String from = info.maxjar;
            String to = installDir+"/Drizzle.jar";
            FileUtils.CopyFile(from, to, false, false, true);

            //copy over settings, if it doesn't already exist
            from = thisJarsFile.getParent()+"/"+Gui.settingsfilename;
            to = installDir+"/"+Gui.settingsfilename;
            if( FileUtils.Exists(from) && !FileUtils.Exists(to) )
            {
                FileUtils.CopyFile(from, to, false, false, false);
            }
        }

        if(doupdate)
        {
            try
            {
                /*if(firstInstall)
                {
                    //create Drizzle.jar
                    String from = info.launchUpdater;
                    String to = installDir+"/Drizzle.jar";
                    shared.FileUtils.CopyFile(from, to, false, false, true);

                    //copy over settings
                    from = thisJarsFile.getParent()+"/"+Gui.settingsfilename;
                    to = installDir+"/"+Gui.settingsfilename;
                    shared.FileUtils.CopyFile(from, to, false, false, false);

                    //if(warnAboutRestart)
                    //{
                    //    msg += m.trans("  Since this is the first time, we'll copy your settings over, and you should use this new copy installed");
                    //}
                }*/
                
                if(warnAboutRestart)
                {
                    //javax.swing.JOptionPane.sh
                    //shared.GuiUtils.getStringFromUser(msg, msg)
                    shared.GuiUtils.DisplayMessage(m.trans("Restarting to update Drizzle..."), m.trans("Drizzle is about to restart, in order to update itself.  You should use the Drizzle.exe (or less preferably, Drizzle.jar) file in the 'Drizzle' subfolder of Uru to start it.  Please don't get confused and use copies elsewhere, and think they are updated; you can always see which version you're using at the top of Drizzle.  And don't forget to have fun :D"));
                }


                //String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                //File ffile = new File(jarpath);
                //String file = ffile.getAbsolutePath();

                String[] command = new String[]{
                    "java",
                    "-Xmx"+Integer.toString(requestedheapsize)+"m",//"-Xmx1020m",//"-Xmx800m",
                    "-DDrizzle.IsUpdater=true",
                    "-jar",
                    info.launchUpdater,
                };

                String[] fullcommand = new String[command.length+args.length];
                for(int i=0;i<command.length;i++)
                {
                    fullcommand[i] = command[i];
                }
                for(int i=0;i<args.length;i++)
                {
                    fullcommand[command.length+i] = args[i];
                }

                debug("Calling updater...");
                Process proc = Runtime.getRuntime().exec(fullcommand);

                //don't wait for this process, terminate now.
                System.exit(0);

                /*if(args.length>0)
                {
                    //only redirect the output/err streams if we have command-line arguments; i.e. command-line interface is being used.
                    shared.m.StreamRedirector.Redirect(proc);
                }*/

                /*if(args.length>0)
                {
                    //this is a command line invocation, so keep the parent open to redirect io.
                    proc.waitFor();
                }*/
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(info.launchUpdater!=null)
        {
            //so we're not going to update, *but* there is a newer version.
            m.msg("You have a newer version of Drizzle installed.  Perhaps you want to use that?  It's located at: ",info.launchUpdater);
        }
        
    }

    private static void LaunchDrizzle(String drizzlefilename, String[] args, int requestedheapsize)
    {
        debug("Launching another Drizzle...");
        try
        {
            /*if(args.length>0)
            {
                System.out.println("commandline2!");
                for(String arg: args)
                {
                    System.out.println(arg);
                }
            }*/

            //File file = shared.GetResource.getResourceAsFile("/drizzle/DrizzlePrp.jar", true);
            //String jarpath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File ffile = new File(drizzlefilename);
            String file = ffile.getAbsolutePath();
            //file = file.substring(1);
            //String filepath = jarurl.getPath();
            //m.msg("jarfile="+file);

            String[] command = new String[]{
                "java",
                "-Xmx"+Integer.toString(requestedheapsize)+"m",//"-Xmx1020m",//"-Xmx800m",
                "-DDrizzle.IsLauncher=false",
                "-jar",
                file,
            };

            String[] fullcommand = new String[command.length+args.length];
            for(int i=0;i<command.length;i++)
            {
                fullcommand[i] = command[i];
            }
            for(int i=0;i<args.length;i++)
            {
                fullcommand[command.length+i] = args[i];
            }
            /*System.out.println("commandline3!");
            for(String arg: fullcommand)
            {
                System.out.println(arg);
            }*/

            final Process proc = Runtime.getRuntime().exec(fullcommand);
            if(args.length>0)
            {
                //only redirect the output/err streams if we have command-line arguments; i.e. command-line interface is being used.
                shared.m.StreamRedirector.Redirect(proc);
            }


            if(args.length>0)
            {
                //this is a command line invocation, so keep the parent open to redirect io.

                Main.CloseSplashScreen();

                //try a shutdown hook for ctrl-c.  There was a problem where ctrl-c would terminate the parent but not the child.  We never know if ctrl-c is hit, but we can do this.
                Runtime.getRuntime().addShutdownHook(
                    new java.lang.Thread(){
                        public void run()
                        {
                            //shutting down, perhaps because of ctrl-c, so terminate the child.
                            //System.out.println("ctrl-c?");
                            proc.destroy(); //kill it!
                        }
                    }
                );

                //this is a command line invocation, so keep the parent open to redirect io.
                proc.waitFor();
            }
            //m.msg("exitval="+Integer.toString(proc.exitValue()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Integer getVersionFromFilename(String filename)
    {
        if(!filename.startsWith("Drizzle")) return null;
        if(!filename.endsWith(".jar")) return null;
        String verstr = filename.substring(7, filename.length()-4);
        if(verstr.equals("")) return null;
        try{
            int r = Integer.parseInt(verstr);
            return r;
        }catch(Exception e){}
        return null;
    }
    private static class VersionsInfo
    {
        Integer maxver; //the largest installed version, or null if none are installed.
        String maxjar; //the path to the largest installed version, or null if none are installed.
        String launchUpdater; //the path to the largest installed version bigger than this one, or null if there are none bigger than this one.
        boolean hasDrizzleExe; //whether Drizzle.exe is in this folder.
    }
    private static VersionsInfo FindUpdatedDrizzleJar(File installDir)
    {
        //String launchUpdater = null;
        //String jarname = thisJarsFile.getName();
        //if(jarname.equals("Drizzle.jar"))
        //{
            //using general Drizzle, check for updates.
            int thisver = Version.version;

            //find the newest version in this folder.
            int maxver = -1;
            File maxjar = null;
            //for(File f: thisJarsFile.getParentFile().listFiles())
            if(installDir.exists())
            {
                for(File f: installDir.listFiles())
                {
                    String curfilename = f.getName();
                    Integer ver = Main.getVersionFromFilename(curfilename);
                    if(ver!=null && ver>maxver)
                    {
                        maxver = ver;
                        maxjar = f;
                    }
                }
            }

            ////do we want an older version?
            //if(excludeIfNotNewerThanCurrent)
            //{
            //    if(maxver>thisver) launchUpdater = maxjar.getAbsolutePath();
            //}
            //else
            //{
            //    if(maxver>-1) launchUpdater = maxjar.getAbsolutePath();
            //}
        //}
        //return launchUpdater;
        boolean hasDrizzleExe = FileUtils.Exists(installDir+"/Drizzle.exe");

        VersionsInfo r = new VersionsInfo();
        if(maxver!=-1) r.maxver = maxver;
        if(maxjar!=null) r.maxjar = maxjar.getAbsolutePath();
        if(maxver>thisver) r.launchUpdater = r.maxjar;
        r.hasDrizzleExe = hasDrizzleExe;
        return r;
    }

    private static void CloseSplashScreen()
    {
        try
        {
            //close the splashscreen
            java.awt.SplashScreen splashscreen = java.awt.SplashScreen.getSplashScreen();
            if(splashscreen!=null) splashscreen.close();
        }
        catch(Exception e)
        {
            //we only tried; it's no big deal if it fails.
        }
    }
    
    private static void debug(String msg)
    {
        if(debugupdate)
        {
            String time = shared.DateTimeUtils.GetSortableCurrentDate();
            String line = Long.toString(id)+": "+time + ": "+msg + "\n";
            shared.FileUtils.AppendText("C:\\DrizzleDebug\\debug.log", line);
        }
    }
}

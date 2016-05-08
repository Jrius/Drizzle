/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.*;
import java.util.HashSet;
import java.io.File;

public class DrizzleIntermediary
{
    private static String ururoot;
    public static void DoWork(String[] args)
    {
        //first arg is "-drizzleintermediary"

        ururoot = FileUtils.GetInitialWorkingDirectory();
        resetlog();
        log("drizzleintermediary!");

        try
        {
            //do tasks
            CleanFiles();
            PatchResolution();
            LaunchUruExplorer(args);
            log("All done!");
        }
        catch(Exception e)
        {
            log(ExceptionUtils.ExceptionToString(e));
        }
    }
    private static void LaunchUruExplorer(String[] args)
    {
        String[] argsToPassOn = new String[args.length-1];
        for(int i=0;i<argsToPassOn.length;i++)
        {
            argsToPassOn[i] = args[i+1];
        }
        Exec.RunAndReturnImmediately(true, ururoot+"/UruExplorer.exe", argsToPassOn);
    }
    private static void PatchResolution()
    {
        //make this compatible with the UruStarter format for now.

    }
    private static void CleanFiles()
    {
        //read whitelist
        if(FileUtils.Exists(ururoot+"/DrizzleIntermediaryWhitelist.txt"))
        {
            String whiteliststr = FileUtils.ReadFileAsString(ururoot+"/DrizzleIntermediaryWhitelist.txt");
            HashSet<String> whitelist = new HashSet();
            for(String s: whiteliststr.split("\n"))
            {
                whitelist.add(s);
            }

            CleanFolder("/python",whitelist);
            //CleanFolder("/Python/system",whitelist);
        }
        else
        {
            log("Whitelist not found; skipping.");
        }
    }
    private static void CleanFolder(String folder, HashSet<String> whitelist)
    {
        File dir = new File(ururoot+folder);
        for(File f: dir.listFiles())
        {
            String relpath = (folder+"/"+f.getName()).toLowerCase();
            if(f.isFile())
            {
                if(!whitelist.contains(relpath))
                {
                    //not allowed
                    log("Deleting file: "+relpath);
                    FileUtils.DeleteFile(f.getAbsolutePath(), true);
                }
            }
            else
            {
                //recurse
                CleanFolder(relpath, whitelist);
            }
        }
    }
    private static void resetlog()
    {
        FileUtils.ZeroFile(ururoot+"/DrizzleIntermediary.log");
    }
    private static void log(String msg)
    {
        FileUtils.AppendText(ururoot+"/DrizzleIntermediary.log", msg+"\n");
    }
}

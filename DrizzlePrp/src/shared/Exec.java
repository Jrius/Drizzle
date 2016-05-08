/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class Exec
{
    public static void LaunchProgram(String execFile, String name)
    {
        String folder = new File(execFile).getParent();
        //String potsfolder = getPotsFolder()+"/";
        //if(!automation.detectinstallation.isFolderPots(potsfolder)) return;
        if(!FileUtils.Exists(execFile))
        {
            m.err("Unable to launch program: ",execFile," because it doesn't exist.");
            return;
        }
        String[] command = new String[]{
            //potsfolder+"UruSetup.exe",
            execFile
        };
        try
        {
            java.lang.Process p = Runtime.getRuntime().exec(command, null, new File(folder));
            //Process proc = Runtime.getRuntime().exec(command);
            m.status(name," launched!");
        }
        catch(java.io.IOException e)
        {
            m.err("Unable to launch ",name+".");
        }

                
    }
    public static void RunAndReturnImmediately(boolean throwexception, String command, String... commandLineArguments)
    {
        RunAndReturnImmediately(throwexception, null,command,commandLineArguments);
    }
    public static void RunAndReturnImmediately(boolean throwexception, String dir, String command, String... commandsLineArguments)
    {
        File d = (dir==null)?null:new File(dir);
        String[] cmdarray = new String[commandsLineArguments.length+1];
        cmdarray[0] = command;
        for(int i=0;i<commandsLineArguments.length;i++) cmdarray[i+1] = commandsLineArguments[i];
        try{
            java.lang.Process p = Runtime.getRuntime().exec(cmdarray, null, d);
        }catch(Exception e){
            if(throwexception) throw new nested(e);
            else e.printStackTrace();
        }
    }
    public static int RunAndWait(String command, String... commandsLineArguments)
    {
        return RunAndWait(null,command,commandsLineArguments);
    }
    public static int RunAndWait(String dir, String command, String... commandsLineArguments)
    {
        File d = (dir==null)?null:new File(dir);
        String[] cmdarray = new String[commandsLineArguments.length+1];
        cmdarray[0] = command;
        for(int i=0;i<commandsLineArguments.length;i++) cmdarray[i+1] = commandsLineArguments[i];
        try{
            java.lang.Process p = Runtime.getRuntime().exec(cmdarray, null, d);
            int errorvalue = p.waitFor();
            return errorvalue;
        }catch(Exception e){
            e.printStackTrace();
            return -1; //should we use a different value?
        }
    }
    public static ReturnInfo RunAndWaitWithStreams(String dirstr, byte[] stdin_data, String command, String... commandsLineArguments)
    {
        ReturnInfo result = new ReturnInfo();
        File dir = (dirstr==null)?null:new File(dirstr);
        String[] cmdarray = new String[commandsLineArguments.length+1];
        cmdarray[0] = command;
        for(int i=0;i<commandsLineArguments.length;i++) cmdarray[i+1] = commandsLineArguments[i];

        try{
            java.lang.Process p = Runtime.getRuntime().exec(cmdarray, null, dir);
            InputStream stdout = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            OutputStream stdin = p.getOutputStream();

            //send input
            if(stdin_data!=null)
            {
                stdin.write(stdin_data);
                //stdin.flush();
                stdin.close();
            }

            //receive output
            result.stdout = StreamUtils.ReadAll(stdout);

            //look at stderr
            result.stderr = StreamUtils.ReadAll(stderr);
            //String errorstr = b.BytesToString(result.stderr);
            //if(!errorstr.equals("")) m.err(errorstr);

            int errorvalue = p.waitFor();
            //if(errorvalue!=0) m.err(Integer.toString(errorvalue));
            result.returnval = errorvalue;
            return result;
        }catch(Exception e){
            e.printStackTrace();
            //return -1; //should we use a different value?
            return null;
        }
    }
    public static class ReturnInfo
    {
        public byte[] stdout;
        public byte[] stderr;
        public int returnval;
    }
}

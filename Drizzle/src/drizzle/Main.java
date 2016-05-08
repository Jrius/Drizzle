/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drizzle;

import java.io.File;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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

            File file = shared.GetResource.getResourceAsFile("/drizzle/DrizzlePrp.jar", true);
            int heapsize = 800; //900;  //all Simplicity works with 400 on Win32.
            String[] command = new String[]{
                "java",
                "-Xmx"+Integer.toString(heapsize)+"m",//"-Xmx1020m",//"-Xmx800m",
                "-jar",
                file.getAbsolutePath(),
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
            Process proc = Runtime.getRuntime().exec(fullcommand);
            if(args.length>0)
            {
                //only redirect the output/err streams if we have command-line arguments; i.e. command-line interface is being used.
                shared.m.StreamRedirector.Redirect(proc);
            }
            proc.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

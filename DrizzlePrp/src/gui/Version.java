/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *
 * @author user
 */
public class Version
{
    public static final int version = 32;
    public static final boolean debug = false;
    private static String javaver;

    public static String GetVersion()
    {
        return "Drizzle"+Integer.toString(version);
    }

    public static String GetJavaVersion()
    {
        if(javaver==null)
        {
            try{
                javaver = System.getProperty("java.version");
            }catch(Exception e){
                javaver = "UnknownVersion";
            }
            if(javaver==null) javaver = "UnknownVersion"; //if it is still null(i.e. not in properties)
        }
        return javaver;
    }
}

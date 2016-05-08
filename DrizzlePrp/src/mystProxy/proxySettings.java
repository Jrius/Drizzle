/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

/**
 *
 * @author user
 */
public class proxySettings
{
    //modifyable settings...
    static boolean usecookies = false;
    public static String[] defaultFiles = {
        "index.htm", "index.html", "default.htm", "default.html",
    };
    static boolean logFilesRead = false;
    
    //shared objects...
    static String basefolder;
    static allPackages packages;
}

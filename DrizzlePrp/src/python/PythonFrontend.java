/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package python;


public class PythonFrontend
{
    //unfinished; meant to be a cleaner way of doing the hard-coded stuff in decompile.java.
    //We must also allow alternative possibilities to be passed in, e.g. <Urufolder>/tools/Python22/python.exe


    private static final String[] pathsToCPython22 = {
        "C:/Python22/python.exe",
        "/usr/bin/python2.2",
    };
    private static final String[] pathsToCPython23 = {
        "C:/Python23/python.exe",
        "/usr/bin/python2.3",
    };
    public static PythonFrontend getCPython22()
    {
        return null;
    }
}

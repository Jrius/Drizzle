/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

/**
 *
 * @author user
 */
public class SuperManager
{
    //age info stuff:
    //private static String rootFolder;
    private static String agefolder;
    private static String sdlfolder;
    private static AgesInfo agesinfo;

    public static void SetAgeInfoFolder(String agefolder, String sdlfolder)
    {
        //SuperManager.rootFolder = rootFolder;
        SuperManager.agefolder = agefolder;
        SuperManager.sdlfolder = sdlfolder;
    }

    public static int[] GetTalcumNotthedroids()
    {
        return new int[]{0x5776aeed, 0x591eb23d, 0xeef5ddea, 0xc6718ce2};
    }

    public static AgesInfo getAgesInfo()
    {
        if(agesinfo==null)
        {
            agesinfo = new AgesInfo(agefolder,sdlfolder);
        }
        return agesinfo;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package filesearcher;

import java.io.File;
import java.util.Vector;
import shared.*;

public class search
{
    public static boolean searchForStrings(File f, String[] searchstrs)
    {
        byte[] data = FileUtils.ReadFile(f);
        String filedata = b.BytesToString(data);
        for(String searchstr: searchstrs)
        {
            int pos = filedata.indexOf(searchstr);
            if(pos==-1) return false;
        }
        return true;
    }
    public static boolean searchForData(File f, byte[] searchdata)
    {
        return (searchForDataPos(f,searchdata)!=-1);
    }
    public static Vector<Integer> searchForDataPoss(File f, byte[] searchdata)
    {
        Vector<Integer> result = new Vector();
        int l = searchdata.length;
        byte[] data = FileUtils.ReadFile(f);
        int numpostocheck = data.length-l+1;
        for(int i=0;i<numpostocheck;i++)
        {
            Bytes b;
            boolean found = true;
            for(int j=0;j<l;j++)
            {
                if(data[i+j]!=searchdata[j])
                {
                    found = false;
                    break;
                }
            }
            if(found)
            {
                result.add(i);
            }
        }
        return result;
    }
    public static int searchForDataPos(File f, byte[] searchdata)
    {
        int l = searchdata.length;
        byte[] data = FileUtils.ReadFile(f);
        int numpostocheck = data.length-l+1;
        for(int i=0;i<numpostocheck;i++)
        {
            Bytes b;
            boolean found = true;
            for(int j=0;j<l;j++)
            {
                if(data[i+j]!=searchdata[j])
                {
                    found = false;
                    break;
                }
            }
            if(found)
            {
                return i;
            }
        }
        return -1;
    }
    public static boolean searchForInt(File f, int searchint)
    {
        return (searchForIntPos(f,searchint)!=-1);
    }
    public static Vector<Integer> searchForIntPoss(File f, int searchint)
    {
        Vector<Integer> r = new Vector();
        byte[] data = FileUtils.ReadFile(f);
        for(int i=0;i<data.length-3;i++)
        {
            int curint = b.BytesToInt32(data, i);
            if(curint==searchint)
            {
                r.add(i);
            }
            if(false)
            {
                int curintbig = b.reverseEndianness(curint);
                if(curintbig==searchint)
                {
                    r.add(i);
                }
            }
        }
        return r;
    }
    public static int searchForIntPos(File f, int searchint)
    {
        byte[] data = FileUtils.ReadFile(f);
        for(int i=0;i<data.length-3;i++)
        {
            int curint = b.BytesToInt32(data, i);
            if(curint==searchint)
            {
                return i;
            }
            if(false)
            {
                int curintbig = b.reverseEndianness(curint);
                if(curintbig==searchint)
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public static boolean searchForString(File f, String searchstr)
    {
        return (searchForStringPos(f,searchstr)!=-1);
    }
    public static int searchForStringPos(File f, String searchstr)
    {
        byte[] data = FileUtils.ReadFile(f);
        String filedata = b.BytesToString(data);
        int index = filedata.indexOf(searchstr);
        //return (index!=-1);
        return index;
    }
    public static Vector<File> getAllFilesWithExtension(String folder, boolean recurse, String extension)
    {
        Vector<File> allfiles = getallfiles(folder,recurse);
        Vector<File> result = new Vector();
        for(File f: allfiles)
        {
            if(f.getName().endsWith(extension))
            {
                result.add(f);
            }
        }
        return result;
    }
    public static Vector<File> getallfiles(String folder, boolean recurse)
    {
        File dir = new File(folder);
        Vector<File> result = new Vector<File>();
        _getallfiles(result,dir,recurse);
        return result;
    }
    static void _getallfiles(Vector<File> result, File dir, boolean recurse)
    {
        File[] children = dir.listFiles();
        if(children==null)
        {
            int dummy=0;
            return;
        }
        for(File child: children)
        {
            if(child.isDirectory())
            {
                if(recurse)
                {
                    _getallfiles(result,dir,recurse);
                }
            }
            else
            {
                result.add(child);
            }
        }
    }
    /*public static File[] getallfiles(String folder, String regexp, boolean recurse)
    {
        File dir = new File(folder);
        return getallfiles(dir,regexp,recurse);
    }
    public static File[] getallfiles(File dir, String regexp, boolean recurse)
    {
        File[] children = dir.listFiles();
        for(File child: children)
        {
            String name = child.getName();
            java.util.regex.
        }
    }*/
}

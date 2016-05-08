/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.io.File;
import shared.CryptHashes.Hashtype;
import shared.m;
import shared.b;

public class diffs
{

    public static boolean FolderDiff(String folderpath1, String folderpath2)
    {
        File folder1 = new File(folderpath1);
        File folder2 = new File(folderpath2);
        return FolderDiff(folder1,folder2);
    }

    //Compare all files and subfolders
    public static boolean FolderDiff(File folder1, File folder2)
    {
        boolean r = true;
        File[] folder1children = folder1.listFiles();
        File[] folder2children = folder2.listFiles();
        for(File child1: folder1children)
        {
            //find the corresponding file in folder2:
            String child1name = child1.getName();
            File child2 = Find(child1name, folder2children);

            if(child2==null)
            {
                m.msg("File removed: ",child1name);
                r = false;
                continue;
            }
            String child2name = child2.getName();

            //recurse if folder
            if(child1.isDirectory())
            {
                boolean isSubFolderEqual = FolderDiff(child1,child2);
            }
            else
            {
                //handle file generically
                byte[] hash1 = shared.CryptHashes.GetHash(child1.getAbsolutePath(), Hashtype.md5);
                byte[] hash2 = shared.CryptHashes.GetHash(child2.getAbsolutePath(), Hashtype.md5);
                boolean filesequal = b.isEqual(hash1, hash2);
                if(filesequal)
                {
                    //do nothing
                    m.msg("File is unchanged: ",child1name);
                }
                else
                {
                    m.msg("File changed: ",child1name);
                    //do type-specific further analysis:
                    if(child1name.endsWith(".prp"))
                    {
                        auto.PrpDiff.FindDiff(child1.getAbsolutePath(), child2.getAbsolutePath());
                    }
                    else if(child1name.endsWith(".pak"))
                    {
                        onliner.Python.ComparePaks(child1.getAbsolutePath(), "pots", child2.getAbsolutePath(), "pots");
                    }
                    //we could also do sdl or age or fni stuff here.
                }
            }

        }

        //check for added files
        for(File child2: folder2children)
        {
            String child2name = child2.getName();
            File child1 = Find(child2name, folder1children);
            if(child1==null)
            {
                m.msg("File added: ",child2name);
                r = false;
            }
        }

        return r;

    }

    private static File Find(String name, File[] children)
    {
        for(File f: children)
        {
            if(name.equals(f.getName()))
            {
                return f;
            }
        }
        return null;
    }

    
}

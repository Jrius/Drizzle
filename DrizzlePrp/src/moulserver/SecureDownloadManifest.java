/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import uru.server.*;
import shared.*;
import java.util.ArrayList;
import java.io.File;

public class SecureDownloadManifest extends mystobj
{
    public ArrayList<Entry> entries = new ArrayList();

    public SecureDownloadManifest(IBytestream c)
    {
        while(true)
        {
            Entry entry = new Entry(c);
            if(entry.filename.toString().length()==0) break; //we've hit the end.
            entries.add(entry);
        }
    }

    public SecureDownloadManifest(){}

    public static SecureDownloadManifest getEmptyManifest()
    {
        SecureDownloadManifest r = new SecureDownloadManifest();
        return r;
    }

    public static SecureDownloadManifest getManifest(String folder, String extension, Manager manager, AuthServerSecureFiles securefiles)
    {
        SecureDownloadManifest r = new SecureDownloadManifest();
        String path = manager.settings.getFileserverPath()+"/SecureDownload/";
        if(!folder.equals("Python") && !folder.equals("SDL"))
        {
            m.throwUncaughtException("Unexpected folder for SecureDownload: "+folder);
        }

        //get all file info:
        File dir = new File(path+folder);
        for(File file: dir.listFiles())
        {
            if(file.isFile() && file.getName().endsWith(extension))
            {
                Entry entry = new Entry();
                entry.filename = new Str(folder+"\\"+file.getName());
                //entry.filesize = (int)file.length();
                entry.filesize = securefiles.GetEncrypted(file.getAbsolutePath()).length;
                r.entries.add(entry);
            }
        }

        return r;
    }

    public static class Entry extends mystobj
    {
        Str filename; //e.g. Python\python.pak //was Utf16.NT
        int filesize;

        public void write(IBytedeque c)
        {
            filename.writeAsUtf16NT(c);
            c.writeIntAsTwoShorts(filesize);
            c.writeShort((short)0); //null terminator for filesize.
        }

        public Entry(){}
        public Entry(IBytestream c)
        {
            filename = Str.readAsUtf16NT(c);
            if(filename.toString().length()==0) return; //not really an entry.
            filesize = c.readIntAsTwoShorts();
            short nt = c.readShort(); //e.ensure(nt==0);
        }
    }

    public void write(IBytedeque c)
    {
        for(Entry entry: entries)
        {
            entry.write(c);
        }
        c.writeShort((short)0); //null terminator for entire file.
    }
}

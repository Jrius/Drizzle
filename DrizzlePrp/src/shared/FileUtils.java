/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package shared;

//import java.io.FileReader;
//import gui.*;
//import uru.*;
import shared.m;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Vector;

import java.nio.channels.FileChannel;

/**
 *
 * @author user
 */
public class FileUtils {
    
    //only properly handles files less than 4GB.
    
    private static String initialWorkingDirectory; //you may have to call some function in FileUtils on startup for this to be correct.
    private static final long freespaceMargin = 10000000;
    
    static
    {
        initialWorkingDirectory = GetPresentWorkingDirectory();
    }

    public static boolean HasPermissions2(String foldername, boolean printerror)
    {
        //This is important for Vista/Win7, because we might not have write permissions there.
        //It's also possible that we have write permissions but not full control, which might potentially be a problem.

        //FileUtils.Exists(foldername);
        File file = new File(foldername);
        if(!file.exists())
        {
            if(printerror) m.err("Folder doesn't exist: ",foldername);
            return false;
        }
        if(!file.canRead())
        {
            if(printerror) m.err("We don't have read permissions for this folder: ",foldername);
            return false;
        }
        if(!file.canWrite())
        {
            if(printerror) m.err("We don't have permission to change this folder: ",foldername);
            return false;
        }

        //this "canWrite()" isn't reliable.  Try creating a file instead.
        File testname = new File(foldername + "/TempTest.dat");
        //try{
        boolean success = FileUtils.WriteFile(testname, new byte[]{'h','i','!'},false,false,false,false);
        FileUtils.DeleteFile(testname, true);
        if(!success)
        {
            if(printerror) m.err("We don't have permission to change this folder: ",foldername);
            return false;
        }
        //}catch(Exception e){
        //    //Error creating test file!
        //}

        //if(printerror) m.err("no perms message");

        //return false; //remove this!
        return true; //otherwise, we're okay!
    }


    public static void SaveFileIfChanged(String filename, byte[] newcontents, boolean createdirs, boolean throwexception)
    {
        if(FileUtils.Exists(filename))
        {
            byte[] oldcontents = FileUtils.ReadFile(filename);
            boolean changed = !b.isEqual(oldcontents, newcontents);
            if(changed)
            {
               FileUtils.WriteFile(filename, newcontents, createdirs, throwexception);
            }
        }
        else
        {
            FileUtils.WriteFile(filename, newcontents, createdirs, throwexception);
        }
    }
    public static void ZeroFile(String filename)
    {
        ZeroFile(new File(filename));
    }
    public static void ZeroFile(File file)
    {
        FileUtils.WriteFile(file, new byte[]{});
    }
    public static String GetRelativePath(File ancestor, File descendant)
    {
        String result = "";
        File curfolder = descendant;
        if(curfolder.isFile()) curfolder = curfolder.getParentFile();
        while(!curfolder.equals(ancestor))
        {
            //result.append(curfolder.getName()+"/");
            result = curfolder.getName()+"/"+result;
            curfolder = curfolder.getParentFile();
        }
        if(descendant.isFile()) result = result + descendant.getName();
        return result.toString();
    }
    public static long GetFilesize(String filename)
    {
        File f = new File(filename);
        long result = f.length();
        return result;
    }
    public static String SanitizeFilename(String filename)
    {
        String result = filename;
        result = result.replace(":", "_");
        result = result.replace("/", "_");
        result = result.replace("\\", "_");
        result = result.replace("*", "_");
        result = result.replace("?", "_");
        result = result.replace("\"", "_");
        result = result.replace("<", "_");
        result = result.replace(">", "_");
        result = result.replace("|", "_");
        return result;
    }
    public static long GetModtime(String filename)
    {
        File f = new File(filename);
        long result = f.lastModified();
        return result;
    }
    public static String GetNextFilename(String folder, String prefix, String suffix)
    {
        final int max = 1000000;
        for(int i=0;i<max;i++)
        {
            String filename = folder+"/"+prefix+Integer.toString(i)+suffix;
            if(!FileUtils.Exists(filename))
            {
                return filename;
            }
        }
        return null;
    }
    public static Vector<String> FindAllDecendants(File dir, boolean relativepath)
    {
        Vector<String> r = new Vector();
        FindAllDecendants(r,dir,relativepath,"");
        return r;
    }
    private static void FindAllDecendants(Vector<String> r, File dir, boolean relativepath, String pathsofar)
    {
        if(dir.isDirectory())
        {
            for(File child: dir.listFiles())
            {
                if(child.isDirectory())
                    FindAllDecendants(r,child,relativepath,pathsofar+child.getName()+"/");
                else
                    FindAllDecendants(r,child,relativepath,pathsofar+child.getName());
            }
        }
        else
        {
            if(relativepath)
            {
                r.add(pathsofar);
            }
            else
            {
                r.add(dir.getAbsolutePath());
            }
        }
    }
    public static Vector<File> FindAllFiles(String folder, String ext, boolean recurse)
    {
        return FindAllFiles(folder,null,ext,recurse);
    }
    public static Vector<File> FindAllFiles(String folder, String prefix, String ext, boolean recurse)
    {
        return FindAllFiles(folder,prefix,ext,recurse,true);
    }
    public static Vector<File> FindAllFiles(String folder, String prefix, String ext, boolean recurse, boolean casesensitive)
    {
        Vector<File> result = new Vector();
        FindAllFiles(result,folder,prefix,ext,recurse,casesensitive);
        return result;
    }
    public static void FindAllFiles(Vector<File> result, String folder, String prefix, String ext, boolean recurse, boolean casesensitive)
    {
        File fold = new File(folder);
        if(!fold.exists() || !fold.isDirectory())
        {
            m.warn("Folder doesn't exist.");
            return;
        }
        for(File f: fold.listFiles())
        {
            if(f.isDirectory())
            {
                if(recurse)
                {
                    FindAllFiles(result,f.getAbsolutePath(),prefix,ext,recurse,casesensitive);
                }
            }
            else if(f.isFile())
            {
                if(casesensitive)
                {
                    if(prefix==null || f.getName().startsWith(prefix))
                    {
                        if(ext==null || f.getName().endsWith(ext))
                        {
                            result.add(f);
                        }
                    }
                }
                else
                {
                    if(prefix==null || f.getName().toLowerCase().startsWith(prefix.toLowerCase()))
                    {
                        if(ext==null || f.getName().toLowerCase().endsWith(ext.toLowerCase()))
                        {
                            result.add(f);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean HasFreeSpace(String filename, long minimum)
    {
        return HasFreeSpace(new File(filename), minimum);
    }
    
    public static boolean HasFreeSpace(File file, long minimum)
    {
        long usablespace = file.getUsableSpace(); //also returns 0 if Vista/Win7 when not enough priviledges.
        boolean result = usablespace > minimum + freespaceMargin;
        return result;
    }
    
    public static String GetInitialWorkingDirectory()
    {
        return initialWorkingDirectory;
    }
    
    public static String GetPresentWorkingDirectory()
    {
        //return new File(".").getPath();
        return new File(".").getAbsolutePath();
    }
    public static boolean Exists(String filename)
    {
        File file = new File(filename);
        return file.exists();
    }
    public static void DeleteFile2(String absoluteFilename)
    {
        File f = new File(absoluteFilename);

        if(!f.isDirectory())
        {
            //String name = entry.getName();
            //String fullfilename = outputfolder+"/"+name;
            m.msg("Deleting ",absoluteFilename);
            if(f.exists() && f.isFile())
            {
                boolean success = f.delete();
                if(!success)
                {
                    m.warn("Unable to delete file: ",absoluteFilename);
                }
            }
        }
        
    }
    public static void DeleteFile(String filename)
    {
        DeleteFile(filename,false);
    }
    public static void DeleteFile(String filename, boolean throwexception)
    {
        DeleteFile(new File(filename),throwexception);
    }
    public static void DeleteFile(File file, boolean throwexception)
    {
        //File file = new File(filename);
        if(file.exists())
        {
            boolean result = file.delete();
            if(!result)
            {
                m.err("Unable to delete file: ",file.getAbsolutePath());
                if(throwexception) m.throwUncaughtException("");
            }
        }
    }
    public static void DeleteTree(File dir, boolean throwexception)
    {
        if(dir.isDirectory())
        {
            for(File child: dir.listFiles())
            {
                DeleteTree(child, throwexception);
            }
        }
        FileUtils.DeleteFile(dir.getAbsolutePath(), throwexception);
    }
    public static void CopyTree(String from, String to, boolean overwrite, boolean throwexception)
    {
        //does not clear the destination tree first, so it will just overlay.
        File f = new File(from);
        if(f.isFile())
        {
            CopyFile(from,to,overwrite,true,throwexception);
        }
        else if(f.isDirectory())
        {
            for(File child: f.listFiles())
            {
                CopyTree(from+"/"+child.getName(),to+"/"+child.getName(),overwrite,throwexception);
            }
        }
        else
        {
            m.throwUncaughtException("Unhandled case.");
        }
    }
    public static void CopyFile(String infile, String outfile, boolean overwrite, boolean createfolder)
    {
        CopyFile(infile,outfile,overwrite,createfolder,false);
    }
    //public static void CopyFile(String infile, String outfile, boolean overwrite, boolean createfolder, boolean throwexception)
    //{
    //    CopyFile(infile,outfile,overwrite,createfolder,throwexception,false);
    //}
    public static void CopyFile(String infile, String outfile, boolean overwrite, boolean createfolder, boolean throwexception/*, boolean copymodtime*/)
    {
        File in = new File(infile);
        File out = new File(outfile);
        FileChannel inchan = null;
        FileChannel outchan = null;
        if(out.exists())
        {
            if(!overwrite) return;
            
            FileUtils.DeleteFile(outfile,throwexception);
        }
        
        
        try
        {
            if(createfolder && !FileUtils.Exists(out.getParent())) FileUtils.CreateFolder(out.getParent());
            inchan = new FileInputStream(in).getChannel();
            outchan = new FileOutputStream(out).getChannel();
            inchan.transferTo(0, inchan.size(), outchan);

        }
        catch(Exception e)
        {
            m.err("Unable to copy file ",infile," to ",outfile);
            if(throwexception) m.throwUncaughtException("");
        }
        finally
        {
            try
            {
                if(inchan!=null) inchan.close();
                if(outchan!=null) outchan.close();
            }
            catch(Exception e)
            {
                m.err("Unable to close file copying channel.");
                if(throwexception) m.throwUncaughtException("");
            }
        }


    }
    static public void CopyModTime(String infile, String outfile)
    {
        //set modtime to match original.  I think this is the only way to do this in java.
        long modtime = new File(infile).lastModified();
        new File(outfile).setLastModified(modtime);
    }
    static public byte[] ReadFile(String filename)
    {
        File file = new File(filename);
        return ReadFile(file);
    }
    static public String ReadFileAsString(File filename)
    {
        return new String(ReadFile(filename));
    }
    static public String ReadFileAsString(String filename)
    {
        File file = new File(filename);
        return ReadFileAsString(file);
    }
    static public Bytes ReadFileAsBytes(String filename)
    {
        File file = new File(filename);
        return ReadFileAsBytes(file);
    }
    public static Bytes ReadFileAsBytes(File filename)
    {
        return new Bytes(ReadFile(filename));
    }
    static public byte[] ReadFile(File filename)
    {
        try
        {
            //FileReader reader = new FileReader(filename);
            FileInputStream reader = new FileInputStream(filename);
            int filelength = (int)filename.length(); //loss of precision.
            byte[] result = new byte[filelength];
            int bytesread = reader.read(result,0,filelength);
            if(bytesread!=filelength) throw new Exception("Incorrect # of bytes read.");
            reader.close();
            return result;
        }
        catch(Exception e)
        {
            m.err("Error reading file:",filename.getAbsolutePath()+": ( "+e.getMessage()," )");
            return null;
        }
        
    }
    static public void WriteFile(String filename, byte[] content, boolean createdirs)
    {
        File file = new File(filename);
        WriteFile(file,content,createdirs);
    }
    static public void WriteFile(String filename, byte[] content)
    {
        File file = new File(filename);
        WriteFile(file,content);
    }
    static public void WriteFile(String filename, Bytes content)
    {
        WriteFile(filename,content.getByteArray());
    }
    static public void WriteFile(File filename, byte[] content)
    {
        WriteFile(filename,content,false);
    }
    static public void WriteFile(File filename, byte[] content, boolean createdirs)
    {
        WriteFile(filename,content,createdirs,false);
    }
    static public void WriteFile(String filename, byte[] content, boolean createdirs, boolean throwexception)
    {
        WriteFile(new File(filename),content,createdirs,throwexception);
    }
    static public boolean WriteFile(File filename, byte[] content, boolean createdirs, boolean throwexception)
    {
        return WriteFile(filename,content,createdirs,throwexception,false,false);
    }
    static public boolean WriteFile(String filename, String content, boolean createdirs, boolean throwexception)
    {
        return WriteFile(new File(filename), b.StringToBytes(content), createdirs, throwexception);
    }
    static public boolean WriteFile(File filename, byte[] content, boolean createdirs, boolean throwexception, boolean printerror, boolean deletefirst)
    {
        if(createdirs)
        {
            boolean success = filename.getParentFile().mkdirs();
            if(!success)
            {
                //not a problem.
                //m.err("Unable to create parent dirs: "+filename.getAbsolutePath());
            }
        }
        if(deletefirst)
        {
            FileUtils.DeleteFile(filename, throwexception);
        }
        try
        {
            FileOutputStream writer = new FileOutputStream(filename);
            int filelength = content.length;
            writer.write(content);
            writer.flush();
            writer.close();
            int actuallength = (int)filename.length(); //loss of precision.
            if(actuallength != filelength)
            {
                throw new Exception("Error writing file, not correct length.");
            }
            //writer.close();
            return true;
        }
        catch(Exception e)
        {
            String[] msg = {"Error writing file:",filename.getAbsolutePath()+":"+e.getMessage()};
            if(throwexception)
            {
                m.throwUncaughtException(msg);
            }
            if(printerror)
            {
                m.err(msg);
            }
            return false;
        }
        
    }
    static public void AppendText(String filename, String text)
    {
        try
        {
            FileOutputStream writer = new FileOutputStream(filename,true); //append
            byte[] bytes = b.StringToBytes(text);
            writer.write(bytes);
            writer.close();
        }
        catch(Exception e)
        {
            m.err("Error appending file:",filename);
        }
    }
    static public void CreateFolder(String filename)
    {
        File f = new File(filename);
        if(f.exists())
        {
            if(f.isDirectory())
            {
                //already created
            }
            else
            {
                m.warn("Unable to create folder because there is already a file with that name: ",filename);
            }
        }
        else
        {
            boolean result = f.mkdirs();
            if(!result)
            {
                m.warn("Unable to create folder: ",filename);
            }
        }
    }
}

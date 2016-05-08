/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import shared.FileUtils;
import java.io.File;
import shared.m;

public class InplaceFile
{
    private String potsfolder;
    private String path;
    private String origpath;
    private String basepath;
    private static final String pathToOriginals = "/originalfiles/wholefiles/";

    public InplaceFile(String potsfolder)
    {
        this(potsfolder,"");
    }
    public InplaceFile(String potsfolder, String path)
    {
        this.potsfolder = potsfolder;
        this.path = path;
        origpath = potsfolder+"/"+path;
        basepath = potsfolder+pathToOriginals+path;
    }
    public InplaceFile File(String newpath)
    {
        return new InplaceFile(potsfolder,newpath);
    }

    public byte[] ReadAsBytes()
    {
        String path = getPath();
        return FileUtils.ReadFile(path);
    }
    /*public shared.SerialBytestream ReadAsBytestream()
    {
        return shared.SerialBytestream.createFromFilename(basepath)
    }*/
    private String getPath()
    {
        if(FileUtils.Exists(basepath)) return basepath;
        else return origpath;
    }
    public void SaveFile(byte[] data)
    {
        ensureBaseSaved();
        FileUtils.WriteFile(origpath, data);
    }
    public void SaveFile(shared.IBytedeque c)
    {
        ensureBaseSaved();
        c.writeAllBytesToFile(origpath);
    }
    private void ensureBaseSaved()
    {
        if(!hasBaseSaved())
        {
            //save the base file!
            FileUtils.CopyFile(origpath, basepath, false, true, true);
        }
    }
    private boolean hasBaseSaved()
    {
        return FileUtils.Exists(basepath);
    }
    public void RestoreOriginal()
    {
        m.warn("untested");
        FileUtils.CopyFile(basepath, origpath, true, true, true);
    }
    public void RestoreAllOriginals()
    {
        m.warn("untested");
        FileUtils.CopyTree(potsfolder+pathToOriginals, potsfolder, true, true);
    }
    public boolean exists()
    {
        String path = getPath();
        return FileUtils.Exists(path);
    }
    public String toString()
    {
        return "InplaceFile: "+path;
    }
}

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

package prpobjects;

import java.io.File;
import shared.m;
import uru.Bytestream;
import uru.context;
import shared.readexception;
import uru.Bytedeque;
import shared.b;
//import org.bouncycastle.crypto.digests.MD5Digest;
import shared.Bytes;
import java.util.Vector;

public class sumfile
{
    public int filecount;
    int u1; //always 0?
    public sumfileFileinfo[] files;
    
    /*public static byte[] createSumfile(String... files)
    {
        Bytedeque c = new Bytedeque();
        MD5Digest digester = new MD5Digest();
        
        int numfiles = files.length;
        c.writeInt(numfiles);
        c.writeInt(0);
        for(int i=0;i<numfiles;i++)
        {
            //get name
            File curfile = new File(files[i]);
            String curfilename = curfile.getName();
            Urustring name = Urustring.createFromString("dat\\"+curfilename);
            name.compile(c);
            
            //read file
            Bytes data = shared.FileUtils.ReadFileAsBytes(curfile);
            
            //get md5
            digester.update(data.getByteArray(),0,data.length());
            byte[] hash = new byte[16];
            digester.doFinal(hash, 0);
            
            //write md5
            c.writeBytes(hash);
            
            c.writeInt(0); //supposed to be the mtime timestamp.
            c.writeInt(0);
        }
        
        
        return c.getAllBytes();
    }*/
    
    public static sumfile readFromFile(File f, int readversion)
    {
        sumfile result = null;
        try{
            result = new sumfile(shared.FileUtils.ReadFile(f),true,readversion);
        }catch(shared.readexception e){
            m.err("Error reading sumfile:",f.getName());
        }
        return result;
    }
    
    public sumfile(byte[] filedata, boolean isencrypted, int readversion) throws readexception
    {
        if(isencrypted)
        {
            filedata = uru.UruCrypt.DecryptWhatdoyousee(filedata);
        }
        
        context c = new context(new Bytestream(filedata));
        c.readversion = readversion;
        
        filecount = c.readInt();
        u1 = c.readInt();
        files = c.readArray(sumfileFileinfo.class, filecount);
    }
    
    public static class sumfileFileinfo extends uruobj
    {
        public Urustring filename;
        public byte[] md5;
        public int timestamp;
        int u2; //always 0?
        
        //private sumfileFileinfo()
        //{
        //}
        
        public sumfileFileinfo(context c)
        {
            filename = new Urustring(c);
            md5 = c.readBytes(16);
            timestamp = c.readInt();
            u2 = c.readInt();
        }
    }
    public static void regenerateSumfile(String potsfolder, String agename)
    {
        Bytes bytes = createSumfile(potsfolder+"/dat/", agename);
        String outfile = potsfolder+"/dat/"+agename+".sum";
        shared.FileUtils.WriteFile(outfile, bytes);
    }
    
    public static Bytes createEmptySumfile(/*String infolder, String agename*/)
    {
        
        //count the files.
        //Vector<String> files = new Vector<String>();
        
        //int count = files.size();
        int count = 0;
        
        Bytedeque c = new Bytedeque(shared.Format.none);
        c.writeInt(count);
        c.writeInt(0);
        
        byte[] result = c.getAllBytes();
        result = uru.UruCrypt.EncryptWhatdoyousee(result);
        
        //shared.FileUtils.WriteFile(outfolder+agename+".sum", result);
        return new Bytes(result);
    }
    
    public static Bytes createSumfile(String infolder, String agename)
    {
        File datdir = new File(infolder);
        if(!datdir.exists() || !datdir.isDirectory())
        {
            m.err("Dat folder not found.");
            return null;
        }
        
        //count the files.
        Vector<String> files = new Vector<String>();
        File[] datfiles = datdir.listFiles();
        for(int i=0;i<datfiles.length;i++)
        {
            String curfilename = datfiles[i].getName();
            if(curfilename.startsWith(agename+"_") && curfilename.endsWith(".prp"))
            {
                //add this file.
                files.add(curfilename);
            }
        }
        
        int count = files.size();
        
        Bytedeque c = new Bytedeque(shared.Format.none);
        c.writeInt(count);
        c.writeInt(0);
        for(int i=0;i<datfiles.length;i++)
        {
            String curfilename = datfiles[i].getName();
            if(curfilename.startsWith(agename+"_") && curfilename.endsWith(".prp"))
            {
                //add this file.
                
                //filename
                byte[] path = {'d','a','t','\\'};
                byte[] filenameBytes = b.appendBytes(path, b.StringToBytes(curfilename));
                Urustring filename = new Urustring(filenameBytes);
                filename.compile(c);
                
                //md5
                byte[] filecontents = shared.FileUtils.ReadFile(datfiles[i]);
                byte[] md5 = shared.CryptHashes.GetMd5(filecontents);
                c.writeBytes(md5);
                
                //long timestamp = datfiles[i].lastModified()/1000L;
                //int timestamp2 = (int)timestamp;
                //c.writeInt(timestamp2);
                c.writeInt(0); //supposed to be timestamp, but doesn't matter. # of seconds since epoch
                c.writeInt(0);
            }
        }
        
        byte[] result = c.getAllBytes();
        result = uru.UruCrypt.EncryptWhatdoyousee(result);
        
        //shared.FileUtils.WriteFile(outfolder+agename+".sum", result);
        return new Bytes(result);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.*;
import java.util.HashMap;

public class ChunkHandler
{
    public HashMap<Integer, ChunkFile> files = new HashMap();

    public ChunkFile startfile(String filename, int filesize, int transId)
    {
        ChunkFile file = files.get(transId);
        if(file==null)
        {
            file = new ChunkFile(filename,filesize);
            files.put(transId, file);
            return file;
        }
        else
        {
            throw new shared.uncaughtexception("File already started?");
        }
    }

    public ChunkFile addChunk(int transId, int filesize, int offset, byte[] chunk)
    {
        ChunkFile file = files.get(transId);
        if(file==null)
        {
            //file = new ChunkFile(filesize);
            //files.put(transId, file);
            m.throwUncaughtException("File wasn't requested?");
        }
        file.addChunk(offset, chunk);
        return file;
    }

    public ChunkFile clearfile(int transId)
    {
        ChunkFile file = files.get(transId);
        files.remove(transId);
        return file;
    }
    
    public static class ChunkFile
    {
        String filename;
        byte[] data;
        boolean done;

        public ChunkFile(String filename2, int filesize)
        {
            filename = filename2;
            data = new byte[filesize];
            done = false;
        }

        public void addChunk(int offset, byte[] chunk)
        {
            b.CopyBytes(chunk, data, offset);
            if(data.length==offset+chunk.length) done = true;
        }

        public void saveToFile(String filename)
        {
            if(!done) m.throwUncaughtException("File wasn't done yet!");
            FileUtils.WriteFile(filename, data, true, true); //create dirs and throw exception
        }

        public byte[] getBytes()
        {
            return data;
        }
    }
}

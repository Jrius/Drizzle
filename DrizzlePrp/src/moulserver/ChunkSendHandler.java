/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.*;
import java.util.HashMap;
import java.io.InputStream;

public class ChunkSendHandler
{

    public static final int maxbufsize = 100000; //100000 is a little choppy

    public HashMap<Integer, ChunkFile> files = new HashMap(); ///transId->file

    public ChunkFile startfile(String filename, int filesize, int transId, InputStream is, boolean encrypt)
    {
        ChunkFile file = files.get(transId);
        if(file==null)
        {
            file = new ChunkFile(filename,filesize,is,encrypt);
            files.put(transId, file);
            return file;
        }
        else
        {
            throw new shared.uncaughtexception("File already started?");
        }
    }

    /*public ChunkFile addChunk(int transId, int filesize, int offset, byte[] chunk)
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
    }*/

    public ChunkFile ack(int transId)
    {
        return files.get(transId);
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
        //byte[] data;
        boolean done;
        InputStream is;
        int offset;
        int filesize;
        boolean encrypt;

        public ChunkFile(String filename2, int filesize2, InputStream is2, boolean encrypted)
        {
            filename = filename2;
            filesize = filesize2;
            //data = new byte[filesize];
            is = is2;
            done = false;
            offset = 0;
            encrypt = encrypted;
        }

        public void addChunk(int offset, byte[] chunk)
        {
            //b.CopyBytes(chunk, data, offset);
            //if(data.length==offset+chunk.length) done = true;
        }

        /*public void saveToFile(String filename)
        {
            if(!done) m.throwUncaughtException("File wasn't done yet!");
            FileUtils.WriteFile(filename, data, true, true); //create dirs and throw exception
        }*/

        public int offset()
        {
           return offset;
        }

        public byte[] read()
        {
            try{
                

                int remaining = filesize-offset;
                int bufsize = (remaining<maxbufsize)?remaining:maxbufsize;
                byte[] r = new byte[bufsize];
                int read = 0;
                while(read!=bufsize) //this whole thing might be overkill, as it should just read all that we need.  We could just throw an exception if not.
                {
                    int numread = is.read(r,read,bufsize-read);
                    if(numread==-1) m.throwUncaughtException("unexpected"); //end of stream should not be hit.
                    read += numread;
                }
                offset += bufsize;
                if(offset==filesize) done = true;

                if(encrypt)
                {
                    //actually, let's just ignore this, and assume they are all encrypted on disk.
                }

                return r;
            }catch(Exception e){
                throw new shared.nested(e);
            }
        }
    }
}

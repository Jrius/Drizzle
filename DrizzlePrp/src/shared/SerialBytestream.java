/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import shared.b;
import java.util.Vector;
import shared.*;

//public class SerialBytestream implements IBytestream
public class SerialBytestream extends IBytestream
{
    //String sourceName; //was filename
    //FileInputStream in;
    BufferedInputStream in;
    int pos = 0;
    int filelength;
    
    public int getAbsoluteOffset()
    {
        return pos;
    }
    public int getFilelength()
    {
        return filelength;
    }
    public int getBytesRemaining()
    {
        return getFilelength() - getAbsoluteOffset();
    }
    public static IBytestream createFromFile(File f)
    {
        return createFromFilename(f.getAbsolutePath());
    }
    public static IBytestream createFromFilename(String filename)
    {
        return createFromFilenameOffset(filename,0);
    }
    @Override public void close()
    {
        try{
            if(this.in!=null)
            {
                this.in.close();
                this.in = null;
            }
        }catch(Exception e)
        {
            m.err("Unable to close SerialBytestream.");
        }
    }
    protected SerialBytestream(){}
    public static IBytestream createFromFilenameOffset(String filename, int offset)
    {
        SerialBytestream result = new SerialBytestream();
        //serial = true;
        result.sourceName = filename;
        result.openfile();
        result.skip(offset);
        return result;
    }
    public SerialBytestream(String filename)
    {
        this(filename,0);
    }
    public SerialBytestream(String filename, int offset)
    {
        //SerialBytestream result = new SerialBytestream();
        //serial = true;
        sourceName = filename;
        openfile();
        skip(offset);
        //return result;
    }
    protected int read()
    {
        try
        {
            pos += 1;
            int result = in.read();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read byte, probably hit end of file.:"+e.getMessage());
        }
    }
    public byte readByte()
    {
        try
        {
            pos += 1;
            byte result = (byte)in.read();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read byte, probably hit end of file.:"+e.getMessage());
        }
    }
    public byte[] readBytes(int num)
    {
        try
        {
            pos += num;
            byte[] result = new byte[num];
            in.read(result);
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read bytes, probably hit end of file.:"+e.getMessage());
        }
        
    }
    public short readShort()
    {
        try
        {
            pos += 2;
            byte[] buffer = new byte[2];
            in.read(buffer);
            short result = b.BytesToInt16(buffer, 0);
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read short, probably hit end of file.:"+e.getMessage());
        }
    }
    public int readInt()
    {
        try
        {
            pos += 4;
            byte[] buffer = new byte[4];
            in.read(buffer);
            int result = b.BytesToInt32(buffer, 0);
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read int, probably hit end of file.:"+e.getMessage());
        }
        
    }
    /*public int[] readInts(int num)
    {
        try
        {
            //pos += 4*num;
            int[] result = new int[num];
            for(int i=0;i<num;i++)
            {
                result[i] = readInt();
            }
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to read ints, probably hit end of file.:"+e.getMessage());
        }
        
    }*/
    
    private void openfile()
    {
        File f = new File(sourceName);
        this.filelength = (int)f.length();
        if(!f.exists()) throw new shared.uncaughtexception("File doesn't exist:"+sourceName);
        try
        {
            this.in = new BufferedInputStream(new FileInputStream(f));
        }
        catch(java.io.FileNotFoundException e)
        {
            throw new shared.uncaughtexception("File doesn't exist:"+sourceName);
        }
    }
    public IBytestream Fork()
    {
        return Fork(pos);
    }
    public IBytestream Fork(long offset)
    {
        SerialBytestream result = new SerialBytestream();
        result.sourceName = this.sourceName;
        result.openfile();
        result.skip(offset);
        return result;
    }
    public void skip(long n)
    {
        try
        {
            pos += n;
            in.skip(n);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to skip bytes.  Probably hit end of file: "+e.getMessage());
        }
    }
    public String toString()
    {
        int readahead = 128; //you can change this.
        
        //in.mark(readahead + 1024);
        IBytestream fork = this.Fork();
        
        String result = "\n(source="+this.sourceName+")\n";
        try
        {
            result += "(pos=0x"+Integer.toHexString(fork.getAbsoluteOffset())+"="+Integer.toString(fork.getAbsoluteOffset())+")\n";
            result += "(bytes remaining="+Integer.toString(this.getBytesRemaining())+")\n";
            result += "Data:\n";
            //if(!( pos + readahead <= maxpos+1 ))
            //{
            //    //this would go past the end of the file.
            //    readahead = maxpos+1-pos;
            //}
            if(fork.getBytesRemaining()<readahead)
            {
                readahead = fork.getBytesRemaining();
            }
            if(fork.getBytesRemaining()<0)
            {
                throw new uncaughtexception("We've somehow read past the end of the file.  Fix this now!");
            }
            if(fork.getBytesRemaining()==0)
            {
                result += "End of File.\n";
            }
            for(int i=0;i<readahead;i++)
            {
                //byte by = data[pos+i];
                byte by = fork.readByte();
                String hex = Integer.toHexString(b.ByteToInt32(by));
                if(hex.length()==0) hex = "00";
                if(hex.length()==1) hex = "0"+hex;
                //if(hex.length()>2) hex = hex.substring(hex.length()-3);
                result += hex;
                if(i % 4==3) result += " ";
                if(i%16==15) result += "\n";
            }
        }
        catch(Exception e)
        {
            result += "Error reading ahead in stream.";
        }
        
        try
        {
            //in.reset();
        }
        catch(Exception e)
        {
            //this shouldn't ever happen.
            e.printStackTrace();
            throw new shared.uncaughtexception("Unable to reset position in stream.");
        }
        
        return result;
    }
}

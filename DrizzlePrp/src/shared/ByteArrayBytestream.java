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

public class ByteArrayBytestream extends IBytestream
{
    public byte[] data;
    int pos;
    
    
    private ByteArrayBytestream(){}
    public static ByteArrayBytestream createFromByteArray(byte[] data)
    {
        ByteArrayBytestream result = new ByteArrayBytestream();
        result.data = data;
        result.pos = 0;
        return result;
    }

    public void skip(long n)
    {
        pos += n;
        if(pos>=data.length) m.throwUncaughtException("Skipped past end of array.");
    }

    protected int read()
    {
        return b.ByteToInt32(readByte());
    }
    public byte readByte()
    {
        byte result = data[pos];
        pos++;
        return result;
    }
    public byte[] readBytes(int num)
    {
        byte[] result = new byte[num];
        for(int i=0;i<num;i++)
        {
            result[i] = readByte();
        }
        return result;
    }
    public int readInt()
    {
        int result = b.BytesToInt32(data, pos);
        pos+=4;
        return result;
    }
    public short readShort()
    {
        short result = b.BytesToInt16(data, pos);
        pos+=2;
        return result;
    }
    public int getAbsoluteOffset()
    {
        return pos;
    }
    public int getFilelength()
    {
        return data.length;
    }
    public int getBytesRemaining()
    {
        return getFilelength()-getAbsoluteOffset();
    }
    public IBytestream Fork(long offset)
    {
        ByteArrayBytestream result = new ByteArrayBytestream();
        result.data = data;
        result.pos = (int)offset;
        return result;
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

            fork.close();
        }
        catch(Exception e)
        {
            result += "Error reading ahead in stream.";
        }
        return result;
    }
}

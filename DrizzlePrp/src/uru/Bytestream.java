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

package uru;

//import gui.Main;
import shared.b;
import java.util.Vector;
import shared.m;
import shared.Bytes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import shared.*;
/**
 *
 * @author user
 */
//public class Bytestream implements IBytestream
public class Bytestream extends IBytestream
{
    
    //do not change.
    private byte[] data;
    private int minpos;
    private int maxpos;
    //private int length; //fullLength
    //private boolean serial = false;
    //private FileInputStream in;

    //okay to change.
    private int pos;
    public boolean throwExceptionOnEof = false;
    
    public int getBytesRemaining()
    {
        throw new uncaughtexception("Haven't implemented GetBytesRemaining yet.");
    }
    public int getFilelength()
    {
        throw new uncaughtexception("Haven't implemented GetFileLength yet.");
    }
    public IBytestream Fork(long offset)
    {
        //throw new uncaughtexception("Haven't implemented fork yet.");
        return new Bytestream(this, (int)offset);
    }
    public Bytestream(byte[] newData)
    {
        
        data = newData;
        int length = data.length;
        maxpos = length-1;

        pos = 0;
    }
    private Bytestream(){};
    public boolean eof()
    {
        return( pos > maxpos );
    }
    public static Bytestream createFromBytes(Bytes newData)
    {
        Bytestream result = new Bytestream();
        
        result.data = newData.getByteArray();
        int length = result.data.length;
        result.maxpos = length-1;
        
        result.pos = 0;
        
        return result;
    }
    public static Bytestream createFromFilename(String filename)
    {
        return createFromBytes(shared.Bytes.createFromFile(filename));
    }
    /*public static Bytestream createSerial(String filename)
    {
        Bytestream result = new Bytestream();
        result.serial = true;
        File f = new File(filename);
        if(!f.exists()) throw new shared.uncaughtexception("File doesn't exist:"+filename);
        try
        {
            result.in = new FileInputStream(f);
        }
        catch(java.io.FileNotFoundException e)
        {
            throw new shared.uncaughtexception("File doesn't exist:"+filename);
        }
        return result;
    }*/
    public Bytestream Fork()
    {
        return new Bytestream(this,pos);
    }
    public Bytestream Fork(int offset)
    {
        return new Bytestream(this, offset);
    }
    //this should work fine, but we aren't using it right now.
    /*public Bytestream(Bytestream parent, int length)
    {
        data = parent.data;
        minpos = parent.pos;
        maxpos = minpos + length - 1;

        pos = minpos;
        if ( maxpos+1 > data.length )
        {
            msg("bytestream: maxpos is out of range.");
        }
    }*/
    
    public Bytestream(Bytestream parent, int absoluteOffset)
    {
        data = parent.data;
        minpos = absoluteOffset;
        maxpos = data.length-1;
        
        pos = minpos;
        if ( minpos < 0 || maxpos+1 > data.length )
        {
            m.msg("bytestream: maxpos is out of range.");
        }
    }
    public int getAbsoluteOffset()
    {
        return pos;
    }
    public int getNumBytesProcessed()
    {
        return pos - minpos;
    }
    /*private void msg(String s)
    {
        Main.message(s);
    }*/

    //this works, but you shouldn't use it, since it hides the size.
    //public <T extends mystobj> T[] readVector()
    //{
    //    int size = this.readInt();
    //    T[] result = readVector(size);
    //    return result;
    //}
    
    /*public <T extends mystobj> T[] readVector(int size)
    {
        T[] result = (T[])(new Object[size]); //cast from Object[], so we can create the array. Otherwise we get a compile error.
        for(int i=0;i<size;i++)
        {
            mystobj newobj = T.create(this); //create the object.
            T newT = (T)newobj; //cast the object.
            result[i] = newT; //put it in the array.
        }
        return result;
    }*/
    
    public byte peekByte()
    {
        if( pos + 1 <= maxpos+1 )
        {
            return data[pos];
        }
        else
        {
            //if(throwExceptionOnEof) throw new RuntimeException("Eof");
            m.msg("end of file in readByte");
            return 0;
        }
    }
    
    public short peekShort()
    {
        if( pos + 2 <= maxpos+1 )
        {
            return b.BytesToInt16(data, pos);
        }
        else
        {
            //if(throwExceptionOnEof) throw new RuntimeException("Eof");
            m.msg("end of file in readShort");
            return 0;
        }
    }
    
    public int peekInt()
    {
        if( pos + 4 <= maxpos+1 )
        {
            return b.BytesToInt32(data, pos);
        }
        else
        {
            m.msg("end of file in readShort");
            return 0;
        }
    }
    protected int read()
    {
        return b.ByteToInt32(readByte());
    }
    public byte readByte()
    {
        byte result = peekByte();
        pos += 1;
        return result;
    }
    public short readShort()
    {
        short result = peekShort();
        pos += 2;
        return result;
    }
    public int readInt()
    {
        int result = peekInt();
        pos += 4;
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
    public short[] readShorts(int num)
    {
        short[] result = new short[num];
        for(int i=0;i<num;i++)
        {
            result[i] = readShort();
        }
        return result;
    }
    /*public int[] readInts(int num)
    {
        int[] result = new int[num];
        for(int i=0;i<num;i++)
        {
            result[i] = readInt();
        }
        return result;
    }*/
    
    public void skipBytes(int numToSkip)
    {
        if( pos + numToSkip <= maxpos+1 )
        {
            pos += numToSkip;
        }
        else
        {
            m.msg("end of file in readByte");
        }
    }
    
    public void skipShorts(int numToSkip)
    {
        if( pos + 2*numToSkip <= maxpos+1 )
        {
            pos += 2*numToSkip;
        }
        else
        {
            m.msg("end of file in readByte");
        }
    }
    
    public void skipInts(int numToSkip)
    {
        if( pos + 4*numToSkip <= maxpos+1 )
        {
            pos += 4*numToSkip;
        }
        else
        {
            m.msg("end of file in readByte");
        }
    }
    public void skipByte()
    {
        skipBytes(1);
    }
    public void skipShort()
    {
        skipShorts(1);
    }
    public void skipInt()
    {
        skipInts(1);
    }
    
    public String toString()
    {
        int readahead = 128; //you can change this.

        String result = "(pos=0x"+Integer.toHexString(pos)+"="+Integer.toString(pos)+")\n";
        result += "Data:\n";
        if(!( pos + readahead <= maxpos+1 ))
        {
            //this would go past the end of the file.
            readahead = maxpos+1-pos;
        }
        for(int i=0;i<readahead;i++)
        {
            byte by = data[pos+i];
            String hex = Integer.toHexString(b.ByteToInt32(by));
            if(hex.length()==0) hex = "00";
            if(hex.length()==1) hex = "0"+hex;
            //if(hex.length()>2) hex = hex.substring(hex.length()-3);
            result += hex;
            if(i % 4==3) result += " ";
            if(i%16==15) result += "\n";
        }
        return result;

    }
        
}

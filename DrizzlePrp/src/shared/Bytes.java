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

//import uru.*;
import java.util.Vector;
import java.lang.Byte;

public class Bytes
{
    byte[] bytes;
    
    public boolean equalsString(String str)
    {
        Bytes b2 = Bytes.create(str);
        return isequal(b2);
    }
    public static Bytes create(Vector<Byte> bytevector)
    {
        Bytes result2 = new Bytes();
        int size = bytevector.size();
        byte[] result = new byte[size];
        for(int i=0;i<size;i++)
        {
            result[i] = bytevector.get(i);
        }
        result2.bytes = result;
        return result2;
    }
    public static Bytes create(byte ... bytelist)
    {
        Bytes result = new Bytes();
        result.bytes = bytelist;
        return result;
    }
    public static Bytes create(String str)
    {
        int length = str.length();
        byte[] result = new byte[length];
        for(int i=0;i<length;i++)
        {
            result[i] = (byte)(str.charAt(i) & 0xFF);
        }
        
        Bytes result2 = new Bytes();
        result2.bytes = result;
        return result2;
    }
    public static Bytes createFromFile(String filename)
    {
        return create(FileUtils.ReadFile(filename));
    }
    public static Bytes createFromFile(java.io.File filename)
    {
        return create(FileUtils.ReadFile(filename));
    }
    public static Bytes createFromArray(byte[] array, int offset, int length)
    {
        byte[] result = new byte[length];
        for(int i=0;i<length;i++) result[i] = array[offset+i];
        return new Bytes(result);
    }
    public static void copyFromArrayToArray(byte[] inArray, int inOffset, byte[] outArray, int outOffset, int length)
    {
        for(int i=0;i<length;i++)
        {
            outArray[outOffset+i] = inArray[inOffset+i];
        }
    }
    public static int BytesToInt32(byte[] bytes, int startpos)
    {
        //return (((int)bytes[startpos+0])<<0) | (((int)bytes[startpos+1])<<8) | (((int)bytes[startpos+2])<<16) | (((int)bytes[startpos+3])<<24);
        int a = ByteToInt32(bytes[startpos+0])<<0;
        int b = ByteToInt32(bytes[startpos+1])<<8;
        int c = ByteToInt32(bytes[startpos+2])<<16;
        int d = ByteToInt32(bytes[startpos+3])<<24;
        int result = a | b | c | d;
        return result;
    }
    public static short BytesToInt16(byte[] bytes, int startpos)
    {
        int a = ByteToInt32(bytes[startpos+0])<<0;
        int b = ByteToInt32(bytes[startpos+1])<<8;
        short result = (short)( a | b );
        return result;
        
    }
    public static long BytesToInt64(byte[] bytes, int startpos)
    {
        int a = ByteToInt32(bytes[startpos+0])<<0;
        int b = ByteToInt32(bytes[startpos+1])<<8;
        int c = ByteToInt32(bytes[startpos+2])<<16;
        int d = ByteToInt32(bytes[startpos+3])<<24;
        int e = ByteToInt32(bytes[startpos+0])<<32;
        int f = ByteToInt32(bytes[startpos+1])<<40;
        int g = ByteToInt32(bytes[startpos+2])<<48;
        int h = ByteToInt32(bytes[startpos+3])<<56;
        int result = a | b | c | d | e | f | g | h;
        return result;
    }
    public Bytes substr(int start, int length)
    {
        byte[] result = new byte[length];
        for(int i=0;i<length;i++)
        {
            result[i] = bytes[start+i];
        }
        return new Bytes(result);
    }
    public Bytes substr(int start)
    {
        return substr(start, this.length()-start);
    }
    public static byte[] flatten(byte[][] bytes)
    {
        int size=0;
        for(int i=0;i<bytes.length;i++)
        {
            size += bytes[i].length;
        }

        byte[] result = new byte[size];
        int pos = 0;
        for(int i=0;i<bytes.length;i++)
        {
            int l = bytes[i].length;
            Bytes.copyFromArrayToArray(bytes[i], 0, result, pos, l);
            pos += l;
        }

        return result;
    }
    public void saveAsFile(String filename)
    {
        FileUtils.WriteFile(filename, bytes);
    }
    public boolean isequal(Bytes b2)
    {
        if(bytes.length!=b2.bytes.length) return false;
        for(int i=0;i<bytes.length;i++)
        {
            if(bytes[i]!=b2.bytes[i]) return false;
        }
        return true;
    }
    private Bytes(){}
    
    public Bytes(byte[] data)
    {
        bytes = data;
    }
    public Bytes(int size)
    {
        bytes = new byte[size];
    }
    public Bytes(Vector<Byte> bytevector)
    {
        bytes = new byte[bytevector.size()];
        for(int i=0;i<bytes.length;i++)
        {
            bytes[i] = bytevector.get(i);
        }
    }
    public Bytes(String string)
    {
        bytes = b.StringToBytes(string);
    }
    //public static Bytes create(byte[] data)
    //{
    //    Bytes result = new Bytes(data);
    //    return result;
    //}
    public byte[] getByteArray()
    {
        return bytes;
    }
    public byte getbyte(int i)
    {
        return bytes[i];
    }
    public String toString()
    {
        return b.BytesToString(bytes);
    }
    public void setWithString(String str)
    {
        this.bytes = b.StringToBytes(str);
    }
    public Bytes append(Bytes appendage)
    {
        byte[] result = new byte[bytes.length+appendage.length()];
        this.writeIntoBuffer(result, 0);
        appendage.writeIntoBuffer(result, this.length());
        return new Bytes(result);
    }
    public void writeIntoBuffer(byte[] buffer, int pos)
    {
        for(int i=0;i<bytes.length;i++)
        {
            buffer[pos+i] = bytes[i];
        }
    }
    public void appendToVector(Vector<Byte> vector)
    {
        for(int i=0;i<bytes.length;i++)
        {
            vector.add(bytes[i]);
        }
    }
    public boolean startsWith(Bytes startPhrase)
    {
        if(startPhrase.length()>bytes.length) return false;
        for(int i=0;i<startPhrase.length();i++)
        {
            if(startPhrase.bytes[i]!=bytes[i]) return false;
        }
        return true;
    }
    public Bytes remove(byte bytetoremove)
    {
        Vector<Byte> result = new Vector<Byte>();
        int i=0;
        //boolean possibleinstance = false;
        //int possibleinstance = -1;
        while(i<bytes.length)
        {
            byte curbyte = bytes[i];
            if(bytetoremove==curbyte)
            {
                //match, so write newbytes
                i++;
            }
            else
            {
                result.add(curbyte);
                i++;
            }
        }
        return new Bytes(result);
    }
    public Bytes replace(byte oldbyte, Bytes newbytes)
    {
        Vector<Byte> result = new Vector<Byte>();
        int i=0;
        //boolean possibleinstance = false;
        //int possibleinstance = -1;
        while(i<bytes.length)
        {
            byte curbyte = bytes[i];
            if(oldbyte==curbyte)
            {
                //match, so write newbytes
                newbytes.appendToVector(result);
                i = i+newbytes.length();
            }
            else
            {
                result.add(curbyte);
                i++;
            }
        }
        return new Bytes(result);
    }
    public Bytes replace(Bytes oldbytes, Bytes newbytes)
    {
        Vector<Byte> result = new Vector<Byte>();
        int i=0;
        //boolean possibleinstance = false;
        //int possibleinstance = -1;
        while(i<bytes.length)
        {
            byte curbyte = bytes[i];
            if(oldbytes.bytes[0]==curbyte && oldbytes.length()<bytes.length-i)
            {
                //possible instance...
                //possibleinstance = i; //mark where we left off
                //if()
                //{
                //    //can't be an instance.
                //}
                int j = 0;
                while(j<oldbytes.length() && bytes[i+j]==oldbytes.bytes[j])
                {
                    j++;
                }
                if(j==oldbytes.length())
                {
                    //match, so write newbytes
                    newbytes.appendToVector(result);
                    i = i+j;
                }
                else
                {
                    result.add(curbyte);
                    i++;
                }
            }
            else
            {
                result.add(curbyte);
                i++;
            }
        }
        return new Bytes(result);
    }
    public int length()
    {
        return bytes.length;
    }
    public Bytes[] split(byte splitter)
    {
        int length = bytes.length;
        
        //count the subsets
        int splittercount = 0;
        for(int i=0;i<length;i++)
        {
            if(bytes[i]==splitter) splittercount++;
        }
        Bytes[] result = new Bytes[splittercount+1];
        
        //find the size of each subset
        int cursubset = 0;
        int posinsubset = 0;
        for(int i=0;i<length;i++)
        {
            if(bytes[i]==splitter)
            {
                result[cursubset] = new Bytes(posinsubset); //every subset except the last
                //lastsplitterpos = i;
                cursubset++;
                posinsubset = 0;
            }
            else
            {
                posinsubset++;
            }
        }
        result[cursubset] = new Bytes(posinsubset); //last bunch
        
        //copy the bytes to each subset
        cursubset = 0;
        posinsubset = 0;
        for(int i=0;i<length;i++)
        {
            byte curbyte = bytes[i];
            if(curbyte==splitter)
            {
                //lastsplitterpos = i;
                cursubset++;
                posinsubset = 0;
            }
            else
            {
                result[cursubset].bytes[posinsubset] = curbyte;
                posinsubset++;
            }
        }
        
        return result;
    }
    
    public static int ByteToInt32(byte b)
    {
        return ((int)b)&0xFF;
    }
    public static short ByteToInt16(byte b)
    {
        return (short)(((int)b)&0xFF);
    }
    public static int Int16ToInt32(short s)
    {
        return ((int)s)&0xFFFF;
    }
    public static long Int32ToInt64(int i)
    {
        return ((long)i)&0xFFFFFFFFL;
    }
    
}

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

import shared.e;
import java.util.Vector;

/**
 *
 * @author user
 */
public class b
{
    public static byte BooleanToByte(boolean val)
    {
        return val?(byte)1:(byte)0;
    }
    public static byte Int32ToByte(int n)
    {
        return (byte)n;
    }
    public static String ShortsToString(short[] chars)
    {
        return ShortsToString(chars,0,chars.length); //this would work too.
        /*StringBuilder r = new StringBuilder();
        for(short sh: chars)
        {
            r.append((char)sh);
        }
        return r.toString();*/
    }
    public static String ShortsToString(short[] chars, int start, int length)
    {
        StringBuilder r = new StringBuilder();
        for(int i=start;i<start+length;i++)
        {
            r.append((char)chars[i]);
        }
        return r.toString();
    }
    public static Vector<Integer> findBytes_All(byte[] data, byte[] pattern)
    {
        Vector<Integer> r = new Vector();
        int pos = 0;
        while(true)
        {
            int loc = findBytes_Once(data,pattern,pos);
            if(loc==-1) break;
            r.add(loc);
            pos = loc+1;
        }
        return r;
    }
    public static int findBytes_Once(byte[] data, byte[] pattern)
    {
        return findBytes_Once(data, pattern, 0);
    }
    public static int findBytes_Once(byte[] data, byte[] pattern, int offset)
    {
        //This is the Knuth–Morris–Pratt algorithm
        int[] table = gettable(pattern);
        int j = 0;
        for(int i=offset; i<data.length; i++)
        {
            while(j>0 && pattern[j]!=data[i])
            {
                j = table[j - 1];
            }
            if(pattern[j]==data[i])
            {
                j++;
            }
            if(j==pattern.length)
            {
                return i+1-pattern.length;
            }
        }
        return -1;
    }
    private static int[] gettable(byte[] pattern) //helper for byte[] searching.
    {
        int[] table = new int[pattern.length];
        int j = 0;
        for(int i = 1; i<pattern.length; i++)
        {
            while(j>0 && pattern[j]!=pattern[i])
            {
                j = table[j - 1];
            }
            if(pattern[j]==pattern[i])
            {
                j++;
            }
            table[i] = j;
        }
        return table;
    }

    public static byte[] StringToUtf16Bytes(String str)
    {
        byte[] r = new byte[str.length()*2];
        for(int i=0;i<str.length();i++)
        {
            char c = str.charAt(i);
            r[i*2+0] = (byte)c;
            r[i*2+1] = (byte)(c>>>8);
        }
        return r;
    }
    public static void reverseEndianness(byte[] data)
    {
        for(int i=0;i<data.length/2;i++)
        {
            byte temp = data[i];
            data[i] = data[data.length-1-i];
            data[data.length-1-i] = temp;
        }
    }
    public static String readNullTerminatedUtf16FromBytes(byte[] data, int pos)
    {
        StringBuilder r = new StringBuilder();
        while(true)
        {
            short sh = b.BytesToInt16(data, pos);
            if(sh==0) break;
            r.append((char)sh);
            pos += 2;
        }
        return r.toString();
    }
    public static void writeNullTerminatedBytes(byte[] data, int pos, byte[] val)
    {
        for(int i=0;i<val.length;i++)
        {
            data[pos+i] = val[i];
        }
        data[pos+val.length] = 0;
    }
    public static void writeNullTerminatedUtf16FromString(byte[] data, int pos, String val)
    {
        for(int i=0;i<val.length();i++)
        {
            char ch = val.charAt(i);
            data[pos] = (byte)(ch);
            data[pos+1] = (byte)(ch>>>8);
            pos += 2;
        }
        data[pos] = 0;
        data[pos+1] = 0;
    }
    public static boolean f(int flags, int mask)
    {
        boolean r = ((flags&mask)!=0);
        return r;
    }
    public static int getByte0(int int32)
    {
        return (0xFF&int32);
    }
    public static int getByte1(int int32)
    {
        return (0xFF00&int32)>>>8;
    }
    public static int getByte2(int int32)
    {
        return (0xFF0000&int32)>>>16;
    }
    public static int getByte3(int int32)
    {
        return (0xFF000000&int32)>>>24;
    }
    public static boolean startswith(byte[] data, byte[] startstr)
    {
        if(data.length<startstr.length) return false;
        for(int i=0;i<startstr.length;i++)
        {
            if(data[i]!=startstr[i]) return false;
        }
        return true;
    }
    public static byte[] substr(byte[] data, int offset, int length)
    {
        byte[] result = new byte[length];
        for(int i=0;i<length;i++)
        {
            result[i] = data[offset+i];
        }
        return result;
    }
    
    public static boolean isEqual(byte[] bs1, byte[] bs2)
    {
        if(bs1.length!=bs2.length) return false;
        for( int i=0; i<bs1.length; i++ )
        {
            if(bs1[i]!=bs2[i]) return false;
        }
        return true;
    }
    public static boolean isEqualShorts(short[] bs1, short[] bs2)
    {
        if(bs1.length!=bs2.length) return false;
        for( int i=0; i<bs1.length; i++ )
        {
            if(bs1[i]!=bs2[i]) return false;
        }
        return true;
    }
    public static int bytewiseAverage(int i, int j)
    {
        int b1 = ((i&0xFF) + (j&0xFF))/2;
        int b2 = (((i&0xFF00)>>>8) + ((j&0xFF00)>>>8))/2;
        int b3 = (((i&0xFF0000)>>>16) + ((j&0xFF0000)>>>16))/2;
        int b4 = (((i&0xFF000000)>>>24) + ((j&0xFF000000)>>>24))/2;
        int result = b1 | b2<<8 | b3<<16 | b4<<24;
        return result;
    }
    public static byte shr(byte a, byte b)
    {
        return (byte)(ByteToInt32(a) >>> ByteToInt32(b));
    }
    public static byte shr(byte a, int dist)
    {
        return (byte)(ByteToInt32(a) >>> dist);
    }
    public static byte shl(byte a, byte b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) << ByteToInt32(b));
    }
    public static byte shl(byte a, int dist)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) << dist);
    }
    public static byte or(byte a, byte b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) | ByteToInt32(b));
    }
    public static byte and(byte a, byte b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) & ByteToInt32(b));
    }
    public static byte not(byte a)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(~ByteToInt32(a));
    }
    public static byte xor(byte a, byte b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) ^ ByteToInt32(b));
    }
    public static byte xor(byte a, int b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) ^ b);
    }
    public static byte or(byte a, int b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) | b);
    }
    public static byte and(byte a, int b)
    {
        //improve: this probably doesn't need the ByteToInt32 calls.
        return (byte)(ByteToInt32(a) & b);
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
    public static int BytesToInt32(byte b0, byte b1, byte b2, byte b3)
    {
        int a = ByteToInt32(b0)<<0;
        int b = ByteToInt32(b1)<<8;
        int c = ByteToInt32(b2)<<16;
        int d = ByteToInt32(b3)<<24;
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
    public static short BytesToInt16BigEndian(byte[] bytes, int startpos)
    {
        int a = ByteToInt32(bytes[startpos+0])<<8;
        int b = ByteToInt32(bytes[startpos+1])<<0;
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
    public static byte[] ByteToBytes(byte by)
    {
        byte[] result = new byte[1];
        result[0] = by;
        return result;
    }
    /*private static int loadBytesIntoInt32Endian(byte[] bytes, int startpos)
    {
        //return (bytes[startpos+0]<<24) | (bytes[startpos+1]<<16) | (bytes[startpos+2]<<8) | (bytes[startpos+3]<<0);
        int a = ByteToInt32(bytes[startpos+0])<<24;
        int b = ByteToInt32(bytes[startpos+1])<<16;
        int c = ByteToInt32(bytes[startpos+2])<<8;
        int d = ByteToInt32(bytes[startpos+3])<<0;
        int result = a | b | c | d;
        return result;
    }*/
    public static void loadInt32IntoBytes(int value, byte[] bytes, int startpos)
    {
        bytes[startpos+0] = (byte)((value >>> 0) & 0xFF);
        bytes[startpos+1] = (byte)((value >>> 8) & 0xFF);
        bytes[startpos+2] = (byte)((value >>> 16) & 0xFF);
        bytes[startpos+3] = (byte)((value >>> 24) & 0xFF);
    }
    public static void loadInt16IntoBytes(short value, byte[] bytes, int startpos)
    {
        bytes[startpos+0] = (byte)((value >>> 0) & 0xFF);
        bytes[startpos+1] = (byte)((value >>> 8) & 0xFF);
    }
    public static byte[] Int32ToBytes(int value)
    {
        byte[] result = new byte[4];
        result[0] = (byte)((value >> 0) & 0xFF);
        result[1] = (byte)((value >> 8) & 0xFF);
        result[2] = (byte)((value >> 16) & 0xFF);
        result[3] = (byte)((value >> 24) & 0xFF);
        return result;
    }
    public static void Int32IntoBytes(int value, byte[] bytes, int offset)
    {
        bytes[0+offset] = (byte)((value >> 0) & 0xFF);
        bytes[1+offset] = (byte)((value >> 8) & 0xFF);
        bytes[2+offset] = (byte)((value >> 16) & 0xFF);
        bytes[3+offset] = (byte)((value >> 24) & 0xFF);
    }
    public static void Int64IntoBytes(long value, byte[] bytes, int offset)
    {
        bytes[0+offset] = (byte)((value >> 0) & 0xFF);
        bytes[1+offset] = (byte)((value >> 8) & 0xFF);
        bytes[2+offset] = (byte)((value >> 16) & 0xFF);
        bytes[3+offset] = (byte)((value >> 24) & 0xFF);
        bytes[4+offset] = (byte)((value >> 32) & 0xFF);
        bytes[5+offset] = (byte)((value >> 40) & 0xFF);
        bytes[6+offset] = (byte)((value >> 48) & 0xFF);
        bytes[7+offset] = (byte)((value >> 56) & 0xFF);
    }
    public static byte[] Int16ToBytes(short value)
    {
        byte[] result = new byte[2];
        result[0] = (byte)((value >> 0) & 0xFF);
        result[1] = (byte)((value >> 8) & 0xFF);
        return result;
    }
    /*public static int[][] rotateIntGridCounterclockwise(int[][] data)
    {
        int count1 = data.length;
        int count2 = data[0].length;
        int[][] result = new int[count2][count1];
        for(int i=0;i<count1;i++)
        {
            for(int j=0;j<count2;j++)
            {
                result[j][i] = data[i][j];
            }
        }
        return result;
    }*/
    public static int[][] rotateIntGridClockwise(int[][] data)
    {
        int count1 = data.length;
        int count2 = data[0].length;
        int[][] result = new int[count2][count1];
        for(int i=0;i<count1;i++)
        {
            for(int j=0;j<count2;j++)
            {
                result[j][count1-i-1] = data[i][j];
            }
        }
        return result;
    }
    public static <T> void rotateGridClockwise(T[][] datain, T[][] dataout)
    {
        int count1 = datain.length;
        int count2 = datain[0].length;
        if(dataout.length!=count2 || dataout[0].length!=count1)
        {
            shared.m.err("Grids are not correctly sized.");
            return;
        }
        for(int i=0;i<count1;i++)
        {
            for(int j=0;j<count2;j++)
            {
                dataout[j][count1-i-1] = datain[i][j];
            }
        }
    }
    /*private static void loadInt32IntoBytesEndian(int value, byte[] bytes, int startpos)
    {
        bytes[startpos+0] = (byte)((value >> 24) & 0xFF);
        bytes[startpos+1] = (byte)((value >> 16) & 0xFF);
        bytes[startpos+2] = (byte)((value >> 8) & 0xFF);
        bytes[startpos+3] = (byte)((value >> 0) & 0xFF);
    }*/

    /*private static short byteToInt16(byte b)
    {
        short result = (short)(b&0xFF);
        return result;
    }*/
    public static void CopyBytes(byte[] from, int fromstart, byte[] to, int tostart, int length)
    {
        for(int i=0;i<length;i++)
        {
            to[tostart+i] = from[fromstart+i];
        }
    }
    public static void CopyBytes(byte[] from, byte[] to, int tostart)
    {
        CopyBytes(from,0,to,tostart,from.length);
    }
    public static byte[] CopyBytes(byte[] from)
    {
        byte[] result = new byte[from.length];
        for(int i=0;i<from.length;i++) result[i] = from[i];
        return result;
    }
    public static byte[][] splitBytes(byte[] bytesToSplit, byte splitter)
    {
        int length = bytesToSplit.length;
        
        //count the subsets
        int splittercount = 0;
        for(int i=0;i<length;i++)
        {
            if(bytesToSplit[i]==splitter) splittercount++;
        }
        byte[][] result = new byte[splittercount+1][];
        
        //find the size of each subset
        int cursubset = 0;
        int posinsubset = 0;
        for(int i=0;i<length;i++)
        {
            if(bytesToSplit[i]==splitter)
            {
                result[cursubset] = new byte[posinsubset]; //every subset except the last
                //lastsplitterpos = i;
                cursubset++;
                posinsubset = 0;
            }
            else
            {
                posinsubset++;
            }
        }
        result[cursubset] = new byte[posinsubset]; //last bunch
        
        //copy the bytes to each subset
        cursubset = 0;
        posinsubset = 0;
        for(int i=0;i<length;i++)
        {
            byte curbyte = bytesToSplit[i];
            if(curbyte==splitter)
            {
                //lastsplitterpos = i;
                cursubset++;
                posinsubset = 0;
            }
            else
            {
                result[cursubset][posinsubset] = curbyte;
                posinsubset++;
            }
        }
        
        return result;
    }
    
    public static String ByteToHexString(byte b)
    {
        char[] chars = new char[2];
        int upper = ((int)b & 0xF0)>>4;
        int lower = (int)b & 0x0F;
        chars[0] = (char)(upper<10?upper+'0':upper-10+'a');
        chars[1] = (char)(lower<10?lower+'0':lower-10+'a');
        return new String(chars);
    }
    public static void ByteToHexString(StringBuilder r, byte b)
    {
        int upper = ((int)b & 0xF0)>>4;
        int lower = (int)b & 0x0F;
        char char0 = (char)(upper<10?upper+'0':upper-10+'a');
        char char1 = (char)(lower<10?lower+'0':lower-10+'a');
        r.append(char0);
        r.append(char1);
    }
    public static String CharToHexString(char b)
    {
        char[] chars = new char[4];
        int n3 = ((int)b & 0xF000)>>12;
        int n2 = ((int)b & 0x0F00)>>8;
        int n1 = ((int)b & 0x00F0)>>4;
        int n0 = ((int)b & 0x000F);
        chars[0] = (char)(n3<10?n3+'0':n3-10+'a');
        chars[1] = (char)(n2<10?n2+'0':n2-10+'a');
        chars[2] = (char)(n1<10?n1+'0':n1-10+'a');
        chars[3] = (char)(n0<10?n0+'0':n0-10+'a');
        return new String(chars);
    }
    public static String BytesToHexString(byte[] bytes)
    {
        //char[] result = new char[bytes.length*2];
        StringBuilder result = new StringBuilder();
        for(int i=0;i<bytes.length;i++)
        {
            byte curbyte = bytes[i];
            //result.append(ByteToHexString(curbyte));
            ByteToHexString(result,curbyte);
        }
        return result.toString();
    }
    public static String BytesToString(byte[] bytes)
    {
        //String result = new String(bytes, "ISO-8859-1"); //has to have a try/catch block.
        /*String result = new String(bytes, 0); //deprecated, but acts like ascii.
        return result;*/
        int length = bytes.length;
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = (char)ByteToInt16(bytes[i]);
        }
        return new String(result);
    }
    public static String NullTerminatedBytesToString(byte[] bytes)
    {
        //String result = new String(bytes, "ISO-8859-1"); //has to have a try/catch block.
        /*String result = new String(bytes, 0); //deprecated, but acts like ascii.
        return result;*/
        e.ensure(bytes[bytes.length-1]==0);

        int length = bytes.length-1;
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = (char)ByteToInt16(bytes[i]);
        }
        return new String(result);
    }
    public static byte[] Utf16ToBytes(String str)
    {
        byte[] result = new byte[str.length()*2];
        for(int i=0;i<str.length();i++)
        {
            char c = str.charAt(i);
            result[i*2+0] = (byte)(c);
            result[i*2+1] = (byte)(c>>>8);
        }
        return result;
    }
    public static byte[] StringToBytes(String str)
    {
        /*byte[] result = null;
        try
        {
            result = str.getBytes("ISO-8859-1");
        }
        catch(Exception e)
        {
            m.err("Unable to convert string to bytes");
        }
        return result;*/
        int length = str.length();
        byte[] result = new byte[length];
        for(int i=0;i<length;i++)
        {
            result[i] = (byte)(str.charAt(i) & 0xFF);
        }
        return result;
    }
    
    //must have an even number of chars. Not begin with 0x.
    public static int CharToNibble(char c)
    {
        if(c>='a' && c<='f')
        {
            return c-'a'+10;
        }
        else if(c>='A' && c<='F')
        {
            return c-'A'+10;
        }
        else if(c>='0' && c<='9')
        {
            return c-'0';
        }
        else
        {
            throw new shared.uncaughtexception("Invalid char in charToNibble.");
        }
    }
    public static byte[] HexStringToBytes(String hexstr)
    {
        String msg2 = hexstr.replaceAll(" ", "");//.toLowerCase();
        char[] msg = msg2.toCharArray();
        //byte[] msg = b.StringToBytes(hexstr);
        int hexcount = msg.length/2;
        byte[] data = new byte[hexcount];
        for(int i=0;i<hexcount;i++)
        {
            char b1 = msg[i*2+0];
            char b2 = msg[i*2+1];
            int val1 = CharToNibble(b1);
            int val2 = CharToNibble(b2);
            byte result = (byte)(val1<<4 | val2);
            //String hexbyte = hexstr.substring(i*2, i*2+2);
            //byte result = Byte.parseByte(hexbyte, 16);
            //data[i] = result;
            data[i] = result;
        }
        return data;
    }
    
    public static byte[] StringToNullTerminatedBytes(String str)
    {
        int length = str.length();
        byte[] result = new byte[length+1];
        for(int i=0;i<length;i++)
        {
            result[i] = (byte)(str.charAt(i) & 0xFF);
        }
        result[length] = 0;
        return result;
    }
    public static byte[] appendBytes(byte[]... byteblocks)
    {
        int numblocks = byteblocks.length;
        
        //find the full length.
        int fulllength = 0;
        for(int i=0;i<numblocks;i++)
        {
            fulllength += byteblocks[i].length;
        }
        byte[] result = new byte[fulllength];
        
        //copy over the bytes.
        int curpos = 0;
        for(int i=0;i<numblocks;i++)
        {
            byte[] curblock = byteblocks[i];
            int curblocklength = curblock.length;
            for(int j=0;j<curblocklength;j++)
            {
                result[curpos] = curblock[j];
                curpos++;
            }
        }
        
        return result;
    }
    
    /*public static byte[] appendBytes(byte[] bytes1, byte[] bytes2)
    {
        int length = bytes1.length + bytes2.length;
        byte[] result = new byte[length];
        int curpos = 0;
        
        //copy the bytes from bytes1.
        for(int i=0;i<bytes1.length;i++)
        {
            result[curpos] = bytes1[i];
            curpos++;
        }
        
        //copy the bytes from bytes2.
        for(int i=0;i<bytes2.length;i++)
        {
            result[curpos] = bytes2[i];
            curpos++;
        }
        
        return result;
    }*/
    
    //untested
    public static byte[] byteVectorToArray(Vector<Byte> vector)
    {
        int size = vector.size();
        byte[] result = new byte[size];
        for(int i=0;i<size;i++)
        {
            result[i] = vector.get(i);
        }
        return result;
    }
    
    //untested
    public static int reverseEndianness(int value)
    {
        //byte b1 = (byte)((value >> 0) & 0xFF);
        //byte b2 = (byte)((value >> 8) & 0xFF);
        //byte b3 = (byte)((value >> 16) & 0xFF);
        //byte b4 = (byte)((value >> 24) & 0xFF);
        int b1 = ((value >>> 0) & 0xFF);
        int b2 = ((value >>> 8) & 0xFF);
        int b3 = ((value >>> 16) & 0xFF);
        int b4 = ((value >>> 24) & 0xFF);
        int result = (b1 << 24) | (b2 << 16) | (b3 << 8) | (b4 << 0);
        return result;
    }
}

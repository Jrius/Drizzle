/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import shared.*;
import java.util.ArrayList;

public class Utf16
{
    public static class SizedAndNT //extends Utf16Base
    {
        short[] data;
        public SizedAndNT(IBytestream c)
        {
            int numbytes = c.readInt();
            int size = numbytes/2;
            data = new short[size];
            for(int i=0;i<size;i++)
            {
                data[i] = c.readShort();
            }
        }
        public SizedAndNT(String s)
        {
            data = new short[s.length()+1];
            for(int i=0;i<data.length;i++)
            {
                data[i] = (short)s.charAt(i);
            }
            data[data.length-1] = 0;
        }
        public void write(IBytedeque c)
        {
            int numbytes = data.length*2;
            c.writeInt(numbytes);
            for(short sh: data)
            {
                c.writeShort(sh);
            }
        }
        public String toString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                if(sh==0) break;
                r.append((char)sh);
            }
            return r.toString();
        }

    }
    public abstract static class Utf16Base extends mystobj
    {
        short[] data;

        protected Utf16Base(IBytestream c, int size)
        {
            data = new short[size];
            for(int i=0;i<size;i++)
            {
                data[i] = c.readShort();
            }
        }
        //protected Utf16Base(){}
        protected Utf16Base(String s)
        {
            data = new short[s.length()];
            for(int i=0;i<data.length;i++)
            {
                data[i] = (short)s.charAt(i);
            }
        }
        public void write(IBytedeque c)
        {
            for(int i=0;i<data.length;i++)
            {
                c.writeShort(data[i]);
            }
        }
        public String toString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                r.append((char)sh);
            }
            return r.toString();
        }

    }
    public static class Sized16
    {
        short[] data;

        public Sized16(IBytestream c)
        {
            short size = c.readShort();
            int size2 = b.Int16ToInt32(size);
            data = new short[size2];
            for(int i=0;i<size2;i++)
            {
                data[i] = c.readShort();
            }
        }

        public Sized16(String s)
        {
            data = new short[s.length()];
            for(int i=0;i<data.length;i++)
            {
                data[i] = (short)s.charAt(i);
            }
        }

        public void write(IBytedeque c)
        {
            short size = (short)data.length;
            c.writeShort(size);
            for(int i=0;i<data.length;i++)
            {
                c.writeShort(data[i]);
            }
        }

        public String toString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                r.append((char)sh);
            }
            return r.toString();
        }
    }
    //public static class Utf16NullTerminated
    //{
    //    short[] data;
    //}
    //public static class Utf16FixedLength
    //{

    public static class FixedNT
    {
        private ArrayList<Short> data = new ArrayList<Short>();

        public FixedNT(IBytestream c, int size)
        {
            for(int i=0;i<size;i++)
            {
                short sh = c.readShort();
                data.add(sh);
            }
        }

        public FixedNT(String s, int size)
        {
            //add string and add null terminator
            for(int i=0;i<s.length();i++)
            {
                char ch = s.charAt(i);
                short sh = (short)ch;
                data.add(sh);
            }
            data.add((short)0);

            //pad with extra zeros.
            int extras = size-data.size();
            if(extras<0) m.throwUncaughtException("String is too large");
            for(int i=0;i<extras;i++)
            {
                data.add((short)0);
            }

        }

        public void write(IBytedeque c)
        {
            for(short sh: data)
            {
                c.writeShort(sh);
            }
        }
        public String toString()
        {
            return toNullTerminatedString();
        }
        public String toDirectString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                r.append((char)sh);
            }
            return r.toString();
        }
        public String toNullTerminatedString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                if(sh==0) break;
                r.append((char)sh);
            }
            return r.toString();
        }
    }

    public static class NT extends mystobj
    {
        private ArrayList<Short> data = new ArrayList<Short>();
        public int size()
        {
            return data.size()-1;
        }
        public NT(String str)
        {
            for(int i=0;i<str.length();i++)
            {
                char c = str.charAt(i);
                data.add((short)c);
            }
            data.add((short)0);
        }
        public NT(IBytestream c)
        {
            while(true)
            {
                short sh = c.readShort();
                data.add(sh);
                if(sh==0) break;
            }
        }
        public void write(IBytedeque c)
        {
            for(short sh: data)
            {
                c.writeShort(sh);
            }
        }
        public String toString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                if(sh==0) break;
                r.append((char)sh);
            }
            return r.toString();
        }
    }

        //short[] data;
        private ArrayList<Short> data = new ArrayList<Short>();

        public Utf16(IBytestream c, int size)
        {
            //data = c.readShorts(size);
            for(int i=0;i<size;i++)
            {
                short sh = c.readShort();
                data.add(sh);
            }
        }
        public Utf16(IBytestream c)
        {
            while(true)
            {
                short curshort = c.readShort();
                data.add(curshort);
                if(curshort==0) break;
            }
        }
        public Utf16(String s)
        {
            for(int i=0;i<s.length();i++)
            {
                char ch = s.charAt(i);
                short sh = (short)ch;
                data.add(sh);
            }
            data.add((short)0);
        }
        public Utf16(String s, int size)
        {
            this(s);
            int extras = size-data.size();
            if(extras<0) m.throwUncaughtException("String is too large");
            for(int i=0;i<extras;i++)
            {
                data.add((short)0);
            }

        }

        public void write(IBytedeque c)
        {
            for(short sh: data)
            {
                c.writeShort(sh);
            }
        }

        public int numchars()
        {
            return data.size();
        }

        public String toString()
        {
            return toNullTerminatedString();
        }
        public String toDirectString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                r.append((char)sh);
            }
            return r.toString();
        }
        public String toNullTerminatedString()
        {
            StringBuilder r = new StringBuilder();
            for(short sh: data)
            {
                if(sh==0) break;
                r.append((char)sh);
            }
            return r.toString();
        }
    //}

}

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

import java.io.File;
//import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class MystInStream
{
    public static class MystFileInStream
    {
        RandomAccessFile stream;
        MystInStream lastuser;
        //HashMap<MystInStream, Long> positions;
        
        public MystFileInStream(MystInStream user, File file) throws FileNotFoundException
        {
            stream = new RandomAccessFile(file, "r");
            //positions = new HashMap<MystInStream, Long>();
            //positions.put(user, 0L);
            user.offset = 0L;
            lastuser = user;
        }
        
        /*public MystFileInStream handleFork(MystInStream user, MystInStream newuser) throws IOException
        {
            if(user!=lastuser)
            {
                long oldpos = stream.getFilePointer();
                //positions.put(lastuser, oldpos);
                lastuser.offset = oldpos;
                //long newpos = positions.get(user);
                long newpos = user.offset;
                stream.seek(newpos);
                lastuser = user;
            }
            //long forkpos = stream.getFilePointer() + offset;
            //positions.put(newuser, newuser.baseoffset);
            return this;
        }*/
        
        public int read(MystInStream user) throws IOException
        {
            if(user!=lastuser)
            {
                long oldpos = stream.getFilePointer();
                //positions.put(lastuser, oldpos);
                lastuser.offset = oldpos;
                //long newpos = positions.get(user);
                long newpos = user.offset;
                stream.seek(newpos);
                lastuser = user;
            }
            return stream.read();
        }
        
        public void read(MystInStream user, byte[] bytes) throws IOException
        {
            if(user!=lastuser)
            {
                long oldpos = stream.getFilePointer();
                //positions.put(lastuser, oldpos);
                lastuser.offset = oldpos;
                //long newpos = positions.get(user);
                long newpos = user.offset;
                stream.seek(newpos);
                lastuser = user;
            }
            stream.readFully(bytes);
        }
        
        public String toString(MystInStream user)
        {
            try
            {
                String result = "address:0x"+Integer.toHexString(user.hashCode());
                result += "  baseoffset:0x"+Long.toString(user.baseoffset);
                result += "  lastuser:0x"+Integer.toHexString(lastuser.hashCode());
                result += "  pos:0x"+Long.toString(stream.getFilePointer());
                //Iterator<MystInStream> keys = positions.keySet().iterator();
                //while(keys.hasNext())
                //{
                //    MystInStream mis = keys.next();
                //    long val = positions.get(mis);
                //    result += "  "+Integer.toHexString(mis.hashCode())+">>"+Long.toString(val);
                //}
                return result;
            }
            catch(Exception e)
            {
                return "exception";
            }
        }
    }
    
    MystFileInStream stream;
    long baseoffset;
    long offset;
    //RandomAccessFile stream;
    
    private MystInStream(){};
    
    public MystInStream fork(int offset, boolean setAsNewBase) throws readexception
    {
        //try
        //{
            MystInStream result = new MystInStream();
            /*if(makeChildrenUseThisOffsetAsBase)
            {
                result.baseoffset = baseoffset + offset;
            }
            else
            {
                result.baseoffset = baseoffset;
            }*/
            result.offset = baseoffset+offset;
            if(setAsNewBase)
            {
                result.baseoffset = baseoffset+offset;
            }
            else
            {
                result.baseoffset = baseoffset;
            }
            //result.stream = stream.handleFork(this, result);
            result.stream = stream;
            return result;
        //}
        //catch(IOException e)
        //{
        //    throw new readexception("Unable to fork.");
        //}
    }
    
    
    public static MystInStream createFromFile(String filename) throws readexception
    {
        File file = new File(filename);
        return createFromFile(file);
    }
    public static MystInStream createFromFile(File file) throws readexception
    {
        try
        {
            MystInStream result = new MystInStream();
            result.stream = new MystFileInStream(result, file);
            result.baseoffset = 0;
            result.offset = 0;
            return result;
        }
        catch(FileNotFoundException e)
        {
            throw new readexception("Unable to find file.");
        }
    }
    
    public Bytes readbytes( int count ) throws readexception
    {
        try
        {
            byte[] result = new byte[count];
            stream.read(this, result);
            return new Bytes(result);
        }
        catch(IOException e)
        {
            throw new readexception("Unable to read bytes.");
        }
    }
    public byte readbyte() throws readexception
    {
        try
        {
            return (byte)stream.read(this);
        }
        catch(IOException e)
        {
            throw new readexception("Unable to read byte");
        }
    }
    public short readshortBigendian() throws readexception
    {
        try
        {
            int a = stream.read(this)<<8;
            int b = stream.read(this)<<0;
            short result = (short)( a | b );
            return result;
        }
        catch(IOException e)
        {
            throw new readexception("Unable to read short.");
        }
    }
    public int readintBigendian() throws readexception
    {
        try
        {
            int a = stream.read(this)<<24;
            int b = stream.read(this)<<16;
            int c = stream.read(this)<<8;
            int d = stream.read(this)<<0;
            int result = a | b | c | d;
            return result;
        }
        catch(IOException e)
        {
            throw new readexception("Unable to read int.");
        }
    }

    public <T extends mystobj> T[] readVector( Class<T> objclass, int size) throws readexception
    {
        T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, size);
        for(int i=0;i<size;i++)
        {
            result[i] = (T)this.readObj(objclass);
        }
        return result;
    }
    
    public <T extends mystobj> T readObj(Class objclass) throws readexception
    {
        try
        {
            return (T)objclass.getConstructor(MystInStream.class).newInstance(this);
        }
        catch(java.lang.NoSuchMethodException e)
        {
            throw new readexception("MystInStream: NoSuchMethod Exception.");
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.InstantiationException e)
        {
            throw new readexception("MystInStream: Instantiation Exception.");
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.IllegalAccessException e)
        {
            throw new readexception("MystInStream: IllegalAccess Exception.");
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.reflect.InvocationTargetException e)
        {
            Throwable e2 = e.getCause();
            if(e2 instanceof readexception)
            {
                throw (readexception)e2; //rethrow.
            }
            else
            {
                throw new readexception("MystInStream: InvocationTarget Exception.");
                //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
            }
        }
    }

    public String toString()
    {
        return stream.toString(this);
    }
}

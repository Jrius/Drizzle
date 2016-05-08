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

package realmyst;

//import uru.context;
import shared.*;
import shared.readexception;
import shared.mystobj;

public class rmcontext extends SerialBytestream
{
    //IBytestream in;
    public int curnum;
    /*public rmcontext(IBytestream in2)
    {
        this.in = in2;
    }*/
    /*public rmcontext(String filename)
    {
        super(filename);
    }*/
    static rmcontext _curcontext = new rmcontext();
    public static rmcontext get()
    {
        return _curcontext;
    }
    
    private rmcontext(){}
    
    /*public rmcontext Fork()
    {
        rmcontext result = new rmcontext();
        
        
        return result;
    }*/
    
    /*public rmcontext Fork(int offset)
    {
        rmcontext result = this.Fork();
        result.in = result.in.Fork(offset);
        return result;
    }
    public rmcontext Fork()
    {
        rmcontext result = new rmcontext();

        result.in = this.in.Fork();
        
        return result;
    }*/
    
    
    /*public <T extends mystobj> T[] readVector( Class<T> objclass,int size) throws readexception
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
            return (T)objclass.getConstructor(rmcontext.class).newInstance(this);
        }
        catch(java.lang.NoSuchMethodException e)
        {
            throw new readexception("Bytestream: java gunk: unable to create new instance.");
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.InstantiationException e)
        {
            throw new readexception("Bytestream: java gunk: unable to create new instance.");
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.IllegalAccessException e)
        {
            throw new readexception("Bytestream: java gunk: unable to create new instance.");
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
                throw new readexception("Bytestream: java gunk: unable to create new instance.");
                //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
            }
        }
    }

    public int readInt()
    {
        return in.readInt();
    }
    public byte readByte()
    {
        return in.readByte();
    }
    public short readShort()
    {
        return in.readShort();
    }
    public byte[] readBytes(int count)
    {
        return in.readBytes(count);
    }
    public int[] readInts(int count)
    {
        return in.readInts(count);
    }
    public short[] readShorts(int count)
    {
        return in.readShorts(count);
    }
    public String toString()
    {
        String result = "";
        if(in != null) result += in.toString();
        //if(this.curRootObject!=null) result+=" \n"+curRootObject.toString();
        //return "(no info)";
        
        return result;
    }*/
    
}

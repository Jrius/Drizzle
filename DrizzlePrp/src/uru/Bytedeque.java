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

import shared.mystobj;
import shared.Bytes;
import java.util.ArrayDeque;
import java.util.Iterator;
import prpobjects.Uruobjectdesc;
import java.util.Vector;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Bytedeque extends shared.Bytedeque2
{
    public Uruobjectdesc curRootObject = null;
    public prpobjects.prpfile prp = null;

    public Bytedeque(shared.Format format)
    {
        super(format);
    }
    
    
    //these generic functions are tied to uru.
    public <T extends mystobj> void writeObj(T object)
    {
        object.compile(this);
    }
    public <T extends mystobj> void writeArray2(T[] vector)
    {
        int length = vector.length;
        for(int i=0;i<length;i++)
        {
            vector[i].compile(this);
        }
    }
    public <T extends mystobj> void writeVector2(Vector<T> vector)
    {
        //int length = vector.length;
        int length = vector.size();
        for(int i=0;i<length;i++)
        {
            //vector[i].compile(this);
            vector.get(i).compile(this);
        }
    }
    public <T extends mystobj> void writeArrayList2(ArrayList<T> vector)
    {
        //int length = vector.length;
        int length = vector.size();
        for(int i=0;i<length;i++)
        {
            //vector[i].compile(this);
            vector.get(i).compile(this);
        }
    }
    
    /*ArrayDeque<byte[]> deque = new ArrayDeque<byte[]>();
    private int curpos = 0;

    
    //public String curFile = "";
    public Uruobjectdesc curRootObject = null;
    public shared.BaseContext context;
    //public int curRootObjectOffset;
    //public int curRootObjectSize;
    //public int curRootObjectEnd;
    
    public Bytedeque()
    {
    }
    
    public void prependBytes(byte[] data)
    {
        deque.addFirst(data);
    }
    public void writeBytes(Bytes data)
    {
        writeBytes(data.getByteArray());
    }
    public void writeBytes(byte[] data)
    {
        deque.addLast(data);
        int oldpos = curpos;
        curpos += data.length;
        int breakpoint = uru.moulprp._staticsettings.breakpoint;
        if(breakpoint > oldpos && breakpoint <= curpos)
        {
            int i = 0; //this should be set as a breakpoint.
        }
    }
    public void writeMultiDimensionBytes(byte[][] data)
    {
        for(int i=0;i<data.length;i++)
        {
            this.writeBytes(data[i]);
        }
    }
    public void writeMultiDimensionInts(int[][] data)
    {
        for(int i=0;i<data.length;i++)
        {
            this.writeInts(data[i]);
        }
    }
    public <T extends mystobj> void writeObj(T object)
    {
        object.compile(this);
    }
    
    public <T extends mystobj> void writeVector(T[] vector)
    {
        int length = vector.length;
        for(int i=0;i<length;i++)
        {
            vector[i].compile(this);
        }
    }
    
    public void writeInts(int[] ints)
    {
        for(int i=0;i<ints.length;i++)
        {
            writeInt(ints[i]);
        }
    }
    public void writeShorts(short[] shorts)
    {
        for(int i=0;i<shorts.length;i++)
        {
            writeShort(shorts[i]);
        }
    }
    public void prependInts(int[] ints)
    {
        for(int i=ints.length-1;i>=0;i--)
        {
            prependInt(ints[i]);
        }
    }
    public void prependShorts(short[] shorts)
    {
        for(int i=shorts.length-1;i>=0;i--)
        {
            writeShort(shorts[i]);
        }
    }
    
    public void prependByte(byte num)
    {
        byte[] data = b.ByteToBytes(num);
        prependBytes(data);
    }
    public void prependShort(short num)
    {
        byte[] data = b.Int16ToBytes(num);
        prependBytes(data);
    }
    public void prependInt(int num)
    {
        byte[] data = b.Int32ToBytes(num);
        prependBytes(data);
    }
    public void writeByte(byte b)
    {
        byte[] data = new byte[1];
        data[0] = b;
        writeBytes(data);
    }
    public void writeShort(short s)
    {
        byte[] data = b.Int16ToBytes(s);
        writeBytes(data);
    }
    public void writeInt(int i)
    {
        byte[] data = b.Int32ToBytes(i);
        writeBytes(data);
    }
    public void prependBytedeque(Bytedeque d)
    {
        byte[] bytes = d.getAllBytes();
        prependBytes(bytes);
    }
    public void writeBytedeque(Bytedeque d)
    {
        byte[] bytes = d.getAllBytes();
        writeBytes(bytes);
    }
    
    public byte[] getAllBytes()
    {
        int bytecount = 0;
        Iterator<byte[]> iterator = deque.iterator();
        while(iterator.hasNext())
        {
            bytecount += iterator.next().length;
        }
        
        byte[] result = null;
        //try{
        result = new byte[bytecount];
        //}catch(Exception e){
        //    int i=0;
        //}
        int curpos = 0;
        iterator = deque.iterator(); //restart.
        while(iterator.hasNext())
        {
            byte[] curarray = iterator.next();
            b.CopyBytes(curarray, result, curpos);
            curpos += curarray.length;
        }
        
        return result;
    }
    public Bytes getBytes()
    {
        return new Bytes(getAllBytes());
    }*/
}

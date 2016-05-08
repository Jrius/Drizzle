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
import java.lang.reflect.InvocationTargetException;
import uru.Bytestream;
import uru.Bytedeque;
import java.util.Vector;
import shared.readexception;

import prpobjects.Uruobjectdesc;
import prpobjects.Typeid;
import shared.Pair;
import java.util.HashMap;
import shared.cmap;
import shared.IBytestream;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class context extends shared.BaseContext
{
    public int realreadversion;
    public int readversion; //3==pots,6==moul,7==hexisle,5==myst5,4==crowthistle(crowthistle&myst5 are the same),8==mqo
    public int writeversion;
    public boolean compile;
    public IBytestream in;
    public Bytedeque out;
    public boolean outputVertices;
    public Vector<Float> vertices;
    
    public String curFile = "";
    public Uruobjectdesc curRootObject = null;
    public int curRootObjectOffset;
    public int curRootObjectSize;
    public int curRootObjectEnd;
    
    //overrides...
    public Integer sequencePrefix;
    //public Integer sequenceSuffix;
    public cmap<Integer,Integer> pagenumMap;
    public String ageName;

    //public Object extra;
    
    //whether to just read the PrpRootObject as raw data.
    public boolean isRaw;

    //public uru.moulprp.prpfile curprp; //used in plODEPhysical.

    //public Typeid typesToRead;

    public static context createFromBytestream(IBytestream in)
    {
        //return new context(-1,3,false,in,null,false,null);
        context result = new context();
        result.readversion = -1;
        result.writeversion = 3;
        result.compile = false;
        result.in = in;
        result.out = null;
        result.outputVertices = false;
        result.vertices = null;
        result.sequencePrefix = null;
        return result;
    }
    public void close()
    {
        if(this.in!=null) this.in.close();
        //if(this.out!=null) this.out.close();
    }
    
    public static context createDefault(Bytedeque out)
    {
        return new context(-1,3,false,null,out,false,null);
    }

    /*public static context create(int readversion2, int writeversion2, boolean compile2, Bytestream in2, Bytedeque out2)
    {
        return new context(readversion2,writeversion2,compile2,in2,out2);
    }*/
    private context(){}
    public context(int readversion2, int writeversion2, boolean compile2, Bytestream in2, Bytedeque out2, boolean outputVertices2, Vector<Float> vertices2)
    {
        this.readversion = readversion2;
        this.writeversion = writeversion2;
        this.compile = compile2;
        this.in = in2;
        this.out = out2;
        this.outputVertices = outputVertices2;
        this.vertices = vertices2;
    }
    public context(Bytestream in2)
    {
        this.in = in2;
    }
    /*public context Fork(IBytestream newIn)
    {
        context result = Fork();
        result.in.Fork(readversion)
        result.in = newIn;
        return result;
    }*/
    public context Fork()
    {
        context result = Fork_();
        if(result.in!=null) result.in = result.in.Fork();
        return result;
    }
    public context Fork(long offset)
    {
        context result = Fork_();
        if(result.in!=null) result.in = result.in.Fork(offset);
        return result;
    }
    
    private context Fork_() //must be private!!!  Doesn't actually fork the instream.
    {
        context result = new context();
        result.realreadversion = realreadversion;
        result.readversion = readversion;
        result.writeversion = writeversion;
        result.compile = compile;
        //result.in = in==null?in:in.Fork();
        result.in = in;
        result.out = out;
        result.outputVertices = outputVertices;
        result.vertices = vertices;
    
        result.curFile = curFile;
        result.curRootObject = curRootObject;
        result.curRootObjectEnd = curRootObjectEnd;
        result.curRootObjectOffset = curRootObjectOffset;
        result.curRootObjectSize = curRootObjectSize;
        result.sequencePrefix = sequencePrefix;
        //result.sequenceSuffix = sequenceSuffix;
        result.pagenumMap = pagenumMap;
        
        result.isRaw = isRaw;
        
        return result;
    }
    
    public <T extends mystobj> T[] readArray( Class<T> objclass,int size) throws readexception
    {
        T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, size);
        for(int i=0;i<size;i++)
        {
            result[i] = (T)this.readObj(objclass);
            /*try
            {
                //java.lang.reflect.Constructor constr = objclass.getConstructor(context.class);
                //T newT = (T)constr.newInstance(this);
                //result[i] = newT;
                //result[i] = (T)objclass.getConstructor(context.class).newInstance(this);
            }
            //catch(Exception e)
            //{
            //    m.msg("Bytestream: java gunk: unable to create new instance.");
            //    //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
            //}
            catch(java.lang.NoSuchMethodException e)
            {
                throw new readexception("Bytestream: java gunk: unable to create new instance.");
                //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
            }
            catch(java.lang.InstantiationException e)
            {
                throw new readexception("Bytestream: java gunk: unable to create new instance.");
            }
            catch(java.lang.IllegalAccessException e)
            {
                throw new readexception("Bytestream: java gunk: unable to create new instance.");
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
                }
            }*/
            
            //hi[0] = newT;
            //java.lang.reflect.Constructor<T> con = new java.lang.reflect.Constructor();
            //con.
            //mystobj newobj = T.create(this); //create the object.
            //T newT = (T)newobj; //cast the object.
            //result.add(newT); //put it in the array.
        }
        return result;
    }
    public <T extends mystobj> Vector<T> readVector( Class<T> objclass,int size) throws readexception
    {
        //T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, size);
        Vector<T> result = new Vector<T>(size);
        for(int i=0;i<size;i++)
        {
            //result[i] = (T)this.readObj(objclass);
            result.add((T)this.readObj(objclass));
        }
        return result;
    }
    public <T extends mystobj> ArrayList<T> readArrayList(Class<T> klass, int size) throws readexception
    {
        ArrayList<T> result = new ArrayList<T>();
        for(int i=0;i<size;i++)
        {
            result.add((T)this.readObj(klass));
        }
        return result;
        //return in.readArrayList(klass,size);
    }
        //T[] result = (T[])(new Object[size]); //cast from Object[], so we can create the array. Otherwise we get a compile error.
        //Vector<T> result = new Vector<T>();
            //T dummy = new T();
            //T dummy = null;
            //Class who = obj;//obj.getClass();
            //obj.
            //Class wha = T.class;
            //Class as = 
            //T.class.getName();
            //Bytestream dummy2 = null;
            //Class[] cls = new Class[1];
            //cls[0] = dummy2.getClass();
            //T newT = null;
            //java.lang.reflect.Constructor[] cs = objclass.getConstructors();
    
    public <T extends mystobj> T readObj(Class objclass) throws readexception
    {
        //Class bytestreamClass = context.class;
        //T result = null;
        try
        {
            //java.lang.reflect.Constructor constr = objclass.getConstructor(bytestreamClass);
            //T newT = (T)constr.newInstance(this);
            //result = newT;
            return (T)objclass.getConstructor(context.class).newInstance(this);
        }
        catch(java.lang.NoSuchMethodException e)
        {
            //throw new readexception("Bytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.InstantiationException e)
        {
            //throw new readexception("Bytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.IllegalAccessException e)
        {
            //throw new readexception("Bytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
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
                //throw new readexception("Bytestream: java gunk: unable to create new instance.");
                throw new shared.nested(e);
                //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
            }
        }
        /*catch(Exception e)
        {
            //m.msg("Bytestream: java gunk: unable to create new instance.");
            //int sai=1;
            if(e instanceof readexception)
            {
                throw e;
            }
        }*/
        //return result;
        //mystobj newobj = T.create(this);
        //T newT = (T)newobj;
        //return newT;
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
    public float readFloat()
    {
        return in.readFloat();
    }
    public boolean areBytesKnownToBeAvailable()
    {
        return in.areBytesKnownToBeAvailable();
    }
    public String toString()
    {
        String result = "";
        if(in != null)
        {
            result += "(bytes left: "+Integer.toString(this.curRootObjectEnd-in.getAbsoluteOffset())+")";
            result += in.toString();
        }
        if(this.curRootObject!=null) result+=" \n"+curRootObject.toString();
        //return "(no info)";
        
        return result;
    }
}

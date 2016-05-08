/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Vector;
import java.util.HashMap;
import java.io.InputStream;
import java.util.ArrayList;

//public abstract interface IBytestream
public abstract class IBytestream
{
    abstract public byte readByte();
    abstract public byte[] readBytes(int num);
    abstract public int readInt();
    //abstract public int[] readInts(int num);
    abstract public short readShort();
    abstract public int getAbsoluteOffset();
    abstract public int getFilelength();
    abstract public int getBytesRemaining();
    abstract public IBytestream Fork(long offset);
    abstract protected int read();
    private HashMap<String,Object> _context = new HashMap();

    public boolean areBytesKnownToBeAvailable() //override this if we can tell this without the exact number known.
    {
        return (getBytesRemaining()!=0);
    }

    public InputStream getChildStreamIfExists()
    {
        return null;
    }
    public void set(String keyname, Object value)
    {
        _context.put(keyname, value);
    }
    public Object getValue(String keyname)
    {
        return _context.get(keyname);
    }
    public int getIntValue(String keyname)
    {
        return (Integer)getValue(keyname);
    }
    public String getStringValue(String keyname)
    {
        return (String)getValue(keyname);
    }
    public void skip(long n)
    {
        for(long i=0;i<n;i++)
        {
            readByte();
        }
    }
    public long readLong()
    {
        /*byte b0 = this.readByte();
        byte b1 = this.readByte();
        byte b2 = this.readByte();
        byte b3 = this.readByte();
        byte b4 = this.readByte();
        byte b5 = this.readByte();
        byte b6 = this.readByte();
        byte b7 = this.readByte();
        long r = (b7<<56)|(b6<<48)|(b5<<40)|(b4<<32)|(b3<<24)|(b2<<16)|(b1<<8)|(b0<<0);
        return r;*/
        long i0 = b.Int32ToInt64(this.readInt());
        long i1 = b.Int32ToInt64(this.readInt());
        long r = (i1<<32) | (i0<<0);
        return r;
    }
    public float readFloat()
    {
        int data = readInt();
        float result = Float.intBitsToFloat(data);
        return result;
    }
    public float[] readFloats(int num)
    {
        float[] result = new float[num];
        for(int i=0;i<num;i++)
        {
            result[i] = readFloat();
        }
        return result;
    }
    public int[] readInts(int num)
    {
        int[] result = new int[num];
        for(int i=0;i<num;i++)
        {
            result[i] = readInt();
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
    public void close()
    {
        //do nothing by default, but may be overridden.
    }
    /*public static IBytestream createFromFilename(String filename)
    {
        return createFromFilenameOffset(filename,0);
    }
    public abstract static IBytestream createFromFilenameOffset(String filename, int offset);*/
    //public String toString();
    //abstract public <T> Vector<T> readVector( Class<T> objclass, int size);
    //abstract public <T> T readObj(Class<T> objclass);
    //abstract public <T> T[] readArray( Class<T> objclass, int size);
    
    public String sourceName = "";
    
    public IBytestream Fork()
    {
        return this.Fork(this.getAbsoluteOffset());
    }
    public <T> Vector<T> readVector( Class<T> objclass, int size)
    {
        Vector<T> result = new Vector<T>();
        for(int i=0;i<size;i++)
        {
            result.add((T)this.readObj(objclass));
        }
        return result;
    }
    public <T> ArrayList<T> readArrayList( Class<T> objclass, int size)
    {
        ArrayList<T> result = new ArrayList<T>();
        for(int i=0;i<size;i++)
        {
            result.add((T)this.readObj(objclass));
        }
        return result;
    }
    public <T> T[] readArray( Class<T> objclass, int size)
    {
        T[] result = generic.makeArray(objclass, size);
        //T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, size);
        for(int i=0;i<size;i++)
        {
            result[i] = (T)this.readObj(objclass);
        }
        return result;
    }
    <T> java.lang.reflect.Constructor findIBytestreamConstructor(Class<T> objclass) throws java.lang.NoSuchMethodException
    {
        for(java.lang.reflect.Constructor constructor: objclass.getConstructors())
        {
            Class[] args = constructor.getParameterTypes();
            if(args.length==1)// && args[0].equals(IBytestream.class))
            {
                //if(args[0].isAssignableFrom(IBytestream.class))
                if(IBytestream.class.isAssignableFrom(args[0]))
                {
                    return constructor;
                }
            }
        }
        throw new java.lang.NoSuchMethodException();
        //return null;
    }
    public <T> T readObj(Class<T> objclass)
    {
        try
        {

            //return (T)objclass.getConstructor(IBytestream.class).newInstance(this);
            return (T)findIBytestreamConstructor(objclass).newInstance(this);
        }
        catch(java.lang.NoSuchMethodException e)
        {
            //throw new uncaughtexception("IBytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.InstantiationException e)
        {
            //throw new uncaughtexception("IBytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.IllegalAccessException e)
        {
            //throw new uncaughtexception("IBytestream: java gunk: unable to create new instance.");
            throw new shared.nested(e);
            //If an exception is being thrown here, it's probably because an inner class was attempted.  Make it static(which just means that the outer class isn't passed as a parameter.)
        }
        catch(java.lang.reflect.InvocationTargetException e)
        {
            Throwable e2 = e.getCause();
            if(e2 instanceof readexception)
            {
                //throw (readexception)e2; //rethrow.
                e2.printStackTrace();
                throw new uncaughtexception("Encountered readexception.");
            }
            else if(e2 instanceof ignore)
            {
                throw (ignore)e2;
            }
            else
            {
                e2.printStackTrace();
                //throw new uncaughtexception("IBytestream: java gunk: unable to create new instance.");
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
    public byte[][] readMultiDimensionBytes(int num1, int num2)
    {
        byte[][] result = new byte[num1][];
        for(int i=0;i<num1;i++)
        {
            result[i] = this.readBytes(num2);
        }
        return result;
    }
    public int[][] readMultiDimensionInts(int num1, int num2)
    {
        int[][] result = new int[num1][];
        for(int i=0;i<num1;i++)
        {
            result[i] = this.readInts(num2);
        }
        return result;
    }
    public short readShortBigEndian()
    {
        int b1 = this.read();
        int b2 = this.read();
        short result = (short)(b1<<8 | b2);
        return result;
    }
    public int readIntBigEndian()
    {
        int b1 = this.read();
        int b2 = this.read();
        int b3 = this.read();
        int b4 = this.read();
        int result = (b1<<24 | b2<<16 | b3<<8 | b4);
        return result;
    }
    public int readIntAsTwoShorts()
    {
        short s1 = this.readShort();
        short s2 = this.readShort();
        int r = ((s1&0xFFFF) << 16) | ((s2&0xFFFF) << 0);
        return r;
    }
}

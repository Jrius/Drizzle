/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;

public class generic
{
    public static <T> T createObjectWithDefaultConstructor(Class<T> c)
    {
        try
        {
            Constructor constr = c.getConstructor();
            Object result = constr.newInstance();
            T result2 = c.cast(result);
            return result2;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("gereric: Unable to create an instance of this class.");
        }
    }
    public static <T> T createObjectWithSingleArgumentConstructor(Class<T> c, Class arg0class, Object arg0)
    {
        try
        {
            //Constructor constr = c.getConstructor();
            Constructor constr = c.getConstructor(arg0class);
            //Object result = constr.newInstance();
            Object result = constr.newInstance(arg0);
            T result2 = c.cast(result);
            return result2;
        }
        catch(Exception e)
        {
            //throw new shared.uncaughtexception("gereric: Unable to create an instance of this 1-arg class.");
            throw new shared.nested(e);
        }
    }
    public static <T> T createShallowClone(T obj)
    {
        try
        {
            Method m = obj.getClass().getMethod("clone");
            Object clone = m.invoke(obj);
            T result = (T)clone;
            return result;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("gereric: Unable to create a shallow clone of this object.");
        }
    }
    public static <T> T createSerializedClone(T obj)
    {
        try
        {
            //write
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream out2 = new ObjectOutputStream(out);
            out2.writeObject(obj);
            byte[] bytes = out.toByteArray();
            
            //read
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream in2 = new ObjectInputStream(in);
            T result = (T)in2.readObject();
            
            return result;
            
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("gereric: Unable to create a serialized clone of this object.");
        }
    }
    public static <T> T[] convertVectorToArray(Vector<T> v, Class c)
    {
        T[] result = (T[])makeArray(c,v.size());
        for(int i=0;i<result.length;i++)
        {
            result[i] = v.get(i);
        }
        return result;
    }
    public static <T> Vector<T> convertArrayToVector(T[] arry)
    {
        //m.msg("Using untested convertArrayToVector method.");
        Vector<T> result = new Vector<T>();
        for(int i=0;i<arry.length;i++)
        {
            result.add(arry[i]);
        }
        return result;
    }
    public static <T> T[] makeArray(Class<T> objclass, int length)
    {
        T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, length);
        return result;
    }
    public static <T> Vector<T> makeVector(T... values)
    {
        return convertArrayToVector(values);
    }
    public static <T> T[] makeArray(T... values)
    {
        return values;
    }
    public static <T> T[] mergeArrays(Class objclass, T[]... values)
    {
        int size = 0;
        //Vector<T> result = new Vector<T>();
        for(T[] t: values)
        {
            size += t.length;
            //for(int i=0;i<t.length;i++)
            //{
            //    result.add(t[i]);
            //}
        }
        T[] result = (T[])generic.makeArray(objclass, size);
        int pos = 0;
        for(T[] t: values)
        {
            for(T t2: t)
            {
                result[pos] = t2;
                pos++;
            }
        }
        return result;
        //return convertVectorToArray(result, objclass);
        
    }
    public static <T> T[] prependToArray(T item, T[] values, Class c)
    {
        T[] result = (T[])generic.makeArray(c, values.length+1);
        result[0] = item;
        for(int i=0;i<values.length;i++)
        {
            result[i+1] = values[i];
        }
        return result;
    }
    public static <T> T[] appendToArray(T[] values, T item, Class c)
    {
        T[] result = (T[])generic.makeArray(c, values.length+1);
        for(int i=0;i<values.length;i++)
        {
            result[i] = values[i];
        }
        result[values.length] = item;
        return result;
    }
}

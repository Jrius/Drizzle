/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import shared.*;
import java.io.Serializable;
import java.io.ByteArrayOutputStream;

public abstract class SerializationUtils
{
    public static Serializable ReadFromFile(String filename, boolean throwExceptionOnError)
    {
        File file = new File(filename);
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new FileInputStream(file));
            Object obj = in.readObject();
            in.close();
            return (Serializable)obj;
        }
        catch(Exception e)
        {
            try{
                if(in!=null) in.close();
            } catch(Exception e2){}
            if(throwExceptionOnError)
                throw new nested(e);
            else
                return null;
        }
    }
    public static void SaveToFile(String filename, Serializable obj)
    {
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(obj);
            out.close();
        }
        catch(Exception e)
        {
            try{
                if(out!=null) out.close();
            }catch(Exception e2){}
            throw new nested(e);
        }
    }
    public static byte[] SaveToBytes(Serializable obj)
    {
        ObjectOutputStream out = null;
        try
        {
            ByteArrayOutputStream r = new ByteArrayOutputStream();
            out = new ObjectOutputStream(r);
            out.writeObject(obj);
            out.close();
            return r.toByteArray();
        }
        catch(Exception e)
        {
            try{
                if(out!=null) out.close();
            }catch(Exception e2){}
            throw new nested(e);
        }
    }
}

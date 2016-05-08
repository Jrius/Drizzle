/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.sql.ResultSet;
import java.util.Vector;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Results
{
    ResultSet r;

    public Results(ResultSet r)
    {
        this.r = r;
    }
    public boolean first()
    {
        try{
            return r.first();
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public boolean next()
    {
        try{
            return r.next();
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public String getString(int ind)
    {
        try{
            return r.getString(ind);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public String getString(String name)
    {
        try{
            return r.getString(name);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public long getLong(int ind)
    {
        try{
            return r.getLong(ind);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public int getInt(int ind)
    {
        try{
            return r.getInt(ind);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public int getInt(String name)
    {
        try{
            return r.getInt(name);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public byte[] getBytes(int ind)
    {
        try{
            return r.getBytes(ind);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public byte[] getBytes(String name)
    {
        try{
            return r.getBytes(name);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public Timestamp getTimestamp(int ind)
    {
        try{
            return r.getTimestamp(ind);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public byte getByte(String name)
    {
        try{
            return r.getByte(name);
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public <T extends Node> Vector<T> cast()
    {
        try{
            Vector<T> result = new Vector<T>();
            boolean hasresult = r.first();
            while(hasresult)
            {
                T node = (T)Node.getNode(this);
                result.add(node);
                hasresult = r.next();
            }
            return result;
        }catch(Exception e){ throw new shared.nested(e); }
    }
    public ArrayList<Node.Ref> castAsRefs()
    {
        try{
            ArrayList<Node.Ref> result = new ArrayList<Node.Ref>();
            boolean hasresult = r.first();
            while(hasresult)
            {
                Node.Ref ref = new Node.Ref(this);
                result.add(ref);
                hasresult = r.next();
            }
            return result;
        }catch(Exception e){ throw new shared.nested(e); }
    }
}

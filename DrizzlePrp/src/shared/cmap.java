/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

//Used to be ChainedHashMap.

public class cmap<S,T>
{
    HashMap<S,T> map;
    //S leftval;
    //T child;
    
    public cmap()
    {
        map = new HashMap<S,T>();
        //leftval = s;
        //child = new ChainedHashMap;
    }
    public ArrayList<Pair<S,T>> getAllElements()
    {
        ArrayList<Pair<S,T>> result = new ArrayList();
        for(Entry<S,T> e: map.entrySet())
        {
            S s = e.getKey();
            T t = e.getValue();
            result.add(new Pair(s,t));
        }
        //return (Pair<S,T>[])result.toArray();
        return result;
    }
    public T get(S key)
    {
        return map.get(key);
    }
    public Object get2(Object... keys)
    {
        return get(keys, 0);
    }
    private Object get(Object[] keys, int pos)
    {
        Object key = keys[pos];
        Object child = map.get(key);
        if(child instanceof cmap)
        {
            cmap childasmap = (cmap)child;
            if(pos+1==keys.length)
            {
                return childasmap;
            }
            else
            {
                return childasmap.get(keys,pos+1);
            }
        }
        else if(child==null)
        {
            return null; //can't find it.
        }
        else
        {
            return child; //found a leaf.
        }
    }
    public void put(Object... keys)
    {
        put(keys,0);
    }
    private void put(Object[] keys, int pos)
    {
        S key = (S)keys[pos];
        Object child = map.get(key);
        if(child instanceof cmap)
        {
            cmap childasmap = (cmap)child;
            childasmap.put(keys,pos+1);
        }
        else if(child==null)
        {
            if(pos+2==keys.length)
            {
                T value = (T)keys[pos+1];
                map.put(key, value); //put the leaf in.
            }
            else
            {
                //create the rest.
                cmap newchild = new cmap();
                T newchild2 = (T)newchild;
                map.put(key, newchild2);
                newchild.put(keys,pos+1);
            }
        }
        else
        {
            //reassign value.
            T value = (T)keys[pos+1];
            map.put(key, value);
        }
    }
}
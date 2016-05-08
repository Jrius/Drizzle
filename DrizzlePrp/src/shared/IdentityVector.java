/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

public class IdentityVector<T> //implements List<T>
{
    IdentityHashMap<T, Object> map;
    //java.util.
    
    public IdentityVector()
    {
        map = new IdentityHashMap<T, Object>();
    }
    
    public boolean contains(T item)
    {
        return map.containsKey(item);
    }
    
    public void add(T item)
    {
        map.put(item, null);
    }
    
    public void remove(T item)
    {
        map.remove(item);
    }
    
    public Vector<T> getVector()
    {
        Vector<T> result = new Vector<T>();
        int size = map.size();
        Iterator<T> iter = map.keySet().iterator();
        while(iter.hasNext())
        {
            result.add(iter.next());
        }
        return result;
    }
}

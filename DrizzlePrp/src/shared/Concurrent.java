/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
//import java.util.HashSet;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;

public class Concurrent
{

    public static <S,T> Map<S,T> getThreadsafeHashMap()
    {
        Map<S,T> map = new HashMap<S,T>();
        Map<S,T> threadsafeMap = Collections.synchronizedMap(map);
        return threadsafeMap;
    }

    public static <T> List<T> getThreadsafeList()
    {
        List<T> list = new ArrayList<T>();
        List<T> threadsafeList = Collections.synchronizedList(list);
        return threadsafeList;
    }

    public static <T> Set<T> getConcurrentSet()
    {
        ConcurrentHashMap<T,Boolean> hashmap = new ConcurrentHashMap<T,Boolean>();
        Set<T> set = Collections.newSetFromMap(hashmap);
        return set;
    }

    public static <T> Queue<T> getConcurrentQueue()
    {
        ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();
        return queue;
    }

    public static <T> LinkedBlockingQueue<T> getConcurrentBlockingQueue()
    {
        LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();
        return queue;
    }

    public static <S,T> ConcurrentHashMap<S,T> getConcurrentHashMap()
    {
        //returns ConcurrentHashMap instead of just Map, as it has some handy methods.
        ConcurrentHashMap<S,T> hashmap = new ConcurrentHashMap<S,T>();
        return hashmap;
    }

    public static interface Callback<V>
    {
        void callback(V val);
    }

    public static class HashSetMap<S,T> extends HashMap<S,HashSet<T>>
    {
        //private HashMap<S,HashSet<T>> map;
        public HashSetMap()
        {
            super();
            //map = new HashMap<S,HashSet<T>>();
        }

        public Set<T> getSetCreatingIfNeeded(S val)
        {
            HashSet<T> r = this.get(val);
            if(r==null)
            {
                r = new HashSet<T>();
                this.put(val, r);
            }
            return r;
        }
        public void addToSet(S key, T val)
        {
            Set<T> set = this.getSetCreatingIfNeeded(key);
            set.add(val);
        }
    }

}

//public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>//, Cloneable, java.io.Serializable
//{
//    //static final long serialVersionUID = -5024744406713221676L;
//
//    private /*transient*/ ConcurrentHashMap<E,Object> map;
//
//    private static final Object PRESENT = new Object();
//
//    public Concurrent()
//    {
//        map = new ConcurrentHashMap<E,Object>();
//    }
//
//    public Concurrent(Collection<? extends E> c)
//    {
//        map = new ConcurrentHashMap<E,Object>(Math.max((int) (c.size()/.75f) + 1, 16));
//        addAll(c);
//    }
//
//    public Concurrent(int initialCapacity, float loadFactor)
//    {
//        map = new ConcurrentHashMap<E,Object>(initialCapacity, loadFactor);
//    }
//
//    public Concurrent(int initialCapacity)
//    {
//        map = new ConcurrentHashMap<E,Object>(initialCapacity);
//    }
//
//    public Iterator<E> iterator()
//    {
//        return map.keySet().iterator();
//    }
//
//    public int size()
//    {
//        return map.size();
//    }
//
//    public boolean isEmpty()
//    {
//        return map.isEmpty();
//    }
//
//    public boolean contains(Object o)
//    {
//        return map.containsKey(o);
//    }
//
//    public boolean add(E e)
//    {
//        return map.put(e, PRESENT)==null;
//    }
//
//    public boolean remove(Object o)
//    {
//        return map.remove(o)==PRESENT;
//    }
//
//    public void clear()
//    {
//        map.clear();
//    }
//
//}

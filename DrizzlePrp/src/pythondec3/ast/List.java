/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
//import java.util.Iterator;

public class List<T extends Ast> extends Ast implements java.lang.Iterable<Ast>, IList
{
    //Class klass;
    ArrayList<T> list = new ArrayList();

    /*public List(Class kls)
    {
        klass = kls;
    }*/
    public java.util.Iterator iterator()
    {
        return list.iterator();
    }
    
    public List()
    {
        int dummy=0;
    }

    public List getlist(){return this;}

    /*public List(Ast... items)
    {
        for(Ast item: items)
        {
            list.add(item);
        }
    }*/

    /*public List(Class kls, Ast item)
    {
        klass = kls;
        add(item);
    }*/

    public List(T item)
    {
        add(item);
    }

    public List<T> add(Ast item)
    {
        T t = (T)item;
        list.add(t);
        return this;
    }
    public int size()
    {
        return list.size();
    }
    public boolean remove(Object o)
    {
        return list.remove(o);
    }
    public T remove(int index)
    {
        return list.remove(index);
    }
    public T get(int i)
    {
        return list.get(i);
    }
    public T getsafe(int i)
    {
        if(i<0 || i>=list.size()) return null;
        return list.get(i);
    }
    public void gen2(sgen s)
    {
        for(T t: list)
        {
            t.gen(s);
        }
    }
}

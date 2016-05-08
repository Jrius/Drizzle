/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import java.util.Vector;

public class PyTuple extends PyObject
{
    int n;
    public PyObject[] items;

    public PyTuple(IBytestream c)
    {
        n = c.readInt();
        items = new PyObject[n];
        for(int i=0;i<n;i++)
        {
            items[i] = PyObject.read(c);
        }
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'(');
        c.writeInt(n);
        for(PyObject item: items)
        {
            item.marshal(c);
        }
    }
    private PyTuple(){}
    public String toString()
    {
        String result = "(PyTuple) ";
        for(PyObject obj: items)
        {
            result += obj.toString()+" , ";
        }
        return result;
    }

    public static PyTuple create(Vector<PyObject> objs)
    {
        PyTuple r = new PyTuple();
        r.n = objs.size();
        r.items = new PyObject[r.n];
        for(int i=0;i<r.n;i++)
        {
            r.items[i] = objs.get(i);
        }
        return r;
    }
    public boolean equals(Object o2)
    {
        if(o2==null) return false;
        if(o2.getClass()!=PyTuple.class) return false;
        PyTuple o = (PyTuple)o2;
        if(this.items.length!=o.items.length) return false;
        for(int i=0;i<items.length;i++)
        {
            if(!items[i].compare(o.items[i])) return false;
        }
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

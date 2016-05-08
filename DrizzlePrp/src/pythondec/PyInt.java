/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;

public class PyInt extends PyObject
{
    int val;

    public PyInt(IBytestream c)
    {
        val = c.readInt();
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'i');
        c.writeInt(val);
    }

    private PyInt(){}
    public String toString()
    {
        return "PyInt: "+Integer.toString(val);
    }
    public String toJavaString()
    {
        return Integer.toString(val);
    }
    public static PyInt create(int val)
    {
        PyInt r = new PyInt();
        r.val = val;
        return r;
    }

    public int hashCode()
    {
        return val;
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyInt)) return false;
        PyInt o2 = (PyInt)o;
        if(o2.val!=val) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

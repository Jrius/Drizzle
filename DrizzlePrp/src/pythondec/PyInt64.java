/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;

public class PyInt64 extends PyObject
{
    long val;

    public PyInt64(IBytestream c)
    {
        shared.m.throwUncaughtException("untested!");
        val = c.readLong();
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'I');
        c.writeLong(val);
    }

    private PyInt64(){}
    public String toString()
    {
        return "PyInt64: "+Long.toString(val);
    }
    public String toJavaString()
    {
        return Long.toString(val);
    }
    public static PyInt64 create(long val)
    {
        PyInt64 r = new PyInt64();
        r.val = val;
        return r;
    }

    public int hashCode()
    {
        return (int)val;
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyInt64)) return false;
        PyInt64 o2 = (PyInt64)o;
        if(o2.val!=val) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

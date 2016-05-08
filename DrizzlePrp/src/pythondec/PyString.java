/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.b;

public class PyString extends PyObject
{
    public int n;
    public byte[] rawstr;

    public PyString(IBytestream c)
    {
        n = c.readInt();
        rawstr = c.readBytes(n);
    }
    private PyString(){}
    public String toString()
    {
        return "PyString: "+b.BytesToString(rawstr);
    }
    public String toJavaString()
    {
        return b.BytesToString(rawstr);
    }

    public static PyString create(String s)
    {
        return create(b.StringToBytes(s));
    }
    public static PyString create(byte[] data)
    {
        PyString r = new PyString();
        r.rawstr = data;
        r.n = r.rawstr.length;
        return r;
    }

    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'s');
        c.writeInt(n);
        c.writeBytes(rawstr);
    }

    public int hashCode()
    {
        return java.util.Arrays.hashCode(rawstr);
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyString)) return false;
        PyString o2 = (PyString)o;
        if(!java.util.Arrays.equals(o2.rawstr, this.rawstr)) return false;
        return true;
    }

    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.b;

public class PyFloat extends PyObject
{
    byte n;
    byte[] rawstr;

    public PyFloat(IBytestream c)
    {
        n = c.readByte();
        int count = b.ByteToInt32(n);
        rawstr = c.readBytes(count);
    }
    public String toString()
    {
        return "PyFloat: "+b.BytesToString(rawstr);
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(o.getClass()!=PyFloat.class) return false;
        PyFloat o2 = (PyFloat)o;
        if(!b.isEqual(rawstr, o2.rawstr)) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }
    public String toJavaString()
    {
        String r = b.BytesToString(rawstr);
        return r;
    }

}

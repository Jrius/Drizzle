/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;

public class PyNone extends PyObject
{
    public PyNone(IBytestream c)
    {
        //nothing in it.
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'N');
    }
    private PyNone(){}
    public String toString()
    {
        return "PyNone: ";
    }
    public String toJavaString()
    {
        return "None";
    }
    public static PyNone create()
    {
        return new PyNone();
    }

    public int hashCode()
    {
        return 0;
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyNone)) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

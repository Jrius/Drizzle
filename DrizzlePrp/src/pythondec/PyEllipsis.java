/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;

public class PyEllipsis extends PyObject
{
    public PyEllipsis(IBytestream c)
    {
        //nothing in it.
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'.');
    }
    private PyEllipsis(){}
    public String toString()
    {
        return "PyElipsis: ";
    }
    public String toJavaString()
    {
        return "...";
    }
    public static PyEllipsis create()
    {
        return new PyEllipsis();
    }

    public int hashCode()
    {
        return 0;
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyEllipsis)) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

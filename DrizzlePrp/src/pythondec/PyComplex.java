/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.b;

public class PyComplex extends PyObject
{
    byte realn;
    byte[] realrawstr;
    byte imagn;
    byte[] imagrawstr;

    public PyComplex(IBytestream c)
    {
        realn = c.readByte();
        int count = b.ByteToInt32(realn);
        realrawstr = c.readBytes(count);

        imagn = c.readByte();
        count = b.ByteToInt32(imagn);
        imagrawstr = c.readBytes(count);
    }
    //public void marshal(shared.IBytedeque c)
    //{
    //    c.writeByte((byte)'x');
    //}

    private PyComplex(){}
    public String toString()
    {
        return "PyComplex: "+b.BytesToString(realrawstr)+" + i*"+b.BytesToString(imagrawstr);
    }
    public String toJavaString()
    {
        String realstr = b.BytesToString(realrawstr);
        String imagstr = b.BytesToString(imagrawstr);
        if(realstr.equals("0.0"))
        {
            return imagstr+"j";
        }
        else if(imagstr.equals("0.0"))
        {
            //this shouldn't happen in practise, and I guess it might be wrong in that it will be recompiled as a Flt...
            //return realstr;
            return "("+realstr+"+"+imagstr+"j)";
        }
        else
        {
            return "("+realstr+"+"+imagstr+"j)";
        }
    }
    //public static PyComplex create(int val)
    //{
    //    PyComplex r = new PyComplex();
    //    r.val = val;
    //    return r;
    //}

    //public int hashCode()
    //{
    //    return val;
    //}
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyComplex)) return false;
        PyComplex o2 = (PyComplex)o;
        if(!b.isEqual(realrawstr, o2.realrawstr)) return false;
        if(!b.isEqual(imagrawstr, o2.imagrawstr)) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.b;

public class PyUnicode extends PyObject
{
    public int n;
    public byte[] rawstr;

    public PyUnicode(IBytestream c)
    {
        n = c.readInt();
        rawstr = c.readBytes(n);
    }
    private PyUnicode(){}
    public String toString()
    {
        return "PyUnicode: "+toJavaString();
    }
    public String toJavaString()
    {
        //return "u'"+toEscapedString()+"'";
        try{
            return new java.lang.String(rawstr, "UTF-8");
        }catch(Exception e){
            throw new shared.uncaughtexception("unable to convert UTF-8 string");
        }
    }
    /*public String toEscapedString()
    {
        try{
            String r = new java.lang.String(rawstr, "UTF-8");
            r = pythondec3.helpers.escapeUnicodeString(r);
            return r;
        }catch(Exception e){
            throw new shared.uncaughtexception("unable to convert UTF-8 string");
        }
    }*/
    //public static PyUnicode create(String s)
    //{
    //    return create(b.StringToBytes(s));
    //}
    //public static PyUnicode create(byte[] data)
    //{
    //    PyUnicode r = new PyUnicode();
    //    r.rawstr = data;
    //    r.n = r.rawstr.length;
    //    return r;
    //}

    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'u');
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
        if(!(o instanceof PyUnicode)) return false;
        PyUnicode o2 = (PyUnicode)o;
        if(!java.util.Arrays.equals(o2.rawstr, this.rawstr)) return false;
        return true;
    }

    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }
}

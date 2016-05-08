/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.m;
import shared.IBytedeque;

public class PyObject
{
    public static PyObject read(IBytestream c)
    {
        byte type = c.readByte();
        switch(type)
        {
            case 'c':
                return new PyCode(c);
            case 's':
                return new PyString(c);
            case '(':
                return new PyTuple(c);
            case 'N':
                return new PyNone(c);
            case 'i':
                return new PyInt(c);
            case 'f':
                return new PyFloat(c);
            case 'I':
                return new PyInt64(c);
            case 'l':
                return new PyLong(c);
            case 'x':
                return new PyComplex(c);
            case 'u':
                return new PyUnicode(c);
            case '.':
                return new PyEllipsis(c);
            default:
                throw new shared.uncaughtexception("Unhandled PyObject type: "+Byte.toString(type));
        }
    }
    public <T> T cast()
    {
        T result = (T)this;
        return result;
    }

    public void marshal(shared.IBytedeque c)
    {
        m.throwUncaughtException("Unimplemented.");
    }


    public int hashCode()
    {
        throw new shared.uncaughtexception("implement in subclass.");
    }
    public boolean equals(Object o)
    {
        throw new shared.uncaughtexception("implement in subclass.");
        //if(o==null) return false;
        //if(!(o instanceof PyNone)) return false;
        //return true;
    }

    public boolean compare(PyObject o)
    {
        throw new shared.uncaughtexception("implement in class: "+this.getClass().toString());
    }

}

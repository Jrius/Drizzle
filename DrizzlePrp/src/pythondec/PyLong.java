/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;

public class PyLong extends PyObject
{
    int length; //in number of shorts
    //byte[] data;
    short[] datas;

    //0 is 0:[]
    //1 is 1:[1,0]
    //2 is 1:[2,0]
    //-1 is -1:[1,0]
    //-2 is -1:[2,0]
    //32767 is 1:[-1,127]
    //32768 is 2:[0,0,1,0]
    //32769 is 2:[1,0,1,0]
    //65535 is 2:[-1,127,1,0]
    //65536 is 2:[0,0,2,0]
    //65537 is 2:[1,0,2,0]

    java.math.BigInteger num;

    public PyLong(IBytestream c)
    {
        length = c.readInt();
        int truelength = (length<0)?(-length):length;
        //data = c.readBytes(2*truelength);
        datas = c.readShorts(truelength);
        
        //get the raw unsigned data for java.  Java only takes 2's complement big-endian.
        int numbits = 15*datas.length;
        int numbytes = numbits/8 + 1; //extra byte for 0.
        byte[] data2 = new byte[numbytes];
        for(int i=0;i<numbits;i++) //from least to most significant
        {
            int inbyte = i/15;
            int inpos = i%15;
            int curbit = (datas[inbyte]>>>inpos)&0x1;
            int outbyte = i/8;
            int outpos = i%8;
            data2[data2.length-outbyte-1] |= curbit<<outpos;
        }

        //for(int i=0;i<length;i++)
        //{
        //    data2[i*2+0] = data[i*2+1];
        //    data2[i*2+1] = data[i*2+0];
        //}
        //data2[2*truelength] = 0; //make sure this is *unsigned* by prefixing with a zero.
        //for(int i=0;i<2*truelength;i++)
        //{
        //    data2[i] = data[2*truelength-i-1];
        //}

        num = new java.math.BigInteger(data2);
        if(length<0) num = num.negate();
        
    }
    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'l');
        c.writeInt(length);
        c.writeShorts(datas);
    }

    private PyLong(){}
    public String toString()
    {
        return "PyLong: "+num.toString();
    }
    public String toJavaString()
    {
        return num.toString()+"L";
    }
    /*public static PyLong create(int val)
    {
        PyLong r = new PyLong();
        r.val = val;
        return r;
    }*/

    public int hashCode()
    {
        return num.hashCode();
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyLong)) return false;
        PyLong o2 = (PyLong)o;
        if(o2.length!=length) return false;
        if(!shared.b.isEqualShorts(datas, o2.datas)) return false;
        return true;
    }
    public boolean compare(PyObject o2)
    {
        return equals(o2);
    }

}

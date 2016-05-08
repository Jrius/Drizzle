/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.*;

//This is false: this class reads a 64 bit value which is the number of seconds since 1970.
public class Timestamp
{
    int item1;
    int item2;
        
    public Timestamp(IBytestream c)
    {
        item1 = c.readInt();
        item2 = c.readInt();
    }

    public void compile(IBytedeque c)
    {
        c.writeInt(item1);
        c.writeInt(item2);
    }
    public long toLong()
    {
        //long l = ((long)item1)<<32 | ((long)item2);
        //long l2 = ((long)item2)<<32 | ((long)item1);
        long l1 = item1;
        //long l2 = item2;
        l1 *= 1000;
        //l2 *= 1000; //we have to multiple by 1000 since java wants milliseconds.
        return l1;
    }
    public String toString()
    {
        long l1 = this.toLong();
        java.util.Date d1 = new java.util.Date(l1);
        //java.util.Date d2= new java.util.Date(l2);
        return d1.toString();//+"::"+d2.toString();
    }
    public String toLongString()
    {
        return Long.toString(this.toLong());
    }
    private Timestamp(){}
    public static Timestamp createDefault()
    {
        Timestamp r = new Timestamp();
        return r;
    }
}

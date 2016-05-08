/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import prpobjects.uruobj;

public class Int24 extends uruobj implements ICompilable
{
    byte a1;
    byte a2;
    byte a3;
    
    public Int24(IBytestream c)
    {
        a1 = c.readByte();
        a2 = c.readByte();
        a3 = c.readByte();
    }
    
    public void compile(IBytedeque c)
    {
        c.writeByte(a1);
        c.writeByte(a2);
        c.writeByte(a3);
    }
    
    public int toInt()
    {
        int result = (a3<<16) | (a2<<8) | a1;
        return result;
    }
    
    public String toString()
    {
        return Integer.toString(this.toInt());
    }
}

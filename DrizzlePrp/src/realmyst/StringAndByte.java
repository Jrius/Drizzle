/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;

public class StringAndByte
{
    public Bstr str;
    public byte b;

    public StringAndByte(IBytestream c)
    {
        //sub_45A950, I think

        str = new Bstr(c);
        b = c.readByte(); //this is used in the switch.
        int dummy=0;
    }
    public String toString()
    {
        return "str="+str.toString()+" byte="+Byte.toString(b);
    }
}

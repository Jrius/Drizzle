/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.*;
import java.util.Vector;
import shared.b;

public class Ntstring
{
    byte[] bytes;
    private Ntstring(){}
    public Ntstring(IBytestream c)
    {
        Vector<Byte> readbytes = new Vector<Byte>();
        byte curbyte;
        do
        {
            curbyte = c.readByte();
            readbytes.add(curbyte);
        }while(curbyte!=0);
        bytes = new byte[readbytes.size()];
        for(int i=0;i<bytes.length;i++)
        {
            bytes[i] = readbytes.get(i);
        }
    }
    public void compile(IBytedeque c)
    {
        c.writeBytes(bytes);
    }
    public String toString()
    {
        if(bytes[bytes.length-1]==0)
            return b.NullTerminatedBytesToString(bytes);
        else
            return b.BytesToString(bytes);
    }
    public static Ntstring createFromString(String s)
    {
        Ntstring result = new Ntstring();
        int length = s.length()+1;
        result.bytes = new byte[length];
        byte[] bytes = b.StringToBytes(s);
        for(int i=0;i<bytes.length;i++)
        {
            result.bytes[i] = bytes[i];
        }
        result.bytes[bytes.length] = 0;
        return result;
    }
}

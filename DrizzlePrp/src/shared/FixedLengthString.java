/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class FixedLengthString
{
    byte[] str;
    
    public FixedLengthString(IBytestream c, int length)
    {
        str = c.readBytes(length);
    }
    public FixedLengthString(String s, int length)
    {
        if(s.length()>length) m.throwUncaughtException("is this okay?");
        str = new byte[length];
        byte[] str2 = b.StringToBytes(s);
        b.CopyBytes(str2, str, str2.length);
    }
    public void compile(IBytedeque c)
    {
        c.writeBytes(str);
    }
    public String toString()
    {
        return b.BytesToString(str);
    }
}

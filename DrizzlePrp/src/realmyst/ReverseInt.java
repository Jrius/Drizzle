/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import shared.b;

public class ReverseInt
{
    private int value;
    
    public ReverseInt(IBytestream c)
    {
        //int temp = c.readInt();
        //value = b.reverseEndianness(temp);
        value = c.readInt();
    }
    
    public void compile(IBytedeque c)
    {
        //int temp = b.reverseEndianness(value);
        c.writeInt(value);
    }
    
    public String toString()
    {
        return Integer.toString(this.convertToInt());
    }
    
    public int convertToInt()
    {
        return b.reverseEndianness(value);
    }
}

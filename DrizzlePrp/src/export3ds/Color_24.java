/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class Color_24 extends tdsobj
{
    public byte r;
    public byte g;
    public byte b;
    
    private Color_24(){}
    
    public static Color_24 create(byte r, byte g, byte b)
    {
        Color_24 result = new Color_24();
        result.r = r;
        result.g = g;
        result.b = b;
        return result;
    }
    public Typeid type(){return Typeid.primary;}
    public void innercompile(IBytedeque c)
    {
        c.writeByte(r);
        c.writeByte(g);
        c.writeByte(b);
    }
    
}

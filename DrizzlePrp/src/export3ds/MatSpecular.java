/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class MatSpecular extends tdsobj
{
    public Color_24 color;
    
    private MatSpecular(){}
    
    public static MatSpecular create(byte r, byte g, byte b)
    {
        MatSpecular result = new MatSpecular();
        result.color = Color_24.create(r, g, b);
        return result;
    }
    public Typeid type(){return Typeid.matspecular;}
    public void innercompile(IBytedeque c)
    {
        color.compile(c);
    }
    
}

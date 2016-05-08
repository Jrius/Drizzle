/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class MatAmbient extends tdsobj
{
    public Color_24 color;
    
    private MatAmbient(){}
    
    public static MatAmbient create(byte r, byte g, byte b)
    {
        MatAmbient result = new MatAmbient();
        result.color = Color_24.create(r, g, b);
        return result;
    }
    public Typeid type(){return Typeid.matambient;}
    public void innercompile(IBytedeque c)
    {
        color.compile(c);
    }
    
}

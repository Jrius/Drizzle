/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class MatDiffuse extends tdsobj
{
    public Color_24 color;
    
    private MatDiffuse(){}
    
    public static MatDiffuse create(byte r, byte g, byte b)
    {
        MatDiffuse result = new MatDiffuse();
        result.color = Color_24.create(r, g, b);
        return result;
    }
    public Typeid type(){return Typeid.matdiffuse;}
    public void innercompile(IBytedeque c)
    {
        color.compile(c);
    }
    
}

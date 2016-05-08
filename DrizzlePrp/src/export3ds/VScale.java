/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class VScale extends tdsobj
{
    Flt value;
    
    private VScale(){}
    
    public static VScale create(float value)
    {
        VScale result = new VScale();
        result.value = Flt.createFromJavaFloat(value);
        return result;
    }
    
    public Typeid type(){return Typeid.vscale;}

    public void innercompile(IBytedeque c)
    {
        value.compile(c);
    }
    
}

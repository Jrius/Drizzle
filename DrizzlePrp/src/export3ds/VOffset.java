/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class VOffset extends tdsobj
{
    Flt value;
    
    private VOffset(){}
    
    public static VOffset create(float value)
    {
        VOffset result = new VOffset();
        result.value = Flt.createFromJavaFloat(value);
        return result;
    }
    
    public Typeid type(){return Typeid.voffset;}

    public void innercompile(IBytedeque c)
    {
        value.compile(c);
    }
    
}

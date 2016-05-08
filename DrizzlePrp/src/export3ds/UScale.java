/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class UScale extends tdsobj
{
    Flt value;
    
    private UScale(){}
    
    public static UScale create(float value)
    {
        UScale result = new UScale();
        result.value = Flt.createFromJavaFloat(value);
        return result;
    }
    
    public Typeid type(){return Typeid.uscale;}

    public void innercompile(IBytedeque c)
    {
        value.compile(c);
    }
    
}

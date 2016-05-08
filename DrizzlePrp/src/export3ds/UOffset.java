/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class UOffset extends tdsobj
{
    Flt value;
    
    private UOffset(){}
    
    public static UOffset create(float value)
    {
        UOffset result = new UOffset();
        result.value = Flt.createFromJavaFloat(value);
        return result;
    }
    
    public Typeid type(){return Typeid.uoffset;}

    public void innercompile(IBytedeque c)
    {
        value.compile(c);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class NamedObj extends tdsobj
{
    public Ntstring name;
    public NamedTriangleObject namedTriangleObject;
    
    private NamedObj(){}
    
    public static NamedObj createNull(String name)
    {
        NamedObj result = new NamedObj();
        result.name = Ntstring.createFromString(name);
        return result;
    }
    public Typeid type(){return Typeid.namedobj;}
    public void innercompile(IBytedeque c)
    {
        name.compile(c);
        namedTriangleObject.compile(c);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class MaterialName extends tdsobj
{
    public Ntstring name;
    
    private MaterialName(){}
    
    public static MaterialName create(String name)
    {
        MaterialName result = new MaterialName();
        result.name = Ntstring.createFromString(name);
        return result;
    }
    public Typeid type(){return Typeid.matname;}
    public void innercompile(IBytedeque c)
    {
        name.compile(c);
    }
}

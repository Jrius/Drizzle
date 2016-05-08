/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class AAA_Template extends tdsobj
{
    
    
    private AAA_Template(){}
    
    public static AAA_Template create()
    {
        AAA_Template result = new AAA_Template();
        return result;
    }
    
    public Typeid type(){throw new uncaughtexception("implement this.");}

    public void innercompile(IBytedeque c)
    {
        throw new uncaughtexception("implement this.");
    }
    
}

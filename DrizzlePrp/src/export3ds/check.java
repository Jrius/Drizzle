/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class check {
    public static short intToShort(int i)
    {
        if(i>30000)
        {
            String msg = "Value is not short. (Todo: This could be set a little higher.)";
            m.err(msg);
            //throw new uncaughtexception(msg);
        }
        return (short)i;
    }
}

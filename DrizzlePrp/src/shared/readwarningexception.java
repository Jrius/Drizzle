/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import shared.m;

public class readwarningexception extends readexception
{
    public readwarningexception(String msg)
    {
        super(msg,true);
        m.warn("(readexception) " , msg);
    }
}

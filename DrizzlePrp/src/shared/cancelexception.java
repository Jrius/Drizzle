/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

//intended to be caught, so no stackdump will be given.
public class cancelexception extends java.lang.RuntimeException
{
    public cancelexception(String msg)
    {
        super(msg);
        m.warn(msg);
    }
}

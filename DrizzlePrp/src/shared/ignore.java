/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class ignore extends shared.uncaughtexception
{
    public ignore(String msg)
    {
        super(msg);
        m.msg("Ignoring item: "+msg);
    }    
}

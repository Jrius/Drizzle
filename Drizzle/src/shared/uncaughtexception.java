/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class uncaughtexception extends java.lang.RuntimeException
{
    public uncaughtexception(String msg)
    {
        super(msg);
        m.err(msg);
    }
}

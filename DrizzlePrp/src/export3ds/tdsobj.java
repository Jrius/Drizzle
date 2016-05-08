/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public abstract class tdsobj implements ICompilable
{
    abstract public void innercompile(IBytedeque c);
    abstract public Typeid type();
    
    final public void compile(IBytedeque c)
    {
        //get insides...
        IBytedeque inner = c.Fork();
        innercompile(inner);
        byte[] in = inner.getAllBytes();
        
        type().compile(c); //write the object typeid.
        c.writeInt(2+4+in.length); //write the entire length.
        c.writeBytes(in); //write the inner object.
    }
}

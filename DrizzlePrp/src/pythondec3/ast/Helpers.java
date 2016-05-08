/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import pythondec.*;
/**
 *
 * @author user
 */
public class Helpers
{

    public static class Kwarg extends Ast
    {
        Tok name;
        Ast value;

        public Kwarg(Tok consttoken, Ast value2)
        {
            name = consttoken;
            value = value2;
        }
        public void gen2(sgen s)
        {
            String n = name.getName(s);
            s.out(n);
            s.out("=");
            value.gen(s);
        }
    }

}

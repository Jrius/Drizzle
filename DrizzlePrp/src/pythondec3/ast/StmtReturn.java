/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;

public class StmtReturn extends Stmt
{
    Expr returnvalue;

    public StmtReturn(Ast returnvalue/*, Object token */)
    {
        //returnvalue is the value returned by the return statement.
        //super();
        this.returnvalue = (Expr)returnvalue;
    }

    public void gen2(sgen s)
    {
        //if(s.hasparent(Stmt.))
        //if(s.isInFunction())
        //{
            s.indent();
            //we should check if the returnvalue is None, and then just print return, but it doesn't matter.
            s.out("return");
            if(!returnvalue.isNoneConst())
            {
                s.out(" ");
                returnvalue.gen(s);
            }
            s.endline();
        //}
        //else
        //{
            //return statements inside a module return None, and return statements inside a class return the locals.
            //but them being in the generated source will break compilation.
        //}
    }
}

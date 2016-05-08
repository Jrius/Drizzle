/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.List;
import shared.m;

public class ExprList extends Ast
{
    //this is used especially for my fake token for lists of expressions.

    Tok tok; //EXPR_LIST token.
    public ExprList(Tok tok)
    {
        this.tok = tok;
    }
    public void gen2(sgen s)
    {
        m.throwUncaughtException("unexpected");
        /*java.util.List<Ast> list = (java.util.List<Ast>)tok.oi.pattr;
        for(int i=0;i<list.size();i++)
        {
            if(i!=0) s.out(",");
            Ast ast = list.get(i);
            ast.gen(s);
        }*/
    }
    public Ast[] getvals()
    {
        java.util.List<Ast> list = (java.util.List<Ast>)tok.oi.pattr;
        Ast[] r = list.toArray(new Ast[]{});
        return r;
    }
}

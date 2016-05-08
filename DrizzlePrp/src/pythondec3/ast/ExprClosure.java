/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

/**
 *
 * @author user
 */
public class ExprClosure extends Expr.Makefunction
{
    //Tok loadconsttoken; //already in Expr.Makefunction
    //List<Ast> exprs = new List(); //already in Expr.Makefunction
    List<Ast> closures = new List();

    public ExprClosure(Tok loadconst)
    {
        super(loadconst);
        //this.loadconst = loadconst;
    }
    public Ast addClosure(Tok closure)
    {
        closures.add(closure);
        return this;
    }
    public Ast addExpr(Ast expr)
    {
        exprs.add(expr);
        return this;
    }
    public void gen2(sgen s)
    {
        this.genAsExpr(s, "lambda");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
import shared.m;

public class StmtAssign extends Stmt
{
    Ast value;
    List destinations;

    boolean isfunction;

    public StmtAssign(Ast value, Ast destinations )
    {
        //saves value into each variable in destinations.  If there are multiple destinations, then value should be a tuple of the same size.
        super();
        this.value = value;
        this.destinations = (List)destinations;
    }

    public void gen2(sgen s)
    {
        if(value instanceof Expr.Makefunction)
        {
            Expr.Makefunction mkfuncexpr = (Expr.Makefunction)value;

            if(mkfuncexpr.endsWithReturnNone())
            {
                //regular function
                String functionname = destinations.getGenString();
                //if(functionname.equals("clear_break"))
                //{
                //    int dummy=0;
                //}

                isfunction = true;
                if(destinations.size()!=1)
                    m.throwUncaughtException("unhandled");

                s.endline(); //space before
                s.indent();
                s.out("def ");
                //destinations.gen(s);//desig.gen(s);
                String funcname = destinations.getGenString();
                //funcname = pythondec3.helpers.demangleName(s,funcname);
                s.out(funcname);
                //s.out("(");
                /*for(int i=0;i<mkfuncexpr.exprs.size();i++)
                {
                    Ast expr = mkfuncexpr.exprs.get(i);
                    if(i!=0) s.out(",");
                    expr.gen(s);
                }*/
                //s.out("):");
                //s.endline();

                //s.increaseindentation();
                //mkfuncexpr.gen(s); //is this right?  it depends on what a Expr.Mkfunc looks like on it's own.
                mkfuncexpr.genAsFunction(s,funcname);
                //s.decreaseindentation();
                s.endline(); //space after
                return;
            }
            //otherwise it's a lambda
        }

        s.indent();
        //if(destinations.size()>1)
        //{
            //m.throwUncaughtException("unhandled");
            for(int i=0;i<destinations.size();i++)
            {
                Ast dest = destinations.get(i);
                dest.gen(s);
                s.out(" = ");
            }
            value.gen(s);
        //}
        //else
        //{
        //    destinations.get(0).gen(s);
        //    s.out(" = ");
        //    value.gen(s);
        //}
        s.endline();
    }
    
    public String toString()
    {
        if(isfunction) return "function: "+super.toString();
        else return super.toString();
    }

}

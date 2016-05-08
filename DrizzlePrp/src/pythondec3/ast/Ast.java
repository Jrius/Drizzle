/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

//import pythondec2.*;
//import beaver.Symbol;
import shared.m;
import java.util.ArrayList;
import pythondec.*;

public class Ast //implements lpg.runtime.IAst
{
    boolean hasparens = false;
    //String name;
    //Symbol[] children;
    public Ast()
    {
        super();
    }
    /*public Node(String name, Symbol... children)
    {
        super();
        this.name = name;
        this.children = children;
    }*/
    public boolean isimportant()
    {
        return false;
    }
    final public void gen(sgen s)
    {
        s.visit(this);
        gen2(s);
        s.leave();
    }
    public void gen2(sgen s)
    {
        m.throwUncaughtException("Unimplemented gen2 in Symbol: "+this.getClass().toString());
    }

    //protected ArrayList<Ast> getList()
    //{
    //    throw new shared.uncaughtexception("No list in Symbol: "+this.getClass().toString());
    //}
    /*public void add(Ast... items)
    {
        ArrayList<Ast> list = getList();
        for(Ast item: items)
        {
            list.add(item);
        }
    }*/
    public String getGenString()
    {
        sgen s2 = new sgen();
        this.gen(s2);
        String r = s2.getGeneratedSource();
        return r;
    }
    public Ast add(Ast item)
    {
        //throw new shared.uncaughtexception("Object is not a list: "+this.getClass().toString());
        if(this instanceof IList)
        {
            ((IList)this).getlist().add(item);
            return this;
        }
        else
        {
            throw new shared.uncaughtexception("Object is not a list: "+this.getClass().toString());
        }
    }
    //public List aslist()
    //{
    //    List r = (List)this;
    //    return r;
    //}
    public boolean isNumericConst()
    {
        if(this instanceof Expr.Loadconst)
        {
            Expr.Loadconst lc = (Expr.Loadconst)this;
            return lc.token.isNumericConst();
        }
        if(this instanceof Tok)
        {
            Tok tok = (Tok)this;
            if(tok.oi.o==op.LOAD_CONST)
            {
                if(    tok.oi.pattr instanceof PyInt
                    || tok.oi.pattr instanceof PyLong
                    || tok.oi.pattr instanceof PyInt64
                    || tok.oi.pattr instanceof PyFloat
                    || tok.oi.pattr instanceof PyComplex)
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void genWithParentheses(sgen s)
    {
        if(!hasparens) s.out("(");
        this.gen(s);
        if(!hasparens) s.out(")");
    }
}

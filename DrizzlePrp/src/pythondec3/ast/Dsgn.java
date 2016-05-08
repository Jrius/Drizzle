/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
import pythondec.Disassemble.OpInfo;

public class Dsgn extends Ast
{

    //we should probably use the classes like Attr, Subscr, Name, instead of Storefast, Storename, etc.

    public Dsgn()
    {
    }
    public String toString()
    {
        return this.getGenString();
    }

    public static class Name extends Dsgn
    {
        Ast name;
        public Name(Ast name)
        {
            this.name = name;
        }
        public void gen2(sgen s)
        {
            name.gen(s);
        }
    }
    public static class Attr extends Dsgn
    {
        Ast obj;
        Ast attrname;

        public Attr(Ast obj, Ast attrname)
        {
            this.obj = obj;
            this.attrname = attrname;
        }
        public void gen2(sgen s)
        {
            obj.gen(s);
            s.out(".");
            attrname.gen(s);
        }
    }
    public static class Subscr extends Dsgn
    {
        Ast obj;
        Ast subscrindex;

        public Subscr(Ast obj, Ast subscrindex)
        {
            this.obj = obj;
            this.subscrindex = subscrindex;
        }
        public void gen2(sgen s)
        {
            obj.gen(s);
            s.out("[");
            if(subscrindex instanceof Expr.Buildtuple)
            {
                //ugly hack! But even if they want to pass a tuple, this is the exact same.
                Expr.Buildtuple bt2 = (Expr.Buildtuple)subscrindex;
                bt2.gen2NoParentheses(s);
            }
            else
            {
                subscrindex.gen(s);
            }
            s.out("]");
        }
    }
    public static class Slice extends Dsgn
    {
        Ast obj;
        Ast left;
        Ast right;

        public Slice(Ast obj, Ast left, Ast right)
        {
            this.obj = obj;
            this.left = left;
            this.right = right;
        }
        public void gen2(sgen s)
        {
            obj.gen(s);
            s.out("[");
            if(left!=null) left.gen(s);
            s.out(":");
            if(right!=null) right.gen(s);
            s.out("]");
        }
    }



    public static class UnpackSequence extends Dsgn implements IList
    {
        List<Dsgn> desigs = new List();
        public UnpackSequence()
        {
        }
        public List getlist()
        {
            return desigs;
        }
        public void gen2(sgen s)
        {
            s.out("(");
            for(int i=0;i<desigs.size();i++)
            {
                Ast desig = desigs.get(i);
                if(i!=0) s.out(", ");
                desig.gen(s);
            }
            if(desigs.size()==1) s.out(","); //even a singleton tuple needs the comma or it will treat it as a non-tuple expression.
            s.out(")");
        }
    }


    /*public static class Storesubscript extends Dsgn
    {
        Ast expr1;
        Ast expr2;

        public Storesubscript(Ast expr1, Ast expr2)
        {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }

        public void gen2(sgen s)
        {
            expr1.gen(s);
            s.out("[");
            if(expr2 instanceof Expr.Buildtuple)
            {
                //ugly hack!
                Expr.Buildtuple bt2 = (Expr.Buildtuple)expr2;
                bt2.gen2NoParentheses(s);
            }
            else
            {
                expr2.gen(s);
            }
            s.out("]");
        }
    }*/

    public static class Storefast extends Dsgn
    {
        Tok token;
        public Storefast(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String varname = token.getName(s);
            s.out(varname);
        }
    }
    public static class Storeglobal extends Dsgn
    {
        Tok token;
        public Storeglobal(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String varname = token.getName(s);
            s.out(varname);
        }
    }

    public static class Storename extends Dsgn
    {
        //token is the STORE_NAME token.
        Tok token;
        public Storename(Ast token)
        {
            super();
            this.token = (Tok)token;
        }
        public void gen2(sgen s)
        {
            String varname = token.getName(s);
            s.out(varname);
        }
    }

    public static class Storeattr extends Dsgn
    {
        //stores the value in obj.token  (the value is given in the assignment statement.)
        Ast obj;
        Tok token;

        public Storeattr(Ast obj, Tok token)
        {
            this.obj = obj;
            this.token = token;
        }
        public void gen2(sgen s)
        {
            obj.gen(s);
            String name = token.getName(s);
            s.out(".");
            s.out(name);
        }
    }
}

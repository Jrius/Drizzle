/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
import pythondec.*;
import shared.m;

//public interface Expr extends IAst
public class Expr extends Ast
{
    //public static class Expra extends Ast implements Expr
    //{
    //    public boolean isNoneConst()
    //    {
    //        return false;
    //    }
    //}
    //public Expr()
    //{
    //}

    //public boolean isNoneConst();
    public boolean isNoneConst()
    {
        return false;
    }

    public String toString()
    {
        return this.getGenString();
    }

    public static class Buildslice extends Expr
    {
        Expr a1;
        Expr a2;
        Expr a3;
        public Buildslice(Ast a1, Ast a2, Ast a3)
        {
            this.a1 = (Expr)a1;
            this.a2 = (Expr)a2;
            this.a3 = (Expr)a3;
        }
        public void gen2(sgen s)
        {
            if(a3!=null)
            {
                if(!a1.isNoneConst())a1.gen(s);
                s.out(":");
                if(!a2.isNoneConst())a2.gen(s);
                s.out(":");
                if(!a3.isNoneConst())a3.gen(s);
            }
            else
            {
                if(!a1.isNoneConst())a1.gen(s);
                s.out(":");
                if(!a2.isNoneConst())a2.gen(s);
            }
        }
    }

    public static class Listfor extends Expr implements IList
    {
        List<Ast> iters = new List();
        Tok tok; //contains the codename for this anonymous object, e.g. "_[1]"
        Ast expr;
        Tok s1;
        Ast s2;
        Ast s3;
        public List getlist(){return iters;}
        public Listfor(Tok tok, Ast expr)
        {
            this.tok = tok;
            this.expr = expr;
        }
        public void gen2(sgen s)
        {
            s.out("[ ");
            expr.gen(s);
            for(int i=0;i<iters.size();i++)
            {
                Ast ast = iters.get(iters.size()-i-1);
                ast.gen(s);
            }
            s.out(" ]");
        }
        public Ast setInfo(Tok s1, Ast s2, Ast s3)
        {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            return this;
        }
        public static class If extends Ast
        {
            Ast expr;
            Ast ifcmd;
            public If(Ast expr, Ast ifcmd)
            {
                this.expr = expr;
                this.ifcmd = ifcmd;
            }
            public void gen2(sgen s)
            {
                //m.cancel("Haven't implemented this yet.");
                //this is untested:
                s.out(" if ");
                expr.gen(s);
            }
        }
        public static class For extends Ast
        {
            Ast expr;
            Ast looper;
            Ast desig;
            public For(Ast expr, Ast looper, Ast desig)
            {
                this.expr = expr;
                this.looper = looper;
                this.desig = desig;
            }
            public void gen2(sgen s)
            {
                s.out(" for ");
                desig.gen(s);
                s.out(" in ");
                expr.gen(s);
            }
        }
    }

    public static class Complist extends Expr implements IList
    {
        List<Comp> comparisons = new List();
        public List getlist(){return comparisons;}
        public Complist()
        {
        }
        public void gen2(sgen s)
        {
            for(int i=0;i<comparisons.size();i++)
            {
                Comp comp = comparisons.get(comparisons.size()-i-1);
                if(i!=0) //this element has no comparison operator as it's the first element.
                {
                    String cmpstr = (String)comp.compareop.oi.pattr;
                    s.out(" "+cmpstr+" ");
                }
                comp.expr.gen(s);
            }
        }
        public static class Comp extends Ast
        {
            Ast expr;
            Tok compareop;
            public Comp(Ast expr, Tok compareop)
            {
                this.expr = expr;
                this.compareop = compareop;
            }
        }
    }

    public static class Slice extends Expr
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


    public static class ShortcircuitAnd extends Expr
    {
        Ast expr1;
        Ast expr2;
        public ShortcircuitAnd(Ast expr1, Ast expr2)
        {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }
        public void gen2(sgen s)
        {
            s.out("(");
            if(expr1==null) s.out("1"); //might have to be 1 and not True, because that was true of the optimised While statement too.
            else expr1.gen(s);
            s.out(" and ");
            expr2.gen(s);
            s.out(")");
        }
    }

    public static class ShortcircuitOr extends Expr
    {
        Ast expr1;
        Ast expr2;
        public ShortcircuitOr(Ast expr1, Ast expr2)
        {
            this.expr1 = expr1;
            this.expr2 = expr2;
        }
        public void gen2(sgen s)
        {
            s.out("(");
            expr1.gen(s);
            s.out(" or ");
            expr2.gen(s);
            s.out(")");
        }
    }
    public static class Dictionary extends Expr
    {
        List entries;

        public Dictionary(Ast entries)
        {
            this.entries = (List)entries;
        }

        public void gen2(sgen s)
        {
            s.out("{");
            if(!pythondec3.options.breakDictionariesOntoMultipleLines)
            {
                for(int i=0;i<entries.size();i++)
                {
                    if(i!=0) s.out(", ");
                    entries.get(i).gen(s);
                }
            }
            else if (entries.size() > 0)
            {
                s.endline();
                s.increaseindentation();
                for(int i=0;i<entries.size();i++)
                {
                    s.indent();
                    entries.get(i).gen(s);
                    if(i<entries.size()-1) //if not the last item
                    {
                        s.out(",");
                    }
                    s.endline();
                }
                s.decreaseindentation();
                s.indent();
            }
            s.out("}");
        }

        public static class Entry extends Ast
        {
            Ast left;
            Ast right;

            public Entry(Ast right, Ast left)
            {
                this.left = left;
                this.right = right;
            }

            public void gen2(sgen s)
            {
                left.gen(s);
                s.out(": ");
                right.gen(s);
            }
        }
    }

    public static class Compare extends Expr
    {
        Ast expr1;
        Ast expr2;
        Tok compareop;

        public Compare(Ast expr1, Ast expr2, Tok compareop)
        {
            this.expr1 = expr1;
            this.expr2 = expr2;
            this.compareop = compareop;
        }
        public void gen2(sgen s)
        {
            s.out("(");
            expr1.gen(s);
            String compstr = (String)compareop.oi.pattr;
            s.out(" "+compstr+" ");
            expr2.gen(s);
            s.out(")");
        }
    }

    public static class Loadname extends Expr
    {
        //token is the LOAD_NAME token.
        Tok token;
        
        public Loadname(Tok token)
        {
            super();
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String varname = token.getName(s);
            s.out(varname);
        }
    }

    public static class Loadfast extends Expr
    {
        Tok token;

        public Loadfast(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String name = token.getName(s);
            s.out(name);
        }
    }
    public static class Loadattr extends Expr
    {
        //returns expr.token
        Ast expr;
        Tok token;

        public Loadattr(Ast expr, Tok token)
        {
            this.expr = expr;
            this.token = token;
        }
        public void gen2(sgen s)
        {
            if(expr.isNumericConst())
                expr.genWithParentheses(s);
            else
                expr.gen(s);
            s.out(".");
            String name = token.getName(s);
            s.out(name);
        }
    }
    public static class Loadconst extends Expr
    {
        //token is the LOAD_CONST token.
        Tok token;

        public Loadconst(Ast token)
        {
            super();
            this.token = (Tok)token;
        }
        public void gen2(sgen s)
        {
            //String varname = token.oi.getNameQuoted();
            //String varname = token.getName(s);
            //String varname = pythondec3.helpers.escapePythonString((PyString)token.oi.pattr);
            String varname = token.getNameQuoted(s);
            s.out(varname);
        }
        public boolean isNoneConst()
        {
            Tok t = (Tok)token;
            if(t.oi.pattr instanceof pythondec.PyNone) return true;
            return false;
        }

    }

    public static class Loadglobal extends Expr
    {
        Tok token;

        public Loadglobal(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String name = token.getName(s);
            s.out(name);
        }
    }

    public static class Name extends Expr
    {
        //could replace LoadGlobal and Loadfast and Loadname, which are specific to their tokens.
        Tok token;

        public Name(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            String name = token.getName(s);
            s.out(name);
        }
    }

    public static class Loadlocals extends Expr
    {
        //used in a class definition, since the locals are pushed on the stack and passed along.
        Tok token;

        public Loadlocals(Tok token)
        {
            this.token = token;
        }
        public void gen2(sgen s)
        {
            //I think we can safely ignore this.  It seems to be returned from the function that creates the class.  So it should be the return at the end of the PyCode for the class.
        }
    }
    public static class Unary extends Expr
    {
        Ast expr;
        Tok unop_token;

        public Unary(Ast expr, Tok unop_token)
        {
            this.expr = expr;
            this.unop_token = unop_token;
        }

        public void gen2(sgen s)
        {
            s.out("(");
            String opstr = getOpStr();
            s.out(opstr);
            //s.out(" ");
            expr.genWithParentheses(s); //needs to have parentheses or these might be misinterpreted as -5 instead of Unary_negative(5)
            String opstrr = getOpStrRight();
            s.out(opstrr);
            s.out(")");
        }

        public String getOpStr()
        {
            switch(unop_token.oi.o)
            {
                case UNARY_NOT:
                    return "not ";
                case UNARY_CONVERT:
                    return "`";
                case UNARY_NEGATIVE:
                    return "-";
                case UNARY_INVERT:
                    return "~";
                case UNARY_POSITIVE:
                    return "+";
                default:
                    throw new shared.uncaughtexception("unhandled");
            }
        }
        public String getOpStrRight()
        {
            switch(unop_token.oi.o)
            {
                case UNARY_CONVERT:
                    return "`";
                default:
                    return "";
            }
        }
    }

    public static class Binary extends Expr
    {
        //e.g. 2+3 -> left=2 right=3 binop_token=BINARY_ADD
        Expr left;
        Expr right;
        Tok binop_token;

        public Binary(Ast left, Ast right, Ast binop_token)
        {
            super();
            this.left = (Expr)left;
            this.right = (Expr)right;
            this.binop_token = (Tok)binop_token;
        }
        public void gen2(sgen s)
        {
            boolean parens = (binop_token.oi.o==op.BINARY_POWER); //hack because ** has higher precedence than -

            s.out("(");
            if(parens)left.genWithParentheses(s); else left.gen(s);
            s.out(" ");
            String opstr = getOpStr();
            s.out(opstr);
            s.out(" ");
            if(parens)right.genWithParentheses(s); else right.gen(s);
            s.out(")");
        }
        public String getOpStr()
        {
            switch(binop_token.oi.o)
            {
                case BINARY_ADD:
                    return "+";
                case BINARY_SUBTRACT:
                    return "-";
                case BINARY_MULTIPLY:
                    return "*";
                case BINARY_DIVIDE:
                    return "/";
                case BINARY_TRUE_DIVIDE:
                    return "/";
                case BINARY_FLOOR_DIVIDE:
                    return "//";
                case BINARY_MODULO:
                    return "%"; //was "%%", but that appears to be wrong.
                case BINARY_LSHIFT:
                    return "<<";
                case BINARY_RSHIFT:
                    return ">>";
                case BINARY_AND:
                    return "&";
                case BINARY_OR:
                    return "|";
                case BINARY_XOR:
                    return "^";
                case BINARY_POWER:
                    return "**";
                //case BINARY_VALUE:
                default:
                    throw new shared.uncaughtexception("unexpected token.");
            }
        }
    }

    public static class Binarysubscript extends Expr
    {
        Ast expr1;
        Ast expr2;

        public Binarysubscript(Ast expr1, Ast expr2)
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
                //ugly hack! But even if they want to pass a tuple, this is the exact same.
                Expr.Buildtuple bt2 = (Expr.Buildtuple)expr2;
                bt2.gen2NoParentheses(s);
            }
            else
            {
                expr2.gen(s);
            }
            s.out("]");
        }
    }

    //public static class Buildlist extends Expr implements IList
    public static class Buildlist extends Expr implements IList
    {

        public Buildlist(Ast... items)
        {
            for(Ast item: items)
                exprs.add(item);
        }
        /*ArrayList<Ast> exprs = new ArrayList();

        public Buildlist(Ast... exprs2)
        {
            for(Ast expr: exprs2)
            {
                exprs.add(expr);
            }
        }

        public void gen2(sgen s)
        {
            s.out("[");
            for(int i=0;i<exprs.size();i++)
            {
                Ast expr = exprs.get(i);
                if(i!=0) s.out(", ");
                expr.gen(s);
            }
            s.out("]");
        }*/
        List exprs = new List();
        public List getlist(){return exprs;}
        public void gen2(sgen s)
        {
            s.out("[");
            for(int i=0;i<exprs.size();i++)
            {
                Ast expr = exprs.get(exprs.size()-i-1);
                if(i!=0) s.out(", ");
                expr.gen(s);
            }
            s.out("]");
        }
    }
    //public static class Buildtuple extends Expr
    public static class Buildtuple extends Expr implements IList
    {
        //ArrayList<Ast> exprs = new ArrayList();

        public Buildtuple(Ast... newexprs)
        {
            for(Ast expr: newexprs)
            {
                exprs.add(expr);
            }
        }
        /*public void gen2(sgen s)
        {
            s.out("(");
            for(int i=0;i<exprs.size();i++)
            {
                Ast expr = exprs.get(i);
                if(i!=0) s.out(", ");
                expr.gen(s);
            }
            s.out(")");
        }*/
        List exprs = new List();
        public List getlist(){return exprs;}
        public void gen2(sgen s)
        {
            s.out("(");
            gen2NoParentheses(s);
            s.out(")");
        }
        public void gen2NoParentheses(sgen s)
        {
            for(int i=0;i<exprs.size();i++)
            {
                Ast expr = exprs.get(exprs.size()-i-1);
                if(i!=0) s.out(", ");
                expr.gen(s);
            }
            if(exprs.size()==1) s.out(","); //even a singleton tuple needs the comma or it will treat it as a non-tuple expression.
        }
    }
    //public static class Makefunction extends Expr
    public static class Makefunction extends Expr implements IList
    {
        /*Tok loadconsttoken; //code
        ArrayList<Ast> exprs = new ArrayList(); //arguments to the function

        public Makefunction(Tok loadconst, Ast... exprs2)
        {
            loadconsttoken = loadconst;
            for(Ast expr: exprs2)
            {
                exprs.add(expr);
            }
        }
        public void genAsClass(sgen s)
        {
            PyCode code = (PyCode)loadconsttoken.oi.pattr;
            code.root.gen(s);
        }
        public void genAsFunction(sgen s)
        {
            PyCode code = (PyCode)loadconsttoken.oi.pattr;

            s.out("(");
            for(int i=0;i<code.varnames.items.length;i++)
            {
                if(i!=0) s.out(", ");
                PyString varname2 = (PyString)code.varnames.items[i];
                String varname = varname2.toJavaString();
                s.out(varname);
            }
            s.out("):");
            s.endline();

            s.increaseindentation();
            code.root.gen(s);
            s.decreaseindentation();

        }*/
        Tok loadconsttoken; //code
        List exprs = new List();
        public List getlist(){return exprs;}

        public Makefunction(Tok loadconst)
        {
            this.hasparens = true;
            loadconsttoken = loadconst;
        }
        public boolean endsWithReturnNone()
        {
            PyCode code = (PyCode)loadconsttoken.oi.pattr;
            Stmt stmt = code.root.get(code.root.size()-1);
            if(stmt instanceof StmtReturn)
            {
                StmtReturn ret = (StmtReturn)stmt;
                if(ret.returnvalue.isNoneConst())
                {
                    return true;
                }
            }
            return false;
        }
        public void genAsClass(sgen s, String name)
        {
            PyCode code = (PyCode)loadconsttoken.oi.pattr;
            code.debugname = name;

            if(!pythondec3.options.writeLoadGlobals)
            {
                if(code.globals.size()!=0)
                    m.throwUncaughtException("unhandled");
            }

            //remove the first line if it is __module__ = __name__, as this is automatically inserted by Python.
            if(code.root.size()>0)
            {
                Stmt stmt = code.root.get(0);
                if(stmt instanceof StmtAssign)
                {
                    StmtAssign stmt2 = (StmtAssign)stmt;
                    if(stmt2.destinations.getGenString().equals("__module__") && stmt2.value.getGenString().equals("__name__"))
                    {
                        code.root.remove(stmt);
                    }
                }
            }

            //remove extraneous return statements:
            //should be moved to reading, but meh
            if(code.root.size()>0)
            {
                Stmt laststmt = code.root.get(code.root.size()-1);
                if(laststmt instanceof StmtReturn)
                {
                    StmtReturn laststmt2 = (StmtReturn)laststmt;
                    if(laststmt2.returnvalue instanceof Expr.Loadlocals)
                    {
                        code.root.remove(laststmt);

                    }
                }
            }

            code.root.gen(s);

        }
        public void genArgumentList(sgen s, PyCode code)
        {
            //looks like: def funcname(nodefault1, nodefault2, default3,default4,*varargs,*varkeywords):

            int numNonDefaults = code.argcount-exprs.size();

            int curarg = 0;
            for(int i=0;i<code.argcount;i++)
            {
                if(curarg!=0) s.out(", ");
                PyString varname2 = (PyString)code.varnames.items[i];
                String varname = varname2.toJavaString();
                if(varname.startsWith("."))
                {
                    //this is an anonymous tuple; (names are ".4", ".1", etc.)
                    //first thing that should happen in the disassembled code is an unpacking.
                    handleAnonTuple(s,code,varname);
                }
                else
                {
                    s.out(varname);

                    //if(exprs.size()!=0)
                    //{
                    //    int dummy2=0;
                    //}
                }

                if(curarg>=numNonDefaults)
                {
                    int curDefaultArg = curarg-numNonDefaults;
                    Ast defaultval = exprs.get(exprs.size()-curDefaultArg-1);
                    //if(defaultval!=null)
                    //{
                    s.out(" = ");
                    defaultval.gen(s);
                    //}
                }

                curarg++;
            }

            //varargs
            if(code.flag_varargs)
            {
                if(curarg!=0)s.out(", ");
                s.out("*");
                PyString varargsname = (PyString)code.varnames.items[curarg];
                s.out(varargsname.toJavaString());
                curarg++;
            }

            //varkeywords
            if(code.flag_varkeywords)
            {
                if(curarg!=0)s.out(", ");
                s.out("**");
                PyString varargsname = (PyString)code.varnames.items[curarg];
                s.out(varargsname.toJavaString());
                curarg++;
            }


        }
        public void genAsFunction(sgen s, String debugname)
        {
            //looks like: def funcname(nodefault1, nodefault2, default3,default4,*varargs,*varkeywords):
            //and

            PyCode code = (PyCode)loadconsttoken.oi.pattr;
            code.debugname = debugname;
            int numNonDefaults = code.argcount-exprs.size();

            s.out("(");

            genArgumentList(s, code);

            s.out("):");
            s.endline();

            s.increaseindentation();
            for(String global: code.globals)
            {
                s.indent(); s.out("global "); s.out(global); s.endline();
            }

            //remove extraneous return statements:
            //should be moved to reading, but meh
            if(code.root.size()>0)
            {
                Stmt laststmt = code.root.get(code.root.size()-1);
                if(laststmt instanceof StmtReturn)
                {
                    StmtReturn laststmt2 = (StmtReturn)laststmt;
                    if(laststmt2.returnvalue.isNoneConst())
                    {
                        code.root.remove(laststmt);

                    }
                }
            }

            //this is now done in StmtList:
            //if(code.root.size()==0)
            //{
            //    s.indent();s.out("pass");s.endline();
            //}
            
            code.root.gen(s);
            s.decreaseindentation();

        }
        public void genAsExpr(sgen s, String name)
        {
            //lambda

            //if(exprs.size()!=0)
            //    m.throwUncaughtException("unhandled");

            PyCode code = (PyCode)loadconsttoken.oi.pattr;
            code.debugname = name;

            //it should be just a return statement I think.
            //if(code.root.size()!=1)
            //    m.throwUncaughtException("unhandled");
            Ast retstmt = code.root.get(code.root.size()-1);
            if(!(retstmt instanceof StmtReturn))
                m.throwUncaughtException("unhandled");
            //if(code.flag_varargs || code.flag_varkeywords)
            //    m.throwUncaughtException("unhandled");
            StmtReturn ret = (StmtReturn)retstmt;


            s.out("(");
            s.out("lambda ");

            /*for(int i=0;i<code.argcount;i++)
            {
                if(i!=0) s.out(",");
                String argname = ((PyString)code.varnames.items[i]).toJavaString();
                if(argname.startsWith("."))
                {
                    handleAnonTuple(s,code,argname);
                }

                s.out(argname);
            }*/
            genArgumentList(s, code);

            s.out(": ");
            ret.returnvalue.gen(s);
            s.out(")");

            //moved down here as the handleAnonTuple will delete any anonymous tuples it uses.
            if(code.root.size()!=1)
                m.throwUncaughtException("unhandled");
        }
        public static void handleAnonTuple(sgen s, PyCode code, String varname)
        {
            int curseq = 0;
            Ast ast = code.root.get(curseq);
            while(ast instanceof StmtAssign)
            {
                StmtAssign asgn = (StmtAssign)ast;
                String name = asgn.value.getGenString();
                if(name.equals(varname))
                {
                    if(asgn.destinations.size()!=1)
                        m.throwUncaughtException("unhandled");

                    asgn.destinations.gen(s);
                    code.root.remove(asgn); //not clean, as we have to reparse to use this tree again, but that should be the only use-case anyway.
                    curseq = -1;
                    break;
                }
                curseq++;
                ast = code.root.get(curseq);
            }
            if(curseq!=-1)
                m.throwUncaughtException("unhandled"); //unfound
        }
        public void gen2(sgen s)
        {
            //if treated as an expression, it's a lambda.
            genAsExpr(s,"lambda");
        }
    }
    public static class Callfunction extends Expr
    {
        Ast expr;
        ArrayList<Ast> positionals = new ArrayList();
        ArrayList<Ast> keywords = new ArrayList();
        Ast var;
        Ast kw;

        public Callfunction(Ast expr, Ast[] positional, Ast[] keyword, Ast var, Ast kw)
        {
            this.expr = expr;
            for(Ast pos: positional) positionals.add(pos);
            for(Ast key: keyword) keywords.add(key);
            this.var = var;
            this.kw = kw;
        }

        public Callfunction(Ast var, Ast kw)
        {
            this.var = var;
            this.kw = kw;
        }
        public Ast setName(Ast expr)
        {
            this.expr = expr;
            return this;
        }
        public Ast addKwarg(Ast kwarg)
        {
            keywords.add(kwarg);
            return this;
        }
        public Ast addPoarg(Ast poarg)
        {
            positionals.add(poarg);
            return this;
        }
        public void gen2(sgen s)
        {
            //if(var!=null)
            //    m.throwUncaughtException("unhandled");
            //if(kw!=null)
            //    m.throwUncaughtException("unhandled");
            //if(keywords.size()!=0)
            //    m.throwUncaughtException("unhandled");

            //expr.gen(s);
            String funcname = expr.getGenString();
            //funcname = pythondec3.helpers.demangleName(s,funcname);
            s.out(funcname);
            s.out("(");
            int numarg = 0;
            //final String comma = ","; //Drizzle24
            final String comma = ", "; //Drizzle25
            for(int i=0;i<positionals.size();i++)
            {
                Ast positional = positionals.get(positionals.size()-i-1);
                if(numarg!=0) s.out(comma);
                positional.gen(s);
                numarg++;
            }
            for(int i=0;i<keywords.size();i++)
            {
                Ast keyword = keywords.get(keywords.size()-i-1);
                if(numarg!=0) s.out(comma);
                keyword.gen(s);
                numarg++;
            }
            if(var!=null)
            {
                if(numarg!=0) s.out(comma);
                s.out("*");
                var.gen(s);
                numarg++;
            }
            if(kw!=null)
            {
                if(numarg!=0) s.out(",");
                s.out("**");
                kw.gen(s);
                numarg++;
            }
            s.out(")");
        }
    }

}

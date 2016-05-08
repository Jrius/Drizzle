/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;
import pythondec.op;
import pythondec.*;

import shared.m;

public abstract class Stmt extends Ast
{
    public Stmt()
    {
        super();
    }

    public static class Assert extends Stmt
    {
        Ast s1;
        Ast s2;
        Tok s3;
        Ast s4;
        public Assert(Ast s1, Ast s2, Tok s3, Ast s4)
        {
            this.s1 = s1;
            this.s2 = s2;
            this.s3 = s3;
            this.s4 = s4;
        }
        public void gen2(sgen s)
        {
            //if(!s1.getGenString().equals("__debug__"))
            //    m.throwUncaughtException("unexpected");
            //if(!s3.getGenString().equals("AssertionError"))
            //    m.throwUncaughtException("unexpected");

            s.indent();
            s.out("assert ");
            s2.gen(s);
            if(s4!=null)
            {
                s.out(", ");
                s4.gen(s);
            }
            s.endline();
        }
    }

    public static class Yield extends Stmt
    {
        Ast expr;
        public Yield(Ast expr)
        {
            this.expr = expr;
        }
        public void gen2(sgen s)
        {
            s.indent();
            s.out("yield ");
            expr.gen(s);
            s.endline();
        }
    }

    public static class Exec extends Stmt
    {
        Ast s1;
        Expr s2;
        Expr s3;
        public Exec(Ast s1, Ast s2, Ast s3)
        {
            this.s1 = s1;
            this.s2 = (Expr)s2;
            this.s3 = (Expr)s3;
        }
        public void gen2(sgen s)
        {
            s.indent();
            s.out("exec ");
            s1.gen(s);
            if(s2!=null && !s2.isNoneConst())
            {
                s.out(" in ");
                s2.gen(s);
            }
            if(s3!=null && !s3.isNoneConst())
            {
                s.out(", ");
                s3.gen(s);
            }
            s.endline();
        }
    }

    public static class Raisevarargs extends Stmt implements IList
    {
        List args = new List();
        public Raisevarargs()
        {
        }
        public List getlist(){return args;}
        public void gen2(sgen s)
        {
            s.indent();
            s.out("raise ");
            for(int i=0;i<args.size();i++)
            {
                if(i!=0) s.out(", ");
                Ast expr = args.get(args.size()-i-1);
                expr.gen(s);
            }
            s.endline();
        }
    }

    public static class Inplace extends Stmt
    {
        Dsgn desig;
        Ast expr;
        Tok inplacetok;

        public Inplace(Dsgn desig, Ast expr, Ast inplacetok)
        {
            this.desig = desig;
            this.expr = expr;
            this.inplacetok = (Tok)inplacetok;
        }
        public void gen2(sgen s)
        {
            s.indent();
            desig.gen(s);
            s.out(" ");
            String opstr = getInplaceStr();
            s.out(opstr);
            s.out(" ");
            expr.gen(s);
            s.endline();
        }
        public String getInplaceStr()
        {
            switch(inplacetok.oi.o)
            {
                case INPLACE_ADD:
                    return "+=";
                case INPLACE_SUBTRACT:
                    return "-=";
                case INPLACE_OR:
                    return "|=";
                case INPLACE_AND:
                    return "&=";
                case INPLACE_DIVIDE:
                    return "/=";
                case INPLACE_MULTIPLY:
                    return "*=";
                case INPLACE_POWER:
                    return "**=";
                case INPLACE_FLOOR_DIVIDE:
                    return "//=";
                case INPLACE_MODULO:
                    return "%=";
                case INPLACE_XOR:
                    return "^=";
                case INPLACE_RSHIFT:
                    return ">>=";
                case INPLACE_LSHIFT:
                    return "<<=";
                default:
                    throw new shared.uncaughtexception("unhandled");
            }
        }
        public Inplace(Ast s1, Ast s2, Ast s3, Ast s4)
        {
            desig = (Dsgn)s4;
            inplacetok = (Tok)s3;
            expr = s2;

            if(!s1.getGenString().equals(desig.getGenString()))
                m.throwUncaughtException("unexpected/unhandled");
        }

    }

    public static class Exprstmt extends Stmt
    {
        Ast expr;

        public Exprstmt(Ast expr)
        {
            this.expr = expr;
        }
        public void gen2(sgen s)
        {
            s.indent();
            expr.gen(s);
            s.endline();
        }
    }

    public static class Del extends Stmt
    {
        Dsgn desig;
        public Del(Ast desig)
        {
            this.desig = (Dsgn)desig;
        }
        public void gen2(sgen s)
        {
            s.indent();
            s.out("del ");
            //String name = token.oi.getName();
            //s.out(name);
            desig.gen(s);
            s.endline();
        }
    }

    public static class Import extends Stmt
    {
        Tok loadconst;
        Tok importname;
        Ast importer;
        List importerlist;
        int type;

        public Import(Tok loadconst, Tok importname, Ast importer, Ast importerlist, int type)
        {
            this.loadconst = loadconst;
            this.importname = importname;
            this.importer = importer;
            this.importerlist = (List)importerlist;
            this.type = type;
        }
        public void gen2(sgen s)
        {
            if(type==1)
            {
                s.indent();
                s.out("import ");
                importer.gen(s);
                s.endline();
            }
            else if(type==2)
            {
                s.indent();
                s.out("from ");
                String name = importname.getName(s);
                s.out(name);
                s.out(" import *");
                s.endline();
            }
            else if(type==3)
            {
                s.indent();
                s.out("from ");
                String name = importname.getName(s);
                s.out(name);
                s.out(" import ");
                for(int i=0;i<importerlist.size();i++)
                {
                    //if(i!=0)s.out(" ,"); //Drizzle24
                    if(i!=0)s.out(", "); //Drizzle25
                    importerlist.get(i).gen(s);
                }
                s.endline();
            }
        }

        public static class Importer extends Ast
        {
            Tok importname;
            Tok loadattr;
            Ast desig; //actually the root module name to load.

            public Importer(Tok importname, Tok loadattr, Ast desig)
            {
                this.importname = importname;
                this.loadattr = loadattr;
                this.desig = desig;
            }

            public void gen2(sgen s)
            {
                if(loadattr!=null)
                    m.throwUncaughtException("unhandled.");

                String name = importname.getName(s);
                s.out(name);

                //perhaps only do this if the designator is different.
                //if desig is the name of the module (or the root module in case of e.g. "import os.path" this is "os")
                String dstr = desig.getGenString();
                if(!name.equals(dstr) && !name.startsWith(dstr+"."))
                {
                    s.out(" as ");
                    desig.gen(s);
                }
            }
        }
        public static class Importer2 extends Ast
        {
            Tok importfrom;
            Ast desig;

            public Importer2(Tok importfrom, Ast desig)
            {
                this.importfrom = importfrom;
                this.desig = desig;
            }

            public void gen2(sgen s)
            {
                String name = importfrom.getName(s);
                s.out(name);

                //perhaps only do this if the designator is different.
                String dstr = desig.getGenString();
                if(!name.equals(dstr))
                {
                    s.out(" as ");
                    desig.gen(s);
                }
            }
        }
    }


    public static class Try extends Stmt
    {

        public static class Tryfinally extends Try
        {
            List trystmts;
            Tok loadconsttok;
            List finstmts;

            public Tryfinally(Ast trystmts, Tok loadconsttok, Ast finstmts)
            {
                this.trystmts = (List)trystmts;
                this.loadconsttok = loadconsttok;
                this.finstmts = (List)finstmts;
            }
            public void gen2(sgen s)
            {
                if(!(loadconsttok.oi.pattr instanceof PyNone)) m.throwUncaughtException("unhandled");

                s.indent();s.out("try:");s.endline();

                s.increaseindentation();
                trystmts.gen(s);
                s.decreaseindentation();

                //must have finally block:
                s.indent();s.out("finally:");s.endline();

                s.increaseindentation();
                finstmts.gen(s);
                s.decreaseindentation();
            }
        }

        public static class Tryexcept2 extends Try implements IList
        {
            List trystmts;
            List escepts = new List();
            List finstmts;

            public List getlist(){return escepts;}
            public Tryexcept2(Ast elsestmts)
            {
                this.finstmts = (List)elsestmts;
            }
            public Ast setStmts(Ast trystmts)
            {
                this.trystmts = (List)trystmts;
                return this;
            }
            public void gen2(sgen s)
            {
                s.indent();s.out("try:");s.endline();

                s.increaseindentation();
                trystmts.gen(s);
                s.decreaseindentation();

                for(int i=0;i<escepts.size();i++)
                {
                    Ast escept = escepts.get(escepts.size()-i-1);
                    escept.gen(s);
                }

                if(finstmts.size()!=0)
                {
                    s.indent();s.out("else:");s.endline();
                    s.increaseindentation();
                    finstmts.gen(s);
                    s.decreaseindentation();
                }
            }
        }
        
        /*public static class Tryexcept extends Try
        {
            List trystmts;
            List condexcepts;
            Ast except;
            List finstmts;

            public Tryexcept(Ast trystmts, Ast condexcepts, Ast except, Ast finstmts)
            {
                this.trystmts = (List)trystmts;
                this.condexcepts = (List)condexcepts;
                this.except = except;
                this.finstmts = (List)finstmts;
            }
            public void gen2(sgen s)
            {
                s.indent();s.out("try:");s.endline();

                s.increaseindentation();
                trystmts.gen(s);
                s.decreaseindentation();

                //for(Object condexcept2: condexcepts)
                //{
                //     Stmt.Try.Exceptcond condexcept = (Stmt.Try.Exceptcond)condexcept2;
                //     condexcept.g
                //}
                condexcepts.gen(s);

                if(except!=null)
                {
                    s.indent();s.out("except:");s.endline();
                    s.increaseindentation();
                    except.gen(s);
                    s.decreaseindentation();
                }

                if(finstmts.size()!=0)
                {
                    s.indent();s.out("else:");s.endline();
                    s.increaseindentation();
                    finstmts.gen(s);
                    s.decreaseindentation();
                }

            }
        }*/

        public static class Exceptcond extends Ast
        {
            Ast expr;
            Tok compareop;
            Ast remover; //can be null
            List stmts;

            public Exceptcond(Ast expr, Tok compareop, Ast remover, Ast stmts)
            {
                this.expr = expr;
                this.compareop = compareop;
                this.remover = remover; //can be null
                this.stmts = (List)stmts;
            }

            public void gen2(sgen s)
            {
                if(!compareop.oi.pattr.equals("exception match")) m.throwUncaughtException("unhandled");

                s.indent();s.out("except ");
                expr.gen(s);
                if(remover!=null)
                {
                    s.out(", ");
                    remover.gen(s);
                }
                s.out(":");s.endline();

                s.increaseindentation();
                stmts.gen(s);
                s.decreaseindentation();
            }
        }
        public static class Except extends Ast
        {
            Ast stmts;
            public Except(Ast stmts)
            {
                this.stmts = stmts;
            }
            public void gen2(sgen s)
            {
                s.indent();s.out("except:");s.endline();
                s.increaseindentation();
                stmts.gen(s);
                s.decreaseindentation();
            }
        }
    }

    public static class Forelse extends Stmt
    {
        Tok type;
        Ast expr;
        Ast desig;
        List forstmts;
        List elsestmts;

        public Forelse(Tok token)
        {
            this.type = token;
        }
        public Forelse setvals(Ast expr, Ast desig, Ast forstmts, Ast elsestmts)
        {
            this.expr = expr;
            this.desig = desig;
            this.forstmts = (List)forstmts;
            this.elsestmts = (List)elsestmts;
            return this;
        }
        public void gen2(sgen s)
        {
            if(type.oi.o!=op.FOR_ITER) m.throwUncaughtException("unhandled");

            s.indent();
            s.out("for ");
            desig.gen(s);
            s.out(" in ");
            expr.gen(s);
            s.out(":");
            s.endline();

            s.increaseindentation();
            forstmts.gen(s);
            s.decreaseindentation();

            if(elsestmts.size()!=0)
            {
                s.indent();
                s.out("else:");
                s.endline();
                s.increaseindentation();
                elsestmts.gen(s);
                s.decreaseindentation();
            }
        }
    }

    public static class Break extends Stmt
    {
        public Break()
        {
        }

        public void gen2(sgen s)
        {
            s.indent();
            s.out("break");
            s.endline();
        }
    }

    public static class Continue extends Stmt
    {
        public Continue()
        {
        }

        public void gen2(sgen s)
        {
            s.indent();
            s.out("continue");
            s.endline();
        }
    }

    public static class Whileelse extends Stmt
    {
        Ast expr;
        List whilestmts;
        List elsestmts;

        public Whileelse(Ast expr, Ast whilestmts, Ast elsestmts)
        {
            this.expr = expr;
            this.whilestmts = (List)whilestmts;
            this.elsestmts = (List)elsestmts;
        }
        public void gen2(sgen s)
        {
            s.indent();
            s.out("while");
            s.out(" (");
            if(expr==null) s.out("1"); //must be 1 instead of True.  True does not get optimised :P
            else expr.gen(s);
            s.out("):");
            s.endline();

            s.increaseindentation();
            whilestmts.gen(s);
            s.decreaseindentation();

            if(elsestmts.size()!=0)
            {
                s.indent();
                s.out("else:");
                s.endline();
                s.increaseindentation();
                elsestmts.gen(s);
                s.decreaseindentation();
            }
        }
    }

    public static class Ifelse extends Stmt
    {
        Ast expr;
        Tok ifcmd;
        List ifstmts;
        List elsestmts;

        public Ifelse(Ast expr, Ast ifcmd, Ast ifstmts, Ast elsestmts)
        {
            this.expr = expr;
            this.ifcmd = (Tok)ifcmd;
            this.ifstmts = (List)ifstmts;
            this.elsestmts = (List)elsestmts;
        }
        public void gen2(sgen s)
        {
            gen2(s,false);
        }
        public void gen2(sgen s, boolean iselif)
        {
            if(ifcmd.oi.o!=op.JUMP_IF_FALSE) m.throwUncaughtException("unhandled");

            s.indent();
            if(iselif) s.out("elif");
            else s.out("if");
            s.out(" ");
            //s.out("(");
            expr.gen(s);
            //s.out(")");
            s.out(":");
            s.endline();

            s.increaseindentation();
            ifstmts.gen(s);
            s.decreaseindentation();

            if(elsestmts.size()!=0)
            {
                if(elsestmts.size()==1 && (elsestmts.get(0) instanceof Stmt.Ifelse))
                {
                    //elif:
                    Stmt.Ifelse elses = (Stmt.Ifelse)elsestmts.get(0);
                    elses.gen2(s,true);
                }
                else
                {
                    s.indent();s.out("else:");s.endline();
                    s.increaseindentation();
                    elsestmts.gen(s);
                    s.decreaseindentation();
                }
            }
        }
    }

    /*public static class Mkfunc extends Stmt
    {
        Expr.Makefunction mkfuncexpr;
        Ast desig;

        public Mkfunc(Ast mkfuncexpr2, Ast desig2)
        {
            mkfuncexpr = (Expr.Makefunction)mkfuncexpr2;
            desig = desig2;
        }
        public boolean isimportant()
        {
            return true;
        }
        public void gen2(sgen s)
        {
            //if(mkfuncexpr.getlist().size()!=0)
            //    m.throwUncaughtException("unhandled");

            s.endline(); //space before
            s.indent();
            s.out("def ");
            String name = desig.getGenString();
            s.out(name);
            //s.out("(");
            //for(int i=0;i<mkfuncexpr.exprs.size();i++)
            //{
            //    Ast expr = mkfuncexpr.exprs.get(i);
            //    if(i!=0) s.out(",");
            //    expr.gen(s);
            //}
            //s.out("):");
            //s.endline();

            //s.increaseindentation();
            //mkfuncexpr.gen(s); //is this right?  it depends on what a Expr.Mkfunc looks like on it's own.
            mkfuncexpr.genAsFunction(s,name);
            //s.decreaseindentation();
            s.endline(); //space after
        }
    }*/

    public static class Classdef extends Stmt
    {
        Ast classname;
        IList baseclasses; //tuple with 0(?) elements.  Base classes.
        Expr.Makefunction s3; //Expr.Makefunction
        public Ast nametostoreas; //should equal classname.

        public Classdef(Ast t1, Ast s2, Ast s3, Ast s4)
        {
            this.classname = t1;
            this.baseclasses = (IList)s2;
            this.s3 = (Expr.Makefunction)s3;
            this.nametostoreas = s4;
        }

        public boolean isimportant()
        {
            return true;
        }
        public void gen2(sgen s)
        {
            String c1 = classname.getGenString();
            //String c2 = "'"+nametostoreas.getGenString()+"'";
            String c2 = nametostoreas.getGenString();
            if(!c1.equals(c2))
                m.throwUncaughtException("unhandled");

            s.endline(); //space before
            s.indent(); s.out("class "); nametostoreas.gen(s);
            if(baseclasses.getlist().size()!=0)
            {
                s.out("(");
                for(int i=0;i<baseclasses.getlist().size();i++)
                {
                    if(i!=0)s.out(", ");
                    baseclasses.getlist().get(baseclasses.getlist().size()-i-1).gen(s);
                }
                s.out(")");
            }
            s.out(":"); s.endline();
            s.endline(); //space before members
            s.increaseindentation();
            s3.genAsClass(s,c2);
            s.decreaseindentation();
            s.endline(); //space after
        }
    }
}

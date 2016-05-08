/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import shared.m;
import java.util.ArrayDeque;

public class sgen
{
    //private Ast module;
    private int indentation = 0;
    private StringBuilder s = new StringBuilder();
    static private final String tab = "    ";
    static private final String endl = "\n";
    private ArrayDeque stack = new ArrayDeque();

    public sgen()
    {
        //module = mod;
    }

    /*public String generate()
    {
        stack.clear();
        //module = mod;
        indentation = 0;
        s = new StringBuilder();
        //stmtlist(module);
        //module.gen(this);
        genModule(module,this);
        return s.toString();
    }*/
    public String getGeneratedSource()
    {
        return s.toString();
    }
    private static void genModule(Ast mod, sgen s)
    {
        mod.gen(s);
        s.endline(); //because Python is picky :P
        s.endline();
    }
    void visit(Ast ast)
    {
        stack.push(ast);
    }
    void leave()
    {
        stack.pop();
    }
    void increaseindentation()
    {
        indentation++;
    }
    void decreaseindentation()
    {
        indentation--;
        if(indentation<0) m.throwUncaughtException("Indentation went below zero.");
    }
    void indent()
    {
        for(int i=0;i<indentation;i++)
        {
            s.append(tab);
        }
    }
    void endline()
    {
        s.append(endl);
    }
    void out(String str)
    {
        s.append(str);
    }
    Ast getFirstImportantAncestor()
    {
        java.util.Iterator<Ast> i = stack.descendingIterator();
        while(i.hasNext())
        {
            Ast anc = i.next();
            if(anc.isimportant()) return anc;
        }
        return null;
    }
    boolean isInFunction()
    {
        java.util.Iterator<Ast> i = stack.iterator();
        while(i.hasNext())
        {
            Ast anc = i.next();
            //if(anc instanceof Stmt.Mkfunc) return true;
            if(anc instanceof StmtAssign && ((StmtAssign)anc).isfunction) return true;
            if(anc instanceof Stmt.Classdef) return false;
        }
        return false;
    }
    public <T> T getFirstAncestorOfClass(Class<T> klass)
    {
        java.util.Iterator<Ast> i = stack.descendingIterator();
        while(i.hasNext())
        {
            Ast anc = i.next();
            if(anc.getClass()==klass) return (T)anc;
        }
        return null;
    }
    /*void stmtlist(StmtList sl)
    {
        for(Stmt stmt: sl.stmts)
        {
            stmt(stmt);
        }
    }*/

    /*void stmt(Stmt stmt)
    {
        space();
        switch(stmt.getClass().)
        {

        }
    }*/
}

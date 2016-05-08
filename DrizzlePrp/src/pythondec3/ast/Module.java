/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
import pythondec.PyCode;

public class Module extends Ast
{
    StmtList stmtlist;
    PyCode code;

    public Module(Ast stmtlist2, PyCode code2 )
    {
        //Just a list of stmts, e.g. a module.
        //super();
        stmtlist = (StmtList)stmtlist2;
        code = code2;

        //get rid of invalid return statement at end that is tacked on by compiler.
        if(stmtlist.size()!=0)
        {
            Stmt endstmt = stmtlist.get(stmtlist.size()-1);
            if(endstmt instanceof StmtReturn)
            {
                StmtReturn endstmt2 = (StmtReturn)endstmt;
                if(endstmt2.returnvalue.isNoneConst())
                {
                    stmtlist.remove(endstmt);
                }
            }
        }
    }
    public void gen2(sgen s)
    {
        s.out("#Decompiled with Drizzle"+Integer.toString(gui.Version.version)+"!  Enjoy :)\n\n");
        for(String global: code.globals)
        {
            s.indent();
            s.out("global ");
            s.out(global);
            s.endline();
        }

        stmtlist.gen(s);
        s.endline(); //because Python is picky :P
        s.endline();
    }
}

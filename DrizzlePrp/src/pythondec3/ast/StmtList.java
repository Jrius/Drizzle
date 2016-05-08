/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

//import beaver.Symbol;
//import java.util.ArrayList;

public class StmtList extends List<Stmt>
{
    //ArrayList<Ast> stmts = new ArrayList();
    //List<Stmt> stmts = new List();

    //public List getlist()
    //{
    //    return stmts;
    //}
    public StmtList()
    {
        super();
    }
    public StmtList(Ast s)
    {
        super((Stmt)s);
    }
    /*public StmtList(Ast... stmts2 )
    {
        //Just a list of stmts, e.g. a module.
        super();
        for(Ast stmt: stmts2)
        {
            stmts.add(stmt);
        }
    }*/

    //public ArrayList<Ast> getList()
    //{
    //    return stmts;
    //}
    /*public void gen(sgen s)
    {
        for(Stmt stmt: stmts)
        {
            stmt.gen(s);
        }
    }*/

    public void gen2(sgen s)
    {
        //is this okay?  It's possible that the super.gen will remove an item, but I don't think it should.
        if(list.size()==0)
        {
            s.indent();
            s.out("pass");
            s.endline();
        }
        else
        {
            super.gen2(s); //must be gen2 to avoid a loop :P
        }
    }
}

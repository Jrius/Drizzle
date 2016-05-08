/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import java.util.ArrayList;
import shared.m;

public class StmtPrint extends Stmt
{
    Ast thingtoprint;
    Ast dests;
    boolean printnl;

    public StmtPrint(Ast thingtoprint, Ast dests, boolean printnl )
    {
        //thingtoprint is the object to print (possibly a tuple, possibly null), dests are the files to print to (if null, just std out), and printnl is whether to follow with a newline.
        super();
        this.thingtoprint = thingtoprint;
        this.dests = dests;
        this.printnl = printnl;
        //if(this.dests==null) this.dests = new Expr[0];
    }

    public void gen2(sgen s)
    {
        s.indent();
        if(dests!=null)
        {
            s.out("print >> ");
            dests.gen(s);
            if(thingtoprint!=null)
            {
                List list = (List)thingtoprint;
                for(int i=0;i<list.size();i++)
                {
                    s.out(", ");
                    list.get(i).gen(s);
                }
            }
        }
        else
        {
            s.out("print ");
            if(thingtoprint!=null)
            {
                thingtoprint.gen(s);
            }
        }
        if(!printnl) s.out(",");
        s.endline();
    }
}

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package pythondec;
//
//import java.util.Vector;
//import pythondec.Disassemble.OpInfo;
//
//public class Deflattener2
//{
//
//    public Deflattener2(Disassemble disassembledCode, PythonVersions p)
//    {
//        context c = new context();
//        //copy ops
//        c.ops = new OpInfo[disassembledCode.rv.size()];
//        for(int i=0;i<c.ops.length;i++) c.ops[i] = disassembledCode.rv.elementAt(i);
//
//        c.pos = 0;
//        stmt_list_star statements = stmt_list_star.read(c);
//    }
//    public static class context
//    {
//        OpInfo[] ops;
//        int pos;
//    }
//    public static class g
//    {
//    }
//    public static class stmt_list_star extends g
//    {
//        Vector<stmt> stmts = new Vector();
//
//        public static stmt_list_star read(context c)
//        {
//            stmt_list_star r = new stmt_list_star();
//            while(true)
//            {
//                stmt s = stmt.read(c);
//                if(s==null)
//                {
//                    break;
//                }
//                else
//                {
//                    r.stmts.add(s);
//                }
//            }
//            return r;
//        }
//    }
//    public static class stmt extends g
//    {
//        public static stmt read(context c)
//        {
//            return null;
//        }
//    }
//    public static enum g2
//    {
//        assignment_stmt,
//
//    }
//    public try_stmt read_try_stmt()
//    {
//        return null;
//    }
//    public static class try_stmt
//    {
//    }
//    public static class statement
//    {
//
//    }
//    public static class expr
//    {
//
//    }
//    public static class designator
//    {
//
//    }
//    public static class assignment_stmt
//    {
//        expr a;
//        designator b;
//
//        public assignment_stmt(expr a, designator b)
//        {
//            this.a = a;
//            this.b = b;
//        }
//    }
//}

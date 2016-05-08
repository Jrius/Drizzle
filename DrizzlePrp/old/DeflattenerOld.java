///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package pythondec;
//
//import java.util.Vector;
//import pythondec.Disassemble.OpInfo;
//import shared.m;
//import java.util.ArrayDeque;
//import java.util.Deque;
//import shared.e;
//import shared.b;
//
//public class DeflattenerOld
//{
//    Vector<OpInfo> rv;
//    int pos;
//    Tree root;
//    Tree curt;
//
//    static enum tt
//    {
//        leaf, //means it contains a Token.
//        none, //means it doesn't exist.
//        statement,
//        module,
//    }
//    static class Tree
//    {
//        OpInfo t;
//        //Deque<Tree> leaves;
//        Vector<Tree> leaves;
//        //tt type;
//        op type;
//
//        private Tree(){}
//        public static Tree CreateWithToken(OpInfo t)
//        {
//            Tree r = new Tree();
//            r.t = t;
//            //r.type = tt.leaf;
//            r.type = t.o;
//            r.leaves = null;
//            return r;
//        }
//        public static Tree CreateWithType(op type)
//        {
//            Tree r = new Tree();
//            r.t = null;
//            //r.type = type;
//            r.type = type;
//            //r.leaves = new ArrayDeque();
//            r.leaves = new Vector();
//            return r;
//        }
//        public void add(Tree branch)
//        {
//            leaves.add(branch);
//        }
//        public int arg0()
//        {
//            int r = this.t0().t.oparg;
//            return r;
//        }
//        public Tree t0()
//        {
//            Tree r = leaves.get(leaves.size()-1);
//            return r;
//        }
//        public op ox(int i)
//        {
//            if(i<leaves.size())
//            {
//                return leaves.get(leaves.size()-1-i).type;
//            }
//            else
//            {
//                return op.none;
//            }
//        }
//        public op o0()
//        {
//            return ox(0);
//        }
//        public op o1()
//        {
//            return ox(1);
//        }
//        public op o2()
//        {
//            return ox(2);
//        }
//        public op o3()
//        {
//            return ox(3);
//        }
//        public Tree pop()
//        {
//            Tree r = leaves.remove(leaves.size()-1);
//            return r;
//        }
//        public Tree pop(int numargs, op containertype)
//        {
//            Tree subtree = Tree.CreateWithType(containertype);
//            for(int i=0;i<numargs;i++)
//            {
//                subtree.add(this.pop());
//            }
//            this.add(subtree);
//            return subtree;
//        }
//        //format of args is: multiplicityOfType1, type1, multiplicityOfType2, type2, type3, multiplicityOfType4, type4, etc...
//        public Tree newpop(op containertype, Vector<Object> args)
//        {
//            Object[] args2 = shared.generic.convertVectorToArray(args, Object.class);
//            return newpop(containertype, args2);
//        }
//        public Tree newpop(op containertype, Object... args)
//        {
//            Tree subtree = Tree.CreateWithType(containertype);
//            int totalcount=0;
//            for(int i=0;i<args.length;i++)
//            {
//                int multiplicity=1;
//                if(args[i] instanceof Integer)
//                {
//                    multiplicity = (Integer)args[i];
//                    i++;
//                }
//                totalcount+= multiplicity;
//                //op curtype = (op)args[i];
//                //ensuretype(this,stackpos,multiplicity,curtype);
//                //stackpos += multiplicity;
//            }
//            int stackpos = totalcount-1;
//            for(int i=0;i<args.length;i++)
//            {
//                int multiplicity=1;
//                if(args[i] instanceof Integer)
//                {
//                    multiplicity = (Integer)args[i];
//                    i++;
//                }
//                //totalcount+= multiplicity;
//                op curtype = (op)args[i];
//                ensuretype(this,stackpos,multiplicity,curtype);
//                stackpos -= multiplicity;
//            }
//            for(int j=0;j<totalcount;j++)
//            {
//                subtree.add(this.pop());
//            }
//            this.add(subtree);
//            return subtree;
//        }
//        public String toString()
//        {
//            String r = type.toString();
//            if(this.leaves!=null)
//            {
//                r += "(";
//                for(Tree l: this.leaves)
//                {
//                    r += l.toString();
//                    r += ", ";
//                }
//                r += ")";
//            }
//            return r;
//        }
//    }
//
//    public DeflattenerOld(Disassemble disassembledCode, PythonXX p)
//    {
//
//        //initialise some stuff:
//        rv = disassembledCode.rv;
//        pos = 0;
//
//        //create the starting tree.
//        root = Tree.CreateWithType(op.module);
//
//        //start();
//        Deflattener2 gram = new Deflattener2(disassembledCode,p);
//
//        int dummy=0;
//    }
//
//    void start2()
//    {
//    }
//
//    void start()
//    {
//        curt = root;
//        for(OpInfo tk: rv)
//        {
//            read(tk);
//        }
//    }
//    void read(OpInfo tk)
//    {
//        //Token tk = rv.get(pos); pos++;
//        curt.add(Tree.CreateWithToken(tk));
//
//        while(true)
//        {
//            Tree modified = bubble();
//            if(modified==null) break;
//        }
//    }
//    Tree bubble()
//    {
//
//        Tree t = curt;
//        op o0 = t.ox(0);
//        op o1 = t.ox(1);
//        op o2 = t.ox(2);
//
//        //if(o0==op.LOAD_CONST) return t.pop(1,op.expr);
//        //if(o0==op.LOAD_NAME) return t.pop(1, op.expr);
//        //if(o0==op.STORE_NAME) return t.pop(1, op.designator);
//        if(o1.is(op.expr) && o0.is(op.designator)) return t.pop(2, op.assign);
//        if(o1.is(op.expr) && o0.is(op.RETURN_VALUE)) return t.pop(2, op.return_stmt);
//        if(o1.is(op.expr) && o0.is(op.POP_TOP)) return t.pop(2, op.call_stmt);
//        if(o2==op.LOAD_CONST && o1==op.IMPORT_NAME && o0==op.IMPORT_STAR) return t.pop(3, op.importstar2);
//        if(o0==op.STOP_CODE) return t.pop(); //just get rid of it.
//        if(o0==op.SET_LINENO) return t.pop(); //just get rid of it.
//        if(o0==op.BUILD_TUPLE)
//        {
//            //get 'size' expr for the tuple.
//            int size = t.t0().t.oparg;
//            ensuretype(t,1,size,op.expr);
//            return t.pop(size+1, op.build_tuple); //was build_tuple.
//        }
//        if(o0==op.MAKE_FUNCTION)
//        {
//            int size = t.t0().t.oparg;
//            ensuretype(t,1,1,op.LOAD_CONST);
//            ensuretype(t,2,size,op.expr); //in fact, the first must be a load_const.
//            return t.pop(size+1+1, op.mkfunc);
//        }
//        if(o0==op.CALL_FUNCTION)
//        {
//            boolean haskw = (o0==op.CALL_FUNCTION_KW || o0==op.CALL_FUNCTION_VAR_KW);
//            boolean hasvar = (o0==op.CALL_FUNCTION_VAR || o0==op.CALL_FUNCTION_VAR_KW);
//            int numPositionalParams = t.t0().t.i0;
//            int numKeywordParams = t.t0().t.i1;
//            if(numPositionalParams+numKeywordParams!=0)
//            {
//                m.warn("Untested.");
//            }
//            Vector<Object> form2 = new Vector();
//            form2.add(op.mkfunc); //op.expr originally, but it should be a function object, I guess.
//            for(int i=0;i<numPositionalParams;i++) form2.add(op.expr);
//            for(int i=0;i<numKeywordParams;i++)
//            {
//                form2.add(op.expr);
//                form2.add(op.expr);
//            }
//            if(hasvar) form2.add(op.expr);
//            if(haskw) form2.add(op.expr);
//            form2.add(o0);
//            return t.newpop(op.call_function,  form2);
//        }
//        if(o1.is(op.BUILD_CLASS) && o0.is(op.designator))
//        {
//            return t.newpop(op.classdef, op.LOAD_CONST, op.expr, /*op.mkfunc, op.CALL_FUNCTION,*/op.call_function, op.BUILD_CLASS, op.designator);
//        }
//        if(o0.is(op.END_FINALLY))
//        {
//            //either a try/except/else or a try/finally statement (in Python, these are two separate things; you can't have a try/except/finally in Python.
//
//        }
//        return null;
//    }
//    static void ensuretype(Tree t, int startpos, int count, op type)
//    {
//        for(int i=0;i<count;i++)
//        {
//            ensuretype(t,startpos+i,type);
//        }
//    }
//    static void ensuretype(Tree t, int pos, op type)
//    {
//        op tox = t.ox(pos);
//        if(!tox.is(type))
//        {
//            m.throwUncaughtException("Unexpected token.");
//        }
//    }
//}

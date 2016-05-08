/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import java.util.Vector;
import java.util.HashSet;
import pythondec.Disassemble.OpInfo;
import shared.m;
//import pythondec.Demarshal.PyObject;

public class PyCode_Builder
{

    private Vector<OpInfo> ops = new Vector();
    //private HashSet<PyObject> consts = new HashSet(); //hashset is overkill, let's just use a vector.
    private Vector<PyObject> consts = new Vector();
    private Vector<PyObject> names = new Vector();

    public PyCode_Builder(){
    }

    private void addop(OpInfo o)
    {
        ops.add(o);
    }
    private int addconst(PyObject obj)
    {
        int cursize = consts.size();
        for(int i=0;i<cursize;i++)
        {
            PyObject curobj = consts.get(i);
            if(curobj.equals(obj)) return i; //already here so done.
        }
        //otherwise we don't already have it.
        consts.add(obj);
        return cursize; //(cursize+1)-1
    }
    private int addname(PyString obj)
    {
        int cursize = names.size();
        for(int i=0;i<cursize;i++)
        {
            PyObject curobj = names.get(i);
            if(curobj.equals(obj)) return i; //already here so done.
        }
        //otherwise we don't already have it.
        names.add(obj);
        return cursize; //(cursize+1)-1
    }

    public void addop(op o)
    {
        OpInfo oi = OpInfo.create(o);
        addop(oi);
    }
    public void addopWithArg(op o, int arg)
    {
        OpInfo oi = OpInfo.create(o,arg);
        addop(oi);
    }
    public void addopWithConst(op o, Object constObj)
    {
        //create const object.
        PyObject co;
        if(constObj==null) co = PyNone.create();
        else if(constObj instanceof Integer) co = PyInt.create((Integer)constObj);
        else throw new shared.uncaughtexception("unhandled.");

        //add to const array.
        int pos = addconst(co);
        addopWithArg(o, pos);
    }
    public void addopWithName(op o, String name)
    {
        PyString n2 = PyString.create(name);
        int pos = addname(n2);
        addopWithArg(o, pos);
    }
    public PyCode getPyCode()
    {
        //check pyassem.py for details.
        //check http://www.python.org/doc/2.2.3/lib/bytecodes.html for details.

        //compile all this into a PyCode object.
        PyCode r = new PyCode();
        r.argcount = 0;  //okay for module, not functions. depends on if VARARGS are used.
        r.nlocals = 0;  //if we don't optimise, then no flags are set, including CO_NEWLOCALS, and this is 0.
        r.stacksize = 0; //bad, but it is computed at runtime based on a worst-case-scenario involving the blocks.  Let's just go 100 for testing.  edit: further testing shows that 0 seems to work fine.   I guess it's initial stack size.
        r.flags = 0; //if we don't optimise, then no flags are set.
        r.code = compileCode();
        r.consts = PyTuple.create(consts);
        r.names = PyTuple.create(names);
        r.varnames = r.names; //not right!  is it r.names + arg names?  that would make sense.  It seems to be used for LOAD_FAST, STORE_FAST, and DELETE_FAST.
        r.freevars = PyTuple.create(new Vector()); //not right!  Free vars are vars that are used before being defined.  It seems to be used for LOAD_CLOSURE, LOAD_DEREF, STORE_DEREF, and MAKE_CLOSURE.
        r.cellvars = PyTuple.create(new Vector()); //not right!  Cell and free vars are referenced by the same ops.  The index applies to cellvars then spills over into freevars.
        r.filename = PyString.create("<unknown filename>"); //set this later.
        r.name = PyString.create("?"); //is this always ? for modules?
        r.firstlineno = 1;
        r.lnotab = PyString.create(""); //not right!  documented in compile.c
        return r;
    }
    private PyString compileCode()
    {
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        for(OpInfo oi: ops)
        {
            if(oi.o.hasExtendedArgument())
            {
                m.throwUncaughtException("unhandled.");
            }

            os.write(oi.o.getbytecode());
            if(oi.o.hasArgument())
            {
                os.write(oi.i0);
                os.write(oi.i1);
            }
        }
        byte[] bytecodedata = os.toByteArray();

        PyString r = PyString.create(bytecodedata);
        return r;
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.IBytestream;
import shared.b;
import pythondec3.ast.Tok;
import shared.m;

public class PyCode extends PyObject
{
    //in compile.h

    //public short argcount;
    //public short nlocals;
    //public short stacksize;
    //public short flags;
    public int argcount;
    public int nlocals;
    public int stacksize;
    public int flags;
    public PyString code;
    public PyTuple consts;
    public PyTuple names;
    public PyTuple varnames;
    public PyTuple freevars;
    public PyTuple cellvars;
    public PyObject filename;
    public PyObject name;
    //public short firstlineno;
    public int firstlineno;
    public PyObject lnotab;

    //flags
    public boolean flag_optimized;
    public boolean flag_newlocals;
    public boolean flag_varargs;
    public boolean flag_varkeywords;
    public boolean flag_nested;
    public boolean flag_generator;
    //public boolean flag_nofree; //in 2.3 but not 2.2 and redundant anyway.
    public boolean flag_generator_allowed;
    public boolean flag_future_division;

    //disassembly stuff:
    public java.util.Set<String> globals = null;
    public java.util.ArrayList<PyCode> children = null;

    //decompilation stuff:
    public pythondec3.ast.StmtList root = null;
    public lpg.runtime.ILexStream lexstream = null;
    public pythondec3.PythonDec22 pd22 = null;
    public lpg.runtime.IPrsStream prsstream = null;
    public java.util.ArrayList<pythondec3.ast.Tok> tokens = null;
    //public java.util.LinkedList<Tok> tokens = null;

    //debugging stuff
    public String debugname;

    public PyCode(IBytestream c)
    {
        int pyver = c.getIntValue("pyver");
        if(pyver==22)
        {
            argcount = b.Int16ToInt32(c.readShort());
            nlocals = b.Int16ToInt32(c.readShort());
            stacksize = b.Int16ToInt32(c.readShort());
            flags = b.Int16ToInt32(c.readShort());

            flag_optimized = (flags&0x01)!=0;
            flag_newlocals = (flags&0x02)!=0;
            flag_varargs = (flags&0x04)!=0;
            flag_varkeywords = (flags&0x08)!=0;
            flag_nested = (flags&0x10)!=0;
            flag_generator = (flags&0x20)!=0;
            flag_generator_allowed = (flags&0x1000)!=0;
            flag_future_division = (flags&0x2000)!=0;
        }
        else
        {
            argcount = c.readInt();
            nlocals = c.readInt();
            stacksize = c.readInt();
            flags = c.readInt();

            flag_optimized = (flags&0x01)!=0;
            flag_newlocals = (flags&0x02)!=0;
            flag_varargs = (flags&0x04)!=0;
            flag_varkeywords = (flags&0x08)!=0;
            flag_nested = (flags&0x10)!=0;
            flag_generator = (flags&0x20)!=0;
            //flag_nofree = (flags&0x40)!=0;
            flag_generator_allowed = (flags&0x1000)!=0;
            flag_future_division = (flags&0x2000)!=0;
        }
        code = PyObject.read(c).cast();
        if(code!=null) consts = PyObject.read(c).cast();
        if(consts!=null) names = PyObject.read(c).cast();
        if(names!=null) varnames = PyObject.read(c).cast();
        if(varnames!=null) freevars = PyObject.read(c).cast();
        if(freevars!=null) cellvars = PyObject.read(c).cast();
        if(cellvars!=null) filename = PyObject.read(c);
        if(filename!=null) name = PyObject.read(c);
        if(name!=null)
        {
            if(pyver==22)
            {
                firstlineno = b.Int16ToInt32(c.readShort());
            }
            else
            {
                firstlineno = c.readInt();
            }
            lnotab = PyObject.read(c);
        }

        //Vector<Token> rw = disassemble();
    }
    PyCode(){}

    public void marshal(shared.IBytedeque c)
    {
        c.writeByte((byte)'c');
        c.writeShort((short)argcount);
        c.writeShort((short)nlocals);
        c.writeShort((short)stacksize);
        c.writeShort((short)flags);
        code.marshal(c);
        consts.marshal(c);
        names.marshal(c);
        varnames.marshal(c);
        freevars.marshal(c);
        cellvars.marshal(c);
        filename.marshal(c);
        name.marshal(c);
        c.writeShort((short)firstlineno);
        lnotab.marshal(c);
    }
    public int hashCode()
    {
        return argcount+nlocals+stacksize+flags+code.hashCode(); //meh, good engouh.
    }
    public String toString()
    {
        return name.toString();
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof PyCode)) return false;
        PyCode o2 = (PyCode)o;
        if(o2.argcount!=argcount) return false;
        if(o2.nlocals!=nlocals) return false;
        if(o2.stacksize!=stacksize) return false;
        if(o2.flags!=flags) return false;
        if(!o2.code.equals(code)) return false;
        if(!o2.consts.equals(consts)) return false;
        if(!o2.names.equals(names)) return false;
        if(!o2.varnames.equals(varnames)) return false;
        if(!o2.freevars.equals(freevars)) return false;
        if(!o2.cellvars.equals(cellvars)) return false;
        if(!o2.filename.equals(filename)) return false;
        if(!o2.name.equals(name)) return false;
        if(o2.firstlineno!=firstlineno) return false;
        if(!o2.lnotab.equals(lnotab)) return false;
        return true;
    }
    public boolean compare(PyObject o)
    {
        if(o==null) return false;
        if(!(o instanceof PyCode)) return false;
        PyCode o2 = (PyCode)o;
        if(this.argcount!=o2.argcount)
            return false;
        if(this.nlocals!=o2.nlocals)
            return false;
        //if(this.stacksize!=o2.stacksize)
        //    return false;
        if(this.flags!=o2.flags)
            return false;
        //skip code and do tokens instead
        if(this.tokens.size()!=o2.tokens.size())
        {
            m.msg("Printing different length token lists:");
            m.msg("List 1:");
            for(Tok tok: tokens) m.msg(tok.toString());
            m.msg("\n\n\n\nlist 2:");
            for(Tok tok: o2.tokens) m.msg(tok.toString());
            int min = Math.min(tokens.size(), o2.tokens.size());
            int i;
            for(i=0;i<min;i++)
            {
                Tok t1 = this.tokens.get(i);
                Tok t2 = o2.tokens.get(i);
                if(!t1.compare(t2))
                {
                    break;
                }
            }

            m.msg("\n\n\n\nlast similar token: "+Integer.toString(i-1));
            m.msg("In file: "+this.debugname);

            return false;
        }
        for(int i=0;i<this.tokens.size();i++)
        {
            Tok t1 = this.tokens.get(i);
            Tok t2 = o2.tokens.get(i);
            if(!t1.compare(t2))
            {
                t1.compare(t2); //just for testing...
                return false;
            }
        }
        //we'll let the tokens comparisons compare these things, as the order may have changed.
        //if(!consts.compare(o2.consts)) return false;
        //if(!names.compare(o2.names)) return false;
        //if(!varnames.compare(o2.varnames)) return false;
        //if(!freevars.compare(o2.freevars)) return false;
        //if(!cellvars.compare(o2.cellvars)) return false;
        if(!filename.compare(o2.filename))
            return false;
        if(!name.compare(o2.name))
        {
            if(pythondec3.options.demangle)
            {
                return false;
            }
            //not necessarily a problem.
            String n1 = ((PyString)name).toJavaString();
            String n2 = ((PyString)o2.name).toJavaString();
            if(!n1.endsWith(n2) && !n2.endsWith(n1))
            {
                return false;
            }
            //probably just mangled name
        }
        //skip firstlineno
        //skip lnotab
        return true;
    }

}

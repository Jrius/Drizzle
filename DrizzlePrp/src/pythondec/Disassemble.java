/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
//import pythondec.Demarshal.PyCode;
//import pythondec.Demarshal.PyString;
//import pythondec.Demarshal.PyObject;
import shared.b;
import shared.m;
import pythondec3.helpers;

public class Disassemble
{

    //static boolean initialised = false;
    //static HashMap<Byte,op> ops;

    //HashMap<Integer,Integer> jumptable;
    JumpInfo jumptable;
    public Vector<OpInfo> rv;



    public static class OpInfo
    {
        public op o;
        public int offset;

        public Integer i0;
        public Integer i1;
        public Integer oparg;
        public Object pattr;
        //Integer pointerSource;
        public Integer pointerDest;
        public ArrayList<Integer> pointerSources;

        public OpInfo(op o, Integer i0, Integer i1, Integer oparg, Object pattr, int offset)
        {
            this.o = o;
            this.i0 = i0;
            this.i1 = i1;
            this.oparg = oparg;
            this.pattr = pattr;
            this.offset = offset;
        }
        public OpInfo(op o, int offset)
        {
            this.o = o;
            this.offset = offset;
        }

        public static OpInfo create(op o)
        {
            return new OpInfo(o,-1);
        }
        public static OpInfo create(op o, int arg)
        {
            OpInfo o2 = new OpInfo(o,-1);
            o2.setArg(arg);
            return o2;
        }
        public String toString()
        {
            return o.toString();
        }
        public String toDebugString()
        {
            return o.toString() + " arg:" + ((oparg!=null)?oparg.toString():"") + " pattr:" + ((pattr!=null)?pattr.toString():"") + " offset:"+Integer.toString(offset);
        }
        public void setArg(int arg)
        {
            oparg = arg;
            i0 = b.getByte0(arg);
            i1 = b.getByte1(arg);
        }

        public boolean compare(OpInfo o2)
        {
            if(o2==null) return false;
            if(this.o!=o2.o) return false;
            //we can skip a number of things such as pattr, i0, i1, pointerDest, pointerSources
            //we *must* skip offset, as it can change with different set_lineno's, etc.
            /*if(this.oparg==null)
            {
                if(o2.oparg==null)
                {
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if(o2.oparg==null)
                {
                    return false;
                }
                else
                {
                    if(this.o.hasJabs() || this.o.hasJrel())
                    {
                        //ignore jumps as they are affected by line numbers.
                    }
                    else if(this.o.hasLocal()||this.o.)
                    {
                        //if it uses a name, just care about the name, not the position.
                        if(!this.pattr.equals(o2.pattr)) return false;
                    }
                    else
                    {
                        if(!this.oparg.equals(o2.oparg)) return false;
                    }
                }
            }*/
            if(this.o.hasArgument())
            {
                if(this.o.hasJabs() || this.o.hasJrel())
                {
                    //ignore jumps as they are affected by line numbers.
                }
                else if(this.o.hasCompare())
                {
                    //compare the numbers
                    if(!this.oparg.equals(o2.oparg)) return false;
                }
                else if(this.o.hasConst()||this.o.hasFree()||this.o.hasLocal()||this.o.hasName())
                {
                    //when using a name, just care about the name, not the position.
                    PyObject pa = (PyObject)this.pattr;
                    PyObject pa2 = (PyObject)o2.pattr;
                    if(!pa.compare(pa2)) return false;
                }
                else
                {
                    if(!this.oparg.equals(o2.oparg)) return false;
                    //m.throwUncaughtException("unhandled");
                }
            }
            return true;
        }
    }
    //public Disassemble(Demarshal demarshalledCode/*, PythonXX p*/)
    public Disassemble(PyCode pycode, PythonVersions p)
    {
        //initialise();

        //PyString code = demarshalledCode.code.code;
        PyString code = pycode.code;

        //jumptable = find_jump_targets(code);
        //jumptable = find_jumps(code);
        jumptable = new JumpInfo();
        rv = new Vector();

        int i=0;
        while(i<code.rawstr.length)
        {
            int offset = i;
            byte c = code.rawstr[i]; i++;
            op o = op.get(c);
            OpInfo oo = new OpInfo(o,offset);

            //Integer numrefs = jumptable.get(offset);
            /*HashSet<Integer> refs = jumptable.from2to1.get(offset);
            if(refs!=null)
            {
                for(Integer ref: refs)
                {
                    OpInfo t2 = new OpInfo(op.come_from,offset);
                    t2.pointerSource = ref;
                    //add int property =j
                    rv.add(t2);
                }
            }*/

            //Integer i0 = null; Integer i1 = null;
            //Integer oparg = null;
            //Object pattr = null;
            //PyObject pattr;
            if(o.hasArgument())
            {
                byte b1 = code.rawstr[i]; i++; oo.i0 = b.ByteToInt32(b1);
                byte b2 = code.rawstr[i]; i++; oo.i1 = b.ByteToInt32(b2);
                oo.oparg = b.BytesToInt32(b1, b2, (byte)0, (byte)0);
                int extended_arg = 0;
                if(o==op.EXTENDED_ARG)
                {
                    extended_arg = oo.oparg<<16;
                    m.throwUncaughtException("Unhandled.");
                }
                if(o.hasConst())
                {
                    PyObject const1 = pycode.consts.items[oo.oparg];
                    if(const1 instanceof PyCode)
                    {
                        //m.throwUncaughtException("not yet handled PyCode const.");
                        oo.pattr = const1;
                    }
                    else
                    {
                        oo.pattr = const1;
                    }
                }
                else if(o.hasName())
                {
                    oo.pattr = pycode.names.items[oo.oparg];
                }
                else if(o.hasJrel())
                {
                    oo.pointerDest = i+oo.oparg;
                    jumptable.from1to2.put(oo.offset, oo.pointerDest);
                    oo.pattr = Integer.toString(oo.pointerDest);
                }
                else if(o.hasJabs())
                {
                    oo.pointerDest = oo.oparg;
                    jumptable.from1to2.put(oo.offset, oo.pointerDest);
                    oo.pattr = Integer.toString(oo.pointerDest); //not in the std libraries, but in Decompyle.
                }
                else if(o.hasLocal())
                {
                    oo.pattr = pycode.varnames.items[oo.oparg];
                }
                else if(o.hasCompare())
                {
                    oo.pattr = p.cmp_ops()[oo.oparg];
                }
                else if(o.hasFree())
                {
                    if(oo.oparg<pycode.cellvars.items.length)
                    {
                        oo.pattr = pycode.cellvars.items[oo.oparg];
                    }
                    else
                    {
                        oo.pattr = pycode.freevars.items[oo.oparg-pycode.cellvars.items.length];
                    }
                }
            }
            /*switch(o)
            {
                case SET_LINENO:
                    //continue;
                case BUILD_LIST:
                case BUILD_TUPLE:
                case BUILD_SLICE:
                //case UNPACK_LIST:
                //case UNPACK_TUPLE:
                case UNPACK_SEQUENCE:
                case MAKE_FUNCTION:
                case CALL_FUNCTION:
                case MAKE_CLOSURE:
                case CALL_FUNCTION_VAR:
                case CALL_FUNCTION_KW:
                case CALL_FUNCTION_VAR_KW:
                case DUP_TOPX:
                    int dummy=0;
                    break;
            }*/
            rv.add(oo);
        }

        jumptable.finishup();
        jumptable.finishup2(rv);
        
        //return rv;
    }

    public void printTokens()
    {
        for(OpInfo oi: rv)
        {
            m.msg(oi.toDebugString());
        }
    }

    /*HashMap<Integer,Integer> find_jump_targets(PyString s)
    {
        HashMap<Integer,Integer> result = new HashMap();
        //for(int i=0;i<s.rawstr.length;i++)
        int i=0;
        while(i<s.rawstr.length)
        {
            byte c = s.rawstr[i]; i++;
            op o = op.get(c);
            if(o==null)
            {
                int dummy=0;
            }
            if(o.hasArgument())
            {
                byte b1 = s.rawstr[i]; i++;
                byte b2 = s.rawstr[i]; i++;
                int oparg = b.BytesToInt32(b1, b2, (byte)0, (byte)0);
                //int oparg = b.ByteToInt32(s.rawstr[i]) + b.ByteToInt32(s.rawstr[i+1])*256;
                if(o.hasJrel())
                {
                    int label = i + oparg;
                    Integer val = result.get(label);
                    if(val==null) val = 0;
                    val++;
                    result.put(label, val);
                }
                if(o.hasJabs())
                {
                    int label = oparg;
                    Integer val = result.get(label);
                    if(val==null) val = 0;
                    val++;
                    result.put(label, val);
                    //m.throwUncaughtException("This is skipped by Decompyle.  Should we skip it too?  It says TODO in Decompyle.");
                }
            }
        }
        return result;
    }*/

    public static class JumpInfo
    {
        HashMap<Integer, Integer> from1to2 = new HashMap();

        //HashMap<Integer, HashSet<Integer>> from2to1 = new HashMap();
        HashMap<Integer, ArrayList<Integer>> from2to1 = new HashMap();
        //HashMap<Integer, Integer> numRefsPointingHere = new HashMap();
        
        public void finishup()
        {
            //Create the from2to1 map
            for(java.util.Map.Entry<Integer,Integer> entry: from1to2.entrySet())
            {
                Integer key = entry.getKey();
                Integer val = entry.getValue();

                //create reverse map.
                //from2to1.put(val, key);
                //HashSet<Integer> refs = from2to1.get(val);
                ArrayList<Integer> refs = from2to1.get(val);
                if(refs==null)
                {
                    //refs = new HashSet<Integer>();
                    refs = new ArrayList<Integer>();
                    from2to1.put(val, refs);
                }
                refs.add(key);

                //count refs pointing here.
                //Integer curNumRefsPointingHere = numRefsPointingHere.get(val);
                //if(curNumRefsPointingHere==null) curNumRefsPointingHere = 0;
                //curNumRefsPointingHere++;
                //numRefsPointingHere.put(val, curNumRefsPointingHere);
                
            }

        }

        public void finishup2(Vector<OpInfo> rv)
        {
            for(OpInfo oi: rv)
            {
                ArrayList<Integer> refs = from2to1.get(oi.offset);
                oi.pointerSources = refs; //might be null.
            }
        }
    }

    JumpInfo find_jumps(PyString s)
    {
        //HashMap<Integer,Integer> result = new HashMap();
        JumpInfo result = new JumpInfo();
        //for(int i=0;i<s.rawstr.length;i++)
        int i=0;
        while(i<s.rawstr.length)
        {
            int offset = i;
            byte c = s.rawstr[i]; i++;
            op o = op.get(c);
            if(o==null)
            {
                int dummy=0;
            }
            if(o.hasArgument())
            {
                byte b1 = s.rawstr[i]; i++;
                byte b2 = s.rawstr[i]; i++;
                int oparg = b.BytesToInt32(b1, b2, (byte)0, (byte)0);
                //int oparg = b.ByteToInt32(s.rawstr[i]) + b.ByteToInt32(s.rawstr[i+1])*256;
                if(o.hasJrel())
                {
                    int label = i + oparg;
                    /*Integer val = result.get(label);
                    if(val==null) val = 0;
                    val++;
                    result.put(label, val);*/
                    result.from1to2.put(offset, label);
                    //result.from2to1.put(label, offset);
                }
                if(o.hasJabs())
                {
                    int label = oparg;
                    /*Integer val = result.get(label);
                    if(val==null) val = 0;
                    val++;
                    result.put(label, val);*/
                    //m.throwUncaughtException("This is skipped by Decompyle.  Should we skip it too?  It says TODO in Decompyle.");
                    result.from1to2.put(offset,label);
                    //result.from2to1.put(label,offset);
                }
            }
        }
        result.finishup();
        return result;
    }
}

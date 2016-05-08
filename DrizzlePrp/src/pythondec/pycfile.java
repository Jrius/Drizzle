/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import shared.e;
import shared.IBytestream;
import shared.m;
import pythondec3.ast.Module;
import java.util.ArrayList;
import pythondec3.ast.Tok;
import pythondec3.ast.StmtList;
import java.util.LinkedHashSet;
import java.util.ArrayDeque;

public class pycfile
{
    public int magicnum;
    public int timestamp;
    public PyCode code;

    PythonVersions p;
    //Disassemble dis;
    Module root;
    String sourcecode;

    public String filename; //optional.

    public static pycfile createFromFilename(String filename)
    {
        return new pycfile(shared.SerialBytestream.createFromFilename(filename));
    }
    public pycfile(IBytestream c/*, PythonVersions p*/)
    {
        magicnum = c.readInt();
        if(magicnum==PythonVersions.Python22().MagicNumber())
        {
            p = PythonVersions.Python22();
            c.set("pyver", 22);
        }
        else if(magicnum==PythonVersions.Python23().MagicNumber())
        {
            p = PythonVersions.Python23();
            c.set("pyver", 23);
        }
        else
        {
            m.throwUncaughtException("Unexpected/no magic number.");
        }
        timestamp = c.readInt();
        code = PyObject.read(c).cast();

    }
    private pycfile(){}

    public boolean compare(pycfile pyc2)
    {
        if(this.magicnum!=pyc2.magicnum) return false;
        //ignore timestamp
        if(!this.code.compare(pyc2.code)) return false;
        return true;
    }

    public void decompile()
    {
        //if we haven't already disassembled, do that.
        if(code.tokens==null)
        {
            disassemble();
        }

        if(root==null)
        {
            root = new Module(decompile2(code,p),code);
        }
    }

    public String generateSourceCode()
    {
        if(root==null)
        {
            decompile();
        }

        if(sourcecode==null)
        {
            pythondec3.ast.sgen s = new pythondec3.ast.sgen();
            root.gen(s);
            sourcecode = s.getGeneratedSource();
        }

        return sourcecode;
    }

    private static StmtList decompile2(PyCode pycode, PythonVersions p)
    {
        if(pythondec3.stats.printTiming) m.marktime("Starting parsing");
        pycode.root = (pythondec3.ast.StmtList)pythondec3.lpg_parser.Deflatten(pycode.tokens,pycode.pd22,pycode);
        if(pycode.root==null) m.msg("Problem in Object: "+pycode.toString());
        //_roots.add(pycode.root);
        if(pythondec3.stats.printTiming) shared.m.marktime("Done parsing");

        //do each PyCode const object.
        for(PyObject constobj: pycode.consts.items)
        {
            if(constobj instanceof PyCode)
            {
                PyCode childcode = (PyCode)constobj;
                decompile2(childcode,p);
            }
        }

        return pycode.root;
    }
    public void disassemble()
    {
        disassemble2(code,p);
        code.debugname = "(root module)";

        if(pythondec3.stats.printTiming) m.marktime("Starting to find globals");
        ArrayDeque<PyCode> codestack = new ArrayDeque();
        //codestack.push(code);
        getglobals(code,codestack);
        if(pythondec3.stats.printTiming) m.marktime("Done finding globals");
        //printGlobals();
    }
    public void printGlobals()
    {
        printGlobals2(code);
    }
    private static void printGlobals2(PyCode pycode)
    {
        m.msg("Class/function: "+pycode.name.toString());
        for(String glo: pycode.globals)
        {
            m.msg(glo);
        }
        m.msg();

        for(PyCode childcode: pycode.children)
        {
            printGlobals2(childcode);
        }

    }
    private static boolean getAdvancedGlobals(String globalname,ArrayDeque<PyCode> codestack,PyCode pycode)
    {
        //this could be a lot more efficient if we kept track of the names we had seen, rather than go back over them each time a LOAD_GLOBAL is encountered.
        //but in practise, timing of the pots files went from 1:10->1:10 went this was disabled entirely :P

        //String globalname = t.getName(null);
        //search the values on the stack of PyCode objects
        //even if a LOAD_FAST or LOAD_NAME occurs after the pycode's LOAD_CONST definition, it is still bound by a closure.
        //if there are no LOAD_FAST or LOAD_NAME on a pycode on the stack, except for the root pycode(i.e. module), then add to globals.
        //int i = 0;

        //boolean isglobal = true;
        PyCode[] codestack2 = codestack.toArray(new PyCode[0]);

        for(int i=codestack2.length-1;i>=0;i--) //for each function/class higher in the stack
        {
            PyCode curcode = codestack2[i];

            //if it is a global in this function/class, it is a global here, as nothing has gotten in the way!
            String[] globs = curcode.globals.toArray(new String[0]); //java had a bizarre bug, where iterating over the set directly.
            for(String curglob: globs)
            {
                if(globalname.equals(curglob))
                {
                    return true;
                }
            }

            for(Tok curt: curcode.tokens)  //for each token
            {
                if( curt.oi!=null && (curt.oi.o==op.STORE_NAME || curt.oi.o==op.STORE_FAST)) //we should have more here
                {
                    String curname = curt.getName(null);
                    if(curname.equals(globalname))
                    {
                        //if this is not the root, it should not be added to globals.
                        if(i!=0)
                        {
                            return false;
                            //isglobal = false;
                            //break;
                        }
                    }
                }
            }

            //if(!isglobal) break;
            //i++;
        }

        //if(isglobal) pycode.globals.add(globalname);
        return false; //because it will just default to global then, as there is nothing in any other scopes

    }
    private static void getglobals(PyCode pycode, ArrayDeque<PyCode> codestack)
    {
        pycode.globals = new LinkedHashSet();
        for(Tok t: pycode.tokens)
        {
            if(t.oi!=null && t.oi.o!=null)
            {
                if(t.oi.o==op.STORE_GLOBAL)
                {
                    pycode.globals.add(t.getName(null));
                }
                else if(t.oi.o==op.DELETE_GLOBAL)
                {
                    pycode.globals.add(t.getName(null));
                }
                else if(t.oi.o==op.LOAD_GLOBAL)
                {
                    if(pythondec3.options.writeLoadGlobals)
                    {
                        String globalname = t.getName(null);
                        if(getAdvancedGlobals(globalname,codestack,pycode))
                            pycode.globals.add(globalname);
                    }
                }
            }
        }

        codestack.push(pycode);

        //do each PyCode const object.
        for(PyObject constobj: pycode.consts.items)
        {
            if(constobj instanceof PyCode)
            {
                PyCode childcode = (PyCode)constobj;
                //codestack.push(childcode);
                getglobals(childcode,codestack);
                //codestack.pop();
            }
        }

        codestack.pop();
    }
    private static void disassemble2(PyCode pycode, PythonVersions p)
    {
        if(pythondec3.stats.printTiming) m.marktime("Starting disassembling");
        Disassemble disassembledCode = new Disassemble(pycode,p);
        //disassembledCode.printTokens();

        //initialising
        if(pythondec3.stats.printTiming) m.marktime("Starting lpg init");
        pycode.lexstream = new lpg.runtime.LexStream();
        pycode.pd22 = new pythondec3.PythonDec22(pycode.lexstream);
        pycode.prsstream = pycode.lexstream.getIPrsStream();

        if(pythondec3.stats.printTiming) m.marktime("Starting the token production");
        pycode.tokens = pythondec3.lpg_parser.MakeTokens(disassembledCode,pycode.prsstream);

        if(pycode.name.toString().equals("PyString: _build_ast"))
        {
            int dummy=0;
        }

        //regenerate structure
        if(p.recreateStructure() && pythondec3.options.regenerateStructure)
        {
            pythondec3.regenerator regen = new pythondec3.regenerator(pycode);
            regen.regenerateStructure();
        }

        //add tokens to stream
        if(pythondec3.stats.printTiming) shared.m.marktime("Starting adding tokens");
        for(Tok token: pycode.tokens)
        {
            pycode.prsstream.addToken(token);
        }

        //update the streamlength
        pycode.prsstream.setStreamLength();

        if(pythondec3.stats.printDisassembly)
        {
            m.msg("PyCode Object: "+pycode.toString());
            for(Tok t: pycode.tokens) m.msg(t.toString());
        }

        //if(pythondec3.stats.printTiming) m.marktime("Starting to find globals");
        //pycode.globals = pythondec3.decompile.getglobals(pycode.tokens);

        //m.marktime("Starting parsing");
        //pycode.root = (pythondec3.ast.StmtList)lpg_parser.Deflatten(disassembledCode,tokens,pd22);
        //_roots.add(pycode.root);
        //shared.m.marktime("Done parsing");

        //do each PyCode const object.
        pycode.children = new ArrayList();
        for(PyObject constobj: pycode.consts.items)
        {
            if(constobj instanceof PyCode)
            {
                PyCode childcode = (PyCode)constobj;
                pycode.children.add(childcode);
                disassemble2(childcode,p);
            }
        }

        //return pycode.root;
    }

    public static pycfile create(PyCode code, PythonVersions p)
    {
        pycfile r = new pycfile();
        r.code = code;
        r.magicnum = p.MagicNumber();
        r.timestamp = 0; //not right!
        m.warn("fixme: creating incorrect timestamp.");
        return r;
    }
    public byte[] compile()
    {
        shared.IBytedeque c = new shared.Bytedeque2(shared.Format.none);
        c.writeInt(magicnum);
        c.writeInt(timestamp);
        code.marshal(c);
        return c.getAllBytes();
    }
    public void printTokens()
    {
        printTokens2(this.code);
    }
    private static void printTokens2(PyCode pycode)
    {
        m.msg("file/class/func name: "+pycode.name.toString());
        for(Tok token: pycode.tokens)
        {
            m.msg(token.toString());
        }
        m.msg();
        m.msg();

        for(PyObject constobj: pycode.consts.items)
        {
            if(constobj instanceof PyCode)
            {
                PyCode childcode = (PyCode)constobj;
                printTokens2(childcode);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

import shared.m;
import shared.IBytestream;
import shared.ByteArrayBytestream;
import shared.FileUtils;
import pythondec.*;
import pythondec3.ast.Ast;
import java.util.ArrayList;
import pythondec3.ast.sgen;
import pythondec3.ast.Tok;
import java.util.LinkedHashSet;
import java.util.Set;
import java.io.File;
import java.util.Comparator;
import pythondec3.ast.Module;

public class decompile
{
    static ArrayList<Ast> _roots = new ArrayList();

    public static boolean testallupto(String pycfile)
    {
        File pydir = new File(pycfile);
        File folder = pydir.getParentFile();
        File[] files = folder.listFiles();
        java.util.Arrays.sort(files);
        for(File file: files)
        {
            //File f = new File(file);
            if(file.getName().endsWith(".py"))
            {
                boolean aresame = decompile.decompileandcompare(file.getAbsolutePath());
                if(!aresame)
                {
                    m.msg("Not all tests passed. Failed on file: "+file);
                    return false;
                }
            }
            if(file.getName().equals(pydir.getName()))
            {
                //we're done!
                m.msg("All tests passed!");
                return true;
            }
        }
        m.msg("Done all files in dir.");
        return true;
    }
    public static void disassemble(String pycfile)
    {
        IBytestream bs = shared.ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(pycfile));
        pycfile pyc = new pycfile(bs);
        pyc.disassemble();
        pyc.printTokens();
    }

    public static boolean arePycFilesEqual(String pycfile1, String pycfile2)
    {
        IBytestream bs1 = shared.ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(pycfile1));
        IBytestream bs2 = shared.ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(pycfile2));
        pycfile pyc1 = new pycfile(bs1);
        pycfile pyc2 = new pycfile(bs2);
        pyc1.disassemble();
        pyc2.disassemble();
        boolean decompile = true;
        if(decompile)
        {
            pyc1.decompile();
            pyc2.decompile();
            pyc1.generateSourceCode();
            pyc2.generateSourceCode();
        }
        boolean areequal = pyc1.compare(pyc2);
        return areequal;
    }

    public static void compileWithCpython(String infile, String debugfilename, String outfile)
    {
        String pathtopython22 = "C:/Python22/python.exe";
        String pathtopython23 = "C:/Python23/python.exe";
        boolean usepython23 = shared.State.AllStates.getStateAsBoolean("pydec23");
        String pathtopython = usepython23?pathtopython23:pathtopython22;

        //infile = infile.replace("/", "\\");
        //outfile = outfile.replace("/", "\\");

        //Python2.2 doesn't allow the doraise option
        //String cmd = "import py_compile; py_compile.compile('"+infile+"','"+outfile+"','"+debugfilename+"'True);";
        String cmd = "import py_compile; py_compile.compile('"+infile+"','"+outfile+"','"+debugfilename+"');";
        int errorlevel = shared.Exec.RunAndWait(pathtopython, new String[]{"-c",cmd});
        if(errorlevel!=0)
        {
            //1 means exception
            m.cancel("Command failed: "+Integer.toString(errorlevel));
        }
        if(!FileUtils.Exists(outfile))
        {
            m.cancel("Pyc file not created, perhaps because of invalid source code.");
        }
        
        //m.msg("Command went well!");
    }
    public static void compileanddisassemble(String sourcefile)
    {
        String origpotsfolder = "D:/a/leftoff/pythonfiles/potsorig";
        String tempoutputfolder = "D:/delme/tempdecompiletest";

        String pyname = (new File(sourcefile)).getName().replace(".pyc", ".py");
        //String source = sourcefile + "_dis";
        String source = sourcefile.replace(".pyc", ".py");

        File f = new File(pyname);
        String filename = f.getName();
        String origpy = tempoutputfolder+"/"+filename;//+"c_dis";
        String origpyc = tempoutputfolder+"/"+filename+"c";//+"c";
        String decopy = tempoutputfolder+"/"+filename+".drizzle.py";//c_dis";
        String decopyc = tempoutputfolder+"/"+filename+".drizzle.pyc";

        shared.FileUtils.CopyFile(source, origpy, true, true);

        compileWithCpython(origpy,pyname,origpyc);

        //String decsource = decompile(origpyc);
        pycfile pyc = new pycfile(ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(origpyc)));
        pyc.disassemble();
        pyc.printTokens();
    }
    public static boolean decompileandcompare(String sourcefile)
    {
        String origpotsfolder = "D:/a/leftoff/pythonfiles/potsorig";
        String tempoutputfolder = "D:/delme/tempdecompiletest";

        String pyname = (new File(sourcefile)).getName().replace(".pyc", ".py");
        //String source = sourcefile + "_dis";
        String source = sourcefile.replace(".pyc", ".py");

        File f = new File(pyname);
        String filename = f.getName();
        String origpy = tempoutputfolder+"/"+filename;//+"c_dis";
        String origpyc = tempoutputfolder+"/"+filename+"c";//+"c";
        String decopy = tempoutputfolder+"/"+filename+".drizzle.py";//c_dis";
        String decopyc = tempoutputfolder+"/"+filename+".drizzle.pyc";

        //delete old files so there is no confusion.
        FileUtils.DeleteFile(origpy, true);
        FileUtils.DeleteFile(origpyc, true);
        FileUtils.DeleteFile(decopy, true);
        FileUtils.DeleteFile(decopyc, true);

        shared.FileUtils.CopyFile(source, origpy, true, true);

        compileWithCpython(origpy,pyname,origpyc);

        //String decsource = decompile(origpyc);
        pycfile pyc = new pycfile(ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(origpyc)));
        String decsource = pyc.generateSourceCode();
        shared.FileUtils.WriteFile(decopy, shared.b.StringToBytes(decsource));

        compileWithCpython(decopy,pyname,decopyc);

        Boolean areequal = decompile.arePycFilesEqual(origpyc, decopyc);
        if(areequal)
        {
            m.msg("The pyc files match!  ("+sourcefile+")");
            return true;
        }
        else
        {
            m.msg("The pyc files do not match.  ("+sourcefile+")");
            return false;
        }

        //compile original;
        //String source = decompile(infile);
        //String decompylesourcefile = origpotsfolder+f.getName().replace(".pyc", ".pyc_dis");
        //String decompyledestfile = tempoutputfolder+f.getName().replace(".pyc", ".pyc_dis");
        //String tempoutputfile = tempoutputfolder+f.getName()+"_drizzledis";
        ////copy source files:
        //shared.FileUtils.WriteFile(tempoutputfile, shared.b.StringToBytes(source));
        //shared.FileUtils.CopyFile(decompylesourcefile, decompyledestfile, true, false);

        //compile:
        //String cmd = "import sys;";
        //infile,outfile,filename inside the pyc,doraiseexception
    }

    public static String decompile(String infile)
    {
        stats.reset();
        if(stats.printTiming) m.marktime("Starting reading");
        IBytestream c = ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(infile));
        //return decompile(c);

        pycfile pyc = new pycfile(c);
        pyc.disassemble();
        pyc.decompile();
        String r = pyc.generateSourceCode();
        //if(stats.printDecompilation)
        //{
            m.msg(r);
        //}

        if(stats.printStatistics)
        {
            stats.printReport();
        }

        return r;
    }
    /*public static String decompile(IBytestream c)
    {
        _roots.clear();

        m.marktime("Starting demarshalling");
        //pythondec.PythonXX p = pythondec.PythonVersions.Python22();
        Demarshal demarshalledCode = new Demarshal(c);

        //recursively disassemble and deflatten pycode
        Ast root2 = decompile2(demarshalledCode.code,demarshalledCode.p);
        Module root = new Module(root2);

        //generate source code
        sgen s = new sgen();
        root.gen(s);
        String sourcecode = s.getGeneratedSource();
        m.msg("Sourcecode:");
        m.msg(sourcecode);

        Object roots = _roots;
        int dummy=0;
        return sourcecode;
    }*/
    /*public static Ast decompile2(PyCode pycode, PythonVersions p)
    {
        m.marktime("Starting disassembling");
        Disassemble disassembledCode = new Disassemble(pycode,p);
        //disassembledCode.printTokens();

        //initialising
        m.marktime("Starting lpg init");
        lpg.runtime.ILexStream lexstream = new lpg.runtime.LexStream();
        pythondec3.PythonDec22 pd22 = new pythondec3.PythonDec22(lexstream);
        lpg.runtime.IPrsStream prsstream = lexstream.getIPrsStream();

        m.marktime("Starting the token production");
        ArrayList<Tok> tokens = lpg_parser.MakeTokens(disassembledCode,prsstream);
        for(Tok t: tokens) m.msg(t.toString());

        m.marktime("Starting to find globals");
        pycode.globals = getglobals(tokens);

        m.marktime("Starting parsing");
        pycode.root = (pythondec3.ast.StmtList)lpg_parser.Deflatten(tokens,pd22);
        _roots.add(pycode.root);
        shared.m.marktime("Done parsing");

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
    }*/
    /*public static LinkedHashSet<String> getglobals(ArrayList<Tok> tokens)
    {
        LinkedHashSet<String> globals = new LinkedHashSet();
        for(Tok t: tokens)
        {
            if(t.oi!=null && t.oi.o!=null)
            {
                if(t.oi.o==op.STORE_GLOBAL)
                {
                    globals.add(t.getName(null));
                }
                else if(t.oi.o==op.DELETE_GLOBAL)
                {
                    globals.add(t.getName(null));
                }
                else if(t.oi.o==op.LOAD_GLOBAL)
                {
                    if(options.writeLoadGlobals) globals.add(t.getName(null));
                }
            }
        }
        return globals;
    }*/
}

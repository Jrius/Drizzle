/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package onliner;

import java.util.List;
import shared.*;
import pythondec.pycfile;
import java.util.ArrayList;
import java.io.File;

public class Python
{
    
    public static boolean CheckForProblems(String pakfile)
    {
        final int pythonVersion = 22;

        //File pakfile2 = new File(pakfile);

        boolean success = true;
        prpobjects.pakfile pak = prpobjects.pakfile.create(pakfile, auto.AllGames.getPots());
        List<pycfile> pycs = pak.extractPakFile(true);
        for(pycfile pyc: pycs)
        {
            String filename = pyc.filename;

            //decompile
            pyc.disassemble();
            pyc.decompile();
            String py_orig = pyc.generateSourceCode();

            for(String problem: onliner.patches.problems)
            {
                int ind = py_orig.indexOf(problem);
                if(ind!=-1)
                {
                    success = false;
                    m.warn("Python problem found: ",problem," in file: ",filename);
                    m.warn("Dumping source:");
                    m.msg("*********************************************");
                    m.msg(py_orig);
                    m.msg("*********************************************");
                }
            }

        }

        return success;
    }
    
    //returning null indiciates that the file did not change.
    public static byte[] PatchPak(String pakfile)
    {
        final int pythonVersion = 22;
        
        File pakfile2 = new File(pakfile);

        boolean changed = false;
        prpobjects.pakfile pak = prpobjects.pakfile.create(pakfile, auto.AllGames.getPots());
        List<pycfile> pycs = pak.extractPakFile(true);
        for(pycfile pyc: pycs)
        {
            String filename = pyc.filename;
            //m.msg("Filename: ",filename);

            //decompile
            pyc.disassemble();
            pyc.decompile();
            String py_orig = pyc.generateSourceCode();
            String py_new = py_orig;

            //patch
            for(patches.patch patch: patches.patches)
            {
                py_new = py_new.replace(patch.old_str, patch.new_str);
            }

            //save if changed
            if(!py_new.equals(py_orig))
            {
                changed = true;
                m.msg("Changed py: ",filename);
                //FileUtils.WriteFile("c:/python22/bimevi2.py", b.StringToBytes(py_new));
                String compiler = cpythoncompiler.Compiler.FindCPythonVerFromList(pythonVersion, cpythoncompiler.Compiler.CommonPythonPaths);
                byte[] py_new_bs = b.StringToBytes(py_new);
                byte[] pyc_new_bs = cpythoncompiler.Compiler.Compile(py_new_bs, filename, pythonVersion, compiler);
                prpobjects.pakfile.PythonObject pyobj = pak.findByFilename(filename);
                pyobj.rawCompiledPythonObjectData = pyc_new_bs;
            }
        }

        //write modified pak file
        if(changed)
        {
            m.msg("Changed pak: ",pakfile);
            byte[] pak_new_bs = pak.compileEncrypted(Format.pots);
            //String newfilename = pakfile+"_new.pak";
            //FileUtils.WriteFile(newfilename, pak_new_bs, true, true);
            return pak_new_bs;
        }
        else
        {
            return null;
        }
    }

    public static void ComparePaks(String origpak, String origgamename, String newpak, String newgamename)
    {

        prpobjects.pakfile pak1 = prpobjects.pakfile.create(origpak, auto.AllGames.get(origgamename));
        prpobjects.pakfile pak2 = prpobjects.pakfile.create(newpak, auto.AllGames.get(newgamename));
        List<pycfile> pycs1 = pak1.extractPakFile(true);
        List<pycfile> pycs2 = pak2.extractPakFile(true);

        if(pycs1.size()!=pycs2.size())
        {
            //m.msg("Not the same number of files in each one.");
        }

        for(pythondec.pycfile pyc1: pycs1)
        {
            String filename = pyc1.filename;
            //m.msg("Filename: ",filename);
            pycfile pyc2 = Find(pycs2,filename);
            if(pyc2==null)
            {
                m.msg("Python file removed: ",filename);
                continue;
            }

            //decompile
            pyc1.disassemble();
            pyc1.decompile();
            String py1 = pyc1.generateSourceCode();
            pyc2.disassemble();
            pyc2.decompile();
            String py2 = pyc2.generateSourceCode();

            //split into lines
            ArrayList<String> lines1 = new ArrayList();
            for(String s: py1.split("\n")) lines1.add(s);
            ArrayList<String> lines2 = new ArrayList();
            for(String s: py2.split("\n")) lines2.add(s);

            //diff
            difflib.Patch patch = difflib.DiffUtils.diff(lines1, lines2);
            int contextSize = 3; //3 lines of context around patches
            if(patch.getDeltas().isEmpty())
            {
                //m.msg("identical");
                m.msg("Python file unchanged: ",filename);
            }
            else
            {
                m.msg("Python file changed: ",filename);
                List<String> unififf = difflib.DiffUtils.generateUnifiedDiff("infile.py", "outfile.py", lines1, patch, contextSize);
                for(String s: unififf) m.msg(s);
            }
            
        }

        //look for new python files not in old one.
        for(pycfile pyc2: pycs2)
        {
            String filename = pyc2.filename;
            pycfile pyc1 = Find(pycs1,filename);
            if(pyc1==null)
            {
                m.msg("Python file added: ",filename);
            }
        }
    }
    private static pycfile Find(List<pycfile> pycs, String filename)
    {
        for(pycfile pyc: pycs)
        {
            if(pyc.filename.equals(filename)) return pyc;
        }
        return null;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cpythoncompiler;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import shared.*;

public class Compiler
{
    public static final String[] CommonPythonPaths = {
        "C:/Python22/python.exe",
        "C:/Python23/python.exe",
        "/usr/bin/python2.2",
        "/usr/bin/python2.3",
    };

    public static String FindCPythonVerFromList(int pythonVersion, String... PossiblePaths)
    {
        String pyver = GetVersionStrFromInt(pythonVersion);
        for(String PossiblePath: PossiblePaths)
        {
            if(FileUtils.Exists(PossiblePath))
            {
                String ver = GetVersion(PossiblePath);
                if(pyver.equals(ver)) return PossiblePath;
            }
        }
        return null;
    }

    private static String GetVersionStrFromInt(int pythonVersion)
    {
        switch(pythonVersion)
        {
            case 22: return "2.2";
            case 23: return "2.3";
            case 24: return "2.4";
            case 25: return "2.5";
            default: return null;
        }
    }

    public static String GetVersion(String CPythonPath)
    {
        String program = shared.GetResource.getResourceAsString("/cpythoncompiler/getver.py");
        program = program.replace("\r\n", "\n");
        program = program.replace("\r", "\n");
        String[] cmdarray = new String[]{"-u","-E","-c",program};
        Exec.ReturnInfo info = Exec.RunAndWaitWithStreams(null, null, CPythonPath, cmdarray);
        if(info==null) return null;
        if(info.stderr.length!=0) m.err(b.BytesToString(info.stderr));
        if(info.returnval!=0) m.err("ErrorVal="+Integer.toString(info.returnval));
        String version = b.BytesToString(info.stdout);
        return version;
    }

    public static byte[] Compile(byte[] source, String pythonFilename, int pythonVersion, String CPythonPath)
    {
        String pythonver = GetVersionStrFromInt(pythonVersion);

        //byte[] bytecode = null;

        String compileProgram = shared.GetResource.getResourceAsString("/cpythoncompiler/compile.py");
        //prepend with some vars
        compileProgram = "pythonver='"+pythonver+"'; filename='"+pythonFilename+"'; \n" + compileProgram;
        compileProgram = compileProgram.replace("\r\n", "\n");
        compileProgram = compileProgram.replace("\r", "\n");
        //compileProgram = compileProgram.replace("\n", "");
        String[] cmdarray = new String[]{
            //CPythonPath,
            "-u", //unbuffered stdin/stdout (makes it binary instead of text)
            "-E", //no environment variables
            "-c", //run the follow string as a program
            compileProgram
        };

        Exec.ReturnInfo info = Exec.RunAndWaitWithStreams(null, source, CPythonPath, cmdarray);

        /*
        //File d = (dir==null)?null:new File(dir);
        File dir = null;
        try{
            java.lang.Process p = Runtime.getRuntime().exec(cmdarray, null, dir);
            InputStream stdout = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            OutputStream stdin = p.getOutputStream();

            //send input
            stdin.write(source);
            //stdin.flush();
            stdin.close();

            //receive output
            bytecode = StreamUtils.ReadAll(stdout);

            //look at stderr
            byte[] errors = StreamUtils.ReadAll(stderr);
            String errorstr = b.BytesToString(errors);
            if(!errorstr.equals("")) m.err(errorstr);

            int errorvalue = p.waitFor();
            if(errorvalue!=0) m.err(Integer.toString(errorvalue));
        }catch(Exception e){
            e.printStackTrace();
            //return -1; //should we use a different value?
        }*/

        if(info==null) return null;

        if(info.stderr.length!=0) m.err(b.BytesToString(info.stderr));
        if(info.returnval!=0) m.err("ErrorVal="+Integer.toString(info.returnval));
        byte[] bytecode = info.stdout;

        return bytecode;

    }
}

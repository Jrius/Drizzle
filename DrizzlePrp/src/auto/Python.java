/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.m;
import shared.b;
import java.io.File;
import prpobjects.pakfile;
import shared.Format;
import shared.*;

public class Python
{
    public static void ListPak(String pakfilename, String gamename)
    {
        auto.AllGames.GameConversionSub game = auto.AllGames.get(gamename);
        pakfile pak = pakfile.create(pakfilename, game);
        for(pakfile.IndexEntry ind: pak.indices)
        {
            m.msg(ind.objectname.toString());
        }
    }
    public static void RemovePythonOverrides(String pakfolder, String outfolder, String overriddenpakfile, String gamename)
    {
        auto.AllGames.GameConversionSub game = auto.AllGames.get(gamename);
        pakfile pak = pakfile.create(pakfolder+"/"+overriddenpakfile, game);
        //for each pak file in the pakfolder, except the overridden one:
        File pakfolder2 = new File(pakfolder);
        for(File child: pakfolder2.listFiles())
        {
            if(child.isFile() && child.getName().endsWith(".pak") && !child.getName().equals(overriddenpakfile))
            {
                //for each py file
                pakfile curpak = pakfile.create(child.getAbsolutePath(), game);
                for(pakfile.IndexEntry ind: curpak.indices)
                {
                    String pyname = ind.objectname.toString();
                    //if this is in the overriddenpakfile, remove it from there.
                    for(int i=0;i<pak.indices.size();i++)
                    {
                        String pyname2 = pak.indices.get(i).objectname.toString();
                        if(pyname.equals(pyname2))
                        {
                            m.msg("Removing py file: ",pyname2," from ",overriddenpakfile," because it is also present in ",child.getName());
                            pak.remove(i);
                            break;
                        }
                    }
                }
            }
        }

        //save this modified pak file
        String outfilename = outfolder+"/"+overriddenpakfile;
        byte[] pakbs = pak.compileEncrypted(game.g.format);
        FileUtils.WriteFile(outfilename, pakbs, true, true);

    }
    public static void UnpackPak(String infile, String outfolder, String gamename)
    {
            m.status("Unpacking pak file...");
            prpobjects.pakfile pak = prpobjects.pakfile.create(infile, auto.AllGames.get(gamename));
            pak.extractPakFile(true, outfolder);
            m.status("Done unpacking!");
    }
    public static void DecompilePakOrPyc(String infile, String outfolder, String gamename)
    {
        if(infile.endsWith(".pak"))
            DecompilePak(infile,outfolder,gamename);
        else if(infile.endsWith(".pyc"))
            DecompilePyc(infile,outfolder);
        else
            m.err("Filename must end with .pyc or .pak");
    }
    public static void DecompilePak(String infile, String outfolder, String gamename)
    {
        m.status("Decompiling pak file...");
        prpobjects.pakfile pak = prpobjects.pakfile.create(infile, auto.AllGames.get(gamename));
        java.util.List<pythondec.pycfile> pycs = pak.extractPakFile(true);
        int curnum = 0;
        int totalnum = pycs.size();
        for(pythondec.pycfile pyc: pycs)
        {
            curnum++;
            m.msg("Decompiling: ",pyc.filename," (file ",Integer.toString(curnum)," of ",Integer.toString(totalnum),")");
            pyc.decompile();
            String source = pyc.generateSourceCode();
            String outfile = outfolder+"/"+pyc.filename;
            shared.FileUtils.WriteFile(outfile, b.StringToBytes(source),true,true);
        }
        m.status("Done decompiling!");
    }
    public static void DecompilePyc(String infile, String outfolder)
    {
        m.status("Decompiling pyc file...");
        pythondec.pycfile pyc = pythondec.pycfile.createFromFilename(infile);
        pyc.decompile();
        String source = pyc.generateSourceCode();
        String outfile = outfolder+"/"+(new File(infile)).getName().replace(".pyc", ".py");
        shared.FileUtils.WriteFile(outfile, b.StringToBytes(source),true,true);
        m.status("Done decompiling!");
    }

}

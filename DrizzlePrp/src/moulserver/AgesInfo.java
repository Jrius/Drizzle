/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.io.File;
import shared.*;
import java.util.HashMap;
import parsers.sdlparser.*;
import parsers.ageparser.*;

public class AgesInfo
{
    AllSdlInfo sdl;
    AllAgeInfo age;
    //AbstractManager manager;

    public AgesInfo(/*AbstractManager manager*//*String rootPath*/String agefolder, String sdlfolder)
    {
        //this.manager = manager;
        //initialize(rootPath);
        initialize(agefolder,sdlfolder);
    }
    public Agefile getAge(String ageFilename)
    {
        Agefile agefile = age.ages.get(ageFilename);
        return agefile;
    }
    public int getSequencePrefixForAge(String ageFilename)
    {
        Agefile agefile = age.ages.get(ageFilename);
        return agefile.sequenceprefix;
    }
    public Statedesc getStatedesc(String name, int version)
    {
        Statedesc sd = sdl.sdls.get(name);
        if(sd==null) m.throwUncaughtException("Statedesc name not found: "+name);
        if(sd.version!=version) m.throwUncaughtException("Statedesc versions don't match. Server expects "+Integer.toString(sd.version)+" but gets "+Integer.toString(version));
        return sd;
    }
    public Statedesc getNewestStatedesc(String name)
    {
        Statedesc sd = sdl.sdls.get(name);
        if(sd==null) m.throwUncaughtException("Statedesc name not found: "+name);
        return sd;
    }
    public void initialize(/*String filepath*/String agefolder, String sdlfolder)
    {
        //String filepath = manager.getPathToCleanFiles();

        //read sdl info
        sdl = new AllSdlInfo();
        //File sdlpath = new File(filepath+"/SDL/");
        File sdlpath = new File(sdlfolder);
        for(File child: sdlpath.listFiles())
        {
            if(child.isFile() && child.getName().toLowerCase().endsWith(".sdl"))
            {
                byte[] data = FileUtils.ReadFile(child);
                Sdlfile sdlfile = parsers.sdlparser.parse(data);
                sdl.addSdlfile(sdlfile);
            }
        }

        //read age info
        age = new AllAgeInfo();
        //File agepath = new File(filepath+"/dat/");
        File agepath = new File(agefolder);
        for(File child: agepath.listFiles())
        {
            if(child.isFile() && child.getName().toLowerCase().endsWith(".age"))
            {
                byte[] data = FileUtils.ReadFile(child);
                data = uru.UruCrypt.DecryptAny(data, auto.AllGames.getMoul().g);
                String agename = child.getName();
                agename = agename.substring(0,agename.length()-".age".length());
                Agefile agefile = parsers.ageparser.parse(data,agename);
                age.addAgefile(agefile);
            }
        }

    }

    public static class AllSdlInfo
    {
        HashMap<String, Statedesc> sdls = new HashMap();
        public AllSdlInfo()
        {
        }

        public void addSdlfile(Sdlfile sdlfile)
        {
            for(Statedesc statedesc: sdlfile.statedescs)
            {
                Statedesc curstatedesc = sdls.get(statedesc.name);
                if(curstatedesc==null || statedesc.version>=curstatedesc.version)
                {
                    if(curstatedesc!=null && curstatedesc.version==statedesc.version) m.warn("Two statedescs have the same version: "+statedesc.name);
                    //use this one instead.
                    sdls.put(statedesc.name, statedesc);
                }
            }
        }
    }
    public static class AllAgeInfo
    {
        HashMap<String, Agefile> ages = new HashMap();
        public AllAgeInfo(){}
        public void addAgefile(Agefile agefile)
        {
            ages.put(agefile.agename, agefile);
        }
    }

}

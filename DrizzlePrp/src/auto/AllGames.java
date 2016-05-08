/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.*;
import java.util.Vector;
import auto.conversion.FileInfo;
import java.io.File;

public class AllGames
{
    private static GameConversionSub _hexisle;
    private static GameConversionSub _moul;
    private static GameConversionSub _crowthistle;
    private static GameConversionSub _mystv;
    private static GameConversionSub _pots;
    private static GameConversionSub _mqo;
    public static GameConversionSub get(String gamename)
    {
        return get(Game.getFromName(gamename));
    }
    public static GameConversionSub get(Game game)
    {
        switch(game)
        {
            case hexisle:
                return getHexisle();
            case moul:
                return getMoul();
            case crowthistle:
                return getCrowthistle();
            case mystv:
                return getMystV();
            case pots:
                return getPots();
            case mqo:
                return getMqo();
            default:
                throw new shared.uncaughtexception("unexpected");
        }
    }
    public static GameConversionSub getMqo()
    {
        if(_mqo==null) _mqo = new GameConversionSub(magiquest.getGameInfo());
        return _mqo;
    }
    public static GameConversionSub getHexisle()
    {
        if(_hexisle==null) _hexisle = new GameConversionSub(hexisle.getGameInfo());
        return _hexisle;
    }
    public static GameConversionSub getMoul()
    {
        if(_moul==null) _moul = new GameConversionSub(moul.getGameInfo());
        return _moul;
    }
    public static GameConversionSub getCrowthistle()
    {
        if(_crowthistle==null) _crowthistle = new GameConversionSub(crowthistle.getGameInfo());
        return _crowthistle;
    }
    public static GameConversionSub getMystV()
    {
        if(_mystv==null) _mystv = new GameConversionSub(mystv.getGameInfo());
        return _mystv;
    }
    public static GameConversionSub getPots()
    {
        if(_pots==null) _pots = new GameConversionSub(pots.getGameInfo());
        return _pots;
    }

    public static class EmbeddedFileInfo
    {
        String drizzlePath;
        String uruPath;
    }
    public static class GameInfo
    {
        String GameName;
        //public int readversion;
        String DetectionFile;
        String[] MusicFiles;
        //String[] AviFiles;
        public Format format;
        public String prpMarkerForAgename;
        public int PythonVersion;
        public Game game;
        
        //conversion stuff:
        //conversion.Info info;
        //Vector<String> allfiles;
        conversion.RenameInfo renameinfo = new conversion.RenameInfo();
        Vector<conversion.FileInfo> allfiles = new Vector();
        prpobjects.prputils.Compiler.Decider decider;
        conversion.PostConversionModifier prpmodifier;
        conversion.FniModifier fnimodifier;
        conversion.AgeModifier agemodifier;
        Vector<AllGames.EmbeddedFileInfo> embeddedfiles = new Vector();
        Vector<String> automods = new Vector();
        //Vector<auto.inplace.Inplace.InplaceModInfo> inplacemods = new Vector();
        Vector<String> inplacemods = new Vector();


        public void addAgeFiles(String agename, String[] filenames)
        {
            for(String filename: filenames)
            {
                FileInfo fi = new FileInfo();
                fi.agename = agename;
                fi.filename = filename;
                fi.guessFiletype();
                allfiles.add(fi);
            }
        }
        public void addSoundFiles(String[] filenames)
        {
            for(String filename: filenames)
            {
                FileInfo fi = new FileInfo();
                fi.filename = filename;
                fi.guessFiletype();
                allfiles.add(fi);
            }
        }
        public void addAviFiles(String[] filenames)
        {
            for(String filename: filenames)
            {
                FileInfo fi = new FileInfo();
                fi.filename = filename;
                fi.guessFiletype();
                allfiles.add(fi);
            }
        }
        public void addEmbeddedFile(String drizzlePath, String uruPath)
        {
            AllGames.EmbeddedFileInfo fi = new EmbeddedFileInfo();
            fi.drizzlePath = drizzlePath;
            fi.uruPath = uruPath;
            embeddedfiles.add(fi);
        }
        /*public void addInplacemod(String agename, String relpath, String... modnames)
        {
            auto.inplace.Inplace.InplaceModInfo im = new auto.inplace.Inplace.InplaceModInfo();
            im.relpath = relpath;
            im.modnames = modnames;
            im.age = agename;
            inplacemods.add(im);
        }*/
        public void addInplacemods(String... relpaths)
        {
            for(String relpath: relpaths)
            {
                inplacemods.add(relpath);
            }
        }
        public void addAutomods(String[] filenames)
        {
            for(String filename: filenames)
            {
                automods.add(filename);
            }
        }
        public String getNewAgename(FileInfo file)
        {
            String newagename = renameinfo.agenames.get(file.agename);
            if(newagename==null) newagename = file.agename;
            return newagename;
        }

    }



    public static class GameConversionSub
    {
        public GameInfo g;

        public GameConversionSub(GameInfo game)
        {
            g = game;
        }

        public String GetAgenameFromPrpname(String prpfile)
        {
            int lastslash = prpfile.lastIndexOf("/");
            int lastslash2 = prpfile.lastIndexOf("\\");
            int ls = Math.max(lastslash, lastslash2);
            if(ls!=-1) prpfile = prpfile.substring(ls+1);
            int start = prpfile.indexOf(g.prpMarkerForAgename)+g.prpMarkerForAgename.length();
            int end = prpfile.lastIndexOf(".prp");
            String agename = prpfile.substring(start, end);
            return agename;
        }

        public void CopyMusic(String infolder, String potsfolder)
        {
            m.status("Checking the folders you gave...");
            if(!isFolderX(infolder)) return;
            if(!auto.AllGames.getPots().isFolderX(potsfolder)) return;
            if(!uam.Uam.HasPermissions(potsfolder)) return;

            for(String filename: g.MusicFiles)
            {
                String infile = infolder + "/sfx/" + filename;
                String outfile = potsfolder + "/MyMusic/" + filename;

                FileUtils.CopyFile(infile, outfile, true, true);
            }

            m.status("Done copying ",g.GameName," music!");
        }

        public boolean isFolderX(String folder/*, String gamename, String characteristicfile*/)
        {
            File folderfile = new File(folder);
            if(!folderfile.exists())
            {
                m.err("The ",g.GameName," folder you selected doesn't exist.");
                return false;
            }
            if(!folderfile.isDirectory())
            {
                m.err("The ",g.GameName," folder you selected must be a folder, not a file.");
                return false;
            }
            File exe = new File(folder+"/"+g.DetectionFile);
            if(!exe.exists())
            {
                m.err("The ",g.GameName," folder you selected doesn't seem to contain ",g.GameName,".  Please select the folder that contains ",g.DetectionFile);
                return false;
            }

            return true;
        }
        public void ConvertGame(String infolder, String potsfolder)
        {
            m.state.push();
            m.state.curstate.showConsoleMessages = true;
            m.state.curstate.showErrorMessages = true;
            
            boolean debugging = false; // easier to keep track of what Drizzle does to the PRPs
            if (debugging)
            {
                m.state.curstate.showNormalMessages = true;
                m.state.curstate.showWarningMessages = true;
            }
            else
            {
                m.state.curstate.showNormalMessages = false;
                m.state.curstate.showWarningMessages = false;
            }
            m.state.curstate.showStatusMessages = true;

            //verify folders
            m.status("Checking the folders you gave...");
            if(!isFolderX(infolder)) return;
            if(!auto.AllGames.getPots().isFolderX(potsfolder)) return;
            if(!uam.Uam.HasPermissions(potsfolder)) return;

            m.status("Starting conversion of ",g.GameName,"...");
            if(true)
            {
                convert(infolder, potsfolder);
                extractembedded(potsfolder);
                automod(infolder, potsfolder);
                inplacemod(potsfolder);
            }
            else
            {
                m.err("Disable this before release!!!");
                automod(infolder, potsfolder);
                inplacemod(potsfolder);
            }

            m.state.pop();
            m.status("Dont forget to run SoundDecompress.exe in your Pots folder, in order to get the sounds working!  (You can also click the SoundDecompress button on the form if you prefer.) (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
            //m.status("Dont forget to run SoundDecompress.exe; the button is at UAM->SoundDecompress. (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
            m.status("Conversion completed!");

        }
        public void ConvertFiles(String infolder, String outfolder, Vector<String> files)
        {
            m.state.push();
            m.state.curstate.showConsoleMessages = true;
            m.state.curstate.showErrorMessages = true;

            m.state.curstate.showNormalMessages = true;
            m.state.curstate.showWarningMessages = true;
            m.state.curstate.showStatusMessages = true;

            conversion.Info info = new conversion.Info();
            info.infolder = infolder;
            info.outfolder = outfolder;
            info.g = g;
            
            m.status("Conversion of files...");
            for(String filename: files)
            {
                conversion.FileInfo fi = new conversion.FileInfo();
                fi.filename = filename;
                fi.guessFiletype();
                //fi.agename = null; //is this a problem?  yes it is.
                fi.guessAgename();
                m.status("Conversion of " + fi.filename);
                conversion.convertFile(info, fi);
            }
            
            m.state.pop();
            m.status("Conversion completed!");
        }
        public void ExtractPak(String pakfile, String outfolder)
        {
            //byte[] decrypteddata = uru.UruCrypt.DecryptAny(pakfile, this.g);
            prpobjects.pakfile pak = new prpobjects.pakfile(pakfile, g, true);
            pak.extractPakFile(true, outfolder);
        }
        private void inplacemod(String potsfolder)
        {
            auto.inplace.InplaceFile pots = new auto.inplace.InplaceFile(potsfolder,"");
            for(String im: g.inplacemods)
            {
                auto.inplace.Inplace.InplaceMod(pots, im);
            }
        }
        private void automod(String infolder, String outfolder)
        {
            for(String filename: g.automods)
            {
                auto.mod.AutoMod.SimplicityAutoMod(infolder, outfolder, filename);
            }
        }
        private void extractembedded(String potsfolder)
        {
            for(AllGames.EmbeddedFileInfo fi: g.embeddedfiles)
            {
                if(shared.GetResource.hasResource(fi.drizzlePath))
                {
                    Bytes bytes = shared.GetResource.getResourceAsBytes(fi.drizzlePath);
                    bytes.saveAsFile(potsfolder+fi.uruPath);
                }
                else
                {
                    m.throwUncaughtException("Resource not present: "+fi.drizzlePath);
                }
            }
        }
        private void convert(String infolder, String potsfolder)
        {
            conversion.Info info = new conversion.Info();
            info.infolder = infolder;
            info.outfolder = potsfolder;
            info.g = g;
            conversion.convertFiles(info);
        }
    }
}

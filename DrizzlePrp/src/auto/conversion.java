/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.util.Vector;
import shared.FileUtils;
import uru.UruCrypt;
import java.util.HashMap;
import shared.cmap;
import shared.m;
import prpobjects.textfile;
import shared.IBytestream;
import prpobjects.Urustring;
import prpobjects.prpfile;

public class conversion
{
    public static class Info
    {
        //String agename;
        String infolder;
        String outfolder;
        AllGames.GameInfo g;
        
    }
    public static interface AgeModifier
    {
        void ModifyAge(Info info, FileInfo file, textfile tf);
    }
    public static interface FniModifier
    {
        void ModifyFni(Info info, FileInfo file, textfile tf);
    }
    public static interface PostConversionModifier
    {
        void ModifyPrp(Info info, FileInfo file, prpfile prp);
    }
    public static class FileInfo
    {
        String agename;
        String filename;
        Filetype type;

        public void guessFiletype()
        {
            if(filename.endsWith(".fni")) type = Filetype.fni;
            else if(filename.endsWith(".sum")) type = Filetype.sum;
            else if(filename.endsWith(".prp")) type = Filetype.prp;
            else if(filename.endsWith(".age")) type = Filetype.age;
            else if(filename.endsWith(".csv")) type = Filetype.csv;
            else if(filename.endsWith(".ogg")) type = Filetype.ogg;
            else if(filename.endsWith(".sdl")) type = Filetype.sdl;
            else if(filename.endsWith(".pak")) type = Filetype.pak;
            else if(filename.endsWith(".bik")) type = Filetype.bik;

        }
        public String toString(){return filename;}
        public void guessAgename()
        {
            java.io.File f = new java.io.File(filename);
            String name = f.getName();
            int dotpos = name.indexOf(".");
            int undpos = name.indexOf("_");
            int distpos = name.indexOf("_District_");
            if(type==Filetype.prp)
            {
                agename = name.substring(0,undpos); //this is possibly wrong for some games, as an _ in the name can break it.  But I think only fan Ages have that.
            }
            else if(type==Filetype.fni || type==Filetype.sum || type==Filetype.age || type==Filetype.csv)
            {
                agename = name.substring(0,dotpos);
            }
        }
    }
    public static enum Filetype
    {
        fni,sum,prp,age,csv,ogg,sdl,pak,bik,
    }
    public static class RenameInfo
    {
        public HashMap<String, Integer> prefices = new HashMap();
        public HashMap<String, String> agenames = new HashMap();
        public cmap<String,cmap<Integer,Integer>> pagenums = new cmap();
        public cmap<String,cmap<String,String>> pagenames = new cmap();
        public HashMap<String, String> simplefiles = new HashMap();

        public String simplefilesrename(String oldname)
        {
            String newname = simplefiles.get(oldname);
            if(newname==null) newname = oldname;
            return newname;
        }
    }
    public static void convertFiles(Info info)
    {
        for(FileInfo file: info.g.allfiles)
        {
            convertFile(info,file);
        }
    }
    public static void convertFile(Info info, FileInfo file)
    {

        switch(file.type)
        {
            case fni:
                convertFni(info,file);
                break;
            case sum:
                convertSum(info,file);
                break;
            case age:
                convertAge(info,file);
                break;
            case prp:
                convertPrp(info,file);
                break;
            case ogg:
                convertOgg(info,file);
                break;
            case bik:
                convertBik(info,file);
                break;
            case csv:
                convertCsv(info,file);
                break;
            default:
                m.err("Cannot convert this file type: ",file.type.toString());
                break;
        }
    }
    public static void convertPrp(Info info, FileInfo file)
    {
        //String agename = common.getAgenameFromFilename(filename);
        String infile = info.infolder + "/dat/" + file.filename;
        //String outfilename = filename.replaceFirst("_", "_District_");
        //String outfile = outfolder + "/dat/" + outfilename;
        /*String outfile;
        if(file.filename.contains("_District_"))
        {
            outfile = info.outfolder + "/dat/" + file.filename;
        }
        else
        {
            outfile = info.outfolder + "/dat/" + file.filename.replaceFirst("_", "_District_");
        }*/

        IBytestream bytestream = shared.SerialBytestream.createFromFilename(infile);
        uru.context c = uru.context.createFromBytestream(bytestream);
        c.curFile = file.filename; //helpful for debugging.
        c.realreadversion = info.g.game.readversion;

        //modify sequence prefix if Age is in list.
        Integer prefix = info.g.renameinfo.prefices.get(file.agename);
        if(prefix!=null)
        {
            c.sequencePrefix = prefix;
        }

        //modify sequence suffix if Age is in list.
        cmap<Integer,Integer> suffix = info.g.renameinfo.pagenums.get(file.agename);
        if(suffix!=null)
        {
            c.pagenumMap = suffix;
        }

        //modify agename if Age is in list.
        String newAgename = info.g.renameinfo.agenames.get(file.agename);
        if(newAgename!=null)
        {
            c.ageName = newAgename;
        }

        prpobjects.Typeid[] typesToRead = null; //null means real them all.
        prpobjects.prpfile prp = prpobjects.prpfile.createFromContext(c, typesToRead);

        //Change pagename, if applicable.
        String oldpagename = prp.header.pagename.toString();
        String newpagename = (String)info.g.renameinfo.pagenames.get2(file.agename,prp.header.pagename.toString());
        if(newpagename!=null)
        {
            prp.header.pagename = Urustring.createFromString(newpagename);
            //outfile = outfile.replaceFirst("_District_"+oldpagename, "_District_"+newpagename);
        }
        //Change agename, if applicable.
        String oldagename = prp.header.agename.toString();
        String newagename = info.g.renameinfo.agenames.get(file.agename);
        if(newagename!=null)
        {
            prp.header.agename = Urustring.createFromString(newagename);
            auto.postmod.PostMod_MystV.PostMod_ChangeVerySpecialPython(prp, oldagename, newagename);
        }

        //processPrp(prp,agename,agenames,outfolder);
        //do a postProcessPrp callback?
        if(info.g.prpmodifier!=null) info.g.prpmodifier.ModifyPrp(info,file,prp);

        //Bytes prpoutputbytes = prp.saveAsBytes();
        //prpoutputbytes.saveAsFile(outfile);
        String outfile = info.outfolder + "/dat/"+ prp.header.agename.toString() + "_District_" + prp.header.pagename.toString() + ".prp";

        prp.saveAsBytes(info.g.decider).writeAllBytesToFile(outfile);

        //prp.saveAsFile(outfile);

        c.close();

        shared.MemUtils.GarbageCollect();

        //shared.State.AllStates.pop();

    }
    public static void convertOgg(Info info, FileInfo file)
    {
        String infile = info.infolder + "/sfx/" + file.filename;
        String outfile = info.outfolder + "/sfx/" + info.g.renameinfo.simplefilesrename(file.filename);

        FileUtils.CopyFile(infile, outfile, true, true);
    }
    public static void convertAge(Info info, FileInfo file)
    {
        //String agename = common.getAgenameFromFilename(filename);
        String infile = info.infolder + "/dat/" + file.filename;
        String outfile = info.outfolder + "/dat/" + info.g.getNewAgename(file)+".age";

        if(file.agename.toLowerCase().equals("personal")) m.warn("Relto may corrupt your savegame, be sure to back up your /sav folder!");

        //byte[] encryptedData = FileUtils.ReadFile(infile);
        //byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData); //UruCrypt.DecryptEoa(encryptedData);
        byte[] decryptedData = UruCrypt.DecryptAny(infile,info.g);

        //modify sequence prefix if Age is in list.
        Integer prefix = info.g.renameinfo.prefices.get(file.agename);
        if(prefix!=null)
        {
            textfile agefile = textfile.createFromBytes(decryptedData);
            agefile.setVariable("SequencePrefix", Integer.toString(prefix));
            decryptedData = agefile.saveToByteArray();
        }

        //Change pagenames  (I guess I should really create an 'agefile' class and have things like this in there.)
        {
            textfile agefile = textfile.createFromBytes(decryptedData);
            boolean changedpagenames = false;
            for(textfile.textline line: agefile.getLines())
            {
                String linestr = line.getString();
                String[] varparts = linestr.split("=");
                if(varparts.length>1)
                {
                    if(varparts[0].equals("Page"))
                    {
                        String[] pageparts = varparts[1].split(",");
                        String oldpagename = pageparts[0];
                        String newpagename = (String)info.g.renameinfo.pagenames.get2(file.agename,oldpagename);
                        if(newpagename!=null)
                        {
                            //changed pagename
                            changedpagenames = true;
                            String newlinestr = linestr.replace(oldpagename, newpagename);
                            line.setString(newlinestr);
                        }
                    }
                }
            }
            if(changedpagenames)
            {
                decryptedData = agefile.saveToByteArray();
            }
        }


        /*//modify Minkata's Age file.
        if(file.agename.toLowerCase().equals("minkata"))
        {
            textfile agefile = textfile.createFromBytes(decryptedData);
            agefile.appendLine("Page=minkDusttestDay,11");
            agefile.appendLine("Page=minkDusttestNight,12");
            agefile.appendLine("Page=minkDusttest,10");
            decryptedData = agefile.saveToByteArray();
        }

        //modify Ahnona's Age file.
        if(file.agename.toLowerCase().equals("ahnonay"))
        {
            textfile agefile = textfile.createFromBytes(decryptedData);
            agefile.removeVariables("Page");
            agefile.appendLine("Page=EngineerHut,11");
            agefile.appendLine("Page=Vortex,9");
            agefile.appendLine("Page=YeeshaSketchBahro,16");
            //agefile.appendLine("Page=ahnySphereCtrl,31");
            decryptedData = agefile.saveToByteArray();
        }*/

        if(info.g.agemodifier!=null)
        {
            textfile agefile = textfile.createFromBytes(decryptedData);
            info.g.agemodifier.ModifyAge(info, file, agefile);
            decryptedData = agefile.saveToByteArray();
        }

        byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
        FileUtils.WriteFile(outfile, wdysData,true,true);
    }
    public static void convertSum(Info info, FileInfo file)
    {
        //String agename = common.getAgenameFromFilename(filename);
        //Bytes sum1 = uru.moulprp.sumfile.createSumfile(outfolder+"/dat/", common.replaceAgenameIfApplicable(agename, agenames));
        byte[] sum1 = prpobjects.sumfile.createEmptySumfile().getByteArray();
        String outfile = info.outfolder + "/dat/"+info.g.getNewAgename(file)+".sum";
        //FileUtils.WriteFile(outfolder+"/dat/"+common.replaceAgenameIfApplicable(filename, ri.agenames), sum1);
        FileUtils.WriteFile(outfile, sum1,true,true);
    }
    public static void convertFni(Info info, FileInfo file)
    {
        //Handle .fni files...
        //Vector<String> fnifiles = common.filterFilenamesByExtension(info.allfiles, ".fni");
        //for(String filename: fnifiles)
        //{
            //String agename = common.getAgenameFromFilename(filename);
            String infile = info.infolder + "/dat/" + file.filename;
            //String outfile = info.outfolder + "/dat/" + common.replaceAgenameIfApplicable(file.filename, info.renameinfo.agenames);
            String outfile = info.outfolder + "/dat/" + info.g.getNewAgename(file)+".fni";

            //byte[] encryptedData = FileUtils.ReadFile(infile);
            //byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData);// UruCrypt.DecryptEoa(encryptedData);
            byte[] decryptedData = UruCrypt.DecryptAny(infile,info.g);

            if(info.g.fnimodifier!=null)
            {
                textfile fnifile = textfile.createFromBytes(decryptedData);
                info.g.fnimodifier.ModifyFni(info, file, fnifile);
                decryptedData = fnifile.saveToByteArray();
            }

            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData,true,true);
        //}
    }
    public static void convertBik(Info info, FileInfo file)
    {
        String infile = info.infolder + "/avi/" + file.filename;
        String outfile = info.outfolder + "/avi/" + info.g.renameinfo.simplefilesrename(file.filename);

        FileUtils.CopyFile(infile, outfile, true, true);
    }
    public static void convertCsv(Info info, FileInfo file)
    {
        String infile = info.infolder + "/dat/" + file.filename;
        String outfile = info.outfolder + "/dat/" + info.g.getNewAgename(file)+".csv";

        byte[] decryptedData = UruCrypt.DecryptAny(infile,info.g);

        //if(info.g.csvmodifier!=null)
        //{
        //    textfile fnifile = textfile.createFromBytes(decryptedData);
        //    info.g.fnimodifier.ModifyFni(info, file, fnifile);
        //    decryptedData = fnifile.saveToByteArray();
        //}

        byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
        FileUtils.WriteFile(outfile, wdysData,true,true);
    }
}

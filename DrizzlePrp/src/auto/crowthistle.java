/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.m;
import java.io.File;
import java.util.Vector;
import shared.FileUtils;
import prpobjects.*;
import java.util.HashMap;
import shared.*;
import uru.UruCrypt;
import uru.context;
import auto.conversion.FileInfo;
import auto.conversion.Info;

public class crowthistle
{
    public static AllGames.GameInfo getGameInfo()
    {
        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "Crowthistle";
        r.DetectionFile = "CT.exe";
        r.prpMarkerForAgename = "_";
        r.format = shared.Format.crowthistle;
        r.PythonVersion = 23;
        r.game = Game.crowthistle;
        r.renameinfo.prefices.put("MarshScene", 96);
        r.renameinfo.prefices.put("MountainScene", 95);
        r.agemodifier = new conversion.AgeModifier() {
            public void ModifyAge(Info info, FileInfo file, textfile tf) {
                final String[][] alcugsOptionals = {
                    {"MarshScene","Page=mrshFootRgns,93,1"},
                    {"MountainScene","Page=mntnFootRgns,55,1"},
                };
                for(String[] agepair: alcugsOptionals)
                {
                    if(file.agename.equals(agepair[0]))
                    {
                        tf.appendLine(agepair[1]);
                    }
                }
            }
        };
        r.fnimodifier = new conversion.FniModifier() {
            public void ModifyFni(Info info, FileInfo file, textfile tf) {
                if(file.agename.equals("MarshScene"))
                {
                    for(textfile.textline line: tf.getLines())
                    {
                        String linestr = line.getString();
                        if(linestr.startsWith("Graphics.Renderer.Gamma2")) //otherwise it disables gamma in the engine.
                        {
                            line.setString("#"+linestr);
                        }
                    }
                }
            }
        };
        r.addAgeFiles("MarshScene", new String[]{
            "MarshScene.age",
            "MarshScene.fni",
            "MarshScene.sum",
            "MarshScene_Exterior.prp",
            "MarshScene_Extras.prp",
            "MarshScene_MWInterior.prp",
            "MarshScene_Textures.prp",
            "MarshScene_TourCamera.prp",
            "MarshScene_WaterHorses.prp",
        });
        r.addAgeFiles("MountainScene", new String[]{
            "MountainScene.age",
            "MountainScene.fni",
            "MountainScene.sum",
            "MountainScene_Courtyard.prp",
            "MountainScene_EllenHallInterior.prp",
            "MountainScene_Exterior.prp",
            "MountainScene_Extras.prp",
            "MountainScene_Textures.prp",
            "MountainScene_TourCamera.prp",
            "MountainScene_tw_g1_g2.prp",
            "MountainScene_tw_g1_g3.prp",
            "MountainScene_tw_g1_hm.prp",
            "MountainScene_tw_g2_g1.prp",
            "MountainScene_tw_g2_g3.prp",
            "MountainScene_tw_g2_hm.prp",
            "MountainScene_tw_g3_g1.prp",
            "MountainScene_tw_g3_g2.prp",
            "MountainScene_tw_g3_hm.prp",
            "MountainScene_tw_hm_g1.prp",
            "MountainScene_tw_hm_g2.prp",
            "MountainScene_tw_hm_g3.prp",
            "MountainScene_tw_shape.prp",
            "MountainScene_tw_w1.prp",
            "MountainScene_tw_w2.prp",
            "MountainScene_tw_w3.prp",
            "MountainScene_Vista.prp",
        });
        r.addSoundFiles(new String[]{
            "mntnAir_loop.ogg","mntnAmbientMx.ogg","mntnBird01a.ogg","mntnBird01b.ogg","mntnBird02a.ogg","mntnBird02b.ogg","mntnBird02c.ogg","mntnBird03a.ogg","mntnBird04a.ogg","mntnBird04b.ogg","mntnBird05a.ogg","mntnBird05b.ogg","mntnBird06a.ogg","mntnBird06b.ogg","mntnBird07a.ogg","mntnBird07b.ogg","mntnFountain_loop.ogg","mntnWaterfall_loop.ogg","mntnWind_Loop01.ogg",
            "mrshAmb01.ogg","mrshAmb02.ogg","mrshAmb03.ogg","mrshAmb04.ogg","mrshAmbientMx.ogg","mrshBirdAmb.ogg","mrshRandomCricket01.ogg","mrshRandomCricket02.ogg","mrshRandomCricket03.ogg","mrshRandomCritter01.ogg","mrshRandomCritter02.ogg","mrshRandomCritter03.ogg","mrshRandomCritter04a.ogg","mrshRandomCritter04b.ogg","mrshRandomCritter04c.ogg","mrshRandomCritter04d.ogg","mrshRandomCritter04e.ogg","mrshRandomCritter04f.ogg","mrshRandomCritter04g.ogg","mrshRandomCritter05.ogg","mrshRandomCritter06a.ogg","mrshRandomCritter06b.ogg","mrshRandomCritter06c.ogg","mrshRandomCritter06d.ogg","mrshRandomCritter07a.ogg","mrshRandomCritter07b.ogg","mrshRandomCritter07c.ogg","mrshRandomCritter07d.ogg","mrshRandomCritter08.ogg","mrshRandomCritter09a.ogg","mrshRandomCritter09b.ogg","mrshRandomCritter10.ogg","mrshRandomCritter11a.ogg","mrshRandomCritter11b.ogg","mrshRandomCritter12a.ogg","mrshRandomCritter12b.ogg","mrshRandomCritter13.ogg","mrshRandomCritter14.ogg","mrshRandomCritter15.ogg","mrshRandomCritter16.ogg","mrshRandomCritter17.ogg","mrshRandomCritter18.ogg","mrshRandomFrogs01.ogg","mrshRandomFrogs02.ogg","mrshRandomFrogs03.ogg","mrshRandomFrogs04.ogg","mrshRandomFrogs05.ogg","mrshRandomFrogs06.ogg","mrshRandomFrogs07.ogg","mrshRandomFrogs08.ogg",
        });
        r.MusicFiles = new String[]{
            "mntnAmbientMx.ogg",
            "mrshAmbientMx.ogg",
        };
        r.decider = prpobjects.prputils.Compiler.getDefaultDecider(); // accept every object
        r.prpmodifier = new conversion.PostConversionModifier() {

            public void ModifyPrp(Info info, FileInfo file, prpfile prp) {
                auto.postmod.PostMod_MystV.PostMod_FixDynamicMaps(prp);
                
                // fix for echo effect
                auto.postmod.PostMod_MystV.PostMod_FixEchoEffects(prp);

                //moved to conversion:
                /*String newagename = agenames.get(agename);
                if(newagename!=null)
                {
                    auto.postmod.PostMod_MystV.PostMod_ChangeVerySpecialPython(prp, agename, newagename);
                }*/

                //shouldn't be needed because Crowthistle has no ladders:
                auto.postmod.PostMod_MystV.PostMod_RemoveLadders(prp);
            }
        };
        return r;
    }
    /*public static void CopyMusic(String crowfolder, String potsfolder)
    {
        m.status("Checking the folders you gave...");
        if(!detectinstallation.isFolderCrowthistle(crowfolder)) return;
        if(!detectinstallation.isFolderPots(potsfolder)) return;

        for(String filename: auto.fileLists.crowMusic)
        {
            String infile = crowfolder + "/sfx/" + filename;
            String outfile = potsfolder + "/MyMusic/" + filename;

            FileUtils.CopyFile(infile, outfile, true, true);
        }

        m.status("Done copying Crowthistle music!");
    }*/

    /*public static void convertCrowthistle(String crowfolder, String outfolder)
    {
        m.state.push();
        m.state.curstate.showConsoleMessages = true;
        m.state.curstate.showErrorMessages = true;
        m.state.curstate.showNormalMessages = false;
        m.state.curstate.showWarningMessages = false;
        m.state.curstate.showStatusMessages = true;

        
        
        //shared.State.AllStates.push();
        //shared.State.AllStates.revertToDefaults();
        //shared.State.AllStates.setState("removeDynamicCamMap", true);
        //shared.State.AllStates.setState("makePlLayersWireframe", false);
        //shared.State.AllStates.setState("changeVerySpecialPython", true);
        //shared.State.AllStates.setState("translateSmartseeks", false);
        //shared.State.AllStates.setState("removeLadders", true);
        //shared.State.AllStates.setState("automateMystV", true);
        //shared.State.AllStates.setState("includeAuthoredMaterial", shared.State.AllStates.getStateAsBoolean("includeAuthoredMaterial")); //this line doesn't really do anything, just there to remind you.
        //shared.State.AllStates.setState("includeAuthoredMaterial", false);
        
        //verify folders
        m.status("Checking the folders you gave...");
        if(!auto.AllGames.getCrowthistle().isFolderX(crowfolder))return;
        if(!auto.AllGames.getPots().isFolderX(outfolder)) return;
        
        m.status("Starting conversion...");
        //Vector<String> files = fileLists.crowthistleSimplicityList();
        convertCrowthistleToPots(crowfolder, outfolder);
        
        
        
        //shared.State.AllStates.pop();
        m.state.pop();
        m.status("Dont forget to run SoundDecompress.exe in your Pots folder, in order to get the sounds working!  (You can also click the SoundDecompress button on the form if you prefer.) (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        //m.status("Dont forget to run SoundDecompress.exe; the button is at UAM->SoundDecompress. (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        m.status("Conversion completed!");
        
    }*/

    //public static void convertCrowthistleToPots(String crowthistlefolder, String outfolder)
    //{
        /*class crowDecider implements uru.moulprp.prputils.Compiler.Decider
        {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc)
            {
            }

        }*/

        //HashMap<String, Integer> prefices = new HashMap<String, Integer>();
        //prefices.put("MarshScene", 96);
        //prefices.put("MountainScene", 95);

        //HashMap<String, String> agenames = new HashMap<String, String>();

        /*String[] fnifiles = {
            "MarshScene.fni",
            "MountainScene.fni",
        };*/
        //String[] agefiles = {
        //    "MarshScene.age",
        //    "MountainScene.age",
        //};
        //String[] sumfiles = {
        //    "MarshScene.sum",
        //    "MountainScene.sum",
        //};
        /*String[] prpfiles = {
            "MarshScene_Exterior.prp",
            "MarshScene_Extras.prp",
            "MarshScene_MWInterior.prp",
            "MarshScene_Textures.prp",
            "MarshScene_TourCamera.prp",
            "MarshScene_WaterHorses.prp",
            "MountainScene_Courtyard.prp",
            "MountainScene_EllenHallInterior.prp",
            "MountainScene_Exterior.prp",
            "MountainScene_Extras.prp",
            "MountainScene_Textures.prp",
            "MountainScene_TourCamera.prp",
            "MountainScene_tw_g1_g2.prp",
            "MountainScene_tw_g1_g3.prp",
            "MountainScene_tw_g1_hm.prp",
            "MountainScene_tw_g2_g1.prp",
            "MountainScene_tw_g2_g3.prp",
            "MountainScene_tw_g2_hm.prp",
            "MountainScene_tw_g3_g1.prp",
            "MountainScene_tw_g3_g2.prp",
            "MountainScene_tw_g3_hm.prp",
            "MountainScene_tw_hm_g1.prp",
            "MountainScene_tw_hm_g2.prp",
            "MountainScene_tw_hm_g3.prp",
            "MountainScene_tw_shape.prp",
            "MountainScene_tw_w1.prp",
            "MountainScene_tw_w2.prp",
            "MountainScene_tw_w3.prp",
            "MountainScene_Vista.prp",
        };*/

        //Vector<String> files = new Vector();
        //files.add("MarshScene.(others)");

        //cmap<String,cmap<String,Integer>> authored = new cmap();
        //authored.put("MarshScene","FootRgns",93);

        //create folders...
        //FileUtils.CreateFolder(outfolder+"/dat/");

        //convert .fni files...
        /*for(String filename: fnifiles)
        {
            String infile = crowthistlefolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + filename;

            Bytes encryptedData = FileUtils.ReadFileAsBytes(infile);
            Bytes decryptedData = UruCrypt.DecryptEoa(encryptedData);
            Bytes wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/

        //convert .age files...
        //AllGames.getCrowthistle().ConvertGame(crowthistlefolder, outfolder);
        /*for(String filename: agefiles)
        {
            String infile = crowthistlefolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + filename;
            String agename = common.getAgenameFromFilename(filename);

            Bytes encryptedData = FileUtils.ReadFileAsBytes(infile);
            Bytes decryptedData = UruCrypt.DecryptEoa(encryptedData);

            //modify sequence prefix if Age is in list.
            Integer prefix = prefices.get(agename);
            if(prefix!=null)
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.setVariable("SequencePrefix", Integer.toString(prefix));
                decryptedData = agefile.saveToBytes();
            }*/

            //add any pages that are authored.
            /*if(shared.State.AllStates.getStateAsBoolean("includeAuthoredMaterial") && authored.get(agename) != null)
            {
                for(Pair<String,Integer> curauthprp: authored.get(agename).getAllElements())
                {
                    String pagename = curauthprp.left;
                    int pagenum = curauthprp.right;

                    textfile agefile = textfile.createFromBytes(decryptedData);
                    agefile.appendLine("Page="+pagename+","+Integer.toString(pagenum));
                    decryptedData = agefile.saveToBytes();
                }
            }*/

            /*Bytes wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/

        //Handle .ogg files...
        /*Vector<String> oggfiles = auto.fileLists.crowSfxList();
        for(String filename: oggfiles)
        {
            String infile = crowthistlefolder + "/sfx/" + filename;
            String outfile = outfolder + "/sfx/" + filename;

            FileUtils.CopyFile(infile, outfile, true, false);
        }*/

        //convert .prp files...
        /*for(String filename: prpfiles)
        {
            //Runtime.getRuntime().gc();

            String infile = crowthistlefolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + filename.replaceFirst("_", "_District_");
            String agename = common.getAgenameFromFilename(filename);

            //Bytes prpdata = Bytes.createFromFile(infile);
            //Bytestream bytestream = Bytestream.createFromBytes(prpdata);
            IBytestream bytestream = shared.SerialBytestream.createFromFilename(infile);
            context c = context.createFromBytestream(bytestream);
            c.curFile = filename; //helpful for debugging.

            //modify sequence prefix if Age is in list.
            Integer prefix = AllGames.getCrowthistle().g.renameinfo.prefices.get(agename);
            if(prefix!=null)
            {
                c.sequencePrefix = prefix;
            }

            //c.typesToRead = typesToRead;

            prpfile prp = prpfile.createFromContext(c, auto.mystAutomation.crowReadable);

            //processPrp(prp,agename,agenames,outfolder,crowthistlefolder);
            crowProcessPrp(prp,agename,AllGames.getCrowthistle().g.renameinfo.agenames,outfolder,crowthistlefolder);


            //Bytes prpoutputbytes = prp.saveAsBytes(new crowDecider());
            //prpoutputbytes.saveAsFile(outfile);
            prp.saveAsBytes(new crowDecider()).writeAllBytesToFile(outfile);

            c.close();
        }*/

        //Handle .(others) files...
        //Vector<String> otherfiles = common.filterFilenamesByExtension(files, ".(others)");
        //for(String filename: otherfiles)
        //{
        //    String agename = common.getAgenameFromFilename(filename);

            /*if(shared.State.AllStates.getStateAsBoolean("includeAuthoredMaterial") && authored.get(agename) != null)
            {
                for(Pair<String,Integer> curauthprp: authored.get(agename).getAllElements())
                {
                    String pagename = curauthprp.left;
                    int pagenum = curauthprp.right;

                    String outfilename = common.replaceAgenameIfApplicable(agename, agenames)+"_District_"+pagename+".prp";
                    String outfile = outfolder + "/dat/" + outfilename;

                    Bytes bytes = shared.GetResource.getResourceAsBytes("/files/authored/"+outfilename);
                    bytes.saveAsFile(outfile);
                }
            }*/
        //}

        //create .sum files...
        //Bytes sum1 = uru.moulprp.sumfile.createSumfile(outfolder+"/dat/", "MarshScene");
        //Bytes sum1 = uru.moulprp.sumfile.createEmptySumfile();
        //FileUtils.WriteFile(outfolder+"/dat/MarshScene.sum", sum1);
        //Bytes sum2 = uru.moulprp.sumfile.createSumfile(outfolder+"/dat/", "MountainScene");
        //Bytes sum2 = uru.moulprp.sumfile.createEmptySumfile();
        //FileUtils.WriteFile(outfolder+"/dat/MountainScene.sum", sum2);


        //m.msg("Done Crowthistle work!");
    //}
    /*public static void crowProcessPrp(prpfile prp, String agename, HashMap<String, String> agenames,String outfolder, String infolder)
    {
        auto.postmod.PostMod_MystV.PostMod_RemoveDynamicCampMap(prp);

        String newagename = agenames.get(agename);
        if(newagename!=null)
        {
            auto.postmod.PostMod_MystV.PostMod_ChangeVerySpecialPython(prp, agename, newagename);
        }

        //shouldn't be needed because Crowthistle has no ladders:
        auto.postmod.PostMod_MystV.PostMod_RemoveLadders(prp);
        
    }*/
}

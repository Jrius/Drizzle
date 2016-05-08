/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.io.File;
import shared.*;
import prpobjects.*;

public class pots
{
    public static AllGames.GameInfo getGameInfo()
    {
        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "PathOfTheShell/CompleteChronicles";
        r.DetectionFile = "UruExplorer.exe";
        r.prpMarkerForAgename = "_District_";
        r.format = shared.Format.pots;
        r.PythonVersion = 22;
        r.game = Game.pots;
        //r.readversion = 3;
        r.MusicFiles = new String[]{
            //AtrusIntro.bik has some music.
            "ahnyCathedralMusic.ogg",
            "dsntRestAreaMusic_Loop.ogg",
            "ercaHarvesterMusic.ogg",
            "grsnExteriorMusic_Loop.ogg",
            "grsnRandTCMusic01.ogg",
            "grsnRandTCMusic02.ogg",
            "grsnRandTCMusic03.ogg",
            "grsnTrainingWallMusic01.ogg",
            "islmCavernMusic_Loop.ogg",
            "islmLibraryInteriorMusic.ogg",
            "islmLibraryMusic_Loop.ogg",
            "islmPalaceMusic_Loop.ogg",
            "kdshMoonRoomMusic_Loop.ogg",
            "kdshX2VaultMusic.ogg",
            "KverRandMusic01.ogg","KverRandMusic02.ogg","KverRandMusic03.ogg","KverRandMusic04.ogg","KverRandMusic05.ogg",
            "KverYeeshaMusic.ogg",
            "mystLibraryMusic_loop.ogg",
            "psnlMusicPlayer.ogg",
            "tldnBaronCityMusic_Loop.ogg",
            "tldnUpperShroomMusic_Loop.ogg",
            "tmnaBahro_EndMusic.ogg",
            "tmnaYeesha_FinalMusic01.ogg",
            "tmnaYeesha_FinalMusic02.ogg",
            "clftBookRoomMusic_Loop.ogg",
            "clftOpenMusic.ogg",
            "clftYeesha_IntroMusic.ogg",
            "grsnInteriorMusic_Loop.ogg",
            "grsnTCBridgeMusic.ogg",
            "islmGalleryMusic_Loop.ogg",
            "tldnSlaveOfficeMusic_Loop.ogg",
            //don't have "music" in filename:
            "tldnSporeMe_Loop.ogg",
            "kdshPillarSolution_Loop.ogg",
            "ahnySphere03-Amb01_Loop.ogg",
            "ErcaMusSeg01-0.ogg",
            "ercaMusSeg02-1.ogg",
            "ErcaMusSeg03-2.ogg",
            "nb01PrivateRoom_Loop.ogg",
            "clftZandiRadio_Loop.ogg",
            //"tmnaCreditsMusic.ogg", //In ABM, but expansion packs delete it.  Full Peter Gabriel song.
        };

        //It would not hurt to have these in moul conversion too, except for a slowdown.  Perhaps I should do so?
        r.addInplacemods(
            //Pots:
            "/dat/city_District_palace.prp",
            "/dat/city_District_courtyard.prp",
            "/dat/city_District_canyon.prp",
            "/dat/city_District_cavetjunction.prp",
            "/dat/city_District_ferry.prp",
            "/dat/city_District_greatstair.prp",
            "/dat/city_District_KahloPub.prp",
            "/dat/city_District_library.prp",
            "/dat/city_District_harbor.prp",
            "/dat/AhnySphere01_District_Sphere01.prp",
            "/dat/AhnySphere01_District_MaintRoom01.prp",
            "/dat/AhnySphere01_District_Sphere01OutBuildingInterior.prp",
            "/dat/Personal_District_psnlMYSTII.prp",
            "/dat/Garrison_District_WallRoom.prp",
            //Moul,Pots:
            "/dat/city_District_KadishGallery.prp",
            //Myst5:
            "/dat/Cleft_District_Desert.prp",
            "/dat/Cleft_District_tmnaDesert.prp"
        );
        //r.addInplacemod("city", "/dat/city_District_palace.prp", "RemoveRelevanceRegions", "CityBalconyMarkerFix", "FixKadishDoors");
        //r.addInplacemod("city", "/dat/city_District_courtyard.prp", "RemoveRelevanceRegions", "CityMuseumDoorFix", "FixKadishDoors", "MakeTeledahnIntoKirelBook");
        //r.addInplacemod("city", "/dat/city_District_canyon.prp", "RemoveRelevanceRegions", "FixKadishDoors");
        //r.addInplacemod("city", "/dat/city_District_cavetjunction.prp", "RemoveRelevanceRegions");
        //r.addInplacemod("city", "/dat/city_District_ferry.prp", "RemoveRelevanceRegions"); //ferry has RemoveRelevanceRegions and the soccer ball added
        //r.addInplacemod("city", "/dat/city_District_greatstair.prp", "RemoveRelevanceRegions");
        //r.addInplacemod("city", "/dat/city_District_KadishGallery.prp", "RemoveRelevanceRegions", "FixKadishDoors"); //also add door fix?
        //r.addInplacemod("city", "/dat/city_District_KahloPub.prp", "RemoveRelevanceRegions");
        //r.addInplacemod("city", "/dat/city_District_library.prp", "RemoveRelevanceRegions", "FixKadishDoors");



        return r;
    }

    /*public static void CopyMusic(String potsinfolder, String potsfolder)
    {
        m.status("Checking the folders you gave...");
        if(!detectinstallation.isFolderPots(potsinfolder)) return;
        if(!detectinstallation.isFolderPots(potsfolder)) return;

        for(String filename: auto.fileLists.potsMusic)
        {
            String infile = potsinfolder + "/sfx/" + filename;
            String outfile = potsfolder + "/MyMusic/" + filename;

            FileUtils.CopyFile(infile, outfile, true, true);
        }

        m.status("Done copying Pots music!");
    }*/

    public static void CreateAgeReport(String potsfolder, String agename)
    {
        m.state.push();
        m.state.curstate.showConsoleMessages = true;
        m.state.curstate.showErrorMessages = true;
        m.state.curstate.showNormalMessages = false;
        m.state.curstate.showWarningMessages = false;
        m.state.curstate.showStatusMessages = true;

        m.status("Age: "+agename);

        if(!FileUtils.Exists(potsfolder+"/dat/"+agename+".age"))
        {
            m.status("  Couldn't find "+agename+".age, you should probably have entered the filename of the Age. (e.g. 'EderRiltehInaltahv' for Eder Rilteh Inaltahv.)");
        }

        int totalvertcount = 0;
        for(File f: FileUtils.FindAllFiles(potsfolder+"/dat/", agename+"_District_", ".prp", false,false))
        {
            prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(f, true);
            m.status("  Prp file:",f.getName());
            m.status("    Pageid:",prp.header.pageid.toString2());
            
            for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
            {
                prpobjects.plSoundBuffer sound = obj.castTo();
                m.status("    Ogg file:",sound.oggfile.toString()," flags:",Integer.toString(sound.flags)," chans:",Integer.toString(sound.channels));

                //check if ogg file is present:
                if(!FileUtils.Exists(potsfolder+"/sfx/"+sound.oggfile.toString()))
                {
                    m.status("      Warning: this file is not present in the sfx folder.");
                }
            }

            for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plPythonFileMod))
            {
                prpobjects.plPythonFileMod pfm = obj.castTo();
                m.status("    Python file:",pfm.pyfile.toString());
            }

            int numverts = 0;
            for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plDrawableSpans))
            {
                plDrawableSpans spans = obj.castTo();
                for(plDrawableSpans.PlGBufferGroup group: spans.groups)
                {
                    for(plDrawableSpans.PlGBufferGroup.SubMesh mesh: group.submeshes)
                    {
                        numverts += mesh.getCount();
                    }
                }
            }
            if(numverts!=0) m.status("    Vertex count: "+Integer.toString(numverts));
            totalvertcount += numverts;
            
            m.status("");
        }
        if(totalvertcount!=0) m.status("  Total Vertex count: "+Integer.toString(totalvertcount));
        m.status("");
        
        //for(File f: FileUtils.FindAllFiles(folder, ".pak", false))
        for(File f: FileUtils.FindAllFiles(potsfolder+"/Python/", agename, ".pak", false,false))
        {
            m.status("  Pak file:",f.getName());
            prpobjects.pakfile pak = new prpobjects.pakfile(f,auto.AllGames.getPots().g,false);
            //for(prpobjects.pakfile.IndexEntry ind: pak.indices)
            for(int i=0;i<pak.indices.size();i++)
            {
                prpobjects.pakfile.IndexEntry ind = pak.indices.get(i);
                m.status("    Python file:",ind.objectname.toString());
                //it is just the marshalled object, so no timestamp.
                //prpobjects.pakfile.PythonObject po = pak.objects.get(i);
            }
            m.status("");
        }
        
        //for(File f: FileUtils.FindAllFiles(folder, ".age", false))
        for(File f: FileUtils.FindAllFiles(potsfolder+"/dat/", agename, ".age", false,false))
        {
            m.status("  Age file:",f.getName());
            byte[] data = uru.UruCrypt.DecryptWhatdoyousee(FileUtils.ReadFile(f));
            String data2 = b.BytesToString(data);
            m.status(data2);
            m.status("");
        }
        
        //for(File f: FileUtils.FindAllFiles(folder, ".fni", false))
        for(File f: FileUtils.FindAllFiles(potsfolder+"/dat/", agename, ".fni", false,false))
        {
            m.status("  Fni file:",f.getName());
            byte[] data = uru.UruCrypt.DecryptWhatdoyousee(FileUtils.ReadFile(f));
            String data2 = b.BytesToString(data);
            m.status(data2);
            m.status("");
        }
        
        //for(File f: FileUtils.FindAllFiles(folder, ".sdl", false))
        for(File f: FileUtils.FindAllFiles(potsfolder+"/SDL/", agename, ".sdl", false,false))
        {
            m.status("  Sdl file:",f.getName());
            byte[] data = uru.UruCrypt.DecryptWhatdoyousee(FileUtils.ReadFile(f));
            String data2 = b.BytesToString(data);
            m.status(data2);
            m.status("");
        }
        
        //for(File f: FileUtils.FindAllFiles(folder, ".sum", false))
        for(File f: FileUtils.FindAllFiles(potsfolder+"/dat/", agename, ".sum", false,false))
        {
            m.status("  Sum file:",f.getName());
            //uru.moulprp.sumfile sum = new uru.moulprp.sumfile(FileUtils.ReadFile(f), true, 3);
            prpobjects.sumfile sum = prpobjects.sumfile.readFromFile(f, 3);
            for(prpobjects.sumfile.sumfileFileinfo info: sum.files)
            {
                m.status("    file:",info.filename.toString());
            }
            m.status("");
        }
        
        m.state.pop();
    }
}

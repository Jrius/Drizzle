/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import auto.conversion;
import prpobjects.*;
import shared.m;
import java.util.Vector;
import auto.AllGames;

public class magiquest
{
    //Intended for mqo 1.1399

    //todo
    //avi files
    //new avatar animations?
    //guis
    //python and sdl
    //avatar clothing?

    public static AllGames.GameInfo getGameInfo()
    {
        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "MagiQuestOnline";
        //r.readversion = 8;
        r.format = shared.Format.mqo;
        r.PythonVersion = 23;
        r.game = auto.Game.mqo;
        r.prpMarkerForAgename = "_District_";
        r.DetectionFile = "MQExplorer.exe";
        //r.MusicFiles = new String[]{};
        r.renameinfo.prefices.put("PortalWell", 1048); //was 1
        r.renameinfo.prefices.put("Courtyard", 1049); //was 3
        r.renameinfo.prefices.put("Forest", 1050); //was 5
        r.renameinfo.agenames.put("Forest", "ForestMQ"); //because of a fan age called forest.
        //r.addSoundFiles(magiquestSoundFiles);
        r.addAgeFiles("Courtyard", new String[]{
            "Courtyard.age","Courtyard.fni","Courtyard.sum","Courtyard_District_Courtyard.prp","Courtyard_District_CRTRedoGUI.prp","Courtyard_District_MQQuestInterface.prp","Courtyard_District_QuestItems.prp","Courtyard_District_Textures.prp",
        });
        r.addAgeFiles("Forest", new String[]{
            "Forest.age","Forest.fni","Forest.sum","Forest_District_BattleGUI.prp","Forest_District_Forest.prp","Forest_District_FORRedoGUI.prp","Forest_District_HintGUI.prp","Forest_District_QuestItems.prp","Forest_District_Textures.prp","Forest_District_ViewHintGUI.prp","Forest_District_Windmill.prp",
        });
        r.addAgeFiles("PortalWell", new String[]{
            "PortalWell.age","PortalWell.fni","PortalWell.sum","PortalWell_District_PortalWell.prp","PortalWell_District_Textures.prp",
        });
        r.MusicFiles = new String[]{
            //certain:
            "crtCourtyardMusic_01.ogg","crtCourtyardMusic_02.ogg","crtCourtyardMusic_03.ogg",
            "ForestDark.ogg", "ForestIntro.ogg",
            //maybe:
            "BattleFightTemp.ogg", "BattleWinTemp.ogg", //a little cheesy, and probably not meant for the final game^^
            //no:
            //"prtPortalWellAmb_Loop01.ogg", //not really music
        };
        r.addSoundFiles(new String[]{
            //courtyard:
            "BirdFlaps01.ogg","BirdFlaps02.ogg","BirdRandom01.ogg","BirdRandom02.ogg","BirdRandom03.ogg","BirdRandom04.ogg","BirdRandom05.ogg","BirdRandom06.ogg",
            "Random_Buzz01.ogg","Random_Buzz02.ogg","Random_Buzz03.ogg",
            "CourtyardAmbience02.ogg","crtCourtyardMusic_01.ogg","crtCourtyardMusic_02.ogg","crtCourtyardMusic_03.ogg","crtNightCourtLoop.ogg","crtOuterAmb.ogg",
            //"crtLokariNag.ogg","crtMajesticMorph_Backstory.ogg","crtMajesticMorph_Default1.ogg","crtMajesticMorph_Failure.ogg","crtMajesticMorph_Success.ogg","crtShadowMorph_Backstory.ogg","crtShadowMorph_Default1.ogg","crtShadowMorph_Default2.ogg","crtShadowMorph_Failure.ogg","crtShadowMorph_Success.ogg","crtTricksterMorph_Backstory.ogg","crtTricksterMorph_Default1.ogg","crtTricksterMorph_Failure.ogg","crtTricksterMorph_Success.ogg","crtWarriorMorph_Backstory.ogg","crtWarriorMorph_Default1.ogg","crtWarriorMorph_Default2.ogg","crtWarriorMorph_Failure.ogg","crtWarriorMorph_Success.ogg","crtWillowMorph_Backstory.ogg","crtWillowMorph_Default1.ogg","crtWillowMorph_Default2.ogg",
            //"TutorialCourtyard_nag.ogg",

            //forest:
            //no sounds from Forest Age.
            "BattleFightTemp.ogg","BattleWinTemp.ogg",
            "ForestDark.ogg","ForestIntro.ogg",
            "forAmbDark.ogg","forAmbLight.ogg",
            "forBigGearMove.ogg","forBoulderCast.ogg",
            "forCastleDoorClose.ogg","forCastleDoorOpen.ogg",
            "forGearCollide.ogg","forGearGrind.ogg","forGearPower01.ogg","forGearPower02.ogg","forGearPower03.ogg",
            //"forGolemRandom01.ogg","forGolemRandom02.ogg","forGolemRandom03.ogg","forGolemRandom04.ogg","forGolemRandom05.ogg","forGolemRandom06.ogg","forGolem_Vox_01.ogg","forGolem_Vox_02.ogg","forGolem_Vox_03.ogg","forGolem_Vox_04.ogg","forGolem_Vox_05.ogg",
            //"forHelpGlow.ogg",
            //"forIce-Break.ogg","forIce-Return.ogg",
            "forLanternClick.ogg","forLanternLoop.ogg",
            "forLever.ogg",
            "forLittleGearMove.ogg","forMainGears.ogg",
            //"forPixie-Default.ogg","forPixie-Failure.ogg","forPixie-Success.ogg",
            "forPixieWingSFX.ogg",
            //"forQuestStone-AlreadyComplete","forQuestStone-NeedToFinishQuest.ogg","forQuestStone-NotReady01.ogg","forQuestStone-NotReady02.ogg",
            "forStream.ogg","forStreamLap.ogg","forWindmillHighLoop.ogg","forWindmillHighStart.ogg","forWindmillLowLoop.ogg","forWindmillLowStart.ogg","forWindmillSplineLoop.ogg",

            //portalwell:
            "prtPortalWellAmb_Loop01.ogg",
            //"prtPortal-Fire.ogg","prtPortal-FireLoop.ogg","prtPortal-Open.ogg","prtPortal-Stone.ogg","prtPortal-Symbols.ogg","prtPortal-WordActivated.ogg","prtPortal-WordOpen.ogg",
            //"Tutorial01.ogg","Tutorial01_Nag.ogg","Tutorial02.ogg","Tutorial02_Nag.ogg","Tutorial03.ogg","Tutorial03_Nag.ogg","Tutorial04.ogg","Tutorial04_Nag.ogg","Tutorial05.ogg","Tutorial05_Nag.ogg","Tutorial06.ogg","Tutorial06_Nag.ogg","Tutorial07.ogg","Tutorial07_nag.ogg","Tutorial08.ogg",

            //global
            //"xBattleGUISelect.ogg","xPortalOpenQuick.ogg","xQuestStoneGUISelect.ogg",
            //"xWandable-Cloud.ogg","xWandable-Crystal.ogg","xWandable-FairyRing.ogg","xWandable-FireOrb.ogg","xWandable-FloatStone.ogg","xWandable-Flower.ogg","xWandable-GoldChest-FAIL.ogg","xWandable-GoldChest.ogg","xWandable-Rune.ogg","xWandable-Torch.ogg",

        });
        r.addAviFiles(new String[]{
            //"Warrior over Color Loop wAlpha E52.bik","Trixter over Color Loop wAlpha E52.bik","Woodsy over Color Loop wAlpha E52.bik","Shadow over Color Loop wAlpha E52.bik","Magestic over Color Loop wAlpha E52.bik", //just glowing orbs
            //"QM Dragon Intro 512.bik", //old intro to another quest?
            "LITW Magma Rune Default 512sq.bik","PixieOnline_notglowing.bik","Moriki Default.bik","OnlineOwl_DefaultClip.bik",
        });
        r.decider = new prpobjects.prputils.Compiler.Decider() {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc) {
                Typeid type = desc.objecttype;

                if(type==Typeid.plCloneSpawnModifier) return false; //apparently Pots has a broken implementation of this class.
                
                return true;
            }
        };
        return r;
    }
    //public static void convertMagiquest(String infolder, String outfolder)
    //{
    //    AllGames.GameConversionSub _mqo = new AllGames.GameConversionSub(magiquest.getGameInfo());
    //    _mqo.ConvertGame(infolder, outfolder);
        /*m.state.push();
        m.state.curstate.showConsoleMessages = true;
        m.state.curstate.showErrorMessages = true;
        //m.state.curstate.showNormalMessages = false;
        //m.state.curstate.showWarningMessages = false;
        m.state.curstate.showStatusMessages = true;



        shared.State.AllStates.push();
        //shared.State.AllStates.revertToDefaults();
        shared.State.AllStates.setState("removeDynamicCamMap", true);
        shared.State.AllStates.setState("makePlLayersWireframe", false);
        shared.State.AllStates.setState("changeVerySpecialPython", true);
        shared.State.AllStates.setState("translateSmartseeks", false);
        shared.State.AllStates.setState("removeLadders", true);
        shared.State.AllStates.setState("automateMystV", true);
        //shared.State.AllStates.setState("includeAuthoredMaterial", shared.State.AllStates.getStateAsBoolean("includeAuthoredMaterial")); //this line doesn't really do anything, just there to remind you.
        shared.State.AllStates.setState("includeAuthoredMaterial", false);

        //verify folders
        m.status("Checking the folders you gave...");
        m.status("fix the folder check.");
        //if(!auto.detectinstallation.isFolderX(infolder,"MagiQuest","MQExplorer.exe"))return;
        //if(!auto.detectinstallation.isFolderPots(outfolder)) return;

        m.status("Starting conversion...");
        //Vector<String> files = fileLists.crowthistleSimplicityList();
        convertMagiquestToPots(infolder, outfolder);



        shared.State.AllStates.pop();
        m.state.pop();
        m.status("Dont forget to run SoundDecompress.exe in your Pots folder, in order to get the sounds working!  (You can also click the SoundDecompress button on the form if you prefer.) (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        //m.status("Dont forget to run SoundDecompress.exe; the button is at UAM->SoundDecompress. (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        m.status("Conversion completed!");
        */
    //}

    /*static String[] magiquestSimplicityFiles = new String[]{
        "Courtyard.age","Courtyard.fni","Courtyard.sum","Courtyard_District_Courtyard.prp","Courtyard_District_QuestItems.prp","Courtyard_District_Textures.prp",

        "Forest.age","Forest.fni","Forest.sum","Forest_District_BattleGUI.prp","Forest_District_Forest.prp","Forest_District_QuestItems.prp","Forest_District_Textures.prp","Forest_District_HintGUI.prp","Forest_District_FORRedoGUI.prp",

        "PortalWell.age","PortalWell.fni","PortalWell.sum","PortalWell_District_PortalWell.prp","PortalWell_District_Textures.prp",

    };*/
    /*static String[] magiquestSoundFiles = new String[]{
        //courtyard:
        "BirdFlaps01.ogg","BirdFlaps02.ogg","BirdRandom01.ogg","BirdRandom02.ogg","BirdRandom03.ogg","BirdRandom04.ogg","BirdRandom05.ogg","BirdRandom06.ogg","CourtyardAmbience02.ogg","crtCourtyardMusic_01.ogg","crtCourtyardMusic_02.ogg","crtCourtyardMusic_03.ogg","Random_Buzz01.ogg","Random_Buzz02.ogg","Random_Buzz03.ogg","Stream_Loop.ogg",
        //forest:
        //no sounds from Forest Age.
        //portalwell:
        "prtPortalWellAmb_Loop01.ogg","Tutorial01.ogg","Tutorial01_Nag.ogg","Tutorial02.ogg","Tutorial02_Nag.ogg","Tutorial03.ogg","Tutorial03_Nag.ogg","Tutorial04.ogg","Tutorial04_Nag.ogg","Tutorial05.ogg","Tutorial05_Nag.ogg","Tutorial06.ogg","Tutorial06_Nag.ogg","Tutorial07.ogg","Tutorial08.ogg",
    };*/

    /*public static void convertMagiquestToPots(String infolder, String outfolder)
    {
        Vector<String> files = shared.generic.convertArrayToVector(magiquestSimplicityFiles);
        auto.moul.convertMoulToPots(infolder, outfolder, files, true, magiquest.getMagiQuestRenameInfo());

    }*/

    /*public static conversion.RenameInfo getMagiQuestRenameInfo()
    {
        conversion.RenameInfo r = new conversion.RenameInfo();

        r.prefices.put("PortalWell", 1048); //was 1
        r.prefices.put("Courtyard", 1049); //was 3
        r.prefices.put("Forest", 1050); //was 5

        r.agenames.put("Forest", "ForestMQ"); //because of a fan age called forest.

        return r;
    }*/

}

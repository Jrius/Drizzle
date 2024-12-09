/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.*;
import java.util.HashMap;
import prpobjects.Typeid;
import prpobjects.Uruobjectdesc;
import prpobjects.Pageid;
import prpobjects.prpfile;
import prpobjects.*;
import uru.context;
import auto.conversion.FileInfo;
import auto.conversion.Info;
import auto.conversion.RenameInfo;

public class moul
{
    public static AllGames.GameInfo getGameInfo()
    {

        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "MOUL";
        r.DetectionFile = "TOS.txt";
        r.prpMarkerForAgename = "_District_";
        r.format = shared.Format.moul;
        r.PythonVersion = 23;
        r.game = Game.moul;
        r.renameinfo = getMoulRenameInfo();
        r.renameinfo.simplefiles.put("psnlMusicPlayer.ogg", "psnlMusicPlayerMOUL.ogg");
        r.agemodifier = new conversion.AgeModifier() {
            public void ModifyAge(Info info, FileInfo file, textfile tf) {

                //Alcugs optional pages:
                final String[][] alcugsOptionals = {
                    {"Negilahn","Page=negiWalkable,93,1"},
                    {"Payiferen","Page=payiWalkable,92,1"},
                };
                for(String[] agepair: alcugsOptionals)
                {
                    if(file.agename.equals(agepair[0]))
                    {
                        tf.appendLine(agepair[1]);
                    }
                }

                //modify Minkata's Age file.
                if(file.agename.toLowerCase().equals("minkata"))
                {
                    tf.appendLine("Page=minkDusttestDay,11");
                    tf.appendLine("Page=minkDusttestNight,12");
                    tf.appendLine("Page=minkDusttest,10");
                }

                //modify Ahnona's Age file.
                if(file.agename.toLowerCase().equals("ahnonay"))
                {
                    tf.removeVariables("Page");
                    tf.appendLine("Page=EngineerHut,11");
                    tf.appendLine("Page=Vortex,9");
                    tf.appendLine("Page=YeeshaSketchBahro,16");
                    //tf.appendLine("Page=ahnySphereCtrl,31");
                }

            }
        };

        r.addAgeFiles("city", new String[]{
            "city_District_islmLakeLightMeter.prp",
            "city_District_bahroFlyers_arch.prp","city_District_bahroFlyers_city1.prp","city_District_bahroFlyers_city2.prp","city_District_bahroFlyers_city3.prp","city_District_bahroFlyers_city4.prp","city_District_bahroFlyers_city5.prp","city_District_bahroFlyers_city6.prp",
            "city_District_islmBahroShoutFerry.prp","city_District_islmBahroShoutLibrary.prp","city_District_islmBahroShoutPalace.prp", //shouters
        });
        r.addAgeFiles("Dereno", new String[]{
            "Dereno.age","Dereno.fni","Dereno.sum","Dereno_District_DrnoExterior.prp","Dereno_District_DrnoPod.prp","Dereno_District_Textures.prp","Dereno_District_BuiltIn.prp",
        });
        r.addAgeFiles("EderDelin", new String[]{
            "EderDelin.age","EderDelin.fni","EderDelin.sum","EderDelin_District_garden.prp","EderDelin_District_BuiltIn.prp","EderDelin_District_Textures.prp",
        });
        r.addAgeFiles("EderTsogal", new String[]{
            "EderTsogal.age","EderTsogal.fni","EderTsogal.sum","EderTsogal_District_tsoGarden.prp","EderTsogal_District_Textures.prp","EderTsogal_District_BuiltIn.prp",
        });
        r.addAgeFiles("GuildPub-Cartographers", new String[]{
            "GuildPub-Cartographers.age","GuildPub-Cartographers.fni","GuildPub-Cartographers.sum","GuildPub-Cartographers_District_Pub.prp","GuildPub-Cartographers_District_Textures.prp","GuildPub-Cartographers_District_BuiltIn.prp",
        });
        r.addAgeFiles("GuildPub-Greeters", new String[]{
            "GuildPub-Greeters.age","GuildPub-Greeters.fni","GuildPub-Greeters.sum","GuildPub-Greeters_District_Pub.prp","GuildPub-Greeters_District_Textures.prp","GuildPub-Greeters_District_BuiltIn.prp",
        });
        r.addAgeFiles("GuildPub-Maintainers", new String[]{
            "GuildPub-Maintainers.age","GuildPub-Maintainers.fni","GuildPub-Maintainers.sum","GuildPub-Maintainers_District_Pub.prp","GuildPub-Maintainers_District_Textures.prp","GuildPub-Maintainers_District_BuiltIn.prp",
        });
        r.addAgeFiles("GuildPub-Messengers", new String[]{
            "GuildPub-Messengers.age","GuildPub-Messengers.fni","GuildPub-Messengers.sum","GuildPub-Messengers_District_Pub.prp","GuildPub-Messengers_District_Textures.prp","GuildPub-Messengers_District_BuiltIn.prp",
        });
        r.addAgeFiles("GuildPub-Writers", new String[]{
            "GuildPub-Writers.age","GuildPub-Writers.fni","GuildPub-Writers.sum","GuildPub-Writers_District_Pub.prp","GuildPub-Writers_District_Textures.prp","GuildPub-Writers_District_BuiltIn.prp",
        });
        r.addAgeFiles("Jalak", new String[]{
            "Jalak.age","Jalak.fni","Jalak.sum","Jalak_District_jlakArena.prp","Jalak_District_Textures.prp","Jalak_District_BuiltIn.prp",
        });
        r.addAgeFiles("LiveBahroCaves", new String[]{
            "LiveBahroCaves.age","LiveBahroCaves.fni","LiveBahroCaves.sum","LiveBahroCaves_District_MINKcave.prp","LiveBahroCaves_District_POTScave.prp","LiveBahroCaves_District_PODcave.prp","LiveBahroCaves_District_BlueSpiralCave.prp","LiveBahroCaves_District_TheSpecialPage.prp","LiveBahroCaves_District_Textures.prp","LiveBahroCaves_District_BuiltIn.prp",
        });
        r.addAgeFiles("Minkata", new String[]{
            "Minkata.age","Minkata.csv","Minkata.fni","Minkata.sum","Minkata_District_minkExteriorDay.prp","Minkata_District_minkNightLinkSounds.prp","Minkata_District_minkExteriorNight.prp","Minkata_District_minkExcludeRegions.prp","Minkata_District_minkDistCraterPhysicals.prp","Minkata_District_minkDayLinkSounds.prp","Minkata_District_minkCameras.prp","Minkata_District_Textures.prp","Minkata_District_BuiltIn.prp",
        });
        r.addAgeFiles("Negilahn", new String[]{
            "Negilahn.age","Negilahn.fni","Negilahn.sum","Negilahn_District_Jungle.prp","Negilahn_District_MuseumPod.prp","Negilahn_District_Textures.prp","Negilahn_District_BuiltIn.prp",
        });
        r.addAgeFiles("Neighborhood02", new String[]{
            "Neighborhood02.age", "Neighborhood02.fni", "Neighborhood02.sum", "Neighborhood02_District_BuiltIn.prp", "Neighborhood02_District_GuildInfo-Cartographers.prp", "Neighborhood02_District_GuildInfo-Greeters.prp", "Neighborhood02_District_GuildInfo-Maintainers.prp", "Neighborhood02_District_GuildInfo-Messengers.prp", "Neighborhood02_District_GuildInfo-Writers.prp", "Neighborhood02_District_krelClassRm.prp", "Neighborhood02_District_krelCommonRm.prp", "Neighborhood02_District_krelKirel.prp", "Neighborhood02_District_krelPrivateRm.prp", "Neighborhood02_District_Textures.prp",
        });
        r.addAgeFiles("Payiferen", new String[]{
            "Payiferen.age","Payiferen.fni","Payiferen.sum","Payiferen_District_Pod.prp","Payiferen_District_Textures.prp","Payiferen_District_BuiltIn.prp",
        });
        r.addAgeFiles("Tetsonot", new String[]{
            "Tetsonot.age","Tetsonot.fni","Tetsonot.sum","Tetsonot_District_tetsoPod.prp","Tetsonot_District_Textures.prp","Tetsonot_District_BuiltIn.prp",
        });
        r.addAgeFiles("Kveer", new String[]{
            "Kveer.age","Kveer.fni","Kveer.sum","Kveer_District_BuiltIn.prp","Kveer_District_KveerHalls.prp","Kveer_District_Textures.prp",
        });
        r.addAgeFiles("GreatTreePub", new String[]{
            "GreatTreePub.age","GreatTreePub.fni","GreatTreePub.sum","GreatTreePub_District_BuiltIn.prp","GreatTreePub_District_GreatTree.prp","GreatTreePub_District_Pub.prp","GreatTreePub_District_Textures.prp",
        });
        r.addAgeFiles("Ahnonay", new String[]{
            "Ahnonay.age","Ahnonay.fni","Ahnonay.sum","Ahnonay_District_BuiltIn.prp","Ahnonay_District_EngineerHut.prp","Ahnonay_District_Textures.prp","Ahnonay_District_Vortex.prp","Ahnonay_District_YeeshaSketchBahro.prp",/*"Ahnonay_District_ahnySphereCtrl.prp",*/ //partial Ahnonay Age.
        });
        r.addAgeFiles("GlobalAnimations", new String[]{
            "GlobalAnimations_District_FemaleAmazed.prp","GlobalAnimations_District_FemaleAskQuestion.prp","GlobalAnimations_District_FemaleBeckonBig.prp","GlobalAnimations_District_FemaleBeckonSmall.prp","GlobalAnimations_District_FemaleBow.prp","GlobalAnimations_District_FemaleCallMe.prp","GlobalAnimations_District_FemaleCower.prp","GlobalAnimations_District_FemaleCrazy.prp","GlobalAnimations_District_FemaleCringe.prp","GlobalAnimations_District_FemaleCrossArms.prp","GlobalAnimations_District_FemaleDoh.prp","GlobalAnimations_District_FemaleFlinch.prp","GlobalAnimations_District_FemaleGroan.prp","GlobalAnimations_District_FemaleKneel.prp","GlobalAnimations_District_FemaleLeanLeft.prp","GlobalAnimations_District_FemaleLeanRight.prp","GlobalAnimations_District_FemaleLookAround.prp","GlobalAnimations_District_FemaleOkay.prp", /*eyes go funny*/"GlobalAnimations_District_FemaleOverHere.prp","GlobalAnimations_District_FemalePeer.prp","GlobalAnimations_District_FemaleSalute.prp","GlobalAnimations_District_FemaleScratchHead.prp","GlobalAnimations_District_FemaleShakeFist.prp","GlobalAnimations_District_FemaleShoo.prp","GlobalAnimations_District_FemaleSlouchSad.prp","GlobalAnimations_District_FemaleStop.prp","GlobalAnimations_District_FemaleTalkHand.prp","GlobalAnimations_District_FemaleTapFoot.prp","GlobalAnimations_District_FemaleTaunt.prp","GlobalAnimations_District_FemaleThumbsDown.prp","GlobalAnimations_District_FemaleThumbsDown2.prp","GlobalAnimations_District_FemaleThumbsUp.prp","GlobalAnimations_District_FemaleThumbsUp2.prp","GlobalAnimations_District_FemaleWaveLow.prp","GlobalAnimations_District_FemaleWinded.prp",
            "GlobalAnimations_District_MaleAmazed.prp","GlobalAnimations_District_MaleAskQuestion.prp", /*arm doesn't go up correctly.*/"GlobalAnimations_District_MaleBeckonBig.prp","GlobalAnimations_District_MaleBeckonSmall.prp","GlobalAnimations_District_MaleBow.prp","GlobalAnimations_District_MaleCallMe.prp","GlobalAnimations_District_MaleCower.prp", /*eyes go funny*/"GlobalAnimations_District_MaleCrazy.prp", /*face goes funny*/"GlobalAnimations_District_MaleCringe.prp","GlobalAnimations_District_MaleCrossArms.prp", /*arms disappear*/"GlobalAnimations_District_MaleDoh.prp", /*face goes funny*/"GlobalAnimations_District_MaleFlinch.prp", /*face goes funny*/"GlobalAnimations_District_MaleGroan.prp","GlobalAnimations_District_MaleKneel.prp","GlobalAnimations_District_MaleLeanLeft.prp","GlobalAnimations_District_MaleLeanRight.prp","GlobalAnimations_District_MaleLookAround.prp","GlobalAnimations_District_MaleOkay.prp","GlobalAnimations_District_MaleOverHere.prp","GlobalAnimations_District_MalePeer.prp","GlobalAnimations_District_MaleSalute.prp","GlobalAnimations_District_MaleScratchHead.prp","GlobalAnimations_District_MaleShakeFist.prp","GlobalAnimations_District_MaleShoo.prp","GlobalAnimations_District_MaleSlouchSad.prp","GlobalAnimations_District_MaleStop.prp","GlobalAnimations_District_MaleTalkHand.prp","GlobalAnimations_District_MaleTapFoot.prp","GlobalAnimations_District_MaleTaunt.prp","GlobalAnimations_District_MaleThumbsDown.prp","GlobalAnimations_District_MaleThumbsDown2.prp","GlobalAnimations_District_MaleThumbsUp.prp","GlobalAnimations_District_MaleThumbsUp2.prp","GlobalAnimations_District_MaleWaveLow.prp","GlobalAnimations_District_MaleWinded.prp",
            "GlobalAnimations_District_FemaleDance.prp","GlobalAnimations_District_MaleDance.prp", /*arms disappear*/
            // Jalak column animation (right hand doesn't touch left for both).
            "GlobalAnimations_District_MaleKITap.prp", "GlobalAnimations_District_FemaleKITap.prp",
        });
        r.addAgeFiles("Neighborhood", new String[]{
            //"Neighborhood_District_nb01BahroPedestalShout.prp", //shouters //disabled for Drizzle25
            "Neighborhood.age","Neighborhood.fni","Neighborhood.sum","Neighborhood_District_BuiltIn.prp","Neighborhood_District_Classroom.prp","Neighborhood_District_CommonRoom.prp","Neighborhood_District_nb01.prp","Neighborhood_District_nb01Ayhoheek5Man1Dead.prp","Neighborhood_District_nb01Ayhoheek5Man1State.prp","Neighborhood_District_nb01BahroPedestalShout.prp","Neighborhood_District_nb01ClockInfoSheetDlg.prp","Neighborhood_District_nb01DniPaper.prp","Neighborhood_District_nb01HNYVis.prp","Neighborhood_District_nb01LinkBookDelin.prp","Neighborhood_District_nb01LinkBookGarrisonVis.prp","Neighborhood_District_nb01LinkBookTeledahnVis.prp","Neighborhood_District_nb01LinkBookTsogal.prp","Neighborhood_District_nb01LiveAdditions.prp","Neighborhood_District_PrivateRm.prp","Neighborhood_District_Textures.prp", //enabled for Drizzle25.
        });


        r.addSoundFiles(new String[]{
            "dln_Air_Loop.ogg","dln_GeeseFlyBy.ogg","dln_GeeseRandom.ogg","dln_RandBird-A01.ogg","dln_RandBird-A02.ogg","dln_RandBird-A03.ogg","dln_RandBird-B01.ogg","dln_RandBird-B02.ogg","dln_RandBird-B03.ogg","dln_RandBird-B04.ogg","dln_RandBird-C01.ogg","dln_RandBird-C02.ogg","dln_RandBird-C03.ogg","dln_RandBird01.ogg","dln_RandBird02.ogg","dln_RandBird03.ogg","dln_RandBird04.ogg","dln_RandBird05.ogg","dln_RandBird06.ogg",
            "drnIceCave_Loop.ogg","drnIceHarps_loop.ogg","drnRandomIce01.ogg","drnRandomIce02.ogg","drnRandomIce03.ogg","drnRandomIce04.ogg","drnRandomIce05.ogg","drnRandomIce06.ogg","drnRandomIce07.ogg","drnRandomIce08.ogg","drnUnderwaterCreature01.ogg","drnUnderwaterCreature02.ogg","drnUnderwaterCreature03.ogg","drnUnderwaterCreature04.ogg",
            "Eder_Delin_Amb01_Loop.ogg","Eder_Delin_Amb02_Loop.ogg",
            "gpubAmbience.ogg",
            "jlakForceField_Off.ogg","jlakForceField_On.ogg","jlakGridForce01.ogg","jlakGridForce02.ogg","jlakGridForce03.ogg","jlakGridForce04.ogg","jlakObjectCreation01.ogg","jlakObjectCreation02.ogg","jlakObjectCreation03.ogg","jlakObjectCreation04.ogg","jlakPillarDown01a.ogg","jlakPillarUp01a.ogg","jlakPillar_Loop.ogg","jlakPillar_Start.ogg","jlakPillar_Stop.ogg","jlakWidgetImpact01.ogg","jlakWidgetImpact02.ogg","jlakWidgetImpact03.ogg","jlakWidgetImpact04.ogg","jlakWidgetImpact05.ogg",
            "minkBadlands-CaveMusic.ogg","minkBadlands-Reprise.ogg","minkBadlandsMiddle.ogg","minkCenterCircleMx.ogg","minkConstellations.ogg","minkFlagFlap01_loop.ogg","minkFlagFlap02_loop.ogg","minkFlagFlap03_loop.ogg","minkSymbolBuild01.ogg","minkSymbolBuild02.ogg","minkSymbolBuild03.ogg","minkSymbolBuild04.ogg","minkSymbolBuild05.ogg","mink_Wind_CaveDay_Loop.ogg","mink_Wind_Day_Loop.ogg","mink_Wind_Night_Loop.ogg",
            "nglnAnimal04SpeakerCall.ogg","nglnBaseAmb.ogg","nglnBirdFlyBy_Rand01.ogg","nglnBirdFlyBy_Rand02.ogg","nglnBirdFlyBy_Rand03.ogg","nglnBirdFlyBy_Rand04.ogg","nglnBirdFlyBy_Rand05.ogg","nglnBirdFlyBy_Rand06.ogg","nglnBirdFlyBy_Rand07.ogg","nglnGorillaRand01.ogg","nglnGorillaRand02.ogg","nglnGorillaRand03.ogg","nglnGorillaRand04.ogg","nglnGorillaRand05.ogg","nglnGorillaSpeakercall.ogg","nglnMonkeyRand01.ogg","nglnMonkeyRand02.ogg","nglnMonkeyRand03.ogg","nglnMonkeyRand04.ogg","nglnMonkeyRand05.ogg","nglnMonkeyRand06.ogg","nglnMonkeySpeakercall.ogg","nglnMonkey_Alarmed.ogg","nglnMonkey_Chirp.ogg","nglnUrwin-Enter.ogg","nglnUrwin-WalkAway.ogg","nglnUrwinAnimScreech01.ogg","nglnUrwinRandVx01.ogg","nglnUrwinSpeakerCall.ogg","nglnUrwinWalk_Loop.ogg","nglnUrwin_Idle-to-Walk.ogg","nglnUrwin_Walk-to-Idle.ogg",
            "payiAmbWind01_loop.ogg","payiCreatureButton01.ogg","payiCreatureButton02.ogg","payiCreatureButton03.ogg","payiCreatureButton04.ogg","payiSandscritCreatureVx.ogg","payiSandscritEat_to_WalkSniff.ogg","payiSandscritIdle_to_Walk.ogg","payiSandscritWalkSniff_loop.ogg","payiSandscritWalkSniff_to_Eat.ogg","payiSandscritWalkSniff_to_Idle.ogg","payiSandscritWalk_loop01.ogg","payiSandscritWalk_loop02.ogg","payiSandscritWalk_to_Idle.ogg","payiSandscritWalk_to_WalkSniff.ogg","payiWindowWind_loop01.ogg","payiWindowWind_loop02.ogg",
            "tetsoAmb01_loop.ogg","tetsoAmb02_loop.ogg","tetsoCreatureButton01.ogg","tetsoCreatureButton02.ogg","tetsoCreatureButton03.ogg","tetsoCreatureButton04.ogg","tetsoLightOff.ogg","tetsoLightOn.ogg","tetsoRand01Grp01.ogg","tetsoRand01Grp02.ogg","tetsoRand02Grp01.ogg","tetsoRand02Grp02.ogg","tetsoRand03Grp01.ogg","tetsoRand03Grp02.ogg","tetsoRand04Grp01.ogg","tetsoRand04Grp02.ogg","tetsoRand05Grp01.ogg","tetsoRand05Grp02.ogg","tetsoRand06Grp01.ogg","tetsoRand06Grp02.ogg","tetsoRand07Grp01.ogg","tetsoRand07Grp02.ogg","tetsoRand08Grp01.ogg","tetsoRand09Grp01.ogg","tetsoRand10Grp01.ogg","tetsoRand11Grp01.ogg","tetsoWaterDrip01.ogg","tetsoWaterDrip02.ogg","tetsoWaterDrip03.ogg",
            "tso_AmbBirds_Loop.ogg","tso_AmbCicadas02_Loop.ogg","tso_AmbCicadas_Loop.ogg","tso_AmbGrass_Loop.ogg","tso_AmbWind_Loop.ogg","tso_WaterLap_Loop.ogg",
            "xBahroSymbolGlowLoop.ogg","xBahroSymbolGlowStart.ogg", //delin cloths, e.g.
            "xBS-BahroRing_Dissolve.ogg","xBS-Correct.ogg","xBS-ReverseTimerLoop--TEMP.ogg","xBS-SpiralClothes.ogg","xBS-SpiralDoorClose.ogg","xBS-SpiralDoorOpen.ogg","xBS-SpiralTimerGlow.ogg","xBS-TimerEnd.ogg","xBS-TimerLoop.ogg","xBS-TimerReverse.ogg","xBS-TimerStart.ogg", //delin cloths, e.g.
            "xFountain_Loop.ogg", //fountain in delin, e.g.
            "xJalak_KI_Button02.ogg","xJalak_KI_Button.ogg",
            "xMapShow.ogg", //pod map in city?
            "xPod-GiantSwitch_Down.ogg","xPod-GiantSwitch_Up.ogg","xPod-PushButton.ogg","xPod-PushButtonBroke.ogg","xPod_PowerDown.ogg","xPod_PowerUp.ogg",
            "xSparky_Flare.ogg","xSparky_Loop.ogg",
            "KVeerMusic.ogg","kverAmb_Loop.ogg","kverSeasonFinaleMx.ogg",
            //GreatTreePub doesn't have any that aren't already in Pots.
            "xBahroFlapping01.ogg","xBahroFlapping02.ogg","xBahroFlapping03.ogg","xBahroFlapping04.ogg","xBahroFlapping05.ogg","xBahroFlapping06.ogg","xBahroFlapping07.ogg","xBahroFlapping08.ogg","xBahroGroupAmb_loop.ogg", //city flying bahro
            "NB01BahroShout01--Reverb.ogg", "NB01BahroShout01.ogg", // bahro shouters in the city and the hood
            "ahnyOutsideHutWater_Loop.ogg","ahnyRigRotation.ogg",
            //"xLink-Stereo.ogg", //moul's has different stereo properties.
            "FireworksExplode01.ogg","FireworksExplode02.ogg","FireworksExplode03.ogg","FireworksExplode04.ogg","FireworksExplode05.ogg","FireworksExplode06.ogg","FireworksLaunch01.ogg","FireworksLaunch02.ogg","FireworksLaunch03.ogg","FireworksLaunch04.ogg", //sparkly island.
            //Drizzle22:
            "islmExplosionFx.ogg","islmExposionSource.ogg", //guildhall
            "NB01AhyoheekShutters01.ogg","NB01AhyoheekShutters02.ogg","NB01ButtonClick01.ogg", //hood heek dead
            "NB01AhyoheekWin01.ogg","nb01AhyoheekWin02.ogg","NB01AyoheekCountdown01.ogg", //hood heek state
            //"NB01AhyoheekDrone_Loop.ogg"" //moul's has different stereo properties.
            "psnlGalleryMusic.ogg", //canen in KadishGallery makes this playable in your Relto.
            //Drizzle25(for NeighborhoodMOUL):
            "islmRandomAirNailer01.ogg","islmRandomAirNailer02.ogg",
            "islmRandomDoubleHammers01.ogg","islmRandomDoubleHammers02.ogg","islmRandomDoubleHammers03.ogg","islmRandomDoubleHammers04.ogg","islmRandomDoubleHammers05.ogg","islmRandomDoubleHammers06.ogg",
            "islmRandomRock01.ogg","islmRandomRock02.ogg","islmRandomRock03.ogg","islmRandomRock04.ogg","islmRandomRock05.ogg","islmRandomRock06.ogg","islmRandomRock07.ogg","islmRandomRock08.ogg","islmRandomRock09.ogg","islmRandomRock10.ogg","islmRandomRock11.ogg","islmRandomRock12.ogg",
            "islmRandomSingleHammer01.ogg","islmRandomSingleHammer02.ogg","islmRandomSingleHammer03.ogg","islmRandomSingleHammer04.ogg","islmRandomSingleHammer05.ogg","islmRandomSingleHammer06.ogg",



        });
        r.MusicFiles = new String[]{
            //URULiveIntro.bik has some music.
            "minkBadlands-CaveMusic.ogg",
            "gpubAmbience.ogg",
            //"KVeerMusic.ogg" //same as kveerYeeshaMusic.ogg from Pots.
            "kverSeasonFinaleMx.ogg",
            "minkBadlands-Reprise.ogg",
            "minkBadlandsMiddle.ogg",
            "minkCenterCircleMx.ogg",
            "minkConstellations.ogg",
            "psnlMusicPlayer.ogg", //different than Pots.
            "psnlGalleryMusic.ogg", //like islmGalleryMusic.ogg, but more tinny, like from a device.
            "tmnaCreditsMusic.ogg", //the file in ABM, but missing from Pots.
        };
        /*r.decider = new uru.moulprp.prputils.Compiler.Decider() {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc) {
                Typeid type = desc.objecttype;
                String name = desc.objectname.toString();
                Pageid pageid = desc.pageid;

                //blacklist
                //if(type==type.plSceneNode) return false; //do not allow Scene node in here, it must be treated separately.
                //if(pageid.getRawData()==0x220024 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                //if(pageid.getRawData()==0x2A0025 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).
                //if(pageid.prefix==0x22 && pageid.suffix==0x24 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                if(pageid.prefix==0x2A && pageid.suffix==0x25 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).

                if(pageid.prefix==83 && pageid.suffix==42 && type==type.plHKPhysical && name.equals("ChairFallProxy")) return false; //Ahnonay: this physical crashes it on link in.

                // accept the rest
                return true;
            }
        };*/
        r.decider = prpobjects.prputils.Compiler.getDefaultDecider();
        r.prpmodifier = new conversion.PostConversionModifier() {
            public void ModifyPrp(Info info, FileInfo file, prpfile prp) {
                if(!file.agename.equals("Dereno"))
                {
                    // Dereno's DynamicCamMap doesn't look very good when replaced by an envmap, due to the low-resolution mesh.
                    // So do not process it...

                    //shared.State.AllStates.setState("removeDynamicCamMap", false);
                    auto.postmod.PostMod_MystV.PostMod_FixDynamicMaps(prp);
                }

                String newAgename = info.g.getNewAgename(file);

                //moved to conversion:
                /*String newagename = info.g.renameinfo.agenames.get(file.agename);
                String newAgename = (newagename==null)?agename:newagename;
                if(newagename!=null)
                {
                    auto.postmod.PostMod_MystV.PostMod_ChangeVerySpecialPython(prp, agename, newagename);
                }*/

                //auto.postmod.PostMod_MystV.PostMod_RemoveLadders(prp);

                auto.postmod.PostMod_Moul.PostMod_FixTsogalLanguages(prp);

                auto.postmod.PostMod_Moul.PostMod_FixEnablePhysicalMsg(prp);

                auto.postmod.PostMod_Moul.PostMod_RenameAnimations(prp,newAgename);

                auto.postmod.PostMod_Moul.PostMod_FixMinkata(prp,newAgename,info.outfolder);

                if(file.agename.equals("Neighborhood"))
                {
                    auto.postmod.PostMod_RenamePython.RenamePython(prp,"nb01EmgrPhase0","nb01MOULEmgrPhase0");
                }


                if(file.agename.equals("EderDelin")||file.agename.equals("EderTsogal"))
                {
                    //shared.State.AllStates.setState("translateSmartseeks", true);
                    auto.postmod.PostMod_Moul.PostMod_TranslateSmartseeks(prp);
                }


                if(file.agename.equals("Jalak") && prp.header.pagename.toString().equals("jlakArena"))
                {
                    auto.postmod.PostMod_Moul.PostMod_TranslateJalakWarpPoints(prp);
                }

                auto.postmod.PostMod_FixSwimRegions.FixSwimRegions(prp);

                //all the crazy stuff, should be cleaned up:
                HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap<>();
                moul.proccessPrp(prp, file.agename, info.g.renameinfo.agenames, info.outfolder, info.infolder, refReassigns);

                //fix subworlds:
                auto.postmod.PostMod_Moul.PostMod_FixSubworlds(prp);
            }
        };
        r.addAutomods(moulAutomods);
        r.addInplacemods(
            //"/dat/city_District_palace.prp",
            //"/dat/city_District_courtyard.prp",
            //"/dat/city_District_canyon.prp",
            //"/dat/city_District_cavetjunction.prp",
            //"/dat/city_District_ferry.prp",
            //"/dat/city_District_greatstair.prp",
            //"/dat/city_District_KadishGallery.prp",
            //"/dat/city_District_KahloPub.prp",
            //"/dat/city_District_library.prp",
            //"/dat/city_District_harbor.prp"
        );
        return r;
    }
    public static String[] moulAutomods= new String[]{
        //Drizzle23:
        "city_District_KadishGalleryDustAdditions.prp",
        //Drizzle22:
        "city_District_guildhallDustAdditions.prp",
        //"Neighborhood_District_nb01Ayhoheek5Man1Dead.prp", //removed for Drizzle25
        //"Neighborhood_District_nb01Ayhoheek5Man1State.prp", //removed for Drizzle25
        //Drizzle21:
        "Teledahn_District_tldnDustAdditions.prp",
        "Personal02_District_philDustAdditions.prp",
        "GreatZero_District_grtzDustAdditions.prp",
        "Myst_District_mystDustAdditions.prp",
        "Garrison_District_grsnDustAdditions.prp",
        "Garrison_District_grsnDustAdditions2.prp",
        "Kadish_District_kdshDustAdditions.prp",
        "Gira_District_giraDustAdditions.prp",
        "Descent_District_dsntDustAdditions.prp",
        "Cleft_District_clftDustAdditions2.prp",
        "Cleft_District_clftDustAdditions.prp",
        "AhnySphere02_District_ahny2DustAdditions.prp",
        "Ercana_District_ercaDustAdditions.prp",
        "Personal_District_psnlDustAdditions.prp",
    };
    public static RenameInfo getMoulRenameInfo()
    {
        RenameInfo r = new RenameInfo();
        //HashMap<String, Integer> prefices = new HashMap<String, Integer>();
        r.prefices.put("Payiferen", 99);
        r.prefices.put("Kveer", 98);
        r.prefices.put("EderTsogal", 97);
        r.prefices.put("Neighborhood02",86);
        r.prefices.put("Personal",85);
        r.prefices.put("GreatTreePub",84);
        //r.prefices.put("GlobalAnimations", 789);
        r.prefices.put("Ahnonay",83);
        r.prefices.put("Neighborhood",1168); //Drizzle25

        //HashMap<String, String> agenames = new HashMap<String, String>();
        r.agenames.put("Kveer", "KveerMOUL");
        r.agenames.put("Neighborhood02", "KirelMOUL");
        r.agenames.put("Personal", "PersonalMOUL");
        r.agenames.put("Ahnonay", "AhnonayMOUL");
        r.agenames.put("Neighborhood", "NeighborhoodMOUL"); //Drizzle25

        // Drizzle27
        r.pagenames.put("Neighborhood", "nb01Ayhoheek5Man1Dead", "nb01MOULAyhoheek5Man1Dead");
        r.pagenames.put("Neighborhood", "nb01Ayhoheek5Man1State", "nb01MOULAyhoheek5Man1State");
        r.pagenames.put("Neighborhood", "nb01LinkBookGarrisonVis", "nb01MOULLinkBookGarrisonVis");
        r.pagenames.put("Neighborhood", "nb01LinkBookTeledahnVis", "nb01MOULLinkBookTeledahnVis");
        r.pagenames.put("Neighborhood", "nb01BahroPedestalShout", "nb01MOULBahroPedestalShout");

        //these map from filename to oldpagenum to newpagenum
        //HashMap<String, HashMap<Integer,Integer>> suffices = new HashMap();
        //suffices.put("GUI_District_YeeshaPageGUI.prp", new Pair(50,86));
        //suffices.put("GUI_District_jalakControlPanel.prp", new Pair(68,90));
        //cmap<String,cmap<Integer,Integer>> pagenums = new cmap();
        //suffices.put( "GUI", 50, 86); //YeeshaPageGUI
        //suffices.put( "GUI", 68, 90); //jalakControlPanel
        r.pagenums.put( "GUI", 49, 85); //YeeshaPageGUI
        r.pagenums.put( "GUI", 67, 89); //jalakControlPanel
        //add 1 to all these global ones.
        //There is an extra male animation: MaleTreadWater than has no female counterpart.  Just a mistake?
        r.pagenums.put( "GlobalAnimations", 291, 400); //FemaleAmazed
        r.pagenums.put( "GlobalAnimations", 292, 401); //
        r.pagenums.put( "GlobalAnimations", 326, 402); //FemaleAskQuestion
        r.pagenums.put( "GlobalAnimations", 325, 403); //
        r.pagenums.put( "GlobalAnimations",  77, 404); //FemaleBeckonBig
        r.pagenums.put( "GlobalAnimations", 144, 405); //
        r.pagenums.put( "GlobalAnimations", 145, 406); //FemaleBeckonSmall
        r.pagenums.put( "GlobalAnimations", 146, 407); //
        r.pagenums.put( "GlobalAnimations", 197, 408); //FemaleBookAccept
        r.pagenums.put( "GlobalAnimations", 202, 409); //
        r.pagenums.put( "GlobalAnimations", 198, 410); //FemaleBookAcceptIdle
        r.pagenums.put( "GlobalAnimations", 203, 411); //
        r.pagenums.put( "GlobalAnimations", 194, 412); //FemaleBookOffer
        r.pagenums.put( "GlobalAnimations", 199, 413); //
        r.pagenums.put( "GlobalAnimations", 196, 414); //FemaleBookOfferFinish
        r.pagenums.put( "GlobalAnimations", 201, 415); //
        r.pagenums.put( "GlobalAnimations", 195, 416); //FemaleBookOfferIdle
        r.pagenums.put( "GlobalAnimations", 200, 417); //
        r.pagenums.put( "GlobalAnimations", 293, 418); //FemaleBow
        r.pagenums.put( "GlobalAnimations", 294, 419); //
        r.pagenums.put( "GlobalAnimations", 258, 420); //FemaleCallMe
        r.pagenums.put( "GlobalAnimations", 259, 421); //
        r.pagenums.put( "GlobalAnimations", 260, 422); //FemaleCower
        r.pagenums.put( "GlobalAnimations", 261, 423); //
        r.pagenums.put( "GlobalAnimations", 295, 424); //FemaleCrazy
        r.pagenums.put( "GlobalAnimations", 309, 425); //
        r.pagenums.put( "GlobalAnimations", 280, 426); //FemaleCringe
        r.pagenums.put( "GlobalAnimations", 285, 427); //
        r.pagenums.put( "GlobalAnimations", 296, 428); //FemaleCrossArms
        r.pagenums.put( "GlobalAnimations", 310, 429); //
        r.pagenums.put( "GlobalAnimations", 297, 430); //FemaleDoh
        r.pagenums.put( "GlobalAnimations", 311, 431); //
        r.pagenums.put( "GlobalAnimations", 298, 432); //FemaleFlinch
        r.pagenums.put( "GlobalAnimations", 312, 433); //
        r.pagenums.put( "GlobalAnimations", 141, 434); //FemaleGlobalScopeGrab, like FemaleScopeGrab in pots
        r.pagenums.put( "GlobalAnimations",  70, 435); //
        r.pagenums.put( "GlobalAnimations", 142, 436); //FemaleGlobalScopeHold, like FemaleScopeHold in pots
        r.pagenums.put( "GlobalAnimations",  71, 437); //
        r.pagenums.put( "GlobalAnimations", 143, 438); //FemaleGlobalScopeRelease, like FemaleScopeRelease in pots
        r.pagenums.put( "GlobalAnimations",  72, 439); //
        r.pagenums.put( "GlobalAnimations", 262, 440); //FemaleGroan
        r.pagenums.put( "GlobalAnimations", 263, 441); //
        r.pagenums.put( "GlobalAnimations", 344, 442); //FemaleKITap
        r.pagenums.put( "GlobalAnimations", 343, 443); //
        r.pagenums.put( "GlobalAnimations", 282, 444); //FemaleKneel
        r.pagenums.put( "GlobalAnimations", 313, 445); //
        r.pagenums.put( "GlobalAnimations",  43, 446); //FemaleLeanLeft
        r.pagenums.put( "GlobalAnimations",  16, 447); //
        r.pagenums.put( "GlobalAnimations",  44, 448); //FemaleLeanRight
        r.pagenums.put( "GlobalAnimations",  17, 449); //
        r.pagenums.put( "GlobalAnimations", 299, 450); //FemaleLookAround
        r.pagenums.put( "GlobalAnimations", 314, 451); //
        r.pagenums.put( "GlobalAnimations", 264, 452); //FemaleOkay
        r.pagenums.put( "GlobalAnimations", 265, 453); //
        r.pagenums.put( "GlobalAnimations", 266, 454); //FemaleOverHere
        r.pagenums.put( "GlobalAnimations", 267, 455); //
        r.pagenums.put( "GlobalAnimations", 300, 456); //FemalePeer
        r.pagenums.put( "GlobalAnimations", 315, 457); //
        r.pagenums.put( "GlobalAnimations", 301, 458); //FemaleSalute
        r.pagenums.put( "GlobalAnimations", 316, 459); //
        r.pagenums.put( "GlobalAnimations", 302, 460); //FemaleScratchHead
        r.pagenums.put( "GlobalAnimations", 317, 461); //
        r.pagenums.put( "GlobalAnimations", 303, 462); //FemaleShakeFist
        r.pagenums.put( "GlobalAnimations", 318, 463); //
        r.pagenums.put( "GlobalAnimations", 304, 464); //FemaleShoo
        r.pagenums.put( "GlobalAnimations", 319, 465); //
        r.pagenums.put( "GlobalAnimations", 305, 466); //FemaleSlouchSad
        r.pagenums.put( "GlobalAnimations", 320, 467); //
        r.pagenums.put( "GlobalAnimations", 268, 468); //FemaleStop
        r.pagenums.put( "GlobalAnimations", 269, 469); //
        r.pagenums.put( "GlobalAnimations", 270, 470); //FemaleTalkHand
        r.pagenums.put( "GlobalAnimations", 321, 471); //
        r.pagenums.put( "GlobalAnimations", 272, 472); //FemaleTapFoot
        r.pagenums.put( "GlobalAnimations", 273, 473); //
        r.pagenums.put( "GlobalAnimations", 306, 474); //FemaleTaunt
        r.pagenums.put( "GlobalAnimations", 322, 475); //
        r.pagenums.put( "GlobalAnimations", 275, 476); //FemaleThumbsDown
        r.pagenums.put( "GlobalAnimations", 277, 477); //
        r.pagenums.put( "GlobalAnimations", 283, 478); //FemaleThumbsDown2
        r.pagenums.put( "GlobalAnimations", 286, 479); //
        r.pagenums.put( "GlobalAnimations", 274, 480); //FemaleThumbsUp
        r.pagenums.put( "GlobalAnimations", 276, 481); //
        r.pagenums.put( "GlobalAnimations", 284, 482); //FemaleThumbsUp2
        r.pagenums.put( "GlobalAnimations", 287, 483); //
        r.pagenums.put( "GlobalAnimations", 278, 484); //FemaleWallSlide
        r.pagenums.put( "GlobalAnimations", 279, 485); //
        r.pagenums.put( "GlobalAnimations", 307, 486); //FemaleWaveLow
        r.pagenums.put( "GlobalAnimations", 323, 487); //
        r.pagenums.put( "GlobalAnimations", 308, 488); //FemaleWinded
        r.pagenums.put( "GlobalAnimations", 324, 489); //
        r.pagenums.put( "GlobalAnimations",  32, 490); //FemaleDance
        r.pagenums.put( "GlobalAnimations",   5, 491); //MaleDance
        //include kg* for Bahro
        r.pagenums.put( "GlobalAnimations", 367, 379); //kgFall
        r.pagenums.put( "GlobalAnimations", 364, 380); //kgIdle
        r.pagenums.put( "GlobalAnimations", 350, 381); //kgKiBegin
        r.pagenums.put( "GlobalAnimations", 351, 382); //kgKiEnd
        r.pagenums.put( "GlobalAnimations", 352, 383); //kgKiGlance
        r.pagenums.put( "GlobalAnimations", 353, 384); //kgKiTap
        r.pagenums.put( "GlobalAnimations", 354, 385); //kgKiUse
        r.pagenums.put( "GlobalAnimations", 366, 386); //kgRun
        r.pagenums.put( "GlobalAnimations", 368, 387); //kgRunningJump
        r.pagenums.put( "GlobalAnimations", 377, 388); //kgSitDownGround
        r.pagenums.put( "GlobalAnimations", 378, 389); //kgSitIdleGround
        r.pagenums.put( "GlobalAnimations", 379, 390); //kgSitStandGround
        r.pagenums.put( "GlobalAnimations", 369, 391); //kgStandingJump
        r.pagenums.put( "GlobalAnimations", 371, 392); //kgStepLeft
        r.pagenums.put( "GlobalAnimations", 372, 393); //kgStepRight
        r.pagenums.put( "GlobalAnimations", 373, 394); //kgTurnLeft
        r.pagenums.put( "GlobalAnimations", 374, 395); //kgTurnRight
        r.pagenums.put( "GlobalAnimations", 365, 396); //kgWalk
        r.pagenums.put( "GlobalAnimations", 375, 397); //kgWalkBack
        r.pagenums.put( "GlobalAnimations", 370, 398); //kgWalkingJump
        r.pagenums.put( "GlobalAnimations", 376, 399); //kgWave

        //r.pagenums.put( "Neighborhood", 12, 60); //nb01BahroPedestalShout //removed for Drizzle25.

        //cmap<String,cmap<String,String>> pagenames = new cmap();
        r.pagenames.put("GlobalAnimations", "FemaleDance", "FemaleDanceMOUL");
        r.pagenames.put("GlobalAnimations", "MaleDance", "MaleDanceMOUL");
        //r.pagenames.put("CustomAvatars", "Bahro1", "kg");
        /*r.pagenames.put("GlobalAnimations", "kgFall", "BahroFall");
        r.pagenames.put("GlobalAnimations", "kgIdle", "BahroIdle");
        r.pagenames.put("GlobalAnimations", "kgKiBegin", "BahroKiBegin");
        r.pagenames.put("GlobalAnimations", "kgKiEnd", "BahroKiEnd");
        r.pagenames.put("GlobalAnimations", "kgKiGlance", "BahroKiGlance");
        r.pagenames.put("GlobalAnimations", "kgKiTap", "BahroKiTap");
        r.pagenames.put("GlobalAnimations", "kgKiUse", "BahroKiUse");
        r.pagenames.put("GlobalAnimations", "kgRun", "BahroRun");
        r.pagenames.put("GlobalAnimations", "kgRunningJump", "BahroRunningJump");
        r.pagenames.put("GlobalAnimations", "kgSitDownGround", "BahroSitDownGround");
        r.pagenames.put("GlobalAnimations", "kgSitIdleGround", "BahroSitIdleGround");
        r.pagenames.put("GlobalAnimations", "kgSitStandGround", "BahroSitStandGround");
        r.pagenames.put("GlobalAnimations", "kgStandingJump", "BahroStandingJump");
        r.pagenames.put("GlobalAnimations", "kgStepLeft", "BahroStepLeft");
        r.pagenames.put("GlobalAnimations", "kgStepRight", "BahroStepRight");
        r.pagenames.put("GlobalAnimations", "kgTurnLeft", "BahroTurnLeft");
        r.pagenames.put("GlobalAnimations", "kgTurnRight", "BahroTurnRight");
        r.pagenames.put("GlobalAnimations", "kgWalk", "BahroWalk");
        r.pagenames.put("GlobalAnimations", "kgWalkBack", "BahroWalkBack");
        r.pagenames.put("GlobalAnimations", "kgWalkingJump", "BahroWalkingJump");
        r.pagenames.put("GlobalAnimations", "kgWave", "BahroWave");*/

        return r;
    }


    /*public static void CopyMusic(String moulfolder, String potsfolder)
    {
        m.status("Checking the folders you gave...");
        if(!detectinstallation.isFolderMoul(moulfolder)) return;
        if(!detectinstallation.isFolderPots(potsfolder)) return;

        for(String filename: auto.fileLists.moulMusicNotInPotsOrDifferent)
        {
            String infile = moulfolder + "/sfx/" + filename;
            String outfile = potsfolder + "/MyMusic/" + (filename.equals("psnlMusicPlayer.ogg")?"psnlMusicPlayerMOUL.ogg":filename);

            FileUtils.CopyFile(infile, outfile, true, true);
        }

        m.status("Done copying Moul music!");

    }*/

    /*public static void convertMoul(String myst5folder, String potsfolder)
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
        if(!auto.AllGames.getMoul().isFolderX(myst5folder)) return;
        if(!auto.AllGames.getPots().isFolderX(potsfolder)) return;

        m.status("Starting conversion...");
        Vector<String> files = fileLists.moulSimplicityList();
        boolean skipmainfiles = false;
        if(skipmainfiles)
        {
            m.err("Turn this (skipmainfiles) back on!!!!!!");
        }
        else
        {
            convertMoulToPots(myst5folder, potsfolder, files, true, AllGames.getMoul().g.renameinfo);
        }

        //run distillations, etc.
        for(String file: auto.mod.AutoMod.SimplicityAutoModMoulFiles)
        {
            auto.mod.AutoMod.SimplicityAutoMod(myst5folder, potsfolder, file);
        }

        //shared.State.AllStates.pop();
        m.state.pop();
        m.status("Dont forget to run SoundDecompress.exe in your Pots folder, in order to get the sounds working!  (You can also click the SoundDecompress button on the form if you prefer.) (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        //m.status("Dont forget to run SoundDecompress.exe; the button is at UAM->SoundDecompress. (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        m.status("Conversion completed!");
    }*/

    //static HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns;
    public static void proccessPrp(prpfile prp, String agename, HashMap<String, String> agenames, String outfolder, String infolder, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        String pagename = prp.header.pagename.toString();
        if(agename.equals("city") && (pagename.equals("bahroFlyers_arch") || pagename.equals("bahroFlyers_city1")))
        {
            prpfile textprp = prpfile.createFromFile(infolder + "/dat/city_District_Textures.prp", true);
            prpdistiller.distiller.distillTextures(prp, textprp, new String[]{}, refReassigns);
        }
        if(agename.equals("city") && (pagename.equals("islmBahroShoutFerry") || pagename.equals("islmBahroShoutLibrary") || pagename.equals("islmBahroShoutPalace")))
        {
            prpfile textprp = prpfile.createFromFile(infolder + "/dat/city_District_Textures.prp", true);
            prpdistiller.distiller.distillTextures(prp, textprp, new String[]{}, refReassigns);

            //change link sound
            prpobjects.plSoundBuffer sb = prp.findObject("xLink-Stereo.ogg:L", Typeid.plSoundBuffer).castTo();
            sb.flags = 3;
        }
        //removed for Drizzle25:
        /*if(agename.equals("Neighborhood") && pagename.equals("nb01BahroPedestalShout"))
        {
            prpfile textprp = prpfile.createFromFile(infolder + "/dat/Neighborhood_District_Textures.prp", true);
            //prpdistiller.distiller.distillTextures(prp, textprp, new String[]{}, refReassigns);

            prpfile prp1 = prpfile.createFromFile(infolder + "/dat/Neighborhood_District_nb01.prp", false);
            Vector<prpfile> prpfiles = new Vector();
            prpfiles.add(prp1);
            prpfiles.add(textprp);
            //HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
            //prpdistiller.distiller.distillEverythingOneLayerDeep(prp, prpfiles, refReassigns);
            //prpdistiller.distiller.distillEverythingOneLayerDeep(prp, prpfiles, refReassigns);
            //prpdistiller.distiller.distillEverythingOneLayerDeep(prp, prpfiles, refReassigns);
            int levels = prpdistiller.distiller.distillEverything(prp, prpfiles, refReassigns);
            m.msg("Levels deep: ",Integer.toString(levels));

            //change link sound
            prpobjects.x0029SoundBuffer sb = prp.findObject("xLink-Stereo.ogg:L", Typeid.plSoundBuffer).castTo();
            sb.flags = 3;
        }*/
        if(agename.equals("CustomAvatars") && pagename.equals("Blake"))//"Bahro1"))
        {
            //String newname = "Bahro1";
            //String newname = "kg";
            String newname = "Blake";
            plAliasModifier alias = plAliasModifier.createFromName(newname);
            Uruobjectref aliasref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plAliasModifier,"LODAvatar01",prp);
            PrpRootObject aliasroot = PrpRootObject.createFromDescAndObject(aliasref.xdesc, alias);
            //prp.extraobjects.add(aliasroot);
            //prp.mergeExtras();
            prp.addObject(aliasroot);
        }
        if(agename.equals("Kveer") && pagename.equals("KveerHalls"))
        {
            //change pythonfilemod from Myst to MystMystV.
            PrpRootObject ro = prp.findObject("cPythLinkBookMyst", Typeid.plPythonFileMod);
            if (ro != null)
            {
                // old Cyan version
                
                plPythonFileMod pfm = ro.castTo();
                pfm.getListingByIndex(4).xString = Bstr.createFromString("MystMystV");

                //change respondermodifier from Myst to MystMystV.
                plResponderModifier rm = prp.findObject("cRespLinkOutMyst", Typeid.plResponderModifier).castTo();
                PrpMessage.PlLinkToAgeMsg ltam = rm.messages.get(0).commands.get(1).message.castTo();
                ltam.ageLinkStruct.ageinfo.ageFilename = Wpstr.create("MystMystV");

                //GUI_District_BkBookImages:  Pots -2:55(type4) Moul -2:57(type4) Shard -2:55(type4
                x0006Layer layer = prp.findObject("Map #6995", Typeid.plLayer).castTo();
                Uruobjectref mmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelmystisland*1#0.hsm", Pageid.createFromPrefixPagenum(-2, 54), Pagetype.createWithType(4));
                layer.texture = mmref;

                // myst book: remove yeesha share texture
                layer = prp.findObject("Map #2003", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page*0#3.hsm", Pageid.createFromPrefixPagenum(98, -1), Pagetype.createWithType(8));
                layer = prp.findObject("Map #20040", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page1*0#3.hsm", Pageid.createFromPrefixPagenum(98, -1), Pagetype.createWithType(8));
            }
            else
            {
                // Doobes' 2k21 xmas version
                
                plPythonFileMod pfm = prp.findObject("MystLinkingBook_COLLISION_Python_File", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(4).xString = Bstr.createFromString("MystMystV");

                //change respondermodifier from Myst to MystMystV.
                plResponderModifier rm = prp.findObject("MystLinkingBook_COLLISION_MystLinkingBook_COLLISION_Responder", Typeid.plResponderModifier).castTo();
                PrpMessage.PlLinkToAgeMsg ltam = rm.messages.get(0).commands.get(1).message.castTo();
                ltam.ageLinkStruct.ageinfo.ageFilename = Wpstr.create("MystMystV");

                //Myst linking book texture.
                x0006Layer layer = prp.findObject("Minkata_Map__6995", Typeid.plLayer).castTo(); // Minkata ? Go figure.
                Uruobjectref mmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelmystisland*1#0.hsm", Pageid.createFromPrefixPagenum(-2, 54), Pagetype.createWithType(4));
                layer.texture = mmref;

                // myst book: remove yeesha share texture
                layer = prp.findObject("m_465_Map__2003", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page_0_3.dds", Pageid.createFromPrefixPagenum(98, -1), Pagetype.createWithType(8));
                layer = prp.findObject("m_466_Map__20040", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page1_0_3.dds", Pageid.createFromPrefixPagenum(98, -1), Pagetype.createWithType(8));
            }
        }
        if(agename.equals("EderDelin") && pagename.equals("garden"))
        {
            plPythonFileMod pfm = prp.findObject("cPythYeeshaPage15_0", Typeid.plPythonFileMod).castTo();
            pfm.getListingByIndex(2).xInteger = 18;
            pfm = prp.findObject("cPythYeeshaPage15_1", Typeid.plPythonFileMod).castTo();
            pfm.getListingByIndex(2).xInteger = 18;
            pfm = prp.findObject("cPythYeeshaPage15_2", Typeid.plPythonFileMod).castTo();
            pfm.getListingByIndex(2).xInteger = 18;
        }
        if(agename.equals("Ahnonay") && pagename.equals("EngineerHut"))
        {
            //just disable these objects, so user's can't click them.
            prp.markObjectDeleted(Typeid.plHKPhysical, "SaveClothClkRegion");
            prp.markObjectDeleted(Typeid.plHKPhysical, "BahroRockBook");

            //set new python files:
            prpobjects.plPythonFileMod pfm1 = prp.findObject("cPythDoorConsole", Typeid.plPythonFileMod).castTo();
            pfm1.pyfile = Urustring.createFromString("ahnyKadishDoorMOUL");
            prpobjects.plPythonFileMod pfm2 = prp.findObject("cPythHutInside", Typeid.plPythonFileMod).castTo();
            pfm2.pyfile = Urustring.createFromString("ahnyKadishHutMOUL");
            prpobjects.plPythonFileMod pfm3 = prp.findObject("cSaveCloth7", Typeid.plPythonFileMod).castTo();
            pfm3.pyfile = Urustring.createFromString("ahnySaveClothMOUL");

            //set initialiseOnFirstUpdate to false, so that it does ServerInitComplete.
            prpobjects.plPythonFileMod pfm4 = prp.findObject("cPythPOTSsymbol", Typeid.plPythonFileMod).castTo();
            pfm4.getListingByIndex(10).xBoolean = 0;
        }
        if(agename.equals("Ahnonay") && pagename.equals("Vortex"))
        {
            prpobjects.plPythonFileMod pfm = prp.findObject("PythVogondolaRide", Typeid.plPythonFileMod).castTo();
            pfm.pyfile = Urustring.createFromString("ahnyVogondolaRideV2MOUL.py");

            //set initialiseOnFirstUpdate to false, so that it does ServerInitComplete.
            prpobjects.plPythonFileMod pfm2 = prp.findObject("cPythRigAnims", Typeid.plPythonFileMod).castTo();
            pfm2.getListingByIndex(7).xBoolean = 0;

            //disable subworld
            auto.postmod.PostMod_Moul.PostMod_DisableSubworlds(prp);
        }
        if(agename.equals("GreatTreePub") && pagename.equals("Pub"))
        {
            //change pythonfilemod from AhnonayCathedral to AhnonayMOUL.
            PrpRootObject pfmObject = prp.findObject("cPythLinkBookAhnonay", Typeid.plPythonFileMod);
            if (pfmObject == null)
            {
                // Doobes' new version - object names changed...
                pfmObject = prp.findObject("AhnonayBook01_Python_File", Typeid.plPythonFileMod);
                plPythonFileMod pfm = pfmObject.castTo();
                pfm.getListingByIndex(4).xString = Bstr.createFromString("AhnonayMOUL");

                //change respondermodifier from AhnonayCathedral to AhnonayMOUL.
                plResponderModifier rm = prp.findObject("AhnonayBook01_AhnonayBook01_Responder", Typeid.plResponderModifier).castTo();
                PrpMessage.PlLinkToAgeMsg ltam = rm.messages.get(0).commands.get(1).message.castTo();
                ltam.ageLinkStruct.ageinfo.ageFilename = Wpstr.create("AhnonayMOUL");

                //change Ahnonay image.
                x0006Layer layer = prp.findObject("m_1545_Map__69950", Typeid.plLayer).castTo();
                Uruobjectref mmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelahnonayvortex*1#0.hsm", Pageid.createFromPrefixPagenum(-2, 54), Pagetype.createWithType(4));
                layer.texture = mmref;

                // remove the GUID from the plLinkToAgeMsg to Chiso. This crashes PotS.
                // (we could completely remove that responder as it's no longer used. I'm a bit wary of null plKeys though.)
                // (this responder keeps changing names across MOULa versions - just find whichever contains "ChisoBook".)
                for (PrpRootObject rmro: prp.findObjectsContaining("ChisoBook", Typeid.plResponderModifier))
                {
                    rm = rmro.castTo();
                    for (plResponderModifier.PlResponderCmd cmd: rm.messages.get(0).commands)
                    {
                        if (cmd.message.type == Typeid.plLinkToAgeMsg)
                        {
                            ltam = cmd.message.castTo();
                            ltam.ageLinkStruct.ageinfo.flags &= ~0x04;
                            ltam.ageLinkStruct.ageinfo.ageInstanceGuid = null;
                        }
                    }
                }

                // make the Chiso book use the xSimpleLinkingBook script - this circumvents any potential instancing issues with the linking responder's LinkToAge msg.
                for (PrpRootObject pfmro: prp.findObjectsContaining("ChisoBook_Python_File", Typeid.plPythonFileMod))
                {
                    pfm = pfmro.castTo();
                    pfm.pyfile = Urustring.createFromString("xSimpleLinkingBook");
                    // PFM parameters:
                    // 1 - clickable activator, no need to change
                    // 2 - behaviour (smartseek), conflicts with destination Age string, must change to id 11 (msbSeekBeforeUI)
                    // 3 - linkout responder, conflicts with spawn point, must remove
                    // 4 - left page texture, but the actual value is some kind of identifier for the Python script in MOUL,
                    //     while it's the name of the texture in PotS. Also, it's ID 6 in PotS.
                    pfm.getListingByIndex(2).index = 11;
                    pfm.removeListingByIndex(3);
                    pfm.getListingByIndex(4).xString = Bstr.createFromString("xlinkpanelchisopreniv*1#0.hsm");
                    // missing:
                    // 2 - target Age name
                    pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("ChisoPreniv")));
                    pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(6, Bstr.createFromString("xFanAgeStamp02*1#0.hsm")));
                }

                // Fix the Watcher's journals. Not sure if we'll leave those visible by default, but at least they're there...
                PrpRootObject pfmro = prp.findObject("Words01_Python_File", Typeid.plPythonFileMod);
                if (pfmro != null)
                {
                    pfm = pfmro.castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words1");
                    pfm = prp.findObject("Words02_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words2");
                    pfm = prp.findObject("Words03_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words3");
                    pfm = prp.findObject("Words04_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words4");
                    pfm = prp.findObject("Words05_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words5");
                    pfm = prp.findObject("DRCNotebook_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("WatcherPubInfo");
                }
                else
                {
                    // newer version, names changed again...
                    pfm = prp.findObject("cPythWatchersJournal01_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words1");
                    pfm = prp.findObject("cPythWatchersJournal02_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words2");
                    pfm = prp.findObject("cPythWatchersJournal03_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words3");
                    pfm = prp.findObject("cPythWatchersJournal04_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words4");
                    pfm = prp.findObject("cPythWatchersJournal05_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("Words5");
                    pfm = prp.findObject("cPythDRCWatchersJournal_Python_File", Typeid.plPythonFileMod).castTo();
                    pfm.getListingByIndex(3).xString = Bstr.createFromString("WatcherPubInfo");
                }
                
                // remove yeesha share texture
                layer = prp.findObject("m_4650_Map__20030", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page_0_0.dds", Pageid.createFromPrefixPagenum(84, -1), Pagetype.createWithType(8));
            }
            else
            {
                // old Cyan version
                plPythonFileMod pfm = pfmObject.castTo();
                pfm.getListingByIndex(4).xString = Bstr.createFromString("AhnonayMOUL");

                //change respondermodifier from AhnonayCathedral to AhnonayMOUL.
                plResponderModifier rm = prp.findObject("cRespLinkOutAhnonay", Typeid.plResponderModifier).castTo();
                PrpMessage.PlLinkToAgeMsg ltam = rm.messages.get(0).commands.get(1).message.castTo();
                ltam.ageLinkStruct.ageinfo.ageFilename = Wpstr.create("AhnonayMOUL");

                //change Ahnonay image.
                x0006Layer layer = prp.findObject("Map #69950", Typeid.plLayer).castTo();
                //Uruobjectref mmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelahnonayvortex*1#0.hsm", Pageid.createFromPrefixSuffix(-2, 55), Pagetype.createWithType(4));
                Uruobjectref mmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelahnonayvortex*1#0.hsm", Pageid.createFromPrefixPagenum(-2, 54), Pagetype.createWithType(4));
                layer.texture = mmref;
                
                // Fix the Watcher's journals.
                pfm = prp.findObject("cPythWatchersJournal01", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("Words1");
                pfm = prp.findObject("cPythWatchersJournal02", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("Words2");
                pfm = prp.findObject("cPythWatchersJournal03", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("Words3");
                pfm = prp.findObject("cPythWatchersJournal04", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("Words4");
                pfm = prp.findObject("cPythWatchersJournal05", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("Words5");
                pfm = prp.findObject("cPythDRCWatchersJournal", Typeid.plPythonFileMod).castTo();
                pfm.getListingByIndex(3).xString = Bstr.createFromString("WatcherPubInfo");
                
                // remove yeesha share texture
                layer = prp.findObject("Map #20030", Typeid.plLayer).castTo();
                layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "islmdnibook_page*0#0.hsm", Pageid.createFromPrefixPagenum(84, -1), Pagetype.createWithType(8));
            }
        }
        if(agename.equals("city") && pagename.equals("islmLakeLightMeter"))
        {
            prpfile textprp = prpfile.createFromFile(infolder + "/dat/city_District_Textures.prp", true);
            prpdistiller.distiller.distillTextures(prp, textprp, new String[]{}, refReassigns);
        }
        if(!agename.equals("Dereno"))
        {
            // Dereno's DynamicCamMap doesn't look very good when replaced by an envmap, due to the low-resolution mesh.
            // So do not process it...
            auto.postmod.PostMod_MystV.PostMod_FixDynamicMaps(prp);
        }
        if (agename.equals("Negilahn") && pagename.equals("Textures"))
        {
            // Change the yeesha page texture to the old PotS one...
            // The texture can be found in K'veer's texture file, but we embed it in Drizzle itself to make things easier.
            try {
                // load our new tex
                Bytes bytes = shared.GetResource.getResourceAsBytes("/files/pots/uof/yeeshapage_lush.uof");
                context c = context.createFromBytestream(ByteArrayBytestream.createFromByteArray(bytes.getByteArray()));
                c.readversion = 3;
                // read the mipmap
                PrpRootObject mipObject = new PrpRootObject(c, false, bytes.length());
                PrpRootObject curMipObject = prp.findObject("xyeeshapagealphasketchlushrelto*0#0.hsm", Typeid.plMipMap);
                curMipObject.prpobject.object = mipObject.prpobject.object;
                curMipObject.markAsChanged();
            } catch (readexception ex) {
                m.throwUncaughtException("YP resource not present ! Ex: " + ex.getMessage());
            }
        }
    }

    /*public static void convertMoulToPots(String infolder, String outfolder, Vector<String> files)
    {
        class compileDecider implements uru.moulprp.prputils.Compiler.Decider
        {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc)
            {
                Typeid type = desc.objecttype;
                int number = desc.objectnumber;
                String name = desc.objectname.toString();
                Pageid pageid = desc.pageid;

                //blacklist
                if(type==type.plSceneNode) return false; //do not allow Scene node in here, it must be treated separately.
                //if(pageid.getRawData()==0x220024 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                //if(pageid.getRawData()==0x2A0025 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).
                if(pageid.prefix==0x22 && pageid.suffix==0x24 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                if(pageid.prefix==0x2A && pageid.suffix==0x25 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).

                return true;
            }
        }

        HashMap<String, Integer> prefices = new HashMap<String, Integer>();
        prefices.put("Payiferen", 99);
        prefices.put("Kveer", 98);
        prefices.put("EderTsogal", 97);
        prefices.put("Neighborhood02",86);
        prefices.put("Personal",85);
        prefices.put("GreatTreePub",84);

        HashMap<String, String> agenames = new HashMap<String, String>();
        agenames.put("Kveer", "KveerMOUL");
        agenames.put("Neighborhood02", "KirelMOUL");
        agenames.put("Personal", "PersonalMOUL");

        //these map from filename to oldpagenum to newpagenum
        //HashMap<String, HashMap<Integer,Integer>> suffices = new HashMap();
        //suffices.put("GUI_District_YeeshaPageGUI.prp", new Pair(50,86));
        //suffices.put("GUI_District_jalakControlPanel.prp", new Pair(68,90));
        cmap<String,cmap<Integer,Integer>> pagenums = new cmap();
        //suffices.put( "GUI", 50, 86); //YeeshaPageGUI
        //suffices.put( "GUI", 68, 90); //jalakControlPanel
        pagenums.put( "GUI", 49, 85); //YeeshaPageGUI
        pagenums.put( "GUI", 67, 89); //jalakControlPanel
        //add 1 to all these global ones.
        //There is an extra male animation: MaleTreadWater than has no female counterpart.  Just a mistake?
        pagenums.put( "GlobalAnimations", 291, 300); //FemaleAmazed
        pagenums.put( "GlobalAnimations", 292, 301); //
        pagenums.put( "GlobalAnimations", 326, 302); //FemaleAskQuestion
        pagenums.put( "GlobalAnimations", 325, 303); //
        pagenums.put( "GlobalAnimations",  77, 304); //FemaleBeckonBig
        pagenums.put( "GlobalAnimations", 144, 305); //
        pagenums.put( "GlobalAnimations", 145, 306); //FemaleBeckonSmall
        pagenums.put( "GlobalAnimations", 146, 307); //
        pagenums.put( "GlobalAnimations", 197, 308); //FemaleBookAccept
        pagenums.put( "GlobalAnimations", 202, 309); //
        pagenums.put( "GlobalAnimations", 198, 310); //FemaleBookAcceptIdle
        pagenums.put( "GlobalAnimations", 203, 311); //
        pagenums.put( "GlobalAnimations", 194, 312); //FemaleBookOffer
        pagenums.put( "GlobalAnimations", 199, 313); //
        pagenums.put( "GlobalAnimations", 196, 314); //FemaleBookOfferFinish
        pagenums.put( "GlobalAnimations", 201, 315); //
        pagenums.put( "GlobalAnimations", 195, 316); //FemaleBookOfferIdle
        pagenums.put( "GlobalAnimations", 200, 317); //
        pagenums.put( "GlobalAnimations", 293, 318); //FemaleBow
        pagenums.put( "GlobalAnimations", 294, 319); //
        pagenums.put( "GlobalAnimations", 258, 320); //FemaleCallMe
        pagenums.put( "GlobalAnimations", 259, 321); //
        pagenums.put( "GlobalAnimations", 260, 322); //FemaleCower
        pagenums.put( "GlobalAnimations", 261, 323); //
        pagenums.put( "GlobalAnimations", 295, 324); //FemaleCrazy
        pagenums.put( "GlobalAnimations", 309, 325); //
        pagenums.put( "GlobalAnimations", 280, 326); //FemaleCringe
        pagenums.put( "GlobalAnimations", 285, 327); //
        pagenums.put( "GlobalAnimations", 296, 328); //FemaleCrossArms
        pagenums.put( "GlobalAnimations", 310, 329); //
        pagenums.put( "GlobalAnimations", 297, 330); //FemaleDoh
        pagenums.put( "GlobalAnimations", 311, 331); //
        pagenums.put( "GlobalAnimations", 298, 332); //FemaleFlinch
        pagenums.put( "GlobalAnimations", 312, 333); //
        pagenums.put( "GlobalAnimations", 141, 334); //FemaleGlobalScopeGrab, like FemaleScopeGrab in pots
        pagenums.put( "GlobalAnimations",  70, 335); //
        pagenums.put( "GlobalAnimations", 142, 336); //FemaleGlobalScopeHold, like FemaleScopeHold in pots
        pagenums.put( "GlobalAnimations",  71, 337); //
        pagenums.put( "GlobalAnimations", 143, 338); //FemaleGlobalScopeRelease, like FemaleScopeRelease in pots
        pagenums.put( "GlobalAnimations",  72, 339); //
        pagenums.put( "GlobalAnimations", 262, 340); //FemaleGroan
        pagenums.put( "GlobalAnimations", 263, 341); //
        pagenums.put( "GlobalAnimations", 344, 342); //FemaleKITap
        pagenums.put( "GlobalAnimations", 343, 343); //
        pagenums.put( "GlobalAnimations", 282, 344); //FemaleKneel
        pagenums.put( "GlobalAnimations", 313, 345); //
        pagenums.put( "GlobalAnimations",  43, 346); //FemaleLeanLeft
        pagenums.put( "GlobalAnimations",  16, 347); //
        pagenums.put( "GlobalAnimations",  44, 348); //FemaleLeanRight
        pagenums.put( "GlobalAnimations",  17, 349); //
        pagenums.put( "GlobalAnimations", 299, 350); //FemaleLookAround
        pagenums.put( "GlobalAnimations", 314, 351); //
        pagenums.put( "GlobalAnimations", 264, 352); //FemaleOkay
        pagenums.put( "GlobalAnimations", 265, 353); //
        pagenums.put( "GlobalAnimations", 266, 354); //FemaleOverHere
        pagenums.put( "GlobalAnimations", 267, 355); //
        pagenums.put( "GlobalAnimations", 300, 356); //FemalePeer
        pagenums.put( "GlobalAnimations", 315, 357); //
        pagenums.put( "GlobalAnimations", 301, 358); //FemaleSalute
        pagenums.put( "GlobalAnimations", 316, 359); //
        pagenums.put( "GlobalAnimations", 302, 360); //FemaleScratchHead
        pagenums.put( "GlobalAnimations", 317, 361); //
        pagenums.put( "GlobalAnimations", 303, 362); //FemaleShakeFist
        pagenums.put( "GlobalAnimations", 318, 363); //
        pagenums.put( "GlobalAnimations", 304, 364); //FemaleShoo
        pagenums.put( "GlobalAnimations", 319, 365); //
        pagenums.put( "GlobalAnimations", 305, 366); //FemaleSlouchSad
        pagenums.put( "GlobalAnimations", 320, 367); //
        pagenums.put( "GlobalAnimations", 268, 368); //FemaleStop
        pagenums.put( "GlobalAnimations", 269, 369); //
        pagenums.put( "GlobalAnimations", 270, 370); //FemaleTalkHand
        pagenums.put( "GlobalAnimations", 321, 371); //
        pagenums.put( "GlobalAnimations", 272, 372); //FemaleTapFoot
        pagenums.put( "GlobalAnimations", 273, 373); //
        pagenums.put( "GlobalAnimations", 306, 374); //FemaleTaunt
        pagenums.put( "GlobalAnimations", 322, 375); //
        pagenums.put( "GlobalAnimations", 275, 376); //FemaleThumbsDown
        pagenums.put( "GlobalAnimations", 277, 377); //
        pagenums.put( "GlobalAnimations", 283, 378); //FemaleThumbsDown2
        pagenums.put( "GlobalAnimations", 286, 379); //
        pagenums.put( "GlobalAnimations", 274, 380); //FemaleThumbsUp
        pagenums.put( "GlobalAnimations", 276, 381); //
        pagenums.put( "GlobalAnimations", 284, 382); //FemaleThumbsUp2
        pagenums.put( "GlobalAnimations", 287, 383); //
        pagenums.put( "GlobalAnimations", 278, 384); //FemaleWallSlide
        pagenums.put( "GlobalAnimations", 279, 385); //
        pagenums.put( "GlobalAnimations", 307, 386); //FemaleWaveLow
        pagenums.put( "GlobalAnimations", 323, 387); //
        pagenums.put( "GlobalAnimations", 308, 388); //FemaleWinded
        pagenums.put( "GlobalAnimations", 324, 389); //
        //include kg*

        Typeid[] readable = mystAutomation.moulReadable;

        //create folders...
        FileUtils.CreateFolder(outfolder+"/dat/");

        //Handle .fni files...
        Vector<String> fnifiles = common.filterFilenamesByExtension(files, ".fni");
        for(String filename: fnifiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + common.replaceAgenameIfApplicable(filename, agenames);

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = uru.UruCrypt.DecryptWhatdoyousee(encryptedData);// UruCrypt.DecryptEoa(encryptedData);
            byte[] wdysData = uru.UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }


        //Handle .age files...
        Vector<String> agefiles = filterFilenamesByExtension(files, ".age");
        for(String filename: agefiles)
        {
            String agename = getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + replaceAgenameIfApplicable(filename, agenames);

            if(agename.toLowerCase().equals("personal")) m.warn("Relto may corrupt your savegame, be sure to back up your /sav folder!");

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData); //UruCrypt.DecryptEoa(encryptedData);

            //modify sequence prefix if Age is in list.
            Integer prefix = prefices.get(agename);
            if(prefix!=null)
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.setVariable("SequencePrefix", Integer.toString(prefix));
                decryptedData = agefile.saveToByteArray();
            }

            //modify Minkata's Age file.
            if(agename.toLowerCase().equals("minkata"))
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.appendLine("Page=minkDusttestDay,11");
                agefile.appendLine("Page=minkDusttestNight,12");
                agefile.appendLine("Page=minkDusttest,10");
                decryptedData = agefile.saveToByteArray();
            }

            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }


        //Handle .prp files...
        Vector<String> prpfiles = filterFilenamesByExtension(files, ".prp");
        for(String filename: prpfiles)
        {
            String agename = getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + replaceAgenameIfApplicable(filename, agenames);//.replace("_", "_District_");

            Bytes prpdata = Bytes.createFromFile(infile);
            Bytestream bytestream = Bytestream.createFromBytes(prpdata);
            context c = context.createFromBytestream(bytestream);
            c.curFile = filename; //helpful for debugging.

            //modify sequence prefix if Age is in list.
            Integer prefix = prefices.get(agename);
            if(prefix!=null)
            {
                c.sequencePrefix = prefix;
            }

            //modify sequence suffix if Age is in list.
            cmap<Integer,Integer> suffix = pagenums.get(agename);
            if(suffix!=null)
            {
                c.pagenumMap = suffix;
            }

            //modify agename if Age is in list.
            String newAgename = agenames.get(agename);
            if(newAgename!=null)
            {
                c.ageName = newAgename;
            }

            prpfile prp = prpfile.createFromContext(c, readable);

            processPrp(prp,agename,agenames,outfolder,infolder);

            Bytes prpoutputbytes = prp.saveAsBytes(new compileDecider());
            prpoutputbytes.saveAsFile(outfile);
        }


        //Handle .sum files...
        Vector<String> sumfiles = filterFilenamesByExtension(files, ".sum");
        for(String filename: sumfiles)
        {
            String agename = getAgenameFromFilename(filename);
            Bytes sum1 = uru.moulprp.sumfile.createSumfile(outfolder+"/dat/", replaceAgenameIfApplicable(agename, agenames));
            FileUtils.WriteFile(outfolder+"/dat/"+replaceAgenameIfApplicable(filename, agenames), sum1);
        }


        //All done!
        m.msg("Done Moul work!");
    }
    */
    //public static void convertMoulToPots(String infolder, String outfolder, Vector<String> files, boolean isSimplicity, conversion.RenameInfo ri)
    //{
        /*class compileDecider implements uru.moulprp.prputils.Compiler.Decider
        {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc)
            {
            }
        }*/

        //HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();


        //Typeid[] readable = mystAutomation.moulReadable;
        //Typeid[] readable = null;

        //create folders...
        //FileUtils.CreateFolder(outfolder+"/dat/");
        //FileUtils.CreateFolder(outfolder+"/SDL/");

        /*//Handle .sdl files...
        Vector<String> sdlfiles = common.filterFilenamesByExtension(files, ".sdl");
        for(String filename: sdlfiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/SDL/" + filename;
            String outfile = outfolder + "/SDL/" + common.replaceAgenameIfApplicable(filename, agenames);

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData);// UruCrypt.DecryptEoa(encryptedData);
            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/

        //Handle .fni files...
        /*Vector<String> fnifiles = common.filterFilenamesByExtension(files, ".fni");
        for(String filename: fnifiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + common.replaceAgenameIfApplicable(filename, ri.agenames);

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData);// UruCrypt.DecryptEoa(encryptedData);
            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/

        //Handle .csv files...
        /*Vector<String> csvfiles = common.filterFilenamesByExtension(files, ".csv");
        for(String filename: csvfiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + common.replaceAgenameIfApplicable(filename, ri.agenames);

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData);// UruCrypt.DecryptEoa(encryptedData);
            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/

        //Handle .ogg files...
        /*Vector<String> oggfiles = common.filterFilenamesByExtension(files, ".ogg");
        for(String filename: oggfiles)
        {
            String infile = infolder + "/sfx/" + filename;
            String outfile = outfolder + "/sfx/" + filename;
            //if(filename.equals("xLink-Stereo.ogg"))
            //{
            //    outfile = outfolder+"/sfx/"+"xLink-StereoMOUL.ogg";
            //}

            FileUtils.CopyFile(infile, outfile, true, false);
        }*/

        //Handle .age files...
        //AllGames.getMoul().ConvertGame(infolder, outfolder);
        /*Vector<String> agefiles = common.filterFilenamesByExtension(files, ".age");
        for(String filename: agefiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfile = outfolder + "/dat/" + common.replaceAgenameIfApplicable(filename, ri.agenames);

            if(agename.toLowerCase().equals("personal")) m.warn("Relto may corrupt your savegame, be sure to back up your /sav folder!");

            byte[] encryptedData = FileUtils.ReadFile(infile);
            byte[] decryptedData = UruCrypt.DecryptWhatdoyousee(encryptedData); //UruCrypt.DecryptEoa(encryptedData);

            //modify sequence prefix if Age is in list.
            Integer prefix = ri.prefices.get(agename);
            if(prefix!=null)
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.setVariable("SequencePrefix", Integer.toString(prefix));
                decryptedData = agefile.saveToByteArray();
            }

            //modify Minkata's Age file.
            if(agename.toLowerCase().equals("minkata"))
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.appendLine("Page=minkDusttestDay,11");
                agefile.appendLine("Page=minkDusttestNight,12");
                agefile.appendLine("Page=minkDusttest,10");
                decryptedData = agefile.saveToByteArray();
            }

            //modify Ahnona's Age file.
            if(agename.toLowerCase().equals("ahnonay"))
            {
                textfile agefile = textfile.createFromBytes(decryptedData);
                agefile.removeVariables("Page");
                agefile.appendLine("Page=EngineerHut,11");
                agefile.appendLine("Page=Vortex,9");
                agefile.appendLine("Page=YeeshaSketchBahro,16");
                //agefile.appendLine("Page=ahnySphereCtrl,31");
                decryptedData = agefile.saveToByteArray();
            }

            byte[] wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
            FileUtils.WriteFile(outfile, wdysData);
        }*/


        //Handle .prp files...
//        Vector<String> prpfiles = common.filterFilenamesByExtension(files, ".prp");
//        for(String filename: prpfiles)
//        {
//            String agename = common.getAgenameFromFilename(filename);
//            String infile = infolder + "/dat/" + filename;
//            String outfile = outfolder + "/dat/" + common.replaceAgenameIfApplicable(filename, ri.agenames);//.replace("_", "_District_");
//
//            //shared.State.AllStates.push();
//            /*if(isSimplicity)
//            {
//                if(agename.equals("Dereno"))
//                {
//                    shared.State.AllStates.setState("removeDynamicCamMap", false);
//                }
//                else if(agename.equals("EderDelin")||agename.equals("EderTsogal"))
//                {
//                    shared.State.AllStates.setState("translateSmartseeks", true);
//                }
//            }*/
//
//            //Bytes prpdata = Bytes.createFromFile(infile);
//            //Bytestream bytestream = Bytestream.createFromBytes(prpdata);
//            IBytestream bytestream = shared.SerialBytestream.createFromFilename(infile);
//            context c = context.createFromBytestream(bytestream);
//            c.curFile = filename; //helpful for debugging.
//
//            //modify sequence prefix if Age is in list.
//            Integer prefix = ri.prefices.get(agename);
//            if(prefix!=null)
//            {
//                c.sequencePrefix = prefix;
//            }
//
//            //modify sequence suffix if Age is in list.
//            cmap<Integer,Integer> suffix = ri.pagenums.get(agename);
//            if(suffix!=null)
//            {
//                c.pagenumMap = suffix;
//            }
//
//            //modify agename if Age is in list.
//            String newAgename = ri.agenames.get(agename);
//            if(newAgename!=null)
//            {
//                c.ageName = newAgename;
//            }
//
//            prpfile prp = prpfile.createFromContext(c, readable);
//
//            moulProcessPrp(prp,agename,ri.agenames,outfolder,infolder);
//            auto.moul.proccessPrp(prp,agename,ri.agenames,outfolder,infolder, refReassigns);
//
//            //Change pagename, if applicable.
//            String oldpagename = prp.header.pagename.toString();
//            String newpagename = (String)ri.pagenames.get2(agename,prp.header.pagename.toString());
//            if(newpagename!=null)
//            {
//                prp.header.pagename = Urustring.createFromString(newpagename);
//                outfile = outfile.replaceFirst("_District_"+oldpagename, "_District_"+newpagename);
//            }
//
//            //Bytes prpoutputbytes = prp.saveAsBytes(new compileDecider());
//            //prpoutputbytes.saveAsFile(outfile);
//            prp.saveAsBytes(new compileDecider()).writeAllBytesToFile(outfile);
//
//            c.close();
//
//            //shared.State.AllStates.pop();
//
//        }


        //Handle .sum files...
        /*Vector<String> sumfiles = common.filterFilenamesByExtension(files, ".sum");
        for(String filename: sumfiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            //Bytes sum1 = uru.moulprp.sumfile.createSumfile(outfolder+"/dat/", common.replaceAgenameIfApplicable(agename, agenames));
            Bytes sum1 = uru.moulprp.sumfile.createEmptySumfile();
            FileUtils.WriteFile(outfolder+"/dat/"+common.replaceAgenameIfApplicable(filename, ri.agenames), sum1);
        }*/


        //All done!
        //m.msg("Done Moul work!");
    //}
    /*public static void moulProcessPrp(prpfile prp, String agename, HashMap<String, String> agenames,String outfolder, String infolder)
    {
        if(!agename.equals("Dereno"))
        {
            //shared.State.AllStates.setState("removeDynamicCamMap", false);
            auto.postmod.PostMod_MystV.PostMod_RemoveDynamicCampMap(prp);
        }

        String newagename = agenames.get(agename);
        String newAgename = (newagename==null)?agename:newagename;
        if(newagename!=null)
        {
            auto.postmod.PostMod_MystV.PostMod_ChangeVerySpecialPython(prp, agename, newagename);
        }

        //auto.postmod.PostMod_MystV.PostMod_RemoveLadders(prp);

        PostMod_RenameAnimations(prp,newAgename);

        PostMod_FixMinkata(prp,newAgename,outfolder);


        if(agename.equals("EderDelin")||agename.equals("EderTsogal"))
        {
            //shared.State.AllStates.setState("translateSmartseeks", true);
            PostMod_TranslateSmartseeks(prp);
        }


    }*/
}

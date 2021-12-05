/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.Flt;
import prpobjects.*;
import shared.m;
import prpobjects.plPythonFileMod.Pythonlisting;
import shared.*;
import auto.conversion.FileInfo;
import auto.conversion.Info;
import auto.postmod.PostMod_MystV_WhiteBox;
import java.io.File;
import prpobjects.textfile;


public class mystv //was myst5Fixes
{
    public static AllGames.GameInfo getGameInfo()
    {
        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "MystV";
        r.DetectionFile = "eoa.exe";
        r.prpMarkerForAgename = "_";
        r.format = shared.Format.crowthistle;
        r.PythonVersion = 23;
        r.game = Game.mystv;
        r.agemodifier = new conversion.AgeModifier() {
            @Override
            public void ModifyAge(Info info, FileInfo file, textfile tf) {
                final String[][] alcugsOptionals = {
                    {"Descent","Page=dsntFootRgns,97,1"},
                    {"Direbo","Page=drboAdditions,98,1"},
                    {"Kveer","Page=kverFootRgns,97,1"},
                    {"Laki","Page=lakiFootRgns,99,1"},
                    {"Myst","Page=mystFootRgns,89,1"},
                    {"Siralehn","Page=srlnFootRgns,96,1"},
                    {"Tahgira","Page=thgrFootRgns,97,1"},
                    {"Todelmer","Page=tdlmFootRgns,92,1"},
                };

                //Remove all the KveerMystV pages except kverReleeshan and dusttest from the .age file.
                boolean makeMinimalReleeshan = true;
                if(makeMinimalReleeshan)
                {
                    if(file.agename.equals("Kveer"))
                    {
                        //textfile agefile = textfile.createFromBytes(decryptedData);
                        for(textfile.textline line: tf.getLines())
                        {
                            String l = line.getString();
                            if(l.startsWith("Page="))
                            {
                                line.setString("#"+l); //comment it out.
                            }
                        }
                        tf.appendLine("Page=kverReleeshan,22"); //remove the ,1 from the end so that it loads.
                        //agefile.appendLine("Page=dusttest,90"); //leave it alone.
                        //decryptedData = agefile.saveToBytes();
                    }
                }
                
                // add the additional line AFTER the Kveer fix so that it is not commented out
                for(String[] agepair: alcugsOptionals)
                {
                    if(file.agename.equals(agepair[0]))
                    {
                        tf.appendLine(agepair[1]);
                    }
                }

                //add dusttest page, dynamically loaded.
                if(file.agename.equals("Descent") || file.agename.equals("Todelmer") || file.agename.equals("Tahgira") || file.agename.equals("Siralehn") || file.agename.equals("Laki") || file.agename.equals("Kveer"))
                {
                    //textfile agefile = textfile.createFromBytes(decryptedData);
                    tf.appendLine("Page=dusttest,90");
                    //decryptedData = agefile.saveToBytes();
                }
                // add SlatePower pages
                if (file.agename.equals("Todelmer"))
                    tf.appendLine("Page=tdlmSlatePower,91");
                if (file.agename.equals("Tahgira"))
                    tf.appendLine("Page=thgrSlatePower,91");
                if (file.agename.equals("Laki"))
                {
                    tf.appendLine("Page=lakiSlatePower,91");
                    
                    // auto-load pirbirdactor
                    for(textfile.textline line: tf.getLines())
                    {
                        String l = line.getString();
                        if(l.startsWith("Page=PirBirdActor"))
                            line.setString("Page=PirBirdActor,17"); // remove ",1" that prevents it from loading
                    }
                }
            }
        };
        r.renameinfo.prefices.put("Descent", 94);
        r.renameinfo.prefices.put("Direbo", 93);
        r.renameinfo.prefices.put("Kveer", 92);
        r.renameinfo.prefices.put("Laki", 91);
        r.renameinfo.prefices.put("Myst", 90);
        r.renameinfo.prefices.put("Siralehn", 89);
        r.renameinfo.prefices.put("Tahgira", 88);
        r.renameinfo.prefices.put("Todelmer", 87);
        r.renameinfo.agenames.put("Descent", "DescentMystV");
        r.renameinfo.agenames.put("Kveer", "KveerMystV");
        r.renameinfo.agenames.put("Myst", "MystMystV");
        
        // slates, avatars and things we might want to have fun with
        r.renameinfo.prefices.put("Global", 82);
        r.renameinfo.agenames.put("Global", "Slates");
        //r.renameinfo.pagenames.put("GUI", "BkBookImages", "BkBookImagesEoA");
        // handles PotS pagenum rule (probably some kind of overflow somewhere)... Below 10 or above 90 = deleted
        //r.renameinfo.pagenums.put("GUI", 16, 34); // bkBookImages (EoA)
        r.renameinfo.pagenums.put("GUI", 9, 34); // xSpecialEffectGlare
        r.renameinfo.pagenums.put("GUI", 7, 22); // srlnBahroSlate
        
        r.addAgeFiles("Descent", new String[]{
            "Descent.age",
            "Descent.fni",
            "Descent.sum",
            "Descent_dsntBahro_Idle02.prp","Descent_dsntBahro_Idle03.prp","Descent_dsntBahro_Idle04.prp","Descent_dsntBahro_Idle05.prp","Descent_dsntBahro_Idle06.prp","Descent_dsntBahro_Idle07.prp","Descent_dsntBahro_Idle08.prp","Descent_dsntBahro_Idle09.prp","Descent_dsntBahro_Shot02.prp","Descent_dsntBahro_Shot03.prp","Descent_dsntBahro_Shot04.prp","Descent_dsntBahro_Shot05.prp","Descent_dsntBahro_Shot06.prp","Descent_dsntBahro_Shot07.prp","Descent_dsntBahro_Shot08.prp","Descent_dsntBahro_Shot09.prp","Descent_dsntBahro_Tunnel01.prp","Descent_dsntBahro_Tunnel01Idle.prp","Descent_dsntBats.prp","Descent_dsntEsherIdleTopOfShaft.prp","Descent_dsntEsher_BottomOfShaft.prp","Descent_dsntEsher_FirstHub.prp","Descent_dsntEsher_Intro.prp","Descent_dsntEsher_TopOfShaft.prp","Descent_dsntGreatShaftBalcony.prp","Descent_dsntGreatShaftLowerRm.prp","Descent_dsntLowerBats.prp","Descent_dsntMapGUI.prp","Descent_dsntPostBats.prp","Descent_dsntPostShaftNodeAndTunnels.prp","Descent_dsntShaftGeneratorRoom.prp","Descent_dsntShaftTunnelSystem.prp","Descent_dsntTianaCave.prp","Descent_dsntTianaCaveNode2.prp","Descent_dsntTianaCaveTunnel1.prp","Descent_dsntTianaCaveTunnel3.prp","Descent_dsntUpperBats.prp","Descent_dsntUpperShaft.prp","Descent_dsntVolcano.prp","Descent_Textures.prp",
            //"Descent_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/DescentMystV_District_dusttest.prp", "/dat/DescentMystV_District_dusttest.prp");
        r.addAgeFiles("Direbo", new String[]{
            "Direbo.age",
            "Direbo.fni",
            "Direbo.sum",
            "Direbo_DragonFly.prp","Direbo_drboEsherIdleDirebo.prp","Direbo_drboEsher_DireboLaki.prp","Direbo_drboEsher_DireboSrln.prp","Direbo_drboEsher_DireboTdlm.prp","Direbo_drboEsher_DireboThgr.prp","Direbo_drboUrwinShape.prp","Direbo_RestAge.prp","Direbo_Textures.prp","Direbo_UrwinIdle.prp","Direbo_UrwinWalk.prp",
        });
        r.addAgeFiles("Kveer", new String[]{
            "Kveer.age",
            "Kveer.fni",
            "Kveer.sum",
            /*"Kveer_bkMystBookLocked.prp","Kveer_GreatRm.prp","Kveer_KveerBats.prp","Kveer_kverAtrus.prp","Kveer_kverAtrus_1.prp","Kveer_kverAtrus_Idle.prp","Kveer_kverBahroWingsGUI.prp","Kveer_kverBahro_1.prp","Kveer_kverBahro_2.prp","Kveer_kverBahro_Ballroom01.prp","Kveer_kverBahro_Ballroom02.prp","Kveer_kverBahro_Ballroom03.prp","Kveer_kverBahro_Exit01.prp","Kveer_kverBahro_Exit02.prp","Kveer_kverBahro_Idle05.prp","Kveer_kverBahro_Idle06.prp","Kveer_kverBahro_Idle07.prp","Kveer_kverBahro_Idle08.prp","Kveer_kverBahro_Idle09.prp","Kveer_kverBahro_Shot03.prp","Kveer_kverBahro_Shot04.prp","Kveer_kverBahro_Shot05.prp","Kveer_kverBahro_Shot06.prp","Kveer_kverBahro_Shot07.prp","Kveer_kverBahro_Shot08.prp","Kveer_kverBahro_Shot09.prp","Kveer_kverConc3Music.prp","Kveer_kverEsher_1.prp",*/"Kveer_kverReleeshan.prp",/*"Kveer_kverYeesha_1.prp","Kveer_kverYeesha_Conc01.prp","Kveer_kverYeesha_Conc02.prp","Kveer_kverYeesha_Conc03.prp","Kveer_kverYeesha_ConcIntro.prp","Kveer_kverYeesha_ConcIntro2.prp","Kveer_kverYeesha_IdleForIntro.prp","Kveer_kverYeesha_Intro.prp","Kveer_Prison.prp",*/"Kveer_Textures.prp",
            //"Kveer_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/KveerMystV_District_dusttest.prp", "/dat/KveerMystV_District_dusttest.prp");
        r.addAgeFiles("Laki", new String[]{
            "Laki.age",
            "Laki.fni",
            "Laki.sum",
            "Laki_Exterior.prp","Laki_LakiArenaVillaInt.prp","Laki_LakiCreatures.prp","Laki_lakiEsher-Arena.prp","Laki_lakiEsher-FighterBeach.prp","Laki_lakiEsher-Keep.prp","Laki_lakiEsher-Villa.prp","Laki_lakiEsherIdleKeep.prp","Laki_lakiEsherIdleVilla.prp","Laki_LakiMaze.prp","Laki_lakiMazeClue.prp","Laki_LakiTrees01.prp","Laki_PirBirdActor.prp","Laki_PirBirdChomp.prp","Laki_PirBirdIdle.prp","Laki_PirBirdSwallow.prp","Laki_PirBirdVocalize.prp","Laki_PirBirdWalk.prp","Laki_Textures.prp",
            //"Laki_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/Laki_District_dusttest.prp", "/dat/Laki_District_dusttest.prp");
        r.addAgeFiles("Myst", new String[]{
            "Myst.age",
            "Myst.fni",
            "Myst.sum",
            "Myst_Island.prp","Myst_mystEsher-Conc01.prp","Myst_mystEsher-Conc02.prp","Myst_Textures.prp",
        });
        r.addAgeFiles("Siralehn", new String[]{
            "Siralehn.age",
            "Siralehn.fni",
            "Siralehn.sum",
            "Siralehn_Birds.prp","Siralehn_Drawing01.prp","Siralehn_Drawing02.prp","Siralehn_Drawing03.prp","Siralehn_Drawing04.prp","Siralehn_Drawing05.prp","Siralehn_Drawing06.prp","Siralehn_Drawing07.prp","Siralehn_Drawing08.prp","Siralehn_Exterior.prp","Siralehn_rock.prp","Siralehn_srlnEsherIdleBeach.prp","Siralehn_srlnEsherIdleLab.prp","Siralehn_srlnEsher_NolobenBeach.prp","Siralehn_srlnEsher_NolobenKeep.prp","Siralehn_srlnEsher_NolobenLab.prp","Siralehn_srlnKeepInter.prp","Siralehn_Textures.prp","Siralehn_tunnels.prp",
            //"Siralehn_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/Siralehn_District_dusttest.prp", "/dat/Siralehn_District_dusttest.prp");
        r.addAgeFiles("Tahgira", new String[]{
            "Tahgira.age",
            "Tahgira.fni",
            "Tahgira.sum",
            "Tahgira_Exterior.prp","Tahgira_IceCave.prp","Tahgira_Textures.prp","Tahgira_thgrEsherIdleIntro.prp","Tahgira_thgrEsherIdleTake.prp","Tahgira_thgrEsher_TahgiraGrave.prp","Tahgira_thgrEsher_TahgiraIntro.prp","Tahgira_thgrEsher_TahgiraTake.prp","Tahgira_thgrEsher_TahgiraThermals.prp","Tahgira_thgrEsher_TahgiraVillage.prp",
            //"Tahgira_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/Tahgira_District_dusttest.prp", "/dat/Tahgira_District_dusttest.prp");
        r.addAgeFiles("Todelmer", new String[]{
            "Todelmer.age",
            "Todelmer.fni",
            "Todelmer.sum",
            "Todelmer_Exterior.prp","Todelmer_InteriorPillar1.prp","Todelmer_InteriorPillar3.prp","Todelmer_MiniScope.prp","Todelmer_Pod.prp","Todelmer_Sky.prp","Todelmer_tdlmEsherIdleP3.prp","Todelmer_tdlmEsherIdleRing.prp","Todelmer_tdlmEsher_TodelmerP1.prp","Todelmer_tdlmEsher_TodelmerP3.prp","Todelmer_tdlmEsher_TodelmerRing.prp","Todelmer_Textures.prp",
            //"Todelmer_dusttest.prp",
        });
        r.addEmbeddedFile("/files/myst5/Todelmer_District_dusttest.prp", "/dat/Todelmer_District_dusttest.prp");
        //"MystMystV_District_Additions.prp","Direbo_District_Additions.prp", //original authored material.
        
        // add SlatePower files
        r.addEmbeddedFile("/files/myst5/Tahgira_District_thgrSlatePower.prp", "/dat/Tahgira_District_thgrSlatePower.prp");
        r.addEmbeddedFile("/files/myst5/Todelmer_District_tdlmSlatePower.prp", "/dat/Todelmer_District_tdlmSlatePower.prp");
        r.addEmbeddedFile("/files/myst5/Laki_District_lakiSlatePower.prp", "/dat/Laki_District_lakiSlatePower.prp");
        
        // add BuiltIn SDL hooks
        r.addEmbeddedFile("/files/myst5/Tahgira_District_BuiltIn.prp", "/dat/Tahgira_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/Todelmer_District_BuiltIn.prp", "/dat/Todelmer_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/Siralehn_District_BuiltIn.prp", "/dat/Siralehn_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/Laki_District_BuiltIn.prp", "/dat/Laki_District_BuiltIn.prp");
        
        r.addEmbeddedFile("/files/myst5/DescentMystV_District_BuiltIn.prp", "/dat/DescentMystV_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/Direbo_District_BuiltIn.prp", "/dat/Direbo_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/KveerMystV_District_BuiltIn.prp", "/dat/KveerMystV_District_BuiltIn.prp");
        r.addEmbeddedFile("/files/myst5/MystMystV_District_BuiltIn.prp", "/dat/MystMystV_District_BuiltIn.prp");
        
        // GUI
        r.addAgeFiles("GUI", new String[] {
            "GUI_xSpecialEffectGlare.prp",
        });
        
        r.addAviFiles(new String[]{
            "direbo.bik","restStop1.bik","restStop2.bik","restStop3.bik","restStop4.bik","direboWithAlpha.bik","mystWithAlpha.bik",
            "dsntRestStop1WithAlpha.bik", "dsntRestStop2WithAlpha.bik", "dsntRestStop3WithAlpha.bik", "dsntRestStop4WithAlpha.bik",
            "dsntYeesha-Imager01_eng.bik", "dsntYeesha-Imager02.bik", "dsntYeesha-Imager03.bik", // for imagers in descent
        });

        r.MusicFiles = new String[]{
            //GameIntro.bik has music.
            "dsntElevatorMusic.ogg",
            "dsntRestAreaMusic_Loop.ogg",
            "KverRandMusic01.ogg", "KverRandMusic02.ogg", "KverRandMusic03.ogg", "kverRandMusic04.ogg", "kverRandMusic05.ogg",
            "lakiArena-RevealMusic.ogg",
            "lakiRandMusic01.ogg", "lakiRandMusic02.ogg", "lakiRandMusic03.ogg", "lakiRandMusic04.ogg", "lakiRandMusic05.ogg", "lakiRandMusic06.ogg", "lakiRandMusic07.ogg", "lakiRandMusic08.ogg", "lakiRandMusic09.ogg",
            "lakiWindmill-PuzzleMusic_Loop.ogg",
            "mystAmbMusic.ogg",
            "srlnMainMusic_Loop.ogg",
            "srlnRandMusic01.ogg", "srlnRandMusic02.ogg", "srlnRandMusic03.ogg", "srlnRandMusic04.ogg", "srlnRandMusic05.ogg", "srlnRandMusic06.ogg", "srlnRandMusic07.ogg", "srlnRandMusic08.ogg", "srlnRandMusic09.ogg", "srlnRandMusic10.ogg", "srlnRandMusic11.ogg", "srlnRandMusic12.ogg", "srlnRandMusic13.ogg", "srlnRandMusic14.ogg", "srlnRandMusic15.ogg",
            "tdlmAmbMusic01_loop.ogg",
            "xBubbleMusic.ogg",
            "dsntEsher-Intro_Mx.ogg",
            "dsntYeesha-Imager01Mx.ogg",
            "dsntYeesha-Imager02Mx.ogg",
            "dsntYeesha-Imager03Mx.ogg",
            "kverConc03MxPart01.ogg",
            "kverConc03MxPart02.ogg",
            "kverYeesha-IntroMx.ogg",
            "kverYeeshaConc02Mx.ogg",
            "lakiEsher-Arena_Mx.ogg",
            "lakiEsher-Keep_Mx.ogg",
            "lakiEsher-Villa_Mx.ogg",
            "mystEsher-Conc01Mx.ogg",
            "mystEsher-Conc02Mx.ogg",
            "tdlmEsher-TodelmerP1_Mx.ogg",
            "thgrIceFildsMx_loop.ogg",
        };
        /*r.decider = new uru.moulprp.prputils.Compiler.Decider() {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc) {
                //return false;
                Typeid type = desc.objecttype;
                String name = desc.objectname.toString();
                Pageid pageid = desc.pageid;

                //blacklist some plBoundInterface
                if(type==Typeid.plBoundInterface&&name.equals("PartColl08")&&pageid.prefix==0x5C&&pageid.suffix==0x23) return false; //Kveer
                if(type==Typeid.plBoundInterface&&name.equals("PartColl07")&&pageid.prefix==0x5C&&pageid.suffix==0x23) return false; //Kveer
                if(type==Typeid.plBoundInterface&&name.equals("PartColl06")&&pageid.prefix==0x5C&&pageid.suffix==0x23) return false; //Kveer
                if(type==Typeid.plBoundInterface && pageid.prefix==89) //Siralehn/Noloben
                {
                    if(name.equals("Cone01") || name.equals("doorParticleColliderMesh") || name.startsWith("RainDef0") || name.startsWith("RainDef1") || name.equals("RsinDefTop"))
                    {
                        return false;
                    }
                }
                if(type==Typeid.plBoundInterface && pageid.prefix==88) //tahgira
                {
                    if(name.equals("CaveSnowKiller01")) return false;
                    if(name.equals("FieldBubbleKiller")) return false; //this makes tahgira crash when you link there in 3rd person, I think.
                }

                // accept the rest
                return true;
            }
        };*/
        r.decider = prpobjects.prputils.Compiler.getDefaultDecider();
        //These are the oggs from MystV, that aren't already present in Pots or MoulOffline(they might be in the rest of Moul).
        //As it turns out, this is the same as the ones that simply aren't present in Pots.
        /*public static String[] mystvOggsNotInPotsNorMouloffline = {
            "drboCalmNight-Distant_Loop.ogg","drboEsher-DireboLaki_eng.ogg","drboEsher-DireboLaki_fre.ogg","drboEsher-DireboLaki_ger.ogg","drboEsher-DireboLaki_spa.ogg","drboEsher-DireboSrln_eng.ogg","drboEsher-DireboSrln_fre.ogg","drboEsher-DireboSrln_ger.ogg","drboEsher-DireboSrln_spa.ogg","drboEsher-DireboTdlm_eng.ogg","drboEsher-DireboTdlm_fre.ogg","drboEsher-DireboTdlm_ger.ogg","drboEsher-DireboTdlm_spa.ogg","drboEsher-DireboTghr_eng.ogg","drboEsher-DireboTghr_fre.ogg","drboEsher-DireboTghr_ger.ogg","drboEsher-DireboTghr_spa.ogg","drboGateButton.ogg","drboGate_Close.ogg","drboGate_LatchClose.ogg","drboGate_LeverClose.ogg","drboGate_LeverOpen.ogg","drboGate_Open.ogg","drboLakiBubbleAmb_Loop.ogg","drboRandomCricket01.ogg","drboRandomCricket02.ogg","drboRandomCricket03.ogg","drboRandomCritter01.ogg","drboRandomCritter02.ogg","drboRandomCritter03.ogg","drboRandomCritter04a.ogg","drboRandomCritter04b.ogg","drboRandomCritter04c.ogg","drboRandomCritter04d.ogg","drboRandomCritter04e.ogg","drboRandomCritter04f.ogg","drboRandomCritter04g.ogg","drboRandomCritter05.ogg","drboRandomCritter06a.ogg","drboRandomCritter06b.ogg","drboRandomCritter06c.ogg","drboRandomCritter06d.ogg","drboRandomCritter07a.ogg","drboRandomCritter07b.ogg","drboRandomCritter07c.ogg","drboRandomCritter07d.ogg","drboRandomCritter09a.ogg","drboRandomCritter09b.ogg","drboRandomCritter10.ogg","drboRandomCritter12a.ogg","drboRandomCritter12b.ogg","drboRockRandom01.ogg","drboRockRandom02.ogg","drboRockRandom03.ogg","drboRockRandom04.ogg","drboRockRandom05.ogg","drboRockRandom06.ogg","drboSiralehn-BubbleMix_Loop.ogg","drboTdlmBubbleAmb_Loop.ogg","drboThgrBubbleAmb_Loop.ogg","drboWaterLaps_Loop.ogg",
            "dsntAirShaftLadder_down.ogg","dsntAirShaftLadder_up.ogg","dsntBatFlock01_loop.ogg","dsntBats-Rnd01.ogg","dsntBats-Rnd02.ogg","dsntBats-Rnd03.ogg","dsntBats-Rnd04.ogg","dsntBats-Rnd05.ogg","dsntBats-Rnd06.ogg","dsntBats-Rnd07.ogg","dsntBats-Rnd08.ogg","dsntBats-Rnd09.ogg","dsntBats-Rnd10.ogg","dsntCave-PoolDrips_Loop.ogg","dsntCave-WaterDrips_Loop.ogg","dsntCave-WaterTrickle_Loop.ogg","dsntCave-WindAmb_Loop01.ogg","dsntDoor-Switch_Broken.ogg","dsntEarthquakeLoop.ogg","dsntElevator-Handle.ogg","dsntElevatorMusic.ogg","dsntElevLeverEngage.ogg","dsntElevLever_end.ogg","dsntEsher-BottomOfShaft_eng.ogg","dsntEsher-BottomOfShaft_fre.ogg","dsntEsher-BottomOfShaft_ger.ogg","dsntEsher-BottomOfShaft_spa.ogg","dsntEsher-FirstHub_eng.ogg","dsntEsher-FirstHub_fre.ogg","dsntEsher-FirstHub_ger.ogg","dsntEsher-FirstHub_spa.ogg","dsntEsher-Intro_eng.ogg","dsntEsher-Intro_fre.ogg","dsntEsher-Intro_ger.ogg","dsntEsher-Intro_Mx.ogg","dsntEsher-Intro_spa.ogg","dsntEsher-TopOfShaft_eng.ogg","dsntEsher-TopOfShaft_fre.ogg","dsntEsher-TopOfShaft_ger.ogg","dsntEsher-TopOfShaft_spa.ogg","dsntFan-NodeAmb_Loop.ogg","dsntFan-On_Loop.ogg","dsntFan-Power_Off.ogg","dsntFan-Power_On.ogg","dsntFan-Switch_Jiggle.ogg","dsntFloorColorButtonPress.ogg","dsntFloorElev-Countdown01.ogg","dsntFloorElev-Countdown02.ogg","dsntFloorElev-Countdown03.ogg","dsntFloorElev-Drive_Loop.ogg","dsntFloorElev-Gears_Loop.ogg","dsntFloorElev-Gears_Start.ogg","dsntFloorElev-Gears_Stop.ogg","dsntFloorElev-Pillars_End.ogg","dsntFloorElev-Pillars_Loop.ogg","dsntFloorElev-TimerSwitch.ogg","dsntFloorLowButton_down.ogg","dsntFloorLowButton_up.ogg","dsntFloorRaiseWarn.ogg","dsntFloorUpButton_down.ogg","dsntFloorUpButton_up.ogg","dsntGeneratorAmb_Loop.ogg","dsntImagerButtonClick.ogg","dsntImagerSquelch.ogg","dsntImager_Loop.ogg","dsntLavaDoor_Open.ogg","dsntMarbles_Loop.ogg","dsntNodeAmb_Loop.ogg","dsntNodeDoor_Open.ogg","dsntNodeLeverDragLoop.ogg","dsntRandomRocks01.ogg","dsntRandomRocks02.ogg","dsntRandomRocks03.ogg","dsntRndAbstractAmb01.ogg","dsntRndAbstractAmb02.ogg","dsntRndAbstractAmb03.ogg","dsntRndAbstractAmb04.ogg","dsntRndAbstractAmb05.ogg","dsntRndAbstractAmb06.ogg","dsntRndAbstractAmb07.ogg","dsntRndAbstractAmb08.ogg","dsntSafetyGate_close.ogg","dsntSafetyGate_open.ogg","dsntShaftAmb02_Loop.ogg","dsntSteamPipe01_Loop.ogg","dsntSteamPipe02_Loop.ogg","dsntSteamVents_Loop.ogg","dsntUpperElev-Away_Bottom.ogg","dsntUpperElev-Away_Top.ogg","dsntUpperElev-Dock_Bottom.ogg","dsntUpperElev-Dock_Top.ogg","dsntUpperElev-WeightsJiggle.ogg","dsntUpperElev-Weights_Loop.ogg","dsntUpperElevator_Ride.ogg","dsntUpperElevator_Start.ogg","dsntUpperElevator_Stop.ogg","dsntYeesha-Imager01Mx.ogg","dsntYeesha-Imager01_eng.ogg","dsntYeesha-Imager01_fre.ogg","dsntYeesha-Imager01_ger.ogg","dsntYeesha-Imager01_spa.ogg","dsntYeesha-Imager02Mx.ogg","dsntYeesha-Imager02_eng.ogg","dsntYeesha-Imager02_fre.ogg","dsntYeesha-Imager02_ger.ogg","dsntYeesha-Imager02_spa.ogg","dsntYeesha-Imager03Mx.ogg","dsntYeesha-Imager03_eng.ogg","dsntYeesha-Imager03_fre.ogg","dsntYeesha-Imager03_ger.ogg","dsntYeesha-Imager03_spa.ogg","dsntYeesha-Journal00_eng.ogg","dsntYeesha-Journal00_fre.ogg","dsntYeesha-Journal00_ger.ogg","dsntYeesha-Journal00_spa.ogg","dsntYeesha-Journal01_eng.ogg","dsntYeesha-Journal01_fre.ogg","dsntYeesha-Journal01_ger.ogg","dsntYeesha-Journal01_spa.ogg","dsntYeesha-Journal02_eng.ogg","dsntYeesha-Journal02_fre.ogg","dsntYeesha-Journal02_ger.ogg","dsntYeesha-Journal02_spa.ogg","dsntYeesha-Journal03_eng.ogg","dsntYeesha-Journal03_fre.ogg","dsntYeesha-Journal03_ger.ogg","dsntYeesha-Journal03_spa.ogg","dsntYeesha-Journal04_eng.ogg","dsntYeesha-Journal04_fre.ogg","dsntYeesha-Journal04_ger.ogg","dsntYeesha-Journal04_spa.ogg","dsntYeesha-Journal05_eng.ogg","dsntYeesha-Journal05_fre.ogg","dsntYeesha-Journal05_ger.ogg","dsntYeesha-Journal05_spa.ogg","dsntYeesha-Journal06_eng.ogg","dsntYeesha-Journal06_fre.ogg","dsntYeesha-Journal06_ger.ogg","dsntYeesha-Journal06_spa.ogg","dsntYeesha-Journal07_eng.ogg","dsntYeesha-Journal07_fre.ogg","dsntYeesha-Journal07_ger.ogg","dsntYeesha-Journal07_spa.ogg","dsntYeesha-Journal08_eng.ogg","dsntYeesha-Journal08_fre.ogg","dsntYeesha-Journal08_ger.ogg","dsntYeesha-Journal08_spa.ogg","dsntYeesha-Journal09_eng.ogg","dsntYeesha-Journal09_fre.ogg","dsntYeesha-Journal09_ger.ogg","dsntYeesha-Journal09_spa.ogg","dsntYeesha-Journal10_eng.ogg","dsntYeesha-Journal10_fre.ogg","dsntYeesha-Journal10_ger.ogg","dsntYeesha-Journal10_spa.ogg","dsntYeesha-Journal11_eng.ogg","dsntYeesha-Journal11_fre.ogg","dsntYeesha-Journal11_ger.ogg","dsntYeesha-Journal11_spa.ogg",
            "glblEsher-AfterSlate_eng.ogg","glblEsher-AfterSlate_fre.ogg","glblEsher-AfterSlate_ger.ogg","glblEsher-AfterSlate_spa.ogg","glblEsher-FirstAgeKeep_eng.ogg","glblEsher-FirstAgeKeep_fre.ogg","glblEsher-FirstAgeKeep_ger.ogg","glblEsher-FirstAgeKeep_spa.ogg","glblEsher-FirstAgeTake_eng.ogg","glblEsher-FirstAgeTake_fre.ogg","glblEsher-FirstAgeTake_ger.ogg","glblEsher-FirstAgeTake_spa.ogg","glblEsher-FourthAgeKeep_eng.ogg","glblEsher-FourthAgeKeep_fre.ogg","glblEsher-FourthAgeKeep_ger.ogg","glblEsher-FourthAgeKeep_spa.ogg","glblEsher-SecondAgeKeep_eng.ogg","glblEsher-SecondAgeKeep_fre.ogg","glblEsher-SecondAgeKeep_ger.ogg","glblEsher-SecondAgeKeep_spa.ogg","glblEsher-ThirdAgeKeep_eng.ogg","glblEsher-ThirdAgeKeep_fre.ogg","glblEsher-ThirdAgeKeep_ger.ogg","glblEsher-ThirdAgeKeep_spa.ogg",
            "kverAmb_Loop.ogg","kverAtrus_1_eng.ogg","kverAtrus_1_fre.ogg","kverAtrus_1_ger.ogg","kverAtrus_1_spa.ogg","kverBahroSneakUp.ogg","kverBahroTakeOff.ogg","kverBahroWingsCover.ogg","kverConc03MxPart01.ogg","kverConc03MxPart02.ogg","kverEsher_1_eng.ogg","kverEsher_1_fre.ogg","kverEsher_1_ger.ogg","kverEsher_1_spa.ogg","kverMystBookLocked.ogg","kverPrisonDoorHandle.ogg","kverPrisonDoor_Open.ogg","kverRandAmb01.ogg","kverReleeshanAmb.ogg","kverSwingingLamp_loop.ogg","kverTabletMaterialize.ogg","kverYeesha-Conc03_eng.ogg","kverYeesha-Conc03_fre.ogg","kverYeesha-Conc03_ger.ogg","kverYeesha-Conc03_spa.ogg","kverYeesha-IntroMx.ogg","kverYeesha-Intro_eng.ogg","kverYeesha-Intro_fre.ogg","kverYeesha-Intro_ger.ogg","kverYeesha-Intro_spa.ogg","kverYeeshaConc02Mx.ogg","kverYeesha_1_eng.ogg","kverYeesha_1_fre.ogg","kverYeesha_1_ger.ogg","kverYeesha_1_spa.ogg",
            "lakiArena-PedDownFull.ogg","lakiArena-PedDownHalf.ogg","lakiArena-PedUpFull.ogg","lakiArena-PedUpHalf.ogg","lakiArena-RevealMusic.ogg","LakiArena-ScaleLight_Off1.ogg","LakiArena-ScaleLight_On1.ogg","LakiArena-ScaleLight_On2.ogg","LakiArena-ScaleLight_On3.ogg","LakiArena-WeightBtnClose.ogg","LakiArena-WeightBtnOpen.ogg","lakiArena_PedEnd.ogg","lakiBird-Rnd01.ogg","lakiBird-Rnd02a.ogg","lakiBird-Rnd02b.ogg","lakiBird-Rnd02c.ogg","lakiBird-Rnd02d.ogg","lakiBird-Rnd02e.ogg","lakiBird-Rnd02f.ogg","lakiBird-Rnd02g.ogg","lakiBird-Rnd02h.ogg","lakiBird-Rnd03.ogg","lakiBird-Rnd04.ogg","lakiBird-Rnd06c.ogg","lakiBird-Rnd08.ogg","lakiBird-Rnd09.ogg","lakiBird-Rnd10a.ogg","lakiBird-Rnd10b.ogg","lakiBird-Rnd11.ogg","lakiBird-Rnd15.ogg","lakiBird-Rnd16.ogg","lakiBird-Rnd17.ogg","lakiBird-Rnd18a.ogg","lakiBird-Rnd18b.ogg","lakiBird-Rnd18c.ogg","lakiBird-Rnd18d.ogg","lakiBird-Rnd18e.ogg","lakiBird-Rnd18f.ogg","lakiBird-Rnd18g.ogg","lakiBird-Rnd20.ogg","lakiBird-Rnd21.ogg","lakiBird-Rnd23.ogg","lakiBird-Rnd24.ogg","lakiBird-Rnd25.ogg","lakiBoulderFallLarge.ogg","lakiBoulderFallMed.ogg","lakiBoulderFallSmall.ogg","lakiCageDoorBtn.ogg","lakiCageDoors_Close.ogg","lakiCageDoors_Open.ogg","lakiElevatorLever-Latch01.ogg","lakiElevatorLeverDrag_Loop.ogg","lakiEsher-Arena_eng.ogg","lakiEsher-Arena_fre.ogg","lakiEsher-Arena_ger.ogg","lakiEsher-Arena_Mx.ogg","lakiEsher-Arena_spa.ogg","lakiEsher-FighterBeach_eng.ogg","lakiEsher-FighterBeach_fre.ogg","lakiEsher-FighterBeach_ger.ogg","lakiEsher-FighterBeach_spa.ogg","lakiEsher-Keep_eng.ogg","lakiEsher-Keep_fre.ogg","lakiEsher-Keep_ger.ogg","lakiEsher-Keep_Mx.ogg","lakiEsher-Keep_spa.ogg","lakiEsher-Villa_eng.ogg","lakiEsher-Villa_fre.ogg","lakiEsher-Villa_ger.ogg","lakiEsher-Villa_Mx.ogg","lakiEsher-Villa_spa.ogg","lakiHutFulcrum-DoorLand.ogg","lakiHutFulcrum-MoveFull.ogg","lakiHutFulcrum-MoveHalf.ogg","lakiHutPulley_loop.ogg","lakiMaze-Elevator_down.ogg","lakiMaze-Elevator_up.ogg","lakiMazeButton01.ogg","LakiMazeDoor1Close.ogg","LakiMazeDoor1Open.ogg","LakiMazeExitDoorClose.ogg","LakiMazeExitDoorOpen.ogg","lakiOceanAirAmb_Loop.ogg","lakiPirBird_Vocalize01.ogg","LakiPuzzleDoorBolt.ogg","LakiPuzzleDoorButton01.ogg","LakiPuzzleDoorButton02.ogg","LakiPuzzleDoorButton03.ogg","LakiPuzzleDoorOpen.ogg","lakiRandMusic01.ogg","lakiRandMusic02.ogg","lakiRandMusic03.ogg","lakiRandMusic04.ogg","lakiRandMusic05.ogg","lakiRandMusic06.ogg","lakiRandMusic07.ogg","lakiRandMusic08.ogg","lakiRandMusic09.ogg","lakiRegisterBtn_press.ogg","lakiRegisterBtn_unpress.ogg","lakiTunnelAmb_loop.ogg","lakiVillaFrontDoorClose.ogg","lakiVillaFrontDoorOpen.ogg","lakiWaterLaps_Loop.ogg","lakiWhaleCall_Loop.ogg","LakiWind-Beach_loop.ogg","lakiWindAmb01_Loop.ogg","LakiWindBranches_loop.ogg","LakiWindLeaves_loop.ogg","LakiWindMetal_loop.ogg","lakiWindmill-CageLever_On01.ogg","lakiWindmill-Lever_loop.ogg","lakiWindmill-PuzzleMusic_Loop.ogg","lakiWindmill_Loop.ogg","LakiWindThruFence_loop.ogg","lakiWindWaterCombo_Loop.ogg","laki_BahroCommandWind.ogg","Laki_ScalePlateDown.ogg","Laki_ScalePlateUp.ogg",
            "mystAmbMusic.ogg","mystCliffs-WavesBreak_Loop.ogg","mystDockWaveAmb.ogg","mystDoorSlam_Loop.ogg","mystEsher-Conc01Mx.ogg","mystEsher-Conc01_eng.ogg","mystEsher-Conc01_fre.ogg","mystEsher-Conc01_ger.ogg","mystEsher-Conc01_spa.ogg","mystEsher-Conc02Mx.ogg","mystEsher-Conc02_eng.ogg","mystEsher-Conc02_fre.ogg","mystEsher-Conc02_ger.ogg","mystEsher-Conc02_spa.ogg","mystHandleBreak.ogg","mystMastCreak01.ogg","mystMastCreak02.ogg","mystMastCreak03.ogg","mystOceanAir_Loop.ogg","mystRainOnGrass_InLoop.ogg","mystRainOnMetal-Ext_Loop01.ogg","mystRainOnMetal-Ext_Loop02.ogg","mystRainOnMetal-Int_Loop.ogg","mystRainOnWater_InLoop.ogg","mystShipCreak_Loop.ogg","mystTabletTrap.ogg","mystThunder01.ogg","mystThunder02.ogg","mystThunder03.ogg","mystThunder04.ogg","mystThunder05.ogg","mystThunder06.ogg","mystThunder07.ogg","mystWind-Animating_Loop.ogg","mystWind-SpookyAmb_Loop.ogg","mystWind-ThruTrees_Loop.ogg","mystWind_InLoop.ogg",
            "srlnBird01.ogg","srlnBird02.ogg","srlnBird03.ogg","srlnBird04.ogg","srlnBird05.ogg","srlnBird06.ogg","srlnBird07.ogg","srlnBird08.ogg","srlnBird09.ogg","srlnBird10.ogg","srlnBird11.ogg","srlnBird12.ogg","srlnBird13.ogg","srlnCliffLadder-Rope_loop.ogg","srlnCliffLadder_Drop.ogg","srlnEsher-NolobenBeach_eng.ogg","srlnEsher-NolobenBeach_fre.ogg","srlnEsher-NolobenBeach_ger.ogg","srlnEsher-NolobenBeach_spa.ogg","srlnEsher-NolobenKeep_eng.ogg","srlnEsher-NolobenKeep_fre.ogg","srlnEsher-NolobenKeep_ger.ogg","srlnEsher-NolobenKeep_spa.ogg","srlnEsher-NolobenLab_eng.ogg","srlnEsher-NolobenLab_fre.ogg","srlnEsher-NolobenLab_ger.ogg","srlnEsher-NolobenLab_spa.ogg","srlnKeepDoor_appear.ogg","srlnKeepDoor_disappear.ogg","srlnMainMusic_Loop.ogg","srlnOuterWatersAmb_Loop.ogg","srlnPoolPuzzleWaterDrain.ogg","srlnPoolTrickle_Loop.ogg","srlnRainInTunnels_Loop.ogg","srlnRainOnGrass_Loop.ogg","srlnRainOnWater_Loop.ogg","srlnRandMusic01.ogg","srlnRandMusic02.ogg","srlnRandMusic03.ogg","srlnRandMusic04.ogg","srlnRandMusic05.ogg","srlnRandMusic06.ogg","srlnRandMusic07.ogg","srlnRandMusic08.ogg","srlnRandMusic09.ogg","srlnRandMusic10.ogg","srlnRandMusic11.ogg","srlnRandMusic12.ogg","srlnRandMusic13.ogg","srlnRandMusic14.ogg","srlnRandMusic15.ogg","srlnRockAmb_Loop.ogg","srlnRockPillar-Rotate_Loop.ogg","srlnRockWindow_Close.ogg","srlnRockWindow_Open.ogg","srlnShoreWavesLight_Lp.ogg","srlnTunnels-Aperture01_Loop.ogg","srlnTunnels-Aperture02_Loop.ogg","srlnTunnels-Aperture03_Loop.ogg","srlnTunnels-Aperture04b_Loop.ogg","srlnTunnels-Aperture04_Loop.ogg","srlnTunnels-Aperture05_Loop.ogg","srlnTunnelsAmb_Loop.ogg","srlnUndergroundRockRumble.ogg","srlnUpperBugs_Loop.ogg","srlnWindAmb01_Loop.ogg","srlnYeeshaSymbGlow.ogg","srln_BahroCommandRain.ogg",
            "tdlmAirLockdoor_Close.ogg","tdlmAirLockDoor_Open.ogg","tdlmAmb01_Loop.ogg","tdlmAmbMusic01_loop.ogg","tdlmAmbWind01_Loop.ogg","tdlmBigScopeCables_loop.ogg","tdlmCableBrake.ogg","tdlmCableSway-Fast_loop.ogg","tdlmCableSway_Loop.ogg","tdlmCableSway_Loop02.ogg","tdlmclockFast_loop.ogg","tdlmClockTurn01.ogg","tdlmClockTurn02.ogg","tdlmClockTurn03.ogg","tdlmDock-Handle_loop.ogg","tdlmDock-Pump02_loop.ogg","tdlmDock-PumpBeginning.ogg","tdlmEsher-TodelmerP1_eng.ogg","tdlmEsher-TodelmerP1_fre.ogg","tdlmEsher-TodelmerP1_ger.ogg","tdlmEsher-TodelmerP1_Mx.ogg","tdlmEsher-TodelmerP1_spa.ogg","tdlmEsher-TodelmerP3_eng.ogg","tdlmEsher-TodelmerP3_fre.ogg","tdlmEsher-TodelmerP3_ger.ogg","tdlmEsher-TodelmerP3_spa.ogg","tdlmEsher-TodelmerRing_eng.ogg","tdlmEsher-TodelmerRing_fre.ogg","tdlmEsher-TodelmerRing_ger.ogg","tdlmEsher-TodelmerRing_spa.ogg","tdlmInsideGUILevers01.ogg","tdlmInsideGUILevers02.ogg","tdlmInsideGUILevers03.ogg","tdlmJoySticksDragLoop.ogg","tdlmPodAmb_Loop.ogg","tdlmPodRand_01.ogg","tdlmPodRand_02.ogg","tdlmPodRand_03.ogg","tdlmPodRand_04.ogg","tdlmPodRand_05.ogg","tdlmPodRand_06.ogg","tdlmPower-Ring_Loop.ogg","tdlmPower-Ring_off.ogg","tdlmPower-Ring_Start.ogg","tdlmPowerOn_Loop.ogg","tdlmScope-ZoomButton.ogg","tdlmScopeGUI-Sliders.ogg","tdlmScreenStatic_loop.ogg","tdlmSpeedUpAmb.ogg","tdlmStoneStairs_Hide.ogg","tdlmStoneStairs_Unhide.ogg","tdlmTram-BigSpool_loop.ogg","tdlmTramCar-Lever_loop.ogg","tdlmTramCar-MidtoP1.ogg","tdlmTramCar-MidTurn.ogg","tdlmTramCar-P3toMid.ogg","tdlmTramCarDoors_close.ogg","tdlmTramCarDoors_open.ogg","tdlmTramDockCables.ogg","tdlm_BahroCommandTime.ogg",
            "thgrDistantAmb.ogg","thgrEsher-TahgiraGrave_eng.ogg","thgrEsher-TahgiraGrave_fre.ogg","thgrEsher-TahgiraGrave_ger.ogg","thgrEsher-TahgiraGrave_spa.ogg","thgrEsher-TahgiraIntro_eng.ogg","thgrEsher-TahgiraIntro_fre.ogg","thgrEsher-TahgiraIntro_ger.ogg","thgrEsher-TahgiraIntro_spa.ogg","thgrEsher-TahgiraTake_eng.ogg","thgrEsher-TahgiraTake_fre.ogg","thgrEsher-TahgiraTake_ger.ogg","thgrEsher-TahgiraTake_spa.ogg","thgrEsher-TahgiraVillage_eng.ogg","thgrEsher-TahgiraVillage_fre.ogg","thgrEsher-TahgiraVillage_ger.ogg","thgrEsher-TahgiraVillage_spa.ogg","thgrEsher-Thermals_eng.ogg","thgrEsher-Thermals_fre.ogg","thgrEsher-Thermals_ger.ogg","thgrEsher-Thermals_spa.ogg","thgrGeyser_Loop.ogg","thgrIceCaveAmb02_Loop.ogg","thgrIceCaveWind_Loop.ogg","thgrIceCave_Loop.ogg","thgrIceCrack01.ogg","thgrIceCrack02.ogg","thgrIceCrack03.ogg","thgrIceCrack04.ogg","thgrIceCrack05.ogg","thgrIceCrack06.ogg","thgrIceCrack07.ogg","thgrIceCrack08.ogg","thgrIceFieldBubbles_loop.ogg","thgrIceFildsMx_loop.ogg","thgrIceWaves01.ogg","thgrKeepBreakaway.ogg","thgrRandomIce01.ogg","thgrRandomIce02.ogg","thgrRandomIce03.ogg","thgrRandomIce04.ogg","thgrRandomIce05.ogg","thgrRandomIce06.ogg","thgrRandomIce07.ogg","thgrRandomIce08.ogg","thgrSteamPipe01_Loop.ogg","thgrSteamPipe02_Loop.ogg","thgrSteamVents_Loop.ogg","thgrSteamVents_Loop02.ogg","thgrThermalLeverDrag.ogg","thgrThrmlActivity_loop01.ogg","thgrWaterFieldHandle_Down.ogg","thgrWaterFieldHandle_Up.ogg","thgrWind_Loop01.ogg","thgr_BahroCommandHeat.ogg",
            "xAudioBubble_Enter.ogg","xAudioBubble_Exit.ogg","xBahro51.ogg","xBahro52.ogg","xBahro54.ogg","xBahro55.ogg","xBahro56.ogg","xBahro58.ogg","xBahro59.ogg","xBahro62.ogg","xBahro64.ogg","xBahro69.ogg","xBahro70.ogg","xBahroConfused.ogg","xBahroFriendship.ogg","xBahroLink.ogg","xBahroPickup01.ogg","xBahroPickup02.ogg","xBahroReturn01.ogg","xBahroReturn02.ogg","xBahroReturn03.ogg","xBahroSing.ogg","xBahroSlate-Draw_Loop.ogg","xBahrosnake.ogg","xBahroTimid19.ogg","xBahroTimid20.ogg","xBahroTorture.ogg","xBhroLinkIn_Clean.ogg","xBhroLinkIn_Scared.ogg","xbhroPlaceSlate01b.ogg","xBubbleAmb_loop.ogg","xBubbleMusic.ogg","xCameraPickUp.ogg","xdrboIntBubbleAmb.ogg","xFlySwarm.ogg","xKeepUnlock.ogg","xLakiBubKeepAmb_Loop.ogg","xOptionScreenSFX01.ogg","xOptionScreenSFX02.ogg","xOptionScreenSFX03.ogg","xScreenshot.ogg","xSlateVaporToSolid.ogg","xSpecialTransitionEffect03.ogg","xSrlnBubKeepAmb_Loop.ogg","xTakeSymbolGlow.ogg","xTdlmBubKeepAmb_Loop.ogg","xThgrBubKeepAmb_Loop.ogg",
        };*/
        r.addSoundFiles(new String[]{
            //This one is like mystvOggsNotInPotsNorMouloffline, but it removes all the speeches which end in _eng, _fre, _ger, and _spa.
            //Takes only half as much space.
            //public static String[] mystvOggsNotInPotsNorMoulofflineMinusSpeeches = {
            "drboCalmNight-Distant_Loop.ogg","drboGateButton.ogg","drboGate_Close.ogg","drboGate_LatchClose.ogg","drboGate_LeverClose.ogg","drboGate_LeverOpen.ogg","drboGate_Open.ogg","drboLakiBubbleAmb_Loop.ogg","drboRandomCricket01.ogg","drboRandomCricket02.ogg","drboRandomCricket03.ogg","drboRandomCritter01.ogg","drboRandomCritter02.ogg","drboRandomCritter03.ogg","drboRandomCritter04a.ogg","drboRandomCritter04b.ogg","drboRandomCritter04c.ogg","drboRandomCritter04d.ogg","drboRandomCritter04e.ogg","drboRandomCritter04f.ogg","drboRandomCritter04g.ogg","drboRandomCritter05.ogg","drboRandomCritter06a.ogg","drboRandomCritter06b.ogg","drboRandomCritter06c.ogg","drboRandomCritter06d.ogg","drboRandomCritter07a.ogg","drboRandomCritter07b.ogg","drboRandomCritter07c.ogg","drboRandomCritter07d.ogg","drboRandomCritter09a.ogg","drboRandomCritter09b.ogg","drboRandomCritter10.ogg","drboRandomCritter12a.ogg","drboRandomCritter12b.ogg","drboRockRandom01.ogg","drboRockRandom02.ogg","drboRockRandom03.ogg","drboRockRandom04.ogg","drboRockRandom05.ogg","drboRockRandom06.ogg","drboSiralehn-BubbleMix_Loop.ogg","drboTdlmBubbleAmb_Loop.ogg","drboThgrBubbleAmb_Loop.ogg","drboWaterLaps_Loop.ogg",
            "dsntAirShaftLadder_down.ogg","dsntAirShaftLadder_up.ogg","dsntBatFlock01_loop.ogg","dsntBats-Rnd01.ogg","dsntBats-Rnd02.ogg","dsntBats-Rnd03.ogg","dsntBats-Rnd04.ogg","dsntBats-Rnd05.ogg","dsntBats-Rnd06.ogg","dsntBats-Rnd07.ogg","dsntBats-Rnd08.ogg","dsntBats-Rnd09.ogg","dsntBats-Rnd10.ogg","dsntCave-PoolDrips_Loop.ogg","dsntCave-WaterDrips_Loop.ogg","dsntCave-WaterTrickle_Loop.ogg","dsntCave-WindAmb_Loop01.ogg","dsntDoor-Switch_Broken.ogg","dsntEarthquakeLoop.ogg","dsntElevator-Handle.ogg","dsntElevatorMusic.ogg","dsntElevLeverEngage.ogg","dsntElevLever_end.ogg","dsntEsher-Intro_Mx.ogg","dsntFan-NodeAmb_Loop.ogg","dsntFan-On_Loop.ogg","dsntFan-Power_Off.ogg","dsntFan-Power_On.ogg","dsntFan-Switch_Jiggle.ogg","dsntFloorColorButtonPress.ogg","dsntFloorElev-Countdown01.ogg","dsntFloorElev-Countdown02.ogg","dsntFloorElev-Countdown03.ogg","dsntFloorElev-Drive_Loop.ogg","dsntFloorElev-Gears_Loop.ogg","dsntFloorElev-Gears_Start.ogg","dsntFloorElev-Gears_Stop.ogg","dsntFloorElev-Pillars_End.ogg","dsntFloorElev-Pillars_Loop.ogg","dsntFloorElev-TimerSwitch.ogg","dsntFloorLowButton_down.ogg","dsntFloorLowButton_up.ogg","dsntFloorRaiseWarn.ogg","dsntFloorUpButton_down.ogg","dsntFloorUpButton_up.ogg","dsntGeneratorAmb_Loop.ogg","dsntImagerButtonClick.ogg","dsntImagerSquelch.ogg","dsntImager_Loop.ogg","dsntLavaDoor_Open.ogg","dsntMarbles_Loop.ogg","dsntNodeAmb_Loop.ogg","dsntNodeDoor_Open.ogg","dsntNodeLeverDragLoop.ogg","dsntRandomRocks01.ogg","dsntRandomRocks02.ogg","dsntRandomRocks03.ogg","dsntRndAbstractAmb01.ogg","dsntRndAbstractAmb02.ogg","dsntRndAbstractAmb03.ogg","dsntRndAbstractAmb04.ogg","dsntRndAbstractAmb05.ogg","dsntRndAbstractAmb06.ogg","dsntRndAbstractAmb07.ogg","dsntRndAbstractAmb08.ogg","dsntSafetyGate_close.ogg","dsntSafetyGate_open.ogg","dsntShaftAmb02_Loop.ogg","dsntSteamPipe01_Loop.ogg","dsntSteamPipe02_Loop.ogg","dsntSteamVents_Loop.ogg","dsntUpperElev-Away_Bottom.ogg","dsntUpperElev-Away_Top.ogg","dsntUpperElev-Dock_Bottom.ogg","dsntUpperElev-Dock_Top.ogg","dsntUpperElev-WeightsJiggle.ogg","dsntUpperElev-Weights_Loop.ogg","dsntUpperElevator_Ride.ogg","dsntUpperElevator_Start.ogg","dsntUpperElevator_Stop.ogg","dsntYeesha-Imager01Mx.ogg","dsntYeesha-Imager02Mx.ogg","dsntYeesha-Imager03Mx.ogg",
            //
            "kverAmb_Loop.ogg","kverBahroSneakUp.ogg","kverBahroTakeOff.ogg","kverBahroWingsCover.ogg","kverConc03MxPart01.ogg","kverConc03MxPart02.ogg","kverMystBookLocked.ogg","kverPrisonDoorHandle.ogg","kverPrisonDoor_Open.ogg","kverRandAmb01.ogg","kverReleeshanAmb.ogg","kverSwingingLamp_loop.ogg","kverTabletMaterialize.ogg","kverYeesha-IntroMx.ogg","kverYeeshaConc02Mx.ogg",
            "lakiArena-PedDownFull.ogg","lakiArena-PedDownHalf.ogg","lakiArena-PedUpFull.ogg","lakiArena-PedUpHalf.ogg","lakiArena-RevealMusic.ogg","LakiArena-ScaleLight_Off1.ogg","LakiArena-ScaleLight_On1.ogg","LakiArena-ScaleLight_On2.ogg","LakiArena-ScaleLight_On3.ogg","LakiArena-WeightBtnClose.ogg","LakiArena-WeightBtnOpen.ogg","lakiArena_PedEnd.ogg","lakiBird-Rnd01.ogg","lakiBird-Rnd02a.ogg","lakiBird-Rnd02b.ogg","lakiBird-Rnd02c.ogg","lakiBird-Rnd02d.ogg","lakiBird-Rnd02e.ogg","lakiBird-Rnd02f.ogg","lakiBird-Rnd02g.ogg","lakiBird-Rnd02h.ogg","lakiBird-Rnd03.ogg","lakiBird-Rnd04.ogg","lakiBird-Rnd06c.ogg","lakiBird-Rnd08.ogg","lakiBird-Rnd09.ogg","lakiBird-Rnd10a.ogg","lakiBird-Rnd10b.ogg","lakiBird-Rnd11.ogg","lakiBird-Rnd15.ogg","lakiBird-Rnd16.ogg","lakiBird-Rnd17.ogg","lakiBird-Rnd18a.ogg","lakiBird-Rnd18b.ogg","lakiBird-Rnd18c.ogg","lakiBird-Rnd18d.ogg","lakiBird-Rnd18e.ogg","lakiBird-Rnd18f.ogg","lakiBird-Rnd18g.ogg","lakiBird-Rnd20.ogg","lakiBird-Rnd21.ogg","lakiBird-Rnd23.ogg","lakiBird-Rnd24.ogg","lakiBird-Rnd25.ogg","lakiBoulderFallLarge.ogg","lakiBoulderFallMed.ogg","lakiBoulderFallSmall.ogg","lakiCageDoorBtn.ogg","lakiCageDoors_Close.ogg","lakiCageDoors_Open.ogg","lakiElevatorLever-Latch01.ogg","lakiElevatorLeverDrag_Loop.ogg","lakiEsher-Arena_Mx.ogg","lakiEsher-Keep_Mx.ogg","lakiEsher-Villa_Mx.ogg","lakiHutFulcrum-DoorLand.ogg","lakiHutFulcrum-MoveFull.ogg","lakiHutFulcrum-MoveHalf.ogg","lakiHutPulley_loop.ogg","lakiMaze-Elevator_down.ogg","lakiMaze-Elevator_up.ogg","lakiMazeButton01.ogg","LakiMazeDoor1Close.ogg","LakiMazeDoor1Open.ogg","LakiMazeExitDoorClose.ogg","LakiMazeExitDoorOpen.ogg","lakiOceanAirAmb_Loop.ogg","lakiPirBird_Vocalize01.ogg","LakiPuzzleDoorBolt.ogg","LakiPuzzleDoorButton01.ogg","LakiPuzzleDoorButton02.ogg","LakiPuzzleDoorButton03.ogg","LakiPuzzleDoorOpen.ogg","lakiRandMusic01.ogg","lakiRandMusic02.ogg","lakiRandMusic03.ogg","lakiRandMusic04.ogg","lakiRandMusic05.ogg","lakiRandMusic06.ogg","lakiRandMusic07.ogg","lakiRandMusic08.ogg","lakiRandMusic09.ogg","lakiRegisterBtn_press.ogg","lakiRegisterBtn_unpress.ogg","lakiTunnelAmb_loop.ogg","lakiVillaFrontDoorClose.ogg","lakiVillaFrontDoorOpen.ogg","lakiWaterLaps_Loop.ogg","lakiWhaleCall_Loop.ogg","LakiWind-Beach_loop.ogg","lakiWindAmb01_Loop.ogg","LakiWindBranches_loop.ogg","LakiWindLeaves_loop.ogg","LakiWindMetal_loop.ogg","lakiWindmill-CageLever_On01.ogg","lakiWindmill-Lever_loop.ogg","lakiWindmill-PuzzleMusic_Loop.ogg","lakiWindmill_Loop.ogg","LakiWindThruFence_loop.ogg","lakiWindWaterCombo_Loop.ogg","laki_BahroCommandWind.ogg","Laki_ScalePlateDown.ogg","Laki_ScalePlateUp.ogg",
            "mystAmbMusic.ogg","mystCliffs-WavesBreak_Loop.ogg","mystDockWaveAmb.ogg","mystDoorSlam_Loop.ogg","mystEsher-Conc01Mx.ogg","mystEsher-Conc02Mx.ogg","mystHandleBreak.ogg","mystMastCreak01.ogg","mystMastCreak02.ogg","mystMastCreak03.ogg","mystOceanAir_Loop.ogg","mystRainOnGrass_InLoop.ogg","mystRainOnMetal-Ext_Loop01.ogg","mystRainOnMetal-Ext_Loop02.ogg","mystRainOnMetal-Int_Loop.ogg","mystRainOnWater_InLoop.ogg","mystShipCreak_Loop.ogg","mystTabletTrap.ogg","mystThunder01.ogg","mystThunder02.ogg","mystThunder03.ogg","mystThunder04.ogg","mystThunder05.ogg","mystThunder06.ogg","mystThunder07.ogg","mystWind-Animating_Loop.ogg","mystWind-SpookyAmb_Loop.ogg","mystWind-ThruTrees_Loop.ogg","mystWind_InLoop.ogg",
            "srlnBird01.ogg","srlnBird02.ogg","srlnBird03.ogg","srlnBird04.ogg","srlnBird05.ogg","srlnBird06.ogg","srlnBird07.ogg","srlnBird08.ogg","srlnBird09.ogg","srlnBird10.ogg","srlnBird11.ogg","srlnBird12.ogg","srlnBird13.ogg","srlnCliffLadder-Rope_loop.ogg","srlnCliffLadder_Drop.ogg","srlnKeepDoor_appear.ogg","srlnKeepDoor_disappear.ogg","srlnMainMusic_Loop.ogg","srlnOuterWatersAmb_Loop.ogg","srlnPoolPuzzleWaterDrain.ogg","srlnPoolTrickle_Loop.ogg","srlnRainInTunnels_Loop.ogg","srlnRainOnGrass_Loop.ogg","srlnRainOnWater_Loop.ogg","srlnRandMusic01.ogg","srlnRandMusic02.ogg","srlnRandMusic03.ogg","srlnRandMusic04.ogg","srlnRandMusic05.ogg","srlnRandMusic06.ogg","srlnRandMusic07.ogg","srlnRandMusic08.ogg","srlnRandMusic09.ogg","srlnRandMusic10.ogg","srlnRandMusic11.ogg","srlnRandMusic12.ogg","srlnRandMusic13.ogg","srlnRandMusic14.ogg","srlnRandMusic15.ogg","srlnRockAmb_Loop.ogg","srlnRockPillar-Rotate_Loop.ogg","srlnRockWindow_Close.ogg","srlnRockWindow_Open.ogg","srlnShoreWavesLight_Lp.ogg","srlnTunnels-Aperture01_Loop.ogg","srlnTunnels-Aperture02_Loop.ogg","srlnTunnels-Aperture03_Loop.ogg","srlnTunnels-Aperture04b_Loop.ogg","srlnTunnels-Aperture04_Loop.ogg","srlnTunnels-Aperture05_Loop.ogg","srlnTunnelsAmb_Loop.ogg","srlnUndergroundRockRumble.ogg","srlnUpperBugs_Loop.ogg","srlnWindAmb01_Loop.ogg","srlnYeeshaSymbGlow.ogg","srln_BahroCommandRain.ogg",
            "tdlmAirLockdoor_Close.ogg","tdlmAirLockDoor_Open.ogg","tdlmAmb01_Loop.ogg","tdlmAmbMusic01_loop.ogg","tdlmAmbWind01_Loop.ogg","tdlmBigScopeCables_loop.ogg","tdlmCableBrake.ogg","tdlmCableSway-Fast_loop.ogg","tdlmCableSway_Loop.ogg","tdlmCableSway_Loop02.ogg","tdlmclockFast_loop.ogg","tdlmClockTurn01.ogg","tdlmClockTurn02.ogg","tdlmClockTurn03.ogg","tdlmDock-Handle_loop.ogg","tdlmDock-Pump02_loop.ogg","tdlmDock-PumpBeginning.ogg","tdlmEsher-TodelmerP1_Mx.ogg","tdlmInsideGUILevers01.ogg","tdlmInsideGUILevers02.ogg","tdlmInsideGUILevers03.ogg","tdlmJoySticksDragLoop.ogg","tdlmPodAmb_Loop.ogg","tdlmPodRand_01.ogg","tdlmPodRand_02.ogg","tdlmPodRand_03.ogg","tdlmPodRand_04.ogg","tdlmPodRand_05.ogg","tdlmPodRand_06.ogg","tdlmPower-Ring_Loop.ogg","tdlmPower-Ring_off.ogg","tdlmPower-Ring_Start.ogg","tdlmPowerOn_Loop.ogg","tdlmScope-ZoomButton.ogg","tdlmScopeGUI-Sliders.ogg","tdlmScreenStatic_loop.ogg","tdlmSpeedUpAmb.ogg","tdlmStoneStairs_Hide.ogg","tdlmStoneStairs_Unhide.ogg","tdlmTram-BigSpool_loop.ogg","tdlmTramCar-Lever_loop.ogg","tdlmTramCar-MidtoP1.ogg","tdlmTramCar-MidTurn.ogg","tdlmTramCar-P3toMid.ogg","tdlmTramCarDoors_close.ogg","tdlmTramCarDoors_open.ogg","tdlmTramDockCables.ogg","tdlm_BahroCommandTime.ogg",
            "thgrDistantAmb.ogg","thgrGeyser_Loop.ogg","thgrIceCaveAmb02_Loop.ogg","thgrIceCaveWind_Loop.ogg","thgrIceCave_Loop.ogg","thgrIceCrack01.ogg","thgrIceCrack02.ogg","thgrIceCrack03.ogg","thgrIceCrack04.ogg","thgrIceCrack05.ogg","thgrIceCrack06.ogg","thgrIceCrack07.ogg","thgrIceCrack08.ogg","thgrIceFieldBubbles_loop.ogg","thgrIceFildsMx_loop.ogg","thgrIceWaves01.ogg","thgrKeepBreakaway.ogg","thgrRandomIce01.ogg","thgrRandomIce02.ogg","thgrRandomIce03.ogg","thgrRandomIce04.ogg","thgrRandomIce05.ogg","thgrRandomIce06.ogg","thgrRandomIce07.ogg","thgrRandomIce08.ogg","thgrSteamPipe01_Loop.ogg","thgrSteamPipe02_Loop.ogg","thgrSteamVents_Loop.ogg","thgrSteamVents_Loop02.ogg","thgrThermalLeverDrag.ogg","thgrThrmlActivity_loop01.ogg","thgrWaterFieldHandle_Down.ogg","thgrWaterFieldHandle_Up.ogg","thgrWind_Loop01.ogg","thgr_BahroCommandHeat.ogg",
            "xAudioBubble_Enter.ogg","xAudioBubble_Exit.ogg","xBahro51.ogg","xBahro52.ogg","xBahro54.ogg","xBahro55.ogg","xBahro56.ogg","xBahro58.ogg","xBahro59.ogg","xBahro62.ogg","xBahro64.ogg","xBahro69.ogg","xBahro70.ogg","xBahroConfused.ogg","xBahroFriendship.ogg","xBahroLink.ogg","xBahroPickup01.ogg","xBahroPickup02.ogg","xBahroReturn01.ogg","xBahroReturn02.ogg","xBahroReturn03.ogg","xBahroSing.ogg","xBahroSlate-Draw_Loop.ogg","xBahrosnake.ogg","xBahroTimid19.ogg","xBahroTimid20.ogg","xBahroTorture.ogg","xBhroLinkIn_Clean.ogg","xBhroLinkIn_Scared.ogg","xbhroPlaceSlate01b.ogg","xBubbleAmb_loop.ogg","xBubbleMusic.ogg","xCameraPickUp.ogg","xdrboIntBubbleAmb.ogg","xFlySwarm.ogg","xKeepUnlock.ogg","xLakiBubKeepAmb_Loop.ogg","xOptionScreenSFX01.ogg","xOptionScreenSFX02.ogg","xOptionScreenSFX03.ogg","xScreenshot.ogg","xSlateVaporToSolid.ogg","xSpecialTransitionEffect03.ogg","xSrlnBubKeepAmb_Loop.ogg","xTakeSymbolGlow.ogg","xTdlmBubKeepAmb_Loop.ogg","xThgrBubKeepAmb_Loop.ogg",
            
            // bring back yeesha imagers
            "dsntYeesha-Imager01_eng.ogg", "dsntYeesha-Imager01_fre.ogg", "dsntYeesha-Imager01_ger.ogg", "dsntYeesha-Imager02_eng.ogg", "dsntYeesha-Imager02_fre.ogg", "dsntYeesha-Imager02_ger.ogg", "dsntYeesha-Imager03_eng.ogg", "dsntYeesha-Imager03_fre.ogg", "dsntYeesha-Imager03_ger.ogg",
        });
        r.fnimodifier = new conversion.FniModifier() {
            @Override
            public void ModifyFni(Info info, FileInfo file, textfile tf) {
                if(file.agename.equals("Laki"))
                {
                    // Myst V defaults fog settings when linking to a new Age if these are not present.
                    // In Uru, it will just keep the previous' Age fog settings, so we need
                    // to give Laki its own settings so it doesn't copy the ones from the Relto.
                    
                    //textfile fnifile = textfile.createFromBytes(decryptedData);
                    //fnifile.appendLine("Graphics.Renderer.SetClearColor 0 0 0");
                    //fnifile.appendLine("Graphics.Renderer.Fog.SetDefColor 0 0 0");
                    tf.appendLine("Graphics.Renderer.Fog.SetDefLinear 0 0 0");
                    //decryptedData = fnifile.saveToBytes();
                }
                if (file.agename.equals("DescentMystV") || file.agename.equals("Descent"))
                {
                    // Makes fog black in descent, instead of the default supernatural green.
                    // I see absolutely no reason to remove this. However, if you still prefer the old
                    // fog color, please tell Sirius he is a thickheaded nerd, and revert the changes.
                    for(textfile.textline line: tf.getLines())
                    {
                        String linestr = line.getString();
                        if(linestr.startsWith("Graphics.Renderer.Fog.SetDefColor") || linestr.startsWith("Graphics.Renderer.SetClearColor")) //otherwise it disables gamma in the engine.
                        {
                            line.setString("#"+linestr);
                        }
                    }
                    tf.appendLine("Graphics.Renderer.Fog.SetDefColor 0 0 0");
                    tf.appendLine("Graphics.Renderer.SetClearColor 0 0 0");
                }
            }
        };
        r.prpmodifier = new conversion.PostConversionModifier() {
            public void ModifyPrp(Info info, FileInfo file, prpfile prp) {
                String newAgename = info.g.getNewAgename(file);
                PostMod_AutomateMyst5(prp,info.infolder,newAgename);
            }
        };
        //r.addInplacemod("Cleft","/dat/Cleft_District_Desert.prp", "RemoveFence");
        //r.addInplacemod("Cleft","/dat/Cleft_District_tmnaDesert.prp", "RemoveFence");
        r.addInplacemods(
        //    "/dat/Cleft_District_Desert.prp",
        //    "/dat/Cleft_District_tmnaDesert.prp"
        );
        return r;
    }

    /*public static void CopyMusic(String myst5folder, String potsfolder)
    {
        m.status("Checking the folders you gave...");
        if(!detectinstallation.isFolderMyst5(myst5folder)) return;
        if(!detectinstallation.isFolderPots(potsfolder)) return;

        for(String filename: auto.fileLists.myst5Music)
        {
            String infile = myst5folder + "/sfx/" + filename;
            String outfile = potsfolder + "/MyMusic/" + filename;

            FileUtils.CopyFile(infile, outfile, true, true);
        }

        m.status("Done copying Myst5 music!");

    }*/
    /*public static void convertABunchOfMyst5Stuff(String myst5folder, String potsfolder)
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
        if(!auto.AllGames.getMystV().isFolderX(myst5folder)) return;
        if(!auto.AllGames.getPots().isFolderX(potsfolder)) return;
        
        m.status("Starting conversion...");
        Vector<String> files = uru.generics.convertArrayToVector(auto.fileLists.mystvFiles);
        Vector<String> oggfiles = uru.generics.convertArrayToVector(auto.fileLists.mystvOggsNotInPotsNorMoulofflineMinusSpeeches);
        files.addAll(oggfiles);

        try{
            convertMyst5ToPots(myst5folder, potsfolder, files, true);
        }catch(shared.cancelexception e){
            m.warn("Conversion cancelled.");
        }
        
        
        //shared.State.AllStates.pop();
        m.state.pop();
        m.status("Dont forget to run SoundDecompress.exe in your Pots folder, in order to get the sounds working!  (You can also click the SoundDecompress button on the form if you prefer.) (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        //m.status("Dont forget to run SoundDecompress.exe; the button is at UAM->SoundDecompress. (If SoundDecompress crashes, it means you have to log into Uru, quit, then try again.)");
        m.status("Conversion completed!");
    }*/
    public static void fixBinks(String finalname, prpfile prp, String infolder)
    {
        String agename = finalname.toLowerCase();
        String pagename = prp.header.pagename.toString().toLowerCase();
        
        for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plLayerBink))
        {
            plLayerBink binkobj = obj.castTo();
            if((binkobj.parent.parent.tc.flags&0x2)!=0) //if loop
            {
                //Set loop length based on bink video length.
                //These changes require you to delte the Age's sav file.
                String pathtobinkfile = infolder+"/"+binkobj.parent.filename.toString();
                IBytestream binkc = SerialBytestream.createFromFilename(pathtobinkfile);
                bink.binkfile binkfile = new bink.binkfile(binkc);
                float length = binkfile.getLengthInSeconds();
                length = length*59.0f/60.0f; //Cyan's timing seems to be off by this much.
                m.msg("Modifying bink: ",pathtobinkfile," length=",Float.toString(length));
                // animated panels should only play when the player enters a region. However,
                // the bink layer MUST be set as autostart, otherwise regions will have no effect (seems like a bug with Plasma).
                binkobj.parent.parent.tc.flags &= ~0x1; //turn off the "stopped" flag.
                //binkobj.parent.parent.tc.flags |= 0x20; //turn on the easingIn flag.
                //binkobj.parent.parent.tc.flags = 0x22; //can this be removed?
                //binkobj.parent.parent.tc.flags |= 0x88;
                //binkobj.parent.parent.tc.loopEnd = Flt.createFromJavaFloat(length-1.0f);
                binkobj.parent.parent.tc.loopEnd = Flt.createFromJavaFloat(length);
                //binkobj.parent.parent.tc.end = Flt.createFromJavaFloat(length);
            }
            /*String filename = bink.parent.filename.toString().toLowerCase();
            if(
                filename.equals("direbo.bik")||
                filename.equals("reststop1.bik")||
                filename.equals("reststop2.bik")||
                filename.equals("reststop3.bik")||
                filename.equals("reststop4.bik")
                )
            {
                bink.parent.parent.tc.loopEnd = Flt.createFromJavaFloat(30.0f);
            }*/
        }
    }

    public static void fixLinks(String newAgename, prpfile prp)
    {
        PrpRootObject[] objs = prputils.FindAllObjectsOfType(prp, Typeid.plPythonFileMod);
        for(PrpRootObject obj: objs)
        {
            plPythonFileMod pfm = obj.castTo();
            if(newAgename.toLowerCase().equals("descentmystv")||newAgename.toLowerCase().equals("direbo"))
            {
                if(pfm.pyfile.toString().toLowerCase().equals("xlinkingbookguipopup"))
                {
                    String oldlink = pfm.listings.get(2).xString.toString();
                    String age;
                    String spawnpoint;
                    if(oldlink.equals("DireboLaki"))
                    {
                        age = "Direbo";
                        spawnpoint = "LinkInPoint2";
                    }
                    else if(oldlink.equals("DireboSrln"))
                    {
                        age = "Direbo";
                        spawnpoint = "LinkInPoint1";
                    }
                    else if(oldlink.equals("DireboThgr"))
                    {
                        age = "Direbo";
                        spawnpoint = "LinkInPoint4";
                    }
                    else if(oldlink.equals("DireboTdlm"))
                    {
                        age = "Direbo";
                        spawnpoint = "LinkInPoint3";
                    }
                    else if(oldlink.equals("DescentRestAreaA"))
                    {
                        age = "DescentMystV";
                        spawnpoint = "LinkInFromThgrDirebo";
                    }
                    else if(oldlink.equals("DescentRestAreaB"))
                    {
                        age = "DescentMystV";
                        spawnpoint = "LinkInFromTdlmDirebo";
                    }
                    else if(oldlink.equals("DescentRestAreaC"))
                    {
                        age = "DescentMystV";
                        spawnpoint = "LinkInFromSrlnDirebo";
                    }
                    else if(oldlink.equals("DescentRestAreaD"))
                    {
                        age = "DescentMystV";
                        spawnpoint = "LinkInFromLakiDirebo";
                    }
                    else
                    {
                        m.err("Broken linking book in prpprocess.");
                        age="";
                        spawnpoint="";
                    }
                    pfm.pyfile = Urustring.createFromString("dusttest");
                    pfm.clearListings();
                    //pfm.listcount = 3;
                    //pfm.listings = new Pythonlisting[3];
                    pfm.addListing(Pythonlisting.createWithString(4, 1, Bstr.createFromString("linktoage")));
                    pfm.addListing(Pythonlisting.createWithString(4, 2, Bstr.createFromString(age)));
                    pfm.addListing(Pythonlisting.createWithString(4, 3, Bstr.createFromString(spawnpoint)));
                    /*pfm.listings[0] = new Pythonlisting();
                    pfm.listings[0].index = 1;
                    pfm.listings[0].type = 4; //string
                    pfm.listings[0].xString = Bstr.createFromString("linktoage");
                    pfm.listings[1] = new Pythonlisting();
                    pfm.listings[1].index = 2;
                    pfm.listings[1].type = 4; //string
                    pfm.listings[1].xString = Bstr.createFromString(age);
                    pfm.listings[2] = new Pythonlisting();
                    pfm.listings[2].index = 3;
                    pfm.listings[2].type = 4; //string
                    pfm.listings[2].xString = Bstr.createFromString(spawnpoint);*/

                    //Vector<Pythonlisting> pls = new Vector<Pythonlisting>();
                    //for(Pythonlisting pl: pfm.listings)
                }
            }
        }
    }
    
    public static void fixClickables(String finalname, prpfile prp)
    {
        //restore limited clickables
        //String[] clickables = {};
        String agename = finalname.toLowerCase();
        String pagename = prp.header.pagename.toString().toLowerCase();
        
        if(agename.equals("direbo") && pagename.equals("restage"))
        {
            //clickables = new String[]{
            restoreClickability(prp, "PedButton02ClickProxyLaki");
            restoreClickability(prp, "PedButton03ClickProxyLaki");
            restoreClickability(prp, "PedButton04ClickProxyLaki");
            restoreClickability(prp, "PedButton05ClickProxyLaki");
            restoreClickability(prp, "PedButton02ClickProxyTdlm");
            restoreClickability(prp, "PedButton03ClickProxyTdlm");
            //restoreClickability(prp, "PedButton04ClickProxyTdlm");
            restoreClickability(prp, "PedButton05ClickProxyTdlm");
            restoreClickability(prp, "PedButton02ClickProxyThgr");
            restoreClickability(prp, "PedButton03ClickProxyThgr");
            restoreClickability(prp, "PedButton04ClickProxyThgr");
            restoreClickability(prp, "PedButton05ClickProxyThgr");
            restoreClickability(prp, "PedButton02ClickProxySrln");
            restoreClickability(prp, "PedButton03ClickProxySrln");
            //restoreClickability(prp, "PedButton04ClickProxySrln");
            restoreClickability(prp, "PedButton05ClickProxySrln");
            //};
        }
        if(agename.equals("laki") && pagename.equals("exterior"))
        {
            //clickables = new String[]{
                //restoreClickability(prp, "PedButton01ClickProxy"); //don't link to self.
            restoreClickability(prp, "PedButton02ClickProxy");
            restoreClickability(prp, "PedButton03ClickProxy");
            restoreClickability(prp, "PedButton04ClickProxy");
            restoreClickability(prp, "PedButton05ClickProxy");
            //};
            makeClickableUsePythonfilemod(prp, "ClickPed1DireboLinkProxy", "fakelink", "Laki", "LinkInTake");
            makeClickableUsePythonfilemod(prp, "ClickPed3DireboLinkProxy", "fakelink", "Laki", "LinkInTake");
            //makeClickableUsePythonfilemod(prp, "Ped1ClickableProxy", "linktoage", "Todelmer", "LinkInPointDefault"); //this is the tablet button.
            //PedestalClickableProxy is the tablet symbol on the take
            makeClickableUsePythonfilemod(prp, "TakeOrDireboLinkProxy", "linktoage", "Direbo", "LinkInPoint2"); //link to direbo
        }
        if(agename.equals("laki") && pagename.equals("lakiarenavillaint"))
        {
            makeClickableUsePythonfilemod(prp, "ClickPed2DireboLinkProxy", "fakelink", "Laki", "LinkInTake");
        }
        if(agename.equals("siralehn") && pagename.equals("exterior"))
        {
            //clickables = new String[]{
            //restoreClickability(prp, "PedButton01ClickProxy", //don't link to self.
            restoreClickability(prp, "PedButton02ClickProxy");
            restoreClickability(prp, "PedButton03ClickProxy");
            //restoreClickability(prp, "PedButton04ClickProxy"); //links to the area with Esher on the beach.
            restoreClickability(prp, "PedButton05ClickProxy");
            //};
            makeClickableUsePythonfilemod(prp, "DireboLinkProxy", "linktoage", "Direbo", "LinkInPoint1");
            makeClickableUsePythonfilemod(prp, "TakeLinkProxy", "fakelink", "Siralehn", "LinkInTake");
            prp.markObjectDeleted(Typeid.plSceneObject,"LandTopCollision"); //so we can jump off the top
            prp.markObjectDeleted(Typeid.plSceneObject,"XrgnKeepDoor"); //so we can enter the keep
        }
        /*if(agename.equals("siralehn") && pagename.equals("srlnkeepinter"))
        {
            clickables = new String[]{
                "LakiTakeLinkProxy",
                "SrlnTakeLinkProxy",
                "TdlmTakeLinkProxy",
                "ThgrTakeLinkProxy",
                //"SrlnKeepClickableProxy",
            };
            
            PrpRootObject resp = prp.findObject("RespLakiActivateKeep", Typeid.plResponderModifier);
            //createPythonLoader(prp, 8, resp.header.desc.toRef());
            //PlLogicModifier logmod = prp.findObject("ClickLakiKeepTakeLink", Typeid.plLogicModifier).castTo();
            //logmod.conditionalcount = 2;
            //logmod.conditionals = new Uruobjectref[]{logmod.conditionals[2],logmod.conditionals[3]};
            //int dummy=0;
        }*/

        //for(String clickable: clickables)
        //{
        //    restoreClickability(prp, clickable);
        //}

        if(agename.equals("siralehn") && pagename.equals("rock"))
        {
            makeClickableUsePythonfilemod(prp, "TakeLinkProxy", "fakelink", "Siralehn", "LinkInTake"); //same name, different page from above.
        }
        if(agename.equals("tahgira") && pagename.equals("icecave"))
        {
            //disable physics on IceCaveCrackMesh
            prp.markObjectDeleted(Typeid.plSceneObject,"IceCaveCrackMesh");
            //restoreClickability(prp, "PedButton01ClickProxy");
            restoreClickability(prp, "PedButton02ClickProxy");
            restoreClickability(prp, "PedButton03ClickProxy");
            restoreClickability(prp, "PedButton04ClickProxy");
            restoreClickability(prp, "PedButton05ClickProxy");
            makeClickableUsePythonfilemod(prp, "DireboLinkProxy", "linktoage", "Direbo", "LinkInPoint4");
        }
        if(agename.equals("tahgira") && pagename.equals("exterior"))
        {
            makeClickableUsePythonfilemod(prp, "TakeLink1Proxy", "fakelink", "Tahgira", "LinkInTake");
            makeClickableUsePythonfilemod(prp, "TakeLink2Proxy", "fakelink", "Tahgira", "LinkInTake");
            makeClickableUsePythonfilemod(prp, "TakeLink3Proxy", "fakelink", "Tahgira", "LinkInTake");
        }
        if(agename.equals("todelmer") && pagename.equals("exterior"))
        {
            //restoreClickability(prp, "PedButton01ClickProxy");
            restoreClickability(prp, "PedButton02ClickProxy");
            restoreClickability(prp, "PedButton03ClickProxy");
            //restoreClickability(prp, "PedButton04ClickProxy");
            restoreClickability(prp, "PedButton05ClickProxy");
            makeClickableUsePythonfilemod(prp, "DireboLinkProxy", "linktoage", "Direbo", "LinkInPoint3");
            makeClickableUsePythonfilemod(prp, "TakeLinkProxy", "fakelink", "Todelmer", "LinkInTake");
            makeClickableUsePythonfilemod(prp, "TakeLinkProxy01", "fakelink", "Todelmer", "LinkInTake");
        }
        if(agename.equals("todelmer") && pagename.equals("interiorpillar1"))
        {
            prp.markObjectDeleted(Typeid.plSceneObject,"XrgnStairs01"); //the blocker for the stairs in the building on the main pillar.
        }
        if(agename.equals("todelmer") && pagename.equals("interiorpillar3"))
        {
            prp.markObjectDeleted(Typeid.plSceneObject,"XrgnStairs01"); //the blocker for the stairs in the building on the main pillar.
        }
        if(agename.equals("mystmystv") && pagename.equals("island"))
        {
            prp.markObjectDeleted(Typeid.plSceneObject, "PlanetariumDoorBlocker");
            
            plRandomSoundMod rsm = prp.findObject("cSfxRndThunder", Typeid.plRandomSoundMod).castTo();
            rsm.parent.state = 0; //turn it on
            rsm.parent.mode = 3; //must disable the kOneCmd
            //rsm.parent.minDelay = Flt.createFromJavaFloat(4); //just picked a number.
            //rsm.parent.maxDelay = Flt.createFromJavaFloat(30); //just picked a number.
            rsm.parent.minDelay = Flt.createFromJavaFloat(4); //just picked a number.
            rsm.parent.maxDelay = Flt.createFromJavaFloat(20); //just picked a number.
            
            //PlRandomSoundMod rs2 = prp.findObject("cSfxRandomCreaks", Typeid.plRandomSoundMod).castTo();
            //rs2.parent.state = 0;
        }

    }
    public static void createPythonLoader(prpfile prp, int type, Uruobjectref ref)
    {
        if(type==8) //responder
        {
            plPythonFileMod mod = plPythonFileMod.createDefault();
            mod.pyfile = Urustring.createFromString("dusttest");
            mod.addListing(Pythonlisting.createWithString(4, 1, Bstr.createFromString("storeattrib")));
            mod.addListing(Pythonlisting.createWithRef(type, 10, ref));
            Uruobjectref modref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, ref.xdesc.objectname.toString()+"_pfm", prp.header.pageid);
            PrpRootObject modroot = PrpRootObject.createFromDescAndObject(modref.xdesc, mod);
            
            PrpRootObject sn = null;
            for(PrpRootObject obj: prp.objects2)
                if(obj.header.objecttype==Typeid.plSceneNode)
                    sn = obj;
            
            
            plSceneObject so = plSceneObject.createDefaultWithScenenode(sn.header.desc.toRef());
            so.addToObjectrefs2(modref);
            Uruobjectref soref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, ref.xdesc.objectname.toString()+"_so", prp.header.pageid);
            PrpRootObject soroot = PrpRootObject.createFromDescAndObject(soref.xdesc, so);
            
            //prp.extraobjects.add(soroot);
            //prp.extraobjects.add(modroot);
            prp.addObject(soroot);
            prp.addObject(modroot);
        }
    }
    public static void modifyPythonfilemod(prpfile prp, String sceneobjectname, String... pythonparams)
    {
        m.warn("Untested modifyPythonfilemod.");
        plSceneObject so = prp.findObject(sceneobjectname, Typeid.plSceneObject).castTo();
        plLogicModifier logmod=null;
        for(Uruobjectref curref: so.modifiers)
        {
            if(curref.hasref() && curref.xdesc.objecttype==Typeid.plLogicModifier)
            {
                logmod = prp.findObjectWithRef(curref).castTo();
                break;
            }
        }
        if(logmod==null) throw new shared.uncaughtexception("modifyPythonfilemod couldn't find a ref.");
        if(logmod.parent.message.type!=Typeid.plNotifyMsg) throw new shared.uncaughtexception("modifyPythonfilemod found the wrong type.");
        PrpMessage.PlNotifyMsg msg = (PrpMessage.PlNotifyMsg)logmod.parent.message.prpobject.object;
        if(msg.parent.receivers.size()!=1) throw new shared.uncaughtexception("Should only be one ref in modifyPythonfilemod.");
        plPythonFileMod pfm = prp.findObjectWithRef(msg.parent.receivers.get(0)).castTo(plPythonFileMod.class);
        pfm.pyfile = Urustring.createFromString("dusttest");
        pfm.clearListings();
        for(int i=0;i<pythonparams.length;i++)
        {
            pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(4, i+1, Bstr.createFromString(pythonparams[i])));
        }
        
    }
    public static void makeClickableUsePythonfilemod(prpfile prp, String sceneobjectname, String... pythonparams)
    {
        plSceneObject so = prp.findObject(sceneobjectname, Typeid.plSceneObject).castTo();
        plLogicModifier logmod=null;
        for(Uruobjectref curref: so.modifiers)
        {
            if(curref.hasref() && curref.xdesc.objecttype==Typeid.plLogicModifier)
            {
                logmod = prp.findObjectWithRef(curref).castTo();
            }
        }

        //PlLogicModifier logmod = prp.findObject(c, Typeid.plLogicModifier).castTo();
        plPythonFileMod pfm = plPythonFileMod.createDefault();
        pfm.pyfile = Urustring.createFromString("dusttest");
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(4, 1, Bstr.createFromString(pythonparams[0])));//"linktoage"
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(4, 2, Bstr.createFromString(pythonparams[1])));//"Direbo")));
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(4, 3, Bstr.createFromString(pythonparams[2])));//"LinkInPointDefault")));
        Uruobjectref pfmref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, sceneobjectname+"_pfm" , prp.header.pageid);
        PrpRootObject pfmroot = PrpRootObject.createFromDescAndObject(pfmref.xdesc, pfm);
        //prp.extraobjects.add(pfmroot); //don't forget to add the PFM!
        prp.addObject(pfmroot); //don't forget to add the PFM!
        so.addToObjectrefs2(pfmref); //at least one sceneobject must reference the PFM, or it won't load.
        //logmod.parent.disabled = 0;
        makeLogicModifierUsePythonFileMod(logmod,pfmref);
    }
    public static void makeLogicModifierUsePythonFileMod(plLogicModifier logicmod, Uruobjectref pythonfilemod)
    {
        if(logicmod.parent.message.type!=Typeid.plNotifyMsg)
        {
            m.err("makeLogicModifierUsePythonFileMod can only handle plNotifyMsg currently.");
            return;
        }
        PrpMessage.PlNotifyMsg notify = PrpMessage.PlNotifyMsg.createWithRef(pythonfilemod);
        logicmod.parent.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plNotifyMsg, notify);
    }
    public static void restoreClickability(prpfile prp, String clickableSceneObject)
    {
        plSceneObject so = prp.findObject(clickableSceneObject, Typeid.plSceneObject).castTo();
        for(Uruobjectref ref: so.modifiers)
        {
            if(ref.hasref() && ref.xdesc.objecttype==Typeid.plLogicModifier)
            {
                plLogicModifier lm2 = prp.findObjectWithRef(ref).castTo();
                lm2.parent.disabled = 0; //set disabled to false.
                /*for(Uruobjectref condref: lm2.conditionals)
                {
                    if(condref.hasref()&&condref.xdesc.objecttype==Typeid.plActivatorConditionalObject)
                    {
                        PlActivatorConditionalObject aco2 = prp.findObjectWithRef(condref).castTo();
                        aco2.parent.satisfied = 1;
                        int dummy=0;
                    }                                
                }*/
                //int dummy=0;
            }
        }
    }
    
    
    public static void PostMod_AutomateMyst5(prpfile prp, String infolder, String newAgename)
    {
        auto.postmod.PostMod_MystV.PostMod_RemoveLadders(prp);
        
        // not the best solution, but works 100% fine
        auto.postmod.PostMod_MystV.PostMod_InvertEnvironmaps2(prp);
        
        // in Myst V, using start/stop responders was enough, but not here...
        mystv.fixBinks(newAgename, prp, infolder);
        
        
        // BEGIN Sirius' MV fixes
        
        // fix cast flags for respmod, logicmod, animevmod
        auto.postmod.PostMod_MystV.PostMod_FixCastFlags(prp);
        // fix for incidental sound
        auto.postmod.PostMod_MystV.PostMod_FixIncidentalSounds(prp);
        // fix for PFM variable names
        auto.postmod.PostMod_MystV.PostMod_FixPythonFileMods(prp);
        // fix for dynamiccammaps and dynamicenvmaps
        auto.postmod.PostMod_MystV.PostMod_FixDynamicMaps(prp);
        // fix for echo effect
        auto.postmod.PostMod_MystV.PostMod_FixEchoEffects(prp);
        // fix for camera references (has changed since Uru, but is basically the same)
        auto.postmod.PostMod_MystV.PostMod_FixCameraReferences(prp);
        // fix for clickables crashing game (Tahgira)
        auto.postmod.PostMod_MystV.PostMod_FixInvalidLogicModConditions(prp);
        // fix for pedestals
        auto.postmod.PostMod_MystV.PostMod_FixPedestals(prp);
        // fix for direbo responders
        auto.postmod.PostMod_MystV.PostMod_FixLinkResponderNames(prp);
        // makes objects such as Siralehn rock send notifies to their Python scripts
        auto.postmod.PostMod_MystV.PostMod_AddAnimEventForDraggables(prp);
        // fixes visregion, rootnode n'stuff
        auto.postmod.PostMod_MystV.PostMod_TweakEnvmapSettings(prp);
        
        // fix for door button not compatible with PotS' avatar
        if (prp.header.pagename.toString().startsWith("InteriorPillar"))
            auto.postmod.PostMod_MystV.PostMod_FixTdlmDoors(prp);
        // fix for tram lever moving on its own
        if (prp.header.agename.toString().equals("Todelmer") && prp.header.pagename.toString().equals("Exterior"))
            auto.postmod.PostMod_MystV.PostMod_FixTdlmTramLevers(prp);
        
        // adjust a few animations used by draggables
        auto.postmod.PostMod_MystV.PostMod_FixTdlmPowerDraggables(prp);
        // fade bubble interior, just because it looks cool
        auto.postmod.PostMod_MystV.PostMod_FadeBubble(prp);
        // replace DynamicMusicSound for some voices and brings back Laki arena music
        auto.postmod.PostMod_MystV.PostMod_ReplaceDirectMusicSound(prp);
        
        
        // remove annoying physical
        if (newAgename.equals("MystMystV") && prp.header.pagename.toString().equals("Island"))
            prp.markObjectDeleted(Typeid.plSceneObject, "PlanetariumDoorBlocker");
        if (newAgename.equals("Siralehn") && prp.header.pagename.toString().equals("Exterior"))
            prp.markObjectDeleted(Typeid.plSceneObject,"LandTopCollision"); //so we can jump off the top
        
        // fix laki buttons facing the wrong direction
        if (newAgename.equals("Laki") && prp.header.pagename.toString().equals("LakiMaze"))
            auto.postmod.PostMod_MystV.PostMod_FixLakiMazeButtons(prp);
        
        // fix descent elevators and floor
        if (newAgename.equals("DescentMystV") && prp.header.pagename.toString().equals("dsntUpperShaft"))
            auto.postmod.PostMod_MystV.PostMod_FixDescentElev(prp);
        if (newAgename.equals("DescentMystV") && prp.header.pagename.toString().equals("dsntGreatShaftLowerRm"))
            auto.postmod.PostMod_MystV.PostMod_FixDescentFloor(prp);
        // fix Lakis so that the python can find their animations
        auto.postmod.PostMod_MystV.PostMod_FixLakiCreatures(prp);
        // fix cameras (especially Todelmer ones)
        auto.postmod.PostMod_MystV.PostMod_FixCameraTargetPoints(prp);
        // flyovers
        auto.postmod.PostMod_MystV.PostMod_MakeFlyOvers(prp);
        // Laki pirhana bird
        auto.postmod.PostMod_MystV.PostMod_FixPirahnaBird(prp);
        
        // adjust those thrice-damned draggables
        auto.postmod.PostMod_MystV.PostMod_AdjustDraggableAnimations(prp);
        
        // put laki column sp at correct height (was too low)
        if (newAgename.equals("Laki") && prp.header.pagename.toString().equals("LakiArenaVillaInt"))
            auto.postmod.PostMod_MystV.PostMod_FixLakiElev(prp);
        
        
        // MAKE GOG COMPATIBLE WITH WHITEBOX VERSION
        PostMod_MystV_WhiteBox.ConvertToWhitebox(prp, new File(infolder + "/MystV.exe").exists());
        
        
        // END Sirius' MV fixes
        
        
        if (false)
        {
            // this is a collections of hacks to make walking around ages a bit easier, but it breaks things when fully fixing logic
            // Shouldn't be required once we convert things properly
            mystv.fixClickables(newAgename, prp);
            
            //fix direbo links. Doesn't look too good, we'll find a better way to do this (maybe based off my handy-dandy xSimpleLinkingBook).
            mystv.fixLinks(newAgename, prp);
            
            // hack: convert a detector region to a collider so we can walk on something.
            if(newAgename.toLowerCase().equals("descentmystv") && prp.header.pagename.toString().toLowerCase().equals("dsntgreatshaftlowerrm"))
            {
                plHKPhysical erf = prp.findObject("ElevRisingFloor", Typeid.plHKPhysical).castTo();
                erf.ode.convertee.coltype = 0x200;
                erf.ode.convertee.LOSDB = 0x44;
                erf.ode.convertee.group = new HsBitVector(4);
                erf.ode.convertee.flagsdetect = 0x0;
                erf.ode.convertee.mass = Flt.one();
            }
        }
    }
}

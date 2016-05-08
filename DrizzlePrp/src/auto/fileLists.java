/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.util.Vector;

public class fileLists
{
    //Pots python files
    public static String[] partialListOfSharedPythonFiles = {
        "xAgeSDLBoolRespond",
        "xAgeSDLBoolShowHide",
        "xAgeSDLBoolToggle",
    };
    //Pots sound files.
    public static String[] partialListofSharedSoundFiles = {

    };

    /*//These are the oggs from MystV, that aren't already present in Pots or MoulOffline(they might be in the rest of Moul).
    //As it turns out, this is the same as the ones that simply aren't present in Pots.
    public static String[] mystvOggsNotInPotsNorMouloffline = {
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
    };
    
    //This one is like mystvOggsNotInPotsNorMouloffline, but it removes all the speeches which end in _eng, _fre, _ger, and _spa.
    //Takes only half as much space.
    public static String[] mystvOggsNotInPotsNorMoulofflineMinusSpeeches = {
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
    };*/
    
    //public static String[] mystvFiles = {
    //    "Descent.(others)","Descent.age","Descent.fni","Descent.sum","Descent_dsntBahro_Idle02.prp","Descent_dsntBahro_Idle03.prp","Descent_dsntBahro_Idle04.prp","Descent_dsntBahro_Idle05.prp","Descent_dsntBahro_Idle06.prp","Descent_dsntBahro_Idle07.prp","Descent_dsntBahro_Idle08.prp","Descent_dsntBahro_Idle09.prp","Descent_dsntBahro_Shot02.prp","Descent_dsntBahro_Shot03.prp","Descent_dsntBahro_Shot04.prp","Descent_dsntBahro_Shot05.prp","Descent_dsntBahro_Shot06.prp","Descent_dsntBahro_Shot07.prp","Descent_dsntBahro_Shot08.prp","Descent_dsntBahro_Shot09.prp","Descent_dsntBahro_Tunnel01.prp","Descent_dsntBahro_Tunnel01Idle.prp","Descent_dsntBats.prp","Descent_dsntEsherIdleTopOfShaft.prp","Descent_dsntEsher_BottomOfShaft.prp","Descent_dsntEsher_FirstHub.prp","Descent_dsntEsher_Intro.prp","Descent_dsntEsher_TopOfShaft.prp","Descent_dsntGreatShaftBalcony.prp","Descent_dsntGreatShaftLowerRm.prp","Descent_dsntLowerBats.prp","Descent_dsntMapGUI.prp","Descent_dsntPostBats.prp","Descent_dsntPostShaftNodeAndTunnels.prp","Descent_dsntShaftGeneratorRoom.prp","Descent_dsntShaftTunnelSystem.prp","Descent_dsntTianaCave.prp","Descent_dsntTianaCaveNode2.prp","Descent_dsntTianaCaveTunnel1.prp","Descent_dsntTianaCaveTunnel3.prp","Descent_dsntUpperBats.prp","Descent_dsntUpperShaft.prp","Descent_dsntVolcano.prp","Descent_Textures.prp","Descent_dusttest.prp",
    //    "Direbo.(others)","Direbo.age","Direbo.fni","Direbo.sum","Direbo_DragonFly.prp","Direbo_drboEsherIdleDirebo.prp","Direbo_drboEsher_DireboLaki.prp","Direbo_drboEsher_DireboSrln.prp","Direbo_drboEsher_DireboTdlm.prp","Direbo_drboEsher_DireboThgr.prp","Direbo_drboUrwinShape.prp","Direbo_RestAge.prp","Direbo_Textures.prp","Direbo_UrwinIdle.prp","Direbo_UrwinWalk.prp",
    //    "Kveer.(others)","Kveer.age","Kveer.fni","Kveer.sum",/*"Kveer_bkMystBookLocked.prp","Kveer_GreatRm.prp","Kveer_KveerBats.prp","Kveer_kverAtrus.prp","Kveer_kverAtrus_1.prp","Kveer_kverAtrus_Idle.prp","Kveer_kverBahroWingsGUI.prp","Kveer_kverBahro_1.prp","Kveer_kverBahro_2.prp","Kveer_kverBahro_Ballroom01.prp","Kveer_kverBahro_Ballroom02.prp","Kveer_kverBahro_Ballroom03.prp","Kveer_kverBahro_Exit01.prp","Kveer_kverBahro_Exit02.prp","Kveer_kverBahro_Idle05.prp","Kveer_kverBahro_Idle06.prp","Kveer_kverBahro_Idle07.prp","Kveer_kverBahro_Idle08.prp","Kveer_kverBahro_Idle09.prp","Kveer_kverBahro_Shot03.prp","Kveer_kverBahro_Shot04.prp","Kveer_kverBahro_Shot05.prp","Kveer_kverBahro_Shot06.prp","Kveer_kverBahro_Shot07.prp","Kveer_kverBahro_Shot08.prp","Kveer_kverBahro_Shot09.prp","Kveer_kverConc3Music.prp","Kveer_kverEsher_1.prp",*/"Kveer_kverReleeshan.prp",/*"Kveer_kverYeesha_1.prp","Kveer_kverYeesha_Conc01.prp","Kveer_kverYeesha_Conc02.prp","Kveer_kverYeesha_Conc03.prp","Kveer_kverYeesha_ConcIntro.prp","Kveer_kverYeesha_ConcIntro2.prp","Kveer_kverYeesha_IdleForIntro.prp","Kveer_kverYeesha_Intro.prp","Kveer_Prison.prp",*/"Kveer_Textures.prp","Kveer_dusttest.prp",
    //    "Laki.(others)","Laki.age","Laki.fni","Laki.sum","Laki_Exterior.prp","Laki_LakiArenaVillaInt.prp","Laki_LakiCreatures.prp","Laki_lakiEsher-Arena.prp","Laki_lakiEsher-FighterBeach.prp","Laki_lakiEsher-Keep.prp","Laki_lakiEsher-Villa.prp","Laki_lakiEsherIdleKeep.prp","Laki_lakiEsherIdleVilla.prp","Laki_LakiMaze.prp","Laki_lakiMazeClue.prp","Laki_LakiTrees01.prp","Laki_PirBirdActor.prp","Laki_PirBirdChomp.prp","Laki_PirBirdIdle.prp","Laki_PirBirdSwallow.prp","Laki_PirBirdVocalize.prp","Laki_PirBirdWalk.prp","Laki_Textures.prp","Laki_dusttest.prp",
    //    "Myst.(others)","Myst.age","Myst.fni","Myst.sum","Myst_Island.prp","Myst_mystEsher-Conc01.prp","Myst_mystEsher-Conc02.prp","Myst_Textures.prp",
    //    //"MystMystV_District_Additions.prp","Direbo_District_Additions.prp", //original authored material.
    //    "Siralehn.(others)","Siralehn.age","Siralehn.fni","Siralehn.sum","Siralehn_Birds.prp","Siralehn_Drawing01.prp","Siralehn_Drawing02.prp","Siralehn_Drawing03.prp","Siralehn_Drawing04.prp","Siralehn_Drawing05.prp","Siralehn_Drawing06.prp","Siralehn_Drawing07.prp","Siralehn_Drawing08.prp","Siralehn_Exterior.prp","Siralehn_rock.prp","Siralehn_srlnEsherIdleBeach.prp","Siralehn_srlnEsherIdleLab.prp","Siralehn_srlnEsher_NolobenBeach.prp","Siralehn_srlnEsher_NolobenKeep.prp","Siralehn_srlnEsher_NolobenLab.prp","Siralehn_srlnKeepInter.prp","Siralehn_Textures.prp","Siralehn_tunnels.prp","Siralehn_dusttest.prp",
    //    "Tahgira.(others)","Tahgira.age","Tahgira.fni","Tahgira.sum","Tahgira_Exterior.prp","Tahgira_IceCave.prp","Tahgira_Textures.prp","Tahgira_thgrEsherIdleIntro.prp","Tahgira_thgrEsherIdleTake.prp","Tahgira_thgrEsher_TahgiraGrave.prp","Tahgira_thgrEsher_TahgiraIntro.prp","Tahgira_thgrEsher_TahgiraTake.prp","Tahgira_thgrEsher_TahgiraThermals.prp","Tahgira_thgrEsher_TahgiraVillage.prp","Tahgira_dusttest.prp",
    //    "Todelmer.(others)","Todelmer.age","Todelmer.fni","Todelmer.sum","Todelmer_Exterior.prp","Todelmer_InteriorPillar1.prp","Todelmer_InteriorPillar3.prp","Todelmer_MiniScope.prp","Todelmer_Pod.prp","Todelmer_Sky.prp","Todelmer_tdlmEsherIdleP3.prp","Todelmer_tdlmEsherIdleRing.prp","Todelmer_tdlmEsher_TodelmerP1.prp","Todelmer_tdlmEsher_TodelmerP3.prp","Todelmer_tdlmEsher_TodelmerRing.prp","Todelmer_Textures.prp","Todelmer_dusttest.prp",
    //    "direbo.bik","restStop1.bik","restStop2.bik","restStop3.bik","restStop4.bik","direboWithAlpha.bik","mystWithAlpha.bik",
    //};
    
    public static Vector<String> moulSimplicityList()
    {
        Vector<String> result = uru.generics.convertArrayToVector(moulSimplicityList2);
        //result.addAll(uru.generics.convertArrayToVector(moulSfxList));
        return result;
    }
    public static Vector<String> moulSupportedList()
    {
        Vector<String> result = uru.generics.convertArrayToVector(moulSimplicityList2);
        result.addAll(uru.generics.convertArrayToVector(moulSupportedList2));
        return result;
    }
    
    public static Vector<String> mystvSupportedList() {
        Vector<String> result = uru.generics.convertArrayToVector(mystvSupportedList);
        return result;
    }
    
    /*public static Vector<String> crowSfxList()
    {
        Vector<String> result = uru.generics.convertArrayToVector(crowSfxList2);
        return result;
    }
    private static String[] crowSfxList2 = {
        "mntnAir_loop.ogg","mntnAmbientMx.ogg","mntnBird01a.ogg","mntnBird01b.ogg","mntnBird02a.ogg","mntnBird02b.ogg","mntnBird02c.ogg","mntnBird03a.ogg","mntnBird04a.ogg","mntnBird04b.ogg","mntnBird05a.ogg","mntnBird05b.ogg","mntnBird06a.ogg","mntnBird06b.ogg","mntnBird07a.ogg","mntnBird07b.ogg","mntnFountain_loop.ogg","mntnWaterfall_loop.ogg","mntnWind_Loop01.ogg",
        "mrshAmb01.ogg","mrshAmb02.ogg","mrshAmb03.ogg","mrshAmb04.ogg","mrshAmbientMx.ogg","mrshBirdAmb.ogg","mrshRandomCricket01.ogg","mrshRandomCricket02.ogg","mrshRandomCricket03.ogg","mrshRandomCritter01.ogg","mrshRandomCritter02.ogg","mrshRandomCritter03.ogg","mrshRandomCritter04a.ogg","mrshRandomCritter04b.ogg","mrshRandomCritter04c.ogg","mrshRandomCritter04d.ogg","mrshRandomCritter04e.ogg","mrshRandomCritter04f.ogg","mrshRandomCritter04g.ogg","mrshRandomCritter05.ogg","mrshRandomCritter06a.ogg","mrshRandomCritter06b.ogg","mrshRandomCritter06c.ogg","mrshRandomCritter06d.ogg","mrshRandomCritter07a.ogg","mrshRandomCritter07b.ogg","mrshRandomCritter07c.ogg","mrshRandomCritter07d.ogg","mrshRandomCritter08.ogg","mrshRandomCritter09a.ogg","mrshRandomCritter09b.ogg","mrshRandomCritter10.ogg","mrshRandomCritter11a.ogg","mrshRandomCritter11b.ogg","mrshRandomCritter12a.ogg","mrshRandomCritter12b.ogg","mrshRandomCritter13.ogg","mrshRandomCritter14.ogg","mrshRandomCritter15.ogg","mrshRandomCritter16.ogg","mrshRandomCritter17.ogg","mrshRandomCritter18.ogg","mrshRandomFrogs01.ogg","mrshRandomFrogs02.ogg","mrshRandomFrogs03.ogg","mrshRandomFrogs04.ogg","mrshRandomFrogs05.ogg","mrshRandomFrogs06.ogg","mrshRandomFrogs07.ogg","mrshRandomFrogs08.ogg",
    };*/
    /*private static String[] moulSfxList = {
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
    };*/
        private static String[] moulSimplicityList2 = {
            //ignore the .age files here; they're moved into the new system.
            "city_District_islmLakeLightMeter.prp",
            "Dereno.age","Dereno.fni","Dereno.sum","Dereno_District_DrnoExterior.prp","Dereno_District_DrnoPod.prp","Dereno_District_Textures.prp","Dereno_District_BuiltIn.prp",
            "EderDelin.age","EderDelin.fni","EderDelin.sum","EderDelin_District_garden.prp","EderDelin_District_BuiltIn.prp","EderDelin_District_Textures.prp",
            "EderTsogal.age","EderTsogal.fni","EderTsogal.sum","EderTsogal_District_tsoGarden.prp","EderTsogal_District_Textures.prp","EderTsogal_District_BuiltIn.prp",
            "GuildPub-Cartographers.age","GuildPub-Cartographers.fni","GuildPub-Cartographers.sum","GuildPub-Cartographers_District_Pub.prp","GuildPub-Cartographers_District_Textures.prp","GuildPub-Cartographers_District_BuiltIn.prp",
            "GuildPub-Greeters.age","GuildPub-Greeters.fni","GuildPub-Greeters.sum","GuildPub-Greeters_District_Pub.prp","GuildPub-Greeters_District_Textures.prp","GuildPub-Greeters_District_BuiltIn.prp",
            "GuildPub-Maintainers.age","GuildPub-Maintainers.fni","GuildPub-Maintainers.sum","GuildPub-Maintainers_District_Pub.prp","GuildPub-Maintainers_District_Textures.prp","GuildPub-Maintainers_District_BuiltIn.prp",
            "GuildPub-Messengers.age","GuildPub-Messengers.fni","GuildPub-Messengers.sum","GuildPub-Messengers_District_Pub.prp","GuildPub-Messengers_District_Textures.prp","GuildPub-Messengers_District_BuiltIn.prp",
            "GuildPub-Writers.age","GuildPub-Writers.fni","GuildPub-Writers.sum","GuildPub-Writers_District_Pub.prp","GuildPub-Writers_District_Textures.prp","GuildPub-Writers_District_BuiltIn.prp",
            "Jalak.age","Jalak.fni","Jalak.sum","Jalak_District_jlakArena.prp","Jalak_District_Textures.prp","Jalak_District_BuiltIn.prp",
            "LiveBahroCaves.age","LiveBahroCaves.fni","LiveBahroCaves.sum","LiveBahroCaves_District_MINKcave.prp","LiveBahroCaves_District_POTScave.prp","LiveBahroCaves_District_PODcave.prp","LiveBahroCaves_District_BlueSpiralCave.prp","LiveBahroCaves_District_TheSpecialPage.prp","LiveBahroCaves_District_Textures.prp","LiveBahroCaves_District_BuiltIn.prp",
            "Minkata.age","Minkata.csv","Minkata.fni","Minkata.sum","Minkata_District_minkExteriorDay.prp","Minkata_District_minkNightLinkSounds.prp","Minkata_District_minkExteriorNight.prp","Minkata_District_minkExcludeRegions.prp","Minkata_District_minkDistCraterPhysicals.prp","Minkata_District_minkDayLinkSounds.prp","Minkata_District_minkCameras.prp","Minkata_District_Textures.prp","Minkata_District_BuiltIn.prp",
            "Negilahn.age","Negilahn.fni","Negilahn.sum","Negilahn_District_Jungle.prp","Negilahn_District_MuseumPod.prp","Negilahn_District_Textures.prp","Negilahn_District_BuiltIn.prp",
            "Neighborhood02.age", "Neighborhood02.fni", "Neighborhood02.sum", "Neighborhood02_District_BuiltIn.prp", "Neighborhood02_District_GuildInfo-Cartographers.prp", "Neighborhood02_District_GuildInfo-Greeters.prp", "Neighborhood02_District_GuildInfo-Maintainers.prp", "Neighborhood02_District_GuildInfo-Messengers.prp", "Neighborhood02_District_GuildInfo-Writers.prp", "Neighborhood02_District_krelClassRm.prp", "Neighborhood02_District_krelCommonRm.prp", "Neighborhood02_District_krelKirel.prp", "Neighborhood02_District_krelPrivateRm.prp", "Neighborhood02_District_Textures.prp",
            "Payiferen.age","Payiferen.fni","Payiferen.sum","Payiferen_District_Pod.prp","Payiferen_District_Textures.prp","Payiferen_District_BuiltIn.prp",
            "Tetsonot.age","Tetsonot.fni","Tetsonot.sum","Tetsonot_District_tetsoPod.prp","Tetsonot_District_Textures.prp","Tetsonot_District_BuiltIn.prp",
            "Kveer.age","Kveer.fni","Kveer.sum","Kveer_District_BuiltIn.prp","Kveer_District_KveerHalls.prp","Kveer_District_Textures.prp",
            "GreatTreePub.age","GreatTreePub.fni","GreatTreePub.sum","GreatTreePub_District_BuiltIn.prp","GreatTreePub_District_GreatTree.prp","GreatTreePub_District_Pub.prp","GreatTreePub_District_Textures.prp",
            "GlobalAnimations_District_FemaleAmazed.prp","GlobalAnimations_District_FemaleAskQuestion.prp","GlobalAnimations_District_FemaleBeckonBig.prp","GlobalAnimations_District_FemaleBeckonSmall.prp","GlobalAnimations_District_FemaleBow.prp","GlobalAnimations_District_FemaleCallMe.prp","GlobalAnimations_District_FemaleCower.prp","GlobalAnimations_District_FemaleCrazy.prp","GlobalAnimations_District_FemaleCringe.prp","GlobalAnimations_District_FemaleCrossArms.prp","GlobalAnimations_District_FemaleDoh.prp","GlobalAnimations_District_FemaleFlinch.prp","GlobalAnimations_District_FemaleGroan.prp","GlobalAnimations_District_FemaleKneel.prp","GlobalAnimations_District_FemaleLeanLeft.prp","GlobalAnimations_District_FemaleLeanRight.prp","GlobalAnimations_District_FemaleLookAround.prp","GlobalAnimations_District_FemaleOkay.prp", /*eyes go funny*/"GlobalAnimations_District_FemaleOverHere.prp","GlobalAnimations_District_FemalePeer.prp","GlobalAnimations_District_FemaleSalute.prp","GlobalAnimations_District_FemaleScratchHead.prp","GlobalAnimations_District_FemaleShakeFist.prp","GlobalAnimations_District_FemaleShoo.prp","GlobalAnimations_District_FemaleSlouchSad.prp","GlobalAnimations_District_FemaleStop.prp","GlobalAnimations_District_FemaleTalkHand.prp","GlobalAnimations_District_FemaleTapFoot.prp","GlobalAnimations_District_FemaleTaunt.prp","GlobalAnimations_District_FemaleThumbsDown.prp","GlobalAnimations_District_FemaleThumbsDown2.prp","GlobalAnimations_District_FemaleThumbsUp.prp","GlobalAnimations_District_FemaleThumbsUp2.prp","GlobalAnimations_District_FemaleWaveLow.prp","GlobalAnimations_District_FemaleWinded.prp",
            "GlobalAnimations_District_MaleAmazed.prp","GlobalAnimations_District_MaleAskQuestion.prp", /*arm doesn't go up correctly.*/"GlobalAnimations_District_MaleBeckonBig.prp","GlobalAnimations_District_MaleBeckonSmall.prp","GlobalAnimations_District_MaleBow.prp","GlobalAnimations_District_MaleCallMe.prp","GlobalAnimations_District_MaleCower.prp", /*eyes go funny*/"GlobalAnimations_District_MaleCrazy.prp", /*face goes funny*/"GlobalAnimations_District_MaleCringe.prp","GlobalAnimations_District_MaleCrossArms.prp", /*arms disappear*/"GlobalAnimations_District_MaleDoh.prp", /*face goes funny*/"GlobalAnimations_District_MaleFlinch.prp", /*face goes funny*/"GlobalAnimations_District_MaleGroan.prp","GlobalAnimations_District_MaleKneel.prp","GlobalAnimations_District_MaleLeanLeft.prp","GlobalAnimations_District_MaleLeanRight.prp","GlobalAnimations_District_MaleLookAround.prp","GlobalAnimations_District_MaleOkay.prp","GlobalAnimations_District_MaleOverHere.prp","GlobalAnimations_District_MalePeer.prp","GlobalAnimations_District_MaleSalute.prp","GlobalAnimations_District_MaleScratchHead.prp","GlobalAnimations_District_MaleShakeFist.prp","GlobalAnimations_District_MaleShoo.prp","GlobalAnimations_District_MaleSlouchSad.prp","GlobalAnimations_District_MaleStop.prp","GlobalAnimations_District_MaleTalkHand.prp","GlobalAnimations_District_MaleTapFoot.prp","GlobalAnimations_District_MaleTaunt.prp","GlobalAnimations_District_MaleThumbsDown.prp","GlobalAnimations_District_MaleThumbsDown2.prp","GlobalAnimations_District_MaleThumbsUp.prp","GlobalAnimations_District_MaleThumbsUp2.prp","GlobalAnimations_District_MaleWaveLow.prp","GlobalAnimations_District_MaleWinded.prp",
            "GlobalAnimations_District_FemaleDance.prp","GlobalAnimations_District_MaleDance.prp", /*arms disappear*/
            // Jalak column animation
            "GlobalAnimations_District_MaleKITap.prp", "GlobalAnimations_District_FemaleKITap.prp",
            "Neighborhood_District_nb01BahroPedestalShout.prp", "city_District_islmBahroShoutFerry.prp","city_District_islmBahroShoutLibrary.prp","city_District_islmBahroShoutPalace.prp",
            "city_District_bahroFlyers_arch.prp","city_District_bahroFlyers_city1.prp","city_District_bahroFlyers_city2.prp","city_District_bahroFlyers_city3.prp","city_District_bahroFlyers_city4.prp","city_District_bahroFlyers_city5.prp","city_District_bahroFlyers_city6.prp",
            "Ahnonay.age","Ahnonay.fni","Ahnonay.sum","Ahnonay_District_BuiltIn.prp","Ahnonay_District_EngineerHut.prp","Ahnonay_District_Textures.prp","Ahnonay_District_Vortex.prp","Ahnonay_District_YeeshaSketchBahro.prp",/*"Ahnonay_District_ahnySphereCtrl.prp",*/ //partial Ahnonay Age.
    };
    
    public static String[] moulSupportedList2 = { //used in addition to Simplicity for Manual conversion.
                "Personal.age","Personal.fni","Personal.sum","Personal_District_BuiltIn.prp","Personal_District_psnlMYSTII.prp","Personal_District_Textures.prp",
                "GlobalClothing.age","GlobalClothing.sum","GlobalClothing_District_Female01.prp","GlobalClothing_District_Female02.prp","GlobalClothing_District_Female04.prp","GlobalClothing_District_Female.prp","GlobalClothing_District_Male01.prp","GlobalClothing_District_Male02.prp","GlobalClothing_District_Male04.prp","GlobalClothing_District_Male.prp",
                "Ahnonay.age","Ahnonay.fni","Ahnonay.sum","Ahnonay_District_ahnySphere01.prp","Ahnonay_District_ahnySphere02.prp","Ahnonay_District_ahnySphere03.prp","Ahnonay_District_ahnySphere04.prp","Ahnonay_District_ahnySphereCtrl.prp","Ahnonay_District_BuiltIn.prp","Ahnonay_District_EngineerHut.prp","Ahnonay_District_FemaleSwimDockExit.prp","Ahnonay_District_FemaleValveWheelCCW.prp","Ahnonay_District_FemaleValveWheelCW.prp","Ahnonay_District_FemaleVogChairExit.prp","Ahnonay_District_FemaleVogChairRide.prp","Ahnonay_District_Hub.prp","Ahnonay_District_MaintRoom01.prp","Ahnonay_District_MaintRoom02.prp","Ahnonay_District_MaintRoom03.prp","Ahnonay_District_MaintRoom04.prp","Ahnonay_District_MaleSwimDockExit.prp","Ahnonay_District_MaleValveWheelCCW.prp","Ahnonay_District_MaleValveWheelCW.prp","Ahnonay_District_MaleVogChairExit.prp","Ahnonay_District_MaleVogChairRide.prp","Ahnonay_District_QuabIdle01.prp","Ahnonay_District_QuabIdle02.prp","Ahnonay_District_QuabIdle03.prp","Ahnonay_District_QuabRun01.prp","Ahnonay_District_QuabRun02.prp","Ahnonay_District_QuabRun03.prp","Ahnonay_District_Sphere01BuildingInterior.prp","Ahnonay_District_Textures.prp","Ahnonay_District_Vortex.prp","Ahnonay_District_YeeshaSketchBahro.prp",
                "AhnonayCathedral.age","AhnonayCathedral.fni","AhnonayCathedral.sum","AhnonayCathedral_District_BuiltIn.prp","AhnonayCathedral_District_LinkRoom.prp","AhnonayCathedral_District_Textures.prp",
                


//"GlobalAnimations.age",

/*
//"GlobalAnimations_District_FemaleAFKEnter.prp",
//"GlobalAnimations_District_FemaleAFKExit.prp",
//"GlobalAnimations_District_FemaleAFKIdle.prp",
//"GlobalAnimations_District_FemaleAgree.prp",
"GlobalAnimations_District_FemaleAmazed.prp",
"GlobalAnimations_District_FemaleAskQuestion.prp",
"GlobalAnimations_District_FemaleBeckonBig.prp",
"GlobalAnimations_District_FemaleBeckonSmall.prp",
//"GlobalAnimations_District_FemaleBlindsLeverDown.prp",
//"GlobalAnimations_District_FemaleBlindsLeverUp.prp",
//"GlobalAnimations_District_FemaleBlndFrntLeverDown.prp",
//"GlobalAnimations_District_FemaleBlndFrntLeverUp.prp",
"GlobalAnimations_District_FemaleBookAccept.prp", //arms disappear
"GlobalAnimations_District_FemaleBookAcceptIdle.prp", //avatar disappear
"GlobalAnimations_District_FemaleBookOffer.prp", //arms disappear
"GlobalAnimations_District_FemaleBookOfferFinish.prp", //arms disappear
"GlobalAnimations_District_FemaleBookOfferIdle.prp", //avatar disappear
"GlobalAnimations_District_FemaleBow.prp",
//"GlobalAnimations_District_FemaleButtonTouch.prp",
"GlobalAnimations_District_FemaleCallMe.prp",
//"GlobalAnimations_District_FemaleCheer.prp",
//"GlobalAnimations_District_FemaleClap.prp",
"GlobalAnimations_District_FemaleCower.prp",
"GlobalAnimations_District_FemaleCrazy.prp",
"GlobalAnimations_District_FemaleCringe.prp",
"GlobalAnimations_District_FemaleCrossArms.prp",
//"GlobalAnimations_District_FemaleCry.prp",
//"GlobalAnimations_District_FemaleDance.prp",
"GlobalAnimations_District_FemaleDoh.prp",
//"GlobalAnimations_District_FemaleDoorButtonTouch.prp",
//"GlobalAnimations_District_FemaleFall.prp",
//"GlobalAnimations_District_FemaleFall2.prp",
//"GlobalAnimations_District_FemaleFallingLinkOut.prp",
//"GlobalAnimations_District_FemaleFishBookLinkOut.prp",
"GlobalAnimations_District_FemaleFlinch.prp",
//"GlobalAnimations_District_FemaleFloorLeverA.prp",
//"GlobalAnimations_District_FemaleFloorLeverAUp.prp",
"GlobalAnimations_District_FemaleGlobalScopeGrab.prp", //not an emote
"GlobalAnimations_District_FemaleGlobalScopeHold.prp", //not an emote
"GlobalAnimations_District_FemaleGlobalScopeRelease.prp", //not an emote
"GlobalAnimations_District_FemaleGroan.prp",
//"GlobalAnimations_District_FemaleGroundImpact.prp",
//"GlobalAnimations_District_FemaleIdle.prp",
//"GlobalAnimations_District_FemaleInsertKiHand.prp",
//"GlobalAnimations_District_FemaleInsertKiHandLonger.prp",
//"GlobalAnimations_District_FemaleKiBegin.prp",
//"GlobalAnimations_District_FemaleKiEnd.prp",
//"GlobalAnimations_District_FemaleKiGlance.prp",
"GlobalAnimations_District_FemaleKITap.prp", //might not be an emote?
//"GlobalAnimations_District_FemaleKiUse.prp",
"GlobalAnimations_District_FemaleKneel.prp",
//"GlobalAnimations_District_FemaleLadderDown.prp",
//"GlobalAnimations_District_FemaleLadderDownOff.prp",
//"GlobalAnimations_District_FemaleLadderDownOn.prp",
//"GlobalAnimations_District_FemaleLadderUp.prp",
//"GlobalAnimations_District_FemaleLadderUpOff.prp",
//"GlobalAnimations_District_FemaleLadderUpOn.prp",
//"GlobalAnimations_District_FemaleLaugh.prp",
"GlobalAnimations_District_FemaleLeanLeft.prp",
"GlobalAnimations_District_FemaleLeanRight.prp",
//"GlobalAnimations_District_FemaleLinkOut.prp",
"GlobalAnimations_District_FemaleLookAround.prp",
"GlobalAnimations_District_FemaleOkay.prp", //eyes go funny
"GlobalAnimations_District_FemaleOverHere.prp",
"GlobalAnimations_District_FemalePeer.prp",
//"GlobalAnimations_District_FemalePelletBookLeft.prp",
//"GlobalAnimations_District_FemalePelletBookRight.prp",
//"GlobalAnimations_District_FemalePelletBookWait.prp",
//"GlobalAnimations_District_FemalePersonalLink.prp",
//"GlobalAnimations_District_FemalePoint.prp",
//"GlobalAnimations_District_FemaleRun.prp",
//"GlobalAnimations_District_FemaleRunningImpact.prp",
//"GlobalAnimations_District_FemaleRunningJump.prp",
"GlobalAnimations_District_FemaleSalute.prp",
"GlobalAnimations_District_FemaleScratchHead.prp",
"GlobalAnimations_District_FemaleShakeFist.prp",
//"GlobalAnimations_District_FemaleShakeHead.prp",
"GlobalAnimations_District_FemaleShoo.prp",
//"GlobalAnimations_District_FemaleShooterTrapActivate.prp",
//"GlobalAnimations_District_FemaleShortIdle.prp",
//"GlobalAnimations_District_FemaleShortLeap.prp",
//"GlobalAnimations_District_FemaleShrug.prp",
//"GlobalAnimations_District_FemaleSideSwimLeft.prp",
//"GlobalAnimations_District_FemaleSideSwimRight.prp",
//"GlobalAnimations_District_FemaleSitDown.prp",
//"GlobalAnimations_District_FemaleSitDownGround.prp",
//"GlobalAnimations_District_FemaleSitDownLeft.prp",
//"GlobalAnimations_District_FemaleSitFront.prp",
//"GlobalAnimations_District_FemaleSitIdle.prp",
//"GlobalAnimations_District_FemaleSitIdleGround.prp",
//"GlobalAnimations_District_FemaleSitLeft.prp",
//"GlobalAnimations_District_FemaleSitRight.prp",
//"GlobalAnimations_District_FemaleSitStand.prp",
//"GlobalAnimations_District_FemaleSitStandGround.prp",
"GlobalAnimations_District_FemaleSlouchSad.prp",
//"GlobalAnimations_District_FemaleSneeze.prp",
//"GlobalAnimations_District_FemaleSoftLanding.prp",
//"GlobalAnimations_District_FemaleStandingJump.prp",
//"GlobalAnimations_District_FemaleStandUpFront.prp",
//"GlobalAnimations_District_FemaleStandUpLeft.prp",
//"GlobalAnimations_District_FemaleStandUpRight.prp",
//"GlobalAnimations_District_FemaleStepLeft.prp",
//"GlobalAnimations_District_FemaleStepOnFloorPlate.prp",
//"GlobalAnimations_District_FemaleStepRight.prp",
"GlobalAnimations_District_FemaleStop.prp",
//"GlobalAnimations_District_FemaleSwimBackward.prp",
//"GlobalAnimations_District_FemaleSwimFast.prp",
//"GlobalAnimations_District_FemaleSwimIdle.prp",
//"GlobalAnimations_District_FemaleSwimSlow.prp",
//"GlobalAnimations_District_FemaleSwimSurfaceDive.prp",
//"GlobalAnimations_District_FemaleSwimUnderwater.prp",
//"GlobalAnimations_District_FemaleTalk.prp",
"GlobalAnimations_District_FemaleTalkHand.prp",
"GlobalAnimations_District_FemaleTapFoot.prp",
"GlobalAnimations_District_FemaleTaunt.prp",
//"GlobalAnimations_District_FemaleThank.prp",
"GlobalAnimations_District_FemaleThumbsDown.prp",
"GlobalAnimations_District_FemaleThumbsDown2.prp",
"GlobalAnimations_District_FemaleThumbsUp.prp",
"GlobalAnimations_District_FemaleThumbsUp2.prp",
//"GlobalAnimations_District_FemaleTouchPellet.prp",
//"GlobalAnimations_District_FemaleTreadWaterTurnLeft.prp",
//"GlobalAnimations_District_FemaleTreadWaterTurnRight.prp",
//"GlobalAnimations_District_FemaleTurnLeft.prp",
//"GlobalAnimations_District_FemaleTurnRight.prp",
//"GlobalAnimations_District_FemaleWalk.prp",
//"GlobalAnimations_District_FemaleWalkBack.prp",
//"GlobalAnimations_District_FemaleWalkingJump.prp",
"GlobalAnimations_District_FemaleWallSlide.prp", //not an emote (Gahreesen?)
//"GlobalAnimations_District_FemaleWave.prp",
"GlobalAnimations_District_FemaleWaveLow.prp",
"GlobalAnimations_District_FemaleWinded.prp",
//"GlobalAnimations_District_FemaleYawn.prp",
        
//"GlobalAnimations_District_kgFall.prp",
//"GlobalAnimations_District_kgIdle.prp",
//"GlobalAnimations_District_kgKiBegin.prp",
//"GlobalAnimations_District_kgKiEnd.prp",
//"GlobalAnimations_District_kgKiGlance.prp",
//"GlobalAnimations_District_kgKiTap.prp",
//"GlobalAnimations_District_kgKiUse.prp",
//"GlobalAnimations_District_kgRun.prp",
//"GlobalAnimations_District_kgRunningJump.prp",
//"GlobalAnimations_District_kgSitDownGround.prp",
//"GlobalAnimations_District_kgSitIdleGround.prp",
//"GlobalAnimations_District_kgSitStandGround.prp",
//"GlobalAnimations_District_kgStandingJump.prp",
//"GlobalAnimations_District_kgStepLeft.prp",
//"GlobalAnimations_District_kgStepRight.prp",
//"GlobalAnimations_District_kgTurnLeft.prp",
//"GlobalAnimations_District_kgTurnRight.prp",
//"GlobalAnimations_District_kgWalk.prp",
//"GlobalAnimations_District_kgWalkBack.prp",
//"GlobalAnimations_District_kgWalkingJump.prp",
//"GlobalAnimations_District_kgWave.prp",
        
//"GlobalAnimations_District_MaleAFKEnter.prp",
//"GlobalAnimations_District_MaleAFKExit.prp",
//"GlobalAnimations_District_MaleAFKIdle.prp",
//"GlobalAnimations_District_MaleAgree.prp",
"GlobalAnimations_District_MaleAmazed.prp",
"GlobalAnimations_District_MaleAskQuestion.prp", //arm doesn't go up correctly.
"GlobalAnimations_District_MaleBeckonBig.prp",
"GlobalAnimations_District_MaleBeckonSmall.prp",
//"GlobalAnimations_District_MaleBlindsLeverDown.prp",
//"GlobalAnimations_District_MaleBlindsLeverUp.prp",
//"GlobalAnimations_District_MaleBlndFrntLeverDown.prp",
//"GlobalAnimations_District_MaleBlndFrntLeverUp.prp",
"GlobalAnimations_District_MaleBookAccept.prp", //not an emote
"GlobalAnimations_District_MaleBookAcceptIdle.prp", //not an emote
"GlobalAnimations_District_MaleBookOffer.prp", //not an emote
"GlobalAnimations_District_MaleBookOfferFinish.prp", //not an emote
"GlobalAnimations_District_MaleBookOfferIdle.prp", //avatar disappears
"GlobalAnimations_District_MaleBow.prp",
//"GlobalAnimations_District_MaleButtonTouch.prp",
"GlobalAnimations_District_MaleCallMe.prp",
//"GlobalAnimations_District_MaleCheer.prp",
//"GlobalAnimations_District_MaleClap.prp",
"GlobalAnimations_District_MaleCower.prp", //eyes go funny
"GlobalAnimations_District_MaleCrazy.prp", //face goes funny
"GlobalAnimations_District_MaleCringe.prp",
"GlobalAnimations_District_MaleCrossArms.prp", //arms disappear
//"GlobalAnimations_District_MaleCry.prp",
//"GlobalAnimations_District_MaleDance.prp",
"GlobalAnimations_District_MaleDoh.prp", //face goes funny
//"GlobalAnimations_District_MaleDoorButtonTouch.prp",
//"GlobalAnimations_District_MaleFall.prp",
//"GlobalAnimations_District_MaleFall2.prp",
//"GlobalAnimations_District_MaleFallingLinkOut.prp",
//"GlobalAnimations_District_MaleFishBookLinkOut.prp",
"GlobalAnimations_District_MaleFlinch.prp", //face goes funny
//"GlobalAnimations_District_MaleFloorLeverA.prp",
//"GlobalAnimations_District_MaleFloorLeverAUp.prp",
"GlobalAnimations_District_MaleGlobalScopeGrab.prp", //not an emote
"GlobalAnimations_District_MaleGlobalScopeHold.prp", //not an emote
"GlobalAnimations_District_MaleGlobalScopeRelease.prp", //not an emote
"GlobalAnimations_District_MaleGroan.prp",
//"GlobalAnimations_District_MaleGroundImpact.prp",
//"GlobalAnimations_District_MaleIdle.prp",
//"GlobalAnimations_District_MaleInsertKiHand.prp",
//"GlobalAnimations_District_MaleInsertKiHandLonger.prp",
//"GlobalAnimations_District_MaleKiBegin.prp",
//"GlobalAnimations_District_MaleKiEnd.prp",
//"GlobalAnimations_District_MaleKiGlance.prp",
"GlobalAnimations_District_MaleKITap.prp", //not an emote?
//"GlobalAnimations_District_MaleKiUse.prp",
"GlobalAnimations_District_MaleKneel.prp",
//"GlobalAnimations_District_MaleLadderDown.prp",
//"GlobalAnimations_District_MaleLadderDownOff.prp",
//"GlobalAnimations_District_MaleLadderDownOn.prp",
//"GlobalAnimations_District_MaleLadderUp.prp",
//"GlobalAnimations_District_MaleLadderUpOff.prp",
//"GlobalAnimations_District_MaleLadderUpOn.prp",
//"GlobalAnimations_District_MaleLaugh.prp",
"GlobalAnimations_District_MaleLeanLeft.prp",
"GlobalAnimations_District_MaleLeanRight.prp",
//"GlobalAnimations_District_MaleLinkOut.prp",
"GlobalAnimations_District_MaleLookAround.prp",
"GlobalAnimations_District_MaleOkay.prp",
"GlobalAnimations_District_MaleOverHere.prp",
"GlobalAnimations_District_MalePeer.prp",
//"GlobalAnimations_District_MalePelletBookLeft.prp",
//"GlobalAnimations_District_MalePelletBookRight.prp",
//"GlobalAnimations_District_MalePelletBookWait.prp",
//"GlobalAnimations_District_MalePersonalLink.prp",
//"GlobalAnimations_District_MalePoint.prp",
//"GlobalAnimations_District_MaleRun.prp",
//"GlobalAnimations_District_MaleRunningImpact.prp",
//"GlobalAnimations_District_MaleRunningJump.prp",
"GlobalAnimations_District_MaleSalute.prp",
"GlobalAnimations_District_MaleScratchHead.prp",
"GlobalAnimations_District_MaleShakeFist.prp",
//"GlobalAnimations_District_MaleShakeHead.prp",
"GlobalAnimations_District_MaleShoo.prp",
//"GlobalAnimations_District_MaleShooterTrapActivate.prp",
//"GlobalAnimations_District_MaleShortIdle.prp",
//"GlobalAnimations_District_MaleShortLeap.prp",
//"GlobalAnimations_District_MaleShrug.prp",
//"GlobalAnimations_District_MaleSideSwimLeft.prp",
//"GlobalAnimations_District_MaleSideSwimRight.prp",
//"GlobalAnimations_District_MaleSitDown.prp",
//"GlobalAnimations_District_MaleSitDownGround.prp",
//"GlobalAnimations_District_MaleSitDownLeft.prp",
//"GlobalAnimations_District_MaleSitFront.prp",
//"GlobalAnimations_District_MaleSitIdle.prp",
//"GlobalAnimations_District_MaleSitIdleGround.prp",
//"GlobalAnimations_District_MaleSitLeft.prp",
//"GlobalAnimations_District_MaleSitRight.prp",
//"GlobalAnimations_District_MaleSitStand.prp",
//"GlobalAnimations_District_MaleSitStandGround.prp",
"GlobalAnimations_District_MaleSlouchSad.prp",
//"GlobalAnimations_District_MaleSneeze.prp",
//"GlobalAnimations_District_MaleSoftLanding.prp",
//"GlobalAnimations_District_MaleStandingJump.prp",
//"GlobalAnimations_District_MaleStandUpFront.prp",
//"GlobalAnimations_District_MaleStandUpLeft.prp",
//"GlobalAnimations_District_MaleStandUpRight.prp",
//"GlobalAnimations_District_MaleStepLeft.prp",
//"GlobalAnimations_District_MaleStepOnFloorPlate.prp",
//"GlobalAnimations_District_MaleStepRight.prp",
"GlobalAnimations_District_MaleStop.prp",
//"GlobalAnimations_District_MaleSwimBackward.prp",
//"GlobalAnimations_District_MaleSwimFast.prp",
//"GlobalAnimations_District_MaleSwimIdle.prp",
//"GlobalAnimations_District_MaleSwimSlow.prp",
//"GlobalAnimations_District_MaleTalk.prp",
"GlobalAnimations_District_MaleTalkHand.prp",
"GlobalAnimations_District_MaleTapFoot.prp",
"GlobalAnimations_District_MaleTaunt.prp",
//"GlobalAnimations_District_MaleThank.prp",
"GlobalAnimations_District_MaleThumbsDown.prp",
"GlobalAnimations_District_MaleThumbsDown2.prp",
"GlobalAnimations_District_MaleThumbsUp.prp",
"GlobalAnimations_District_MaleThumbsUp2.prp",
//"GlobalAnimations_District_MaleTouchPellet.prp",
//"GlobalAnimations_District_MaleTreadWater.prp",
//"GlobalAnimations_District_MaleTreadWaterTurnLeft.prp",
//"GlobalAnimations_District_MaleTreadWaterTurnRight.prp",
//"GlobalAnimations_District_MaleTurnLeft.prp",
//"GlobalAnimations_District_MaleTurnRight.prp",
//"GlobalAnimations_District_MaleWalk.prp",
//"GlobalAnimations_District_MaleWalkBack.prp",
//"GlobalAnimations_District_MaleWalkingJump.prp",
"GlobalAnimations_District_MaleWallSlide.prp", //not an emote
//"GlobalAnimations_District_MaleWarpHack.prp",
//"GlobalAnimations_District_MaleWave.prp",
"GlobalAnimations_District_MaleWaveLow.prp",
"GlobalAnimations_District_MaleWinded.prp",
//"GlobalAnimations_District_MaleYawn.prp",

"GlobalAnimations_District_FemaleDance.prp",
"GlobalAnimations_District_MaleDance.prp", //arms disappear
*/
                
"GlobalAnimations_District_kgFall.prp",
"GlobalAnimations_District_kgIdle.prp",
"GlobalAnimations_District_kgKiBegin.prp",
"GlobalAnimations_District_kgKiEnd.prp",
"GlobalAnimations_District_kgKiGlance.prp",
"GlobalAnimations_District_kgKiTap.prp",
"GlobalAnimations_District_kgKiUse.prp",
"GlobalAnimations_District_kgRun.prp",
"GlobalAnimations_District_kgRunningJump.prp",
"GlobalAnimations_District_kgSitDownGround.prp",
"GlobalAnimations_District_kgSitIdleGround.prp",
"GlobalAnimations_District_kgSitStandGround.prp",
"GlobalAnimations_District_kgStandingJump.prp",
"GlobalAnimations_District_kgStepLeft.prp",
"GlobalAnimations_District_kgStepRight.prp",
"GlobalAnimations_District_kgTurnLeft.prp",
"GlobalAnimations_District_kgTurnRight.prp",
"GlobalAnimations_District_kgWalk.prp",
"GlobalAnimations_District_kgWalkBack.prp",
"GlobalAnimations_District_kgWalkingJump.prp",
"GlobalAnimations_District_kgWave.prp",
                
                
                
/*"city_District_bahroFlyers_arch.prp",
"city_District_bahroFlyers_city1.prp",
"city_District_bahroFlyers_city2.prp",
"city_District_bahroFlyers_city3.prp",
"city_District_bahroFlyers_city4.prp",
"city_District_bahroFlyers_city5.prp",
"city_District_bahroFlyers_city6.prp",*/
"city_District_islmPodMapGUI.prp",
                
    };
    
    /*public static String[] crowMusic = {
        "mntnAmbientMx.ogg",
        "mrshAmbientMx.ogg",
    };*/
    
    /*public static String[] myst5Music = {
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
    };*/
    
    /*public static String[] potsMusic = {
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
    };*/
    
    /*public static String[] moulMusicNotInPotsOrDifferent = {
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
    };*/
    
    private static String[] mystvSupportedList = {
        "Descent.age","Descent.fni","Descent.sum","Descent_dsntBahro_Idle02.prp","Descent_dsntBahro_Idle03.prp","Descent_dsntBahro_Idle04.prp","Descent_dsntBahro_Idle05.prp","Descent_dsntBahro_Idle06.prp","Descent_dsntBahro_Idle07.prp","Descent_dsntBahro_Idle08.prp","Descent_dsntBahro_Idle09.prp","Descent_dsntBahro_Shot02.prp","Descent_dsntBahro_Shot03.prp","Descent_dsntBahro_Shot04.prp","Descent_dsntBahro_Shot05.prp","Descent_dsntBahro_Shot06.prp","Descent_dsntBahro_Shot07.prp","Descent_dsntBahro_Shot08.prp","Descent_dsntBahro_Shot09.prp","Descent_dsntBahro_Tunnel01.prp","Descent_dsntBahro_Tunnel01Idle.prp","Descent_dsntBats.prp","Descent_dsntEsherIdleTopOfShaft.prp","Descent_dsntEsher_BottomOfShaft.prp","Descent_dsntEsher_FirstHub.prp","Descent_dsntEsher_Intro.prp","Descent_dsntEsher_TopOfShaft.prp","Descent_dsntGreatShaftBalcony.prp","Descent_dsntGreatShaftLowerRm.prp","Descent_dsntLowerBats.prp","Descent_dsntMapGUI.prp","Descent_dsntPostBats.prp","Descent_dsntPostShaftNodeAndTunnels.prp","Descent_dsntShaftGeneratorRoom.prp","Descent_dsntShaftTunnelSystem.prp","Descent_dsntTianaCave.prp","Descent_dsntTianaCaveNode2.prp","Descent_dsntTianaCaveTunnel1.prp","Descent_dsntTianaCaveTunnel3.prp","Descent_dsntUpperBats.prp","Descent_dsntUpperShaft.prp","Descent_dsntVolcano.prp","Descent_Textures.prp",
        "Direbo.age","Direbo.fni","Direbo.sum","Direbo_DragonFly.prp","Direbo_drboEsherIdleDirebo.prp","Direbo_drboEsher_DireboLaki.prp","Direbo_drboEsher_DireboSrln.prp","Direbo_drboEsher_DireboTdlm.prp","Direbo_drboEsher_DireboThgr.prp","Direbo_drboUrwinShape.prp","Direbo_RestAge.prp","Direbo_Textures.prp","Direbo_UrwinIdle.prp","Direbo_UrwinWalk.prp",
        "Kveer.age","Kveer.fni","Kveer.sum","Kveer_bkMystBookLocked.prp","Kveer_GreatRm.prp","Kveer_KveerBats.prp","Kveer_kverAtrus.prp","Kveer_kverAtrus_1.prp","Kveer_kverAtrus_Idle.prp","Kveer_kverBahroWingsGUI.prp","Kveer_kverBahro_1.prp","Kveer_kverBahro_2.prp","Kveer_kverBahro_Ballroom01.prp","Kveer_kverBahro_Ballroom02.prp","Kveer_kverBahro_Ballroom03.prp","Kveer_kverBahro_Exit01.prp","Kveer_kverBahro_Exit02.prp","Kveer_kverBahro_Idle05.prp","Kveer_kverBahro_Idle06.prp","Kveer_kverBahro_Idle07.prp","Kveer_kverBahro_Idle08.prp","Kveer_kverBahro_Idle09.prp","Kveer_kverBahro_Shot03.prp","Kveer_kverBahro_Shot04.prp","Kveer_kverBahro_Shot05.prp","Kveer_kverBahro_Shot06.prp","Kveer_kverBahro_Shot07.prp","Kveer_kverBahro_Shot08.prp","Kveer_kverBahro_Shot09.prp","Kveer_kverConc3Music.prp","Kveer_kverEsher_1.prp","Kveer_kverReleeshan.prp","Kveer_kverYeesha_1.prp","Kveer_kverYeesha_Conc01.prp","Kveer_kverYeesha_Conc02.prp","Kveer_kverYeesha_Conc03.prp","Kveer_kverYeesha_ConcIntro.prp","Kveer_kverYeesha_ConcIntro2.prp","Kveer_kverYeesha_IdleForIntro.prp","Kveer_kverYeesha_Intro.prp","Kveer_Prison.prp","Kveer_Textures.prp",
        "Laki.age","Laki.fni","Laki.sum","Laki_Exterior.prp","Laki_LakiArenaVillaInt.prp","Laki_LakiCreatures.prp","Laki_lakiEsher-Arena.prp","Laki_lakiEsher-FighterBeach.prp","Laki_lakiEsher-Keep.prp","Laki_lakiEsher-Villa.prp","Laki_lakiEsherIdleKeep.prp","Laki_lakiEsherIdleVilla.prp","Laki_LakiMaze.prp","Laki_lakiMazeClue.prp","Laki_LakiTrees01.prp","Laki_PirBirdActor.prp","Laki_PirBirdChomp.prp","Laki_PirBirdIdle.prp","Laki_PirBirdSwallow.prp","Laki_PirBirdVocalize.prp","Laki_PirBirdWalk.prp","Laki_Textures.prp",
        "Myst.age","Myst.fni","Myst.sum","Myst_Island.prp","Myst_mystEsher-Conc01.prp","Myst_mystEsher-Conc02.prp","Myst_Textures.prp",
        "Siralehn.age","Siralehn.fni","Siralehn.sum","Siralehn_Birds.prp","Siralehn_Drawing01.prp","Siralehn_Drawing02.prp","Siralehn_Drawing03.prp","Siralehn_Drawing04.prp","Siralehn_Drawing05.prp","Siralehn_Drawing06.prp","Siralehn_Drawing07.prp","Siralehn_Drawing08.prp","Siralehn_Exterior.prp","Siralehn_rock.prp","Siralehn_srlnEsherIdleBeach.prp","Siralehn_srlnEsherIdleLab.prp","Siralehn_srlnEsher_NolobenBeach.prp","Siralehn_srlnEsher_NolobenKeep.prp","Siralehn_srlnEsher_NolobenLab.prp","Siralehn_srlnKeepInter.prp","Siralehn_Textures.prp","Siralehn_tunnels.prp",
        "Tahgira.age","Tahgira.fni","Tahgira.sum","Tahgira_Exterior.prp","Tahgira_IceCave.prp","Tahgira_Textures.prp","Tahgira_thgrEsherIdleIntro.prp","Tahgira_thgrEsherIdleTake.prp","Tahgira_thgrEsher_TahgiraGrave.prp","Tahgira_thgrEsher_TahgiraIntro.prp","Tahgira_thgrEsher_TahgiraTake.prp","Tahgira_thgrEsher_TahgiraThermals.prp","Tahgira_thgrEsher_TahgiraVillage.prp",
        "Todelmer.age","Todelmer.fni","Todelmer.sum","Todelmer_Exterior.prp","Todelmer_InteriorPillar1.prp","Todelmer_InteriorPillar3.prp","Todelmer_MiniScope.prp","Todelmer_Pod.prp","Todelmer_Sky.prp","Todelmer_tdlmEsherIdleP3.prp","Todelmer_tdlmEsherIdleRing.prp","Todelmer_tdlmEsher_TodelmerP1.prp","Todelmer_tdlmEsher_TodelmerP3.prp","Todelmer_tdlmEsher_TodelmerRing.prp","Todelmer_Textures.prp",
    };
}

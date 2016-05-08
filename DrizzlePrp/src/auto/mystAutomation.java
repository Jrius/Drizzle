/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.FileUtils;
import shared.Bytes;
import uru.UruCrypt;
import prpobjects.textfile;
import java.util.HashMap;
import uru.Bytestream;
import uru.context;
import prpobjects.prpfile;
import shared.m;
import prpobjects.Typeid;
import prpobjects.prputils.Compiler.Decider;
import prpobjects.Uruobjectdesc;
import prpobjects.Pageid;
import java.util.Vector;
import java.io.File;
import prpobjects.PrpRootObject;
import prpobjects.prputils;
import prpobjects.Uruobjectref;
import shared.Flt;
import prpobjects.Rgba;
import shared.State.AllStates;
import prpobjects.Urustring;
import prpobjects.Uruobjectdesc;
import prpobjects.Transmatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import prpobjects.plPythonFileMod.Pythonlisting;
import prpobjects.plPythonFileMod;
import prpobjects.*;
import shared.Pair;
import shared.generic;
import shared.cmap;
import shared.IBytestream;

public class mystAutomation
{
    /*public static class moulDecider implements Decider
    {
        public boolean isObjectToBeIncluded(Uruobjectdesc desc)
        {
                Typeid type = desc.objecttype;
                int number = desc.objectnumber;
                String name = desc.objectname.toString();
                Pageid pageid = desc.pageid;

                String[] namestartswith = {};
                String[] nameequals = {};
                Typeid[] typeequals = {};
                
                boolean useObject = false;
                
                //blacklist
                if(type==type.plSceneNode) return false; //do not allow Scene node in here, it must be treated separately.
                //if(pageid.getRawData()==0x220024 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                //if(pageid.getRawData()==0x2A0025 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).
                if(pageid.prefix==0x22 && pageid.suffix==0x24 && type==type.plResponderModifier && name.equals("RespWedges")) return false; //livebahrocaves pod district problem. (crashes when linking.)
                if(pageid.prefix==0x2A && pageid.suffix==0x25 && type==type.plResponderModifier && name.equals("cRespExcludeRgn")) return false; //minkata cameras district problem. (crashes when going to night).
                             
                            
                             
                
                int useCase = 11;
                switch(useCase)
                {
                    case 0: //ederdelin main
                        //blacklist
                        if(name.equals("LampBall40")) return false; //ball in alcove lamp doesn't work. Uses LayerAnimation
                        
                        //whitelist
                        
                        //specific sceneobjects to include:
                        if(type==type.plSceneObject) return true; //test
                        //if(number==406 && type==type.plSceneObject) return true; //LinkInPointDefault.
                        if(name.equals("LinkInPointDefault")) return true;
                        //if(name.startsWith("LampRTOmniLight")) return true; //get all the omni lamps
                        //if(name.startsWith("RTFillLight")) return true; //get all the directional lamps
                        //if(name.startsWith("dlnMArbleLanternGlare")) return true;
                        //if(name.startsWith("dlnLanternGlare")) return true;
                        //if(name.startsWith("ChainPlane")) return true; //this crashes the game. It's two SceneObjects that enables two viewfacemodifiers. Those viewfacemodifier have different flags than the one's that work.  Is this a new option not available in pots? Or does it use a material that isn't loading right(i.e. one of the animated ones)?
                        namestartswith = new String[]{
                            "SnowParty","Garden02Snow", //required for snow.
                            "a",
                            "bench05","bench06","big","blend","bluedoor",
                            "bluespiraldoorglow",
                            ////"bluespiralglow", //side-effect of huge lampshade in alcove. Caused by my faked materials for plLayerAnimation?
                            "bluespiralrig",
                            "bluespiraltapestry",
                            "blulamp",
                            "bscamera", //not working.
                            "bsdoortimegage",
                            "bugpath01",
                            "cam-sitbench","canyonpillar","chainplane",
                            "clothglowgroup","clusterflowers",
                            "contemplationsteps",
                            "dln",
                            ////"dust", //crashes the game; contains plAGMasterMod
                            "fern0","fern1","fern2","fern3","fern4","fern5",
                            "followcamera", "followclosecamera",
                            ////"fountain", //has side-effect of huge lampshade.
                            "garden02snow",
                            ////"Garden2background", //has side-effect
                            ////"garden2top", //has side-effect
                            "gazebo1camera","gazebo2camera",
                            "lamp","lapm",
                            ////"leafgenerator", //crashes game, probably has LayerAnimation for leaves (no normal animation, though) The leaves don't seem visible in screenshots anyway.
                            ////"leafkiller" //not needed, since we have no leaves anyway. It doesn't appear to have anything to do with the snow either.
                            ////"lightcone", //layeranimation
                            "MaintainersMarkerCracks",
                            ////"MaintainersMarker", //layeranimation
                            "NbhdBkPodiumPost",
                            "Object",
                            "path",
                            "patiobench",
                            "pythonbox",
                            "restroom",
                            ////"rock" //stuff related to movable and falling rocks.  Is this even used?
                            "rope",
                            "rt",
                            "sfxbs",
                            ////"sfxgrass" //run on grass sound, need physics
                            "sfxsnd-amb","sfxsnd-birds","sfxsnd-winteramb", //can't hear most of these(i did hear a random bird sound); it may just be a setting and perhaps you can't hear them in moul either. But I think responderModifier needs to be implemented.
                            ////"sfxsndamb" //uses linefollowmod
                            "sfxsndfountain", //works great.
                            ////"sfxsndloon", //use animation
                            "sfxsndwinter",
                            ////"sfxstone" //run on stone sound, need physics.
                            "signpost",
                            "smallrock",
                            ////"soundcontrol", //needs respondermodifier
                            "stairway02",
                            "statuebase","statueblue","statuechrome","statueplinth",
                            "treasure",
                            "tree",
                            "yeeshapage15decal","yeeshapageleafdecal",
                            "yeeshapage15","yeeshapageleaf", //should allow use to hide/show the two pages.
                        };
                        nameequals = new String[]{
                            "BigTree06",
                            "BigTree06Decal",
                            //"fountainpool", //layeranimation
                            //"fountainpool01", //layeranimation
                            "fountainwalkwaydecal",
                            //"fountainwater", //layeranimation
                            "Garden2",
                        };
                        
                        //if(number==1 && type==type.plSpawnModifier) return true;
                        if(type==type.plSpawnModifier) return true;
                        //if(number==175 && type==type.plCoordinateInterface) return true;
                        if(type==type.plCoordinateInterface) return true;
                        if(type==type.plDrawInterface) return true;
                        //if(number==1 && type==type.plDrawableSpans) return true;
                        if(type==type.plDrawableSpans) return true;
                        if(type==type.hsGMaterial) return true;
                        if(type==type.plLayer) return true;
                        if(type==type.plOmniLightInfo) return true;
                        if(type==type.plPointShadowMaster) return true;
                        if(type==type.plCubicEnvironMap) return true;
                        if(type==type.plMipMap) return true;
                        if(type==type.plPythonFileMod) return true;
                        if(type==type.plDirectionalLightInfo) return true;
                        if(type==type.plSimulationInterface) return true;
                        if(type==type.plViewFaceModifier) return true;
                        if(type==type.plAudioInterface) return true;
                        if(type==type.plStereizer) return true;
                        if(type==type.plSoundBuffer) return true;
                        if(type==type.plRandomSoundMod) return true;
                        if(type==type.plWin32StreamingSound) return true;
                        if(type==type.plWin32StaticSound) return true;
                        if(type==type.plWinAudio) return true;
                        if(type==type.plParticleSystem) return true;
                        if(type==type.plParticleCollisionEffectDie) return true;
                        if(type==type.plParticleLocalWind) return true;
                        if(type==type.plBoundInterface) return true;
                        typeequals = new Typeid[]{
                            type.plExcludeRegionModifier
                            ,type.plCameraBrain1
                            ,type.plCameraBrain1_Avatar
                            ,type.plCameraBrain1_Circle
                            ,type.plCameraBrain1_Fixed
                            ,type.plCameraModifier1,
                            type.plAGModifier
                        };
                        
                        //unstable
                        //if(name.equals("Archway")) return true;
                        //if(name.equals("ArchwayLight")) return true;
                        //if(type==type.plLayerAnimation) return true;
                        break;
                    case 1: //include all: spyroom builtin, spyroom textures, ederdelin builtin, ederdelin textures
                        return true;
                        //break;
                    case 2: //spyroom main
                        if(name.equals("LinkInPointSpyroom")) return true;
                        if(name.equals("StartPoint01")) return true;
                        break;
                    case 3: //basic link in
                        if(name.toLowerCase().startsWith("linkinpoint")) return true;
                        if(name.toLowerCase().startsWith("startpoint")) return true;
                        break;
                    case 4: //basic drawables
                        typeequals = new Typeid[]{
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                            //"atria",
                        };
                        break;
                    case 5: //guild pub
                        //if(name.toLowerCase().startsWith("imager")) return false;
                        //if(name.toLowerCase().startsWith("billboard")) return false;
                        //if(name.toLowerCase().startsWith("cpythgpub")) return false;
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                            //"a",
                            //"b","c","d",
                            //"e","f","g",//"h","i","j",
                            //"hanging","j",
                            //"j","half","hanginglantern",
                            "hanginglightflare",
                            //"agesdlhook",
                            //"atria",
                            //"ayhoheekrm01",
                            //"ayhoheekrm02",
                            //"balcony",
                            //"ballister",
                            //"boothwood",
                            //"cam",
                            //"canvashang",
                            //"cavewalls",
                            //"circlepath",
                            //"convchair",
                            //"curtain",
                            //"doorxrgn",
                        };
                        break;
                    case 6: //livebahrocaves
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                            "rt",
                            "starfield",
                        };
                        break;
                    case 7: //tetsonot
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                            
                            type.plSpotLightInfo,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                        };
                        break;
                    case 8: //minkata
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                            
                            type.plSpotLightInfo,

                            type.plShadowCaster,
                            type.plShadowCaster,
                            type.plDirectShadowMaster,
                            type.plRelevanceRegion,
                            type.plSoftVolumeSimple,
                            //type.plResponderModifier,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                        };
                        break;
                    case 9: //jalak, payiferen
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                            
                            type.plSpotLightInfo,

                            type.plShadowCaster,
                            type.plShadowCaster,
                            type.plDirectShadowMaster,
                            type.plRelevanceRegion,
                            type.plSoftVolumeSimple,
                            //type.plResponderModifier,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                        };
                        break;
                    case 10: //negilahn, edertsogal, new ederdelin, new minkata
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                            
                            type.plSpotLightInfo,

                            type.plShadowCaster,
                            type.plShadowCaster,
                            type.plDirectShadowMaster,
                            type.plRelevanceRegion,
                            type.plSoftVolumeSimple,
                            //type.plResponderModifier,
                            
                            type.plParticleFlockEffect,
                            type.plFadeOpacityMod,
                            type.plClusterGroup,
                        };
                        namestartswith = new String[]{
                            "linkinpoint",
                            "startpoint",
                        };
                        break;
                    case 11: //new minkata
                        typeequals = new Typeid[]{
                            type.plSceneObject,
                            
                            type.plCoordinateInterface,
                            type.plSpawnModifier,
                            type.plDrawInterface,
                            type.plDrawableSpans,
                            type.hsGMaterial,
                            type.plLayer,
                            type.plMipMap,
                            type.plCubicEnvironMap,
                            
                            type.plOmniLightInfo,
                            type.plPointShadowMaster,
                            type.plPythonFileMod,
                            type.plDirectionalLightInfo,
                            type.plSimulationInterface,
                            type.plViewFaceModifier,
                            type.plAudioInterface,
                            type.plStereizer,
                            type.plSoundBuffer,
                            type.plRandomSoundMod,
                            type.plWin32StreamingSound,
                            type.plWin32StaticSound,
                            type.plWinAudio,
                            type.plParticleSystem,
                            type.plParticleCollisionEffectDie,
                            type.plParticleLocalWind,
                            type.plBoundInterface,
                            type.plExcludeRegionModifier,
                            type.plCameraBrain1,
                            type.plCameraBrain1_Avatar,
                            type.plCameraBrain1_Circle,
                            type.plCameraBrain1_Fixed,
                            type.plCameraModifier1,
                            type.plAGModifier,
                            
                            type.plOccluder,
                            type.plDynamicTextMap,
                            
                            type.plParticleCollisionEffectBounce,
                            
                            type.plSpotLightInfo,

                            type.plShadowCaster,
                            type.plDirectShadowMaster,
                            type.plRelevanceRegion,
                            type.plSoftVolumeSimple,
                            
                            type.plParticleFlockEffect,
                            type.plFadeOpacityMod,
                            type.plClusterGroup,
                            type.plVisRegion,
                            type.plSoftVolumeUnion,
                            type.plObjectInVolumeDetector,
                            type.plObjectInBoxConditionalObject,
                            type.plInterfaceInfoModifier,
                            type.plVolumeSensorConditionalObject,
                            type.plLogicModifier,
                            type.plActivatorConditionalObject,
                            type.plFacingConditionalObject,
                            type.plOneShotMod,
                            type.plAvLadderMod,
                            type.plPickingDetector,
                            type.plCameraRegionDetector,
                            
                            type.plHKPhysical,
                            
                            type.plSoftVolumeIntersect,
                            type.plEAXListenerMod,
                            type.plPhysicalSndGroup,
                            type.plSeekPointMod,
                            type.plRailCameraMod,
                            type.plLayerAnimation,
                            type.plATCAnim,
                            type.plAGMasterMod,
                            type.plPanicLinkRegion,
                            type.plLineFollowMod,
                            type.plMsgForwarder,
                            type.plAnimEventModifier,
                            type.plMultiStageBehMod,

                            type.plDynaFootMgr,
                            type.plResponderModifier, //crashes POD district of LiveBahroCaves, and minkCameras district of Minkata.
                            type.plSittingModifier,
                            type.plImageLibMod,
                            type.plLimitedDirLightInfo,
                            type.plAgeGlobalAnim,
                            type.plDynaPuddleMgr,
                            type.plWaveSet7,
                            type.plDynamicEnvMap,
                            
                            //version2
                            type.plSoftVolumeInvert,
                            
                            //personal
                            type.plDynaRippleMgr,
                            type.plLayerSDLAnimation,
                            
                            type.plMaintainersMarkerModifier,
                            type.plDistOpacityMod,
                            type.plMorphSequence,
                            type.plMorphDataSet,
                            type.plClothingItem,
                            type.plSharedMesh,
                            type.plLayerLinkAnimation,
                            
                            type.plEmoteAnim,
                            type.pfGUIDraggableMod,
                            type.pl2WayWinAudible,
                            type.plArmatureLODMod,
                            type.plClothingOutfit,
                            type.plClothingBase,
                            type.plArmatureEffectsMgr,
                            type.plAliasModifier,
                            type.plPrintShape,
                            
                        };
                        namestartswith = new String[]{
                            //"linkinpoint",
                            //"startpoint",
                            //"cratercentral",
                            //"tower",
                            //"outer",
                            //"centertotem",
                            //"ground", //ground
                            //"criticalcave", //crater around caves
                            //"soccer",
                            
                            //"crespring",
                            //"csfxresp",
                            //"respdisablejc",
                            //"respdrnowedge",
                            //"respjconeshot",
                            //"resplinkout",
                            //"RespNglnWedge",
                            //"RespPayiWedge",
                            //"RespSolutionSymbols",
                            //"RespTetsWedge",
                            //"RespWedges", //pod problem.
                            
                            //"cRespExcludeRgn", //minkata problem.
                            //"cRespSfxLinkIn",
                        };
                        break;

                }
                
                for(int i=0;i<nameequals.length;i++)
                {
                    if(name.toLowerCase().equals(nameequals[i].toLowerCase())) return true;
                }
                for(int i=0;i<namestartswith.length;i++)
                {
                    if(name.toLowerCase().startsWith(namestartswith[i].toLowerCase())) return true;
                }
                for(int i=0;i<typeequals.length;i++)
                {
                    if(type==typeequals[i]) return true;
                }
                
                return useObject;
        }
    }*/
    
    public static Typeid[] moulReadable={
        Typeid.plSceneNode,
        Typeid.plSceneObject,
        Typeid.plMipMap,
        Typeid.plCubicEnvironMap,
        Typeid.plLayer,
        Typeid.hsGMaterial,
        Typeid.plParticleSystem,
        Typeid.plBoundInterface,
        Typeid.plAudioInterface,
        Typeid.plWinAudio,
        Typeid.plCoordinateInterface,
        Typeid.plSpawnModifier,
        Typeid.plDirectionalLightInfo,
        Typeid.plOmniLightInfo,
        Typeid.plDrawInterface,
        Typeid.plDrawableSpans,
        Typeid.plAGModifier,
        Typeid.plLayerAnimation,
        Typeid.plATCAnim,
        Typeid.plAGMasterMod,
        Typeid.plShadowCaster,
        Typeid.plPointShadowMaster,
        Typeid.plDirectShadowMaster,
        Typeid.plPythonFileMod,
        Typeid.plSimulationInterface,
        Typeid.plViewFaceModifier,
        Typeid.plSittingModifier,
        Typeid.plStereizer,
        Typeid.plSoundBuffer,
        Typeid.plRandomSoundMod,
        Typeid.plWin32StreamingSound,
        Typeid.plWin32StaticSound,
        Typeid.plParticleLocalWind,
        Typeid.plParticleCollisionEffectDie,
        Typeid.plExcludeRegionModifier,
        Typeid.plCameraBrain1,
        Typeid.plCameraBrain1_Avatar,
        Typeid.plCameraBrain1_Fixed,
        Typeid.plCameraBrain1_Circle,
        Typeid.plCameraModifier1,
        Typeid.plOccluder,
        Typeid.plDynamicTextMap,
        Typeid.plParticleCollisionEffectBounce,
        Typeid.plSpotLightInfo,
        Typeid.plRelevanceRegion,
        Typeid.plSoftVolumeSimple,
        Typeid.plResponderModifier,
        Typeid.plParticleFlockEffect,
        Typeid.plFadeOpacityMod,
        Typeid.plClusterGroup,
        Typeid.plVisRegion,
        Typeid.plSoftVolumeUnion,
        Typeid.plObjectInVolumeDetector,
        Typeid.plObjectInBoxConditionalObject,
        Typeid.plInterfaceInfoModifier,
        Typeid.plVolumeSensorConditionalObject,
        Typeid.plLogicModifier,
        Typeid.plActivatorConditionalObject,
        Typeid.plFacingConditionalObject,
        Typeid.plOneShotMod,
        Typeid.plAvLadderMod,
        Typeid.plDynaFootMgr,
        Typeid.plPickingDetector,
        Typeid.plCameraRegionDetector,
        Typeid.plHKPhysical, //crashes ahnonay
        Typeid.plSoftVolumeIntersect,
        Typeid.plEAXListenerMod,
        Typeid.plPhysicalSndGroup,
        Typeid.plSeekPointMod,
        Typeid.plRailCameraMod,
        Typeid.plPanicLinkRegion,
        Typeid.plLineFollowMod,
        Typeid.plMsgForwarder,
        Typeid.plAnimEventModifier,
        Typeid.plMultiStageBehMod,
        Typeid.plImageLibMod,
        Typeid.plLimitedDirLightInfo,
        Typeid.plAgeGlobalAnim,
        Typeid.plDynaPuddleMgr,
        Typeid.plWaveSet7,
        Typeid.plDynamicEnvMap,
        Typeid.plRidingAnimatedPhysicalDetector,
        Typeid.plGrassShaderMod,
        Typeid.plDynamicCamMap,
        Typeid.plSoftVolumeInvert,
        Typeid.plLayerBink,
        
        Typeid.plPostEffectMod,
        Typeid.plSoundVolumeApplicator,
        Typeid.plSimSuppressMsg,
        Typeid.plPostEffectMod,
        Typeid.plAxisAnimModifier,
        Typeid.pfGUIDialogMod,
        Typeid.pfGUIButtonMod,
        
        Typeid.plMobileOccluder,
        Typeid.plLayerLinkAnimation,
        Typeid.plArmatureMod,
        Typeid.plArmatureEffectsMgr,
        Typeid.plFilterCoordInterface,
        
        Typeid.plParticleFollowSystemEffect,
        Typeid.plParticleCollisionEffectBeat,
        Typeid.plParticleFadeVolumeEffect,
        
        Typeid.pfGUIKnobCtrl,
        
        Typeid.plDynaRippleMgr,
        Typeid.plLayerSDLAnimation,
        
        Typeid.pfGUIDragBarCtrl,
        
        Typeid.plMaintainersMarkerModifier,
        Typeid.plDistOpacityMod,
        Typeid.plMorphSequence,
        Typeid.plMorphDataSet,
        Typeid.plClothingItem,
        Typeid.plSharedMesh,
        
        Typeid.plEmoteAnim,
        Typeid.pfGUIDraggableMod,
        Typeid.pl2WayWinAudible,
        Typeid.plArmatureLODMod,
        Typeid.plClothingOutfit,
        Typeid.plClothingBase,
        Typeid.plAliasModifier,
        Typeid.plPrintShape,
        
        Typeid.plObjectInVolumeAndFacingDetector,
        Typeid.plNPCSpawnMod,
        Typeid.plDynaRippleVSMgr,
        Typeid.plSwimDetector,
        Typeid.plSwimRegionInterface,
        Typeid.plSwimCircularCurrentRegion,
        Typeid.plSwimStraightCurrentRegion,
        Typeid.plVolumeSensorConditionalObjectNoArbitration,

        Typeid.plSubworldRegionDetector,

        Typeid.plParticleUniformWind,
        //Typeid.plCameraBrain1_FirstPerson,
        
    };
    
    public static Typeid[] crowReadable = moulReadable;
    
    public static void convertEoaToWhatdoyousee(String infile, String outfile)
    {
        Bytes encryptedData = FileUtils.ReadFileAsBytes(infile);
        Bytes decryptedData = UruCrypt.DecryptEoa(encryptedData);
        Bytes wdysData = UruCrypt.EncryptWhatdoyousee(decryptedData);
        FileUtils.WriteFile(outfile, wdysData);
    }
    
    /*public static void processPrp(prpfile prp, String agename, HashMap<String, String> agenames, String outfolder, String infolder)
    {
        String newagename = agenames.get(agename);
        String finalname = newagename;
        if(finalname==null) finalname = agename;
        
        if(shared.State.AllStates.getStateAsBoolean("automateMystV"))
        {
        }
        if(true) //attempt to rename animation in GlobalAnimations
        {
        }
        
        if(true) //attempts to fix the invisible minkata craters.
        {
        }

        if(AllStates.getStateAsBoolean("removeLadders"))
        {
        }
        
        if(AllStates.getStateAsBoolean("translateSmartseeks"))
        {
        }
        if(AllStates.getStateAsBoolean("changeVerySpecialPython"))
        {
        }
        if(AllStates.getStateAsBoolean("makePlLayersWireframe"))
        {
        }
        if(AllStates.getStateAsBoolean("removeDynamicCamMap"))
        {
        }

    }*/
    /*public static void removeDynamicCamMapsFromMaterials(prpfile prp)
    {
        PrpRootObject[] layers = prputils.FindAllObjectsOfType(prp, Typeid.plLayer);
        for(PrpRootObject layer2: layers)
        {
            uru.moulprp.x0006Layer layer = layer2.castTo();
            if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plDynamicCamMap)
            {
                //found it!
                m.msg("Removing DynamicCamMap from layerRefs.");
                //mat.layerrefs.remove(layerref);
                //layer.flags5 = layer.flags5 | 0x1; //wireframe! //misc
                //layer.flags1 |= 0x80; //kBlendNoColor //blend //makes it invisible?
                //layer.opacity = Flt.zero();
                //layer.specular = new Rgba(0x3f800000,0x3f800000,0x3f800000,0x3f800000);
                //layer.flags3 |= 0x80; //kShadeSpecular //shade
            }
        }
        PrpRootObject[] mats = prputils.FindAllObjectsOfType(prp, Typeid.hsGMaterial);
        for(PrpRootObject mat2: mats)
        {
            uru.moulprp.x0007Material mat = mat2.castTo();
            for(Uruobjectref layerref: mat.layerrefs)
            {
                PrpRootObject layer2 = prputils.findObjectWithDesc(prp, layerref.xdesc);
                if(layer2.getObject() instanceof uru.moulprp.x0006Layer)
                {
                    uru.moulprp.x0006Layer layer = layer2.castTo();
                    if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plDynamicCamMap)
                    {
                        //found it!
                        m.msg("Removing DynamicCamMap from layerRefs.");
                        mat.layerrefs.remove(layerref);
                    }
                }
            }
        }
        mats = prputils.FindAllObjectsOfType(prp, Typeid.hsGMaterial);
        for(PrpRootObject mat2: mats)
        {
            uru.moulprp.x0007Material mat = mat2.castTo();
            for(Uruobjectref layerref: mat.maplayerrefs)
            {
                PrpRootObject layer2 = prputils.findObjectWithDesc(prp, layerref.xdesc);
                uru.moulprp.x0006Layer layer = layer2.castTo();
                if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plDynamicCamMap)
                {
                    //found it!
                    m.warn("Removing DynamicCamMap from mapLayerRefs.(untested.)");
                    mat.maplayerrefs.remove(layerref);
                }
            }
        }
    }*/
    
    
    
    public static void readPotsPrp(String filename)
    {
        byte[] filedata = FileUtils.ReadFile(filename);
        context c = context.createFromBytestream(new Bytestream(filedata));
        c.curFile = filename;
        prpobjects.prpprocess.ProcessAllObjects(c, false);
    }
    public static void readPotsPrps(String infolder, Vector<String> files)
    {
        for(String file: files)
        {
            String filename = infolder + "/dat/" + file;
            //open prp file and process it.
            byte[] filedata = FileUtils.ReadFile(filename);

            //do work.
            context c = context.createFromBytestream(new Bytestream(filedata));
            //c.readversion = version;
            c.curFile = file;
            prpobjects.prpprocess.ProcessAllObjects(c,false);
            //if(version==3) prputils.ProcessPotsPrp(filedata);
        }
    }
    public static void readAllPotsPrps(String prpdirname)
    {
        File prpfolder = new File(prpdirname+"/dat/");
        if (!prpfolder.isDirectory() || !prpfolder.exists())
        {
            m.err("Prp directory not in proper format or not found.");
            return;
        }
        
        File[] files = prpfolder.listFiles();
        m.msg("Parsing files... count=",Integer.toString(files.length));
        for(int i=0;i<files.length;i++)
        {
            File curfile = files[i];
            if(curfile.getName().toLowerCase().endsWith(".prp"))
            {
                m.msg(curfile.getName());
                //open prp file and process it.
                byte[] filedata = FileUtils.ReadFile(curfile);
                
                //do work.
                context c = context.createFromBytestream(new Bytestream(filedata));
                //c.readversion = version;
                c.curFile = curfile.getName();
                prpobjects.prpprocess.ProcessAllObjects(c,false);
                //if(version==3) prputils.ProcessPotsPrp(filedata);
                //if(version==6) prputils.ProcessAll(filedata);
            }
        }
        
        m.msg("Finished Processing all files.");
        
    }
    
    public static void readAllPrpsFromAllGames(String potsdir, String crowdir, String myst5dir, String mouldir)
    {
        Typeid[] alltypes = {
            Typeid.hsGMaterial, //large
            Typeid.pfGUIButtonMod,
            Typeid.pfGUIDialogMod,
            Typeid.pfObjectFlocker,
            Typeid.plAGAnimBink,
            Typeid.plAGMasterMod,
            Typeid.plAGModifier, //has embedded
            Typeid.plATCAnim, //has embedded
            Typeid.plActivatorConditionalObject,
            Typeid.plActivatorMsg,
            Typeid.plAgeGlobalAnim, //has embedded
            Typeid.plAnimCmdMsg,
            Typeid.plAnimEventModifier,
            Typeid.plAnimPath,
            Typeid.plArmatureEffectStateMsg,
            Typeid.plAudioInterface,
            Typeid.plAvLadderMod,
            Typeid.plAxisAnimModifier,
            Typeid.plBoundInterface,
            Typeid.plBubbleShaderMod,
            Typeid.plCameraBrain1,
            Typeid.plCameraBrain1_Avatar,
            Typeid.plCameraBrain1_Circle,
            Typeid.plCameraBrain1_Fixed,
            Typeid.plCameraBrainUru,
            Typeid.plCameraBrainUru_Fixed,
            Typeid.plCameraModifier,
            Typeid.plCameraModifier1,
            Typeid.plCameraMsg,
            Typeid.plCameraRegionDetector,
            Typeid.plClusterGroup, //large
            Typeid.plCompoundController,
            Typeid.plCompoundPosController,
            Typeid.plCompoundRotController,
            Typeid.plConvexIsect,
            Typeid.plCoordinateInterface,
            Typeid.plCubicEnvironMap, //large
            Typeid.plDirectMusicSound,
            Typeid.plDirectShadowMaster,
            Typeid.plDirectionalLightInfo,
            Typeid.plDrawInterface,
            Typeid.plDrawableSpans, //large
            Typeid.plDynaFootMgr,
            Typeid.plDynaPuddleMgr,
            Typeid.plDynamicCamMap,
            Typeid.plDynamicEnvMap,
            Typeid.plDynamicTextMap,
            Typeid.plEAXListenerMod,
            Typeid.plEAXReverbEffect,
            Typeid.plEnableMsg,
            Typeid.plEventCallbackMsg,
            Typeid.plExcludeRegionModifier,
            Typeid.plExcludeRegionMsg,
            Typeid.plFacingConditionalObject,
            Typeid.plFadeOpacityMod,
            Typeid.plGenRefMsg,
            Typeid.plGrassShaderMod,
            
            Typeid.plHKPhysical, //large
            Typeid.plImageLibMod,
            Typeid.plInterfaceInfoModifier,
            Typeid.plLayer,
            Typeid.plLayerAnimation,
            Typeid.plLayerBink,
            Typeid.plLeafController,
            Typeid.plLightDiffuseApplicator,
            Typeid.plLightSpecularApplicator,
            Typeid.plLimitedDirLightInfo,
            Typeid.plLineFollowMod,
            Typeid.plLinkToAgeMsg,
            Typeid.plLogicModifier,
            
            Typeid.plMatrix44Controller,
            Typeid.plMatrixChannelApplicator,
            Typeid.plMatrixControllerChannel,
            Typeid.plMipMap, //large
            Typeid.plMsgForwarder,
            Typeid.plMultiStageBehMod,
            Typeid.plNodeRegionModifier,
            Typeid.plNotifyMsg,
            Typeid.plObjectInBoxConditionalObject,
            Typeid.plObjectInVolumeDetector,
            Typeid.plOccluder,
            Typeid.plOmniLightInfo,
            Typeid.plOmniSqApplicator,
            Typeid.plOneShotMod,
            Typeid.plOneShotMsg,
            Typeid.plOneTimeParticleGenerator,
            Typeid.plPanicLinkRegion,
            Typeid.plParticleCollisionEffectBounce,
            Typeid.plParticleCollisionEffectDie,
            Typeid.plParticleEmitter,
            Typeid.plParticleFlockEffect,
            Typeid.plParticleLocalWind,
            Typeid.plParticlePPSApplicator,
            Typeid.plParticleSystem,
            Typeid.plPhysicalSndGroup,
            Typeid.plPickingDetector,
            Typeid.plPointControllerChannel,
            Typeid.plPointShadowMaster,
            Typeid.plPostEffectMod,
            Typeid.plPythonFileMod,
            
            Typeid.plRailCameraMod,
            Typeid.plRandomSoundMod,
            Typeid.plRefMsg,
            Typeid.plRelativeMatrixChannelApplicator,
            Typeid.plRelevanceRegion,
            Typeid.plResponderEnableMsg,
            Typeid.plResponderModifier,
            Typeid.plRideAnimatedPhysMsg,
            Typeid.plRidingAnimatedPhysicalDetector,
            Typeid.plScalarController,
            Typeid.plScalarControllerChannel,
            Typeid.plSceneNode,
            Typeid.plSceneObject,
            Typeid.plSeekPointMod,
            Typeid.plShadowCaster,
            Typeid.plSimSuppressMsg,
            Typeid.plSimpleParticleGenerator,
            Typeid.plSimplePosController,
            Typeid.plSimpleRotController,
            Typeid.plSimpleScaleController,
            Typeid.plSimulationInterface,
            Typeid.plSittingModifier,
            Typeid.plSoftVolumeIntersect,
            Typeid.plSoftVolumeInvert,
            Typeid.plSoftVolumeSimple,
            Typeid.plSoftVolumeUnion,
            Typeid.plSoundBuffer,
            Typeid.plSoundMsg,
            Typeid.plSoundVolumeApplicator,
            Typeid.plSpaceTree,
            Typeid.plSpawnMod,
            Typeid.plSpawnModifier,
            Typeid.plSpotLightInfo,
            Typeid.plStereizer,
            Typeid.plSubWorldMsg,
            
            Typeid.plTMController,
            Typeid.plTimerCallbackMsg,
            Typeid.plViewFaceModifier,
            Typeid.plVisRegion,
            Typeid.plVolumeIsect,
            Typeid.plVolumeSensorConditionalObject,
            Typeid.plWaveSet7,
            Typeid.plWin32StaticSound,
            Typeid.plWin32StreamingSound,
            Typeid.plWinAudio,
            
        };
        Typeid[] potstypes = {};
        Typeid[] myst5types = {};
        Typeid[] moultypes = {};
        boolean dopots = shared.State.AllStates.getStateAsBoolean("readAllFromPots");
        boolean docrow = shared.State.AllStates.getStateAsBoolean("readAllFromCrowthistle");
        boolean domyst5 = shared.State.AllStates.getStateAsBoolean("readAllFromMystv");
        boolean domoul = shared.State.AllStates.getStateAsBoolean("readAllFromMoul");
        
        
        //Typeid[] crowtypes = {}; //this should just be the myst5types.
        String[] dirs = {potsdir,crowdir,myst5dir,mouldir};
        
        Typeid[] potstypes2 = new Typeid[alltypes.length+potstypes.length];
        int i=0;
        for(int j=0;j<alltypes.length;j++) { potstypes2[i] = alltypes[j]; i++; }
        for(int j=0;j<potstypes.length;j++) { potstypes2[i] = potstypes[j]; i++; }
        
        Typeid[] myst5types2 = new Typeid[alltypes.length+myst5types.length];
        i=0;
        for(int j=0;j<alltypes.length;j++) { myst5types2[i] = alltypes[j]; i++; }
        for(int j=0;j<myst5types.length;j++) { myst5types2[i] = myst5types[j]; i++; }

        Typeid[] moultypes2 = new Typeid[alltypes.length+moultypes.length];
        i=0;
        for(int j=0;j<alltypes.length;j++) { moultypes2[i] = alltypes[j]; i++; }
        for(int j=0;j<moultypes.length;j++) { moultypes2[i] = moultypes[j]; i++; }
        /*class fileinfo
        {
            File file;
        }*/
        
        //get a list of all the prp files.
        //Vector<File> allfiles = new Vector<File>();
        for(int dircount = 0;dircount<dirs.length;dircount++)
        {
            String dir = dirs[dircount];
            
            Typeid[] readable = null;
            if(dircount==0)
            {
                readable = potstypes2;
                if(!dopots) continue;
            }
            else if(dircount==1)
            {
                readable = myst5types2;
                if(!docrow) continue;
            }
            else if(dircount==2)
            {
                readable = myst5types2;
                if(!domyst5) continue;
            }
            else if(dircount==3)
            {
                readable = moultypes2;
                if(!domoul) continue;
            }
            else
            {
                m.err("Programming error.");
                return;
            }
            
            File prpfolder = new File(dir+"/dat/");
            if (!prpfolder.isDirectory() || !prpfolder.exists())
            {
                m.err("Prp directory not in proper format or not found: ",dir);
                return;
            }
            File[] files = prpfolder.listFiles();
            for(File curfile: files)
            {
                if(curfile.getName().toLowerCase().endsWith(".prp"))
                {
                    //process file...
                    //allfiles.add(file);
                    
                    //open prp file and process it.
                    byte[] filedata = FileUtils.ReadFile(curfile);

                    //do work.
                    context c = context.createFromBytestream(new Bytestream(filedata));
                    //c.readversion = version;
                    c.curFile = curfile.getPath();
                    //uru.moulprp.prpprocess.ProcessAllObjects(c);
                    prpfile prp = prpfile.createFromContext(c, readable);
                }
            }
        }
        //m.msg("Parsing files... count="+Integer.toString(allfiles.size()));
        
        
    }
}

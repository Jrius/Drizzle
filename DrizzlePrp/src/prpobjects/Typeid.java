/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.m;
import shared.b;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

/**
 *
 * @author user
 */
//this is a class I made myself, to encapsulate changing object type ids.
//public class Typeid
public enum Typeid implements compilable, java.io.Serializable
{
    //starting bunch.
    plSceneNode,
    plSceneObject,
    plMipMap,
    plCubicEnvironMap,
    plLayer,
    hsGMaterial,
    plParticleSystem,
    plScalarController,
    plSimplePosController,
    plParticleEmitter,
    plSimpleParticleGenerator,
    plBoundInterface,
    plPythonFileMod,
    plAudioInterface,
    plWinAudio,
    plCoordinateInterface,
    plDrawInterface,
    plSpawnModifier,
    plDrawableSpans,
    plSpaceTree,
    plDirectionalLightInfo,
    plOmniLightInfo,
    plHKPhysical, //plPXPhysical in Moul, plODEPhysical in Crowthistle,Myst5
    plLayerAnimation,
    
    //the rest from eder delin.
    plSimulationInterface,
    plSoundBuffer,
    plPickingDetector,
    plLogicModifier,
    plActivatorConditionalObject,
    plObjectInBoxConditionalObject,
    plFacingConditionalObject,
    plViewFaceModifier,
    plAGModifier,
    plAGMasterMod,
    plCameraRegionDetector,
    plLineFollowMod,
    plOneShotMod,
    plRandomSoundMod,
    plObjectInVolumeDetector,
    plResponderModifier,
    plWin32StreamingSound,
    plWin32StaticSound,
    plCameraBrain1,
    plCameraModifier1,
    plCameraBrain1_Avatar,
    plCameraBrain1_Fixed,
    plExcludeRegionModifier,
    plVolumeSensorConditionalObject,
    plMsgForwarder,
    plSittingModifier,
    plRailCameraMod,
    plMultiStageBehMod,
    plCameraBrain1_Circle,
    plAnimEventModifier,
    plParticleCollisionEffectDie,
    plInterfaceInfoModifier,
    plParticleLocalWind,
    plPointShadowMaster,
    plATCAnim,
    plPanicLinkRegion,
    plStereizer,
    
    //guild pubs
    plOccluder,
    plLimitedDirLightInfo,
    plSoftVolumeSimple,
    plSoftVolumeIntersect,
    plDynamicTextMap,
    plEAXListenerMod,
    plImageLibMod,
    plPhysicalSndGroup,
    
    //livebahrocaves
    plParticleCollisionEffectBounce,
    plVisRegion,
    
    //tetsonot
    plSpotLightInfo,
    plAvLadderMod,
    plDynaPuddleMgr,
    plWaveSet7,
    plDynamicEnvMap,
    
    //minkata
    plShadowCaster,
    plDirectShadowMaster,
    plDynaFootMgr,
    plRelevanceRegion, //used for .csv files
    plClusterGroup,
    
    //jalak
    plSeekPointMod,
    
    //negilahn
    plAgeGlobalAnim, //0x00F2
    plParticleFlockEffect, //0x0123
    
    //edertsogal
    plFadeOpacityMod, //0x012F
    
    plMatrix44Controller,
    plSoftVolumeInvert,
    plSoftVolumeUnion,
    plVolumeIsect, //2f0->2f5
    plConvexIsect, //2f5->2fa
    plExcludeRegionMsg,
    plSoundMsg,
    plActivatorMsg,
    plArmatureEffectStateMsg,
    plNotifyMsg,
    plRefMsg,
    plGenRefMsg,
    plCameraMsg,
    plOneShotMsg,
    plAnimCmdMsg,
    plTimerCallbackMsg,
    plEnableMsg,
    plLinkToAgeMsg,
    plResponderEnableMsg,
    plSubWorldMsg,
    plSimSuppressMsg,
    plEventCallbackMsg,
    plOneTimeParticleGenerator,
    plAnimPath,
    plCompoundController,
    plSimpleRotController,
    plCompoundRotController,
    plSimpleScaleController,
    plCompoundPosController,
    plTMController,
    plMatrixChannelApplicator,
    plMatrixControllerChannel,
    plParticlePPSApplicator,
    plScalarControllerChannel,
    plLightDiffuseApplicator,
    plPointControllerChannel,
    plLightSpecularApplicator,
    plOmniSqApplicator,
    plRidingAnimatedPhysicalDetector,
    plGrassShaderMod,
    plDynamicCamMap,
    plRideAnimatedPhysMsg,
    
    //crowthistle
    plRelativeMatrixChannelApplicator,
    plEAXReverbEffect,
    plCameraBrainUru,
    plCameraBrainUru_Fixed,
    plCameraModifier,
    plSpawnMod,
    
    //myst5 direbo
    pfObjectFlocker,
    plAGAnimBink,
    plBubbleShaderMod,
    plLayerBink,
    
    //myst5 kveer
    plDirectMusicSound,
    plNodeRegionModifier,
    plSoundVolumeApplicator,
    plPostEffectMod,
    plAxisAnimModifier,
    pfGUIDialogMod,
    pfGUIButtonMod,
    
    //myst5 descent
    plElevatorModifier,
    plPythonFileModConditionalObject,
    plConstAccelEaseCurve,
    plMobileOccluder,
    plLadderModifier,
    
    //myst5 laki
    plPiranhaRegionModifier,
    plCrossfade,
    plCrossfadeMsg,
    plWindBoneMod,
    plLayerLinkAnimation,
    plArmatureMod,
    plAvBrainPirahna,
    plAvBrainQuab, //in pots
    plArmatureEffectsMgr,
    plFilterCoordInterface,
    
    //siralehn
    plParticleFollowSystemEffect,
    plParticleCollisionEffectBeat,
    plParticleFadeVolumeEffect,
    
    //todelmer
    plParticleVelMinApplicator,
    pfGUIKnobCtrl,
    
    //moul personal
    plDynaRippleMgr,
    plLayerSDLAnimation,
    
    //ahnonay
    plDynaRippleVSMgr,
    plSwimDetector,
    plSwimRegionInterface,
    plSwimCircularCurrentRegion,
    plSwimStraightCurrentRegion,
    plObjectInVolumeAndFacingDetector,
    plNPCSpawnMod,
    
    //GUI_jalakControlPanel
    pfGUIDragBarCtrl,
    pfGUICheckBoxCtrl,
    pfGUIRadioGroupCtrl,
    pfGUIPopUpMenu,
    
    //GreatTreePub
    plMaintainersMarkerModifier,
    //Kirel
    plDistOpacityMod,
    //GlobalClothing
    plMorphSequence,
    plMorphDataSet,
    plClothingItem,
    plSharedMesh,
    
    //globalaaimations,city, customavatars
    plMatrixConstant,
    plEmoteAnim,
    pfGUIDraggableMod,
    pl2WayWinAudible,
    plArmatureLODMod,
    plAvBrainHuman,
    plClothingOutfit,
    plClothingBase,
    plAliasModifier,
    plPrintShape,
    
    //ahnonay
    plSwimMsg,
    plVolumeSensorConditionalObjectNoArbitration,

    //misc pots
    plInstanceDrawInterface,
    plLODMipmap,
    plSubworldRegionDetector,
    plPipeResMakeMsg,

    //misc moul
    plActivePrintShape,
    plCameraBrain1_FirstPerson,
    plClimbMsg,
    plParticleUniformWind,
    plSimpleRegionSensor, //garrison wall
    plWin32LinkSound, //Wha???? :P  Used in GlobalAvatars_District_Audio
    plArmatureEffectFootSound, //GlobalAvatars_District_Audio

    //hexisle
    plMovableMod,
    pfGUICreditsCtrl,
    plParticleBulletEffect,
    plParticleLifeMinApplicator,
    plParticleLifeMaxApplicator,
    plParticleAngleApplicator,
    plEventCallbackSetupMsg,

    //mqo
    pfGUITextBoxMod,
    plAvatarMgr, //mgr

    //myst5 bugfix
    plConvexVolume,

    //drizzle25
    plHKSubWorld,
    plCloneSpawnModifier,

    //TheArt new classes
    plCubicRenderTarget,
    plCubicRenderTargetModifier,

    //talcum
    plNetMsgLoadClone,
    plLoadAvatarMsg,
    plNetClientMgr, //mgr
    plNetMsgPlayerPage,
    plNetMsgTestAndSet,
    plNetMsgMembersListReq,
    plNetMsgGameStateRequest,
    plNetMsgRelevanceRegions,
    plNetMsgGameMessage,
    plAvatarInputStateMsg,
    plServerReplyMsg,
    plNetMsgMembersList,
    plInputIfaceMgrMsg,
    //plLoadCloneMsg,
    plNetMsgSDLState,
    plNetMsgInitialAgeStateSent,
    plLinkEffectsTriggerMsg,
    plLinkEffectsMgr, //mgr
    plClient, //mgr (seen as a receiver for a plMessage)
    plNetMsgPagingRoom,
    plNetMsgGroupOwner,
    plNetMsgSDLStateBCast,
    plClothingMsg,
    plAvTaskMsg,
    plNetMsgMemberUpdate,
    plAvOneShotLinkTask,
    //talcumproxy
    plAvAnimTask,
    

    plLeafController,
    nil,
    unknown,

    pfGUIEditBoxMod,
    pfGUIClickMapCtrl,
    pfGUIListBoxMod,
    pfGUIDynDisplayCtrl,
    pfGUIProgressCtrl,
    pfGUIMultiLineEditCtrl,
    pfGUISkin,
    pfGUIUpDownPairMod,
    plGameMarkerModifier,
    plAvBrainCritter,
    plParticleVelMaxApplicator,
    plVehicleModifier,
    plBlower,
    plDynaTorpedoMgr,
    plDynaBulletMgr,
    plDynaTorpedoVSMgr,
    plSplineEaseCurve,
    plParticleScaleMinApplicator,

    plDecalEnableMod,
    
    // more GUI from MV
    pfGUISketchCtrl,


    ;

    public void compile(Bytedeque deque)
    {
        //old:
        //short result = lookup(this);

        //new:
        short result = getAsShort(deque.format);

        //short result = (short)
        deque.writeShort(result);
    }

    public short getAsShort(IBytedeque c)
    {
        return getAsShort(c.format);
    }
    public short getAsShort(Format format)
    {
        triplet t2 = findTriByType(this);
        if(t2!=null)
        {
            short val;
            if(format==Format.pots) val = t2.pots;
            else if(format==Format.moul) val = t2.moul;
            else throw new shared.uncaughtexception("unimplemented");
            if(val!=-2)
            {
                return val;
            }
            else
            {
                m.warn("Typeid: Compilation problem: "+format.toString()+" isn't listed as having type: ",this.toString());
                return (short)0x8000;
                //throw new shared.uncaughtexception("unimplemented for this game");
            }
        }
        else
        {
            throw new shared.uncaughtexception("unimplemented for this class");
        }

    }

    
    //short type;
    
    //types enumvalue;
    //plSwimStraightCurrentRegion is the highest regular member of pots.
    
    //cc -> moul -> mv -> hexisle
    //This list is done with the assumption that all pots values are listed if present, but that is not necessarily true of the other two collumns.
    //8000 -> 8000 (null? it would mean they could just check the highest bit.) 
    //2d4 -> 2d9 plParticleEmitter
    //2d3 -> 2d8 plSimpleParticleGenerator
    //258 -> 25d plSpaceTree                  //258 -> 240 plSpaceTree
    //22b -> 230 plLeafController
    public static triplet[] triplets = {
        
        tri( 0x000, 0x000, 0x000, 0x000, plSceneNode ),
        tri( 0x001, 0x001, 0x001, 0x001, plSceneObject ),
        tri( 0x004, 0x004, 0x004, 0x004, plMipMap ),
        tri( 0x005, 0x005, 0x005, 0x005, plCubicEnvironMap ),
        tri( 0x006, 0x006, 0x006, 0x006, plLayer ),
        tri( 0x007, 0x007, 0x007, 0x007, hsGMaterial ),
        tri( 0x008, 0x008, 0x008, 0x008, plParticleSystem ),
        tri( 0x00C, 0x00C, 0x00C, -2, plBoundInterface ),
        tri( 0x011, 0x011, 0x011, 0x011, plAudioInterface ),
        tri( 0x014, 0x014, 0x014, 0x014, plWinAudio ), //plWinAudible, really
        tri( 0x015, 0x015, 0x015, 0x015, plCoordinateInterface ),
        tri( 0x016, 0x016, 0x016, 0x016, plDrawInterface ),
        tri( 0x01C, 0x01C, 0x01C, 0x01C, plSimulationInterface ),
        tri( 0x029, 0x029, 0x029, 0x029, plSoundBuffer ),
        tri( 0x02B, 0x02B, 0x02A, -2, plPickingDetector ), //3rd code from myst5
        tri( 0x02D, 0x02D, 0x02C, 0x02C, plLogicModifier ), //3rd code from myst5
        tri( 0x032, 0x032, 0x031, -2, plActivatorConditionalObject ), //3rd code from myst5
        tri( 0x037, 0x037, 0x036, -2, plObjectInBoxConditionalObject ), //3rd code from myst5
        tri( 0x03D, 0x03D, 0x03C, 0x03C, plSpawnModifier ),
        tri( 0x03E, 0x03E, 0x03D, -2, plFacingConditionalObject ), //3rd code from myst5
        tri( 0x03F, 0x03F, 0x103, 0x103, plHKPhysical ),
        tri( 0x040, 0x040, 0x03E, 0x03E, plViewFaceModifier ),
        tri( 0x043, 0x043, 0x040, 0x040, plLayerAnimation ),
        tri( 0x04C, 0x04C, 0x049, 0x049, plDrawableSpans ),
        tri( 0x055, 0x055, 0x050, 0x050, plDirectionalLightInfo ),
        tri( 0x056, 0x056, 0x051, 0x051, plOmniLightInfo ),
        tri( 0x057, 0x057, 0x052, 0x052, plSpotLightInfo ), //3rd code from myst5
        tri( 0x067, 0x067, 0x05A, 0x05A, plOccluder ),
        tri( 0x06A, 0x06A, -2, -2, plLimitedDirLightInfo ),
        tri( 0x06C, 0x06C, 0x05F, 0x05F, plAGModifier ),
        tri( 0x06D, 0x06D, 0x060, 0x060, plAGMasterMod ),
        tri( 0x06F, 0x06F, -2, -2, plCameraRegionDetector ),
        tri( 0x071, 0x071, 0x062, -2, plLineFollowMod ), //3rd code from myst5
        tri( 0x076, 0x076, -2, -2, plSeekPointMod ),
        tri( 0x077, 0x077, -2, 0x068, plOneShotMod ),
        tri( 0x079, 0x079, 0x06A, 0x06A, plRandomSoundMod ),
        tri( 0x07B, 0x07B, 0x06C, 0x06C, plObjectInVolumeDetector ),
        tri( 0x07C, 0x07C, 0x06D, 0x06D, plResponderModifier ),
        tri( 0x084, 0x084, 0x073, 0x073, plWin32StreamingSound ),
        tri( 0x088, 0x088, 0x076, 0x076, plSoftVolumeSimple ),
        tri( 0x08A, 0x08A, 0x078, -2, plSoftVolumeUnion ),
        tri( 0x08B, 0x08B, 0x079, -2, plSoftVolumeIntersect ),
        tri( 0x08C, 0x08C, 0x07A, -2, plSoftVolumeInvert ),
        tri( 0x096, 0x096, 0x07E, 0x07E, plWin32StaticSound ),
        tri( 0x099, 0x099, -2, -2, plCameraBrain1 ),
        tri( 0x09B, 0x09B, -2, -2, plCameraModifier1 ),
        tri( 0x09E, 0x09E, -2, -2, plCameraBrain1_Avatar ),
        tri( 0x09F, 0x09F, -2, -2, plCameraBrain1_Fixed ),
        tri( 0x0A2, 0x0A2, 0x088, 0x088, plPythonFileMod ),
        tri( 0x0A4, 0x0A4, 0x08A, -2, plExcludeRegionModifier ), //3rd code from myst5
        tri( 0x0A6, 0x0A6, 0x08C, 0x08C, plVolumeSensorConditionalObject ), //3rd code from myst5
        tri( 0x0A8, 0x0A8, 0x08E, 0x08E, plMsgForwarder ),
        tri( 0x0AD, 0x0AD, 0x093, 0x093, plDynamicTextMap ),
        tri( 0x0AE, 0x0AE, -2, -2, plSittingModifier ),
        tri( 0x0B2, 0x0B2, -2, -2, plAvLadderMod ),
        tri( 0x0C0, 0x0C0, -2, -2, plRailCameraMod ),
        tri( 0x0C1, 0x0C1, -2, -2, plMultiStageBehMod ),
        tri( 0x0C2, 0x0C2, -2, -2, plCameraBrain1_Circle ),
        tri( 0x0C4, 0x0C4, 0x0A5, 0x0A5, plAnimEventModifier ), //3rd code from myst5
        tri( 0x0C9, 0x0C9, 0x0AA, -2, plParticleCollisionEffectDie ),
        tri( 0x0CA, 0x0CA, 0x0AB, -2, plParticleCollisionEffectBounce ), //3rd code from myst5
        tri( 0x0CB, 0x0CB, 0x0AC, 0x0AC, plInterfaceInfoModifier ), //3rd code from myst5
        tri( 0x0D0, 0x0D0, 0x0B0, -2, plParticleLocalWind ),
        tri( 0x0D4, 0x0D4, 0x0B4, 0x0B4, plShadowCaster ),
        tri( 0x0D5, 0x0D5, 0x0B5, 0x0B5, plPointShadowMaster ), //3rd code from myst5
        tri( 0x0D6, 0x0D6, 0x0B6, -2, plDirectShadowMaster ),
        tri( 0x0E5, 0x0E5, -2, -2, plEAXListenerMod ),
        tri( 0x0E7, 0x0E7, -2, -2, plObjectInVolumeAndFacingDetector),
        tri( 0x0E8, 0x0E8, -2, -2, plDynaFootMgr ),
        tri( 0x0ED, 0x0ED, -2, -2, plDynaPuddleMgr ),
        tri( 0x0F1, 0x0F1, 0x0CD, 0x0CD, plATCAnim ),
        tri( 0x0F2, 0x0F2, -2, -2, plAgeGlobalAnim ),
        tri( 0x0F5, 0x0F5, -2, -2, plNPCSpawnMod),
        tri( 0x0FB, 0x0FB, 0x0D4, 0x0D4, plWaveSet7 ), //3rd code from myst5
        tri( 0x0FC, 0x0FC, -2, 0x0D5, plPanicLinkRegion ),
        tri( 0x0FF, 0x0FF, 0x0D8, -2, plStereizer ),
        tri( 0x106, 0x106, 0x0DA, 0x0DA, plDynamicEnvMap ),
        tri( 0x10A, 0x10A, -2, -2, plDynaRippleVSMgr),
        tri( 0x116, 0x116, 0x0E9, 0x0E9, plVisRegion ),
        tri( 0x11E, 0x11E, -2, -2, plRelevanceRegion ), //used for .csv files
        tri( 0x122, 0x122, 0x0F1, -2, plImageLibMod ),
        tri( 0x123, 0x123, 0x0F2, -2, plParticleFlockEffect ), //3rd code from myst5
        tri( 0x127, 0x127, -2, -2, plPhysicalSndGroup ),
        tri( 0x12B, 0x12B, -2, -2, plClusterGroup ),
        tri( 0x12E, 0x12E, -2, -2, plSwimDetector),
        tri( 0x12F, 0x12F, -2, -2, plFadeOpacityMod ),
        tri( 0x133, 0x133, -2, -2, plSwimRegionInterface),
        tri( 0x134, 0x134, -2, -2, plSwimCircularCurrentRegion),
        tri( 0x136, 0x136, -2, -2, plSwimStraightCurrentRegion),

        
        tri( 0x203, 0x203, -2, -2, plRefMsg ),
        tri( 0x204, 0x204, -2, -2, plGenRefMsg ),
        tri( 0x206, 0x206, 0x204, 0x204, plAnimCmdMsg ),
        tri( 0x20A, 0x20A, 0x206, 0x206, plCameraMsg ),
        tri( 0x219, 0x219, -2, -2, plActivatorMsg ),
        tri( 0x22B, 0x230, 0x21A, 0x21A, plLeafController ),
        tri( -2, 0x231, 0x21B, 0x21B, plCompoundController ), //moul-only
        tri( 0x22F, -2, -2, -2, plScalarController ), //pots-only
        tri( 0x234, -2, -2, -2, plMatrix44Controller ), //pots-only
        tri( 0x236, -2, -2, -2, plSimpleScaleController ), //pots-only
        tri( 0x237, -2, -2, -2, plSimpleRotController ), //pots-only
        tri( 0x238, -2, -2, -2, plCompoundRotController ), //pots-only
        tri( 0x239, -2, -2, -2, plSimplePosController ), //pots-only
        tri( 0x23A, -2, -2, -2, plCompoundPosController ), //pots-only
        tri( 0x23B, -2, -2, -2, plTMController ), //pots-only
        tri( 0x24A, 0x24F, 0x232, 0x232, plTimerCallbackMsg ), //3rd code from myst5
        tri( 0x24B, 0x250, 0x233, 0x233, plEventCallbackMsg ),
        tri( 0x24F, 0x254, 0x237, 0x237, plEnableMsg ), //3rd code from myst5
        tri( 0x255, 0x25A, 0x23D, 0x23D, plSoundMsg ), //3rd code from myst5
        tri( 0x258, 0x25D, 0x240, 0x240, plSpaceTree ),
        tri( 0x2D3, 0x2d8, 0x261, 0x261, plSimpleParticleGenerator ),
        tri( 0x2D4, 0x2d9, 0x262, 0x262, plParticleEmitter ),
        tri( 0x2E1, 0x2E6, 0x26E, -2, plLinkToAgeMsg ), //3rd code from myst5
        tri( 0x2E6, 0x2EB, 0x271, -2, plAnimPath ), //3rd code from myst5
        tri( 0x2E8, 0x2ED, 0x272, 0x272, plNotifyMsg ),
        tri( 0x2F0, 0x2F5, -2, -2, plVolumeIsect ),
        tri( 0x2F5, 0x2FA, 0x27C, 0x27C, plConvexIsect ),
        tri( 0x2FD, 0x302, 0x283, -2, plResponderEnableMsg ),
        tri( 0x302, 0x307, -2, 0x285, plOneShotMsg ),
        tri( 0x330, 0x335, 0x2A2, -2, plExcludeRegionMsg ), //3rd code from myst5
        tri( 0x331, 0x336, 0x2A3, -2, plOneTimeParticleGenerator ), //3rd code from myst5
        tri( 0x38E, 0x393, -2, -2, plArmatureEffectStateMsg ),
        tri( 0x3BA, 0x3BF, -2, -2, plSubWorldMsg ),
        
        tri( 0x309, 0x30E, 0x28A, 0x28A, plMatrixChannelApplicator ),
        tri( 0x2D9, 0x2DE, 0x267, 0x267, plMatrixControllerChannel ),
        tri( 0x335, 0x33A, 0x2A7, 0x2A7, plParticlePPSApplicator ), //3rd code from myst5
        tri( 0x313, 0x318, 0x294, 0x294, plScalarControllerChannel ), //3rd code from myst5
        tri( 0x30B, 0x310, 0x28C, -2, plLightDiffuseApplicator ), //3rd code from myst5
        tri( 0x306, 0x30B, 0x287, -2, plPointControllerChannel ), //3rd code from myst5
        tri( 0x30D, 0x312, -2, -2, plLightSpecularApplicator ),
        tri( -2, 0x22A, -2, -2, plOmniSqApplicator ), //moul-only
        tri( -2, 0x13A, -2, -2, plRidingAnimatedPhysicalDetector ), //moul-only
        tri( -2, 0x138, 0x23, -2, plGrassShaderMod ), //moul-only
        tri( -2, 0x139, 0x111, -2, plDynamicCamMap ), //moul-only
        tri( -2, 0x49D, -2, -2, plRideAnimatedPhysMsg ), //moul-only
        
        tri( -2, -2, 0x338, -2, plRelativeMatrixChannelApplicator ), //not in pots. //start of crowthistle section.
        tri( -2, -2, 0x0C1, -2, plEAXReverbEffect ), //not in pots.
        tri( -2, -2, 0x081, 0x081, plCameraBrainUru ), //not in pots, but may correspond to plCameraBrain1
        tri( -2, -2, 0x086, 0x086, plCameraBrainUru_Fixed ), //not in pots, but may correspond to plCameraBrain1
        tri( -2, -2, 0x083, 0x083, plCameraModifier ), //not in pots, but may correspond to plCameraModifier1
        tri( -2, -2, 0x0D0, -2, plSpawnMod), //not in pots
        
        tri( -2, -2, 0x10E, -2, pfObjectFlocker), //not in pots //starts of myst5 section.
        tri( -2, -2, 0x110, -2, plAGAnimBink), //not in pots
        tri( -2, -2, 0x03B, -2, plBubbleShaderMod), //not in pots
        tri( 0x046, 0x046, 0x043, -2, plLayerBink),
        
        tri( -2, -2, 0x100, -2, plDirectMusicSound), //not in pots, but apparently plDirectMusicStream is.
        tri( -2, -2, 0x113, -2, plNodeRegionModifier), //not in pots
        tri( 0x39F, 0x3A4, 0x2D0, -2, plSoundVolumeApplicator),
        tri( 0x45B, -2, 0x2EC, 0x2EC, plSimSuppressMsg),
        tri( 0x07A, 0x07A, 0x06B, 0x06B, plPostEffectMod),
        tri( 0x07D, -2, 0x06E, -2, plAxisAnimModifier),
        tri( 0x098, 0x098, 0x080, 0x080, pfGUIDialogMod),
        tri( 0x0A1, 0x0A1, 0x087, -2, pfGUIButtonMod),
        
        tri( -2, -2, 0x106, -2, plElevatorModifier), //not in pots
        tri( -2, -2, 0x026, -2, plPythonFileModConditionalObject), //not in pots
        tri( 0x31A, 0x31F, 0x299, -2, plConstAccelEaseCurve),
        tri( 0x068, -2, 0x05B, -2, plMobileOccluder),
        tri( -2, -2, 0x098, -2, plLadderModifier), //not in pots
        
        tri( -2, -2, 0x114, -2, plPiranhaRegionModifier), //not in pots (surprise!)
        tri( -2, -2, 0x01B, -2, plCrossfade), //not in pots
        tri( -2, -2, 0x20D, -2, plCrossfadeMsg), //not in pots
        tri( -2, -2, 0x021, -2, plWindBoneMod), //not in pots
        tri( 0x08E, 0x08E, 0x07C, -2, plLayerLinkAnimation),
        tri( 0x08F, 0x08F, 0x07D, -2, plArmatureMod),
        tri( -2, -2, 0x200, -2, plAvBrainPirahna), //not in pots
        tri( 0x494, -2, -2, -2, plAvBrainQuab),
        tri( 0x0CD, 0x0CD, 0x0AE, -2, plArmatureEffectsMgr),
        tri( 0x0FE, 0xFE, 0x0D7, -2, plFilterCoordInterface),
        
        tri( 0x135, 0x135, 0x101, 0x101, plParticleFollowSystemEffect),
        tri( 0x00A, 0x00A, 0x00A, -2, plParticleCollisionEffectBeat),
        tri( 0x00B, 0x00B, 0x00B, -2, plParticleFadeVolumeEffect),
        
        tri( 0x337, -2, 0x2A9, 0x2A9, plParticleVelMinApplicator), //not in pots.  oops, it is!
        tri( 0x0B1, 0x0B1, 0x097, -2, pfGUIKnobCtrl),
        
        tri( 0x0E9, 0x0E9, -2, -2, plDynaRippleMgr),
        tri( 0x0F0, 0x0F0, -2, -2, plLayerSDLAnimation),
        
        tri( 0x0B9, 0x0B9, -2, -2, pfGUIDragBarCtrl),
        tri( 0x0BA, 0x0BA, 0x9c, -2, pfGUICheckBoxCtrl),
        tri( 0x0BB, 0x0BB, -2, -2, pfGUIRadioGroupCtrl),
        tri( 0x119, 0x119, -2, -2, pfGUIPopUpMenu),
        
        tri( 0x10D, 0x10D, -2, -2, plMaintainersMarkerModifier),
        tri( 0x131, 0x131, -2, -2, plDistOpacityMod),
        tri( 0x108, 0x108, -2, -2, plMorphSequence),
        tri( 0x10F, 0x10F, -2, -2, plMorphDataSet),
        tri( 0x0B5, 0x0B5, -2, -2, plClothingItem),
        tri( 0x0CC, 0x0CC, -2, -2, plSharedMesh),
        
        tri( 0x32C, 0x331, -2, -2, plMatrixConstant),
        tri( 0x109, 0x109, -2, -2, plEmoteAnim),
        tri( 0x0A5, 0x0A5, -2, -2, pfGUIDraggableMod),
        tri( 0x053, 0x053, -2, -2, pl2WayWinAudible),
        tri( 0x095, 0x095, -2, -2, plArmatureLODMod),
        tri( 0x357, 0x35C, -2, -2, plAvBrainHuman),
        tri( 0x0B6, 0x0B6, -2, -2, plClothingOutfit),
        tri( 0x0B7, 0x0B7, -2, -2, plClothingBase),
        tri( 0x02A, -2, -2, -2, plAliasModifier),
        tri( 0x0EC, 0x0EC, -2, -2, plPrintShape),
                
        //tri( 0x456, 0x456, -2, -2, plSwimMsg),
        tri( 0x451, 0x456, -2, -2, plSwimMsg),
        tri( 0x44C, 0x451, -2, -2, plClimbMsg),
        tri( 0x456, -2, -2, -2, plPipeResMakeMsg),
        tri( -2, 0x13B, -2, -2, plVolumeSensorConditionalObjectNoArbitration), //not in pots

        tri( 0x0D2, 0x0D2, -2, -2, plInstanceDrawInterface),
        tri( 0x12D, -2, -2, -2, plLODMipmap),
        tri( 0x0F3, 0x0F3, -2, -2, plSubworldRegionDetector),
        tri( 0x0F6, 0x0F6, -2, -2, plActivePrintShape),
        tri( 0x0B3, 0x0B3, -2, -2, plCameraBrain1_FirstPerson),
        tri( 0x0D1, 0x0D1, -2, -2, plParticleUniformWind),
        tri( 0x107, 0x107, -2, -2, plSimpleRegionSensor),
        tri( 0x08D, 0x08D, -2, -2, plWin32LinkSound),
        tri( 0x0E4, 0x0E4, -2, -2, plArmatureEffectFootSound),
        
        tri( -2, -2, -2, 0x01B, plMovableMod), //not in pots  (used for movable platforms, in hexisle, I think.)
        tri( -2, -2, -2, 0x056, pfGUICreditsCtrl), //not in pots
        tri( -2, -2, -2, 0x0DF, plParticleBulletEffect), //not in pots
        tri( 0x333, -2, -2, 0x2A5, plParticleLifeMinApplicator),
        tri( 0x334, -2, -2, 0x2A6, plParticleLifeMaxApplicator),
        tri( 0x336, -2, -2, 0x2A8, plParticleAngleApplicator),
        tri( -2, -2, -2, 0x336, plEventCallbackSetupMsg), //not in pots

        tri( 0xAB, 0xAB, -2, -2, pfGUITextBoxMod), //not present in moul prps, but in mqo.
        tri( 0xF4, 0xF4, -2, -2, plAvatarMgr), //not present in moul prps, but in mqo.

        tri( 0x2D1, 0x2D6, 0x25F, -2, plConvexVolume),

        tri( 0x0E2, -2, -2, -2, plHKSubWorld),
        tri( 0x0B4, 0x0B4, -2, -2, plCloneSpawnModifier),

        //TheArt
        tri( 0x00E, 0x00E, -2, -2, plCubicRenderTarget),
        tri( 0x00F, 0x00F, -2, -2, plCubicRenderTargetModifier),


        //actually, these are tested with Moulagain, and may or may not match with Moul.
        tri( -2, 0x3B3, -2, -2, plNetMsgLoadClone),
        tri( -2, 0x3B1, -2, -2, plLoadAvatarMsg),
        tri( -2, 0x052, -2, -2, plNetClientMgr),
        tri( -2, 0x3B4, -2, -2, plNetMsgPlayerPage),
        tri( -2, 0x27D, -2, -2, plNetMsgTestAndSet),
        tri( -2, 0x2AD, -2, -2, plNetMsgMembersListReq),
        tri( -2, 0x265, -2, -2, plNetMsgGameStateRequest),
        tri( -2, 0x3AC, -2, -2, plNetMsgRelevanceRegions),
        tri( -2, 0x26B, -2, -2, plNetMsgGameMessage),
        tri( -2, 0x347, -2, -2, plAvatarInputStateMsg),
        tri( -2, 0x26F, -2, -2, plServerReplyMsg),
        tri( -2, 0x2AE, -2, -2, plNetMsgMembersList),
        tri( -2, 0x363, -2, -2, plInputIfaceMgrMsg),
        //tri( -2, -2, -2, -2, plLoadCloneMsg),
        tri( -2, 0x2CD, -2, -2, plNetMsgSDLState),
        tri( -2, 0x2B8, -2, -2, plNetMsgInitialAgeStateSent),
        tri( -2, 0x300, -2, -2, plLinkEffectsTriggerMsg),
        tri( -2, 0x083, -2, -2, plLinkEffectsMgr),
        tri( -2, 0x05B, -2, -2, plClient),
        tri( -2, 0x218, -2, -2, plNetMsgPagingRoom),
        tri( -2, 0x264, -2, -2, plNetMsgGroupOwner),
        tri( -2, 0x329, -2, -2, plNetMsgSDLStateBCast),
        tri( -2, 0x357, -2, -2, plClothingMsg),
        tri( -2, 0x298, -2, -2, plAvTaskMsg),
        tri( -2, 0x2B1, -2, -2, plNetMsgMemberUpdate),
        //tri( -2, 0x2CB, -2, -2, plNetMsgTerminated),
        tri( -2, 0x488, -2, -2, plAvOneShotLinkTask),
        tri( -2, 0x36B, -2, -2, plAvAnimTask),
        
        tri( 0x0AC, 0x0AC, -2, -2, pfGUIEditBoxMod),
        tri( 0x062, 0x062, 0x57, -2, pfGUIClickMapCtrl),
        tri( 0x0AA, 0x0AA, -2, -2, pfGUIListBoxMod),
        tri( 0x0BD, 0x0BD, 0x9e, -2, pfGUIDynDisplayCtrl),
        tri( 0x10C, 0x10C, 0xe0, -2, pfGUIProgressCtrl),
        tri( 0x0EE, 0x0EE, -2, -2, pfGUIMultiLineEditCtrl),
        tri( 0x0C6, 0x0C6, -2, -2, pfGUISkin),
        tri( 0x0AF, 0x0AF, -2, -2, pfGUIUpDownPairMod),
        tri( 0x12C, 0x12C, -2, -2, plGameMarkerModifier),
        tri( 0x358, 0x35D, -2, -2, plAvBrainCritter),
        tri( 0x338, -2, 0x2AA, 0x2AA, plParticleVelMaxApplicator), //BahroCave02, Todelmer
        tri( 0x0CF, -2, -2, -2, plVehicleModifier), //Cleft
        tri( 0x0A9, 0x0A9, -2, -2, plBlower), //Garden
        tri( 0x129, -2, -2, -2, plDynaTorpedoMgr), //Teledahn
        tri( 0x0EA, -2, -2, -2, plDynaBulletMgr), //Teledahn
        tri( 0x12A, -2, -2, -2, plDynaTorpedoVSMgr), //Teledahn
        tri( 0x31B, -2, -2, -2, plSplineEaseCurve), //Teledahn
        tri( 0x339, -2, -2, -2, plParticleScaleMinApplicator), //Teledahn
        tri( 0x0EB, 0x0EB, -2, -2, plDecalEnableMod), //3dsmax
        
        // added: things for MV slates (just for debugging purpose, cannot be converted to anything anyway...)
        tri( -2, -2, 0x094, -2, pfGUISketchCtrl),

        tri( 0x8000, 0x8000, 0x8000, 0x8000, nil ),
        //cc -> moul -> mv -> hexisle
    };
    
    
    public static int noval = -2;
    public static class triplet
    {
        Typeid type;
        short pots;
        short moul;
        short mv;
        short hexisle;
        
        public triplet(short pots2, short moul2, short mv2, short hexisle2, Typeid type2)
        {
            type = type2;
            pots = pots2;
            moul = moul2;
            mv = mv2;
            hexisle = hexisle2;
        }
    }
    public static triplet tri(int pots2, int moul2, int mv2, int hexisle2, Typeid type2)
    {
        return new triplet((short)pots2,(short)moul2,(short)mv2,(short)hexisle2,type2);
    }
    public static boolean validateTriplets()
    {
        boolean valid = true;
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            for(int j=i+1;j<num;j++)
            {
                triplet t1 = triplets[i];
                triplet t2 = triplets[j];
                if(t1.type==t2.type) valid = false;
                if(t1.moul==t2.moul && t1.moul!=-2) valid = false;
                if(t1.pots==t2.pots && t1.pots!=-2) valid = false;
                if(t1.mv==t2.mv && t1.mv!=-2) valid = false;
            }
        }
        if(!valid) m.err("Triplets are not valid!");
        return valid;
    }
    public static triplet findTriByPots(short pots)
    {
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            if(triplets[i].pots==pots)
            {
                return triplets[i];
            }
        }
        return null;
    }
    public static triplet findTriByMoul(short moul)
    {
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            if(triplets[i].moul==moul)
            {
                return triplets[i];
            }
        }
        return null;
    }
    public static triplet findTriByMv(short mv)
    {
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            if(triplets[i].mv==mv)
            {
                return triplets[i];
            }
        }
        return null;
    }
    public static triplet findTriByHexisle(short hexisle)
    {
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            if(triplets[i].hexisle==hexisle)
            {
                return triplets[i];
            }
        }
        return null;
    }
    public static triplet findTriByType(Typeid type)
    {
        int num = triplets.length;
        for(int i=0;i<num;i++)
        {
            if(triplets[i].type==type)
            {
                return triplets[i];
            }
        }
        return null;
    }
    public static Typeid Read(context c) //throws readexception
    {
        Typeid result = ReadEvenIfUnknown(c);
        /*if(result==Typeid.unknown)
        {
            throw new readexception("Typeid: unknown type: (see previous message for details.)");
        }
        else
        {
            return result;
        }*/
        return result;
    }
    public static Typeid ReadEvenIfUnknown(context c)
    {
        short type = c.in.readShort();
        //if((c.readversion==3) && (type>0x56)) m.err("Unhandled pots typeid read.");
        if(c.readversion==6)
        {
            triplet t = findTriByMoul(type);
            if(t!=null)
            {
                if(t.type==Typeid.plAvatarMgr)
                {
                    int dummy=0;
                    m.warn("encountered ref to plAvatarMgr");
                }
                return t.type;
            }
            else
            {
                m.err("Moul isn't listed as having type: 0x",Integer.toHexString(b.Int16ToInt32(type)));
                return unknown;
                //throw new readexception("Typeid: unknown type:0x"+Integer.toHexString(b.Int16ToInt32(type)));
            }
            /*switch(type)
            {
                case 0x0000: return plSceneNode;
                case 0x0001: return plSceneObject;
                case 0x0004: return plMipMap;
                case 0x0005: return plCubicEnvironMap;
                case 0x0006: return plLayer;
                case 0x0007: return hsGMaterial;
                case 0x0008: return plParticleSystem;
                case 0x000C: return plBoundInterface; //unsure
                case 0x0011: return plAudioInterface;
                case 0x0014: return plWinAudio;
                case 0x0015: return plCoordinateInterface;
                case 0x0016: return plDrawInterface;
                case 0x003D: return plSpawnModifier;
                case 0x003F: return plHKPhysical;
                case 0x0043: return plLayerAnimation;
                case 0x004C: return plDrawableSpans;
                case 0x0055: return plDirectionalLightInfo;
                case 0x0056: return plOmniLightInfo;
                case 0x00A2: return plPythonFileMod;

                case 0x001C: return plSimulationInterface;
                case 0x0029: return plSoundBuffer;
                case 0x002B: return plPickingDetector;
                case 0x002D: return plLogicModifier;
                case 0x0032: return plActivatorConditionalObject;
                case 0x0037: return plObjectInBoxConditionalObject;
                case 0x003E: return plFacingConditionalObject;
                case 0x0040: return plViewFaceModifier;
                case 0x006C: return plAGModifier;
                case 0x006D: return plAGMasterMod;
                case 0x006F: return plCameraRegionDetector;
                case 0x0071: return plLineFollowMod;
                case 0x0077: return plOneShotMod;
                case 0x0079: return plRandomSoundMod;
                case 0x007B: return plObjectInVolumeDetector;
                case 0x007C: return plResponderModifier;
                case 0x0084: return plWin32StreamingSound;
                case 0x0096: return plWin32StaticSound;
                case 0x0099: return plCameraBrain1;
                case 0x009B: return plCameraModifier1;
                case 0x009E: return plCameraBrain1_Avatar;
                case 0x009F: return plCameraBrain1_Fixed;
                case 0x00A4: return plExcludeRegionModifier;
                case 0x00A6: return plVolumeSensorConditionalObject;
                case 0x00A8: return plMsgForwarder;
                case 0x00AE: return plSittingModifier;
                case 0x00C0: return plRailCameraMod;
                case 0x00C1: return plMultiStageBehMod;
                case 0x00C2: return plCameraBrain1_Circle;
                case 0x00C4: return plAnimEventModifier;
                case 0x00C9: return plParticleCollisionEffectDie;
                case 0x00CB: return plInterfaceInfoModifier;
                case 0x00D0: return plParticleLocalWind;
                case 0x00D5: return plPointShadowMaster;
                case 0x00F1: return plATCAnim;
                case 0x00FC: return plPanicLinkRegion;
                case 0x00FF: return plStereizer;

                case 0x0067: return plOccluder;
                case 0x006A: return plLimitedDirLightInfo;
                case 0x0088: return plSoftVolumeSimple;
                case 0x008B: return plSoftVolumeIntersect;
                case 0x00AD: return plDynamicTextMap;
                case 0x00E5: return plEAXListenerMod;
                case 0x0122: return plImageLibMod;
                case 0x0127: return plPhysicalSndGroup;

                case 0x00CA: return plParticleCollisionEffectBounce;
                case 0x0116: return plVisRegion;
                
                case 0x0057: return plSpotLightInfo;
                case 0x00B2: return plAvLadderMod;
                case 0x00ED: return plDynaPuddleMgr;
                case 0x00FB: return plWaveSet7;
                case 0x0106: return plDynamicEnvMap;
                
                case 0x00D4: return plShadowCaster;
                case 0x00D6: return plDirectShadowMaster;
                case 0x00E8: return plDynaFootMgr;
                case 0x011E: return plRelevanceRegion; //used for .csv files
                case 0x012B: return plClusterGroup;
                
                case 0x0076: return plSeekPointMod;
                
                case 0x00F2: return plAgeGlobalAnim;
                case 0x0123: return plParticleFlockEffect;
                case 0x012F: return plFadeOpacityMod;
                
                case 0x008A: return plSoftVolumeUnion;
                case 0x008C: return plSoftVolumeInvert;
                
                case 0x0230: return plLeafController;
                case 0x025D: return plSpaceTree;
                case 0x02d9: return plParticleEmitter;
                case 0x02d8: return plSimpleParticleGenerator;
                case 0x02F5: return plVolumeIsect;
                case 0x02FA: return plConvexIsect;
                
                case 0x0203: return plRefMsg;
                case 0x0204: return plGenRefMsg;
                case 0x0206: return plAnimCmdMsg;
                case 0x020A: return plCameraMsg;
                case 0x0219: return plActivatorMsg;
                case 0x024F: return plTimerCallbackMsg;
                case 0x0250: return plEventCallbackMsg;
                case 0x0254: return plEnableMsg;
                case 0x025A: return plSoundMsg;
                case 0x02E6: return plLinkToAgeMsg;
                case 0x02ED: return plNotifyMsg;
                case 0x0302: return plResponderEnableMsg;
                case 0x0307: return plOneShotMsg;
                case 0x0335: return plExcludeRegionMsg;
                case 0x0393: return plArmatureEffectStateMsg;
                case 0x03BF: return plSubWorldMsg;
                //plSimSuppressMsg,
                case 0x0336: return plOneTimeParticleGenerator;
                case 0x02EB: return plAnimPath;
                case 0x0231: return plCompoundController;
                
                case (short)0x8000:
                    return nil;
                default:
                    m.msg("Typeid: unknown type:0x"+Integer.toHexString(b.Int16ToInt32(type)));
                    return unknown;
            }*/
        }
        else if(c.readversion==3)
        {
            triplet t = findTriByPots(type);
            if(t!=null)
            {
                return t.type;
            }
            else
            {
                m.err("Pots isn't listed as having type: 0x",Integer.toHexString(b.Int16ToInt32(type)));
                return unknown;
            }
            /*switch(type)
            {
                case 0x0000: return plSceneNode;
                case 0x0001: return plSceneObject;
                case 0x0004: return plMipMap;
                case 0x0005: return plCubicEnvironMap;
                case 0x0006: return plLayer;
                case 0x0007: return hsGMaterial;
                case 0x0008: return plParticleSystem;
                case 0x000C: return plBoundInterface; //unsure
                case 0x0011: return plAudioInterface;
                case 0x0014: return plWinAudio;
                case 0x0015: return plCoordinateInterface;
                case 0x0016: return plDrawInterface;
                case 0x003D: return plSpawnModifier;
                case 0x003F: return plHKPhysical;
                case 0x0043: return plLayerAnimation;
                case 0x004C: return plDrawableSpans;
                case 0x0055: return plDirectionalLightInfo;
                case 0x0056: return plOmniLightInfo;
                case 0x00A2: return plPythonFileMod;

                case 0x001C: return plSimulationInterface;
                case 0x0029: return plSoundBuffer;
                case 0x002B: return plPickingDetector;
                case 0x002D: return plLogicModifier;
                case 0x0032: return plActivatorConditionalObject;
                case 0x0037: return plObjectInBoxConditionalObject;
                case 0x003E: return plFacingConditionalObject;
                case 0x0040: return plViewFaceModifier;
                case 0x006C: return plAGModifier;
                case 0x006D: return plAGMasterMod;
                case 0x006F: return plCameraRegionDetector;
                case 0x0071: return plLineFollowMod;
                case 0x0077: return plOneShotMod;
                case 0x0079: return plRandomSoundMod;
                case 0x007B: return plObjectInVolumeDetector;
                case 0x007C: return plResponderModifier;
                case 0x0084: return plWin32StreamingSound;
                case 0x0096: return plWin32StaticSound;
                case 0x0099: return plCameraBrain1;
                case 0x009B: return plCameraModifier1;
                case 0x009E: return plCameraBrain1_Avatar;
                case 0x009F: return plCameraBrain1_Fixed;
                case 0x00A4: return plExcludeRegionModifier;
                case 0x00A6: return plVolumeSensorConditionalObject;
                case 0x00A8: return plMsgForwarder;
                case 0x00AE: return plSittingModifier;
                case 0x00C0: return plRailCameraMod;
                case 0x00C1: return plMultiStageBehMod;
                case 0x00C2: return plCameraBrain1_Circle;
                case 0x00C4: return plAnimEventModifier;
                case 0x00C9: return plParticleCollisionEffectDie;
                case 0x00CB: return plInterfaceInfoModifier;
                case 0x00D0: return plParticleLocalWind;
                case 0x00D5: return plPointShadowMaster;
                case 0x00F1: return plATCAnim;
                case 0x00FC: return plPanicLinkRegion;
                case 0x00FF: return plStereizer;
                
                case 0x0067: return plOccluder;
                case 0x006A: return plLimitedDirLightInfo;
                case 0x0088: return plSoftVolumeSimple;
                case 0x008B: return plSoftVolumeIntersect;
                case 0x00AD: return plDynamicTextMap;
                case 0x00E5: return plEAXListenerMod;
                case 0x0122: return plImageLibMod;
                case 0x0127: return plPhysicalSndGroup;

                case 0x00CA: return plParticleCollisionEffectBounce;
                case 0x0116: return plVisRegion;

                case 0x0057: return plSpotLightInfo;
                case 0x00B2: return plAvLadderMod;
                case 0x00ED: return plDynaPuddleMgr;
                case 0x00FB: return plWaveSet7;
                case 0x0106: return plDynamicEnvMap;

                case 0x00D4: return plShadowCaster;
                case 0x00D6: return plDirectShadowMaster;
                case 0x00E8: return plDynaFootMgr;
                case 0x011E: return plRelevanceRegion; //used for .csv files
                case 0x012B: return plClusterGroup;

                case 0x0076: return plSeekPointMod;

                case 0x00F2: return plAgeGlobalAnim;
                case 0x0123: return plParticleFlockEffect;
                case 0x012F: return plFadeOpacityMod;

                case 0x008A: return plSoftVolumeUnion;
                case 0x008C: return plSoftVolumeInvert;

                case 0x022b: return plLeafController;
                case 0x02d4: return plParticleEmitter;
                case 0x02d3: return plSimpleParticleGenerator;
                case 0x0258: return plSpaceTree;
                case 0x022F: return plScalarController;
                case 0x0239: return plSimplePosController;
                case 0x0234: return plMatrix44Controller;
                case 0x02F0: return plVolumeIsect;
                case 0x02F5: return plConvexIsect;

                case 0x0203: return plRefMsg;
                case 0x0204: return plGenRefMsg;
                case 0x0206: return plAnimCmdMsg;
                case 0x020A: return plCameraMsg;
                case 0x0219: return plActivatorMsg;
                case 0x024A: return plTimerCallbackMsg;
                case 0x024F: return plEnableMsg;
                case 0x0255: return plSoundMsg;
                case 0x02E1: return plLinkToAgeMsg;
                case 0x02E8: return plNotifyMsg;
                case 0x02FD: return plResponderEnableMsg;
                case 0x0302: return plOneShotMsg;
                case 0x0330: return plExcludeRegionMsg;
                case 0x038E: return plArmatureEffectStateMsg;
                case 0x03BA: return plSubWorldMsg;
                case 0x045B: return plSimSuppressMsg;
                case 0x0331: return plOneTimeParticleGenerator;
                case 0x024B: return plEventCallbackMsg;
                case 0x02E6: return plAnimPath;
                case 0x0237: return plSimpleRotController;
                case 0x0238: return plCompoundRotController;
                case 0x0236: return plSimpleScaleController;
                case 0x023A: return plCompoundPosController;
                case 0x023B: return plTMController;

                case (short)0x8000:
                    return nil;
                default:
                    m.msg("Typeid: unknown type:0x"+Integer.toHexString(b.Int16ToInt32(type)));
                    return unknown;
            }*/
        }
        else if(c.readversion==4)
        {
            triplet t = findTriByMv(type);
            if(t!=null)
            {
                return t.type;
            }
            else
            {
                m.err("Myst5 isn't listed as having type: 0x",Integer.toHexString(b.Int16ToInt32(type)));
                return unknown;
            }
        }
        else if(c.readversion==7) //hexisle
        {
            /*if(type==-1)
            {
                return nil;
            }*/
            triplet t = findTriByHexisle(type);
            if(t!=null)
            {
                return t.type;
            }
            else
            {
                m.err("HexIsle isn't listed as having type: 0x",Integer.toHexString(b.Int16ToInt32(type)));
                return unknown;
            }
        }
        m.err("Unknown readversion in Typeid");
        return unknown;
    }
    
    //these values should be for version 3 (pots)
    private static short lookup(Typeid t)
    {
        triplet t2 = findTriByType(t);
        if(t2!=null)
        {
            if(t2.pots!=-2)
            {
                return t2.pots;
            }
            else
            {
                m.warn("Typeid: Compilation problem: Pots isn't listed as having type: ",t.toString());
                return (short)0x8000;
            }
        }
        else
        {
            m.err("Typeid: Compilation error(you shouldn't see this): ",t.toString());
            return (short)0x8000;
        }
        /*switch(t)
        {
            case plSceneNode: return 0x0000;
            case plSceneObject: return 0x0001;
            case plMipMap: return 0x0004;
            case plCubicEnvironMap: return 0x0005;
            case plLayer: return 0x0006;
            case hsGMaterial: return 0x0007;
            case plParticleSystem: return 0x0008;
            case plBoundInterface: return 0x000C; //unsure
            case plAudioInterface: return 0x0011;
            case plWinAudio: return 0x0014;
            case plCoordinateInterface: return 0x0015;
            case plDrawInterface: return 0x0016;
            case plSpawnModifier: return 0x003D;
            case plHKPhysical: return 0x003F;
            case plLayerAnimation: return 0x0043;
            case plDrawableSpans: return 0x004C;
            case plDirectionalLightInfo: return 0x0055;
            case plOmniLightInfo: return 0x0056;
            case plPythonFileMod: return 0x00A2;
                
            case plSimulationInterface: return 0x001C;
            case plSoundBuffer: return 0x0029;
            case plPickingDetector: return 0x002B;
            case plLogicModifier: return 0x002D;
            case plActivatorConditionalObject: return 0x0032;
            case plObjectInBoxConditionalObject: return 0x0037;
            case plFacingConditionalObject: return 0x003E;
            case plViewFaceModifier: return 0x0040;
            case plAGModifier: return 0x006C;
            case plAGMasterMod: return 0x006D;
            case plCameraRegionDetector: return 0x006F;
            case plLineFollowMod: return 0x0071;
            case plOneShotMod: return 0x0077;
            case plRandomSoundMod: return 0x0079;
            case plObjectInVolumeDetector: return 0x007B;
            case plResponderModifier: return 0x007C;
            case plWin32StreamingSound: return 0x0084;
            case plWin32StaticSound: return 0x0096;
            case plCameraBrain1: return 0x0099;
            case plCameraModifier1: return 0x009B;
            case plCameraBrain1_Avatar: return 0x009E;
            case plCameraBrain1_Fixed: return 0x009F;
            case plExcludeRegionModifier: return 0x00A4;
            case plVolumeSensorConditionalObject: return 0x00A6;
            case plMsgForwarder: return 0x00A8;
            case plSittingModifier: return 0x00AE;
            case plRailCameraMod: return 0x00C0;
            case plMultiStageBehMod: return 0x00C1;
            case plCameraBrain1_Circle: return 0x00C2;
            case plAnimEventModifier: return 0x00C4;
            case plParticleCollisionEffectDie: return 0x00C9;
            case plInterfaceInfoModifier: return 0x00CB;
            case plParticleLocalWind: return 0x00D0;
            case plPointShadowMaster: return 0x00D5;
            case plATCAnim: return 0x00F1;
            case plPanicLinkRegion: return 0x00FC;
            case plStereizer: return 0x00FF;

            case plOccluder: return 0x0067;
            case plLimitedDirLightInfo: return 0x006A;
            case plSoftVolumeSimple: return 0x0088;
            case plSoftVolumeIntersect: return 0x008B;
            case plDynamicTextMap: return 0x00AD;
            case plEAXListenerMod: return 0x00E5;
            case plImageLibMod: return 0x0122;
            case plPhysicalSndGroup: return 0x0127;

            case plParticleCollisionEffectBounce: return 0x00CA;
            case plVisRegion: return 0x0116;

            case plSpotLightInfo: return 0x0057;
            case plAvLadderMod: return 0x00B2;
            case plDynaPuddleMgr: return 0x00ED;
            case plWaveSet7: return 0x00FB;
            case plDynamicEnvMap: return 0x0106;

            case plShadowCaster: return 0x00D4;
            case plDirectShadowMaster: return 0x00D6;
            case plDynaFootMgr: return 0x00E8;
            case plRelevanceRegion: return 0x011E;  //used for .csv files
            case plClusterGroup: return 0x012B;

            case plSeekPointMod: return 0x0076;

            case plAgeGlobalAnim: return 0x00F2;
            case plParticleFlockEffect: return 0x0123;
            case plFadeOpacityMod: return 0x012F;

            case plSoftVolumeUnion: return 0x008A;
            case plSoftVolumeInvert: return 0x008C;
            
            case plLeafController: return 0x022b;
            case plSpaceTree: return 0x0258;
            case plParticleEmitter: return 0x02d4; //return 0x02d9;
            case plSimpleParticleGenerator: return 0x02d3; //return 0x02d8;
            case plVolumeIsect: return 0x02F0;
            case plConvexIsect: return 0x02F5;
            case plExcludeRegionMsg: return 0x0335;
            case plRefMsg: return 0x0203;
            case plGenRefMsg: return 0x0204;
            case plAnimCmdMsg: return 0x0206;
            case plCameraMsg: return 0x020A;
            case plActivatorMsg: return 0x0219;
            case plTimerCallbackMsg: return 0x024A;
            case plEnableMsg: return 0x024F;
            case plSoundMsg: return 0x0255;
            case plLinkToAgeMsg: return 0x02E1;
            case plNotifyMsg: return 0x02E8;
            case plResponderEnableMsg: return 0x02FD;
            case plOneShotMsg: return 0x0302;
            case plArmatureEffectStateMsg: return 0x038E;
            case plSubWorldMsg: return 0x03BA;
            case plScalarController: return 0x022F;
            case plSimplePosController: return 0x0239;
            case plMatrix44Controller: return 0x0234;
            case plAnimPath: return 0x02E6;
            case plTMController: return 0x023B;
            
            case nil: return (short)0x8000;
                
            case unknown:
            default:
                m.msg("Typeid: Compilation error.: "+t.toString());
                return (short)0x8000;
        }*/
    }
    /*public types getType()
    {
        return enumvalue;
    }*/
    /*public boolean equals(types t)
    {
        return t == enumvalue;
    }*/
    public void addXml(StringBuilder s)
    {
        s.append(this.toString());
    }
    public static Typeid createFromXml(Element e1)
    {
        String name = e1.getTextContent();
        Typeid result = java.lang.Enum.valueOf(Typeid.class, name);
        return result;
    }

    public Class<uruobj> findClass()
    {
        try{
            String classname = PrpObject.objpackage+"."+this.toString();
            Class plasmaClass = Class.forName(classname);
            Class<uruobj> plasmaClass2 = (Class<uruobj>)plasmaClass;
            return plasmaClass2;
        }catch(Exception e){
            throw new shared.nested(e);
        }
    }
}

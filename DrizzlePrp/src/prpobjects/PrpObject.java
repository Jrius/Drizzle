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

import uru.context;
import uru.Bytedeque;
import shared.readexception;
import shared.m;

public class PrpObject extends uruobj
{
    public uruobj object;
    static final String objpackage = plSceneObject.class.getPackage().getName();

    
    public uruobj getObject(context c, Typeid type) throws readexception
    {
        switch(type)
        {
            case plSceneNode:
                return new x0000Scenenode(c);
            case plSceneObject:
                return new plSceneObject(c);
            //case 0x0002:
            //    return new x0002Keyedobject(stream,true);
            case plMipMap:
                return new x0004MipMap(c);
            case plCubicEnvironMap:
                return new x0005Environmap(c);
            case plLayer:
                return new x0006Layer(c);
            case hsGMaterial:
                return new x0007Material(c);
            case plParticleSystem:
                return new plParticleSystem(c);
            case plBoundInterface:
                return new x000CBoundInterface(c);
            case plAudioInterface:
                return new x0011AudioInterface(c);
            case plWinAudio:
                return new plWinAudible(c);
            case plCoordinateInterface:
                return new plCoordinateInterface(c);
            //case plDrawInterface:
            //    return new plDrawInterface(c);
            case plSpawnModifier:
                return new x003DSpawnModifier(c);
            case plDrawableSpans:
                return new plDrawableSpans(c);
            case plDirectionalLightInfo:
                return new x0055DirectionalLightInfo(c);
            case plOmniLightInfo:
                return new x0056OmniLightInfo(c);
            //case plLayerAnimation:
            //    return new x0043LayerAnimation(stream,true);
            //case 0x0027:
            //    return new x0027MultiModifier(stream,true);
            //case 0x0028:
            //    return new x0028Synchedobject(stream,true);
            case plPythonFileMod:
                return new plPythonFileMod(c);
            case plPointShadowMaster:
                return new x00D5PointShadowMaster(c);
            case plSimulationInterface:
                return new plSimulationInterface(c);
            case plHKPhysical:
                return new plHKPhysical(c);
            case plViewFaceModifier:
                return new plViewFaceModifier(c);
            case plSittingModifier: //haven't
                return new x00AESittingModifier(c);
            case plStereizer:
                return new x00FFStereizer(c);
            case plSoundBuffer:
                return new plSoundBuffer(c);
            case plRandomSoundMod:
                return new plRandomSoundMod(c);
            case plWin32StreamingSound:
                return new x0084Win32StreamingSound(c);
            case plWin32StaticSound:
                return new x0096Win32StaticSound(c);
            case plParticleLocalWind:
                return new x00D0ParticleLocalWind(c);
            case plParticleCollisionEffectDie:
                return new x00C9ParticleCollisionEffectDie(c);
            case plExcludeRegionModifier:
                return new x00A4ExcludeRegionModifier(c);
            case plCameraBrain1:
                return new plCameraBrain1(c);
            case plCameraBrain1_Avatar:
                return new plCameraBrain1_Avatar(c);
            case plCameraBrain1_Fixed:
                return new plCameraBrain1_Fixed(c);
            case plCameraBrain1_Circle:
                return new plCameraBrain1_Circle(c);
            case plCameraModifier1:
                return new x009BCameraModifier1(c);
            case plRailCameraMod:
                return new plRailCameraMod(c);
            case plAGMasterMod:
                return new plAGMasterMod(c);
            case plAGModifier:
                return new plAGModifier(c);
            case plOccluder:
                return new plOccluder(c);
            //case plLimitedDirLightInfo:
            //    return new PlLimitedDirLightInfo(stream,true);
            case plDynamicTextMap:
                return new plDynamicTextMap(c);
            case plParticleCollisionEffectBounce:
                return new plParticleCollisionEffectBounce(c);
            case plSpotLightInfo:
                return new x0057SpotLightInfo(c);
            case plShadowCaster:
                return new plShadowCaster(c);
            case plDirectShadowMaster:
                return new plDirectShadowMaster(c);
            case plRelevanceRegion:
                return new plRelevanceRegion(c);
            case plSoftVolumeSimple:
                return new plSoftVolumeSimple(c);
            case plResponderModifier:
                return new plResponderModifier(c);
            case plParticleFlockEffect:
                return new plParticleFlockEffect(c);
            case plFadeOpacityMod:
                return new plFadeOpacityMod(c);
            case plClusterGroup:
                return new plClusterGroup(c);
            case plVisRegion:
                return new plVisRegion(c);
            case plSoftVolumeUnion:
                return new plSoftVolumeUnion(c);
            case plObjectInVolumeDetector:
                return new plObjectInVolumeDetector(c);
            case plObjectInBoxConditionalObject:
                return new plObjectInBoxConditionalObject(c);
            case plInterfaceInfoModifier:
                return new plInterfaceInfoModifier(c);
            case plVolumeSensorConditionalObject:
                return new plVolumeSensorConditionalObject(c);
            case plLogicModifier:
                return new plLogicModifier(c);
            case plActivatorConditionalObject:
                return new plActivatorConditionalObject(c);
            case plFacingConditionalObject:
                return new plFacingConditionalObject(c);
            case plOneShotMod:
                return new plOneShotMod(c);
            case plAvLadderMod:
                return new plAvLadderMod(c);
            case plDynaFootMgr:
                return new plDynaFootMgr(c);
            case plPickingDetector:
                return new plPickingDetector(c);
            case plCameraRegionDetector:
                return new plCameraRegionDetector(c);
                
                
                
            //case plSceneNode: //really used as a null here.
            //    break;
            case plExcludeRegionMsg:
                return new PrpMessage.PlExcludeRegionMsg(c);
            case plSoundMsg:
                return new PrpMessage.PlSoundMsg(c);
            case plEnableMsg:
                return new PrpMessage.PlEnableMsg(c);
            case plNotifyMsg:
                return new PrpMessage.PlNotifyMsg(c);
            case plOneShotMsg:
                return new PrpMessage.PlOneShotMsg(c);
            case plArmatureEffectStateMsg:
                return new PrpMessage.PlArmatureEffectStateMsg(c);
            case plLinkToAgeMsg:
                return new PrpMessage.PlLinkToAgeMsg(c);
            case plAnimCmdMsg:
                return new PrpMessage.PlAnimCmdMsg(c);
            case plTimerCallbackMsg:
                return new PrpMessage.PlTimerCallbackMsg(c);
            case plEventCallbackMsg:
                return new PrpMessage.PlEventCallbackMsg(c);
            case plCameraMsg:
                return new PrpMessage.PlCameraMsg(c);

            case plParticleEmitter:
                return new plParticleSystem.plParticleEmitter(c);
            case plOneTimeParticleGenerator:
                return new plParticleSystem.plOneTimeParticleGenerator(c);
            case plSimpleParticleGenerator:
                return new plParticleSystem.plSimpleParticleGenerator(c);
            case plSoftVolumeIntersect:
                return new plSoftVolumeIntersect(c);
            case plEAXListenerMod:
                return new plEAXListenerMod(c);
            case plPhysicalSndGroup:
                return new plPhysicalSndGroup(c);
            case plSeekPointMod:
                return new plSeekPointMod(c);
            case plAnimPath:
                return new plRailCameraMod.plAnimPath(c);
            case plCompoundController:
                return new PrpController.plCompoundController(c);
            case plLeafController:
                return new PrpController.plLeafController(c);
            case plLayerAnimation:
                return new plLayerAnimation(c);
            case plScalarController:
                return new PrpController.plScalarController(c);
            case plMatrix44Controller:
                return new PrpController.plMatrix44Controller(c);
            case plSimplePosController:
                return new PrpController.plSimplePosController(c);
            case plCompoundPosController:
                return new PrpController.plCompoundPosController(c);
            case plSimpleRotController:
                return new PrpController.plSimpleRotController(c);
            case plCompoundRotController:
                return new PrpController.plCompoundRotController(c);
            case plSimpleScaleController:
                return new PrpController.plSimpleScaleController(c);
            case plATCAnim:
                return new plATCAnim(c);
            case plMatrixChannelApplicator:
                return new plAGAnim.plMatrixChannelApplicator(c);
            case plMatrixControllerChannel:
                return new plAGAnim.plMatrixControllerChannel(c);
            case plParticlePPSApplicator:
                return new plAGAnim.plParticlePPSApplicator(c);
            case plScalarControllerChannel:
                return new plAGAnim.plScalarControllerChannel(c);
            case plTMController:
                return new PrpController.plTMController(c);
            case plPanicLinkRegion:
                return new plPanicLinkRegion(c);
            case plLineFollowMod:
                return new plLineFollowMod(c);
            case plMsgForwarder:
                return new plMsgForwarder(c);
            case plAnimEventModifier:
                return new plAnimEventModifier(c);
            case plMultiStageBehMod:
                return new plMultiStageBehMod(c);
            case plLightDiffuseApplicator:
                return new EmbeddedClasses.PlLightDiffuseApplicator(c);
            case plPointControllerChannel:
                return new EmbeddedClasses.PlPointControllerChannel(c);
            case plImageLibMod:
                return new plImageLibMod(c);
            case plLimitedDirLightInfo:
                return new plLimitedDirLightInfo(c);
            case plAgeGlobalAnim:
                return new plAgeGlobalAnim(c);
            case plLightSpecularApplicator:
                return new EmbeddedClasses.PlLightSpecularApplicator(c);
            case plDynaPuddleMgr:
                return new plDynaPuddleMgr(c);
            case plWaveSet7:
                return new plWaveSet7(c);
            case plDynamicEnvMap:
                return new plDynamicEnvMap(c);
            case plOmniSqApplicator:
                return new EmbeddedClasses.PlOmniSqApplicator(c);
            case plRidingAnimatedPhysicalDetector:
                return new plRidingAnimatedPhysicalDetector(c);
            case plGrassShaderMod:
                return new plGrassShaderMod(c);
            case plDynamicCamMap:
                return new plDynamicCamMap(c);
            case plRideAnimatedPhysMsg:
                return new PrpMessage.PlRideAnimatedPhysMsg(c);
            case plSoftVolumeInvert:
                return new plSoftVolumeInvert(c);
            case plLayerBink:
                return new plLayerBink(c);
            case plRelativeMatrixChannelApplicator:
                return new plAGAnim.plRelativeMatrixChannelApplicator(c);
            case plSoundVolumeApplicator:
                return new EmbeddedClasses.PlSoundVolumeApplicator(c);
            case plPostEffectMod:
                return new plPostEffectMod(c);
            case plSimSuppressMsg:
                return new PrpMessage.PlSimSuppressMsg(c);
            case plAxisAnimModifier:
                return new plAxisAnimModifier(c);
            case pfGUIButtonMod:
                return new pfGUIButtonMod(c);
            case pfGUIDialogMod:
                return new pfGUIButtonMod.PfGUIDialogMod(c);
            case plConstAccelEaseCurve:
                return new plConstAccelEaseCurve(c);
            case plMobileOccluder:
                return new plMobileOccluder(c);
            case plLayerLinkAnimation:
                return new plLayerLinkAnimation(c);
            case plArmatureMod:
                return new plArmatureMod(c);
            case plAvBrainPirahna:
                return new plAvBrainPirahna(c);
            case plAvBrainQuab:
                return new plAvBrainQuab(c);
            case plArmatureEffectsMgr:
                return new plArmatureEffectsMgr(c);
            case plFilterCoordInterface:
                return new plFilterCoordInterface(c);
            case plParticleFollowSystemEffect:
                return new plParticleFollowSystemEffect(c);
            case plParticleCollisionEffectBeat:
                return new plParticleCollisionEffectBeat(c);
            case plParticleFadeVolumeEffect:
                return new plParticleFadeVolumeEffect(c);
            case pfGUIKnobCtrl:
                return new pfGUIButtonMod.PfGUIKnobCtrl(c);
            case plDynaRippleMgr:
                return new plDynaRippleMgr(c);
            case plLayerSDLAnimation:
                return new plLayerSDLAnimation(c);
            case pfGUIDragBarCtrl:
                return new pfGUIButtonMod.PfGUIDragBarCtrl(c);
            case plMaintainersMarkerModifier:
                return new plMaintainersMarkerModifier(c);
            case plDistOpacityMod:
                return new plDistOpacityMod(c);
            case plMorphSequence:
                return new plMorphSequence(c);
            case plMorphDataSet:
                return new plMorphDataSet(c);
            case plClothingItem:
                return new plClothingItem(c);
            case plSharedMesh:
                return new plSharedMesh(c);
            case plMatrixConstant:
                return new plMatrixConstant(c);
            case plEmoteAnim:
                return new plEmoteAnim(c);
            case pfGUIDraggableMod:
                return new pfGUIDraggableMod(c);
            case pl2WayWinAudible:
                return new pl2WayWinAudible(c);
            case plArmatureLODMod:
                return new plArmatureLODMod(c);
            case plAvBrainHuman:
                return new plAvBrainHuman(c);
            case plClothingOutfit:
                return new plClothingOutfit(c);
            case plClothingBase:
                return new plClothingBase(c);
            case plAliasModifier:
                return new plAliasModifier(c);
            case plPrintShape:
                return new plPrintShape(c);
            case plObjectInVolumeAndFacingDetector:
                return new plObjectInVolumeAndFacingDetector(c);
            case plNPCSpawnMod:
                return new plNPCSpawnMod(c);
            case plDynaRippleVSMgr:
                return new plDynaRippleVSMgr(c);
            case plSwimDetector:
                return new plSwimDetector(c);
            case plSwimMsg:
                return new plSwimDetector.PlSwimMsg(c);
            case plSwimRegionInterface:
                return new plSwimRegionInterface(c);
            case plSwimCircularCurrentRegion:
                return new plSwimRegionInterface.PlSwimCircularCurrentRegion(c);
            case plSwimStraightCurrentRegion:
                return new plSwimRegionInterface.PlSwimStraightCurrentRegion(c);
            case plVolumeSensorConditionalObjectNoArbitration:
                return new plVolumeSensorConditionalObject.PlVolumeSensorConditionalObjectNoArbitration(c);
            case plSubworldRegionDetector:
                return new plSubworldRegionDetector(c);
            case plResponderEnableMsg:
                return new PrpMessage.PlResponderEnableMsg(c);
            case plParticleUniformWind:
                return new plParticleWindEffect.PlParticleUniformWind(c);
            case plCubicRenderTarget:
                return new plDynamicEnvMap.ithinkthisisPlCubicRenderTarget(c);
            //case plCubicRenderTargetModifier:
            //    return new plCubicRenderTarg
            case plNetMsgSDLState:
                try{
                  Class<uruobj> klass = (Class<uruobj>)Class.forName("prpobjects.plNetMsgSDLState"); //
                  return shared.generic.createObjectWithSingleArgumentConstructor(klass, context.class, c);
                }catch(Exception e){
                    throw new shared.nested(e);
                }

            //case plAvatarMgr:
            //    return new plAvatarMgr(c);
            default:
                //if(type==Typeid.plHKSubWorld){
                //    int d3 = 0;
                //}
                uruobj result = tryread(c,type);
                if(result!=null) return result;
                //m.err("prprootobject: unhandled type.");
                throw new shared.readwarningexception("PrpObject: type constructor not in main list: "+type.toString());
        }
    }
    public uruobj tryread(context c, Typeid type)
    {
        //final String prefix = "uru.moulprp."; //not typesafe!
        try{
            String classname = objpackage+"."+type.toString();
            Class plasmaClass = Class.forName(classname);
            Class<uruobj> plasmaClass2 = (Class<uruobj>)plasmaClass;
            uruobj r = shared.generic.createObjectWithSingleArgumentConstructor(plasmaClass2, context.class, c);
            return r;
        }catch(Exception e){
            return null;
            //throw new shared.nested(e);
        }
    }
    public PrpObject(context c, Typeid type) throws readexception
    {
        object = this.getObject(c, type);
    }
    private PrpObject(){}
    public static PrpObject createFromUruobj(uruobj obj)
    {
        PrpObject result = new PrpObject();
        result.object = obj;
        return result;
    }
    public void compile(Bytedeque c)
    {
        object.compile(c);
    }
    
    public String toString()
    {
        return object.toString();
    }
    
        
}

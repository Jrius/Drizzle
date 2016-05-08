/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import prpobjects.*;
import auto.inplace.Inplace.InplaceModInfo;

public class Inplace_city
{
    public static void MakeTeledahnInfoKirelBook(InplaceModInfo info, prpfile prp)
    {
        //turns the Teledahn linking book by the center spire into a KirelMOUL linking book.

        //change texture:
        PrpRootObject layer_ro = prp.findObject("Map #8251",Typeid.plLayer);
        prpobjects.x0006Layer layer = layer_ro.castTo();
        //layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelkirel*1#0.hsm", Pageid.createFromPrefixSuffix(-2, 55), Pagetype.createWithType(4));
        layer.texture = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plMipMap, "xlinkpanelkirel*1#0.hsm", Pageid.createFromPrefixPagenum(-2, 54), Pagetype.createWithType(4));
        layer_ro.markAsChanged();

        //change pythonfilemod:
        PrpRootObject pfm_ro = prp.findObject("PythLinkBookGUITeledahn", Typeid.plPythonFileMod);
        prpobjects.plPythonFileMod pfm = pfm_ro.castTo();
        prpobjects.plPythonFileMod.Pythonlisting listing = pfm.listings.get(3);
        listing.xString = Bstr.createFromString("KirelMOUL");
        pfm_ro.markAsChanged();
    }
    public static void CityBalconyMarkerFix(InplaceModInfo info, prpfile prp)
    {
        //A'moaca and Ashtar's fix for the player not being able to interact with the marker on Alcugs because they link in too close.
        //This would be *much* simpler if we could just translate it down -25.796 in the y direction.  But we can't because it must be compatible with the pre-existing prp change.
        PrpRootObject ci_ro = prp.findObject("GZMarkerDetector21Far", Typeid.plCoordinateInterface);
        prpobjects.plCoordinateInterface ci = ci_ro.castTo();
        PrpRootObject parent_ro = prpobjects.plCoordinateInterface.findParent(prp, ci_ro);
        prpobjects.plCoordinateInterface parent = parent_ro.castTo();
        //Transmatrix p2w = parent.localToWorld;
        Transmatrix w2p = parent.worldToLocal;
        // set l2w
        ci.localToWorld.setelement(1, 3, -203.82f);
        // calc l2p
        ci.recalcFromL2WandW2P(w2p);

        ci_ro.markAsChanged();
    }
    public static void CityMuseumDoorFix(InplaceModInfo info, prpfile prp)
    {
        //A'moaca and Ashtar's fix for the Museum doors which wouldn't open otherwise online.  (Neither Alcugs nor UU nor MOUL.)
        //from MuseumDoorSencor(sceneobject) remove the entries for MDoorCloseResponder and MDoorOpenResponder and add MDoorResponder.
        //from cPythMuseumDoor(pythonfilemod) change pythonfile to xNewHighLevelStarTrekDoor, change entry 4 from MDoorOpenResponder to MDoorResponder, and remove entry 5(MDoorCloseResponder).
        //remove MDoorOpenResponder and MDoorCloseResponder(respondermods)
        //create MDoorResponder(respondermod).
        if(prp.contains("MDoorResponder", Typeid.plResponderModifier))
        {
            //skip it, as this already has the modified prp as a base.
        }
        else
        {
            PrpRootObject oldopen_ro = prp.findObject("MDoorOpenResponder", Typeid.plResponderModifier);
            PrpRootObject oldclose_ro = prp.findObject("MDoorCloseResponder", Typeid.plResponderModifier);
            prpobjects.plResponderModifier oldopen = oldopen_ro.castTo();
            prpobjects.plResponderModifier oldclose = oldclose_ro.castTo();
            Uruobjectref newdoor = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plResponderModifier, "MDoorResponder", prp);

            //remove old responder modifiers
            prp.markObjectDeleted(oldopen_ro.getref(), true);
            prp.markObjectDeleted(oldclose_ro.getref(), true);

            //merge oldclose into oldopen.
            prpobjects.plResponderModifier.PlResponderState openrs = oldopen.messages.get(0);
            prpobjects.plResponderModifier.PlResponderState closers = oldclose.messages.get(0);
            oldopen.messages.add(closers);
            for(Uruobjectref ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectref.class, oldopen.messages))
            {
                if(ref.hasref() && ref.xdesc.objectname.toString().equals("MDoorOpenResponder"))
                {
                    ref.xdesc.objectname = Urustring.createFromString("MDoorResponder");
                }
                else if(ref.hasref() && ref.xdesc.objectname.toString().equals("MDoorCloseResponder"))
                {
                    ref.xdesc.objectname = Urustring.createFromString("MDoorResponder");
                }
            }
            closers.switchToState = 1;
            closers.numCallbacks = 2;
            prpobjects.PrpMessage.PlAnimCmdMsg acm = closers.commands.get(0).message.castTo();
            prpobjects.PrpMessage.PlEventCallbackMsg othercallback = prpobjects.PrpMessage.PlEventCallbackMsg.createWithSenderAndReceiver(Uruobjectref.none(), newdoor);
            othercallback.parent.broadcastFlags = 0x800;
            othercallback.event = 1;
            othercallback.user = 1;
            acm.parent.callbacks.add(PrpTaggedObject.createWithTypeidUruobj(Typeid.plEventCallbackMsg, othercallback));
            closers.waitToCmdTable.add(prpobjects.plResponderModifier.PlResponderState.WaitToCmd.createWithWaitCmd(1,0));
            closers.commands.get(3).waitOn = 1;
            //reorder commands, but I don't think we actually need to do this.
            prpobjects.plResponderModifier.PlResponderCmd anim = closers.commands.get(0);
            prpobjects.plResponderModifier.PlResponderCmd soun = closers.commands.get(1);
            prpobjects.plResponderModifier.PlResponderCmd excl = closers.commands.get(2);
            closers.commands.set(0, excl);
            closers.commands.set(1, anim);
            closers.commands.set(2, soun);
            prp.addObject(newdoor, oldopen);

            //change pythonfilemod
            PrpRootObject pfm_ro = prp.findObject("cPythMuseumDoor", Typeid.plPythonFileMod);
            prpobjects.plPythonFileMod pfm = pfm_ro.castTo();
            pfm.pyfile = Urustring.createFromString("xNewHighLevelStarTrekDoor");
            pfm.getListingByIndex(4).xRef = newdoor;
            pfm.removeListingByIndex(5);

            //change sceneobject
            PrpRootObject so_ro = prp.findObject("MuseumDoorSencor", Typeid.plSceneObject);
            prpobjects.plSceneObject so = so_ro.castTo();
            so.modifiers.remove(oldopen_ro.getref());
            so.modifiers.remove(oldclose_ro.getref());
            so.modifiers.add(newdoor);

            pfm_ro.markAsChanged();
            so_ro.markAsChanged();

            int dummy=0;
        }
    }
    public static void FixKadishDoors(InplaceModInfo info, prpfile prp)
    {
        if(info.relpath.equals("/dat/city_District_KadishGallery.prp"))
        {
            //PrpRootObject ro1 = prp.findObject("cSfxSoRegGallery-Verb02", Typeid.plSoftVolumeSimple);
            //PrpRootObject ro2 = prp.findObject("SoftRegionGalleryRelevance", Typeid.plSoftVolumeSimple); //could be removed in other inplacemod.
            //PrpRootObject ro3 = prp.findObject("SoftRegionKadishGalleryVis", Typeid.plSoftVolumeSimple);
            //Vector<PrpRootObject> ro1refs = prp.FindObjectsThatReferenceAnother(ro1.getref().xdesc);
            //Vector<PrpRootObject> ro2refs = prp.FindObjectsThatReferenceAnother(ro1.getref().xdesc);
            //Vector<PrpRootObject> ro3refs = prp.FindObjectsThatReferenceAnother(ro3.getref().xdesc);

            prpobjects.Pageid newpid = prpobjects.Pageid.createFromPrefixPagenum(6, 80); //6=city
            PrpRootObject ro1_a = prp.findObject("SfxSoRegGallery-Verb02", Typeid.plSceneObject);
            PrpRootObject ro1_b = prp.findObject("cSfxSoRegUnion-Gallery-Verb01", Typeid.plSoftVolumeUnion);
            PrpRootObject ro3_a = prp.findObject("KAdishGAlleryRegionVis", Typeid.plSceneObject);
            PrpRootObject ro3_b = prp.findObject("VisRegionKDSHgallery", Typeid.plVisRegion);
            prpobjects.plSceneObject a1 = ro1_a.castTo();
            prpobjects.plSoftVolumeUnion b1 = ro1_b.castTo();
            prpobjects.plSceneObject a3 = ro3_a.castTo();
            prpobjects.plVisRegion b3 = ro3_b.castTo();
            a1.interfaces.get(0).xdesc.pageid = newpid;
            b1.refs.get(1).xdesc.pageid = newpid;
            a3.interfaces.get(0).xdesc.pageid = newpid;
            b3.softvolumesimple.xdesc.pageid = newpid;
            ro1_a.markAsChanged();
            ro1_b.markAsChanged();
            ro3_a.markAsChanged();
            ro3_b.markAsChanged();

            int dummy=0;
        }
        else if(info.relpath.equals("/dat/city_District_palace.prp"))
        {
            PrpRootObject ro1 = prp.findObject("SoftRegionUnionCantSeePalace", Typeid.plSoftVolumeUnion);
            prpobjects.plSoftVolumeUnion un1 = ro1.castTo();
            un1.refs.remove(0);
            ro1.markAsChanged();
        }
        else if(info.relpath.equals("/dat/city_District_library.prp"))
        {
            PrpRootObject ro1 = prp.findObject("SoftRegionUnionKadishAndKahlo", Typeid.plSoftVolumeUnion);
            prpobjects.plSoftVolumeUnion un1 = ro1.castTo();
            un1.refs.remove(0);
            ro1.markAsChanged();
        }
        else if(info.relpath.equals("/dat/city_District_canyon.prp"))
        {
            PrpRootObject ro1 = prp.findObject("SoftRegionUnionKadishAndKahlo", Typeid.plSoftVolumeUnion);
            prpobjects.plSoftVolumeUnion un1 = ro1.castTo();
            un1.refs.remove(0);
            ro1.markAsChanged();
        }
        else if(info.relpath.equals("/dat/city_District_courtyard.prp"))
        {
            PrpRootObject ro1 = prp.findObject("SoftRegionUnionCantSeeCourtyard", Typeid.plSoftVolumeUnion);
            prpobjects.plSoftVolumeUnion un1 = ro1.castTo();
            un1.refs.remove(0);
            ro1.markAsChanged();
        }
        else if(info.relpath.equals("/dat/city_District_harbor.prp"))
        {
            //the harbor contains the D'ni backdrop for some reason, so we need to do this one too.
            PrpRootObject ro1 = prp.findObject("SoftRegionUnionExcludeHarborDrawables", Typeid.plSoftVolumeUnion);
            prpobjects.plSoftVolumeUnion un1 = ro1.castTo();
            un1.refs.remove(2);
            ro1.markAsChanged();
        }
    }
}

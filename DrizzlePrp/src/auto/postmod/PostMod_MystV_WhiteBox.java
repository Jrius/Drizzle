/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto.postmod;

import java.util.ArrayList;
import prpobjects.*;
import shared.Flt;
import shared.m;

public class PostMod_MystV_WhiteBox
{
    /**
     * Ensures all versions of Myst V's prps result in the same output file.
     * This is useful so that anyone can play on Shards, as these won't accept two different versions of the same file
     * (even if the differences are minor).
     * 
     * Must be run on both the original ("White") version of the file, and the new (GOG, possibly Steam) versions.
     * 
     * Things it changes:
     * - removes additional translations (such as Russian), since Uru doesn't support these.
     * - sets the softvalue in EAX sound properties to 0, as these seem completely random in the GOG version.
     * - [[removes some weird new flag in ObjInterface]] -> no longer needed (wtf ?...)
     * - removes some weird new flag in plHKPhysical.group
     * - changes Quat from XYZW to WXYZ in plHKPhysical.orientation (I have no idea why these were changed...)
     * 
     * @param prp the prp to modify
     * @param isAlreadyWhite true if this PRP comes from the original version of the game, false if it comes from GOG/Steam
     */
    public static void MakeWhite(prpfile prp, boolean isAlreadyWhite)
    {
        // well, not completely white, but closer to white anyway...
        
        prp.markAsChanged();
        
        // eax soft values never match
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plWin32StreamingSound))
        {
            if (obj.castTo() instanceof x0084Win32StreamingSound)
            {
                x0084Win32StreamingSound ws = (x0084Win32StreamingSound) obj.castTo();
                ws.parent.parent.EAXSettings.occlusionSoftValue = Flt.zero();
            }
            else
            {
                plWin32Sound ws = (plWin32Sound) obj.castTo();
                ws.parent.EAXSettings.occlusionSoftValue = Flt.zero();
            }
            obj.markAsChanged();
        }
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plWin32StaticSound))
        {
            if (obj.castTo() instanceof x0096Win32StaticSound)
            {
                x0096Win32StaticSound ws = (x0096Win32StaticSound) obj.castTo();
                ws.parent.parent.EAXSettings.occlusionSoftValue = Flt.zero();
            }
            else
            {
                plWin32Sound ws = (plWin32Sound) obj.castTo();
                ws.parent.EAXSettings.occlusionSoftValue = Flt.zero();
            }
            obj.markAsChanged();
        }
        
        // remove all russian translations
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
        {
            if (obj.header.desc.objectname.toString().endsWith("_rus.ogg"))
                prp.markObjectDeleted(obj.getref(), true);
        }
        
        // some objects in the gog version (such as plCoordinateInterface) have a flag set in their ObjInterface
        // I don't know what that flag is (hsplasma only ever mentions one flag, which is kDisable - not sure what that means)
        // Anyway, we must disable that flag for all objects which might have it.
        // Unfortunately for us, it seems there is no way to get a list of all plObjInterface easily...
        ArrayList<plObjInterface> allObjInterfaces = new ArrayList();
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plCoordinateInterface))
        {
            allObjInterfaces.add(((plCoordinateInterface)obj.castTo()).parent);
            obj.markAsChanged();
        }
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plOmniLightInfo))
        {
            allObjInterfaces.add(((x0056OmniLightInfo)obj.castTo()).parent.parent);
            obj.markAsChanged();
        }
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plPointShadowMaster))
        {
            allObjInterfaces.add(((x00D5PointShadowMaster)obj.castTo()).parent.parent);
            obj.markAsChanged();
        }
        
        for (plObjInterface objInterface: allObjInterfaces)
        //    objInterface.bv = HsBitVector.createDefault();
            if (objInterface.bv.count != 0);
        
        for (PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            plHKPhysical phys = obj.castTo();
            obj.markAsChanged();
            if (phys.havok != null && phys.havok.group!=null && phys.havok.group.count == 1)
            {
                if (phys.havok.group.values[0] == 0)
                {
                    phys.havok.group.count = 0;
                    phys.havok.group.values = new int[0];
                }
            }
            
            if (!isAlreadyWhite && phys.havok != null)
            {
                Flt buf = phys.havok.orientation.w;
                phys.havok.orientation.w = phys.havok.orientation.z;
                phys.havok.orientation.z = phys.havok.orientation.y;
                phys.havok.orientation.y = phys.havok.orientation.x;
                phys.havok.orientation.x = buf;
            }
        }
    }
}

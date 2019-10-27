/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto.postmod;

import auto.PrpDiff;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import prpobjects.*;
import shared.Flt;
import shared.m;
import uru.Bytedeque;

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
     * - do some majiik in MystMystV_District_Island.prp -> Myst_Island_40000008_aBlendSpans0.
     *      In the Steam version, there is a one-bit difference that cause a read out of bounds. Haven't found yet when/how/why.
     *      However fixing it is pretty simple by altering the rawdata...
     * 
     * @param prp the prp to modify
     * @param isAlreadyWhite true if this PRP comes from the original version of the game, false if it comes from GOG/Steam
     */
    public static void MakeWhite(prpfile prp, boolean isAlreadyWhite)
    {
        // well, not completely white, but closer to white anyway...
        // (future me: wtf is wrong with me ??? did I really write that sentence ? I must have had a deathwish...)
        
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
        
        // fix some weird bit in Myst's island drawable span
        if (prp.header.agename.toString().equals("MystMystV") && prp.header.pagename.toString().equals("Island"))
        {
            PrpRootObject ro = prp.findObject("Myst_Island_40000008_aBlendSpans0", Typeid.plDrawableSpans);
            
            // it seems the bogus byte is located in some kind of vertex descriptor. However it's ultra-hard for me to find its exact location.
            // So ! Instead, let's use my unique superpower: DUMB HACKING.
            // By which I mean: recompile the object manually, then assign the compiled data to the PrpRootObject's rawdata.
            // Don't forget to pretend it was compiled all along and whistle inconspicuously, this way the PRPRO won't notice.
            
            plDrawableSpans dspan = (plDrawableSpans)ro.castTo();
            Bytedeque dequeue = new Bytedeque(shared.Format.pots);
            dspan.compile(dequeue);
            ro.hasChanged = false;
            ro.hasRaw = true;
            ro.rawdata = dequeue.getAllBytes();
            ro.rawdata[0xb0c4] = 0; // there. All good.
            
            /*
            try (FileOutputStream stream = new FileOutputStream("c:/bin.uof")) {
                stream.write(ro.rawdata);
            } catch (IOException ex) {
                Logger.getLogger(PrpDiff.class.getName()).log(Level.SEVERE, null, ex);
            }
            // */
        }
    }
}

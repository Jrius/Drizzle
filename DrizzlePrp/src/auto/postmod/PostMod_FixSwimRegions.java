/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import prpobjects.*;
import shared.*;

public class PostMod_FixSwimRegions
{
    public static void FixSwimRegions(prpfile prp)
    {
        /*for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plSwimDetector))
        {
            plSwimDetector sd = obj.castTo();
            plSwimDetector.PlSwimMsg entermsg = sd.parent.enterMsg.castTo();
            PrpRootObject so_ro = prp.findObjectWithRef(entermsg.ref2);
            plSceneObject so = so_ro.castToSceneObject();
            PrpRootObject si_ro = prp.findObjectWithRef(so.simulationinterface);
            plSimulationInterface si = si_ro.castTo();
            PrpRootObject phys_ro = prp.findObjectWithRef(si.physical);
            plHKPhysical phys = phys_ro.castTo();
            phys.havok.flagsdetect = 0x20000; //make it volume detect instead of boundary detect.  (Swim regions use volume detect in Pots, but nothing uses them in Moul, I think.)
            phys.havok.coltype = 0x0; //it was set to 0x400 otherwise
            phys.havok.mass = Flt.zero(); //it was set to 1.0 otherwise
            phys.havok.group = HsBitVector.createDefault(); //was set to kPinned otherwise
        }*/
        for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = obj.castToSceneObject();
            boolean isSwimRegion = false;

            //detect if this is a swim region
            for(Uruobjectref ref: so.modifiers)
            {
                if(ref.hasref() && ref.xdesc.objecttype==Typeid.plSwimDetector)
                {
                    isSwimRegion = true;
                    break;
                }
            }

            if(isSwimRegion)
            {
                //fix it!
                PrpRootObject si_ro = prp.findObjectWithRef(so.simulationinterface);
                plSimulationInterface si = si_ro.castTo();
                PrpRootObject phys_ro = prp.findObjectWithRef(si.physical);
                plHKPhysical phys = phys_ro.castTo();
                phys.convertPXtoHK();
                phys.havok.flagsdetect = 0x20000; //make it volume detect instead of boundary detect.  (Swim regions use volume detect in Pots, but nothing uses them in Moul, I think.)
                phys.havok.coltype = 0x0; //it was set to 0x400 otherwise
                phys.havok.mass = Flt.zero(); //it was set to 1.0 otherwise
                phys.havok.group = HsBitVector.createDefault(); //was set to kPinned otherwise
            }
        }
    }

}

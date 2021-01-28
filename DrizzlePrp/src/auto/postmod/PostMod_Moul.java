/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import prpobjects.*;
import java.util.Vector;

public class PostMod_Moul
{
    public static void PostMod_FixTsogalLanguages(prpfile prp)
    {
        //Fix Tsogal language problem by renaming the 5 objects with a name of "" to "SfxFootstepRegion-Stone50".
        //Their types are 0x1,0x15,0x1c,0x3f,0xcb.
        PrpRootObject r1 = prp.findObject("", Typeid.plSceneObject);
        PrpRootObject r2 = prp.findObject("", Typeid.plCoordinateInterface);
        PrpRootObject r3 = prp.findObject("", Typeid.plSimulationInterface);
        PrpRootObject r4 = prp.findObject("", Typeid.plHKPhysical);
        PrpRootObject r5 = prp.findObject("", Typeid.plInterfaceInfoModifier);
        //Vector<PrpRootObject> r1refs = prp.FindObjectsThatReferenceAnother(r1.getref().xdesc);
        //Vector<PrpRootObject> r2refs = prp.FindObjectsThatReferenceAnother(r2.getref().xdesc);
        //Vector<PrpRootObject> r3refs = prp.FindObjectsThatReferenceAnother(r3.getref().xdesc);
        //Vector<PrpRootObject> r4refs = prp.FindObjectsThatReferenceAnother(r4.getref().xdesc);
        //Vector<PrpRootObject> r5refs = prp.FindObjectsThatReferenceAnother(r5.getref().xdesc);
        //something like 17 replacements.
        for(PrpRootObject ro: new PrpRootObject[]{r1,r2,r3,r4,r5})
        {
            for(Uruobjectdesc desc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, ro))
            {
                if(desc.objectname.toString().equals(""))
                {
                    desc.objectname = Urustring.createFromString("SfxFootstepRegion-Stone50");
                }
            }
        }
        int dummy=0;
    }
}

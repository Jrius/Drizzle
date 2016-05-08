/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import prpobjects.*;
import shared.*;

public class Inplace_Fanages
{
    public static void FixGeostates(Inplace.InplaceModInfo info, prpfile prp)
    {
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plWaveSet7))
        {
            prpobjects.plWaveSet7 ws7 = ro.castTo();
            prpobjects.plWaveSet7.WaveState ws = ws7.sub1.fGeoState;
            if(ws.fMaxLength.isequalto(0) && ws.fMinLength.isequalto(0) && ws.fAmpOverLen.isequalto(0) && ws.fChop.isequalto(0) && ws.fAngleDev.isequalto(0))
            {
                m.msg("Setting default waveset in object: ",ro.toString());
                ws.fMaxLength = Flt.createFromJavaFloat(6.25f);
                ws.fMinLength = Flt.createFromJavaFloat(6.0f);
                ws.fAmpOverLen = Flt.createFromJavaFloat(0.001f);
                ws.fChop = Flt.createFromJavaFloat(0.0f);
                ws.fAngleDev = Flt.createFromJavaFloat(1.0f);
                ro.markAsChanged();
            }
            else
            {
                m.msg("Waveset already has nonzero values: ",ro.toString());
            }
        }
    }

}

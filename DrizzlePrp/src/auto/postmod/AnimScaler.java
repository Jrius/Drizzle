/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto.postmod;

import shared.*;
import prpobjects.*;

/**
 * Contains lots of stuff to edit animations
 */
public class AnimScaler {
    private plATCAnim anim;
    private float scale;
    private boolean backwards;
    private float length;
    
    public AnimScaler(plATCAnim a, float s)
    {
        anim = a;
        if (s < 0)
        {
            scale = -s;
            backwards = true;
        }
        else
        {
            scale = s;
            backwards = false;
        }
    }
    
    
    public void scale()
    {
        length = anim.parent.stoptime.toJavaFloat();
        anim.parent.stoptime = new Flt(anim.parent.stoptime.toJavaFloat() * scale);
        anim.loopEnd = new Flt(anim.loopEnd.toJavaFloat() * scale); // shouldn't be required, but you never know...
        for (PrpTaggedObject ourEffect: anim.parent.effects) // take the matrix controller
        {
            switch (ourEffect.type)
            {
                // heh, once again no way to avoid duplicate code...
                case plMatrixChannelApplicator:
                {
                    // that's just the channel name, no need to bother
                    break;
                }
                case plMatrixControllerChannel:
                {
                    scaleMatrixController((plAGAnim.plMatrixControllerChannel)(ourEffect.castTo())); // cast it
                    break;
                }
                default:
                    m.err("Unsupported controller ! " + ourEffect.type);
            }
        }

    }
    
    
    private void scaleMatrixController(plAGAnim.plMatrixControllerChannel mtxctrl)
    {
        PrpController.plCompoundController ctrl = mtxctrl.controller.castTo(); // take the compound controller in it
        
        if (ctrl.u1 != null && ctrl.u1.prpobject != null)
        {
            PrpController.plCompoundController posCtrl = ctrl.u1.castTo(); // take the position controller
            
            if (posCtrl.u1.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)posCtrl.u1.castTo()); // & rescale it
            if (posCtrl.u2.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)posCtrl.u2.castTo());
            if (posCtrl.u3.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)posCtrl.u3.castTo());
        }
        if (ctrl.u2 != null && ctrl.u2.prpobject != null)
        {
            PrpController.plCompoundController rotCtrl = ctrl.u2.castTo(); // take the rotation controller

            if (rotCtrl.u1.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)rotCtrl.u1.castTo());
            if (rotCtrl.u2.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)rotCtrl.u2.castTo());
            if (rotCtrl.u3.prpobject != null) scaleLeafCtrl((PrpController.plLeafController)rotCtrl.u3.castTo());
        }
        if (ctrl.u3 != null && ctrl.u3.prpobject != null)
        {
            PrpController.plLeafController scaCtrl = ctrl.u3.castTo(); // take the scale controller
            scaleLeafCtrl(scaCtrl);
        }
    }
    
    private void scaleLeafCtrl(PrpController.plLeafController leafCtrl)
    {
        // aaaaaand... duplicate code !
        
        if (backwards)
        {
            if (leafCtrl.xtype1 != null)
            {
                for (PrpController.moul1 weirdCtrl: leafCtrl.xtype1)
                {
                    if (weirdCtrl.framenum <= length*30) // if that's over the anim endtime, don't touch it (yes, this does happen...)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                // and of course, this would be too easy. We must also reverse the animation keys.
                int i=leafCtrl.xtype1.length;
                PrpController.moul1[] nl = new PrpController.moul1[i];
                for (PrpController.moul1 weirdCtrl: leafCtrl.xtype1)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype1 = nl;
            }
            if (leafCtrl.xtype2 != null)
            {
                for (PrpController.moul2 weirdCtrl: leafCtrl.xtype2)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype2.length;
                PrpController.moul2[] nl = new PrpController.moul2[i];
                for (PrpController.moul2 weirdCtrl: leafCtrl.xtype2)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype2 = nl;
            }
            if (leafCtrl.xtype3 != null)
            {
                for (PrpController.moul3 weirdCtrl: leafCtrl.xtype3)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype3.length;
                PrpController.moul3[] nl = new PrpController.moul3[i];
                for (PrpController.moul3 weirdCtrl: leafCtrl.xtype3)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype3 = nl;
            }
            if (leafCtrl.xtype4 != null)
            {
                for (PrpController.moul4 weirdCtrl: leafCtrl.xtype4)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype4.length;
                PrpController.moul4[] nl = new PrpController.moul4[i];
                for (PrpController.moul4 weirdCtrl: leafCtrl.xtype4)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype4 = nl;
            }
            if (leafCtrl.xtype5 != null)
            {
                for (PrpController.moul5 weirdCtrl: leafCtrl.xtype5)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype5.length;
                PrpController.moul5[] nl = new PrpController.moul5[i];
                for (PrpController.moul5 weirdCtrl: leafCtrl.xtype5)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype5 = nl;
            }
            if (leafCtrl.xtype6 != null)
            {
                for (PrpController.moul6 weirdCtrl: leafCtrl.xtype6)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype6.length;
                PrpController.moul6[] nl = new PrpController.moul6[i];
                for (PrpController.moul6 weirdCtrl: leafCtrl.xtype6)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype6 = nl;
            }
            if (leafCtrl.xtype7 != null)
            {
                for (PrpController.moul7 weirdCtrl: leafCtrl.xtype7)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype7.length;
                PrpController.moul7[] nl = new PrpController.moul7[i];
                for (PrpController.moul7 weirdCtrl: leafCtrl.xtype7)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype7 = nl;
            }
            if (leafCtrl.xtype8 != null)
            {
                for (PrpController.moul8 weirdCtrl: leafCtrl.xtype8)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype8.length;
                PrpController.moul8[] nl = new PrpController.moul8[i];
                for (PrpController.moul8 weirdCtrl: leafCtrl.xtype8)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype8 = nl;
            }
            if (leafCtrl.xtype9 != null)
            {
                for (PrpController.moul9 weirdCtrl: leafCtrl.xtype9)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype9.length;
                PrpController.moul9[] nl = new PrpController.moul9[i];
                for (PrpController.moul9 weirdCtrl: leafCtrl.xtype9)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype9 = nl;
            }
            if (leafCtrl.xtype10 != null)
            {
                for (PrpController.moul10 weirdCtrl: leafCtrl.xtype10)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype10.length;
                PrpController.moul10[] nl = new PrpController.moul10[i];
                for (PrpController.moul10 weirdCtrl: leafCtrl.xtype10)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype10 = nl;
            }
            if (leafCtrl.xtype11 != null)
            {
                for (PrpController.moul11 weirdCtrl: leafCtrl.xtype11)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype11.length;
                PrpController.moul11[] nl = new PrpController.moul11[i];
                for (PrpController.moul11 weirdCtrl: leafCtrl.xtype11)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype11 = nl;
            }
            if (leafCtrl.xtype12 != null)
            {
                for (PrpController.moul12 weirdCtrl: leafCtrl.xtype12)
                {
                    if (weirdCtrl.framenum <= length*30)
                        weirdCtrl.framenum = (short) (  (length*30 - weirdCtrl.framenum)*scale  );
                    else
                        weirdCtrl.framenum = 0;
                }
                int i=leafCtrl.xtype12.length;
                PrpController.moul12[] nl = new PrpController.moul12[i];
                for (PrpController.moul12 weirdCtrl: leafCtrl.xtype12)
                {
                    i--;
                    nl[i] = weirdCtrl;
                }
                leafCtrl.xtype12 = nl;
            }
        }
        else
        {
            if (leafCtrl.xtype1 != null)
                for (PrpController.moul1 weirdCtrl: leafCtrl.xtype1)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype2 != null)
                for (PrpController.moul2 weirdCtrl: leafCtrl.xtype2)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype3 != null)
                for (PrpController.moul3 weirdCtrl: leafCtrl.xtype3)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype4 != null)
                for (PrpController.moul4 weirdCtrl: leafCtrl.xtype4)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype5 != null)
                for (PrpController.moul5 weirdCtrl: leafCtrl.xtype5)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype6 != null)
                for (PrpController.moul6 weirdCtrl: leafCtrl.xtype6)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype7 != null)
                for (PrpController.moul7 weirdCtrl: leafCtrl.xtype7)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype8 != null)
                for (PrpController.moul8 weirdCtrl: leafCtrl.xtype8)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype9 != null)
                for (PrpController.moul9 weirdCtrl: leafCtrl.xtype9)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype10 != null)
                for (PrpController.moul10 weirdCtrl: leafCtrl.xtype10)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype11 != null)
                for (PrpController.moul11 weirdCtrl: leafCtrl.xtype11)
                    weirdCtrl.framenum *= scale;
            if (leafCtrl.xtype12 != null)
                for (PrpController.moul12 weirdCtrl: leafCtrl.xtype12)
                    weirdCtrl.framenum *= scale;
        }
    }
}

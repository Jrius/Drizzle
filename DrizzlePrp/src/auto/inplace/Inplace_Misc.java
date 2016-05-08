/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import prpobjects.*;

public class Inplace_Misc
{

    public static boolean TranslateAhny(Inplace.InplaceModInfo info, prpfile prp, String actualmd5)
    {
        actualmd5 = actualmd5.toUpperCase();
        String page = prp.header.pagename.toString();
        if(page.equals("Sphere01"))
        {
            String offlinekimd5 = "52B861244614C328E9B36416D6EE48CC";
            if(!actualmd5.equals(offlinekimd5))
            {
                auto.mod.AutoMod_Translate.DustinModAhnySphere01(prp);
                return true;
            }
        }
        else if(page.equals("MaintRoom01"))
        {
            String offlinekimd5 = "012D1BB5B7D110F7700C41D747D2877E";
            if(!actualmd5.equals(offlinekimd5))
            {
                auto.mod.AutoMod_Translate.DustinModAhnyMaint01(prp);
                return true;
            }
        }
        else if(page.equals("Sphere01OutBuildingInterior"))
        {
            String offlinekimd5 = "636C36F916B25A0595088DEE01736B80";
            if(!actualmd5.equals(offlinekimd5))
            {
                auto.mod.AutoMod_Translate.DustinModAhnyOutBuilding(prp);
                return true;
            }
        }
        return false;

    }

    public static boolean ReltoFixPineTree(Inplace.InplaceModInfo info, prpfile prp)
    {
        PrpRootObject ro = prp.findObject("cPythYeeshaPage21 - PineTrees_0", Typeid.plPythonFileMod);
        if(ro==null)
        {
            auto.mod.AutoMod_Relto.ModRelto_FixPineTrees(prp);
            return true;
        }
        else
        {
            //otherwise we have the Offline-KI pre-modded version.
            return false;
        }
    }

    public static boolean ReltoMakeDynamicCovers(Inplace.InplaceModInfo info, prpfile prp)
    {
        PrpRootObject ro = prp.findObject("book15DynTexture", Typeid.plDynamicTextMap);
        if(ro==null)
        {
            auto.mod.AutoMod_Relto.ModRelto_AddBookCovers(prp);
            return true;
        }
        else
        {
            //otherwise we have the Offline-KI pre-modded version.
            return false;
        }
    }

    public static void GahreesenWallSoundFix(Inplace.InplaceModInfo info, prpfile prp)
    {
        PrpRootObject ro1 = prp.findObject("cSfxRespFeet-MaintOnGlass", Typeid.plResponderModifier);
        PrpRootObject ro2 = prp.findObject("cSfxRespFeet-MainOnStone", Typeid.plResponderModifier);
        plResponderModifier rm1 = ro1.castTo();
        plResponderModifier rm2 = ro2.castTo();
        PrpTaggedObject to1 = rm1.messages.get(0).commands.get(0).message;
        PrpTaggedObject to2 = rm2.messages.get(0).commands.get(0).message;
        prpobjects.PrpMessage.PlArmatureEffectStateMsg aesm1 = to1.castTo();
        prpobjects.PrpMessage.PlArmatureEffectStateMsg aesm2 = to2.castTo();
        aesm1.surface = 4; //choose a different sound (was 16)
        aesm2.surface = 11; //choose a different sound (was 17)

        ro1.markAsChanged();
        ro2.markAsChanged();
    }
}

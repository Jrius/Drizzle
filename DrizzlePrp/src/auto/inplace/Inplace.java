/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import prpobjects.Typeid;
import prpobjects.prpfile;
import java.util.Vector;
import java.util.HashMap;
import prpobjects.*;
import shared.m;
import shared.b;

public class Inplace
{
    public static enum ModName
    {
        //automatically done ones:
        RemoveFence,
        FixKadishDoors,
        RemoveRelevanceRegions,
        CityBalconyMarkerFix,
        CityMuseumDoorFix,
        MakeTeledahnIntoKirelBook,
        AhnonayTranslation,
        ReltoFixPineTree,
        ReltoMakeDynamicCovers,
        GahreesenWallSoundFix, //so that you don't crash when nearing the Gahreesen wall.

        //manual ones:
        FixGeostates, //fixes the water problem many fanages have.
    }
    public static ModName getModName(String modname)
    {
        return ModName.valueOf(modname);
    }
    public static void printAllModNames()
    {
        for(ModName modname: ModName.values())
        {
            m.msg(modname.toString());
        }
    }
    public static void InplaceMod(String potsfolder, String relpath, String modname)
    {
        if(!auto.AllGames.getPots().isFolderX(potsfolder)) m.cancel();

        InplaceFile pots = new InplaceFile(potsfolder);
        InplaceModInfo info = new InplaceModInfo();
        info.relpath = relpath;
        info.modnames = new ModName[]{getModName(modname)};
        info.age = auto.AllGames.getPots().GetAgenameFromPrpname(relpath);
        InplaceMod(pots,info);
    }
    public static void InplaceMod(InplaceFile potsfolder, String relpath)
    {
        InplaceMod(potsfolder, InplaceModInfo.get(relpath));
    }
    public static void InplaceMod(InplaceFile potsfolder, InplaceModInfo info)
    {
        InplaceFile f = potsfolder.File(info.relpath);
        byte[] data = f.ReadAsBytes();
        prpfile prp = prpfile.createFromBytes(data, true);
        boolean waschanged = true;

        for(ModName modname: info.modnames)
        {
            if(modname==ModName.RemoveFence)
            {
                //Dustin's removal of colliders for the Cleft fence so that avatars can jump over it (and eventually jump into the volcano.)
                Inplace_Cleft.RemoveFence(info,prp);
            }
            else if(modname==ModName.FixKadishDoors)
            {
                //Fixes the Kadish Gallery doors in the city, so that they can be opened!
                Inplace_city.FixKadishDoors(info,prp);
            }
            else if(modname==ModName.RemoveRelevanceRegions)
            {
                //A'moaca and Ashtar's fix for Relevance regions in the city that otherwise makes avatars invisible or some such thing.
                PrpRootObject[] relregs = prp.FindAllObjectsOfType(Typeid.plRelevanceRegion);
                for(PrpRootObject relreg: relregs)
                {
                    prpobjects.plRelevanceRegion rr = relreg.castTo();
                    Uruobjectref r1 = rr.ref;
                    Uruobjectref r2 = rr.parent.ref;
                    prp.markObjectDeleted(relreg.getref(),false);
                    prp.markObjectDeleted(r1,false);
                    prp.markObjectDeleted(r2,false);
                    int i=0;
                }
            }
            else if(modname==ModName.CityBalconyMarkerFix)
            {
                //A'moaca and Ashtar's fix for the player not being able to interact with the marker on Alcugs because they link in too close.
                Inplace_city.CityBalconyMarkerFix(info, prp);
            }
            else if(modname==ModName.CityMuseumDoorFix)
            {
                //A'moaca and Ashtar's fix for the Museum doors which wouldn't open otherwise online.  (Neither Alcugs nor UU nor MOUL.)
                Inplace_city.CityMuseumDoorFix(info, prp);
            }
            else if(modname==ModName.MakeTeledahnIntoKirelBook)
            {
                //turns the Teledahn linking book by the center spire into a KirelMOUL linking book.
                Inplace_city.MakeTeledahnInfoKirelBook(info, prp);
            }
            else if(modname==ModName.FixGeostates)
            {
                Inplace_Fanages.FixGeostates(info, prp);
            }
            else if(modname==ModName.AhnonayTranslation)
            {
                String actualmd5 = b.BytesToHexString(shared.CryptHashes.GetMd5(data));
                if(!Inplace_Misc.TranslateAhny(info, prp, actualmd5))
                    waschanged = false;
            }
            else if(modname==ModName.ReltoFixPineTree)
            {
                if(!Inplace_Misc.ReltoFixPineTree(info, prp))
                    waschanged = false;
            }
            else if(modname==ModName.ReltoMakeDynamicCovers)
            {
                if(!Inplace_Misc.ReltoMakeDynamicCovers(info, prp))
                    waschanged = false;
            }
            else if(modname==ModName.GahreesenWallSoundFix)
            {
                Inplace_Misc.GahreesenWallSoundFix(info, prp);
            }
            else
            {
                m.err("Unable to find modname: "+modname.toString());
            }
        }

        if(waschanged)
        {
            f.SaveFile(prp.saveAsBytes());
            UpdateSumfile(potsfolder, info);
        }
        
    }

    public static class InplaceModInfo
    {
        public String relpath;
        //public String[] modnames;
        public ModName[] modnames;
        public String age;

        private InplaceModInfo(){}
        private static HashMap<String,InplaceModInfo> mods;
        private static HashMap<String,InplaceModInfo> getmods()
        {
            if(mods==null)
            {
                mods = new HashMap();
                addinfo("Cleft","/dat/Cleft_District_Desert.prp", ModName.RemoveFence);
                addinfo("Cleft","/dat/Cleft_District_tmnaDesert.prp", ModName.RemoveFence);
                addinfo("city", "/dat/city_District_palace.prp", ModName.RemoveRelevanceRegions, ModName.CityBalconyMarkerFix, ModName.FixKadishDoors);
                addinfo("city", "/dat/city_District_courtyard.prp", ModName.RemoveRelevanceRegions, ModName.CityMuseumDoorFix, ModName.FixKadishDoors, ModName.MakeTeledahnIntoKirelBook);
                addinfo("city", "/dat/city_District_canyon.prp", ModName.RemoveRelevanceRegions, ModName.FixKadishDoors);
                addinfo("city", "/dat/city_District_cavetjunction.prp", ModName.RemoveRelevanceRegions);
                addinfo("city", "/dat/city_District_ferry.prp", ModName.RemoveRelevanceRegions); //ferry has RemoveRelevanceRegions and the soccer ball added
                addinfo("city", "/dat/city_District_greatstair.prp", ModName.RemoveRelevanceRegions);
                addinfo("city", "/dat/city_District_KadishGallery.prp", ModName.RemoveRelevanceRegions, ModName.FixKadishDoors); //also add door fix?
                addinfo("city", "/dat/city_District_KahloPub.prp", ModName.RemoveRelevanceRegions);
                addinfo("city", "/dat/city_District_library.prp", ModName.RemoveRelevanceRegions, ModName.FixKadishDoors);
                addinfo("city", "/dat/city_District_harbor.prp", ModName.FixKadishDoors);
                addinfo("AhnySphere01", "/dat/AhnySphere01_District_Sphere01.prp", ModName.AhnonayTranslation);
                addinfo("AhnySphere01", "/dat/AhnySphere01_District_MaintRoom01.prp", ModName.AhnonayTranslation);
                addinfo("AhnySphere01", "/dat/AhnySphere01_District_Sphere01OutBuildingInterior.prp", ModName.AhnonayTranslation);
                addinfo("Personal", "/dat/Personal_District_psnlMYSTII.prp", ModName.ReltoFixPineTree, ModName.ReltoMakeDynamicCovers);
                addinfo("Garrison", "/dat/Garrison_District_WallRoom.prp", ModName.GahreesenWallSoundFix);

            }
            return mods;
        }
        private static void addinfo(String age, String relpath, ModName... modnames)
        {
            InplaceModInfo r = new InplaceModInfo();
            r.age = age;
            r.relpath = relpath;
            r.modnames = modnames;
            mods.put(relpath, r);
        }
        public static InplaceModInfo get(String relpath)
        {
            InplaceModInfo r = getmods().get(relpath);
            return r;
        }
    }

    public static void UpdateSumfile(InplaceFile potsfolder, InplaceModInfo info)
    {
        InplaceFile sumfile = potsfolder.File("/dat/"+info.age+".sum");
        if(sumfile.exists())
        {
            byte[] sumdata = prpobjects.sumfile.createEmptySumfile().getByteArray();
            sumfile.SaveFile(sumdata);
        }
    }

}

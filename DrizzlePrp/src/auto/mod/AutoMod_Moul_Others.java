/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.mod;

import auto.mod.*;
import auto.mod.AutoMod.AutoModInfo;
import prpobjects.*;
import java.util.Vector;
import prpdistiller.distiller;

public class AutoMod_Moul_Others
{
    public static class DistillHelper
    {
        String destAgename;
        String destPagename;
        Integer destPrefix;
        Integer destPagenum;
        
        private AutoModInfo c;
        private Vector<String> moulsources = new Vector();
        private Vector<String> potssources = new Vector();
        private Vector<String> sceneobjects = new Vector();
        private Vector<NameAndType> plainobjects = new Vector();
        private Vector<String> pfms = new Vector();
        private String mainprp;
        private boolean includeAllObjects = false;
        public PostMod postmod;
        public distiller.includeDuplicateDecider extrainclusions;


        public static interface PostMod
        {
            public void Proccess(prpfile dest);
        }
        
        
        public DistillHelper(AutoModInfo c2)
        {
            c = c2;
        }
        
        public void addMoulSources(String... filenames)
        {
            for(String s: filenames) moulsources.add(s);
        }
        
        public void addPotsSources(String... filenames)
        {
            for(String s: filenames) potssources.add(s);
        }
        
        public void addSceneobjects(String... filenames)
        {
            for(String s: filenames) sceneobjects.add(s);
        }

        public void addPlainobjects(NameAndType... filenames)
        {
            for(NameAndType s: filenames) plainobjects.add(s);
        }
        
        public void addPythonfilemods(String... pfmss)
        {
            for(String s: pfmss) pfms.add(s);
        }
        
        public void setMainPrp(String mainprp)
        {
            this.mainprp = mainprp;
            this.addMoulSources(mainprp);
        }
        
        public void includeEveryObjectInMainPrp()
        {
            this.includeAllObjects = true;
        }

        public void work2(prpfile dest)
        {
            distiller.distillInfo info = new distiller.distillInfo();

            dest.addScenenode();

            Vector<prpfile> sources = new Vector();
            prpfile prp=null;
            for(String s: moulsources)
            {
                prpfile curprp = prpfile.createFromFile(c.infolder+"/dat/"+s, true);
                sources.add(curprp);
                if(s.equals(mainprp)) prp = curprp; //keep track of the main prp.
            }

            if(!c.useProfiles)
            {
                Vector<prpfile> altdests = new Vector();
                for(String s: potssources)
                {
                    altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/"+s, true));
                }
                info.altdests = altdests;

                Vector<Uruobjectdesc> list = new Vector();

                if(this.includeAllObjects)
                {
                    for(PrpRootObject ro: prp.objects2)
                    {
                        list.add(ro.header.desc);
                    }
                }

                for(String sobj: sceneobjects)
                {
                    list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
                }

                for(String pfm: pfms)
                {
                    for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod(pfm))
                    {
                        list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
                    }
                }

                for(NameAndType objinf: plainobjects)
                {
                    list.add(prp.findObject(objinf.name, objinf.type).header.desc);
                }

                info.list = list;

                info.createObjectList = true; info.outputFileForObjectList = c.outfolder+"/dat/"+c.filename+".profile";
            }
            else
            {
                info.usePreexistingObjectList = true; info.objectListResourceName = "/files/profiles/"+c.filename+".profile";
            }

            info.dest = dest;
            info.sourceprpfiles = sources;
            info.trimDrawableSpans = true;
            info.runtests = false;
            final distiller.includeDuplicateDecider extinc = this.extrainclusions;
            info.forcedDuplicateInclusions = new distiller.includeDuplicateDecider() {
                public boolean include(Uruobjectdesc desc)
                {
                    Typeid tid = desc.objecttype;
                    String name = desc.objectname.toString();
                    if(tid==Typeid.hsGMaterial || tid==Typeid.plLayer || tid==Typeid.plLayerAnimation) return true;
                    if(tid==Typeid.plViewFaceModifier) return true;
                    if(extinc!=null && extinc.include(desc)) return true;
                    return false;
                }
            };
            prpdistiller.distiller.distillList(info);
        }
        public prpfile getdest()
        {
            return prpfile.create(destAgename, destPagename/*+"DustAdditions"*/, Pageid.createFromPrefixPagenum(destPrefix, destPagenum), Pagetype.createWithType(0));
        }
        public void save(prpfile dest)
        {
            //save the dest file.
            dest.saveAsFile(c.outfolder+"/dat/"+c.filename);
        }
        public void work()
        {
            prpfile dest = getdest();

            work2(dest);

            if(postmod!=null) postmod.Proccess(dest);

            save(dest);
        }
    }
    public static void DustGuildhall(AutoModInfo c)
    {
        DistillHelper helper = new DistillHelper(c);
        helper.destAgename = "city";
        helper.destPagename = "guildhallDustAdditions";
        helper.destPrefix = 6;
        helper.destPagenum = 81;
        helper.setMainPrp("city_District_guildhall.prp");

        helper.addMoulSources(
                "city_District_Textures.prp"
                );
        helper.addPotsSources(
                "city_District_Textures.prp",
                "city_District_guildhall.prp"
                );

        helper.addSceneobjects(
                "SfxExplosionDryEmit",
                "SfxExplosionWetEmit",
                "SfxSoReg-Canyon01-Dry",
                "SfxSoReg-Canyon01-Wet",
                "SfxSoReg-Canyon02-Dry",
                "SfxSoReg-Canyon02-Wet",
                "SfxSoReg-CaveVol-Dry",
                "SfxSoReg-CaveVol-Wet",
                "SfxSoReg-KahloPub-Dry",
                "SfxSoReg-KahloPub-Wet",
                "SfxSoReg-Library01-Dry",
                "SfxSoReg-Library01-Wet",
                "SfxSoReg-Library02-Dry",
                "SfxSoReg-Library02-Wet",
                "SfxSoReg-Opera-Dry",
                "SfxSoReg-Opera-Wet",
                "SfxSoReg-Rotunda-Dry",
                "SfxSoReg-Rotunda-Wet",
                "SfxSoReg-Stairs01-Dry",
                "SfxSoReg-Stairs01-Wet",
                "SfxSoReg-Stairs02-Dry",
                "SfxSoReg-Stairs02-Wet",
                "SfxSoReg-Stairs03-Dry",
                "SfxSoReg-Stairs03-Wet",
                "SfxSoReg-Tjuction-Dry",
                "SfxSoReg-Tjuction-Wet",
                "SmokeEmitter01",
                "SmokeEmitter02"
                );

        helper.work();
    }
    public static void DustHeek1(AutoModInfo c)
    {
        //Neighborhood_District_nb01Ayhoheek5Man1Dead.prp
        DistillHelper helper = new DistillHelper(c);
        helper.destAgename = "Neighborhood";
        helper.destPagename = "nb01Ayhoheek5Man1Dead";
        helper.destPrefix = 3;
        helper.destPagenum = 15;
        helper.setMainPrp("Neighborhood_District_nb01Ayhoheek5Man1Dead.prp");

        helper.addMoulSources(
                "Neighborhood_District_Textures.prp"
                );
        helper.addPotsSources(
                "Neighborhood_District_Textures.prp"
                );
        helper.includeEveryObjectInMainPrp();

        //helper.work();
        /*prpfile dest = helper.getdest();
        helper.work2(dest);
        uru.moulprp.x0029SoundBuffer sb = dest.findObject("NB01AhyoheekDrone_Loop.ogg", Typeid.plSoundBuffer).castTo();
        sb.flags |= 0x4; //set "Left Channel Only" flag.
        helper.save(dest);*/
        helper.work();
    }
    public static void DustHeek2(AutoModInfo c)
    {
        //Neighborhood_District_nb01Ayhoheek5Man1State.prp
        DistillHelper helper = new DistillHelper(c);
        helper.destAgename = "Neighborhood";
        helper.destPagename = "nb01Ayhoheek5Man1State";
        helper.destPrefix = 3;
        helper.destPagenum = 13;
        helper.setMainPrp("Neighborhood_District_nb01Ayhoheek5Man1State.prp");

        helper.addMoulSources(
                "Neighborhood_District_Textures.prp"
                );
        helper.addPotsSources(
                "Neighborhood_District_Textures.prp"
                );
        helper.includeEveryObjectInMainPrp();

        helper.postmod = new DistillHelper.PostMod() {
            public void Proccess(prpfile dest) {
                prpobjects.plSoundBuffer sb = dest.findObject("NB01AhyoheekDrone_Loop.ogg", Typeid.plSoundBuffer).castTo();
                sb.flags |= 0x4; //set "Left Channel Only" flag.
            }
        };
        helper.work();
    }
    public static void DustKadishGalleryAdditions(AutoModInfo c)
    {
        DistillHelper helper = new DistillHelper(c);
        helper.destAgename = "city";
        helper.destPagename = "KadishGalleryDustAdditions";
        helper.destPrefix = 6;
        helper.destPagenum = 80;
        helper.setMainPrp("city_District_KadishGallery.prp");
        helper.addMoulSources("city_District_Textures.prp");
        helper.addPotsSources("city_District_KadishGallery.prp");
        helper.addSceneobjects(
            "behKdshDoor1Ext",
            "behKdshDoor1Int",
            "behKdshDoor2Ext",
            "behKdshDoor2Int",
            "KdshDoor1",
            "KdshDoor1BtnLeftExt",
            "KdshDoor1BtnLeftInt",
            "KdshDoor1CamBlock",
            "KdshDoor1ConeCollision",
            "KdshDoor2",
            "KdshDoor2BtnLeftExt",
            "KdshDoor2BtnLeftInt",
            "KdshDoor2CamBlock",
            "KdshDoor2ConeCollision",
            "KdshGalleryDoor1Frame",
            "KdshGalleryDoor2Frame",
            "rgnKdshDoor1Close",
            "SfxKdshDoor1Emit",
            "rgnKdshDoor1OpenExt",
            "rgnKdshDoor1OpenInt",
            "rgnKdshDoor2Close",
            "SfxKdshDoor2Emit",
            "rgnKdshDoor2OpenExt",
            "rgnKdshDoor2OpenInt",
            "xrgnKdshDoor1",
            "xrgnKdshDoor2"
        );
        helper.addSceneobjects(
            "GalleryThemePlayerCollision",
            "MusicalButton",
            "MusicButtonRegion",
            "MusicPlayerClickRegion",
            "MusicPlayerPOS",
            "SfxMusicPlayerEmit",
            "StandingInstrument"
        );
        helper.addPlainobjects(
            NameAndType.createWithNameType("cSfxSoRegGallery-Verb02", Typeid.plSoftVolumeSimple),
            NameAndType.createWithNameType("SoftRegionGalleryRelevance", Typeid.plSoftVolumeSimple),
            NameAndType.createWithNameType("SoftRegionKadishGalleryVis", Typeid.plSoftVolumeSimple)
        );
        helper.extrainclusions = new distiller.includeDuplicateDecider() {
            public boolean include(Uruobjectdesc desc) {
                String name = desc.objectname.toString();
                Typeid type = desc.objecttype;
                if(type==Typeid.plSoftVolumeSimple && name.equals("cSfxSoRegGallery-Verb02")) return true;
                if(type==Typeid.plSoftVolumeSimple && name.equals("SoftRegionGalleryRelevance")) return true; //we don't actually need this one, since we're removing them anyway.
                if(type==Typeid.plSoftVolumeSimple && name.equals("SoftRegionKadishGalleryVis")) return true;
                return false;
            }
        };
        
        helper.work();
    }
    /*public static void DustKadishGalleryAdditions2(AutoModInfo c)
    {
        String agename = "city";
        String agenamePrefix = "kdshgallery";
        int prefix = 6;

        distiller.distillInfo info = new distiller.distillInfo();

        prpfile dest = prpfile.create(agename, agenamePrefix+"DustAdditions", Pageid.createFromPrefixPagenum(prefix, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/city_District_KadishGallery.prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/city_District_Textures.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/city_District_KadishGallery.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/city_District_canyon.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/city_District_Textures.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();

            //descent KI-light machine:
            for(String sobj: new String[]{
                "KdshDoor1",
                "KdshDoor2",
                "KdshDoor1CamBlock",
                "KdshDoor2CamBlock",
                "KdshDoor1ConeCollision",
                "KdshDoor2ConeCollision",
                "rgnKdshDoor1Close",
                "rgnKdshDoor1OpenExt",
                "rgnKdshDoor1OpenInt",
                "rgnKdshDoor2Close",
                "rgnKdshDoor2OpenExt",
                "rgnKdshDoor2OpenInt",
                "xrgnKdshDoor1",
                "xrgnKdshDoor2",
                "SfxKdshDoor1Emit",
                "SfxKdshDoor2Emit",
                //"SfxMusicPlayerEmit",
                
            })list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            //list.add(prp.findObject("CalStar07Dtct", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarGlare07", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarStarDecal", Typeid.plSceneObject).header.desc);

            //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythPOTSIcon"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("POTSiconRgn", Typeid.plInterfaceInfoModifier).header.desc);
            //list.add(prp.findObject("cRgnSnsIconLinker_Enter", Typeid.plObjectInVolumeDetector).header.desc);
            //list.add(prp.findObject("POTSiconRgn", Typeid.plSceneObject).header.desc);

            //calendar star
            //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar05Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar05Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStarSNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            info.list = list;

            info.createObjectList = true; info.outputFileForObjectList = c.outfolder+"/dat/"+c.filename+".profile";
        }
        else
        {
            info.usePreexistingObjectList = true; info.objectListResourceName = "/files/profiles/"+c.filename+".profile";
        }

        info.dest = dest;
        info.sourceprpfiles = sources;
        info.trimDrawableSpans = true;
        info.runtests = false;
        info.forcedDuplicateInclusions = new distiller.includeDuplicateDecider() {
            public boolean include(Uruobjectdesc desc)
            {
                Typeid tid = desc.objecttype;
                String name = desc.objectname.toString();
                if(tid==Typeid.hsGMaterial || tid==Typeid.plLayer || tid==Typeid.plLayerAnimation) return true;
                if(tid==Typeid.plViewFaceModifier) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);

        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);
    }*/
    public static void DustDescentAdditions(AutoModInfo c)
    {
        String agename = "Descent";
        String agenamePrefix = "dsnt";
        int moulSequencePrefix = 21;
        //also change the prp files below and the object lists below

        distiller.distillInfo info = new distiller.distillInfo();

        prpfile dest = prpfile.create(agename, agenamePrefix+"DustAdditions", Pageid.createFromPrefixPagenum(moulSequencePrefix, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/Descent_District_TreasureBookShaft.prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Descent_District_Textures.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Descent_District_dsntTreasureBookShaft.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Descent_District_Textures.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();

            //descent KI-light machine:
            boolean dokilight = false;
            if(dokilight)
            {
                for(String sobj: new String[]{
                    "clkKISlot",
                    "grsnKILightDistributor",
                    "KIHandInsertPOS",
                    "KILightBtnOn",
                    "KILightBtnSputter",
                    "KILightDispenserDetector",
                    "KI-LightGlow",
                    "KI-LightLogo",
                    "ParticleCollisionLow",
                    "ParticlePlaneCollision",
                    "RTKILightOn",
                    "RTKILightSputter",
                    "SfxKIDispensorIdleLEmit",
                    "SfxKIDispensorIdleREmit",
                    "SfxKILightEngageEmit",
                    "SfxSoRegDispensor-Volume",
                    "SparkEmitter",
                })list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            }

            //list.add(prp.findObject("CalStar07Dtct", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarGlare07", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarStarDecal", Typeid.plSceneObject).header.desc);

            //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythPOTSIcon"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("POTSiconRgn", Typeid.plInterfaceInfoModifier).header.desc);
            //list.add(prp.findObject("cRgnSnsIconLinker_Enter", Typeid.plObjectInVolumeDetector).header.desc);
            //list.add(prp.findObject("POTSiconRgn", Typeid.plSceneObject).header.desc);

            //calendar star
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar05Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar05Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStarSNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            info.list = list;

            info.createObjectList = true; info.outputFileForObjectList = c.outfolder+"/dat/"+c.filename+".profile";
        }
        else
        {
            info.usePreexistingObjectList = true; info.objectListResourceName = "/files/profiles/"+c.filename+".profile";
        }

        info.dest = dest;
        info.sourceprpfiles = sources;
        info.trimDrawableSpans = true;
        info.runtests = false;
        info.forcedDuplicateInclusions = new distiller.includeDuplicateDecider() {
            public boolean include(Uruobjectdesc desc)
            {
                Typeid tid = desc.objecttype;
                String name = desc.objectname.toString();
                if(tid==Typeid.hsGMaterial || tid==Typeid.plLayer || tid==Typeid.plLayerAnimation) return true;
                if(tid==Typeid.plViewFaceModifier) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);

        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.mod;

import prpobjects.prpfile;
import shared.m;
import prpobjects.Urustring;
import prpobjects.Bstr;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;
import prpobjects.Uruobjectdesc;
import prpobjects.Uruobjectref;
import java.util.Vector;
import prpobjects.Pageid;
import prpobjects.Pagetype;
import auto.mod.AutoMod.AutoModInfo;
import prpdistiller.distiller;
import shared.Vertex;
import prpobjects.Transmatrix;

public class AutoMod_Moul_Sparklies
{

    public static void DustAdditionsGenericCalendarStar(AutoModInfo c, String agename, String fourLetterAgename, int sequencePrefix, int additionsPagenum, String sourcePagename, int calendarStarNum)
    {
        DustAdditionsGenericCalendarStar(c,agename,fourLetterAgename,sequencePrefix,additionsPagenum,sourcePagename,calendarStarNum,"",sourcePagename);
    }
    public static void DustAdditionsGenericCalendarStar(AutoModInfo c, String agename, String fourLetterAgename, int sequencePrefix, int additionsPagenum, String sourcePagename, int calendarStarNum, String outpagesuffix, String altDestPagename)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create(agename, fourLetterAgename+"DustAdditions"+outpagesuffix, Pageid.createFromPrefixPagenum(sequencePrefix, additionsPagenum), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/"+agename+"_District_"+sourcePagename+".prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/"+agename+"_District_Textures.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/"+agename+"_District_"+altDestPagename+".prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/"+agename+"_District_Textures.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            String starnum = ((calendarStarNum<10)?"0":"")+Integer.toString(calendarStarNum);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Vis_3"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStarSNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythSparky-SNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            if(agename.equals("Myst")) list.add(prp.findObject("floor_closed_door", Typeid.plCoordinateInterface).header.desc);

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
        info.forcedDuplicateInclusions = new prpdistiller.distiller.includeDuplicateDecider() {
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

        if(agename.equals("Gira"))
        {
            //this changes the draw order so that the fog doesn't override these, but it may mess up other things.  In practice, I can't see a problem on Relto, so we don't have to distill to another drawablespans or use a different prp file to keep them separate.
            for(String sobj: new String[]{"CalendarStarDecal",})
            {
                try{
                    prpobjects.plDrawInterface di = dest.findObject(sobj, Typeid.plDrawInterface).castTo();
                for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
                {
                    if(sgr.subsetgroupindex!=-1)
                    {
                            prpobjects.plDrawableSpans spans = dest.findObject(sgr.span.xdesc.objectname.toString(), sgr.span.xdesc.objecttype).castTo();
                        //spans.criteria = 0x10000007; //the fog is 20000008
                        spans.criteria = 0x20000003; //0x20000007 was too high.
                    }
                }
                }catch(Exception e){m.err("exception");}
            }
        }
        if(agename.equals("Kadish"))
        {
            //CalendarStarDecal has messed up coordinateinterface, as if it should have a parent but doesn't.  Perhaps it is on another page, tied to pillar03, so that it moves up with pillar03.
            //let's translate it by the height pillar03 moves up (48 feet)
            prpobjects.plCoordinateInterface ci = dest.findObject("CalendarStarDecal", Typeid.plCoordinateInterface).castTo();
            ci.translate(0, 0, (-89.4645)-(-137.4645), false);
            ci.localToParent = ci.localToWorld;
            ci.parentToLocal = ci.worldToLocal;
        }

        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);
    }
    public static void DustAdditionsPersonal02(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("Personal02", "philDustAdditions", Pageid.createFromPrefixPagenum(12, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/philRelto_District_PhilsRelto.prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/philRelto_District_Textures.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Personal02_District_philRelto.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Personal02_District_Textures.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar10Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar10Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
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
        prpdistiller.distiller.distillList(info);

        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);
    }
    public static void DustReltoAdditions(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("Personal", "psnlDustAdditions", Pageid.createFromPrefixPagenum(13, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Personal_District_Textures.prp", true));
        //sources.add(prpfile.createFromFile(infolder+"/dat/Personal_District_BuiltIn.prp", true));
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/Personal_District_psnlMYSTII.prp", true));
        //sources.add(prpfile.createFromFile("C:\\Documents and Settings\\user\\Desktop\\output/dat/PersonalMOUL_District_Textures.prp", true));
        //sources.add(prpfile.createFromFile("C:\\Documents and Settings\\user\\Desktop\\output/dat/PersonalMOUL_District_BuiltIn.prp", true));
        //sources.add(prp = prpfile.createFromFile("C:\\Documents and Settings\\user\\Desktop\\output/dat/PersonalMOUL_District_psnlMYSTII.prp", true));

        //final prpfile prpf = prp;
        info.updateRefsCallback = new distiller.UpdateRefsCallback() {
            public void callback(distiller.distillInfo info) {
                Uruobjectdesc fogdesc = info.dest.findObject("FogLayer", Typeid.plSceneObject).header.desc;
                Uruobjectdesc newfogdesc = fogdesc.deepClone();
                newfogdesc.objectname = Urustring.createFromString("DustinFogLayer");
                info.refReassigns.put(fogdesc, newfogdesc);

                Uruobjectdesc fogdesc2 = info.dest.findObject("FogLayerBill", Typeid.plSceneObject).header.desc;
                Uruobjectdesc newfogdesc2 = fogdesc2.deepClone();
                newfogdesc2.objectname = Urustring.createFromString("DustinFogLayerBill");
                info.refReassigns.put(fogdesc2, newfogdesc2);

                //Uruobjectdesc fogdesc3 = info.dest.findObject("ImgrPhotoPlane02", Typeid.plSceneObject).header.desc;
                //Uruobjectdesc newfogdesc3 = fogdesc3.deepClone();
                //newfogdesc3.objectname = Urustring.createFromString("DustinImgrPhotoPlane02");
                //info.refReassigns.put(fogdesc3, newfogdesc3);
            }
        };

        if(!c.useProfiles)
        {
            //rename some fog objects.
            //we can't do this here, since the refReassigns would be overwritten again.  Do it later.
            /*Uruobjectdesc fogdesc = prp.findObject("FogLayer", Typeid.plSceneObject).header.desc;
            Uruobjectdesc newfogdesc = fogdesc.deepClone();
            newfogdesc.objectname = Urustring.createFromString("DustinFogLayer");
            Uruobjectdesc fogdesc2 = prp.findObject("FogLayerBill", Typeid.plSceneObject).header.desc;
            Uruobjectdesc newfogdesc2 = fogdesc2.deepClone();
            newfogdesc2.objectname = Urustring.createFromString("DustinFogLayerBill");
            info.refReassigns.put(fogdesc, newfogdesc);
            info.refReassigns.put(fogdesc2, newfogdesc2);*/

            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Personal_District_Textures.prp", true));
            //altdests.add(prpfile.createFromFile(cleanpotsfolder+"/dat/Personal_District_BuiltIn.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Personal_District_psnlMYSTII.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();

            //storm:
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage24 - Storm_6")) list.add(prp.findObject(sobj, Typeid.plSceneObject).header.desc);

            //bridge/calendar:  The 3 Buttes with a 4 are the big calendar island; the 3 with the 6 are the little intermediate island which is also in pots.
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage20- Calendar_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: new String[]{"Butte004Top01","Butte04btm01","Butte04topDecal01","Butte006Top01","Butte06btm","Butte06topDecal"})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            //bench:
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage15 - Bench_0"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage19 - Birds_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage21 - MapleTrees_11"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage21 - PineTrees_0"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage22 - Grass_0"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage23 - ErcanaPlants_7"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            //cleft pole
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage25 - CleftPole_0"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("AgeSDLHook", Typeid.plSceneObject).header.desc); //collider around wedges.

            //wedges
            for(String sobj: prp.findAllSceneobjectsThatStartWith("Wedge"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("Cylinder02", Typeid.plSceneObject).header.desc); //collider around wedges.

            //fog
            for(String sobj: new String[]{"FogLayer","FogLayerBill",})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //for(String sobj: new String[]{"DustinFogLayer","DustinFogLayerBill",})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);

            //imager
            //for(String sobj: new String[]{
                //"ImagerDetectorRgn","Imgr-KI-Logo02","ImgrPhotoPlane02",
            //})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            String objs = "";
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythYeeshaPage12 - Imager_8"))
            {
                //objs
                list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            }
            //did not include Map #411_LayerAnim_(plLayerAnimation)or Map #411_anim(plLayerAnimation)
            //did not remove old PythonFileMod.

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
        info.forcedDuplicateInclusions = new prpdistiller.distiller.includeDuplicateDecider() {

            public boolean include(Uruobjectdesc desc) {
                String name = desc.objectname.toString();
                Typeid type = desc.objecttype;
                /*if(desc.objectname.toString().startsWith("Map #7370"))
                {
                    return true;
                }*/
                //if(true) return true;
                //if(type==Typeid.plPythonFileMod) return true;
                if(type==Typeid.plViewFaceModifier) return true; //they all have similar names and are small.
                if(type==Typeid.hsGMaterial || type==Typeid.plLayer || type==Typeid.plLayerAnimation) return true;
                if(name.startsWith("Butte04")||name.startsWith("Butte004")||name.startsWith("Butte06")||name.startsWith("Butte006")) return true;
                if(name.equals("AgeSDLHook")) return true;
                if(name.equals("FogLayer") || name.equals("FogLayerBill") || name.equals("DustinFogLayer") || name.equals("DustinFogLayerBill")) return true;
                
                //for the imager:
                if(name.equals("Imgr-KI-Logo02") && type==Typeid.plSceneObject) return true;
                if(name.equals("ImgrPhotoPlane02") && type==Typeid.plSceneObject) return true;
                if(name.equals("cPythYeeshaPage12 - Imager_8") && type==Typeid.plPythonFileMod) return true; //include a duplicate of this, so that we can modify it later.
                //if(name.startsWith("Butte")) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);

        //this changes the draw order so that the fog doesn't override these, but it may mess up other things.  In practice, I can't see a problem on Relto, so we don't have to distill to another drawablespans or use a different prp file to keep them separate.
        for(String sobj: new String[]{"Butte04btm01","Butte06btm",})
        {
            try{
                prpobjects.plDrawInterface di = dest.findObject(sobj, Typeid.plDrawInterface).castTo();
            for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
            {
                if(sgr.subsetgroupindex!=-1)
                {
                        prpobjects.plDrawableSpans spans = dest.findObject(sgr.span.xdesc.objectname.toString(), sgr.span.xdesc.objecttype).castTo();
                    spans.criteria = 0x20000007; //the fog is 20000008
                }
            }
            }catch(Exception e){m.err("exception");}
        }

        //hack to make flickery objects visible by adding a pythonfilemod and a python file to force them as shown.
        //fixed by fixing plSpaceTrees instead.
        /*for(String sobj: new String[]{"Butte004Top01","Butte04btm01","Butte04topDecal01","Butte006Top01","Butte06btm","Butte06topDecal","WedgeRingsBase"})
        {
            try{
            x0001Sceneobject so = prp.findObject(sobj,Typeid.plSceneObject).castTo();
            AddXDustShowToObject(dest, so);
            }catch(Exception e){m.err("exception");}
        }*/

        //change cleft pole python page:
        try{
            prpobjects.plPythonFileMod pfm = dest.findObject("cPythBahroPoles", Typeid.plPythonFileMod).castTo();
        pfm.pyfile = Urustring.createFromString("psnlBahroPolesMOUL");
        /*Vector<uru.moulprp.x00A2Pythonfilemod.Pythonlisting> listings = new Vector();
        for(int i=0;i<pfm.listcount;i++)
        {
            uru.moulprp.x00A2Pythonfilemod.Pythonlisting listing = pfm.listings.get(i);
            if(listing.index >= 48)
            {
                listings.add(listing);
            }
        }
        pfm.clearListings();
        for(uru.moulprp.x00A2Pythonfilemod.Pythonlisting listing: listings)
        {
            //pfm.addListing(listing);
        }*/
        }catch(Exception e){m.err("exception");}

        /*try{
        uru.moulprp.x00A2Pythonfilemod pfm = dest.findObject("cPythClftLinkBookGUI", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm.listcount;i++)
        {
            if(pfm.listings.get(i).index==4)
            {
                pfm.listings.get(i).xString = Bstr.createFromString("Cleft");
            }
        }
        }catch(Exception e){m.err("exception");}*/

        //switch calendar yeesha page number from 20 to 26:
        try{
            prpobjects.plPythonFileMod pfm = dest.findObject("cPythYeeshaPage20- Calendar_1", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm.listcount;i++)
        {
            if(i%2==0)
            {
                if(pfm.listings.get(i).xInteger!=20)
                {
                    m.err("Unexpected.");
                }
                pfm.listings.get(i).xInteger = 26; //switch calendar page from 20 to 26.
            }
        }
        }catch(Exception e){m.err("exception");}
        //switch bench yeesha page number from 15 to 18:
        try{
            prpobjects.plPythonFileMod pfm2 = dest.findObject("cPythYeeshaPage15 - Bench_0", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm2.listcount;i++)
        {
            if(i%2==0)
            {
                if(pfm2.listings.get(i).xInteger!=15)
                {
                    m.err("Unexpected.");
                }
                pfm2.listings.get(i).xInteger = 18; //switch calendar page from 20 to 26.
            }
        }
        }catch(Exception e){m.err("exception");}
        //switch activation states for cleft pole from "0,2,4" to "1,3"
        try{
            prpobjects.plPythonFileMod pfm3 = dest.findObject("cPythYeeshaPage25 - CleftPole_0", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm3.listcount;i++)
        {
            if(i%2==1)
            {
                if(!pfm3.listings.get(i).xString.toString().equals("0,2,4"))
                {
                    m.err("Unexpected.");
                }
                pfm3.listings.get(i).xString = Bstr.createFromString("1,3");
            }
        }
        }catch(Exception e){m.err("exception");}

        //add flag to imager for the Offline-KI to use.
        prpobjects.plPythonFileMod pfm4 = dest.findObject("cPythYeeshaPage12 - Imager_8", Typeid.plPythonFileMod).castTo();
        pfm4.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithInteger(8, 1));

        //take away the mass from the CalendarStoneProxyXX
        /*try{
            for(int i=1;i<=12;i++)
            {
                String prox = "CalendarStoneProxy"+(i<10?"0":"")+Integer.toString(i);
                uru.moulprp.PlHKPhysical phys = dest.findObject(prox, Typeid.plHKPhysical).castTo();
                phys.convertPXtoHK();
                phys.havok.mass = Flt.zero();
            }
        }catch(Exception e){m.err("exception");}*/

        //change fog names.
        /*m.time();
        HashMap<Uruobjectdesc, Uruobjectdesc> newrefs = new HashMap();
        Uruobjectdesc fogdesc = dest.findObject("FogLayer", Typeid.plSceneObject).header.desc;
        Uruobjectdesc newfogdesc = fogdesc.deepClone();
        newfogdesc.objectname = Urustring.createFromString("DustinFogLayer");
        Uruobjectdesc fogdesc2 = dest.findObject("FogLayerBill", Typeid.plSceneObject).header.desc;
        Uruobjectdesc newfogdesc2 = fogdesc2.deepClone();
        newfogdesc2.objectname = Urustring.createFromString("DustinFogLayerBill");
        newrefs.put(fogdesc, newfogdesc);
        newrefs.put(fogdesc2, newfogdesc2);
        distiller.updateAllReferences(dest, newrefs);
        m.time();*/


        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);

    }
    
    public static void DustErcanaAdditions(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("Ercana", "ercaDustAdditions", Pageid.createFromPrefixPagenum(15, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/Ercana_District_Canyon.prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Ercana_District_Textures.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Ercana_District_Canyon.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Ercana_District_Textures.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            list.add(prp.findObject("YeeshaPageErcaPlants", Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("YeeshaPageErcaPlantsDecal", Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("RgnYeeshaPageErcaPlants", Typeid.plSceneObject).header.desc);


            //list.add(prp.findObject("CalStar07Dtct", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarGlare07", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("CalendarStarDecal", Typeid.plSceneObject).header.desc);

            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythPOTSIcon"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("POTSiconRgn", Typeid.plInterfaceInfoModifier).header.desc);
            //list.add(prp.findObject("cRgnSnsIconLinker_Enter", Typeid.plObjectInVolumeDetector).header.desc);
            list.add(prp.findObject("POTSiconRgn", Typeid.plSceneObject).header.desc);

            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar07Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar07Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
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
    public static void DustAhnySphere02Additions(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("AhnySphere02", "ahny2DustAdditions", Pageid.createFromPrefixPagenum(26, 80), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp;
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Ahnonay_District_Textures.prp", true));
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Ahnonay_District_BuiltIn.prp", true));
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/Ahnonay_District_MaintRoom02.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/AhnySphere02_District_Textures.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/AhnySphere02_District_BuiltIn.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/AhnySphere02_District_MaintRoom02.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/AhnySphere02_District_Sphere02.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            list.add(prp.findObject("YeeshaPage24", Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("YeeshaPage24Decal", Typeid.plSceneObject).header.desc);
            list.add(prp.findObject("RgnYeeshaPageStorm", Typeid.plSceneObject).header.desc);
            //list.add(prp.findObject("Dummy01Relocator", Typeid.plSceneObject).header.desc); //do we need this one?
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
                //include these two because they used the same name but changed the page texture for moul.
                if(tid==Typeid.hsGMaterial && name.equals("Material #10969")) return true;
                if(tid==Typeid.plLayer && name.equals("Map #7392")) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);




        //translate the page since the entire sphere was shifted over for moul to accomodate all the sphere's in one age. Also, translate 3.5 units to the right so it is not in the same place as the other.
        double yoff = 3.5; //move the page a little to the right.
        double x = 43.0636-32.8057; double y = 548.7101-665.3181-yoff; double z = 2.5576-0.3330;
        prpobjects.plCoordinateInterface ci = dest.findObject("YeeshaPage24", Typeid.plCoordinateInterface).castTo();
        ci.translate(x,y,z, true);
        ci = dest.findObject("YeeshaPage24Decal", Typeid.plCoordinateInterface).castTo();
        ci.translate(x,y,z, true);
        ci = dest.findObject("VisRegionMaintRm02", Typeid.plCoordinateInterface).castTo();
        ci.translate(x,y,z, true);
        //uru.moulprp.x0016DrawInterface di = dest.findObject("YeeshaPage24", Typeid.plDrawInterface).castTo();
        //di.visregioncount = 0; di.visibleregion = new Uruobjectref[]{};
        //di = dest.findObject("YeeshaPage24Decal", Typeid.plDrawInterface).castTo();
        //di.visregioncount = 0; di.visibleregion = new Uruobjectref[]{};

        //ci = dest.findObject("RgnYeeshaPageStorm", Typeid.plCoordinateInterface).castTo();
        //ci.translate(x, y, z, true);
        prpobjects.plHKPhysical phys = dest.findObject("RgnYeeshaPageStorm", Typeid.plHKPhysical).castTo();
        phys.convertPXtoHK();
        phys.havok.position = Vertex.zero();
        phys.havok.transformVertices(Transmatrix.createFromVector2(x,y,z));

        prpobjects.plHKPhysical phys2 = dest.findObject("YeeshaPage24", Typeid.plHKPhysical).castTo();
        phys2.convertPXtoHK();
        phys2.havok.position = Vertex.zero();
        phys2.havok.transformVertices(Transmatrix.createFromVector2(x,y,z));

        //rename foglayer and foglayerbill objects
        //dest.renameObject("FogLayer",Typeid.plSceneObject,"DustinFogLayer");
        //dest.renameObject("FogLayerBill",Typeid.plSceneObject,"DustinFogLayerBill");

        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);

    }
    public static void DustCleftAdditions(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("Cleft", "clftDustAdditions", Pageid.createFromPrefixPagenum(7, 120), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp; prpfile prp2;
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Cleft_District_Textures.prp", true));
        sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/Cleft_District_Desert.prp", true));
        //sources.add(prp2 = prpfile.createFromFile(infolder+"/dat/Cleft_District_BookRoom.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Cleft_District_Textures.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Cleft_District_Desert.prp", true));
            //altdests.add(prpfile.createFromFile(cleanpotsfolder+"/dat/Cleft_District_BookRoom.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            for(String sobj: new String[]{"campfire","campfire01","CampfireDecal","CampfireRock54",})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: new String[]{"RgnYeeshaPage20","RgnYeeshaPageBirds","YeeshaPage20","YeeshaPageBirds","YeeshaPageBirdsDecal","YeeshaPageIcon20"})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            //for(String sobj: new String[]{"ZandiChair29","ZandiChair36","ZandiChair42",})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: prp.findAllSceneobjectsThatStartWith("ZandiChair"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
            for(String sobj: new String[]{"Box01","Object01","Torus01",})list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc); //these are colliders around the fireplace (at least some of them, but probably all of them.)
            //for(String sobj: new String[]{"RgnYeeshaPage01","YeeshaPage01","YeeshaPage01Decal",})list.add(prp2.findObject(sobj,Typeid.plSceneObject).header.desc);

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
        info.forcedDuplicateInclusions = new prpdistiller.distiller.includeDuplicateDecider() {

            public boolean include(Uruobjectdesc desc) {
                String name = desc.objectname.toString();
                Typeid type = desc.objecttype;
                //if(type==Typeid.plPythonFileMod) return true;
                if(type==Typeid.plViewFaceModifier) return true; //they all have similar names and are small.
                if(type==Typeid.hsGMaterial || type==Typeid.plLayer || type==Typeid.plLayerAnimation) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);

        //switch calendar yeesha page number from 20 to 26:
        try{
            prpobjects.plPythonFileMod pfm = dest.findObject("cPythYeeshaPage20_0", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm.listcount;i++)
        {
            if(i%2==1)
            {
                if(pfm.listings.get(i).xInteger!=20)
                {
                    m.err("Unexpected.");
                }
                pfm.listings.get(i).xInteger = 26; //switch calendar page from 20 to 26.
            }
        }
            prpobjects.plPythonFileMod pfm2 = dest.findObject("cPythYeeshaPage20_1", Typeid.plPythonFileMod).castTo();
        for(int i=0;i<pfm2.listcount;i++)
        {
            if(i%2==1)
            {
                if(pfm2.listings.get(i).xInteger!=20)
                {
                    m.err("Unexpected.");
                }
                pfm2.listings.get(i).xInteger = 26; //switch calendar page from 20 to 26.
            }
        }
        }catch(Exception e){m.err("exception");}


        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);

    }
    public static void DustCleftAdditions2(AutoModInfo c)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();

        prpfile dest = prpfile.create("Cleft", "clftDustAdditions2", Pageid.createFromPrefixPagenum(7, 121), Pagetype.createWithType(0));
        dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        prpfile prp; prpfile prp2;
        sources.add(prpfile.createFromFile(c.infolder+"/dat/Cleft_District_Textures.prp", true));
        //sources.add(prp = prpfile.createFromFile(infolder+"/dat/Cleft_District_Desert.prp", true));
        sources.add(prp2 = prpfile.createFromFile(c.infolder+"/dat/Cleft_District_BookRoom.prp", true));

        if(!c.useProfiles)
        {
            Vector<prpfile> altdests = new Vector();
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Cleft_District_Textures.prp", true));
            altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/Cleft_District_BookRoom.prp", true));
            info.altdests = altdests;

            Vector<Uruobjectdesc> list = new Vector();
            for(String sobj: new String[]{"RgnYeeshaPage01","YeeshaPage01","YeeshaPage01Decal",})list.add(prp2.findObject(sobj,Typeid.plSceneObject).header.desc);

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
        info.forcedDuplicateInclusions = new prpdistiller.distiller.includeDuplicateDecider() {

            public boolean include(Uruobjectdesc desc) {
                String name = desc.objectname.toString();
                Typeid type = desc.objecttype;
                //if(type==Typeid.plPythonFileMod) return true;
                if(type==Typeid.plViewFaceModifier) return true; //they all have similar names and are small.
                if(type==Typeid.hsGMaterial || type==Typeid.plLayer || type==Typeid.plLayerAnimation) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);

        //set this page to only be visible when the relto book isn't.
        for(String sobj: new String[]{"RgnYeeshaPage01","YeeshaPage01","YeeshaPage01Decal",})
        {
            prpobjects.plSceneObject so = dest.findObject(sobj,Typeid.plSceneObject).castTo();
            //AddXAgeSDLBoolShowHideToObject(dest, so, "clftYeeshaBookVis", true);
            //AddXAgeSDLBoolShowHideToObject(dest, so, "clftZandiVis", true);
            AutoMod_Shared.AddXDustChronicleShowHideToObject(dest, so, "CleftSolved","yes", false);
        }


        //save the dest file.
        dest.saveAsFile(c.outfolder+"/dat/"+c.filename);

    }
}

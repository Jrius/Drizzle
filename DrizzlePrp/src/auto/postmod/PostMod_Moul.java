/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import auto.fixCraters;
import auto.hackFactory;
import prpobjects.*;
import java.util.Vector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import shared.m;

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
    
    public static void PostMod_FixEnablePhysicalMsg(prpfile prp)
    {
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plResponderModifier))
        {
            prpobjects.plResponderModifier resp = ro.castTo();
            
            for (plResponderModifier.PlResponderState state: resp.messages)
            {
                for (plResponderModifier.PlResponderCmd c: state.commands)
                {
                    Typeid msgtype = c.message.type;
                    
                    if (msgtype == Typeid.plEnableMsg)
                    {
                        PrpMessage.PlEnableMsg msg = c.message.castTo();
                        if (msg.cmd.get(0) == (PrpMessage.PlEnableMsg.kDisable | PrpMessage.PlEnableMsg.kPhysical))
                        {
                            m.msg("Replacing incompatible enable msg kDisable kPhysical");
                            PrpMessage.PlSimSuppressMsg replacement = PrpMessage.PlSimSuppressMsg.createEmpty();
                            replacement.parent = msg.parent;
                            replacement.b1 = 1;
                            c.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, replacement);
                        }
                        if (msg.cmd.get(0) == (PrpMessage.PlEnableMsg.kEnable | PrpMessage.PlEnableMsg.kPhysical))
                        {
                            m.msg("Replacing incompatible enable msg kEnable kPhysical");
                            PrpMessage.PlSimSuppressMsg replacement = PrpMessage.PlSimSuppressMsg.createEmpty();
                            replacement.parent = msg.parent;
                            replacement.b1 = 0;
                            c.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, replacement);
                        }
                    }
                }
            }
        }
    }

    public static void PostMod_RenameAnimations(prpfile prp, String newAgename)
    {
        if(newAgename.toLowerCase().equals("globalanimations"))
        {
            if(prp.header.pagename.toString().equals("FemaleDanceMOUL"))
            {
                plEmoteAnim ea = prp.findObject("FemaleDance", Typeid.plEmoteAnim).castTo();
                ea.parent.parent.name = Urustring.createFromString("FemaleDanceMOUL");
            }
            if(prp.header.pagename.toString().equals("MaleDanceMOUL"))
            {
                plEmoteAnim ea = prp.findObject("MaleDance", Typeid.plEmoteAnim).castTo();
                ea.parent.parent.name = Urustring.createFromString("MaleDanceMOUL");
            }
        }
    }
    public static void PostMod_DisableSubworlds(prpfile prp)
    {
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            plHKPhysical phys = ro.castTo();
            if(phys.physx.subworld.hasref())
            {
                phys.physx.subworld = Uruobjectref.none();
            }
        }
    }
    public static void PostMod_FixSubworlds(prpfile prp)
    {
        class Subworlds
        {
            class SubworldInfo
            {
                Vector<PrpRootObject> subworldregion = new Vector();
                Vector<PrpRootObject> subworldphysics = new Vector();
                Vector<plSubWorldMsg> subworldmsgs = new Vector();
                Uruobjectref subworld;
            }
            public java.util.LinkedHashMap<Uruobjectref,SubworldInfo> worlds = new java.util.LinkedHashMap();
            private SubworldInfo get(Uruobjectref subworld)
            {
                SubworldInfo r = worlds.get(subworld);
                if(r==null)
                {
                    r = new SubworldInfo();
                    r.subworld = subworld;
                    worlds.put(subworld, r);
                }
                return r;
            }
            public void addSubworldRegion(Uruobjectref subworld, PrpRootObject subworldregion)
            {
                get(subworld).subworldregion.add(subworldregion);
            }
            public void addSubworldPhysics(Uruobjectref subworld, PrpRootObject subworldphysics)
            {
                get(subworld).subworldphysics.add(subworldphysics);
            }
            public void addSubworldMsg(Uruobjectref subworld, plSubWorldMsg subworldmsg)
            {
                get(subworld).subworldmsgs.add(subworldmsg);
            }
        }

        Subworlds subworlds = new Subworlds();
        //find subworlds in plSubworldRegionDetectors
        //java.util.LinkedHashSet<Uruobjectref> subworlds = new java.util.LinkedHashSet();
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSubworldRegionDetector))
        {
            plSubworldRegionDetector rd = ro.castTo();
            //subworlds.add(rd.sub); //subworld
            subworlds.addSubworldRegion(rd.sub, ro);
        }

        //find subworlds in plHKPhysicals
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            plHKPhysical phys = ro.castTo();
            //phys.convertPXtoHK();
            if(phys.physx !=null && phys.physx.subworld.hasref())
            {
                //subworlds.add(phys.physx.subworld);
                subworlds.addSubworldPhysics(phys.physx.subworld, ro);
            }
        }

        //find subworld msgs
        //m.marktime("start");
        /*for(plSubWorldMsg ro: prp.FindAllInstances(plSubWorldMsg.class))
        {
            if(ro.subworld.hasref())
            {
                subworlds.addSubworldMsg(ro.subworld, ro);
            }
        }*/
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plResponderModifier))
        {
            plResponderModifier resp = ro.castTo();
            for(uruobj obj: resp.findMessagesOfType(Typeid.plSubWorldMsg))
            {
                plSubWorldMsg msg = (plSubWorldMsg)obj;
                if(msg.subworld.hasref())
                {
                    subworlds.addSubworldMsg(msg.subworld,msg);
                }
            }
        }
        //m.marktime("end");

        for(Subworlds.SubworldInfo swi: subworlds.worlds.values())
        {
            //fix plHKSubworld
            if(swi.subworld.hasref())
            {
                //plSceneObject so = prp.findObjectWithRef(swi.subworld).castTo();
                PrpRootObject so_ro = prp.findObjectWithRef(swi.subworld);
                //if(so_ro==null)
                //{
                //    m.warn("Subworld not found in this prp: ",swi.subworld.toString());
                //    continue;
                //}
                plSceneObject so = so_ro.castTo();
                plHKSubWorld sw = plHKSubWorld.createWithSceneobject(swi.subworld);
                PrpRootObject sw_ro = prp.addObject(Typeid.plHKSubWorld, swi.subworld.xdesc.objectname.toString(), sw);
                Uruobjectref sw_ref = sw_ro.getref();

                //fix subworld sceneobject
                so.interfaces.add(sw_ref);

                //fix physics
                for(PrpRootObject phys_ro: swi.subworldphysics)
                {
                    plHKPhysical phys = phys_ro.castTo();
                    phys.physx.subworld = sw_ref;
                    //phys.physx.subworld = Uruobjectref.none();
                }

                //fix subworld regions
                for(PrpRootObject reg_ro: swi.subworldregion)
                {
                    plSubworldRegionDetector reg = reg_ro.castTo();
                    //reg.sub = sw_ref;
                }

                //fix subworld msgs
                for(plSubWorldMsg msg: swi.subworldmsgs)
                {
                    //msg.subworld = sw_ref; //neither one crashes on link-in.  We need more info.
                }

                //find children
                /*plCoordinateInterface ci = prp.findObjectWithRef(so.coordinateinterface).castTo();
                for(PrpRootObject child_ro: prp.getAllChildren(swi.subworld))
                {
                    plSceneObject child_so = child_ro.castTo();
                    plHKPhysical phys = child_so.getPhysics(prp);

                    //attach subworld.
                    if(phys!=null)
                        phys.physx.subworld = sw_ro.getref();
                }*/
            }
            else
            {
                //subworld is none, so starting/default subworld. I.e. this is an exit region.
                int dummy=0;
            }
        }
        /*for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            plHKPhysical phys = ro.castTo();
            if(phys.physx!=null && phys.physx.subworld.hasref())
            {
                PrpRootObject so_ro = prp.findObjectWithRef(phys.physx.subworld);
                if(so_ro.header.objecttype!=Typeid.plSceneObject)
                    m.throwUncaughtException("unexpected");
                plSceneObject so = so_ro.castTo();
                Uruobjectref sw_ref = so.interfaces.find(Typeid.plHKSubWorld);
                if(sw_ref==null)
                    m.throwUncaughtException("unexpected");
                phys.physx.subworld = sw_ref;
            }
        }*/

        //do we need to change it in plSubWorldMsg(probably embedded in a plResponderModifier) like we did with plHKPhysicals?
        
    }
    public static void PostMod_FixMinkata(prpfile prp, String finalname, String outfolder)
    {
        if(finalname.toLowerCase().equals("minkata") && prp.header.pagename.toString().toLowerCase().equals("minkexteriorday"))
        {
            fixCraters.fixCraters(prp, finalname, "minkDusttestDay", outfolder, Pageid.createFromPrefixPagenum(42, 11));

            //go ahead and make the dummy prp while we're at it.
            hackFactory.createMinkataClusterGroupPythonMod(outfolder);

            /*PrpRootObject[] clustergroups = prp.FindAllObjectsOfType(Typeid.plClusterGroup);
            for(PrpRootObject clustergroup: clustergroups)
            {
                uru.moulprp.PlClusterGroup plcg = clustergroup.castTo();
                plcg.count2 = 0;
                plcg.fRegions = new Uruobjectref[0];
            }*/

            //fix link in point
            //auto.mod.AutoMod_Translate.TranslateObject(prp, "LinkInPointDefault", 0, 0, 1.0f);

        }

        if(finalname.toLowerCase().equals("minkata") && prp.header.pagename.toString().toLowerCase().equals("minkcameras"))
        {
            //fix link in point
            auto.mod.AutoMod_Translate.TranslateObject(prp, "LinkInPointDefault", 0, 0, 1.0f);
        }

        if(finalname.toLowerCase().equals("minkata") && prp.header.pagename.toString().toLowerCase().equals("minkexteriornight"))
        {
            fixCraters.fixCraters(prp, finalname, "minkDusttestNight", outfolder, Pageid.createFromPrefixPagenum(42, 12));

            //fix link in point
            auto.mod.AutoMod_Translate.TranslateObject(prp, "LinkInPointDefault", 0, 0, 1.0f);
        }

            //disable visregion.
            /*cgroup.count2 = 0; //disable visregion.
            cgroup.refs = new Uruobjectref[0]; //disable visregion.*/

            //replace material.
            /*PrpRootObject[] objs = prputils.FindAllObjectsWithName(prp, "RegMoundNew_3");
            uru.moulprp.x0007Material mat = objs[0].castTo();
            Uruobjectdesc desc = objs[0].header.desc;
            cgroup.ref = Uruobjectref.createFromUruobjectdesc(desc);*/

            //change material flags.
            /*PrpRootObject obj = cgroup.ref.xdesc.getObjectDescribed(prp);
            uru.moulprp.x0007Material mat = obj.castTo();
            for(Uruobjectref layerref: mat.layerrefs)
            {
                PrpRootObject layer = layerref.xdesc.getObjectDescribed(prp);
                uru.moulprp.x0006Layer layer2 = layer.castTo();
                layer2.flags1 = 8;
                int dummy=0;
            }*/
            int dummy=0;
        //}
    }
    public static void PostMod_TranslateSmartseeks(prpfile prp)
    {
        PrpRootObject[] objs = prputils.FindAllObjectsOfType(prp, Typeid.plSceneObject);
        for(PrpRootObject obj: objs)
        {
            prpobjects.plSceneObject so = obj.castTo();
            for(Uruobjectref ref: so.modifiers)
            {
                if(ref.hasref()&&ref.xdesc.objecttype==Typeid.plOneShotMod)
                {
                    prpobjects.plOneShotMod osm = prp.findObjectWithDesc(ref.xdesc).castTo();
                    if(osm.smartseek!=0)
                    {
                        //found it!
                        Uruobjectref coordsref = so.coordinateinterface;
                        if(coordsref.hasref())
                        {
                            m.msg("Translating smartseek for object... ",obj.header.desc.toString());
                            prpobjects.plCoordinateInterface coords = prp.findObjectWithDesc(coordsref.xdesc).castTo();
                            Transmatrix m = coords.localToParent;
                            RealMatrix m2 = m.convertToMatrix();
                            //org.apache.commons.math.linear.RealMatrixImpl b;
                            double[][] rawdata = m2.getData();
                            double scalar = 0.0; //set this to determine the number of feet back from the cloth to be.  A negative value will move you closer.
                            //double height = 0.65; //the height to translate the avatar.  1.0 works.  0.6 is too small for the tsogal cloth on the back of the hood book structure.  0.8 seems just right.
                            double height = 0.8; // .65 seems enough in all Ages, but I guess Shard admins would prefer we keep the .8 value just in case...
                            rawdata[0][3] -= scalar*rawdata[1][0];
                            rawdata[1][3] += scalar*rawdata[0][0];
                            rawdata[2][3] += height; //set this to move the avatar up.  A negative value will move it down.
                            m2 = new RealMatrixImpl(rawdata);
                            coords.localToParent = Transmatrix.createFromMatrix(m2);
                            coords.localToWorld = coords.localToParent;
                            RealMatrix m3 = m2.inverse();
                            coords.parentToLocal = Transmatrix.createFromMatrix(m3);
                            coords.worldToLocal = coords.parentToLocal;
                        }
                    }
                }
            }
        }
    }
    
    private static void transMtxZ(prpobjects.plCoordinateInterface coords, double height)
    {
        Transmatrix m = coords.localToParent;
        RealMatrix m2 = m.convertToMatrix();
        double[][] rawdata = m2.getData();
        rawdata[2][3] += height;
        m2 = new RealMatrixImpl(rawdata);
        coords.localToParent = Transmatrix.createFromMatrix(m2);
        coords.localToWorld = coords.localToParent;
    }
    
    public static void PostMod_TranslateJalakWarpPoints(prpfile prp)
    {
        // Translates warppoints up for objects so they don't sink through
        // It even works fine without, because the Python script handles object physics pretty well B)
        
        m.msg("Translating widgets warp points");
        transMtxZ((plCoordinateInterface) prp.findObject("Ramp0Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Ramp1Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Ramp2Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Ramp3Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Ramp4Warp", Typeid.plCoordinateInterface).castTo(), 5);
        
        transMtxZ((plCoordinateInterface) prp.findObject("Sphere0Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Sphere1Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Sphere2Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Sphere3Warp", Typeid.plCoordinateInterface).castTo(), 5);
        transMtxZ((plCoordinateInterface) prp.findObject("Sphere4Warp", Typeid.plCoordinateInterface).castTo(), 5);
    }
}

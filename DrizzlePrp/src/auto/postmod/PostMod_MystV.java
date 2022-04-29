/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import prpobjects.*;
import shared.*;
import java.util.Vector;

public class PostMod_MystV
{
    private static class Draggable
    {
        // Name of the draggable object
        public String draggableName;
        // SDL variable bound to this object (none if it's a oneshot anim)
        public String sdlName;
        // True for simple boolean levers where sdl=1 at t=0 and sdl=0 at t=1
        public boolean reverse;
        // Whether the draggable loops (Noloben only)
        public boolean loop;
        // True if the clickable should be reenabled once the anim is done. False if an external script handles this.
        // (Only valid for ping-pong boolean levers. Oneshot and multistate handle this differently.)
        public boolean autoReenable;
        // "{sdl0: t0, sdl1: t1, etc}"
        public String sdlValueToTime;

        // Simple oneshot clickable
        public Draggable(String draggableName)
        {
            this.draggableName = draggableName;
        }

        // Two-states boolean clickables that ping-pongs between two states
        public Draggable(String draggableName, String sdlName, boolean reverse, boolean autoReenable)
        {
            this.draggableName = draggableName;
            this.sdlName = sdlName;
            this.reverse = reverse;
            this.autoReenable = autoReenable;
        }

        // Multi-state control (usually has extra up/down clickables)
        public Draggable(String draggableName, String sdlName, boolean loop, String sdlValueToTime)
        {
            this.draggableName = draggableName;
            this.sdlName = sdlName;
            this.reverse = reverse;
            this.sdlValueToTime = sdlValueToTime;
        }
    }

    private static class PrpDraggables
    {
        public String ageName;
        public String pageName;
        public List<Draggable> draggables;

        public PrpDraggables(String ageName, String pageName, List<Draggable> draggables)
        {
            this.ageName = ageName;
            this.pageName = pageName;
            this.draggables = draggables;
        }
    }

    private static List<PrpDraggables> prpDraggablesList = Arrays.asList(
            new PrpDraggables("DescentMystV", "dsntPostShaftNodeAndTunnels", Arrays.asList(
                    new Draggable("FanRoomCrank_Drag", "FanOn", false, false),
                    new Draggable("handle01", "ShaftDoorOpen", false, false)
            )),
            new PrpDraggables("DescentMystV", "dsntShaftGeneratorRoom", Arrays.asList(
                    new Draggable("GenCrank", "GeneratorOn", true, true)
            )),
            new PrpDraggables("DescentMystV", "dsntTianaCaveNode2", Arrays.asList(
                    new Draggable("handle01", "TCaveDoor01Open", false, false)
            )),
            new PrpDraggables("DescentMystV", "dsntTianaCaveTunnel3", Arrays.asList(
                    new Draggable("handle02", "TCaveDoor01Open", false, false)
            )),
            new PrpDraggables("DescentMystV", "dsntUpperShaft", Arrays.asList(
                    new Draggable("ElevA_PullHandle"),
                    new Draggable("ElevB_PullHandle"),
                    new Draggable("LeverElevABottom"),
                    new Draggable("LeverElevATop"),
                    new Draggable("LeverElevBBottom"),
                    new Draggable("LeverElevBTop")
            )),
            new PrpDraggables("Laki", "Exterior", Arrays.asList(
//                    new Draggable("WindmillHeightLever"),
//                    new Draggable("WindmillRotateLever"),
//                    new Draggable("WindmillGearLever"),
                    new Draggable("HutRopeBig1", "boolBigRock1", false, false),
                    new Draggable("HutRopeBig2", "boolBigRock2", false, false),
                    new Draggable("HutRopeMed1", "boolMedRock1", false, false),
                    new Draggable("HutRopeMed2", "boolMedRock2", false, false),
                    new Draggable("HutRopeMed3", "boolMedRock3", false, false),
                    new Draggable("HutRopeSmall1", "boolSmallRock1", false, false),
                    new Draggable("HutRopeSmall2", "boolSmallRock2", false, false)
            )),
//            new PrpDraggables("Laki", "LakiMaze", Arrays.asList(
//                    new Draggable("ElevLeverLwr", DraggableType.Toggle, null),
//                    new Draggable("ElevLeverUpr", DraggableType.Toggle, null)
//            )),
            new PrpDraggables("MystMystV", "Island", Arrays.asList(
                    new Draggable("Marker01Base")
            )),
            new PrpDraggables("Siralehn", "Exterior", Arrays.asList(
                    new Draggable("LadderPullMaster"),
                    new Draggable("PillarTop01", "TurnableRock01", true, "{0: 0.0, 1: 0.833, 2: 1.666, 3: 2.5, 4: 3.333, 5: 4.166, 6: 5, 7: 5.833}"),
                    new Draggable("PillarTop02", "TurnableRock02", true, "{0: 0.0, 1: 0.833, 2: 1.666, 3: 2.5, 4: 3.333, 5: 4.166, 6: 5, 7: 5.833}"),
                    new Draggable("PillarTop03", "TurnableRock03", true, "{0: 0.0, 1: 0.833, 2: 1.666, 3: 2.5, 4: 3.333, 5: 4.166, 6: 5, 7: 5.833}"),
                    new Draggable("PillarTop04", "TurnableRock04", true, "{0: 0.0, 1: 0.833, 2: 1.666, 3: 2.5, 4: 3.333, 5: 4.166, 6: 5, 7: 5.833}")
            )),
            new PrpDraggables("Tahgira", "Exterior", Arrays.asList(
                    new Draggable("ThermValveDummyE1", "boolThermalE1", false, true),
                    new Draggable("ThermValveDummyE2", "boolThermalE2", false, true),
                    new Draggable("ThermValveDummyE3", "boolThermalE3", false, true),
                    new Draggable("ThermValveDummyW1", "boolThermalW1", false, true),
                    new Draggable("ThermValveDummyW2", "boolThermalW2", false, true),
                    new Draggable("ThermValveDummyW3", "boolThermalW3", false, true),
                    new Draggable("v-FieldValve_E1", "byteFieldE1", false, "{1: 0, 0: 0.5, 2: 1}"),
                    new Draggable("v-FieldValve_E2", "byteFieldE2", false, "{1: 0, 0: 0.5, 2: 1}"),
                    new Draggable("v-FieldValve_E3", "byteFieldE3", false, "{1: 0, 0: 0.5, 2: 1}"),
                    new Draggable("v-FieldValve_W1", "byteFieldW1", false, "{1: 0, 0: 0.5, 2: 1}"),
                    new Draggable("v-FieldValve_W2", "byteFieldW2", false, "{1: 0, 0: 0.5, 2: 1}"),
                    new Draggable("v-FieldValve_W3", "byteFieldW3", false, "{1: 0, 0: 0.5, 2: 1}")
            )),
            new PrpDraggables("Todelmer", "Exterior", Arrays.asList(
                    new Draggable("HandCrankDir1", "CrankDir1", false, true),
                    new Draggable("HandCrankDir2", "CrankDir2", false, true),
                    new Draggable("TramCrank01"),
                    new Draggable("TramCrank02")
                    //new Draggable("TramCarLever")
            )),
            new PrpDraggables("Todelmer", "InteriorPillar1", Arrays.asList(
                    // power joystick... let's say 10 steps
                    new Draggable("PowerJoystickLeftRight", "MainPower01LeftRight", false, "{0.0: 0.0, 0.111: 0.037, 0.222: 0.074, 0.333: 0.111, 0.444: 0.148, 0.556: 0.185, 0.667: 0.222, 0.778: 0.259, 0.889: 0.296, 1.0: 0.333}"),
                    new Draggable("PowerJoystickUpDown",    "MainPower01UpDown",    false, "{0.0: 0.0, 0.111: 0.037, 0.222: 0.074, 0.333: 0.111, 0.444: 0.148, 0.556: 0.185, 0.667: 0.222, 0.778: 0.259, 0.889: 0.296, 1.0: 0.333}"),
                    // all the annoying scopes: 25 steps to match the GUI
                    new Draggable("Scope01Horiz", "BigScope01HorizSlider", false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope01Vert",  "BigScope01VertSlider",  false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope02Horiz", "BigScope02HorizSlider", false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope02Vert",  "BigScope02VertSlider",  false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope03Horiz", "BigScope03HorizSlider", false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope03Vert",  "BigScope03VertSlider",  false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope04Horiz", "BigScope04HorizSlider", false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}"),
                    new Draggable("Scope04Vert",  "BigScope04VertSlider",  false, "{0.0: 0.0, 0.04: 0.133, 0.08: 0.267, 0.12: 0.4, 0.16: 0.533, 0.2: 0.667, 0.24: 0.8, 0.28: 0.933, 0.32: 1.067, 0.36: 1.2, 0.4: 1.333, 0.44: 1.467, 0.48: 1.6, 0.52: 1.733, 0.56: 1.867, 0.6: 2.0, 0.64: 2.133, 0.68: 2.267, 0.72: 2.4, 0.76: 2.533, 0.8: 2.667, 0.84: 2.8, 0.88: 2.933, 0.92: 3.067, 0.96: 3.2, 1.0: 3.333}")
            )),
            new PrpDraggables("Todelmer", "InteriorPillar3", Arrays.asList(
                    new Draggable("JoystickLeftRightb", "MainPower03LeftRight", false, "{0.0: 0.0, 0.111: 0.037, 0.222: 0.074, 0.333: 0.111, 0.444: 0.148, 0.556: 0.185, 0.667: 0.222, 0.778: 0.259, 0.889: 0.296, 1.0: 0.333}"),
                    new Draggable("JoystickUpDownb",    "MainPower03UpDown",    false, "{0.0: 0.0, 0.111: 0.037, 0.222: 0.074, 0.333: 0.111, 0.444: 0.148, 0.556: 0.185, 0.667: 0.222, 0.778: 0.259, 0.889: 0.296, 1.0: 0.333}")
            ))
    );

    public static void PostMod_ChangeVerySpecialPython(prpfile prp,String oldAgename, String newAgename)
    {
        //String newagename = agenames.get(agename);
        if(newAgename!=null)
        {
            if(prp.header.pagename.toString().toLowerCase().equals("builtin"))
            {
                PrpRootObject[] objs = prputils.FindAllObjectsWithName(prp, "VeryVerySpecialPythonFileMod");
                if(objs.length>0)
                {
                    if(objs.length>1) m.err("More than one VeryVerySpecialPythonFileMod found, just handling the first.");
                    prpobjects.plPythonFileMod pythfilemod =  objs[0].castTo();
                    Urustring oldpyfile = pythfilemod.pyfile;
                    pythfilemod.pyfile = Urustring.createFromString(pythfilemod.pyfile.toString().replace(oldAgename, newAgename));
                    if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Changing Agename in VeryVerySpecialPythonFileMod from ",oldAgename," to ",newAgename);
                }
            }
        }
    }
    public static void PostMod_RemoveLadders(prpfile prp)
    {
        //find all sceneobjects that contain LogicModifiers that contain LadderModifiers, and remove them.
        PrpRootObject[] objs = prputils.FindAllObjectsOfType(prp, Typeid.plSceneObject);
        Vector<PrpRootObject> objsToDelete = new Vector<PrpRootObject>();
        for(PrpRootObject obj: objs)
        {
            boolean removeThisSceneobject = false;
            prpobjects.plSceneObject so = obj.castTo();
            for(Uruobjectref ref: so.modifiers)
            {
                if(ref.hasref() && ref.xdesc.objecttype==Typeid.plLogicModifier)
                {
                    prpobjects.plLogicModifier lmod = prp.findObjectWithDesc(ref.xdesc).castTo();
                    if(lmod.parent.message.type==Typeid.plNotifyMsg)
                    {
                        prpobjects.uruobj a = lmod.parent.message.prpobject.object;
                        if(a instanceof prpobjects.PrpMessage.PlNotifyMsg)
                        {
                            prpobjects.PrpMessage.PlNotifyMsg notifymsg = (prpobjects.PrpMessage.PlNotifyMsg)a;
                            for(Uruobjectref notmsgref: notifymsg.parent.receivers)
                            {
                                if(notmsgref.hasref()&&notmsgref.xdesc.objecttype==Typeid.plLadderModifier)
                                {
                                    //remove this root sceneobject.
                                    removeThisSceneobject = true;
                                }
                            }
                        }
                    }
                }
            }
            if(removeThisSceneobject)
            {
                //actually remove it.
                m.msg("Removing SceneObject that indirectly references PlLadderModifier: ",obj.header.desc.toString());
                //prp.removeRootObject(obj);
                objsToDelete.add(obj);
            }
        }
        for(PrpRootObject obj: objsToDelete)
        {
            prp.tagRootObjectAsDeleted(obj);
        }
        //if(xdesc!=null && xdesc.objecttype==Typeid.plLadderModifier && c.curRootObject!= null && c.curRootObject.objecttype==Typeid.plLogicModifier)
        //{
        //    {
        //        //throw new shared.readwarningexception("Removing plLogicModifier that references plLadderModifier:"+xdesc.objectname.toString());
        //    }
        //}
    }
    public static void PostMod_MakePlLayersWireframe(prpfile prp)
    {
        PrpRootObject[] layers = prputils.FindAllObjectsOfType(prp, Typeid.plLayer);
        for(PrpRootObject layer2: layers)
        {
            prpobjects.x0006Layer layer = layer2.castTo();
            m.msg("Making wireframes!");
            layer.flags5 |= 0x1; //wireframe! //misc
        }
    }
    public static void PostMod_FixDynamicMaps(prpfile prp)
    {
//        PrpRootObject[] layers = prp.FindAllObjectsOfType(Typeid.plLayer);
//        for(PrpRootObject layer2: layers)
//        {
//            prpobjects.x0006Layer layer = layer2.castTo();
//            if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plDynamicCamMap)
//            {
//                //found it!
//                m.msg("Blackifying DynamicCamMap in PlLayer.");
//                //layer.flags1 |= 0x80; //kBlendNoColor //blend //makes it invisible?
//                //float r = (float)(15.0/255.0); //15
//                //float g = (float)(20.0/255.0); //20
//                //float b = (float)(26.0/255.0); //26
//                Flt r = Flt.createFromJavaFloat((float)(0.0/255.0)); //15
//                Flt g = Flt.createFromJavaFloat((float)(0.0/255.0)); //20
//                Flt b = Flt.createFromJavaFloat((float)(0.0/255.0)); //26
//                //float a = (float)1.0;
//                //layer.ambient = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
//                //layer.diffuse = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
//                //layer.emissive = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
//                layer.ambient.r = r;
//                layer.ambient.g = g;
//                layer.ambient.b = b;
//                layer.diffuse.r = r;
//                layer.diffuse.g = g;
//                layer.diffuse.b = b;
//                layer.emissive.r = r;
//                layer.emissive.g = g;
//                layer.emissive.b = b;
//                layer.specular.r = r;
//                layer.specular.g = g;
//                layer.specular.b = b;
//            }
//        }
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plLayer))
        {
            prpobjects.x0006Layer lyr = ro.castTo();

            if (lyr.texture == null || lyr.texture.xdesc == null || lyr.texture.xdesc.objecttype == null) continue;

            if (lyr.texture.xdesc.objecttype == Typeid.plDynamicCamMap)
            {
                PrpRootObject existing = prp.findObject(lyr.texture.xdesc.objectname.toString(), Typeid.plDynamicEnvMap);
                if (existing == null)
                {
                    // time to create an envmap !
                    prpobjects.plDynamicCamMap dcm = prp.findObjectWithRef(lyr.texture).castTo();
                    prpobjects.plDynamicEnvMap dem = prpobjects.plDynamicEnvMap.createFromCamMap(dcm, prp);

                    Uruobjectref demref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plDynamicEnvMap, lyr.texture.xdesc.objectname.toString(), prp.header.pageid);
                    PrpRootObject demroot = PrpRootObject.createFromDescAndObject(demref.xdesc, dem);
                    prp.addObject(demroot);

                    prp.markObjectDeleted(lyr.texture, true);

                    lyr.texture = demref;
                }
                else
                    lyr.texture = existing.getref();

                lyr.uvwSource = 196608; // change uvwsource to EnvMap
                if (ro.header.desc.objectname.toString().equals("LensCameraMap"))
                    // doesn't look as good, but only way to show the door...
                    lyr.flags5 |= x0006Layer.kMiscUseRefractionXform;
                else
                    lyr.flags5 |= x0006Layer.kMiscUseReflectionXform;
            }
            else if (lyr.texture.xdesc.objecttype == Typeid.plDynamicEnvMap)
            {
                prpobjects.plDynamicEnvMap dem = prp.findObjectWithRef(lyr.texture).castTo();
                if (dem.rootNode != null)
                {
                    PrpRootObject rn = prp.findObjectWithRef(dem.rootNode);
                    if (rn != null)
                    {
                        m.msg("Replacing incompatible rootnode in envmap with pos vector...");
                        prpobjects.plCoordinateInterface ci = prp.findObjectWithRef(rn.castToSceneObject().coordinateinterface).castTo();
                        float x = Float.intBitsToFloat(ci.localToWorld.xmatrix[3]);
                        float y = Float.intBitsToFloat(ci.localToWorld.xmatrix[7]);
                        float z = Float.intBitsToFloat(ci.localToWorld.xmatrix[11]);

                        if (dem.fPos == null || (dem.fPos.x == Flt.zero() && dem.fPos.y == Flt.zero() && dem.fPos.z == Flt.zero()))
                            dem.fPos = new Vertex(x, y, z);
                        else
                        {
                            m.warn("Envmap already has rootnode. Replacing...");
                            dem.fPos = new Vertex(x, y, z);

                            // don't do this ! Todelmer's pod proves us it's wrong.
                            //dem.fPos = new Vertex(dem.fPos.x.toJavaFloat() + x, dem.fPos.y.toJavaFloat() + y, dem.fPos.z.toJavaFloat() + z);
                        }
                    }
                }
            }
        }
    }
    public static void PostMod_InvertEnvironmaps2(prpfile prp)
    {
        String age = prp.header.agename.toString();
        String page = prp.header.pagename.toString();
        //instead of a manual list, does it depend on a layer flag?
        //this works, but it's simpler to just use the manual list, so we don't have to hop accross different prps.
        boolean manual = true;
        if(!manual)
        {
            for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plLayer))
            {
                prpobjects.x0006Layer layer = ro.castTo();
                if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plCubicEnvironMap)
                {
                    if((layer.flags5&0x400000)!=0) //kMiscCam2Screen flag
                    {
                        //do something!
                        m.status("flagset: "+age+": "+layer.texture.toString());
                        int dummy=0;
                    }
                }
            }
        }
        else
        {
            if(page.equals("Textures"))
            {
                if(age.equals("Direbo"))
                {
                    PostMod_InvertEnvironmaps(prp,"xbublakitake_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubsrlntake_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubtdlmtake_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubthgrtake_fr*0.hsm");
                }
                if(age.equals("KveerMystV")) //we don't really need to do this one, because we just do the Releeshan part.
                {
                    PostMod_InvertEnvironmaps(prp,"xbublakikeep_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubsrlnkeep_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubtdlmkeep_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubthgrkeep_fr*0.hsm");
                }
                if(age.equals("Siralehn"))
                {
                    PostMod_InvertEnvironmaps(prp,"xbubdrbotake01_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubkverkeep_fr*0.hsm");
                }
                if(age.equals("Laki"))
                {
                    PostMod_InvertEnvironmaps(prp,"xbubdrbotake01_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubkverkeep_fr*0.hsm");
                }
                if(age.equals("Tahgira"))
                {
                    PostMod_InvertEnvironmaps(prp,"xbubdrbotake01_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubkverkeep_fr*0.hsm");
                }
                if(age.equals("Todelmer"))
                {
                    PostMod_InvertEnvironmaps(prp,"xbubdrbotake01_fr*0.hsm");
                    PostMod_InvertEnvironmaps(prp,"xbubkverkeep_fr*0.hsm");
                }
            }
        }
    }
    public static void PostMod_InvertEnvironmaps(prpfile prp, String objname)
    {
        PrpRootObject ro = prp.findObject(objname, Typeid.plCubicEnvironMap);
        prpobjects.x0005Environmap em = ro.castTo();
        em.invert();
    }
    public static void PostMod_FixCastFlags(prpfile prp)
    {
        // Myst V cast flags always propagate even though you don't set the LocalPropagate flag.
        // Uru doesn't. Which means we must set the flag for these objects



        // responders: local propagate +cast by type for cameras
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plResponderModifier))
        {
            prpobjects.plResponderModifier resp = ro.castTo();
            for (plResponderModifier.PlResponderState state: resp.messages)
            {
                for (plResponderModifier.PlResponderCmd c: state.commands)
                {
                    // very long and annoying switch (can't do it any other way)

                    Typeid msgtype = c.message.type;
                    prpobjects.plMessage msg = null;
                    boolean ok = true;

                    switch (msgtype)
                    {
                        case plResponderEnableMsg:
                            msg = ((prpobjects.PrpMessage.PlResponderEnableMsg)c.message.castTo()).parent;
                            break;
                        case plSimSuppressMsg:
                            msg = ((prpobjects.PrpMessage.PlSimSuppressMsg)c.message.castTo()).parent;
                            break;
                        //case plDampMsg:
                        //    msg = ((prpobjects.PrpMessage.PlDampMsg)c.message.castTo()).parent;
                        //    break;
                        case plRideAnimatedPhysMsg:
                            msg = ((prpobjects.PrpMessage.PlRideAnimatedPhysMsg)c.message.castTo()).parent;
                            break;
                        case plCameraMsg:
                            msg = ((prpobjects.PrpMessage.PlCameraMsg)c.message.castTo()).parent;
                            break;
                        case plEventCallbackMsg:
                            msg = ((prpobjects.PrpMessage.PlEventCallbackMsg)c.message.castTo()).parent;
                            break;
                        case plTimerCallbackMsg:
                            msg = ((prpobjects.PrpMessage.PlTimerCallbackMsg)c.message.castTo()).parent;
                            break;
                        case plAnimCmdMsg:
                            msg = ((prpobjects.PrpMessage.PlAnimCmdMsg)c.message.castTo()).parent.parent;
                            break;
                        case plLinkToAgeMsg:
                            msg = ((prpobjects.PrpMessage.PlLinkToAgeMsg)c.message.castTo()).parent;
                            break;
                        case plEventCallbackSetupMsg:
                            msg = ((prpobjects.PrpMessage.plEventCallbackSetupMsg)c.message.castTo()).parent;
                            break;
                        case plOneShotMsg:
                            msg = ((prpobjects.PrpMessage.PlOneShotMsg)c.message.castTo()).parent;
                            break;
                        case plNotifyMsg:
                            msg = ((prpobjects.PrpMessage.PlNotifyMsg)c.message.castTo()).parent;
                            break;
                        case plEnableMsg:
                            msg = ((prpobjects.PrpMessage.PlEnableMsg)c.message.castTo()).parent;
                            break;
                        case plActivatorMsg:
                            msg = ((prpobjects.PrpMessage.PlActivatorMsg)c.message.castTo()).parent;
                            break;
                        case plArmatureEffectStateMsg:
                            msg = ((prpobjects.PrpMessage.PlArmatureEffectStateMsg)c.message.castTo()).parent;
                            break;
                        case plExcludeRegionMsg:
                            msg = ((prpobjects.PrpMessage.PlExcludeRegionMsg)c.message.castTo()).parent;
                            break;
                        case plSoundMsg:
                            msg = ((prpobjects.PrpMessage.PlSoundMsg)c.message.castTo()).parent.parent;
                            break;

                        default:
                            m.warn("Unknown responder message type " + msgtype + ". Doing nothing.");
                            ok = false;
                    }

                    if (ok)
                    {
                        if (msgtype == Typeid.plCameraMsg)
                        {
                            // UPDATE: leaving the camera messages broken AGAIN, so we can now control them via Python.
                            // This allows us to reset the cameras without having a headache of setting up responders...
//                            if (msg.broadcastFlags == (prpobjects.plMessage.kPropagateToChildren | prpobjects.plMessage.kBCastByType))
//                                // that's a camera responder - in MystV, looks like 0x5, in PotS, recommended value is 0x801
//                                msg.broadcastFlags = prpobjects.plMessage.kLocalPropagate | prpobjects.plMessage.kBCastByType;
//                            else
//                                m.warn("Unexpected flags " + msg.broadcastFlags + " for camera responder " + ro.header.desc.objectname + ". Doing nothing.");
                        }
                        else
                        {
                            if (msg.broadcastFlags == prpobjects.plMessage.kBCastNone)
                                // that's a regular responder - MV looks 0x0, PotS recommended is 0x800
                                msg.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
                            else if (msg.broadcastFlags == (prpobjects.plMessage.kPropagateToChildren | prpobjects.plMessage.kBCastByType))
                                // that's a camera responder - what is it doing here ?
                                m.warn("Unexpected flags " + msg.broadcastFlags + " in responder " + ro.header.desc.objectname + ". Isn't that a "
                                        + "camera responder ? Doing nothing.");
                            else
                                m.warn("Unexpected flags " + msg.broadcastFlags + " in responder " + ro.header.desc.objectname + ". Doing nothing.");
                        }


                        // the followings have additionnal callback msgs that need correction
                        if (msgtype == Typeid.plAnimCmdMsg)
                        {
                            for (int i=0; i<(((prpobjects.PrpMessage.PlAnimCmdMsg)c.message.castTo()).parent.callbacks.size()); i++)
                            {
                                prpobjects.PrpMessage.PlEventCallbackMsg cb = ((prpobjects.PrpMessage.PlAnimCmdMsg)c.message.castTo()).parent.callbacks.get(i).castTo();
                                if (cb.parent.broadcastFlags == prpobjects.plMessage.kBCastNone)
                                    // regular callback - MV looks 0x0, PotS recommended is 0x800
                                    cb.parent.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
                                else
                                    m.warn("Unexpected flags " + msg.broadcastFlags + " in responder callback " + ro.header.desc.objectname + ". Doing nothing.");
                            }
                        }
                        else if (msgtype == Typeid.plSoundMsg)
                        {
                            prpobjects.PrpMessage.PlSoundMsg soundMsg = c.message.castTo();
                            for (int i=0; i<(soundMsg.parent.callbacks.size()); i++)
                            {
                                prpobjects.PrpMessage.PlEventCallbackMsg cb = ((prpobjects.PrpMessage.PlSoundMsg)c.message.castTo()).parent.callbacks.get(i).castTo();
                                if (cb.parent.broadcastFlags == prpobjects.plMessage.kBCastNone)
                                    // regular callback - MV looks 0x0, PotS recommended is 0x800
                                    cb.parent.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
                                else
                                    m.warn("Unexpected flags " + msg.broadcastFlags + " in responder callback " + ro.header.desc.objectname + ". Doing nothing.");
                            }

                            // While we're at it - and not really related to cast flags...
                            // Remove the kIsLocalOnly (0x20000) command - if the responder was triggered by someone else,
                            // this would prevent the sound from playing on other clients.
                            if (soundMsg.cmd != null) // (Laki's crossfade message is replaced by soundmsg, but it's not setup yet... sorry about that !)
                            {
                                soundMsg.cmd.values[0] &= ~(1 << 17);
                            }
                        }
                    }
                }
            }
        }

        // logic modifiers: local propagate + net propagate
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plLogicModifier))
        {
            prpobjects.plLogicModifier lo = ro.castTo();
            prpobjects.plMessage msg = ((prpobjects.PrpMessage.PlNotifyMsg)lo.parent.message.castTo()).parent;

            if (msg.broadcastFlags == prpobjects.plMessage.kBCastNone)
                // that's a regular logic mod
                msg.broadcastFlags = prpobjects.plMessage.kLocalPropagate | prpobjects.plMessage.kNetPropagate;
            else
                m.warn("Unexpected flags " + msg.broadcastFlags + " in logic modifier " + ro.header.desc.objectname + ". Doing nothing.");
        }

        // axis anim modifiers: this class is too broken to be used in multiplayer. Ignore it.
//        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plAxisAnimModifier))
//        {
//            prpobjects.plAxisAnimModifier aa = ro.castTo();
//            prpobjects.PrpMessage.PlNotifyMsg msg = aa.message.castTo();
//
//            if (msg.parent.broadcastFlags == prpobjects.plMessage.kBCastNone)
//                msg.parent.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
//            else
//                m.warn("Unexpected flags " + msg.parent.broadcastFlags + " in axis anim modifier " + ro.header.desc.objectname + ". Doing nothing.");
//        }

        // anim event modifier: local propagate
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plAnimEventModifier))
        {
            prpobjects.plAnimEventModifier aem = ro.castTo();

            PrpMessage.PlAnimCmdMsg amsg = aem.message.castTo();
            if (amsg.parent.parent.broadcastFlags == prpobjects.plMessage.kBCastNone)
                // regular anim event modifier. MV: 0x0, Uru: 0x800
                amsg.parent.parent.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
            else
                m.warn("Unexpected flags " + amsg.parent.parent.broadcastFlags + " in anim event modifier " + ro.header.desc.objectname + ". Doing nothing.");

            // callbacks
            for (int i=0; i<amsg.parent.callbacks.size(); i++)
            {
                prpobjects.PrpMessage.PlEventCallbackMsg cb = amsg.parent.callbacks.get(i).castTo();
                if (cb.parent.broadcastFlags == prpobjects.plMessage.kBCastNone)
                    // regular anim event modifier callback. MV: 0x0, Uru: 0x800
                    cb.parent.broadcastFlags = prpobjects.plMessage.kLocalPropagate;
                else
                    m.warn("Unexpected flags " + cb.parent.broadcastFlags + " in anim event modifier callback " + ro.header.desc.objectname + ". Doing nothing.");
            }
        }
    }

    public static void PostMod_FixIncidentalSounds(prpfile prp)
    {
        // Uru cannot play sounds using incidental + looping + 3D.
        // (I guess "incidental" is just a sound type, and doesn't have any impact on how the sound plays ?)

        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plWin32StreamingSound))
        {
            prpobjects.x0084Win32StreamingSound strsnd = ro.castTo();
            if (prp.header.pagename.toString().equals("IceCave")
                    && ro.header.desc.objectname.toString().equals("cSfxThermalActivityL"))
            {
                // this object has wrong properties that set it to start even though the env effect is at 0.
                // Additionally, it does not have a fadein/out effect (unlike the right channel).
                strsnd.parent.parent.properties = 0x5;
                strsnd.parent.parent.fadeInParams.lengthInSecs = Flt.one();
                strsnd.parent.parent.fadeInParams.volEnd = Flt.one();

                strsnd.parent.parent.fadeOutParams.lengthInSecs = Flt.createFromJavaFloat(3f);
                strsnd.parent.parent.fadeOutParams.volStart = Flt.one();
                strsnd.parent.parent.fadeOutParams.stopWhenDone = 1;
            }

            //else if (strsnd.parent.parent.properties == (plWin32Sound.PlSound.kProp3D | plWin32Sound.PlSound.kPropLooping | plWin32Sound.PlSound.kPropIncidental))
            //    strsnd.parent.parent.properties &= ~plWin32Sound.PlSound.kPropIncidental; // unset incidental
            // you know what ? Let's just forget about incidental. Seems it causes other problems for no difference in audio
            // Let's just disable the whole thing altogether.
            strsnd.parent.parent.properties &= ~plWin32Sound.PlSound.kPropIncidental; // unset incidental
        }
    }

    public static void PostMod_FixPythonFileMods(prpfile prp)
    {
        // MystV stores the full sdl var path, whereas PotS always defaults the varname to the one in the age's sdl
        // which means we must take the word after the last dot.
        // Of course, this won't work for inter-Age sdl operations, but this is fixed via postmods and Python scripts.
        // Additionnaly, we need to fix some python mods, such as xPopupGui

        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plPythonFileMod))
        {
            prpobjects.plPythonFileMod py = ro.castTo();

            // fix SDL var
            for (int i=0; i<py.listings.size(); i++)
            {
                if (py.listings.get(i).type == 4) // string value
                {
                    if (prp.header.agename.toString().equals("Direbo"))
                    {
                        // fix gate sdls
                        String sdlvar = py.listings.get(i).xString.toString();
                        String org = sdlvar;
                        if (sdlvar.indexOf(".thgrIsland.Gate1Open") != -1)
                            sdlvar = "thgrGate1Open";
                        else if (sdlvar.indexOf(".thgrIsland.Gate2Open") != -1)
                            sdlvar = "thgrGate2Open";
                        else if (sdlvar.indexOf(".thgrIsland.Gate3Open") != -1)
                            sdlvar = "thgrGate3Open";

                        else if (sdlvar.indexOf(".tdlmIsland.Gate1Open") != -1)
                            sdlvar = "tdlmGate1Open";
                        else if (sdlvar.indexOf(".tdlmIsland.Gate2Open") != -1)
                            sdlvar = "tdlmGate2Open";
                        else if (sdlvar.indexOf(".tdlmIsland.Gate3Open") != -1)
                            sdlvar = "tdlmGate3Open";

                        else if (sdlvar.indexOf(".srlnIsland.Gate1Open") != -1)
                            sdlvar = "srlnGate1Open";
                        else if (sdlvar.indexOf(".srlnIsland.Gate2Open") != -1)
                            sdlvar = "srlnGate2Open";
                        else if (sdlvar.indexOf(".srlnIsland.Gate3Open") != -1)
                            sdlvar = "srlnGate3Open";

                        else if (sdlvar.indexOf(".lakiIsland.Gate1Open") != -1)
                            sdlvar = "lakiGate1Open";
                        else if (sdlvar.indexOf(".lakiIsland.Gate2Open") != -1)
                            sdlvar = "lakiGate2Open";
                        else if (sdlvar.indexOf(".lakiIsland.Gate3Open") != -1)
                            sdlvar = "lakiGate3Open";

                        if (!org.equals(sdlvar))
                        {
                            py.listings.get(i).xString = Bstr.createFromString(sdlvar);
                            continue;
                        }
                    }

                    String sdlvar = py.listings.get(i).xString.toString();
                    int lastDot = sdlvar.lastIndexOf('.');
                    if (lastDot != -1)
                    {
                        String newSdlVar = sdlvar.substring(lastDot+1);
                        py.listings.get(i).xString = Bstr.createFromString(newSdlVar);
                    }
                }
            }

            // fix gui popups
            if (py.pyfile.equals(Urustring.createFromString("xDialogToggle")))
                for (int i=0; i<py.listings.size(); i++)
                {
                    if (py.listings.get(i).index == 2)
                    {
                        py.listings.get(i).index = 4;
                        break;
                    }
                }

            // fix - adds spawn point to bubble pedestals. This allows us to use fakelinking
            if (py.pyfile.equals(Urustring.createFromString("drboPedestal")))
            {
                if (prp.header.agename.toString().equals("Tahgira"))
                {
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 9, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed1", Pageid.createFromPrefixPagenum(88, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 10, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed2", Pageid.createFromPrefixPagenum(88, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 11, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed3", Pageid.createFromPrefixPagenum(88, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 12, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInThgrKeep", Pageid.createFromPrefixPagenum(88, 1))));
                }
                if (prp.header.agename.toString().equals("Todelmer"))
                {
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 9, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed1", Pageid.createFromPrefixPagenum(87, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 10, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed2", Pageid.createFromPrefixPagenum(87, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 12, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTdlmKeep", Pageid.createFromPrefixPagenum(87, 12))));
                }
                if (prp.header.agename.toString().equals("Siralehn"))
                {
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 9, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed1", Pageid.createFromPrefixPagenum(89, 4))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 10, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed2", Pageid.createFromPrefixPagenum(89, 8))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 12, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInSrlnKeep", Pageid.createFromPrefixPagenum(89, 7))));
                }
                if (prp.header.agename.toString().equals("Laki"))
                {
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 9, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed1", Pageid.createFromPrefixPagenum(91, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 10, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed2", Pageid.createFromPrefixPagenum(91, 3))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 11, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInPed3", Pageid.createFromPrefixPagenum(91, 1))));
                    py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 12, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInLakiKeep", Pageid.createFromPrefixPagenum(91, 1))));
                }
            }

            // fix linking books - simple and efficient. Reuses script from MV.
            if (py.pyfile.equals(Urustring.createFromString("xLinkingBookGUIPopup")))
                py.pyfile = Urustring.createFromString("xEoALinkingBook");
        }
    }

    public static void PostMod_FixEchoEffects(prpfile prp)
    {
        // will require the DSOUND/DSOAL fix
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            prpobjects.plSceneObject so = ro.castTo();

            for (Uruobjectref modif: so.modifiers)
            {
                if (modif.xdesc.objecttype == Typeid.plEAXReverbEffect)
                {
                    prpobjects.plEAXReverbEffect eaxre = prp.findObjectWithRef(modif).castTo();
                    prpobjects.plEAXListenerMod eaxlm = prpobjects.plEAXListenerMod.createFromReverbEffect(eaxre);


                    Uruobjectref eaxlmref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plEAXListenerMod, modif.xdesc.objectname.toString(), prp.header.pageid);
                    PrpRootObject eaxlmroot = PrpRootObject.createFromDescAndObject(eaxlmref.xdesc, eaxlm);
                    prp.addObject(eaxlmroot);

                    prp.markObjectDeleted(modif, true);

                    modif.shallowCopyFrom(eaxlmref);
                }
            }
        }
    }

    public static void PostMod_FixCameraReferences(prpfile prp)
    {
        // change the reference type in the scene object
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            prpobjects.plSceneObject so = ro.castTo();

            for (Uruobjectref modif: so.modifiers)
                if (modif.xdesc.objecttype == Typeid.plCameraModifier)
                    modif.xdesc.objecttype = Typeid.plCameraModifier1;
        }

        // change the reference type in the camera modifier
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCameraModifier))
        {
            prpobjects.x009BCameraModifier1 cm1 = ro.castTo();
            if (cm1.brain.xdesc.objecttype == Typeid.plCameraBrainUru)
                cm1.brain.xdesc.objecttype = Typeid.plCameraBrain1;
            else if (cm1.brain.xdesc.objecttype == Typeid.plCameraBrainUru_Fixed)
                cm1.brain.xdesc.objecttype = Typeid.plCameraBrain1_Fixed;


            // I'm not sure about next iterations, but either way it will make sure every reference is fixed
            for (Uruobjectref sender: cm1.messageQueueSenders)
            {
                if (sender.xdesc.objecttype == Typeid.plCameraBrainUru)
                    sender.xdesc.objecttype = Typeid.plCameraBrain1;
                else if (sender.xdesc.objecttype == Typeid.plCameraBrainUru_Fixed)
                    sender.xdesc.objecttype = Typeid.plCameraBrain1_Fixed;
            }

            for (x009BCameraModifier1.CamTrans t: cm1.trans)
            {
                if (t.transTo == null) continue;
                if (t.transTo.xdesc.objecttype == Typeid.plCameraBrainUru)
                    t.transTo.xdesc.objecttype = Typeid.plCameraBrain1;
                else if (t.transTo.xdesc.objecttype == Typeid.plCameraBrainUru_Fixed)
                    t.transTo.xdesc.objecttype = Typeid.plCameraBrain1_Fixed;
            }
        }

        // change the reference type in the camera brain (target point)
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCameraBrainUru_Fixed))
        {
            prpobjects.plCameraBrain1_Fixed cbf = ro.castTo();
            if (cbf.targetPoint != null && cbf.targetPoint.xdesc.objecttype == Typeid.plCameraModifier)
                cbf.targetPoint.xdesc.objecttype = Typeid.plCameraModifier1;
        }

        // fix the headers for all these objects
        for (PrpRootObject object: prp.objects2)
        {
            if (object.header.objecttype == Typeid.plCameraBrainUru_Fixed)
                object.header.objecttype = Typeid.plCameraBrain1_Fixed;
            else if (object.header.objecttype == Typeid.plCameraBrainUru)
                object.header.objecttype = Typeid.plCameraBrain1;
            else if (object.header.objecttype == Typeid.plCameraModifier)
                object.header.objecttype = Typeid.plCameraModifier1;

            if (object.header.desc.objecttype == Typeid.plCameraBrainUru_Fixed)
                object.header.desc.objecttype = Typeid.plCameraBrain1_Fixed;
            else if (object.header.desc.objecttype == Typeid.plCameraBrainUru)
                object.header.desc.objecttype = Typeid.plCameraBrain1;
            else if (object.header.desc.objecttype == Typeid.plCameraModifier)
                object.header.desc.objecttype = Typeid.plCameraModifier1;
        }
    }

    public static void PostMod_FixInvalidLogicModConditions(prpfile prp)
    {
        // prevents logic modifier from crashing game when hovering cursor over Tahgira valves

        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plLogicModifier))
        {
            prpobjects.plLogicModifier lo = ro.castTo();

            int i=0;
            for (Uruobjectref cond: lo.conditionals)
                if (cond.xdesc.objecttype != Typeid.plPythonFileModConditionalObject)
                    i++;

            Uruobjectref[] newconds = new Uruobjectref[i];

            i=0;
            for (Uruobjectref cond: lo.conditionals)
                if (cond.xdesc.objecttype != Typeid.plPythonFileModConditionalObject)
                {
                    newconds[i] = cond;
                    i++;
                }
            lo.conditionals = newconds;
            lo.conditionalcount = i;
        }
    }

    public static void PostMod_FixPedestals(prpfile prp)
    {
        // disables the "put slate" clickables,
        // attach a new python mod to the pedestal take clickable,
        // and make this clickable notify the python mod

        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            prpobjects.plSceneObject so = ro.castTo();

            Uruobjectref modifToAdd = null;

            for (Uruobjectref mod: so.modifiers)
            {
                if (mod.xdesc.objecttype == Typeid.plLogicModifier)
                {
                    if (prp.header.agename.toString().equals("Tahgira"))
                    {
                        if (mod.xdesc.objectname.toString().equals("ClickPed1") ||
                            mod.xdesc.objectname.toString().equals("ClickPed2") ||
                            mod.xdesc.objectname.toString().equals("ClickPed3") ||
                            mod.xdesc.objectname.toString().equals("ClickKeepPedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickTakePedestal"))
                        {
                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            lo.parent.disabled = 1;
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickDireboLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Direbo")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInPoint4")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyDrbo"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.set(0, pyref);
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickPed1TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(88, 2))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("ThgrActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("01")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed2TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(88, 2))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("ThgrActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("02")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed3TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(88, 2))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("ThgrActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("03")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTahgiraKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(88, 2))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("ThgrActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("Keep")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTodelmerKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Todelmer")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickSiralehnKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Siralehn")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickLakiKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Laki")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                    }

                    else if (prp.header.agename.toString().equals("Todelmer"))
                    {
                        if (mod.xdesc.objectname.toString().equals("ClickPed1") ||
                            mod.xdesc.objectname.toString().equals("ClickPed2") ||
                            mod.xdesc.objectname.toString().equals("ClickPed3") ||
                            mod.xdesc.objectname.toString().equals("ClickKeepPedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickTakePedestal"))
                        {
                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            lo.parent.disabled = 1;
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickDireboLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Direbo")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInPoint3")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyDrbo"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.set(0, pyref);
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickPed1TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(87, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("TdlmActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("01")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed2TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(87, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("TdlmActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("02")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTahgiraKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Tahgira")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTodelmerKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(87, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("TdlmActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("Keep")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickSiralehnKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Siralehn")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickLakiKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Laki")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                    }

                    else if (prp.header.agename.toString().equals("Siralehn"))
                    {
                        if (mod.xdesc.objectname.toString().equals("ClickPed1Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickPed2Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickPed3Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickKeepPedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickTakePedestal"))
                        {
                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            lo.parent.disabled = 1;
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickDireboLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Direbo")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInPoint1")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyDrbo"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.set(0, pyref);
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickPed1TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(89, 8))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("SrlnActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("01")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed2TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(89, 8))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("SrlnActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("02")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTahgiraKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Tahgira")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTodelmerKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Todelmer")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickSiralehnKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(89, 8))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("SrlnActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("Keep")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickLakiKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Laki")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                    }

                    else if (prp.header.agename.toString().equals("Laki"))
                    {
                        if (mod.xdesc.objectname.toString().equals("ClickPed1Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickPed2Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickPed3Pedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickKeepPedestal") ||
                            mod.xdesc.objectname.toString().equals("ClickTakePedestal"))
                        {
                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            lo.parent.disabled = 1;
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickTakeDireboLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Direbo")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInPoint2")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyDrbo"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }

                        if (mod.xdesc.objectname.toString().equals("ClickPed1TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(91, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LakiActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("01")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed2TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(91, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LakiActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("02")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickPed3TakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(91, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LakiActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("03")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTahgiraKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Tahgira")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickTodelmerKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Todelmer")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickSiralehnKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xSimpleLink");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("Siralehn")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LinkInTake")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "PyKp"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                        if (mod.xdesc.objectname.toString().equals("ClickLakiKeepTakeLink"))
                        {
                            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
                            py.pyfile = Urustring.createFromString("xLinkPedestal");
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 1, mod));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(5, 2, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "LinkInTake", Pageid.createFromPrefixPagenum(91, 1))));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(3, Bstr.createFromString("LakiActivePedestals")));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithString(4, Bstr.createFromString("Keep")));

                            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Py"+ro.header.desc.objectname.toString(), prp.header.pageid);
                            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
                            prp.addObject(pyroot);

                            modifToAdd = pyref;
                            //so.modifiers.add(pyref);

                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            PrpMessage.PlNotifyMsg nmsg = lo.parent.message.castTo();
                            nmsg.parent.receivers.add(pyref);
                        }
                    }

                    else if (prp.header.agename.toString().equals("Direbo"))
                    {
                        if (mod.xdesc.objectname.toString().equals("ClickThgrTake") ||
                            mod.xdesc.objectname.toString().equals("ClickTdlmTake") ||
                            mod.xdesc.objectname.toString().equals("ClickSrlnTake") ||
                            mod.xdesc.objectname.toString().equals("ClickLakiTake"))
                        {
                            prpobjects.plLogicModifier lo = prp.findObjectWithDesc(mod.xdesc).castTo();
                            lo.parent.disabled = 1;
                        }
                    }
                }
            }

            if (modifToAdd != null)
                so.modifiers.add(modifToAdd);
        }
    }

    public static void PostMod_FixLinkResponderNames(prpfile prp)
    {
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plResponderModifier))
        {
            prpobjects.plResponderModifier rm = ro.castTo();
            for (plResponderModifier.PlResponderState state: rm.messages)
            {
                for (plResponderModifier.PlResponderCmd cmd: state.commands)
                {
                    if (cmd.message.type == Typeid.plLinkToAgeMsg)
                    {
                        PrpMessage.PlLinkToAgeMsg lta = cmd.message.castTo();
                        if (lta.ageLinkStruct.ageinfo.ageFilename.toString().equals("Descent"))
                            lta.ageLinkStruct.ageinfo.ageFilename = Wpstr.create("DescentMystV");
                        if (lta.ageLinkStruct.ageinfo.ageInstanceName.toString().equals("Descent"))
                            lta.ageLinkStruct.ageinfo.ageInstanceName = Wpstr.create("DescentMystV");
                    }
                }
            }
        }
    }

    public static void PostMod_makeAnimEvForObj(prpfile prp, prpobjects.plSceneObject obj, String pythonName, String animName, String aeName, String agName, float time)
    {
        prpobjects.plAnimEventModifier animev = prpobjects.plAnimEventModifier.createEmpty();
        animev.parent = plSingleModifier.createDefault();
        animev.parent.parent.parent.flags = plSynchedObject.kExcludeAllPersistentState;
        animev.numreceivers = 1;
        animev.receivers = new Uruobjectref[1];
        animev.receivers[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, pythonName, prp.header.pageid);
        PrpMessage.PlAnimCmdMsg msg = PrpMessage.PlAnimCmdMsg.createDefault();
        msg.animName = Urustring.createFromString(animName);
        msg.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kAddCallbacks);
        msg.parent.parent.broadcastFlags = 0x800;
        msg.parent.parent.sender = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, aeName, prp.header.pageid);
        msg.parent.parent.receivers.add(Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAGMasterMod, agName, prp.header.pageid));
        PrpMessage.PlEventCallbackMsg cb = PrpMessage.PlEventCallbackMsg
                .createWithSenderAndReceiver(Uruobjectref.none(),
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, aeName, prp.header.pageid));
        cb.event = 3;
        cb.eventTime = new Flt(time);
        cb.index = 0;
        cb.parent.broadcastFlags = 0x800;
        cb.repeats = -1;
        cb.user = 0;

        msg.parent.callbacks.add(PrpTaggedObject.createWithTypeidUruobj(Typeid.plEventCallbackMsg, cb));
        animev.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plAnimCmdMsg, msg);


        // add to the prp
        Uruobjectref aeref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, aeName, prp.header.pageid);
        PrpRootObject aeroot = PrpRootObject.createFromDescAndObject(aeref.xdesc, animev);
        prp.addObject(aeroot);

        // add to the object
        obj.modifiers.add(aeref);
    }

    public static void PostMod_AddAnimEventForDraggables(prpfile prp)
    {
        if (prp.header.agename.toString().equals("Todelmer"))
        {
            // draggables to fix:
            // power consoles
            // tram pumps ? Might just be a Python coding error.

            // Cyan left animation marks for most objects in the PRP - this allows us to have precise data for the animation events.

            if (prp.header.pagename.toString().equals("InteriorPillar1"))
            {
                // big scopes controls

                float keyframes[] = {
                    0.03333333507f,
                    0.1666666716f,
                    0.3333333433f,
                    0.5f,
                    0.6666666865f,
                    0.8333333135f,
                    1f,
                    1.166666627f,
                    1.333333373f,
                    1.5f,
                    1.666666627f,
                    1.833333373f,
                    2f,
                    2.166666746f,
                    2.333333254f,
                    2.5f,
                    2.666666746f,
                    2.833333254f,
                    3f,
                    3.166666746f,
                    3.333333254f,
                    };

                for (int i=1; i<4; i++)
                {
                    PrpRootObject roh = prp.findObject("Scope0"+i+"Horiz", Typeid.plSceneObject);
                    PrpRootObject rov = prp.findObject("Scope0"+i+"Vert", Typeid.plSceneObject);
                    prpobjects.plSceneObject soh = roh.castTo();
                    prpobjects.plSceneObject sov = rov.castTo();

                    PrpRootObject ropy = prp.findObject("cPythBigScope0"+i, Typeid.plPythonFileMod);
                    plPythonFileMod py = ropy.castTo();

                    for (int j=0; j<21; j++)
                    {
                        if (j<10)
                        {
                            PostMod_makeAnimEvForObj(prp, soh, "cPythBigScope0"+i, "slide", "AnimEvSliderH0"+i+"Step0"+j, "cAnimGrpScopeHoriz0"+i+"_0", keyframes[j]);
                            PostMod_makeAnimEvForObj(prp, sov, "cPythBigScope0"+i, "slide", "AnimEvSliderV0"+i+"Step0"+j, "cAnimGrpScopeVert0" +i+"_0", keyframes[j]);

                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 19+j*2,
                                Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderH0"+i+"Step0"+j, prp.header.pageid)));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 19+j*2+1,
                                Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderV0"+i+"Step0"+j, prp.header.pageid)));
                        }
                        else
                        {
                            PostMod_makeAnimEvForObj(prp, soh, "cPythBigScope0"+i, "slide", "AnimEvSliderH0"+i+"Step"+j, "cAnimGrpScopeHoriz0"+i+"_0", keyframes[j]);
                            PostMod_makeAnimEvForObj(prp, sov, "cPythBigScope0"+i, "slide", "AnimEvSliderV0"+i+"Step"+j, "cAnimGrpScopeVert0" +i+"_0", keyframes[j]);

                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 19+j*2,
                                Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderH0"+i+"Step"+j, prp.header.pageid)));
                            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 19+j*2+1,
                                Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderV0"+i+"Step"+j, prp.header.pageid)));
                        }
                    }
                }


                PrpRootObject roh = prp.findObject("Scope04Horiz", Typeid.plSceneObject);
                PrpRootObject rov = prp.findObject("Scope04Vert", Typeid.plSceneObject);
                prpobjects.plSceneObject soh = roh.castTo();
                prpobjects.plSceneObject sov = rov.castTo();

                PrpRootObject ropy = prp.findObject("cPythRopePile", Typeid.plPythonFileMod);
                plPythonFileMod py = ropy.castTo();

                for (int j=0; j<21; j++)
                {
                    if (j<10)
                    {
                        PostMod_makeAnimEvForObj(prp, soh, "cPythRopePile", "slide", "AnimEvSliderH04Step0"+j, "cAnimGrpScopeHoriz04_1", keyframes[j]);
                        PostMod_makeAnimEvForObj(prp, sov, "cPythRopePile", "slide", "AnimEvSliderV04Step0"+j, "cAnimGrpScopeVert04_1", keyframes[j]);

                        py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17+j*2,
                            Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderH04Step0"+j, prp.header.pageid)));
                        py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17+j*2+1,
                            Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderV04Step0"+j, prp.header.pageid)));
                    }
                    else
                    {
                        PostMod_makeAnimEvForObj(prp, soh, "cPythRopePile", "slide", "AnimEvSliderH04Step"+j, "cAnimGrpScopeHoriz04_1", keyframes[j]);
                        PostMod_makeAnimEvForObj(prp, sov, "cPythRopePile", "slide", "AnimEvSliderV04Step"+j, "cAnimGrpScopeVert04_1", keyframes[j]);

                        py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17+j*2,
                            Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderH04Step"+j, prp.header.pageid)));
                        py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17+j*2+1,
                            Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvSliderV04Step"+j, prp.header.pageid)));
                    }
                }

                // power console
                PrpRootObject ropcud = prp.findObject("PowerJoystickUpDown", Typeid.plSceneObject);
                PrpRootObject ropclr = prp.findObject("PowerJoystickLeftRight", Typeid.plSceneObject);
                prpobjects.plSceneObject sopcud = ropcud.castTo();
                prpobjects.plSceneObject sopclr = ropclr.castTo();

                PrpRootObject ropypc = prp.findObject("cPythMainPower", Typeid.plPythonFileMod);
                plPythonFileMod pypc = ropypc.castTo();

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "vert",  "AnimEvVertIn",  "cAnimGrpPowerVert_0",  0.49f*0.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "horiz", "AnimEvHorizIn", "cAnimGrpPowerHoriz_1", 0.9f*0.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 12,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizIn", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 13,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertIn", prp.header.pageid)));

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "vert",  "AnimEvVertOut1",  "cAnimGrpPowerVert_0",  0.59f*0.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "horiz", "AnimEvHorizOut1", "cAnimGrpPowerHoriz_1", 0.99f*0.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 14,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizOut1", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 15,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertOut1", prp.header.pageid)));

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "vert",  "AnimEvVertOut2",  "cAnimGrpPowerVert_0",  0.39f*0.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "horiz", "AnimEvHorizOut2", "cAnimGrpPowerHoriz_1", 0.8f*0.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 16,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizOut2", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertOut2", prp.header.pageid)));

                // we also need to edit the animation for this draggable...
                PrpRootObject animro = prp.findObject("PowerJoystickLeftRight_horiz_anim_0", Typeid.plATCAnim);
                prpobjects.plATCAnim anim = animro.castTo();
                // todo
            }
            else if (prp.header.pagename.toString().equals("InteriorPillar3"))
            {
                // power console
                PrpRootObject ropcud = prp.findObject("JoystickUpDownb", Typeid.plSceneObject);
                PrpRootObject ropclr = prp.findObject("JoystickLeftRightb", Typeid.plSceneObject);
                prpobjects.plSceneObject sopcud = ropcud.castTo();
                prpobjects.plSceneObject sopclr = ropclr.castTo();

                PrpRootObject ropypc = prp.findObject("cPythMainPower", Typeid.plPythonFileMod);
                plPythonFileMod pypc = ropypc.castTo();

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "move", "AnimEvVertIn",  "cAnimGrpBallVert_0",  .78f*.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "move", "AnimEvHorizIn", "cAnimGrpBallHoriz_0", .62f*.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 12,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizIn", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 13,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertIn", prp.header.pageid)));

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "move", "AnimEvVertOut1",  "cAnimGrpBallVert_0",  .88f*.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "move", "AnimEvHorizOut1", "cAnimGrpBallHoriz_0", .72f*.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 14,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizOut1", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 15,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertOut1", prp.header.pageid)));

                PostMod_makeAnimEvForObj(prp, sopcud, "cPythMainPower", "move", "AnimEvVertOut2",  "cAnimGrpBallVert_0",  .68f*.3333333433f);
                PostMod_makeAnimEvForObj(prp, sopclr, "cPythMainPower", "move", "AnimEvHorizOut2", "cAnimGrpBallHoriz_0", .52f*.3333333433f);

                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 16,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvHorizOut2", prp.header.pageid)));
                pypc.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 17,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvVertOut2", prp.header.pageid)));
            }
        }
        else if (prp.header.agename.toString().equals("Siralehn"))
        {
            // spinning rocks
            if (prp.header.pagename.toString().equals("Exterior"))
            {
                for (int i=1; i<5; i++)
                {
                    PrpRootObject ro = prp.findObject("PillarTop0"+i, Typeid.plSceneObject);
                    prpobjects.plSceneObject so = ro.castTo();
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol00", "cAnimTurnableRock0"+i, 0f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol01", "cAnimTurnableRock0"+i, 0.8333333135f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol02", "cAnimTurnableRock0"+i, 1.666666627f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol03", "cAnimTurnableRock0"+i, 2.5f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol04", "cAnimTurnableRock0"+i, 3.333333254f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol05", "cAnimTurnableRock0"+i, 4.166666508f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol06", "cAnimTurnableRock0"+i, 5f);
                    PostMod_makeAnimEvForObj(prp, so, "cPythTurnableRock0"+i, "rockspin", "AnimEvRock0"+i+"Symbol07", "cAnimTurnableRock0"+i, 5.833333492f);
                    PrpRootObject ropy = prp.findObject("cPythTurnableRock0"+i, Typeid.plPythonFileMod);
                    plPythonFileMod py = ropy.castTo();
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 3, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol00", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 4, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol01", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 5, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol02", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 6, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol03", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 7, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol04", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 8, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol05", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 9, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol06", prp.header.pageid)));
                    py.addListing(plPythonFileMod.Pythonlisting.createWithRef(7,10, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAnimEventModifier, "AnimEvRock0"+i+"Symbol07", prp.header.pageid)));
                }
            }
        }
        // Myst, K'veer, the Tiwah, Laki'Ahn, Direbo, Tahgira: hopefully, none required
    }

    public static void PostMod_FixTdlmDoors(prpfile prp)
    {
        // disables the clickable physics, so we can go through the door.
        PrpRootObject ro = prp.findObject("cRespRoofDoorCloseToOpen", Typeid.plResponderModifier);
        if (ro == null)
            ro = prp.findObject("cRespRoofDoorClosedToOpen", Typeid.plResponderModifier);
        prpobjects.plResponderModifier rm = ro.castTo();
        for (plResponderModifier.PlResponderState state: rm.messages)
        {
            state.waitToCmdTable.get(0).cmd+=2;

            plResponderModifier.PlResponderCmd disableBtn1 = plResponderModifier.PlResponderCmd.createEmpty();
            disableBtn1.waitOn = -1;
            PrpMessage.PlSimSuppressMsg ssmsg1 = PrpMessage.PlSimSuppressMsg.createEmpty();
            ssmsg1.b1 = 1;
            ssmsg1.parent = plMessage.createWithSenderAndReceiver(Uruobjectref.createFromUruobjectdesc(ro.header.desc),
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "ClkDoorExt", prp.header.pageid));
            disableBtn1.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, ssmsg1);

            state.commands.add(disableBtn1);

            plResponderModifier.PlResponderCmd disableBtn2 = plResponderModifier.PlResponderCmd.createEmpty();
            disableBtn2.waitOn = -1;
            PrpMessage.PlSimSuppressMsg ssmsg2 = PrpMessage.PlSimSuppressMsg.createEmpty();
            ssmsg2.b1 = 1;
            ssmsg2.parent = plMessage.createWithSenderAndReceiver(Uruobjectref.createFromUruobjectdesc(ro.header.desc),
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "ClkDoorInt", prp.header.pageid));
            disableBtn2.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, ssmsg2);

            state.commands.add(disableBtn2);

            break; // there is only one state in these responders
        }

        PrpRootObject ro2 = prp.findObject("cRespRoofDoorOpenToClosed", Typeid.plResponderModifier);
        if (ro2 == null)
            ro2 = prp.findObject("cRespRoofDoorOpenToClose", Typeid.plResponderModifier);
        prpobjects.plResponderModifier rm2 = ro2.castTo();
        for (plResponderModifier.PlResponderState state: rm2.messages)
        {
            state.waitToCmdTable.get(0).cmd+=2;

            plResponderModifier.PlResponderCmd disableBtn1 = plResponderModifier.PlResponderCmd.createEmpty();
            disableBtn1.waitOn = -1;
            PrpMessage.PlSimSuppressMsg ssmsg1 = PrpMessage.PlSimSuppressMsg.createEmpty();
            ssmsg1.b1 = 0;
            ssmsg1.parent = plMessage.createWithSenderAndReceiver(Uruobjectref.createFromUruobjectdesc(ro2.header.desc),
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "ClkDoorExt", prp.header.pageid));
            disableBtn1.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, ssmsg1);

            state.commands.add(4, disableBtn1);

            plResponderModifier.PlResponderCmd disableBtn2 = plResponderModifier.PlResponderCmd.createEmpty();
            disableBtn2.waitOn = -1;
            PrpMessage.PlSimSuppressMsg ssmsg2 = PrpMessage.PlSimSuppressMsg.createEmpty();
            ssmsg2.b1 = 0;
            ssmsg2.parent = plMessage.createWithSenderAndReceiver(Uruobjectref.createFromUruobjectdesc(ro2.header.desc),
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "ClkDoorInt", prp.header.pageid));
            disableBtn2.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSimSuppressMsg, ssmsg2);

            state.commands.add(4, disableBtn2);

            break; // there is only one state in these responders
        }
    }

    public static void PostMod_FixTdlmTramLevers(prpfile prp)
    {
        // AxisAnimModifier specifies break points for the anim, anim must stop when reaching this point.
        // However, this parameter doesn't exist on PotS, which means when the reset responder tell
        // the anim "play forward/backward (until middle specified by break point)", the anim goes
        // all the way to the end/beginning of the anim.
        // To prevent this, we tell it to play _until_ a specified time.

        PrpRootObject ro = prp.findObject("cRespCarLevReset", Typeid.plResponderModifier);
        prpobjects.plResponderModifier rm = ro.castTo();

        plResponderModifier.PlResponderState st1 = rm.messages.get(0);
        PrpTaggedObject msg = st1.commands.get(2).message;
        PrpMessage.PlAnimCmdMsg amsg = msg.castTo();
        amsg.time = new Flt(0.5f);
        amsg.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kPlayToTime);

        plResponderModifier.PlResponderState st2 = rm.messages.get(1);
        PrpTaggedObject msg2 = st2.commands.get(2).message;
        PrpMessage.PlAnimCmdMsg amsg2 = msg2.castTo();
        amsg2.time = new Flt(0.5f);
        amsg2.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kPlayToTime);


        // For some reason anim events for the two cranks don't have a receiver, and the python arguments are wrong
        // and for some reason it still works in Myst V

        PrpRootObject anevro1 = prp.findObject("cAnimEvntDraggedDock1", Typeid.plAnimEventModifier);
        PrpRootObject anevro2 = prp.findObject("cAnimEvntDraggedDock3", Typeid.plAnimEventModifier);
        prpobjects.plAnimEventModifier anev1 = anevro1.castTo();
        prpobjects.plAnimEventModifier anev2 = anevro2.castTo();
        anev1.numreceivers++;
        anev2.numreceivers++;
        anev1.receivers = new Uruobjectref[1];
        anev2.receivers = new Uruobjectref[1];
        anev1.receivers[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "cPythTram", prp.header.pageid);
        anev2.receivers[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "cPythTram", prp.header.pageid);

        PrpRootObject pyro = prp.findObject("cPythTram", Typeid.plPythonFileMod);
        prpobjects.plPythonFileMod py = pyro.castTo();

        for (int i=0; i<py.listings.size(); i++)
        {
            if (py.listings.get(i).index == 14 || py.listings.get(i).index == 16)
            {
                py.listings.remove(i);
                i--;
            }
        }

        py.listings.add(
            prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 14, Uruobjectref.createDefaultWithTypeNamePage(
                    Typeid.plAnimEventModifier, "cAnimEvntDraggedDock1", prp.header.pageid)));

        py.listings.add(
            prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 16, Uruobjectref.createDefaultWithTypeNamePage(
                    Typeid.plAnimEventModifier, "cAnimEvntDraggedDock3", prp.header.pageid)));


//        // while we're at it, add the subworld reference to the list of children to the tram.
//        PrpRootObject coro = prp.findObject("TramCarGroup", Typeid.plCoordinateInterface);
//        prpobjects.plCoordinateInterface co = coro.castTo();
//        co.count++;
//        Uruobjectref children[] = new Uruobjectref[co.count];
//
//        int i=0;
//        for (Uruobjectref child: co.children)
//        {
//            children[i] = co.children[i];
//            i++;
//        }
//
//        children[co.count-1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "TramCarGroup", Pageid.createFromPrefixPagenum(87, 99));
//        co.children = children;
//
//        // and put some physicals in this subworld !
//        PrpRootObject physro1 = prp.findObject("TramCarDoor1Blocker", Typeid.plHKPhysical);
//        prpobjects.plHKPhysical phys1 = physro1.castTo();
//        phys1.ode.convertee.subworld = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plHKSubWorld, "TramCarGroup", Pageid.createFromPrefixPagenum(87, 99));
//
//        PrpRootObject physro2 = prp.findObject("TramCarDoor2Blocker", Typeid.plHKPhysical);
//        prpobjects.plHKPhysical phys2 = physro2.castTo();
//        phys2.ode.convertee.subworld = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plHKSubWorld, "TramCarGroup", Pageid.createFromPrefixPagenum(87, 99));
//
//        PrpRootObject physro3 = prp.findObject("TramCarLever", Typeid.plHKPhysical);
//        prpobjects.plHKPhysical phys3 = physro3.castTo();
//        phys3.ode.convertee.subworld = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plHKSubWorld, "TramCarGroup", Pageid.createFromPrefixPagenum(87, 99));
//
//        PrpRootObject physro4 = prp.findObject("TramCarLeverDtct", Typeid.plHKPhysical);
//        prpobjects.plHKPhysical phys4 = physro4.castTo();
//        phys4.ode.convertee.subworld = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plHKSubWorld, "TramCarGroup", Pageid.createFromPrefixPagenum(87, 99));


        // entirely disable tram colliders
        for (int i=1; i<5; i++)
            prp.markObjectDeleted(Typeid.plSceneObject, "TramCollisionMesh0"+i);

        // and also remove xrgns that warp us out of the tram.
        // This has the side effect of allowing players to jump off the dock, and into the empty.
        // Not much of an issue, it will even please my skydivers friends.
        // However, due to current setup of colliders, we can still jump from the tram roof while it's moving
        // - I simply don't want to bother with making it SDL dependant.
        prp.markObjectDeleted(Typeid.plSceneObject, "xRgnPillar1TramStation");
        prp.markObjectDeleted(Typeid.plSceneObject, "xRgnTram");

        // and set it to parent our new objects in dusttest.prp
        PrpRootObject coro = prp.findObject("TramCarGroup", Typeid.plCoordinateInterface);
        prpobjects.plCoordinateInterface co = coro.castTo();
        co.count++;
        Uruobjectref children[] = new Uruobjectref[co.count];

        int i=0;
        for (Uruobjectref child: co.children)
        {
            children[i] = co.children[i];
            i++;
        }

        children[co.count-1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "TramNewObjectsGroup", Pageid.createFromPrefixPagenum(87, 90));
        co.children = children;
    }

    public static void PostMod_TweakEnvmapSettings(prpfile prp)
    {
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plDynamicEnvMap))
        {
            prpobjects.plDynamicEnvMap dem = ro.castTo();

            // age-specific fix for VisRegions and root nodes
            // root nodes have been checked in Blender, should give the best possible result (still doesn't follow the actual object though...)
            // Main problem with RootNodes: it seems Plasma automatically calculates visregions based
            // on the location of this rootnode; which isn't the case of CC, when using pos vector.
            // Which means we must postedit all those to add correct visregions...
            // Plus, we must add all visregionsname which aren't in the prp...

            // Noloben rock lens. Using Plasma's ugly refraction instead of reflection. Nothing better I can think of.
            if (ro.header.desc.objectname.toString().equals("EnvMap-LensCamera"))
            {
                dem.fPos = new Vertex(2.461743f, -362.640686f, 24.492285f);
                dem.refreshRate = new Flt(0.02f);
            }
            // Noloben pool puzzle (just a little higher than default, still won't look good anyway)
            if (ro.header.desc.objectname.toString().equals("EnvMap-Pool Puzzle"))
            {
                dem.fPos = new Vertex(dem.fPos.x.toJavaFloat(), dem.fPos.y.toJavaFloat(), dem.fPos.z.toJavaFloat() + 0.5f);
            }

            // note: todelmer has a LOT of dynenvmap already - might have to make sure all are correctly positioned, with correct visreg...

            // Todelmer planet surface. Fortunately looks just as good as a dynamic cam map.
            if (ro.header.desc.objectname.toString().equals("EnvMapLandRivers"))
            {
                Uruobjectref[] newvrarray = new Uruobjectref[dem.numVisRegions + 1];
                int i=0;
                for (Uruobjectref ref: dem.visRegions)
                {
                    newvrarray[i] = ref;
                    i++;
                }
                newvrarray[i] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetSky", Pageid.createFromPrefixPagenum(87, 4));
                dem.visRegions = newvrarray;
                dem.numVisRegions++;
                dem.refreshRate = new Flt(0.08f);
            }

            // Todelmer pillar 1: remove top pillar visrgn and interior visreg (which looks bad) and replace it with sky visrgn (which got lost in visregionnames)
            if (ro.header.desc.objectname.toString().equals("EnvMapPlanarRoof1"))
            {
                Uruobjectref[] newvrarray = new Uruobjectref[dem.numVisRegions];
                int i=0;
                for (Uruobjectref ref: dem.visRegions)
                {
                    if (!ref.xdesc.objectname.toString().equals("EffVisSetP1Refl"))
                    {
                        newvrarray[i] = ref;
                        i++;
                    }
                }
                newvrarray[i] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetSky", Pageid.createFromPrefixPagenum(87, 4));
                dem.visRegions = newvrarray;
                //dem.numVisRegions++;

                // offset it a bit, this avoids being centered inside a cylindrical object
                // not required because we disabled the visrgn with this object.
                //dem.fPos = new Vertex(dem.fPos.x.toJavaFloat()-40f, dem.fPos.y.toJavaFloat(), dem.fPos.z.toJavaFloat());

                // and add a refresh rate to it !
                dem.refreshRate = new Flt(0.01f); // must be high enough for falling stars

                // double the texture rez so it looks better
                // (there are 7 textures to modify, but because of the way cammaps are converted, the 6
                // next are the same instance as the parent one)
                dem.faces.parent.u1 *= 2; // width
                dem.faces.parent.u2 *= 2; // height
                dem.faces.parent.xu10 *= 2; // right ?
                dem.faces.parent.xu11 *= 2; // bottom ?
            }

            // Todelmer pillar 3 (I removed interior and pillar top visregion, which don't look good)
            if (ro.header.desc.objectname.toString().equals("EnvMapPlanarRoof3"))
            {
                Uruobjectref[] newvrarray = new Uruobjectref[dem.numVisRegions];
                int i=0;
                for (Uruobjectref ref: dem.visRegions)
                {
                    if (!ref.xdesc.objectname.toString().equals("EffVisSetP3Refl"))
                    {
                        newvrarray[i] = ref;
                        i++;
                    }
                }
                newvrarray[i] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetSky", Pageid.createFromPrefixPagenum(87, 4));
                dem.visRegions = newvrarray;
                //dem.numVisRegions++;

                // offset it a bit, this avoids being centered inside the pillar entrance
                // not required since in disabled visregion
                //dem.fPos = new Vertex(dem.fPos.x.toJavaFloat()+32f, dem.fPos.y.toJavaFloat()-38f, dem.fPos.z.toJavaFloat());

                // and add a refresh rate to it !
                dem.refreshRate = new Flt(0.01f);

                // double the texture rez so it looks better.
                dem.faces.parent.u1 *= 2; // width
                dem.faces.parent.u2 *= 2; // height
                dem.faces.parent.xu10 *= 2; // right ?
                dem.faces.parent.xu11 *= 2; // bottom ?
            }

            // Todelmer power scopes: now working !
            if (ro.header.desc.objectname.toString().equals("EnvMap-PowerCam"))
            {
                /*
                Beware PRP magic !
                In Myst V, these work thanks to a camera with a 5 deg FOV pointed at the surface of a rotating ball, with a gradient uvmapping
                applied to it.

                Problem: since the console uses the envmap as a reflection, we're staring in the wrong direction.
                In Noloben, I fixed this by using refraction instead - but it looks so bad, plus we still have a too wide FOV

                Here, I kept the reflective screen, and put the reflection point inside the rotating ball. Now, since we can't see anything
                this way, I made the ball surface twosided - which means it can be seen from the inside.
                This also fixes the issue with the FOV, because the render point is VERY close to the ball's surface. And since the screen is
                sphere-shaped, it's enough to reduce the FOV to something that looks fine !
                */
                if (prp.header.pageid.getPageNumber() == 2) // pillar 1
                {
                    // new coordinates found in Blender
                    dem.fPos = new Vertex(888.352783f, 138.264038f, -156.976501f);

                    // also need to reduce hither, we're very close to the ball's surface
                    dem.hither = new Flt(0.1f);

                    // and add a refresh rate to it !
                    dem.refreshRate = new Flt(0.03f); // lower values don't make it any better... that's the limit of dynenvmaps

                    PrpRootObject lyrro = prp.findObject("powergrad", Typeid.plLayer);
                    if (lyrro != null)
                    {
                        prpobjects.x0006Layer lyr = lyrro.castTo();
                        lyr.flags5 |= prpobjects.x0006Layer.kMiscTwoSided;
                    }
                }
                else if (prp.header.pageid.getPageNumber() == 3) // pillar 3
                {
                    dem.hither = new Flt(0.1f);
                    dem.fPos = new Vertex(-859.970764f, 242.050507f, -29.507595f);

                    // and add a refresh rate to it !
                    dem.refreshRate = new Flt(0.03f);

                    PrpRootObject lyrro = prp.findObject("powergrad", Typeid.plLayer);
                    if (lyrro != null)
                    {
                        prpobjects.x0006Layer lyr = lyrro.castTo();
                        lyr.flags5 |= prpobjects.x0006Layer.kMiscTwoSided;
                    }
                }
            }

            // scopes: these won't work either way... See the workaround I added in the Python file

            // scope 1
            if (ro.header.desc.objectname.toString().equals("EnvMapCam01"))
            {
                // disable it and the 4 others
                // Personally, I think the view is quite stunning, however it's totally irrealistic
                // (and only lessens FPS if we update it)

                // clever trick: use one of the visregions from the pod, which disable the whole exterior scene.

                dem.numVisRegions++;
                Uruobjectref[] visrgn = new Uruobjectref[1];
                visrgn[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetPodView", Pageid.createFromPrefixPagenum(87, 12));
                dem.visRegions = visrgn;
            }
            // scope 2
            if (ro.header.desc.objectname.toString().equals("EnvMapCam02"))
            {
                // same as above

                dem.numVisRegions++;
                Uruobjectref[] visrgn = new Uruobjectref[1];
                visrgn[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetPodView", Pageid.createFromPrefixPagenum(87, 12));
                dem.visRegions = visrgn;
            }
            // scope 3
            if (ro.header.desc.objectname.toString().equals("EnvMapCam03"))
            {
                // same as above

                dem.numVisRegions++;
                Uruobjectref[] visrgn = new Uruobjectref[1];
                visrgn[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetPodView", Pageid.createFromPrefixPagenum(87, 12));
                dem.visRegions = visrgn;
            }
            // pod camera
            if (ro.header.desc.objectname.toString().equals("EnvMap-CamIntoPod"))
            {
                // same as above
                // the Python simply never enables its drawables. Simpler.
            }






            // things that were already envmaps


            if (ro.header.desc.objectname.toString().equals("EnvMap-Pod"))
            {
                // this envmap doesn't have any visregion. This adds some.
                Uruobjectref[] newvrarray = new Uruobjectref[2];
                newvrarray[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "EffVisSetPodView", prp.header.pageid);
                newvrarray[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVisRegion, "VisRegion-Pod", prp.header.pageid);
                dem.visRegions = newvrarray;
                dem.numVisRegions=2;
            }
        }
    }

    public static void PostMod_FixTdlmPowerDraggables(prpfile prp)
    {
        // ok, so it really isn't easy with the animations classes being not really user-friendly. Either way, should work fine

        // Todelmer power scope 1
        PrpRootObject atcro = prp.findObject("PowerJoystickLeftRight_horiz_anim_0", Typeid.plATCAnim);
        if (atcro != null)
        {
            prpobjects.plATCAnim atc = atcro.castTo();

            atc.loopEnd = new Flt(atc.loopEnd.toJavaFloat() / 10.f);
            atc.parent.stoptime = new Flt(atc.parent.stoptime.toJavaFloat() / 10.f);

            PrpTaggedObject ourEffect = atc.parent.effects[1]; // take the matrix controller
            prpobjects.plAGAnim.plMatrixControllerChannel effect = ourEffect.castTo(); // cast it
            //m.status(""+effect.controller.type);
            prpobjects.PrpController.plCompoundController ctrl = effect.controller.castTo(); // take the compound controller in it
            //m.status(""+ctrl.u2.type);
            prpobjects.PrpController.plCompoundController rotCtrl = ctrl.u2.castTo(); // take the rotation controller
            //m.status(""+rotCtrl.u3.type);
            prpobjects.PrpController.plLeafController rotLeafCtrl = rotCtrl.u3.castTo(); // take the Z controller


            // take the Z rotation things... err, should be moul-type-4 ?
            //m.status(rotLeafCtrl.controllertype+"");
            for (PrpController.moul4 weirdCtrl: rotLeafCtrl.xtype4)
            {
                //m.status(weirdCtrl.data1 + "; " + weirdCtrl.data2 + "; " + weirdCtrl.data3 + "; " + weirdCtrl.framenum + "; ");
                if (weirdCtrl.framenum == 100)
                    // make the frame number 10, and get out of this animation mess
                    weirdCtrl.framenum = 10;
            }
        }

        PrpRootObject atcro2 = prp.findObject("PowerJoystickUpDown_vert_anim_0", Typeid.plATCAnim);
        if (atcro2 != null)
        {
            prpobjects.plATCAnim atc = atcro2.castTo();

            atc.loopEnd = new Flt(atc.loopEnd.toJavaFloat() / 10.f);
            atc.parent.stoptime = new Flt(atc.parent.stoptime.toJavaFloat() / 10.f);

            PrpTaggedObject ourEffect = atc.parent.effects[1]; // take the matrix controller
            prpobjects.plAGAnim.plMatrixControllerChannel effect = ourEffect.castTo(); // cast it
            //m.status(""+effect.controller.type);
            prpobjects.PrpController.plCompoundController ctrl = effect.controller.castTo(); // take the compound controller in it
            //m.status(""+ctrl.u2.type);
            prpobjects.PrpController.plCompoundController rotCtrl = ctrl.u2.castTo(); // take the rotation controller
            //m.status(""+rotCtrl.u2.type);
            prpobjects.PrpController.plLeafController rotLeafCtrl = rotCtrl.u2.castTo(); // take the Y controller


            // take the Z rotation things... err, should be moul-type-4 ?
            //m.status(rotLeafCtrl.controllertype+"");
            for (PrpController.moul4 weirdCtrl: rotLeafCtrl.xtype4)
            {
                //m.status(weirdCtrl.data1 + "; " + weirdCtrl.data2 + "; " + weirdCtrl.data3 + "; " + weirdCtrl.framenum + "; ");
                if (weirdCtrl.framenum == 100)
                    // make the frame number 10, and get out of this animation mess
                    weirdCtrl.framenum = 10;
            }
        }
    }

    public static void PostMod_FadeBubble(prpfile prp)
    {
        // makes Age bubbles fade at close range.
        // Certainly doesn't look as good as with the bubble shader modifier, it's using the pixel depth buffer instead of ranged vertex,
        // resulting in the fade region being cubic instead of spherical. However, since it's setup correctly, it's hardly noticeable.

        for (PrpRootObject matro: prp.FindAllObjectsOfType(Typeid.hsGMaterial))
        {
            // naming for these material is usually BubbleInterior, BubbleInner01/Tk, etc
            if (!matro.header.desc.objectname.toString().contains("BubbleIn")) continue;
            // do NOT process this layer ! It's not a bubble envmap, but the animation switching between all envmaps.
            if (matro.header.desc.objectname.toString().equals("BubbleInteriorTdlm")) continue;
            // do NOT process this one either. It also has a layer animation. Will have to find a fix for this one.
            if (matro.header.desc.objectname.toString().equals("BubbleInteriorTdlm0")) continue;

            prpobjects.x0007Material mat = matro.castTo();

            PrpRootObject lyrro = prp.findObjectWithRef(mat.layerrefs.get(0));
            prpobjects.x0006Layer lyr = lyrro.castTo();

            lyr.flags5 |= prpobjects.x0006Layer.kMiscBindNext;

            prpobjects.x0006Layer nlyr = prpobjects.x0006Layer.createEmpty();

            nlyr.parent = x0041LayerInterface.createEmpty();
            nlyr.flags1 = prpobjects.x0006Layer.kBlendAlpha
                    | prpobjects.x0006Layer.kBlendAlphaMult
                    | prpobjects.x0006Layer.kBlendNoTexColor;
            nlyr.flags2 = prpobjects.x0006Layer.kClampTextureU | prpobjects.x0006Layer.kClampTextureV;
            nlyr.flags3 = 0;
            nlyr.flags4 = prpobjects.x0006Layer.kZNoZWrite;
            nlyr.flags5 = 0;
            nlyr.matrix = Transmatrix.createDefault();
            nlyr.matrix.isnotIdentity = 1;

                    // not really sure how these attibutes work, but they remap the UV to be screen-depth based.
                    // basically:
                    //      faded (near fade rgn) visible (far fade rgn) faded.
                    // fade region has a strength.
                    // fade region is more or less located to f/{FS}
                    // fade strength is contolled by {FS}.

                    // I couldn't figure out the exact mathematical function, but these values should work.

                    float nFS=.45f;     /* near fade strength */ float nF=1f;   // near fade
                    float fFS=.035f;     /* far fade strength */  float fF=3.5f;   // far fade

                    nlyr.matrix.setelement(0, 0, 0); nlyr.matrix.setelement(0, 1, 0); nlyr.matrix.setelement(0, 2, nFS);  nlyr.matrix.setelement(0, 3, -nF);
                    nlyr.matrix.setelement(1, 0, 0); nlyr.matrix.setelement(1, 1, 0); nlyr.matrix.setelement(1, 2, -fFS); nlyr.matrix.setelement(1, 3, fF);
                    nlyr.matrix.setelement(2, 0, 0); nlyr.matrix.setelement(2, 1, 0); nlyr.matrix.setelement(2, 2, 0);    nlyr.matrix.setelement(2, 3, 0);
                    nlyr.matrix.setelement(3, 0, 0); nlyr.matrix.setelement(3, 1, 0); nlyr.matrix.setelement(3, 2, 0);    nlyr.matrix.setelement(3, 3, 1);

            nlyr.ambient = Rgba.createFromVals(0f, 0f, 0f, 1f);
            nlyr.diffuse = Rgba.createFromVals(0f, 0f, 0f, 1f);
            nlyr.emissive = Rgba.createFromVals(1f, 1f, 1f, 1f);
            nlyr.specular = Rgba.createFromVals(0f, 0f, 0f, 1f);
            nlyr.uvwSource = 131072;
            nlyr.opacity=new Flt(1f);
            nlyr.lodbias=new Flt(-1f);
            nlyr.specularPower=new Flt(1f);
            // hopefully, the texture is always loaded for each Age...
            Uruobjectref ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plMipMap, "AttenRampMult_0_100", Pageid.createFromPrefixPagenum(prp.header.pageid.getSequencePrefix(), -1));
            ref.xdesc.pagetype.pagetype = 8; // textures have specials plKey flag (BuiltIn), it seems.
            nlyr.texture = ref;
            nlyr.shader1 = Uruobjectref.none();
            nlyr.shader2 = Uruobjectref.none();
            nlyr.identity = Transmatrix.createFromMatrix44(Matrix44.createIdentity());
            nlyr.parent.ref = Uruobjectref.none();

            String name = mat.layerrefs.get(0).xdesc.objectname.toString() + "_funkRamp";


            Uruobjectref lyrref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLayer, name, prp.header.pageid);
            PrpRootObject lyrroot = PrpRootObject.createFromDescAndObject(lyrref.xdesc, nlyr);
            prp.addObject(lyrroot);

            mat.layerrefs.add(lyrref);
        }
    }

    public static void PostMod_ReplaceDirectMusicSound(prpfile prp)
    {
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plWinAudio))
        {
            prpobjects.plWinAudible wa = ro.castTo();

            for (int i=0; i<wa.count;i++)
            {
                Uruobjectref ref = wa.objectrefs[i];
                if (ref.xdesc.objecttype == Typeid.plDirectMusicSound)
                {
                    PrpRootObject dmsro = prp.findObjectWithRef(ref);
                    if (dmsro != null)
                    {
                        // switch the ref to the dms' parent, add this parent to the prp, and delete the dms

                        prpobjects.plDirectMusicSound dms = dmsro.castTo();

                        prpobjects.plWin32Sound w32s = prpobjects.plWin32Sound.createEmpty();
                        w32s.channel = 0; w32s.parent = dms.parent;

                        Uruobjectref w32sref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plWin32StreamingSound,
                                ref.xdesc.objectname.toString(), prp.header.pageid);
                        PrpRootObject w32root = PrpRootObject.createFromDescAndObject(w32sref.xdesc, w32s);
                        prp.addObject(w32root);

                        prp.markObjectDeleted(ref, true);

                        wa.objectrefs[i] = w32sref;

                        // and don't forget to set the sound buffer. Just doing some string mess, only works if Cyan actually
                        // kept the correct name between objects...
                        // The sound buffer itself seems to always be created so the sound's loaded by the engine before use.

                        // funny error: the filename string I first chose was wrong. Result: garbage, containing default "mystnerd" decrypt key.
                        // Being called a nerd by my own script definitely hurts my pride.
                        w32s.parent.dataBuffer = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSoundBuffer,
                                dms.filename.toString().substring(0, dms.filename.toString().length()-4) + ".ogg",
                                prp.header.pageid);
                    }
                }
            }
        }

        // replace crossfademsg. Took me hours to get it working right.
        if (prp.header.agename.toString().equals("Laki") && prp.header.pagename.toString().equals("Exterior"))
        {
            PrpRootObject ro = prp.findObject("cSfxResp-ArenaRevealStart", Typeid.plResponderModifier);
            prpobjects.plResponderModifier resp = ro.castTo();
            PrpMessage.PlSoundMsg sm = resp.messages.get(0).commands.get(0).message.castTo();

            sm.parent.parent.broadcastFlags = 0x800;
            sm.parent.parent.receivers.set(0, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAudioInterface, "cSfxArenaRevealMusic", prp.header.pageid));
            sm.begin = Double64.fromJavaDouble(0);
            sm.cmd = new HsBitVector(PrpMessage.PlSoundMsg.kPlay | PrpMessage.PlSoundMsg.kIsLocalOnly);
            sm.end = Double64.fromJavaDouble(0);
            sm.fadetype = 0;
            sm.index = 0;
            sm.loop = 0;
            sm.namestr = 0;
            sm.playing = 0;
            sm.repeats = 0;
            sm.speed = new Flt(0);
            sm.time = Double64.fromJavaDouble(0);
            sm.volume = new Flt(0);

            // stop already playing music
            PrpMessage.PlSoundMsg nsm = PrpMessage.PlSoundMsg.createDefault();
            nsm.parent.parent = plMessage.createWithSenderAndReceiver(
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cSfxResp-ArenaRevealStart", prp.header.pageid),
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAudioInterface, "cSfxArenaRevealMusic", prp.header.pageid));
            nsm.parent.parent.broadcastFlags = 0x800;
            nsm.cmd = new HsBitVector(PrpMessage.PlSoundMsg.kStop | PrpMessage.PlSoundMsg.kIsLocalOnly);
            nsm.index = 1;

            plResponderModifier.PlResponderCmd ncmd = prpobjects.plResponderModifier.PlResponderCmd.createEmpty();
            ncmd.waitOn = -1;
            ncmd.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSoundMsg, nsm);

            // ideally we would also wait a bit for the first music to fade out (which isn't currently the case)
            // before starting the next one, since without cross-fading, it sounds a bit bad... but I'm not going to waist more time on this

            resp.messages.get(0).commands.add(0, ncmd);
            resp.messages.get(0).waitToCmdTable.get(0).cmd++;
        }

        // descent imagers. We like hearing mad Yeesha, don't we ? :)
        if (prp.header.agename.toString().contains("Descent"))
        {
            if (prp.header.pagename.toString().equals("dsntTianaCaveTunnel1"))
            {
                PrpRootObject ro = prp.findObject("cRespImager01On", Typeid.plResponderModifier);
                prpobjects.plResponderModifier resp = ro.castTo();

                PrpMessage.PlSoundMsg nsm = PrpMessage.PlSoundMsg.createDefault();
                nsm.parent.parent = plMessage.createWithSenderAndReceiver(
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespImager01On", prp.header.pageid),
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAudioInterface, "cSfxYeeshaImager01", Pageid.createFromPrefixPagenum(94, 13)));
                nsm.parent.parent.broadcastFlags = 0x800;
                nsm.cmd = new HsBitVector(PrpMessage.PlSoundMsg.kPlay | PrpMessage.PlSoundMsg.kIsLocalOnly);

                plResponderModifier.PlResponderCmd ncmd = prpobjects.plResponderModifier.PlResponderCmd.createEmpty();
                ncmd.waitOn = -1;
                ncmd.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSoundMsg, nsm);

                resp.messages.get(0).commands.add(6, ncmd);
                for (plResponderModifier.PlResponderState.WaitToCmd wait: resp.messages.get(0).waitToCmdTable)
                    if (wait.cmd >= 6)
                        wait.cmd++;
            }


            if (prp.header.pagename.toString().equals("dsntShaftGeneratorRoom"))
            {
                PrpRootObject ro = prp.findObject("cRespImager02On", Typeid.plResponderModifier);
                prpobjects.plResponderModifier resp = ro.castTo();

                PrpMessage.PlSoundMsg nsm = PrpMessage.PlSoundMsg.createDefault();
                nsm.parent.parent = plMessage.createWithSenderAndReceiver(
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespImager02On", prp.header.pageid),
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAudioInterface, "cSfxYeeshaImagerMx02", prp.header.pageid));
                nsm.parent.parent.broadcastFlags = 0x800;
                nsm.cmd = new HsBitVector(PrpMessage.PlSoundMsg.kPlay | PrpMessage.PlSoundMsg.kIsLocalOnly);
                nsm.index = 4;

                plResponderModifier.PlResponderCmd ncmd = prpobjects.plResponderModifier.PlResponderCmd.createEmpty();
                ncmd.waitOn = -1;
                ncmd.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSoundMsg, nsm);

                resp.messages.get(0).commands.add(6, ncmd);
                for (plResponderModifier.PlResponderState.WaitToCmd wait: resp.messages.get(0).waitToCmdTable)
                    if (wait.cmd >= 6)
                        wait.cmd++;

                // edit sound properties (this took me a VERY long while to figure out)
                PrpRootObject sbro = prp.findObject("dsntYeesha-Imager02_eng.ogg", Typeid.plSoundBuffer);
                prpobjects.plSoundBuffer sb = sbro.castTo();
                sb.flags = 0x13; // external - always external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager02_fre.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager02_ger.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
            }


            if (prp.header.pagename.toString().equals("dsntPostShaftNodeAndTunnels"))
            {
                PrpRootObject ro = prp.findObject("cRespImager03On", Typeid.plResponderModifier);
                prpobjects.plResponderModifier resp = ro.castTo();

                PrpMessage.PlSoundMsg nsm = PrpMessage.PlSoundMsg.createDefault();
                nsm.parent.parent = plMessage.createWithSenderAndReceiver(
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespImager03On", prp.header.pageid),
                        Uruobjectref.createDefaultWithTypeNamePage(Typeid.plAudioInterface, "cSfxYeeshaImagerMx03", prp.header.pageid));
                nsm.parent.parent.broadcastFlags = 0x800;
                nsm.cmd = new HsBitVector(PrpMessage.PlSoundMsg.kPlay | PrpMessage.PlSoundMsg.kIsLocalOnly);
                nsm.index = 4;

                plResponderModifier.PlResponderCmd ncmd = prpobjects.plResponderModifier.PlResponderCmd.createEmpty();
                ncmd.waitOn = -1;
                ncmd.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plSoundMsg, nsm);

                resp.messages.get(0).commands.add(6, ncmd);
                for (plResponderModifier.PlResponderState.WaitToCmd wait: resp.messages.get(0).waitToCmdTable)
                    if (wait.cmd >= 6)
                        wait.cmd++;

                // edit sound properties
                PrpRootObject sbro = prp.findObject("dsntYeesha-Imager03_eng.ogg", Typeid.plSoundBuffer);
                prpobjects.plSoundBuffer sb = sbro.castTo();
                sb.flags = 0x13; // external - always external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager03_fre.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager03_ger.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
            }

            if (prp.header.pagename.toString().equals("dsntTianaCave"))
            {
                // edit sound properties
                PrpRootObject sbro = prp.findObject("dsntYeesha-Imager01_eng.ogg", Typeid.plSoundBuffer);
                prpobjects.plSoundBuffer sb = sbro.castTo();
                sb.flags = 0x13; // external - always external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager01_fre.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
                sbro = prp.findObject("dsntYeesha-Imager01_ger.ogg", Typeid.plSoundBuffer);
                sb = sbro.castTo();
                sb.flags = 0x11; // external - stream compressed
            }
        }
    }

    public static void PostMod_FixLakiMazeButtons(prpfile prp)
    {
        /*
        Looks like setting mass in the physics ignore an object's rotation (and scale). Why ? Because
        rotation is combined with scale because of the object's coordinate system being stored in a matrix.
        And Havok (or at least its implementation in Plasma) doesn't know how to scale movable objects, which
        wouldn't then be rigid bodies anymore.

        Only solution: do what Dustin did: remove mass, and put the physics at the object's location... :/
        */

        for (int i=1; i<=61; i++)
        {
            PrpRootObject ro;
            PrpRootObject ro2;

            if (i == 61)
            {
                // also required for outside button
                ro = prp.findObject("ExitDoorOpenBtnLight", Typeid.plHKPhysical);
                ro2 = prp.findObject("ExitDoorOpenBtnLight", Typeid.plCoordinateInterface);
            }
            else
            {
                ro = prp.findObject("MazeButton" + i, Typeid.plHKPhysical);
                ro2 = prp.findObject("MazeButton" + i, Typeid.plCoordinateInterface);
            }

            prpobjects.plHKPhysical hk = ro.castTo();
            prpobjects.plCoordinateInterface coo = ro2.castTo();

            float[][] coords = coo.localToWorld.convertToFloatArray();

            // since most of the data is determined when compiling the object, I'm using a quick ugly hack...
            hk.ode.sphereposoverride = new Vertex(new Flt(coords[0][3]), new Flt(coords[1][3]), new Flt(coords[2][3]));
            hk.ode.convertee.mass = Flt.zero();
        }

        // Also disable some useless blocker while we're at it
        // WTF ? No way to remove an object from the scenenode. Okaaaayyy...
        prp.markObjectDeleted(Typeid.plHKPhysical, "MazeElevUprBlocker");
    }

    public static void PostMod_FixDescentElev(prpfile prp)
    {
        // we need a detector region to parent the avatar for the ride and run camera thingies.

        // add the ref to the PythonFMod
        PrpRootObject ro1 = prp.findObject("cPythElevatorA", Typeid.plPythonFileMod);
        prpobjects.plPythonFileMod py1 = ro1.castTo();
        plPythonFileMod.Pythonlisting l1 = prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 27, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorA_enter", prp.header.pageid));
        plPythonFileMod.Pythonlisting l11 = prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 27, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorA_exit", prp.header.pageid));
        py1.addListing(l1);
        py1.addListing(l11);

        PrpRootObject ro2 = prp.findObject("cPythElevatorB", Typeid.plPythonFileMod);
        prpobjects.plPythonFileMod py2 = ro2.castTo();
        plPythonFileMod.Pythonlisting l2 = prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 27, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorB_enter", prp.header.pageid));
        plPythonFileMod.Pythonlisting l21 = prpobjects.plPythonFileMod.Pythonlisting.createWithRef(7, 27, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorB_exit", prp.header.pageid));
        py2.addListing(l2);
        py2.addListing(l21);


        // create the regions. It's crazy how much simpler it is when done manually.
            PrpRootObject ro3 = prp.findObject("RgnElevatorA", Typeid.plSceneObject);
            prpobjects.plSceneObject so3 = ro3.castTo();
            so3.modifiers.remove(so3.modifiers.get(0)); // remove useless ref

                // logic modifier
                prpobjects.plLogicModifier lo11 = prpobjects.plLogicModifier.createWithRef(Uruobjectref.createDefaultWithTypeNamePage
                            (Typeid.plPythonFileMod, "cPythElevatorA", prp.header.pageid));
                PrpMessage.PlNotifyMsg msg = lo11.parent.message.castTo();
                msg.parent.broadcastFlags = 0x840; // local+net propagate.
                lo11.conditionalcount = 2;
                Uruobjectref[] conds = new Uruobjectref[2];
                conds[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_enter", prp.header.pageid);
                conds[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_enter", prp.header.pageid);
                lo11.conditionals = conds;
                lo11.parent.u1 = new HsBitVector(0x40); // kMultiTrigger

                Uruobjectref lo11ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier,
                        "RgnElevatorA_enter", prp.header.pageid);
                PrpRootObject lo11ro = PrpRootObject.createFromDescAndObject(lo11ref.xdesc, lo11);
                prp.addObject(lo11ro);
                so3.modifiers.add(lo11ref);

                // volumne sensor
                plVolumeSensorConditionalObject volsens11 = prpobjects.plVolumeSensorConditionalObject.createDefault();

                Uruobjectref vs11ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject,
                        "RgnElevatorA_enter", prp.header.pageid);
                PrpRootObject vs11ro = PrpRootObject.createFromDescAndObject(vs11ref.xdesc, volsens11);
                prp.addObject(vs11ro);

                // ObjectInVolumeDetector
                plObjectInVolumeDetector oivd = plObjectInVolumeDetector.createDefault();
                oivd.parent.refs = new Uruobjectref[2];
                oivd.parent.count = 2;
                oivd.parent.refs[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_enter", prp.header.pageid);
                oivd.parent.refs[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorA_enter", prp.header.pageid);

                Uruobjectref oivd11ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plObjectInVolumeDetector,
                        "RgnElevatorA_enter", prp.header.pageid);
                PrpRootObject oivd11ro = PrpRootObject.createFromDescAndObject(oivd11ref.xdesc, oivd);
                prp.addObject(oivd11ro);
                so3.modifiers.add(oivd11ref);


                // SECOND one

                // logic modifier
                prpobjects.plLogicModifier lo12 = prpobjects.plLogicModifier.createWithRef(Uruobjectref.createDefaultWithTypeNamePage
                            (Typeid.plPythonFileMod, "cPythElevatorA", prp.header.pageid));
                PrpMessage.PlNotifyMsg msg2 = lo12.parent.message.castTo();
                msg2.parent.broadcastFlags = 0x800; // local propagate
                lo12.conditionalcount = 2;
                Uruobjectref[] conds2 = new Uruobjectref[2];
                conds2[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_exit", prp.header.pageid);
                conds2[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_exit", prp.header.pageid);
                lo12.conditionals = conds2;
                lo12.parent.u1 = new HsBitVector(0x40); // kMultiTrigger

                Uruobjectref lo12ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier,
                        "RgnElevatorA_exit", prp.header.pageid);
                PrpRootObject lo12ro = PrpRootObject.createFromDescAndObject(lo12ref.xdesc, lo12);
                prp.addObject(lo12ro);
                so3.modifiers.add(lo12ref);

                // volumne sensor
                plVolumeSensorConditionalObject volsens12 = prpobjects.plVolumeSensorConditionalObject.createDefault();
                volsens12.direction = 2; // make it exit this time

                Uruobjectref vs12ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject,
                        "RgnElevatorA_exit", prp.header.pageid);
                PrpRootObject vs12ro = PrpRootObject.createFromDescAndObject(vs12ref.xdesc, volsens12);
                prp.addObject(vs12ro);

                // ObjectInVolumeDetector
                plObjectInVolumeDetector oivd2 = plObjectInVolumeDetector.createDefault();
                oivd2.parent.refs = new Uruobjectref[2];
                oivd2.parent.count = 2;
                oivd2.parent.refs[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorA_exit", prp.header.pageid);
                oivd2.parent.refs[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorA_exit", prp.header.pageid);

                Uruobjectref oivd12ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plObjectInVolumeDetector,
                        "RgnElevatorA_exit", prp.header.pageid);
                PrpRootObject oivd12ro = PrpRootObject.createFromDescAndObject(oivd12ref.xdesc, oivd2);
                prp.addObject(oivd12ro);
                so3.modifiers.add(oivd12ref);

            // interface info
            plInterfaceInfoModifier iim = plInterfaceInfoModifier.createDefault();
            iim.count = 2;
            iim.logicmodifiers = new Uruobjectref[2];
            iim.logicmodifiers[0] = lo11ref;
            iim.logicmodifiers[1] = lo12ref;

            Uruobjectref iim1ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plInterfaceInfoModifier,
                    "RgnElevatorA", prp.header.pageid);
            PrpRootObject iim1ro = PrpRootObject.createFromDescAndObject(iim1ref.xdesc, iim);
            prp.addObject(iim1ro);
            so3.modifiers.add(iim1ref);



            // REGION 2

            PrpRootObject ro4 = prp.findObject("RgnElevatorB", Typeid.plSceneObject);
            prpobjects.plSceneObject so4 = ro4.castTo();
            so4.modifiers.remove(so4.modifiers.get(0)); // remove useless ref

                // logic modifier
                prpobjects.plLogicModifier lo21 = prpobjects.plLogicModifier.createWithRef(Uruobjectref.createDefaultWithTypeNamePage
                            (Typeid.plPythonFileMod, "cPythElevatorB", prp.header.pageid));
                PrpMessage.PlNotifyMsg msg3 = lo21.parent.message.castTo();
                msg3.parent.broadcastFlags = 0x800; // local propagate
                lo21.conditionalcount = 2;
                Uruobjectref[] conds3 = new Uruobjectref[2];
                conds3[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_enter", prp.header.pageid);
                conds3[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_enter", prp.header.pageid);
                lo21.conditionals = conds3;
                lo21.parent.u1 = new HsBitVector(0x40); // kMultiTrigger

                Uruobjectref lo21ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier,
                        "RgnElevatorB_enter", prp.header.pageid);
                PrpRootObject lo21ro = PrpRootObject.createFromDescAndObject(lo21ref.xdesc, lo21);
                prp.addObject(lo21ro);
                so4.modifiers.add(lo21ref);

                // volumne sensor
                plVolumeSensorConditionalObject volsens21 = prpobjects.plVolumeSensorConditionalObject.createDefault();

                Uruobjectref vs21ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject,
                        "RgnElevatorB_enter", prp.header.pageid);
                PrpRootObject vs21ro = PrpRootObject.createFromDescAndObject(vs21ref.xdesc, volsens21);
                prp.addObject(vs21ro);

                // ObjectInVolumeDetector
                plObjectInVolumeDetector oivd3 = plObjectInVolumeDetector.createDefault();
                oivd3.parent.refs = new Uruobjectref[2];
                oivd3.parent.count = 2;
                oivd3.parent.refs[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_enter", prp.header.pageid);
                oivd3.parent.refs[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorB_enter", prp.header.pageid);

                Uruobjectref oivd21ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plObjectInVolumeDetector,
                        "RgnElevatorB_enter", prp.header.pageid);
                PrpRootObject oivd21ro = PrpRootObject.createFromDescAndObject(oivd21ref.xdesc, oivd3);
                prp.addObject(oivd21ro);
                so4.modifiers.add(oivd21ref);


                // SECOND one

                // logic modifier
                prpobjects.plLogicModifier lo22 = prpobjects.plLogicModifier.createWithRef(Uruobjectref.createDefaultWithTypeNamePage
                            (Typeid.plPythonFileMod, "cPythElevatorB", prp.header.pageid));
                PrpMessage.PlNotifyMsg msg4 = lo22.parent.message.castTo();
                msg4.parent.broadcastFlags = 0x800; // local propagate
                lo22.conditionalcount = 2;
                Uruobjectref[] conds4 = new Uruobjectref[2];
                conds4[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_exit", prp.header.pageid);
                conds4[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_exit", prp.header.pageid);
                lo22.conditionals = conds4;
                lo22.parent.u1 = new HsBitVector(0x40); // kMultiTrigger

                Uruobjectref lo22ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier,
                        "RgnElevatorB_exit", prp.header.pageid);
                PrpRootObject lo22ro = PrpRootObject.createFromDescAndObject(lo22ref.xdesc, lo22);
                prp.addObject(lo22ro);
                so4.modifiers.add(lo22ref);

                // volumne sensor
                plVolumeSensorConditionalObject volsens22 = prpobjects.plVolumeSensorConditionalObject.createDefault();
                volsens22.direction = 2; // make it exit this time

                Uruobjectref vs22ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject,
                        "RgnElevatorB_exit", prp.header.pageid);
                PrpRootObject vs22ro = PrpRootObject.createFromDescAndObject(vs22ref.xdesc, volsens22);
                prp.addObject(vs22ro);

                // ObjectInVolumeDetector
                plObjectInVolumeDetector oivd4 = plObjectInVolumeDetector.createDefault();
                oivd4.parent.refs = new Uruobjectref[2];
                oivd4.parent.count = 2;
                oivd4.parent.refs[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plVolumeSensorConditionalObject, "RgnElevatorB_exit", prp.header.pageid);
                oivd4.parent.refs[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "RgnElevatorB_exit", prp.header.pageid);

                Uruobjectref oivd22ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plObjectInVolumeDetector,
                        "RgnElevatorB_exit", prp.header.pageid);
                PrpRootObject oivd22ro = PrpRootObject.createFromDescAndObject(oivd22ref.xdesc, oivd4);
                prp.addObject(oivd22ro);
                so4.modifiers.add(oivd22ref);

            // interface info
            plInterfaceInfoModifier iim2 = plInterfaceInfoModifier.createDefault();
            iim2.count = 2;
            iim2.logicmodifiers = new Uruobjectref[2];
            iim2.logicmodifiers[0] = lo21ref;
            iim2.logicmodifiers[1] = lo22ref;

            Uruobjectref iim2ref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plInterfaceInfoModifier,
                    "RgnElevatorB", prp.header.pageid);
            PrpRootObject iim2ro = PrpRootObject.createFromDescAndObject(iim2ref.xdesc, iim2);
            prp.addObject(iim2ro);
            so4.modifiers.add(iim2ref);


        // also move the xrgn safe point
        // since the xrgn is too big and encompasses the elevator, it'll warp the avatar to the safe point
        // which we put directly inside the elevator. This has the side effect of putting every avatar inside each other.
        // Ideally, we would simply edit the xrgn to make it thinner and move it to the side of the elevator,
        // but that's not as easy...
        PrpRootObject coro = prp.findObject("safenodetopA", Typeid.plCoordinateInterface);
        prpobjects.plCoordinateInterface co = coro.castTo();

        co.localToParent.setelement(0, 3, 831.137939f);
        co.localToParent.setelement(1, 3, -676.847534f);

        coro = prp.findObject("Dummy06", Typeid.plCoordinateInterface);
        co = coro.castTo();

        co.localToParent.setelement(0, 3, 831.137939f);
        co.localToParent.setelement(1, 3, -676.847534f);

        coro = prp.findObject("SafeNodeBBottom", Typeid.plCoordinateInterface);
        co = coro.castTo();

        co.localToParent.setelement(0, 3, 709.707214f);
        co.localToParent.setelement(1, 3, -736.592285f);

        coro = prp.findObject("SafeNodeBTop", Typeid.plCoordinateInterface);
        co = coro.castTo();

        co.localToParent.setelement(0, 3, 709.707214f);
        co.localToParent.setelement(1, 3, -736.592285f);
    }

    public static void PostMod_FixDescentFloor(prpfile prp)
    {
        // SHAFT FLOOR:
        // new region/collision in dustadd prp
        // however need to parent the two

        PrpRootObject coro = prp.findObject("NewFloorParent", Typeid.plCoordinateInterface);
        prpobjects.plCoordinateInterface co = coro.castTo();

        co.count++;
        Uruobjectref children[] = new Uruobjectref[co.count];

        int i=0;
        for (Uruobjectref child: co.children)
        {
            children[i] = co.children[i];
            i++;
        }

        children[co.count-1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "NewFloorProxy", Pageid.createFromPrefixPagenum(94, 90));
        co.children = children;

        // also replace detector region with something more accurate
        PrpRootObject siro = prp.findObject("FloorDtct", Typeid.plSimulationInterface);
        prpobjects.plSimulationInterface si = siro.castTo();
        si.physical = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plHKPhysical, "NewFloorRegion", Pageid.createFromPrefixPagenum(94, 90));


        // also, we need to disable this physical while under the floor, or it blocks the exit.
        PrpRootObject pyro = prp.findObject("cPythFloorLogic", Typeid.plPythonFileMod);

        prpobjects.plPythonFileMod py = pyro.castTo();

        py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(7, 40,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "DisableColliderRgn_enter", Pageid.createFromPrefixPagenum(94, 90))));
        py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(7, 40,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plLogicModifier, "DisableColliderRgn_exit", Pageid.createFromPrefixPagenum(94, 90))));
    }

    public static void PostMod_FixLakiCreatures(prpfile prp)
    {
        if (prp.header.agename.toString().equals("Laki") && prp.header.pagename.toString().equals("LakiCreatures"))
        {
            PrpRootObject pyro = prp.findObject("cPythCreatures", Typeid.plPythonFileMod);
            plPythonFileMod py = pyro.castTo();

            // fix responder index for the lakis animations
            py.listings.get(10).index = 21;
            py.listings.get(11).index = 22;

            py.listings.get(12).index = 23;
            py.listings.get(13).index = 24;
            py.listings.get(14).index = 25;

            // add the bird warper (because "handle" is too generic for PtFindSceneobject)
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(5, 26, Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "handle", Pageid.createFromPrefixPagenum(91, 17))));
        }
    }

    public static void PostMod_FixCameraTargetPoints(prpfile prp)
    {
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCameraModifier1))
        {
            // add transform, this should help it cut to its pos (generally good practice)

            prpobjects.x009BCameraModifier1 cmod = ro.castTo();
            if (cmod.count == 0)
            {
                cmod.count++;
                cmod.trans = new prpobjects.x009BCameraModifier1.CamTrans[1];
                cmod.trans[0] = prpobjects.x009BCameraModifier1.CamTrans.createDefault();
            }
        }

        // fix for animated cameras not following their target point.
        // This forces them to use a target point instead of a subject key.

        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCameraBrain1_Fixed))
        {
            prpobjects.plCameraBrain1_Fixed maincam = ro.castTo();

            if (maincam.targetPoint.hasRef == 0 && maincam.parent.subjectKey.hasRef == 1)
            {
                // need to create a new cameramodifier for target point...
                PrpRootObject tgpro = prp.findObjectWithRef(maincam.parent.subjectKey);
                prpobjects.plSceneObject tgp = tgpro.castTo();

                // actually create camera modifier
                prpobjects.x009BCameraModifier1 nCam = prpobjects.x009BCameraModifier1.createDefault();
                nCam.brain = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plCameraBrain1, tgpro.header.desc.objectname.toString(), prp.header.pageid);

                Uruobjectref nCamRef = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plCameraModifier1,
                        tgpro.header.desc.objectname.toString(), prp.header.pageid);
                PrpRootObject nCamRo = PrpRootObject.createFromDescAndObject(nCamRef.xdesc, nCam);
                prp.addObject(nCamRo);
                tgp.modifiers.add(nCamRef);

                // and create camera brain
                prpobjects.plCameraBrain1 nTgt = prpobjects.plCameraBrain1.createDefault();
                Uruobjectref nTgtRef = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plCameraBrain1,
                        tgpro.header.desc.objectname.toString(), prp.header.pageid);
                PrpRootObject nTgtRo = PrpRootObject.createFromDescAndObject(nTgtRef.xdesc, nTgt);
                prp.addObject(nTgtRo);

                // finally, delete subject
                maincam.parent.subjectKey = Uruobjectref.none();
                // and set new ref for targetpoint
                maincam.targetPoint = nCamRef;
            }
        }
    }

    public static void PostMod_MakeFlyOvers(prpfile prp)
    {
        // restore Cyan's unused camera flyovers on link in, which look great

        String pn = prp.header.pagename.toString();

        if (pn.equals("RestAge"))
        {
            // Direbo

            // create the python modifier
            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
            py.pyfile = Urustring.createFromString("xLinkInFlyover");
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString("DescentMystV")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(8, 3,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespLinkPanelCam", prp.header.pageid)));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(6, 4,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "linkPanelCamera", prp.header.pageid)));

            // add it to the prp
            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "LinkInFlyover", prp.header.pageid);
            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
            prp.addObject(pyroot);

            // modify the responder so the timer has a time of 0
            prpobjects.plResponderModifier resp = prp.findObjectWithRef(py.listings.get(1).xRef).castTo();
            PrpMessage.PlTimerCallbackMsg tmsg = resp.messages.get(0).commands.get(0).message.castTo();
            tmsg.u2 = new Flt(0f);

            // attach it to a sceneobject
            ((prpobjects.plSceneObject) prp.findObject("linkPanelCamResponderDummy", Typeid.plSceneObject).castTo()).modifiers.add(pyref);
        }

        else if (pn.equals("dsntTianaCaveTunnel1"))
        {
            // dsnt from thgr drbo

            // create the python modifier
            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
            py.pyfile = Urustring.createFromString("xLinkInFlyover");
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString("Direbo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("LinkInFromThgrDirebo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(8, 3,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespLinkPanelCam1", prp.header.pageid)));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(6, 4,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "linkPanelCamera1", prp.header.pageid)));

            // add it to the prp
            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "LinkInFlyover", prp.header.pageid);
            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
            prp.addObject(pyroot);

            // modify the responder so the timer has a time of 0
            prpobjects.plResponderModifier resp = prp.findObjectWithRef(py.listings.get(2).xRef).castTo();
            PrpMessage.PlTimerCallbackMsg tmsg = resp.messages.get(0).commands.get(0).message.castTo();
            tmsg.u2 = new Flt(0f);

            // attach it to a sceneobject
            ((prpobjects.plSceneObject) prp.findObject("linkPanelCamResponderDummy", Typeid.plSceneObject).castTo()).modifiers.add(pyref);
        }

        else if (pn.equals("dsntUpperShaft"))
        {
            // dsnt from tdlm drbo

            // create the python modifier
            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
            py.pyfile = Urustring.createFromString("xLinkInFlyover");
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString("Direbo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("LinkInFromTdlmDirebo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(8, 3,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespLinkPanelCam2", prp.header.pageid)));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(6, 4,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "linkPanelCamera2", prp.header.pageid)));

            // add it to the prp
            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "LinkInFlyover", prp.header.pageid);
            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
            prp.addObject(pyroot);

            // modify the responder so the timer has a time of 0
            prpobjects.plResponderModifier resp = prp.findObjectWithRef(py.listings.get(2).xRef).castTo();
            PrpMessage.PlTimerCallbackMsg tmsg = resp.messages.get(0).commands.get(0).message.castTo();
            tmsg.u2 = new Flt(0f);

            // attach it to a sceneobject
            ((prpobjects.plSceneObject) prp.findObject("linkPanelCamResponderDummy", Typeid.plSceneObject).castTo()).modifiers.add(pyref);
        }

        else if (pn.equals("dsntShaftGeneratorRoom"))
        {
            // dsnt from srln drbo

            // create the python modifier
            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
            py.pyfile = Urustring.createFromString("xLinkInFlyover");
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString("Direbo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("LinkInFromSrlnDirebo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(8, 3,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespLinkPanelCam3", prp.header.pageid)));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(6, 4,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "linkPanelCamera3", prp.header.pageid)));

            // add it to the prp
            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "LinkInFlyover", prp.header.pageid);
            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
            prp.addObject(pyroot);

            // modify the responder so the timer has a time of 0
            prpobjects.plResponderModifier resp = prp.findObjectWithRef(py.listings.get(2).xRef).castTo();
            PrpMessage.PlTimerCallbackMsg tmsg = resp.messages.get(0).commands.get(0).message.castTo();
            tmsg.u2 = new Flt(0f);

            // attach it to a sceneobject
            // do NOT attach it to the object that has the responder. Because of the dot in its name,
            // Python module loading fails with the error "NULL result without error in PyObject_Call"
            ((prpobjects.plSceneObject) prp.findObject("linkPanelCamera3", Typeid.plSceneObject).castTo()).modifiers.add(pyref);
        }

        else if (pn.equals("dsntPostShaftNodeAndTunnels"))
        {
            // dsnt from laki drbo

            // create the python modifier
            prpobjects.plPythonFileMod py = prpobjects.plPythonFileMod.createDefault();
            py.pyfile = Urustring.createFromString("xLinkInFlyover");
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString("Direbo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString("LinkInFromLakiDirebo")));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(8, 3,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "cRespLinkPanelCam4", prp.header.pageid)));
            py.addListing(plPythonFileMod.Pythonlisting.createWithRef(6, 4,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "linkPanelCamera4", prp.header.pageid)));

            // add it to the prp
            Uruobjectref pyref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "LinkInFlyover", prp.header.pageid);
            PrpRootObject pyroot = PrpRootObject.createFromDescAndObject(pyref.xdesc, py);
            prp.addObject(pyroot);

            // modify the responder so the timer has a time of 0
            prpobjects.plResponderModifier resp = prp.findObjectWithRef(py.listings.get(2).xRef).castTo();
            PrpMessage.PlTimerCallbackMsg tmsg = resp.messages.get(0).commands.get(0).message.castTo();
            tmsg.u2 = new Flt(0f);

            // attach it to a sceneobject
            ((prpobjects.plSceneObject) prp.findObject("linkPanelCamResponderDummy", Typeid.plSceneObject).castTo()).modifiers.add(pyref);
        }
    }

    public static void PostMod_FixPirahnaBird(prpfile prp)
    {
        if (prp.header.pagename.toString().equals("LakiCreatures"))
        {
            PrpRootObject pyro = prp.findObject("cPythCreatures", Typeid.plPythonFileMod);
            plPythonFileMod py = pyro.castTo();

            // add the sceneobject so we can warp the bird
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(6, 26,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "handle", Pageid.createFromPrefixPagenum(91, 90))));

            // add animation responders
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(8, 27,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "respPirBirdChomp", Pageid.createFromPrefixPagenum(91, 90))));
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(8, 28,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "respPirBirdIdle", Pageid.createFromPrefixPagenum(91, 90))));
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(8, 29,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "respPirBirdSwallow", Pageid.createFromPrefixPagenum(91, 90))));
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(8, 30,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "respPirBirdVocalize", Pageid.createFromPrefixPagenum(91, 90))));
            py.listings.add(plPythonFileMod.Pythonlisting.createWithRef(8, 31,
                    Uruobjectref.createDefaultWithTypeNamePage(Typeid.plResponderModifier, "respPirBirdWalk", Pageid.createFromPrefixPagenum(91, 90))));

            plCoordinateInterface sp = prp.findObject("PirBird1Warper", Typeid.plCoordinateInterface).castTo();
            sp.localToWorld.setelement(0, 3, 325.385559f);
            sp.localToWorld.setelement(1, 3, -207.186584f);
            sp.localToWorld.setelement(2, 3, -10.850807f);
            sp.localToParent.setelement(0, 3, 325.385559f);
            sp.localToParent.setelement(1, 3, -207.186584f);
            sp.localToParent.setelement(2, 3, -10.850807f);

//            sp = prp.findObject("PirBird2Warper", Typeid.plCoordinateInterface).castTo();
//            sp.localToWorld.setelement(2, 3, 0.040728f);
//            sp.localToParent.setelement(2, 3, 0.040728f);
//
//            sp = prp.findObject("PirBird4Warper", Typeid.plCoordinateInterface).castTo();
//            sp.localToWorld.setelement(2, 3, 6.065973f);
//            sp.localToParent.setelement(2, 3, 6.065973f);
        }

        else if (prp.header.pagename.toString().equals("PirBirdActor"))
        {
            PrpRootObject ro = prp.findObject("cAnmPrpPirBird", Typeid.plAGMasterMod);
            plAGMasterMod mm = ro.castTo();

            mm.count = 5;
            mm.ATCAnim = new Uruobjectref[5];
            mm.ATCAnim[0] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plATCAnim, "PirBirdChomp", Pageid.createFromPrefixPagenum(91, 5));
            mm.ATCAnim[1] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plATCAnim, "PirBirdIdle", Pageid.createFromPrefixPagenum(91, 4));
            mm.ATCAnim[2] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plATCAnim, "PirBirdSwallow", Pageid.createFromPrefixPagenum(91, 10));
            mm.ATCAnim[3] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plATCAnim, "PirBirdVocalize", Pageid.createFromPrefixPagenum(91, 6));
            mm.ATCAnim[4] = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plATCAnim, "PirBirdWalk", Pageid.createFromPrefixPagenum(91, 7));

            // required since the object now has a parent in dusttest.prp
            plCoordinateInterface co = prp.findObject("handle", Typeid.plCoordinateInterface).castTo();
            co.localToParent.isnotIdentity = 0;
            co.parentToLocal.isnotIdentity = 0;
        }
    }

    public static void PostMod_AdjustDraggableAnimations(prpfile prp)
    {
        // alright, so NOW we're talking about annoying stuff.
        // We must manually adjust animations (we could use values in the draggable modifiers, but I really don't care)
        // Problem: - we must make sure animations events still work, so might require adjusting them → ok, animevents work anyway
        //          - some anims require being played /backwards/, which will certainly mess up some things → indeed. Simply don't use, that's a hell of a mess to fix. Users will have to drag their cursor backwards.
        //          - some animations are triggered by Pythons and they must be updated accordingly (esp. for backward anims) → updated a few scripts, doesn't affect everything and only used for objects that might be seen doing so.
        //          - some animations are triggered by responder. Same problem. → don't care, too annoying to fix.

        // made a simple class, AnimScaler, that can handle the whole of it. Definitely easier.

        // updated scripts :
        // dsntTwoHandleDoor
        // dsntElevator
        // tdlmTram


        if ((prp.header.agename.toString().equals("Descent") || prp.header.agename.toString().equals("DescentMystV"))
                && prp.header.pagename.toString().equals("dsntTianaCaveNode2"))
        {
            PrpRootObject handlero = prp.findObject("handle01_open_anim_0", Typeid.plATCAnim);
            plATCAnim handle = handlero.castTo();

            AnimScaler modifier = new AnimScaler(handle, .1f);
            modifier.scale();
        }

        if ((prp.header.agename.toString().equals("Descent") || prp.header.agename.toString().equals("DescentMystV"))
                && prp.header.pagename.toString().equals("dsntTianaCaveTunnel3"))
        {
            PrpRootObject handlero = prp.findObject("handle02_open_anim_0", Typeid.plATCAnim);
            plATCAnim handle = handlero.castTo();

            AnimScaler modifier = new AnimScaler(handle, .1f);
            modifier.scale();
        }

        if ((prp.header.agename.toString().equals("Descent") || prp.header.agename.toString().equals("DescentMystV"))
                && prp.header.pagename.toString().equals("dsntUpperShaft"))
        {
            String[] names = {
                    "ElevA_PullHandle_pull_anim_0",
                    "ElevB_PullHandle_pull_anim_0",
                    "LeverElevABottom_pull_anim_0",
                    "LeverElevATop_pull_anim_0",
                    "LeverElevBBottom_pull_anim_0",
                    "LeverElevBTop_pull_anim_0",
                    };
            float[] values = {.33f, .33f, .33f, .33f, .33f, .33f};

            int i=0;
            for (String objname: names)
            {
                PrpRootObject ro = prp.findObject(objname, Typeid.plATCAnim);
                plATCAnim anim = ro.castTo();

                AnimScaler modifier = new AnimScaler(anim, values[i]);
                modifier.scale();
                i++;
            }
        }

        if ((prp.header.agename.toString().equals("Descent") || prp.header.agename.toString().equals("DescentMystV"))
                && prp.header.pagename.toString().equals("dsntShaftGeneratorRoom"))
        {
            String[] names = {
                    "GenCrank_crank_anim_0", // *.1 for this one ! wayyy too long to rotate it otherwise
                    };
            float[] values = {.1f};

            int i=0;
            for (String objname: names)
            {
                PrpRootObject ro = prp.findObject(objname, Typeid.plATCAnim);
                plATCAnim anim = ro.castTo();

                AnimScaler modifier = new AnimScaler(anim, values[i]);
                modifier.scale();
                i++;
            }
        }

        if ((prp.header.agename.toString().equals("Descent") || prp.header.agename.toString().equals("DescentMystV"))
                && prp.header.pagename.toString().equals("dsntPostShaftNodeAndTunnels"))
        {
            String[] names = {
                    "FanRoomCrank_Drag_fan_anim_0",
                    "handle01_open_anim_0",
                    };
            float[] values = {.1f, .1f};

            int i=0;
            for (String objname: names)
            {
                PrpRootObject ro = prp.findObject(objname, Typeid.plATCAnim);
                plATCAnim anim = ro.castTo();

                AnimScaler modifier = new AnimScaler(anim, values[i]);
                modifier.scale();
                i++;
            }
        }

        // Not modifying Noloben's animations
        // (I don't remember why, but there must be a good reason...)
        /*if (prp.header.agename.toString().equals("Siralehn")
                && prp.header.pagename.toString().equals("dsntPostShaftNodeAndTunnels"))
        {
            String[] names = {
                    "LadderPullMaster_LadderPull_anim_0",
                    "PillarTop01_rockspin_anim_0",
                    "PillarTop02_rockspin_anim_0",
                    "PillarTop03_rockspin_anim_0",
                    "PillarTop04_rockspin_anim_0",
                    };
            float[] values = {.66f, .66f, .66f, .66f, .66f};

            int i=0;
            for (String objname: names)
            {
                PrpRootObject ro = prp.findObject(objname, Typeid.plATCAnim);
                plATCAnim anim = ro.castTo();

                AnimScaler modifier = new AnimScaler(anim, values[i]);
                modifier.scale();
                i++;
            }
        } //*/

        if (prp.header.agename.toString().equals("Todelmer")
                && prp.header.pagename.toString().equals("Exterior"))
        {
            String[] names = {
                    "HandCrankDir1_forward_anim_0",
                    "HandCrankDir2_forward_anim_0",
                    "TramCrank01_down_anim_0",
                    "TramCrank02_down_anim_0",
                    };
            float[] values = {.33f, .33f, .1f, .1f};

            int i=0;
            for (String objname: names)
            {
                PrpRootObject ro = prp.findObject(objname, Typeid.plATCAnim);
                plATCAnim anim = ro.castTo();

                AnimScaler modifier = new AnimScaler(anim, values[i]);
                modifier.scale();
                i++;
            }
        }
    }

    public static void PostMod_FixLakiElev(prpfile prp)
    {
        plCoordinateInterface sp = prp.findObject("LinkInPed2", Typeid.plCoordinateInterface).castTo();
        sp.localToParent.setelement(2, 3, 3.9579f);
        sp.parentToLocal.setelement(2, 3, -3.9579f);
    }

    public static void PostMod_ReplaceAllDraggables(prpfile prp)
    {
        for (PrpRootObject ro : prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            boolean nukeAnimEvents = false;
            ArrayList<Uruobjectref> animEvents = new ArrayList<>();
            for (int i = 0; i < so.modifiers.size(); i++)
            {
                Uruobjectref modref = so.modifiers.get(i);
                if (modref.xdesc.objecttype == Typeid.plAnimEventModifier)
                    animEvents.add(modref);
                else if (modref.xdesc.objecttype == Typeid.plAxisAnimModifier)
                {
                    Draggable draggable = GetDraggable(prp, ro);
                    if (draggable == null)
                    {
                        m.err("Unknown draggable: " + ro.header.desc.objectname.toString() + ", " + prp.header.agename.toString() + "_" + prp.header.pagename.toString());
                        continue;
                    }
                    plPythonFileMod pfm = CreateDefaultAAMReplacementScript(prp, ro, modref);
                    if (draggable.reverse)
                        pfm.addListing(plPythonFileMod.Pythonlisting.createWithBoolean(8, true));
                    if (draggable.loop)
                        pfm.addListing(plPythonFileMod.Pythonlisting.createWithBoolean(9, true));
                    if (!draggable.autoReenable)
                        pfm.addListing(plPythonFileMod.Pythonlisting.createWithBoolean(10, false));
                    if (draggable.sdlName != null)
                    {
                        nukeAnimEvents = true;
                        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(5, Bstr.createFromString(draggable.sdlName)));
                    }
                    if (draggable.sdlValueToTime != null)
                        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(6, Bstr.createFromString(draggable.sdlValueToTime)));
                }
            }
            if (nukeAnimEvents)
            {
                // Anim events will trigger other scripts which will try to handle SDL variables on their own...
                // This is going to conflict with our own xEoADraggable script. So silence the anim event modifiers...
                for (Uruobjectref ref : animEvents)
                {
                    PrpRootObject animEventRo = prp.findObjectWithRef(ref);
                    plAnimEventModifier animEvent = animEventRo.castTo();
                    animEvent.numreceivers = 0;
                    animEvent.receivers = new Uruobjectref[0];
                    animEventRo.markAsChanged();
                }
            }
        }
    }

    private static Draggable GetDraggable(prpfile prp, PrpRootObject ro)
    {
        for (PrpDraggables draggables : prpDraggablesList)
            if (draggables.ageName.equals(prp.header.agename.toString()) &&
                    draggables.pageName.equals(prp.header.pagename.toString()))
                for (Draggable draggable : draggables.draggables)
                    if (draggable.draggableName.equals(ro.header.desc.objectname.toString()))
                        return draggable;
        return null;
    }

    private static plPythonFileMod CreateDefaultAAMReplacementScript(prpfile prp, PrpRootObject soRo, Uruobjectref aamRef)
    {
        // Remove the axis anim mod from the PRP - we won't use it once ingame.
        prp.markObjectDeleted(aamRef, true);
        plAxisAnimModifier aam = prp.findObjectWithRef(aamRef).castTo();

        // Create a new plPythonFileMod to handle clicking the object.
        plPythonFileMod pfm = plPythonFileMod.createDefault();
        pfm.pyfile = Urustring.createFromString("xEoADraggable");
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithRef(7, 2, aam.notificationKey));
        Uruobjectref animRef = (aam.xAnim.hasRef == 1) ? aam.xAnim : aam.yAnim;
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithString(7, Bstr.createFromString(aam.animLabel.toString())));
        pfm.addListing(plPythonFileMod.Pythonlisting.createWithRef(12, 7, animRef));
        Uruobjectdesc pfmDesc = Uruobjectdesc.createDefaultWithTypeNamePrp(Typeid.plPythonFileMod, aamRef.xdesc.objectname.toString() + "_axisanim", prp);
        Uruobjectref pfmRef = Uruobjectref.createFromUruobjectdesc(pfmDesc);
        prp.addObject(pfmRef, pfm);
        plSceneObject so = soRo.castTo();
        so.modifiers.add(pfmRef);

        // Reroute the plNotifyMsg towards our newly created Python file.
        plLogicModifier logicMod = prp.findObjectWithRef(aam.notificationKey).castTo();
        PrpMessage.PlNotifyMsg logicModMsg = logicMod.parent.message.castTo();
        logicModMsg.parent.receivers.set(0, pfmRef);

        return pfm;
    }

    /**
     * Replace an AxisAnimModifier with a single-use responder.
     * @param prp prp in which all dependant objects are located
     * @param soRo root object of the parent sceneobject
     * @param aamRef reference to the AxisAnimModifier
     */
    private static void MakeDraggableOneShot(prpfile prp, PrpRootObject soRo, Uruobjectref aamRef)
    {
        // Remove the axis anim mod from the PRP - we won't use it once ingame.
        prp.markObjectDeleted(aamRef, true);
        plAxisAnimModifier aam = prp.findObjectWithRef(aamRef).castTo();

        // Create a plResponderModifier to handle anim playback on click.
        plResponderModifier responder = plResponderModifier.createDefault();
        Uruobjectdesc responderDesc = Uruobjectdesc.createDefaultWithTypeNamePrp(Typeid.plResponderModifier, aamRef.xdesc.objectname.toString() + "_axisanim", prp);
        Uruobjectref responderRef = Uruobjectref.createFromUruobjectdesc(responderDesc);
        plResponderModifier.PlResponderState state0 = plResponderModifier.PlResponderState.createDefault();
        responder.messages.add(state0);
        PrpMessage.PlAnimCmdMsg animMsg = PrpMessage.PlAnimCmdMsg.createDefault();
        animMsg.animName = Urustring.createFromString(aam.animLabel.toString());
        animMsg.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kContinue);
        animMsg.parent.parent.sender = responderRef;
        animMsg.parent.parent.receivers.add((aam.xAnim.hasRef == 1) ? aam.xAnim : aam.yAnim);
        PrpTaggedObject msgTaggedObject = PrpTaggedObject.createWithTypeidUruobj(Typeid.plAnimCmdMsg, animMsg);
        plResponderModifier.PlResponderCmd cmd0 = plResponderModifier.PlResponderCmd.createDefaultFromMessage(msgTaggedObject);
        state0.commands.add(cmd0);
        prp.addObject(responderRef, responder);
        aamRef.xdesc = responderDesc;

        // Reroute the plNotifyMsg towards our newly created responder.
        plLogicModifier logicMod = prp.findObjectWithRef(aam.notificationKey).castTo();
        PrpMessage.PlNotifyMsg logicModMsg = logicMod.parent.message.castTo();
        logicModMsg.parent.receivers.set(0, responderRef);
    }

    /**
     * Replace an AxisAnimModifier with a two-state responder that ping-pongs anytime it's triggered.
     * @param prp prp in which all dependant objects are located
     * @param soRo root object of the parent sceneobject
     * @param aamRef reference to the AxisAnimModifier
     */
    private static void MakeDraggablePingPong(prpfile prp, PrpRootObject soRo, Uruobjectref aamRef)
    {
        // Works reasonably well, but:
        // - synced draggables (ex: door handles) may trigger the anim remotely. This screws up
        //   tracking of which responder state is active
        // - some objects like the GSLR generator disabling itself after being enabled.
        // Might need to replace it all with a Python script...

        // Remove the axis anim mod from the PRP - we won't use it once ingame.
        prp.markObjectDeleted(aamRef, true);
        plAxisAnimModifier aam = prp.findObjectWithRef(aamRef).castTo();

        // Create a plResponderModifier to handle anim playback on click.
        plResponderModifier responder = plResponderModifier.createDefault();
        Uruobjectdesc responderDesc = Uruobjectdesc.createDefaultWithTypeNamePrp(Typeid.plResponderModifier, aamRef.xdesc.objectname.toString() + "_axisanim", prp);
        Uruobjectref responderRef = Uruobjectref.createFromUruobjectdesc(responderDesc);

        plResponderModifier.PlResponderState state0 = plResponderModifier.PlResponderState.createDefault();
        state0.switchToState = 1;
        responder.messages.add(state0);
        PrpMessage.PlAnimCmdMsg animMsg = PrpMessage.PlAnimCmdMsg.createDefault();
        animMsg.animName = Urustring.createFromString(aam.animLabel.toString());
        animMsg.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kSetForwards | PrpMessage.PlAnimCmdMsg.kContinue);
        animMsg.parent.parent.sender = responderRef;
        animMsg.parent.parent.receivers.add((aam.xAnim.hasRef == 1) ? aam.xAnim : aam.yAnim);
        PrpTaggedObject msgTaggedObject = PrpTaggedObject.createWithTypeidUruobj(Typeid.plAnimCmdMsg, animMsg);
        plResponderModifier.PlResponderCmd cmd0 = plResponderModifier.PlResponderCmd.createDefaultFromMessage(msgTaggedObject);
        state0.commands.add(cmd0);

        plResponderModifier.PlResponderState state1 = plResponderModifier.PlResponderState.createDefault();
        responder.messages.add(state1);
        animMsg = PrpMessage.PlAnimCmdMsg.createDefault();
        animMsg.animName = Urustring.createFromString(aam.animLabel.toString());
        animMsg.command = new HsBitVector(PrpMessage.PlAnimCmdMsg.kSetBackwards | PrpMessage.PlAnimCmdMsg.kContinue);
        animMsg.parent.parent.sender = responderRef;
        animMsg.parent.parent.receivers.add((aam.xAnim.hasRef == 1) ? aam.xAnim : aam.yAnim);
        msgTaggedObject = PrpTaggedObject.createWithTypeidUruobj(Typeid.plAnimCmdMsg, animMsg);
        plResponderModifier.PlResponderCmd cmd1 = plResponderModifier.PlResponderCmd.createDefaultFromMessage(msgTaggedObject);
        state1.commands.add(cmd1);

        prp.addObject(responderRef, responder);
        aamRef.xdesc = responderDesc;

        // Reroute the plNotifyMsg towards our newly created responder.
        plLogicModifier logicMod = prp.findObjectWithRef(aam.notificationKey).castTo();
        PrpMessage.PlNotifyMsg logicModMsg = logicMod.parent.message.castTo();
        logicModMsg.parent.receivers.set(0, responderRef);
    }
}

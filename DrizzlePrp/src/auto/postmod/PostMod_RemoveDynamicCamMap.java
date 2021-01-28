/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import prpobjects.*;
import shared.*;
import java.util.Vector;

public class PostMod_RemoveDynamicCamMap
{
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
    public static void PostMod_RemoveDynamicCampMap(prpfile prp)
    {
        PrpRootObject[] layers = prp.FindAllObjectsOfType(Typeid.plLayer);
        for(PrpRootObject layer2: layers)
        {
            prpobjects.x0006Layer layer = layer2.castTo();
            if(layer.texture.hasref() && layer.texture.xdesc.objecttype==Typeid.plDynamicCamMap)
            {
                //found it!
                m.msg("Blackifying DynamicCamMap in PlLayer.");
                //layer.flags1 |= 0x80; //kBlendNoColor //blend //makes it invisible?
                //float r = (float)(15.0/255.0); //15
                //float g = (float)(20.0/255.0); //20
                //float b = (float)(26.0/255.0); //26
                Flt r = Flt.createFromJavaFloat((float)(0.0/255.0)); //15
                Flt g = Flt.createFromJavaFloat((float)(0.0/255.0)); //20
                Flt b = Flt.createFromJavaFloat((float)(0.0/255.0)); //26
                //float a = (float)1.0;
                //layer.ambient = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
                //layer.diffuse = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
                //layer.emissive = new Rgba(Flt.createFromJavaFloat(r),Flt.createFromJavaFloat(g),Flt.createFromJavaFloat(b),Flt.createFromJavaFloat(a));
                layer.ambient.r = r;
                layer.ambient.g = g;
                layer.ambient.b = b;
                layer.diffuse.r = r;
                layer.diffuse.g = g;
                layer.diffuse.b = b;
                layer.emissive.r = r;
                layer.emissive.g = g;
                layer.emissive.b = b;
                layer.specular.r = r;
                layer.specular.g = g;
                layer.specular.b = b;
            }
        }
    }
}

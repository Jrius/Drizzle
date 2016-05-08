/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.prpfile;
import shared.m;
import prpobjects.Pageid;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;
import java.util.Vector;
import prpobjects.Uruobjectdesc;
import prpobjects.Uruobjectref;
import prpobjects.Pagetype;
import shared.Bytes;
import java.util.IdentityHashMap;
import shared.IdentityVector;
import prpobjects.x0000Scenenode;
import prpobjects.Urustring;

public class fixCraters
{
    public static void fixCraters(prpfile prp, String agename, String pagename, String outfolder, Pageid pid)
    {
        m.msg("Creating alternate page for Minkata craters.");
        //Pageid pid = Pageid.createFromPrefixPagenum(42, 11);

        PrpRootObject sn = auto.hackFactory.createSceneNode(agename+"_"+pagename, pid);
        PrpRootObject so = auto.hackFactory.createSceneObject(sn, pagename+"_so", pid);
        sn.castToSceneNode().addToObjectrefs1(so.header.desc.toRef());

        PrpRootObject[] clustergroups = prp.FindAllObjectsOfType(Typeid.plClusterGroup);
        Vector<PrpRootObject> movedobjects = new Vector<PrpRootObject>();
        for(PrpRootObject clustergroup: clustergroups)
        {
            //remove visregions.
            prpobjects.plClusterGroup plcg = clustergroup.castTo();
            plcg.count2 = 0;
            plcg.fRegions = new Uruobjectref[0];
            clustergroup.header.desc.objectname = Urustring.createFromString(clustergroup.header.desc.objectname.toString()+"_"+pagename);
            PrpRootObject movedCG = movePrpRootObject(clustergroup, movedobjects, pid, sn, prp, false);
            clustergroup.tagDeleted = true; //just delete the clustergroup, not its dependencies.
            
            //add clustergroup to scenenode.
            sn.castTo(x0000Scenenode.class).addToObjectrefs2(movedCG.header.desc.toRef());
            
            /*clustergroup.tagDeleted = true; //remove from this prp
            Uruobjectdesc desc = Uruobjectdesc.createDefaultWithTypeNamePage(Typeid.plClusterGroup, clustergroup.header.desc.objectname.toString(), pid);
            //PrpRootObject newclustergroup = PrpRootObject.createFromDescAndObject(desc, clustergroup); //create a new PrpRootObject for the other prp
            PrpRootObject newclustergroup = PrpRootObject.createFromDescAndObject(desc, clustergroup.getObject()); //create a new PrpRootObject for the other prp
            //newclustergroup.header.desc.pageid.setPagenum(11); //put on page 11

            //uru.moulprp.PlClusterGroup cgroup = newclustergroup.castTo();
            Vector<Uruobjectref> refs = shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectref.class, newclustergroup);
            for(Uruobjectref ref: refs)
            {
                if(ref.hasref() && ref.xdesc.objecttype==Typeid.plSceneNode)
                {
                    ref.xdesc = sn.header.desc;
                }
                    //desc.pageid = pid; //they ref the correct page already.
            }
            //Vector<Pageid> pageids = shared.FindAllDescendants.FindAllDescendantsByClass(Pageid.class, newclustergroup);

            newclustergroups.add(newclustergroup);*/
        }

        movedobjects.add(so);
        movedobjects.add(sn);
        
        //works, but we don't need it.
        //hackFactory.create3SpawnpointObjects(sn, "DustLinkInPoint", pid, movedobjects);

        prpfile newprp = prpfile.createFromObjectsAndInfo(movedobjects, agename, pagename, pid, Pagetype.createDefault());
        //sn.castToSceneNode().regenerateAllSceneobjectsFromPrpRootObjects(uru.generics.convertArrayToVector(newprp.objects));

        //Bytes bytestowrite = newprp.saveAsBytes();
        //bytestowrite.saveAsFile(outfolder+"/dat/"+agename+"_District_"+pagename+".prp");
        newprp.saveAsFile(outfolder+"/dat/"+agename+"_District_"+pagename+".prp");
        
    }
    
    //returns the modified PrpRootObject for convenience.
    public static PrpRootObject movePrpRootObject(PrpRootObject objToMove, Vector<PrpRootObject> movedObjects, Pageid pid, PrpRootObject scenenode, prpfile prp, boolean deleteOriginal)
    {
        if(deleteOriginal) objToMove.tagDeleted = true; //remove from this prp

        //create duplicate, but fixed PrpRootObject.
        Uruobjectdesc desc = Uruobjectdesc.createDefaultWithTypeNamePage(objToMove.header.desc.objecttype, objToMove.header.desc.objectname.toString(), pid);
        //PrpRootObject newclustergroup = PrpRootObject.createFromDescAndObject(desc, clustergroup); //create a new PrpRootObject for the other prp
        PrpRootObject newclustergroup = PrpRootObject.createFromDescAndObject(desc, objToMove.getObject()); //create a new PrpRootObject for the other prp
        //newclustergroup.header.desc.pageid.setPagenum(11); //put on page 11
        m.msg("moving object from ",objToMove.header.desc.toString()+" to "+newclustergroup.header.desc.toString());

        movedObjects.add(newclustergroup); //its now been moved, but not all its dependencies.

        //check out the references for any dependencies.
        Vector<Uruobjectref> refs = shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectref.class, newclustergroup);
        /*Vector<Uruobjectdesc> descs = shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, newclustergroup);
        Vector<Pageid> pageids = shared.FindAllDescendants.FindAllDescendantsByClass(Pageid.class, newclustergroup);
        if(descs.size()!=pageids.size())
        {
            int dummy=0;
        }*/
        for(Uruobjectref ref: refs)
        {
            if(!ref.hasref()) continue;
            
            //if(ref.xdesc.pageid.suffix!=34)
            //{
            //    int dummy=0;
            //}
            
            if(ref.xdesc.objecttype==Typeid.plSceneNode) //change to this page's scenenode.
            {
                //use this page's sceneobject instead.
                m.msg("modifying scenenode from ",ref.xdesc.toString()+" to "+scenenode.header.desc.toString());
                ref.xdesc = scenenode.header.desc;
            }
            else if(objectsContainDesc(movedObjects, ref.xdesc))
            {
                //we've already brought this object over. Just set the pageid.
                m.msg("already moved obj ",ref.xdesc.toString()+" new pageid is "+pid.toString());
                ref.xdesc.pageid = pid;
            }
            else
            {
                //we don't already have this object.
                if(ref.xdesc.objecttype==Typeid.plCubicEnvironMap || ref.xdesc.objecttype==Typeid.plMipMap)
                {
                    //these objects should stay where they are.
                    m.msg("not moving texture ",ref.xdesc.toString());
                }
                else
                {
                    //we need to move these to our page.
                    
                    PrpRootObject dependency = prp.findObjectWithRef(ref);
                    movePrpRootObject(dependency,movedObjects,pid,scenenode,prp, deleteOriginal);
                    ref.xdesc.pageid = pid;
                }
            }
            //else if( movedObjectsref.xdesc.equals(objToMove.header.desc)) //change page for self-refs.
                //desc.pageid = pid; //they ref the correct page already.
        }
        //Vector<Pageid> pageids = shared.FindAllDescendants.FindAllDescendantsByClass(Pageid.class, newclustergroup);

        return newclustergroup; //just for convenience.
    }
    
    public static boolean objectsContainDesc(Vector<PrpRootObject> objs, Uruobjectdesc desc)
    {
        for(PrpRootObject obj: objs)
        {
            if(obj.header.desc.objectname.equals(desc.objectname))
                if(obj.header.desc.objecttype.equals(desc.objecttype))
                    return true;
        }
        return false;
    }
}

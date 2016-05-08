/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.prpfile;
import java.util.HashMap;
import prpobjects.Uruobjectdesc;
import prpdistiller.distiller;
import java.io.File;
import java.util.Vector;
import prpobjects.Typeid;
import prpobjects.PrpRootObject;
import shared.m;

public class Distiller
{
    public static void DistillTextures(String destfile, String texturefile, String outfolder)
    {
        //distill
        prpfile destprp = prpfile.createFromFile(destfile, false); //don't read raw, because we want to reocmpile all objects I think.
        prpfile textureprp = prpfile.createFromFile(texturefile, true);
        HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
        distiller.distillTextures(destprp, textureprp, new String[]{}, refReassigns);

        //compile and save
        File destfile2 = new File(destfile);
        String outfile = outfolder+"/"+destfile2.getName();
        destprp.saveAsFile(outfile);

    }

    public static void SimpleDistill(String srcfile, String texturefile, String outfolder, String objectnames)
    {
        prpdistiller.distiller.distillInfo info = new prpdistiller.distiller.distillInfo();
        
        prpfile src = prpfile.createFromFile(srcfile, true);
        prpfile text = prpfile.createFromFile(texturefile, true);
        
        prpfile dest = prpfile.create(src.header.agename.toString(), src.header.pagename.toString(), src.header.pageid, src.header.pagetype);
        dest.addScenenode();
        //prpfile dest = prpfile.create(agename, fourLetterAgename+"DustAdditions"+outpagesuffix, Pageid.createFromPrefixPagenum(sequencePrefix, additionsPagenum), Pagetype.createWithType(0));
        //dest.addScenenode();

        Vector<prpfile> sources = new Vector();
        //prpfile prp;
        sources.add(src);
        sources.add(text);
        //sources.add(prp = prpfile.createFromFile(c.infolder+"/dat/"+agename+"_District_"+sourcePagename+".prp", true));
        //sources.add(prpfile.createFromFile(c.infolder+"/dat/"+agename+"_District_Textures.prp", true));

        Vector<prpfile> altdests = new Vector();
        //altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/"+agename+"_District_"+altDestPagename+".prp", true));
        //altdests.add(prpfile.createFromFile(c.cleanpotsfolder+"/dat/"+agename+"_District_Textures.prp", true));
        info.altdests = altdests;

        Vector<Uruobjectdesc> list = new Vector();
        //String starnum = ((calendarStarNum<10)?"0":"")+Integer.toString(calendarStarNum);
        //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Get"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
        //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Vis_1"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
        //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStar"+starnum+"Vis_3"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
        //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythCalStarSNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
        //for(String sobj: prp.findAllSceneobjectsThatReferencePythonfilemod("cPythSparky-SNDCtrl"))list.add(prp.findObject(sobj,Typeid.plSceneObject).header.desc);
        //if(agename.equals("Myst")) list.add(prp.findObject("floor_closed_door", Typeid.plCoordinateInterface).header.desc);
        for(String objectname: objectnames.split(","))
        {
            PrpRootObject ro = src.findObject(objectname, Typeid.plSceneObject);
            if(ro==null)
            {
                m.err("SceneObject not found: ",objectname);
                return;
            }
            else
            {
                list.add(ro.header.desc);
            }
        }

        info.list = list;
        //info.createObjectList = true;
        //info.outputFileForObjectList = c.outfolder+"/dat/"+c.filename+".profile";

        info.dest = dest;
        info.sourceprpfiles = sources;
        info.trimDrawableSpans = true;
        info.runtests = false;
        //forcedDuplicateInclusions returning true for something will *force* it to be included, even if it is already in the dest prps.
        info.forcedDuplicateInclusions = new prpdistiller.distiller.includeDuplicateDecider() {
            public boolean include(Uruobjectdesc desc)
            {
                Typeid tid = desc.objecttype;
                String name = desc.objectname.toString();
                //if(tid==Typeid.hsGMaterial || tid==Typeid.plLayer || tid==Typeid.plLayerAnimation) return true;
                //if(tid==Typeid.plViewFaceModifier) return true;
                return false;
            }
        };
        prpdistiller.distiller.distillList(info);


        //save the dest file.
        
        dest.saveAsFile(outfolder+"/"+(new File(srcfile)).getName());
        
    }

}

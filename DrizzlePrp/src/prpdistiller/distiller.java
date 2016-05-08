/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpdistiller;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import prpobjects.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.HashSet;
import shared.m;
import java.io.InputStream;

public class distiller
{
    //Moves part of a drawablespan to another drawablespan and changes the refs.
    //Not finished...
    public static void distillDrawableSpans(prpfile prp, Vector<prpobjects.plDrawInterface> drawInterfacesToDistill, String targetDrawableSpansName)
    {
        if(true) throw new shared.uncaughtexception("not finished.");
        //find or create the destination drawablespans.
        /*PrpRootObject destobj = prp.findObject(targetDrawableSpansName, Typeid.plDrawableSpans);
        if(destobj==null)
        {
            PlDrawableSpans destspan = new uru.moulprp.PlDrawableSpans();
            destobj = prp.addObject(Typeid.plDrawableSpans, targetDrawableSpansName, destspan);
        }
        else
        {
            throw new shared.uncaughtexception("Unhandled case.");
        }
        PlDrawableSpans dest = destobj.castTo();*/
        plDrawableSpans.modPlDrawableSpans dest = new plDrawableSpans.modPlDrawableSpans();

        //collect items for distillation:
        for(plDrawInterface di: drawInterfacesToDistill)
        {
            for(plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
            {
                if(sgr.subsetgroupindex!=-1)
                {
                    prpobjects.plDrawableSpans spans = prp.findObject(sgr.span.xdesc.objectname.toString(), sgr.span.xdesc.objecttype).castTo();
                    dest.addFromSubsetgroupindex(spans, sgr.subsetgroupindex);
                }
            }
        }
        
        //move items to new drawablespans:

        //trim the old one.

    }
    public static int distillEverything(prpfile dest, Vector<prpfile> prpfiles, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        int numlevels = 0;
        while(true)
        {
            int numSaw = distillEverythingOneLayerDeep(dest,prpfiles,refReassigns);
            if(numSaw==0)
            {
                return numlevels;
            }
            else
            {
                numlevels++;
            }
        }
    }
    //not functional yet:
    public static interface includeDuplicateDecider
    {
        abstract public boolean include(Uruobjectdesc desc);
    }
    public static class distillInfo
    {
        public prpfile dest;
        public Vector<Uruobjectdesc> list;
        public Vector<prpfile> sourceprpfiles;
        public Vector<prpfile> altdests = new Vector();
        public HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
        public boolean trimDrawableSpans = true;
        public boolean runtests = false;
        
        public boolean createObjectList = false;
        public String outputFileForObjectList; //only if createObjectList
        
        public boolean usePreexistingObjectList = false;
        public String objectListResourceName; //only if usePreexistingObjectList
        public includeDuplicateDecider forcedDuplicateInclusions = new distiller.includeDuplicateDecider() { public boolean include(Uruobjectdesc desc) { return false; } }; //only if !usePreexistingObjectList.

        public UpdateRefsCallback updateRefsCallback;

        public void addRefReassignment(Uruobjectdesc d1, Uruobjectdesc d2)
        {
            if(d1!=null && d2!=null)
            {
                if(!d1.equals(d2))
                {
                    refReassigns.put(d1, d2);
                }
            }
        }
    }
    public static interface UpdateRefsCallback
    {
        void callback(distillInfo info);
    }
    /*static class serializableState implements java.io.Serializable
    {
        HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns;
        HashSet<Uruobjectdesc> everythingNeeded2;
    }*/
    public static void distillList(distillInfo d)
    {
        prpfile dest = d.dest;
        Vector<prpfile> altdests = d.altdests;
        Vector<prpfile> sourceprpfiles = d.sourceprpfiles;
        //HashMap<Uruobjectdesc,Uruobjectdesc> refReassigns = d.refReassigns;
        /*int numlevels = 0;
        while(true)
        {
            int numSaw = distillListOneLayerDeep(dest,altdests,sourceprpfiles,refReassigns,list);
            if(numSaw==0)
            {
                return numlevels;
            }
            else
            {
                numlevels++;
            }
        }*/
        HashSet<Uruobjectdesc> everythingNeeded2 = new HashSet();;
        if(!d.usePreexistingObjectList)
        {
            //find all the objects we want to move:
            HashSet<Uruobjectdesc> everythingNeeded = new HashSet();
            for(Uruobjectdesc desc: d.list)
            {
                PrpRootObject obj = findObject(d.sourceprpfiles, desc);
                findDistillList(d.sourceprpfiles, everythingNeeded, obj, null);
            }
            /*for(Uruobjectdesc desc: everythingNeeded)
            {
                m.msg(desc.toString());
            }*/

            //reduce them to those not already in the dest or altdests
            for(Uruobjectdesc desc: everythingNeeded)
            {
                if(desc.objectname.toString().equals("19"))
                {
                    int dummy=0;
                }
                PrpRootObject destdesc = findObject(d.dest,d.altdests,desc.objectname.toString(),desc.objecttype);
                //do visRegions anyway, because it may crash otherwise.
                //do materials and layers, because they can be different and they are very small objects.
                if(destdesc==null || d.forcedDuplicateInclusions.include(desc) || destdesc.header.objecttype==Typeid.plVisRegion)
                {
                    everythingNeeded2.add(desc);
                }
                else
                {
                    //we've got it, enter the refReassignment.
                    //d.refReassigns.put(desc, destdesc.header.desc);
                    d.addRefReassignment(desc, destdesc.header.desc);
                    m.msg("Skipping object, because one with the same name and typeid is in a pots prp already: ", desc.toString());
                }
            }
        }
        else
        {
            /*//read the xml file containing refReassigns and everythingNeeded2
            InputStream in = shared.GetResource.getResourceAsStream(d.objectListResourceName);
            shared.xml.xmlreader xml = new shared.xml.xmlreader(in);
            for(Node child=xml.root.getFirstChild();child!=null;child=child.getNextSibling())
            {
                if(child.getNodeType()==Node.ELEMENT_NODE)
                {
                    Element e = (Element)child;
                    String tag = e.getTagName();
                    if(tag.equals("everythingneeded2"))
                    {
                        for(Node c2=e.getFirstChild();c2!=null;c2=c2.getNextSibling())
                        {
                            Element e2 = (Element)c2;
                            String tag2 = e2.getTagName();
                            Uruobjectdesc desc = Uruobjectdesc.createFromXml(e2);
                            everythingNeeded2.add(desc);
                        }
                    }
                    else if(tag.equals("refreassigns"))
                    {
                        for(Node c2=e.getFirstChild();c2!=null;c2=c2.getNextSibling())
                        {
                            Element e2 = (Element)c2;
                            String tag2 = e2.getTagName();
                            if(tag2.equals("map"))
                            {
                                Uruobjectdesc key=null; Uruobjectdesc val=null;
                                for(Node c3=e2.getFirstChild();c3!=null;c3=c3.getNextSibling())
                                {
                                    Element e3 = (Element)c3;
                                    String tag3 = e3.getTagName();
                                    if(tag3.equals("key")) key = Uruobjectdesc.createFromXml(e3);
                                    else if(tag3.equals("val")) val = Uruobjectdesc.createFromXml(e3);
                                }
                                refReassigns.put(key, val);
                            }
                        }
                    }
                }
            }*/
            try{
                java.io.InputStream in = shared.GetResource.getResourceAsStream(d.objectListResourceName);
                java.io.ObjectInputStream oin = new java.io.ObjectInputStream(in);

                everythingNeeded2 = (HashSet<Uruobjectdesc>)oin.readObject();
                d.refReassigns = (HashMap<Uruobjectdesc,Uruobjectdesc>)oin.readObject();
                oin.close();

            }catch(Exception e)
            {
                m.err("Unable to load object in distiller. Reason:");
                m.err(e.getMessage());
            }
        }

        if(d.createObjectList)
        {
            /*//save the refReassigns and everythingNeeded2 to a file for future use without the pots files.
            StringBuilder s = new StringBuilder();
            s.append("<conversionprofile>");
            s.append("<everythingneeded2>");
            for(Uruobjectdesc desc: everythingNeeded2)
            {
                desc.addXml(s);
            }
            s.append("</everythingneeded2>");
            s.append("<refreassigns>");
            for(Uruobjectdesc key: d.refReassigns.keySet())
            {
                s.append("<map>");
                Uruobjectdesc val = d.refReassigns.get(key);
                s.append("<key>");
                key.addXml(s);
                s.append("</key>");
                s.append("<val>");
                val.addXml(s);
                s.append("</val>");
                s.append("</map>");
            }
            s.append("</refreassigns>");
            s.append("</conversionprofile>");
            byte[] data = shared.b.StringToBytes(s.toString());
            shared.FileUtils.WriteFile(d.outputFileForObjectList, data);*/
            try{
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                java.io.ObjectOutputStream oout = new java.io.ObjectOutputStream(out);
                oout.writeObject(everythingNeeded2);
                oout.writeObject(d.refReassigns);
                oout.close();
                shared.FileUtils.WriteFile(d.outputFileForObjectList, out.toByteArray());
            }catch(Exception e)
            {
                m.err("Unable to save object in distiller.");
            }

        }

        //move all the objects, setting the d.refReassigns at the same time.
        for(Uruobjectdesc desc: everythingNeeded2)
        {
            PrpRootObject obj = findObject(d.sourceprpfiles, desc);
            if(obj==null)
            {
                int dummy=0;
            }
            copyObjectAndModifyRef(obj, d.dest, /*d.refReassigns,*/d);
        }

        //add the scenenodes to refReassigns
        PrpRootObject[] SNsrc = d.dest.FindAllObjectsOfType(Typeid.plSceneNode);
        Uruobjectdesc destsndesc = SNsrc[0].header.desc;
        for(prpfile prp: d.sourceprpfiles)
        {
            PrpRootObject[] SNdst = prp.FindAllObjectsOfType(Typeid.plSceneNode);
            if(SNdst.length>0)
            {
                Uruobjectdesc srcsndesc = SNdst[0].header.desc;
                //d.refReassigns.put(srcsndesc, destsndesc);
                d.addRefReassignment(srcsndesc, destsndesc);
            }
        }

        //change all the refs in the dest:
        if(d.updateRefsCallback!=null)d.updateRefsCallback.callback(d);
        updateAllReferences(dest, /*d.refReassigns,*/d);

        //trim down the dest PlDrawableSpans by removing unused meshes, maybe?
        if(!d.trimDrawableSpans)
        {
            m.msg("Not trimming PlDrawableSpans.");
        }
        else
        {
            m.msg("Trimming PlDrawableSpans.");
            HashSet<prpobjects.plDrawableSpans.PlGBufferGroup> usedgroups = new HashSet();
            HashSet<prpobjects.plDrawableSpans.PlIcicle> usedicicles = new HashSet();
            HashSet<Uruobjectref> usedmaterials = new HashSet();
            HashSet<prpobjects.plDrawableSpans.PlDISpanIndex> usedspanindexs = new HashSet();

            /*HashMap<Integer, Integer> groupsRenumbering = new HashMap();
            HashMap<Integer, Integer> groupsRenumberingRev = new HashMap();
            HashMap<Integer, Integer> iciclesRenumbering = new HashMap();
            HashMap<Integer, Integer> RevIciclesRenumbering = new HashMap();
            HashMap<Integer, Integer> materialsRenumbering = new HashMap();
            HashMap<Integer, Integer> materialsRenumberingRev = new HashMap();
            HashMap<Integer, Integer> spanindexRenumbering = new HashMap();
            HashMap<Integer, Integer> spanindexRenumberingRev = new HashMap();*/

            //find the used parts of the DrawableSpan:
            for(PrpRootObject diro: dest.FindAllObjectsOfType(Typeid.plDrawInterface))
            {
                prpobjects.plDrawInterface di = diro.castTo();
                for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
                {
                    if(sgr.subsetgroupindex!=-1)
                    {
                        prpobjects.plDrawableSpans spans = prpdistiller.distiller.findObject(sourceprpfiles, sgr.span.xdesc).castTo();
                        prpobjects.plDrawableSpans.PlDISpanIndex spanindex = spans.DIIndices[sgr.subsetgroupindex];
                        usedspanindexs.add(spanindex);
                        for(int subsetind: spanindex.indices)
                        {
                            prpobjects.plDrawableSpans.PlIcicle icicle = spans.icicles[subsetind];
                            Uruobjectref matrefb = spans.materials.get(icicle.parent.parent.materialindex);
                            prpobjects.plDrawableSpans.PlGBufferGroup bg = spans.groups[icicle.parent.groupIdx];
                            usedmaterials.add(matrefb);
                            usedicicles.add(icicle);
                            usedgroups.add(bg);
                        }
                    }
                }
            }

            boolean printflags = false;
            if(printflags)
            {
                for(prpobjects.plDrawableSpans.PlIcicle icicle: usedicicles)
                {
                    m.msg("Props: 0x"+Integer.toHexString(icicle.parent.parent.props));
                    for(Uruobjectref ref: icicle.parent.parent.permaProjs)
                    {
                        m.msg("Permaproj ref: "+ref.toString());
                    }
                }
            }

            //null entries and make array remappings.
            for(PrpRootObject spanobj: dest.FindAllObjectsOfType(Typeid.plDrawableSpans))
            {
                prpobjects.plDrawableSpans spans = spanobj.castTo();

                HashMap<Integer, Integer> groupsRenumbering = new HashMap();
                HashMap<Integer, Integer> groupsRenumberingRev = new HashMap();
                HashMap<Integer, Integer> iciclesRenumbering = new HashMap();
                HashMap<Integer, Integer> RevIciclesRenumbering = new HashMap();
                HashMap<Integer, Integer> materialsRenumbering = new HashMap();
                HashMap<Integer, Integer> materialsRenumberingRev = new HashMap();
                HashMap<Integer, Integer> spanindexRenumbering = new HashMap();
                HashMap<Integer, Integer> spanindexRenumberingRev = new HashMap();

                //trim materials
                int cur=0;
                for(int i=0;i<spans.materials.size();i++)
                {
                    if(!usedmaterials.contains(spans.materials.get(i)))
                    {
                        //spans.materials.set(i, Uruobjectref.none());
                        spans.materials.set(i, null);
                    }
                    else
                    {
                        materialsRenumbering.put(cur, i);
                        materialsRenumberingRev.put(i, cur);
                        cur++;
                    }
                }
                Vector<Uruobjectref> newmaterials = new Vector();
                for(int i=0;i<cur;i++)
                {
                    Uruobjectref mat = spans.materials.get(materialsRenumbering.get(i));
                    newmaterials.add(mat);
                }
                spans.materials = newmaterials;
                spans.materialsCount = newmaterials.size();

                //trim icicles
                cur=0;
                if(iciclesRenumbering.size()!=0 || RevIciclesRenumbering.size()!=0)
                {
                    int dummy=0;
                }
                for(int i=0;i<spans.icicles.length;i++)
                {
                    if(!usedicicles.contains(spans.icicles[i]))
                    {
                        spans.icicles[i] = null;
                    }
                    else
                    {
                        iciclesRenumbering.put(cur, i);
                        RevIciclesRenumbering.put(i, cur);
                        if(iciclesRenumbering.size()!=RevIciclesRenumbering.size())
                        {
                            int dummy=0;
                        }
                        cur++;
                    }
                }
                prpobjects.plDrawableSpans.PlIcicle[] newicicles = new prpobjects.plDrawableSpans.PlIcicle[cur];
                for(int i=0;i<cur;i++)
                {
                    prpobjects.plDrawableSpans.PlIcicle ici = spans.icicles[iciclesRenumbering.get(i)];
                    newicicles[i] = ici;
                }
                spans.icicles = newicicles;
                spans.oldiciclecount = spans.icicleCount;
                spans.icicleCount = newicicles.length;

                //trim buffergroups
                cur=0;
                for(int i=0;i<spans.groups.length;i++)
                {
                    if(!usedgroups.contains(spans.groups[i]))
                    {
                        spans.groups[i] = null;
                    }
                    else
                    {
                        groupsRenumbering.put(cur, i);
                        groupsRenumberingRev.put(i, cur);
                        cur++;
                    }
                }
                prpobjects.plDrawableSpans.PlGBufferGroup[] newgroups = new prpobjects.plDrawableSpans.PlGBufferGroup[cur];
                for(int i=0;i<cur;i++)
                {
                    prpobjects.plDrawableSpans.PlGBufferGroup group = spans.groups[groupsRenumbering.get(i)];
                    newgroups[i] = group;
                }
                spans.groups = newgroups;
                spans.meshcount = newgroups.length;

                //trim DIIndices
                cur=0;
                for(int i=0;i<spans.DIIndices.length;i++)
                {
                    if(!usedspanindexs.contains(spans.DIIndices[i]))
                    {
                        spans.DIIndices[i] = null;
                    }
                    else
                    {
                        spanindexRenumbering.put(cur, i);
                        spanindexRenumberingRev.put(i, cur);
                        cur++;
                    }
                }
                prpobjects.plDrawableSpans.PlDISpanIndex[] newspanindexs = new prpobjects.plDrawableSpans.PlDISpanIndex[cur];
                for(int i=0;i<cur;i++)
                {
                    prpobjects.plDrawableSpans.PlDISpanIndex item = spans.DIIndices[spanindexRenumbering.get(i)];
                    newspanindexs[i] = item;
                }
                spans.DIIndices = newspanindexs;
                spans.DIIndicesCount = newspanindexs.length;

                //trim spansourceindex
                int[] newssi = new int[spans.icicleCount];
                for(int i=0;i<spans.icicleCount;i++)
                {
                    //int item = spans.spanSourceIndices[iciclesRenumbering.get(i)];
                    int item = i; //it was an n->n mapping before; keep it that way.
                    newssi[i] = item;
                }
                spans.spanSourceIndices = newssi;
                spans.spanCount = newssi.length; //which is equal to icicleCount, as it should be.

                //trim fogEnvironmentRefs
                Uruobjectref[] newfogs = new Uruobjectref[spans.icicleCount];
                for(int i=0;i<spans.icicleCount;i++)
                {
                    Uruobjectref item = spans.fogEnvironmentRefs[iciclesRenumbering.get(i)];
                    newfogs[i] = item;
                }
                spans.fogEnvironmentRefs = newfogs;
                //no count item here.

                //trim spans (not actually compiled, but used during compilation, so we should do them too.
                Vector<prpobjects.plDrawableSpans.PlIcicle> newspans = new Vector();
                for(int i=0;i<spans.icicleCount;i++)
                {
                    prpobjects.plDrawableSpans.PlIcicle item = spans.spans.get(iciclesRenumbering.get(i));
                    newspans.add(item);
                }
                spans.spans = newspans;
                //no count item here.

                //trim spacetree
                if(spans.embeddedtype==Typeid.plSpaceTree)
                {
                    boolean advancedPlSpaceTrim = true;
                    if(advancedPlSpaceTrim)
                    {
                        plDrawableSpans.PlSpaceTree st = spans.xspacetree;
                        plDrawableSpans.modPlSpaceTree modst = new plDrawableSpans.modPlSpaceTree();
                        modst.treespansource = spans;
                        modst.readFromPlSpaceTree(st);
                        modst.renumber(RevIciclesRenumbering);
                        if(modst.root!=null)
                        {
                            spans.xspacetree = modst.generatePlSpaceTree();
                        }
                        else
                        {
                            spans.embeddedtype = Typeid.nil;
                            spans.xspacetree = null;
                        }
                        int dummy=0;
                    }
                    else
                    {
                        spans.embeddedtype = Typeid.nil;
                    }
                }



                //change references to the arrays.
                for(PrpRootObject diro: dest.FindAllObjectsOfType(Typeid.plDrawInterface))
                {
                    prpobjects.plDrawInterface di = diro.castTo();
                    for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
                    {
                        if(sgr.subsetgroupindex!=-1)
                        {
                            if(sgr.span.hasref() && sgr.span.xdesc.equals(spanobj.header.desc))
                            {
                                //uru.moulprp.PlDrawableSpans spans = prpdistiller.distiller.findObject(sourceprpfiles, sgr.span.xdesc).castTo();
                                sgr.subsetgroupindex = spanindexRenumberingRev.get(sgr.subsetgroupindex);
                                prpobjects.plDrawableSpans.PlDISpanIndex spanindex = spans.DIIndices[sgr.subsetgroupindex];
                                for(int i=0;i<spanindex.indices.length;i++)
                                {
                                    spanindex.indices[i] = RevIciclesRenumbering.get(spanindex.indices[i]);
                                    int subsetind = spanindex.indices[i];

                                    prpobjects.plDrawableSpans.PlIcicle icicle = spans.icicles[subsetind];
                                    icicle.parent.parent.materialindex = materialsRenumberingRev.get(icicle.parent.parent.materialindex);
                                    icicle.parent.groupIdx = groupsRenumberingRev.get(icicle.parent.groupIdx);
                                    Uruobjectref matrefb = spans.materials.get(icicle.parent.parent.materialindex);
                                    prpobjects.plDrawableSpans.PlGBufferGroup bg = spans.groups[icicle.parent.groupIdx];
                                }
                            }
                        }
                    }
                }
            }

            //run through quickly and see if we haven't broken it *too* badly.
            if(d.runtests)
            {
                for(PrpRootObject diro: dest.FindAllObjectsOfType(Typeid.plDrawInterface))
                {
                    prpobjects.plDrawInterface di = diro.castTo();
                    for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
                    {
                        if(sgr.subsetgroupindex!=-1)
                        {
                            prpobjects.plDrawableSpans spans = prpdistiller.distiller.findObject(sourceprpfiles, sgr.span.xdesc).castTo();
                            prpobjects.plDrawableSpans.PlDISpanIndex spanindex = spans.DIIndices[sgr.subsetgroupindex];
                            for(int subsetind: spanindex.indices)
                            {
                                prpobjects.plDrawableSpans.PlIcicle icicle = spans.icicles[subsetind];
                                Uruobjectref matrefb = spans.materials.get(icicle.parent.parent.materialindex);
                                prpobjects.plDrawableSpans.PlGBufferGroup bg = spans.groups[icicle.parent.groupIdx];
                            }
                        }
                    }
                }
            }
        }

        //trim coordinate interfaces.
        boolean trimci = false;
        if(trimci)
        {
            for(PrpRootObject ro: dest.FindAllObjectsOfType(Typeid.plCoordinateInterface))
            {
                prpobjects.plCoordinateInterface ci = ro.castTo();
                Vector<Uruobjectref> newchildren = new Vector();
                for(Uruobjectref ref: ci.children)
                {
                    if(ref.hasref())
                    {
                        //PrpRootObject obj = findObject(dest, altdests,ref.xdesc.objectname.toString(),ref.xdesc.objecttype);
                        PrpRootObject obj = dest.findObject(ref.xdesc.objectname.toString(),ref.xdesc.objecttype);
                        if(obj!=null) newchildren.add(ref);
                    }
                }
                Uruobjectref[] newchildren2 = shared.generic.convertVectorToArray(newchildren, Uruobjectref.class);
                ci.children = newchildren2;
                ci.count = newchildren2.length;
            }
        }

        //test check for missing refs.
        for(PrpRootObject ro: dest.objects2)
        {
            for(Uruobjectdesc ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, ro))
            {
                PrpRootObject obj = findObject(dest, altdests, ref);
                if(obj==null)
                {
                    m.msg("broken ref: "+ref.toString()+" pointed to by "+ro.toString());
                }
            }
        }

        //boolean dotests = false;
        if(!d.runtests)
        {
            m.msg("skipping tests.");
        }
        else
        {

            //test check for missing refs.
            for(PrpRootObject ro: dest.objects2)
            {
                for(Uruobjectdesc ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, ro))
                {
                    PrpRootObject obj = findObject(dest, altdests, ref.objectname.toString(), ref.objecttype);
                    if(obj==null)
                    {
                        m.msg("not found: "+ref.toString()+" pointed to by "+ro.toString());
                    }
                }
            }

            //test check for source objects which link to dest, but are not included in dest.
            for(prpfile prp: sourceprpfiles)
            {
                for(PrpRootObject ro: prp.objects2)
                {
                    ro.ensureParsed();
                    Uruobjectdesc linksToDest=null;
                    for(Uruobjectdesc ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, ro))
                    {
                        if(dest.findObject(ref.objectname.toString(), ref.objecttype)!=null)
                        {
                            linksToDest=ref;
                            break;
                        }
                    }
                    if(linksToDest!=null)
                    {
                        if(findObject(dest, altdests, ro.header.desc.objectname.toString(), ro.header.desc.objecttype)==null)
                        {
                            m.msg("unused linkee: "+ro.toString()+" points to "+linksToDest.toString());
                        }
                    }

                }
            }
        }

    }
    private static void findDistillList(Vector<prpfile> sourceprpfiles, HashSet<Uruobjectdesc> everythingNeeded, PrpRootObject obj, PrpRootObject callee)
    {
        if(obj==null)
        {
            m.warn("Found a desc whose object is not present in the source prp files.");
            return;
        }

        //PrpRootObject obj = findObject(sourceprpfiles, needed);
        obj.ensureParsed();
        Uruobjectdesc ref = obj.header.desc;

        if(!everythingNeeded.contains(ref) || ref.objecttype==Typeid.plDrawableSpans) //only recurse if we don't have this.
        {
            if(ref.objecttype==Typeid.plSceneNode)
            {
                //skip scenenodes.
            }
            /*else if(ref.objecttype==Typeid.plCoordinateInterface)
            {
                uru.moulprp.x0015CoordinateInterface ci = obj.castTo();
                everythingNeeded.add(ref);
                //skip the children.
            }*/
            else if(callee!=null && ref.objecttype==Typeid.plDrawableSpans) //if callee==null, then we should just include the whole thing, since it was selected to be included in the main list.
            {
                if(callee.header.objecttype==Typeid.plDrawInterface)
                {
                    //do this differently to skip all the other junk it references.
                    //refs are: materials, fogEnvironmentRefs, sceneobject,
                    //PrpRootObject obj = shared.generic.createSerializedClone(obj);
                    prpobjects.plDrawableSpans spans = obj.castTo();

                    if(spans.scenenode.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, spans.scenenode.xdesc), obj);
                    for(Uruobjectref fog: spans.fogEnvironmentRefs)
                    {
                        if(fog.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, fog.xdesc), obj);
                    }
                    prpobjects.plDrawInterface di = callee.castTo();
                    /*for(Uruobjectref matref: di.findAllMaterials(sourceprpfiles))
                    {
                        if(matref.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, matref.xdesc), obj);
                    }*/
                    for(prpobjects.plDrawInterface.SubsetGroupRef sgr: di.subsetgroups)
                    {
                        if(sgr.subsetgroupindex!=-1)
                        {
                            if(sgr.span.hasref() && sgr.span.xdesc.equals(obj.header.desc))
                            {
                                prpobjects.plDrawableSpans.PlDISpanIndex spanindex = spans.DIIndices[sgr.subsetgroupindex];
                                for(int subsetind: spanindex.indices)
                                {
                                    prpobjects.plDrawableSpans.PlIcicle icicle = spans.icicles[subsetind];
                                    Uruobjectref matref = spans.materials.get(icicle.parent.parent.materialindex);
                                    if(matref.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, matref.xdesc), obj);

                                    if(icicle.parent.parent.permaLights!=null)
                                        for(Uruobjectref light: icicle.parent.parent.permaLights)
                                        {
                                            if(light.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, light.xdesc), obj);
                                        }
                                    if(icicle.parent.parent.permaProjs!=null)
                                        for(Uruobjectref light: icicle.parent.parent.permaProjs)
                                        {
                                            if(light.hasref()) findDistillList(sourceprpfiles, everythingNeeded, findObject(sourceprpfiles, light.xdesc), obj);
                                        }
                                }
                            }
                        }
                    }



                    everythingNeeded.add(ref);
                    
                }
                else if(callee.header.objecttype==Typeid.plDrawableSpans)
                {
                    //points to itself; ignore.
                    if(!obj.header.desc.equals(callee.header.desc))
                    {
                        m.warn("Unusual case in distiller.");
                    }
                }
                else
                {
                    m.warn("Unusual case in distiller.");
                }

            }
            else
            {
                everythingNeeded.add(ref);
                for(Uruobjectdesc outref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, obj))
                {
                    PrpRootObject outobj = findObject(sourceprpfiles, outref);
                    //if(outobj!=null)
                    //{
                        findDistillList(sourceprpfiles, everythingNeeded, outobj, obj);
                    //}
                    //else
                    //{
                    //    m.warn("Found a desc whose object is not present in the source prp files: "+outref.toString());
                    //}
                }
            }
        }

    }
/*    private static void findDistillList(Vector<prpfile> sourceprpfiles, HashSet<Uruobjectdesc> everythingNeeded, Uruobjectdesc needed)
    {
        PrpRootObject obj = findObject(sourceprpfiles, needed);
        obj.ensureParsed();

        for(Uruobjectdesc ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, obj))
        {
            if(!everythingNeeded.contains(ref)) //only recurse if we don't have this.
            {
                if(ref.objecttype==Typeid.plSceneNode)
                {
                    //skip scenenodes.
                }
                else if(ref.objecttype==Typeid.plDrawableSpans)
                {
                    //do this differently to skip all the other junk it references.
                    everythingNeeded.add(ref);
                    findDistillList(sourceprpfiles, everythingNeeded, ref);
                }
                else
                {
                    everythingNeeded.add(ref);
                    findDistillList(sourceprpfiles, everythingNeeded, ref);
                }
            }
        }
    }*/
    private static PrpRootObject findObject(prpfile p1, Vector<prpfile> ps, String name, Typeid tid)
    {
        //String name = desc.objectname.toString();
        //Typeid tid = desc.objecttype;
        PrpRootObject result = p1.findObject(name, tid);
        if(result!=null) return result;
        for(prpfile prp: ps)
        {
            result = prp.findObject(name, tid);
            if(result!=null) return result;
        }
        return null;
    }
    public static PrpRootObject findObject(Vector<prpfile> prps, Uruobjectdesc desc)
    {
        for(prpfile prp: prps)
        {
            PrpRootObject ro = prp.findObjectWithDesc(desc);
            if(ro!=null) return ro;
        }
        return null;
    }
    public static PrpRootObject findObject(prpfile p1, Vector<prpfile> prps, Uruobjectdesc desc)
    {
        PrpRootObject ro2 = p1.findObjectWithDesc(desc);
        if(ro2!=null) return ro2;
        for(prpfile prp: prps)
        {
            PrpRootObject ro = prp.findObjectWithDesc(desc);
            if(ro!=null) return ro;
        }
        return null;
    }
    //not functional yet:
    /*public static int distillListOneLayerDeep(prpfile dest, Vector<prpfile> altdests, Vector<prpfile> sourceprpfiles, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns, Vector<Uruobjectdesc> list)
    {
        Vector<Uruobjectdesc> nextlist = new Vector();

        //move all the items over.
        for(Uruobjectdesc desc: list)
        {
            //check if we have this item already.
            Uruobjectdesc descFoundInDest = findDesc(dest,altdests,desc.objectname.toString(),desc.objecttype);

            if(descFoundInDest==null)
            {
                //find it in the source
                PrpRootObject ro = findObject(sourceprpfiles,desc);
                if(ro==null)
                {
                    m.warn("obj not found.");
                }

                //add its references to the new list.
                ro.ensureParsed();
                for(Uruobjectdesc ref: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, ro))
                {
                    nextlist.add(ref);
                }

                //copy it to the dest

            }
        }

        updateAllReferences(dest, refReassigns);

        //copy nextlist into list
        list.clear();
        list.addAll(nextlist);
        return list.size();





        Pageid destid = dest.header.pageid;

        int numSaw = 0;

        //ArrayDeque<PrpRootObject> newobjs = new ArrayDeque();
        //HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
        for(Uruobjectdesc desc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, dest))
        {
            if(!desc.pageid.equals(destid))
            {
                //copy this object.
                numSaw++;

                if(desc.objecttype==Typeid.plSceneNode)
                {
                    //point it to the local scenenode instead. (assume it has 1 and only 1.)
                    Uruobjectdesc sndesc = dest.FindAllObjectsOfType(Typeid.plSceneNode)[0].header.desc;
                    sndesc.copyInto(desc);
                }
                else
                {
                    //find it
                    boolean found = false;
                    for(prpfile prp: prpfiles)
                    {
                        PrpRootObject obj = prp.findObject(desc.objectname.toString(), desc.objecttype);
                        if(obj!=null)
                        {
                            //found it
                            distiller.copyObjectAndModifyRef(obj, dest, refReassigns);
                            if(desc.objecttype==Typeid.plSceneObject)
                            {
                                //add to scenenode.
                                uru.moulprp.x0000Scenenode sn = dest.FindAllObjectsOfType(Typeid.plSceneNode)[0].castTo();
                                sn.addToObjectrefs1(Uruobjectref.createFromUruobjectdesc(desc));
                            }
                            found = true;
                            break;
                        }
                    }
                    if(!found) m.warn("Unable to find obj.");
                }
            }
        }

        updateAllReferences(dest, refReassigns);

        return numSaw;
    }*/
    public static int distillEverythingOneLayerDeep(prpfile dest, Vector<prpfile> prpfiles, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        distillInfo reallyShouldntDoThis = new distillInfo();
        reallyShouldntDoThis.refReassigns = refReassigns;

        Pageid destid = dest.header.pageid;

        int numSaw = 0;

        //ArrayDeque<PrpRootObject> newobjs = new ArrayDeque();
        //HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
        for(Uruobjectdesc desc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, dest))
        {
            if(!desc.pageid.equals(destid))
            {
                //copy this object.
                numSaw++;

                if(desc.objecttype==Typeid.plSceneNode)
                {
                    //point it to the local scenenode instead. (assume it has 1 and only 1.)
                    Uruobjectdesc sndesc = dest.FindAllObjectsOfType(Typeid.plSceneNode)[0].header.desc;
                    sndesc.copyInto(desc);
                }
                else
                {
                    //find it
                    boolean found = false;
                    for(prpfile prp: prpfiles)
                    {
                        PrpRootObject obj = prp.findObject(desc.objectname.toString(), desc.objecttype);
                        if(obj!=null)
                        {
                            //found it
                            distiller.copyObjectAndModifyRef(obj, dest, reallyShouldntDoThis);
                            if(desc.objecttype==Typeid.plSceneObject)
                            {
                                //add to scenenode.
                                prpobjects.x0000Scenenode sn = dest.FindAllObjectsOfType(Typeid.plSceneNode)[0].castTo();
                                sn.addToObjectrefs1(Uruobjectref.createFromUruobjectdesc(desc));
                            }
                            found = true;
                            break;
                        }
                    }
                    if(!found) m.warn("Unable to find obj.");
                }
            }
        }

        updateAllReferences(dest, reallyShouldntDoThis);

        return numSaw;
    }

    /*public static HashMap<Uruobjectdesc, Uruobjectdesc> distillTextures(String texturePrp, String destinationPrp, String[] affectedPrps)
    {
        prpfile destprp = prpfile.createFromFile(destinationPrp, true);
        prpfile textprp = prpfile.createFromFile(texturePrp, true);
        HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns = new HashMap();
        distillTextures(destprp, textprp, new String[]{}, refReassigns);
        return refReassigns;
    }*/
    //moves the textures used by destinationPrp, into destinationPrp, and updates references in the affectedPrps.
    public static void distillTextures(prpfile destprp, prpfile textprp, String[] affectedPrps, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        
        
        PrpRootObject[] layers = destprp.FindAllObjectsOfType(Typeid.plLayer);
        for(PrpRootObject layer2: layers)
        {
            x0006Layer layer = layer2.castTo();
            if(layer.texture.hasref())
            {
                PrpRootObject texture2 = textprp.findObjectWithRef(layer.texture);
                //if(texture2.header.objecttype==Typeid.plMipMap)
                //{
                    //uru.moulprp.x0004MipMap texture = texture2.castTo();
                if(texture2!=null)
                {
                    distillInfo reallyShouldntDoThis = new distillInfo();
                    reallyShouldntDoThis.refReassigns = refReassigns;
                    distiller.copyObjectAndModifyRef(texture2, destprp/*, refReassigns*/,reallyShouldntDoThis);
                }
                else
                {
                    //Test to see if we still have this texture:
                    PrpRootObject texture3 = destprp.findObjectWithRef(layer.texture);
                    if(texture3==null)
                    {
                        m.warn("Unable to find texture in prp file: ",layer.texture.toString());
                    }
                }
                //}
                //else if (texture2.header.objecttype==Typeid.plCubicEnvironMap)
                //{
                //    uru.moulprp.x0005Environmap texture = texture2.castTo();
                //    distiller.copyObjectAndModifyRef(texture2, destprp, refReassigns);
                //}
                //else
                //{
                //    m.throwUncaughtException("Should have plMipMap or plCubicEnvironMap.");
                //}
            }
        }
        
        distiller.updateTextureRefs(destprp,refReassigns);
        for(String s: affectedPrps)
        {
            distiller.updateTextureRefs(s, refReassigns);
        }
        
    }
    
    private static void updateTextureRefs(String prpname, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        prpfile destprp = prpfile.createFromFile(prpname, true);
        updateTextureRefs(destprp,refReassigns);
    }
    public static void updateTextureRefs(prpfile destprp, HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns)
    {
        for(PrpRootObject layer2: destprp.FindAllObjectsOfType(Typeid.plLayer))
        {
            x0006Layer layer = layer2.castTo();
            if(layer.texture.hasref())
            {
                //int u1 = layer.texture.xdesc.hashCode();
                //int u2 = refReassigns.keySet().toArray()[0].hashCode();
                Uruobjectdesc newdesc = refReassigns.get(layer.texture.xdesc);
                if(newdesc!=null)
                {
                    newdesc.copyInto(layer.texture.xdesc);
                }
            }
        }
                
    }
    public static void copyObjectAndModifyRef(PrpRootObject object, prpfile dest, /*HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns,*/ distillInfo d)
    {
        //check if we have already reassigned this object.
        if(d.refReassigns==null || object==null||object.header==null)
        {
            int dummy=0;
        }
        Uruobjectdesc newpos = d.refReassigns.get(object.header.desc);
        if(newpos==null)
        {
            //copy the object and add the ref to our list.
            Uruobjectdesc olddesc = object.header.desc.deepClone();
            Uruobjectdesc newdesc = olddesc.deepClone();
            newdesc.pageid = dest.header.pageid; //change pageid.
            newdesc.pagetype = dest.header.pagetype;
            PrpRootObject newobj = PrpRootObject.createFromDescAndObject(newdesc, object.getObject());
            dest.addObject(newobj);
            //refReassigns.put(olddesc, newdesc);
            d.addRefReassignment(olddesc, newdesc);
        }
        else
        {
            //do nothing, we've already got this object.
        }
    }
    
    public static void updateAllReferences(prpfile prpToUpdate, /*HashMap<Uruobjectdesc, Uruobjectdesc> refReassigns,*/distillInfo d)
    {
        Vector<Uruobjectdesc> descs = shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, prpToUpdate);
        for(Uruobjectdesc desc: descs)
        {
            Uruobjectdesc newdesc = d.refReassigns.get(desc);
            //Uruobjectdesc lastdesc = null;
            /*if(newdesc==null)
            {
                //not reassigned so ignore.
            }
            else
            {
                newdesc.copyInto(desc);
            }*/

            //this is okay, since we now make sure that when refReassigns are added, they don't point directly to themselves, and there seems to be no problem with loops.
            while(newdesc!=null)
            {
                newdesc.copyInto(desc);
                //lastdesc = newdesc;
                newdesc = d.refReassigns.get(newdesc); //keep going while there are transitive reassignments.
            }
        }
    }
    
}

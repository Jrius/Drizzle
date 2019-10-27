/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import prpobjects.prpfile;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;
import static prpobjects.Typeid.plDrawableSpans;
import shared.m;
import shared.b;

public class PrpDiff
{
    static final boolean reportIdenticals = false;

    public static void FindDiff(String sourceprp, String destprp)
    {
        FindDiff(sourceprp, destprp, null);
    }
    
    public static void FindDiff(String sourceprp, String destprp, String diffDumpFolder)
    {
        prpfile prp1 = prpfile.createFromFile(sourceprp, true);
        prpfile prp2 = prpfile.createFromFile(destprp, true);
        
        boolean dumpChangedObjects = diffDumpFolder != null && !diffDumpFolder.isEmpty();
        if (dumpChangedObjects)
        {
            if (!diffDumpFolder.endsWith("\\") || !diffDumpFolder.endsWith("/"))
                diffDumpFolder += "/";
            diffDumpFolder += new File(sourceprp).getName();
        }

        for(PrpRootObject obj1: prp1.objects2)
        {
            PrpRootObject obj2 = prp2.findObjectWithDesc(obj1.header.desc);
            if(obj2==null)
            {
                //prp1 has but prp2 does not.
                m.msg("Remove object: ",obj1.toString());
            }
            else
            {
                //in both, comparing
                if(obj1.getRawSize()!=obj2.getRawSize())
                {
                    //different sizes.
                    m.msg("Object changed: ",obj1.toString(), " old size: ",Integer.toString(obj1.getRawSize())," new size: ",Integer.toString(obj2.getRawSize()));
                    
                    if (dumpChangedObjects)
                    {
                        String fileName = diffDumpFolder + obj1.toString().replace(':', 'x');
                        try (FileOutputStream stream = new FileOutputStream(fileName + "_a.uof")) {
                            stream.write(obj1.rawdata);
                        } catch (IOException ex) {
                            Logger.getLogger(PrpDiff.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try (FileOutputStream stream = new FileOutputStream(fileName + "_b.uof")) {
                            stream.write(obj2.rawdata);
                        } catch (IOException ex) {
                            Logger.getLogger(PrpDiff.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if(!b.isEqual(obj1.getRawMd5(), obj2.getRawMd5()))
                {
                    //same size, different content.
                    m.msg("Object changed: ",obj1.toString()," (stayed the same size.)");
                    
                    if (dumpChangedObjects)
                    {
                        String fileName = diffDumpFolder + obj1.toString().replace(':', 'x');
                        try (FileOutputStream stream = new FileOutputStream(fileName + "_a.uof")) {
                            stream.write(obj1.rawdata);
                        } catch (IOException ex) {
                            Logger.getLogger(PrpDiff.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try (FileOutputStream stream = new FileOutputStream(fileName + "_b.uof")) {
                            stream.write(obj2.rawdata);
                        } catch (IOException ex) {
                            Logger.getLogger(PrpDiff.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        /*
                        if (obj2.castTo() instanceof prpobjects.plDrawableSpans)
                        {
                            prpobjects.plDrawableSpans dspan1 = obj1.castTo();
                            prpobjects.plDrawableSpans dspan2 = obj2.castTo();
                            
                            assert (dspan1.props == dspan2.props);
                            assert (dspan1.renderLevel == dspan2.renderLevel);
                            assert (dspan1.criteria == dspan2.criteria);
                            assert (dspan1.materialsCount == dspan2.materialsCount);
                            // compare materials here
                            assert (dspan1.icicleCount == dspan2.icicleCount);
                            // compare icicles here
                            assert (dspan1.unused == dspan2.unused);
                            assert (dspan1.spanCount == dspan2.spanCount);
                            // compare spanSourceIndices
                            
                            
                            /*
                            public int[] spanSourceIndices; //unused2
                            //byte[] unused3;
                            public Uruobjectref[] fogEnvironmentRefs; //unused3
                            //BoundingBox[] xboundingBoxes;
                            public BoundingBox xLocalBounds;
                            public BoundingBox xWorldBounds;
                            public BoundingBox xMaxWorldBounds;
                            //int lightcount;
                            //Vector<LightInfo> lightinfos = new Vector<LightInfo>();
                            //public GrowVector<Uruobjectref> lightinfos;
                            //public Vector<Uruobjectref> permaLight = new Vector();
                            //public Vector<Uruobjectref> permaProj = new Vector();
                            public int sourceSpanCount;
                            public int matrixsetcount;
                            public Transmatrix[] localToWorlds; //was blendmatrix
                            public Transmatrix[] worldToLocals; //was matrix2
                            public Transmatrix[] localToBones; //was matrix3
                            public Transmatrix[] boneToLocals; //was matrix4
                            public int DIIndicesCount; //subsetgroupcount
                            public PlDISpanIndex[] DIIndices; //subsetgroups
                            public int meshcount;
                            public PlGBufferGroup[] groups; //meshes
                            public Typeid embeddedtype;
                            public PlSpaceTree xspacetree;
                            public Uruobjectref scenenode;
                            
                            * /
                        }*/
                    }
                }
                else
                {
                    //nothing changed!
                    if(reportIdenticals)
                    {
                        m.msg("Object is identical: ",obj1.toString());
                    }
                }
            }
        }
        for(PrpRootObject obj2: prp2.objects2)
        {
            PrpRootObject obj1 = prp1.findObjectWithDesc(obj2.header.desc);
            if(obj1==null)
            {
                //prp2 has but prp1 does not.
                m.msg("Add object: ",obj2.toString());
            }
        }
    }
}

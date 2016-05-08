package auto.mod;

import static auto.mod.AutoMod_Light.*;
import java.io.File;
import java.io.FilenameFilter;
import static java.lang.Math.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import prpobjects.*;
import shared.*;

public class AutoMod_FixFanAge {
    
    public static float max(float x, float y)
    {
        if (x>y) return x;
        return y;
    }
    
    /**
     * Fix old PyPRP physics bug.
     * @param prp file to process
     */
    public static void removeBumpyPhysics(prpfile prp)
    {
        for (PrpRootObject rophys: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            plHKPhysical phys = rophys.castTo();
            if (phys.havok.EL.toJavaFloat() == -1 && phys.havok.RC.toJavaFloat() == 10)
            {
                phys.havok.EL = Flt.createFromJavaFloat(0.0f);
                phys.havok.RC = Flt.createFromJavaFloat(0.5f);
                rophys.markAsChanged();
            }
        }
    }
    
    /**
     * Disables all dynamic shadows in the age. Provides a HUUUGE speedup in some cases.
     * (alternative: disable in options menu)
     * @param prp file to process
     */
    public static void removeShadowCasters(prpfile prp)
    {
        for (PrpRootObject soro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = soro.castTo();
            
            Refvector torem = new Refvector();
            for (Uruobjectref modif: so.modifiers)
            {
                if (modif.xdesc.objecttype == Typeid.plShadowCaster)
                    torem.add(modif);
            }
            if (torem.size() != 0) soro.markAsChanged();
            for (Uruobjectref modif: torem)
            {
                so.modifiers.remove(modif);
                prp.markObjectDeleted(modif, false);
            }
        }
    }
    
    /**
     * Disables specularity for all layers, providing a good speedup.
     * @param prp file to process
     */
    public static void disableSpecularity(prpfile prp)
    {
        for (PrpRootObject lyrro: prp.FindAllObjectsOfType(Typeid.plLayer))
        {
            prpobjects.x0006Layer lyr = lyrro.castTo();
            lyr.flags3 &= ~(x0006Layer.kShadeSpecular |
                            x0006Layer.kShadeSpecularAlpha |
                            x0006Layer.kShadeSpecularColor |
                            x0006Layer.kShadeSpecularHighlight);
            lyrro.markAsChanged();
        }
    }
    
    /**
     * Bakes light from dynamic lights to static vertex color map.
     * Not finished, probably not worth it anyway.
     * @param prp file to process
     */
    public static void makeDynamicLightingStatic(prpfile prp)
    {
        for (PrpRootObject dsro: prp.FindAllObjectsOfType(Typeid.plDrawableSpans))
        {
            plDrawableSpans ds = dsro.castTo();
            plDrawableSpans.PlSpan span = ds.spans.get(0).parent.parent;
            
            if (span.permaLights == null) continue;
            
            if (span.permaLights.length != 0)
            {
                for (Uruobjectref lightref: span.permaLights)
                {
                    PrpRootObject liro = prp.findObjectWithRef(lightref);
                    if (lightref.xdesc.objecttype == Typeid.plDirectionalLightInfo)
                    {
                        x0055DirectionalLightInfo dli = liro.castTo();
                        x0054LightInfo li = dli.parent;
                        
                        // do something with light here
                    }
                    else
                    {
                        // these are plLimitedDirLightInfo plOmniLightInfo plSpotLightInfo
                        // m.warn("Unhandled light type: " + lightref.xdesc.objecttype);
                    }
                }
            }
        }
        
        AutoMod_FixFanAge.removeAllDynamicLights(prp);
    }
    
    /**
     * Makes all objects use true shadeless - meaning, unaffected by any light.
     * @param prp file to process
     */
    public static void removeAllDynamicLights(prpfile prp)
    {
        // TODO - see if we need to remove permaproj (should be projected texture - if we ever process such PRP, then the user clearly doesn't
        // know what he's doing...)
        
        
        for (PrpRootObject dsro: prp.FindAllObjectsOfType(Typeid.plDrawableSpans))
        {
            plDrawableSpans ds = dsro.castTo();
            
            for (plDrawableSpans.PlIcicle icicle: ds.spans)
            {
                plDrawableSpans.PlSpan span = icicle.parent.parent;

                if (span.permaLights == null) continue;

                if (span.permaLights.length != 0)
                {
                    span.permaLights = new Uruobjectref[0];
                    span.permaLightsCount = 0;
                    span.props &= ~(plDrawableSpans.PlSpan.kPropHasPermaLights);
                    dsro.markAsChanged();
                }
            }
        }
    }

    /**
     * Disable updating on all reflections (water). Provides a very light speedup.
     * @param prp file to process
     */
    public static void disableEnvmapRefresh(prpfile prp)
    {
        for (PrpRootObject emro: prp.FindAllObjectsOfType(Typeid.plDynamicEnvMap))
        {
            plDynamicEnvMap envmap = emro.castTo();
            envmap.refreshRate = new Flt(0);
            emro.markAsChanged();
        }
    }
    
    /**
     * Makes all objects use mix/max distances for drawing. Meaning small objects will be hidden from faraway
     * (a bit similar to LoDs, but without meshes at half resolution). Should provide a very good speedup in Elsewhere.
     * @param prp file to process
     * @param angle if the object is rendered as smaller than this angle (degrees), hide it
     */
    public static void makeMaxDistance(prpfile prp, float angle)
    {
        if (prp.FindAllObjectsOfType(Typeid.plPostEffectMod).length != 0)
            // simple, quick, and a bit hacky
            return;
        
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            
            if (so.drawinterface == null || !so.drawinterface.hasref())
                // if it isn't a visual object, we can't process it
                continue;
            
            Transmatrix l2w = null;
            if (so.coordinateinterface != null && so.coordinateinterface.hasref())
            {
                PrpRootObject ref = prp.findObjectWithRef(so.coordinateinterface);
                if (ref != null)
                {
                    plCoordinateInterface ciref = ref.castTo();
                    if (ciref.localToWorld != null)
                        l2w = ciref.localToWorld;
                }
            }
            
            PrpRootObject diro = prp.findObjectWithRef(so.drawinterface);
            plDrawInterface di = diro.castTo();
            for (plDrawInterface.SubsetGroupRef digroup: di.subsetgroups)
            {
                PrpRootObject dspanro = prp.findObjectWithRef(digroup.span);
                dspanro.markAsChanged();
                plDrawableSpans dspan = dspanro.castTo();
                plDrawableSpans.PlDISpanIndex dispanindex = dspan.DIIndices[digroup.subsetgroupindex];
                for (int icicleindex: dispanindex.indices)
                {
                    simpleobject obj = new simpleobject();
                    
                    plDrawableSpans.PlIcicle icicle = dspan.icicles[icicleindex];
                    plDrawableSpans.PlGBufferGroup group = dspan.groups[icicle.parent.groupIdx];
                    plDrawableSpans.PlGBufferGroup.SubMesh subgroup = group.submeshes[icicle.parent.VBufferIdx];
                    
                    
                    ArrayList<Float> scale;
                    if (l2w != null)
                        scale = getScale(l2w);
                    else
                        scale = getScale(icicle.parent.parent.localToWorld);
                    
                    DrawableSpansEncoders.DecompressedPotsVertices verts = subgroup.decompressAllAndSave(group.fformat);
                    
                    for (int i=icicle.parent.VStartIdx; i<(icicle.parent.VStartIdx+icicle.parent.VLength); i++)
                    {
                        Vector3 v = new Vector3(verts.xs[i],verts.ys[i],verts.zs[i]);

                        
                        if (v.x > obj.boundX)
                            obj.boundX = v.x;
                        if (v.x < obj.boundMX)
                            obj.boundMX = v.x;
                        if (v.y > obj.boundY)
                            obj.boundY = v.y;
                        if (v.y < obj.boundMY)
                            obj.boundMY = v.y;
                        if (v.z > obj.boundZ)
                            obj.boundZ = v.z;
                        if (v.z < obj.boundMZ)
                            obj.boundMZ = v.z;
                    }
                    
                    // where the real stuff begins
                    
                    float sizex = obj.boundX - obj.boundMX;
                    float sizey = obj.boundY - obj.boundMY;
                    float sizez = obj.boundZ - obj.boundMZ;
                    
                    sizex *= scale.get(0);
                    sizey *= scale.get(1);
                    sizez *= scale.get(2);
                    
                    float mbs = max(sizex, max(sizey, sizez));
                    
                    // we will hide the object if it's roughly under mindeg degrees
                    // meaning... a bit of trigonometry ! Yeah !
                    // adjacent = opposite / tan a
                    
                    float mindeg = (float) (mbs*.5/tan(angle / 180. * PI)); // divide by 2 (since we're in a rectangle triangle (is that the english for it?))
                    
                    icicle.parent.parent.maxDist = new Flt(mindeg);
                }
            }
        }
    }
    
    public static void simpleOpti(String infolder, String outfolder, String ageName)
    {
        m.msg("Removing performance-eaters in ALL of " + ageName + "'s prps in this directory...");
        File folder1 = new File(infolder);

        File[] folder1children = folder1.listFiles();
        for(File child1: folder1children)
        {
            if (child1.getName().startsWith(ageName+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
            {
                m.msg("  Loading " + child1.getName() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                m.msg("  Editing...");
                AutoMod_FixFanAge.disableSpecularity(prp);
                AutoMod_FixFanAge.removeShadowCasters(prp);
                AutoMod_FixFanAge.removeBumpyPhysics(prp);
                AutoMod_FixFanAge.disableEnvmapRefresh(prp);
                AutoMod_FixFanAge.removeAllDynamicLights(prp);
                AutoMod_FixFanAge.makeMaxDistance(prp,.35f);

                m.msg("  Saving...");
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
            }
        }
        m.msg("Done !");
    }
    
    public static void setExcludeFlags(String infile, String outfolder)
    {
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(infile, true);

        // parse the PRP: find all object referenced by responders, logicmod or pfm
        // de-sync any object with animations not in the ref list, which don't have physics

        ArrayList<String> objlist = new ArrayList();

        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plPythonFileMod))
        {
            prpobjects.plPythonFileMod pfm = ro.castTo();
            for (plPythonFileMod.Pythonlisting element: pfm.listings)
            {
                switch (element.type)
                {
                    case 5: case 6: case 7: case 8: case 9: case 10:
                    case 11: case 12: case 14: case 15: case 16: case 17:
                    case 18: case 19: case 21:
                        if (element.xRef.xdesc.objecttype == Typeid.plAGMasterMod)
                            objlist.add(element.xRef.xdesc.objectname.toString());
                        if (element.xRef.xdesc.objecttype == Typeid.plLayerAnimation)
                            // err, can this even happen ?
                            ;
                        break;
                }
            }
        }
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plAnimCmdMsg))
        {
            prpobjects.PrpMessage.PlAnimCmdMsg amsg = ro.castTo();
            for (prpobjects.Uruobjectref recv: amsg.parent.parent.receivers)
            {
                if (recv.xdesc.objecttype == Typeid.plLayerAnimation)
                    // will need to figure something out with these... for now we won't touch them.
                    ;
                if (recv.xdesc.objecttype == Typeid.plAGMasterMod)
                    objlist.add(recv.xdesc.objectname.toString());
            }
        }

        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            prpobjects.plSceneObject so = ro.castTo();
            for (Uruobjectref mod: so.modifiers)
            {
                if (mod.xdesc.objecttype == Typeid.plAGMasterMod)
                {
                    boolean notExcluded = false;
                    for (String name: objlist)
                    {
                        if (name.equals(mod.xdesc.objectname.toString()))
                            notExcluded = true;
                    }
                    if (!notExcluded)
                    {
                        so.parent.flags |= plSynchedObject.kExcludeAllPersistentState;
                        ro.markAsChanged();
                    }
                }
            }
        }

        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
        m.msg("Done.");
    }
    
    /**
     * Removes duplicate textures due to per-page export from PyPRP.
     * Moves any duplicated texture to the dedicated "Textures" PRP.
     * Because textures are big objects, it seems this sometimes fail - that's bad news.
     * @param infolder
     * @param outfolder
     * @param ageName
     */
    public static void squashDuplicateTextures(String infolder, String outfolder, String ageName)
    {
        m.warn("This is a very memory-intensive process, if Drizzle crashes complaining about GC and memory limit, try increasing the VM memory...");
        
        File folder1 = new File(infolder);

        // get the age's prps
        File[] folder1children = folder1.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File f, String s)
            {
                return s.endsWith(".prp") && s.startsWith(ageName + "_District_") && !s.contains("_District_BuiltIn.prp");
            }
        });
        
        // sort by priority: newer comes last, so they can override textures from older files
        Arrays.sort(folder1children, new Comparator<File>(){
            @Override
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
        
        // get the textures prp, and remove it from the "to process" list
        File texFile = null;
        for (int i=0; i<folder1children.length; i++)
        {
            File child = folder1children[i];
            if (child.getName().equals(ageName + "_District_Textures.prp"))
            {
                texFile = child;
                folder1children[i] = null;
                break;
            }
        }
        
        if (texFile == null)
            throw new RuntimeException("Couldn't find texture PRP !");
        
        prpobjects.prpfile texPRP = prpobjects.prpfile.createFromFile(texFile.getAbsoluteFile(), true);
        
        for (File child1: folder1children)
        {
            if (child1 == null) // ignore the textures prp we removed
                continue;
            
            prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);
            
            int removed = 0;
            
            for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plMipMap))
            {
                PrpRootObject texRo = texPRP.findObject(ro.header.desc.objectname.toString(), Typeid.plMipMap);
                if (texRo != null)
                {
                    // move mipmap to tex prp
                    // because we process newer files last, this ensures we only use the latest version of the texture
                    texPRP.deleteObject(Typeid.plMipMap, ro.header.desc.objectname.toString());
                    x0004MipMap mip = (x0004MipMap)(ro.castTo());
                    texPRP.addObject(Typeid.plMipMap, ro.header.desc.objectname.toString(), mip);
                    texPRP.markAsChanged();
                    
                    prp.deleteObject(Typeid.plMipMap, ro.header.desc.objectname.toString());
                    prp.markAsChanged();
                    
                    // editing the layers to reference the new mipmap is done in a second pass after all textures are sorted
                    removed++;
                }
                else
                {
                    // mipmap not already in texture prp, create it
                    x0004MipMap mip = (x0004MipMap)(ro.castTo());
                    texPRP.addObject(Typeid.plMipMap, ro.header.desc.objectname.toString(), mip);
                    texPRP.markAsChanged();
                    
                    prp.deleteObject(Typeid.plMipMap, ro.header.desc.objectname.toString());
                    prp.markAsChanged();
                    
                    // once again, correcting texture references in layers is done in second pass only
                    removed++;
                }
            }
            if (removed == 0 && false) // we MUST save the PRP, since we'll need to modify some layer references later...
            {
                // free mem
                prp = null;
                continue;
            }
            
            String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
            prp.saveAsFile(outputfilename);
            
            m.msg("Removed " + removed + " textures for " + prp.filename);
            
            // free mem
            prp = null;
        }
        
        
        // now, search for duplicates using different (but similar) names
        ArrayList<PrpRootObject> toDelete = new ArrayList();
        ArrayList<PrpRootObject> remapTo = new ArrayList();
        for (PrpRootObject mmro: texPRP.FindAllObjectsOfType(Typeid.plMipMap))
        {
            String texName = mmro.header.desc.objectname.toString();
            if (texName.matches("^\\d+-.*$")) // 1-duplicateTextureName, etc
            {
                String normalName = texName.substring(texName.indexOf("-")+1);
                int prefix = Integer.parseInt(texName.substring(0, texName.indexOf("-")));
                
                PrpRootObject lower = texPRP.findObject(normalName, Typeid.plMipMap);
                PrpRootObject upper = texPRP.findObject("" + (prefix+1) + "-" + normalName, Typeid.plMipMap);
                
                if (lower == null)
                    // there is no older version of this texture, so we'll keep it.
                    continue;
                
                toDelete.add(mmro);
                remapTo.add(lower);
                
                if (upper != null)
                {
                    // no more recent texture present, we'll replace the older one !
                    texPRP.deleteObject(Typeid.plMipMap, normalName);
                    texPRP.addObject(Typeid.plMipMap, normalName, (x0004MipMap)(mmro.castTo()));
                }
            }
            if (texName.matches("^.*\\.\\d{3,3}$")) // duplicateTextureName.001, etc
            {
                String normalName = texName.substring(0, texName.lastIndexOf("."));
                int prefix = Integer.parseInt(texName.substring(texName.lastIndexOf(".")+1));
                
                PrpRootObject lower = texPRP.findObject(normalName, Typeid.plMipMap);
                PrpRootObject upper = texPRP.findObject(normalName + String.format(".%03d", prefix+1), Typeid.plMipMap);
                
                if (lower == null)
                    // there is no older version of this texture, so we'll keep it.
                    continue;
                
                toDelete.add(mmro);
                remapTo.add(lower);
                
                if (upper != null)
                {
                    // no more recent texture present, we'll replace the older one !
                    texPRP.deleteObject(Typeid.plMipMap, normalName);
                    texPRP.addObject(Typeid.plMipMap, normalName, (x0004MipMap)(mmro.castTo()));
                }
            }
        }
        
        if (!toDelete.isEmpty())
        {
            m.msg("Detected " + toDelete.size() + " implicit duplicate textures, removing...");
            texPRP.markAsChanged();
        }
        
        for (PrpRootObject tdro: toDelete)
            texPRP.deleteObject(Typeid.plMipMap, tdro.header.desc.objectname.toString());
        
        
        if (!texPRP.hasChanged())
        {
            m.msg("No texture needs to be squashed into main PRP.");
            return;
        }
        
        
        // update the references in the PRPs
        File[] outFolderChildren = new File(outfolder).listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File f, String s)
            {
                return s.endsWith(".prp") && s.startsWith(ageName + "_District_") && !s.contains("_District_BuiltIn.prp") && !s.contains("_District_Textures.prp");
            }
        });
        for (File child1: outFolderChildren)
        {
            prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);
            for (PrpRootObject lyrro: prp.FindAllObjectsOfType(Typeid.plLayer))
            {
                x0006Layer lyr = lyrro.castTo();
                if (lyr.texture == null || lyr.texture.xdesc == null)
                    continue;
                if (lyr.texture.xdesc.objecttype == Typeid.plMipMap)
                {
                    String texName = lyr.texture.xdesc.objectname.toString();
                    
                    int i=0;
                    for (PrpRootObject delRo: toDelete)
                    {
                        if (delRo.header.desc.objectname.toString().equals(texName))
                            texName = remapTo.get(i).header.desc.objectname.toString();
                        i++;
                    }
                    
                    lyr.texture = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plMipMap, texName, texPRP.header.pageid);
                    lyr.texture.xdesc.pagetype.pagetype |= Pagetype.kBuiltIn; // probably because Textures.prp has negative id, as BuiltIn...
                    
                    lyrro.markAsChanged();
                    prp.markAsChanged();
                }
            }
            
            if (prp.hasChanged())
            {
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
            }
        }
        
        
        String outputfilename = outfolder + "/" + texPRP.header.agename.toString()+"_District_Textures.prp";
        texPRP.saveAsFile(outputfilename);
        
        m.msg("Done !");
    }
}

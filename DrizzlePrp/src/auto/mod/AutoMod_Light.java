package auto.mod;

import java.io.File;
import static java.lang.Math.abs;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.LinkedList;
import prpobjects.*;
import shared.*;
import static shared.m.gettime;

/**
 * Utility for setting vertex color based on predefined light settings.
 * 
 * Ambient light and sun:
 *      Bake ambient light with single sun light source.
 *      Parameters: ambient color, sun color, sun direction.
 * 
 * Ambient occlusion:
 *      Bake AO to vertices. Using only faces.
 *      Parameters: reach, intensity, minimum color
 * 
 * General parameters:
 *      math optimization class: used instead of java.lang.Math class.
 * 
 * 
 * 
 * Automatically excludes wavesets since they don't use vertex color.
 * PRPs are automatically considered GUIs and skipped if a single plPostEffectMod is found. GUIs don't need to be lit.
 * Can also provide a list of objects to exclude (sky...). Might be worth automatically excluding mistless objects.
 * 
 * 
 * Performance:
 *      main perf killer: acos
 *      second killer ?: sqrt
 *      both were replaced with new algorithms found on the internet. Roughly as fast as removing acos, same quality.
 *      Magic !
 * 
 * 
 * Todo:
 *      use object transforms (partly implemented, IIRC)
 * 
 * Issues:
 *      - some objects suffer from wrong normals exported by PyPRP, resulting in incorrect shading on some objects (think the water
 *      or some of the fields in Elodea). Unfortunately, rebuilding those is too complicated.
 *      - obviously, shading will not take sharp edges into account, unless both faces aren't sharing the same vertices.
 * 
 * Note: AO doesn't work across PRPs. Would be too slow.
 * 
 * 
 * Also, each color channel in Plasma is coded on an unsigned byte. Of course, Java only knows signed bytes -
 * watch out when making any modifications to these...
 * 
 */
public class AutoMod_Light {
    public static interface mathInterface
    {
        public float sqrt(float number);
        public float acos(float angle);
        public Vector3 getNormalized(Vector3 vect);
        public float getLength(Vector3 vect);
    }
    public static class mathFISqrtNVidiaAcos implements mathInterface
    {
        public float fisqrt(float number)
        {
            // Quake's Fast Inverse Square Root
            float xhalf = 0.5f*number;
            int i = Float.floatToIntBits(number);
            i = 0x5f3759df - (i>>1); // what the f*ck ?
            number = Float.intBitsToFloat(i);
            number = number*(1.5f - xhalf*number*number);
            return number;
        }
        
        @Override
        public float sqrt(float number)
        {
            return 1/fisqrt(number);
        }

        @Override
        public float acos(float number)
        {
            // taken from nVidia's resources
            float negate = (number<0) ? -1 : 1;
            number=abs(number);
            float ret = ((((
                    -0.0187293f * number + 0.0742610f)
                    * number - 0.2121144f)
                    * number + 1.5707288f)
                    * sqrt(1.0f-number));
            ret = ret - 2 * negate * ret;
            return negate * 3.14159265358979f + ret;
        }

        @Override
        public Vector3 getNormalized(Vector3 vect)
        {
            float length = getLength(vect);
            return new Vector3(vect.x/length, vect.y/length, vect.z/length);
        }

        @Override
        public float getLength(Vector3 vect)
        {
            float f = vect.x*vect.x + vect.y*vect.y + vect.z*vect.z;
            return opti.sqrt(f);
        }
    }
    public static class mathFISqrtCubicApproxAcos implements mathInterface
    {
        public float fisqrt(float number)
        {
            // Quake's Fast Inverse Square Root
            float xhalf = 0.5f*number;
            int i = Float.floatToIntBits(number);
            i = 0x5f3759df - (i>>1); // what the f*ck ?
            number = Float.intBitsToFloat(i);
            number = number*(1.5f - xhalf*number*number);
            return number;
        }
        
        @Override
        public float sqrt(float number)
        {
            return 1/fisqrt(number);
        }

        @Override
        public float acos(float number)
        {
            // found on StackOverflow, really simple function with max 10 deg of error (suitable for us)
            return (-0.69813170079773212f * number * number - 0.87266462599716477f) * number + 1.5707963267948966f;
        }

        @Override
        public Vector3 getNormalized(Vector3 vect)
        {
            float length = getLength(vect);
            return new Vector3(vect.x/length, vect.y/length, vect.z/length);
        }

        @Override
        public float getLength(Vector3 vect)
        {
            float f = vect.x*vect.x + vect.y*vect.y + vect.z*vect.z;
            return opti.sqrt(f);
        }
    }
    
    public static class mathReal implements mathInterface
    {
        @Override
        public float sqrt(float number)
        {
            return (float) java.lang.Math.sqrt(number);
        }

        @Override
        public float acos(float angle)
        {
            return (float) java.lang.Math.acos(angle);
        }

        @Override
        public Vector3 getNormalized(Vector3 vect)
        {
            float length = getLength(vect);
            return new Vector3(vect.x/length, vect.y/length, vect.z/length);
        }

        @Override
        public float getLength(Vector3 vect)
        {
            float f = vect.x*vect.x + vect.y*vect.y + vect.z*vect.z;
            return opti.sqrt(f);
        }
    }
    
    
    public static float[] ambientColor  = {.36f, .385f, .485f, 1.f};                     // base color for vertices with 0% light
    public static float[] directColor   = {1f, 1f, .855f, 1.f};                          // color added to vertices with 100% light
    public static Vector3 sunDirection  = new Vector3(-0.55667f, 0.321394f, -0.766044f); // direction of sunlight

    public static float aoReach=35f;                                            // how far ambient occlusion affects vertices (20ft = 6m)
    public static float aoIntensity=2.2f;                                       // intensity of light occluded by face (2 with acos)
    //public static float[] minAOColor = {0.101857f, 0.079127f, 0.058782f, 1.f};  // clamp AO color removal to these values
    public static float[] minAOColor = {0.2037f, 0.158254f, 0.11756f, 1.f};  // clamp AO color removal to these values
    
    public static mathInterface opti = new mathFISqrtCubicApproxAcos();

    
    public static void setMathInterface(int mathModule)
    {
        // 0: real (accurate), 1: FISqrt + nVidia, 2: FISqrt + cubic approx)
        switch (mathModule)
        {
            case 1:     opti = new mathFISqrtNVidiaAcos(); break;
            case 2:     opti = new mathFISqrtCubicApproxAcos(); break;
            default:    opti = new mathReal(); break;
        }
    }
    
    
    /**
     * Bake ambient color and sunlight to vertices. Sunlight intensity depends of sun orientation.
     * 
     * @param prp the file to process
     * @param excludes objects to exclude from lighting
     * @param replace replace or multiply original vertex color
     */
    public static void bakeSun(prpfile prp, String[] excludes, boolean replace)
    {
        if (prp.FindAllObjectsOfType(Typeid.plPostEffectMod).length != 0)
            // simple, quick, and a bit hacky
            return;
        
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            
            boolean excludedOrHasWaveset = false;
            for (String objname: excludes)
                if (objname.equals(ro.header.desc.objectname.toString()))
                    // some objects (ie: sky) musn't be lit
                    excludedOrHasWaveset = true;
            for (Uruobjectref modif: so.modifiers)
                if (modif.xdesc.objecttype == Typeid.plWaveSet7)
                    // vertex color affects water shader params, not color itself.
                    excludedOrHasWaveset = true;
            if (excludedOrHasWaveset)
                continue;
            
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
                    plDrawableSpans.PlIcicle icicle = dspan.icicles[icicleindex];
                    plDrawableSpans.PlGBufferGroup group = dspan.groups[icicle.parent.groupIdx];
                    plDrawableSpans.PlGBufferGroup.SubMesh subgroup = group.submeshes[icicle.parent.VBufferIdx];
                    
                    DrawableSpansEncoders.DecompressedPotsVertices verts = subgroup.decompressAllAndSave(group.fformat);
                    
                    if (l2w == null)
                        if (icicle.parent.parent.localToWorld.isnotIdentity == 1)
                            l2w = icicle.parent.parent.localToWorld;
                    if (l2w != null)
                    {
                        l2w = l2w.deepClone();
                        l2w.setelement(0, 3, 0); // remove translation, since we only want normal scale
                        l2w.setelement(1, 3, 0);
                        l2w.setelement(2, 3, 0);
                    }
                    
                    for (int i=icicle.parent.VStartIdx; i<(icicle.parent.VStartIdx+icicle.parent.VLength); i++)
                    {
                        /*
                        
                        -- Sunlight baking --
                        
                        First, assign the ambient color as vertex color
                        Then, find the intensity of the sun's ray on this vertex (vtx norm colinear with sunray but in opposite directions)
                        Add sunlight*intensity to vertex color
                        
                        */
                        
                        float intensity; float angle;
                        if (l2w == null)
                            angle = opti.acos(sunDirection.getDotProduct(opti.getNormalized(new Vector3(
                                verts.normxs[i],
                                verts.normys[i],
                                verts.normzs[i]
                            ))));
                        else
                            angle = opti.acos(sunDirection.getDotProduct(opti.getNormalized(l2w.mult(new Vertex(
                                verts.normxs[i],
                                verts.normys[i],
                                verts.normzs[i]
                            )).getVector3())));
                        intensity = angle / (float) PI * 2 - 1;
                        
                        // /!\ Do NOT negate the resulting intensity: we need vectors in opposite directions (dot product = -1)
                        //     That's because we make the light direction dot the normal.
                        if (intensity < 0) intensity = 0; // vertex already has no light after angle>90, don't remove anymore light.
                        
                        float newr = ambientColor[0] + directColor[0]*intensity;
                        float newg = ambientColor[1] + directColor[1]*intensity;
                        float newb = ambientColor[2] + directColor[2]*intensity;
                        
                        if (newr > 1) newr = 1;
                        if (newg > 1) newg = 1;
                        if (newb > 1) newb = 1;
                        
                        if (replace)
                        {
                            verts.reds[i] =     (byte) (255*(newr));
                            verts.greens[i] =   (byte) (255*(newg));
                            verts.blues[i] =    (byte) (255*(newb));
                        }
                        else
                        {
                            verts.reds[i] =     (byte) (255*(newr * ((int) verts.reds[i] & 0xff)/255f));
                            verts.greens[i] =   (byte) (255*(newg * ((int) verts.greens[i] & 0xff)/255f));
                            verts.blues[i] =    (byte) (255*(newb * ((int) verts.blues[i] & 0xff)/255f));
                        }
                    }
                    subgroup.setWithDecompressedPotsVerts(verts);
                }
            }
        }
    }
    
    
    
    /**
     * Bake AO to vertices. Using only faces.
     * 
     * @param prp the prp to read
     * @param excludes objects that should not be processed
     * @param replace replace original vertex color
     */
    public static void bakeAmbientOcclusion(prpfile prp, String[] excludes, boolean replace)
    {
        if (prp.FindAllObjectsOfType(Typeid.plPostEffectMod).length != 0)
            return;
        
        LinkedList<simpleobject> objects = new LinkedList();
        
        m.msg("    (Loading faces...)");
        
        // REGISTER ALL FACES FROM ALL OBJECTS OF THE WHOLE SCENE, WITH THEIR POSITION, NORMAL AND AREA //
        // (not too slow, but may take some memory)
        
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            
            boolean excludedOrHasWaveset = false;
            for (String objname: excludes)
                if (objname.equals(ro.header.desc.objectname.toString()))
                    // some objects (ie: sky) musn't be lit
                    excludedOrHasWaveset = true;
            for (Uruobjectref modif: so.modifiers)
                if (modif.xdesc.objecttype == Typeid.plWaveSet7)
                    // vertex color affects water shader params, not color itself.
                    excludedOrHasWaveset = true;
            if (excludedOrHasWaveset)
                continue;
            
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
                plDrawableSpans dspan = dspanro.castTo();
                plDrawableSpans.PlDISpanIndex dispanindex = dspan.DIIndices[digroup.subsetgroupindex];
                for (int icicleindex: dispanindex.indices)
                {
                    simpleobject obj = new simpleobject();
                    
                    plDrawableSpans.PlIcicle icicle = dspan.icicles[icicleindex];
                    plDrawableSpans.PlGBufferGroup group = dspan.groups[icicle.parent.groupIdx];
                    plDrawableSpans.PlGBufferGroup.SubMesh subgroup = group.submeshes[icicle.parent.VBufferIdx];
                    
                    DrawableSpansEncoders.DecompressedPotsVertices verts = subgroup.decompressAllAndSave(group.fformat);
                    
                    ShortTriplet[] plfaces = group.surfaces[icicle.IBufferIdx].faces;
                    
                    obj.faces = new simpleface[icicle.ILength/3];
                    
                    if (l2w == null)
                        if (icicle.parent.parent.localToWorld.isnotIdentity == 1)
                            l2w = icicle.parent.parent.localToWorld;
                    if (l2w != null)
                    {
                        l2w = l2w.deepClone();
                        Transmatrix l2wn = l2w.deepClone(); // used to rotate normals
                        l2wn.setelement(0, 3, 0); // remove translation, since we only want normal scale
                        l2wn.setelement(1, 3, 0);
                        l2wn.setelement(2, 3, 0);
                    }
                    
                    int j=0;
                    for (int i=icicle.IStartIdx/3; i<(icicle.IStartIdx+icicle.ILength)/3; i++, j++)
                    {
                        simpleface f = new simpleface();
                        
                        Vector3 v1, v2, v3;
                        /*if (l2w != null)
                        {
                            v1 = l2w.mult(new Vertex(verts.xs[plfaces[i].p], verts.ys[plfaces[i].p], verts.zs[plfaces[i].p])).getVector3();
                            v2 = l2w.mult(new Vertex(verts.xs[plfaces[i].q], verts.ys[plfaces[i].q], verts.zs[plfaces[i].q])).getVector3();
                            v3 = l2w.mult(new Vertex(verts.xs[plfaces[i].r], verts.ys[plfaces[i].r], verts.zs[plfaces[i].r])).getVector3();
                        }
                        else
                        { //*/
                            v1 = new Vector3(verts.xs[plfaces[i].p], verts.ys[plfaces[i].p], verts.zs[plfaces[i].p]);
                            v2 = new Vector3(verts.xs[plfaces[i].q], verts.ys[plfaces[i].q], verts.zs[plfaces[i].q]);
                            v3 = new Vector3(verts.xs[plfaces[i].r], verts.ys[plfaces[i].r], verts.zs[plfaces[i].r]);
                        //}
                        f.center = new Vector3(
                                                (v1.x + v2.x + v3.x)/3f,
                                                (v1.y + v2.y + v3.y)/3f,
                                                (v1.z + v2.z + v3.z)/3f
                                            );
                        /*if (l2w != null)
                        {
                            f.normal = opti.getNormalized(new Vector3(
                                                (verts.normxs[plfaces[i].p] + verts.normxs[plfaces[i].q] + verts.normxs[plfaces[i].r]) / 3f,
                                                (verts.normys[plfaces[i].p] + verts.normys[plfaces[i].q] + verts.normys[plfaces[i].r]) / 3f,
                                                (verts.normzs[plfaces[i].p] + verts.normzs[plfaces[i].q] + verts.normzs[plfaces[i].r]) / 3f
                                            ));
                            
                            short nx1 = verts.normxs[plfaces[i].p];
                        }
                        else //*/
                            f.normal = opti.getNormalized(new Vector3(
                                                (verts.normxs[plfaces[i].p] + verts.normxs[plfaces[i].q] + verts.normxs[plfaces[i].r]) / 3f,
                                                (verts.normys[plfaces[i].p] + verts.normys[plfaces[i].q] + verts.normys[plfaces[i].r]) / 3f,
                                                (verts.normzs[plfaces[i].p] + verts.normzs[plfaces[i].q] + verts.normzs[plfaces[i].r]) / 3f
                                            ));
                        
                        Vector3 edge1 = v1.getSubtract(v2);
                        Vector3 edge2 = v1.getSubtract(v3);
                        Vector3 edge3 = v2.getSubtract(v3);
                        
                        float a = opti.getLength(edge1);
                        float b = opti.getLength(edge2);
                        float c = opti.getLength(edge3);
                        float s = (a + b + c) / 2f;
                        
                        f.area = (float) opti.sqrt( s * (s-a) * (s-b) * (s-c) );
                        
                        
                        if (f.center.x > obj.boundX)
                            obj.boundX = f.center.x;
                        if (f.center.x < obj.boundMX)
                            obj.boundMX = f.center.x;
                        if (f.center.y > obj.boundY)
                            obj.boundY = f.center.y;
                        if (f.center.y < obj.boundMY)
                            obj.boundMY = f.center.y;
                        if (f.center.z > obj.boundZ)
                            obj.boundZ = f.center.z;
                        if (f.center.z < obj.boundMZ)
                            obj.boundMZ = f.center.z;
                        
                        
                        obj.faces[j] = f;
                    }
                    
                    obj.boundX  += aoReach;
                    obj.boundMX -= aoReach;
                    obj.boundY  += aoReach;
                    obj.boundMY -= aoReach;
                    obj.boundZ  += aoReach;
                    obj.boundMZ -= aoReach;
                    
                    objects.add(obj);
                }
            }
        }
        
        m.msg("    (Applying AO to vertices)");
        
        // APPLY OCCLUSION FROM ALL FACES TO ALL VERTICES IN THE SCENE //
        // (slower, processing time increases with aoReach)
        
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            
            boolean excludedOrHasWaveset = false;
            for (String objname: excludes)
                if (objname.equals(ro.header.desc.objectname.toString()))
                    // some objects (ie: sky) musn't be lit
                    excludedOrHasWaveset = true;
            for (Uruobjectref modif: so.modifiers)
                if (modif.xdesc.objecttype == Typeid.plWaveSet7)
                    // vertex color affects water shader params, not color itself.
                    excludedOrHasWaveset = true;
            if (excludedOrHasWaveset)
                continue;
            
            if (so.drawinterface == null || !so.drawinterface.hasref())
                // if it isn't a visual object, we can't process it
                continue;
            
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
                    plDrawableSpans.PlIcicle icicle = dspan.icicles[icicleindex];
                    plDrawableSpans.PlGBufferGroup group = dspan.groups[icicle.parent.groupIdx];
                    plDrawableSpans.PlGBufferGroup.SubMesh subgroup = group.submeshes[icicle.parent.VBufferIdx];
                    
                    DrawableSpansEncoders.DecompressedPotsVertices verts = subgroup.decompressAllAndSave(group.fformat);
                    
                    for (int i=icicle.parent.VStartIdx; i<(icicle.parent.VStartIdx+icicle.parent.VLength); i++)
                    {
                        Vector3 vpos = new Vector3 (
                                verts.xs[i],
                                verts.ys[i],
                                verts.zs[i]
                        );
                        Vector3 vnorm = opti.getNormalized(new Vector3 (
                                verts.normxs[i],
                                verts.normys[i],
                                verts.normzs[i]
                        ));
                        
                        float colIntensity = 1.0f;
                        
                        for (simpleobject object: objects)
                        {
                            if (
                                    object.boundX   < vpos.x ||
                                    object.boundMX  > vpos.x ||
                                    object.boundY   < vpos.y ||
                                    object.boundMY  > vpos.y ||
                                    object.boundZ   < vpos.z ||
                                    object.boundMZ  > vpos.z
                                )
                                continue;
                            
                            for (simpleface f: object.faces)
                            {
                                Vector3 fromVtxToFace = f.center.getSubtract(vpos);

                                /*

                                --- AO calculation per-vertex ---

                                factors:
                                    distanceFactor  = 1 - dist(vertex, face) / aoReach → how close the face is to the vertex
                                    angleFactor     = norm dot vtxtoface → whether the face is on the vertex' normal path
                                    normalFactor    = -norm dot facenorm → whether the vextex is on the face normal's path
                                    areaFactor      = f.area * aoIntensity * .005 → how big the face is, and how much occlusion it produces. 1 face of area 100 produces about as much occlusion as 10 of area 10 (not exactly, but close enough)

                                */


                                float fvtflen = opti.getLength(fromVtxToFace);
                                fromVtxToFace = opti.getNormalized(fromVtxToFace);

                                float distanceFactor = 1 - fvtflen / aoReach;
                                if (distanceFactor <= 0) continue; // face too far away - this was a good speedup before using bounding box for objects

                                float angleFactor;
                                angleFactor = (float) (1 - opti.acos (vnorm.getDotProduct(fromVtxToFace)) / PI * 2);
                                if (angleFactor <= 0) continue; // face not aligned with vertex

                                float normalFactor;
                                normalFactor = (float) (opti.acos (f.normal.getDotProduct(fromVtxToFace)) / PI * 2 - 1);
                                if (normalFactor <= 0) continue; // vertex not aligned with face

                                float areaFactor = (float) (f.area * aoIntensity * .005);

                                float fullintensity = distanceFactor * angleFactor * normalFactor * areaFactor;

                                colIntensity -= fullintensity;

                                // don't forget clamping
                                if (colIntensity < 0)
                                    break; // might speed things up.
                            }
                            if (colIntensity < 0)
                            {
                                colIntensity = 0;
                                break;
                            }
                        }
                        
//                        float rInt = colIntensity * (1f-minAOColor[0]);
//                        float gInt = colIntensity * (1f-minAOColor[1]);
//                        float bInt = colIntensity * (1f-minAOColor[2]);
                        float rInt = 1 - ( (1-colIntensity) * (1-minAOColor[0]) );
                        float gInt = 1 - ( (1-colIntensity) * (1-minAOColor[1]) );
                        float bInt = 1 - ( (1-colIntensity) * (1-minAOColor[2]) );
                        
                        if (!replace)
                        {
                            rInt *= ((int) verts.reds[i] & 0xff)   / 255f;
                            gInt *= ((int) verts.greens[i] & 0xff) / 255f;
                            bInt *= ((int) verts.blues[i] & 0xff)  / 255f;
                        }
                        
                        if (rInt < minAOColor[0])
                            rInt = minAOColor[0];
                        if (gInt < minAOColor[1])
                            gInt = minAOColor[1];
                        if (bInt < minAOColor[2])
                            bInt = minAOColor[2];
                        
                        
                        verts.reds[i] = (byte) (255f * rInt);
                        verts.greens[i] = (byte) (255f * gInt);
                        verts.blues[i] = (byte) (255f * bInt);
                    }
                    subgroup.setWithDecompressedPotsVerts(verts);
                }
            }
        }
    }
    
    
    /**
     * Get list of objects that are part of a GUI and should not be lit.
     * We could simply abort as soon as we find a plPostEffectMod in the PRP.
     * @param prp the prp to find
     * @param excludes list of objects to be expanded
     */
    public static void excludeGUI(prpfile prp, ArrayList<String> excludes)
    {
        for (PrpRootObject pemro: prp.FindAllObjectsOfType(Typeid.plPostEffectMod))
        {
            plPostEffectMod pem = pemro.castTo();
            x0000Scenenode scenenode = prp.findObjectWithDesc(pem.ref.xdesc).castTo();
            for (Uruobjectref obj: scenenode.objectrefs1)
                excludes.add(obj.xdesc.objectname.toString());
            for (Uruobjectref obj: scenenode.objectrefs2)
                excludes.add(obj.xdesc.objectname.toString());
        }
    }
    
    
    
    /**
     * Helper class for storing face informations.
     */
    public static class simpleface
    {
        public float area=0f;
        public Vector3 center;
        public Vector3 normal;
    }
    
    
    /**
     * Helper class for storing object bounding box and faces.
     */
    public static class simpleobject
    {
        public simpleface[] faces;
        public float boundX=0;
        public float boundMX=0;
        public float boundY=0;
        public float boundMY=0;
        public float boundZ=0;
        public float boundMZ=0;
    }
    
    
    
    /**
     * Removes all textures from this PRP (useful to see baking results in Ages with per-page textures).
     * @param prp the prp to process
     */
    public static void stripTextures(prpfile prp)
    {
        for (PrpRootObject texro: prp.FindAllObjectsOfType(Typeid.plMipMap))
            prp.markObjectDeleted(Uruobjectref.createFromUruobjectdesc(texro.header.desc), true);
    }
    
    
    public static ArrayList<Float> getScale(Transmatrix co)
    {
        ArrayList<Float> scale = new ArrayList();
        scale.add(1f); scale.add(1f); scale.add(1f);
        
        if (co != null && co.isnotIdentity == 1) // that's the one used by PyPRP, should be correct
        {
            Flt[][] values = co.convertToFltArray();
            Vector3 v1 = new Vector3(values[0][0].toJavaFloat(), values[0][1].toJavaFloat(), values[0][2].toJavaFloat());
            Vector3 v2 = new Vector3(values[1][0].toJavaFloat(), values[1][1].toJavaFloat(), values[1][2].toJavaFloat());
            Vector3 v3 = new Vector3(values[2][0].toJavaFloat(), values[2][1].toJavaFloat(), values[2][2].toJavaFloat());
            scale.set(0, v1.getLength());
            scale.set(1, v2.getLength());
            scale.set(2, v3.getLength());
        }
        return scale;
    }
    
    public static void bakeSunSimple(String fname, String[] excludes, String outfolder)
    {
        AutoMod_Light.setMathInterface(2);
        m.msg("Loading file " + fname + "...");
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(fname, true);

        m.msg("Calculating lights for this file...");
        AutoMod_Light.bakeSun(prp, excludes, true);

        m.msg("Saving file to " + outfolder + "...");
        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
        m.msg("Done !");
    }
    
    public static void bakeSunSimpleAge(String inputfolder, String outfolder, String[] excludes, String ageName)
    {
        AutoMod_Light.setMathInterface(2);
        m.msg("Baking lights for ALL of " + ageName + "'s prps in this directory...");
        long starttime = gettime();
        File folder1 = new File(inputfolder);

        File[] folder1children = folder1.listFiles();
        int i=0;
        for(File child1: folder1children)
        {
            if (child1.getName().startsWith(ageName+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
            {
                m.msg("  Loading " + child1.getName() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                m.msg("  Baking...");
                AutoMod_Light.bakeSun(prp, excludes, true);

                m.msg("  Saving...");
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
                i++;
            }
        }
        m.msg("Done baking " + ageName + "'s " + i + " PRPs in " + (gettime()-starttime)/1000f + " seconds !");
    }
    
    public static void bakeAOSimple(String fname, String[] excludes, String outfolder)
    {
        AutoMod_Light.setMathInterface(2);
        long starttime = gettime();
        m.msg("Loading file " + fname + "...");
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(fname, true);

        m.msg("Calculating AO for this file... this may take a while.");
        AutoMod_Light.bakeAmbientOcclusion(prp, excludes, true);

        m.msg("Saving file to " + outfolder + "...");
        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
        m.msg("Done !");
        m.msg("in " + (gettime()-starttime)/1000f + " seconds !");
    }
    
    public static void bakeAOSimpleAge(String inputfolder, String outfolder, String[] excludes, String ageName)
    {
        AutoMod_Light.setMathInterface(2);
        m.msg("Baking ambient occlusion for ALL of " + ageName + "'s prps in this directory...");
        long starttime = gettime();
        File folder1 = new File(inputfolder);

        File[] folder1children = folder1.listFiles();
        int i=0;
        for(File child1: folder1children)
        {
            if (child1.getName().startsWith(ageName+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
            {
                m.msg("  Loading " + child1.getName() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                m.msg("  Baking...");
                AutoMod_Light.bakeAmbientOcclusion(prp, excludes, true);

                m.msg("  Saving...");
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
                i++;
            }
        }
        m.msg("Done baking " + ageName + "'s " + i + " PRPs in " + (gettime()-starttime)/1000f + " seconds !");
    }
    
    public static void bakeAllSimpleAge(String inputfolder, String outfolder, String[] excludes, String ageName)
    {
        AutoMod_Light.setMathInterface(2);
        m.msg("Baking light & ambient occlusion for ALL of " + ageName + "'s prps in this directory...");
        long starttime = gettime();
        File folder1 = new File(inputfolder);

        File[] folder1children = folder1.listFiles();
        int i=0;
        for(File child1: folder1children)
        {
            if (child1.getName().startsWith(ageName+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
            {
                m.msg("  Loading " + child1.getName() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                m.msg("  Baking sun...");
                AutoMod_Light.bakeSun(prp, excludes, true);
                m.msg("  Baking AO...");
                AutoMod_Light.bakeAmbientOcclusion(prp, excludes, false);

                m.msg("  Saving...");
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
                i++;
            }
        }
        m.msg("Done baking " + ageName + "'s " + i + " PRPs in " + (gettime()-starttime)/1000f + " seconds !");
    }
    
    public static void stripTexturesSimple(String infilename, String outfolder)
    {
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(infilename, true);

        m.msg("Stripping file from its textures...");
        AutoMod_Light.stripTextures(prp);
        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
    }

    public static void stripAgeTexturesSimple(String infilename, String outfolder, String ageName)
    {
        File folder1 = new File(infilename);

        File[] folder1children = folder1.listFiles();
        for(File child1: folder1children)
        {
            if (child1.getName().startsWith(ageName+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
            {
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                AutoMod_Light.stripTextures(prp);
                String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                prp.saveAsFile(outputfilename);
            }
        }
    }

    /*
    / * *
     * Attempts to fix stupid normals, which for some reason get exported by PyPRP.
     * @param prp 
     * /
    public static void renormal(prpfile prp)
    {
        int corrected = 0;
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = ro.castTo();
            
            if (so.drawinterface == null || !so.drawinterface.hasref())
                // if it isn't a visual object, we can't process it
                continue;
            
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
                    plDrawableSpans.PlIcicle icicle = dspan.icicles[icicleindex];
                    plDrawableSpans.PlGBufferGroup group = dspan.groups[icicle.parent.groupIdx];
                    plDrawableSpans.PlGBufferGroup.SubMesh subgroup = group.submeshes[icicle.parent.VBufferIdx];
                    
                    DrawableSpansEncoders.DecompressedPotsVertices verts = subgroup.decompressAllAndSave(group.fformat);
                    ShortTriplet[] plfaces = group.surfaces[icicle.IBufferIdx].faces;
                    for (ShortTriplet face: plfaces)
                    {
                        float ax = verts.normxs[face.p] / 32767f;
                        float ay = verts.normxs[face.p] / 32767f;
                        float az = verts.normxs[face.p] / 32767f;
                        float bx = verts.normxs[face.q] / 32767f;
                        float by = verts.normxs[face.q] / 32767f;
                        float bz = verts.normxs[face.q] / 32767f;
                        float cx = verts.normxs[face.r] / 32767f;
                        float cy = verts.normxs[face.r] / 32767f;
                        float cz = verts.normxs[face.r] / 32767f;
                        
                        if (
                                abs(ax) == 1f ||
                                abs(ay) == 1f ||
                                abs(az) == 1f ||
                                abs(bx) == 1f ||
                                abs(by) == 1f ||
                                abs(bz) == 1f ||
                                abs(cx) == 1f ||
                                abs(cy) == 1f ||
                                abs(cz) == 1f
                                )
                        {
                            corrected++;
                            // TODO
                        }
                    }
                }
            }
        }
    }
    
    //*/

}

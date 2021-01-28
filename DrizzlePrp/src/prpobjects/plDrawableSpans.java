/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import java.util.Vector;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayDeque;

/**
 *
 * @author user
 */
public class plDrawableSpans extends uruobj
{
    //Objheader xheader;
    //PlSynchedObject parent;
    public int props; //unknown
    public int renderLevel; //zbias
    public int criteria; //blendflags
    public int materialsCount;
    //public Uruobjectref[] materials;
    public Vector<Uruobjectref> materials = new Vector();
    public int icicleCount; //subsetcount
    public PlIcicle[] icicles; //subsets
    public int unused;
    public int spanCount; //listcount
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

    static final int kSpanTypeMask = 0xC0000000;
    static final int kSpanIDMask = 0x3FFFFFFF;
    static final int kSpanTypeIcicle = 0x00000000;
    static final int kSpanTypeUnknown = 0x80000000;
    static final int kSpanTypeParticleSpan = 0xC0000000;
    public Vector<PlIcicle> spans = new Vector(); //should really allow any PlSpans, in practice, just all of the icicles, I think.
    public int oldiciclecount;
    
    public plDrawableSpans(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        //parent = new PlSynchedObject(c); //should this be a keyedobject??? No, it shouldn't!
        props = data.readInt();
        
        renderLevel = data.readInt();
        criteria = data.readInt();
        materialsCount = data.readInt(); //so far so good.
        materials = c.readVector(Uruobjectref.class,materialsCount);
        //for(Uruobjectref mat: materials)
        //{
        //    m.msg("material: "+mat.toString());
        //}
        //materials = new Uruobjectref[matcount];
        /*for(int i=0;i<matcount;i++)
        {
            materials[i] = new Uruobjectref(c);
            //TODO: remove the next block, it's a test hack!
            //if(xheader.desc.objectname.toString().equals("EderDelin_garden_00000000_0Spans"))
            //{
            //    if(i==17 || i==24 || i==34) materials[i] = materials[0];
            //}
            if(materials[i].hasref() && materials[i].xdesc.objectname.toString().toLowerCase().startsWith("watercurrent"))
            {
                int dummy=0;
            }
        }*/
        
        icicleCount = data.readInt();
        icicles = c.readArray(PlIcicle.class,icicleCount);
        /*if(c.readversion==7)
        {
            //set icicle values for missing hexisle vars.
            int curStartIdx = 0;
            int curGroupIdx = 0;
            for(int i=0;i<icicles.length;i++)
            {
                PlIcicle icicle = icicles[i];
                if(i==0) curGroupIdx = icicle.parent.groupIdx;
                if(icicle.parent.groupIdx!=curGroupIdx)
                {
                    //a new group...
                    int dummy=0;
                }
                if(icicle.IBufferIdx!=0)
                {
                    int dummy=0;
                }
                icicle.IBufferIdx = 0;  //always 0 in Pots anyway, so they probably just removed it.
                if(icicle.IStartIdx!=curStartIdx)
                {
                    int dummy=0;
                }
                icicle.IStartIdx = curStartIdx;
                curStartIdx += icicle.ILength; //they just go in order, so this should work.
            }
        }*/
        for(int i=0;i<icicleCount;i++)
        {
            if(icicles[i].parent.parent.materialindex==58)
            {
                int breakdummy = 0;
            }
        }
        if(c.readversion==7)
        {
            //Hexisle doesn't have this field, but it is always 0 in Pots anyway.
            unused = 0;
        }
        else
        {
            unused = data.readInt(); e.ensure(unused==0);
        }
        spanCount = data.readInt(); e.ensure(spanCount==icicleCount);//so far so good.
        //spanSourceIndices = data.readInts(spanCount);
        spanSourceIndices = new int[spanCount];
        for(int i=0;i<spanCount;i++)
        {
            spanSourceIndices[i] = c.readInt();
            if((spanSourceIndices[i]&kSpanTypeMask)==kSpanTypeIcicle)
            {
                int iciIdx = spanSourceIndices[i]&kSpanIDMask;
                if(iciIdx!=i)
                {
                    m.warn("icicles and spans don't align.");
                }
                spans.add(icicles[iciIdx]);
            }
            else if((spanSourceIndices[i]&kSpanTypeMask)==kSpanTypeParticleSpan)
            {
                throw new shared.uncaughtexception("Unhandled case in PlDrawableSpans");
            }
        }

        //unused3 = data.readBytes(subsetcount); //should all be zero. see pyprp.
        fogEnvironmentRefs = new Uruobjectref[icicleCount];
        for(int i=0;i<icicleCount;i++)
        {
            fogEnvironmentRefs[i] = new Uruobjectref(c);
        }
        
        /*if(c.readversion==7)
        {
            //2 uruobjectref vectors here, but apparently they don't get called.
            //c.readInt();
            //c.readInt();
        }*/
        
        if(icicleCount>0)
        {
            //xboundingBoxes = c.readArray(BoundingBox.class,3);
            xLocalBounds = new BoundingBox(c);
            xWorldBounds = new BoundingBox(c);
            xMaxWorldBounds = new BoundingBox(c);
        }

        //lightcount = data.readInt();
        /*LightInfo newLightInfo;
        do
        {
            newLightInfo = data.readObj();
            lightinfos.add(newLightInfo);
        }
        while(newLightInfo.lightcount > 0);*/
        //The lightinfos is read very differently in pyprp, the final int is actually part of another structure, which happens to always? be zero.
        //lightinfos = new GrowVector<Uruobjectref>(Uruobjectref.class, c);
        for(int i=0;i<spanCount;i++)
        {
            if((spans.get(i).parent.parent.props&PlSpan.kPropHasPermaLights)!=0)
            {
                if(spans.get(i).parent.parent.permaLights!=null) throw new shared.uncaughtexception("Unhandled permalights.");
                spans.get(i).parent.parent.permaLightsCount = c.readInt();
                spans.get(i).parent.parent.permaLights = c.readArray(Uruobjectref.class, spans.get(i).parent.parent.permaLightsCount);
            }
            if((spans.get(i).parent.parent.props&PlSpan.kPropHasPermaProjs)!=0)
            {
                if(spans.get(i).parent.parent.permaProjs!=null) throw new shared.uncaughtexception("Unhandled permaprojs.");
                spans.get(i).parent.parent.permaProjsCount = c.readInt();
                spans.get(i).parent.parent.permaProjs = c.readArray(Uruobjectref.class, spans.get(i).parent.parent.permaProjsCount);
            }
        }

        sourceSpanCount = c.readInt();
        if(sourceSpanCount!=0)
        {
            throw new shared.uncaughtexception("Unhandled sourceSpans.");
        }
        
        matrixsetcount = data.readInt();
        //localToWorlds = c.readArray(Transmatrix.class,matrixsetcount);
        //worldToLocals = c.readArray(Transmatrix.class,matrixsetcount);
        //localToBones = c.readArray(Transmatrix.class,matrixsetcount);
        //boneToLocals = c.readArray(Transmatrix.class,matrixsetcount);
        localToWorlds = new Transmatrix[matrixsetcount];
        worldToLocals  = new Transmatrix[matrixsetcount];
        localToBones = new Transmatrix[matrixsetcount];
        boneToLocals = new Transmatrix[matrixsetcount];
        for(int i=0;i<matrixsetcount;i++)
        {
            localToWorlds[i] = new Transmatrix(c);
            worldToLocals[i] = new Transmatrix(c);
            localToBones[i] = new Transmatrix(c);
            boneToLocals[i] = new Transmatrix(c);
        }

        DIIndicesCount = data.readInt(); //hexisle line 346
        DIIndices = c.readArray(PlDISpanIndex.class,DIIndicesCount); //fDIIndices, hexisle starts at line 386

        meshcount = data.readInt(); //so far so good.
        if(c.readversion==7)
        {
            groups = new PlGBufferGroup[meshcount];
            for(int i=0;i<groups.length;i++)
            {
                HexislePlDrawableSpans.HexislePlGBufferGroup higroup = new HexislePlDrawableSpans.HexislePlGBufferGroup(c);
                PlGBufferGroup group = higroup.convertToPotsPlGBufferGroup();
                groups[i] = group;
            }
        }
        else
        {
            groups = c.readArray(PlGBufferGroup.class,meshcount); //plGBufferGroups
        }

        embeddedtype = Typeid.Read(c);
        switch(embeddedtype)
        {
            case plSceneNode: //means null here.
                break;
            case nil:
                break;
            case plSpaceTree:
                xspacetree = new PlSpaceTree(c);
                break;
            default:
                m.msg("unknown type.");
                break;
        }
        scenenode = c.readObj(Uruobjectref.class);
        
    }

    
    public static class PlGBufferCell
    {
        int fVtxStart;
        int fColorStart;
        int fLength;
        
        public PlGBufferCell(context c)
        {
            fVtxStart = c.readInt();
            fColorStart = c.readInt();
            fLength = c.readInt();

            if(fVtxStart!=0)
            {
                int dummy=0;
            }
            if(fColorStart!=-1)
            {
                int dummy=0;
            }
        }
        public PlGBufferCell(int vtxstart, int colorstart, int length)
        {
            fVtxStart = vtxstart;
            fColorStart = colorstart;
            fLength = length;
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(fVtxStart);
            c.writeInt(fColorStart);
            c.writeInt(fLength);
        }
    }

    //this class is not finished...
    public static class modPlDrawableSpans
    {
        int props;
        int renderLevel;
        int criteria;
        Uruobjectref scenenode;
        BoundingBox xLocalBounds;
        BoundingBox xWorldBounds;
        BoundingBox xMaxWorldBounds;
        Transmatrix[] localToBones;
        Transmatrix[] boneToLocals;
        Transmatrix[] worldToLocals;
        Transmatrix[] localToWorlds;
        Uruobjectref[] fogEnvironmentRefs;
        //HashSet<PlGBufferGroup> buffergroups = new HashSet();
        HashSet<HashSet<modPlIcicle>> modicicles = new HashSet();
        //HashSet<Uruobjectref> materials = new HashSet();
        public static class modPlIcicle
        {
            PlIcicle icicle;
            Uruobjectref material;
            PlGBufferGroup buffergroup;
        }

        public void addFromSubsetgroupindex(plDrawableSpans span, int subsetGroupIndex)
        {
            if(subsetGroupIndex!=-1)
            {
                plDrawableSpans.PlDISpanIndex spanindex = span.DIIndices[subsetGroupIndex];
                HashSet<modPlIcicle> icicles = new HashSet();
                for(int subsetind: spanindex.indices)
                {
                    PlIcicle icicle = span.icicles[subsetind];
                    Uruobjectref material = span.materials.get(icicle.parent.parent.materialindex);
                    PlGBufferGroup bg = span.groups[icicle.parent.groupIdx];

                    modPlIcicle mi = new modPlIcicle();
                    mi.icicle = icicle.deepClone();
                    mi.material = material.deepClone();
                    mi.buffergroup = bg; //perhaps we should be cloning this, but I'm lazy :P
                    icicles.add(mi);
                }
                modicicles.add(icicles);
            }
        }
        public void copyDefaultsFromOtherDrawableSpans(plDrawableSpans span)
        {
            this.props = span.props;
            this.renderLevel = span.renderLevel;
            this.criteria = span.criteria;
            this.fogEnvironmentRefs = span.fogEnvironmentRefs;
            this.xLocalBounds = span.xLocalBounds;
            this.xWorldBounds = span.xWorldBounds;
            this.xMaxWorldBounds = span.xMaxWorldBounds;
            this.localToBones = span.localToBones;
            this.boneToLocals = span.boneToLocals;
            this.localToWorlds = span.localToWorlds;
            this.worldToLocals = span.worldToLocals;
            this.scenenode = span.scenenode;
        }
        public plDrawableSpans createDrawableSpans()
        {
            plDrawableSpans r = new plDrawableSpans();

            r.props = this.props;
            r.renderLevel = this.renderLevel;
            r.criteria = this.criteria;
            r.unused = 0;
            r.fogEnvironmentRefs = this.fogEnvironmentRefs;
            r.xLocalBounds = this.xLocalBounds;
            r.xWorldBounds = this.xWorldBounds;
            r.xMaxWorldBounds = this.xMaxWorldBounds;

            r.localToBones = this.localToBones;
            r.boneToLocals = this.boneToLocals;
            r.localToWorlds = this.localToWorlds;
            r.worldToLocals = this.worldToLocals;

            r.embeddedtype = Typeid.plSceneNode; //means null here.
            r.xspacetree = null;

            r.scenenode = this.scenenode;

            //generate the rest...
            m.err("not finished.");
            return r;
        }
    }
    public void compile(Bytedeque data)
    {
        //parent.compile(data);
        data.writeInt(props);
        data.writeInt(renderLevel);
        data.writeInt(criteria);
        data.writeInt(materialsCount);
        //data.writeArray2(materials);
        data.writeVector2(materials);
        data.writeInt(icicleCount);
        data.writeArray2(icicles);
        data.writeInt(unused);
        data.writeInt(spanCount);
        data.writeInts(spanSourceIndices);
        //data.writeBytes(unused3);
        data.writeArray2(fogEnvironmentRefs);
        if(icicleCount>0)
        {
            //data.writeArray(xboundingBoxes);
            xLocalBounds.compile(data);
            xWorldBounds.compile(data);
            xMaxWorldBounds.compile(data);
        }
        //lightinfos.compile(data);
        for(int i=0;i<spanCount;i++)
        {
            if((spans.get(i).parent.parent.props&PlSpan.kPropHasPermaLights)!=0)
            {
                data.writeInt(spans.get(i).parent.parent.permaLightsCount);
                data.writeArray2(spans.get(i).parent.parent.permaLights);
            }
            if((spans.get(i).parent.parent.props&PlSpan.kPropHasPermaProjs)!=0)
            {
                data.writeInt(spans.get(i).parent.parent.permaProjsCount);
                data.writeArray2(spans.get(i).parent.parent.permaProjs);
            }
        }

        data.writeInt(sourceSpanCount);

        data.writeInt(matrixsetcount);
        //data.writeArray2(localToWorlds);
        //data.writeArray2(worldToLocals);
        //data.writeArray2(localToBones);
        //data.writeArray2(boneToLocals);
        for(int i=0;i<matrixsetcount;i++)
        {

            localToWorlds[i].compile(data);
            worldToLocals[i].compile(data);
            localToBones[i].compile(data);
            boneToLocals[i].compile(data);
        }

        data.writeInt(DIIndicesCount);
        data.writeArray2(DIIndices);
        data.writeInt(meshcount);
        data.writeArray2(groups);
        //TODO: remove next line, it's a hack.
        //embeddedtype = Typeid.nil;
        embeddedtype.compile(data);
        
        switch(embeddedtype)
        {
            case nil:
                break;
            case plSpaceTree:
                xspacetree.compile(data);
                break;
            default:
                m.throwUncaughtException("unknown type.");
                break;
        }
        scenenode.compile(data);
    }
    public plDrawableSpans(){}
    public int addMaterial(Uruobjectref mat)
    {
        materials.add(mat);
        materialsCount++;
        return materialsCount-1;
    }
    static public class PlSpan extends uruobj
    {
        public int subType; //visible
        public int materialindex;
        public Transmatrix localToWorld; //was transforms1
        public Transmatrix worldToLocal; //was transforms2
        public int props; //lightingflags
        public BoundingBox localBounds; //was uegclassesca1
        public BoundingBox worldBounds; //was uegclassesca2
        public int numMatrices; //was blendflag
        public int baseMatrix; //was blendindex
        public short localUVWChans; //u1
        public short maxBoneIdx; //u2
        public short penBoneIdx; //u3
        public Flt minDist; //u4
        public Flt maxDist; //u5
        public Flt waterHeight; //xu6

        public static int kLiteMaterial = 0x0;
        public static int kPropNoDraw = 0x1;
        public static int kPropNoShadowCast = 0x2;
        public static int kPropFacesSortable = 0x4;
        public static int kPropVolatile = 0x8;
        public static int kWaterHeight = 0x10;
        public static int kPropRunTimeLight = 0x20;
        public static int kPropReverseSort = 0x40;
        public static int kPropHasPermaLights = 0x80;
        public static int kPropHasPermaProjs = 0x100;
        public static int kLiteVtxPreshaded = 0x200;
        public static int kLiteVtxNonPreshaded = 0x400;
        public static int kLiteProjection = 0x800;
        public static int kLiteShadowErase = 0x1000;
        public static int kLiteShadow = 0x2000;
        public static int kPropMatHasSpecular = 0x10000;
        public static int kPropProjAsVtx = 0x20000;
        public static int kPropSkipProjection = 0x40000;
        public static int kPropNoShadow = 0x80000;
        public static int kPropForceShadow = 0x100000;
        public static int kPropDisableNormal = 0x200000;
        public static int kPropCharacter = 0x400000;
        public static int kPartialSort = 0x800000;
        public static int kVisLOS = 0x1000000;

        //do not compile these:
        public int permaLightsCount;
        public Uruobjectref[] permaLights;// = new Vector();
        public int permaProjsCount;
        public Uruobjectref[] permaProjs;// = new Vector();


        public PlSpan(context c) throws readexception
        {
            subType = c.readInt(); e.ensureflags(subType,1,0); //is sometimes 0 in hex isle.
            materialindex = c.readInt();
            if(materialindex==0 || materialindex==3)
            {
                if(c.curRootObject.objectname.toString().toLowerCase().startsWith("minkata_minkexteriorday_4000000c_2blendspans"))
                {
                    int breakdummy= 0;
                }
            }
            localToWorld = c.readObj(Transmatrix.class);
            worldToLocal = c.readObj(Transmatrix.class);
            props = c.readInt(); //props
            localBounds = c.readObj(BoundingBox.class);
            worldBounds = c.readObj(BoundingBox.class);
            numMatrices = c.readInt();
            baseMatrix = c.readInt();
            localUVWChans = c.readShort();
            maxBoneIdx = c.readShort();
            penBoneIdx = c.readShort();
            minDist = c.readObj(Flt.class);
            maxDist = c.readObj(Flt.class); //so far so good
            if((props & 0x10)!=0)
            {
                waterHeight = c.readObj(Flt.class);
            }
        }

        public static PlSpan createEmpty()
        {
            PlSpan r = new PlSpan();
            return r;
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(subType);
            c.writeInt(materialindex);
            localToWorld.compile(c);
            worldToLocal.compile(c);
            c.writeInt(props);
            localBounds.compile(c);
            worldBounds.compile(c);
            c.writeInt(numMatrices);
            c.writeInt(baseMatrix);
            c.writeShort(localUVWChans);
            c.writeShort(maxBoneIdx);
            c.writeShort(penBoneIdx);
            minDist.compile(c);
            maxDist.compile(c);
            if((props & 0x10)!=0)
            {
                waterHeight.compile(c);
            }
        }
        PlSpan(){}
        public PlSpan deepClone()
        {
            PlSpan r = new PlSpan();
            r.subType = this.subType;
            r.materialindex = this.materialindex;
            r.localToWorld = this.localToWorld.deepClone();
            r.worldToLocal = this.worldToLocal.deepClone();
            r.props = this.props;
            r.localBounds = this.localBounds.deepClone();
            r.worldBounds = this.worldBounds.deepClone();
            r.numMatrices = this.numMatrices;
            r.baseMatrix = this.baseMatrix;
            r.localUVWChans = this.localUVWChans;
            r.maxBoneIdx = this.maxBoneIdx;
            r.penBoneIdx = this.penBoneIdx;
            r.minDist = this.minDist.deepClone();
            r.maxDist = this.maxDist.deepClone();
            r.waterHeight = this.waterHeight.deepClone();
            return r;
        }
    }
    static public class PlVertexSpan extends uruobj
    {
        public PlSpan parent;
        public int groupIdx; //meshindex
        public int VBufferIdx; //unused1
        public int cellIdx; //unused2
        public int cellOffset; //vertexstart1
        public int VStartIdx; //vertexstart2
        public int VLength; //vertexcount

        public PlVertexSpan(context c) throws readexception
        {
            parent = new PlSpan(c);
            groupIdx = c.readInt();
            VBufferIdx = c.readInt();
            if(c.readversion==7)
            {
                VStartIdx = c.readInt();
                VLength = c.readInt();
                //I think the reasoning here is that there is only ever one cell, so they removed it.  Because there is only one, its idx=0, and its offset is just the vertex offset.
                cellIdx = 0;
                cellOffset = VStartIdx;
            }
            else
            {
                cellIdx = c.readInt();
                cellOffset = c.readInt();
                VStartIdx = c.readInt();
                VLength = c.readInt();
            }
        }
        public static PlVertexSpan createEmpty()
        {
            PlVertexSpan r = new PlVertexSpan();
            r.parent = PlSpan.createEmpty();
            return r;
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(groupIdx);
            c.writeInt(VBufferIdx);
            c.writeInt(cellIdx);
            c.writeInt(cellOffset);
            c.writeInt(VStartIdx);
            c.writeInt(VLength);
        }
        PlVertexSpan(){}
        public PlVertexSpan deepClone()
        {
            PlVertexSpan result = new PlVertexSpan();
            result.parent = parent.deepClone();
            result.groupIdx = this.groupIdx;
            result.VBufferIdx = this.VBufferIdx;
            result.cellIdx = this.cellIdx;
            result.cellOffset = this.cellOffset;
            result.VStartIdx = this.VStartIdx;
            result.VLength = this.VLength;
            return result;
        }
    }
    static public class PlIcicle extends uruobj //SpanSubset
    {
        public PlVertexSpan parent;

        /*public int visible;
        public int materialindex;
        public Transmatrix localToWorld; //was transforms1
        public Transmatrix worldToLocal; //was transforms2
        public int lightingflags;
        public BoundingBox localBounds; //was uegclassesca1
        public BoundingBox worldBounds; //was uegclassesca2
        public int blendflag;
        public int blendindex;
        short u1;
        short u2;
        short u3;
        Flt u4;
        Flt u5;
        Flt xu6;
        public int meshindex;
        int unused1;
        int unused2;
        int vertexstart1;
        int vertexstart2;
        int vertexcount;*/
        public int IBufferIdx; //surfaceindex
        public int IStartIdx; //indexstart
        public int ILength; //indexcount

        public static PlIcicle createEmpty()
        {
            PlIcicle r = new PlIcicle();
            r.parent = PlVertexSpan.createEmpty();
            return r;
        }
        public PlIcicle(context c) throws readexception
        {
            /*shared.IBytestream data = c.in;
            meshindex = data.readInt(); //groupIdx
            unused1 = data.readInt(); //vBufferIdx
            unused2 = data.readInt(); //cellIdx
            vertexstart1 = data.readInt(); //cellOffset
            vertexstart2 = data.readInt(); //vStartIdx
            vertexcount = data.readInt(); //vLength*/
            parent = new PlVertexSpan(c);
            /*if(c.readversion==7)
            {
                //hex isle doesn't have the next two fields.
                m.warn("not sure if setting correct default for PlIcicle.");
            }
            else
            {*/
                IBufferIdx = c.readInt(); //bufferIdx
                IStartIdx = c.readInt(); //startIdx

                //IBufferIdx = 0;
                //IStartIdx = 0;
            //}
            ILength = c.readInt(); //iLength
            if((parent.parent.props&0x4)!=0)
            {
                //int dummy=0;
                //there is more stuff to read here.
                throw new shared.uncaughtexception("Unhandled case in PlIcicle.");
            }
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(IBufferIdx);
            c.writeInt(IStartIdx);
            c.writeInt(ILength);
        }
        PlIcicle(){}
        public PlIcicle deepClone()
        {
            PlIcicle result = new PlIcicle();
            result.parent = parent.deepClone();
            result.IBufferIdx = IBufferIdx;
            result.IStartIdx = IStartIdx;
            result.ILength = ILength;
            return result;
        }
    }
    


    /*static public class LightInfo extends uruobj
    {
        int lightcount;
        Uruobjectref[] xlights;
        
        public LightInfo(Bytestream data)
        {
            lightcount = data.readInt();
            if(lightcount>0)
            {
                xlights = data.readVector(Uruobjectref.class,lightcount);
            }
        }
    }*/
    
    static public class PlDISpanIndex extends uruobj //SubsetGroup
    {
        public int flags; //u1
        public int indicesCount; //subsetcount
        public int[] indices; //subsetindex
        
        public PlDISpanIndex(context c)
        {
            flags = c.in.readInt();
            indicesCount = c.in.readInt();
            indices = c.in.readInts(indicesCount);
        }
        PlDISpanIndex(){}
        public static PlDISpanIndex createWithIndex(int ind)
        {
            PlDISpanIndex r = new PlDISpanIndex();
            r.flags = 0x0;
            r.indicesCount = 1;
            r.indices = new int[]{ind};
            return r;
        }
        public void compile(Bytedeque data)
        {
            data.writeInt(flags);
            data.writeInt(indicesCount);
            data.writeInts(indices);
        }
    }
    static public class PlSpaceTree extends uruobj
    {
        ////Objheader xheader;
        public short childnodes;
        public int leafnodes;
        public int allnodes;
        public Nodes[] nodes2;


        public PlSpaceTree(context c) throws readexception
        {
            shared.IBytestream data = c.in;
            ////if(hasHeader) xheader = new Objheader(data);
            childnodes = data.readShort();
            leafnodes = data.readInt();
            allnodes = data.readInt();
            nodes2 = new Nodes[allnodes];
            for(int i=0;i<allnodes;i++)
            {
                nodes2[i] = new Nodes(c);
            }
            //nodes2 = data.readVector(Nodes.class, allnodes);
        }
        public PlSpaceTree(){}
        public void compile(Bytedeque data)
        {
            data.writeShort(childnodes);
            data.writeInt(leafnodes);
            data.writeInt(allnodes);
            for(int i=0;i<allnodes;i++)
            {
                nodes2[i].compile(data);
            }
        }

        public static class Nodes
        {
            public BoundingBox boundingbox;
            public short type;
            public short parent;
            public short left;
            public short right;

            public Nodes(context c) throws readexception
            {
                boundingbox = c.readObj(BoundingBox.class);
                type = c.in.readShort(); //is this a typeid?
                parent = c.in.readShort(); //is this a typeid?
                left = c.in.readShort(); //typeid?
                right = c.in.readShort(); //typeid?
            }
            public Nodes(){}
            public void compile(Bytedeque data)
            {
                boundingbox.compile(data);
                data.writeShort(type);
                data.writeShort(parent);
                data.writeShort(left);
                data.writeShort(right);
            }
        }
    }
    static public class modPlSpaceTree
    {
        public modNode root;
        //private static boolean breadthFirst = true;

        //for debugging:
        private PlSpaceTree source;
        private HashMap<Integer,Integer> renumberingsource;
        public plDrawableSpans treespansource;

        public PlSpaceTree generatePlSpaceTree()
        {
            PlSpaceTree r = new PlSpaceTree();
            PlSpaceTree.Nodes rootnode = new PlSpaceTree.Nodes();
            rootnode.parent = -1;
            //if(breadthFirst)
            //{
                //recommended.
                modNode[] nodes = getSortedNodeList();
                PlSpaceTree.Nodes[] nodes2 = new PlSpaceTree.Nodes[nodes.length];
                for(int i=0;i<nodes.length;i++)
                {
                    modNode node = nodes[i];
                    PlSpaceTree.Nodes node2 = new PlSpaceTree.Nodes();
                    if(node==null)
                    {
                        int dummy=0;
                    }
                    if(node.isleaf())
                    {
                        node2.boundingbox = node.icicleBounds;
                        node2.parent = (node.parent==null)?-1:(short)node.parent.index;
                        node2.type = (short)node.type;
                        node2.left = (short)node.icicle; //equals node.index
                        node2.right = 0;
                    }
                    else
                    {
                        node2.boundingbox = node.icicleBounds;
                        node2.parent = (node.parent==null)?-1:(short)node.parent.index;
                        node2.type = (short)node.type;
                        node2.left = (short)node.left.index;
                        node2.right = (short)node.right.index;
                    }
                    nodes2[i] = node2;
                }
                r.nodes2 = nodes2;
            //}
            //else
            //{
                //Vector<PlSpaceTree.Nodes> nodes = new Vector();
                //generatePlSpaceTreeDepthFirst(nodes, root, rootnode);
                //r.nodes2 = shared.generic.convertVectorToArray(nodes, PlSpaceTree.Nodes.class);
            //}
            r.allnodes = r.nodes2.length;
            int leafcount = 0;
            for(modNode node: nodes)
            {
                if(node.isleaf()) leafcount++;
            }
            r.leafnodes = leafcount;
            r.childnodes = (short)(r.allnodes - 1);  //assuming one root.
            return r;
        }
        private modNode[] getSortedNodeList()
        {
            Vector<modNode> nodes = new Vector();
            getNodeList(nodes, root);

            int count = nodes.size();
            int leafcount = 0;
            for(modNode node: nodes) if(node.isleaf()) leafcount++;

            modNode[] result = new modNode[nodes.size()];

            int curPos = leafcount;
            for(int i=0;i<count;i++)
            {
                modNode node = nodes.get(i);
                if(node.isleaf())
                {
                    node.index = node.icicle;
                }
                else
                {
                    if(node==root)
                    {
                        node.index = count-1;
                    }
                    else
                    {
                        node.index = curPos;
                        curPos++;
                    }
                }
                if(result[node.index]!=null)
                {
                    int dummy=0;
                }
                result[node.index] = node;
            }
            return result;
        }
        private void getNodeList(Vector<modNode> nodes, modNode curnode)
        {
            if(curnode.isleaf())
            {
                nodes.add(curnode);
            }
            else
            {
                curnode.left.parent = curnode;
                curnode.right.parent = curnode;
                getNodeList(nodes,curnode.left);
                getNodeList(nodes,curnode.right);
                nodes.add(curnode);
            }
        }

        /*private void generatePlSpaceTreeDepthFirst(Vector<PlSpaceTree.Nodes> nodes, modNode node, PlSpaceTree.Nodes node2)
        {
            if(node.isleaf)
            {
                node2.left = (short)node.icicle;
                node2.right = 0;
                node2.boundingbox = node.icicleBounds;
                node2.type = 1;
                nodes.add(node2);
            }
            else
            {
                //do the left
                PlSpaceTree.Nodes left = new PlSpaceTree.Nodes();
                //left.parent = (short)node.icicle;
                generatePlSpaceTreeDepthFirst(nodes,node.left,left);
                node2.left = (short)(nodes.size()-1); //the last thing in the vector.
                //do the right
                PlSpaceTree.Nodes right = new PlSpaceTree.Nodes();
                //right.parent = (short)node.icicle;
                generatePlSpaceTreeDepthFirst(nodes,node.right,right);
                node2.right = (short)(nodes.size()-1); //the last thing in the vector.
                //add this node

                node2.boundingbox = node.icicleBounds;
                node2.type = 0;
                nodes.add(node2);
                int thisindex = nodes.size() - 1;
                left.parent = (short)thisindex;
                right.parent = (short)thisindex;
            }
        }*/
        public void renumber(HashMap<Integer,Integer> renumbering)
        {
            this.renumberingsource = renumbering;
            boolean keep = renumber(renumbering, root);
            if(!keep)
            {
                //throw new shared.uncaughtexception("Unhandled case.");
                m.warn("Nulling PlSpaceTree...");
                this.root = null;
            }
        }
        private boolean renumber(HashMap<Integer, Integer> renumbering, modNode node)
        {
            if(node.isleaf())
            {
                Integer newicicle = renumbering.get(node.icicle);
                if(newicicle!=null)
                {
                    node.icicle = newicicle;
                    return true;
                }
                else
                {
                    //this node has been removed.
                    return false;
                }
            }
            else
            {
                boolean stillhaveleft = renumber(renumbering, node.left);
                boolean stillhaveright = renumber(renumbering, node.right);
                if(stillhaveleft)
                {
                    if(stillhaveright)
                    {
                        return true; //all is well.
                    }
                    else
                    {
                        //make this node into the left node.
                        node.icicle = node.left.icicle;
                        node.type = node.left.type;
                        node.right = node.left.right;
                        node.icicleBounds = node.left.icicleBounds;
                        node.left = node.left.left; //must be the last thing moved :P
                        return true;
                    }
                }
                else
                {
                    if(stillhaveright)
                    {
                        //make this node into the right node.
                        node.icicle = node.right.icicle;
                        node.type = node.right.type;
                        node.left = node.right.left;
                        node.icicleBounds = node.right.icicleBounds;
                        node.right = node.right.right; //must be the last thing moved :P
                        return true;
                    }
                    else
                    {
                        return false; //we don't have any leafs, so kill this node.
                    }
                }
            }
        }
        public void readFromPlSpaceTree(PlSpaceTree t)
        {
            int roots=0;
            for(PlSpaceTree.Nodes n: t.nodes2)
            {
                if(n.parent==-1) roots++;
            }
            if(roots!=1)
            {
                throw new shared.uncaughtexception("More than one root found.");
            }
            this.source = t;
            PlSpaceTree.Nodes node = t.nodes2[t.nodes2.length-1];
            root = readFromSpaceTree(t,node);
        }
        private modNode readFromSpaceTree(PlSpaceTree t, PlSpaceTree.Nodes node)
        {
            modNode node2 = new modNode();
            node2.icicleBounds = node.boundingbox;
            node2.type = node.type;
            if(!node2.isleaf())
            {
                //node2.isleaf = false;
                PlSpaceTree.Nodes left = t.nodes2[node.left];
                PlSpaceTree.Nodes right = t.nodes2[node.right];
                node2.left = readFromSpaceTree(t,left);
                node2.right = readFromSpaceTree(t,right);
            }
            else //else type==1
            {
                //node2.isleaf = true;
                node2.icicle = node.left; //this is how it's encoded; and node.right is always 0.
            }
            return node2;
        }

        public static class modNode
        {
            int icicle;
            BoundingBox icicleBounds; //we could suck this out of the icicle itself too, probably.
            //boolean isleaf;
            int type;
            //modNode parent;
            modNode left;
            modNode right;

            private int index; //just used internally, don't rely on it.
            private modNode parent; //just used internally, don't rely on it.

            public boolean isleaf()
            {
                return (type&0x1)!=0;
            }
        }
    }
    
    static public class PlGBufferGroup extends uruobj //Mesh
    {
        //byte vertexformat;
        public byte fformat;
        public int size; //length to end of mesh.
        //byte[] restofmesh; //ignore it for now.
        public int vertexstoragecount;
        public SubMesh[] submeshes;
        //left off here.
        public int surfacecount;
        //Uruvector<Short>[] surfaces;
        //Shortvector[] surfaces;
        public Surface[] surfaces;
        //int indexstoragecount;
        //IndexStorage[] indexstorages;
        
        //int u1;
        //int u2;
        //int u3;
        //int lastindex;

        public PlGBufferCell[][] fCells;

        public PlGBufferGroup(){}

        public PlGBufferGroup(context c) throws readexception
        {
            shared.IBytestream data = c.in;
            if(c.readversion==7)
            {
                HexislePlDrawableSpans.ReadPlGBufferGroup(c);
                /*if(true)return;
                //sub_5059A0  //504f80???
                int mesh1 = c.readInt(); //76
                byte mesh2 = c.readByte(); //0 //this+4+6
                byte mesh3 = c.readByte(); e.ensure(mesh3==1);//1 //v3+11, used in an if statement.
                byte mesh4 = c.readByte(); //0
                byte mesh5 = c.readByte(); //0
                int mesh6 = c.readInt(); //864 //v4+4
                byte mesh7 = c.readByte(); //0
                byte hi6a = (byte)(mesh6&0xFF);
                byte hi6b = (byte)((mesh6>>>8)&0xFF);
                int v7 = b.ByteToInt32(((mesh3&0x2)!=0)?hi6b:hi6a);
                //hex isle doesn't look like it has the size field.  It's ignored by pots, so we'll just stick 0 in it.
                size = 0;
                vertexstoragecount = data.readInt();
                
                int count = HexislePlDrawableSpans.getvertexcount(mesh1, mesh2);
                
                SubMeshAlt[] submeshalt = new SubMeshAlt[vertexstoragecount];
                for(int i=0;i<vertexstoragecount;i++)
                {
                    //submeshalt[i] = new SubMeshAlt(c,fformat,v7,mesh6,mesh2,mesh1,count);
                    //submeshalt[i] = new SubMeshAlt(c,hi1,);
                    int v12 = c.readInt();
                    byte v15 = c.readByte();
                    //sub50e890(c,mesh1);
                }
                //TODO rest of this, but it seems to work perfectly up to this point.
                surfacecount = data.readInt();
                surfaces = new Shortvector[surfacecount];
                for(int i=0;i<surfacecount;i++)
                {
                    //surfaces[i] = new Shortvector(data);
                    int surf1 = c.readInt(); //3 times the number of vertices?
                    byte surf2 = c.readByte();
                    byte[] surf3 = c.readBytes(surf1);
                    int dummy=0;
                }
                int dummy=0;
                */
            }
            else
            {
                fformat = data.readByte();
                size = data.readInt();
                
                //restofmesh = data.readBytes(size); //should this be size*4 ?
                vertexstoragecount = data.readInt();
                submeshes = new SubMesh[vertexstoragecount];
                for(int i=0;i<vertexstoragecount;i++)
                {
                    submeshes[i] = new SubMesh(c,fformat);
                }
                //TODO rest of this, but it seems to work perfectly up to this point.
                surfacecount = data.readInt();
                surfaces = new Surface[surfacecount];
                for(int i=0;i<surfacecount;i++)
                {
                    //surfaces[i] = new Shortvector(data);
                    surfaces[i] = new Surface(c);
                }

                fCells = new PlGBufferCell[vertexstoragecount][];
                for(int i=0;i<vertexstoragecount;i++)
                {
                    int bufferCellCount = data.readInt();
                    fCells[i] = new PlGBufferCell[bufferCellCount];
                    for(int j=0;j<bufferCellCount;j++)
                    {
                        fCells[i][j] = new PlGBufferCell(c);
                    }
                }
                //u1 = data.readInt();
                //u2 = data.readInt();
                //u3 = data.readInt();
                //lastindex = data.readInt();
                int dummy=0;
            }
        }
        public static class Surface
        {
            public int numshorts;
            public shared.ShortTriplet[] faces;

            public Surface(){}
            public Surface(context c) throws readexception
            {
                numshorts = c.readInt();
                int numfaces = numshorts/3;
                e.force(numfaces*3==numshorts);
                faces = c.readArray(shared.ShortTriplet.class, numfaces);
            }
            public void compile(Bytedeque c)
            {
                c.writeInt(numshorts);
                c.writeArray2(faces);
            }
        }
        public void createCells()
        {
            //e.force(vertexstoragecount==1);
            /*if(vertexstoragecount==1)
            {
                int c2 = b.Int16ToInt32(submeshes[0].count);
                PlGBufferCell cell= new PlGBufferCell(0,-1,c2);
                fCells = new PlGBufferCell[1][1];
                fCells[0][0] = cell;
            }
            else*/
            {
                fCells = new PlGBufferCell[vertexstoragecount][1];
                for(int i=0;i<vertexstoragecount;i++)
                {
                    int c2 = b.Int16ToInt32(submeshes[i].count);
                    PlGBufferCell cell = new PlGBufferCell(0,-1,c2);
                    fCells[i][0] = cell;
                }
            }
        }
        /*public void sub50e890()
        {

        }*/
        /*public static class SubMeshAlt
        {
            public SubMeshAlt(context c, byte fformat, int v7, int v17, byte mesh2, int mesh1, int count)
            {
                //sub_50e890?
                int submesh1 = c.readInt(); //768, size in bytes?
                byte submesh2 = c.readByte(); //0
                m.warn("TODO: make this check if the short is in range.");
                
                int v16 = submesh1 / v7; //e.ensure(hi1 % v7 == 0);
                int shouldbe0a = submesh1 % v7;
                //int count = v16;
                int finalcount = submesh1 / count;
                int shouldbe0b = submesh1 % count;
                
                //hi1 is the size in bytes?
                //v16 is the count of GetVertexDataSize calls.
                //v7 is the stride?
                
                //c.readBytes(submesh1);
                //if(true)return;
                
                context c2 = c.Fork();
                //int dataSize = SubMesh.GetVertexDataSize(count, fformat, c2);
                //int dataSize = HexislePlDrawableSpans.GetVertexDataSize(count, fformat, c2, v17, hi2orig, hi1orig);
                m.warn("Using count, but this doesn't seem to be correct.");
                int dataSize = HexislePlDrawableSpans.GetVertexDataSize(count, fformat, c2, v17, mesh2, mesh1);
                //int dataSize = HexislePlDrawableSpans.GetVertexDataSize();
                byte[] rawdata = c.in.readBytes(dataSize);
                int rawdataversion = c.readversion;

                c2.close();
            }
        }*/
        public void compile(Bytedeque data)
        {
            data.writeByte(fformat);
            data.writeInt(size); //this may be different, but apparently it's ignored anyway.
            data.writeInt(vertexstoragecount);
            for(int i=0;i<vertexstoragecount;i++)
            {
                submeshes[i].compile(data,fformat);
            }
            data.writeInt(surfacecount);
            for(int i=0;i<surfacecount;i++)
            {
                surfaces[i].compile(data);
            }

            for(int i=0;i<vertexstoragecount;i++)
            {
                int bufferCellCount = fCells[i].length;
                data.writeInt(bufferCellCount);
                for(int j=0;j<bufferCellCount;j++)
                {
                    fCells[i][j].compile(data);
                }
            }

            //data.writeInt(u1);
            //data.writeInt(u2);
            //data.writeInt(u3);
            //data.writeInt(lastindex);
            
        }
        
        //this is essentially pyprp's plVertexCoder.
        static public class SubMesh
        {
            short count;

            //SubMeshVertex[] vertices;
            public byte[] _rawdata;
            
            int rawdataversion; //not actual data, just useful for compiling.

            //just convenience:
            //float[] vertices;
            DrawableSpansEncoders.DecompressedPotsVertices verts = null;

            public SubMesh(){}
            public int getCount()
            {
                return b.Int16ToInt32(count);
            }
            public float[] getVertices(byte fformat2)
            {

                context c = context.createFromBytestream(new Bytestream(_rawdata));
                //c.compile = true;
                //c.out = data;
                c.readversion = rawdataversion;
                int count2 = getCount();
                float[] result = new float[count2*3];
                //GetVertexDataSize(count2,fformat,c);

                //int start = c.in.getAbsoluteOffset();
                int fformat = b.ByteToInt32(fformat2);
                int A = (fformat & 0x40) >>> 6;
                int B = (fformat & 0x30) >>> 4;
                int C = (fformat & 0x0F) >>> 0;


                DrawableSpansEncoders.rundataElement x = new DrawableSpansEncoders.rundataElement();
                DrawableSpansEncoders.rundataElement y = new DrawableSpansEncoders.rundataElement();
                DrawableSpansEncoders.rundataElement z = new DrawableSpansEncoders.rundataElement();

                DrawableSpansEncoders.rundataElement[] ws = new DrawableSpansEncoders.rundataElement[3];
                for(int i=0;i<3;i++) ws[i] = new DrawableSpansEncoders.rundataElement();

                DrawableSpansEncoders.rundataColour blue = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour green = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour red = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour alpha = new DrawableSpansEncoders.rundataColour();

                DrawableSpansEncoders.rundataElement[][] uvs = new DrawableSpansEncoders.rundataElement[15][];
                for(int i=0;i<15;i++)
                {
                    uvs[i] = new DrawableSpansEncoders.rundataElement[3];
                    for(int j=0;j<3;j++)
                    {
                        uvs[i][j] = new DrawableSpansEncoders.rundataElement();
                    }
                }

                for(int i=0;i<count2;i++)
                {
                    //int possofar = c.in.getAbsoluteOffset() - start;

                    //get vertex.
                    float xval = x.pollAsElement3(c);
                    float yval = y.pollAsElement3(c);
                    float zval = z.pollAsElement3(c);
                    result[i*3+0] = xval;
                    result[i*3+1] = yval;
                    result[i*3+2] = zval;

                    //get weights
                    for(int j=0;j<B;j++)
                    {
                        ws[j].pollAsElement2(c);
                    }

                    //if A is set to true, get bones
                    if ((B!=0) && (A!=0))
                    {
                        int out1 = c.in.readInt(); //bones
                        //if(c.compile) c.out.writeInt(out1);
                    }

                    //normals: these are just a byte now.
                    //data.readShort(); //nx
                    //data.readShort(); //ny
                    //data.readShort(); //nz
                    if(c.readversion==6)
                    {
                        byte out2 = c.in.readByte(); //nx
                        byte out3 = c.in.readByte(); //ny
                        byte out4 = c.in.readByte(); //nz
                    }
                    else if(c.readversion==3||c.readversion==4)
                    {
                        short out2 = c.in.readShort();
                        short out3 = c.in.readShort();
                        short out4 = c.in.readShort();
                    }

                    //colours:
                    blue.pollAsColour(c);
                    green.pollAsColour(c);
                    red.pollAsColour(c);
                    alpha.pollAsColour(c);

                    //uv texture coordinates
                    for(int j=0;j<C;j++)
                    {
                        for(int k=0;k<3;k++)
                        {
                            uvs[j][k].pollAsElement(c);
                        }
                    }

                }
                return result;
                //int stop = c.in.getAbsoluteOffset();
                //return stop-start; //return the size we processed.
            //}
            }
            public DrawableSpansEncoders.DecompressedPotsVertices decompressAllAndDontSave(byte fformat2)
            {
                return new DrawableSpansEncoders.DecompressedPotsVertices(_rawdata, fformat2, rawdataversion, this.getCount());
            }
            public DrawableSpansEncoders.DecompressedPotsVertices decompressAllAndSave(byte fformat2)
            {
                if(verts==null)
                {
                    DrawableSpansEncoders.DecompressedPotsVertices r = new DrawableSpansEncoders.DecompressedPotsVertices(_rawdata, fformat2, rawdataversion, this.getCount());
                    verts = r;
                }
                return verts;
            }
            public void setWithDecompressedPotsVerts(DrawableSpansEncoders.DecompressedPotsVertices verts)
            {
                this.verts = verts;
                this.rawdataversion = 3; //pots version.
                this.count = (short)verts.count;
            }
            public DrawableSpansEncoders.RawPotsVertices parseAll(byte fformat2)
            {
                context c = context.createFromBytestream(new Bytestream(_rawdata));
                //c.compile = true;
                //c.out = data;
                e.force(rawdataversion==3);
                c.readversion = rawdataversion;
                int count2 = getCount();
                DrawableSpansEncoders.RawPotsVertices r = new DrawableSpansEncoders.RawPotsVertices(count2);
                //float[] result = new float[count2*3];
                //GetVertexDataSize(count2,fformat,c);

                //int start = c.in.getAbsoluteOffset();
                int fformat = b.ByteToInt32(fformat2);
                int A = (fformat & 0x40) >>> 6;
                int B = (fformat & 0x30) >>> 4;
                int C = (fformat & 0x0F) >>> 0;


                DrawableSpansEncoders.rundataElement x = new DrawableSpansEncoders.rundataElement();
                DrawableSpansEncoders.rundataElement y = new DrawableSpansEncoders.rundataElement();
                DrawableSpansEncoders.rundataElement z = new DrawableSpansEncoders.rundataElement();

                DrawableSpansEncoders.rundataElement[] ws = new DrawableSpansEncoders.rundataElement[3];
                for(int i=0;i<3;i++) ws[i] = new DrawableSpansEncoders.rundataElement();

                DrawableSpansEncoders.rundataColour blue = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour green = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour red = new DrawableSpansEncoders.rundataColour();
                DrawableSpansEncoders.rundataColour alpha = new DrawableSpansEncoders.rundataColour();

                DrawableSpansEncoders.rundataElement[][] uvs = new DrawableSpansEncoders.rundataElement[15][];
                for(int i=0;i<15;i++)
                {
                    uvs[i] = new DrawableSpansEncoders.rundataElement[3];
                    for(int j=0;j<3;j++)
                    {
                        uvs[i][j] = new DrawableSpansEncoders.rundataElement();
                    }
                }

                for(int i=0;i<count2;i++)
                {
                    DrawableSpansEncoders.RawPotsVertex v = new DrawableSpansEncoders.RawPotsVertex();
                    //int possofar = c.in.getAbsoluteOffset() - start;

                    //get vertex.
                    v.x = x.pollAsElement(c);
                    v.y = y.pollAsElement(c);
                    v.z = z.pollAsElement(c);
                    //result[i*3+0] = xval;
                    //result[i*3+1] = yval;
                    //result[i*3+2] = zval;

                    //get weights
                    v.ws = new DrawableSpansEncoders.RawPotsElement[B];
                    for(int j=0;j<B;j++)
                    {
                        v.ws[j] = ws[j].pollAsElement(c);
                    }

                    //if A is set to true, get bones
                    if ((B!=0) && (A!=0))
                    {
                        v.bones = c.in.readInt(); //bones
                        //if(c.compile) c.out.writeInt(out1);
                    }

                    //normals: these are just a byte now.
                    //data.readShort(); //nx
                    //data.readShort(); //ny
                    //data.readShort(); //nz
                    /*if(c.readversion==6)
                    {
                        byte out2 = c.in.readByte(); //nx
                        byte out3 = c.in.readByte(); //ny
                        byte out4 = c.in.readByte(); //nz
                    }
                    else if(c.readversion==3||c.readversion==4)
                    {*/
                        v.normx = c.in.readShort();
                        v.normy = c.in.readShort();
                        v.normz = c.in.readShort();
                    //}

                    //colours:
                    v.blue = blue.pollAsColour(c);
                    v.green = green.pollAsColour(c);
                    v.red = red.pollAsColour(c);
                    v.alpha = alpha.pollAsColour(c);

                    //uv texture coordinates
                    v.uvs = new DrawableSpansEncoders.RawPotsElement[C][3];
                    for(int j=0;j<C;j++)
                    {
                        for(int k=0;k<3;k++)
                        {
                            v.uvs[j][k] = uvs[j][k].pollAsElement(c);
                        }
                    }

                    r.vertices[i] = v;
                    
                }
                //return result;
                //int stop = c.in.getAbsoluteOffset();
                //return stop-start; //return the size we processed.
            //}
                return r;
            }
            public SubMesh(context c, byte fformat)
            {
                if((fformat&0x80)!=0)
                {
                    /*if(c.readversion==7)
                    {
                        int hi1 = c.readInt();
                        byte hi2 = c.readByte();
                        m.warn("TODO: make this check if the short is in range.");
                        count = (short)hi1;
                    }
                    else
                    {*/
                        count = c.in.readShort(); //number of vertexes.
                    //}
                    
                    //vertices = new SubMeshVertex[count];
                    //for(int i=0;i<count;i++)
                    //{
                    //}
                    context c2 = c.Fork();
                    //c2.readversion = 3;
                    //context c2 = new context(6,3,false,c.in.CreateFork(),null);
                    int count2 = b.Int16ToInt32(count);
                    int dataSize = GetVertexDataSize(count2, fformat, c2);
                    _rawdata = c.in.readBytes(dataSize);
                    rawdataversion = c.readversion;
                    //PlDrawableSpansEncoders.DecompressedPotsVertices dpv = this.decompressAll(fformat);

                    c2.close();
                }
                else
                {
                    m.err("not supported.");
                }
            }
            
            public void compile(Bytedeque data, byte fformat)
            {
                if((fformat&0x80)!=0)
                {
                    data.writeShort(count);
                    //int dataSize = GetVertexDataSize(count, fformat, data.CreateFork());
                    //rawdata = data.readBytes(dataSize);
                    //Bytestream input = new Bytestream(rawdata);
                    //context c = new context(6,3,true,new Bytestream(rawdata),data,false,null);
                    if(this.verts==null)
                    {
                        context c = context.createFromBytestream(new Bytestream(_rawdata));
                        c.compile = true;
                        c.out = data;
                        c.readversion = rawdataversion;
                        int count2 = b.Int16ToInt32(count);
                        GetVertexDataSize(count2,fformat,c);
                    }
                    else
                    {
                        verts.compileSophisticated(data);
                    }
                }
                else
                {
                    m.err("not supported.");
                }
                
            }
            
            //parse and get size. Used for both creation and compilation.
            static public int GetVertexDataSize(int count, byte fformat2, context c)
            {

                class rundata
                {
                    Flt basee;
                    byte basec;
                    int count;
                    boolean rle;
                    byte b1;
                    
                    public rundata()
                    {
                        basee = null;
                        count = 0;
                        basec = 0;
                        rle = false;
                    }
                    
                    //returns the compressed value, in some circumstances.
                    public short pollAsElement(context c)
                    {
                        if(count==0)
                        {
                            basee = new Flt(c);
                            if(c.compile) basee.compile(c.out);

                            if(c.readversion==6)
                            {
                                b1 = c.in.readByte();
                                if(c.compile && c.writeversion==6) c.out.writeByte(b1);
                            }
                            else if(c.readversion==3||c.readversion==4||c.readversion==7)
                            {
                                //do nothing
                            }
                            
                            short out7 = c.in.readShort();
                            if(c.compile) c.out.writeShort(out7);
                            count = b.Int16ToInt32(out7);
                        }
                        if(count!=0)
                        {
                            if(c.readversion==6)
                            {
                                if(b1==0)
                                {
                                    count--;
                                    //return data.readShort();
                                    short out9 = c.in.readShort();
                                    if(c.compile) c.out.writeShort(out9);
                                    //return;
                                    return out9;
                                }
                                else if(b1==1)
                                {
                                    count--;
                                    if(c.compile && c.writeversion==3)
                                    {
                                        c.out.writeShort((short)0);//Is this right? Is this the default? //(32768);
                                    }
                                    return (short)0;
                                }
                                else
                                {
                                    m.msg("unknown byte.");
                                }
                            }
                            else if(c.readversion==3||c.readversion==4||c.readversion==7)
                            {
                                count--;
                                short out9 = c.in.readShort();
                                if(c.compile) c.out.writeShort(out9);
                            }
                        }
                        return 0;
                    }
                    public void pollAsColour(context c)
                    {
                        if(count==0)
                        {
                            short out5 = c.in.readShort();
                            if(c.compile) c.out.writeShort(out5);
                            count = b.Int16ToInt32(out5);
                            if((count & 0x8000)!=0)
                            {
                                //m.msg("haven't tested this.");
                                rle = true;
                                basec = c.in.readByte();
                                if(c.compile) c.out.writeByte(basec);
                                count = count & 0x7FFF;
                            }
                            else
                            {
                                rle = false;
                            }
                        }
                        if(count!=0)
                        {
                            count--;
                            if(rle)
                            {
                                //return base;
                                return;
                            }
                            else
                            {
                                //return data.readByte();
                                byte out6 = c.in.readByte();
                                if(c.compile) c.out.writeByte(out6);
                                return;
                            }
                        }
                        m.err("We shouldn't be able to reach here.");
                    }
                }
                
                int start = c.in.getAbsoluteOffset();
                int fformat = b.ByteToInt32(fformat2);
                int A = (fformat & 0x40) >>> 6;
                int B = (fformat & 0x30) >>> 4;
                int C = (fformat & 0x0F) >>> 0;

                
                rundata x = new rundata();
                rundata y = new rundata();
                rundata z = new rundata();
                
                rundata[] ws = new rundata[3];
                for(int i=0;i<3;i++) ws[i] = new rundata();

                rundata blue = new rundata();
                rundata green = new rundata();
                rundata red = new rundata();
                rundata alpha = new rundata();
                
                rundata[][] uvs = new rundata[15][];
                for(int i=0;i<15;i++)
                {
                    uvs[i] = new rundata[3];
                    for(int j=0;j<3;j++)
                    {
                        uvs[i][j] = new rundata();
                    }
                }

                for(int i=0;i<count;i++)
                {
                    int possofar = c.in.getAbsoluteOffset() - start;
                    
                    //get vertex.
                    short xval = x.pollAsElement(c);
                    short yval = y.pollAsElement(c);
                    short zval = z.pollAsElement(c);
                    
                    //compute actuall, uncompressed vertex:
                    if (c.outputVertices)
                    {
                        float output;
                        output = x.basee.toJavaFloat();
                        output = output + (float)(xval)/(float)(1024.0);
                        c.vertices.add(output);
                        output = x.basee.toJavaFloat();
                        output = output + (float)(yval)/(float)(1024.0);
                        c.vertices.add(output);
                        output = x.basee.toJavaFloat();
                        output = output + (float)(zval)/(float)(1024.0);
                        c.vertices.add(output);
                    }
                    
                    //get weights
                    for(int j=0;j<B;j++)
                    {
                        ws[j].pollAsElement(c);
                    }
                    
                    //if A is set to true, get bones
                    if ((B!=0) && (A!=0))
                    {
                        int out1 = c.in.readInt(); //bones
                        if(c.compile) c.out.writeInt(out1);
                    }
                    
                    //normals: these are just a byte now.
                    //data.readShort(); //nx
                    //data.readShort(); //ny
                    //data.readShort(); //nz
                    if(c.readversion==6)
                    {
                        byte out2 = c.in.readByte(); //nx
                        byte out3 = c.in.readByte(); //ny
                        byte out4 = c.in.readByte(); //nz
                        if(c.compile)
                        {
                            //c.out.writeShort((short)(b.ByteToInt32(out2)*257)); //the *257 expands it to 2 bytes. i.e. 0->0, 255->65535.
                            //c.out.writeShort((short)(b.ByteToInt32(out3)*257));
                            //c.out.writeShort((short)(b.ByteToInt32(out4)*257));
                            int out2i = b.ByteToInt32(out2);
                            int fin2 = out2i*257-32768;
                            if(fin2<-32768 || fin2>32767)
                            {
                                int dummy=0;
                            }
                            short sfin2 = (short)fin2;
                            c.out.writeShort(sfin2);
                            
                            int out3i = b.ByteToInt32(out3);
                            int fin3 = out3i*257-32768;
                            if(fin3<-32768 || fin3>32767)
                            {
                                int dummy=0;
                            }
                            short sfin3 = (short)fin3;
                            c.out.writeShort(sfin3);

                            int out4i = b.ByteToInt32(out4);
                            int fin4 = out4i*257-32768;
                            if(fin4<-32768 || fin4>32767)
                            {
                                int dummy=0;
                            }
                            short sfin4 = (short)fin4;
                            c.out.writeShort(sfin4);
                            //float fout2 = (((float)out2)/128f)-1f;
                            //short sout2 = (short)(fout2*100f);
                            //float fout3 = (((float)out3)/128f)-1f;
                            //short sout3 = (short)(fout3*100f);
                            //float fout4 = (((float)out4)/128f)-1f;
                            //short sout4 = (short)(fout4*100f);
                        }
                    }
                    else if(c.readversion==3||c.readversion==4)
                    {
                        short out2 = c.in.readShort();
                        short out3 = c.in.readShort();
                        short out4 = c.in.readShort();
                        if(c.compile)
                        {
                            c.out.writeShort(out2);
                            c.out.writeShort(out3);
                            c.out.writeShort(out4);
                        }
                    }
                    
                    //colours:
                    blue.pollAsColour(c);
                    green.pollAsColour(c);
                    red.pollAsColour(c);
                    alpha.pollAsColour(c);
                    
                    //uv texture coordinates
                    for(int j=0;j<C;j++)
                    {
                        for(int k=0;k<3;k++)
                        {
                            uvs[j][k].pollAsElement(c);
                        }
                    }
                    
                }

                int stop = c.in.getAbsoluteOffset();
                return stop-start; //return the size we processed.
            }
            
        }
    }
    
    
}

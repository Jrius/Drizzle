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

import uru.context;
//import shared.readexception;
//import uru.Bytestream;
//import uru.Bytedeque;
//import shared.e;
//import shared.m;
//import shared.b;
//import shared.readexception;
//import java.util.Vector;
import shared.*;
import shared.m;


public class HexislePlDrawableSpans extends uruobj
{
    public static float decodeHexisleUvFloat(short shrt)
    {
        //65536,32768
        //sub504560 seems to determine this formula.
        //float f = ((float)b.Int16ToInt32(shrt))/(65536.0f); //looks good!
        //float f = (float)(shrt)/(float)(1024.0); //should be use a number other than 1024?  should we make shrt unsigned? should we have a fixed offset?
        //float f = ((float)b.Int16ToInt32(shrt))/(255.0f);
        int ishrt = b.Int16ToInt32(shrt);
        //sub657030
        int v5 = ishrt&0x3FF; //value from 0 to 1023
        int v1 = (ishrt>>>10)&0x1F; //the scale: 1-30: 2^(v1-15)  case 0 and 31
        boolean v4 = (ishrt&0x8000)!=0; //the sign
        boolean v2 = v1==0;
        if(v1!=0)
        {
            if(v1<0x1F) //(31)(11111) i.e. if not all 5 bits are set.
            {
                //deal with whether the scale is negative or positive.
                if(v1<0xF) //(15)(01111) i.e. if not all first 4 bits are set.
                {
                    //float val = (float)ishrt;
                    float sign = v4?-1.0f:1.0f;
                    float divisor =  (float)(1 << (15 - v1));
                    float uv = sign*((v5/1024.0f)+1.0f)/divisor;
                    return uv;
                }
                else // >=15
                {
                    float sign = v4?-1.0f:1.0f;
                    float multiplier = (float)(1<<(v1-15));
                    float uv = sign*((v5/1024.0f)+1.0f)*multiplier;
                    return uv;
                }
            }
            else
            {
                //what the heck do we do here!!!
                m.throwUncaughtException("unhandled.");
            }

            //normally follows at the very end. we can only get here if v1==0x1F, so we might as well put this there.
            if(v5!=0)
            {
                //0x4F800000
                float uv = Float.intBitsToFloat(0x4F800000);
            }
            else
            {
                if(v4)
                {
                    //0x4F7F8000
                    float uv = Float.intBitsToFloat(0x4F7F8000);
                }
                else
                {
                    //0x4EFF0000
                    float uv = Float.intBitsToFloat(0x4EFF0000);
                }
            }
        }
        else //v1=scale=0
        {
            if(v5!=0) //scale=0, value!=0  //this looks like the same as setting the scale to 1 :P
            {
                //actually, 0.0f may be the correct thing to return, since disassembly suggests that it never stores this value that it computes :P
                m.warn("May be not correctly converting uv coordinate.");
                float sign = v4?-1.0f:1.0f;
                float uv = sign*(v5/1024.0f)/16384.0f;
                return uv;
            }
            else //scale=0, value=0
            {
                float uv = 0.0f;
                return uv;
            }
        }
        return 0.0f;
    }
    public static class HexislePlGBufferGroup
    {
        int off4;
        byte off10;
        byte off11;
        byte off12;
        byte off13;
        int ignored;
        byte off14;
        int vertexstoragecount;
        Mesh[] meshes;
        int surfacecount;
        Surface[] surfaces;

        //public static int minu;
        //public static int minv;
        //public static int maxu;
        //public static int maxv;

        public HexislePlGBufferGroup(context c) throws readexception
        {
            //sub_5059A0  //504f80???
            //m.warn("HexIsle PlDrawableSpans cannot be written yet.");

            off4 = c.readInt(); //76 //contains all the info on what data is stored for each vertex.
            off10 = c.readByte(); //0 //this+4+6 //byte
            off11 = c.readByte(); e.ensure(off11==1);//1 //v3+11, used in an if statement. //byte
            int _off8 = sub5049c0(off4,16,0,0,off10);
            int _off9 = sub5049c0(off4,16,1,0,off10);
            byte off8 = (byte)_off8;
            byte off9 = (byte)_off9;
            off12 = c.readByte(); //0  //bool
            off13 = c.readByte(); //0  //bool
            ignored = c.readInt(); //864 //v4+4 //size of something?  It isn't used by HexIsle anyway.
            off14 = c.readByte(); //0 //byte
            //byte hi6a = (byte)(ignored&0xFF);
            //byte hi6b = (byte)((ignored>>>8)&0xFF);
            //int v7 = b.ByteToInt32(((off11&0x2)!=0)?hi6b:hi6a);
            //hex isle doesn't look like it has the size field.  It's ignored by pots, so we'll just stick 0 in it.
            //size = 0;
            vertexstoragecount = c.readInt();

            //int count = HexislePlDrawableSpans.getvertexcount(off4, off10);

            //SubMeshAlt[] submeshalt = new SubMeshAlt[vertexstoragecount];

            //create the vertex coder now? yes.
            Coders coders = new Coders();

            meshes = new Mesh[vertexstoragecount];
            for(int i=0;i<vertexstoragecount;i++)
            {
                Mesh mesh = new Mesh();
                //submeshalt[i] = new SubMeshAlt(c,fformat,v7,mesh6,mesh2,mesh1,count);
                //submeshalt[i] = new SubMeshAlt(c,hi1,);
                mesh.v12 = c.readInt(); //decompressed size of all vertices?
                mesh.v15 = c.readByte();
                //sub50e890(c,mesh1);
                int v7;  //decompressed length per vertex?
                if((off11&2)!=0)
                {
                    v7 = off9;
                }
                else
                {
                    v7 = off8;
                }
                int v16 = mesh.v12 / v7;  //number of vertices?
                mesh.vertexcount = v16;
                //create the vertexcoder now? no.
                if(v16*v7!=mesh.v12)
                {
                    m.err("Problem calculating vertex stride.");
                }

                //hack: remove this!!!
                //v16 = v16 + 1000000;

                //sub50e890(0,0,0,off4,off8,v16,  c,off10,coders);
                //minu = 0; maxu = 0; minv = 0; maxv = 0;
                mesh.vertices = new VertexData[v16];
                for(int j=0;j<v16;j++)
                {
                    //sub50e570(dummyobj,dummyinstream,dummyarray,off4,off8,   c,off10,coders);
                    mesh.vertices[j] = new VertexData(0,0,0,off4,off8,   c,off10,coders);
                    //m.msg("done vertexdata");
                    if(coders.isCountZero())
                    {
                        if(j+1!=v16)
                        {
                            m.err("VertexCoder counts should be zero.");
                        }
                        //break;
                    }
                }
                //m.msg("done mesh************************************************************");
                //m.msg("minu="+Integer.toString(minu)+"  maxu="+Integer.toString(maxu)+"  minv="+Integer.toString(minv)+"  maxv="+Integer.toString(maxv));

                //convert uvw coords

                meshes[i] = mesh;
            }
            //TODO rest of this, but it seems to work perfectly up to this point.
            surfacecount = c.readInt();
            //Shortvector[] surfaces = new Shortvector[surfacecount];
            surfaces = new Surface[surfacecount];
            for(int i=0;i<surfacecount;i++)
            {
                //Surface surface = new Surface();
                //surfaces[i] = new Shortvector(data);
                //int surf1 = c.readInt(); //3 times the number of vertices?
                //byte surf2 = c.readByte();
                //byte[] surf3 = c.readBytes(surf1);
                //int dummy=0;
                //surface.v24 = c.readInt(); //length of face data, each face is 6 bytes: 2 bytes per vertex number.
                //surface.b = c.readByte();
                //surface.bs = c.readBytes(surface.v24);
                //surfaces[i] = surface;
                surfaces[i] = new Surface(c);
            }
            int dummy=0;
        }

        public plDrawableSpans.PlGBufferGroup convertToPotsPlGBufferGroup()
        {
            plDrawableSpans.PlGBufferGroup r = new plDrawableSpans.PlGBufferGroup();

            //implement!
            r.fformat = 0; //we need to figure this out.
            r.size = 0; //pots just ignores it anyway, but maybe we should set it so the tools don't mind?


            //meshes
            r.vertexstoragecount = this.vertexstoragecount;
            r.submeshes = new plDrawableSpans.PlGBufferGroup.SubMesh[r.vertexstoragecount];


            for(int i=0;i<r.vertexstoragecount;i++)
            {
                plDrawableSpans.PlGBufferGroup.SubMesh rm = new plDrawableSpans.PlGBufferGroup.SubMesh();
                r.submeshes[i] = rm;
                Mesh me = meshes[i];

                if(me.vertexcount>=(1<<16))
                {
                    int dummy=0;
                }
                e.force(me.vertexcount<(1<<16));
                rm.count = (short)me.vertexcount;
                
                //encode uv coords
                //float base = 0.0f;
                int numverts = me.vertices.length;
                for(int vx=0;vx<me.vertices.length;vx++)
                {
                    VertexData v = me.vertices[vx];
                    int v29 = sub5042d0(off4); //find the number of used 2bit blocks in off4
                    rundata2.RawPotsElement[][] uvs = new rundata2.RawPotsElement[v29][];
                    for(int j=0;j<v29;j++)
                    {
                        int v12 = sub504300(off4,j); //for each 2bit block in off4, find the number of components (from 0 to 4)
                        e.force(v12==3); //could this be something else?
                        uvs[j] = new rundata2.RawPotsElement[v12];
                        for(int k=0;k<v12;k++)
                        {
                            short shrt = v.textureCoords[j][k];
                            float f = decodeHexisleUvFloat(shrt);
                            //m.msg(Float.toString(f));
                            rundata2.RawPotsElement vc = new rundata2.RawPotsElement();
                            /*if(vx==0){
                                vc.hadcount0 = true;
                                //vc.basee = Flt.zero();
                                vc.basee = Flt.createFromJavaFloat(-10.0f);
                                vc.out7 = (short)numverts;
                            }else{
                                vc.hadcount0 = false;
                            }
                            int result = Math.round((f-(-10.0f))*1024.0f);
                            //if(result>32767 || result<-32767)
                            if(result<0 || result > 65534)
                            {
                                //throw new shared.uncaughtexception("out of range!");
                                result = 65000;

                            }
                            vc.out9 = (short)result;*/
                            vc.hadcount0 = true;
                            vc.basee = Flt.createFromJavaFloat(f);
                            vc.out7 = 1;
                            vc.out9 = 0;
                            uvs[j][k] = vc;
                        }
                    }
                    v.potsTextureCoords = uvs;
                }

                byte[] data = me.compileVertices();
                rm.rawdataversion = 3;
                rm._rawdata = data;
            }

            //surfaces
            r.surfacecount = this.surfacecount;
            r.surfaces = new plDrawableSpans.PlGBufferGroup.Surface[r.surfacecount];
            for(int j=0;j<r.surfacecount;j++)
            {
                r.surfaces[j] = new plDrawableSpans.PlGBufferGroup.Surface();
                r.surfaces[j].faces = this.surfaces[j].faces;
                r.surfaces[j].numshorts = r.surfaces[j].faces.length*3;
            }

            //cells
            r.createCells();

            //set fformat:
            int A = 0; //bones?
            int B = 0; //weights?
            int v29 = sub5042d0(off4); //find the number of used 2bit blocks in off4
            int C = v29; //uv coords
            //int A = (fformat & 0x40) >>> 6;
            //int B = (fformat & 0x30) >>> 4;
            //int C = (fformat & 0x0F) >>> 0;
            r.fformat = (byte)(0x80 | C);

            return r;
        }

        public static class Mesh
        {
            int v12;
            byte v15;
            VertexData[] vertices;

            //not to be compiled:
            int vertexcount;

            public byte[] compileVertices()
            {
                IBytedeque c = new shared.Bytedeque2(shared.Format.pots);
                for(int i=0;i<vertices.length;i++)
                {
                    vertices[i].compileAsPots(c);
                }
                byte[] result = c.getAllBytes();
                return result;
            }
        }
        public static class Surface
        {
            int v24; //size in bytes of data.
            byte b;
            //byte[] bs;
            shared.ShortTriplet[] faces;

            public Surface(context c) throws readexception
            {
                v24 = c.readInt();
                b = c.readByte();
                int facecount = v24/6;
                faces = c.readArray(ShortTriplet.class, facecount);

            }
        }
    }
    public static void ReadPlGBufferGroup(context c)
    {
        //sub_5059A0  //504f80???
        m.warn("HexIsle PlDrawableSpans cannot be written yet.");

        int off4 = c.readInt(); //76 //contains all the info on what data is stored for each vertex.
        byte off10 = c.readByte(); //0 //this+4+6 //byte
        byte off11 = c.readByte(); e.ensure(off11==1);//1 //v3+11, used in an if statement. //byte
        int _off8 = sub5049c0(off4,16,0,0,off10);
        int _off9 = sub5049c0(off4,16,1,0,off10);
        byte off8 = (byte)_off8;
        byte off9 = (byte)_off9;
        byte off12 = c.readByte(); //0  //bool
        byte off13 = c.readByte(); //0  //bool
        int ignored = c.readInt(); //864 //v4+4 //size of something?  It isn't used by HexIsle anyway.
        byte off14 = c.readByte(); //0 //byte
        //byte hi6a = (byte)(ignored&0xFF);
        //byte hi6b = (byte)((ignored>>>8)&0xFF);
        //int v7 = b.ByteToInt32(((off11&0x2)!=0)?hi6b:hi6a);
        //hex isle doesn't look like it has the size field.  It's ignored by pots, so we'll just stick 0 in it.
        //size = 0;
        int vertexstoragecount = c.readInt();

        //int count = HexislePlDrawableSpans.getvertexcount(off4, off10);

        //SubMeshAlt[] submeshalt = new SubMeshAlt[vertexstoragecount];

        //create the vertex coder now? yes.
        Coders coders = new Coders();

        for(int i=0;i<vertexstoragecount;i++)
        {
            //submeshalt[i] = new SubMeshAlt(c,fformat,v7,mesh6,mesh2,mesh1,count);
            //submeshalt[i] = new SubMeshAlt(c,hi1,);
            int v12 = c.readInt(); //decompressed size of all vertices?
            byte v15 = c.readByte();
            //sub50e890(c,mesh1);
            int v7;  //decompressed length per vertex?
            if((off11&2)!=0)
            {
                v7 = off9;
            }
            else
            {
                v7 = off8;
            }
            int v16 = v12 / v7;  //number of vertices?
            //create the vertexcoder now? no.
            if(v16*v7!=v12)
            {
                m.err("Problem calculating vertex stride.");
            }

            //hack: remove this!!!
            //v16 = v16 + 1000000;

            sub50e890(0,0,0,off4,off8,v16,  c,off10,coders);
        }
        //TODO rest of this, but it seems to work perfectly up to this point.
        int surfacecount = c.readInt();
        //Shortvector[] surfaces = new Shortvector[surfacecount];
        byte[] bs;
        for(int i=0;i<surfacecount;i++)
        {
            //surfaces[i] = new Shortvector(data);
            //int surf1 = c.readInt(); //3 times the number of vertices?
            //byte surf2 = c.readByte();
            //byte[] surf3 = c.readBytes(surf1);
            //int dummy=0;
            int v24 = c.readInt(); //length of face data, each face is 6 bytes: 2 bytes per vertex number.
            byte b = c.readByte();
            bs = c.readBytes(v24);
        }
        int dummy=0;
    }
    static void sub50e890(int dummyobj, int dummyinstream, int dummyarray, int off4, int off8, int v16,     context c, int off10, Coders coders)
    {
        //coders = new Coders();
        for(int i=0;i<v16;i++)
        {
            sub50e570(dummyobj,dummyinstream,dummyarray,off4,off8,   c,off10,coders);
            if(coders.isCountZero())
            {
                if(i+1!=v16)
                {
                    m.err("VertexCoder counts should be zero.");
                }
                int dummy=0;
                return;
            }
        }
    }
    public static class Coders
    {
        rundata2 x = new rundata2();
        rundata2 y = new rundata2();
        rundata2 z = new rundata2();

        rundata2 b = new rundata2();
        rundata2 g = new rundata2();
        rundata2 r = new rundata2();
        rundata2 a = new rundata2();

        public boolean isCountZero()
        {
            if(x.count==0 && y.count==0 && z.count==0 && b.count==0 && g.count==0 && r.count==0 && a.count==0) return true;
            return false;
        }
    }
    public static class VertexData
    {
        byte[] bytes;
        byte[] bytes2;
        rundata2.RawPotsElement x;
        rundata2.RawPotsElement y;
        rundata2.RawPotsElement z;
        byte b1x;
        byte b1y;
        byte b1z;
        byte b2x;
        byte b2y;
        byte b2z;
        byte b3x;
        byte b3y;
        byte b3z;
        rundata2.RawPotsColour b1;
        rundata2.RawPotsColour g1;
        rundata2.RawPotsColour r1;
        rundata2.RawPotsColour a1;
        rundata2.RawPotsColour b2;
        rundata2.RawPotsColour g2;
        rundata2.RawPotsColour r2;
        rundata2.RawPotsColour a2;
        short[][] textureCoords; //is that what these are?
        //hack:
        rundata2.RawPotsElement[][] potsTextureCoords;

        //don't compile this, just here for convenience:
        int off4;

        public VertexData(int dummyobj, int dummyinstream, int dummyarray, int off4, int off8,     context c, int off10, Coders coders)
        {
            this.off4 = off4;
            //v7 = off10
            //read v7 bytes.
            //int ofb4 = 0;
            //int ofb12 = 0;
            //int ofb20 = 0;
            if(off10!=0) //off10=0,1,3
            {
                int dummy=0;
                m.warn("Unhandled bytes in VertexData.");
                if(off10!=1)
                {
                    int dummy2=0;
                    if(off10!=3)
                    {
                        int dummy3=0;
                    }
                }
            }
            bytes = c.readBytes(off10); //stored as 0x(00)(b0)(b1)(b2) or 0x(00)(b0)(00)(00)
            if(sub504070(off4,1))
            {
                bytes2 = c.readBytes(off10+1); //stored as 0x(00)(b2)(b1)(b0)
                if(off10!=3)
                {
                    int dummy=0;
                }
            }
            if(sub504070(off4,2))
            {
                x = coders.x.pollAsElement(c);
                y = coders.y.pollAsElement(c);
                z = coders.z.pollAsElement(c);
            }
            if(sub504070(off4,3))
            {
                b1x = c.readByte();
                b1y = c.readByte();
                b1z = c.readByte();
            }
            if(sub504070(off4,4))
            {
                b2x = c.readByte();
                b2y = c.readByte();
                b2z = c.readByte();
            }
            if(sub504070(off4,5))
            {
                b3x = c.readByte();
                b3y = c.readByte();
                b3z = c.readByte();
            }
            if(sub504070(off4,6))
            {
                b1 = coders.b.pollAsColour(c);
                g1 = coders.g.pollAsColour(c);
                r1 = coders.r.pollAsColour(c);
                a1 = coders.a.pollAsColour(c);
            }
            if(sub504070(off4,7))
            {
                //should these really be the same coders?
                m.throwUncaughtException("unhandled");
                b2 = coders.b.pollAsColour(c);
                g2 = coders.g.pollAsColour(c);
                r2 = coders.r.pollAsColour(c);
                a2 = coders.a.pollAsColour(c);
            }

            int v29 = sub5042d0(off4); //find the number of used 2bit blocks in off4
            textureCoords = new short[v29][];
            for(int i=0;i<v29;i++)
            {
                int v12 = sub504300(off4,i); //for each 2bit block in off4, find the number of components (from 0 to 4)
                if(/*v29!=1 ||*/ v12!=3)
                {
                    int dummy=0;
                }
                textureCoords[i] = new short[v12];
                String msg = "";
                for(int j=0;j<v12;j++)
                {
                    short shrt = c.readShort();
                    textureCoords[i][j] = shrt;
                    /*msg = msg+Short.toString(shrt)+"  ";//+msg;
                    int shrt2 = b.Int16ToInt32(shrt);
                    if(j==0)
                    {
                        if (HexislePlGBufferGroup.minu==0) HexislePlGBufferGroup.minu = shrt2;
                        if (HexislePlGBufferGroup.maxu==0) HexislePlGBufferGroup.maxu = shrt2;
                        if (shrt2<HexislePlGBufferGroup.minu) HexislePlGBufferGroup.minu = shrt2;
                        if (shrt2>HexislePlGBufferGroup.maxu) HexislePlGBufferGroup.maxu = shrt2;
                    }
                    if(j==1)
                    {
                        if (HexislePlGBufferGroup.minv==0) HexislePlGBufferGroup.minv = shrt2;
                        if (HexislePlGBufferGroup.maxv==0) HexislePlGBufferGroup.maxv = shrt2;
                        if (shrt2<HexislePlGBufferGroup.minv) HexislePlGBufferGroup.minv = shrt2;
                        if (shrt2>HexislePlGBufferGroup.maxv) HexislePlGBufferGroup.maxv = shrt2;
                    }*/
                }
                //m.msg(msg);
            }

        }

        public void compileAsPots(IBytedeque c)
        {

            //coords
            e.force(sub504070(off4,2));
            this.x.compile(c);
            this.y.compile(c);
            this.z.compile(c);


            //normals
            e.force(sub504070(off4,3));
            byte out2 = this.b1x; //nx
            byte out3 = this.b1y; //ny
            byte out4 = this.b1z; //nz
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
            c.writeShort(sfin2);

            int out3i = b.ByteToInt32(out3);
            int fin3 = out3i*257-32768;
            if(fin3<-32768 || fin3>32767)
            {
                int dummy=0;
            }
            short sfin3 = (short)fin3;
            c.writeShort(sfin3);

            int out4i = b.ByteToInt32(out4);
            int fin4 = out4i*257-32768;
            if(fin4<-32768 || fin4>32767)
            {
                int dummy=0;
            }
            short sfin4 = (short)fin4;
            c.writeShort(sfin4);


            //colours
            e.force(sub504070(off4,6));
            b1.compile(c);
            g1.compile(c);
            r1.compile(c);
            a1.compile(c);


            //uv texture coordinates
            for(rundata2.RawPotsElement[] a2b: potsTextureCoords)
            {
                for(rundata2.RawPotsElement vc: a2b)
                {
                    vc.compile(c);
                }
            }
            
        }
    }
    static void sub50e570(int dummyobj, int dummyinstream, int dummyarray, int off4, int off8,     context c, int off10, Coders coders)
    {
        //v7 = off10
        //read v7 bytes.
        int ofb4 = 0;
        int ofb12 = 0;
        int ofb20 = 0;
        byte[] bytes = c.readBytes(off10);
        byte[] bytes2;
        if(sub504070(off4,1))
        {
            bytes2 = c.readBytes(off10);
        }
        if(sub504070(off4,2))
        {
            /*if(ofb4==0)
            {
                Flt f1 = new Flt(c);
                short s2 = c.readShort();
                ofb4 = s2;
            }
            short s3 = c.readShort();
            if(ofb12==0)
            {
                Flt f4 = new Flt(c);
                short s5 = c.readShort();
                ofb12 = s5;
            }
            short s6 = c.readShort();
            if(ofb20==0)
            {
                Flt f7 = new Flt(c);
                short s8 = c.readShort();
                ofb20 = s8;
            }
            short s9 = c.readShort();*/
            coders.x.pollAsElement(c);
            coders.y.pollAsElement(c);
            coders.z.pollAsElement(c);
        }
        if(sub504070(off4,3))
        {
            byte b1 = c.readByte();
            byte b2 = c.readByte();
            byte b3 = c.readByte();
        }
        if(sub504070(off4,4))
        {
            byte b1 = c.readByte();
            byte b2 = c.readByte();
            byte b3 = c.readByte();
        }
        if(sub504070(off4,5))
        {
            byte b1 = c.readByte();
            byte b2 = c.readByte();
            byte b3 = c.readByte();
        }
        if(sub504070(off4,6))
        {
            coders.b.pollAsColour(c);
            coders.g.pollAsColour(c);
            coders.r.pollAsColour(c);
            coders.a.pollAsColour(c);
        }
        if(sub504070(off4,7))
        {
            //should these really be the same coders?
            m.throwUncaughtException("unhandled");
            coders.b.pollAsColour(c);
            coders.g.pollAsColour(c);
            coders.r.pollAsColour(c);
            coders.a.pollAsColour(c);
        }
        int v29 = sub5042d0(off4);
        for(int i=0;i<v29;i++)
        {
            int v12 = sub504300(off4,i);
            for(int j=0;j<v12;j++)
            {
                short shrt = c.readShort();
            }
        }
    }
    static boolean sub504070(int flags, int pos)
    {
        return ((flags&(1<<pos))!=0);
    }
    static int sub5049c0(int off4, int b, int c, int d, byte off10)
    {
        //final double flt8ab0e4 = 4294967300.0;  //=2^32  //4.2949673e10
        //final double flt8ab0e0 = 60;
        //flt93b584 = 0.5;
        //flt93b610 = 4.0;
        boolean a3 = c!=0;
        int v4 = 0;
        switch(b)
        {
            case 0x10:
                int thiscount = sub504300(off4,7);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    //equivalent to: if even, =thiscount*2, else =(thiscount+1)*2
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0F:
                thiscount = sub504300(off4,6);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0E:
                thiscount = sub504300(off4,5);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0D:
                thiscount = sub504300(off4,4);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0C:
                thiscount = sub504300(off4,3);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0B:
                thiscount = sub504300(off4,2);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x0A:
                thiscount = sub504300(off4,1);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x09:
                thiscount = sub504300(off4,0);
                if(a3)
                {
                    thiscount *= 4;
                    v4 += thiscount;
                    if(thiscount<0)
                    {
                        m.throwUncaughtException("unhandled");
                    }
                }
                else
                {
                    //v4 += ceil(thiscount*0.5)*4.0;
                    int val = (thiscount%2==0)?(thiscount*2):(thiscount*2+2);
                    v4 += val;
                }
            case 0x08:
                if(((off4>>>7)&1)!=0)
                {
                    v4 += 4;
                }
            case 0x07:
                if(((off4>>>6)&1)!=0)
                {
                    v4 += 4;
                }
            case 0x06:
                if(((off4>>>5)&1)!=0)
                {
                    v4 += (a3?4:0) + 8;
                }
            case 0x05:
                if(((off4>>>4)&1)!=0)
                {
                    v4 += (a3?4:0) + 8;
                }
            case 0x04:
                if(((off4>>>3)&1)!=0)
                {
                    v4 += (a3?4:0) + 8;
                }
            case 0x03:
                if(((off4>>>2)&1)!=0)
                {
                    v4 += 12;
                }
            case 0x02:
                if(d!=0)
                {
                    return v4;
                }
                if(((off4>>>1)&1)!=0)
                {
                    v4 += 4;
                }
            case 0x01:
                if(d!=0)
                {
                    return v4;
                }
                if(off10==0)
                {
                    return v4;
                }
                if(a3)
                {
                    v4 += 4*off10;
                    return v4;
                }
                else
                {
                    v4 += 4;
                    return v4;
                }
            default:
                //return v4; //will be 0 here.
                return 0;

        }
    }
    static int sub504300(int off4, int num)
    {
        //num must be from 0 to 7.
        //this function will return from 0 to 4.
        int bit = num+8; //from lookup table; table=[8,9,10,11,12,13,14,15] so, bit ranges from 8 to 15.
        if((off4 & (1<<bit))!=0)
        {
            int result = ((off4 >>> (2 * (15-num)))&3)+1;
            return result; //so, result is 2 bits from off4 plus 1.  Which is from 1 to 4.
        }
        else
        {
            return 0;
        }
    }
        public static class rundata2
        {
            Flt basee;
            byte basec;
            int count;
            boolean rle;
            byte b1;

            public rundata2()
            {
                basee = null;
                count = 0;
                basec = 0;
                rle = false;
            }
            public static class RawPotsElement
            {
                boolean hadcount0;
                Flt basee; //may be null.
                //byte b1; //only here if read as MOUL/v6.
                short out7;

                //if(count!=0) block should always occur.
                short out9; //only present in Moul/v6 if b1 above is set, otherwise make it 0.

                public RawPotsElement(){}
                /*public RawPotsElement(short out9)
                {
                    this.hadcount0 = false;
                    this.out9 = out9;
                }
                public RawPotsElement(Flt basee, short out7, short out9)
                {
                    this.hadcount0 = true;
                    this.basee = basee;
                    this.out7 = out7;
                    this.out9 = out9;
                }*/
                public void compile(IBytedeque c)
                {
                    if(hadcount0)
                    {
                        basee.compile(c);
                        c.writeShort(out7);
                    }
                    c.writeShort(out9);
                }
            }
            //returns the compressed value, in some circumstances.
            public RawPotsElement pollAsElement(/*int A, int B, int C,*/ context c)
            {
                RawPotsElement r = new RawPotsElement();
                if(count==0)
                {
                    r.hadcount0 = true;

                    basee = new Flt(c);
                    //if(c.compile) basee.compile(c.out);
                    r.basee = basee;

                    if(c.readversion==6)
                    {
                        b1 = c.in.readByte();
                        //if(c.compile && c.writeversion==6) c.out.writeByte(b1);
                    }
                    else if(c.readversion==3||c.readversion==4||c.readversion==7)
                    {
                        //do nothing
                    }

                    short out7 = c.in.readShort();
                    r.out7 = out7;
                    //if(c.compile) c.out.writeShort(out7);
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
                            //if(c.compile) c.out.writeShort(out9);
                            //return;
                            //return out9;
                            r.out9 = out9;
                        }
                        else if(b1==1)
                        {
                            count--;
                            //if(c.compile && c.writeversion==3)
                            //{
                            //    c.out.writeShort((short)0);//Is this right? Is this the default? //(32768);
                            //}
                            //return (short)0;
                            r.out9 = 0;
                        }
                        else
                        {
                            m.msg("unknown byte.");
                        }
                    }
                    else if(c.readversion==3||c.readversion==4||c.readversion==7)
                    {
                        count--;
                        //short out9 = c.in.readShort();
                        r.out9 = c.readShort();
                        //if(c.compile) c.out.writeShort(out9);
                    }
                }
                //return 0;
                return r;
            }
            public static class RawPotsColour
            {
                boolean hadCount0;
                short out5;
                boolean hadRle;
                byte basec;
                byte out6;

                public void compile(IBytedeque c)
                {
                    if(hadCount0)
                    {
                        c.writeShort(out5);
                        if(hadRle)
                        {
                            c.writeByte(basec);
                        }
                    }
                    if(!hadRle)
                    {
                        c.writeByte(out6);
                    }
                }
            }
            public RawPotsColour pollAsColour(/*int A, int B, int C,*/ context c)
            {
                //sub_50e270
                RawPotsColour r = new RawPotsColour();
                if(count==0)
                {
                    r.hadCount0 = true;
                    short out5 = c.in.readShort();
                    r.out5 = out5;
                    //if(c.compile) c.out.writeShort(out5);
                    count = b.Int16ToInt32(out5);
                    if((count & 0x8000)!=0) //negative
                    {
                        //r.hadRle = true;
                        //m.msg("haven't tested this.");
                        rle = true;
                        basec = c.in.readByte();
                        r.basec = basec;
                        //if(c.compile) c.out.writeByte(basec);
                        count = count & 0x7FFF;
                    }
                    else // >=0
                    {
                        rle = false;
                    }
                }
                if(count!=0)
                {
                    count--;
                    r.hadRle = rle;
                    if(rle)
                    {
                        //return base;
                        //return;
                        return r;
                    }
                    else
                    {
                        //return data.readByte();
                        byte out6 = c.in.readByte();
                        r.out6 = out6;
                        //if(c.compile) c.out.writeByte(out6);
                        //return;
                        return r;
                    }
                }
                m.err("We shouldn't be able to reach here.");
                return r;
            }
        }




    private static int getatpos(int flags, int pos)
    {
        int resutl = ((flags >>> 2*(15-pos))&0x3) + 1;
        return resutl;
    }
    /*public static int getvertexcount(int flags, int hi2orig)
    {
        
        //sub_5049c0
        
        int a3 = 0; //actually set in object, either 0 or 1. hi2 is the var, I think
        
        int a = 0;
        a += getatpos(flags,7);
        a += getatpos(flags,6);
        a += getatpos(flags,5);
        a += getatpos(flags,4);
        a += getatpos(flags,3);
        a += getatpos(flags,2);
        a += getatpos(flags,1);
        a += getatpos(flags,0);
        if(((flags>>>7)&1)!=0) a+= 4;
        if(((flags>>>6)&1)!=0) a+= 4;
        if(((flags>>>5)&1)!=0) a+= 4*a3+8;
        if(((flags>>>4)&1)!=0) a+= 4*a3+8;
        if(((flags>>>3)&1)!=0) a+= 4*a3+8;
        if(((flags>>>2)&1)!=0) a+= 12;
        if(((flags>>>1)&1)!=0) a+= 4;
        if(hi2orig!=0 )
        {
            a += 4;
            //if(a3!=0)
            //{
            //    a += 4*hi2orig;
            //}
            //else
            //{
            //    a += 4;
            //}
        }
        return a;
    }
    */
    //parse and get size. Used for both creation and compilation.
    static boolean flag(int flags, int pos)
    {
        return ((flags & (0x1 << pos))!=0);
    }
    /*static public int GetVertexDataSize(int count, byte fformat2, context c, int a5, byte mesh2, int mesh1)
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
            public short pollAsElement(int A, int B, int C, context c)
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
            public void pollAsColour(int A, int B, int C, context c)
            {
                //sub_50e270
                if(count==0)
                {
                    short out5 = c.in.readShort();
                    if(c.compile) c.out.writeShort(out5);
                    count = b.Int16ToInt32(out5);
                    if((count & 0x8000)!=0) //negative
                    {
                        //m.msg("haven't tested this.");
                        rle = true;
                        basec = c.in.readByte();
                        if(c.compile) c.out.writeByte(basec);
                        count = count & 0x7FFF;
                    }
                    else // >=0
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

        //e.ensure(hi1orig==76);
        
        byte v5 = (byte)((a5>>>8)&0xFF);
        if((a5&0x2000000)!=0)
        {
            v5 = (byte)(a5&0xFF);
        }
        int v7 = b.ByteToInt32(mesh2);
        int flags = mesh1;
        //int a4 = mesh1;
        //if(v7)
        
        int start = c.in.getAbsoluteOffset();
        int fformat = b.ByteToInt32(fformat2);
        fformat = flags;
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

            
            if(v7!=0)
            {
                //not tested.
                byte[] b1 = c.readBytes(v7);
                if(flag(flags,1))
                {
                    byte[] b2 = c.readBytes(v7);
                    int dummy=0;
                }
                int unhandled = 0;
            }
            if(flag(flags,2))
            {
                short xval = x.pollAsElement(A, B, C, c);
                short yval = y.pollAsElement(A, B, C, c);
                short zval = z.pollAsElement(A, B, C, c);
                //not implemented yet.
                int dummy=0;
            }
            if(flag(flags,3))
            {
                //these are decompressed to floats.
                byte b1 = c.readByte();
                byte b2 = c.readByte();
                byte b3 = c.readByte();
                int dummy=0;
                //not implemented yet.
            }
            if(flag(flags,4))
            {
                int dummy=0;
                //not implemented yet.
            }
            if(flag(flags,5))
            {
                int dummy=0;
                //not implemented yet.
            }
            if(flag(flags,6))
            {
                blue.pollAsColour(A, B, C, c);
                green.pollAsColour(A, B, C, c);
                red.pollAsColour(A, B, C, c);
                alpha.pollAsColour(A, B, C, c);
                int dummy=0;
                //not implemented yet.
            }
            if(flag(flags,7))
            {
                int dummy=0;
                //not implemented yet.
            }
            
            int result2 = sub5042d0(flags);
            
            for(int j=0;j<result2;j++)
            {
                int wha = sub504300(flags, j);
                for(int k=0;k<wha;k++)
                {
                    short s1 = c.readShort(); //encoded float
                    int dummy=0;
                }
                if(wha==1 || wha==3)
                {
                    //decompress byte to float?
                    int dummy2=0;
                }
                //unhandled
                int dummy=0;
            }
            
            if(true)continue;
            
            
            //get vertex.
            short xval = x.pollAsElement(A, B, C, c);
            short yval = y.pollAsElement(A, B, C, c);
            short zval = z.pollAsElement(A, B, C, c);

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
                ws[j].pollAsElement(A, B, C, c);
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
            if(c.readversion==6||c.readversion==7)
            {
                byte out2 = c.in.readByte(); //nx
                byte out3 = c.in.readByte(); //ny
                byte out4 = c.in.readByte(); //nz
                if(c.compile)
                {
                    c.out.writeShort((short)(b.ByteToInt32(out2)*257)); //the *257 expands it to 2 bytes. i.e. 0->0, 255->65535.
                    c.out.writeShort((short)(b.ByteToInt32(out3)*257));
                    c.out.writeShort((short)(b.ByteToInt32(out4)*257));
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
            blue.pollAsColour(A, B, C, c);
            green.pollAsColour(A, B, C, c);
            red.pollAsColour(A, B, C, c);
            alpha.pollAsColour(A, B, C, c);

            if(true)continue;
            //this is slightly wrong I think...
            int result = sub5042d0(flags);
            for(int j=0;j<result;j++)
            {
                //reads some number of shorts
                m.err("Unhandled.");
            }
            if(true)continue;
            
            //uv texture coordinates
            for(int j=0;j<C;j++)
            {
                for(int k=0;k<3;k++)
                {
                    uvs[j][k].pollAsElement(A, B, C, c);
                }
            }

        }

        int stop = c.in.getAbsoluteOffset();
        return stop-start; //return the size we processed.
    }*/
    /*public static int sub504300(int val, int val2)
    {
        int shift = val2+8;
        int othershift = 15-val2;
        if((val&(1<<shift))!=0)
        {
            return ((val >> 2 * (othershift)) & 3) + 1;
        }
        else
        {
            return 0;
        }
    }*/
    public static int sub5042d0(int val)
    {
        //actually does a table lookup, but the table just has 8 ints and they run from 0x8 to 0xF.
        /*if((val&(1<<8))!=0) return 0;
        if((val&(1<<9))!=0) return 1;
        if((val&(1<<10))!=0) return 2;
        if((val&(1<<11))!=0) return 3;
        if((val&(1<<12))!=0) return 4;
        if((val&(1<<13))!=0) return 5;
        if((val&(1<<14))!=0) return 6;
        if((val&(1<<15))!=0) return 7;*/
        if((val&(1<<8))==0) return 0;
        if((val&(1<<9))==0) return 1;
        if((val&(1<<10))==0) return 2;
        if((val&(1<<11))==0) return 3;
        if((val&(1<<12))==0) return 4;
        if((val&(1<<13))==0) return 5;
        if((val&(1<<14))==0) return 6;
        if((val&(1<<15))==0) return 7;
        return 8;
    }
}

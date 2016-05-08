/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import shared.e;
import export3ds.*;


/**
 * realMyst MeshDataBase.
 * Just one mesh.
 */
public class Mdb
{
    //public int tag;
    public Typeid type;
    public int filesize;
    public int u2;
    public Bstr name;
    //public int u3;
    //public Bstr u3;
    //public int strCount; //number of Bstrs that follow
    //public Bstr[] strs;
    public byte b3;
    public int u4;
    public int u5;
    public int u6;
    public int u7;
    public int u8;
    public int u9;
    public int u10;
    public Sixlet start;
    public Sixlet[] bunch;
    public Quat[] quats;
    public Face[] fs;    
    public IntsIndex ii;
    public IntsFullIndex ifi;
    public wha[] whas;
    public Vertex[] trips;
    public Face[] fs2;
    public int[] extra;
    
    
    static Primary main;
    public String sourceName;
    public Mdb(IBytestream c)
    {
        this(c,"none");
    }
    public Mdb(IBytestream c, String fntrap)
    {
        int curfilenum = realmyst.rmcontext.get().curnum;
        sourceName = c.sourceName;
        
        //tag = c.readInt();
        type = Typeid.read(c);
        if(type!=Typeid.mdb)
        {
            throw new uncaughtexception("Mdb didn't have the expected magic number.");
        }
        //mdb
        filesize = c.readInt(); //filesize (including header)
        u2 = c.readInt(); e.ensure(u2,1);//=1?
        name = new Bstr(c,true);
        String trap = 
                //"aurora..sn_TOWER_observe_roof01"
                //"aurora..switchtube"
                //"aurora..boltref03"
                //"myst..portfloor"
                "myst..portstairstep"
                ;
        if(name.toString().toLowerCase().startsWith(trap.toLowerCase()))
        {
            int dummy=0;
        }
        if(c.sourceName.toLowerCase().endsWith(fntrap.toLowerCase()))
        {
            int dummy=0;
        }
        if(c.sourceName.toLowerCase().endsWith("104988212.vdb".toLowerCase()))
        {
            int dummy=0;
        }

        b3 = c.readByte(); e.ensure((int)b3,3); //always 3
        u4 = c.readInt(); e.ensure(u4,0); //always 0
        u5 = c.readInt(); e.ensure(u5,3); //always 3
        u6 = c.readInt(); e.ensure(u6,0,1); //0 or 1
        u7 = c.readInt(); e.ensure(u7,257,1,267,321,331,11,265,9,2305,283); //1,11,2305,257,265,267,283,321,331,9 //0x1,B,901,101,109,10B,11B,141,14B,9 //100101011011 are the bits used.
        //m.msg("u7="+Integer.toString(u7));
        if(u7==2305)
        {
            int altu8 = c.readInt(); e.ensure(altu8,0);
            //int altu9 = c.readInt(); e.ensure(altu9,2305);
            //int altu10 = c.readInt(); e.ensure(altu10,1);
            //int altu11 = c.readInt(); e.ensure(altu11,0);
            //Sixlet start = new Sixlet(c);
            //int vertcount = c.readInt();
            //Vertex[] verts = c.readArray(Vertex.class, vertcount);
        }
        u8 = c.readInt(); e.ensure(u8,257,1,267,321,331,11,265,9,2305,283); //0,1,11,257,265,267,283,321,331,9 //0x0,1,B,101,109,10B,11B,141,14B,9
        //m.msg("u8="+Integer.toString(u8));
        u9 = c.readInt(); e.ensure(u9,1); //1,2305 //0x1,901
        //m.msg("u9="+Integer.toString(u9));
        u10 = c.readInt(); e.ensure(u10,0); //0 or 1
        //m.msg("u10="+Integer.toString(u10));
        //m.msg(name.toString());
        //m.err("Unhandled tag: 0x"+Integer.toHexString(tag));
        
        start = new Sixlet(c); //opposite corners of bounding box?
        
        if(u7==2305)
        {
            int vertcount = c.readInt();
            Vertex[] verts = c.readArray(Vertex.class, vertcount);
        }
        else
        {
            int s1 = c.readInt(); e.ensure(s1,3,1,2);//3 (1 seemed the same, 2 ignores quats: this is probably flags.)

            int s2 = c.readInt(); //4
            bunch = c.readArray(Sixlet.class, s2); //vertex and vertex normal (vertex normal is average of face normals of adjacent faces.)
            quats = null; //I think this is actually a RGBA colour.
            if(s1!=2)
            {
                quats = c.readArray(Quat.class, s2);
            }

            int u21 = c.readInt(); //e.ensure(u21,0);
            trips = c.readArray(Vertex.class, u21); //uvw maybe?
        }
        
        if(u6==1) //u7 and u8 are also 1 in this case.
        {
            //int xu1 = c.readInt(); e.ensure(xu1,0);
            int xu2 = c.readInt(); e.ensure(xu2,0,1); //1 seems to be the same.
            int xu3 = c.readInt(); //e.ensure(xu3,3);
            
            repeataround[] repeatarounds = c.readArray(repeataround.class, xu3);
            
            /*int xu4 = c.readInt(); e.ensure(xu4,0);
            int xu5 = c.readInt(); e.ensure(xu5,18433);
            int xu6 = c.readInt(); e.ensure(xu6,1);
            int xu7 = c.readInt(); e.ensure(xu7,0);
            Sixlet xu8 = new Sixlet(c);
            int xu9 = c.readInt(); e.ensure(xu9,0);
            repeat r1 = new repeat(c);
            int xub3 = c.readInt(); e.ensure(xub3,0);
            
            int xub4 = c.readInt(); e.ensure(xub4,0);
            int xub5 = c.readInt(); e.ensure(xub5,18433);
            int xub6 = c.readInt(); e.ensure(xub6,1);
            int xub7 = c.readInt(); e.ensure(xub7,0);
            Sixlet xub8 = new Sixlet(c);
            int xub9 = c.readInt(); e.ensure(xub9,0);
            
            repeat r2 = new repeat(c);
            int xuc3 = c.readInt(); e.ensure(xuc3,0);
            int xuc4 = c.readInt(); e.ensure(xuc4,0);
            int xuc5 = c.readInt(); e.ensure(xuc5,18433);
            int xuc6 = c.readInt(); e.ensure(xuc6,1);
            int xuc7 = c.readInt(); e.ensure(xuc7,0);
            Sixlet xuc8 = new Sixlet(c);
            int xuc9 = c.readInt(); e.ensure(xuc9,0);
            
            repeat r3 = new repeat(c);
            
            int x10 = c.readInt(); e.ensure(x10,0);*/
            
            
            int x11 = c.readInt(); //e.ensure(x11,5);
            huh[] huhs = c.readArray(huh.class, x11);
            
            //int[] extraints = c.readInts(6);
            IntsIndex[] intIndexes = c.readArray(IntsIndex.class, xu3);
            IntsFullIndex[] ind = new IntsFullIndex[xu3];
            for(int i=0;i<xu3;i++)
            {
                int curnum = repeatarounds[i].r1.u24;
                ind[i] = new IntsFullIndex(c, intIndexes[i], curnum);
            }
            
            return;
        }
        
        int u22 = c.readInt(); e.ensure(u22,128,131,129,165); //131,129 seems to be the same.
        
        int u23 = c.readInt(); //e.ensure(u23,4);
        fs = c.readArray(Face.class, u23); //this maps from the longer lists to the shorter lists; or maybe it gives multiple items from the longer lists given an item from the shorter lists         //or should this be u23 instead of s2?
        
        int u24 = c.readInt(); //e.ensure(u24,2);
        whas = c.readArray(wha.class, u24);

        if(whas.length>0)
        {
            int abc = whas[0].u1;
            int dummy=0;
        }
        
        
        //if(true)return;
        
        
        
        
        //Face[] fs2=null;
        //if(u7!=267)
        if(u7==1||u7==11||u7==9)
        {
            int dummy=0;
        }
        else
        {
            int u25 = c.readInt(); //???
            
            fs2 = c.readArray(Face.class, u24);
        }
        
        //int u26 = c.readInt(); //6  6  6  4  3
        //int u27 = c.readInt(); //0  0  0  0  0
        //int u28 = c.readInt(); //6  6  6  2  16
        //int u29 = c.readInt(); //22 18 18 6  28
        //int u30 = c.readInt(); //30 24 24 10 2
        //int u31 = c.readInt(); //49 43 43 3
        //int u32 = c.readInt(); //59 53 53
        //int u33 = c.readInt(); //5  5  5
        
        
        //maps faces to material indexes, use IntsFullIndex.
        //IntsIndex:
        int u26 = c.readInt();
        //int[] extra = c.readInts(u26-1); //n-1 extra ints.
        //int u32 = c.readInt();
        extra = c.readInts(u26);

        //if(u7!=267)
        //{
        ii = new IntsIndex(c);
        ifi = new IntsFullIndex(c, ii, u24);
        //}
        
        
        /*int u33 = c.readInt();
        int[] intsIndex = c.readInts(u33);//u32+u33);//u21);
        if(u33!=1) //if there's only one index, I think it ignores these, since they must all be that value.
        {
            int[] ints = c.readInts(u32);
        }
        else
        {
            int dummy=0;
        }*/
        
        int dummy=0;
    }
    public static class IntsFullIndex
    {
        public int[] values;
        
        public IntsFullIndex(IBytestream c, IntsIndex index, int count)
        {
            if(index.count==0)
            {
                values = new int[0]; //no values to put in.
            }
            else if(index.count==1)
            {
                //only one possible values, so just fill it in.
                int value = index.indices[0];
                values = new int[count];
                for(int i=0;i<count;i++)
                {
                    values[i] = value;
                }
            }
            else
            {
                values = c.readInts(count);
            }
        }
    }
    public static class IntsIndex
    {
        public int count;
        public int[] indices;
        
        public IntsIndex(IBytestream c)
        {
            count = c.readInt();
            indices = c.readInts(count);
        }
    }
    public static class huh
    {
        short u1;
        short u2;
        int u3;
        short u4;
        int u5;
        Flt f6;
        int u7;
        Flt f8;
        int u9;
        int u10;
        Sixlet s11;
        
        public huh(IBytestream c)
        {
            u1 = c.readShort();
            u2 = c.readShort();
            u3 = c.readInt();
            u4 = c.readShort();
            u5 = c.readInt();
            f6 = new Flt(c);
            u7 = c.readInt();
            f8 = new Flt(c);
            u9 = c.readInt();
            u10 = c.readInt();
            s11 = new Sixlet(c);
        }
    }
    public static class repeataround
    {
        int xu4;
        int xu5;
        int xu6;
        int xu7;
        Sixlet xu8;
        int xu9;
        repeat r1;
        int xub3;
        
        int xxvertcount;
        Face[] xxfaces;
        
        public repeataround(IBytestream c)
        {
            xu4 = c.readInt(); e.ensure(xu4,0);
            xu5 = c.readInt(); e.ensure(xu5,18433,18753);
            xu6 = c.readInt(); e.ensure(xu6,1);
            xu7 = c.readInt(); e.ensure(xu7,0);
            xu8 = new Sixlet(c);
            xu9 = c.readInt(); //e.ensure(xu9,0,2527);
            r1 = new repeat(c);
            if(xu5==18753)
            {
                xxvertcount = c.readInt();
                xxfaces = c.readArray(Face.class, r1.u24);
            }
            xub3 = c.readInt(); e.ensure(xub3,0);
            
            int dummy=0;
        }
    }
    public static class repeat
    {
        int u22;
        int u23;
        Face[] fs;
        int u24;
        wha[] whas;
        
        public repeat(IBytestream c)
        {
            u22 = c.readInt(); e.ensure(u22,128,129);

            u23 = c.readInt(); //e.ensure(u23,4);
            fs = c.readArray(Face.class, u23); //or should this be u23 instead of s2?

            u24 = c.readInt(); //e.ensure(u24,2);
            whas = c.readArray(wha.class, u24);
        }
    }
    public static class wha
    {
        public int u1; //tag?
        public Flt f2; //normal x
        public Flt f3; //normal y
        //int u4;
        public Flt f4; //normal z
        public Flt f5; //dot product of normal and vertex.
        public int u6; //face vert1
        public int u7; //face vert2
        public int u8; //face vert3
        public Flt f9;  //vertex x
        public Flt f10; //vertex y
        public Flt f11; //vertex z
        public Flt f12;
        
        public wha(IBytestream c)
        {
            u1 = c.readInt();
            
            //9f,9b,9d,bf,
            if(u1==159||u1==155||u1==157||u1==191||u1==189||u1==185||u1==153||u1==187)
            {
                f2 = new Flt(c);
                f3 = new Flt(c);
                //u4 = c.readInt();
                f4 = new Flt(c);
                f5 = new Flt(c);
                u6 = c.readInt();
                u7 = c.readInt();
                u8 = c.readInt();
                f9 = new Flt(c);
                f10 = new Flt(c);
                f11 = new Flt(c);
                f12 = new Flt(c);
            }
            else if(u1==23||u1==21||u1==17||u1==53||u1==55)
            {
                f2 = new Flt(c);
                f3 = new Flt(c);
                f4 = new Flt(c);
                f5 = new Flt(c);
                u6 = c.readInt();
                u7 = c.readInt();
                u8 = c.readInt();
            }
            else
            {
                int dummy=0;
            }
        }
        
        public String toString()
        {
            return "f12="+f12.toString();
        }
    }
    public static class Sixlet
    {
        public Flt f1;
        public Flt f2;
        public Flt f3;
        public Flt f4;
        public Flt f5;
        public Flt f6;
        
        public Sixlet(IBytestream c)
        {
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
            f4 = new Flt(c);
            f5 = new Flt(c);
            f6 = new Flt(c);
        }
        
        public String toString()
        {
            String s = "  ";
            String result = "("+f1.toString()+s+f2.toString()+s+f3.toString()+s+f4.toString()+s+f5.toString()+s+f6.toString()+s+")";
            return result;
        }
    }
}

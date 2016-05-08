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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;

//I reverse-engineered this myself, via decompilation.
public class plClusterGroup extends uruobj
{
    //hsKeyedObject
    short fNumVerts;
    short fFormat;
    short fNumTris;
    //byte[] fData;
    public FData[] fData2;
    short[] fIndices;
    
    public Uruobjectref fMaterial;
    public int count;
    public plCluster[] subclustergroups;
    public int count2;
    public Uruobjectref[] fRegions;
    int count3;
    Uruobjectref[] fLights;
    Flt fMinDist;
    Flt fMaxDist;
    int fRenderLevel;
    
    Uruobjectref fSceneNode;
    
    public plClusterGroup(context c) throws readexception
    {
        //sub_7de8f0
        //begin plSpanTemplate
        fNumVerts = c.readShort(); //was varA
        fFormat = c.readShort(); //was varB
        fNumTris = c.readShort(); //was varC
        int fNumVerts2 = b.Int16ToInt32(fNumVerts); //was A
        int fFormat2 = b.Int16ToInt32(fFormat); //was B
        int fNumTris2 = b.Int16ToInt32(fNumTris); //was C
        int fStride = this.CalcStride(fFormat); //was X calling getx
        //fData = c.readBytes(fNumVerts2*fStride); //was block1
        fData2 = new FData[fNumVerts2];
        for(int i=0;i<fNumVerts2;i++)
            fData2[i] = new FData(c, fFormat);
        //fIndices = c.readBytes(3*2*fNumTris2); //was block2
        fIndices = c.readShorts(3*fNumTris2);
        //end plSpanTemplate
            
        
        fMaterial = new Uruobjectref(c);  //was ref, material
        count = c.readInt();
        subclustergroups = new plCluster[count];
        for(int i=0;i<count;i++)
        {
            subclustergroups[i] = new plCluster(c,fNumVerts);
        }
        //if(count>100) count = 100;
        
        count2 = c.readInt();
        fRegions = new Uruobjectref[count2]; //was refs
        for(int i=0;i<count2;i++)
        {
            fRegions[i] = new Uruobjectref(c); //visregion, sometimes empty in pots.
        }
        
        count3 = c.readInt();
        fLights = new Uruobjectref[count3]; //was refs2
        for(int i=0;i<count3;i++)
        {
            fLights[i] = new Uruobjectref(c); //empty
        }
        
        //start plLODDist
        fMinDist = new Flt(c); //was u1
        fMaxDist = new Flt(c); //was u2
        //fMaxDist = Flt.createFromJavaFloat((float)5000.0);
        //end plLODDist
        
        fRenderLevel = c.readInt(); //values are 0x40000000, 40000004, 40000005, ...8, ...9, ...c, ...d, ...e, ...f, ...1, 20000004, 20000005, and perhaps a few others.  Pots and Moul seem to agree on the kinds that appear. //was u3
        //fRenderLevel = 0x20000004;
        
        fSceneNode = new Uruobjectref(c); //scenenode //was endref
    }
    private int CalcStride(short fFormat3) //was getx(short B)
    {
        int fStride3 = 0; //was X
        
        if (((fFormat3>>>0) & 0x01)!=0) fStride3+=12; //kPosMask: a point
        if (((fFormat3>>>1) & 0x01)!=0) fStride3+=12; //kNormMask: a point
        if (((fFormat3>>>2) & 0x01)!=0) fStride3+=4; //kColorMask, an int
        if (((fFormat3>>>3) & 0x01)!=0) fStride3+=4; //kWgtIdxMask, an int
        if (((fFormat3>>>4) & 0x0F)!=0) fStride3+=12*((fFormat3>>>4) & 0x0F); //kUVWMask,0-15 points
        if (((fFormat3>>>8) & 0x03)!=0) fStride3+=4*((fFormat3>>>8) & 0x03); //kWeightMask,0-3 ints
        if (((fFormat3>>>10) & 0x01)!=0) fStride3+=4; //kColor2Mask, an int
        
        return fStride3;
    }
    public void compile(Bytedeque c)
    {
        //sub_7de8f0
        c.writeShort(fNumVerts);
        c.writeShort(fFormat);
        c.writeShort(fNumTris);
        int A = b.Int16ToInt32(fNumVerts);
        int B = b.Int16ToInt32(fFormat);
        int C = b.Int16ToInt32(fNumTris);
        int X = this.CalcStride(fFormat);
        //c.writeBytes(fData);
        c.writeArray2(fData2);
        //c.writeBytes(fIndices);
        c.writeShorts(fIndices);

        fMaterial.compile(c);
        c.writeInt(count);
        for(int i=0;i<count;i++)
        {
            subclustergroups[i].compile(c,fNumVerts);
        }

        c.writeInt(count2);
        for(int i=0;i<count2;i++)
        {
            fRegions[i].compile(c);
        }
        //c.writeInt(0);
        
        c.writeInt(count3);
        for(int i=0;i<count3;i++)
        {
            fLights[i].compile(c);
        }
        
        fMinDist.compile(c);
        fMaxDist.compile(c);
        c.writeInt(fRenderLevel);
        
        fSceneNode.compile(c);
    }
    public static class FData extends uruobj
    {
        private short fFormat;
        
        public Vertex v1,v2,v3,v4;
        int u1,u2,u3,u4;
        Vertex[] uvws;
        int[] weights;

        public FData(context c, short fFormat) throws readexception
        {
            this.fFormat = fFormat;
            if (((fFormat>>>0) & 0x01)!=0)// fStride3+=12; //kPosMask: a point
            {
                v1 = new Vertex(c);
                //v1.x = v1.x.mult((float)0.1);
                //v1.y = v1.y.mult((float)0.1);
                //v1.z = v1.z.mult((float)0.1);
            }
            if (((fFormat>>>1) & 0x01)!=0)// fStride3+=12; //kNormMask: a point
                v2 = new Vertex(c);
            if (((fFormat>>>2) & 0x01)!=0)// fStride3+=4; //kColorMask, an int ARGB?
            {
                u1 = c.readInt();
                //u1 = 0xFFFFFFFF;
            }
            if (((fFormat>>>3) & 0x01)!=0)// fStride3+=4; //kWgtIdxMask, an int
                u2 = c.readInt();
            if (((fFormat>>>4) & 0x0F)!=0)// fStride3+=12*((fFormat3>>>4) & 0x0F); //kUVWMask,0-15 points
                uvws = c.readArray(Vertex.class, ((fFormat>>>4) & 0x0F));
            if (((fFormat>>>8) & 0x03)!=0)// fStride3+=4*((fFormat3>>>8) & 0x03); //kWeightMask,0-3 ints
                weights = c.readInts(((fFormat>>>8) & 0x03));
            if (((fFormat>>>10) & 0x01)!=0)// fStride3+=4; //kColor2Mask, an int
            {
                u3 = c.readInt();
                //u3 = 0xAAAAAAAA;
                //u3 = 0xFFFFFFFF;
            }
            if(u3==0)
            {
                int dummy2=0;
            }
            int dummy=0;
        }
        
        public void compile(Bytedeque c)
        {
            if (((fFormat>>>0) & 0x01)!=0)// fStride3+=12; //kPosMask: a point
                v1.compile(c);
            if (((fFormat>>>1) & 0x01)!=0)// fStride3+=12; //kNormMask: a point
                v2.compile(c);
            if (((fFormat>>>2) & 0x01)!=0)// fStride3+=4; //kColorMask, an int ARGB?
                c.writeInt(u1);
            if (((fFormat>>>3) & 0x01)!=0)// fStride3+=4; //kWgtIdxMask, an int
                c.writeInt(u2);
            if (((fFormat>>>4) & 0x0F)!=0)// fStride3+=12*((fFormat3>>>4) & 0x0F); //kUVWMask,0-15 points
                c.writeArray(uvws);
            if (((fFormat>>>8) & 0x03)!=0)// fStride3+=4*((fFormat3>>>8) & 0x03); //kWeightMask,0-3 ints
                c.writeInts(weights);
            if (((fFormat>>>10) & 0x01)!=0)// fStride3+=4; //kColor2Mask, an int
                c.writeInt(u3);
        }
    }
    public static class plCluster extends uruobj //was subclustergroup
    {
        byte fCode;
        Flt fPosScale;
        public int count;
        public plSpanInstance[] fInstances;
        
        public plCluster(context c, short numVerts) throws readexception //was subclustergroup(context c, short varA)
        {
            //plSpanEncoding
            fCode = c.readByte(); //was varF
            fPosScale = new Flt(c); //was u2
            //end plSpanEncoding
            
            count = c.readInt();
            fInstances = new plSpanInstance[count]; //was subsubcgs
            for(int i=0;i<count;i++)
            {
                fInstances[i] = new plSpanInstance(c,fCode, numVerts);
            }
        }
        
        public void compile(Bytedeque c, short varA)
        {
            c.writeByte(fCode);
            fPosScale.compile(c);
            
            c.writeInt(count);
            for(int i=0;i<count;i++)
            {
                fInstances[i].compile(c,fCode, varA);
            }
        }
        
        public static class plSpanInstance extends uruobj
        {
            //byte[] fL2W; //was u1, float[3][4]
            public Flt[][] fL2W = new Flt[3][4];
            byte[] fPosDelta; //was u2, vertices?
            byte[] fCol; //was u3, color data
            
            public plSpanInstance(context c, byte fCode2, short numVerts) throws readexception //was subsubcg(context c, byte varF, short varA)
            {
                int numVerts2 = b.Int16ToInt32(numVerts); //was varG
                //fL2W = c.readBytes(48);
                for(int i=0;i<3;i++)
                    for(int j=0;j<4;j++)
                        fL2W[i][j] = new Flt(c);
                float x = fL2W[0][3].toJavaFloat();
                float y = fL2W[1][3].toJavaFloat();
                if(x<100 && x>-100 && y<100 && y>-100)
                {
                    int dummy=0;
                }
                int posStride = this.CalcPosStride(fCode2); //was special1 calling getspecial1
                fPosDelta = c.readBytes(numVerts2*posStride);
                int colStride = this.CalcColStride(fCode2); //was special2 calling getspecial2
                fCol = c.readBytes(numVerts2*colStride);
                if(posStride!=0)
                {
                    int dummy=0;
                }
                if(colStride!=0)
                {
                    int dummy=0;
                }
            }
            
            public void compile(Bytedeque c, byte varF, short varA)
            {
                int varG = b.Int16ToInt32(varA);
                //c.writeBytes(fL2W);
                for(int i=0;i<3;i++)
                    for(int j=0;j<4;j++)
                        fL2W[i][j].compile(c);
                int special1 = this.CalcPosStride(varF);
                c.writeBytes(fPosDelta);
                int special2 = this.CalcColStride(varF);
                c.writeBytes(fCol);
            }
            
            private int CalcColStride(byte varF) //sub_7d5200, according to libPlasma(PlasmaTools), this always returns 0 due to a bug in Cyan code.
            {
                int v1 = (b.ByteToInt32(varF) & 0x0F);
                if(v1<=64)
                {
                    if(v1==64) return 2;
                    if(v1==16 || v1==32) return 1;
                    return 0;
                }
                if(v1==128) return 3;
                if(v1!=256) return 0;
                return 4;
            }
            private int CalcPosStride(byte varF) //returns number of bytes taken up.
            {
                int v1 = (b.ByteToInt32(varF) & 0x0F);
                switch(v1)
                {
                    case 1: return 3; //kPos888
                    case 2: return 6; //kPos161616
                    case 4: return 4; //kPos101010
                    case 8: return 1; //kPos008
                    default:
                        //throw new readexception("plclustergroup: problem in subsubcg.");
                        return 0;
                }
            }
        }
    }
}

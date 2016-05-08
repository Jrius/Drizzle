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
import shared.Format;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plDynamicEnvMap extends uruobj
{
    public plCubicRenderTarget faces;
    public Vertex fPos;
    //Flt[] u3; //8
    public Flt hither, yon, fogStart;
    public Rgba color;
    public Flt refreshRate;
    public byte includeCharacters;
    public int numVisRegions;
    public Uruobjectref[] visRegions;
    public int numVisRegionNames;
    public Urustring[] visRegionNames;
    public Uruobjectref rootNode;
    
    public plDynamicEnvMap(context c) throws readexception
    {
        faces = new plCubicRenderTarget(c);
        fPos = new Vertex(c);
        //u3 = c.readArray(Flt.class, 8);
        hither = new Flt(c);
        yon = new Flt(c);
        fogStart = new Flt(c);
        color = new Rgba(c);
        refreshRate = new Flt(c);
        includeCharacters = c.readByte();
        numVisRegions = c.readInt();
        visRegions = c.readArray(Uruobjectref.class, numVisRegions);
        if(c.readversion==6||c.readversion==4||c.readversion==7)
        {
            numVisRegionNames = c.readInt();
            visRegionNames = c.readArray(Urustring.class, numVisRegionNames);
            rootNode = new Uruobjectref(c);
        }
    }

    private plDynamicEnvMap() {}
    
    public static plDynamicEnvMap createEmpty()
    {
        return new plDynamicEnvMap();
    }
    
    public static plDynamicEnvMap createFromCamMap(prpobjects.plDynamicCamMap dcm, prpfile prp)
    {
        plDynamicEnvMap result = plDynamicEnvMap.createEmpty();
        
        result.faces = plCubicRenderTarget.createFromRenderTarget(dcm.target);
        
        PrpRootObject rn = prp.findObjectWithRef(dcm.rootNode);
        if (rn != null)
        {
            prpobjects.plCoordinateInterface ci = prp.findObjectWithRef(rn.castToSceneObject().coordinateinterface).castTo();
            float x = Float.intBitsToFloat(ci.localToWorld.xmatrix[3]);
            float y = Float.intBitsToFloat(ci.localToWorld.xmatrix[7]);
            float z = Float.intBitsToFloat(ci.localToWorld.xmatrix[11]);
            
            result.fPos = new Vertex(x, y, z);
        }
        else
            result.fPos = new Vertex(0,0,0);
        
        result.hither = dcm.hither;
        result.yon = dcm.yon;
        result.fogStart = dcm.fogStart;
        result.color = Rgba.createFromVals(dcm.color.r.toJavaFloat(), dcm.color.g.toJavaFloat(), dcm.color.b.toJavaFloat(), dcm.color.a.toJavaFloat());
        //result.refreshRate = dcm.refreshRate; // generally bad practice
        result.refreshRate = Flt.zero(); // FPS friendly
        //result.u4 = dcm.includeCharacters; // generally bad practice
        result.includeCharacters = 0; // no giant looking avatars in the reflection
        
        result.numVisRegions = dcm.numVisRegions;
        result.visRegions = new Uruobjectref[result.numVisRegions];
        System.arraycopy(dcm.visRegions, 0, result.visRegions, 0, result.numVisRegions);
        
        //result.numVisRegionNames = ; // bah, don't bother, PotS doesn't have it.
        //result.visRegionNames = ;
        //result.rootNode = ;
        return result;
    }
    
    public void compile(Bytedeque c)
    {
        faces.compile(c);
        fPos.compile(c);
        //c.writeArray(u3);
        hither.compile(c);
        yon.compile(c);
        fogStart.compile(c);
        color.compile(c);
        refreshRate.compile(c);
        c.writeByte(includeCharacters);
        c.writeInt(numVisRegions);
        c.writeArray2(visRegions);
        //skip this next block, since it's moul-only.
        /*if(c.readversion==6)
        {
            xsubcount = c.readInt();
            xsubs = c.readVector(Urustring.class, xsubcount);
            xref2 = new Uruobjectref(c);
        }*/
    }
    public static class plCubicRenderTarget extends uruobj //plCubicRenderTarget probably.
    {
        public plRenderTarget parent; //sub_566C80
        public plRenderTarget child1;
        public plRenderTarget child2;
        public plRenderTarget child3;
        public plRenderTarget child4;
        public plRenderTarget child5;
        public plRenderTarget child6;
        
        public plCubicRenderTarget(context c) throws readexception
        {
            //sub4e0580 in hexisle
            parent = new plRenderTarget(c);
            if(c.readversion==3||c.readversion==6)
            {
                child1 = new plRenderTarget(c);
                child2 = new plRenderTarget(c);
                child3 = new plRenderTarget(c);
                child4 = new plRenderTarget(c);
                child5 = new plRenderTarget(c);
                child6 = new plRenderTarget(c);
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Uruobjectref ref1 = new Uruobjectref(c);
                child1 = new plRenderTarget(c);
                Uruobjectref ref2 = new Uruobjectref(c);
                child2 = new plRenderTarget(c);
                Uruobjectref ref3 = new Uruobjectref(c);
                child3 = new plRenderTarget(c);
                Uruobjectref ref4 = new Uruobjectref(c);
                child4 = new plRenderTarget(c);
                Uruobjectref ref5 = new Uruobjectref(c);
                child5 = new plRenderTarget(c);
                Uruobjectref ref6 = new Uruobjectref(c);
                child6 = new plRenderTarget(c);
            }
        }

        private plCubicRenderTarget() {}

        public static plCubicRenderTarget createEmpty()
        {
            return new plCubicRenderTarget();
        }

        public static plCubicRenderTarget createFromRenderTarget(plRenderTarget tgt)
        {
            plCubicRenderTarget result = plCubicRenderTarget.createEmpty();
            result.parent = tgt;
            result.child1 = tgt;
            result.child2 = tgt;
            result.child3 = tgt;
            result.child4 = tgt;
            result.child5 = tgt;
            result.child6 = tgt;
            return result;
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            child1.compile(c);
            child2.compile(c);
            child3.compile(c);
            child4.compile(c);
            child5.compile(c);
            child6.compile(c);
        }
    }
    
    //sub_566C80
    public static class plRenderTarget extends uruobj
    {
        public x0003Bitmap parent;
        public short u1;
        public short u2;
        public byte u3;
        public Flt xu4;
        public Flt xu5;
        public Flt xu6;
        public Flt xu7;
        public short xu8;
        public short xu9;
        public short xu10;
        public short xu11;
        public byte u12;
        public byte u13;
        
        public plRenderTarget(context c)
        {
            parent = new x0003Bitmap(c);
            u1 = c.readShort();
            u2 = c.readShort();
            if(c.readversion==7)
            {
                //this extra byte must be here, because the next one is used to determine what data to read next.
                byte u4 = c.readByte();
            }
            u3 = c.readByte();
            if(u3!=0)
            {
                xu4 = new Flt(c);
                xu5 = new Flt(c);
                xu6 = new Flt(c);
                xu7 = new Flt(c);
            }
            else
            {
                xu8 = c.readShort();
                xu9 = c.readShort();
                xu10 = c.readShort();
                xu11 = c.readShort();
            }
            u12 = c.readByte();
            u13 = c.readByte();
        }

        private plRenderTarget() {}

        public static plRenderTarget createEmpty()
        {
            return new plRenderTarget();
        }

        public static plRenderTarget createDefault()
        {
            plRenderTarget result = plRenderTarget.createEmpty();
            
            return result;
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeShort(u1);
            c.writeShort(u2);
            c.writeByte(u3);
            if(u3!=0)
            {
                xu4.compile(c);
                xu5.compile(c);
                xu6.compile(c);
                xu7.compile(c);
            }
            else
            {
                c.writeShort(xu8);
                c.writeShort(xu9);
                c.writeShort(xu10);
                c.writeShort(xu11);
            }
            c.writeByte(u12);
            c.writeByte(u13);
        }
    }
    /*public static class subdynamicenvmap extends uruobj
    {
        Urustring u1;
        
        public subdynamicenvmap(context c)
        {
            m.msg("dynamicenvmap: untested.");
            u1 = new Urustring(c);
        }
    }*/
}

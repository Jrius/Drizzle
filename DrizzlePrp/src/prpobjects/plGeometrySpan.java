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

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;
import shared.*;


public class plGeometrySpan extends uruobj
{
    Transmatrix localToWorld;
    Transmatrix worldToLocal;
    BoundingBox localBounds;
    Transmatrix OBBtoLocal;
    Transmatrix localToOBB;
    int baseMatrix;
    byte numMatrices;
    short localUVWChans;
    short maxBoneIdx;
    short penBoneIdx;
    Flt minDist;
    Flt maxDist;
    byte format;
    int props;
    int numVerts;
    int numIndices;
    int u1;
    byte b1;
    int decalLevel;
    Flt waterHeight;
    
    byte[] rawdata;
    
    Rgba[] colors;
    int[] diffuseRgba;
    int[] specularRgba;
    
    short[] indexData;
    int instanceGroup;
    
    public plGeometrySpan(context c) throws readexception
    {
        localToWorld = new Transmatrix(c);
        worldToLocal = new Transmatrix(c);
        localBounds = new BoundingBox(c);
        OBBtoLocal = new Transmatrix(c);
        localToOBB = new Transmatrix(c);
        baseMatrix = c.readInt();
        numMatrices = c.readByte();
        localUVWChans = c.readShort();
        maxBoneIdx = c.readShort();
        penBoneIdx = c.readShort();
        minDist = new Flt(c);
        maxDist = new Flt(c);
        format = c.readByte();
        props = c.readInt();
        numVerts = c.readInt();
        numIndices = c.readInt();
        u1 = c.readInt();
        b1 = c.readByte();
        decalLevel = c.readInt();
        if((props&0x800)!=0)
        {
            waterHeight = new Flt(c);
        }
        if(numVerts>0)
        {
            int stride = ((format&0xF)+2)*12;
            stride += ((format&0x30)>>4)*4;
            if((format&0x40)!=0) stride += 4;
            rawdata = c.readBytes(stride*numVerts);
            
            colors = c.readArray(Rgba.class,numVerts*2);
            diffuseRgba = c.readInts(numVerts);
            specularRgba = c.readInts(numVerts);
        }
        indexData = c.readShorts(numIndices);
        instanceGroup = c.readInt(); e.ensure(instanceGroup,0); //we don't handle the case where it's not 0.
        if(instanceGroup!=0)
        {
            m.err("Cannot currently handle PlGeometrySpans with non-zero instanceGroup count.");
        }
    }
    
    public void compile(Bytedeque c)
    {
        localToWorld.compile(c);
        worldToLocal.compile(c);
        localBounds.compile(c);
        OBBtoLocal.compile(c);
        localToOBB.compile(c);
        c.writeInt(baseMatrix);
        c.writeByte(numMatrices);
        c.writeShort(localUVWChans);
        c.writeShort(maxBoneIdx);
        c.writeShort(penBoneIdx);
        minDist.compile(c);
        maxDist.compile(c);
        c.writeByte(format);
        c.writeInt(props);
        c.writeInt(numVerts);
        c.writeInt(numIndices);
        c.writeInt(u1);
        c.writeByte(b1);
        c.writeInt(decalLevel);
        if((props&0x800)!=0)
        {
            waterHeight.compile(c);
        }
        if(numVerts>0)
        {
            int stride = ((format&0xF)+2)*12;
            stride += ((format&0x30)>>4)*4;
            if((format&0x40)!=0) stride += 4;
            c.writeBytes(rawdata);
            c.writeArray2(colors);
            c.writeInts(diffuseRgba);
            c.writeInts(specularRgba);
            
        }
        c.writeShorts(indexData);

        c.writeInt(instanceGroup);
        if(instanceGroup!=0)
        {
            m.err("Cannot currently write PlGeometrySpans with non-zero instanceGroup count.");
        }
    }
    
}

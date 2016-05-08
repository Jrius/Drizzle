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
import shared.readexception;
//import java.util.Vector;


public class plDynamicCamMap extends uruobj
{
    public plDynamicEnvMap.plRenderTarget target;
    //public Flt[] u1;
    public Flt hither, yon, fogStart;
    public Rgba color;
    public Flt refreshRate;
    public byte includeCharacters;
    public Uruobjectref camera;
    public Uruobjectref rootNode;
    public byte numTargetNodes;
    public Uruobjectref[] targetNodes;
    public int numVisRegions;
    public Uruobjectref[] visRegions;
    public int numVisRegionNames;
    public Urustring[] visRegionNames;
    public Uruobjectref disableTexture;
    public byte numMatLayers;
    public Uruobjectref[] matLayers;
    
    public plDynamicCamMap(context c) throws readexception
    {
        e.ensure(c.readversion==6||c.readversion==4);
        
        target = new plDynamicEnvMap.plRenderTarget(c);
        //u1 = c.readArray(Flt.class, 8);
        hither = new Flt(c);
        yon = new Flt(c);
        fogStart = new Flt(c);
        color = new Rgba(c);
        refreshRate = new Flt(c);
        includeCharacters = c.readByte();
        camera = new Uruobjectref(c);
        rootNode = new Uruobjectref(c);
        numTargetNodes = c.readByte();
        int count = b.ByteToInt32(numTargetNodes); //should this be signed instead?
        targetNodes = c.readArray(Uruobjectref.class, count);
        numVisRegions = c.readInt();
        visRegions = c.readArray(Uruobjectref.class, numVisRegions);
        numVisRegionNames = c.readInt();
        visRegionNames = c.readArray(Urustring.class, numVisRegionNames);
        disableTexture = new Uruobjectref(c);
        numMatLayers = c.readByte();
        int count2 = b.ByteToInt32(numMatLayers);
        matLayers = c.readArray(Uruobjectref.class, count2);
        
        //throw new shared.readwarningexception("plDynamicCamMap: can read okay, but failing in order to ignore.");
    }
    
    public void compile(Bytedeque c)
    {
        m.warn("compile not implemented.",this.toString());
        m.warn("not tested with pots.",this.toString());
    }
    
}

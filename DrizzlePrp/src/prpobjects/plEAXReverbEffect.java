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
import shared.Vertex;
import uru.context;
import uru.Bytedeque;
import shared.m;
import shared.readexception;

// This class comes from HSPlasma
public class plEAXReverbEffect extends uruobj
{
    public plSingleModifier parent;
    public Uruobjectref softRegion;
    public int environment;
    public Flt environmentSize;
    public Flt environmentDiffusion;
    public int room;
    public int roomHF;
    public int roomLF;
    public Flt decayTime;
    public Flt decayHFRatio;
    public Flt decayLFRatio;
    public int reflections;
    public Flt reflectionsDelay;
    public int reverb;
    public Flt reverbDelay;
    public Flt echoTime;
    public Flt echoDepth;
    public Flt modulationTime;
    public Flt modulationDepth;
    public Flt airAbsorptionHF;
    public Flt HFReference;
    public Flt LFReference;
    public Flt roomRolloffFactor;
    public int flags;
    public int numApertures;
    public EAXaperture[] apertures;
    
    public plEAXReverbEffect(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        softRegion = new Uruobjectref(c);
        environment = c.readInt();
        environmentSize = new Flt(c);
        environmentDiffusion = new Flt(c);
        room = c.readInt();
        roomHF = c.readInt();
        roomLF = c.readInt();
        decayTime = new Flt(c);
        decayHFRatio = new Flt(c);
        decayLFRatio = new Flt(c);
        reflections = c.readInt();
        reflectionsDelay = new Flt(c);
        reverb = c.readInt();
        reverbDelay = new Flt(c);
        echoTime = new Flt(c);
        echoDepth = new Flt(c);
        modulationTime = new Flt(c);
        modulationDepth = new Flt(c);
        airAbsorptionHF = new Flt(c);
        HFReference = new Flt(c);
        LFReference = new Flt(c);
        roomRolloffFactor = new Flt(c);
        flags = c.readInt();
        
        // apertures (not in pots)
        numApertures = c.readInt();
        
        apertures = new EAXaperture[numApertures];
        for (int i=0; i<numApertures; i++)
        {
            apertures[i] = new EAXaperture();
            apertures[i].x = new Vertex(c);
            apertures[i].y = new Vertex(c);
            apertures[i].z = new Vertex(c);
        }
    }
    
    @Override
    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("Can't compile a reverb effect for PotS...");
    }
    
    
    public static class EAXaperture
    {
        Vertex x;
        Vertex y;
        Vertex z;
    }
}

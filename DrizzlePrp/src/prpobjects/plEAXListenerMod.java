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

//I reverse-engineered this myself, via decompilation.
public class plEAXListenerMod extends uruobj
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
    
    public plEAXListenerMod(context c) throws readexception
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
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        softRegion.compile(c);
        c.writeInt(environment);
        environmentSize.compile(c);
        environmentDiffusion.compile(c);
        c.writeInt(room);
        c.writeInt(roomHF);
        c.writeInt(roomLF);
        decayTime.compile(c);
        decayHFRatio.compile(c);
        decayLFRatio.compile(c);
        c.writeInt(reflections);
        reflectionsDelay.compile(c);
        c.writeInt(reverb);
        reverbDelay.compile(c);
        echoTime.compile(c);
        echoDepth.compile(c);
        modulationTime.compile(c);
        modulationDepth.compile(c);
        airAbsorptionHF.compile(c);
        HFReference.compile(c);
        LFReference.compile(c);
        roomRolloffFactor.compile(c);
        c.writeInt(flags);
    }
    
    private plEAXListenerMod() {}
    
    public static plEAXListenerMod createEmpty()
    {
        return new plEAXListenerMod();
    }
    
    public static plEAXListenerMod createFromReverbEffect(prpobjects.plEAXReverbEffect eaxre)
    {
        plEAXListenerMod result = plEAXListenerMod.createEmpty();
        
        result.parent = eaxre.parent;
        result.softRegion = eaxre.softRegion;
        result.environment = eaxre.environment;
        result.environmentSize = eaxre.environmentSize;
        result.environmentDiffusion = eaxre.environmentDiffusion;
        result.room = eaxre.room;
        result.roomHF = eaxre.roomHF;
        result.roomLF = eaxre.roomLF;
        result.decayTime = eaxre.decayTime;
        result.decayHFRatio = eaxre.decayHFRatio;
        result.decayLFRatio = eaxre.decayLFRatio;
        result.reflections = eaxre.reflections;
        result.reflectionsDelay = eaxre.reflectionsDelay;
        result.reverb = eaxre.reverb;
        result.reverbDelay = eaxre.reverbDelay;
        result.echoTime = eaxre.echoTime;
        result.echoDepth = eaxre.echoDepth;
        result.modulationTime = eaxre.modulationTime;
        result.modulationDepth = eaxre.modulationDepth;
        result.airAbsorptionHF = eaxre.airAbsorptionHF;
        result.HFReference = eaxre.HFReference;
        result.LFReference = eaxre.LFReference;
        result.roomRolloffFactor = eaxre.roomRolloffFactor;
        result.flags = eaxre.flags;
        
        return result;
    }
}

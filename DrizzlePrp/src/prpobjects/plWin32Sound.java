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
import uru.Bytestream;
import uru.Bytedeque;
import uru.context; import shared.readexception;
import shared.m;

public class plWin32Sound extends uruobj
{
    public PlSound parent;
    public byte channel; //0 means left channel, 1 means right channel.
    
    public plWin32Sound(context c) throws readexception
    {
        parent = new PlSound(c);
        channel = c.readByte();
    }

    private plWin32Sound() {}
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeByte(channel);
    }
    
    public static plWin32Sound createEmpty()
    {
        return new plWin32Sound();
    }
    
    public static class PlSound extends uruobj
    {
        public plSynchedObject parent;
        public byte playing;
        public Double64 time;
        public int maxfalloff;
        public int minfalloff;
        public Flt curvolume;
        public Flt desiredvolume;
        public int outervol;
        public int innercone;
        public int outercone;
        public Flt fadedvolume;
        public int properties;
        public byte type;
        public byte priority;
        public plFadeParams fadeInParams;
        public plFadeParams fadeOutParams;
        public Uruobjectref softRegion;
        public Uruobjectref dataBuffer; //plSoundBuffer
        public plEAXSourceSettings EAXSettings;
        public Uruobjectref softOcclusionRegion;
        
        //short xu1;
        Urustring xu1;

        public static final int kProp3D = 0x1;
        public static final int kPropDisableLOD = 0x2;
        public static final int kPropLooping = 0x4;
        public static final int kPropAutoStart = 0x8;
        public static final int kPropLoadOnly = 0x10;
        public static final int kPropLoadOnlyOnCall = 0x20;
        public static final int kPropFullyDisabled = 0x40;
        public static final int kPropDontFade = 0x80;
        public static final int kPropIncidental = 0x100;


        public PlSound(context c) throws readexception
        {
            parent = new plSynchedObject(c);
            playing = c.readByte();
            time = new Double64(c);
            maxfalloff = c.readInt();
            minfalloff = c.readInt();
            curvolume = new Flt(c);
            desiredvolume = new Flt(c);
            outervol = c.readInt();
            innercone = c.readInt();
            outercone = c.readInt();
            fadedvolume = new Flt(c);
            properties = c.readInt();
            type = c.readByte();
            priority = c.readByte();
            if(c.readversion==4||c.readversion==7)
            {
                //xu1 = c.readShort();
                xu1 = new Urustring(c);
                if(xu1.unencryptedString.length>0)
                {
                    int dummy=0;
                }
            }
            fadeInParams = new plFadeParams(c);
            fadeOutParams = new plFadeParams(c);
            softRegion = new Uruobjectref(c);
            dataBuffer = new Uruobjectref(c);
            EAXSettings = new plEAXSourceSettings(c);
            softOcclusionRegion = new Uruobjectref(c);
        }

        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeByte(playing);
            time.compile(c);
            c.writeInt(maxfalloff);
            c.writeInt(minfalloff);
            curvolume.compile(c);
            desiredvolume.compile(c);
            c.writeInt(outervol);
            c.writeInt(innercone);
            c.writeInt(outercone);
            fadedvolume.compile(c);
            c.writeInt(properties);
            c.writeByte(type);
            c.writeByte(priority);
            fadeInParams.compile(c);
            fadeOutParams.compile(c);
            softRegion.compile(c);
            dataBuffer.compile(c);
            EAXSettings.compile(c);
            softOcclusionRegion.compile(c);

        }

        /*public String toString()
        {
        }*/
    }
    
    public static class plFadeParams extends uruobj
    {
        public Flt lengthInSecs;
        public Flt volStart;
        public Flt volEnd;
        public byte type;
        public Flt curTime;
        public int stopWhenDone;
        public int fadeSoftVol;
        
        public plFadeParams(context c)
        {
            lengthInSecs = new Flt(c);
            volStart = new Flt(c);
            volEnd = new Flt(c);
            type = c.readByte();
            curTime = new Flt(c); //can be -1.0
            stopWhenDone = c.readInt();
            fadeSoftVol = c.readInt();
        }
        
        public void compile(Bytedeque c)
        {
            lengthInSecs.compile(c);
            volStart.compile(c);
            volEnd.compile(c);
            c.writeByte(type);
            curTime.compile(c);
            c.writeInt(stopWhenDone);
            c.writeInt(fadeSoftVol);
        }
    }
    
    public static class plEAXSourceSettings extends uruobj
    {
        
        byte enabled;
        
        //these are only present if enabled is set.
        short room;
        short roomHF;
        byte roomAuto;
        byte roomHFAuto;
        short outsideVolHF;
        public Flt airAbsorptionFactor;
        public Flt roomRolloffFactor;
        public Flt dopplerFactor;
        public Flt rolloffFactor;
        public plEAXSourceSoftSettings softStarts;
        public plEAXSourceSoftSettings softEnds;
        public Flt occlusionSoftValue;
        
        Flt xu1;
        plEAXSourceSoftSettings xsoft;
        
        public plEAXSourceSettings(context c) throws readexception
        {
            enabled = c.readByte();
            if(enabled!=0)
            {
                room = c.readShort();
                roomHF = c.readShort();
                roomAuto = c.readByte();
                roomHFAuto = c.readByte();
                outsideVolHF = c.readShort();
                airAbsorptionFactor = new Flt(c);
                roomRolloffFactor = new Flt(c);
                dopplerFactor = new Flt(c);
                rolloffFactor = new Flt(c);
                if(c.readversion==3||c.readversion==6)
                {
                    softStarts = new plEAXSourceSoftSettings(c);
                    softEnds = new plEAXSourceSoftSettings(c);
                    occlusionSoftValue = new Flt(c);
                    if(shared.State.AllStates.getStateAsBoolean("reportEaxSourceSettings")) m.msg("PlEaxSourceSettings(pots/moul): ",softStarts.toString()+" : "+softEnds.toString()+" : "+occlusionSoftValue.toString());
                }
                else if(c.readversion==4)
                {
                    xu1 = new Flt(c);
                    xsoft = new plEAXSourceSoftSettings(c);
                    if(shared.State.AllStates.getStateAsBoolean("reportEaxSourceSettings")) m.msg("PlEaxSourceSettings(crow/mystv): ",xu1.toString()+" : "+xsoft.toString());
                    
                    //set values for writing...
                    occlusionSoftValue = xu1;
                    softStarts = xsoft;
                    softEnds = new plEAXSourceSoftSettings();
                    softEnds.occlusion = 0;
                    softEnds.occlusionDirectRatio = new Flt((float)1.0);
                    softEnds.occlusionLFRatio = new Flt((float)0.25);
                    softEnds.occlusionRoomRatio = new Flt((float)1.5);
                    
                    /*
                    occlusionSoftValue = Flt.zero();
                    softStarts = new plEAXSourceSoftSettings();
                    softStarts.occlusion = 0;
                    softStarts.occlusionDirectRatio = new Flt((float)1.0);
                    softStarts.occlusionLFRatio = new Flt((float)0.25);
                    softStarts.occlusionRoomRatio = new Flt((float)1.5);
                    softEnds = softStarts;
                    
                    m.warn("Sound: Fudging some eax settings.");
                    //throw new readexception("plEAXSourceSetting: can read, but throwing error to skip."); //*/
                }
                else
                {
                    m.throwUncaughtException("Unimplemented.");
                }
            }
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(enabled);
            if(enabled!=0)
            {
                c.writeShort(room);
                c.writeShort(roomHF);
                c.writeByte(roomAuto);
                c.writeByte(roomHFAuto);
                c.writeShort(outsideVolHF);
                airAbsorptionFactor.compile(c);
                roomRolloffFactor.compile(c);
                dopplerFactor.compile(c);
                rolloffFactor.compile(c);
                softStarts.compile(c);
                softEnds.compile(c);
                occlusionSoftValue.compile(c);
            }
        }
    }
    
    public static class plEAXSourceSoftSettings extends uruobj
    {
        short occlusion;
        Flt occlusionLFRatio;
        Flt occlusionRoomRatio;
        Flt occlusionDirectRatio;
        
        public plEAXSourceSoftSettings(context c)
        {
            occlusion = c.readShort();
            occlusionLFRatio = new Flt(c);
            occlusionRoomRatio = new Flt(c);
            occlusionDirectRatio = new Flt(c);
        }
        
        private plEAXSourceSoftSettings()
        {
        }
        
        public void compile(Bytedeque c)
        {
            c.writeShort(occlusion);
            occlusionLFRatio.compile(c);
            occlusionRoomRatio.compile(c);
            occlusionDirectRatio.compile(c);
        }
        
        public String toString()
        {
            return "occ:"+Short.toString(occlusion)+"LF:"+occlusionLFRatio.toString()+"Room:"+occlusionRoomRatio.toString()+"Direct:"+occlusionDirectRatio.toString();
        }
    }
}

    

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
//import java.util.Vector;

//plSoundBuffer
//x0029SoundBuffer
public class plSoundBuffer extends uruobj
{
    //Objheader xheader;
    public static final int kIsExternal       = 0x01;
    public static final int kAlwaysExternal   = 0x02;
    public static final int kOnlyLeftChannel  = 0x04;
    public static final int kOnlyRightChannel = 0x08;
    public static final int kStreamCompressed = 0x10;
    
    public int flags;
    public int datalength; //the length of the uncompressed file.
    public Urustring oggfile;
    public short channels;
    public plWAVHeader wavHeader;
    public byte[] xinternaldata;
    
    public plSoundBuffer(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        flags = c.readInt();
        datalength = c.readInt();
        oggfile = new Urustring(c);
        wavHeader = new plWAVHeader(c);
        if((flags & kIsExternal)==0)
        {
            m.msg("haven't tested this: soundbuffer");
            xinternaldata = c.readBytes(datalength);
        }
        if(shared.State.AllStates.getStateAsBoolean("reportOggFiles")) m.msg("Ogg File: ",oggfile.toString());
    }
    public void compile(Bytedeque data)
    {
        data.writeInt(flags);
        data.writeInt(datalength);
        oggfile.compile(data);
        wavHeader.compile(data);
        if((flags & kIsExternal)==0)
        {
            m.msg("haven't tested this: soundbuffer");
            data.writeBytes(xinternaldata);
        }
    }
    
    public static class plWAVHeader extends uruobj
    {
        short formattag;
        short numchannels;
        int numSamplesPerSec;
        int AvgBytesPerSec;
        short blockAlign;
        short bitsPerSample;
        
        public plWAVHeader(context c)
        {
            formattag = c.readShort();
            numchannels = c.readShort();
            numSamplesPerSec = c.readInt();
            AvgBytesPerSec = c.readInt();
            blockAlign = c.readShort();
            bitsPerSample = c.readShort();
        }
        
        public void compile(Bytedeque data)
        {
            data.writeShort(formattag);
            data.writeShort(numchannels);
            data.writeInt(numSamplesPerSec);
            data.writeInt(AvgBytesPerSec);
            data.writeShort(blockAlign);
            data.writeShort(bitsPerSample);
        }
    }
}

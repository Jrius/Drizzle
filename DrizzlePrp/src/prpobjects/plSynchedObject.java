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

/**
 *
 * @author user
 */
public class plSynchedObject extends uruobj
{
    public static final int kDontDirty = 0x1,
                            kSendReliably = 0x2,
                            kHasConstantNetGroup = 0x4,
                            kDontSynchGameMessages = 0x8,
                            kExcludePersistentState = 0x10,
                            kExcludeAllPersistentState = 0x20,
                            kLocalOnly = kExcludeAllPersistentState | kDontSynchGameMessages,
                            kHasVolatileState = 0x40,
                            kAllStateIsVolatile = 0x80;
    
    public static final int kBCastToClients = 0x1,
                            kForceFullSend = 0x2,
                            kSkipLocalOwnershipCheck = 0x4,
                            kSendImmediately = 0x8,
                            kDontPersistOnServer = 0x10,
                            kUseRelevanceRegions = 0x20,
                            kNewState = 0x40,
                            kIsAvatarState = 0x80;
    
    
    //Objheader xheader;
    //x0002Keyedobject parent;
    public int flags;
    short xstringcount;
    Wpstr[] sdllinks;
    
    short xstringcount2;
    Wpstr[] sdllinks2;
    
    /*public PlSynchedObject(context c) //handy
    {
        this(c);//,false);
    }*/
    private plSynchedObject(){}
    public static plSynchedObject createEmpty()
    {
        return new plSynchedObject();
    }
    public static plSynchedObject createDefault()
    {
        plSynchedObject result = plSynchedObject.createEmpty();
        result.flags = 0;
        return result;
    }
    public plSynchedObject(context c)//,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        //parent = new x0002Keyedobject(data);
        flags = data.readInt(); e.ensureflags(flags,0x00,0x04,0x0C,0x10,0x20,0x28,0x38,0x80,0x84,0x8C); //if fails, check cobbs
        if(c.readversion==3||c.readversion==6)
        {
            if ((flags & 0x10)!=0)
            {
                xstringcount = data.readShort();
                int count = b.Int16ToInt32(xstringcount);
                sdllinks = new Wpstr[count];
                for(int i=0;i<count;i++)
                {
                    sdllinks[i] = new Wpstr(c);
                }

            }
            if((flags & 0x40)!=0)
            {
                m.warn("plsynchedobject: haven't tested this yet.");
                xstringcount2 = data.readShort();
                int count2 = b.Int16ToInt32(xstringcount2);
                sdllinks2 = new Wpstr[count2];
                for(int i=0;i<count2;i++)
                {
                    sdllinks2[i] = new Wpstr(c);
                }
                //haven't implemented this yet.
                //m.err("plsynchedobject: haven't implemented this yet.");
            }
        }
        else if(c.readversion==4||c.readversion==7)
        {
            if ((flags & ~(0x1 | 0x2 | 0x4)) != 0)
                m.warn("Unknown synch flag combination ! " + flags);
            
            //if neither of bits 2 nor 3 are set...
            //I'm assuming that I should be assigning to the first set.  The 2nd set is hardly (possibly never) used.
            if ((flags & 0x6)==0)
            {
                //m.warn("plsynchedobject: untested case 2.");
                xstringcount = data.readShort();
                int count = b.Int16ToInt32(xstringcount);
                sdllinks = new Wpstr[count];
                for(int i=0;i<count;i++)
                {
                    sdllinks[i] = new Wpstr(c);
                }

                flags |= kExcludePersistentState;
            }
            
            
            // the synch flags are reworked in MV - which means we _must_ correct them, otherwise we lose kExcludeAllPersistentState
            // (in short: big, bad SDL synching that cause heavy slowdown). Thanks to D'Lanor for pointing this out.
            
            if ((flags & 0x4) != 0) // Myst V's kExcludeAllPersistentState
                flags = (flags & ~0x4) | kExcludeAllPersistentState;
            
            // other things from HSPlasma: 0x1=kDontDirty, 0x2=kExcludePersistentState
            // However, according to Dustin, we have kExcludePersistentState if 0x2 and 0x4 are off...
            // Let's keep kDontDirty, because it makes sense.

            if ((flags & 0x1) != 0)
                flags = (flags & ~0x1) | kDontDirty;
            
//            if ((flags & 0x2) != 0)
//                flags = (flags & ~0x2) | kExcludePersistentState;
        }
    }
    public void compile(Bytedeque deque)
    {
        deque.writeInt(flags);
        if ((flags & 0x10)!=0)
        {
            deque.writeShort(xstringcount);
            int count = b.Int16ToInt32(xstringcount);
            for(int i=0;i<count;i++)
            {
                sdllinks[i].compile(deque);
            }
        }
    }
    public plSynchedObject deepClone()
    {
        plSynchedObject result = new plSynchedObject();
        result.flags = flags;
        if(sdllinks!=null)
        {
            result.sdllinks = new Wpstr[sdllinks.length];
            for(int i=0;i<sdllinks.length;i++)
            {
                result.sdllinks[i] = sdllinks[i].deepClone();
            }
        }
        if(sdllinks2!=null)
        {
            result.sdllinks2 = new Wpstr[sdllinks2.length];
            for(int i=0;i<sdllinks.length;i++)
            {
                result.sdllinks2[i] = sdllinks2[i].deepClone();
            }
        }
        result.xstringcount = xstringcount;
        result.xstringcount2 = xstringcount2;
        return result;
    }
}

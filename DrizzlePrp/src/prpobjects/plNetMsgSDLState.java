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

import prpobjects.plMessage;
import prpobjects.uruobj;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
//import java.util.Vector;
import shared.*;
import moulserver.SdlBinary;


public class plNetMsgSDLState extends uruobj
{
    //parents are off9c4850 and off9c5fec
    public plMessage.plNetMsgStreamedObject parent;
    public byte isInitialState; //bool
    public byte persistOnServer; //bool
    public byte isAvatarState; //bool
    boolean extended = true;
    
    public plNetMsgSDLState(context c) throws readexception
    {
        parent = new plMessage.plNetMsgStreamedObject(c,SdlBinary.class);
        if(c.areBytesKnownToBeAvailable())
        {
            isInitialState = c.readByte();
            persistOnServer = c.readByte();
            isAvatarState = c.readByte();
        }
        else extended = false;
    }

    public SdlBinary getSdlBinary()
    {
        uruobj obj = parent.obj;
        SdlBinary r = (SdlBinary)obj;
        return r;
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        if(extended)
        {
            c.writeByte(isInitialState);
            c.writeByte(persistOnServer);
            c.writeByte(isAvatarState);
        }
    }

    private plNetMsgSDLState(){} //this can be made private after the merge
    public static plNetMsgSDLState createWithInfo(Uruobjectdesc desc, SdlBinary sdlbin, boolean isInitial)
    {
        //todo: when merged, this can access SdlBinary directly.
        //if(!sdlbin.getClass().getSimpleName().equals("SdlBinary"))
        //    m.throwUncaughtException("unexpected");

        plNetMsgSDLState r = new plNetMsgSDLState();
        r.parent = plMessage.plNetMsgStreamedObject.createWithDescObj(desc, sdlbin);
        r.isInitialState = b.BooleanToByte(isInitial);
        r.persistOnServer = 0;
        r.isAvatarState = 0;
        return r;
    }

}

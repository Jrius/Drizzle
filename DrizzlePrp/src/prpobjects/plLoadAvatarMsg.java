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


public class plLoadAvatarMsg extends plLoadCloneMsg
{
    //plLoadCloneMsg parent;
    byte isPlayer;
    Uruobjectref spawnPoint; //can be none.
    byte hasInitialTask;
    PrpTaggedObject initialTask;
    Urustring userStr; //can be ""
    
    public plLoadAvatarMsg(context c) throws readexception
    {
        //parent = new plLoadCloneMsg(c);
        super(c);
        isPlayer = c.readByte();
        spawnPoint = new Uruobjectref(c);
        hasInitialTask = c.readByte();
        if(hasInitialTask!=0)
        {
            initialTask = new PrpTaggedObject(c);
        }
        if(c.readversion==6)
        {
            userStr = new Urustring(c);
        }
    }
    
    public Typeid typeid(){return Typeid.plLoadAvatarMsg;}

    public void compile(Bytedeque c)
    {
        super.compile(c);
        c.writeByte(isPlayer);
        spawnPoint.compile(c);
        c.writeByte(hasInitialTask);
        if(hasInitialTask!=0)
        {
            initialTask.compile(c);
        }
        if(c.format==Format.moul)
        {
            userStr.compile(c);
        }
    }

}

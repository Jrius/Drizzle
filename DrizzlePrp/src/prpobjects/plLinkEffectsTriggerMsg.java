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
//import java.util.Vector;
import shared.*;


public class plLinkEffectsTriggerMsg extends uruobj
{
    plMessage parent;
    int invisLevel;
    byte leavingAge;
    Uruobjectref linkKey; //Male sceneobject, e.g.  I guess the clone/avatar key.
    int effects;
    Uruobjectref linkInAnimKey;
    
    public plLinkEffectsTriggerMsg(context c) throws readexception
    {
        parent = new plMessage(c);
        invisLevel = c.readInt();
        leavingAge = c.readByte();
        linkKey = new Uruobjectref(c);
        effects = c.readInt();
        linkInAnimKey = new Uruobjectref(c);
    }
    
    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("compile not implemented.",this.toString());
    }
    
}

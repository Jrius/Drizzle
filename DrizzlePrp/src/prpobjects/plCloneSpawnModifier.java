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


public class plCloneSpawnModifier extends uruobj
{
    plSingleModifier modifierparent;
    Urustring s1;
    int xu2;
    
    public plCloneSpawnModifier(context c) throws readexception
    {
        if(c.realreadversion==8)
        {
            //used for duplicated objects in mqo. Each plCloneSpawnModifier represents a place a cloned object can spawn.
            modifierparent = new plSingleModifier(c);
            s1 = new Urustring(c);
            xu2 = c.readInt();
            //throw new shared.ignore("plCloneSpawnModifier");
        }
        else
        {
            m.throwUncaughtException("unhandled");
            //pots and moul version look like:
            //-Urustring
            //-plSingleModifier
            //which is odd, because the plSingleModifier should be first if it is inherited.  The Cyan Pots/Moul implementation may be broken.
        }
    }
    
    public void compile(Bytedeque c)
    {
        //I think the pots and moul implementation of this class are broken.
        //This is verifed by trying combinations: string, modifier; modifier, string; modifier, string, int.  All of them crash Pots.

        m.throwUncaughtException("compile not implemented.",this.toString());
        //modifierparent.compile(c);
        //s1.compile(c);
        //c.writeInt(xu2);
    }
    
}

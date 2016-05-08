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


public class plNetMsgRelevanceRegions extends uruobj
{
    public plNetMessage parent;
    public HsBitVector regionsICareAbout; //first bit is set when no regions; default?
    public HsBitVector regionsImIn; //first bit is set when no regions; default?
    boolean extended = true;
    
    public plNetMsgRelevanceRegions(context c) throws readexception
    {
        parent = new plNetMessage(c);
        if(c.areBytesKnownToBeAvailable())
        {
            regionsICareAbout = new HsBitVector(c);
            regionsImIn = new HsBitVector(c);
        }
        else extended = false;
    }
    
    public void compile(Bytedeque c)
    {
        m.throwUncaughtException("compile not implemented.",this.toString());
    }
    
}

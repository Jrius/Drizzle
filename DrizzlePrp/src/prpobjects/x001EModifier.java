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
//is this correct? does it have any members?
public class x001EModifier extends uruobj
{
    //Objheader xheader;
    plSynchedObject parent;
    
    public x001EModifier(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);
        
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
    }
    private x001EModifier(){}
    public static x001EModifier createDefault()
    {
        x001EModifier result = new x001EModifier();
        result.parent = plSynchedObject.createDefault();
        return result;
    }
}

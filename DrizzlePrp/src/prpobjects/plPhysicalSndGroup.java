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


public class plPhysicalSndGroup extends uruobj
{
    int u1;
    int count1;
    Uruobjectref[] hitrefs;
    int count2;
    Uruobjectref[] sliderefs;
    
    public plPhysicalSndGroup(context c) throws readexception
    {
        u1 = c.readInt();
        count1 = c.readInt();
        hitrefs = new Uruobjectref[count1];
        for(int i=0;i<count1;i++)
        {
            hitrefs[i] = new Uruobjectref(c);
        }
        
        count2 = c.readInt();
        sliderefs = new Uruobjectref[count2];
        for(int i=0;i<count2;i++)
        {
            sliderefs[i] = new Uruobjectref(c);
        }
        
    }
    
    public void compile(Bytedeque c)
    {
        c.writeInt(u1);
        c.writeInt(count1);
        for(int i=0;i<count1;i++)
        {
            hitrefs[i].compile(c);
        }
        
        c.writeInt(count2);
        for(int i=0;i<count2;i++)
        {
            sliderefs[i].compile(c);
        }
    }
    
}

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
public class plWinAudible extends uruobj
{
    //Objheader xheader;
    public x0012Audible parent; //this is empty; do we need it?
    public int count;
    public Uruobjectref[] objectrefs;
    public Uruobjectref scenenode;
    
    public plWinAudible(context c) throws readexception //,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new x0012Audible(c);//,false);
        count = data.readInt();
        objectrefs = new Uruobjectref[count];
        for(int i=0;i<count;i++)
        {
            objectrefs[i] = new Uruobjectref(c);
        }
        scenenode = new Uruobjectref(c);
        
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(count);
        for(int i=0;i<count;i++)
        {
            objectrefs[i].compile(c);
        }
        scenenode.compile(c);
    }
}

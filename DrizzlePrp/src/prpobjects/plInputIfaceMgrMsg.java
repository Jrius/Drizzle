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


public class plInputIfaceMgrMsg extends uruobj
{
    plMessage parent;
    byte command;
    int pageId;
    Urustring agename;
    Urustring ageFilename;
    Urustring spawnpoint;
    Uruobjectref avKey; //avatar
    
    public plInputIfaceMgrMsg(context c) throws readexception
    {
        parent = new plMessage(c);
        command = c.readByte(); //can be 12
        pageId = c.readInt(); //can be 0x4031286C, so what is this?
        agename = new Urustring(c);
        ageFilename = new Urustring(c);
        spawnpoint = new Urustring(c);
        avKey = new Uruobjectref(c); //e.g. the Male sceneobject.
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeByte(command);
        c.writeInt(pageId);
        agename.compile(c);
        ageFilename.compile(c);
        spawnpoint.compile(c);
        avKey.compile(c);
    }
    
}

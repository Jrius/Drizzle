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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class pfGUIPopUpMenu extends uruobj
{
    pfGUIButtonMod.PfGUIDialogMod parent;
    short margin;
    Item[] items;
    Uruobjectref skin;
    Uruobjectref originAnchor;
    Uruobjectref originContext;
    byte alignment;
    
    public pfGUIPopUpMenu(context c) throws readexception
    {
        //This typeid was referenced in GUI_District_KIBlackBar, but it wasn't actually present.
        //m.throwUncaughtException("pfGUIPopupMenu is untested");
        parent = new pfGUIButtonMod.PfGUIDialogMod(c);
        margin = c.readShort();
        int count = c.readInt();
        items = new Item[count];
        for(int i=0;i<count;i++)
        {
            m.throwUncaughtException("pfGUIPopUpMenu unimplemented.");
            items[i] = new Item(c);
        }
        skin = new Uruobjectref(c);
        originAnchor = new Uruobjectref(c);
        originContext = new Uruobjectref(c);
        alignment = c.readByte();
    }

    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeShort(margin);
        c.writeInt(items.length);
        for(int i=0;i<items.length;i++)
        {
            items[i].compile(c);
        }
        skin.compile(c);
        originAnchor.compile(c);
        originContext.compile(c);
        c.writeByte(alignment);
    }

    public static class Item extends uruobj
    {
        byte[] name;
        public Item(context c)
        {
            name = c.readBytes(256);

        }
    }
}

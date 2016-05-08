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


public class plClothingItem extends uruobj
{
    //HsKeyedObject parent;
    Urustring itemName;
    byte group;
    byte type;
    byte tileset;
    byte sortOrder;
    Urustring description;
    Urustring customText;
    byte hasIcon;
    Uruobjectref icon;
    int elementCount;
    ElementInfo[] elements;
    byte hasmesh1;
    Uruobjectref mesh1;
    byte hasmesh2;
    Uruobjectref mesh2;
    byte hasmesh3;
    Uruobjectref mesh3;
    Uruobjectref accessory;
    byte defaultTint11;
    byte defaultTint21;
    byte defaultTint12;
    byte defaultTint22;
    byte defaultTint13;
    byte defaultTint23;
    
    public plClothingItem(context c) throws readexception
    {
        itemName = new Urustring(c); //name?
        group = c.readByte(); //1
        type = c.readByte(); //8
        tileset = c.readByte(); //0
        sortOrder = c.readByte(); //0
        description = new Urustring(c); //semi-colon seperated vars.
        customText = new Urustring(c); //empty
        hasIcon = c.readByte(); //1
        if(hasIcon!=0)
        {
            icon = new Uruobjectref(c);
        }
        elementCount = c.readInt();
        elements = c.readArray(ElementInfo.class, elementCount);
        hasmesh1 = c.readByte();
        if(hasmesh1!=0) mesh1 = new Uruobjectref(c);
        hasmesh2 = c.readByte();
        if(hasmesh2!=0) mesh2 = new Uruobjectref(c);
        hasmesh3 = c.readByte();
        if(hasmesh3!=0) mesh3 = new Uruobjectref(c);
        accessory = new Uruobjectref(c);
        defaultTint11 = c.readByte();
        defaultTint21 = c.readByte();
        defaultTint12 = c.readByte();
        defaultTint22 = c.readByte();
        defaultTint13 = c.readByte();
        defaultTint23 = c.readByte();
    }
    public static class ElementInfo extends uruobj
    {
        Urustring elementname;
        byte count;
        SubElementInfo[] textures;
        
        public ElementInfo(context c) throws readexception
        {
            elementname = new Urustring(c);
            count = c.readByte();
            int count2 = b.ByteToInt32(count);
            textures = c.readArray(SubElementInfo.class, count2);
        }
        public void compile(Bytedeque c)
        {
            elementname.compile(c);
            c.writeByte(count);
            c.writeArray2(textures);
        }
        public static class SubElementInfo extends uruobj
        {
            byte idx;
            Uruobjectref k;
            
            public SubElementInfo(context c) throws readexception
            {
                idx = c.readByte();
                k = new Uruobjectref(c);
            }
            public void compile(Bytedeque c)
            {
                c.writeByte(idx);
                k.compile(c);
            }
        }
    }
    public void compile(Bytedeque c)
    {
        itemName.compile(c);
        c.writeByte(group);
        c.writeByte(type);
        c.writeByte(tileset);
        c.writeByte(sortOrder);
        description.compile(c);
        customText.compile(c);
        c.writeByte(hasIcon);
        if(hasIcon!=0)
        {
            icon.compile(c);
        }
        c.writeInt(elementCount);
        c.writeArray2(elements);
        c.writeByte(hasmesh1);
        if(hasmesh1!=0) mesh1.compile(c);
        c.writeByte(hasmesh2);
        if(hasmesh2!=0) mesh2.compile(c);
        c.writeByte(hasmesh3);
        if(hasmesh3!=0) mesh3.compile(c);
        accessory.compile(c);
        c.writeByte(defaultTint11);
        c.writeByte(defaultTint21);
        c.writeByte(defaultTint12);
        c.writeByte(defaultTint22);
        c.writeByte(defaultTint13);
        c.writeByte(defaultTint23);
        
    }
    
}

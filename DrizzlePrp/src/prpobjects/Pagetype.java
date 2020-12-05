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

import uru.context;
import shared.Bytes;
import uru.Bytedeque;
import shared.e;
import shared.m;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

public class Pagetype extends uruobj implements java.io.Serializable
{
    static final long serialVersionUID = -7148572319556349354L;
    
    //usually: 8 is Textures or BuiltIn, 4 is a global one, and 0 is a regular page.
    public static final int kLocalOnly = 0x01;
    public static final int kVolatile =  0x02;
    public static final int kReserved =  0x04;
    public static final int kBuiltIn =   0x08;
    public static final int kItinerant = 0x10;

    public short pagetype;
    
    public Pagetype(context c)
    {
        if(c.readversion==6 || c.readversion==3)
        {
            pagetype = c.readShort();
        }
        else if(c.readversion==4||c.readversion==7)
        {
            pagetype = Bytes.ByteToInt16(c.readByte());
        }
        e.ensure(pagetype==0||pagetype==4||pagetype==8||pagetype==16||pagetype==20); //should this be a byte? //0=page, 4=global, 8=texture/builtin. 16 was used for garden_district_itinerantbugcloud. 20 was used in a GlobalAnimation.
    }
    private Pagetype(){}
    public static Pagetype createDefault()
    {
        Pagetype result = new Pagetype();
        result.pagetype = 0;
        return result;
    }
    public static Pagetype createWithType(int pagetype)
    {
        if(pagetype<0 || pagetype>20) m.err("Incorrect pagetype in Pagetype.createWithType.");
        Pagetype result = new Pagetype();
        result.pagetype = (short)pagetype;
        return result;
    }
    public void compile(Bytedeque c)
    {
        if(c.format==Format.pots || c.format==Format.moul)
        {
            c.writeShort(pagetype);
        }
        else
        {
            m.throwUncaughtException("unimplemented");
        }
    }
    
    public String toString()
    {
        return Short.toString(pagetype);
    }
    public void addXml(StringBuilder s)
    {
        s.append(Short.toString(pagetype));
    }
    public static Pagetype createFromXml(Element e1)
    {
        int pagetype = Integer.parseInt(e1.getTextContent());
        return Pagetype.createWithType(pagetype);
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Pagetype)) return false;
        Pagetype o2 = (Pagetype)o;
        if(this.pagetype!=o2.pagetype) return false;
        return true;
    }
    
    public Pagetype deepClone()
    {
        Pagetype result = new Pagetype();
        result.pagetype = pagetype;
        return result;
    }
}

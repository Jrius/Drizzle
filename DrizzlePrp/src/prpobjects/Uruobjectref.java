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
import shared.e;
import shared.m;
import uru.Bytedeque;
import shared.*;

/**
 *
 * @author user
 */
public class Uruobjectref extends uruobj
{
    public byte hasRef;
    public Uruobjectdesc xdesc;
    
    public Uruobjectref(context c) throws readexception
    {
        if(c.readversion==6||c.readversion==3)
        {
            hasRef = c.in.readByte();
            if(hasRef != 0)
            {
                xdesc = new Uruobjectdesc(c);
            }
        }
        else if(c.readversion==4||c.readversion==7)
        {
            xdesc = new Uruobjectdesc(c);
            //try to tell if this is a valid reference...
            if(xdesc.objectname.unencryptedString.length==0)
            {
                hasRef = 0;
            }
            else
            {
                hasRef = 1;
            }
        }
        else
        {
            m.throwUncaughtException("Unhandled readversion.");
        }
        
        //trap
        /*if(xdesc!=null && xdesc.objecttype==Typeid.plLadderModifier && c.curRootObject!= null && c.curRootObject.objecttype==Typeid.plLogicModifier)
        {
            if(shared.State.AllStates.getStateAsBoolean("removeLadders"))
            {
                int dummy=0;
                //throw new shared.readwarningexception("Removing plLogicModifier that references plLadderModifier:"+xdesc.objectname.toString());
            }
        }*/
    }
    public static Uruobjectref createDefaultWithTypeNamePage(Typeid type, String name, Pageid page)
    {
        return createDefaultWithTypeNamePagePagetype(type,name,page, Pagetype.createDefault());
    }
    public static Uruobjectref createDefaultWithTypeNamePrp(Typeid type, String name, prpfile prp)
    {
        return createDefaultWithTypeNamePagePagetype(type,name,prp.header.pageid,prp.header.pagetype);
    }
    public static Uruobjectref createDefaultWithTypeNamePagePagetype(Typeid type, String name, Pageid page, Pagetype pagetype)
    {
        Uruobjectref result = new Uruobjectref();
        result.hasRef = 1;
        result.xdesc = Uruobjectdesc.createDefaultWithTypeNamePagePagetype(type, name, page, pagetype);
        return result;
    }
    public static Uruobjectref none()
    {
        Uruobjectref result = new Uruobjectref();
        result.hasRef = 0;
        return result;
    }
    
    public boolean hasref()
    {
        return !(hasRef==0);
    }
    
    private Uruobjectref(){}
    
    public static Uruobjectref createFromUruobjectdesc(Uruobjectdesc desc)
    {
        Uruobjectref result = new Uruobjectref();
        result.hasRef = 1;
        result.xdesc = desc;
        return result;
    }
    
    public void shallowCopyFrom(Uruobjectref src)
    {
        this.hasRef = src.hasRef;
        this.xdesc = src.xdesc;
    }
    //public static Uruobjectref create(Bytestream data)
    //{
    //    return new Uruobjectref(data);
    //}
    public void compile(Bytedeque deque)
    {
        if(deque.format==Format.pots || deque.format==Format.moul)
        {
            deque.writeByte(hasRef);
            if(hasRef != 0)
            {
                xdesc.compile(deque);
            }
        }
        else
        {
            m.throwUncaughtException("unimplemented");
        }
    }
    
    public String toString()
    {
        if(hasRef==0)
        {
            return "none";
        }
        else
        {
            return xdesc.toString();
        }
    }
    
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Uruobjectref)) return false;
        Uruobjectref o2 = (Uruobjectref)o;
        if(this.hasRef != o2.hasRef)  return false;
        if(!this.hasref()) return true;
        return this.xdesc.equals(o2.xdesc);
    }
    public int hashCode()
    {
        if(this.hasRef==0) return 0;
        return this.xdesc.objectname.hashCode()+this.xdesc.objecttype.hashCode();
    }

    public Uruobjectref deepClone()
    {
        Uruobjectref result = new Uruobjectref();
        result.hasRef = hasRef;
        if(hasRef!=0)
        {
            result.xdesc = xdesc.deepClone();
        }
        return result;
    }
}

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

import shared.readexception;
import uru.context; import shared.readexception;
import shared.m;
import uru.Bytedeque;
import uru.writesOwnTypeid;

public class PrpTaggedSizedObject extends uruobj
{
    public Typeid type;
    public int size;
    public PrpObject prpobject;
    
    public PrpTaggedSizedObject(context c) throws readexception
    {
        type = Typeid.Read(c);
        if(type==type.plSceneNode || type==type.nil) //really used as a null here.
        {
            prpobject = null;
        }
        else
        {
            size = c.readInt(); //do we care about this?
            prpobject = new PrpObject(c,type);
        }
    }
    
    public <T> T castTo(Class<T> cls)
    {
        T result = (T)this.prpobject.object;
        return result;
    }
    public <T> T castTo()
    {
        T result = (T)this.prpobject.object;
        return result;
    }
    public void compile(Bytedeque c)
    {
        if(type==type.plSceneNode || type==type.nil)
        {
            type.compile(c);
        }
        else
        {
            if(prpobject.object instanceof writesOwnTypeid)
            {
                //if it writes its own typeid, let it.
                ((writesOwnTypeid)prpobject.object).compileTypeid(c);
            }
            else
            {
                type.compile(c);
            }
            
            prpobject.compile(c);
        }
    }
    public void compileWithoutTypeid(Bytedeque c)
    {
        if(type==type.plSceneNode || type==type.nil)
        {
        }
        else
        {
            prpobject.compile(c);
        }
    }
    private PrpTaggedSizedObject(){}
    public static PrpTaggedSizedObject createWithTypeidUruobj(Typeid typeid, uruobj obj)
    {
        PrpTaggedSizedObject result = new PrpTaggedSizedObject();
        result.type = typeid;
        result.prpobject = PrpObject.createFromUruobj(obj);
        return result;
    }
    public String toString()
    {
        return "type:"+type.toString()+" info:"+(prpobject==null?"(null)":prpobject.toString());
    }
    
    
}

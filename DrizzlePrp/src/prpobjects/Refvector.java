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
import java.util.Vector;
import java.util.Iterator;


public class Refvector extends uruobj implements Iterable<Uruobjectref>
{
    private int initialcount;
    //Uruobjectref[] refs;
    Vector<Uruobjectref> refs;
    java.lang.Iterable a;

    public Iterator<Uruobjectref> iterator()
    {
        return refs.iterator();
    }
    public Refvector(context c) throws readexception
    {
        initialcount = c.readInt();
        refs = c.readVector(Uruobjectref.class, initialcount);
        //refs = new Uruobjectref[initialcount];
        //for(int i=0;i<initialcount;i++)
        //{
        //    refs[i] = new Uruobjectref(c);
        //}
        
    }
    public Refvector()
    {
        initialcount = 0;
        refs = new Vector<Uruobjectref>();
    }
    public void compile(Bytedeque c)
    {
        //c.writeInt(initialcount);
        //for(int i=0;i<initialcount;i++)
        //{
        //    refs[i].compile(c);
        //}
        c.writeInt(refs.size());
        c.writeVector2(refs);
    }
    public void add(Uruobjectref ref)
    {
        refs.add(ref);
    }
    public void clear()
    {
        refs.clear();
    }
    public boolean has(Typeid type)
    {
        return (find(type)!=null);
    }
    public Uruobjectref find(Typeid type)
    {
        for(Uruobjectref ref: refs)
        {
            if(ref.hasref() && ref.xdesc.objecttype==type)
            {
                return ref;
            }
        }
        return null;
    }
    public int size()
    {
        return refs.size();
    }
    public void remove(Uruobjectref ref)
    {
        refs.remove(ref);
    }
    public Uruobjectref get(int ind)
    {
        return refs.get(ind);
    }
}

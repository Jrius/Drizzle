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
public class x0041LayerInterface extends uruobj
{
    //Objheader xheader;
    public plSynchedObject parent;
    public Uruobjectref ref; //underlay, i.e. plMipMap
    
    public x0041LayerInterface(context c) throws readexception //,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);//,false);
        ref = new Uruobjectref(c);
        //ref = Uruobjectref.createFromUruobjectdesc(new Uruobjectdesc(c));
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        ref.compile(data);
    }
    public x0041LayerInterface(x0041LayerInterface s)
    {
        this.parent = s.parent.deepClone();
        this.ref = s.ref.deepClone();
    }
    public x0041LayerInterface deepClone()
    {
        return new x0041LayerInterface(this);
    }
    private x0041LayerInterface(){}
    public static x0041LayerInterface createEmpty()
    {
        x0041LayerInterface r = new x0041LayerInterface();
        r.parent = plSynchedObject.createEmpty();
        return r;
    }
}

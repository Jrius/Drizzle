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
public class plObjInterface extends uruobj
{
    //Objheader xheader;
    plSynchedObject parent;
    public Uruobjectref sceneobject;
    //int count;
    //int[] dwarray;
    public HsBitVector bv;
    
    
    public plObjInterface(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);
        sceneobject = new Uruobjectref(c);
        //count = data.readInt();
        //dwarray = data.readInts(count);
        bv = new HsBitVector(c);
    }
    private plObjInterface(){}
    public static plObjInterface createDefault(Uruobjectref sceneobject)
    {
        plObjInterface result = new plObjInterface();
        result.parent = plSynchedObject.createDefault();
        result.sceneobject = sceneobject;
        result.bv = HsBitVector.createDefault();
        return result;
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
        sceneobject.compile(deque);
        //deque.writeInt(count);
        //deque.writeInts(dwarray);
        bv.compile(deque);
    }
}

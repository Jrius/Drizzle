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
import prpobjects.Transmatrix;
import shared.*;
import java.util.Vector;

/**
 *
 * @author user
 */
//was x0015CoordinateInterface
public class plCoordinateInterface extends uruobj
{
    //Objheader xheader;
    public plObjInterface parent;
    public Transmatrix localToParent; //1 and 2 should be inverses of each other.
    public Transmatrix parentToLocal;
    public Transmatrix localToWorld; //3 and 4 should be inverses of each other.
    public Transmatrix worldToLocal;
    public int count;
    public Uruobjectref[] children;
    
    public plCoordinateInterface(context c) throws readexception
    {
        if(c.curRootObject.objectname.toString().toLowerCase().startsWith("ropeladder"))
        {
            int dummy=0;
        }

        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plObjInterface(c);//,false);
        localToParent = new Transmatrix(c);
        parentToLocal = new Transmatrix(c);
        localToWorld = new Transmatrix(c);
        worldToLocal = new Transmatrix(c);
        count = data.readInt();
        children = new Uruobjectref[count];
        for(int i=0;i<count;i++)
        {
            children[i] = new Uruobjectref(c);
        }
    }
    public void recalcFromL2WandW2P(Transmatrix w2p)
    {
        Transmatrix w2l = this.localToWorld.inverse();
        //Transmatrix l2p = this.localToWorld.mult(w2p);
        Transmatrix l2p = w2p.mult(this.localToWorld);
        Transmatrix p2l = l2p.inverse();
        this.worldToLocal = w2l;
        this.localToParent = l2p;
        this.parentToLocal = p2l;
    }
    public static PrpRootObject findParent(prpfile prp, PrpRootObject childcoordinterface)
    {
        plCoordinateInterface ci = childcoordinterface.castTo();
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCoordinateInterface))
        {
            plCoordinateInterface possibleparent = ro.castTo();
            for(Uruobjectref ch: possibleparent.children)
            {
                if(ch.equals(ci.parent.sceneobject))
                {
                    return ro;
                }
            }
        }
        return null;
    }
    public void translate(double x, double y, double z, PrpRootObject[] coordinateinterfaces)
    {
        Uruobjectref parentCI = findCIParent(coordinateinterfaces);
        boolean moveLocalToParent = (parentCI==null);
        translate(x,y,z,moveLocalToParent);
    }
    public void translate(double x2, double y2, double z2, boolean moveLocalToParent)
    {
        float x = (float)x2;
        float y = (float)y2;
        float z = (float)z2;

        Transmatrix translation = Transmatrix.createFromVector(x, y, z);
        Transmatrix invtranslation = Transmatrix.createFromVector(-x, -y, -z);


        this.localToWorld = translation.mult(this.localToWorld);
        this.worldToLocal = this.worldToLocal.mult(invtranslation);

        //we don't want to translate localtoparent if we'll be translating the parent anyway.
        if(moveLocalToParent)
        {
            this.localToParent = translation.mult(this.localToParent);
            this.parentToLocal = this.parentToLocal.mult(invtranslation);
        }
        else
        {
            //do nothing.
        }

    }
    public Uruobjectref findCIParent(PrpRootObject[] coordinateinterfaces)
    {
        Uruobjectref so = this.parent.sceneobject;
        return findCIParent(so, coordinateinterfaces);
    }
    public static Uruobjectref findCIParent(Uruobjectref sceneobject, PrpRootObject[] coordinateinterfaces)
    {
        for(PrpRootObject obj: coordinateinterfaces)
        {
            plCoordinateInterface ci2 = obj.castTo();
            for(Uruobjectref ref: ci2.children)
            {
                if(ref.equals(sceneobject)) return ref;
            }
        }
        return null;
    }
    private plCoordinateInterface(){}
    public static plCoordinateInterface createDefault(Uruobjectref sceneobject)
    {
        plCoordinateInterface result = new plCoordinateInterface();
        result.parent = plObjInterface.createDefault(sceneobject);
        result.localToParent = Transmatrix.createDefault();
        result.parentToLocal = Transmatrix.createDefault();
        result.localToWorld = Transmatrix.createDefault();
        result.worldToLocal = Transmatrix.createDefault();
        result.count = 0;
        result.children = new Uruobjectref[0];
        return result;
    }
    public static plCoordinateInterface createWithMatrix44(Uruobjectref sceneobject, Matrix44 coords)
    {
        plCoordinateInterface r = new plCoordinateInterface();
        Matrix44 inv = coords.getInverse();
        r.parent = plObjInterface.createDefault(sceneobject);
        r.localToParent = Transmatrix.createFromMatrix44(coords);
        r.parentToLocal = Transmatrix.createFromMatrix44(inv);
        r.localToWorld = Transmatrix.createFromMatrix44(coords);
        r.worldToLocal = Transmatrix.createFromMatrix44(inv);
        r.count = 0;
        r.children = new Uruobjectref[0];
        return r;
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
        localToParent.compile(deque);
        parentToLocal.compile(deque);
        localToWorld.compile(deque);
        worldToLocal.compile(deque);
        deque.writeInt(count);
        for(int i=0;i<count;i++)
        {
            children[i].compile(deque);
        }
    }
}

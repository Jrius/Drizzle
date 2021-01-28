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
import java.util.Vector;

/**
 *
 * @author user
 */

//was x0001Sceneobject
public class plSceneObject extends uruobj
{
    //Objheader xheader;
    plSynchedObject parent;
    public Uruobjectref drawinterface; //draw //was spaninfo
    public Uruobjectref simulationinterface; //simulation //was animationinfo
    public Uruobjectref coordinateinterface; //coordinate //was regioninfo
    public Uruobjectref audiointerface; //audio //was soundinfo
    //private int count1;
    //public Vector<Uruobjectref> interfaces = new Vector<Uruobjectref>();
    //public Uruobjectref[] objectrefs1;
    public Refvector interfaces;
    //private int count2;
    //public Vector<Uruobjectref> modifiers = new Vector<Uruobjectref>();
    //public Uruobjectref[] objectrefs2;
    public Refvector modifiers;
    Uruobjectref scenenode;

    //public boolean wasread; //was this object read rather than created from scratch
    public Boolean includeInScenenode;
    
    public plSceneObject(context c) throws readexception //,boolean hasHeader)
    {
        //wasread = true;

        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);//,false);
        drawinterface = new Uruobjectref(c); //drawinterface
        simulationinterface = new Uruobjectref(c); //simulation interface
        coordinateinterface = new Uruobjectref(c); //coordinateinterface
        audiointerface = new Uruobjectref(c); //audio interface
        //count1 = data.readInt();
        /*objectrefs1 = new Uruobjectref[count1];
        for(int i=0;i<count1;i++)
        {
            objectrefs1[i] = new Uruobjectref(c);
        }*/
        //interfaces = c.readVector(Uruobjectref.class, count1); //was objectrefs1
        interfaces = new Refvector(c);
        //count2 = data.readInt();
        /*objectrefs2 = new Uruobjectref[count2];
        for(int i=0;i<count2;i++)
        {
            objectrefs2[i] = new Uruobjectref(c);
        }*/
        //modifiers = c.readVector(Uruobjectref.class, count2); //was objectrefs2
        modifiers = new Refvector(c);
        scenenode = new Uruobjectref(c);
    }
    private plSceneObject(){}
    public static plSceneObject createDefaultWithScenenode(Uruobjectref scenenode)
    {
        plSceneObject result = new plSceneObject();
        result.parent = plSynchedObject.createDefault();
        result.drawinterface = Uruobjectref.none();
        result.simulationinterface = Uruobjectref.none();
        result.coordinateinterface = Uruobjectref.none();
        result.audiointerface = Uruobjectref.none();
        //result.count1 = 0;
        //result.objectrefs1 = new Uruobjectref[0];
        //result.interfaces = new Vector<Uruobjectref>();
        result.interfaces = new Refvector();
        //result.count2 = 0;
        //result.objectrefs2 = new Uruobjectref[0];
        //result.modifiers = new Vector<Uruobjectref>();
        result.modifiers = new Refvector();
        result.scenenode = scenenode;
        return result;
    }
    public void clearObjectrefs2()
    {
        //count2 = 0;
        modifiers.clear();
    }
    public void addToObjectrefs2(Uruobjectref ref)
    {
        //count2++;
        modifiers.add(ref);
    }
    public void compile(Bytedeque deque)
    {
        
        parent.compile(deque);
        drawinterface.compile(deque);
        simulationinterface.compile(deque);
        coordinateinterface.compile(deque);
        audiointerface.compile(deque);
        //deque.writeInt(count1);
        /*for(int i=0;i<count1;i++)
        {
            objectrefs1[i].compile(deque);
        }*/
        //deque.writeInt(interfaces.size());
        //deque.writeVector2(interfaces);
        interfaces.compile(deque);
        //deque.writeInt(count2);
        /*for(int i=0;i<count2;i++)
        {
            objectrefs2[i].compile(deque);
        }*/
        //deque.writeInt(modifiers.size());
        //deque.writeVector2(modifiers);
        modifiers.compile(deque);
        scenenode.compile(deque);
    }
    /*public Uruobjectref getChildByType(Typeid type)
    {
        return null;
    }*/
    public plHKPhysical getPhysics(prpfile prp)
    {
        PrpRootObject sim_ro = prp.findObjectWithRef(simulationinterface);
        if(sim_ro==null) return null;
        plSimulationInterface sim = sim_ro.castTo();
        PrpRootObject phys_ro = prp.findObjectWithRef(sim.physical);
        if(phys_ro==null) return null;
        plHKPhysical phys = phys_ro.castTo();
        return phys;
    }
}

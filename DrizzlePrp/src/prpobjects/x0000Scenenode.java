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
import java.util.Vector;

/**
 *
 * @author user
 */
public class x0000Scenenode extends uruobj
{
    //Objheader xheader;
    public int count1;
    //public Uruobjectref[] objectrefs1; //all x0001Sceneobject
    public Vector<Uruobjectref> objectrefs1;
    public int count2;
    //public Uruobjectref[] objectrefs2; //misc others.
    public Vector<Uruobjectref> objectrefs2;

    public x0000Scenenode(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        ////if(hasHeader) xheader = new Objheader(c);
        count1 = data.readInt();
        /*objectrefs1 = new Uruobjectref[count1];
        for(int i=0;i<count1;i++)
        {
            //if(c.readversion==6||c.readversion==3)
            //{
                objectrefs1[i] = new Uruobjectref(c);
            //}
            //else if(c.readversion==4)
            //{
            //    objectrefs1[i] = Uruobjectref.createFromUruobjectdesc(new Uruobjectdesc(c));
            //}
        }*/
        objectrefs1 = c.readVector(Uruobjectref.class, count1);
        count2 = data.readInt();
        /*objectrefs2 = new Uruobjectref[count2];
        for(int i=0;i<count2;i++)
        {
            //if(c.readversion==6||c.readversion==3)
            //{
                objectrefs2[i] = new Uruobjectref(c);
            //}
            //else if(c.readversion==4)
            //{
            //    objectrefs2[i] = Uruobjectref.createFromUruobjectdesc(new Uruobjectdesc(c));
            //}
        }*/
        objectrefs2 = c.readVector(Uruobjectref.class, count2);
        
    }
    private x0000Scenenode(){}
    private x0000Scenenode shallowcopy()
    {
        x0000Scenenode r = new x0000Scenenode();
        r.count1 = count1;
        r.objectrefs1 = (Vector)objectrefs1.clone();
        r.count2 = count2;
        r.objectrefs2 = (Vector)objectrefs2.clone();
        return r;
    }
    public static x0000Scenenode createDefault()
    {
        x0000Scenenode result = new x0000Scenenode();
        result.count1 = 0;
        //result.objectrefs1 = new Uruobjectref[0];
        result.objectrefs1 = new Vector<Uruobjectref>();
        result.count2 = 0;
        //result.objectrefs2 = new Uruobjectref[0];
        result.objectrefs2 = new Vector<Uruobjectref>();
        return result;
    }
    public void regenerateAllSceneobjectsFromPrpRootObjects(Vector<PrpRootObject> objs/*,x0000Scenenode orig*/)
    {
        this.count1 = 0;
        this.objectrefs1.clear();
        
        for(PrpRootObject obj: objs)
        {
            if(obj.tagDeleted) continue;
            
            if(obj.header.desc.objecttype==Typeid.plSceneObject)
            {
                //boolean oldScenenodeHadThis = orig.objectrefs1.contains(obj.getref());
                prpobjects.plSceneObject so = obj.castTo();
                //if(oldScenenodeHadThis || !so.wasread) //if we had it before or it was newly created
                //{
                if(so.includeInScenenode==null || so.includeInScenenode==true)
                {
                    this.addToObjectrefs1(obj.header.desc.toRef());
                }
                else
                {
                    //do nothing.
                    int dummy=0;
                }
            }
        }
    }
    public void addToObjectrefs1(Uruobjectref ref)
    {
        count1++;
        objectrefs1.add(ref);
    }
    public void addToObjectrefs2(Uruobjectref ref)
    {
        count2++;
        objectrefs2.add(ref);
    }
    public void compile(Bytedeque deque)
    {
        m.msg("compile not implemented");
    }
    public void compileSpecial(Bytedeque deque, Vector<PrpRootObject> allobjects, prputils.Compiler.Decider decider)
    {
        //x0000Scenenode orig = this.shallowcopy();

        //this will get all the sceneobjects, except those tagged as deleted.
        this.regenerateAllSceneobjectsFromPrpRootObjects(allobjects/*,orig*/);

        
        ///prputils.Compiler.isNormalObjectToBeIncluded(desc);
        int newcount1 = 0;
        int newcount2 = 0;

        //count refs to be used from the first group.
        for(int i=0;i<count1;i++)
        {
            //Uruobjectref curref = objectrefs1[i];
            Uruobjectref curref = objectrefs1.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    newcount1++;
                }
            }
        }
        //count refs to be used from the second group.
        for(int i=0;i<count2;i++)
        {
            //Uruobjectref curref = objectrefs2[i];
            Uruobjectref curref = objectrefs2.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    newcount2++;
                }
            }
        }
        
        //output first group.
        deque.writeInt(newcount1);
        for(int i=0;i<count1;i++)
        {
            //Uruobjectref curref = objectrefs1[i];
            Uruobjectref curref = objectrefs1.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    curref.compile(deque);
                }
            }
        }
        
        //output second group.
        deque.writeInt(newcount2);
        for(int i=0;i<count2;i++)
        {
            //Uruobjectref curref = objectrefs2[i];
            Uruobjectref curref = objectrefs2.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    curref.compile(deque);
                }
            }
        }
    }
    /*public void compileSpecial(Bytedeque deque, prputils.Compiler.Decider decider)
    {
        m.warn("Using deprecated Scenenode compileSpecial.  Change this!");
        
        //prputils.Compiler.isNormalObjectToBeIncluded(desc);
        int newcount1 = 0;
        int newcount2 = 0;

        //count refs to be used from the first group.
        for(int i=0;i<count1;i++)
        {
            //Uruobjectref curref = objectrefs1[i];
            Uruobjectref curref = objectrefs1.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    newcount1++;
                }
            }
        }
        //count refs to be used from the second group.
        for(int i=0;i<count2;i++)
        {
            //Uruobjectref curref = objectrefs2[i];
            Uruobjectref curref = objectrefs2.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    newcount2++;
                }
            }
        }
        
        //output first group.
        deque.writeInt(newcount1);
        for(int i=0;i<count1;i++)
        {
            //Uruobjectref curref = objectrefs1[i];
            Uruobjectref curref = objectrefs1.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    curref.compile(deque);
                }
            }
        }
        
        //output second group.
        deque.writeInt(newcount2);
        for(int i=0;i<count2;i++)
        {
            //Uruobjectref curref = objectrefs2[i];
            Uruobjectref curref = objectrefs2.get(i);
            if(curref.hasRef!=0)
            {
                //if(prputils.Compiler.isNormalObjectToBeIncluded(curref.xdesc))
                if(decider.isObjectToBeIncluded(curref.xdesc))
                {
                    curref.compile(deque);
                }
            }
        }
    }*/
}

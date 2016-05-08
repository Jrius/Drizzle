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
public class plDrawInterface extends uruobj
{
    //Objheader xheader;
    public plObjInterface parent;
    public int subsetgroupcount;
    public SubsetGroupRef[] subsetgroups;
    public int visregioncount;
    public Uruobjectref[] visibleregion;
    
    public plDrawInterface(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plObjInterface(c);//,false);
        subsetgroupcount = c.in.readInt();
        subsetgroups = new SubsetGroupRef[subsetgroupcount];
        for(int i=0;i<subsetgroupcount;i++)
        {
            subsetgroups[i] = new SubsetGroupRef(c);
        }
        visregioncount = c.in.readInt();
        visibleregion = new Uruobjectref[visregioncount];
        for(int i=0;i<visregioncount;i++)
        {
            visibleregion[i] = new Uruobjectref(c);
        }
        
    }
    private plDrawInterface(){}
    public static plDrawInterface createDefault(Uruobjectref sceneobject)
    {
        plDrawInterface r = new plDrawInterface();
        r.parent = plObjInterface.createDefault(sceneobject);
        return r;
    }
    public Vector<Uruobjectref> findAllMaterials(Vector<prpfile> prps)
    {
        Vector<Uruobjectref> result = new Vector();
        for(SubsetGroupRef sgr: subsetgroups)
        {
            if(sgr.subsetgroupindex!=-1)
            {
                prpobjects.plDrawableSpans spans = prpdistiller.distiller.findObject(prps, sgr.span.xdesc).castTo();
                prpobjects.plDrawableSpans.PlDISpanIndex spanindex = spans.DIIndices[sgr.subsetgroupindex];
                for(int subsetind: spanindex.indices)
                {
                    prpobjects.plDrawableSpans.PlIcicle icicle = spans.icicles[subsetind];
                    Uruobjectref matrefb = spans.materials.get(icicle.parent.parent.materialindex);
                    result.add(matrefb);
                }
            }
        }
        return result;
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        data.writeInt(subsetgroupcount);
        for(int i=0;i<subsetgroupcount;i++)
        {
            subsetgroups[i].compile(data);
        }
        data.writeInt(visregioncount);
        for(int i=0;i<visregioncount;i++)
        {
            visibleregion[i].compile(data);
        }
    }
    
    static public class SubsetGroupRef extends uruobj
    {
        public int subsetgroupindex;
        public Uruobjectref span;
        
        public SubsetGroupRef(context c) throws readexception
        {
            subsetgroupindex = c.in.readInt();
            span = new Uruobjectref(c);
        }
        public SubsetGroupRef(){}
        public void compile(Bytedeque data)
        {
            data.writeInt(subsetgroupindex);
            span.compile(data);
        }
    }
}

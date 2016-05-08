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
//aka hsGMaterial
public class x0007Material extends uruobj
{
    //Objheader xheader;
    public plSynchedObject parent;
    public int loadFlags; //was u1
    public int compFlags; //was flags
    public int layercount; //was layercount
    public int piggybackcount; //was lightmapcount
    //public Uruobjectref[] layerrefs;
    //public Uruobjectref[] maplayerrefs;
    public Vector<Uruobjectref> layerrefs = new Vector<Uruobjectref>();
    public Vector<Uruobjectref> piggybackrefs = new Vector<Uruobjectref>(); //was maplayerrefs
    
    public x0007Material(context c) throws readexception //,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);//,false);
        loadFlags = data.readInt(); e.ensureflags(loadFlags,0); //loadflags
        compFlags = data.readInt(); //e.ensureflags(compFlags,0x00,0x0400,0x1000,0x2000,0x2400/*fanages:*/,0x2010,0x10); //compflags
        layercount = data.readInt();
        piggybackcount = data.readInt();
        //layerrefs = new Uruobjectref[layercount];
        for(int i=0;i<layercount;i++)
        {
            //layerrefs[i] = new Uruobjectref(c);
            layerrefs.add(new Uruobjectref(c));
        }
        //maplayerrefs = new Uruobjectref[lightmapcount];
        for(int i=0;i<piggybackcount;i++)
        {
            //maplayerrefs[i] = new Uruobjectref(c);
            piggybackrefs.add(new Uruobjectref(c));
        }
    }
    public static x0007Material createEmpty()
    {
        x0007Material r = new x0007Material();
        r.parent = plSynchedObject.createEmpty();
        r.layerrefs = new Vector();
        r.piggybackrefs = new Vector();
        return r;
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        data.writeInt(loadFlags);
        data.writeInt(compFlags);
        /*data.writeInt(layercount);
        data.writeInt(lightmapcount);
        for(int i=0;i<layercount;i++)
        {
            layerrefs[i].compile(data);
        }
        for(int i=0;i<lightmapcount;i++)
        {
            maplayerrefs[i].compile(data);
        }*/
        data.writeInt(layerrefs.size());
        data.writeInt(piggybackrefs.size());
        for(int i=0;i<layerrefs.size();i++)
        {
            layerrefs.get(i).compile(data);
        }
        for(int i=0;i<piggybackrefs.size();i++)
        {
            piggybackrefs.get(i).compile(data);
        }
    }
    private x0007Material(){}
    public x0007Material deepClone()
    {
        x0007Material result = new x0007Material();
        result.parent = parent.deepClone();
        result.compFlags = compFlags;
        result.layercount = layercount;
        result.layerrefs = new Vector();
        for(Uruobjectref ref: layerrefs)
        {
            result.layerrefs.add(ref.deepClone());
        }
        result.loadFlags = loadFlags;
        result.piggybackcount = piggybackcount;
        result.piggybackrefs = new Vector();
        for(Uruobjectref ref: piggybackrefs)
        {
            result.piggybackrefs.add(ref.deepClone());
        }
        return result;
    }
    public void addLayer(Uruobjectref layerref)
    {
        layerrefs.add(layerref);
        layercount = layerrefs.size();
    }
}

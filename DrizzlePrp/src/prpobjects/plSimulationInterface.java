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
//was x001CSimulationInterface
public class plSimulationInterface extends uruobj
{
    //Objheader xheader;
    public plObjInterface parent;
    //int count;
    //int[] props;
    public HsBitVector props;
    public int u2;
    public Uruobjectref physical;
    
    public plSimulationInterface(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        if(c.curRootObject.objectname.toString().toLowerCase().startsWith("ropeladder"))
        {
            int dummy=0;
        }
        
        parent = new plObjInterface(c);//,false);
        //count = c.readInt();
        //props = c.in.readInts(count);
        if(c.readversion==6||c.readversion==3)
        {
            props = new HsBitVector(c); //always empty in both pots and moul.
            u2 = c.readInt(); //always zero in both pots and moul.
        }
        else if(c.readversion==4||c.readversion==7)
        {
            //these are always zero in moul and pots, so we are justified in doing this.
            props = HsBitVector.createDefault();
            u2 = 0;
        }
        physical = new Uruobjectref(c);
    }
    public plSimulationInterface(){}
    public static plSimulationInterface createWithPlHKPhysical(Uruobjectref sceneobj, Uruobjectref physref)
    {
        plSimulationInterface r = new plSimulationInterface();
        r.parent = plObjInterface.createDefault(sceneobj);
        r.physical = physref;
        r.u2 = 0;
        r.props = HsBitVector.createDefault();
        return r;
    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        //data.writeInt(count);
        //data.writeInts(props);
        props.compile(data);
        data.writeInt(u2);
        physical.compile(data);
    }
}

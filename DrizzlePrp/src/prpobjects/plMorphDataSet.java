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
//import java.util.Vector;
import shared.*;


public class plMorphDataSet extends uruobj
{
    int morphArrayCount;
    PlMorphArray[] morphs;
    
    public plMorphDataSet(context c) throws readexception
    {
        morphArrayCount = c.readInt();
        morphs = c.readArray(PlMorphArray.class, morphArrayCount);
        
    }
    
    public static class PlMorphArray extends uruobj
    {
        int deltaCount;
        PlMorphDelta[] deltas;
        
        public PlMorphArray(context c) throws readexception
        {
            deltaCount = c.readInt();
            deltas = c.readArray(PlMorphDelta.class, deltaCount);
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(deltaCount);
            c.writeArray2(deltas);
        }
        
        public static class PlMorphDelta extends uruobj
        {
            Flt weight;
            int spanCount;
            PlMorphSpan[] spans;
            
            public PlMorphDelta(context c) throws readexception
            {
                weight = new Flt(c);
                if(!weight.approxequals(0)) //always 0 for males and females.
                {
                    int dummy=0;
                }
                spanCount = c.readInt();
                spans = c.readArray(PlMorphSpan.class, spanCount);
            }
            public void compile(Bytedeque c)
            {
                weight.compile(c);
                c.writeInt(spanCount);
                c.writeArray2(spans);
            }
            
            public static class PlMorphSpan extends uruobj
            {
                int deltaCount;
                int numUVWChans;
                PlVertDelta[] deltas;
                Vertex[] UVWs;
                
                public PlMorphSpan(context c) throws readexception
                {
                    deltaCount = c.readInt();
                    numUVWChans = c.readInt();
                    deltas = c.readArray(PlVertDelta.class, deltaCount);
                    UVWs = c.readArray(Vertex.class, deltaCount*numUVWChans);
                    for(Vertex v: UVWs)
                    {
                        //v.x = Flt.zero();
                        //v.y = Flt.zero();
                        //v.z = Flt.zero();
                        if(!v.x.approxequals(0)) //only happens with male cloths in the moul files.
                        {
                            int dummy=0;
                        }
                    }
                }
                public void compile(Bytedeque c)
                {
                    c.writeInt(deltaCount);
                    c.writeInt(numUVWChans);
                    c.writeArray2(deltas);
                    c.writeArray(UVWs);
                }

                public static class PlVertDelta extends uruobj
                {
                    short idx;
                    short padding;
                    Vertex pos;
                    Vertex norm;
                    
                    public PlVertDelta(context c) throws readexception
                    {
                        idx = c.readShort();
                        padding = c.readShort();
                        if(padding!=0)
                        {
                            int dummy=0;
                        }
                        pos = new Vertex(c);
                        norm = new Vertex(c);
                        float dot = pos.dot(norm);
                        float normdot = norm.dot(norm);
                        if(!norm.x.approxequals(0))
                        {
                            int dummy=0;
                        }
                    }
                    public void compile(Bytedeque c)
                    {
                        c.writeShort(idx);
                        c.writeShort(padding);
                        pos.compile(c);
                        norm.compile(c);
                    }
                }
            }
        }
    }
    public void compile(Bytedeque c)
    {
        c.writeInt(morphArrayCount);
        c.writeArray2(morphs);
    }
    
}

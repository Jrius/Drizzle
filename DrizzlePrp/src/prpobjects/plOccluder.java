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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;

public class plOccluder extends uruobj
{
    //Objheader xheader;
    
    plObjInterface parent;
    Flt f1;
    public short counta;
    public plCullPoly[] blocks;
    Uruobjectref scenenode;
    short countd;
    Uruobjectref[] visRegion;
    
    //int u1;
    //int u2;
    //Vertex v1;
    //Vertex v2;
    //Flt[] conglomerate;
    public BoundingBox fWorldBounds;
    
    //Flt[] u3;
    //short counta;
    //SubOccluder[] blocks;
    //Uruobjectref scenenode;
    //short countd;
    //Uruobjectref[] visRegion;
    
    public plOccluder(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new plObjInterface(c);//,false);
        
        //same as in PlMobileOccluder:
        //in pots: sub_48a930, in m5: sub_643ca0
        //int u1,int,vertex,vertex,
        //if u1&1==0 then vertex, 3*(vertex, flt, flt)
        
        /*u1 = c.readInt();
        u2 = c.readInt();
        v1 = new Vertex(c);
        v2 = new Vertex(c);
        if((u1&0x1)==0)
        {
            //if u1&1==0 then vertex, 3*(vertex, flt, flt)
            //m.warn("untested case in PlOccluder.");
            conglomerate = c.readArray(Flt.class, 18);
        }*/
        fWorldBounds = new BoundingBox(c);
        
        f1 = new Flt(c);
        counta = c.readShort();
        blocks = c.readArray(plCullPoly.class, b.Int16ToInt32(counta));
        scenenode = new Uruobjectref(c);
        countd = c.readShort();
        visRegion = c.readArray(Uruobjectref.class, b.Int16ToInt32(countd));
        
        //u1 = c.readInt();
        //u2 = c.readInt();
        //u3 = c.readVector(Flt.class, 7);
        //counta = c.readShort();
        //blocks = c.readVector(SubOccluder.class, counta);
        //scenenode = new Uruobjectref(c);
        //countd = c.readShort();
        //visRegion = c.readVector(Uruobjectref.class, countd);
        
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        
        //same as in PlMobileOccluder:
        /*c.writeInt(u1);
        c.writeInt(u2);
        v1.compile(c);
        v2.compile(c);
        if((u1&0x1)==0)
        {
            c.writeArray(conglomerate);
        }*/
        fWorldBounds.compile(c);
        
        f1.compile(c);
        c.writeShort(counta);
        c.writeArray2(blocks);
        scenenode.compile(c);
        c.writeShort(countd);
        c.writeArray2(visRegion);
    }
    
    public static class plCullPoly extends uruobj//was SubOccluder
    {
        int u1;
        public Vertex fNorm; //was v1
        Flt f1; //dist
        public Vertex fCenter; //was v2
        Flt f2; //radius
        public int count;
        public Vertex[] vertices;
        
        //int countb;
        //Flt[] floats1;
        //int countc;
        //Flt[] floats2;
        
        public plCullPoly(context c) throws readexception
        {
            u1 = c.readInt();
            fNorm = new Vertex(c);
            f1 = new Flt(c);
            fCenter = new Vertex(c);
            f2 = new Flt(c);
            count = c.readInt();
            vertices = c.readArray(Vertex.class, count);
            
            //countb = c.readInt();
            //int countb2 = (countb==0) ? 2 : countb; //if countb=0, set it to 2.
            //floats1 = c.readVector(Flt.class, countb2*4);
            //countc = c.readInt();
            //floats2 = c.readVector(Flt.class, countc*3);
            
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(u1);
            fNorm.compile(c);
            f1.compile(c);
            fCenter.compile(c);
            f2.compile(c);
            c.writeInt(count);
            c.writeArray(vertices);
            //c.writeInt(countb);
            //c.writeVector(floats1);
            //c.writeInt(countc);
            //c.writeVector(floats2);
        }
    }
}
/*public class PlOccluder extends uruobj
{
    //Objheader xheader;
    
    PlObjInterface parent;
    int u1;
    int u2;
    Flt[] u3;
    short counta;
    SubOccluder[] blocks;
    Uruobjectref scenenode;
    short countd;
    Uruobjectref[] visRegion;
    
    public PlOccluder(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        parent = new PlObjInterface(c);//,false);
        u1 = c.readInt();
        u2 = c.readInt();
        u3 = c.readVector(Flt.class, 7);
        counta = c.readShort();
        blocks = c.readVector(SubOccluder.class, counta);
        scenenode = new Uruobjectref(c);
        countd = c.readShort();
        visRegion = c.readVector(Uruobjectref.class, countd);
        
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(u1);
        c.writeInt(u2);
        c.writeVector(u3);
        c.writeShort(counta);
        c.writeVector(blocks);
        scenenode.compile(c);
        c.writeShort(countd);
        c.writeVector(visRegion);
    }
    
    public static class SubOccluder extends uruobj
    {
        int countb;
        Flt[] floats1;
        int countc;
        Flt[] floats2;
        
        public SubOccluder(context c) throws readexception
        {
            countb = c.readInt();
            int countb2 = (countb==0) ? 2 : countb; //if countb=0, set it to 2.
            floats1 = c.readVector(Flt.class, countb2*4);
            countc = c.readInt();
            floats2 = c.readVector(Flt.class, countc*3);
            
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(countb);
            c.writeVector(floats1);
            c.writeInt(countc);
            c.writeVector(floats2);
        }
    }
}*/

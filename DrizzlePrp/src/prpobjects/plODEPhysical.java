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

import static java.lang.Math.*;
import shared.Vertex;
import shared.Quat;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plODEPhysical extends uruobj
{
    plSynchedObject parent;
    int type;
    
    //type 5
    int vertexcount;
    Vertex[] vertices;
    int surfacecount;
    int[] surfaces;
    int xu3;
    byte[] xu3s;
    
    //type 1
    Flt f1;
    Flt f2;
    Flt f3;
    
    //type 2
    Flt u4;
    
    Flt f8;
    int u8;
    int u9;
    int u10;
    int u11;
    int u12;
    short u13;
    Uruobjectref sceneobject;
    Uruobjectref scenenode;
    
    //type 6
    Flt f6a;
    Flt f6b;
    
    public Vertex sphereposoverride;
    
    public plHKPhysical.HKPhysical convertee;
    public plHKPhysical.HKPhysical convertToHKPhysical(prpfile prp) // only used by HexIsle.
    {
        // note: doesn't include physics fixes for Myst V - hopefully this means people won't have to re-convert HexIsle...
        
        if(type==5)
        {
            convertee.format = 4;
            convertee.xproxybounds = new plHKPhysical.HKProxyBounds();
            convertee.xproxybounds.parent.vertexcount = vertexcount;
            e.ensure(vertices.length==vertexcount);
            convertee.xproxybounds.parent.vertices = vertices;

            //write surfaces
            convertee.xproxybounds.facecount = surfacecount;
            convertee.xproxybounds.faces = new shared.ShortTriplet[surfacecount];

            for(int i=0;i<surfacecount;i++)
            {
                short point1 = (short)surfaces[i*3+0];
                short point2 = (short)surfaces[i*3+1];
                short point3 = (short)surfaces[i*3+2];
                shared.ShortTriplet trip = shared.ShortTriplet.createFromShorts(point1, point2, point3);
                convertee.xproxybounds.faces[i] = trip;
            }

        }
        else if(type==1)
        {
            convertee.format = 1;

            convertee.xboxbounds = new plHKPhysical.HKBoxBounds();
            //convertee.xboxbounds.parent.

            Vertex center = findOffsetVectorFromSceneObject(prp, sceneobject);
            m.msg("Resizing boxes in ODEPhysical...");
            Vertex cornervector = new Vertex(f1.mult((float)0.5),f2.mult((float)0.5),f3.mult((float)0.5)); //is this order correct?

            //boxbounds is just proxybounds
            int vertexcount = 8;
            int facecount = 12;
            Vertex[] vertices = new Vertex[]{
                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
            };
            short[] faces = new short[]{
                0,2,1,
                1,2,3,
                0,1,4,
                1,5,4,
                0,4,2,
                2,4,6,
                1,3,7,
                7,5,1,
                3,2,7,
                2,6,7,
                4,7,6,
                4,5,7,
            };
            //reverse-chirality:
            //short[] faces = new short[]{
            //    0,1,2,
            //    1,3,2,
            //    0,4,1,
            //    1,4,5,
            //    0,2,4,
            //    2,6,4,
            //    1,7,3,
            //    7,1,5,
            //    3,7,2,
            //    2,7,6,
            //    4,6,7,
            //    4,7,5,
            //};

            e.ensure(vertices.length==vertexcount);
            e.ensure(faces.length==facecount*3);

            convertee.xboxbounds.parent.parent.vertexcount = vertexcount;
            convertee.xboxbounds.parent.parent.vertices = vertices;
            convertee.xboxbounds.parent.facecount = facecount;
            convertee.xboxbounds.parent.faces = new shared.ShortTriplet[facecount];
            for(int i=0;i<facecount;i++)
            {
                short point1 = (short)faces[i*3+0];
                short point2 = (short)faces[i*3+1];
                short point3 = (short)faces[i*3+2];
                shared.ShortTriplet trip = shared.ShortTriplet.createFromShorts(point1, point2, point3);
                convertee.xboxbounds.parent.faces[i] = trip;
            }

        }
        else if(type==2)
        {
            convertee.format = 2;

            convertee.xspherebounds = new plHKPhysical.HKSphereBounds();
            Vertex offset = findOffsetVectorFromSceneObject(prp, sceneobject);
            convertee.xspherebounds.offset = offset;
            convertee.xspherebounds.radius = u4;
        }
        else if(type==6)
        {
            m.throwUncaughtException("ODEPhysical: unable to compile type 6.");
        } //convertee.format = 0;//changethis!!!
        else
        {
            m.throwUncaughtException("ODEPhysical: unable to compile unknown type.");
        }
        return convertee;
    }
    public plODEPhysical(context c) throws readexception
    {
        parent = new plSynchedObject(c);
        type = c.readInt();
        if(type==5)
        {
            vertexcount = c.readInt(); //vertex count
            vertices = c.readArray(Vertex.class, vertexcount); //vertices

            surfacecount = c.readInt(); //face count
            surfaces = c.readInts(3*surfacecount); //faces
            for(int point: surfaces)
            {
                if(point >= 0x00010000)
                {
                    throw new readexception("ODEPhysical has more than 2^16 points, so it can't be converted.");
                }
            }
            
            xu3 = c.readInt(); //equals surfacecount?
            xu3s = c.readBytes(xu3); //one byte for each surface?
            
        }
        else if(type==1) // box
        {
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
            //throw new readexception("ODEPhysical: can read okay but throwing error to ignore.");
        }
        else if(type==2) // sphere
        {
            u4 = new Flt(c); //same as 6
            //throw new readexception("ODEPhysical: can read okay but throwing error to ignore.");
        }
        else if(type==6) // cylinder
        {
            //cylinder.  I would have to convert it to a discreet cylinder.
            //m.msg("Untested ODE case6...");
            f6a = new Flt(c); //same as 2 //cylinder length
            f6b = new Flt(c); //cylinder radius
            //Vertex offset = this.findOffsetVectorFromSceneObject(c.prp, sceneobject);
            //throw new readexception("ODEPhysical: can read okay but throwing error to ignore.");
        }
        else
        {
            m.msg("Untested ODE case unknown...");
        }
        
        f8 = new Flt(c); //mass
        u8 = c.readInt(); //category (=coltype?)
        u9 = c.readInt();
        u10 = c.readInt();
        u11 = c.readInt();
        if(c.readversion==4)
        {
            u12 = c.readInt(); //flags
        }
        u13 = c.readShort(); //LOSDB
        
        /*if(c.readversion==7)
        {
            //int xu14 = c.readInt();
            //int xu15 = c.readInt();
        }*/
        
        sceneobject = new Uruobjectref(c); //plSceneObject
        scenenode = new Uruobjectref(c); //plSceneNode
        
        if(shared.State.AllStates.getStateAsBoolean("reportPhysics"))
        {

            m.msg("Physics: type=",Integer.toString(type)+";f8="+f8.toString()+";u8=0x"+Integer.toHexString(u8)
                    +";u9=0x"+Integer.toHexString(u9)+";u10=0x"+Integer.toHexString(u10)
                    +";u11="+Integer.toString(u11)+";u12=0x"+Integer.toHexString(u12)
                    +";u13="+Short.toString(u13)+";object="+c.curRootObject.toString());
        }
        
        
        //make converted version...
        convertee = plHKPhysical.HKPhysical.createBlank();
        convertee.parent = this.parent;
        convertee.position = Vertex.zero();
        convertee.orientation = Quat.identity();
        convertee.mass = f8;
        convertee.RC = new Flt(0.5f);
        convertee.EL = Flt.zero();
        if(type==5)
        {
            convertee.format = 4;
        }
        else if(type==1)
        {
            convertee.format = 1;
        }
        else if(type==2)
        {
            convertee.format = 2;
        }
        else if(type==6)
        {
            convertee.format = 3; // will create hull cylinder
            //throw new shared.readwarningexception("ODEPhysical: able to read, but ignoring unhandled type 6.(cylinder)");
        }
        else
        {
            throw new readexception("ODEPhysical: able to read okay, but throwing error to ignore unhandled format.");
        }
        convertee.u1 = 0;
        convertee.coltype = 0x200;
        convertee.flagsdetect = 0;
        convertee.flagsrespond = 0;
        convertee.u2 = 0;
        convertee.u3 = 0;
        convertee.sceneobject = this.sceneobject;
        convertee.group = new HsBitVector(0);
        convertee.scenenode = this.scenenode;
        convertee.LOSDB = 0x44;
        convertee.subworld = Uruobjectref.none();
        convertee.soundgroup = Uruobjectref.none();
        
        //handle flags...
        convertee.LOSDB = b.Int16ToInt32(u13);
        
        boolean givemass = false;
        
        //ordinary surface (collider)
        if(
                (u8==0x2000000 && u9==0x0 && u10==0x0) //verified - all over!
                //||(u8==0x4800000 && u9==0x20000 && u10==0x0)
                ||(u8==0x2800000 && u9==0x0 && u10==0x0) //verified, bubble collider in direbo
                ||(u8==0x2000000 && u9==0x7860000 && u10==0x0) //laki collision
                )
        {
            //m.warn("physics - collider: " + convertee.sceneobject.xdesc.objectname);
            convertee.u1 = 0;
            convertee.coltype = 0x200;
            convertee.flagsdetect = 0;
            convertee.flagsrespond = 0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x44;
            convertee.LOSDB |= 0x4; // camera blocking
            convertee.group = new HsBitVector(0);
        }
        else if(u8==0x2000000 && u9==0x20000 && u10==0x0) //descent
        {
            // it seems these are just more complex colliders
            // Dustin set a respond flag, but HSplasma doesn't show it.
            // No need to set mass for these
            
            //m.warn("physics - descent collision: " + convertee.sceneobject.xdesc.objectname);
            convertee.u1 = 0;
            convertee.coltype = 0x200;
            convertee.flagsdetect = 0;
            convertee.flagsrespond = 0x20000;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x2;
            convertee.group = new HsBitVector(4);
        }
        else if(u8==0x1800000 && u9==0x3000000 && u10==0x0) //descent, dragable
        {
            // these should always have mass, but as a matter of fact they ALWAYS have mass already anyway...
            // (unless they are handled by the mass flag ? let's try this way, since it seems to work)
            
            //m.warn("physics - descent draggable: " + convertee.sceneobject.xdesc.objectname);
            //guessing...
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0x20000;
            convertee.flagsrespond = 0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x2;
            convertee.group = new HsBitVector(4);
        }
        //detection
        else if( false
                //(u8==0x4000000 && u9==0x0 && u10==0x20000)
              ||(u8==0x4000000 && u9==0x0 && u10==0x8000000) //verified Direbo linking books to descent
              ||(u8==0x4800000 && u9==0x0 && u10==0x8000000) //verified direbo pedestal buttons
                
                )
        {
            //m.warn("physics - direbo book+pedestal(verified) + detection: " + convertee.sceneobject.xdesc.objectname);
            //m.warn("(Avatar detector region - detector+avatar)");
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0x8000000;
            convertee.flagsrespond = 0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x0;
            convertee.group = new HsBitVector(4);
            
            givemass = true;
        }
        else if((u8==0x4000000 && u9==0x0 && u10==0x20000) //not verified - direbo gates: may actually just be blocker.
                ||(u8==0x4800000 && u9==0x0 && u10==0x20000) //dtctFloot greatShaft platform
                ||(u8==0x4000000 && u9==0x0 && u10==0x20002) //laki detector
                ||(u8==0x4800000 && u9==0x0 && u10==0x20002) //laki detector
        ){
            //m.warn("physics - direbo+laki+shaft floo - setting mass: " + convertee.sceneobject.xdesc.objectname);
            //m.warn("(avatar+npc detectors)"); // sometimes regular avatar-only regions
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0x20000;
            convertee.flagsrespond = 0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x0; //stting this to 2 seemed to make everything else a clickable, except the exclude regions covering the gate and switch.  Is this because it thinks the gate is open?
            convertee.group = new HsBitVector(4);
            
            givemass = true;
        }
        //myst5 has a kind of clickable that is dragable, e.g. the door handle in k'veer.
        else if((u8==0x4000000 && u9==0x0 && u10==0x0) //wrong, it produces blockers, when it shouldn't
            //|| (u8==0x4000000 && u9==0x0 && u10==0x8000000))
            //|| (u8==0x4000000 && u9==0x20000)
              ||(u8==0x4800000 && u9==0x0 && u10==0x0) //verified - direbo pedestal buttons
              ||(u8==0x4000000 && u9==0x0 && u10==0x2) //e.g. Tahgira CrackBahroDtct
        ){
            //m.warn("physics - draggable+direbo pedestal+tahgira crack: " + convertee.sceneobject.xdesc.objectname);
            //m.warn("(more clickables/draggables (Noloben Ladder, Shaft ventilation cranck))");
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0;
            convertee.flagsrespond = 0x0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x2;
            convertee.group = new HsBitVector(0x4);
            
            givemass = true;
        }
        else if((u8==0x4000000 && u9==0x20000 && u10==0x0) //verified - Direbo descent linking books.
              ||(u8==0x4800000 && u9==0x20000 && u10==0x0) //not verified - direbo gates
              ||(u8==0x4000000 && u9==0x1020000 && u10==0x0) //not verified - Direbo gates(may actually just be physical blocker)
              ||(u8==0x4800000 && u9==0x1020000 && u10==0x0) //e.g. some telescope stuff in Todelmer
                )
        {
            //m.warn("physics - direbo linking books+gates+telescope: " + convertee.sceneobject.xdesc.objectname);
            //m.warn("(clickable+some drag+xrgn? - detector)");
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0;
            convertee.flagsrespond = 0x20000;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x2;
            convertee.group = new HsBitVector(0x4);
            
            givemass = true;
        }
        else if ((u8==0x4800000 && u9==0x0 && u10==0x2000000) // piranha bird
                || (u8==0x20000 && u9==0x3040000 && u10==0x0))
        {
            // not sure, but close enough
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0x8000000;
            convertee.flagsrespond = 0;
            convertee.u2 = 0;
            convertee.u3 = 0;
            convertee.group = new HsBitVector(4);
            givemass = true;
        }
        else if(false)//u8==0x4000000 && u9==0x1020000 && u10==0x0)
        {
            convertee.u1 = 0;
            convertee.coltype = 0x400;
            convertee.flagsdetect = 0;
            convertee.flagsrespond = 0x1020000;
            convertee.u2 = 0;
            convertee.u3 = 0;
            //convertee.LOSDB = 0x0;
            convertee.group = new HsBitVector(0x4);
            //convertee.mass = Flt.one(); //assign mass
        }
        else
        {
            m.msg("Skipping physics: type=",Integer.toString(type)+";f8="+f8.toString()+";u8=0x"+Integer.toHexString(u8)
                    +";u9=0x"+Integer.toHexString(u9)+";u10=0x"+Integer.toHexString(u10)
                    +";u11="+Integer.toString(u11)+";u12=0x"+Integer.toHexString(u12)
                    +";u13="+Short.toString(u13)+";object="+c.curRootObject.toString());
            //if(shared.State.AllStates.getStateAsBoolean("skipPhysics")) throw new readexception("ODEPhysical: unhandled case.");
            boolean skipPhysics = false;
            if(skipPhysics) throw new readexception("ODEPhysical: unhandled case.");
        }
        
        
        //m.msg("type: "+type);
        //m.msg(u8 + ";" + u9 + ";" + u10);

        //if (u8 == 0x2800000)
        //    m.warn("MASS RELATED");
        //else if ((u8 & 0x800000) != 0)
        //    m.warn("FLAG FOR MASS");
        
        
        if (givemass == true || (type == 5 && (u8 & 0x800000) != 0))
        {
            if (convertee.mass.equals(Flt.zero()))
                convertee.mass = Flt.one(); //assign mass
            convertee.group.values[0] |= plHKPhysical.HKPhysical.kPropPinned;
        }
        
        if (type==1 || type==2)
        {
            if (convertee.mass.equals(Flt.zero()))
            {
                convertee.mass = Flt.one();
                convertee.group.values[0] |= plHKPhysical.HKPhysical.kPropPinned;
            }
        }
       
       //if(convertee.flagsdetect!=0x0) convertee.mass = Flt.one();
        
    }
    
    public void compile(Bytedeque c)
    {
        m.warn("compile not implemented.",this.toString());
        m.warn("not tested with pots.",this.toString());
    }
    
    public void compileSpecial(Bytedeque c)
    {
        convertee.parent.compile(c);
        convertee.position.compile(c);
        convertee.orientation.compile(c);
        convertee.mass.compile(c);
        convertee.RC.compile(c);
        convertee.EL.compile(c);
        
        c.writeInt(convertee.format); //format, proxy bounds/mesh bounds
        c.writeShort(convertee.u1);//u1 short
        c.writeShort(convertee.coltype);//coltype short
        c.writeInt(convertee.flagsdetect);//flagsdetect int
        c.writeInt(convertee.flagsrespond);//flagsrespond int
        c.writeByte(convertee.u2);//u2 byte
        c.writeByte(convertee.u3);//u3 byte
        
        if(type==6)
        {
            // cylinder type, using f6a and f6b;
            
            // only Laki's bird uses these, but let's do this either way...
            
            int resolution = 24; // should be enough for accurate collisions
            Vertex[] verts;
            verts = new Vertex[resolution*2];
            
            // generate vertices
            for (int i=0; i<resolution; i++)
            {
                Vertex vert1 = new Vertex(0,0,0);
                vert1.x = new Flt( (float) (cos( ((float)i) / resolution * 2*PI )*f6b.toJavaFloat()) );
                vert1.y = new Flt( (float) (sin( ((float)i) / resolution * 2*PI )*f6b.toJavaFloat()) );
                vert1.z = new Flt(f6a.toJavaFloat()/2.f);
                verts[i] = vert1;
                
                Vertex vert2 = new Vertex(0,0,0);
                vert2.x = new Flt( (float) (cos( ((float)i) / resolution * 2*PI )*f6b.toJavaFloat()) );
                vert2.y = new Flt( (float) (sin( ((float)i) / resolution * 2*PI )*f6b.toJavaFloat()) );
                vert2.z = new Flt(-f6a.toJavaFloat()/2.f);
                verts[i+resolution] = vert2;
            }
            
            c.writeInt(resolution*2);
            c.writeArray(verts);
        }
        else if(type==5)
        {
            //write vertices
            c.writeInt(vertexcount);
            e.ensure(vertices.length==vertexcount);
            c.writeArray(vertices);

            //write surfaces
            c.writeInt(surfacecount);
            for(int point: surfaces)
            {
                short point2 = (short)point;
                c.writeShort(point2);
            }
        }
        else if(type==1) //box
        {
            //Vertex center = findOffsetVectorFromSceneObject(c.prp, sceneobject);
            //m.msg("Resizing boxes in ODEPhysical...");
            Vertex cornervector = new Vertex(f1.mult((float)0.5),f2.mult((float)0.5),f3.mult((float)0.5)); //is this order correct?

            //boxbounds is just proxybounds
            int vertexcount = 8;
            int facecount = 12;
            Vertex[] vertices = new Vertex[]{
//                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
//                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
//                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
//                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
//                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
//                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
//                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
//                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(cornervector.x.neg() , cornervector.y.neg()  ,cornervector.z.neg()   ),
                new Vertex(cornervector.x       , cornervector.y.neg()  ,cornervector.z.neg()   ),
                new Vertex(cornervector.x.neg() , cornervector.y        ,cornervector.z.neg()   ),
                new Vertex(cornervector.x       , cornervector.y        ,cornervector.z.neg()   ),
                new Vertex(cornervector.x.neg() , cornervector.y.neg()  ,cornervector.z         ),
                new Vertex(cornervector.x       , cornervector.y.neg()  ,cornervector.z         ),
                new Vertex(cornervector.x.neg() , cornervector.y        ,cornervector.z         ),
                new Vertex(cornervector.x       , cornervector.y        ,cornervector.z         ),
            };
            short[] faces = new short[]{
                0,2,1,
                1,2,3,
                0,1,4,
                1,5,4,
                0,4,2,
                2,4,6,
                1,3,7,
                7,5,1,
                3,2,7,
                2,6,7,
                4,7,6,
                4,5,7,
            };
            //reverse-chirality:
            //short[] faces = new short[]{
            //    0,1,2,
            //    1,3,2,
            //    0,4,1,
            //    1,4,5,
            //    0,2,4,
            //    2,6,4,
            //    1,7,3,
            //    7,1,5,
            //    3,7,2,
            //    2,7,6,
            //    4,6,7,
            //    4,7,5,
            //};
            
            e.ensure(vertices.length==vertexcount);
            e.ensure(faces.length==facecount*3);
            
            c.writeInt(vertexcount);
            c.writeArray(vertices);
            c.writeInt(facecount);
            c.writeShorts(faces);
        }
        else if(type==2) //sphere
        {
            //Vertex offset = findOffsetVectorFromSceneObject(c.prp, sceneobject);
            Vertex offset = new Vertex(0,0,0); // MV doesn't actually have any offset param.
            
            if (sphereposoverride != null)
                offset = sphereposoverride;
            
            offset.compile(c); //offset
            u4.compile(c); //radius
        }
        else
        {
            m.err("critical error: ODEPhysical: currently unable to compile this type");
        }
        
        convertee.sceneobject.compile(c);
        convertee.group.compile(c);
        convertee.scenenode.compile(c);
        c.writeInt(convertee.LOSDB);
        convertee.subworld.compile(c);
        convertee.soundgroup.compile(c);

        
        //m.err("ODE compile not implemented.");
        
        /*parent.compile(c);
        Vertex.zero().compile(c);//position
        Quat.identity().compile(c);//orientation;
        Flt.zero().compile(c);//mass
        Flt.zero().compile(c);//RC
        Flt.zero().compile(c);//EL
        
        
        if(type==5)
        {
            //write flags
            c.writeInt(4); //format, proxy bounds/mesh bounds
            c.writeShort((short)0);//u1 short
            c.writeShort((short)0x200);//coltype short
            c.writeInt(0);//flagsdetect int
            c.writeInt(0);//flagsrespond int
            c.writeByte((byte)0);//u2 byte
            c.writeByte((byte)0);//u3 byte

            //write vertices
            c.writeInt(vertexcount);
            e.ensure(vertices.length==vertexcount);
            c.writeVector(vertices);

            //write surfaces
            c.writeInt(surfacecount);
            for(int point: surfaces)
            {
                short point2 = (short)point;
                c.writeShort(point2);
            }
            
        }
        else if(type==1) //box
        {
            //write flags
            c.writeInt(1); //format, box bounds
            c.writeShort((short)0);//u1 short
            c.writeShort((short)0x200);//coltype short
            c.writeInt(0);//flagsdetect int
            c.writeInt(0);//flagsrespond int
            c.writeByte((byte)0);//u2 byte
            c.writeByte((byte)0);//u3 byte

            Vertex center = findOffsetVectorFromSceneObject(c.prp, sceneobject);
            Vertex cornervector = new Vertex(f1,f2,f3); //is this order correct?

            //boxbounds is just proxybounds
            int vertexcount = 8;
            int facecount = 12;
            Vertex[] vertices = new Vertex[]{
                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.sub(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.sub(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.sub(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
                new Vertex(center.x.add(cornervector.x),center.y.add(cornervector.y),center.z.add(cornervector.z)),
            };
            short[] faces = new short[]{
                0,2,1,
                1,2,3,
                0,1,4,
                1,5,4,
                0,4,2,
                2,4,6,
                1,3,7,
                7,5,1,
                3,2,7,
                2,6,7,
                4,7,6,
                4,5,7,
            };
            //reverse-chirality:
            //short[] faces = new short[]{
            //    0,1,2,
            //    1,3,2,
            //    0,4,1,
            //    1,4,5,
            //    0,2,4,
            //    2,6,4,
            //    1,7,3,
            //    7,1,5,
            //    3,7,2,
            //    2,7,6,
            //    4,6,7,
            //    4,7,5,
            //};
            
            e.ensure(vertices.length==vertexcount);
            e.ensure(faces.length==facecount*3);
            
            c.writeInt(vertexcount);
            c.writeVector(vertices);
            c.writeInt(facecount);
            c.writeShorts(faces);
            
        }
        else if(type==2) //sphere
        {
            //write flags
            c.writeInt(2); //format, sphere bounds
            c.writeShort((short)0);//u1 short
            c.writeShort((short)0x200);//coltype short
            c.writeInt(0);//flagsdetect int
            c.writeInt(0);//flagsrespond int
            c.writeByte((byte)0);//u2 byte
            c.writeByte((byte)0);//u3 byte

            Vertex offset = findOffsetVectorFromSceneObject(c.prp, sceneobject);
            
            offset.compile(c); //offset
            u4.compile(c); //radius
        }
        else
        {
            m.err("cricial error: ODEPhysical: currently unable to compile this type");
        }
        
        sceneobject.compile(c);
        new HsBitVector(0).compile(c);//group hsbitvector
        scenenode.compile(c);
        c.writeInt(0x44);//LOSDB int, could also be 0x40 or 0x45, I guess.
        Uruobjectref.none().compile(c);//subworld uruobjectref
        Uruobjectref.none().compile(c);//soundgroup uruobjectref
        */
                
                
    }
    
    public static Vertex findOffsetVectorFromSceneObject(prpfile prp, Uruobjectref sceneobject)
    {
        //find the offset vector through the coordinate interface.
        e.ensure(prp!=null);
        e.ensure(sceneobject.hasref());
        PrpRootObject a = prp.findObjectWithDesc(sceneobject.xdesc);
        e.ensure(a.header.objecttype==Typeid.plSceneObject);
        plSceneObject b = a.castTo();
        Uruobjectref d = b.coordinateinterface;
        e.ensure(d.hasref());
        PrpRootObject f = prp.findObjectWithDesc(d.xdesc);
        plCoordinateInterface g = f.castTo();
        Vertex offset = g.localToParent.convertTo3Vector();
        return offset;
    }
}

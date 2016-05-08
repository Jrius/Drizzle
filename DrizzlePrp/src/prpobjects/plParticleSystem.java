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
import uru.context;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
//import java.util.Vector;
import shared.readexception;

/**
 *
 * @author user
 */
public class plParticleSystem extends uruobj
{
    //Objheader xheader;
    x001EModifier parent; //should this be an x0028Synchedobject directly?
    Uruobjectref material;
    //UnknownBlock[] unknownblocks;
    PrpTaggedObject[] unknownobjects;
    //PrpController[] controllers;
    int u1;
    int u2;
    int somecount;
    int u3;
    Flt u4;
    //Flt u5;
    //Flt u6;
    //Flt u7;
    Vertex u5;
    Flt u8;
    Flt u9;
    int count;
    //short subtype1;
    //short subtype2;
    //Typeid subtype1;
    //Typeid subtype2;
    //plParticleEmitter xplParticleEmitter;
    //plSimpleParticleGenerator xplSimpleParticleGenerator;
    PrpTaggedObject[] particleObjects;
    Refvector refs1;
    Refvector refs2;
    Refvector refs3;
    Refvector refs4;
    
    /*Flt[] u11;
    int u12;
    int someothercount;
    int flag;
    Flt[] u13; //a color, perhaps?
    int movements;
    Uruobjectref[] movementsarray;
    int areas;
    Uruobjectref[] areasarray;
    int collisioneffects;
    Uruobjectref[] collisioneffectsarray;
    int lights;
    Uruobjectref[] lightsarray;*/
    
    public plParticleSystem(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new x001EModifier(c);//,false);
        material = new Uruobjectref(c);
        unknownobjects = new PrpTaggedObject[5];
        for(int i=0;i<5;i++)
        {
            unknownobjects[i] = new PrpTaggedObject(c);
        }
        //unknownblocks = c.readVector(UnknownBlock.class, 5);
        //controllers = c.readVector(AbstractController.class, 5);
        u1 = data.readInt();
        u2 = data.readInt();
        somecount = data.readInt();
        u3 = data.readInt();
        u4 = new Flt(c);
        u5 = new Vertex(c);
        u8 = new Flt(c);
        u9 = new Flt(c);
        count = data.readInt();
        //short test1 = data.readShort(); //e.ensure(subtype1,0x02d9);
        //short test2 = data.readShort(); //e.ensure(subtype2,0x02d8);
        //subtype1 = Typeid.Read(c); e.ensure(subtype1,Typeid.plParticleEmitter);
        //subtype2 = Typeid.Read(c); e.ensure(subtype2,Typeid.plSimpleParticleGenerator);
        //almost certainly wrong:
        /*if(subtype1==Typeid.plParticleEmitter)
        {
            xplParticleEmitter = new plParticleEmitter(c);
        }*/
        particleObjects = new PrpTaggedObject[count];
        for(int i=0;i<count;i++)
        {
            particleObjects[i] = new PrpTaggedObject(c);
        }
        //if(subtype2==Typeid.plSimpleParticleGenerator)
        //{
        //    xplSimpleParticleGenerator = new plSimpleParticleGenerator(data);
        //}
        refs1 = new Refvector(c);
        refs2 = new Refvector(c);
        refs3 = new Refvector(c);
        
        refs4 = new Refvector(c);
        
        /*u11 = new Flt[5];
        for(int i=0;i<5;i++)
        {
            u11[i] = new Flt(c);
        }
        u12 = data.readInt();
        someothercount = data.readInt();
        flag = data.readInt();
        u13 = new Flt[4];
        for(int i=0;i<4;i++)
        {
            u13[i] = new Flt(c);
        }
        
        movements = data.readInt();
        movementsarray = new Uruobjectref[movements];
        for(int i=0;i<movements;i++)
        {
            movementsarray[i] = new Uruobjectref(c);
        }

        areas = data.readInt();
        areasarray = new Uruobjectref[areas];
        for(int i=0;i<areas;i++)
        {
            areasarray[i] = new Uruobjectref(c);
        }
        
        collisioneffects = data.readInt();
        collisioneffectsarray = new Uruobjectref[collisioneffects];
        for(int i=0;i<collisioneffects;i++)
        {
            collisioneffectsarray[i] = new Uruobjectref(c);
        }

        lights = data.readInt();
        lightsarray = new Uruobjectref[lights];
        for(int i=0;i<lights;i++)
        {
            lightsarray[i] = new Uruobjectref(c);
        }*/
                
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        material.compile(c);
        for(int i=0;i<5;i++)
        {
            unknownobjects[i].compile(c);
        }
        c.writeInt(u1);
        c.writeInt(u2);
        c.writeInt(somecount);
        c.writeInt(u3);
        u4.compile(c);
        u5.compile(c);
        u8.compile(c);
        u9.compile(c);
        c.writeInt(count);
        for(int i=0;i<count;i++)
        {
            particleObjects[i].compile(c);
        }
        refs1.compile(c);
        refs2.compile(c);
        refs3.compile(c);
        refs4.compile(c);
        
    }
    
    
    /*public static class UnknownBlock extends uruobj
    {
        //short subtype;
        Typeid subtype;
        byte xu1;
        int xsubblockCount;
        SubUnknownBlock2[] subblocks2;*/
        /*int xmarker;
        int xu1;
        int xu2;
        int xu3;
        int xu4;
        int xcounta;
        SubUnknownBlock[] xsubblocks;*/
        
        /*public UnknownBlock(context c) throws readexception
        {
            Bytestream data = c.in;
            subtype = Typeid.Read(c); e.ensure(subtype,Typeid.nil,Typeid.plLeafController);
            
            
            switch(subtype)
            {
                case nil: //this one has no data in it.
                    break;
                case plLeafController:
                    //m.warn("plLeafController not really supported.");
                    throw new readexception("plLeafController not really supported.");
                    //uncomment these lines to return to old way.
                    //xu1 = data.readByte();
                    //xsubblockCount = data.readInt(); //e.ensure(xsubblockCount,4);
                    //subblocks2 = c.readVector(SubUnknownBlock2.class, xsubblockCount);
                    //break;
                default:
                    throw new readexception("x0008ParticleSystem: unknown type");
            }
            //if(subtype!=Typeid.nil)
            //{*/
                /*m.msg("x0008: haven't verified this.");
                xmarker = data.readInt();
                if(xmarker==1)
                {
                    xu1 = data.readInt();
                }
                xu2 = data.readInt();
                xu3 = data.readInt();
                xu4 = data.readInt();
                xcounta = data.readInt();
                xsubblocks = new SubUnknownBlock[xcounta];
                for(int i=0;i<xcounta;i++)
                {
                    xsubblocks[i] = new SubUnknownBlock(data,xmarker);
                }*/
            //}
        /*}
        
        public void compile(Bytedeque c)
        {
            subtype.compile(c);
            
            switch(subtype)
            {
                case nil: //this one has no data in it.
                    break;
                case plLeafController:
                    c.writeByte(xu1);
                    c.writeInt(xsubblockCount);
                    for(int i=0;i<xsubblockCount;i++)
                    {
                        subblocks2[i].compile(c);
                    }
                    break;
                default:
                    m.msg("x0008: unkown type");
                    break;
            }
        }
        
        public static class SubUnknownBlock2 extends uruobj
        {
            int u1;
            byte[] u2 = new byte[6];
            Flt u3;
            
            public SubUnknownBlock2(context c)
            {
                u1 = c.in.readInt();
                u2 = c.in.readBytes(6);
                u3 = new Flt(c);
            }
            
            public void compile(Bytedeque c)
            {
                c.writeInt(u1);
                c.writeBytes(u2);
                u3.compile(c);
            }
        }*/
        
        /*public static class SubUnknownBlock extends uruobj
        {
            int u1;
            int u2;
            Float32[] u3;
            Float32[] xu4;
            
            public SubUnknownBlock(context c, int xmarker)
            {
                m.msg("x0008: haven't verified this.(2)");
                u1 = c.in.readInt();
                u2 = c.in.readInt();
                u3 = new Float32[4];
                for(int i=0;i<4;i++)
                {
                    u3[i] = new Float32(c);
                }
                if(xmarker==1)
                {
                    m.msg("x0008: haven't verified this.(2)");
                    xu4 = new Float32[6];
                    for(int i=0;i<6;i++)
                    {
                        xu4[i] = new Float32(c);
                    }
                }
            }
            
            public void compile(Bytedeque c)
            {
                m.msg("x0008: haven't verified this.(2)");
                c.writeInt(u1);
                c.writeInt(u2);
                for(int i=0;i<4;i++)
                {
                    u3[i].compile(c);
                }
                if(xmarker==1)
                {
                    m.msg("x0008: haven't verified this.(2)");
                    for(int i=0;i<6;i++)
                    {
                        xu4[i].compile(c);
                    }
                }
            }
        }*/
        
    /*}*/
    public static class plParticleEmitter extends uruobj
    {
        PrpTaggedObject subobject;
        //byte[] u1;
        int u2;
        int u3;
        int u4;
        Flt u5;
        Flt u6;
        Flt u7;
        Flt u8;
        
        public plParticleEmitter(context c) throws readexception
        {
            subobject = new PrpTaggedObject(c);
            //u1 = c.readBytes(4);
            u2 = c.readInt();
            u3 = c.readInt();
            u4 = c.readInt();
            u5 = new Flt(c);
            u6 = new Flt(c);
            u7 = new Flt(c);
            u8 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            subobject.compile(c);
            c.writeInt(u2);
            c.writeInt(u3);
            c.writeInt(u4);
            u5.compile(c);
            u6.compile(c);
            u7.compile(c);
            u8.compile(c);
        }
    }
    public static class plOneTimeParticleGenerator extends uruobj
    {
        int count;
        Flt u2;
        Flt u3;
        Flt u4;
        Flt u5;
        Flt u6;
        Vertex[] u7;
        
        public plOneTimeParticleGenerator(context c) throws readexception
        {
            count = c.readInt();
            u2 = new Flt(c);
            u3 = new Flt(c);
            u4 = new Flt(c);
            u5 = new Flt(c);
            u6 = new Flt(c);
            u7 = c.readArray(Vertex.class, count*2);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(count);
            u2.compile(c);
            u3.compile(c);
            u4.compile(c);
            u5.compile(c);
            u6.compile(c);
            c.writeArray(u7);
        }
    }
    /*public static class plParticleEmitter extends uruobj
    {
        Flt[] u1 = new Flt[4];
        int count;
        Flt[][] u2 = new Flt[5][];
        Flt[] u3 = new Flt[4];

        public plParticleEmitter(context c)
        {
            for(int i=0;i<4;i++)
            {
                u1[i] = new Flt(c);
            }
            count = c.in.readInt();
            for(int i=0;i<5;i++)
            {
                u2[i] = new Flt[count];
                for(int j=0;j<count;j++)
                {
                    u2[i][j] = new Flt(c);
                }
            }
            for(int i=0;i<4;i++)
            {
                u3[i] = new Flt(c);
            }
        }
        
        public void compile(Bytedeque c)
        {
            for(int i=0;i<4;i++)
            {
                u1[i].compile(c);
            }
            c.writeInt(count);
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<count;j++)
                {
                    u2[i][j].compile(c);
                }
            }
            for(int i=0;i<4;i++)
            {
                u3[i].compile(c);
            }
        }
    }*/
    
    public static class plSimpleParticleGenerator extends uruobj
    {
        Flt[] u1;
        int count;
        Flt[] u2;
        Flt[] u3;
        int xhexisle;
        
        public plSimpleParticleGenerator(context c) throws readexception
        {
            u1 = c.readArray(Flt.class, 4);
            count = c.readInt();
            u2 = c.readArray(Flt.class, count*5); //5 actually consists of a vertex and 2 floats.
            u3 = c.readArray(Flt.class, 9);
            if(c.readversion==7)
            {
                xhexisle = c.readInt(); //I don't know what this is for...
            }
        }
        
        public void compile(Bytedeque c)
        {
            c.writeArray(u1);
            c.writeInt(count);
            c.writeArray(u2);
            c.writeArray(u3);
        }
    }
    /*public static class plSimpleParticleGenerator extends uruobj
    {
        int count;
        Flt[][] u1 = new Flt[6][];
        
        public plSimpleParticleGenerator(context c)
        {
            count = c.in.readInt();
            for(int i=0;i<6;i++)
            {
                u1[i] = new Flt[count];
                for(int j=0;j<count;j++)
                {
                    u1[i][j] = new Flt(c);
                }
            }
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(count);
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<count;j++)
                {
                    u1[i][j].compile(c);
                }
            }
        }
    }*/
}

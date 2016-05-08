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

import shared.Flt;
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
public class plAGAnim extends uruobj
{
    //Objheader xheader;
    plSynchedObject parent;
    public Urustring name;
    public Flt starttime; //float
    public Flt stoptime; //float
    float xuk1;
    float xuk2;
    public int effectcount;
    //effect[] effects;
    public PrpTaggedObject[] effects;
    
    public plAGAnim(context c) throws readexception
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);//,false);
        name = new Urustring(c);
        starttime = new Flt(c);
        stoptime = new Flt(c);
        if(c.realreadversion==8)
        {
            xuk1 = c.readFloat();
            xuk2 = c.readFloat();
        }
        effectcount = data.readInt();
        //effects = new effect[effectcount];
        effects = new PrpTaggedObject[effectcount*2];
        for(int i=0;i<2*effectcount;i++)
        {
            //effects[i] = new effect(c);
            effects[i] = new PrpTaggedObject(c);
            
            //convert RelativeMatrixChannelApplicator to MatrixChannelApplicator
            if(effects[i].type==Typeid.plRelativeMatrixChannelApplicator)
            {
                if(auto.options.convertRelativeMatrixChannelApplicator)
                {
                    //m.msg("PlAGAnim: converting RelativeMatrixChannelApplicator to MatrixChannelApplicator");
                    effects[i].type = Typeid.plMatrixChannelApplicator;
                }
                else
                {
                    throw new shared.readwarningexception("RelativeMatrixChannelApplicator: not converting.");
                }
            }
        }
        if(c.readversion==4||c.readversion==7) //sep9revert
        {
            byte flag = c.readByte();
            if((flag&0x01)!=0 && name.toString().equals("(Entire Animation)"))
            {
                //do something?
                m.warn("plaganim: possibly unhandled case.");
            }
        }
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        name.compile(c);
        starttime.compile(c);
        stoptime.compile(c);
        c.writeInt(effectcount);
        for(int i=0;i<2*effectcount;i++)
        {
            effects[i].compile(c);
        }
    }
    
    public static class plMatrixChannelApplicator extends uruobj
    {
        byte u1;
        Urustring u2;
        
        public plMatrixChannelApplicator(context c)
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
        }
    }
    public static class plRelativeMatrixChannelApplicator extends uruobj //myst5 only
    {
        byte u1;
        Urustring u2;
        
        public plRelativeMatrixChannelApplicator(context c) throws readexception
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
            //throw new readexception("PlRelativeMatrixChannelApplicator: Can read okay, but throwing error in order to ignore.");
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
        }
    }
    public static class plMatrixControllerChannel extends uruobj
    {
        public Urustring channel;
        public PrpTaggedObject controller;
        public PrpController.affinePart affineParts;
        
        public plMatrixControllerChannel(context c) throws readexception
        {
            channel = new Urustring(c);
            controller = new PrpTaggedObject(c);
            affineParts = new PrpController.affinePart(c);
        }
        
        public void compile(Bytedeque c)
        {
            channel.compile(c);
            controller.compile(c);
            affineParts.compile(c);
        }
    }
    public static class plParticlePPSApplicator extends uruobj
    {
        byte u1;
        Urustring u2;

        public plParticlePPSApplicator(context c)
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
        }
    }
    public static class plScalarControllerChannel extends uruobj
    {
        Urustring u1;
        PrpTaggedObject u2;
        
        public plScalarControllerChannel(context c) throws readexception
        {
            u1 = new Urustring(c);
            u2 = new PrpTaggedObject(c);
        }
        
        public void compile(Bytedeque c)
        {
            u1.compile(c);
            u2.compile(c);
        }
    }
    /*public class effect
    {
        short subtype1;
        //applicator
        byte bool;
        Urustring item;
        int xu1;
        
        short subtype2;
        //controllerchannel
        Urustring u2;
        short subtype3;
        //controller
        plTMController xplTMController;
        plSimplePosController xplSimplePosController;
        plScalarController xplScalarController;
        plCompoundPosController xplCompoundPosController;
        int xu3;
        int[] xu4;
        int[][] xu5;
        int[] xu6;
        int xu7;
        
        public effect(context c)
        {
            Bytestream data = c.in;
    
            subtype1 = data.readShort(); //e.ensure(subtype1,);
            bool = data.readByte();
            item = new Urustring(c);
            if(subtype1==0x039F)
            {
                xu1 = data.readInt();
            }
            
            subtype2 = data.readShort(); //e.ensure(subtype2,);
            u2 = new Urustring(c);
            subtype3 = data.readShort(); //e.ensure(subtype3,);
            switch(subtype3)
            {
                case 0x023B:
                    xplTMController = new plTMController(data);
                    break;
                case 0x0239:
                    xplSimplePosController = new plSimplePosController(data);
                    break;
                case 0x022F:
                    xplScalarController = new plScalarController(data);
                    break;
                case 0x023A:
                    xplCompoundPosController = new plCompoundPosController(data);
                    break;
                default:
                    m.msg("x006BAganim: unknown controller type");
                    break;
            }
            if(subtype2==0x2D9)
            {
                xu3 = data.readInt();
                xu4 = data.readInts(3);
                xu5 = new int[2][];
                xu5[0] = data.readInts(4);
                xu5[1] = data.readInts(4);
                xu6 = data.readInts(3);
                xu7 = data.readInt();
            }
            
        }*/
        /*public class plTMController
        {
            int postype;
            plSimplePosController xplSimplePosController;
            plCompoundPosController xplCompoundPosController;
            int rottype;
            plSimpleRotController xplSimpleRotController;
            plCompoundRotController xplCompoundRotController; //plCompoundRotController is the same as plCompoundPosController.
            plSimpleScalarController xplSimpleScalarController;
            int u1;
            
            public plTMController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.");
                postype = data.readInt(); //e.ensure(postype,);
                switch(postype)
                {
                    case 1:
                        xplSimplePosController = new plSimplePosController(data);
                        break;
                    case 2:
                        xplCompoundPosController = new plCompoundPosController(data);
                        break;
                    default:
                        m.msg("x006BAganim: unknown controller");
                        break;
                }
                rottype = data.readInt(); //e.ensure(rottype,);
                switch(rottype)
                {
                    case 1:
                        xplSimpleRotController = new plSimpleRotController(data);
                        break;
                    case 3:
                        xplCompoundRotController = new plCompoundRotController(data);
                        break;
                    default:
                        m.msg("x006BAganim: unknown rotcontroller"); //possibly the plSimpleScalarController?
                        break;
                }
                u1 = data.readInt(); //should this be here?
                if(u1!=0)
                {
                    xplSimpleScalarController = new plSimpleScalarController(data);
                }
            }
        }
        public class plSimplePosController
        {
            int u1;
            BasicControllerHeader u2;
            int u3;
            int keycount;
            key[] keys;
            
            public plSimplePosController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this(2).");
                u1 = data.readInt();
                if(u1==1)
                {
                    m.msg("x006BAganim: haven't verified this(3).");
                    u2 = new BasicControllerHeader(data);
                    u3 = data.readInt();
                    if(u3==1)
                    {
                        m.msg("x006BAganim: haven't verified this(4).");
                        keycount = data.readInt();
                        keys = new key[keycount];
                        for(int i=0;i<keycount;i++)
                        {
                            keys[i] = new key(data);
                        }
                    }
                }
            }
            
            public class key
            {
                FrameMarker u1;
                int[][] xtangents;
                int[] position;
                
                public key(Bytestream data)
                {
                    m.msg("x006BAganim: haven't verified this.(5)");
                    u1 = new FrameMarker(data);
                    if(u1.keytype==2)
                    {
                        xtangents = new int[2][];
                        xtangents[0] = data.readInts(3);
                        xtangents[1] = data.readInts(3);
                    }
                    position = data.readInts(3);
                }
            }
                    
        }
        public class plScalarController
        {
            BasicControllerHeader u1;
            int u2;
            int xkeycount;
            key2[] xkeys;
            
            public plScalarController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(6)");
                u1 = new BasicControllerHeader(data);
                u2 = data.readInt();
                if(u2==1)
                {
                    m.msg("x006BAganim: haven't verified this.(7)");
                    xkeycount = data.readInt();
                    xkeys = new key2[xkeycount];
                    for(int i=0;i<xkeycount;i++)
                    {
                        xkeys[i] = new key2(data);
                    }
                }
            }
            
            public class key2
            {
                FrameMarker u1;
                int[] xtangents;
                int scalevalue;
                
                public key2(Bytestream data)
                {
                    m.msg("x006BAganim: haven't verified this.(8)");
                    u1 = new FrameMarker(data);
                    if(u1.keytype==2)
                    {
                        xtangents = data.readInts(2);
                    }
                    scalevalue = data.readInt();
                }
            }
        }
        public class plCompoundPosController //same as plComoundRotController.
        {
            int u1;
            plScalarController xplScalarController1;
            int u2;
            plScalarController xplScalarController2;
            int u3;
            plScalarController xplScalarController3;
            
            public plCompoundPosController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(9)");
                u1 = data.readInt();
                if(u1!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(10)");
                    xplScalarController1 = new plScalarController(data);
                }
                u2 = data.readInt();
                if(u2!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(11)");
                    xplScalarController2 = new plScalarController(data);
                }
                u3 = data.readInt();
                if(u3!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(12)");
                    xplScalarController3 = new plScalarController(data);
                }
            }
        }
        public class plCompoundRotController //same as plCompoundPosController.
        {
            int u1;
            plScalarController xplScalarController1;
            int u2;
            plScalarController xplScalarController2;
            int u3;
            plScalarController xplScalarController3;
            
            public plCompoundRotController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(13)");
                u1 = data.readInt();
                if(u1!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(14)");
                    xplScalarController1 = new plScalarController(data);
                }
                u2 = data.readInt();
                if(u2!=0)
                {
                m.msg("x006BAganim: haven't verified this.(15)");
                    xplScalarController2 = new plScalarController(data);
                }
                u3 = data.readInt();
                if(u3!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(16)");
                    xplScalarController3 = new plScalarController(data);
                }
            }
        }
        public class FrameMarker
        {
            int keytype; //1=linear, 2=bezier?
            int frame;
            int frametime; //float
            
            public FrameMarker(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(17)");
                keytype = data.readInt();
                frame = data.readInt();
                frametime = data.readInt();
            }
        }
        public class BasicControllerHeader
        {
            int u1;
            int easecontrollers;
            //plEaseController[] controllers;
            int u2;
            
            public BasicControllerHeader(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(18)");
                u1 = data.readInt();
                easecontrollers = data.readInt(); e.ensure(easecontrollers,0); //it could be more, but I haven't implemented it.
                //controllers = new plEaseController[easecontrollers];
                u2 = data.readInt();
            }
        }
        public class plSimpleRotController
        {
            int u1;
            BasicControllerHeader u2;
            int keycount;
            key3[] keys;
            
            public plSimpleRotController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(19)");
                u1 = data.readInt();
                u2 = new BasicControllerHeader(data);
                keycount = data.readInt();
                keys = new key3[keycount];
                for(int i=0;i<keycount;i++)
                {
                    keys[i] = new key3(data);
                }
            }
            
            public class key3
            {
                FrameMarker u1;
                int[] orientation;
                
                public key3(Bytestream data)
                {
                    m.msg("x006BAganim: haven't verified this.(20)");
                    u1 = new FrameMarker(data);
                    orientation = data.readInts(4);
                }
            }
                    
        }
        
        public class plSimpleScalarController
        {
            int u1;
            BasicControllerHeader xu2;
            int xkeycount;
            key4[] keys;
            
            public plSimpleScalarController(Bytestream data)
            {
                m.msg("x006BAganim: haven't verified this.(21)");
                u1 = data.readInt();
                if(u1!=0)
                {
                    m.msg("x006BAganim: haven't verified this.(22)");
                    xu2 = new BasicControllerHeader(data);
                    xkeycount = data.readInt();
                    keys = new key4[xkeycount];
                    for(int i=0;i<xkeycount;i++)
                    {
                        keys[i] = new key4(data);
                    }
                }
            }
            
            public class key4
            {
                FrameMarker u1;
                int[][] xtangents;
                int[] scalevalues;
                int[] orientation;
                
                public key4(Bytestream data)
                {
                    m.msg("x006BAganim: haven't verified this.(23)");
                    u1 = new FrameMarker(data);
                    if(u1.keytype==2)
                    {
                        m.msg("x006BAganim: haven't verified this.(24)");
                        xtangents = new int[2][];
                        xtangents[0] = data.readInts(3);
                        xtangents[1] = data.readInts(3);
                    }
                    scalevalues = data.readInts(3);
                    orientation = data.readInts(4);
                }
            }
        }*/
    //}
}
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

//incomplete; doesn't handle messages. This is your warning!
public class x009BCameraModifier1 extends uruobj
{
    //Objheader xheader;
    //x001FSingleModifier parent;
    public Uruobjectref brain;
    public int count;
    public CamTrans[] trans;
    public Flt FOVw;
    public Flt FOVh;
    public int count2;
    public PrpTaggedObject[] messageQueue;
    public Uruobjectref[] messageQueueSenders;
    public int count3;
    public PrpTaggedObject[] FOVInstructions;
    public byte animated;
    public byte startAnimOnPush;
    public byte stopAnimOnPop;
    public byte resetAnimOnPop;
    
    public x009BCameraModifier1(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        //parent = new x001FSingleModifier(c);//,false);
        brain = new Uruobjectref(c);
        count = c.readInt();
        trans = new CamTrans[count];
        for(int i=0;i<count;i++)
        {
            trans[i] = new CamTrans(c);
        }
        FOVw = new Flt(c);
        FOVh = new Flt(c);
        count2 = c.readInt();
        /*if(count2!=0)
        {
            m.err("plCameraModifier1: I can't actually handle any messages, yet.");
        }*/
        messageQueue = c.readArray(PrpTaggedObject.class, count2);
        messageQueueSenders = c.readArray(Uruobjectref.class, count2);
        
        count3 = c.readInt();
        /*if(count3!=0)
        {
            m.err("plCameraModifier1: I can't actually handle any messages, yet.");
        }*/
        FOVInstructions = c.readArray(PrpTaggedObject.class, count3);
        animated = c.readByte();
        startAnimOnPush = c.readByte();
        stopAnimOnPop = c.readByte();
        resetAnimOnPop = c.readByte();
    }

    private x009BCameraModifier1() {
        
    }
    public void compile(Bytedeque c)
    {
        brain.compile(c);
        c.writeInt(count);
        for(int i=0;i<count;i++)
        {
            trans[i].compile(c);
        }

        FOVw.compile(c);
        FOVh.compile(c);
        c.writeInt(count2);
        /*if(count2!=0)
        {
            m.err("plCameraModifier1: I can't actually handle any messages, yet.");
        }*/
        c.writeArray2(messageQueue);
        c.writeArray2(messageQueueSenders);
        
        c.writeInt(count3);
        /*if(count3!=0)
        {
            m.err("plCameraModifier1: I can't actually handle any messages, yet.");
        }*/
        c.writeArray2(FOVInstructions);
        
        c.writeByte(animated);
        c.writeByte(startAnimOnPush);
        c.writeByte(stopAnimOnPop);
        c.writeByte(resetAnimOnPop);
        
    }

    public static x009BCameraModifier1 createDefault() {
        x009BCameraModifier1 e = new x009BCameraModifier1();
        e.FOVw = new Flt(5f);
        e.FOVh = new Flt(3.75f);
        e.FOVInstructions = new PrpTaggedObject[0];
        e.animated = 0;
        e.brain = Uruobjectref.none();
        e.count = 0;
        e.count2 = 0;
        e.count3 = 0;
        e.messageQueue = new PrpTaggedObject[0];
        e.messageQueueSenders = new Uruobjectref[0];
        e.resetAnimOnPop = 0;
        e.startAnimOnPush = 0;
        e.stopAnimOnPop = 0;
        e.trans = new CamTrans[0];
        return e;
    }
    
    public static class CamTrans extends uruobj
    {
        public Uruobjectref transTo;
        byte cutPos;
        byte cutPOA;
        byte ignore;
        Flt Velocity;
        Flt Accel;
        Flt Decel;
        Flt POAVelocity;
        Flt POAAccel;
        Flt POADecel;

        public CamTrans(context c) throws readexception
        {
            transTo = new Uruobjectref(c);
            cutPos = c.readByte();
            cutPOA = c.readByte();
            ignore = c.readByte();
            Velocity = new Flt(c);
            Accel = new Flt(c);
            Decel = new Flt(c);
            POAVelocity = new Flt(c);
            POAAccel = new Flt(c);
            POADecel = new Flt(c);
        }

        private CamTrans() {}
        
        public void compile(Bytedeque c)
        {
            transTo.compile(c);
            c.writeByte(cutPos);
            c.writeByte(cutPOA);
            c.writeByte(ignore);
            Velocity.compile(c);
            Accel.compile(c);
            Decel.compile(c);
            POAVelocity.compile(c);
            POAAccel.compile(c);
            POADecel.compile(c);
            
        }

        public static CamTrans createDefault() {
            CamTrans e = new CamTrans();
            
            e.Accel = new Flt(60f);
            e.Decel = new Flt(60f);
            e.POAAccel = new Flt(60f);
            e.POADecel = new Flt(60f);
            e.POAVelocity = new Flt(60f);
            e.Velocity = new Flt(60f);
            e.cutPOA = 1;
            e.cutPos = 1;
            e.ignore = 0;
            e.transTo = Uruobjectref.none();
            
            return e;
        }
    }
}

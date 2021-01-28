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
    Uruobjectref brain;
    int count;
    CamTrans[] trans;
    Flt FOVw;
    Flt FOVh;
    int count2;
    PrpTaggedObject[] messageQueue;
    Uruobjectref[] messageQueueSenders;
    int count3;
    PrpTaggedObject[] FOVInstructions;
    byte animated;
    byte startAnimOnPush;
    byte stopAnimOnPop;
    byte resetAnimOnPop;
    
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
    
    public static class CamTrans extends uruobj
    {
        Uruobjectref transTo;
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
    }
}

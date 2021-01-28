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

import shared.readexception;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import java.util.Vector;

//untested and needs PrpMessages to be at least partially complete.
public class plResponderModifier extends uruobj
{
    //Objheader xheader;
    
    public plSingleModifier parent;
    public byte count;
    public Vector<PlResponderState> messages;
    public byte state;
    public byte enabled;
    public byte flags;
    
    public plResponderModifier(context c) throws readexception
    {
        ////if(hasHeader) xheader = new Objheader(c);
        
        parent = new plSingleModifier(c);//,false);
        count = c.readByte();
        messages = c.readVector(PlResponderState.class, b.ByteToInt32(count));
        state = c.readByte();
        enabled = c.readByte();
        flags = c.readByte();
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        //c.writeByte(count);
        c.writeByte((byte)messages.size());
        c.writeVector2(messages);
        c.writeByte(state);
        c.writeByte(enabled);
        c.writeByte(flags);
    }

    public Vector<uruobj> findMessagesOfType(Typeid type)
    {
        Vector<uruobj> r = new Vector<uruobj>();
        for(PlResponderState state: messages)
        {
            for(PlResponderCmd cmd: state.commands)
            {
                if(cmd.message.type==type)
                {
                    r.add(cmd.message.prpobject.object);
                }
            }
        }
        return r;
    }
    
    public static class PlResponderState extends uruobj
    {
        public byte numCallbacks;
        public byte switchToState;
        public byte count;
        public Vector<PlResponderCmd> commands;
        public byte count2;
        //public byte[][] u1;
        public Vector<WaitToCmd> waitToCmdTable;
        
        public PlResponderState(context c) throws readexception
        {
            numCallbacks = c.readByte();
            switchToState = c.readByte();
            count = c.readByte();
            /*commands = new PlResponderCmd[count];
            for(int i=0;i<count;i++)
            {
                commands[i] = new PlResponderCmd(c);
            }*/
            commands = c.readVector(PlResponderCmd.class, count);
            //if(true) return;
            count2 = c.readByte();
            /*u1 = new byte[count2][];
            for(int i=0;i<count2;i++)
            {
                u1[i] = new byte[2];
                u1[i][0] = c.readByte();
                u1[i][1] = c.readByte();
            }*/
            waitToCmdTable = c.readVector(WaitToCmd.class, b.ByteToInt32(count2));
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(numCallbacks);
            c.writeByte(switchToState);
            /*c.writeByte(count);
            for(int i=0;i<count;i++)
            {
                commands[i].compile(c);
            }*/
            c.writeByte((byte)commands.size());
            c.writeVector2(commands);
            /*c.writeByte(count2);
            for(int i=0;i<count2;i++)
            {
                c.writeByte(u1[i][0]);
                c.writeByte(u1[i][1]);
            }*/
            c.writeByte((byte)waitToCmdTable.size());
            c.writeVector2(waitToCmdTable);
            
        }

        public static class WaitToCmd extends uruobj
        {
            byte wait;
            byte cmd;

            public WaitToCmd(context c)
            {
                wait = c.readByte();
                cmd = c.readByte();
            }
            private WaitToCmd(){}

            public void compile(Bytedeque c)
            {
                c.writeByte(wait);
                c.writeByte(cmd);
            }

            public static WaitToCmd createWithWaitCmd(int wait, int cmd)
            {
                WaitToCmd r = new WaitToCmd();
                r.wait = (byte)wait;
                r.cmd = (byte)cmd;
                return r;
            }
        }
    }
    
    public static class PlResponderCmd extends uruobj
    {
        public PrpTaggedObject message;
        public byte waitOn;
        
        public PlResponderCmd(context c) throws readexception
        {
            message = new PrpTaggedObject(c);
            waitOn = c.readByte();
        }
        
        public void compile(Bytedeque c)
        {
            message.compile(c);
            c.writeByte(waitOn);
        }
    }
}

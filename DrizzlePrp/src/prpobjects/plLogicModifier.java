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


public class plLogicModifier extends uruobj
{
    public PlLogicModBase parent;
    public int conditionalcount;
    public Uruobjectref[] conditionals;
    int u1;
    
    Uruobjectref xref;
    
    public plLogicModifier(context c) throws readexception
    {
        parent = new PlLogicModBase(c);
        conditionalcount = c.readInt();
        conditionals = c.readArray(Uruobjectref.class, conditionalcount);
        u1 = c.readInt();
        if(c.readversion==4||c.readversion==7)
        {
            xref = new Uruobjectref(c); //e.g. KeepClickLinkLaki(plPickingDetector)
        }
    }
    private plLogicModifier(){}
    /*public static PlLogicModifier createWithRef(Uruobjectref ref)
    {
        PlLogicModifier result = new PlLogicModifier();
        result.parent = PlLogicModBase.createWithRef(ref);
        
    }*/
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(conditionalcount);
        c.writeArray2(conditionals);
        c.writeInt(u1);
    }
    
    public static class PlLogicModBase extends uruobj
    {
        plSingleModifier parent;
        int count; //we don't handle it if this is not 0. This could be changed in the future, though.
        //short[] unused;
        //public PrpMessage message;
        public PrpTaggedObject message;
        HsBitVector u1;
        public byte disabled;
        
        public PlLogicModBase(context c) throws readexception
        {
            parent = new plSingleModifier(c);
            count = c.readInt(); e.ensure(count==0); //we don't handle the other situation, which is an array of PrpMessages
            //unused = c.readInts(count);
            //message = new PrpMessage(c);
            message = new PrpTaggedObject(c);
            u1 = new HsBitVector(c);
            disabled = c.readByte();

        }
        private PlLogicModBase(){}
        public static PlLogicModBase createWithRef(Uruobjectref ref)
        {
            PlLogicModBase result = new PlLogicModBase();
            result.parent = plSingleModifier.createDefault();
            PrpMessage.PlNotifyMsg msg = PrpMessage.PlNotifyMsg.createWithRef(ref);
            result.message = PrpTaggedObject.createWithTypeidUruobj(Typeid.plNotifyMsg, msg);
            result.u1 = new HsBitVector(1); //just copying from known object
            result.disabled = 0; //enable it.
            return result;
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(count);
            //unhandled part if count!=0
            message.compile(c);
            u1.compile(c);
            c.writeByte(disabled);
        }
    }
    
}

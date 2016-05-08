/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import shared.*;
import java.util.ArrayList;
import uru.context;
import uru.Bytedeque;

//was PrpMessage.PlMessage
public class plMessage extends uruobj
{

    public static final int kBCastNone = 0x0;
    public static final int kBCastByType = 0x1;
    public static final int kBCastUNUSED_0 = 0x2;
    public static final int kPropagateToChildren = 0x4;
    public static final int kBCastByExactType = 0x8;
    public static final int kPropagateToModifiers = 0x10;
    public static final int kClearAfterBCast = 0x20;
    public static final int kNetPropagate = 0x40;
    public static final int kNetSent = 0x80;
    public static final int kNetUseRelevanceRegions = 0x100;
    public static final int kNetForce = 0x200;
    public static final int kNetNonLocal = 0x400;
    public static final int kLocalPropagate = 0x800;
    //public static final int kNetNonDeterministic =  0x200;
    public static final int kMsgWatch = 0x1000;
    public static final int kNetStartCascade = 0x2000;
    public static final int kNetAllowInterAge = 0x4000;
    public static final int kNetSendUnreliable = 0x8000;
    public static final int kCCRSendToAllPlayers = 0x10000;
    public static final int kNetCreatedRemotely = 0x20000;

    public Uruobjectref sender; //was parentobj
    private int refcount;
    public ArrayList<Uruobjectref> receivers; //was refs
    //int u1;
    //int u2;
    public Timestamp timestamp;
    public int broadcastFlags; //was flags

    //int xu1;

    public plMessage(context c) throws readexception
    {
        sender = new Uruobjectref(c);
        refcount = c.readInt();
        receivers = c.readArrayList(Uruobjectref.class, refcount);
        if(c.readversion==3||c.readversion==6)
        {
            //u1 = c.readInt(); //timestamp p1
            //u2 = c.readInt(); //timestamp p2
            timestamp = new Timestamp(c.in);
            broadcastFlags = c.readInt();
        }
        else if(c.readversion==4||c.readversion==7)
        {
            //if(refcount!=0)
            //{
                //timestamp = Timestamp.createDefault();
                //changed xu1 to flags.
                broadcastFlags = c.readInt();
                if(broadcastFlags!=0)
                {
                    //m.err("not an error, but remember to check this out.");
                    int dummy=0;
                }
            //}
            //the other flags will all default to 0.
        }
    }
    private plMessage(){}
    public static plMessage createWithRef(Uruobjectref ref)
    {
        plMessage result = new plMessage();
        result.sender = Uruobjectref.none();
        result.refcount = 1;
        //result.receivers = new Uruobjectref[]{ ref };
        result.receivers = new ArrayList();
        result.receivers.add(ref);
        return result;
    }
    public static plMessage createDefault()
    {
        plMessage r = new plMessage();
        r.sender = Uruobjectref.none();
        r.receivers = new ArrayList();
        r.timestamp = Timestamp.createDefault();
        return r;
    }
    public static plMessage createWithSenderAndReceiver(Uruobjectref sender, Uruobjectref receiver)
    {
        plMessage r = new plMessage();
        r.sender = sender;
        r.refcount = 1;
        //r.receivers = new Uruobjectref[]{ receiver };
        r.receivers = new ArrayList();
        r.receivers.add(receiver);
        return r;
    }
    public void compile(Bytedeque c)
    {
        sender.compile(c);
        //c.writeInt(refcount);
        c.writeInt(receivers.size());
        //c.writeArray2(receivers);
        c.writeArrayList2(receivers);
        //c.writeInt(u1);
        //c.writeInt(u2);
        if(c.format==Format.pots||c.format==Format.moul)
        {
            if(timestamp==null) timestamp = Timestamp.createDefault();
            timestamp.compile(c);
        }
        c.writeInt(broadcastFlags);
    }





    public static class plNetMsgObject
    {
        public plNetMessage parent;
        public Uruobjectdesc desc;
        boolean extended = true;

        public plNetMsgObject(context c) throws readexception
        {
            parent = new plNetMessage(c);
            if(c.areBytesKnownToBeAvailable())
            {
                desc = new Uruobjectdesc(c);
            }
            else extended = false;
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            if(extended)
            {
                desc.compile(c);
            }
        }

        private plNetMsgObject(){}
        public static plNetMsgObject createWithDesc(Uruobjectdesc desc)
        {
            plNetMsgObject r = new plNetMsgObject();
            r.parent = plNetMessage.createDefault();
            r.desc = desc;
            return r;
        }
    }

    public static class plNetMsgStreamedObject //not an uruobj, because of custom constructor.
    {
        public plNetMsgObject parent;
        public uruobj obj;
        boolean extended = true;

        public plNetMsgStreamedObject(context c, Class klassToRead) throws readexception
        {
            //not done //okay, now it is!
            //bwaa

            parent = new plNetMsgObject(c);
            if(c.areBytesKnownToBeAvailable())
            {
                plNetMsgLoadClone.plNetMsgStreamHelper helper = new plNetMsgLoadClone.plNetMsgStreamHelper(c,klassToRead,true/*,0*/);
                obj = helper.obj;
            }
            else extended = false;
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            if(extended)
            {
                //this isn't bytewise exactly the same as the original, but we shouldn't care, because this is intended for net use anyway.
                plNetMsgLoadClone.plNetMsgStreamHelper helper = plNetMsgLoadClone.plNetMsgStreamHelper.createFromObj(c,obj);
                helper.compile(c);
            }
        }

        private plNetMsgStreamedObject(){}
        public static plNetMsgStreamedObject createWithDescObj(Uruobjectdesc desc, uruobj obj)
        {
            plNetMsgStreamedObject r = new plNetMsgStreamedObject();
            r.parent = plNetMsgObject.createWithDesc(desc);
            r.obj = obj;
            return r;
        }
    }

    public static class plNetMsgSharedState //not uruobj because of custom constructor
    {
        public plNetMsgStreamedObject parent;
        public byte lockRequest;
        boolean extended = true;

        public plNetMsgSharedState(context c, Class klassToRead) throws readexception
        {
            parent = new plNetMsgStreamedObject(c,klassToRead);
            if(c.areBytesKnownToBeAvailable())
            {
                lockRequest = c.readByte();
            }
            else extended = false;
        }
    }

    public static class plNetSharedState extends uruobj
    {
        public Wpstr name;
        public int count;
        public byte serverMayDelete;
        public plGenericVar[] vars;

        public plNetSharedState(context c) throws readexception
        {
            name = new Wpstr(c);
            count = c.readInt();
            serverMayDelete = c.readByte();
            vars = c.readArray(plGenericVar.class, count);
        }
    }

    public static class plGenericVar extends uruobj
    {
        public Urustring name;
        public plGenericType value;

        public plGenericVar(context c) throws readexception
        {
            name = new Urustring(c);
            value = new plGenericType(c);
        }
    }

    public static class plGenericType
    {
        public static final byte kNone = (byte)0xFF;
        public static final byte kInt = 0;
        public static final byte kFloat = 1;
        public static final byte kBool = 2;
        public static final byte kString = 3;
        public static final byte kByte = 4;
        public static final byte kAny = 5;
        public static final byte kUint = 6;
        public static final byte kDouble = 7;

        public byte type;
        public int xint;
        public float xfloat;
        public byte xbyte;
        public Urustring xstr;
        public Double64 xdouble;

        public plGenericType(context c) throws readexception
        {
            type = c.readByte();
            switch(type)
            {
                case kInt:
                case kUint:
                    xint = c.readInt();
                    break;
                case kFloat:
                    xfloat = c.readFloat();
                    break;
                case kBool:
                case kByte:
                    xbyte = c.readByte();
                    break;
                case kString:
                case kAny:
                    xstr = new Urustring(c);
                    break;
                case kDouble:
                    xdouble = new Double64(c);
                    break;
                default:
                    throw new shared.uncaughtexception("unexpected");
            }
        }
    }







}

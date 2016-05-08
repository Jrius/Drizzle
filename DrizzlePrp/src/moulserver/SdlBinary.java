/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.*;
import prpobjects.*;
import uru.context;
import parsers.sdlparser.*;
import java.util.ArrayList;
import uru.Bytedeque;

public class SdlBinary extends uruobj//basically plStateDataRecord with a header to describe the name/version.
{
    public static final int kVolatile = 1;

    public static final int kHasUoid = 0x1;
    public static final int kHasNotificationInfo = 0x2;
    public static final int kHasTimeStamp = 0x4;
    public static final int kSameAsDefault = 0x8;
    public static final int kHasDirtyFlag = 0x10;
    public static final int kWantTimeStamp = 0x20;

    //plVarDescriptor type
    public static final byte kNone = (byte)0xFF;
    public static final byte kInt = (byte)0;
    public static final byte kFloat = (byte)1;
    public static final byte kBool = (byte)2;
    public static final byte kString = (byte)3;
    public static final byte kKey = (byte)4;
    public static final byte kStateDescriptor = (byte)5;
    public static final byte kCreatable = (byte)6;
    public static final byte kDouble = (byte)7;
    public static final byte kTime = (byte)8;
    public static final byte kByte = (byte)9;
    public static final byte kShort = (byte)10;
    public static final byte kAgeTimeOfDay = (byte)11;
    public static final byte kVector3 = (byte)50;
    public static final byte kPoint3 = (byte)51;
    public static final byte kRGB = (byte)52;
    public static final byte kRGBA = (byte)53;
    public static final byte kQuaternion = (byte)54;
    public static final byte kRGB8 = (byte)55;
    public static final byte kRGBA8 = (byte)56;

    short flag;
    Urustring name;
    short ver;
    plStateDataRecord statedata;
    //Statedesc origsd;

    //AbstractManager manager;

    //public SdlBinary(context c, AbstractManager manager)
    public SdlBinary(context c)
    {
        //this.manager = manager;
        //this.manager = (AbstractManager)c.extra; //hack!!
        //this.manager = Manager.manager;

        //header
        flag = c.readShort();
        if((flag&0x8000)==0)
        {
            m.throwUncaughtException("unhandled");
        }
        name = new Urustring(c);
        ver = c.readShort();
        int version = b.Int16ToInt32(ver);
        if((flag&SdlBinary.kVolatile)!=0)
        {
            m.throwUncaughtException("unhandled");
        }

        Statedesc sd = SuperManager.getAgesInfo().getStatedesc(name.toString(),version);
        //origsd = sd;
        statedata = new plStateDataRecord(c,sd);
    }
    public void compile(Bytedeque c)
    {
        c.writeShort(flag);
        if((flag&0x8000)==0)
        {
            m.throwUncaughtException("unhandled");
        }
        name.compile(c);
        c.writeShort(ver);
        int version = b.Int16ToInt32(ver);
        if((flag&SdlBinary.kVolatile)!=0)
        {
            m.throwUncaughtException("unhandled");
        }

        Statedesc sd = SuperManager.getAgesInfo().getStatedesc(name.toString(),version);
        statedata.compile(c,sd);
    }
    public void update(SdlBinary sdl2)
    {
        //merge the simplevars
        mergeSimple(this.statedata.simpleVars,sdl2.statedata.simpleVars);

        //merge the compoundvars
        mergeStruct(this.statedata.compoundVars,sdl2.statedata.compoundVars);
    }
    private void mergeSimple(plSimpleStateVariable[] current, plSimpleStateVariable[] update)
    {
        int length = current.length; //=update.length, they are both the same.
        for(int i=0;i<length;i++)
        {
            //only update the dirty ones
            if(update[i]!=null)
            {
                if(update[i]!=null && (update[i].simpleVarContents&kHasDirtyFlag)!=0)
                {
                    current[i] = update[i];
                }
            }
        }
    }
    private void mergeStruct(plSDStateVariable[] current, plSDStateVariable[] update)
    {
        int length = current.length; //=update.length, they are both the same.
        for(int i=0;i<length;i++)
        {
            //update all of them
            if(update[i]!=null)
            {
                if(current[i]==null)
                {
                    current[i] = update[i];
                }
                else
                {
                    //should we just replace it wholesale, or do it piece by piece?
                    if(current[i].countVars()!=update[i].countVars())
                    {
                        m.warn("should we do something different?");
                    }
                    
                    current[i] = update[i];
                }
            }
        }
    }

    public static class plStateDataRecord
    {
        short flags;
        byte ioversion;
        //ArrayList<plStateVariable> simpleVars = new ArrayList();
        //ArrayList<plStateVariable> compoundVars = new ArrayList();
        //plStateVariable[] simpleVars;
        plSimpleStateVariable[] simpleVars;
        //plStateVariable[] compoundVars;
        plSDStateVariable[] compoundVars;

        public plStateDataRecord(context c, Statedesc sd)
        {
            //plStateDataRecord begins here
            flags = c.readShort();
            ioversion = c.readByte();
            if(ioversion!=6) m.throwUncaughtException("unexpected");


            {
                //int totalvarcount = sd.getVarCount();
                int totalvarcount = sd.getSimpleVarCount();
                int varcount = readSmallest(c.in,totalvarcount);
                boolean isAllVars = varcount==totalvarcount;
                simpleVars = new plSimpleStateVariable[totalvarcount];
                for(int i=0;i<varcount;i++)
                {
                    int index = isAllVars?i:readSmallest(c.in,totalvarcount);
                    //Varline varinfo = sd.getVar(index);
                    Varline varinfo = sd.getSimpleVar(index);
                    try{
                        if(varinfo.type.isStatedesc())
                        {
                            //simpleVars[index] = new plSDStateVariable(c,varinfo);
                            m.throwUncaughtException("unexpected");
                        }
                        else
                        {
                            simpleVars[index] = new plSimpleStateVariable(c,varinfo);
                        }
                    }catch(Exception e){
                        throw new shared.nested(e);
                    }
                }
            }

            {
                int totalvarcount = sd.getStatedescVarCount();
                int varcount = readSmallest(c.in,totalvarcount);
                compoundVars = new plSDStateVariable[totalvarcount];
                boolean isAllVars = varcount==totalvarcount;
                for(int i=0;i<varcount;i++)
                {
                    int index = isAllVars?i:readSmallest(c.in,totalvarcount);
                    //Varline varinfo = sd.getVar(index);
                    Varline varinfo = sd.getStatedescVar(index);
                    try{
                        if(varinfo.type.isStatedesc())
                        {
                            compoundVars[index] = new plSDStateVariable(c,varinfo);
                        }
                        else
                        {
                            //compoundVars[index] = new plSimpleStateVariable(c,varinfo);
                            m.throwUncaughtException("unexpected");
                        }
                    }catch(Exception e){
                        throw new shared.nested(e);
                    }
                }
            }
        }

        public void compile(Bytedeque c, Statedesc sd)
        {
            c.writeShort(flags);
            c.writeByte(ioversion);
            if(ioversion!=6) m.throwUncaughtException("unexpected");

            {
                int totalvarcount = sd.getSimpleVarCount();
                int varcount = 0; for(int i=0;i<simpleVars.length;i++) if(simpleVars[i]!=null) varcount++;
                writeSmallest(c,totalvarcount,varcount);
                boolean isAllVars = varcount==totalvarcount;
                for(int index=0;index<totalvarcount;index++)
                {
                    if(simpleVars[index]!=null)
                    {
                        if(!isAllVars) writeSmallest(c,totalvarcount,index);
                        Varline varinfo = sd.getSimpleVar(index);
                        simpleVars[index].compile(c,varinfo);
                    }
                }
            }

            //I'm feeling too lazy to rename these vars XD
            {
                int totalvarcount = sd.getStatedescVarCount();
                int varcount = 0; for(int i=0;i<compoundVars.length;i++) if(compoundVars[i]!=null) varcount++;
                writeSmallest(c,totalvarcount,varcount);
                boolean isAllVars = varcount==totalvarcount;
                for(int index=0;index<totalvarcount;index++)
                {
                    if(compoundVars[index]!=null)
                    {
                        if(!isAllVars) writeSmallest(c,totalvarcount,index);
                        Varline varinfo = sd.getStatedescVar(index);
                        compoundVars[index].compile(c,varinfo);
                    }
                }
            }
        }
    }

    static int readSmallest(IBytestream c, int totalvarcount)
    {
        if(totalvarcount<0x100) return b.ByteToInt32(c.readByte());
        else if(totalvarcount<0x10000) return b.Int16ToInt32(c.readShort());
        else return c.readInt();
    }
    static void writeSmallest(Bytedeque c, int totalvarcount, int val)
    {
        if(totalvarcount<0x100) c.writeByte((byte)val);
        else if(totalvarcount<0x10000) c.writeShort((short)val);
        else c.writeInt(val);
    }

    public static class plSimpleStateVariable extends plStateVariable
    {
        byte simpleVarContents;
        Timestamp timestamp;
        //int count;
        Object[] vals;

        public plSimpleStateVariable(context c, Varline varinfo)
        {
            super(c);
            simpleVarContents = c.readByte();
            if((simpleVarContents&kHasTimeStamp)!=0)
            {
                timestamp = new Timestamp(c.in);
            }

            if((simpleVarContents&kSameAsDefault)==0)
            {
                //get count
                int count;
                if(varinfo.isVariableLength())
                {
                    count = c.readInt();
                    if(count>9999) m.throwUncaughtException("out of range");
                }
                else
                {
                    count = varinfo.getCount();
                }

                vals = new Object[count];
                try{
                    for(int i=0;i<count;i++)
                    {
                        switch(varinfo.type.type)
                        {
                            case Type.kInt:
                                vals[i] = c.readInt();
                                break;
                            case Type.kByte:
                                vals[i] = c.readByte();
                                break;
                            case Type.kBool:
                                vals[i] = c.readByte();
                                break;
                            case Type.kFloat:
                                vals[i] = c.readFloat();
                                break;
                            case Type.kCreatable:
                                vals[i] = new prpobjects.PrpTaggedSizedObject(c);
                                break;
                            case Type.kTime:
                                vals[i] = new Timestamp(c.in);
                                break;
                            case Type.kString:
                                vals[i] = new shared.FixedLengthString(c.in, 32);
                                break;
                            case Type.kShort:
                                vals[i] = c.readShort();
                                break;
                            case Type.kKey:
                                vals[i] = new Uruobjectdesc(c);
                                break;
                            case Type.kPoint3:
                                vals[i] = new Vertex(c.in);
                                break;
                            case Type.kRGB8:
                                vals[i] = new Rgb8(c);
                                break;
                            case Type.kAgeTimeOfDay:
                                //vals[i] = null; //don't actually store anything?
                                vals[i] = new Object(); //just a dummy
                                break;
                            case Type.kQuaternion:
                                vals[i] = new Quat(c);
                                break;
                            case Type.kVector3:
                                vals[i] = new Vertex(c.in);
                                break;
                            default:
                                throw new shared.uncaughtexception("unimplemented1");
                        }
                    }
                }catch(Exception e){
                    throw new shared.nested(e);
                }
            }
            else
            {
                toDefault(varinfo);
            }
        }

        public void compile(Bytedeque c, Varline varinfo)
        {
            super.compile(c,varinfo);
            c.writeByte(simpleVarContents);
            if((simpleVarContents&kHasTimeStamp)!=0)
            {
                timestamp.compile(c);
            }

            int count = vals.length;
            if((simpleVarContents&kSameAsDefault)==0)
            {
                //get count
                if(varinfo.isVariableLength())
                {
                    c.writeInt(count);
                    if(count>9999) m.throwUncaughtException("out of range");
                }
                else
                {
                    //do nothing
                }

                try{
                    for(int i=0;i<vals.length;i++)
                    {
                        switch(varinfo.type.type)
                        {
                            case Type.kInt:
                                c.writeInt((Integer)vals[i]);
                                break;
                            case Type.kByte:
                                c.writeByte((Byte)vals[i]);
                                break;
                            case Type.kBool:
                                c.writeByte((Byte)vals[i]);
                                break;
                            case Type.kFloat:
                                c.writeFloat((Float)vals[i]);
                                break;
                            case Type.kCreatable:
                                ((prpobjects.PrpTaggedSizedObject)vals[i]).compile(c);
                                break;
                            case Type.kTime:
                                ((Timestamp)vals[i]).compile(c);
                                break;
                            case Type.kString:
                                ((FixedLengthString)vals[i]).compile(c);
                                break;
                            case Type.kShort:
                                c.writeShort((Short)vals[i]);
                                break;
                            case Type.kKey:
                                ((Uruobjectdesc)vals[i]).compile(c);
                                break;
                            case Type.kPoint3:
                                ((Vertex)vals[i]).compile(c);
                                break;
                            case Type.kRGB8:
                                ((Rgb8)vals[i]).compile(c);
                                break;
                            case Type.kAgeTimeOfDay:
                                //vals[i] = null; //don't actually store anything?
                                //but we put vals[i]=new Object() as a dummy.
                                break;
                            case Type.kQuaternion:
                                ((Quat)vals[i]).compile(c);
                                break;
                            case Type.kVector3:
                                ((Vertex)vals[i]).compile(c);
                                break;
                            default:
                                throw new shared.uncaughtexception("unimplemented2");
                        }
                    }
                }catch(Exception e){
                    throw new shared.nested(e);
                }
            }
            else
            {
                //do nothing
            }
        }

        public void toDefault(Varline varinfo)
        {
            //m.throwUncaughtException("unimplemented");
            Value defval = varinfo.getDefault();
            Object def = (defval==null)?null:defval.getValue();
            vals = new Object[varinfo.getCount()];
            for(int i=0;i<varinfo.getCount();i++)
            {
                switch(varinfo.type.type)
                {
                    case Type.kInt:
                        if(def==null) vals[i] = (int)0;
                        else vals[i] = (Integer)def;
                        break;
                    case Type.kByte:
                        if(def==null) vals[i] = (byte)0;
                        else vals[i] = ((Integer)def).byteValue();
                        break;
                    case Type.kBool:
                        if(def==null) vals[i] = (byte)0;
                        else vals[i] = ((Integer)def).byteValue();
                        break;
                    case Type.kFloat:
                        if(def==null) vals[i] = (float)0.0f;
                        else if(def.getClass()==Float.class) vals[i] = (Float)def;
                        else if(def.getClass()==Integer.class) vals[i] = ((float)(int)(Integer)def);
                        else m.throwUncaughtException("unexpected");
                        break;
                    case Type.kKey:
                        //there is no default desc.
                        break;
                    case Type.kQuaternion:
                        if(def==null) vals[i] = new Quat(0f,0f,0f,0f);
                        else
                        {
                            ArrayList<Value> quatvals = (ArrayList<Value>)def;
                            float w = quatvals.get(0).AsFloat();
                            float x = quatvals.get(1).AsFloat();
                            float y = quatvals.get(2).AsFloat();
                            float z = quatvals.get(3).AsFloat();
                            vals[i] = new Quat(w,x,y,z);
                            if(quatvals.size()>4) throw new shared.uncaughtexception("unexpected");
                        }
                        break;
                    case Type.kVector3:
                        if(def==null) vals[i] = new Vertex(0f,0f,0f);
                        else
                        {
                            ArrayList<Value> tupvals = (ArrayList<Value>)def;
                            float x = tupvals.get(0).AsFloat();
                            float y = tupvals.get(1).AsFloat();
                            float z = tupvals.get(2).AsFloat();
                            vals[i] = new Vertex(x,y,z);
                            if(tupvals.size()>3) throw new shared.uncaughtexception("unexpected");
                        }
                        break;
                    case Type.kString:
                        if(def==null) vals[i] = new FixedLengthString("", 32);
                        else
                        {
                            //ValueString def2 = (ValueString)def;
                            vals[i] = new FixedLengthString((String)def, 32);
                        }
                        break;
                    default:
                        int dummy=0;
                        throw new shared.uncaughtexception("unimplemented3");
                }
            }

            int dummy=0;
        }
    }
    public static class plSDStateVariable extends plStateVariable
    {
        byte u1; //0? ignored?
        //int totalvarcount;
        //int count;
        plStateDataRecord[] vars;

        //AbstractManager manager;

        public plSDStateVariable(context c, Varline varinfo)
        {
            super(c);
            //this.manager = manager;
            //throw new shared.uncaughtexception("unimplemented");

            String statedescName = ((TypeStatedesc)varinfo.type).typestr;
            Statedesc sd = SuperManager.getAgesInfo().getNewestStatedesc(statedescName);

            u1 = c.readByte();
            int totalvarcount;
            if(varinfo.isVariableLength())
            {
                totalvarcount = c.readInt();
            }
            else
            {
                totalvarcount = varinfo.getCount();
            }

            int count = readSmallest(c.in,totalvarcount);
            boolean isAllVars = count == totalvarcount;
            vars = new plStateDataRecord[totalvarcount];
            for(int i=0;i<count;i++)
            {
                int index = isAllVars?i:(readSmallest(c.in,totalvarcount)-1);
                vars[index] = new plStateDataRecord(c,sd); //only if index is in range?
            }

        }

        public int countVars()
        {
            int count = 0;
            for(plStateDataRecord sd: vars)
            {
                if(sd!=null) count++;
            }
            return count;
        }

        public void compile(Bytedeque c, Varline varinfo)
        {
            super.compile(c, varinfo);

            String statedescName = ((TypeStatedesc)varinfo.type).typestr;
            Statedesc sd = SuperManager.getAgesInfo().getNewestStatedesc(statedescName);

            c.writeByte(u1);
            int totalvarcount = vars.length;
            if(varinfo.isVariableLength())
            {
                c.writeInt(totalvarcount);
            }

            int count = 0; for(int i=0;i<vars.length;i++) if(vars[i]!=null) count++;
            writeSmallest(c,totalvarcount,count);
            boolean isAllVars = count==totalvarcount;
            for(int index=0;index<vars.length;index++)
            {
                vars[index].compile(c, sd);
            }
        }

    }
    public abstract static class plStateVariable
    {
        byte contents;
        byte xu1;
        Urustring hintString;

        public plStateVariable(context c)
        {
            contents = c.readByte();
            if((contents&SdlBinary.kHasNotificationInfo)!=0)
            {
                xu1 = c.readByte();
                hintString = new Urustring(c);
            }
        }

        public void compile(Bytedeque c, Varline varinfo)
        {
            c.writeByte(contents);
            if((contents&SdlBinary.kHasNotificationInfo)!=0)
            {
                c.writeByte(xu1);
                hintString.compile(c);
            }
        }
    }

}

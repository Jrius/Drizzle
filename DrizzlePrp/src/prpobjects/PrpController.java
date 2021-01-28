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
import shared.Quat;
import shared.Flt;
import uru.Bytestream;
import uru.Bytedeque;
import shared.m;
import uru.context;
import shared.readexception;
import shared.e;
import shared.b;

//the following work fine in pots:
//Point3Key, LeafController, ScalarController, Point3Controller, Point3KeyList, SimplePosController, ScalarKeyList, ScalarKey(apparently done wrong in pyprp, though), KeyFrame
public class PrpController extends uruobj
{
    /*Typeid type;
    uruobj controller;
    public PrpController(context c) throws readexception
    {
        type = Typeid.Read(c);
        switch(type)
        {
            case nil:
                break;
            case plLeafController:
                controller = new plLeafController(c);
                //controller = new plScalarController(c);
                //controller = new plSimplePosController(c);
                break;
            case plScalarController:
                controller = new plScalarController(c);
                break;
            case plSimplePosController:
                controller = new plSimplePosController(c);
                break;
            case plMatrix44Controller:
                controller = new plMatrix44Controller(c);
                break;
            default:
                m.err("unhandled type in AbstractController");
        }
    }
    public void compile(Bytedeque deque)
    {
        m.err("not implemented");
    }*/
    
    /*public static void specialCompileController(Bytedeque c, PrpTaggedObject object)
    {
        if(object.type==Typeid.plCompoundController)
        {
            m.err("haven't implemented this yet.");
        }
        else if(object.type==Typeid.plLeafController)
        {
        }
        else
        {
            object.compile(c); //compile as normal.
        }
    }*/
    public static class plCompoundPosController extends uruobj
    {
        public int flag1;
        public plScalarController pos1;
        public int flag2;
        public plScalarController pos2;
        public int flag3;
        public plScalarController pos3;
        
        public plCompoundPosController(context c) throws readexception
        {
            e.ensure(c.readversion==3);
            
            flag1 = c.readInt();
            if(flag1!=0)
            {
                pos1 = new plScalarController(c);
            }
            flag2 = c.readInt();
            if(flag2!=0)
            {
                pos2 = new plScalarController(c);
            }
            flag3 = c.readInt();
            if(flag3!=0)
            {
                pos3 = new plScalarController(c);
            }
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(flag1);
            if(flag1!=0)
            {
                pos1.compile(c);
            }
            c.writeInt(flag2);
            if(flag2!=0)
            {
                pos2.compile(c);
            }
            c.writeInt(flag3);
            if(flag3!=0)
            {
                pos3.compile(c);
            }
        }
    }
    
    public static class plSimpleRotController extends uruobj
    {
        int flag;
        plQuatController value;
        
        public plSimpleRotController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            flag = c.readInt();
            if(flag!=0)
            {
                value = new plQuatController(c);
            }
        }
    }
    
    public static class plQuatController extends uruobj
    {
        plLeafController parent;
        int count;
        hsQuatKey[] keys;
        
        public plQuatController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            count = c.readInt();
            keys = new hsQuatKey[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsQuatKey(c);
            }
        }
    }
    public static class hsQuatKey extends uruobj
    {
        hsKeyFrame keyframe;
        Quat value;
        
        public hsQuatKey(context c)
        {
            e.ensure(c.readversion==3);

            keyframe = new hsKeyFrame(c);
            value = new Quat(c);
            /*if(value.w.toJavaFloat()==1.0)
            {
                int dummy=0;
            }*/
            if(value.w.toJavaFloat()==value.y.toJavaFloat() && value.w.toJavaFloat()!=0.0)
            {
                int dummy=0;
            }
        }
    }
    public static class plCompoundRotController extends uruobj
    {
        int flag1;
        plScalarController pos1;
        int flag2;
        plScalarController pos2;
        int flag3;
        plScalarController pos3;
        
        public plCompoundRotController(context c) throws readexception
        {
            e.ensure(c.readversion==3);
            
            flag1 = c.readInt();
            if(flag1!=0)
            {
                pos1 = new plScalarController(c);
            }
            flag2 = c.readInt();
            if(flag2!=0)
            {
                pos2 = new plScalarController(c);
            }
            flag3 = c.readInt();
            if(flag3!=0)
            {
                pos3 = new plScalarController(c);
            }
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(flag1);
            if(flag1!=0)
            {
                pos1.compile(c);
            }
            c.writeInt(flag2);
            if(flag2!=0)
            {
                pos2.compile(c);
            }
            c.writeInt(flag3);
            if(flag3!=0)
            {
                pos3.compile(c);
            }
        }
    }
    public static class plSimpleScaleController extends uruobj
    {
        int flag;
        plScaleValueController scalecontroller;
        
        public plSimpleScaleController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            flag = c.readInt();
            if(flag!=0)
            {
                scalecontroller = new plScaleValueController(c);
            }
            if(scalecontroller.keys[0].keyframe.flags!=1)
            {
                int dummy=0;
            }
        }

        public void compile(Bytedeque c)
        {
            c.writeInt(flag);
            scalecontroller.compile(c);
        }
    }
    public static class plScaleValueController extends uruobj
    {
        plLeafController parent;
        int count;
        hsScaleKey[] keys;
        
        public plScaleValueController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            count = c.readInt();
            keys = new hsScaleKey[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsScaleKey(c);
            }
        }

        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(count);
            c.writeArray2(keys);
        }
    }
    public static class hsScaleKey extends uruobj
    {
        hsKeyFrame keyframe;
        Vertex xintan;
        Vertex xouttan;
        hsScaleValue value;
        
        public hsScaleKey(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            keyframe = new hsKeyFrame(c);
            if((keyframe.flags&0x02)!=0)
            {
                xintan = new Vertex(c);
                xouttan = new Vertex(c);
            }
            value = new hsScaleValue(c);
        }

        public void compile(Bytedeque c)
        {
            keyframe.compile(c);
            if((keyframe.flags&0x02)!=0)
            {
                xintan.compile(c);
                xouttan.compile(c);
            }
            value.compile(c);
        }
    }
    public static class hsScaleValue extends uruobj
    {
        Vertex s;
        Quat q;
        
        public hsScaleValue(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            s = new Vertex(c);
            q = new Quat(c);
        }

        public void compile(Bytedeque c)
        {
            s.compile(c);
            q.compile(c);
        }
    }
    public static class hsMatrix44Key extends uruobj
    {
        public hsKeyFrame parent;
        public Transmatrix value;
        
        public hsMatrix44Key(context c)
        {
            e.ensure(c.readversion==3);

            parent = new hsKeyFrame(c);
            value = new Transmatrix(c);
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            value.compile(c);
        }
    }
    public static class plMatrix44Controller extends uruobj
    {
        plLeafController parent;
        public int count;
        public hsMatrix44Key[] keys;
        
        public plMatrix44Controller(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            count = c.readInt();
            keys = new hsMatrix44Key[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsMatrix44Key(c);
            }
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(count);
            c.writeArray2(keys);
        }
    }
    public static class hsPoint3Key extends uruobj
    {
        hsKeyFrame parent;
        public Vertex xintan;
        public Vertex xouttan;
        public Vertex value;
        
        public hsPoint3Key(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new hsKeyFrame(c);
            if((parent.flags & 0x02)!=0)
            {
                xintan = new Vertex(c);
                xouttan = new Vertex(c);
            }
            value = new Vertex(c);
            //m.msg("hspoint3key: "+c.curRootObject.objecttype.toString()+":"+c.curRootObject.objectname.toString());
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            if((parent.flags & 0x02)!=0)
            {
                xintan.compile(c);
                xouttan.compile(c);
            }
            value.compile(c);
        }
    }
    public static class hsPoint3KeyList extends uruobj
    {
        public int count;
        public hsPoint3Key[] keys;
        
        public hsPoint3KeyList(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            count = c.readInt();
            keys = new hsPoint3Key[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsPoint3Key(c);
            }
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(count);
            c.writeArray2(keys);
        }
    }
    public static class plPoint3Controller extends uruobj
    {
        plLeafController parent;
        public int flag;
        public hsPoint3KeyList xkeylist;
        
        public plPoint3Controller(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            flag = c.readInt();
            if(flag !=0)
            {
                xkeylist = new hsPoint3KeyList(c);
            }
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(flag);
            if(flag !=0)
            {
                xkeylist.compile(c);
            }
        }
    }
    public static class plSimplePosController extends uruobj
    {
        public int flag;
        public plPoint3Controller xpoint3controller;
        
        public plSimplePosController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            flag = c.readInt();
            if(flag != 0)
            {
                xpoint3controller = new plPoint3Controller(c);
            }
            //m.msg("simpleposcontroller:"+c.curRootObject.toString());
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(flag);
            if(flag != 0)
            {
                xpoint3controller.compile(c);
            }
        }
    }
    public static class hsScalarKeyList extends uruobj //same as hsEaseKeyList
    {
        public int count;
        public hsScalarKey[] keys;
        
        public hsScalarKeyList(context c)
        {
            e.ensure(c.readversion==3);

            count = c.readInt();
            
            keys = new hsScalarKey[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsScalarKey(c);
            }
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(count);
            
            c.writeArray2(keys);
        }
    }
    public static class plScalarController extends uruobj
    {
        plLeafController parent;
        public int flag;
        public hsScalarKeyList xkeylist;
        
        public plScalarController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            flag = c.readInt();
            if(flag != 0)
            {
                xkeylist = new hsScalarKeyList(c);
            }
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            c.writeInt(flag);
            if(flag != 0)
            {
                xkeylist.compile(c);
            }
        }
    }
    public static class hsKeyFrame extends uruobj
    {
        int flags;
        int framenum;
        Flt frametime;
        
        public hsKeyFrame(context c)
        {
            e.ensure(c.readversion==3);

            flags = c.readInt();
            framenum = c.readInt();
            frametime = new Flt(c);
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(flags);
            c.writeInt(framenum);
            frametime.compile(c);
        }
    }
    public static class hsScalarKey extends uruobj
    {
        hsKeyFrame parent;
        //Vertex xintan;
        //Vertex xouttan;
        public Flt value;
        
        public Flt test1;
        public Flt test2;
        
        public hsScalarKey(context c)
        {
            e.ensure(c.readversion==3);

            parent = new hsKeyFrame(c);
            if((parent.flags & 0x02)!=0)
            {
                //xintan = new Vertex(c);
                //xouttan = new Vertex(c);
                test1 = new Flt(c);
                test2 = new Flt(c);
            }
            value = new Flt(c);
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            if((parent.flags & 0x02)!=0)
            {
                test1.compile(c);
                test2.compile(c);
            }
            value.compile(c);
        }
    }
    public static class hsEaseKeyList extends uruobj
    {
        int count;
        hsScalarKey[] keys;
        
        public hsEaseKeyList(context c)
        {
            e.ensure(c.readversion==3);

            count = c.readInt();
            
            keys = new hsScalarKey[count];
            for(int i=0;i<count;i++)
            {
                keys[i] = new hsScalarKey(c);
            }
        }
    }
    public static class plEaseController extends uruobj
    {
        plLeafController parent;
        int haskeylist;
        hsEaseKeyList xeasekeylist;
        
        public plEaseController(context c) throws readexception
        {
            e.ensure(c.readversion==3);

            parent = new plLeafController(c);
            haskeylist = c.readInt();
            if(haskeylist!=0)
            {
                xeasekeylist = new hsEaseKeyList(c);
            }
        }
    }
    
    public static class plLeafController extends uruobj implements uru.writesOwnTypeid
    {
        //pots:
        int u1;
        int count;
        plEaseController[] easecontrollers;
        int garbage;
        //byte[] rawdata;

        //moul:
        byte controllertype;
        int count2;
        moul1[] xtype1;
        moul2[] xtype2;
        moul3[] xtype3;
        moul4[] xtype4;
        moul5[] xtype5;
        moul6[] xtype6;
        moul7[] xtype7;
        moul8[] xtype8;
        moul9[] xtype9;
        moul10[] xtype10;
        moul11[] xtype11;
        moul12[] xtype12;
        
        private int readversion; //keep track of read version for compilation.
        
        public plLeafController(context c) throws readexception
        {
            this.readversion = c.readversion;
            
            if(c.readversion==3)
            {
                u1 = c.readInt(); //int in pots, byte in moul
                count = c.readInt();
                easecontrollers = c.readArray(plEaseController.class, count);
                garbage = c.readInt(); //doesn't exist in moul
            }
            else if(c.readversion==6)
            {
                controllertype = c.readByte(); //int in pots, byte in moul
                count2 = c.readInt();
                //rawdata = c.readBytes(14*count); //each 14byte block seems to be byte[2]floatfloatfloat or maybe shortfloatfloatfloat
                switch(controllertype)
                {
                    case 1: //plPoint3Controller (plSimplePosController)
                        xtype1 = c.readArray(moul1.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type1.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 2:
                        xtype2 = c.readArray(moul2.class, count2);
                        break;
                    case 3: //plScalarController
                        //only used in the neighborhood with objects not found in pots. But, in analogy with 4, I've implemented it.
                        xtype3 = c.readArray(moul3.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type3.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 4:
                        xtype4 = c.readArray(moul4.class, count2);
                        break;
                    case 5: //plScaleValueController
                        xtype5 = c.readArray(moul5.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type5.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 6:
                        xtype6 = c.readArray(moul6.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type6.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 7: //plQuatController
                        xtype7 = c.readArray(moul7.class, count2);
                        break;
                    case 8:
                        xtype8 = c.readArray(moul8.class, count2);
                        break;
                    case 9:
                        xtype9 = c.readArray(moul9.class, count2);
                        //m.msg("type9 encountered:"+c.curFile);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type9.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type9.txt", uru.debug.getStackTrace());
                        break;
                    case 10:
                        xtype10 = c.readArray(moul10.class, count2);
                        break;
                    case 11: //plMatrix33Controller
                        xtype11 = c.readArray(moul11.class, count2);
                        break;
                    case 12: //plMatrix44Controller
                        xtype12 = c.readArray(moul12.class, count2);
                        break;
                    default:
                        m.err("plleafcontroller: unknown type.");
                        break;
                }
            }
            else if(c.readversion==4||c.readversion==7)
            {
                byte controllertype2 = c.readByte(); //int in pots, byte in moul
                count2 = c.readInt();
                //rawdata = c.readBytes(14*count); //each 14byte block seems to be byte[2]floatfloatfloat or maybe shortfloatfloatfloat
                switch(controllertype2)
                {
                    case 1: //plPoint3Controller (plSimplePosController)
                        controllertype = 1;
                        xtype1 = c.readArray(moul1.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type1.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 2:
                        controllertype = 2;
                        xtype2 = c.readArray(moul2.class, count2);
                        break;
                    case 3: //plScalarController
                        controllertype = 3;
                        //only used in the neighborhood with objects not found in pots. But, in analogy with 4, I've implemented it.
                        xtype3 = c.readArray(moul3.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type3.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 4:
                        controllertype = 4;
                        xtype4 = c.readArray(moul4.class, count2);
                        break;
                    case 5: //plScaleValueController
                        controllertype = 5;
                        xtype5 = c.readArray(moul5.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type5.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 6:
                        controllertype = 6;
                        xtype6 = c.readArray(moul6.class, count2);
                        //uru.FileUtils.AppendText(_staticsettings.outputdir+"type6.txt", c.curFile+"::"+c.curRootObject.objectname.toString()+"::"+c.curRootObject.objecttype.toString()+"\n");
                        break;
                    case 7: //plQuatController
                        controllertype = 7;
                        xtype7 = c.readArray(moul7.class, count2);
                        break;
                    case 8:
                        controllertype = 8;
                        xtype8 = c.readArray(moul8.class, count2);
                        break;
                    case 9:
                        controllertype = 10;
                        xtype10 = c.readArray(moul10.class, count2);
                        break;
                    case 10: //plMatrix33Controller
                        controllertype = 11;
                        xtype11 = c.readArray(moul11.class, count2);
                        break;
                    case 11: //plMatrix44Controller
                        controllertype = 12;
                        xtype12 = c.readArray(moul12.class, count2);
                        break;
                    default:
                        m.err("plleafcontroller: unknown type.");
                        break;
                }
            }
        }
        
        public void compile(Bytedeque c)
        {
            //m.err("plLeafController doesn't implement compile directly.");
            
            if(this.readversion==4 || this.readversion==6 || this.readversion==7) //sep9brevert
            {
                compileSpecial(c);
            }
            else if(this.readversion==3)
            {
                c.writeInt(u1);
                c.writeInt(count);
                c.writeArray2(easecontrollers);
                c.writeInt(garbage);
            }
            else
            {
                throw new shared.uncaughtexception("PlLeafController: unexpected readversion in compile.");
            }
        }
        public void compileTypeid(Bytedeque c)
        {
            //do nothing, we'll write it when we compile.
            switch(controllertype)
            {
                case 1:
                    Typeid.plSimplePosController.compile(c);
                    break;
                case 2:
                    //should this be plEaseController instead?
                    Typeid.plSimplePosController.compile(c);
                    break;
                case 3:
                    //should this be plEaseController instead?
                    Typeid.plScalarController.compile(c);
                    break;
                case 4:
                    Typeid.plScalarController.compile(c);
                    break;
                case 5:
                    Typeid.plSimpleScaleController.compile(c);
                    break;
                case 6:
                    Typeid.plSimpleScaleController.compile(c);
                    break;
                case 9:
                    //This is actually a compressed quaternion. Type 7 has an uncompressed 4-float quaternion.
                    Typeid.plSimpleRotController.compile(c);
                    break;
                case 12:
                    Typeid.plMatrix44Controller.compile(c);
                    break;
                default:
                    m.err("plLeafController: error");
                    break;
            }
        }
        public void compileSpecial(Bytedeque c)
        {
            //plLeafController leaf = (plLeafController)object.prpobject.object;
            plLeafController leaf = this;
            
            int count = leaf.count2;
            switch(leaf.controllertype)
            {
                case 1:
                    //plSimplePosController
                        //flag
                            c.writeInt(1);
                        //plPoint3Controller
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //flags
                                c.writeInt(1);
                            //hsPoint3KeyList
                                //count
                                    c.writeInt(count);
                                //keys
                                    for(int i=0;i<count;i++)
                                    {
                                        //hsKeyFrame
                                            //flags
                                                c.writeInt(1);
                                            //framenum
                                                int framenum = b.Int16ToInt32(leaf.xtype1[i].framenum);
                                                c.writeInt(framenum);
                                            //time
                                                float time = framenum/30.0f;
                                                new Flt(time).compile(c);
                                        //data
                                            leaf.xtype1[i].data.compile(c);
                                    }
                    break;
                case 2:
                    //plSimplePosController
                        //flag
                            c.writeInt(1);
                        //plPoint3Controller
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //flags
                                c.writeInt(1);
                            //hsPoint3KeyList
                                //count
                                    c.writeInt(count);
                                //keys
                                    for(int i=0;i<count;i++)
                                    {
                                        //hsKeyFrame
                                            //flags
                                                c.writeInt(2);
                                            //framenum
                                                int framenum = b.Int16ToInt32(leaf.xtype2[i].framenum);
                                                c.writeInt(framenum);
                                            //time
                                                float time = framenum/30.0f;
                                                new Flt(time).compile(c);
                                        //data
                                            leaf.xtype2[i].data1.compile(c);
                                            leaf.xtype2[i].data2.compile(c);
                                            leaf.xtype2[i].data3.compile(c);
                                    }
                    break;
                case 3:
                    //doesn't occur in pots, so this is just an educated guess, in comparison with case 4.
                    //plScalarController
                        //plLeafController parent
                            c.writeInt(0);
                            c.writeInt(0);
                            c.writeInt(0);
                        //flags
                            c.writeInt(1);
                        //hsScalarKeyList
                            //count
                                c.writeInt(count);
                            //keys
                                for(int i=0;i<count;i++)
                                {
                                    //hsKeyFrame
                                        //flags
                                            c.writeInt(1);
                                        //framenum
                                            int framenum = b.Int16ToInt32(leaf.xtype3[i].framenum);
                                            c.writeInt(framenum);
                                        //time
                                            float time = framenum/30.0f;
                                            new Flt(time).compile(c);
                                    //data
                                        leaf.xtype3[i].data.compile(c);
                                }
                    break;
                case 4:
                    //plScalarController
                        //plLeafController parent
                            c.writeInt(0);
                            c.writeInt(0);
                            c.writeInt(0);
                        //flags
                            c.writeInt(1);
                        //hsScalarKeyList
                            //count
                                c.writeInt(count);
                            //keys
                                for(int i=0;i<count;i++)
                                {
                                    //hsKeyFrame
                                        //flags
                                            c.writeInt(2);
                                        //framenum
                                            int framenum = b.Int16ToInt32(leaf.xtype4[i].framenum);
                                            c.writeInt(framenum);
                                        //time
                                            float time = framenum/30.0f;
                                            new Flt(time).compile(c);
                                    //data
                                        leaf.xtype4[i].data1.compile(c);
                                        leaf.xtype4[i].data2.compile(c);
                                        leaf.xtype4[i].data3.compile(c);
                                }
                    break;
                case 5:
                    //plSimpleScaleController
                        //flag
                        c.writeInt(1);
                        //plScaleValueController
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //count
                                c.writeInt(count);
                            //keys
                            for(int i=0;i<count;i++)
                            {
                                //hsKeyFrame
                                    //flags
                                        c.writeInt(1);
                                    //framenum
                                        int framenum = b.Int16ToInt32(leaf.xtype5[i].framenum);
                                        c.writeInt(framenum);
                                    //time
                                        float time = framenum/30.0f;
                                        new Flt(time).compile(c);
                                //data
                                    leaf.xtype5[i].data1.compile(c); //vertex
                                    leaf.xtype5[i].data2.compile(c); //quat
                            }
                    break;
                case 6:
                    //plSimpleScaleController
                        //flag
                        c.writeInt(1);
                        //plScaleValueController
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //count
                                c.writeInt(count);
                            //keys
                            for(int i=0;i<count;i++)
                            {
                                //hsKeyFrame
                                    //flags
                                        c.writeInt(2);
                                    //framenum
                                        int framenum = b.Int16ToInt32(leaf.xtype6[i].framenum);
                                        c.writeInt(framenum);
                                    //time
                                        float time = framenum/30.0f;
                                        new Flt(time).compile(c);
                                //data
                                    //I verified this order.
                                    leaf.xtype6[i].data1.compile(c); //xintan
                                    leaf.xtype6[i].data2.compile(c); //xouttan
                                    leaf.xtype6[i].data3.compile(c); //s
                                    leaf.xtype6[i].data4.compile(c); //q
                            }
                    break;
                case 7: //added for myst5.
                    //m.warn("conpile prpcontroller: case7 untested.");
                    //plSimpleRotController
                        //flag
                        c.writeInt(1);
                        //plQuatController
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //count
                                c.writeInt(count);
                            //keys
                            for(int i=0;i<count;i++)
                            {
                                //hsKeyFrame
                                    //flags
                                        c.writeInt(1);
                                    //framenum
                                        int framenum = b.Int16ToInt32(leaf.xtype7[i].framenum);
                                        c.writeInt(framenum);
                                    //time
                                        float time = framenum/30.0f;
                                        new Flt(time).compile(c);
                                //data (hsQuat)
                                    //Quat quat = PrpController.decompressQuaternion(leaf.xtype9[i].data1, leaf.xtype9[i].data2);
                                    //quat.compile(c);
                                    leaf.xtype7[i].data.compile(c);
                            }
                    break;
                case 9:
                    //plSimpleRotController
                        //flag
                        c.writeInt(1);
                        //plQuatController
                            //plLeafController parent
                                c.writeInt(0);
                                c.writeInt(0);
                                c.writeInt(0);
                            //count
                                c.writeInt(count);
                            //keys
                            for(int i=0;i<count;i++)
                            {
                                //hsKeyFrame
                                    //flags
                                        c.writeInt(1);
                                    //framenum
                                        int framenum = b.Int16ToInt32(leaf.xtype9[i].framenum);
                                        c.writeInt(framenum);
                                    //time
                                        float time = framenum/30.0f;
                                        new Flt(time).compile(c);
                                //data (hsQuat)
                                    Quat quat = PrpController.decompressQuaternion(leaf.xtype9[i].data1, leaf.xtype9[i].data2);
                                    quat.compile(c);
                            }
                    break;
                case 12:
                    //plMatrix44Controller
                        //plLeafController parent
                            c.writeInt(0);
                            c.writeInt(0);
                            c.writeInt(0);
                        //count
                            c.writeInt(count);
                        //keys
                            for(int i=0;i<count;i++)
                            {
                                //hsKeyFrame
                                    //flags
                                        c.writeInt(2);
                                    //framenum
                                        int framenum = b.Int16ToInt32(leaf.xtype12[i].framenum);
                                        c.writeInt(framenum);
                                    //time
                                        float time = framenum/30.0f;
                                        new Flt(time).compile(c);
                                //data
                                    leaf.xtype12[i].data.compile(c);
                            }
                    break;
                default:
                    m.err("prpcontroller: specialcompile: unhandled type.",Integer.toString(leaf.controllertype));
                    break;
            }
        }
    }
    
    public static class moul1 extends uruobj
    {
        short framenum;
        Vertex data;
        
        public moul1(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = new Vertex(c);
        }
    }
    public static class moul2 extends uruobj
    {
        short framenum;
        Vertex data1;
        Vertex data2;
        Vertex data3;
        
        public moul2(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7) //sep10rev
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data1 = new Vertex(c);
            data2 = new Vertex(c);
            data3 = new Vertex(c);
        }
    }
    public static class moul3 extends uruobj
    {
        short framenum;
        Flt data;
        
        public moul3(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = new Flt(c);
        }
    }
    public static class moul4 extends uruobj
    {
        short framenum;
        Flt data1;
        Flt data2;
        Flt data3;
        
        public moul4(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data1 = new Flt(c);
            data2 = new Flt(c);
            data3 = new Flt(c);
        }
    }
    public static class moul5 extends uruobj
    {
        short framenum;
        Vertex data1;
        Quat data2;
        
        public moul5(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7) //sep10rev
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data1 = new Vertex(c);
            data2 = new Quat(c);
        }
    }
    public static class moul6 extends uruobj
    {
        short framenum;
        Vertex data1;
        Vertex data2;
        Vertex data3;
        Quat data4;
        
        public moul6(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7) //sep10rev
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data1 = new Vertex(c);
            data2 = new Vertex(c);
            data3 = new Vertex(c);
            data4 = new Quat(c);
        }
    }
    public static class moul7 extends uruobj
    {
        short framenum;
        Quat data;
        
        public moul7(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = new Quat(c);
        }
    }
    public static class moul8 extends uruobj
    {
        short framenum;
        int data;
        
        public moul8(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = c.readInt();
        }
    }
    public static class moul9 extends uruobj
    {
        short framenum;
        int data1;
        int data2;
        
        public moul9(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data1 = c.readInt();
            data2 = c.readInt();
            PrpController.decompressQuaternion(data1, data2);
        }
    }
    public static class moul10 extends uruobj
    {
        short framenum;
        PrpController.uk data;
        
        public moul10(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = new PrpController.uk(c);
        }
    }
    public static class moul11 extends uruobj
    {
        short framenum;
        Flt[] matrixdata;
        
        public moul11(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            matrixdata = c.readArray(Flt.class, 9); //3x3 matrix. Is the order correct? Probably, since it was with 4x4 matrices.
        }
    }
    public static class moul12 extends uruobj
    {
        short framenum;
        Transmatrix data;
        
        public moul12(context c) throws readexception
        {
            if(c.readversion==6)
            {
                framenum = c.readShort();
            }
            else if(c.readversion==4||c.readversion==7)
            {
                Flt flt = new Flt(c);
                //m.msg(flt.toString());
                framenum = (short)java.lang.Math.round(flt.toJavaFloat()*30.0f);
            }
            data = new Transmatrix(c);
        }
    }
    
    public static class plCompoundController extends uruobj implements uru.writesOwnTypeid
    {
        PrpTaggedObject u1;
        PrpTaggedObject u2;
        PrpTaggedObject u3;
        
        public plCompoundController(context c) throws readexception
        {
            e.ensure(c.readversion==6||c.readversion==4||c.readversion==7);
            
            u1 = new PrpTaggedObject(c);
            u2 = new PrpTaggedObject(c);
            u3 = new PrpTaggedObject(c);
        }
        
        public void compileTypeid(Bytedeque c)
        {
            //do nothing, but write the typeid during compilation.
            int mytype = this.gettype();
            if(mytype==4)
            {
                Typeid.plTMController.compile(c);
            }
            else if(mytype==6)
            {
                //todo: how do we differentiate between these two? Do we need to? There doesn't seem to be any way of telling which we want.
                Typeid.plCompoundPosController.compile(c);
                //Typeid.plCompoundRotController.compile(c);
            }
            else
            {
                m.err("plcompoundcontroller: compiletypeid: problem.");
            }
        }
        
        public void compile(Bytedeque c)
        {
            compileSpecial(c);
        }
        private static int gettype(PrpTaggedObject obj)
        {
            //0=nil
            //1=pos
            //2=rot
            //3=scale
            //4=TMcontroller
            //5=compoundrot/compoundpos plscalarcontroller subelement
            //6=compoundrot/compoundpos
            int type = -1;
            if(obj.type==Typeid.nil)
            {
                type = 0;
            }
            else if(obj.type==Typeid.plLeafController)
            {
                PrpController.plLeafController leaf = (PrpController.plLeafController)obj.prpobject.object;
                switch(leaf.controllertype)
                {
                    case 1:
                    case 2:
                        type = 1;
                        break;
                    case 3:
                    case 4:
                        if(leaf.controllertype==3)
                        {
                            m.warn("Experimental prpcontroller case...");
                        }
                        type = 5;
                        break;
                    case 7: //myst5 added, untested, but it's just a quat so it should be a rotation.
                    case 9:
                        type = 2;
                        break;
                    case 5:
                    case 6:
                        type = 3;
                        break;
                    default:
                        m.err("plcompoundcontroller:gettype:problem");
                        break;
                }
            }
            else if(obj.type==Typeid.plCompoundController)
            {
                PrpController.plCompoundController leaf = (PrpController.plCompoundController)obj.prpobject.object;
                type = leaf.gettype();
            }
            else
            {
                m.err("plCompoundController:gettype:problem.");
            }
            return type;
        }
        private int gettype()
        {
            //0=nil
            //1=pos
            //2=rot
            //3=scale
            //4=TMcontroller
            //5=compoundrot/compoundpos plscalarcontroller subelement
            //6=compoundrot/compoundpos
            int type1 = gettype(u1);
            int type2 = gettype(u2);
            int type3 = gettype(u3);
            
            if((type1==0 || type1==1 || type1==6)&&(type2==0||type2==2 || type2==6)&&(type3==0||type3==3))
            {
                return 4; //TMController.
            }

            //m.warn("plcompoundcontroller: not a TMController.");
            
            if((type1==0 || type1==5) && (type2==0 || type2==5) && (type3==0 || type3==5))
            {
                return 6; //compoundposcontroller or compoundrotcontroller.
            }

            //if(type1==type2 && type2==type3) return type1; //all agree.
            //if(type1==0 && type2==type3) return type2;
            //if(type2==0 && type1==type3) return type1;
            //if(type3==0 && type1==type2) return type1;
            //if(type1==0 && type2==0) return type3;
            //if(type1==0 && type3==0) return type2;
            //if(type2==0 && type3==0) return type1;
            
            
            //if none of these are true, we have a potential problem.
            m.warn("plcompoundcontroller: no type matches.");
            return -1;
        }
        public void compileSpecial(Bytedeque c)
        {
            String breakname="RTSpiralLight01_back_anim_1";
            if(c.curRootObject.objectname.toString().toLowerCase().equals(breakname.toLowerCase()))
            {
                int dummy=0;
            }
            
            //check type:
            int mytype = this.gettype();
            
            if(mytype==4)
            {
                //plTMController
                if(u1.type==Typeid.nil)
                {
                    c.writeInt(0);
                }
                else if(u1.type==Typeid.plLeafController)
                {
                    c.writeInt(1);
                    u1.prpobject.object.compile(c);
                }
                else if(u1.type==Typeid.plCompoundController)
                {
                    c.writeInt(2);
                    u1.prpobject.object.compile(c);
                }
                else
                {
                    m.err("plCompoundController: unhandled.");
                }

                if(u2.type==Typeid.nil)
                {
                    c.writeInt(0);
                }
                else if(u2.type==Typeid.plLeafController)
                {
                    c.writeInt(1);
                    u2.prpobject.object.compile(c);
                }
                else if(u2.type==Typeid.plCompoundController)
                {
                    c.writeInt(3);
                    u2.prpobject.object.compile(c);
                }
                else
                {
                    m.err("plCompoundController: unhandled.");
                }

                if(u3.type==Typeid.nil)
                {
                    c.writeInt(0);
                }
                else if(u3.type==Typeid.plLeafController)
                {
                    c.writeInt(1);
                    u3.prpobject.object.compile(c);
                }
                else
                {
                    m.err("plCompoundController: unhandled.");
                }
            }
            else if(mytype==6)
            {
                //plCompoundPosController or plCompoundRotController.
                if(u1.type==Typeid.nil)
                {
                    c.writeInt(0); //flag1
                }
                else if(u1.type==Typeid.plLeafController)
                {
                    plLeafController lc = (plLeafController)u1.prpobject.object;
                    if(lc.controllertype==4||lc.controllertype==3)
                    {
                        c.writeInt(1); //flag1
                        u1.prpobject.object.compile(c); //scalarcontroller1
                    }
                    else
                    {
                        m.err("plcompoundcontroller: unhandled");
                    }
                }
                else
                {
                    m.err("plcompoundcontroller: unhandled.");
                }
                if(u2.type==Typeid.nil)
                {
                    c.writeInt(0); //flag2
                }
                else if(u2.type==Typeid.plLeafController)
                {
                    plLeafController lc = (plLeafController)u2.prpobject.object;
                    if(lc.controllertype==4||lc.controllertype==3)
                    {
                        c.writeInt(1); //flag2
                        u2.prpobject.object.compile(c); //scalarcontroller2
                    }
                    else
                    {
                        m.err("plcompoundcontroller: unhandled");
                    }
                }
                else
                {
                    m.err("plcompoundcontroller: unhandled.");
                }
                if(u3.type==Typeid.nil)
                {
                    c.writeInt(0); //flag3
                }
                else if(u3.type==Typeid.plLeafController)
                {
                    plLeafController lc = (plLeafController)u3.prpobject.object;
                    if(lc.controllertype==4||lc.controllertype==3)
                    {
                        c.writeInt(1); //flag3
                        u3.prpobject.object.compile(c); //scalarcontroller3
                    }
                    else
                    {
                        m.err("plcompoundcontroller: unhandled");
                    }
                }
                else
                {
                    m.err("plcompoundcontroller: unhandled.");
                }
            }
            else
            {
                m.err("plCompoundController: unhandled.");
            }
        }
    }
    
    public static class uk extends uruobj //hsAffineParts
    {
        int xu0; //apparently always zero.
        public Vertex u1; //T
        public Quat u2; //Q
        public Quat u3; //U
        public Vertex u4; //K
        public Flt u5; //F
        
        public uk(context c) throws readexception
        {
            if(c.readversion==3||c.readversion==4||c.readversion==7) //sep9revert
            {
                xu0 = c.readInt();
                //m.msg("xu0="+Integer.toString(xu0));
            }
            u1 = new Vertex(c);
            u2 = new Quat(c);
            u3 = new Quat(c);
            u4 = new Vertex(c);
            u5 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(0); //xu0
            u1.compile(c);
            u2.compile(c);
            u3.compile(c);
            u4.compile(c);
            u5.compile(c);
        }
    }
    public static class plTMController extends uruobj
    {
        public int type1;
        public int type2;
        public int type3;
        public PrpObject poscontroller;
        public PrpObject rotcontroller;
        public PrpObject scalecontroller;
        
        public plTMController(context c) throws readexception
        {
            e.ensure(c.readversion==3);
            
            type1 = c.readInt();
            switch(type1) //posController
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimplePosController
                    poscontroller = new PrpObject(c,Typeid.plSimplePosController);
                    break;
                case 0x02: //plCompoundPosController
                    poscontroller = new PrpObject(c,Typeid.plCompoundPosController);
                    break;
                default:
                    throw new readexception("pltmcontroller: error");
            }
            
            type2 = c.readInt(); //rotController
            switch(type2)
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimpleRotController
                    rotcontroller = new PrpObject(c,Typeid.plSimpleRotController);
                    break;
                case 0x03: //plCompoundRotController
                    rotcontroller = new PrpObject(c,Typeid.plCompoundRotController);
                    break;
                default:
                    throw new readexception("pltmcontroller: error");
            }
            
            type3 = c.readInt(); //scaleController
            switch(type3)
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimpleScaleController
                    scalecontroller = new PrpObject(c,Typeid.plSimpleScaleController);
                    break;
                default:
                    throw new readexception("pltmcontroller: error");
            }

        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(type1);
            switch(type1) //posController
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimplePosController
                    poscontroller.compile(c);
                    break;
                case 0x02: //plCompoundPosController
                    poscontroller.compile(c);
                    break;
                default:
                    throw new shared.uncaughtexception("pltmcontroller: error");
            }
            
            c.writeInt(type2);
            switch(type2)
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimpleRotController
                    rotcontroller.compile(c);
                    break;
                case 0x03: //plCompoundRotController
                    rotcontroller.compile(c);
                    break;
                default:
                    throw new shared.uncaughtexception("pltmcontroller: error");
            }
            
            c.writeInt(type3);
            switch(type3)
            {
                case 0x00: //nil
                    break;
                case 0x01: //plSimpleScaleController
                    scalecontroller.compile(c);
                    break;
                default:
                    throw new shared.uncaughtexception("pltmcontroller: error");
            }
        }
    }
    
    public static Quat decompressQuaternion(int data1,int data2)
    {
        //There is an alternatively compressed version, that just uses a single int.  It apparently isn't used in moul, but it probably goes: 10bits, 10bits, 10bits, 2bits.
        
        long d1 = b.Int32ToInt64(data1);
        long d2 = b.Int32ToInt64(data2);
        long data = (d1<<0) | (d2<<32);
        
        int bits1 = (int)((data >>> 0) & 0x03FF); //get 10 bits;
        int bits2 = (int)((data >>> 10) & 0x0FFFFF); //get 20 bits;
        int bits5 = (int)((data >>> 30) & 0x03); //get 2 bits;
        int bits4 = (int)((data >>> 32) & 0x1FFFFF); //get 21 bits;
        int bits3 = (int)((data >>> 53) & 0x08FF); //get 11 bits;
        int bits13 = bits3 | (bits1 << 11); //21 bits
        //long bits2a=0;
        //long bits4a=0;
        //long bits13a=0;
        //if((bits2&0x80000)!=0) bits2a = 4294967296L - bits2;
        //if((bits4&0x100000)!=0) bits4a = 4294967296L - bits4;
        //if((bits13&0x100000)!=0) bits13a = 4294967296L - bits13;
        //float a = java.lang.Float.intBitsToFloat(0x49b504ee);
        //float a2 = java.lang.Float.intBitsToFloat(0x493504e8);
        //float b = (float)Math.sqrt(2)*(1024*1024)/2;
        //float b2 = (float)Math.sqrt(2)*(1024*1024-1)/2;
        //float b3 = (float)Math.sqrt(2)*(1024*1024+1)/2;
        //float ba = (float)(Math.sqrt(2)*(1024*1024));
        //float ba2 = (float)(Math.sqrt(2)*(1024*1024-1));
        //float ba3 = (float)(Math.sqrt(2)*(1024*1024+1));
        //2097151 = 2^21-1 and 1048575 = 2^20-1
        double decoded1 = (bits2/1048575.0-0.5)*Math.sqrt(2); //x
        double decoded2 = (bits13/2097151.0-0.5)*Math.sqrt(2); //y
        double decoded3 = (bits4/2097151.0-0.5)*Math.sqrt(2); //z
        double last = Math.sqrt(1.0-(decoded2*decoded2+decoded1*decoded1+decoded3*decoded3));
        
        float w = 0;
        float x = 0;
        float y = 0;
        float z = 0;
        switch(bits5)
        {
            case 0:
                w = (float)last;
                x = (float)decoded1;
                y = (float)decoded2;
                z = (float)decoded3;
                break;
            case 1:
                w = (float)decoded1;
                x = (float)last;
                y = (float)decoded2;
                z = (float)decoded3;
                break;
            case 2:
                w = (float)decoded1;
                x = (float)decoded2;
                y = (float)last;
                z = (float)decoded3;
                break;
            case 3:
                w = (float)decoded1;
                x = (float)decoded2;
                y = (float)decoded3;
                z = (float)last;
                break;
        }
        Quat result = new Quat(new Flt(w), new Flt(x), new Flt(y), new Flt(z));
        return result;
        /*int bits1 = (int)((data >>> 0) & 0x03FF); //get 10 bits;
        //int bits1 = (int)((data >>> 0) & 0x0FFFFF); //get 20 bits;
        int bits2 = (int)((data >>> 10) & 0x03FF); //get next 10 bits;
        int bits3 = (int)((data >>> 20) & 0x03FF); //get next 10 bits;
        int bits4 = (int)((data >>> 33) & 0x03FF); //get next 10 bits;
        int bits5 = (int)((data >>> 43) & 0x03FF); //get next 10 bits;
        int bits6 = (int)((data >>> 53) & 0x03FF); //get next 10 bits;
        int bits12 = (bits1<<10) | bits2;
        int bits34 = (bits3<<10) | bits4;
        int bits56 = (bits5<<10) | bits6;
        int missingval1 = (int)((data >>> 30) & 0x03); //get last 2 bits;
        int missingval2 = (int)((data >>> 62) & 0x03); //get last 2 bits;
        int missingval3 = (int)((data >>> 60) & 0x0F); //get last 4 bits;
        int missingval4 = (int)((data >>> 30) & 0x07); //get last 3 bits;
        int missingval5 = (int)((data >>> 63) & 0x01); //get last 1 bits;
        m.msg(":::"+Integer.toString(missingval1)+":::"+Integer.toString(missingval2)+":::"+Integer.toString(missingval3)+":::"+Integer.toString(missingval4)+":::"+Integer.toString(missingval5));
        double decoded1 = (bits1/1023.0-0.5)*Math.sqrt(2);
        double decoded12 = (bits12/1048575.0-0.5)*Math.sqrt(2);
        double decoded34 = (bits34/1048575.0-0.5)*Math.sqrt(2);
        double decoded56 = (bits56/1048575.0-0.5)*Math.sqrt(2);
        //double decoded1 = (bits1/1048575.0-0.5)*Math.sqrt(2);
        double decoded2 = (bits2/1023.0-0.5)*Math.sqrt(2);
        double decoded3 = (bits3/1024.0-0.5)*Math.sqrt(2);
        double decoded4 = (bits4/1023.0-0.5)*Math.sqrt(2);
        double decoded5 = (bits5/1023.0-0.5)*Math.sqrt(2);
        double decoded6 = (bits6/1023.0-0.5)*Math.sqrt(2);
        float result1 = (float)decoded1;
        float result2 = (float)decoded2;
        float result3 = (float)decoded3;*/
        
    }
}

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

package realmyst;

import shared.*;

public class Count9
{
    
    
    public Count9(IBytestream c)
    {
        Typeid type = Typeid.read(c); e.ensure(type==Typeid.count9);
        int size = c.readInt(); //size of this object.
        
        int u1 = c.readInt(); e.ensure(u1,1);
        Bstr s1 = new Bstr(c);
        byte b1 = c.readByte(); e.ensure((int)b1,15);
        int u2 = c.readInt(); e.ensure(u2,0);
        int u3 = c.readInt(); e.ensure(u3,0,5,3,1,2,4);
        int u4 = c.readInt(); //e.ensure(u4,16418,4,24580,17412,16388,25604);
        int u5 = c.readInt(); e.ensure(u5,0);
        int u6 = c.readInt(); e.ensure(u6,0,1,2);
        //int u7 = c.readInt(); e.ensure(u7,0);
        Bstr u7 = new Bstr(c); //usually empty.
        //int u8 = c.readInt(); e.ensure(u8,0);
        Bstr u8 = new Bstr(c); //usually empty.
        int u9 = c.readInt();
        //Bstr s2 = new Bstr(c);
        Bstr[] s2s = c.readArray(Bstr.class, u9);
        int u10 = c.readInt(); e.ensure(u10>=0);//e.ensure(u10,0,1,2,3); //0
        if(u10!=1)
        {
            int dummy=0;
        }
        //else if(u10==1 || u10==3 || u10==2)
        //{
            //int u11 = c.readInt(); e.ensure(u11,1001); //3
            //Typeid reftype = Typeid.read(c);
            //TaggedObj[] refs = c.readArray(TaggedObj.class, u10);
            Ref2[] ref2s = c.readArray(Ref2.class, u10);
            //if(reftype==Typeid.ref)
            //{
            //    if(u10!=1)
            //    {
            //        int dummy=0;
            //    }
                /*count9ref ref = new count9ref(c);
                int u12 = c.readInt(); e.ensure(u12,1); //if this is not 1, the array part might break because it might be wrong.
                //Bstr s3 = new Bstr(c);
                Bstr[] s3s = c.readArray(Bstr.class, u12);*/
            //    Ref1[] ref1s = c.readArray(Ref1.class, u10);
            //    int dummy=0;
            //}
            //else if(reftype==Typeid.ref2)
            //{
            //    if(u10!=1)
            //    {
            //        int dummy=0;
            //    }
            //    Ref2[] ref2s = c.readArray(Ref2.class, u10);
            //}
            //else
            //{
            //    m.err("Unhandled reftype in Count9.");
            //}
        //}
        int strcount = c.readInt();
        Bstr[] strs = c.readArray(Bstr.class, strcount);
       
        int dummy=0;
    }
    public static class Subref2
    {
        public Subref2(IBytestream c)
        {
            //int count = c.readInt();
            //Bstr[] strs = c.readArray(Bstr.class, 1);
            int dummy=0;
        }
    }
    public static class Ref1
    {
        public Ref1(IBytestream c)
        {
            Count10.occref ref = new Count10.occref(c);
            int u12 = c.readInt(); //e.ensure(u12,1); //if this is not 1, the array part might break because it might be wrong.
            //Bstr s3 = new Bstr(c);
            Bstr[] s3s = c.readArray(Bstr.class, u12);
            int dummy=0;
            
        }
    }
    public static class Ref2
    {
        public Ref2(IBytestream c)
        {
            //Typeid thisguy = Typeid.read(c); //ref2, ref
            int tag = c.readInt();
            
            Typeid type2 = Typeid.read(c); e.ensure(type2==Typeid.count9ref);//count9ref
            int u1 = c.readInt(); e.ensure(u1,0);
            int u2 = c.readInt(); e.ensure(u2,1);
            int u3 = c.readInt(); e.ensure(u3,8,24,10,26); //24
            int dummy2=0;
            /*int u4 = c.readInt(); e.ensure(u4,2,1);
            if(u4==2)
            {
                int u5 = c.readInt(); e.ensure(u5,1); //
                int u6 = c.readInt(); e.ensure(u6,0);
                Bstr s1 = new Bstr(c);
                int u7 = c.readInt(); e.ensure(u7,1); //3 seems the same.
            }
            else if(u4==1)
            {
                if(u3==8)
                {
                    int u5 = c.readInt(); e.ensure(u5,0);
                    int u12 = c.readInt();
                    Bstr[] s3s = c.readArray(Bstr.class, u12);
                }
                else if(u3==24)
                {
                    int v1 = c.readInt(); //1
                    int v2 = c.readInt(); //0
                    int v3 = c.readInt(); //0
                    int v4 = c.readInt(); //2000
                    int v5 = c.readInt(); //2000
                    int v6 = c.readInt(); //1
                    byte v7 = c.readByte(); //0
                    int v8 = c.readInt(); //1
                    int v9 = c.readInt(); //0
                }
                
            }
            else
            {
                int dummy=0;
            }*/
            
            
            //if(thisguy==Typeid.ref2)
            if(tag==0x3ea)
            {
                //Typeid targetTypeMaybe = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,8,10,24);
                int u4 = c.readInt(); e.ensure(u4,2);
                int u5 = c.readInt(); //e.ensure(u5,676); //676,762,1064,etc
                //int u6 = c.readInt(); e.ensure(u6,0);
                Bstr s6 = new Bstr(c); //usually empty.
                Bstr s7 = new Bstr(c);
                int u8 = c.readInt(); e.ensure(u8,3,1,2); //1 seems the same.
                int dummy=0;
            }
            //else if(thisguy==Typeid.ref)
            else if(tag==0x3e9)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,8,10);
                int u4 = c.readInt(); e.ensure(u4,1);
                int count = c.readInt(); e.ensure(count,0,2);//e.ensure(u5,2);
                //suboccref[] subs = c.readArray(suboccref.class, count);

                int u12 = c.readInt(); //e.ensure(u12,1); //if this is not 1, the array part might break because it might be wrong.
                //Bstr s3 = new Bstr(c);
                Bstr[] s3s = c.readArray(Bstr.class, u12);
                int dummy=0;
                
            }
            else if(tag==0x3ec)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,24,26); //24
                int u4 = c.readInt(); e.ensure(u4,1);
                int count = c.readInt(); e.ensure(count,1,0,2);//e.ensure(u5,2);
                //suboccref[] subs = c.readArray(suboccref.class, count);

                int u12 = c.readInt(); //e.ensure(u12,0,551,253,691,400,112); //if this is not 1, the array part might break because it might be wrong.
                //Bstr s3 = new Bstr(c);
                //Bstr[] s3s = c.readArray(Bstr.class, u12);
                
                int u20 = c.readInt(); //e.ensure(u20,0,90,513,93,92,510);
                int u21 = c.readInt(); //e.ensure(u21,2000,0,800,250);
                int u22 = c.readInt(); //e.ensure(u22,2000,0,90,513,600);
                //int u23 = c.readInt(); e.ensure(u23,1);
                //byte b245 = c.readByte(); e.ensure((int)b245,0);
                Bstr s23 = new Bstr(c);
                int u24 = c.readInt(); e.ensure(u24,1);
                int u25 = c.readInt(); e.ensure(u25,0,1);
                //int u26 = c.readInt();
                int dummy=0;
            }
            else if(tag==0x3ed)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,24,26); //24
                int u4 = c.readInt(); e.ensure(u4,1);
                int count = c.readInt(); e.ensure(count,0,1,2);//1 might do something special.
                //suboccref[] subs = c.readArray(suboccref.class, count);

                int u12 = c.readInt(); //e.ensure(u12,0,400,112,253); //if this is not 1, the array part might break because it might be wrong.
                //Bstr s3 = new Bstr(c);
                //Bstr[] s3s = c.readArray(Bstr.class, u12);
                
                int u20 = c.readInt(); //e.ensure(u20,0,95,93);
                int u21 = c.readInt(); //e.ensure(u21,0,688,397,548);
                int u22 = c.readInt(); //e.ensure(u22,0,507,510);
                Bstr s23 = new Bstr(c);
                int dummy=0;
            }
            else if(tag==0x3)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,8);
            }
            else if(tag==0x3eb)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,8);
                int u4 = c.readInt(); e.ensure(u4,1);
                int u5 = c.readInt(); //e.ensure(u5,0x41200000,0x43340000,0x40000001);
                int u6 = c.readInt(); e.ensure(u6,4);
                int u7 = c.readInt(); e.ensure(u7,0,0x3f000000);
                int dummy=0;
            }
            else if(tag==0x3ee)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,10,8);
                int u4 = c.readInt(); e.ensure(u4,1);
                int u5 = c.readInt(); e.ensure(u5,2,3);
                int u6 = c.readInt(); e.ensure(u6,1,6,4);
                //Bstr s7 = new Bstr(c);
                Bstr[] s7s = c.readArray(Bstr.class, u6);
                int dummy=0;
            }
            else if(tag==0x0)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,24);
                int dummy=0;
            }
            else if(tag==0x1)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,24);
                int dummy=0;
            }
            else if(tag==0x9)
            {
                //Typeid type = Typeid.read(c); //count9ref
                //int u1 = c.readInt(); e.ensure(u1,0);
                //int u2 = c.readInt(); e.ensure(u2,1);
                //int u3 = c.readInt(); e.ensure(u3,8,24);
                int u4 = c.readInt(); e.ensure(u4,1);
                int u5 = c.readInt(); e.ensure(u5,0,2);
                Flt f6 = new Flt(c);
                Bstr s7 = new Bstr(c);
                if(u5==2)
                {
                    Bstr s8 = new Bstr(c);
                }
                int dummy=0;
            }
            else if(tag==0x3f0)
            {
                int u4 = c.readInt(); e.ensure(u4,1);
                int u5 = c.readInt(); e.ensure(u5,2);
                int u6 = c.readInt(); e.ensure(u6,3);
                Bstr s7 = new Bstr(c);
                Bstr s8 = new Bstr(c);
                Bstr s9 = new Bstr(c);
                int dummy=0;
            }
            else
            {
                int dummy=0;
            }
        
            //int strcount = c.readInt();
            //Bstr[] strs = c.readArray(Bstr.class, strcount);
        }
    }
}

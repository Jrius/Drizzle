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

public class Count10
{
    //int tag;
    public Typeid type;
    public int size;
    public int u1;
    public Bstr s1;
    public byte b1;
    public int u2;
    public int u3;
    public int u4;
    public int u5;
    public Bstr s2;
    public int count1;
    public occref[] occrefs1;
    public int count2;
    public occref[] occrefs2;
    public int count3Maybe;
    public int count4;
    public StringAndByte[] stringAndBytes;
    //int sub1;
    //Bstr xsubs;
    //Flagsen xsubs;
    //int sub2;
    //Flagsen[] subs2;
    //TaggedObj[] subs2;
    
    public Count10(IBytestream c)
    {
        //sub_5027f0???
        
        //tag = c.readInt(); e.ensure(Typeid.has(tag));
        type = Typeid.read(c); e.ensure(type==Typeid.count10);
        size = c.readInt(); //size of this object (including type).
        
        /*
        IBytestream f = c.Fork();
        byte[] data = f.readBytes(size-8);
        
        String tmp = new String(data);
        if (tmp.contains("bird_toy03"))
        {
            m.msg("FOUND BIRD_TOY03 10");
            IBytedeque out = new Bytedeque2(shared.Format.realmyst);
            out.writeBytes(data);
            byte[] filedata = out.getAllBytes();

            FileUtils.WriteFile("out.dat", filedata);
        }//*/
        
        u1 = c.readInt(); e.ensure(u1,1);
        s1 = new Bstr(c);
        b1 = c.readByte(); e.ensure((int)b1,16);
        u2 = c.readInt(); e.ensure(u2,0);
        u3 = c.readInt(); e.ensure(u3,0);
        u4 = c.readInt(); e.ensure(u4,4);
        u5 = c.readInt(); e.ensure(u5,0,256,8,1);
        s2 = new Bstr(c);
        
        count1 = c.readInt();
        occrefs1 = c.readArray(occref.class, count1);

        count2 = c.readInt();
        occrefs2 = c.readArray(occref.class, count2);
        
        count3Maybe = c.readInt(); e.ensure(count3Maybe==0);
        
        count4 = c.readInt();
        stringAndBytes = c.readArray(StringAndByte.class, count4);
        
        /*sub1 = c.readInt();
        if(sub1!=0)
        {
            //45a950 block
            //xsubs = new Bstr(c);
            //byte type = c.readByte();
            xsubs = new Flagsen(c);
            int dummy=0;
        }
        sub2 = c.readInt();
        subs2 = new TaggedObj[sub2];
        for(int j=0;j<sub2;j++)
        {
            //45a950 block
            //m.err("unhandled");
            //Bstr subs2 = new Bstr(c);
            subs2[j] = new TaggedObj(c);
        }*/
        
    }
    public static class occref
    {
        public Typeid type;
        public int u1;
        public int u2;
        public int u3;
        public int u4;
        public int count;
        public suboccref[] subs;
        
        public static class suboccref
        {
            public int u6;
            public int u7;
            public int u8;
            public int u9;
            public int u10;
            
            public int xint;
            public Flt xfloat;
            public Bstr xstr;
            public Vertex xvertex;
            //public Quat xquat;
            
            public suboccref(IBytestream c)
            {
                //sub_5070E0??? (called only by sub_508420)
                
                u6 = c.readInt(); e.ensure(u6,0);
                u7 = c.readInt(); e.ensure(u7,1);
                u8 = c.readInt(); e.ensure(u8,0,37842956); //only saw 27842956 once
                u9 = c.readInt(); e.ensure(u9,1,2,3,4,5,6);
                //m.msg("u9="+Integer.toString(u9));
                u10 = c.readInt();
                if(u9==5)
                {
                    //this is presumably a vertex or quaternion. Possibly a RGBA.
                    Flt x = new Flt(c);
                    Flt y = new Flt(c);
                    Flt z = new Flt(c);
                    int uc1 = c.readInt(); //seems to be 0, so probably a Flt, making this a quaternion.
                    //if(uc1!=0)
                    //{
                    //    int dummy=0;
                    //}
                    //xquat = new Quat(c);
                    int dummy=0;
                }
                else if(u9==6)
                {
                    int count = c.readInt();
                    Bstr[] sb1 = c.readArray(Bstr.class, count);
                    int dummy=0;
                }
                else if(u9==4)
                {
                    //vertex?
                    //int ua1 = c.readInt();
                    //int ua2 = c.readInt();
                    //int ua3 = c.readInt();
                    //Flt ua1 = new Flt(c);
                    //Flt ua2 = new Flt(c);
                    //Flt ua3 = new Flt(c);
                    xvertex = new Vertex(c);
                    //if(!ua1.approxequals(0))
                    //{
                    //    int dummy=0;
                    //}
                }
                else if(u9==3)
                {
                    //int u10 = c.readInt(); //e.ensure(u10,1,0,16);
                    xstr = new Bstr(c);
                    int dummy=0;
                }
                else if(u9==1)
                {
                    xint = c.readInt();
                }
                else if(u9==2)
                {
                    xfloat = new Flt(c);
                }
            
            }
        }
        public occref(IBytestream c)
        {
            //sub_508420?
            
            type = Typeid.read(c); //e.ensure(type==Typeid.occref);
            u1 = c.readInt(); e.ensure(u1,0);
            u2 = c.readInt(); e.ensure(u2,1);
            u3 = c.readInt(); //?
            u4 = c.readInt(); e.ensure(u4,0,1,4,6,7,5,37837212);
            count = c.readInt(); //e.ensure(u5,2);
            subs = c.readArray(suboccref.class, count);
            //Object[] subs = new Object[count];
            //for(int i=0;i<count;i++)
            //{
            //    subs[i] = TaggedObj.readwithtype(type, c);
            //}
            
            /*int u6 = c.readInt(); e.ensure(u6,0);
            int u7 = c.readInt(); e.ensure(u7,1);
            int u8 = c.readInt(); e.ensure(u8,0);
            int u9 = c.readInt(); e.ensure(u9,3);
            int u10 = c.readInt(); e.ensure(u10,1);
            Bstr s1 = new Bstr(c);
            int u11 = c.readInt();
            int u12 = c.readInt();
            int u13 = c.readInt();
            int u14 = c.readInt();
            int u15 = c.readInt();
            int u16 = c.readInt();*/
            int dummy=0;
        }
    }
}

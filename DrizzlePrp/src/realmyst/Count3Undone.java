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

//SceneObject?
public class Count3Undone
{
    Typeid type;
    int size;
    
    public int possibility;
    public int[] possibilities;
    
    public StringAndByte sb2;
    public StringAndByte[] sb3a;
    public int u7;
    public int u5;
    public subcount3[] subs;
    public static int numhandled=0;
    public static int numignored=0;
    
    //material, I think.
    //sub_409d40, I think, because it reads a sub_40cb70 (4x4 matrix).
    public Count3Undone(IBytestream c)
    {
        int startpos = c.getAbsoluteOffset();
        String start = "0x"+Integer.toHexString(startpos);
        type = Typeid.read(c); e.ensure(type==Typeid.count3);
        size = c.readInt();
        String end = "0x"+Integer.toHexString(startpos+size);
        
        //skip the rest.
        IBytestream d = c.Fork();
        //if(size>184)
        {
            //byte[] rawdata = c.readBytes(size-8);
            byte[] rawdata1 = c.readBytes(size-8-4);
            possibility = c.readInt(); //the texture index! -1 presumably means no texture.
            m.msg("Possibility: ",Integer.toString(possibility));
            //if(true)return;
        }
        
        //if(size>1100) return;
        //if(size>194) return;
        
        //just use a fork
        c = d;
        
        int u1 = c.readInt();  e.ensure(u1,1);
        
        sb2 = new StringAndByte(c);
        //int bytecount = c.readInt();
        //if(bytecount>256) return;
        //c.readBytes(bytecount);
        //byte endbyte = c.readByte();
        //if(true)return;
        //if(sb2.str.toString().startsWith("myst..base_mountain"))
        {
            int dummy=0;
        }
        int u3 = c.readInt(); e.ensure(u3,0,1,2);
        //if(u3==1) 
        //{
        //    StringAndByte sb3a = new StringAndByte(c);
        //    int dummy=0;
        //}
        sb3a = c.readArray(StringAndByte.class, u3);
        int u4 = c.readInt(); e.ensure(u4,3);
        u5 = c.readInt(); e.ensure(u5,0,1);
        int u6 = c.readInt(); e.ensure(u6,655,645,557,649,521,533,544,512,644,556,535,513,520,549,133,524,23,6,525,516,517,29,7,15,5,37,21,13,12,45,9,4,36,1,0,33,8,141);//e.ensure(u6,5,8,4,13,37,12,36,21,9,1,33); //13
        u7 = c.readInt(); e.ensure(u7,1,2,3,4,5,6);
        IBytestream c2 = c.Fork();
        {
            int curpos = c2.getAbsoluteOffset();
            byte[] rawdata2 = c2.readBytes(size-(curpos-startpos)-(4*u7));
            possibilities = c2.readInts(u7);
        }
        
        try{
            numignored++;
            subs = c.readArray(subcount3.class, u7);
            numignored--;
            numhandled++;
        }catch(ignore e){
            return;
        }
        
        //wrong place probably. (u6==517, has flag 512=0x200)
        //belongs after the reverse-engineered object, using flag from before it.
        //if((esiPlus120&0x20000)!=0)
        //esiPlus120 is 200c0 for bik and 60c0 for avi
        if((u6&512)!=0)
        {
            //int u300 = c.readInt(); e.ensure(u300,0,4);
            //Bstr s301 = new Bstr(c); //.bik video filename. 
            int dummy=0;
        }
        //does this go before the other one?
        if(u5==1)
        {
            int u500 = c.readInt(); e.ensure(u500,1,2);
            for(int i=0;i<u500;i++)
            {
                int u501 = c.readInt(); e.ensure(u501,0,1);
                if(u501==1)
                {
                    //these are actually floats.
                    int u510 = c.readInt();
                    int u511 = c.readInt();
                    int u512 = c.readInt();
                    int u513 = c.readInt();
                    //int u514 = c.readInt();
                    int dummy=0;
                }
                int u502 = c.readInt(); //e.ensure(u502,0x40000,0x41700000);
                int u503 = c.readInt(); e.ensure(u503,2);
                Vertex[] vertices = c.readArray(Vertex.class, 2);//u503);
                int dummy=0;
            }
            int dummy=0;
        }
        
        //int ending = c.readInt();
        int[] endings = c.readInts(u7);
        int bytesleft2 = (startpos+size)-c.getAbsoluteOffset();
        if(bytesleft2!=0)
        {
            int dummy=0;
        }
        if(true) return;
        
        
        
        /*int u25=0; Bstr s26=null;//int u26=0; Flt f27=null; Flt f28=null;
        if((esiPlus120&0x2000)!=0)
        {
            u25 = c.readInt(); e.ensure(u25,4,0);
            //u26 = c.readInt(); e.ensure(u26,8);
            //f27 = new Flt(c);
            //f28 = new Flt(c);
            s26 = new Bstr(c);
        }
        int u29=0; Bstr s30=null;
        if((esiPlus120&0x20000)!=0)
        {
            u29 = c.readInt(); e.ensure(u29,0);
            s30 = new Bstr(c);
        }
        if((esiPlus120&0x10)!=0) //16 floats; a matrix?  xform state?
        {
            Flt f31 = new Flt(c);
            //int u32 = c.readInt(); e.ensure(u32,0x80000000);
            Flt u32 = new Flt(c);
            //int u33 = c.readInt(); e.ensure(u33,0);
            Flt u33 = new Flt(c);
            Flt f34 = new Flt(c);
            //int u35 = c.readInt(); e.ensure(u35,0,0x35000000,0xc0000000);
            Flt u35 = new Flt(c);
            Flt f36 = new Flt(c);
            //int u36 = c.readInt(); e.ensure(u36,0);
            Flt u36 = new Flt(c);
            //int u37 = c.readInt(); e.ensure(u35,0);
            Flt u37 = new Flt(c);
            //int u38 = c.readInt(); e.ensure(u38,0);
            Flt u38 = new Flt(c);
            //int u39 = c.readInt(); e.ensure(u39,0x80000000);
            Flt u39 = new Flt(c);
            Flt f40 = new Flt(c);
            //int u41 = c.readInt(); e.ensure(u41,0);
            Flt u41 = new Flt(c);
            int u42 = c.readInt(); e.ensure(u42,0);
            int u43 = c.readInt(); e.ensure(u43,0);
            int u44 = c.readInt(); e.ensure(u44,0);
            Flt f45 = new Flt(c);
            int dummy=0;
        }
        if((esiPlus120&0x4000)!=0)
        {
            //u8b = c.readInt(); e.ensure(u8b,0,2,0x20,130,8,34);
        }
        
        if(u5==1)
        {
            int u100 = c.readInt(); e.ensure(u100,1);
            int u101 = c.readInt(); e.ensure(u101,0);
            int u102 = c.readInt(); e.ensure(u102,0x40000);
            int u103 = c.readInt(); e.ensure(u103,2);
            Flt f104 = new Flt(c);
            Flt f105 = new Flt(c);
            Flt f106 = new Flt(c);
            Flt f107 = new Flt(c);
            Flt f108 = new Flt(c);
            Flt f109 = new Flt(c);
        }
        
        int u24 = c.readInt(); //e.ensure(u24,-1,13,50,0,7,20,22,47,27,28,41); //50
        int bytesleft = (startpos+size)-c.getAbsoluteOffset();
        if(bytesleft!=0)
        {
            int dummy=0;
        }*/
        
    }
    
    public static class subcount3
    {
        public int esiPlus120;
        int ending;
        public Flt u14;
        public Flt u15;
        public Flt f200;
        public Flt f203;
        public Flt f16;
        public Flt f17;
        public Flt f18;
        public Flt f19;
        public Flt f20;
        public Flt f21;
        public Flt f22;
        public Flt f23;
        public Matrix m400;
        
        public static int numignored = 0;
        public static int numhandled = 0;
        
        public subcount3(IBytestream c)
        {
            esiPlus120 = c.readInt();
            numignored++;
            if((esiPlus120&0x8000)!=0) throw new ignore("ignoring...");
            numignored--;
            numhandled++;
            e.allowflags(esiPlus120,0x10,0x40,0x80,0x2000,0x4000,0x20000);//e.ensure(esiPlus120,0,0x208,0x4050,0x4090,0x4010,0x40d0,0x20040,0x200c0,0x80,0x4040,0x40c0,0x4080,0x4000,0x60c0);//e.ensure(esiPlus120,0,0x40c0,0x80,0x4000,0x4080,0x4040,0x60c0,0x200c0,0x20040,0x40d0,0x4050,0x4010,0x4090); //16576,128, 0x4000

            int u8b=0;
            if((esiPlus120&0x4000)!=0)
            {
                //u8b = c.readInt(); e.ensure(u8b,0,2,0x20,130,8,34);
            }
            int u9 = c.readInt(); e.ensure(u9,0,128,2,32,34,130,8,160);//e.ensure(u9,0,128,2,3,8,32,130,1,34,160); //128
            //if((u6&0x20)!=0)
            //{
            //    //Flt x1 = new Flt(c);
            //    int dummy=0;
            //}
            int u10 = c.readInt(); e.ensure(u10,0,3,2,1);//e.ensure(u10,0,1,0x2000,0x40,0x41,0x200,0x2040,3,2,0x4000); //1,0x2000
            int u11 = c.readInt(); e.allowflags(u11,0x1,0x2,0x4,0x20,0x40,0x80,0x100,0x200,0x400,0x800,0x2000,0x4000);//e.ensure(u11,0,0x4101,0x6040,0x4040,0x6101,0x4100,0x4200,0x204,0x2002,0x6001,0x4002,0x240,0x880,0x241,0xc81,0x4,0x5,0x141,0x102,0x61,0x480,0x101,0x4001,128,1,65,0x2000,3,320,64,2,66,0x4000,0x2001,256,512,0x2040,0x303,67,0x243,0x6000,0x201);//e.ensure(u11,0,0x2000,0x40,0x1,0x11,0x19,0x8,0x243,0x80,0x41,2,0x100,0x18,0x2040); //0x2000,0x40
            int u12 = c.readInt(); e.ensure(u12,0,33,17,25,16,1,24,8,4);//e.ensure(u12,0,16,17,8,4,1,24,128,256,32);
            int u13 = c.readInt(); e.ensure(u13,0,4,128,256,32,132);//e.ensure(u13,0,4); //sometimes a float.
            //m.msg("count3: u8b="+Integer.toString(u8b)+" u9="+Integer.toString(u9));
            //if(true)return;
            u14 = new Flt(c);
            u15 = new Flt(c);
            if((u12&0x20)!=0)
            {
                f200 = new Flt(c);
                int dummy=0;
            }
            if((esiPlus120&0x100)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x200)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x4000)!=0)
            {
                f203 = new Flt(c);
                int dummy=0;
            }
            f16 = new Flt(c);
            f17 = new Flt(c);
            f18 = new Flt(c);
            f19 = new Flt(c); //often the previous ones are all 0.0 and this is 1.0
            f20 = new Flt(c);
            f21 = new Flt(c);
            f22 = new Flt(c);
            f23 = new Flt(c);

            //Matrix m400=null;
            if((esiPlus120&0x10)!=0)
            {
                m400 = new Matrix(c);
                int dummy=0;
            }
            //if((esiPlus120&0x8000)!=0 && (esiPlus120&0x8)!=0) throw new ignore("wait til later.");
            if((esiPlus120&0x8000)!=0)
            {
                //sub_406bd0
                if(true) throw new ignore("wait til later.");
                //c.readBytes(136);
                //int dummy=0;
                
                int u600 = c.readInt();
                int u601 = c.readInt();
                int u602 = c.readInt();
                int u603 = c.readInt();
                ReverseInt r604 = new ReverseInt(c); //e.ensure(r604.convertToInt()==99099);
                ReverseInt r605 = new ReverseInt(c);
                ReverseInt r606 = new ReverseInt(c);
                int u607 = c.readInt();
                Flt f608 = new Flt(c);
                int u609 = c.readInt(); //esi+36
                Flt f610 = new Flt(c);
                int u611 = c.readInt();
                int u612 = c.readInt();
                int u613 = c.readInt(); e.ensure(u613,0);//count
                //int[] u614s = c.readInts(9*u613); //what is this struct? 8 ints and a Bstr.
                Flt[] u615s = c.readArray(Flt.class, u609);
                //possible matrix right here.
                
                int dummy=0;
            }
            if((esiPlus120&0x8)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x4)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x2)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x20)!=0)
            {
                int dummy=0;
            }
            if((esiPlus120&0x1000)!=0)
            {
                int dummy=0;
            }
            
            //wrong place probably. (u6==517, has flag 512=0x200)
            //belongs after the reverse-engineered object, using flag from before it.
            //if((esiPlus120&0x20000)!=0)
            //esiPlus120 is 200c0 for bik and 60c0 for avi
            //if((u6&512)!=0)
            int extracount=0;
            if((esiPlus120&0x20000)!=0)
            {
                int u300 = c.readInt(); e.ensure(u300,0,4);
                Bstr s301 = new Bstr(c); //.bik video filename. 
                extracount++;
                int dummy=0;
            }
            if((esiPlus120&0x2000)!=0)
            {
                int u300 = c.readInt(); e.ensure(u300,0,4);
                Bstr s301 = new Bstr(c); //.avi video filename. 
                extracount++;
                int dummy=0;
            }
            if(extracount>1)
            {
                int dummy=0;
            }
            
            
            //ending = c.readInt();
            
        }
    }
}

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
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plWaveSet7 extends uruobj
{
    plMultiModifier parent;
    Flt u1;
    public plFixedWaterState7 sub1;
    int refcount1;
    Uruobjectref[] refs1;
    int refcount2;
    Uruobjectref[] refs2;
    Uruobjectref ref3;
    Uruobjectref xref4;

    
    public plWaveSet7(context c) throws readexception
    {
        parent = new plMultiModifier(c);
        u1 = new Flt(c);
        sub1 = new plFixedWaterState7(c);
        refcount1 = c.readInt();
        refs1 = c.readArray(Uruobjectref.class, refcount1);
        refcount2 = c.readInt();
        refs2 = c.readArray(Uruobjectref.class, refcount2);
        ref3 = new Uruobjectref(c);
        //_EDI+104 = PlMultiModifier.count
        //_EDI+100 = PlMultiModifier.DWArray
        if(parent.count!=0)
        {
            if((parent.DWarray[0] & 0x10000)!=0)
            {
                //m.msg("PlWaveSet7: untested.");
                xref4 = new Uruobjectref(c);
            }
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        u1.compile(c);
        sub1.compile(c);
        c.writeInt(refcount1);
        c.writeArray2(refs1);
        c.writeInt(refcount2);
        c.writeArray2(refs2);
        ref3.compile(c);
        
        if(parent.count!=0)
        {
            if((parent.DWarray[0] & 0x10000)!=0)
            {
                xref4.compile(c);
            }
        }
    }
    public static class WaveState extends uruobj
    {
        public Flt fMaxLength;
        public Flt fMinLength;
        public Flt fAmpOverLen;
        public Flt fChop;
        public Flt fAngleDev;
        public Flt xhexisle;

        public WaveState(context c)
        {
            fMaxLength = new Flt(c);
            fMinLength = new Flt(c);
            fAmpOverLen = new Flt(c);
            fChop = new Flt(c);
            fAngleDev = new Flt(c);
            if(c.readversion==7)
            {
                m.warn("Guessing hexisle plWaveSet7 value.");
                //just guessing that the new one is at the end.
                xhexisle = new Flt(c);
            }
        }

        public void compile(Bytedeque c)
        {
            fMaxLength.compile(c);
            fMinLength.compile(c);
            fAmpOverLen.compile(c);
            fChop.compile(c);
            fAngleDev.compile(c);
        }
    }
    public static class plFixedWaterState7 extends uruobj //was waveset7sub
    {
        //Flt[] u2; //5
        //Flt[] u3; //5
        public WaveState fGeoState;
        WaveState fTexState;
        Flt u4;
        Vertex u5;
        Vertex u6;
        public Flt u7; //fWaterHeight
        public Vertex u8; //waterOffset
        Vertex u9;
        Vertex u10;
        Vertex u11;
        Flt[] u12; //25, actually seperately listed.
        public Vertex u13; //fEnvCenter
        Flt u14;
        Flt u15;
        
        public plFixedWaterState7(context c) throws readexception
        {
            //These first two things are actually objects with
            //u2 = c.readArray(Flt.class, 5);
            //u3 = c.readArray(Flt.class, 5);
            fGeoState = new WaveState(c);
            fTexState = new WaveState(c);
            u4 = new Flt(c);
            u5 = new Vertex(c);
            u6 = new Vertex(c);
            u7 = new Flt(c);
            u8 = new Vertex(c);
            u9 = new Vertex(c);
            u10 = new Vertex(c);
            u11 = new Vertex(c);
            u12 = c.readArray(Flt.class, 25); //just all in a row.
            u13 = new Vertex(c);
            u14 = new Flt(c);
            u15 = new Flt(c);
        }
        
        public void compile(Bytedeque c)
        {
            //c.writeArray(u2);
            //c.writeArray(u3);
            fGeoState.compile(c);
            fTexState.compile(c);
            u4.compile(c);
            u5.compile(c);
            u6.compile(c);
            u7.compile(c);
            u8.compile(c);
            u9.compile(c);
            u10.compile(c);
            u11.compile(c);
            c.writeArray(u12);
            u13.compile(c);
            u14.compile(c);
            u15.compile(c);
        }
    }
}

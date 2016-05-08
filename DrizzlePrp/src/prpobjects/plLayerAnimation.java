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

/**
 *
 * @author user
 */
public class plLayerAnimation extends uruobj
{
    //Objheader xheader;
    public plLayerAnimationBase parent;
    public plAnimTimeConvert tc;
    
    
    public plLayerAnimation(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plLayerAnimationBase(c);
        tc = new plAnimTimeConvert(c);

    }
    public void compile(Bytedeque data)
    {
        parent.compile(data);
        tc.compile(data);
    }
    public static class plAnimTimeConvert extends uruobj
    {
        public int flags; //was u1
        public Flt begin; //was starttime
        public Flt end; //was endtime
        public Flt loopEnd; //was u2
        public Flt loopBegin; //was u3
        public Flt speed; //was u4
        PrpTaggedObject curvecontroller1;
        PrpTaggedObject curvecontroller2;
        PrpTaggedObject curvecontroller3;
        Flt u5;
        //int u6;
        //PrpController u6;
        Double64 u6;
        int count;
        PrpTaggedObject[] others;
        int count2;
        Flt[] others2;
        //int flag;
        //Flt xu9;

        public plAnimTimeConvert(context c) throws readexception
        {
            flags = c.in.readInt();
            begin = new Flt(c);
            end = new Flt(c);
            loopEnd = new Flt(c);
            loopBegin = new Flt(c);
            speed = new Flt(c);
            curvecontroller1 = new PrpTaggedObject(c);
            curvecontroller2 = new PrpTaggedObject(c);
            curvecontroller3 = new PrpTaggedObject(c);
            u5 = new Flt(c);
            //u6 = c.in.readInt();
            //u6 = new PrpController(c);
            u6 = new Double64(c);
            count = c.in.readInt();
            others = new PrpTaggedObject[count];
            for(int i=0;i<count;i++)
            {
                others[i] = new PrpTaggedObject(c);
            }
            count2 = c.in.readInt();
            others2 = c.readArray(Flt.class, count2);
            /*flag = c.in.readInt();
            if(flag==1)
            {
                xu9 = new Flt(c);
            }*/
        }
        
        public void compile(Bytedeque c)
        {
            c.writeInt(flags);
            begin.compile(c);
            end.compile(c);
            loopEnd.compile(c);
            loopBegin.compile(c);
            speed.compile(c);
            curvecontroller1.compile(c);
            curvecontroller2.compile(c);
            curvecontroller3.compile(c);
            u5.compile(c);
            u6.compile(c);
            c.writeInt(count);
            for(int i=0;i<count;i++)
            {
                others[i].compile(c);
            }
            c.writeInt(count2);
            c.writeArray(others2);
        }
    }
    public static class plLayerAnimationBase extends uruobj
    {
        plLayerLightBase parent;
        PrpTaggedObject controller1;
        PrpTaggedObject controller2;
        PrpTaggedObject controller3;
        PrpTaggedObject controller4;
        PrpTaggedObject controller5;
        PrpTaggedObject matrixcontroller;

        public plLayerAnimationBase(context c) throws readexception
        {
            parent = new plLayerLightBase(c);
            controller1 = new PrpTaggedObject(c);
            controller2 = new PrpTaggedObject(c);
            controller3 = new PrpTaggedObject(c);
            controller4 = new PrpTaggedObject(c);
            controller5 = new PrpTaggedObject(c);
            matrixcontroller = new PrpTaggedObject(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            controller1.compile(c);
            controller2.compile(c);
            controller3.compile(c);
            controller4.compile(c);
            controller5.compile(c);
            matrixcontroller.compile(c);
        }
    }
    
    //equivalent to plLayerOr
    public static class plLayerLightBase extends uruobj
    {
        plSynchedObject parent;
        Uruobjectref ref;
        
        public plLayerLightBase(context c) throws readexception
        {
            parent = new plSynchedObject(c);//,false);
            ref = new Uruobjectref(c);
        }
        
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            ref.compile(c);
        }
    }   
}

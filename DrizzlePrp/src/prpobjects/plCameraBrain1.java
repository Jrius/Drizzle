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
//import java.util.Vector;

/**
 *
 * @author user
 */
public class plCameraBrain1 extends uruobj
{
    //Objheader xheader;
    
    Vertex POAOffset;
    Uruobjectref subjectKey;
    Uruobjectref rail;
    HsBitVector flags;
    Flt accel;
    Flt decel;
    Flt velocity;
    Flt POAAccel;
    Flt POADecel;
    Flt POAVelocity;
    Flt XPanLimit;
    Flt YPanLimit;
    Flt zoomRate;
    Flt zoomMin;
    Flt zoomMax;
    
    public plCameraBrain1(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        POAOffset = new Vertex(c);
        subjectKey = new Uruobjectref(c);
        rail = new Uruobjectref(c);
        flags = new HsBitVector(c);
        accel = new Flt(c);
        decel = new Flt(c);
        velocity = new Flt(c);
        POAAccel = new Flt(c);
        POADecel = new Flt(c);
        POAVelocity = new Flt(c);
        XPanLimit = new Flt(c);
        YPanLimit = new Flt(c);
        zoomRate = new Flt(c);
        zoomMin = new Flt(c);
        zoomMax = new Flt(c);
    }
    public void compile(Bytedeque c)
    {
        POAOffset.compile(c);
        subjectKey.compile(c);
        rail.compile(c);
        flags.compile(c);
        accel.compile(c);
        decel.compile(c);
        velocity.compile(c);
        POAAccel.compile(c);
        POADecel.compile(c);
        POAVelocity.compile(c);
        XPanLimit.compile(c);
        YPanLimit.compile(c);
        zoomRate.compile(c);
        zoomMin.compile(c);
        zoomMax.compile(c);
    }
}

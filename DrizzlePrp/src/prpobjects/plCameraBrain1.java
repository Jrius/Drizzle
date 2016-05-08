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
    
    public static final int kCutPos=0x1,
                            kCutPosOnce=0x2,
                            kCutPOA=0x4,
                            kCutPOAOnce=0x8,
                            kAnimateFOV=0x10,
                            kFollowLocalAvatar=0x20,
                            kPanicVelocity=0x40,
                            kRailComponent=0x80,
                            kSubject=0x100,
                            kCircleTarget=0x200,
                            kMaintainLOS=0x400,
                            kZoomEnabled=0x800,
                            kIsTransitionCamera=0x1000,
                            kWorldspacePOA=0x2000,
                            kWorldspacePos=0x4000,
                            kCutPosWhilePan=0x8000,
                            kCutPOAWhilePan=0x10000,
                            kNonPhys=0x20000,
                            kNeverAnimateFOV=0x40000,
                            kIgnoreSubworldMovement=0x80000,
                            kFalling=0x100000,
                            kRunning=0x200000,
                            kVerticalWhenFalling=0x400000,
                            kSpeedUpWhenRunning=0x800000,
                            kFallingStopped=0x1000000,
                            kBeginFalling=0x2000000;
    
    Vertex POAOffset;
    public Uruobjectref subjectKey;
    Uruobjectref rail;
    HsBitVector eoaFlags;
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
        if (c.readversion == 4)
            eoaFlags = new HsBitVector(c);
        flags = new HsBitVector(c);
        
        if (c.readversion == 4)
            if (flags.count != 0)
                flags = new HsBitVector(flags.get(0) | kCutPos | kCutPOA); // it seems it's necessary for all cameras...
            else
                flags = new HsBitVector(kCutPos | kCutPOA); // it seems it's necessary for all cameras...
        
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

    private plCameraBrain1() {
        
    }
    public void compile(Bytedeque c)
    {
        POAOffset.compile(c);
        subjectKey.compile(c);
        rail.compile(c);
        //eoaFlags.compile(c);
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

    public static plCameraBrain1 createDefault() {
        plCameraBrain1 e = new plCameraBrain1();
        e.POAAccel = new Flt(30f);
        e.POADecel = new Flt(30f);
        e.POAOffset = new Vertex(0f, 0f, 0f);
        e.POAVelocity = new Flt(30f);
        e.XPanLimit = new Flt(0f);
        e.YPanLimit = new Flt(0f);
        e.accel = new Flt(30f);
        e.decel = new Flt(30f);
        e.eoaFlags = new HsBitVector(0);
        e.flags = new HsBitVector(0);
        e.rail = Uruobjectref.none();
        e.subjectKey = Uruobjectref.none();
        e.velocity = new Flt(30f);
        e.zoomMax = new Flt(0f);
        e.zoomMin = new Flt(0f);
        e.zoomRate = new Flt(0f);
        return e;
    }
}

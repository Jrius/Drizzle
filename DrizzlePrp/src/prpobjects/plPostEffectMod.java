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
import shared.readexception;
//import java.util.Vector;


public class plPostEffectMod extends uruobj
{
    public plSingleModifier parent;
    public HsBitVector u1;
    public Flt hither;
    public Flt yon;
    public Flt fovX;
    public Flt fovY;
    public Uruobjectref ref;
    public Transmatrix t1;
    public Transmatrix t2;
    
    public plPostEffectMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        u1 = new HsBitVector(c);
        hither = new Flt(c);
        yon = new Flt(c);
        fovX = new Flt(c);
        fovY = new Flt(c);
        ref = new Uruobjectref(c);
        t1 = new Transmatrix(c);
        t2 = new Transmatrix(c);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        u1.compile(c);
        hither.compile(c);
        yon.compile(c);
        fovX.compile(c);
        fovY.compile(c);
        ref.compile(c);
        t1.compile(c);
        t2.compile(c);
    }
    
}

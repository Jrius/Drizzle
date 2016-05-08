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

public class plParticleCollisionEffectBounce extends uruobj
{
    //Objheader xheader;
    
    Uruobjectref sceneobject;
    Flt u1;
    Flt u2;
    
    public plParticleCollisionEffectBounce(context c) throws readexception
    {
        //if(hasHeader) xheader = new Objheader(c);
        
        sceneobject = new Uruobjectref(c);
        u1 = new Flt(c);
        u2 = new Flt(c);
    }
    public void compile(Bytedeque c)
    {
        sceneobject.compile(c);
        u1.compile(c);
        u2.compile(c);
    }
}

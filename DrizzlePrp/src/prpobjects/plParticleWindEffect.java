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

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;
import shared.*;


public class plParticleWindEffect extends uruobj
{
    Flt f1;
    Flt f2;
    Flt f3;
    byte b4;
    Vertex v5;
    Vertex v6;
    
    public plParticleWindEffect(context c) throws readexception
    {
        f1 = new Flt(c);
        f2 = new Flt(c);
        f3 = new Flt(c);
        b4 = c.readByte();
        v5 = new Vertex(c);
        v6 = new Vertex(c);
        
    }
    
    public void compile(Bytedeque c)
    {
        f1.compile(c);
        f2.compile(c);
        f3.compile(c);
        c.writeByte(b4);
        v5.compile(c);
        v6.compile(c);
    }

    public static class PlParticleUniformWind extends uruobj
    {
        plParticleWindEffect parent;
        Flt f1;
        Flt f2;
        Flt f3;

        public PlParticleUniformWind(context c) throws readexception
        {
            parent = new plParticleWindEffect(c);
            f1 = new Flt(c);
            f2 = new Flt(c);
            f3 = new Flt(c);
        }
        public void compile(Bytedeque c)
        {
            parent.compile(c);
            f1.compile(c);
            f2.compile(c);
            f3.compile(c);
        }
    }
    
}

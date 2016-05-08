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
//import java.util.Vector;
import shared.*;


public class plVehicleModifier extends uruobj
{
    plSingleModifier parent;
    Uruobjectref root;
    Tire tire1;
    Tire tire2;
    Tire tire3;
    Tire tire4;
    
    public plVehicleModifier(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        root = new Uruobjectref(c);
        tire1 = new Tire(c);
        tire2 = new Tire(c);
        tire3 = new Tire(c);
        tire4 = new Tire(c);
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        root.compile(c);
        tire1.compile(c);
        tire2.compile(c);
        tire3.compile(c);
        tire4.compile(c);
    }

    public static class Tire
    {
        Uruobjectref tire;
        Vector3 position;
        Vector3 direction;
        float radius;

        public Tire(context c) throws readexception
        {
            tire = new Uruobjectref(c);
            position = new Vector3(c.in);
            direction = new Vector3(c.in);
            radius = c.readFloat();
        }
        public void compile(Bytedeque c)
        {
            tire.compile(c);
            position.compile(c);
            direction.compile(c);
            c.writeFloat(radius);
        }
    }
    
}

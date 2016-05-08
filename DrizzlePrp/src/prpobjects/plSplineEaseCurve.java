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


public class plSplineEaseCurve extends uruobj
{
    plATCEaseCurve parent;
    float coefficient1;
    float coefficient2;
    float coefficient3;
    float coefficient4;
    
    public plSplineEaseCurve(context c) throws readexception
    {
        parent = new plATCEaseCurve(c);
        coefficient1 = c.readFloat();
        coefficient2 = c.readFloat();
        coefficient3 = c.readFloat();
        coefficient4 = c.readFloat();
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeFloat(coefficient1);
        c.writeFloat(coefficient2);
        c.writeFloat(coefficient3);
        c.writeFloat(coefficient4);
    }

    public static class plATCEaseCurve extends uruobj
    {
        float minlength;
        float maxlength;
        float length;
        float startspeed;
        float speed;
        Double64 beginworldtime;

        public plATCEaseCurve(context c)
        {
            minlength = c.readFloat();
            maxlength = c.readFloat();
            length = c.readFloat();
            startspeed = c.readFloat();
            speed = c.readFloat();
            beginworldtime = new Double64(c);
        }
        public void compile(Bytedeque c)
        {
            c.writeFloat(minlength);
            c.writeFloat(maxlength);
            c.writeFloat(length);
            c.writeFloat(startspeed);
            c.writeFloat(speed);
            beginworldtime.compile(c);
        }
    }
}

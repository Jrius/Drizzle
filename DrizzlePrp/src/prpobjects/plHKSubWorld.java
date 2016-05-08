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


public class plHKSubWorld extends uruobj
{
    plObjInterface parent;
    float f1;
    float f2;
    float f3;
    
    public plHKSubWorld(context c) throws readexception
    {
        //Pyprp reads this as a plSynchedObject, a ref, a vertex, and a 32bit int flags.
        //But that is wrong I think, and this here is correct.

        //When setting, just use all defaults and set the sceneobject in the plObjInterface to the subworld sceneobject.

        parent = new plObjInterface(c);
        f1 = c.readFloat(); //0
        f2 = c.readFloat(); //0
        f3 = c.readFloat(); //-32.17405==0xC200B23A
    }
    private plHKSubWorld(){}

    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeFloat(f1);
        c.writeFloat(f2);
        c.writeFloat(f3);
    }

    public static plHKSubWorld createWithSceneobject(Uruobjectref sceneobject)
    {
        plHKSubWorld r = new plHKSubWorld();
        r.parent = plObjInterface.createDefault(sceneobject);
        r.f1 = 0.0f;
        r.f2 = 0.0f;
        r.f3 = Flt.IntCodeToJavafloat(0xC200B23A); //-32.17405
        return r;
    }
    
}

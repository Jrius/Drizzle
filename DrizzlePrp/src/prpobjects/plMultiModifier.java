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
//import java.util.Vector;


/**
 *
 * @author user
 */
public class plMultiModifier extends uruobj
{
    //Objheader xheader;
    public plSynchedObject parent;
    public int count;
    public int[] DWarray;
    public plMultiModifier(context c)//,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plSynchedObject(c);//,false);
        count = c.in.readInt();
        DWarray = new int[count];
        for(int i=0;i<count;i++)
        {
            DWarray[i] = c.in.readInt();
        }
    }
    private plMultiModifier(){}
    public static plMultiModifier createEmtpy()
    {
        return new plMultiModifier();
    }
    public static plMultiModifier createDefault()
    {
        plMultiModifier result = plMultiModifier.createEmtpy();
        result.parent = plSynchedObject.createDefault();
        result.count = 0;
        result.DWarray = new int[0];
        return result;
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
        deque.writeInt(count);
        for(int i=0;i<count;i++)
        {
            deque.writeInt(DWarray[i]);
        }
    }

}

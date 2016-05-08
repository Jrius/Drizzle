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

import uru.Bytestream;
import uru.Bytedeque;
import java.util.Vector;
import uru.context; import shared.readexception;
import shared.readexception;
import uru.generics;

/**
 *
 * @author user
 */
//This represents uru objects that look like this: (e.g. the lights in plDrawableSpans)
//int nextcount
//while nextcount > 0
//  T[count] array
//  int nextcount
public class GrowVector<T extends uruobj> extends uruobj
{
    GrowVectorElement[] children;
    
    public GrowVector(Class<T> objclass, context c) throws readexception
    {
        Vector<GrowVectorElement> children2 = new Vector<GrowVectorElement>();
        GrowVectorElement child;
        do
        {
            child = new GrowVectorElement(objclass, c);
            children2.add(child);
        }while(child.count > 0);
        children = generics.convertVectorToArray(children2,GrowVectorElement.class);
    }
    //static public GrowVector create(Bytestream data)
    //{
    //    return new GrowVector(data);
    //}
    public void compile(Bytedeque data)
    {
        for(int i=0;i<children.length;i++)
        {
            children[i].compile(data);
        }
    }

    public class GrowVectorElement extends uruobj
    {
        int count;
        T[] elements;
        
        public GrowVectorElement(Class<T> objclass, context c) throws readexception
        {
            count = c.in.readInt();
            if(count > 0)
            {
                //elements = (T[])new Object[count]; //hack
                /*elements = (T[])java.lang.reflect.Array.newInstance(objclass, count);
                for(int i=0;i<count;i++)
                {
                    elements[i] = (T)T.create(data);
                }*/
                //elements = data.readVector(count);
                elements = c.readArray(objclass, count);
            }
        }
        //static public GrowVectorElement<S> create(Bytestream data)
        //{
        //    return new GrowVectorElement<S>(data);
        //}
        
        public void compile(Bytedeque data)
        {
            data.writeInt(count);
            if(count > 0)
            {
                data.writeArray2(elements);
            }
        }
    }
    
}

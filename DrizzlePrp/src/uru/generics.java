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

package uru;

import shared.mystobj;
import shared.m;
import java.util.Vector;
/**
 *
 * @author user
 */
public class generics
{

    public static <T> T[] convertVectorToArray(Vector<T> v, Class c)
    {
        T[] result = (T[])makeArray(c,v.size());
        for(int i=0;i<result.length;i++)
        {
            result[i] = v.get(i);
        }
        return result;
    }
    public static <T> Vector<T> convertArrayToVector(T[] arry)
    {
        //m.msg("Using untested convertArrayToVector method.");
        Vector<T> result = new Vector<T>();
        for(int i=0;i<arry.length;i++)
        {
            result.add(arry[i]);
        }
        return result;
    }
    public static <T> T[] makeArray(Class objclass, int length)
    {
        T[] result = (T[])java.lang.reflect.Array.newInstance(objclass, length);
        return result;
    }
    
    public static <T> T constructObject(Class objclass)
    {
        T result = null;
        try
        {
            java.lang.reflect.Constructor constr = objclass.getConstructor();
            result = (T)constr.newInstance();
        }
        catch(Exception e)
        {
            m.err("Unable to create generic object");
        }
        return result;
    }
    
    public static <T extends mystobj> T constructUruObject(Class objclass, Bytestream data)
    {
        T result = null;
        try
        {
            java.lang.reflect.Constructor constr = objclass.getConstructor(data.getClass());//,int.class);
            result = (T)constr.newInstance(data);
        }
        catch(Exception e)
        {
            m.err("Unable to create generic object");
        }
        return result;
    }
    
}

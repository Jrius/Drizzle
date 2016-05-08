/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import java.lang.reflect.Field;
import prpobjects.*;

public class dvObject extends dvNode
{
    //Object obj;
    //String name;
    //nodeinfo info;

    public dvObject(nodeinfo info)
    {
        this.info = info;
        //this.obj = obj;
        //this.name = name;
        //loadSelf();
    }
    public String toString()
    {
        return info.name + " ("+info.cls.getName()+")";
    }
    /*public void loadSelf()
    {
        if(obj==null)
        {
            int dummy=0;
        }

        Class c = obj.getClass();

        //overrides go here?
        if(c==PrpRootObject.class)
        {

        }

        if(c.isPrimitive())
        {

        }
    }*/
    public void loadChildren()
    {
        for(dvNode child: dvNode.findChildren(info))
        {
            children.add(child);
        }
    }
    /*public static Object[] getchildren(Object obj)
    {
        if(obj==null)
        {
            int dummy=0;
        }

        Class c = obj.getClass();


        if(c.isPrimitive())
        {
            return new Object[0];
        }
        else if(c.isArray())
        {

        }
        else
        {
            Field[] fields = c.getDeclaredFields();

        }



        return null;
    }*/
}

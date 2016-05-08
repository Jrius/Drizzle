/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Vector;
import shared.m;

public class reflect
{
    public static void printClassHierarchy(Object o)
    {
        StringBuilder s = new StringBuilder();
        for(Class cls: getClassHierarchy(o))
        {
            s.append(cls.toString()+"->");
        }
        m.status(s.toString());
    }
    public static Vector<Class> getClassHierarchy(Object o)
    {
        Vector<Class> r = new Vector();
        Class curclass = o.getClass();
        while(curclass!=null)
        {
            r.add(curclass);
            curclass = curclass.getSuperclass();
        }
        return r;
    }
}

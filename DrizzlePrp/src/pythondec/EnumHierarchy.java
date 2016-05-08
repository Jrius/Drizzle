/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec;

import java.util.EnumMap;

public class EnumHierarchy<T extends Enum<T>>
{
    //EnumMap<T,EnumMap<T,Object>> map;
    EnumMap<T,T> map;
    Class<T> cls;

    public EnumHierarchy(T[][] hierdata)
    {
        cls = hierdata[0][0].getDeclaringClass();
        map = new EnumMap(cls);

        //place immediate entries.
        for(T[] ts: hierdata)
        {
            T head = ts[0];
            for(int i=1;i<ts.length;i++)
            {
                T child = ts[i];
                //put(child,head);
                map.put(child, head);
            }
        }

        //place transient entries.

    }
    public boolean isXaY(T x, T y)
    {
        while(true)
        {
            T parent = map.get(x);
            if(parent==null) return false;  //if top of tree, then no.
            if(parent==y) return true;
            x = parent;
        }

    }
    /*private void put(T from, T to)
    {
        EnumMap<T,Object> tos = map.get(from);
        if(tos==null) tos = new EnumMap(cls);
        tos.put(to, null);
    }*/
}

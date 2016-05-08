/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.HashMap;
import shared.*;

public abstract class Enums
{

    public static class Int
    {

    }

    //assumes it's a 1-1 mapping.
    public static class ClassToVal extends Enums
    {
        private HashMap<Class,Integer> map = new HashMap();
        private HashMap<Integer,Class> backmap = new HashMap();

        public ClassToVal()
        {
        }

        public void add(Class klass, int val)
        {
            map.put(klass, val);
            backmap.put(val,klass);
        }

        public int get(Class klass)
        {
            return map.get(klass);
        }

        public Class get(int val)
        {
            return backmap.get(val);
        }

        public Class read(IBytestream c)
        {
            int val = c.readInt();
            Class r = backmap.get(val);
            if(r==null)
            {
                m.throwUncaughtException("unhandled");
            }
            return r;
        }
    }

    //assumes it's a 1 to 1 mapping.
    public static class EnumToVal extends Enums
    {
    }
}

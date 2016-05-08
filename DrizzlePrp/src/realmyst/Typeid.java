/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;

public enum Typeid
{
    
    unknown,
    sdbstart,
    count10,
    occref,
    count11,
    count9,
    count9ref,
    //ref,
    //ref2,
    mdb,
    count2,
    count3,
    count4,
    count5,
    count6,
    count7,
    count8,
    hasthing,
    ;
    public static pair[] pairs = {
        p(0x9469DF4E, sdbstart),
        p(0x33571FBE, count10),
        p(0x000f3e5c, occref),
        p(0x41DE0279, count11),
        p(0x3A5B6799, count9),
        p(0x000f3e59, count9ref),
        //p(0x000003e9, ref),
        //p(0x000003ea, ref2), //used in count9
        p(0x529A6D54, mdb),
        p(0x77bd62c4, count2),
        p(0x7440708d, count3),
        p(0x0ced663e, count6),
        p(0x1b9575cf, count8),
        p(0x656d2f93, count5),
        p(0x75e35755, count7),
        p(0x3e9b13da, hasthing),
        p(0x1b570365, count4),
    };
    
    
    public static Typeid read(IBytestream c)
    {
        int data = c.readInt();
        Typeid result = getType(data);
        if(result==Typeid.unknown)
        {
            int error=0;
        }
        return result;
    }
    public static boolean has(int i)
    {
        for(pair p: pairs)
        {
            if(p.i==i) return true;
        }
        return false;
    }
    public static pair p(int i, Typeid type)
    {
        return new pair(i,type);
    }
    public static class pair
    {
        int i;
        Typeid type;
        
        public pair(int i, Typeid type)
        {
            this.i = i;
            this.type = type;
        }
    }
    
    public int getInt()
    {
        for(pair p: pairs)
        {
            if(p.type==this)
            {
                return p.i;
            }
        }
        return -1;
    }
    public static int getInt(Typeid type)
    {
        return type.getInt();
    }
    public static Typeid getType(int i)
    {
        for(pair p: pairs)
        {
            if(p.i==i)
            {
                return p.type;
            }
        }
        m.warn("Unhandled type in Typeid.");
        return unknown;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import shared.e;

public class Shp
{
    ReverseInt ver1;
    ReverseInt ver2;
    ReverseInt ver3;
    
    //these aren't used.
    int u1;
    int u2;
    int u3;
    int u4;
    
    //sub_4d1130
    int v3;
    int v35;
    ShpInfo1[] infos;

    public Shp(IBytestream c) //sub4cf330
    {
        //this block is sub4d24a0, called only from function sub4cf330
        //these 3 ints must be these 3 values according to the binary.
        ver1 = new ReverseInt(c);
        ver2 = new ReverseInt(c);
        ver3 = new ReverseInt(c);
        e.ensure(ver1.convertToInt()==123456789);
        e.ensure(ver2.convertToInt()==4);
        e.ensure(ver3.convertToInt()==0);
        //these 4 ints appear to be ignored.  always 0?
        u1 = c.readInt();
        u2 = c.readInt();
        u3 = c.readInt();
        u4 = c.readInt();

        //sub4d1130
        v3 = c.readInt();
        v35 = c.readInt();
        infos = new ShpInfo1[v3];
        for(int i=0;i<v3;i++)
        {
            //does something???
            infos[i] = new ShpInfo1(c);
        }
        int counter = v3>0?v3:v35; //this seems to be what the decomilation shows.
        for(int i=0;i<counter;i++)
        {
            
        }
    }

    public static class ShpInfo1
    {
        int maynotbeint1;
        Bstr name;
        Flt maynotbeint2;

        public ShpInfo1(IBytestream c)
        {
            maynotbeint1 = c.readInt();
            name = new Bstr(c);
            maynotbeint2 = new Flt(c);
        }
    }
}

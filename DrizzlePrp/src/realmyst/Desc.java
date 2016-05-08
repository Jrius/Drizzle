/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;

public class Desc
{
    Bstr name;
    byte b1;
    int u2;
    int u3;
    int u4;
    int u5;
    int u6;
    int u7;
    int u8;
    
    public Desc(IBytestream c)
    {
        name = new Bstr(c);
        b1 = c.readByte();
        if(b1!=3)
        {
            int dummy=0;
        }
        u2 = c.readInt();
        u3 = c.readInt();
        u4 = c.readInt();
        u5 = c.readInt();
        u6 = c.readInt();
        u7 = c.readInt();
        u8 = c.readInt();
    }
}

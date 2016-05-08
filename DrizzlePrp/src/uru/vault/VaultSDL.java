/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import shared.*;
import prpobjects.Urustring;

public class VaultSDL
{
    public VaultSDL(IBytestream c, int readversion)
    {
        //If work continues on this, see sdlbinary.cpp of Alcugs.
        byte b1 = c.readByte(); e.ensure(b1==0);//0
        byte b2 = c.readByte(); e.ensure(b2==(byte)0x80);//0x80
        Urustring age = new Urustring(c, readversion);
        if(age.toString().equals("Neighborhood"))
        {
            int dummy=0;
        }
        int version = c.readInt();
        byte b4 = c.readByte(); e.ensure(b4==6);
        byte b5 = c.readByte();
        int count = b.ByteToInt32(b5);
        if(count>=89)
        {
            for(int i=0;i<count;i++)
            {
                byte cur = c.readByte();
                if(i+1!=cur)
                {
                    int dummy=0;
                }
                //bool 020000f008
                c.readBytes(5);
            }
        }
        else
        {
            for(int i=0;i<count;i++)
            {
                //bool 020000f008
                c.readBytes(5);
            }
        }
        byte end = c.readByte(); //0
        if(c.getBytesRemaining()!=0)
        {
            int dummy=0;
        }
    }
}

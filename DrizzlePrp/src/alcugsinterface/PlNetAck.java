/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alcugsinterface;

import shared.*;

public class PlNetAck
{
    short u1;
    sub[] subs;
    
    public PlNetAck(IBytestream c, Packet p)
    {
        u1 = c.readShort();
        subs = c.readArray(sub.class, p.dataSize);
    }
    
    public static class sub
    {
        byte fr_n; //fragment number to ack
        Int24 sn; //packet number to confirm
        int unknownA;
        byte fr_ack; //previous acked fragment number
        Int24 ps; //previous confirmed packet number
        int unknownB;
        
        public sub(IBytestream c)
        {
            fr_n = c.readByte();
            sn = new Int24(c);
            unknownA = c.readInt();
            fr_ack = c.readByte();
            ps = new Int24(c);
            unknownB = c.readInt();
        }
    }
}

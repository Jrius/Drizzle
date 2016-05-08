/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alcugsinterface;

import shared.ByteArrayBytestream;
import shared.m;
import shared.Int24;
import shared.b;

public class parser
{
    public static void testmsg(String msg)
    {
        byte[] data = b.HexStringToBytes(msg);
        //parse(data);
        Packet p = new Packet(data);
    }
    
}

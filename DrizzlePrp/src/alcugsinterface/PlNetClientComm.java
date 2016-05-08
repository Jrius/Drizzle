/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alcugsinterface;

import shared.*;

public class PlNetClientComm
{
    int bandwidth;
    int cdate;
    int microseconds;
    
    public PlNetClientComm(IBytestream c)
    {
        bandwidth = c.readInt();
        cdate = c.readInt();
        microseconds = c.readInt();
    }
}

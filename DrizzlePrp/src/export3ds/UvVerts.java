/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class UvVerts extends tdsobj
{

    public short numCoords;
    public FltPair[] coords;
    
    private UvVerts(){}
    
    public static UvVerts create(FltPair[] coords)
    {
        UvVerts result = new UvVerts();
        result.numCoords = check.intToShort(coords.length);
        result.coords = coords;
        
        //add dummy:
        //result.numCoords += 1;
        //FltPair[] newcoords = new FltPair[coords.length];
        //newcoords[0] = FltPair.createFromFloats(0, 0);
        //for(int i=0;i<coords.length;i++)
        //{
        //    newcoords[i+1] = coords[i];
        //}
        //result.coords = newcoords;
        
        return result;
    }
    
    public Typeid type(){return Typeid.uvcoords;}

    public void innercompile(IBytedeque c)
    {
        c.writeShort(numCoords);
        c.writeArray(coords);
    }
    
}

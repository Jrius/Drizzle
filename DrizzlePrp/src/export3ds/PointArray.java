/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class PointArray extends tdsobj
{
    public short numpoints;
    public Vertex[] vertices;
    
    private PointArray(){}
    
    public static PointArray create(Vertex[] vertices)
    {
        PointArray result = new PointArray();
        if(vertices.length>30000)
        {
            throw new uncaughtexception("Too many vertices(this could be set higher, check it out.)");
        }
        result.numpoints = (short)vertices.length;
        result.vertices = vertices;
        return result;
    }
    public Typeid type(){return Typeid.pointarray;}
    public void innercompile(IBytedeque c)
    {
        c.writeShort(numpoints);
        c.writeArray(vertices);
    }
}

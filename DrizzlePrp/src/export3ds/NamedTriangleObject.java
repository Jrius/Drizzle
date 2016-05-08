/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class NamedTriangleObject extends tdsobj
{
    public PointArray points;
    public FaceArray faces;
    
    public UvVerts uvcoords;
    
    private NamedTriangleObject(){}
    
    public static NamedTriangleObject createNull()
    {
        NamedTriangleObject result = new NamedTriangleObject();
        return result;
    }
    public Typeid type(){return Typeid.namedtriobj;}
    public void innercompile(IBytedeque c)
    {
        points.compile(c);
        faces.compile(c);
        if(uvcoords!=null) uvcoords.compile(c);
    }
}

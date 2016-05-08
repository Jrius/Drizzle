/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Vector3
{
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(IBytestream c)
    {
        x = c.readFloat();
        y = c.readFloat();
        z = c.readFloat();
    }
    public void compile(IBytedeque c)
    {
        c.writeFloat(x);
        c.writeFloat(y);
        c.writeFloat(z);
    }

    public Vector3 getInverse()
    {
        return new Vector3(-x,-y,-z);
    }
    public Vector3 getAdd(Vector3 v2)
    {
        return new Vector3(x+v2.x,y+v2.y,z+v2.z);
    }
    public Vector3 getSubtract(Vector3 v2)
    {
        return new Vector3(x-v2.x,y-v2.y,z-v2.z);
    }
    public float getDotProduct(Vector3 v2)
    {
        return x*v2.x + y*v2.y + z*v2.z;
    }
    public float getLength()
    {
        float f = x*x + y*y + z*z;
        return (float)Math.sqrt(f);
    }
    public float getDistance(Vector3 v2)
    {
        return getSubtract(v2).getLength();
    }
    public Vector3 getNormalized()
    {
        float length = getLength();
        return new Vector3(x/length, y/length, z/length);
    }
    public String toString()
    {
        return Float.toString(x)+", "+Float.toString(y)+", "+Float.toString(z);
    }

}

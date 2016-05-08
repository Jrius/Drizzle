/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class RGBA
{
    public float r;
    public float g;
    public float b;
    public float a;

    public RGBA(){}
    public RGBA(IBytestream c)
    {
        r = c.readFloat();
        g = c.readFloat();
        b = c.readFloat();
        a = c.readFloat();
    }
    public void compile(IBytedeque c)
    {
        c.writeFloat(r);
        c.writeFloat(g);
        c.writeFloat(b);
        c.writeFloat(a);
    }
    public RGBA(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public String toString()
    {
        return "r="+Float.toString(r)+" g="+Float.toString(g)+" b="+Float.toString(b)+" a="+Float.toString(a);
    }
}

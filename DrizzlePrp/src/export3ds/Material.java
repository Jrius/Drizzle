/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class Material extends tdsobj
{
    public MaterialName name;
    public MatAmbient ambient;
    public MatDiffuse diffuse;
    public MatSpecular specular;
    
    public TextureMap texturemap;
    
    
    private Material(){}
    
    public static Material create(String name)
    {
        Material result = new Material();
        result.name = MaterialName.create(name);
        //just use the same colors Blender defaults to.
        result.ambient = MatAmbient.create((byte)102,(byte)102,(byte)102);
        result.diffuse = MatDiffuse.create((byte)204,(byte)204,(byte)204);
        result.specular = MatSpecular.create((byte)255,(byte)255,(byte)255);
        result.texturemap = null;
        return result;
    }
    public Typeid type(){return Typeid.material;}
    public void innercompile(IBytedeque c)
    {
        name.compile(c);
        ambient.compile(c);
        diffuse.compile(c);
        specular.compile(c);
        if(texturemap!=null) texturemap.compile(c);
    }
}

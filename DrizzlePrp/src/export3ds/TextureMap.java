/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class TextureMap extends tdsobj
{
    
    public TextureFilename texturefilename;
    
    public UOffset uoffset;
    public VOffset voffset;
    public UScale uscale;
    public VScale vscale;

    private TextureMap(){}
    
    public static TextureMap create(String filename)
    {
        TextureMap result = new TextureMap();
        result.texturefilename = TextureFilename.create(filename);
        return result;
    }
    
    public Typeid type(){return Typeid.texturemap;}

    public void innercompile(IBytedeque c)
    {
        texturefilename.compile(c);

        if(uoffset!=null) uoffset.compile(c);
        if(voffset!=null) voffset.compile(c);
        if(uscale!=null) uscale.compile(c);
        if(vscale!=null) vscale.compile(c);
    }
    
}

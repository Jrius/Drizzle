/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;

public class TextureFilename extends tdsobj
{
    public Ntstring filename;
    
    private TextureFilename(){}
    
    public static TextureFilename create(String filename)
    {
        TextureFilename result = new TextureFilename();
        result.filename = Ntstring.createFromString(filename);
        return result;
    }
    
    public Typeid type(){return Typeid.texturefilename;}

    public void innercompile(IBytedeque c)
    {
        filename.compile(c);
    }
    
}

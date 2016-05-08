/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.vault;

import shared.*;
import java.io.File;

public class vfile
{
    public int u1;
    public short u2;
    public FixedLengthString s3;
    public Node node;
            
    private vfile(){}
    
    public static vfile createFromFilename(String filename)
    {
        vfile result = new vfile();
        
        byte[] bytes = shared.FileUtils.ReadFile(filename);
        byte[] data = shared.zip.decompressGzip(bytes);
        IBytestream c = ByteArrayBytestream.createFromByteArray(data);
        c.sourceName = new File(filename).getName();
        
        if(c.sourceName.equals("0000D080.48F7F841.000CF8BE.v"))
        {
            int dummy=0;
        }
        
        //header
        result.u1 = c.readInt(); //7
        result.u2 = c.readShort(); //16
        //int u3 = c.readInt();
        //int u4 = c.readInt();
        //int u5 = c.readInt();
        //int u6 = c.readInt();
        result.s3 = new FixedLengthString(c,16);
        
        result.node = new Node(c);
        
        int bytesleft = c.getBytesRemaining();
        if(bytesleft!=0)
        {
            m.err("Node has unexpected length.");
        }
        
        return result;
    }
}

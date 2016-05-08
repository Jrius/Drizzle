/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images;

import shared.*;
import images.Image.Dxt;

public class dds
{
    public static void createFromDxt(IBytedeque c, Dxt dxt)
    {
        int compressionType;
        if(dxt.texelsize==8) //DXT1
        {
            compressionType = 1;
        }
        else if(dxt.texelsize==16) //DXT5
        {
            compressionType = 5;
        }
        else
        {
            throw new uncaughtexception("Unhandled texelsize in dds class.");
        }
        
        c.writeBytes(new byte[]{'D','D','S',' '});
        c.writeInt(124);
        c.writeInt(0x000a1007);
        c.writeInt(dxt.texheight);
        c.writeInt(dxt.texwidth);
        if(compressionType==1) //DXT1
        {
            c.writeInt(dxt.texheight*dxt.texwidth/2);
        }
        else if(compressionType==5) //DXT5
        {
            c.writeInt(dxt.texheight*dxt.texwidth);
        }
        
        c.writeInt(0);
        c.writeInt(dxt.numLevels);
        for(int i=0;i<11;i++)
        {
            c.writeInt(0);
        }
        c.writeInt(32);
        c.writeInt(0x00000004);
        if(compressionType==1)
        {
            c.writeBytes(new byte[]{'D','X','T','1'});
        }
        else if(compressionType==5)
        {
            c.writeBytes(new byte[]{'D','X','T','5'});
        }
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        
        c.writeInt(0x00401008);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        
        //for(int i=0;i<dxt.numLevels;i++)
        //{
        //    Dxt.Level curlevel = dxt.levels[i];
        //    curlevel.
        //}
        
        dxt.compile(c);
        
    }
    public static void createFromUncompressed(IBytedeque c, byte[][] leveldata, int width, int height)
    {
        int numlevels = leveldata.length;
        
        c.writeBytes(new byte[]{'D','D','S',' '});
        c.writeInt(124);
        c.writeInt(0x0000100f);
        c.writeInt(height);
        c.writeInt(width);
        c.writeInt(width*4);
        c.writeInt(0);
        c.writeInt(numlevels);
        for(int i=0;i<11;i++)
        {
            c.writeInt(0);
        }
        c.writeInt(32);
        c.writeInt(0x00000041);
        c.writeInt(0);
        c.writeInt(32);
        c.writeInt(0x00ff0000); //red mask
        c.writeInt(0x0000ff00); //green mask
        c.writeInt(0x000000ff); //blue mask
        c.writeInt(0xff000000); //alpha mask
        c.writeInt(0x00001000);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        c.writeInt(0);
        
        for(int i=0;i<numlevels;i++)
        {
            byte[] curleveldata = leveldata[i];
            c.writeBytes(curleveldata);
        }
        
    }
}

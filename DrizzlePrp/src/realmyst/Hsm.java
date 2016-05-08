/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;

public class Hsm
{
    public String name="(unnamed)";
    
    public int u1;
    public int flagsMaybe; //flags
    public int widthMaybe; //width or maybe height
    public int heightMaybe; //height or maybe width
    public byte b5;
    public int u6;
    public int numLevels; //num levels?
    public int u8;
    public int bytesPerBlock; //bytes per 4x4 DXT block
    public int dxtType; //DXT type 1=DXT1, 5=DXT5
    
    public images.Image.Dxt dxt;
    public byte[][] xagrb;
    
    public int getCompressionType()
    {
        if((u6&0x20000000)!=0)
        {
            return 0;
        }
        return dxtType;
    }
    
    public Hsm(IBytestream c, String name)
    {
        this.name = name;
        
        u1 = c.readInt(); e.ensure(u1,1);
        flagsMaybe = c.readInt(); e.ensure(flagsMaybe,48,49,176,117,53,145,50,113,241,17,52,114,112,177,80,54,242,81,144,213,245); //145
        widthMaybe = c.readInt();
        heightMaybe = c.readInt();
        b5 = c.readByte(); e.ensure((int)b5,0,-128,64); //-128
        u6 = c.readInt(); e.ensure(u6,0,0x20000000,0x20000002,0x20000001,0x20000004); //0x20000000
        numLevels = c.readInt();
        if((u6&0x20000000)!=0)//(u6==0x20000000||u6==0x20000002||u6==0x20000001||u6==0x20000004) //is this the right way to check this? Or should be check if b5==-128 or flagsMaybe==125?
        {
            //uncompressed.
            try
            {
                //dxt = new uru.moulprp.Image.Dxt(c, numLevels, widthMaybe, heightMaybe, ???);
                xagrb = new byte[numLevels][];
                for(int i=0;i<numLevels;i++)
                {
                    int levelsize = (widthMaybe >>> i)*(heightMaybe >>> i);
                    xagrb[i] = c.readBytes(levelsize*4);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                throw new uncaughtexception("Error in Dxt parsing.");
            }
            
            //IBytedeque out = new Bytedeque2();
            //images.dds.createFromUncompressed(out, xagrb, widthMaybe, heightMaybe);
            //byte[] outdata = out.getAllBytes();
            //FileUtils.WriteFile("c:/testimage.dds", outdata);
        }
        else
        {
            u8 = c.readInt(); e.ensure(u8,1); //0
            bytesPerBlock = c.readInt();
            dxtType = c.readInt();
            if(dxtType==1)
            {
                try{
                    dxt = new images.Image.Dxt(c, numLevels, widthMaybe, heightMaybe, (byte)bytesPerBlock);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new uncaughtexception("Error in Dxt parsing.");
                }
            }
            else if(dxtType==5)
            {
                try{
                    dxt = new images.Image.Dxt(c, numLevels, widthMaybe, heightMaybe, (byte)bytesPerBlock);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new uncaughtexception("Error in Dxt parsing.");
                }
            }
            else
            {
                m.err("Unhandled dxtType.");
            }
        }
        int dummy = 0;
    }
}

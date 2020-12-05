/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images;

import shared.Bytes;
import shared.e;
import shared.m;
import uru.Bytestream;
import shared.b;
import uru.Bytedeque;
import shared.readexception;
import shared.IBytestream;
import shared.IBytedeque;

/**
 *
 * @author user
 */
public class Image
{
    public static class Dxt
    {
        public Level[] levels;
        public int numLevels;
        public int texwidth;
        public int texheight;
        public byte texelsize;
        
        public static class Level
        {
            public Texel[][] texels;
            
            public int numTexelsWide;
            public int numTexelsHigh;
            public int width;
            public int height;
            
            //int[] extraPixels;
            public int[][] extraPixels;
            
            public Level(IBytestream data, int width, int height, int texelsize) throws readexception
            {
                this.width = width;
                this.height = height;
                numTexelsWide = width/4;
                numTexelsHigh = height/4;
                if(numTexelsWide==0 || numTexelsHigh==0)
                {
                    if(width==0 || height==0)
                    {
                        throw new readexception("Dxt: Unhandled case.");
                    }
                    else
                    {
                        extraPixels = data.readMultiDimensionInts(width, height);
                    }
                }
                else
                {
                    texels = new Texel[numTexelsHigh][numTexelsWide];
                    for(int i=0;i<numTexelsHigh;i++)
                    {
                        for(int j=0;j<numTexelsWide;j++)
                        {
                            texels[i][j] = new Texel(data, texelsize);
                        }
                    }
                }
            }
            
            public void invert()
            {
                if(texels==null)
                {
                    int[][] tempPixels = new int[height][];
                    for(int i=0;i<height;i++)
                    {
                        tempPixels[i] = extraPixels[height-i-1];
                    }
                    extraPixels = tempPixels;
                }
                else
                {
                    //invert each texel
                    for(int i=0;i<numTexelsHigh;i++)
                    {
                        for(int j=0;j<numTexelsWide;j++)
                        {
                            texels[i][j].invert();
                        }
                    }

                    //invert the texel order.
                    Texel[][] newtexels = new Texel[numTexelsHigh][];
                    for(int i=0;i<numTexelsHigh;i++)
                    {
                        newtexels[i] = texels[numTexelsHigh-i-1];
                    }
                    texels = newtexels;
                }
            }
            
            public void rotate90clockwise()
            {
                if(texels==null)
                {
                    int[][] tempPixels = b.rotateIntGridClockwise(extraPixels);
                    extraPixels = tempPixels;
                }
                else
                {
                    //invert each texel
                    for(int i=0;i<numTexelsHigh;i++)
                    {
                        for(int j=0;j<numTexelsWide;j++)
                        {
                            texels[i][j].rotate90clockwise();
                        }
                    }

                    //invert the texel order.
                    Texel[][] tempTexels = new Texel[texels.length][texels[0].length];
                    b.rotateGridClockwise(texels, tempTexels);
                    texels = tempTexels;
                }
            }
            
            public void compile(IBytedeque c)
            {
                if(texels==null)
                {
                    c.writeMultiDimensionInts(extraPixels);
                }
                else
                {
                    for(int i=0;i<numTexelsHigh;i++)
                    {
                        for(int j=0;j<numTexelsWide;j++)
                        {
                            texels[i][j].compile(c);
                        }
                    }
                }
            }
            
        }
        
        public static class Texel
        {
            byte[] rawdata;
            
            public Texel(IBytestream data, int texelsize)
            {
                rawdata = data.readBytes(texelsize);
            }
            public void movebits(int count, byte[] from, byte[] to, int fromoffset, int tooffset)
            {
                for(int i=0;i<count;i++)
                {
                    int frombytenum = (fromoffset+i) / 8;
                    int posInByte = (fromoffset+i) % 8;
                    if((from[frombytenum]&(0x1<<posInByte))!=0)
                    {
                        int tobytenum = (tooffset+i) / 8;
                        int pos2InByte = (tooffset+i) % 8;
                        to[tobytenum] = b.or(to[tobytenum],0x1<<pos2InByte); //set the bit
                    }
                }
            }
            public void invert()
            {
                if(rawdata.length==8)
                {
                    //DXT1
                    //byte a = rawdata[4];
                    int a = b.BytesToInt32(rawdata, 4);
                    //reverse the x-direction.
                    //int a2 = ((a&0xC0C0C0C0)>>>6) | ((a&0x30303030)>>>2) | ((a&0x0C0C0C0C))<<2 | ((a&0x03030303)<<6) ;
                    //reverse the y-direction.
                    int a2 = ((a&0x000000FF)<<24) | ((a&0xFF000000)>>>24) | ((a&0x00FF0000)>>>8) | ((a&0x0000FF00)<<8);
                    b.Int32IntoBytes(a2, rawdata, 4);
                }
                else if(rawdata.length==16)
                {
                    //DXT5 (DXT3 is also possible and works differntly than this, but I don't think Cyan uses it.  There is an ensure in plBitmap against it.
                    
                    //RGB data is same as DXT1...
                    int a = b.BytesToInt32(rawdata, 12);
                    int a2 = ((a&0x000000FF)<<24) | ((a&0xFF000000)>>>24) | ((a&0x00FF0000)>>>8) | ((a&0x0000FF00)<<8);
                    b.Int32IntoBytes(a2, rawdata, 12);
                    
                    //Alpha data... skip the first 16 bits, move 3 bits at at time.
                    //0..15:ignore  16..27:row0  28..39:row1  40..51:row2  52..63:row3
                    byte[] newrawdata = new byte[16];
                    this.movebits(16,rawdata, newrawdata,0,0);
                    this.movebits(64,rawdata, newrawdata,64,64);
                    this.movebits(3*4,rawdata,newrawdata,16,52);
                    this.movebits(3*4,rawdata,newrawdata,52,16);
                    this.movebits(3*4,rawdata,newrawdata,28,40);
                    this.movebits(3*4,rawdata,newrawdata,40,28);
                    //long c = b.BytesToInt64(rawdata, 0);
                    //long c2 = ((c&0xFFF00)<<32) | ((c&0xFFF0000000000));
                    //b.Int64IntoBytes(c2, rawdata, 0);
                    
                    rawdata = newrawdata;
                }
            }
            
            public void rotate90clockwise()
            {
                if(rawdata.length==8)
                {
                    int a = b.BytesToInt32(rawdata, 4);
                    //           0               2              4                  6               8                  10               12                 14                  16                   18               20                 22                    24                      26                     28                       30
                    int a2 = ((a&0x3)<<6) | ((a&0xC)<<12) | ((a&0x30)<<18) | ((a&0xC0)<<24) | ((a&0x300)>>>4) | ((a&0xC00)<<2) | ((a&0x3000)<<8) | ((a&0xC000)<<14) | ((a&0x30000)>>>14) | ((a&0xC0000)>>>8) | ((a&0x300000)>>>2) | ((a&0xC00000)<<4) | ((a&0x3000000)>>>24) | ((a&0xC000000)>>>18) | ((a&0x30000000)>>>12) | ((a&0xC0000000)>>>6);
                    //int a2 = ((a&0x000000FF)<<8) | ((a&0x0000FF00)<<8) | ((a&0x00FF0000)<<8) | ((a&0xFF000000)<<8);
                    b.Int32IntoBytes(a2, rawdata, 4);
                }
                else if(rawdata.length==16)
                {
                    //DXT5
                    
                    //handle the RGB data...
                    int a = b.BytesToInt32(rawdata, 12);
                    //           0               2              4                  6               8                  10               12                 14                  16                   18               20                 22                    24                      26                     28                       30
                    int a2 = ((a&0x3)<<6) | ((a&0xC)<<12) | ((a&0x30)<<18) | ((a&0xC0)<<24) | ((a&0x300)>>>4) | ((a&0xC00)<<2) | ((a&0x3000)<<8) | ((a&0xC000)<<14) | ((a&0x30000)>>>14) | ((a&0xC0000)>>>8) | ((a&0x300000)>>>2) | ((a&0xC00000)<<4) | ((a&0x3000000)>>>24) | ((a&0xC000000)>>>18) | ((a&0x30000000)>>>12) | ((a&0xC0000000)>>>6);
                    //int a2 = ((a&0x000000FF)<<8) | ((a&0x0000FF00)<<8) | ((a&0x00FF0000)<<8) | ((a&0xFF000000)<<8);
                    b.Int32IntoBytes(a2, rawdata, 12);
                    
                    //handle the Alpha data...
                    //Alpha data... skip the first 16 bits, move 3 bits at at time.
                    //0..15:ignore  16..27:row0  28..39:row1  40..51:row2  52..63:row3
                    byte[] newrawdata = new byte[16];
                    this.movebits(16,rawdata, newrawdata,0,0);
                    this.movebits(64,rawdata, newrawdata,64,64);
                    this.movebits(3,rawdata,newrawdata,16,25);
                    this.movebits(3,rawdata,newrawdata,28,22);
                    this.movebits(3,rawdata,newrawdata,40,19);
                    this.movebits(3,rawdata,newrawdata,52,16);
                    this.movebits(3,rawdata,newrawdata,19,37);
                    this.movebits(3,rawdata,newrawdata,31,34);
                    this.movebits(3,rawdata,newrawdata,43,31);
                    this.movebits(3,rawdata,newrawdata,55,28);
                    this.movebits(3,rawdata,newrawdata,22,49);
                    this.movebits(3,rawdata,newrawdata,34,46);
                    this.movebits(3,rawdata,newrawdata,46,43);
                    this.movebits(3,rawdata,newrawdata,58,40);
                    this.movebits(3,rawdata,newrawdata,25,61);
                    this.movebits(3,rawdata,newrawdata,37,58);
                    this.movebits(3,rawdata,newrawdata,49,55);
                    this.movebits(3,rawdata,newrawdata,61,52);
                    
                    rawdata = newrawdata;
                }
            }
            
            public void compile(IBytedeque c)
            {
                c.writeBytes(rawdata);
            }
        }
        
        public Dxt(IBytestream data, int numLevels, int texwidth, int texheight, byte texelsize) throws readexception
        {
            //e.ensure(texelsize==8); //DXT1
            e.ensure((texwidth&3)==0); //divisible by 4
            e.ensure((texheight&3)==0); //divisible by 4
            
            this.numLevels = numLevels;
            this.texwidth = texwidth;
            this.texheight = texheight;
            this.texelsize = texelsize;
            
            levels = new Level[numLevels];
            
            for(int curLevel=0;curLevel<numLevels;curLevel++)
            {
                int levelwidth = texwidth >>> curLevel;
                int levelheight = texheight >>> curLevel;
                
                if (levelwidth == 0 || levelheight == 0)
                {
                    // some extra mip levels with null width or height... this happens with Korman sometimes.
                    // I have no idea how much data we're supposed to read in this case, so
                    // we'll just delete those mip levels and call it a day.
                    // (this will display a warning in Drizzle but should be harmless.)
                    this.numLevels = curLevel - 1;
                    Level[] tmpLevels = new Level[this.numLevels];
                    System.arraycopy(levels, 0, tmpLevels, 0, tmpLevels.length);
                    levels = tmpLevels;
                    break;
                }
                else
                    levels[curLevel] = new Level(data, levelwidth, levelheight, texelsize);
                /*int levelsize; //number of bytes of raw data.
                
                if(levelwidth<3 || levelheight<3)
                {
                    m.err("Image convert not implemented.");
                    levelsize = levelwidth * levelheight * 4; //not enough for a texel, so just use raw data.
                    
                }
                else
                {
                    levelsize = levelwidth * levelheight * Bytes.ByteToInt32(texelsize) / 16; //16 pixels per texel.
                    number o
                }*/
                
            }
        }
        
        public void invert()
        {
            if(texelsize!=8&&texelsize!=16)
            {
                m.err("Inversion of non-DXT1/DXT5 not currently supported.");
                return;
            }
            for(int i=0;i<numLevels;i++)
            {
                levels[i].invert();
            }
        }
        
        public void rotate90clockwise()
        {
            if(texelsize!=8&&texelsize!=16)
            {
                m.err("Rotation of non-DXT1/DXT5 not currently supported.");
                return;
            }
            for(int i=0;i<numLevels;i++)
            {
                levels[i].rotate90clockwise();
            }
        }
        
        public void compile(IBytedeque c)
        {
            for(int curLevel=0;curLevel<numLevels;curLevel++)
            {
                levels[curLevel].compile(c);
            }            
        }
    
        public int getDxtType()
        {
            if(texelsize==16) return 5;
            if(texelsize==8) return 1;
            throw new shared.uncaughtexception("unhandled.");
        }

    }
    
    public static class Agrb
    {
        public byte[][][][] image; //[level][row][column][subpixel]
        public int levels;
        public int texwidth;
        public int texheight;
        
        
        public Agrb(byte[] data, int levels, int texwidth, int texheight) throws readexception
        {
            this.levels = levels;
            this.texwidth = texwidth;
            this.texheight = texheight;
            
            image = new byte[levels][][][];
            int pos = 0;
            for(int level=0;level<levels;level++)
            {
                int width = texwidth >>> level;
                int height = texheight >>> level;
                int bytesPerRow = width*4;
                image[level] = new byte[height][width][4];
                for(int row=0;row<height;row++)
                {
                    for(int col=0;col<width;col++)
                    {
                        for(int i=0;i<4;i++)
                        {
                            image[level][row][col][i] = data[pos];
                            pos++;
                        }
                    }
                }
            }
            
            throw new readexception("Argb is untested: throwing error.");
        }
        
        public void invert()
        {
            for(int level=0;level<levels;level++)
            {
                int width = texwidth >>> level;
                int height = texheight >>> level;
                int bytesPerRow = width*4;
                byte[][][] newimage = new byte[height][][];
                for(int row=0;row<height;row++)
                {
                    newimage[row] = image[level][height-row-1];
                }
                image[level] = newimage;
            }
        }
        
        public byte[][] save()
        {
            byte[][] result = new byte[levels][];
            for(int level=0;level<levels;level++)
            {
                int width = texwidth >>> level;
                int height = texheight >>> level;
                int bytesPerRow = width*4;
                result[level] = new byte[bytesPerRow*height];
                int pos=0;
                for(int row=0;row<height;row++)
                {
                    for(int col=0;col<width;col++)
                    {
                        for(int i=0;i<4;i++)
                        {
                            result[level][pos] = image[level][row][col][i];
                            pos++;
                        }
                    }
                }
            }
            
            return result;
        }
    }
}

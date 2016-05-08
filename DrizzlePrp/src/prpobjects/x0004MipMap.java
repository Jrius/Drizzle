/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import images.Image;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.b;
import shared.e;
import shared.m;
import java.util.Vector;
import uru.generics;
import shared.Bytes;

/*BASE
DWORD TexWidth
DWORD TexHeight
DWORD Stride // always TexWidth * 4
DWORD MemorySize 
if HsmType = 0x02
  BYTE Unknown?
  BYTE Type // 0 1 2 or 3
  // if Type====3 then there are 8 Dwords then EOF (I only found 1)
  if (Type====1)
    DWORD[4] Unknown?
  DWORD JpgSize1
  BYTE[JpgSize1] JpegFile1
  if Type====2
    do
      DWORD[2] Extra
    while Extra[0] != 0
  elseif Type====0
    DWORD JpgSize2
    BYTE[JpgSize2] JpegFile2 // blue channel is the alpha channel
if HsmType = 0x00
  BYTE MipmapLevels
  for L = 1 to MipmapLevels
    DWORD[TexWidth >> (L - 1) x TexHeight >> (L - 1)] AGRB
if HsmType = 0x01
  BYTE MipmapLevels
  for L = 1 to MipmapLevels
    BYTE[(TexWidth >> (L + 1)) * (TexHeight >> (L + 1)) * texel_size] Texel*/
public class x0004MipMap extends uruobj
{
    public boolean resetmemorysize = false;

    //Objheader xheader;
    public x0003Bitmap parent;
    public int texwidth;
    public int texheight;
    public int stride;
    public int memorysize;
    //byte xu1;
    public byte xtype;
    public int[] xu2;
    public int xjpgsize1;
    public byte[] xjpegfile1;
    //int[] xextra;
    //Vector<Integer> xextra1 = new Vector<Integer>();
    //Vector<Integer> xextra2 = new Vector<Integer>();
    public Integer[] xextra1;
    public Integer[] xextra2;
    public int xjpgsize2;
    public byte[] xjpegfile2;
    public byte xmipmaplevels;
    //int[][] xagrb;
    public byte[][] xagrb;
    //byte[][] xtexel;
    public Image.Dxt xDxt;
    
    public void invert()
    {
        switch(parent.type)
        {
            case 0x00:
                /*byte[] data = Bytes.flatten(xagrb);
                Image.Agrb image = new Image.Agrb(data,xmipmaplevels,texwidth,texheight);
                image.invert();
                byte[][] newXagrb = image.save();
                xagrb = newXagrb;*/
                m.err("Image convert not implemented.");
                break;
            case 0x01:
                //byte[] data2 = Bytes.flatten(xtexel);
                //Image.Dxt image2 = new Image.Dxt(data2,xmipmaplevels,texwidth,texheight, parent.xtexel_size);
                //image2.invert();
                //byte[][] newDxt = image2.save();
                //xtexel = newDxt;
                xDxt.invert();
                break;
            case 0x02:
                m.err("Image convert not implemented.");
                break;
            default:
                m.err("PlMipMap: invert: unexpected type.");
                break;
        }
    }
    
    public void rotate90clockwise()
    {
        switch(parent.type)
        {
            case 0x00:
                m.err("Image rotate not implemented.");
                break;
            case 0x01:
                xDxt.rotate90clockwise();
                break;
            case 0x02:
                m.err("Image rotate not implemented.");
                break;
            default:
                m.err("PlMipMap: invert: unexpected type.");
                break;
        }
    }

    private x0004MipMap(){}
    public x0004MipMap(context c) throws readexception //,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new x0003Bitmap(c);//,false);
        texwidth = data.readInt();
        texheight = data.readInt();
        stride = data.readInt(); e.ensure(stride==texwidth*4);
        memorysize = data.readInt(); //the size of the rest of the object, past the next byte. (i.e. the size of xDxt or xagrb.)
        xmipmaplevels = data.readByte();
        switch(parent.type)
        {
            case 0x00:
                //xmipmaplevels = data.readByte();
                xagrb = new byte[xmipmaplevels][];
                //this may be wrong, see how its done in case 0x01.
                for(int i=0;i<xmipmaplevels;i++)
                {
                    int levelsize = (texwidth >>> i)*(texheight >>> i);
                    xagrb[i] = data.readBytes(levelsize*4);
                }
                break;
            case 0x01:
                //xmipmaplevels = data.readByte();
                xDxt = new Image.Dxt(data, xmipmaplevels,texwidth,texheight,parent.xtexel_size);
                /*xtexel = new byte[xmipmaplevels][];
                for(int i=0;i<xmipmaplevels;i++)
                {
                    //see ptMipMap in pyprp
                    e.ensure(texwidth!=0);
                    e.ensure(texheight!=0);
                    int levelsize;
                    int levelwidth = texwidth >>> i;
                    int levelheight = texheight >>> i;
                    if(levelwidth<3 || levelheight<3)
                    {
                        levelsize = levelwidth * levelheight * 4; //not enough for a texel, so just use raw data.
                    }
                    else
                    {
                        levelsize = levelwidth * levelheight * b.ByteToInt32(parent.xtexel_size) / 16; //16 pixels per texel.
                    }
                    xtexel[i] = data.readBytes(levelsize);
                }*/
                break;
            case 0x02:
                xtype = data.readByte();
                
                if((xtype&0x01)==0)
                {
                    xjpgsize1 = data.readInt();
                    xjpegfile1 = data.readBytes(xjpgsize1);
                }
                else
                {
                    int i1;
                    int i2;
                    Vector<Integer> xextra1temp = new Vector<Integer>();
                    do
                    {
                        i1 = data.readInt();
                        i2 = data.readInt();
                        xextra1temp.add(i1);
                        xextra1temp.add(i2);
                    }while(i1!=0);
                    xextra1 = generics.convertVectorToArray(xextra1temp, Integer.class);
                }
                if((xtype&0x02)==0)
                {
                    xjpgsize2 = data.readInt();
                    xjpegfile2 = data.readBytes(xjpgsize2); //blue channel is the alpha channel.
                }
                else
                {
                    int i1;
                    int i2;
                    Vector<Integer> xextra2temp = new Vector<Integer>();
                    do
                    {
                        i1 = data.readInt();
                        i2 = data.readInt();
                        xextra2temp.add(i1);
                        xextra2temp.add(i2);
                    }while(i1!=0);
                    xextra2 = generics.convertVectorToArray(xextra2temp, Integer.class);
                }
                /*//xu1 = data.readByte();
                switch(xtype)
                {
                    case 0:
                        xjpgsize1 = data.readInt();
                        xjpegfile1 = data.readBytes(xjpgsize1);

                        xjpgsize2 = data.readInt();
                        xjpegfile2 = data.readBytes(xjpgsize2); //blue channel is the alpha channel.
                        break;
                    case 1:
                        xu2 = data.readInts(4);
                        
                        xjpgsize1 = data.readInt();
                        xjpegfile1 = data.readBytes(xjpgsize1);
                        break;
                    case 2:
                        xjpgsize1 = data.readInt();
                        xjpegfile1 = data.readBytes(xjpgsize1);

                        int i1;
                        int i2;
                        do
                        {
                            i1 = data.readInt();
                            i2 = data.readInt();
                            xextra.add(i1);
                            xextra.add(i2);
                        }while(i1!=0);
                        break;
                    default:
                         //it could be a 3 too, but that needs further analysis.
                        m.msg("unsupported type in mipmap.");
                        break;
                }*/
                break;
            default:
                m.msg("x0004mipmap: mystery!");
                break;
        }
    }
    public void compile(Bytedeque deque)
    {
        //get subportion first.
        Bytedeque subdeque = new Bytedeque(shared.Format.pots);
        switch(parent.type)
        {
            case 0x00:
                //deque.writeByte(xmipmaplevels);
                for(int i=0;i<xmipmaplevels;i++)
                {
                    int levelsize = (texwidth >>> i)*(texheight >>> i);
                    subdeque.writeBytes(xagrb[i]);
                }
                break;
            case 0x01:
                //deque.writeByte(xmipmaplevels);
                xDxt.compile(subdeque);
                /*for(int i=0;i<xmipmaplevels;i++)
                {
                    int levelsize;
                    int levelwidth = texwidth >>> i;
                    int levelheight = texheight >>> i;
                    if(levelwidth<3 || levelheight<3)
                    {
                        levelsize = levelwidth * levelheight * 4; //not enough for a texel, so just use raw data.
                    }
                    else
                    {
                        levelsize = levelwidth * levelheight * b.ByteToInt32(parent.xtexel_size) / 16; //16 pixels per texel.
                    }
                    deque.writeBytes(xtexel[i]);
                }*/
                break;
            case 0x02:
                //deque.writeByte(xu1);
                subdeque.writeByte(xtype);

                if((xtype&0x01)==0)
                {
                    subdeque.writeInt(xjpgsize1);
                    subdeque.writeBytes(xjpegfile1);
                }
                else
                {
                    for(int i=0;i<xextra1.length;i++)
                    {
                        subdeque.writeInt(xextra1[i]);
                    }
                }
                if((xtype&0x02)==0)
                {
                    subdeque.writeInt(xjpgsize2);
                    subdeque.writeBytes(xjpegfile2); //blue channel.
                }
                else
                {
                    for(int i=0;i<xextra2.length;i++)
                    {
                        subdeque.writeInt(xextra2[i]);
                    }
                }
                /*switch(xtype)
                {
                    case 0:
                        deque.writeInt(xjpgsize1);
                        deque.writeBytes(xjpegfile1);
                        deque.writeInt(xjpgsize2);
                        deque.writeBytes(xjpegfile2);
                        break;
                    case 1:
                        deque.writeInts(xu2);
                        deque.writeInt(xjpgsize1);
                        deque.writeBytes(xjpegfile1);
                        break;
                    case 2:
                        deque.writeInt(xjpgsize1);
                        deque.writeBytes(xjpegfile1);
                        int i1;
                        int i2;
                        //do
                        //{
                            //i1 = data.readInt();
                            //i2 = data.readInt();
                            //xextra.add(i1);
                            //xextra.add(i2);
                        //}while(i1!=0);
                        for(int i=0;i<xextra1.size();i++)
                        {
                            deque.writeInt(xextra1.get(i));
                        }
                        break;
                    default:
                         //it could be a 3 too, but that needs further analysis.
                        m.msg("unsupported type in mipmap.");
                        break;
                }*/
                break;
            default:
                m.msg("x0004mipmap: mystery!");
                break;
        }
        byte[] subdequebytes = subdeque.getAllBytes();

        //m.err("fixme");
        parent.compile(deque);
        deque.writeInt(texwidth);
        deque.writeInt(texheight);
        deque.writeInt(stride);
        if(this.resetmemorysize) memorysize = subdequebytes.length;
        deque.writeInt(memorysize);
        
        deque.writeByte(xmipmaplevels);

        deque.writeBytes(subdequebytes);
    }

    public static x0004MipMap createEmpty()
    {
        x0004MipMap r = new x0004MipMap();
        r.parent = x0003Bitmap.createEmpty();
        return r;
    }
}

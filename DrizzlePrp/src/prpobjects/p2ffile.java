/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import shared.IBytestream;
import shared.SerialBytestream;
import shared.Ntstring;
import shared.FixedLengthString;
import shared.m;
import shared.e;
import shared.Flt;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import shared.b;
import java.awt.Graphics2D;

public class p2ffile
{

    FixedLengthString fontname;
    byte fontsize;
    int flags; //0 normal, 1 bold, 2 italics
    int bmwidth;
    int bmheight;
    int largestLetterHeight;
    byte bpp;
    byte[][] bitmaps;
    short unknown;
    int numLetters;
    Letter[] letters;

    public static p2ffile readFromStream(IBytestream c)
    {
        p2ffile result = new p2ffile(c);
        return result;
    }

    public p2ffile(IBytestream c)
    {
        fontname = new FixedLengthString(c,256);
        fontsize = c.readByte();
        flags = c.readInt();
        bmwidth = c.readInt();
        bmheight = c.readInt();
        largestLetterHeight = c.readInt();
        bpp = c.readByte(); e.ensure(bpp==8);
        //int bmcount = 256;
        //bitmaps = new byte[bmcount][][];
        //for(int i=0;i<bmcount;i++)
        //{
            //bitmaps[i] = new byte[bmwidth][bmheight];
        //}
        bitmaps = new byte[bmheight][bmwidth];
        for(int i=0;i<bmheight;i++)
        {
            for(int j=0;j<bmwidth;j++)
            {
                bitmaps[i][j] = c.readByte();
            }
        }
        unknown = c.readShort(); e.ensure(unknown==0);
        numLetters = c.readInt();
        letters = c.readArray(Letter.class, numLetters);
    }
    public void drawStringToCanvas(Graphics g, int x, int y, String str, int colour)
    {
        boolean halve = false;

        //load images
        BufferedImage[] imgs = new BufferedImage[numLetters];
        for(int i=0;i<numLetters;i++)
        {
            Letter letter = letters[i];
            int rowoffset = letter.offset/bmwidth;
            if(letter.letterHeight==0)
            {
                imgs[i] = null;
                continue;
            }
            //e.ensure((bmwidth%2)==0);
            int imgwidth = halve?(bmwidth/2):bmwidth;
            BufferedImage img = new BufferedImage(imgwidth, letter.letterHeight, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            imgs[i] = img;
            int lastval=0;
            for(int k=0;k<letter.letterHeight;k++)
            {
                for(int j=0;j<bmwidth;j++)
                {
                    int pixel = colour;
                    if(rowoffset+k>=bmheight)
                    {
                        int dummy=0;
                    }
                    byte a = bitmaps[rowoffset+k][j];
                    pixel = pixel | (a << 24);
                    if(halve)
                    {
                        /*if(j==bmwidth-1 && bmwidth%2==1)
                        {
                            //special case for odd numbered
                        }*/
                        if(j%2==1)
                        {
                            //long newval = (b.Int32ToInt64(pixel)+b.Int32ToInt64(lastval))/2;
                            //pixel = (int)newval;
                            //int a1 = (pixel&0xFF000000+lastval&0xFF000000)/2;
                            //int a2 = ((pixel&0xFF)+(lastval&0xFF))/2;
                            //int newpixel = (((pixel&0xFF000000)>>>8+(lastval&0xFF000000)>>>8)/2)<<8 | ((pixel&0xFF0000)+(lastval&0xFF0000))/2 | ((pixel&0xFF00)+(lastval&0xFF00))/2 | ((pixel&0xFF+lastval&0xFF))/2;
                            int newpixel = b.bytewiseAverage(pixel, lastval);
                            img.setRGB(j/2, k, newpixel);
                        }
                        else
                        {
                            lastval = pixel;
                        }
                    }
                    else
                    {
                        img.setRGB(j, k, pixel);
                    }

                }

            }
        }

        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(java.awt.AlphaComposite.SrcOver);
        byte[] strbytes = b.StringToBytes(str);
        int curx = x;
        int cury = y;
        for(byte by: strbytes)
        {
            Letter l = letters[b.ByteToInt32(by)];
            Image img = imgs[b.ByteToInt32(by)];

            float lpadding = l.lPadding.toJavaFloat();
            float rpadding = l.rPadding.toJavaFloat();
            int lpad = Math.round(lpadding);
            int rpad = Math.round(rpadding);
            double loff = lpadding - Math.floor(lpadding);
            double roff = rpadding - Math.floor(rpadding);
            if(loff>0.1 || roff>0.1)
            {
                int dummy=0;
            }

            curx += lpad;
            int actualy = cury-l.stHeight;
            //int actualy = cury;
            //int actualy = cury-l.stHeight+l.letterHeight;
            g2.drawImage(img, curx, actualy, null);
            curx += rpad;
            curx += bmwidth;

        }
    }
    public static class Letter
    {
        int offset;
        int letterHeight;
        int stHeight;
        Flt lPadding;
        Flt rPadding;

        //Raster raster;
        //BufferedImage img;

        public Letter(IBytestream c)
        {
            offset = c.readInt();
            letterHeight = c.readInt();
            stHeight = c.readInt();
            lPadding = new Flt(c);
            rPadding = new Flt(c);
        }
    }
}

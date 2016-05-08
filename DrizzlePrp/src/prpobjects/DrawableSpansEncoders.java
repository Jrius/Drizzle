/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import shared.Flt;
import shared.IBytedeque;
import uru.context;
import shared.b;
import shared.m;
import shared.e;

public class DrawableSpansEncoders
{
    public static class RawPotsVertices
    {
        public RawPotsVertex[] vertices;

        public RawPotsVertices(int count)
        {
            vertices = new RawPotsVertex[count];
        }
        public byte[] compileall()
        {
            uru.Bytedeque c = new uru.Bytedeque(shared.Format.pots);
            for(RawPotsVertex v: vertices)
            {
                v.compile(c);
            }
            byte[] r = c.getAllBytes();
            return r;
        }
        /*public void makeEachCount0()
        {
            float f;
            for(RawPotsVertex v: vertices)
            {
                for(RawPotsElement[] rpes: v.uvs)
                {
                    for(RawPotsElement rpe: rpes)
                    {
                        rpe.
                    }
                }
            }
        }*/
        /*public void halverange(int start, int length)
        {
            if(vertices[start].)
        }*/
    }
    public static class DecompressedPotsVertices
    {
        public int count;
        public int A;
        public int B;
        public int C;
        public float[] xs;
        public float[] ys;
        public float[] zs;
        public float[][] wss;
        public int[] bones;
        //public int[][] bones;
        public short[] normxs;
        public short[] normys;
        public short[] normzs;
        public byte[] blues;
        public byte[] greens;
        public byte[] reds;
        public byte[] alphas;
        public float[][][] uvss;

        public DecompressedPotsVertices(byte[] rawdata, byte fformat2, int rawdataversion, int count2)
        {
            count = count2;
            context c = context.createFromBytestream(shared.ByteArrayBytestream.createFromByteArray(rawdata));
            //c.compile = true;
            //c.out = data;
            e.force(rawdataversion==3);
            c.readversion = rawdataversion;
            //int count2 = getCount();
            //PlDrawableSpansEncoders.RawPotsVertices r = new PlDrawableSpansEncoders.RawPotsVertices(count2);
            //float[] result = new float[count2*3];
            //GetVertexDataSize(count2,fformat,c);

            //int start = c.in.getAbsoluteOffset();
            int fformat = b.ByteToInt32(fformat2);
            A = (fformat & 0x40) >>> 6;
            B = (fformat & 0x30) >>> 4;
            C = (fformat & 0x0F) >>> 0;


            DrawableSpansEncoders.rundataElement x = new DrawableSpansEncoders.rundataElement();
            DrawableSpansEncoders.rundataElement y = new DrawableSpansEncoders.rundataElement();
            DrawableSpansEncoders.rundataElement z = new DrawableSpansEncoders.rundataElement();

            DrawableSpansEncoders.rundataElement[] ws = new DrawableSpansEncoders.rundataElement[B];
            for(int i=0;i<B;i++) ws[i] = new DrawableSpansEncoders.rundataElement();

            DrawableSpansEncoders.rundataColour blue = new DrawableSpansEncoders.rundataColour();
            DrawableSpansEncoders.rundataColour green = new DrawableSpansEncoders.rundataColour();
            DrawableSpansEncoders.rundataColour red = new DrawableSpansEncoders.rundataColour();
            DrawableSpansEncoders.rundataColour alpha = new DrawableSpansEncoders.rundataColour();

            DrawableSpansEncoders.rundataElement[][] uvs = new DrawableSpansEncoders.rundataElement[C][3];
            for(int i=0;i<C;i++)
            {
                //uvs[i] = new PlDrawableSpansEncoders.rundataElement[3];
                for(int j=0;j<3;j++)
                {
                    uvs[i][j] = new DrawableSpansEncoders.rundataElement();
                }
            }

            initArrays();

            for(int i=0;i<count2;i++)
            {
                //PlDrawableSpansEncoders.RawPotsVertex v = new PlDrawableSpansEncoders.RawPotsVertex();
                //int possofar = c.in.getAbsoluteOffset() - start;

                //get vertex.
                xs[i] = x.pollAsElement3(c);
                ys[i] = y.pollAsElement3(c);
                zs[i] = z.pollAsElement3(c);
                //result[i*3+0] = xval;
                //result[i*3+1] = yval;
                //result[i*3+2] = zval;

                //get weights
                //v.ws = new PlDrawableSpansEncoders.RawPotsElement[B];
                for(int j=0;j<B;j++)
                {
                    wss[j][i] = ws[j].pollAsElement4(c);
                }

                //if A is set to true, get bones
                if ((B!=0) && (A!=0))
                {
                    bones[i] = c.in.readInt(); //bones
                    //if(c.compile) c.out.writeInt(out1);
                }

                //normals: these are just a byte now.
                //data.readShort(); //nx
                //data.readShort(); //ny
                //data.readShort(); //nz
                /*if(c.readversion==6)
                {
                    byte out2 = c.in.readByte(); //nx
                    byte out3 = c.in.readByte(); //ny
                    byte out4 = c.in.readByte(); //nz
                }
                else if(c.readversion==3||c.readversion==4)
                {*/
                    normxs[i] = c.in.readShort();
                    normys[i] = c.in.readShort();
                    normzs[i] = c.in.readShort();
                //}

                //colours:
                blues[i] = blue.pollAsColour2(c);
                greens[i] = green.pollAsColour2(c);
                reds[i] = red.pollAsColour2(c);
                alphas[i] = alpha.pollAsColour2(c);

                //uv texture coordinates
                //v.uvs = new PlDrawableSpansEncoders.RawPotsElement[C][3];
                for(int j=0;j<C;j++)
                {
                    for(int k=0;k<3;k++)
                    {
                        uvss[j][k][i] = uvs[j][k].pollAsElement5(c);
                    }
                }

                //r.vertices[i] = v;

            }
            if(c.in.getBytesRemaining()!=0)
            {
                int dummy=0;
            }
            //return result;
            //int stop = c.in.getAbsoluteOffset();
            //return stop-start; //return the size we processed.
        //}
            //return r;
        }
        DecompressedPotsVertices(){}
        public static DecompressedPotsVertices createEmpty()
        {
            DecompressedPotsVertices r = new DecompressedPotsVertices();
            return r;
        }
        public byte[] compileall()
        {
            uru.Bytedeque c = new uru.Bytedeque(shared.Format.pots);
            //compileNaive(c);
            compileSophisticated(c);
            byte[] r = c.getAllBytes();
            return r;
        }

        public void initArrays()
        {
            xs = new float[count];
            ys = new float[count];
            zs = new float[count];
            if(B!=0) wss = new float[B][count];
            if ((B!=0) && (A!=0)) bones = new int[count];
            //bones = new int[A][count2];
            normxs = new short[count];
            normys = new short[count];
            normzs = new short[count];
            blues = new byte[count];
            greens = new byte[count];
            reds = new byte[count];
            alphas = new byte[count];
            if(C!=0) uvss = new float[C][3][count];

        }
        public byte getfformat()
        {
            //set fformat:
            //int A = (fformat & 0x40) >>> 6;
            //int B = (fformat & 0x30) >>> 4;
            //int C = (fformat & 0x0F) >>> 0;
            int fformat2 = A<<6 | B<<4 | C<<0;
            byte fformat = (byte)(0x80 | fformat2);
            return fformat;
        }
        public void compileSophisticated(uru.Bytedeque c)
        {
            PotsElementEncoder x = new PotsElementEncoder(xs,1024.0f);
            PotsElementEncoder y = new PotsElementEncoder(ys,1024.0f);
            PotsElementEncoder z = new PotsElementEncoder(zs,1024.0f);

            PotsElementEncoder[] ws = new PotsElementEncoder[B];
            for(int i=0;i<B;i++) ws[i] = new PotsElementEncoder(wss[i],32768.0f);

            PotsColourEncoder blue = new PotsColourEncoder(blues);
            PotsColourEncoder green = new PotsColourEncoder(greens);
            PotsColourEncoder red = new PotsColourEncoder(reds);
            PotsColourEncoder alpha = new PotsColourEncoder(alphas);

            PotsElementEncoder[][] uvs = new PotsElementEncoder[C][3];
            for(int i=0;i<C;i++)
            {
                //uvs[i] = new PlDrawableSpansEncoders.rundataElement[3];
                for(int j=0;j<3;j++)
                {
                    //uvs[i][j] = new PlDrawableSpansEncoders.rundataElement();
                    uvs[i][j] = new PotsElementEncoder(uvss[i][j],65536.0f);
                }
            }

            for(int i=0;i<count;i++)
            {
                x.compileNext(c);
                y.compileNext(c);
                z.compileNext(c);

                if(wss!=null)
                {
                    for(int j=0;j<B;j++)
                    {
                        ws[j].compileNext(c);
                    }
                }

                if(bones!=null)
                {
                    c.writeInt(bones[i]);
                }

                c.writeShort(normxs[i]);
                c.writeShort(normys[i]);
                c.writeShort(normzs[i]);

                blue.compileNext(c);
                green.compileNext(c);
                red.compileNext(c);
                alpha.compileNext(c);

                if(uvss!=null)
                {
                    for(int j=0;j<C;j++)
                    {
                        for(int k=0;k<3;k++)
                        {
                            uvs[j][k].compileNext(c);
                        }
                    }
                }
            }
        }

        public void compileNaive(uru.Bytedeque c)
        {
            for(int i=0;i<count;i++)
            {
                RawPotsElement.createSingleton(xs[i]).compile(c);
                RawPotsElement.createSingleton(ys[i]).compile(c);
                RawPotsElement.createSingleton(zs[i]).compile(c);

                if(wss!=null)
                {
                    /*for(float fs: wss[i])
                    {
                        RawPotsElement.createSingleton(fs).compile(c);
                    }*/
                    for(int j=0;j<wss.length;j++)
                    {
                        RawPotsElement.createSingleton(wss[j][i]).compile(c);
                    }
                }

                if(bones!=null)
                {
                    c.writeInt(bones[i]);
                }

                c.writeShort(normxs[i]);
                c.writeShort(normys[i]);
                c.writeShort(normzs[i]);

                RawPotsColour.createSingleton(blues[i]).compile(c);
                RawPotsColour.createSingleton(greens[i]).compile(c);
                RawPotsColour.createSingleton(reds[i]).compile(c);
                RawPotsColour.createSingleton(alphas[i]).compile(c);

                if(uvss!=null)
                {
                    /*for(float[] fs: uvss[i])
                    {
                        for(float f: fs)
                        {
                            RawPotsElement.createSingleton(f).compile(c);
                        }
                    }*/
                    for(int j=0;j<uvss.length;j++)
                    {
                        for(int k=0;k<uvss[j].length;k++)
                        {
                            RawPotsElement.createSingleton(uvss[j][k][i]).compile(c);
                        }
                    }
                }
            }
        }
    }

    public static class PotsElementEncoder
    {
        float[] vals;
        float divisor;
        int i = 0;
        float range;

        //float curbase;
        float min;
        int curRunLength = 0;

        public PotsElementEncoder(float[] _vals, float _divisor)
        {
            vals = _vals;
            divisor = _divisor;
            range = 65536.0f/divisor;
        }
        public void compileNext(uru.Bytedeque c)
        {
            if(curRunLength==0)
            {
                lookahead();
                
                //write the top.
                c.writeFloat(min);
                short l = (short)curRunLength;
                c.writeShort(l);
            }

            //get short
            short out = (short)((vals[i]-min)*divisor+0.5f);
            c.writeShort(out);

            curRunLength--;
            i++;

        }
        void lookahead()
        {
            //find how much we can encode
            min = (float)Math.floor((vals[i]*divisor)+0.5f)/divisor;
            float max = min;
            curRunLength = 1;

            for(int j=i+1;j<vals.length;j++)
            {
                if(curRunLength==65535)
                {
                    return; //we can't do any more even if we wanted to.
                }

                float curval = (float)Math.floor((vals[j]*divisor)+0.5f)/divisor;
                if(curval<min)
                {
                    //make it the new min if we can
                    if(max-curval<range)
                    {
                        //we're good!
                        min = curval;
                        curRunLength++;
                    }
                    else
                    {
                        //skip out of the for loop; we can't go any further.
                        return;
                    }
                }
                else if(curval>max)
                {
                    if(curval-min<range)
                    {
                        max = curval;
                        curRunLength++;
                    }
                    else
                    {
                        return;
                    }
                }
            }
        }
    }
    public static class PotsColourEncoder
    {
        byte[] vals;
        int i = 0;

        int curRunLength = 0;
        boolean homogenous;
        byte homogenousBasis;

        public PotsColourEncoder(byte[] newvals)
        {
            vals = newvals;
        }
        public void compileNext(uru.Bytedeque c)
        {
            if(curRunLength==0)
            {
                lookahead();

                if(homogenous)
                {
                    short out = (short)(curRunLength | 0x8000);
                    c.writeShort(out);
                    c.writeByte(homogenousBasis);
                }
                else
                {
                    short out = (short)curRunLength;
                    c.writeShort(out);
                }
            }

            if(!homogenous)
            {
                c.writeByte(vals[i]);
            }

            curRunLength--;
            i++;
        }
        void lookahead()
        {
            curRunLength = 1;

            int numRemaining = vals.length-i;

            //if there's not enough left for a look ahead, just write them.
            if(numRemaining<4)
            {
                homogenous = false;
                curRunLength = numRemaining;
                return;
            }

            //look ahead for homogenous values.
            homogenousBasis = vals[i];
            if(homogenousBasis==vals[i+1] && homogenousBasis==vals[i+2] && homogenousBasis==vals[i+3])
            {
                //definately use homogenous values.
                homogenous = true;


                for(int j=i+1;j<vals.length;j++)
                {
                    if(curRunLength==32767)
                    {
                        return; //we can't go any high than this.
                    }
                    
                    byte curval = vals[j];
                    if(curval!=homogenousBasis)
                    {
                        return;
                    }
                    curRunLength++;
                }
                return;
            }

            //so, at this point, we have at least 4 values, and they are not homogenous.
            //let's see how far we have to go before we could find at least 4 homogenous values.  That will be our runlength, so the next run can have those homogenous values.
            for(int j=i+1;j<vals.length-4;j++) //is the 4 correct?  I don't feel like thinking about it :D
            {
                int testCurRunLength = j-i;
                if(testCurRunLength==32767)
                {
                    homogenous = false;
                    curRunLength = testCurRunLength;
                    return; //we can't go any higher than this.
                }

                byte curval = vals[j];
                if(curval==vals[j+1] && curval==vals[j+2] && curval==vals[j+3])
                {
                    //found a homogenous run!  So, don't include that.
                    homogenous = false;
                    curRunLength = testCurRunLength;
                    return;
                }
                //curRunLength++;
            }

            //otherwise there is no homogenous run, so just use the whole thing.
            homogenous = false;
            curRunLength = numRemaining;
            return;
            
        }
    }

    public static class RawPotsVertex
    {
        RawPotsElement x;
        RawPotsElement y;
        RawPotsElement z;
        RawPotsElement[] ws;
        Integer bones;
        short normx;
        short normy;
        short normz;
        RawPotsColour blue;
        RawPotsColour green;
        RawPotsColour red;
        RawPotsColour alpha;
        public RawPotsElement[][] uvs;

        public void compile(uru.Bytedeque c)
        {
            x.compile(c);
            y.compile(c);
            z.compile(c);

            for(RawPotsElement rpe: ws)
            {
                rpe.compile(c);
            }

            if(bones!=null)
            {
                c.writeInt(bones);
            }

            c.writeShort(normx);
            c.writeShort(normy);
            c.writeShort(normz);

            blue.compile(c);
            green.compile(c);
            red.compile(c);
            alpha.compile(c);

            for(RawPotsElement[] rpes: uvs)
            {
                for(RawPotsElement rpe: rpes)
                {
                    rpe.compile(c);
                }
            }
        }
    }
    public static class RawPotsElement
    {
        boolean hadcount0;
        //Flt basee; //may be null.
        float basee;
        //byte b1; //only here if read as MOUL/v6.
        short out7;

        //if(count!=0) block should always occur.
        short out9; //only present in Moul/v6 if b1 above is set, otherwise make it 0.

        public static RawPotsElement createSingleton(float val)
        {
            RawPotsElement r = new RawPotsElement();
            r.hadcount0 = true;
            r.basee = val;
            r.out7 = 1;
            r.out9 = 0;
            return r;
        }

        public RawPotsElement(){}
        /*public RawPotsElement(short out9)
        {
            this.hadcount0 = false;
            this.out9 = out9;
        }
        public RawPotsElement(Flt basee, short out7, short out9)
        {
            this.hadcount0 = true;
            this.basee = basee;
            this.out7 = out7;
            this.out9 = out9;
        }*/
        public void doubleval()
        {
            /*if(hadcount0)
            {
                basee = basee*2.0f;
            }
            if(b.Int16ToInt32(out9)>32767)
            {
                m.throwUncaughtException("short cannot be doubled.");
            }
            out9 = (short)(b.Int16ToInt32(out9)*2);*/
        }
        public void compile(IBytedeque c)
        {
            if(hadcount0)
            {
                //basee.compile(c);
                c.writeFloat(basee);
                c.writeShort(out7);
            }
            c.writeShort(out9);
        }
    }
    public static class RawPotsColour
    {
        boolean hadCount0;
        short out5;
        boolean hadRle;
        byte basec;
        byte out6;

        public static RawPotsColour createSingleton(byte val)
        {
            RawPotsColour r = new RawPotsColour();
            r.hadCount0 = true;
            r.out5 = 1;
            r.hadRle = false;
            r.out6 = val;
            return r;
        }

        public void compile(IBytedeque c)
        {
            if(hadCount0)
            {
                c.writeShort(out5);
                if(hadRle)
                {
                    c.writeByte(basec);
                }
            }
            if(!hadRle)
            {
                c.writeByte(out6);
            }
        }
    }
    public static class rundataElement
    {
        //Flt basee;
        float basee;
        //byte basec;
        int count;
        //boolean rle;
        byte b1;

        public RawPotsElement pollAsElement(/*int A, int B, int C,*/ context c)
        {
            RawPotsElement r = new RawPotsElement();
            if(count==0)
            {
                r.hadcount0 = true;

                //basee = new Flt(c);
                basee = c.readFloat();
                //if(c.compile) basee.compile(c.out);
                r.basee = basee;

                if(c.readversion==6)
                {
                    b1 = c.in.readByte();
                    //if(c.compile && c.writeversion==6) c.out.writeByte(b1);
                }
                else if(c.readversion==3||c.readversion==4||c.readversion==7)
                {
                    //do nothing
                }

                short out7 = c.in.readShort();
                r.out7 = out7;
                //if(c.compile) c.out.writeShort(out7);
                count = b.Int16ToInt32(out7);
            }
            //r.basee = basee; //always save the base, so we can use this for convenience later.
            if(count!=0)
            {
                if(c.readversion==6)
                {
                    if(b1==0)
                    {
                        count--;
                        //return data.readShort();
                        short out9 = c.in.readShort();
                        //if(c.compile) c.out.writeShort(out9);
                        //return;
                        //return out9;
                        r.out9 = out9;
                    }
                    else if(b1==1)
                    {
                        count--;
                        //if(c.compile && c.writeversion==3)
                        //{
                        //    c.out.writeShort((short)0);//Is this right? Is this the default? //(32768);
                        //}
                        //return (short)0;
                        r.out9 = 0;
                    }
                    else
                    {
                        m.msg("unknown byte.");
                    }
                }
                else if(c.readversion==3||c.readversion==4||c.readversion==7)
                {
                    count--;
                    //short out9 = c.in.readShort();
                    r.out9 = c.readShort();
                    //if(c.compile) c.out.writeShort(out9);
                }
            }
            //return 0;
            return r;
        }
        public float pollAsElement3(context c)
        {
            short out9 = pollAsElement2(c);
            float result = (float)b.Int16ToInt32(out9)/1024.0f + basee;
            return result;
        }
        public float pollAsElement4(context c)
        {
            short out9 = pollAsElement2(c);
            float result = (float)b.Int16ToInt32(out9)/32768.0f + basee;
            return result;
        }
        public float pollAsElement5(context c)
        {
            short out9 = pollAsElement2(c);
            float result = (float)b.Int16ToInt32(out9)/65536.0f + basee;
            return result;
        }
        public short pollAsElement2(context c)
        {
            short out9;
            //RawPotsElement r = new RawPotsElement();
            if(count==0)
            {
                //r.hadcount0 = true;

                //basee = new Flt(c);
                basee = c.readFloat();
                //if(c.compile) basee.compile(c.out);
                //r.basee = basee;

                if(c.readversion==6)
                {
                    b1 = c.in.readByte();
                    //if(c.compile && c.writeversion==6) c.out.writeByte(b1);
                }
                else if(c.readversion==3||c.readversion==4||c.readversion==7)
                {
                    //do nothing
                }

                short out7 = c.in.readShort();
                //r.out7 = out7;
                //if(c.compile) c.out.writeShort(out7);
                count = b.Int16ToInt32(out7);
            }
            //if(count!=0)
            //{
                if(c.readversion==6)
                {
                    if(b1==0)
                    {
                        count--;
                        //return data.readShort();
                        out9 = c.in.readShort();
                        //if(c.compile) c.out.writeShort(out9);
                        //return;
                        //return out9;
                        //r.out9 = out9;
                    }
                    else if(b1==1)
                    {
                        count--;
                        //if(c.compile && c.writeversion==3)
                        //{
                        //    c.out.writeShort((short)0);//Is this right? Is this the default? //(32768);
                        //}
                        //return (short)0;
                        out9 = 0;
                    }
                    else
                    {
                        throw new shared.uncaughtexception("unknown byte.");
                        //out9 = 0;
                    }
                }
                else if(c.readversion==3||c.readversion==4||c.readversion==7)
                {
                    count--;
                    //short out9 = c.in.readShort();
                    out9 = c.readShort();
                    //if(c.compile) c.out.writeShort(out9);
                }
                else
                {
                    throw new shared.uncaughtexception("Unhandled readversion.");
                }
            //}
            //return 0;
            //return r;
            return out9;
        }
    }
    public static class rundataColour
    {
        //Flt basee;
        byte basec;
        int count;
        boolean rle;
        //byte b1;

        /*public rundataColour()
        {
            //basee = null;
            //count = 0;
            //basec = 0;
            //rle = false;
        }*/
        public byte pollAsColour2(context c)
        {
            if(count==0)
            {
                short out5 = c.in.readShort();
                count = b.Int16ToInt32(out5);
                if((count & 0x8000)!=0) //negative
                {
                    rle = true;
                    basec = c.in.readByte();
                    count = count & 0x7FFF;
                }
                else // >=0
                {
                    rle = false;
                }
            }
            //if(count!=0)
            //{
                count--;
                if(rle)
                {
                    //return base;
                    //return;
                    return basec;
                }
                else
                {
                    byte out6 = c.in.readByte();
                    return out6;
                }
            //}
        }
        //returns the compressed value, in some circumstances.
        public RawPotsColour pollAsColour(/*int A, int B, int C,*/ context c)
        {
            //sub_50e270
            RawPotsColour r = new RawPotsColour();
            if(count==0)
            {
                r.hadCount0 = true;
                short out5 = c.in.readShort();
                r.out5 = out5;
                //if(c.compile) c.out.writeShort(out5);
                count = b.Int16ToInt32(out5);
                if((count & 0x8000)!=0) //negative
                {
                    //r.hadRle = true;
                    //m.msg("haven't tested this.");
                    rle = true;
                    basec = c.in.readByte();
                    r.basec = basec;
                    //if(c.compile) c.out.writeByte(basec);
                    count = count & 0x7FFF;
                }
                else // >=0
                {
                    rle = false;
                }
            }
            if(count!=0)
            {
                count--;
                r.hadRle = rle;
                if(rle)
                {
                    //return base;
                    //return;
                    return r;
                }
                else
                {
                    //return data.readByte();
                    byte out6 = c.in.readByte();
                    r.out6 = out6;
                    //if(c.compile) c.out.writeByte(out6);
                    //return;
                    return r;
                }
            }
            m.err("We shouldn't be able to reach here.");
            return r;
        }
    }

}

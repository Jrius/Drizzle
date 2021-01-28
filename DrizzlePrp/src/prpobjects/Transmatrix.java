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

import shared.Vertex;
import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import shared.b;
import shared.m;
import uru.Bytedeque;
import shared.e;
import shared.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

//aka hsMatrix44
public strictfp class Transmatrix extends uruobj
{
    byte isnotIdentity;
    int[] xmatrix = new int[16]; //raw data (floats are 32bit, so they fit in integer.)
    
    //private Flt[][] testvals;
    
    private Transmatrix(){}
    
    public Transmatrix(context c)
    {
        if(c.readversion==6||c.readversion==7)
        {
            isnotIdentity = c.in.readByte();
            if(isnotIdentity!=0)
            {
                xmatrix = c.in.readInts(16);
            }
        }
        else if(c.readversion==3||c.readversion==4)
        {
            isnotIdentity = 1; //this byte doesn't exist in pots.
            xmatrix = c.in.readInts(16);
        }
        
        //testvals = this.convertToFltArray();
    }
    public static Transmatrix createDefault()
    {
        Transmatrix result = new Transmatrix();
        result.isnotIdentity = 0;
        return result;
    }
    /*public void fillInMatrix()
    {
        if(isnotIdentity==0)
        {
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    result[i][j] = i==j?1:0;
        }
    }*/
    public float[][] convertToFloatArray()
    {
        float[][] r = new float[4][4];
        if(isnotIdentity!=0)
        {
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    r[i][j] = Float.intBitsToFloat(xmatrix[i*4+j]);
                }
            }
        }
        else
        {
            for(int i=0;i<4;i++)
            {
                r[i][i] = 1.0f;
            }
        }
        return r;
    }
    public static Transmatrix createFromVector2(double x, double y, double z)
    {
        return createFromVector((float)x,(float)y,(float)z);
    }
    public static Transmatrix createFromVector(float x, float y, float z)
    {
        double[][] doublemat = new double[4][4];
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                doublemat[i][j] = i==j?1:0;
        doublemat[0][3] = x;
        doublemat[1][3] = y;
        doublemat[2][3] = z;
        RealMatrix rm = new RealMatrixImpl(doublemat);
        return createFromMatrix(rm);
    }
    public void setelement(int row, int col, float val)
    {
        int newval = Flt.FloatToIntCode(val);
        xmatrix[row*4+col] = newval;
    }
    public static Transmatrix createFromMatrix44(Matrix44 matrix)
    {
        Transmatrix r = new Transmatrix();
        r.isnotIdentity = 1;
        r.xmatrix = new int[16];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                r.xmatrix[i*4+j] = Float.floatToRawIntBits(matrix.mat[i][j]);
            }
        }
        return r;
    }
    public Transmatrix mult(Transmatrix t2)
    {
        double[][] m1 = this.convertToDoubleArray();
        double[][] m2 = t2.convertToDoubleArray();
        RealMatrix rm1 = new RealMatrixImpl(m1);
        RealMatrix rm2 = new RealMatrixImpl(m2);
        RealMatrix result = rm1.multiply(rm2);
        Transmatrix result2 = Transmatrix.createFromMatrix(result);
        return result2;
    }
    public Vertex mult(Vertex v)
    {
        double[][] m1 = this.convertToDoubleArray();
        double[][] m2 = v.convertToDouble4x1Matrix();
        RealMatrix rm1 = new RealMatrixImpl(m1);
        RealMatrix rm2 = new RealMatrixImpl(m2);
        RealMatrix result = rm1.multiply(rm2);
        double[][] result2 = result.getData();
        Vertex result3 = Vertex.createFromDouble4x1Matrix(result2);
        return result3;
    }
    private void assign(Transmatrix t2)
    {
        this.isnotIdentity = t2.isnotIdentity;
        this.xmatrix = t2.xmatrix;
    }
    public void multModify(Transmatrix t2)
    {
        Transmatrix newmat = this.mult(t2);
        this.assign(newmat);
    }
    public void compile(Bytedeque deque)
    {
        //there is no isnotIdentity flag in pots, it always has the full matrix.
        if(isnotIdentity==1)
        {
            deque.writeInts(xmatrix);
        }
        else
        {
            Flt zero = new Flt(0);
            Flt one = new Flt(1);

            one.compile(deque);
            zero.compile(deque);
            zero.compile(deque);
            zero.compile(deque);

            zero.compile(deque);
            one.compile(deque);
            zero.compile(deque);
            zero.compile(deque);

            zero.compile(deque);
            zero.compile(deque);
            one.compile(deque);
            zero.compile(deque);

            zero.compile(deque);
            zero.compile(deque);
            zero.compile(deque);
            one.compile(deque);

        }
    }

    /*public float[] toFloats()
    {
        float[] result = new float[16];
        for(int i=0;i<16;i++)
        {
            result[i] = Float.intBitsToFloat(xmatrix[i]);
        }
        return result;
    }*/
    public String toString()
    {
        //float[] m2 = toFloats();
        String result = "";
        if(isnotIdentity==0)
        {
            result += "identity matrix";
        }
        else
        {
            for(int i=0;i<16;i++)
            {
                if(i%4==0) result += "\n";
                result += ", "+shared.Flt.toString(xmatrix[i]);
            }
        }
        return result;
    }
    public double[][] convertToDoubleArray()
    {
        double[][] result = new double[4][4];
        if(isnotIdentity==0)
        {
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    result[i][j] = i==j?1:0;
        }
        else
        {
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    int datum = xmatrix[i*4+j];
                    result[i][j] = Flt.createFromData(datum).toJavaFloat();
                }
            }
        }
        return result;
        
    }
    public Flt[][] convertToFltArray()
    {
        Flt[][] result = new Flt[4][4];
        if(isnotIdentity==0)
        {
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    result[i][j] = i==j?Flt.one():Flt.zero();
        }
        else
        {
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    int datum = xmatrix[i*4+j];
                    result[i][j] = Flt.createFromData(datum);
                }
            }
        }
        return result;
    }
    
    public RealMatrix convertToMatrix()
    {
        double[][] rawdata = new double[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int datum = xmatrix[i*4+j];
                rawdata[i][j] = (double)Flt.createFromData(datum).toJavaFloat();
            }
        }
        RealMatrix rm = new RealMatrixImpl(rawdata);
        return rm;
    }
    
    public static Transmatrix createFromFloatArray(float[][] arr)
    {
        Transmatrix r = new Transmatrix();
        r.isnotIdentity = 1;
        r.xmatrix = new int[16];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int intval = Float.floatToRawIntBits(arr[i][j]);
                r.xmatrix[i*4+j] = intval;
            }
        }
        return r;
    }
    public static Transmatrix createFromDoubleArray(double[][] arr)
    {
        m.warn("Untested createFromDoubleArray");
        RealMatrix mat = createMatrixFromDoubleArray(arr);
        return createFromMatrix(mat);
    }
    public static RealMatrix createMatrixFromDoubleArray(double[][] arr)
    {
        m.warn("Untested createMatrixFromDoubleArray");
        RealMatrix mat = new RealMatrixImpl(arr);
        return mat;
    }
    public Flt[][] convertTo3x4FltArray()
    {
        Flt[][] result = new Flt[3][4];
        if(this.isnotIdentity==0) throw new shared.uncaughtexception("Unhandled case in convertTo3x4FltArray");
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                int raw = this.xmatrix[i*4+j];
                Flt raw2 = Flt.createFromData(raw);
                if(i==3)
                {
                    if((j==3 && !raw2.approxequals(1)) || (j!=3 && !raw2.approxequals(0)))
                        throw new shared.uncaughtexception("Unhandled case2 in convertTo3x4FltArray");
                }
                else
                {
                    result[i][j] = raw2;
                }
            }
        }
        return result;
    }
    public static Transmatrix createFrom3x4Array(double[][] arr)
    {
        Transmatrix result = new Transmatrix();
        result.isnotIdentity = 1;
        result.xmatrix = new int[16];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                float f;
                if(i==3)
                    f = j==3?1:0;
                else
                    f = (float)arr[i][j];
                int rawflt = Flt.createFromJavaFloat(f).toRawdata();
                result.xmatrix[i*4+j] = rawflt;
            }
        }
        return result;
    }
    public static Transmatrix createFromMatrix(RealMatrix rm)
    {
        double[][] rawdata = rm.getData();
        Transmatrix result = new Transmatrix();
        result.isnotIdentity = 1;
        result.xmatrix = new int[16];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                float f = (float)rawdata[i][j];
                int rawflt = Flt.createFromJavaFloat(f).toRawdata();
                result.xmatrix[i*4+j] = rawflt;
            }
        }
        return result;
    }
    
    //returns null if not possible.
    public Vertex convertTo3Vector()
    {
        Flt[][] flts = this.convertToFltArray();
        Flt x = flts[0][3];
        Flt y = flts[1][3];
        Flt z = flts[2][3];
        //e.ensure(flts[3][3]==1);
        return new Vertex(x,y,z);
    }

    public Transmatrix deepClone()
    {
        return new Transmatrix(this);
    }
    public Transmatrix(Transmatrix t)
    {
        this.isnotIdentity = t.isnotIdentity;
        if(t.isnotIdentity!=0)
        {
            this.xmatrix = new int[t.xmatrix.length];
            for(int i=0;i<t.xmatrix.length;i++)
            {
                this.xmatrix[i] = t.xmatrix[i];
            }
        }
    }
    public Transmatrix inverse()
    {
        RealMatrix inv = this.convertToMatrix().inverse();
        Transmatrix invm = Transmatrix.createFromMatrix(inv);
        return invm;
    }
}

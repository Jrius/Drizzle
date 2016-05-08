/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class Matrix44
{
    public float[][] mat;

    public Matrix44()
    {
        mat = new float[4][4];
    }

    public static Matrix44 createFromVector(float x, float y, float z)
    {
        Matrix44 r = createIdentity();
        r.mat[0][3] = x;
        r.mat[1][3] = y;
        r.mat[2][3] = z;
        return r;
    }
    public static Matrix44 createIdentity()
    {
        Matrix44 r = new Matrix44();
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                r.mat[i][j] = (i==j)?1:0;
            }
        }
        return r;
    }
    public org.apache.commons.math.linear.RealMatrix getApacheMatrix()
    {
        double[][] m2 = new double[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                m2[i][j] = mat[i][j];
            }
        }
        org.apache.commons.math.linear.RealMatrixImpl m = new org.apache.commons.math.linear.RealMatrixImpl(m2);
        return m;
    }
    public Matrix44 getInverse()
    {
        org.apache.commons.math.linear.RealMatrix m = this.getApacheMatrix();
        org.apache.commons.math.linear.RealMatrix inv = m.inverse();
        Matrix44 r = Matrix44.createFromApacheMatrix(inv);
        return r;
    }
    public static Matrix44 createFromApacheMatrix(org.apache.commons.math.linear.RealMatrix m)
    {
        Matrix44 r = new Matrix44();
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                r.mat[i][j] = (float)m.getEntry(i, j);
        return r;
    }
}

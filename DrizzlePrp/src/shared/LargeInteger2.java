/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.math.BigInteger;
import java.util.Random;

//behaves as though it were big-endian.
public class LargeInteger2
{
    private BigInteger val;

    public LargeInteger2(long l)
    {
        val = BigInteger.valueOf(l);
    }

    public LargeInteger2(int numbits, Random rng)
    {
        val = new BigInteger(numbits, rng);
    }

    public LargeInteger2(String bigendian_hexstr)
    {
        //String revhexstr = reverseHexstr(littleendian_hexstr);
        val = new java.math.BigInteger(bigendian_hexstr, 16);
    }

    public LargeInteger2(byte[] bigendian_bytes)
    {
        this(b.BytesToHexString(bigendian_bytes));
    }

    private LargeInteger2(BigInteger val2)
    {
        val = val2;
    }
    /*private static String reverseHexstr(String hexstr)
    {
        StringBuilder r = new StringBuilder();
        for(int i=hexstr.length()-2;i>=0;i-=2)
        {
            char c1 = hexstr.charAt(i);
            char c2 = hexstr.charAt(i+1);
            r.append(c1);
            r.append(c2);
        }
        return r.toString();
    }*/
    /*public LargeInteger(byte[] data)
    {
        val = new java.math.BigInteger(data); //might have a sign problem.
    }*/

    public LargeInteger2 modPow(LargeInteger2 exponent, LargeInteger2 mod)
    {
        BigInteger newval = val.modPow(exponent.val, mod.val);
        return new LargeInteger2(newval);
    }

    public byte[] toBytes(int size)
    {
        byte[] bytes = val.toByteArray();
        byte[] result;

        if(bytes.length==size)
        {
            //perfect!
            result = bytes;
        }
        else if(bytes.length<size)
        {
            //pad it out front
            int pad = size=bytes.length;
            result = new byte[size];
            for(int i=0;i<bytes.length;i++)
            {
                result[i+pad] = bytes[i];
            }
        }
        else if(bytes.length==size+1)
        {
            //may be extra positive sign bit.
            if(bytes[0]==0)
            {
                result = new byte[size];
                for(int i=0;i<size;i++)
                {
                    result[i] = bytes[i+1];
                }
            }
            else
            {
                throw new uncaughtexception("Can not fit in given size.");
            }
        }
        else //bytes.length>size+1
        {
            throw new uncaughtexception("Can not fit in given size.");
        }

        //b.reverseEndianness(result);
        return result;
    }

    public String toString(int size)
    {
        byte[] data = this.toBytes(size);
        String r = b.BytesToHexString(data);
        return r;
    }

    public String toString()
    {
        return toString(64); //just for debugging purposes
    }

}

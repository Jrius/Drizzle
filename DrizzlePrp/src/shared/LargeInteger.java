/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.math.BigInteger;
import java.util.Random;

//behaves as though it were little-endian, unlike BigInteger.
public class LargeInteger
{
    private BigInteger val;

    public LargeInteger(long l)
    {
        val = BigInteger.valueOf(l);
    }

    public LargeInteger(int numbits, Random rng)
    {
        val = new BigInteger(numbits, rng);
    }

    public LargeInteger(String littleendian_hexstr)
    {
        String revhexstr = reverseHexstr(littleendian_hexstr);
        val = new java.math.BigInteger(revhexstr, 16);
    }

    public LargeInteger(byte[] littleendian_bytes)
    {
        this(b.BytesToHexString(littleendian_bytes));
    }

    private LargeInteger(BigInteger val2)
    {
        val = val2;
    }
    private LargeInteger(){}

    public static LargeInteger probablePrime(int bitlength, Random rng)
    {
        return new LargeInteger(BigInteger.probablePrime(512, rng));
    }

    private static String reverseHexstr(String hexstr)
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
    }
    /*public LargeInteger(byte[] data)
    {
        val = new java.math.BigInteger(data); //might have a sign problem.
    }*/

    public LargeInteger modPow(LargeInteger exponent, LargeInteger mod)
    {
        BigInteger newval = val.modPow(exponent.val, mod.val);
        return new LargeInteger(newval);
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

        b.reverseEndianness(result);
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

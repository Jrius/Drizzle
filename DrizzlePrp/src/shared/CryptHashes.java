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

package shared;

import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class CryptHashes {
    public static enum Hashtype
    {
        md5,
        sha1,
        whirlpool,
    }
    public static byte[] GetMd5(byte[] inputData)
    {
        MD5Digest d = new MD5Digest();
        byte[] result = new byte[d.getDigestSize()]; //allocate enough bytes for the hash.

        d.update(inputData,0,inputData.length); //calculate the hash.
        d.doFinal(result, 0); //get the result.
        return result;
    }
    public static byte[] GetHash(byte[] inputData, Hashtype type)
    {
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(inputData);
        byte[] result = GetHash(in, type);
        return result;
    }
    public static byte[] GetHash(String filename, Hashtype type)
    {
        FileInputStream in=null;
        try
        {
            in = new FileInputStream(filename);
            byte[] result = GetHash(in, type);
            in.close();
            //in = null;
            return result;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("Problem opening/closing input file for hash.");
        }
        finally
        {
            try
            {
                if(in!=null) in.close();
            }
            catch(Exception e){}
        }
    }
    public static byte[] GetHash(InputStream in, Hashtype type)
    {
        ExtendedDigest d;
        switch(type)
        {
            case md5:
                MD5Digest d4 = new MD5Digest();
                d = d4;
                break;
            case whirlpool:
                WhirlpoolDigest d3 = new WhirlpoolDigest();
                d = d3;
                break;
            case sha1:
                SHA1Digest d2 = new SHA1Digest();
                d = d2;
                break;
            default:
                throw new shared.uncaughtexception("Unhandled hash type in GetHash.");
        }
        
        byte[] result = new byte[d.getDigestSize()];
        
        int read=0;
        byte[] buffer = new byte[1024];
        {
            while(read!=-1)
            {
                try
                {
                    read = in.read(buffer);
                }
                catch(Exception e)
                {
                    throw new shared.uncaughtexception("Problem reading from stream for hash.");
                }
                if(read>0)
                {
                    d.update(buffer, 0, read);
                }
            }
        }
        d.doFinal(result, 0);
        return result;
    }
    
    public static byte[] GetWhirlpool(String filename)
    {
        FileInputStream in=null;
        try
        {
            in = new FileInputStream(filename);
            byte[] result = GetWhirlpool(in);
            in.close();
            return result;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("Problem opening/closing input file for Whirlpool hash.");
        }
        finally
        {
            try
            {
                if(in!=null) in.close();
            }
            catch(Exception e){}
        }
    }
    public static byte[] GetWhirlpool(InputStream in) //this is the regular Whirlpool, not it's variations.
    {
        WhirlpoolDigest d = new WhirlpoolDigest();
        byte[] result = new byte[d.getDigestSize()];
        
        int read=0;
        byte[] buffer = new byte[1024];
        {
            while(read!=-1)
            {
                try
                {
                    read = in.read(buffer);
                }
                catch(Exception e)
                {
                    throw new shared.uncaughtexception("Problem reading from stream for Whirlpool hash.");
                }
                if(read>0)
                {
                    d.update(buffer, 0, read);
                }
            }
        }
        d.doFinal(result, 0);
        return result;
    }
    public static byte[] GetWhirlpool(byte[] inputData) //this is the regular Whirlpool, not it's variations.
    {
        WhirlpoolDigest d = new WhirlpoolDigest();
        byte[] result = new byte[d.getDigestSize()];
        
        d.update(inputData,0,inputData.length);
        d.doFinal(result, 0);
        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import shared.b;
import java.util.Vector;
import shared.*;
import java.io.InputStream;
import java.io.IOException;
import org.bouncycastle.crypto.StreamCipher;

public class CryptoBytestream extends IBytestream
{
    InputStream in;
    StreamCipher cipher;
    
    public CryptoBytestream(InputStream in, StreamCipher cipher)
    {
        this.in = in;
        this.cipher = cipher;
    }

    public void skip(long n)
    {
        throw new uncaughtexception("unimplemented");
    }

    protected int read()
    {
        try{
            int r = in.read();
            if(r==-1) throw new java.net.SocketException("Connection reset"); //actually just closed gracefully.
            return b.ByteToInt32(cipher.returnByte((byte)r));
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public byte readByte()
    {
        try{
            int r = in.read();
            if(r==-1) throw new java.net.SocketException("Connection reset"); //actually just closed gracefully.
            //byte r = (byte)in.read();
            return cipher.returnByte((byte)r);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public byte[] readBytes(int num)
    {
        try{
            byte[] r = new byte[num];

            //method1
            //in.read(r);
            //return r;

            //method2
            int n = 0;
            while (n < num)
            {
                int count = in.read(r, n, num - n);
                //if (count < 0)
                //    throw new uncaughtexception("Read past end of stream.");
                if(count==-1) throw new java.net.SocketException("Connection reset"); //actually just closed gracefully.
                n += count;
            }
            byte[] r2 = new byte[num];
            cipher.processBytes(r, 0, r.length, r2, 0);
            return r2;
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public int readInt()
    {
        //try{
            int ch1 = this.read();
            int ch2 = this.read();
            int ch3 = this.read();
            int ch4 = this.read();
            return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
        //}catch(IOException e){
        //    throw new uncaughtnestedexception(e);
        //}
    }
    public short readShort()
    {
        //try{
            int ch1 = this.read();
            int ch2 = this.read();
            return (short)((ch1 << 0) + (ch2 << 8));
        //}catch(IOException e){
        //    throw new uncaughtnestedexception(e);
        //}
    }
    public int getAbsoluteOffset()
    {
        return -1;
    }
    public int getFilelength()
    {
        return -1;
    }
    public int getBytesRemaining()
    {
        //return getFilelength()-getAbsoluteOffset();
        return -1;
    }
    public IBytestream Fork(long offset)
    {
        throw new uncaughtexception("unimplemented");
    }
    
    public String toString()
    {
        try{
            return "available: "+Integer.toString(in.available());
        }catch(Exception e){
            return "exception";
        }
    }
}

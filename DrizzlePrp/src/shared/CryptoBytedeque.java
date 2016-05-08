/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.bouncycastle.crypto.StreamCipher;

public class CryptoBytedeque extends shared.IBytedeque
{

    OutputStream out;
    StreamCipher cipher;

    public CryptoBytedeque(OutputStream out, StreamCipher cipher)
    {
        this.out = out;
        this.cipher = cipher;
    }

    public void writeInt(int n)
    {
        //try{
            int b1 = ((n >>>  0) & 0xFF);
            int b2 = ((n >>>  8) & 0xFF);
            int b3 = ((n >>> 16) & 0xFF);
            int b4 = ((n >>> 24) & 0xFF);
            this.write(b1);
            this.write(b2);
            this.write(b3);
            this.write(b4);
        //}catch(IOException e){
        //    throw new uncaughtnestedexception(e);
        //}
    }
    public void writeShort(short n)
    {
        //try{
            int b1 = ((n >>> 0) & 0xFF);
            int b2 = ((n >>> 8) & 0xFF);
            this.write(b1);
            this.write(b2);
        //}catch(IOException e){
        //    throw new uncaughtnestedexception(e);
        //}
    }
    public IBytedeque Fork(){
        throw new uncaughtexception("unimplemented");
    }
    public byte[] getAllBytes()
    {
        throw new uncaughtexception("unimplemented");
    }
    public void writeBytes(byte[] bytes)
    {
        try{
            byte[] r = new byte[bytes.length];
            cipher.processBytes(bytes, 0, bytes.length, r, 0);
            out.write(r);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void writeByte(byte b1)
    {
        try{
            //out.write(((int)b)&0xFF);
            byte b2 = cipher.returnByte(b1);
            int b3 = b.ByteToInt32(b2);
            out.write(b3);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void write(int byt)
    {
        try{
            byte b1 = (byte)byt;
            byte b2 = cipher.returnByte(b1);
            int b3 = b.ByteToInt32(b2);
            out.write(b3);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void writeShorts(short[] shorts)
    {
        //try{
            for(short n: shorts)
            {
                int b1 = ((n >>> 0) & 0xFF);
                int b2 = ((n >>> 8) & 0xFF);
                this.write(b1);
                this.write(b2);
            }
        //}catch(IOException e){
        //    throw new uncaughtnestedexception(e);
        //}
    }
    protected Iterator<byte[]> getIterator()
    {
        throw new uncaughtexception("unimplemented");
    }
    public void flush()
    {
        try{
            out.flush();
        }catch(IOException e){
            throw new nested(e);
        }
    }
}

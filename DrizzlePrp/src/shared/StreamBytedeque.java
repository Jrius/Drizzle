/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;

public class StreamBytedeque extends shared.IBytedeque
{

    OutputStream out;

    public StreamBytedeque(OutputStream out)
    {
        this.out = out;
    }

    public OutputStream getChildStreamIfExists()
    {
        return out;
    }
    public void writeInt(int n)
    {
        try{
            out.write((n >>>  0) & 0xFF);
            out.write((n >>>  8) & 0xFF);
            out.write((n >>> 16) & 0xFF);
            out.write((n >>> 24) & 0xFF);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void writeShort(short n)
    {
        try{
            out.write((n >>> 0) & 0xFF);
            out.write((n >>> 8) & 0xFF);
        }catch(IOException e){
            throw new nested(e);
        }
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
            out.write(bytes);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void writeByte(byte b)
    {
        try{
            out.write(((int)b)&0xFF);
        }catch(IOException e){
            throw new nested(e);
        }
    }
    public void writeShorts(short[] shorts)
    {
        try{
            for(short n: shorts)
            {
                out.write((n >>> 0) & 0xFF);
                out.write((n >>> 8) & 0xFF);
            }
        }catch(IOException e){
            throw new nested(e);
        }
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

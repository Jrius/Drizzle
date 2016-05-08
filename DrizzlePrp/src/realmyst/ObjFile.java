/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
//import java.util.zip.
import shared.e;

/**
 * Structure used by dirtfile to decompile DNI file
 */
public class ObjFile
{
    byte[] rawdata;
    
    //String fullname;
    //boolean hasraw = true;
    //boolean hasparsed = false;

    public ObjFile(IBytestream c, int length, int compressedLength)
    {
        if(compressedLength==0)
        {
            //uncompressed.
            rawdata = c.readBytes(length);
        }
        else
        {
            //compressed.
            try
            {
                byte[] compressedData = c.readBytes(compressedLength);
                Inflater decomp = new Inflater();
                decomp.setInput(compressedData);
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                while(!decomp.finished())
                {
                    int num = decomp.inflate(buffer);
                    out.write(buffer,0,num);
                }
                out.close();

                rawdata = out.toByteArray();
                e.ensure(rawdata.length==length);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                throw new uncaughtexception("Error while decompressing stream.");
            }
        }
    }
}

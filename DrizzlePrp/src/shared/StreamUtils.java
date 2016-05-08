/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StreamUtils
{
    public static byte[] ReadAll(InputStream in)
    {
        Bytedeque2 result = new Bytedeque2(Format.none);
        byte[] buf = new byte[1024];
        try{
            int actualRead = 0;
            while(true)
            {
                actualRead = in.read(buf);
                if(actualRead==-1) break; //eof!
                result.writeBytes(buf, 0, actualRead);
            }
        }catch(java.io.IOException e){
            throw new nested(e);
        }
        return result.getAllBytes();
    }
}

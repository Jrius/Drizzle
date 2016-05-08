/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

public class HttpUtils
{

    public static byte[] geturl(String address)
    {
        return geturl(address, "Mozilla/5.0");
    }

    public static byte[] geturl(String address, String useragent)
    {
        //address = java.net.URLEncoder.encode(address, "UTF-8");
        address = address.replaceAll(" ", "%20");
        try {
            //m.msg("Checking...");
            URL url = new URL(address);
            Object content = url.getContent();
            URLConnection con = url.openConnection();
            if(useragent!=null) con.setRequestProperty("User-Agent", useragent);
            InputStream in = new BufferedInputStream(con.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            }
            in.close();
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (Exception e) {
            //e.printStackTrace();
            m.warn("Unable to download: ",address);
        }
        return null;
    }

    public static String geturlAsString(String address)
    {
        byte[] data = geturl(address);
        String result = b.BytesToString(data);
        return result;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.Image;
import java.net.URL;
import java.awt.image.BufferedImage;
import shared.m;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Vector;

public class GetResource
{
    public static boolean enableTranslations = true;

    /*private static void findAllResources(Vector<String> list, String folder)
    {
        try{
            String jarpath = new File(GetResource.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
            shared.zip.getAllEntries(jarpath);
            //shared.zip.getAllEntries("C:/Documents%20and Settings/user/Desktop/DrizzleAdjunct/DrizzleAdjunct/DrizzlePrp.jar");
        }catch(Exception e)
        {
            m.err("You shouldn't see this.");
            m.err(e.getMessage());
            return;
        }
    }*/
    public static boolean hasResource(String path)
    {
        URL url = findResource(path);
        return (url!=null);
    }
    private static URL findResource(String path)
    {
        URL url = GetResource.class.getResource(path);
        if(url==null)
        {
                return null;
        }
        else
        {
            return url;
        }

    }
    public static Image getResourceAsImage(/*Object relativeObj,*/ String path)
    {
        try
        {
            URL url = findResource(/*".."+*/path);
            //URL url = relativeObj.getClass().getResource(path);
            //javax.swing.ImageIcon image = new javax.swing.ImageIcon(url,"");
            BufferedImage img = javax.imageio.ImageIO.read(url);
            return img;
        }
        catch(Exception e)
        {
            m.err("Unable to load Image resource:",path);
            return null;
        }
    }
    
    public static String getResourceAsString(String path)
    {
        try
        {
            URL url = findResource(/*".."+*/path);
            InputStream in = url.openStream();
            int bytesread = 0;
            byte[] buffer = new byte[1024];
            StringBuilder result = new StringBuilder();
            while(bytesread!=-1)
            {
                bytesread = in.read(buffer,0,1024);
                if(bytesread!=-1)
                {
                    result.append(new String(buffer,0,bytesread,"ISO-8859-1"));
                }
            }
            String result2 = result.toString();
            in.close();
            return result2;
        }
        catch(Exception e)
        {
            m.err("Unable to load Image resource:",path);
            return null;
        }
    }
    public static InputStream getResourceAsStream(String path)
    {
        try
        {
            URL url = findResource(path);
            InputStream in = url.openStream();
            return in;
        }
        catch(Exception e)
        {
            m.err("Unable to load Stream resource:",path);
            return null;
        }
    }
    public static File getResourceAsFile(String path, boolean deleteOnExit)
    {
        try
        {
            File temp = File.createTempFile("Drizzle", null);
            if(deleteOnExit) temp.deleteOnExit();
            FileOutputStream out = new FileOutputStream(temp);
            
            URL url = findResource(path);
            InputStream in = url.openStream();
            int bytesread = 0;
            byte[] buffer = new byte[1024];
            //StringBuilder result = new StringBuilder();
            while(bytesread!=-1)
            {
                bytesread = in.read(buffer,0,1024);
                if(bytesread!=-1)
                {
                    //result.append(new String(buffer,0,bytesread));
                    out.write(buffer, 0, bytesread);
                }
            }
            //String result2 = result.toString();
            in.close();
            out.flush();
            out.close();
            return temp;
        }
        catch(Exception e)
        {
            m.err("Unable to open file resource:",path);
            e.printStackTrace();
            return null;
        }
    }

}

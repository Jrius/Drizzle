/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*package uam;

import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class Downloader
{
    public static byte[] DownloadFile(String urlstr)
    {
        
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DownloadFile(urlstr,new BufferedOutputStream(baout));
        try
        {
            //baout.flush();
            baout.close();
        }
        catch(IOException e)
        {
            throw new DownloadErrorException("Unable to flush bytes from stream.");
        }
        byte[] result = baout.toByteArray();
        try
        {
            baout.close();
        }
        catch(IOException e)
        {
            throw new DownloadErrorException("Unable to close output stream.");
        }
        return result;
    }
    public static void DownloadFile(String urlstr, String filename)
    {
        FileOutputStream fout;
        try
        {
            fout = new FileOutputStream(filename);
        }
        catch(FileNotFoundException e)
        {
            throw new DownloadErrorException("Unable to open file for output: "+filename);
        }
        DownloadFile(urlstr,new BufferedOutputStream(fout));
        try
        {
            fout.close();
        }
        catch(IOException e)
        {
            throw new DownloadErrorException("Error closing output file: "+filename);
        }
        
    }
    public static void DownloadFile(String urlstr, OutputStream out)
    {
        int sConnectTimeout = 10;
        int sReadTimeout = 10;
        
        URL url;
        try
        {
            url = new URL(urlstr);
        }
        catch(MalformedURLException e)
        {
            throw new DownloadErrorException("The server address is invalid.");
        }
        URLConnection conn;
        InputStream in;
        try
        {
            conn = url.openConnection();
            //sun.net.www.http.HttpClient httpclient;
            conn.setConnectTimeout(sConnectTimeout*1000);
            conn.setReadTimeout(sReadTimeout*1000);
            conn.setAllowUserInteraction(false); //otherwise it might popup a password question.
            in = conn.getInputStream();
        }
        catch(IOException e)
        {
            if(e instanceof SocketTimeoutException)
            {
                throw new DownloadErrorException("Unable to open connection.");
            }
            else
            {
                throw new DownloadErrorException(e.getMessage());
            }
        }
        byte[] buffer = new byte[1024];
        int read=0;
        int transferred=0;
        while(read!=-1)
        {
            try
            {
                try
                {
                    read = in.read(buffer);
                }
                catch(SocketTimeoutException e)
                {
                    read = 0;
                    //ask the user to continue?
                }
                if(read>0)
                {
                    out.write(buffer, 0, read);
                    transferred += read;
                }
            }
            catch(IOException e)
            {
                throw new DownloadErrorException("Error while reading.");
            }
        }
        try
        {
            out.flush();
            in.close();
        }
        catch(IOException e)
        {
            throw new DownloadErrorException("Unable to close connection.");
        }
        
    }
}*/

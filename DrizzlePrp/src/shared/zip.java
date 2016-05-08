/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.zip.ZipOutputStream;

public class zip
{
    //Zlib streams start with 0x78 0x9c
    public static byte[] decompressZlib(byte[] input)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return decompressZlib(input,0,input.length, out);
    }
    public static byte[] decompressZlib(byte[] input, int offset, int length, ByteArrayOutputStream out)
    {
        try
        {
            //byte[] compressedData = c.readBytes(compressedLength);
            Inflater decomp = new Inflater();
            decomp.setInput(input,offset,length);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            while(!decomp.finished())
            {
                int num = decomp.inflate(buffer);
                out.write(buffer,0,num);
            }
            out.close();
            byte[] result = out.toByteArray();
            return result;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("Exception during zip decompression.");
        }
    }
    public static byte[] decompressGzip(byte[] input)
    {
        try
        {
            //byte[] compressedData = c.readBytes(compressedLength);
            //Inflater decomp = new java.util.zip.Inflater();
            ByteArrayInputStream instr = new ByteArrayInputStream(input);
            GZIPInputStream ginstr = new java.util.zip.GZIPInputStream(instr);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num;
            while((num=ginstr.read(buffer))>0)
            {
                //int num = decomp.inflate(buffer);
                out.write(buffer,0,num);
            }
            out.close();
            ginstr.close();
            instr.close();
            byte[] result = out.toByteArray();
            return result;
        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("Exception during gzip decompression.");
        }
    }
    public static byte[] compressGzip(byte[] input)
    {
        try
        {
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(baout);

            ByteArrayInputStream in = new ByteArrayInputStream(input);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();

            out.finish();
            out.close();
            byte[] result = baout.toByteArray();
            return result;

        }
        catch(Exception e)
        {
            throw new shared.uncaughtexception("Exception during gzip compression.");
        }
    }
    public static Vector<String> getAllEntries(String zipfilename)
    {
        Vector<String> result = new Vector();
        try{
            ZipFile zf = new java.util.zip.ZipFile(zipfilename);
            java.util.Enumeration entries = zf.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry ze = (ZipEntry)entries.nextElement();
                String name = ze.getName();
                result.add(name);
                //m.msg(name);
            }
        }catch(Exception e)
        {
            throw new shared.uncaughtexception("Error while reading zipfile. "+e.getMessage());
        }
        return result;
    }
    public static void extractZipFile(String zipfilename, File outputdir)
    {
        try{
            ZipFile zf = new ZipFile(zipfilename);
            java.util.Enumeration entries = zf.entries();
            while(entries.hasMoreElements())
            {
                ZipEntry ze = (ZipEntry)entries.nextElement();
                String name = ze.getName();
                if(!ze.isDirectory())
                {
                    File filename = new File(outputdir+"/"+name);
                    filename.getParentFile().mkdirs();
                    InputStream in = zf.getInputStream(ze);
                    FileOutputStream out = new FileOutputStream(filename);
                    byte[] buffer = new byte[1024];
                    while(true)
                    {
                        int num = in.read(buffer);
                        if(num<0) break;
                        out.write(buffer, 0, num);
                    }
                    out.close();
                    in.close();
                }
            }
        }catch(Exception e)
        {
            throw new shared.uncaughtexception("Error while reading zipfile. "+e.getMessage());
        }

    }
    public static void createZipFile(File inputfolder, File outputfile)
    {
        try{
            outputfile.getParentFile().mkdirs();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputfile));

            Vector<String> files = FileUtils.FindAllDecendants(inputfolder, true);
            for(String file: files)
            {
                String fullpath = inputfolder+"/"+file;
                FileInputStream in = new FileInputStream(fullpath);
                ZipEntry ze = new java.util.zip.ZipEntry(file);
                out.putNextEntry(ze);
                byte[] buffer = new byte[1024];
                while(true)
                {
                    int len = in.read(buffer);
                    if(len<0) break;
                    out.write(buffer,0,len);
                }
                out.closeEntry();
                in.close();
            }

            out.close();

        }catch(Exception e){
            throw new shared.uncaughtexception("Error while creating zipfile. "+e.getMessage());
        }


    }
}

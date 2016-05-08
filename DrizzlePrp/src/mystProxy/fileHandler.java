/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import java.io.InputStream;
import java.io.OutputStream;
//import org.mortbay.jetty.Request;
import org.eclipse.jetty.server.Request;
import java.io.File;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;
import java.io.FileInputStream;

import shared.m;


public class fileHandler
{
    public static void handle(String domain2, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        //find cookie
        String packageToUse = null;
        if(proxySettings.usecookies)
        {
            for(Cookie cookie: getCookies(request)) //the normal getCookies can return a null for some reason.
            {
                if(cookie.getName().equals("DrizzlePackage"))
                {
                    packageToUse = cookie.getValue();
                }
            }
        }
        
        String domain;
        if(domain2==null)
        {
            domain = request.getServerName();
        }
        else
        {
            domain = domain2;
        }

        packageInfo p = null;
        if(packageToUse!=null)
        {
            p = proxySettings.packages.getPackageByName(packageToUse);
            if(p==null)
            {
                m.err("Package no longer exists: ",packageToUse);
                return;
            }
            if(!p.handlesDomain(domain))
            {
                m.warn(p.name," doesn't handle domain: ",domain);
                p = null;
            }
        }
        if(p==null)
        {
            p = proxySettings.packages.findPackageThatHandlesDomain(domain);
        }
        if(p==null)
        {
            m.warn("No package can be found that handles domain: ",domain);
            return;
        }

        //allow p to handle it.
        handle2(p, domain, target, request, response, dispatch);

        //each config file (which may be in the .zip) contains:
        //list of entry addresses, list of domain names
        //we will try to maintain a cookie, so we know to use the same package, but if the cookie is lost, we will just use the first package with the domain name, and if there are other packages with that domain, log a warning.

        
    }
    
    public static Cookie[] getCookies(HttpServletRequest request)
    {
        Cookie[] result = request.getCookies();
        if(result==null) return new Cookie[0];
        else return result;
    }
    public static ZipEntry findEntryCaseInsensitive(ZipFile zip, String entryname)
    {
        Enumeration entries = zip.entries();
        ZipEntry entry;
        String entrynameLowercase = entryname.toLowerCase().replace("\\", "/").replace("//","/");
        while(entries.hasMoreElements())
        {
            entry = (ZipEntry)entries.nextElement();
            if(entry.getName().toLowerCase().replace("\\","/").equals(entrynameLowercase))
            {
                return entry;
            }
        }
        return null;
    }
    public static File findFileCaseInsensitive(File base, String filename) throws IOException
    {
        String[] parts = filename.toLowerCase().replace("\\","/").split("/");
        File curbase = base;
        for(String part: parts)
        {
            if(part.equals(""))
            {
                //go on to the next part.
            }
            else
            {
                File[] children = curbase.listFiles();
                boolean found = false;
                for(File f: children)
                {
                    if(f.getName().toLowerCase().equals(part))
                    {
                        //found the part.
                        curbase = f;
                        found = true;
                        break;
                        //if(f.isFile()) return f;
                        //else break;
                    }
                }
                if(!found) return null;
            }
        }
        
        //if we get here, we've found the final file.
        if(curbase.isFile()) return curbase;
        
        //if we get here, the final file is a folder.
        File[] children = curbase.listFiles();
        for(String defaultname: proxySettings.defaultFiles)
        {
            for(File f: children)
            {
                if(f.getName().toLowerCase().equals(defaultname)) return f;
            }
        }
        
        //if we get here, we can't find a default file.
        throw new handlingException("Can't find a default file for folder: "+filename);
    }
    public static void handleDone(HttpServletRequest request, HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_OK);
        ((Request)request).setHandled(true);
    }
    public static void handle2(packageInfo p, String domain, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        //set the cookie for the future.
        if(proxySettings.usecookies)
        {
            Cookie c = new Cookie("DrizzlePackage",p.name);
            c.setPath("/"); //every subdir can access it.
            //c.setDomain(".");
            response.addCookie(c);
        }
        
        //String domain = request.getServerName();
        
        if(p.customSiteHandler==null)
        {
            handle3(p,domain,target,request,response,dispatch);
            /*//just return the file it asks for:
            InputStream instream = readFile(p, domain, target);
            byte[] buffer = new byte[1024];
            OutputStream outstream = response.getOutputStream();
            while(true)
            {
                int actualRead = instream.read(buffer, 0, 1024);
                if(actualRead==-1) break;
                outstream.write(buffer,0,actualRead);
            }
            //response.setContentType("text/html");
            handleDone(request, response);
            //response.setStatus(HttpServletResponse.SC_OK);
            //((Request)request).setHandled(true);*/
        }
        else
        {
            //custom file handler.
            p.customSiteHandler.handle(p, domain, target, request, response, dispatch);
        }
        
        
    }
    public static void handle3(packageInfo p, String domain, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        //just return the file it asks for:
        InputStream instream = readFile(p, domain, target);
        byte[] buffer = new byte[1024];
        OutputStream outstream = response.getOutputStream();
        while(true)
        {
            int actualRead = instream.read(buffer, 0, 1024);
            if(actualRead==-1) break;
            outstream.write(buffer,0,actualRead);
        }
        //response.setContentType("text/html");
        handleDone(request, response);
        if(proxySettings.logFilesRead) m.msg("Read file: ",domain+target);
        instream.close();
        //response.setStatus(HttpServletResponse.SC_OK);
        //((Request)request).setHandled(true);
    }
    public static void disableCaching(HttpServletResponse response)
    {
        //we could remove some of these.
        response.addHeader("Progma", "no-cache");
        response.addHeader("Cache-Control","no-store");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Expires", "Mon, 15 Feb 1982 10:00:00 GMT");
    }
    
    public static InputStream readFile(packageInfo p, String domain, String filename) throws IOException //filename is like /index.html or /files/pub/a.jpg
    {
        if(!p.isZip)
        {
            //String fullname = basefolder+"/"+name+"/"+domain+"/"+filename;
            String fullname = "/"+p.name+"/"+domain+"/"+filename;
            //File file = new File(fullname);
            //File file2 = file.getCanonicalFile(); //may throw exception
            File file2 = findFileCaseInsensitive(new File(proxySettings.basefolder), fullname);
            if( file2==null || !file2.exists())
            {
                throw new handlingException("Tried to read file that doesn't exist: "+fullname);
            }
            else if( ! file2.getPath().toLowerCase().startsWith(proxySettings.basefolder.toLowerCase()) )
            {
                //security problem!  don't let them access outside the basefolder.
                throw new handlingException("A file tried to access outside the basefolder: "+file2.getPath());
            }
            else
            {
                return new FileInputStream(file2);
            }
        }
        else
        {
            //zip file
            String zipfilename = proxySettings.basefolder+"/"+p.name;
            ZipFile zip = new ZipFile(new File(zipfilename), ZipFile.OPEN_READ);
            //ZipEntry entry = zip.getEntry(domain+"/"+filename);
            ZipEntry entry = findEntryCaseInsensitive(zip, domain+"/"+filename);
            InputStream instream = zip.getInputStream(entry);
            return instream;
        }
        
    }
}

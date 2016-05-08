/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
//import org.mortbay.jetty.Request;
import org.eclipse.jetty.server.Request;
import javax.servlet.http.Cookie;


import java.util.Vector;
import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.util.Map.Entry;
import shared.m;
import java.io.OutputStream;
import java.util.Enumeration;

public class packageInfo
{
    String name;
    //String basefolder;
    boolean isZip;
    Vector<String> domainsHandled;
    Vector<String> startPages;
    ICustomSite customSiteHandler = null;
    
    /*public static String[] defaultFiles = {
        "index.htm", "index.html", "default.htm", "default.html",
    };*/
    
    public packageInfo(File file, boolean isZip)//, String basefolder)
    {
        name = file.getName();
        //this.usecookies = usecookies;
        //this.basefolder = basefolder;
        this.isZip = isZip;
        domainsHandled = new Vector<String>();
        startPages = new Vector<String>();
        
        Properties props = new Properties();
        if(!isZip)
        {
            try
            {
                //File configfile = new File(file.getPath()+"/DrizzleServer.txt");
                File configfile = fileHandler.findFileCaseInsensitive(file, "/DrizzleServer.txt");
                props.load(new FileInputStream(configfile));
            }
            catch(Exception e)
            {
                m.err("Unable to find/read DrizzleServer.txt in dir:",file.getPath());
            }
        }
        else
        {
            try
            {
                ZipFile zip = new ZipFile(new File(file.getPath()), ZipFile.OPEN_READ);
                ZipEntry entry = fileHandler.findEntryCaseInsensitive(zip, "DrizzleServer.txt");
                //ZipEntry entry = zip.getEntry("DrizzleServer.txt");
                InputStream instream = zip.getInputStream(entry);
                props.load(instream);
            }
            catch(Exception e)
            {
                m.err("Unable to find/read DrizzleServer.txt in zip:",file.getPath());
            }
        }
        
        
        for(Entry<Object,Object> e: props.entrySet())
        {
            String key = ((String)e.getKey()).toLowerCase();
            String value = (String)e.getValue();
            if(key.startsWith("domain"))
            {
                //add to list of domains.
                domainsHandled.add(value);
            }
            else if(key.startsWith("start"))
            {
                //add to list of start pages.
                startPages.add(value);
            }
            else if(key.equals("customclass"))
            {
                try
                {
                    String classname = value;
                    Class customclass = Class.forName("mystProxy."+value);
                    Object obj = customclass.getConstructor().newInstance();
                    ICustomSite customSite = (ICustomSite)obj;
                    customSiteHandler = customSite;
                }
                catch(Exception e2)
                {
                    m.warn("Unable to load custom site handler for site: ",name);
                }
            }
        }
        
    }
    /*public static ZipEntry findEntryCaseInsensitive(ZipFile zip, String entryname)
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
        for(String defaultname: defaultFiles)
        {
            for(File f: children)
            {
                if(f.getName().toLowerCase().equals(defaultname)) return f;
            }
        }
        
        //if we get here, we can't find a default file.
        throw new handlingException("Can't find a default file for folder: "+filename);
    }*/
    public boolean handlesDomain(String domain)
    {
        for(String s: domainsHandled)
        {
            if(domain.toLowerCase().equals(s.toLowerCase())) return true;
        }
        return false;
    }

    /*public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        //set the cookie for the future.
        if(proxySettings.usecookies)
        {
            Cookie c = new Cookie("DrizzlePackage",name);
            c.setPath("/"); //every subdir can access it.
            //c.setDomain(".");
            response.addCookie(c);
        }
        
        String domain = request.getServerName();
        
        //just return the file it asks for:
        InputStream instream = readFile(domain, target);
        byte[] buffer = new byte[1024];
        OutputStream outstream = response.getOutputStream();
        while(true)
        {
            int actualRead = instream.read(buffer, 0, 1024);
            if(actualRead==-1) break;
            outstream.write(buffer,0,actualRead);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        ((Request)request).setHandled(true);
        
        
    }
    
    public InputStream readFile(String domain, String filename) throws IOException //filename is like /index.html or /files/pub/a.jpg
    {
        if(!isZip)
        {
            //String fullname = basefolder+"/"+name+"/"+domain+"/"+filename;
            String fullname = "/"+name+"/"+domain+"/"+filename;
            //File file = new File(fullname);
            //File file2 = file.getCanonicalFile(); //may throw exception
            File file2 = this.findFileCaseInsensitive(new File(proxySettings.basefolder), fullname);
            if( ! file2.getPath().toLowerCase().startsWith(proxySettings.basefolder.toLowerCase()) )
            {
                //security problem!  don't let them access outside the basefolder.
                throw new handlingException("A file tried to access outside the basefolder: "+file2.getPath());
            }
            else if( ! file2.exists())
            {
                throw new handlingException("Tried to read file that doesn't exist: "+file2.getPath());
            }
            else
            {
                return new FileInputStream(file2);
            }
        }
        else
        {
            //zip file
            String zipfilename = proxySettings.basefolder+"/"+name;
            ZipFile zip = new ZipFile(new File(zipfilename), ZipFile.OPEN_READ);
            //ZipEntry entry = zip.getEntry(domain+"/"+filename);
            ZipEntry entry = this.findEntryCaseInsensitive(zip, domain+"/"+filename);
            InputStream instream = zip.getInputStream(entry);
            return instream;
        }
        
    }*/
}

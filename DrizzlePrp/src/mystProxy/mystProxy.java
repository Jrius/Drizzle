/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

//import org.mortbay.jetty.Server;
import org.eclipse.jetty.server.Server;
//import org.mortbay.jetty.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.AbstractHandler;
//import org.mortbay.jetty.Handler;
import org.eclipse.jetty.server.Handler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
//import org.mortbay.jetty.Request;
import org.eclipse.jetty.server.Request;
//import org.mortbay.jetty.Connector;
import org.eclipse.jetty.server.Connector;
//import org.mortbay.jetty.bio.SocketConnector;
import org.eclipse.jetty.server.bio.SocketConnector;
import javax.servlet.http.Cookie;

import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Vector;

import shared.m;

public class mystProxy
{
    static Server server;
    //static String basefolder;
    //static allPackages packages;
    
    public static void startServer(String basefolder2, String portstring)
    {
        //Server server;
        if(server!=null)
        {
            m.err("Server is already started.");
            return;
        }
        
        proxySettings.basefolder = basefolder2;
        
        int port=0;
        try
        {
            port = Integer.parseInt(portstring);
        }
        catch(Exception e)
        {
            m.err("Invalid port.");
            return;
        }
        
        loadAllInfo();
        
        server = new Server();
        Connector connector=new SocketConnector();
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});
        Handler handler = new mystHandler();//basefolder);//, packages);
        server.setHandler(handler);
        try
        {
            m.msg("Starting proxy server.");
            server.start();
            //server.join();
        }
        catch(Exception e)
        {
            //server crashed.
            m.err("Proxy server crashed.");
        }
        
        proxySettings.logFilesRead = shared.State.AllStates.getStateAsBoolean("proxyLogReads");
    }
    
    public static void stopServer()
    {
        if(server==null)
        {
            m.msg("Server is already stopped.");
            return;
        }
        try
        {
            server.stop();
            server = null;
            m.msg("Server has been stopped.");
        }
        catch(Exception e)
        {
            m.err("Unable to stop server.");
        }
    }
    
    public static void loadAllInfo()
    {
        proxySettings.packages = new allPackages();
        
        File base = new File(proxySettings.basefolder);
        File[] basefiles = base.listFiles();
        for(File file: basefiles)
        {
            if(file.isDirectory())
            {
                //get config file.
                packageInfo curpackage = new packageInfo(file, false);//, proxySettings.basefolder);
                proxySettings.packages.packages.add(curpackage);
            }
            else if(file.getName().toLowerCase().endsWith(".zip"))
            {
                //read from zip.
                packageInfo curpackage = new packageInfo(file,true);//, proxySettings.basefolder);
                proxySettings.packages.packages.add(curpackage);
            }
        }
    }

    public static class mystHandler extends AbstractHandler
    {
        //String basefolder;
        //allPackages packages;
        //proxycontext context;
        
        public mystHandler()//String basefolder2) //, allPackages packages2)//, boolean usecookies)
        {
            //basefolder = basefolder2;
            //packages = packages2;
            //context = c;
        }
        /*public Cookie[] getCookies(HttpServletRequest request)
        {
            Cookie[] result = request.getCookies();
            if(result==null) return new Cookie[0];
            else return result;
        }*/
        public void handle(String target, Request req2, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
            //req2 just contains jetty specific stuff.
            handle(target,request,response,0); //the dispatch is never used anyway.
        }
        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
        {
            fileHandler.handle(null, target, request, response, dispatch);
            
            /*//response.setContentType("text/html");
            //response.setStatus(HttpServletResponse.SC_OK);
            //response.getWriter().println("<h1>Hello</h1>");
            //((Request)request).setHandled(true);

            //find cookie
            String packageToUse = null;
            if(proxySettings.usecookies)
            {
                for(Cookie cookie: this.getCookies(request)) //the normal getCookies can return a null for some reason.
                {
                    if(cookie.getName().equals("DrizzlePackage"))
                    {
                        packageToUse = cookie.getValue();
                    }
                }
            }
            
            String domain = request.getServerName();
            
            packageInfo p = null;
            if(packageToUse!=null)
            {
                p = proxySettings.packages.getPackageByName(packageToUse);
                if(p==null)
                {
                    m.err("Package no longer exists: "+packageToUse);
                    return;
                }
                if(!p.handlesDomain(domain))
                {
                    m.warn(p.name+" doesn't handle domain: "+domain);
                    p = null;
                }
            }
            if(p==null)
            {
                p = proxySettings.packages.findPackageThatHandlesDomain(domain);
            }
            if(p==null)
            {
                m.warn("No package can be found that handles domain: "+domain);
                return;
            }
            
            //allow p to handle it.
            p.handle(target, request, response, dispatch);

            //each config file (which may be in the .zip) contains:
            //list of entry addresses, list of domain names
            //we will try to maintain a cookie, so we know to use the same package, but if the cookie is lost, we will just use the first package with the domain name, and if there are other packages with that domain, log a warning.
            
            */
        }
    }
}

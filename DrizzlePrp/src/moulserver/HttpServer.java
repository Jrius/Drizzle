/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.*;
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
import java.io.OutputStream;
import java.sql.ResultSet;
import moulserver.Results;
import moulserver.Node.*;
import org.eclipse.jetty.util.log.Logger;


public class HttpServer extends SharedServer
{
    Server server;
    Manager manager;

    public HttpServer(Manager manager)
    {
        this.manager = manager;
    }

    public void run()
    {
        m.msg("Starting Http Server...");
        final int port = 80;
        try{
            //System.setProperty("DEBUG", "true");
            //String loggername = DustLogger.class.getCanonicalName(); //should be "moulserver.HttpServer.DustLogger".
            String loggername = DustLogger.class.getName();
            //System.setProperty("org.mortbay.log.class", loggername);
            System.setProperty("org.eclipse.jetty.util.log.class", loggername);
            server = new Server();
            Connector connector=new SocketConnector();
            connector.setPort(port);
            server.setConnectors(new Connector[]{connector});
            Handler handler = new DustHandler();
            server.setHandler(handler);

            server.start();
            
            //server.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static class DustLogger implements Logger
    {
        String name;
        boolean debug = false;

        public DustLogger() //must have an argumentless constructor.
        {
            this("DustLogger");
        }
        public DustLogger(String name)
        {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void warn(String string, Object... os) {
            m.warn("JETTY WARN: "+string);
        }

        public void warn(Throwable thrwbl) {
            m.warn("JETTY WARN: "+thrwbl.getMessage());
        }

        public void warn(String string, Throwable thrwbl) {
            m.warn("JETTY WARN: "+string);
            m.warn("JETTY WARN: "+thrwbl.getMessage());
        }

        public void info(String string, Object... os) {
            //m.status("JETTY INFO: "+string);
        }

        public void info(Throwable thrwbl) {
            //m.status("JETTY INFO: "+thrwbl.getMessage());
        }

        public void info(String string, Throwable thrwbl) {
            //m.status("JETTY INFO: "+string);
            //m.status("JETTY INFO: "+thrwbl.getMessage());
        }

        public boolean isDebugEnabled() {
            return debug;
        }

        public void setDebugEnabled(boolean bln) {
            debug = bln;
        }

        public void debug(String string, Object... os) {
            //m.msg("JETTY DEBUG: "+string);
        }

        public void debug(Throwable thrwbl) {
            //m.msg("JETTY DEBUG: "+thrwbl.getMessage());
        }

        public void debug(String string, Throwable thrwbl) {
            //m.msg("JETTY DEBUG: "+string);
            //m.msg("JETTY DEBUG: "+thrwbl.getMessage());
        }

        public Logger getLogger(String string) {
            return new DustLogger(string);
        }
    }
    public class DustHandler extends AbstractHandler
    {

        public DustHandler()
        {
        }

        //public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
        public void handle(String target, Request req2, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
        {
            m.msg("HttpServer: Handling target: ",target);

            if(target.equals("/"))
            {
                handleSimpleString(request,response,"<html><body>Welcome to the root!</body></html>");
            }
            else if(target.equals("/serverstatus/moullive.php")) //support.cyanworlds.com
            {
                handleSimpleString(request,response,"This is the server status!");
            }
            else if(target.equals("/signup.html")) //www.mystonline.com
            {
                handleSimpleString(request,response,"Please signup, if you like!");
            }
            else if(target.equals("/admin.html"))
            {
                handleAdmin(request,response);
            }
            else
            {
                m.warn("HttpServer unhandled url: ",target);
            }
        }

        public void handleSimpleString(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException
        {
            javax.servlet.ServletOutputStream out = response.getOutputStream();
            out.print(msg);
            response.setStatus(HttpServletResponse.SC_OK);
            ((Request)request).setHandled(true);
        }

        public void handleAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException
        {
            /*java.util.Enumeration en = request.getParameterNames();
            while(en.hasMoreElements())
            {
                String s = (String)en.nextElement();
                m.msg(s);
            }*/
            javax.servlet.ServletOutputStream out = response.getOutputStream();
            out.print(
                "<html>"+
                "<header><title>DrizzleTalcum Administration</title></header>"+
                "<body>"
            );
            out.print(
                "  Welcome to the DrizzleTalcum Administration panel!<br/><br/>"+
                "  <form name='admin' method='post'>"+
                "    Password:<input type='password' name='password' /><br/>"+
                "    <button type='submit' value='resetdatabase' name='command'>Reset database</button><br/>"+
                "    <button type='submit' value='ping' name='command'>Ping</button><br/>"+
                "    <input type='text' name='createuser_username' /><input type='text' name='createuser_password' /><button type='submit' value='createuser' name='command'>Add User</button><br/>"+
                "    <button type='submit' value='listusers' name='command'>List users</button><br/>"+
                "    <button type='submit' value='test' name='command'>Test</button><br/>"+
                "    <button type='submit' value='listplayers' name='command'>List Players</button><br/>"+
                "    <input type='text' name='dumpplayer_username' /><button type='submit' value='dumpplayer' name='command'>Dump player info</button><br/>"+
                "    <button type='submit' value='dumpvault' name='command'>List entire vault</button><br/>"+
                "    <input type='text' name='dumpnode_idx' /><button type='submit' value='dumpnode' name='command'>Dump node info</button><br/>"+
                "  </form>"+
                "  <br/>"
            );

            
            String mainpassword = request.getParameter("password");
            if(mainpassword==null)
            {
            }
            else if(!mainpassword.equals(manager.settings.getMainPassword()))
            {
                out.print("Wrong password!<br/>");
            }
            else
            {
                try
                {
                    String command = request.getParameter("command"); if(command==null) command="";

                    if(command.equals("ping"))
                    {
                        out.print("Ping!<br/>");
                    }
                    else if(command.equals("resetdatabase"))
                    {
                        manager.database.reset();
                        out.print("Database reset!<br/>");
                    }
                    else if(command.equals("createuser"))
                    {
                        String username = request.getParameter("createuser_username");
                        String password = request.getParameter("createuser_password");
                        manager.database.AddUser(username,password);
                        out.print("User created!");
                    }
                    else if(command.equals("listusers"))
                    {
                        Results results = manager.database.sqlquery("SELECT accountname,passwordhash FROM accounts");
                        boolean hasrow = results.first();
                        while(hasrow)
                        {
                            String acc = results.getString("accountname");
                            byte[] hash = results.getBytes("passwordhash");
                            out.print("Username:"+acc+" pwhash:"+b.BytesToHexString(hash)+"<br/>");
                            hasrow = results.next();
                        }
                        out.print("Done listing users!");
                    }
                    else if(command.equals("test"))
                    {
                        //ResultSet r = Manager.database.sqlquery("SELECT NEXTVAL('abc')");
                        //Manager.database.sqlupdate("CREATE SEQUENCE abc;");
                        //Results r = manager.database.sqlquery("SELECT NEXTVAL('abc');");
                        //r.first();
                        //long num = r.getLong(1);

                        //set owner and seen:
                        String sql = "UPDATE ref_vault SET seen=1, owner_idx=73";
                        manager.database.sqlupdate(sql);
                        int dummy=0;
                    }
                    else if(command.equals("listplayers"))
                    {
                        for(PlayerNode player: PlayerNode.findAll())
                        {
                            out.print("<br/>PlayerName="+player.getPlayerName()+" AvatarShape="+player.getAvatarShape());
                        }
                        out.print("<br/>Done listing players!");
                    }
                    else if(command.equals("dumpplayer"))
                    {
                        String playername = request.getParameter("dumpplayer_username");

                        PlayerNode player = PlayerNode.findByName(playername);
                        String s = NodeUtils.ReportTree(player);
                        //s = s.replace("\n","</br>");
                        for(Node.Ref ref: Node.FindTreeRefs(player.getIdx()))
                        {
                            s += "\n" + ref.dump();
                        }
                        s = "<pre>"+EscapeUtils.escapeHtmlString(s)+"</pre>";
                        handleSimpleString(request, response, s);
                    }
                    else if(command.equals("dumpvault"))
                    {
                        Results results = manager.database.sqlquery("SELECT * FROM vault");
                        boolean hasrow = results.first();
                        while(hasrow)
                        {
                            Node curnode = Node.getNode(results);
                            out.print("<br/>"+curnode.toString());
                            hasrow = results.next();
                        }
                        out.print("<br/><br/>Refs:<br/>");

                        results = manager.database.sqlquery("SELECT * FROM ref_vault");
                        for(Node.Ref ref: results.castAsRefs())
                        {
                            out.print("<br/>"+ref.dump());
                        }

                        out.print("<br/>Done listing entire vault!");
                    }
                    else if(command.equals("dumpnode"))
                    {
                        String idxstr = request.getParameter("dumpnode_idx");
                        int idx = Integer.parseInt(idxstr);

                        Node node = Node.getNodeWithIndex(idx);
                        String s = NodeUtils.ReportTree(node);
                        s = "<pre>"+EscapeUtils.escapeHtmlString(s)+"</pre>";
                        handleSimpleString(request, response, s);
                    }
                    else
                    {
                        out.print("Error: unknown command: "+command);
                    }
                }
                catch(Exception e)
                {
                    java.io.PrintStream ps = new java.io.PrintStream(out);
                    e.printStackTrace(ps);
                }

            }


            out.print(
                "</body>"+
                "</html>"
            );
            response.setStatus(HttpServletResponse.SC_OK);
            ((Request)request).setHandled(true);
        }
    }

}

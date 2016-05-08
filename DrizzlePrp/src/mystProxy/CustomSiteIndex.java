/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

public class CustomSiteIndex implements ICustomSite
{

    public void handle(packageInfo p, String domain, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        if(target.toLowerCase().equals("/startpages.html"))
        {
            StringBuilder s = new StringBuilder();
            s.append("<html><head><title>Drizze/WebMystServer</title></head><body>");
            for(packageInfo pack: proxySettings.packages.packages)
            {
                s.append("<h3>Package name: "+pack.name+"</h3></br></br>");
                
                s.append("Start Pages:</br>");
                for(String startpage: pack.startPages)
                {
                    s.append("<a href='"+startpage+"'>"+startpage+"</a></br>");
                }
                s.append("</br>");
                
                s.append("Custom Site Handler: ");
                if(pack.customSiteHandler==null) s.append("none");
                else s.append(pack.customSiteHandler.getClass().getName());
                s.append("</br></br>");
                
                s.append("Folder/Zipfile: ");
                if(pack.isZip) s.append("Zipfile");
                else s.append("Folder");
                s.append("</br></br>");
                
                s.append("Domains handled:</br>");
                for(String domainhandled: pack.domainsHandled)
                {
                    s.append(domainhandled+"</br>");
                }
                s.append("</br>");
                
                s.append("</br></br>");
            }
            s.append("</body></html>");
            response.getWriter().print(s.toString());
            fileHandler.handleDone(request, response);
        }
        else
        {
            //do the normal handling.
            fileHandler.handle3(p, domain, target, request, response, dispatch);
            //fileHandler.handle(null, target, request, response, dispatch);
        }
    }

}

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
import shared.m;

public class CustomSiteTheRivenJournals implements ICustomSite
{

    public void handle(packageInfo p, String domain, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
    {
        target = target.toLowerCase();
        fileHandler.disableCaching(response);
        
        //redirect folder references.
        if(target.equals("/")) target = "/index.html";
        
        //get cookies
        boolean s1=false, s2=false, s3=false, s4=false, s5=false, s6=false, s7=false;
        boolean nosound=false, nofixedstarfield=false;
        String stamps="";
        for(Cookie c: fileHandler.getCookies(request))
        {
            String name = c.getName();
            if(name.equals("s1")) s1 = (c.getValue().equals("1"));
            if(name.equals("s2")) s2 = (c.getValue().equals("1"));
            if(name.equals("s3")) s3 = (c.getValue().equals("1"));
            if(name.equals("s4")) s4 = (c.getValue().equals("1"));
            if(name.equals("s5")) s5 = (c.getValue().equals("1"));
            if(name.equals("s6")) s6 = (c.getValue().equals("1"));
            if(name.equals("s7")) s7 = (c.getValue().equals("1"));
            if(name.equals("nosound")) nosound = (c.getValue().equals("1"));
            if(name.equals("nofixedstarfield")) nofixedstarfield = (c.getValue().equals("1"));
            if(name.equals("stamps")) stamps = c.getValue();
        }
        
        //create cookies
        if(target.equals("/help/cookie.cgi"))
        {
            response.addCookie(createCookie("s1","1"));
            s1 = true;
            response.sendRedirect("/index.html");
            return;
        }
        if(target.equals("/help/maillist.cgi"))
        {
            //target = "/help/welcome.html";
            response.sendRedirect("/help/welcome.html");
            return;
        }
        if(target.equals("/journal1/cookie.cgi"))
        {
            response.addCookie(createCookie("s2","1"));
            s2 = true;
            response.sendRedirect("/journal1/solved.html");
            return;
        }
        if(target.equals("/journal2/cookie.cgi"))
        {
            response.addCookie(createCookie("s3","1"));
            s3 = true;
            response.sendRedirect("/journal2/solved.html");
            return;
        }
        if(target.equals("/journal3/maglev_cookie.cgi"))
        {
            response.addCookie(createCookie("s4","1"));
            s4 = true;
            response.sendRedirect("/journal3/solved.html");
            return;
        }
        if(target.equals("/journal4/chron_cookie.cgi"))
        {
            response.addCookie(createCookie("s5","1"));
            s5 = true;
            response.sendRedirect("/journal4/solved.html");
            return;
        }
        if(target.equals("/journal5/initial/grid_cookie.cgi"))
        {
            response.addCookie(createCookie("s6","1"));
            s6 = true;
            response.sendRedirect("/journal5/post_initial/index.html");
            return;
        }
        if(target.equals("/journal5/post_initial/set_state.cgi"))
        {
            int dummy=0;
            String code = request.getQueryString();
            m.msg("Code encountered:",code);
            if(code.equals("paul23")) response.addCookie(createCookie("stamps","1"));
            if(code.equals("peter27")) response.addCookie(createCookie("stamps","2"));
            if(code.equals("timothy29")) response.addCookie(createCookie("stamps","3"));
            if(code.equals("lance31")) response.addCookie(createCookie("stamps","4"));
            //stamps = "1"; //we're returning anyway...
            return;
        }
        if(target.equals("/journal5/post_initial/p5_cookie.cgi"))
        {
            response.addCookie(createCookie("s7","1"));
            s7 = true;
            response.sendRedirect("/journal5/post_initial/complete_state.html");
            return;
        }
        //settings
        if(target.equals("/webmystserver-context/clearprogress.cgi"))
        {
            response.addCookie(createCookie("s1","0"));
            response.addCookie(createCookie("s2","0"));
            response.addCookie(createCookie("s3","0"));
            response.addCookie(createCookie("s4","0"));
            response.addCookie(createCookie("s5","0"));
            response.addCookie(createCookie("s6","0"));
            response.addCookie(createCookie("s7","0"));
            response.addCookie(createCookie("stamps","0"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        if(target.equals("/webmystserver-context/clearsettings.cgi"))
        {
            response.addCookie(createCookie("nofixedstarfield","0"));
            response.addCookie(createCookie("nosound","0"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        if(target.equals("/webmystserver-context/enablefixedstarfield.cgi"))
        {
            response.addCookie(createCookie("nofixedstarfield","0"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        if(target.equals("/webmystserver-context/disablefixedstarfield.cgi"))
        {
            response.addCookie(createCookie("nofixedstarfield","1"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        if(target.equals("/webmystserver-context/enablesound.cgi"))
        {
            response.addCookie(createCookie("nosound","0"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        if(target.equals("/webmystserver-context/disablesound.cgi"))
        {
            response.addCookie(createCookie("nosound","1"));
            response.sendRedirect("/webmystserver-context/index.html");
            return;
        }
        
        //redirect based on cookies.
        if(target.equals("/journal1/sound.html"))
        {
            if(nosound) return;
        }
        if(target.equals("/journal2/sound.html"))
        {
            if(nosound) return;
        }
        if(target.equals("/journal3/sound.html"))
        {
            if(nosound) return;
        }
        if(target.equals("/journal4/sound.html"))
        {
            if(nosound) return;
        }
        if(target.equals("/journal5/sound.html"))
        {
            if(nosound) return;
        }
        if(target.equals("/index.html"))
        {
            if(!s1) target = "/index0.html";
            else target = "/index1.html";
        }
        if(target.equals("/temple.html"))
        {
            if(!s7) target = "/temple0.html";
            else target = "/temple1.html";
        }
        if(target.equals("/journal1/entry.html"))
        {
            if(!s2) target = "/journal1/entry0.html";
            else target = "/journal1/entry1.html";
        }
        if(target.equals("/journal1/immersive1.html"))
        {
            if(!s2) target = "/journal1/immersive10.html";
            else target = "/journal1/immersive11.html";
        }
        if(target.equals("/journal1/external0.html"))
        {
            if(!s2) target = "/journal1/external00.html";
            else target = "/journal1/external01.html";
        }
        if(target.equals("/journal1/page1.html"))
        {
            if(!s2) target = "/journal1/page10.html";
            else target = "/journal1/page11.html";
        }
        if(target.equals("/journal1/page2.html"))
        {
            if(!s2) target = "/journal1/page20.html";
            else target = "/journal1/page21.html";
        }
        if(target.equals("/journal1/page3.html"))
        {
            if(!s2) target = "/journal1/page30.html";
            else target = "/journal1/page31.html";
        }
        if(target.equals("/journal1/page4.html"))
        {
            if(!s2) target = "/journal1/page40.html";
            else target = "/journal1/page41.html";
        }
        if(target.equals("/journal1/page5.html"))
        {
            if(!s2) target = "/journal1/page50.html";
            else target = "/journal1/page51.html";
        }
        if(target.equals("/journal2/entry.html"))
        {
            if(!s3) target = "/journal2/entry0.html";
            else target = "/journal2/entry1.html";
        }
        if(target.equals("/journal2/immersive1.html"))
        {
            if(!s3) target = "/journal2/immersive10.html";
            else target = "/journal2/immersive11.html";
        }
        if(target.equals("/journal2/external0.html"))
        {
            if(!s3) target = "/journal2/external00.html";
            else target = "/journal2/external01.html";
        }
        if(target.equals("/journal2/page1.html"))
        {
            if(!s3) target = "/journal2/page10.html";
            else target = "/journal2/page11.html";
        }
        if(target.equals("/journal2/page2.html"))
        {
            if(!s3) target = "/journal2/page20.html";
            else target = "/journal2/page21.html";
        }
        if(target.equals("/journal2/page3.html"))
        {
            if(!s3) target = "/journal2/page30.html";
            else target = "/journal2/page31.html";
        }
        if(target.equals("/journal2/page4.html"))
        {
            if(!s3) target = "/journal2/page40.html";
            else target = "/journal2/page41.html";
        }
        if(target.equals("/journal2/page5.html"))
        {
            if(!s3) target = "/journal2/page50.html";
            else target = "/journal2/page51.html";
        }
        if(target.equals("/journal3/entry.html"))
        {
            if(!s4) target = "/journal3/entry0.html";
            else target = "/journal3/entry1.html";
        }
        if(target.equals("/journal3/immersive1.html"))
        {
            if(!s4) target = "/journal3/immersive10.html";
            else target = "/journal3/immersive11.html";
        }
        if(target.equals("/journal3/external0.html"))
        {
            if(!s4) target = "/journal3/external00.html";
            else target = "/journal3/external01.html";
        }
        if(target.equals("/journal3/page1.html"))
        {
            if(!s4) target = "/journal3/page10.html";
            else target = "/journal3/page11.html";
        }
        if(target.equals("/journal3/page2.html"))
        {
            if(!s4) target = "/journal3/page20.html";
            else target = "/journal3/page21.html";
        }
        if(target.equals("/journal3/page3.html"))
        {
            if(!s4) target = "/journal3/page30.html";
            else target = "/journal3/page31.html";
        }
        if(target.equals("/journal3/page4.html"))
        {
            if(!s4) target = "/journal3/page40.html";
            else target = "/journal3/page41.html";
        }
        if(target.equals("/journal3/page5.html"))
        {
            if(!s4) target = "/journal3/page50.html";
            else target = "/journal3/page51.html";
        }
        if(target.equals("/journal4/entry.html"))
        {
            if(!s5) target = "/journal4/entry0.html";
            else target = "/journal4/entry1.html";
        }
        if(target.equals("/journal4/immersive1.html"))
        {
            if(!s5) target = "/journal4/immersive10.html";
            else target = "/journal4/immersive11.html";
        }
        if(target.equals("/journal4/external0.html"))
        {
            if(!s5) target = "/journal4/external00.html";
            else target = "/journal4/external01.html";
        }
        if(target.equals("/journal4/page1.html"))
        {
            if(!s5) target = "/journal4/page10.html";
            else target = "/journal4/page11.html";
        }
        if(target.equals("/journal4/page2.html"))
        {
            if(nofixedstarfield)
            {
                if(!s5) target = "/journal4/page20.html";
                else target = "/journal4/page21.html";
            }
            else
            {
                if(!s5) target = "/journal4/page20sf.html";
                else target = "/journal4/page21sf.html";
            }
        }
        if(target.equals("/journal4/immersive2.html"))
        {
            if(nofixedstarfield) target = "/journal4/immersive2.html";
            else target = "/journal4/immersive2sf.html";
        }
        if(target.equals("/journal4/page3.html"))
        {
            if(!s5) target = "/journal4/page30.html";
            else target = "/journal4/page31.html";
        }
        if(target.equals("/journal4/page4.html"))
        {
            if(!s5) target = "/journal4/page40.html";
            else target = "/journal4/page41.html";
        }
        if(target.equals("/journal4/page5.html"))
        {
            if(!s5) target = "/journal4/page50.html";
            else target = "/journal4/page51.html";
        }
        if(target.equals("/table5.html"))
        {
            if(!s6) target = "/table50.html";
            else target = "table51.html";
        }
        if(target.equals("/journal5/post_initial/stamp_mech.html"))
        {
            if(stamps.equals("1")) target = "/journal5/post_initial/stamp_mech1.html";
            else if(stamps.equals("2")) target = "/journal5/post_initial/stamp_mech2.html";
            else if(stamps.equals("3")) target = "/journal5/post_initial/stamp_mech3.html";
            else if(stamps.equals("4")) target = "/journal5/post_initial/stamp_mech4.html";
            else target = "/journal5/post_initial/stamp_mech0.html";
        }
        
        

        //otherwise just grab the normal file.
        fileHandler.handle3(p, domain, target, request, response, dispatch);
        
    }

    Cookie createCookie(String name, String value)
    {
        Cookie result = new Cookie(name,value);
        result.setMaxAge(60*60*24*365*10); //10 years.
        result.setPath("/");
        return result;
    }
}

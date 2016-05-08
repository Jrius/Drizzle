/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;


public interface ICustomSite
{
    
    public void handle(packageInfo p, String domain, String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException;


}

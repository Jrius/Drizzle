/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mystProxy;

import java.util.Vector;

public class allPackages
{
    public Vector<packageInfo> packages;
    
    public allPackages()
    {
        packages = new Vector<packageInfo>();
    }
    
    public packageInfo getPackageByName(String name)
    {
        for(packageInfo p: packages)
        {
            if(p.name.equals(name)) return p;
        }
        return null;
    }
    
    public packageInfo findPackageThatHandlesDomain(String domain)
    {
        for(packageInfo p:packages)
        {
            for(String s: p.domainsHandled)
            {
                if(domain.toLowerCase().equals(s.toLowerCase())) return p;
            }
        }
        return null;
    }
}

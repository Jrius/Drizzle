/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parsers;

import prpobjects.textfile;
import java.util.Vector;
import shared.*;

public class ageparser
{
    public static Agefile parse(byte[] data, String agename)
    {
        Agefile r = new Agefile();
        r.agename = agename;
        textfile t = textfile.createFromBytes(data);
        for(textfile.textline line: t.getLines())
        {
            String l = line.getString();
            String[] parts = l.split("=");
            if(parts.length==2)
            {
                String varname = parts[0].trim();
                if(varname.equals("SequencePrefix"))
                {
                    String prefixstr = parts[1].trim();
                    if(r.sequenceprefix!=null) m.throwUncaughtException("The sequence prefix was already set in this file: "+agename);
                    r.sequenceprefix = Integer.parseInt(prefixstr);
                }
                else if(varname.equals("Page"))
                {
                    String[] pageparts = parts[1].split(",");
                    PageInfo page = new PageInfo();
                    page.pagename = pageparts[0].trim();
                    page.pagenum = Integer.parseInt(pageparts[1].trim());
                    if(pageparts.length==3)
                    {
                        String dynstr = pageparts[2].trim();
                        if(dynstr.equals("1")) page.dynamicloading = true;
                        else if(dynstr.equals("0")) page.dynamicloading = false;
                        else m.throwUncaughtException("Unexptected value in .age file: "+agename);
                    }
                    r.pages.add(page);
                }
            }
        }
        return r;
    }

    public static class Agefile
    {
        public String agename;
        public Integer sequenceprefix;
        public Vector<PageInfo> pages = new Vector();

        public PageInfo getPage(int pagenum)
        {
            for(PageInfo page: pages)
            {
                if(page.pagenum==pagenum) return page;
            }
            return null;
        }
    }

    public static class PageInfo
    {
        public String pagename;
        public int pagenum;
        public boolean dynamicloading;
    }
}

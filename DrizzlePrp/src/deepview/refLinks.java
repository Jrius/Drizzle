/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import java.util.Vector;
import shared.reporter.ReportEvent;
import prpobjects.Uruobjectdesc;

public class refLinks
{
    Vector<refLink> reflinks=new Vector<refLink>();
    //boolean acceptNewEntries=false;
    
    public static class refLink
    {
        public Uruobjectdesc from;
        public Uruobjectdesc to;
        
        public refLink(Uruobjectdesc from, Uruobjectdesc to)
        {
            this.from = from;
            this.to = to;
        }
    }
    
    public void add(Uruobjectdesc from, Uruobjectdesc to)
    {
        //if(!acceptNewEntries) return;
        if(from==null || to==null) return;
        if(from.equals(to)) return; //skip self-references.
        reflinks.add(new refLink(from,to));
    }
    
    
    shared.reporter.ReportListener listener = new shared.reporter.ReportListener() {
        public void handleEvent(ReportEvent reportevent) {
            uru.reporterReports.refEncountered event = (uru.reporterReports.refEncountered)reportevent;
            add(event.from, event.to);
        }
    };
    
    public void startListening()
    {
        shared.reporter.registerListener(listener, "refEncountered");
    }
    public void stopListening()
    {
        shared.reporter.removeListener(listener, "refEncountered");
    }
}

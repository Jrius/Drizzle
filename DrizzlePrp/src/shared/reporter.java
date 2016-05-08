/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Vector;
import java.util.HashMap;


public abstract class reporter
{
    
    //static Vector<registration> listeners=new Vector<registration>();
    static HashMap<String,Vector<ReportListener>> listeners = new HashMap<String,Vector<ReportListener>>();
    
    /*static class registration
    {
        //Class cls;
        String eventname;
        ReportListener listener;
        
        public registration(String eventname, ReportListener listener)
        {
            //this.cls = cls;
            this.eventname = eventname;
            this.listener = listener;
        }
    }*/
    
    static Vector<ReportListener> getListeners(String eventname)
    {
        Vector<ReportListener> listenersForEvent = listeners.get(eventname);
        if(listenersForEvent==null) return new Vector<ReportListener>();
        return listenersForEvent;
    }
    
    public static void registerListener(ReportListener listener, String eventname)
    {
        Vector<ReportListener> listenersForEvent = listeners.get(eventname);
        if(listenersForEvent==null)
        {
            Vector<ReportListener> newListeners = new Vector<ReportListener>();
            listeners.put(eventname, newListeners);
            newListeners.add(listener);
        }
        else
        {
            listenersForEvent.add(listener);
        }
        //if(listeners.get(eventname))
        //listeners.put(eventname, value)
        //listeners.add(new registration(eventname,listener));
    }
    public static void removeListener(ReportListener listener, String eventname)
    {
        getListeners(eventname).remove(listener);
        /*for(registration r: listeners)
        {
            if(r.listener==listener)
            {
                listeners.remove(r);
            }
        }*/
    }
    
    public static void reportEvent(ReportEvent reportevent, String eventname)
    {
        //Class c = reportevent.getClass();
        /*for(registration r: listeners)
        {
            if(r.cls.equals(c))
            {
                r.listener.handleEvent(reportevent);
            }
        }*/
        for(ReportListener listener: getListeners(eventname))
        {
            listener.handleEvent(reportevent);
        }
    }
    
    public static interface ReportListener
    {
        void handleEvent(ReportEvent reportevent);
    }
    public static interface ReportEvent
    {
        
    }
}

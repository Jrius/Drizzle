/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import javax.swing.JPanel;
import shared.FileUtils;
import shared.reporter.ReportEvent;
import uru.context;
import uru.Bytestream;
import prpobjects.PrpHeader;
import prpobjects.PrpObjectIndex;
import java.util.Vector;
import prpobjects.PrpRootObject;
import prpobjects.prpprocess;
import shared.m;
import prpobjects.prpfile;
import prpobjects.Typeid;
import prpobjects.uruobj;
import prpobjects.Uruobjectref;
import prpobjects.Uruobjectdesc;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

import java.lang.reflect.Field;
import java.util.Vector;
import java.util.Set;
import java.util.EnumSet;
import java.io.File;
import prpobjects.prputils;
import shared.Bytes;
import prpobjects.Urustring;
import prpobjects.*;
import shared.*;

public class deepview
{
    public Vector<Uruobjectref> allrefs = new Vector<Uruobjectref>();
    public Set<Typeid> alltypes = EnumSet.noneOf(Typeid.class);
    public refLinks allreflinks = new refLinks();
    //public prpfile prp;
    public Vector<prpfile> prps = new Vector<prpfile>();
    public JDesktopPane desktop;
    
    //these are used so a modifyable element, like dvUruobjectref, can keep track of who he belongs to.
    public prpfile curprp;
    public PrpRootObject curobj;
    
    public deepview(JDesktopPane desktop2)
    {
        desktop = desktop2;
    }
    public void reportStrings()
    {
        Vector<Urustring> urustrings = shared.FindAllDescendants.FindAllDescendantsByClass(Urustring.class, prps);
        Vector<Bstr> bstrings = shared.FindAllDescendants.FindAllDescendantsByClass(Bstr.class, prps);
        Vector<Wpstr> wpstrings = shared.FindAllDescendants.FindAllDescendantsByClass(Wpstr.class, prps);
        for(Urustring s: urustrings) m.msg(s.toString());
        for(Bstr s: bstrings) m.msg(s.toString());
        for(Wpstr s: wpstrings) m.msg(s.toString());
    }
    public void reportRefs()
    {
        for(Uruobjectref ref: allrefs)
        {
            m.msg(ref.toString());
        }
    }
    public prpfile addPrp(String filename)
    {
        //allreflinks.reflinks.clear();
        byte[] filedata = FileUtils.ReadFile(filename);
        context c = context.createFromBytestream(new Bytestream(filedata));
        c.curFile = filename;
        //allreflinks.acceptNewEntries = true;
        allreflinks.startListening();
        prpobjects.prpprocess.ProcessAllObjects(c.Fork(), false); //parse all to find all Uruobjectdesc links.
        allreflinks.stopListening();
        //allreflinks.acceptNewEntries = false;
        prpfile prp = prpobjects.prpprocess.ProcessAllObjects(c,true);
        prp.filename = filename;
        prps.add(prp);
        
        //get all Uruobjectrefs.
        //allrefs = new Vector<Uruobjectref>();
        for(PrpRootObject curRootObject: prp.objects2)
        {
            if(curRootObject!=null)
            {
                if(curRootObject.header==null)
                {
                    int dummy=0;
                }
                if(curRootObject.header.desc==null)
                {
                    int dummy=0;
                }
                allrefs.add(Uruobjectref.createFromUruobjectdesc(curRootObject.header.desc));
                alltypes.add(curRootObject.header.desc.objecttype);
            }
        }
        
        return prp;
    }
    
    public void clearAll()
    {
        //desktop.removeAll();
        for(JInternalFrame frame: desktop.getAllFrames())
        {
            frame.doDefaultCloseAction();
        }
        
        prps = new Vector<prpfile>();
        allreflinks = new refLinks();
        allrefs = new Vector<Uruobjectref>();
        alltypes.clear();// = EnumSet.noneOf(Typeid.class);
    }
    
    public void openType(Typeid typeid)
    {
        dvWindow newwindow = new dvWindow();
        desktop.add(newwindow);//, javax.swing.JLayeredPane.DEFAULT_LAYER);
        newwindow.moveToFrontAndSelect();
        
        newwindow.mainPanel.add(new dvAllObjectsOfType(this,typeid));
    }
    
    public void openRef(Uruobjectdesc desc)
    {
        dvWindow newwindow = new dvWindow();
        desktop.add(newwindow);//, javax.swing.JLayeredPane.DEFAULT_LAYER);
        newwindow.moveToFrontAndSelect();

        for(prpfile prp: prps)
        {
            curprp = prp;
            for(PrpRootObject curRootObject: prp.objects2)
            {
                curobj = curRootObject;
                if(curRootObject==null)
                {
                    continue;
                }
                if(curRootObject.header.desc.equals(desc))
                {
                    try
                    {
                        curRootObject.parseRawDataNow(); //actually parse the object.
                        reflect(curRootObject,newwindow.mainPanel);
                    }
                    catch(Exception e)
                    {
                        m.err("Error during reflection.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void openAllTypes()
    {
        dvWindow newwindow = new dvWindow();
        desktop.add(newwindow);
        newwindow.moveToFrontAndSelect();
        
        newwindow.mainPanel.add(new dvAllObjectTypes(this));
    }
    public void openAllObjects()
    {
        dvWindow newwindow = new dvWindow();
        desktop.add(newwindow);
        newwindow.moveToFrontAndSelect();
        
        newwindow.mainPanel.add(new dvAllObjects(this));
        
    }
    
    public void saveChanges(String foldername)
    {
        for(prpfile prp: prps)
        {
            boolean hasChanged = false;
            for(PrpRootObject curRootObject: prp.objects2)
            {
                if(curRootObject.hasChanged)
                {
                    hasChanged = true;
                    break;
                }
            }
            if(hasChanged)
            {
                //then save changes.
                String filename2 = prp.filename;
                String filename = foldername+"/" + new File(filename2).getName();
                //Bytes result = prp.saveAsBytes();
                //FileUtils.WriteFile(filename, result);
                prp.saveAsFile(filename);
            }
        }
        
    }
    
    public void read(String filename)
    {
                
        //process all the objects as raw data.
        //allreflinks = new refLinks();
        /*allreflinks.reflinks.clear();
        byte[] filedata = FileUtils.ReadFile(filename);
        context c = context.createFromBytestream(new Bytestream(filedata));
        c.curFile = filename;
        allreflinks.acceptNewEntries = true;
        uru.moulprp.prpprocess.ProcessAllObjects(c.Fork(), false); //parse all to find all Uruobjectdesc links.
        allreflinks.acceptNewEntries = false;
        prp = uru.moulprp.prpprocess.ProcessAllObjects(c,true);
        
        //get all Uruobjectrefs.
        allrefs = new Vector<Uruobjectref>();
        for(PrpRootObject curRootObject: prp.objects)
        {
            allrefs.add(Uruobjectref.createFromUruobjectdesc(curRootObject.header.desc));
        }*/
        
        //if it ends with * then 
        File f = new File(filename);
        if(filename.endsWith("*"))
        {
            String name = f.getName();
            name = name.substring(0, name.length()-1).toLowerCase();
            File folder = f.getParentFile();
            for(File curfile: folder.listFiles())
            {
                if(curfile.getName().toLowerCase().startsWith(name))
                {
                    if(curfile.getName().toLowerCase().endsWith(".prp"))
                    {
                        prpfile prp = addPrp(curfile.getPath());
                    }
                }
            }
        }
        else
        {
            prpfile prp = addPrp(filename);
        }

        /*dvWindow newwindow = new dvWindow();
        desktop.add(newwindow);
        newwindow.moveToFrontAndSelect();
        
        newwindow.mainPanel.add(new dvAllObjects(this));*/
        
        /*for(PrpRootObject curRootObject: prp.objects)
        {
            try
            {
                curRootObject.parseRawDataNow(); //actually parse the object.
                reflect(curRootObject,newwindow.mainPanel);
            }
            catch(Exception e)
            {
                m.err("Error during reflection.");
                e.printStackTrace();
            }
        }*/
        
        
        
    }
    
    public void reflect(PrpRootObject obj, JPanel panel) throws Exception
    {
        uruobj uo = obj.getObject();
        //panel.setLayout(new FlowLayout());
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel panel2 = new dvPanel();
        panel.add(panel2);
        
        //panel2.add(new JLabel("Name:"+obj.header.desc.objectname.toString()));
        //panel2.add(new JLabel("Type:"+obj.header.desc.objecttype.toString()));

        //reflect2(panel2,uo,uo.getClass(),"putnamehere");
        reflect2(panel2,obj,obj.getClass(),"putnamehere");
        
        //panel.setVisible(false);
        //panel.setVisible(true);
        //panel.invalidate();
        //panel.validate();
        panel.revalidate();
        //panel.repaint();
        //uo.getClass();
    }
    
    public void reflect2(JPanel panel, Object obj, Class objclass, String name) throws Exception
    {
        if(objclass==null)
        {
            m.msg("null class encountered.");
            return;
        }
        if(obj==null)
        {
            panel.add(new JLabel("name:"+name+" type:"+objclass.getName()+" null-value"));
            //m.msg("null object encountered.");
            return;
        }
        
        m.msg("doing "+obj.toString() + " of class "+objclass.getName());
        
        //Class objclass = obj.getClass();
        
        if(objclass==Uruobjectref.class)
        {
            panel.add(new dvUruobjectref((Uruobjectref)obj,name,this,true));
            return;
        }
        else if(objclass==prpobjects.Urustring.class)
        {
            panel.add(new dvUrustring((Urustring)obj,name,this,true));
            return;
        }
        else if(objclass==prpobjects.Wpstr.class)
        {
            panel.add(new dvWpstr((Wpstr)obj,name,this));
            return;
        }
        else if(objclass==prpobjects.Bstr.class)
        {
            panel.add(new dvBstr((Bstr)obj,name,this));
            return;
        }
        else if(objclass==prpobjects.Transmatrix.class)
        {
            panel.add(new dvTransmatrix((Transmatrix)obj,name,this));
            return;
        }
        else if(objclass==shared.Flt.class)
        {
            panel.add(new dvFlt((Flt)obj,name,this));
            return;
        }
        
        if(objclass==PrpRootObject.class)
        {
            //PrpRootObject pro = (PrpRootObject)obj;
            //panel.add(dvWidgets.jlabel("PrpRootObject Name:"+pro.header.desc.objectname.toString()+"Type:"+pro.header.desc.objecttype.toString()));
            panel.add(new dvPrpRootObject((PrpRootObject)obj,this));

        }

        //if simple, just output
        if(objclass.isPrimitive()||objclass==String.class||objclass.isEnum()||objclass==Byte.class||objclass==Integer.class||objclass==Short.class||objclass==Boolean.class||objclass==Long.class||objclass==Float.class||objclass==Double.class||objclass==Character.class)
        {
            panel.add(new JLabel("name:"+name+" type:"+objclass.getName()+" value:"+obj.toString()));
        }
        //if array, do each element
        else if(objclass.isArray())
        {
            JPanel subpanel = new dvPanel();
            panel.add(subpanel);
            //m.msg("array unhandled.");
            //depthString(result,depth);
            //result.append("type:"+objclass.getName()+" array elements:");
            subpanel.add(new JLabel("name:"+name+" type:"+objclass.getName()+" array elements:"));
            int length = java.lang.reflect.Array.getLength(obj);
            if(length>100)
            {
                subpanel.add(new JLabel("Not displaying elements of array with more than 100 elements."));
                return;
            }
            for(int i=0;i<length;i++)
            {
                Object arrayitem = java.lang.reflect.Array.get(obj, i);
                Class arrayclass;
                if(arrayitem!=null)
                {
                    arrayclass = arrayitem.getClass(); //bugfix: get the actual class, not some ancestor or interface.
                }
                else
                {
                    arrayclass = objclass.getComponentType();
                    //m.warn("Null encountered in array.");
                    //return;
                }
                reflect2(subpanel,arrayitem,arrayclass,Integer.toString(i));
                //deepReflectionReportText(arrayitem,arrayitem.getClass(),result, depth+1);
            }
        }
        //otherwise, it's a class?
        else
        {
            if(objclass==Class.class) return;
            
            //JPanel subpanel = dvWidgets.jpanel();
            JPanel subpanel = new dvPanel();
            panel.add(subpanel);

            //depthString(result,depth);
            subpanel.add(dvWidgets.jlabel("name:"+name+" type:"+objclass.getName()+" class members:"));

            //if inherited, do parent
            Class superclass = objclass.getSuperclass();
            if(superclass!=Object.class && superclass!=prpobjects.uruobj.class) //get ancestor's info
            {
                //we don't actually use inheritance this way.
                //deepReflectionReport(obj,objclass.getSuperclass(),result, depth+0);
                m.msg("unhandled inheritance");
            }

            
            Field[] fields = objclass.getDeclaredFields(); //get all fields
            java.lang.reflect.AccessibleObject.setAccessible(fields, true);
            for(int i=0;i<fields.length;i++)
            {
                Class curclass;
                Object curfield = fields[i].get(obj);
                if(curfield!=null)
                {
                    curclass = curfield.getClass(); //bugfix: get the actual class, not some ancestor or interface.
                }
                else
                {
                    curclass = fields[i].getType();
                }
                //deepReflectionReportText(curfield,curclass,result,depth+1);
                String curname = fields[i].getName();
                reflect2(subpanel, curfield, curclass, curname);
            }
        }
    }
    
}

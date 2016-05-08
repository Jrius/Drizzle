/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import prpobjects.*;
import java.util.Vector;
import shared.m;

public class dvPrp extends dvNode
{
    //public prpfile prp;
    //String pagename;
    String pagename;

    public dvPrp(nodeinfo info)
    {
        //this.prp = prp;
        this.info = info;
        pagename = info.prp.header.pagename.toString();
        Vector<Vector<PrpRootObject>> oo = info.prp.getOrderedObjects();
        for(Vector<PrpRootObject> type: oo)
        {
            nodeinfo childinfo = new nodeinfo();
            childinfo.prp = info.prp;
            childinfo.root = info.root;
            this.children.add(new dvPrpType(childinfo, type));
        }
    }
    public boolean isLeaf()
    {
        return false;
    }
    public void onDoubleClick(guiTree tree)
    {
        tree.expandCurrentSelection();
    }
    public String toString()
    {
        return pagename;
    }
    public void onSave() throws Exception
    {
        if(info.prp.hasChanged())
        {
            m.status("Saving prp: "+info.prp.filename);
            info.prp.saveAsFile(info.prp.filename);
        }
    }
    public static class dvPrpType extends dvNode
    {
        Typeid type;
        Vector<PrpRootObject> objects;

        public dvPrpType(nodeinfo info, Vector<PrpRootObject> type)
        {
            this.info = info;
            this.objects = type;
            this.type = objects.get(0).header.objecttype;
        }
        public boolean isLeaf()
        {
            return false;
        }
        public void onDoubleClick(guiTree tree)
        {
            tree.expandCurrentSelection();
        }
        public void loadChildren()
        {
            children.clear();
            for(PrpRootObject ro: objects)
            {
                nodeinfo childinfo = new nodeinfo();
                childinfo.name = ro.header.desc.objectname.toString();
                childinfo.rootobject = ro;
                childinfo.prp = info.prp;
                childinfo.root = info.root;
                dvPrpRootObject child = new dvPrpRootObject(childinfo);
                children.add(child);
            }
        }
        public String toString()
        {
            return type.toString();
        }
    }

}

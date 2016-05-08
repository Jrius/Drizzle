/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import prpobjects.*;
import shared.m;

public class dvPrpRootObject extends dvNode
{
    //PrpRootObject ro;
    //String name;
    nodeinfo info;

    public dvPrpRootObject(nodeinfo info)
    {
        //this.ro = ro;
        this.info = info;
        PrpRootObject ro = (PrpRootObject)info.obj;
        //name = ro.header.desc.objectname.toString();
    }
    public String toString()
    {
        //return name;
        return info.name;
    }
    public void parseObject()
    {
        uruobj obj = ((PrpRootObject)info.rootobject).getObject(); //parses it here.
        info.obj = obj;
        info.cls = obj.getClass();
    }
    public void onDoubleClick(guiTree tree)
    {
        loadChildren();
        tree.createNewWindow(this);
    }
    public void loadChildren()
    {
        m.msg("loading children...");
        parseObject();
        dvNode[] childs = dvNode.findChildren(info);
        children.clear();
        for(dvNode child: childs)
        {
            children.add(child);
        }
    }
}

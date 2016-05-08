/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import prpobjects.*;
import java.util.Vector;
import shared.m;

public class dvAge extends dvNode
{
    String agename;
    //Vector<dvPrp> prps = new Vector();
    public dvAge(prpfile prp,nodeinfo info2)
    {
        info = info2;
        agename = prp.header.agename.toString();
        loadPrp(prp);
    }
    public String toString()
    {
        return agename;
    }
    public void loadPrp(prpfile prp)
    {
        nodeinfo childinfo = new nodeinfo();
        childinfo.prp = prp;
        childinfo.root = info.root;
        dvPrp dvp = new dvPrp(childinfo);
        children.add(dvp);
    }
    public void onDoubleClick(guiTree tree)
    {
        tree.expandCurrentSelection();
    }
    public void onSave() throws Exception
    {
        for(dvNode child: children)
        {
            child.onSave();
        }
    }
}

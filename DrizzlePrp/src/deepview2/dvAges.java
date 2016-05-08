/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.RowMapper;
import prpobjects.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.util.Vector;
import shared.m;
import javax.swing.JTree;
import java.util.HashSet;

public class dvAges extends dvNode
{
    dvGUI gui;
    guiTree gt;

    Vector<prpfile> _allprps = new Vector();
    HashSet<Uruobjectref> _allrefs = new HashSet();

    public dvAges(dvGUI gui)
    {
        info = new nodeinfo();
        info.root = this;
        this.gui = gui;
        //final javax.swing.JTree tree = gui.tree;
        gt = new guiTree(gui.tree, this, this.gui, false);
        //final dvAges ths = this;
        //gt.refreshTree();
    }

    public void onSave() throws Exception
    {
        for(dvNode child: children)
        {
            child.onSave();
        }
    }

    public void loadPrp(String filename)
    {
        prpfile prp = prpfile.createFromFile(filename, true);

        //remember resources
        _allprps.add(prp);
        for(Uruobjectref ref: prp.getAllRootObjectRefs())
        {
            _allrefs.add(ref);
        }

        String agename = prp.header.agename.toString();
        dvAge dvage = findDvAge(agename);
        if(dvage==null)
        {
            nodeinfo childnode = new nodeinfo();
            childnode.root = info.root;
            dvage = new dvAge(prp,childnode);
            children.add(dvage);
        }
        else
        {
            dvage.loadPrp(prp);
        }
        refreshTree();
    }
    public dvAge findDvAge(String agename)
    {
        for(dvNode n: children)
        {
            dvAge age = (dvAge)n;
            if(age.agename.equals(agename)) return age;
        }
        return null;
    }
    public void refreshTree()
    {
        gt.refreshTree();
    }
}

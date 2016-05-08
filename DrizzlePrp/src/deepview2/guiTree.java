/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import javax.swing.JTree;
import shared.m;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.ExpandVetoException;
import javax.swing.event.TreeSelectionEvent;
import java.awt.event.MouseEvent;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;

public class guiTree
{
    JTree tree;
    dvNode root;
    dvGUI gui;

    public guiTree(JTree tree, dvNode root, dvGUI gui, boolean showroot)
    {
        this.tree = tree;
        this.gui = gui;
        this.root = root;
        tree.setRootVisible(showroot);
        initTree();
        refreshTree();
    }
    public void initTree()
    {
        final guiTree ths = this;
        tree.setToggleClickCount(0);
        tree.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {

            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                m.msg("will expand");
                dvNode n = (dvNode)event.getPath().getLastPathComponent();
                n.loadChildren();
            }

            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                m.msg("will collapse");
            }
        });
        tree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event) {
                m.msg("expanded.");
            }

            public void treeCollapsed(TreeExpansionEvent event) {
                m.msg("collapsed.");
            }
        });
        tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                dvNode n = (dvNode)e.getPath().getLastPathComponent();
                n.onSelect(ths);
            }
        });
        tree.addMouseListener(new java.awt.event.MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                Object src = e.getSource();
                if(src==tree)
                {
                    if(e.getClickCount()==2)
                    {
                        m.msg("hi");
                        dvNode n = (dvNode)tree.getSelectionPath().getLastPathComponent();
                        n.onDoubleClick(ths);
                    }
                }
            }
            public void mouseReleased(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    public void refreshTree()
    {
        //final dvAges ths = this;
        tree.setModel(new javax.swing.tree.TreeModel() {
            public Object getRoot() {
                return root;
            }
            public Object getChild(Object parent, int index) {
                dvNode n = (dvNode)parent;
                return n.getChild(index);
            }
            public int getChildCount(Object parent) {
                dvNode n = (dvNode)parent;
                return n.getChildCount();
            }
            public boolean isLeaf(Object node) {
                dvNode n = (dvNode)node;
                return n.isLeaf();
            }
            public void valueForPathChanged(TreePath path, Object newValue) {
            }
            public int getIndexOfChild(Object parent, Object child) {
                dvNode n = (dvNode)parent;
                dvNode c = (dvNode)child;
                return n.getChildIndex(c);
            }
            public void addTreeModelListener(TreeModelListener l) {
            }
            public void removeTreeModelListener(TreeModelListener l) {
            }
        });
    }
    public void expandCurrentSelection()
    {
        m.msg("wha");
        tree.expandPath(tree.getSelectionPath());
    }

    public void createNewWindow(dvNode root)
    {
        guiSubtree newwindow = new guiSubtree(root,gui);
        gui.desktop.add(newwindow);
        moveToFrontAndSelect(newwindow);
    }
    public void createNewEditorWindow(guiEditor win)
    {
        gui.desktop.add(win);
        moveToFrontAndSelect(win);
    }
    public static void moveToFrontAndSelect(javax.swing.JInternalFrame win)
    {
        win.moveToFront();
        try
        {
            win.setSelected(true);
        }
        catch(Exception e)
        {
            m.err("Unable to select window");
        }
    }

}

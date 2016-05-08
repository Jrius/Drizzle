/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import javax.swing.*;

public class guiSubtree extends javax.swing.JInternalFrame
{
    dvNode root;

    public guiSubtree(dvNode root,dvGUI gui)
    {
        this.root = root;
        this.setVisible(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);
        this.setSize(600,300);
        JTree tree = new JTree();
        JScrollPane treenest = new JScrollPane(tree);
        this.add(treenest);
        guiTree gt = new guiTree(tree,root,gui,true);
        //mainPanel = new dvPanel();
        //JScrollPane scrollpane = new JScrollPane(mainPanel);
        //this.add(scrollpane);
    }


}

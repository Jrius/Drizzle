/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import shared.m;

public class GuiAdvanced
{

    //It turns out that this did *not* significantly reduce startup time, though it did help by maybe 0.7s, of which some would no doubt be var setting.
    //Perhaps it would be better to just prune some of the legacy things, and move some things to command-line.

    //private void GuiAdvancedPanelComponentShown(java.awt.event.ComponentEvent evt) {
    //    javax.swing.JPanel panel = (javax.swing.JPanel)evt.getComponent();
    //    GuiAdvanced newpanel = new GuiAdvanced();
    //    LoadPanelDynamically(newpanel, panel);
    //}
    public static void LoadPanelDynamically(javax.swing.JPanel newpanel, javax.swing.JPanel panel)
    {
        m.msg("showing");
        int numComponentsAlready = panel.getComponents().length;
        if(numComponentsAlready==0)
        {
            m.msg("loading");
            panel.setLayout(new java.awt.GridLayout(1, 1));
            panel.add(newpanel);
            panel.validate();
            //panel.invalidate();
            panel.repaint();
        }
    }

}

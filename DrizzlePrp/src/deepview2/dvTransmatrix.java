/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import javax.swing.*;
import shared.Flt;
import prpobjects.Transmatrix;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class dvTransmatrix extends dvNode
{
    Transmatrix t;

    JTextField[][] editfields = new JTextField[4][4];

    public dvTransmatrix(nodeinfo info2)
    {
        this.info = info2;
        this.t = (Transmatrix)info.obj;
    }
    public String toString()
    {
        return info.name + " (Matrix)"+t.toString();
    }
    public void onDoubleClick(guiTree tree)
    {

        //create gui
        guiEditor ge = new guiEditor(this);
        ge.panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                editfields[i][j] = new JTextField();
                editfields[i][j].setPreferredSize(new java.awt.Dimension(100,20));
                c.gridx = i;
                c.gridy = j;
                ge.panel.add(editfields[i][j], c);
                editfields[i][j].invalidate();
            }
        }
        tree.createNewEditorWindow(ge);
        
        //put in initial values
        float[][] fs = t.convertToFloatArray();
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                String f_str = Float.toString(fs[i][j]);
                editfields[i][j].setText(f_str);
            }
        }
        
    }
    public void onSave() throws Exception
    {
        //get values
        float[][] fs = new float[4][4];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                String f_str = editfields[i][j].getText();
                float f = Float.parseFloat(f_str);
                fs[i][j] = f;
            }
        }
        
        //save
        Transmatrix newval = Transmatrix.createFromFloatArray(fs);
        save(newval);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import javax.swing.*;
import shared.Flt;

public class dvFlt extends dvNode
{
    //String name;
    Flt f;

    JTextField _editor;

    public dvFlt(nodeinfo info2)
    {
        this.info = info2;
        this.f = (Flt)info.obj;
        //this.name = name;
    }
    public String toString()
    {
        return info.name + " (Flt)"+f.toString();
    }
    public void onDoubleClick(guiTree tree)
    {
        String curval = f.toString();

        guiEditor ge = new guiEditor(this);
        _editor = new JTextField();
        _editor.setPreferredSize(new java.awt.Dimension(100, 20));
        ge.panel.add(_editor);
        _editor.invalidate();
        tree.createNewEditorWindow(ge);
        _editor.setText(curval);
    }
    public void onSave() throws Exception
    {
        String str = _editor.getText();
        Flt newval = Flt.createFromJavaFloat(java.lang.Float.parseFloat(str));
        save(newval);
    }
}

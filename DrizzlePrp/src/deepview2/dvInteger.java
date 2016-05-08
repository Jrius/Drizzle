/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import javax.swing.*;

public class dvInteger extends dvNode
{
    //String name;
    int i;

    JTextField _editor;

    public dvInteger(nodeinfo info2)
    {
        this.info = info2;
        this.i = (Integer)info.obj;
        //this.name = name;
    }
    public String toString()
    {
        return info.name + " (Integer)"+Integer.toString(i);
    }
    public void onDoubleClick(guiTree tree)
    {
        String curval = Integer.toString(i);

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
        Integer newval = Integer.parseInt(str);
        save(newval);
    }
}

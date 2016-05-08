/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import javax.swing.*;

public class dvByte extends dvNode
{
    //String name;
    byte i;

    JTextField _editor;

    public dvByte(nodeinfo info2)
    {
        this.info = info2;
        this.i = (Byte)info.obj;
        //this.name = name;
    }
    public String toString()
    {
        return info.name + " (Byte)"+Byte.toString(i);
    }
    public void onDoubleClick(guiTree tree)
    {
        String curval = Byte.toString(i);

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
        Byte newval = Byte.parseByte(str);
        save(newval);
    }
}

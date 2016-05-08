/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import prpobjects.*;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;

public class dvUrustring extends dvNode
{
    Urustring val;
    JTextField _editor;

    public dvUrustring(nodeinfo info2)
    {
        info = info2;
        val = (Urustring)info.obj;
    }

    public String toString()
    {
        Urustring s = (Urustring)info.obj;
        return info.name+": "+s.toString();
    }

    public void onDoubleClick(guiTree tree)
    {
        String curval = val.toString();

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
        Urustring newval = Urustring.createFromString(str);
        save(newval);
    }
    
}

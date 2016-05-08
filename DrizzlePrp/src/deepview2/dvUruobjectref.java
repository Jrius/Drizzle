/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import prpobjects.*;
import javax.swing.*;

public class dvUruobjectref extends dvNode
{
    JComboBox _editor;
    Uruobjectref val;

    public dvUruobjectref(nodeinfo info2)
    {
        info = info2;
        val = (Uruobjectref)info.obj;
    }

    public String toString()
    {
        return info.name+": "+val.toString();
    }

    public void onDoubleClick(guiTree tree)
    {
        //String curval = val.toString();

        guiEditor ge = new guiEditor(this);
        _editor = new JComboBox();
        _editor.setPreferredSize(new java.awt.Dimension(300, 20));
        ge.panel.add(_editor);
        _editor.invalidate();
        tree.createNewEditorWindow(ge);
        _editor.addItem(Uruobjectref.none());
        _editor.addItem(val);
        for(Uruobjectref ref: info.root._allrefs)
        {
            _editor.addItem(ref);
        }
        _editor.setSelectedItem(val);
        //_editor.setText(curval);
    }
    public void onSave() throws Exception
    {
        Uruobjectref ref = (Uruobjectref)_editor.getSelectedItem();
        save(ref);
        //String str = _editor.getText();
        //Urustring newval = Urustring.createFromString(str);
        //save(newval);
    }
    
}

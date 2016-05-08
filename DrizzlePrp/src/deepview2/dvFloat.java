/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import prpobjects.*;
import javax.swing.*;

public class dvFloat extends dvNode
{


    public dvFloat(nodeinfo info2)
    {
        info = info2;
    }

    public String toString()
    {
        float val = (Float)info.obj;
        return info.name+"(Float): "+Float.toString(val);
    }

    public void onDoubleClick(guiTree tree)
    {
        //String curval = val.toString();

        //guiEditor ge = new guiEditor(this);
        //_editor = new JTextField();
        //_editor.setPreferredSize(new java.awt.Dimension(100, 20));
        //ge.panel.add(_editor);
        //_editor.invalidate();
        //tree.createNewEditorWindow(ge);
        //_editor.setText(curval);
    }
    public void onSave() throws Exception
    {
        //String str = _editor.getText();
        //Urustring newval = Urustring.createFromString(str);
        //save(newval);
    }
    
}

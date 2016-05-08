/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ListSelectionEvent;
import prpobjects.Uruobjectref;
import javax.swing.JButton;
import shared.m;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JComboBox;
import prpobjects.PrpRootObject;
import prpobjects.prpfile;
import prpobjects.Urustring;
import javax.swing.JTextField;

public class dvUrustring extends dvPanel
{
    Urustring urustring;
    String name;
    deepview parent;
    boolean changeable;
    prpfile prp;
    PrpRootObject rootobj;
    JTextField jtextfield;
    
    public dvUrustring(Urustring urustring, String name, deepview parent, boolean changeable)
    {
        this.urustring = urustring;
        this.name = name;
        this.parent = parent;
        this.changeable = changeable;
        
        prp = parent.curprp;
        rootobj = parent.curobj;
        
        
        
        reload();
    }
    private void handleclick()
    {
        this.urustring.shallowCopyFrom(Urustring.createFromString(jtextfield.getText()));
        m.msg("Changed value.  Press save to commit.");
        rootobj.markAsChanged(); //set it to save the changed version, rather than the raw bytes.
        this.reload();
    }
    private void reload()
    {
        this.removeAll();
        //this.add(dvWidgets.jlabel("Urustring name:"+name+" value:"+urustring.toString()));
        this.add(dvWidgets.jlabel("Urustring name:"+name+" "));
        jtextfield = dvWidgets.jtextfield(urustring.toString());
        this.add(jtextfield);

        if(changeable)
        {
            JButton button = dvWidgets.jbutton("change");
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleclick();
                }
            });
            this.add(button);
        }
        
        
        this.revalidate();
    }
    
}

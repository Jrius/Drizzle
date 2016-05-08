/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import shared.Flt;
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
import prpobjects.*;

public class dvFlt extends dvPanel
{
    Flt flt;
    
    String name;
    deepview parent;
    //boolean changeable;
    prpfile prp;
    PrpRootObject rootobj;
    
    public dvFlt(Flt flt, String name, deepview parent)
    {
        this.flt = flt;
        this.name = name;
        this.parent = parent;
        //this.changeable = changeable;
        
        prp = parent.curprp;
        rootobj = parent.curobj;
        reload();
    }
    private void reload()
    {
        this.removeAll();
        this.add(dvWidgets.jlabel("Flt name:"+name+" value:"+flt.toString()));
        
        this.revalidate();
    }
    
}

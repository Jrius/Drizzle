/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import prpobjects.Uruobjectref;
import prpobjects.Uruobjectdesc;
import prpobjects.Typeid;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

public class dvTypeid extends dvPanel
{
    Typeid typeid;
    deepview parent;
    
    public dvTypeid(Typeid typeid, deepview parent)
    {
        this.typeid = typeid;
        this.parent = parent;
        
        reload();
    }
    
    public void reload()
    {
        this.removeAll();
        this.add(dvWidgets.jlabel("Typeid: "+typeid.toString()));
        
        JButton button2 = dvWidgets.jbutton("open");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openclick();
            }
        });
        this.add(button2);
        
    }
    void openclick()
    {
        parent.openType(typeid);
    }
    
    
}

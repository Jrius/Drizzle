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

public class dvUruobjectref extends dvPanel
{
    Uruobjectref ref;
    String name;
    deepview parent;
    boolean changeable;
    prpfile prp;
    PrpRootObject rootobj;
    
    public dvUruobjectref(Uruobjectref ref, String name, deepview parent, boolean changeable) //, PrpRootObject rootobj)
    {
        this.ref = ref;
        this.name = name;
        this.parent = parent;
        this.changeable = changeable;
        
        prp = parent.curprp;
        rootobj = parent.curobj;
        reload();
    }
    private void reload()
    {
        this.removeAll();
        String type = ref.hasref()?ref.xdesc.objecttype.toString():"null";
        this.add(dvWidgets.jlabel("UruObjectRef name:"+name+" ref:"+ref.toString()+" type:"+type));
        
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
        
        JButton button2 = dvWidgets.jbutton("open");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openclick();
            }
        });
        this.add(button2);
        
        this.revalidate();
    }
    
    void openclick()
    {
        if(ref.hasref())
        {
            parent.openRef(ref.xdesc);
        }
    }
    
    void handleclick()
    {
        m.msg(name);
        Vector<Uruobjectref> refs = (Vector<Uruobjectref>)parent.allrefs.clone();
        refs.insertElementAt(Uruobjectref.none(), 0); //add the null reference.
        JComboBox list = new JComboBox(refs);
        //list.setEditable(false);
        //JList list = new JList();
        //list.setListData(parent.allrefs);
        list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleselection(e.getSource());
            }
        });
        //list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        //    public void valueChanged(ListSelectionEvent e) {
        //        handleselection(e.getSource());
        //    }
        //});
        this.add(list);
        this.revalidate();
    }
    
    void handleselection(Object source)
    {
        //Object ref1 = ((JList)source).getSelectedValue();
        Object ref1 = ((JComboBox)source).getSelectedItem();
        Uruobjectref ref2 = (Uruobjectref)ref1;
        this.ref.shallowCopyFrom(ref2);
        //m.warn("Shallow copy performed, be careful of deeper changes.");
        m.msg("Changed value.  Press save to commit.");
        rootobj.markAsChanged(); //set it to save the changed version, rather than the raw bytes.
        this.reload();
    }
}

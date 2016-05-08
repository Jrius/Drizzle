package shared.State;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.DocumentEvent;
//import shared.m;



public class TextfieldState extends javax.swing.JTextField implements IState
{
    private String _default = "";
    
    public TextfieldState()
    {
        super();
        
        AllStates.register(this);

        
    }
    
    private void change()
    {
        AllStates.update(this);
    }
    
    public void initialise()
    {
        _default = (String)this.getStateValue();
        //is this correct?
        /*this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                change();
            }
        });*/
        this.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                //shared.m.msg("insert");
                change();
            }
            public void removeUpdate(DocumentEvent e) {
                //shared.m.msg("remove");
                change();
            }
            public void changedUpdate(DocumentEvent e) {
                //shared.m.msg("changed");
                change();
            }
        });
        common.addSpecialMenu(this);
    }
 
    public void putStateValue(Object obj)
    {
        this.setText((String)obj);
    }

    public Object getStateValue()
    {
        return this.getText();
    }
    
    public String getStateName()
    {
        return this.getName();
    }
    //public void setDefault(Object obj)
    //{
    //    this._default = (String)obj;
    //}
    public Object getDefault()
    {
        if(_default==null) return getStateValue();
        else return this._default;
    }

}

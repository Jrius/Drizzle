package shared.State;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.JCheckBox;

/**
 *
 * @author user
 */
public class CheckboxState extends javax.swing.JCheckBox implements IState
{
    private Boolean _default = false;
    
    public CheckboxState()
    {
        super();
        
        //this.getParent().getClass().
        //String name = this.getClass().getName();

        //AllStates.register(this.getActionCommand(), this);
        //AllStates.register(this.getName(), this);
        AllStates.register(this);
        
        //this.add
    }
    
    private void change()
    {
        AllStates.update(this);
    }
    
    public void initialise()
    {
        _default = (Boolean)this.getStateValue();
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                change();
            }
        });
        common.addSpecialMenu(this);
    }
    
    public void putStateValue(Object obj)
    {
        //Object obj = AllStates.getState(this.getActionCommand());
        //if(obj!=null) this.setSelected((Boolean)obj);
        this.setSelected((Boolean)obj);
    }
    public Object getStateValue()
    {
        return this.isSelected();
    }
    
    public String getStateName()
    {
        return this.getName();
    }
    /*public void setDefault(Object obj)
    {
        this._default = (Boolean)obj;
        //this.setValue(obj);
    }*/
    public Object getDefault()
    {
        if(_default==null) return getStateValue();
        else return this._default;
    }
    
}

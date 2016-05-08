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
public class SliderState extends javax.swing.JSlider implements IState
{
    private Integer _default = 0;
    
    public SliderState()
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
        _default = (Integer)this.getStateValue();
        /*this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                change();
            }
        });*/
        this.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                change();
            }
        });
        common.addSpecialMenu(this);
    }
    
    public void putStateValue(Object obj)
    {
        //Object obj = AllStates.getState(this.getActionCommand());
        //if(obj!=null) this.setSelected((Boolean)obj);
        //this.setSelected((Integer)obj);
        this.setValue((Integer)obj);
    }
    public Object getStateValue()
    {
        //return this.
        //return (Integer)null;
        return this.getValue();
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

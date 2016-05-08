package shared.State;

import java.util.Enumeration;
import javax.swing.AbstractButton;

public class ButtongroupState extends javax.swing.ButtonGroup implements IState
{
    private String _name;
    private Integer _default;
    
    public ButtongroupState()
    {
        super();
        
        AllStates.register(this);

        //super.
        
    }
    
    private void change()
    {
        AllStates.update(this);
    }
    public void initialise()
    {
        _default = (Integer)this.getStateValue();
        for(AbstractButton curbutton: this.buttons)
        {
            curbutton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    change();
                }
            });
            common.addSpecialMenu(this, curbutton);
        }
        //common.addSpecialMenu(this);
    }
    public void putStateValue(Object obj)
    {
        Enumeration<AbstractButton> buttonses = this.getElements();
        int i = 0;
        while(buttonses.hasMoreElements())
        {
            AbstractButton button = buttonses.nextElement();
            if(i==(Integer)obj)
            {
                button.setSelected(true);
                return;
            }
            /*else
            {
                button.setSelected(false);
            }*/
            i++;
        }
        this.clearSelection(); //otherwise set no selection.
    }

    public Object getStateValue()
    {
        Enumeration<AbstractButton> buttons = this.getElements();
        int i = 0;
        while(buttons.hasMoreElements())
        {
            if(buttons.nextElement().isSelected()) return i;
            i++;
        }
        return -1;
    }
    
    public String getStateName()
    {
        //initialised = true;
        return this.getName();
    }
    
    public String getName()
    {
        return _name;
    }
    public void setName(String newname)
    {
        _name = newname;
    }
    //public void setDefault(Object obj)
    //{
    //    this._default = (Integer)obj;
    //}
    public Object getDefault()
    {
        if(_default==null) return getStateValue();
        else return this._default;
    }

}

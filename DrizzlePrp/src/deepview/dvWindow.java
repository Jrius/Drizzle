/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import shared.m;

public class dvWindow extends JInternalFrame
{
    JPanel mainPanel;
    
    public dvWindow()
    {
        //JInternalFrame newwindow = new JInternalFrame();
        this.setVisible(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);
        //newwindow.setLocation(0,0);
        this.setSize(600,300);
        
        /*newwindow.moveToFront();
        try
        {
            newwindow.setSelected(true);
        }
        catch(Exception e)
        {
            m.err("Cannot select window.");
        }*/
        //return newwindow;
        mainPanel = new dvPanel();
        JScrollPane scrollpane = new JScrollPane(mainPanel);
        this.add(scrollpane);
    }
    
    public void moveToFrontAndSelect()
    {
        this.moveToFront();
        try
        {
            this.setSelected(true);
        }
        catch(Exception e)
        {
            m.err("Unable to select window");
        }
    }

}

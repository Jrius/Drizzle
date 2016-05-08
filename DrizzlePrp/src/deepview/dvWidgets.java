/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import shared.m;

public class dvWidgets
{

    public static JLabel jlabel(String text)//JPanel panel, String text)
    {
        JLabel result = new JLabel(text);
        //panel.add(result);
        return result;
    }
    
    /*public static JPanel jpanel()//JPanel panel)
    {
        JPanel result = new JPanel();
        BevelBorder border = new BevelBorder(BevelBorder.LOWERED);
        EmptyBorder border2 = new EmptyBorder(4,4,4,4);
        CompoundBorder border3 = new CompoundBorder(border2,border);
        result.setBorder(border3);
        result.setLayout(new BoxLayout(result,BoxLayout.Y_AXIS));
        //panel.add(result);
        return result;
    }*/
    
    public static JButton jbutton(String text)
    {
        JButton result = new JButton();
        result.setText(text);
        return result;
    }
    
    public static JTextField jtextfield(String text)
    {
        JTextField result = new JTextField();
        result.setText(text);
        return result;
    }

}

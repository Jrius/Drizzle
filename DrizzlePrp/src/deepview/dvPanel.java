/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.BoxLayout;

public class dvPanel extends JPanel
{
    public dvPanel()
    {
        //JPanel result = new JPanel();
        BevelBorder border = new BevelBorder(BevelBorder.LOWERED);
        EmptyBorder border2 = new EmptyBorder(4,4,4,4);
        CompoundBorder border3 = new CompoundBorder(border2,border);
        this.setBorder(border3);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        //panel.add(result);
        //return result;
    }
}

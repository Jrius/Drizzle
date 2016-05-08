/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.State;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class common
{
    public static void addSpecialMenu(final IState state, final JComponent component)
    {
        //final IState state2 = state;
        //JComponent component = (JComponent)state;
        component.addMouseListener(new javax.swing.event.MouseInputListener() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3)
                {
                    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                    javax.swing.JComponent parent = (javax.swing.JComponent)e.getSource();

                    if(state!=null)
                    {
                        javax.swing.JMenuItem mi = new javax.swing.JMenuItem("set to default");
                        mi.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                Object o = state.getDefault();
                                state.putStateValue(o);
                            }
                        });
                        popup.add(mi);
                    }

                    if(component instanceof javax.swing.text.JTextComponent)
                    {
                        javax.swing.JMenuItem mi2 = new javax.swing.JMenuItem("copy");
                        mi2.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String sel = ((javax.swing.text.JTextComponent)component).getSelectedText();
                                shared.clipboard.SetString(sel);
                            }
                        });
                        popup.add(mi2);

                        javax.swing.JMenuItem mi3 = new javax.swing.JMenuItem("copy all");
                        mi3.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String sel = ((javax.swing.text.JTextComponent)component).getText();
                                shared.clipboard.SetString(sel);
                            }
                        });
                        popup.add(mi3);
                    }

                    java.awt.Point p = e.getPoint();
                    popup.show(parent, p.x , p.y);
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseDragged(MouseEvent e) {}
            public void mouseMoved(MouseEvent e) {}
        });

    }
    
    public static void addSpecialMenu(IState state)
    {
        //final IState state2 = state;
        JComponent component = (JComponent)state;
        addSpecialMenu(state, component);
    }
}

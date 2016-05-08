/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.RootPaneContainer;
import java.awt.event.FocusEvent;
import java.awt.Point;
import javax.swing.SwingUtilities;
import java.util.Vector;
import javax.swing.JLabel;
import java.awt.Container;

public class GuiThread
{
    //private static InvisibleThread thread;
    //private static final boolean useGlassPane = true;

    public static class GuiThreadInfo
    {
        public boolean useGlassPane = true;
        public Vector<RootPaneContainer> rootpanes = new Vector();
        public boolean setWorkingProgressBar = true;
        public boolean setWorkingText = true;
    }
    public static void run(GuiThreadInfo info, Runnable command)
    {
        try{
            if(info.setWorkingProgressBar) m.setWorking(true);
            if(info.useGlassPane) runGlass(info, command);
            else runModal(info,command);
        }catch(shared.cancel e){
            m.status("Cancelled because of an error.");
        }
    }
    private static void runGlass(GuiThreadInfo info, Runnable command)
    {
        InvisibleThread thread = new InvisibleThread(command,info);

        for(RootPaneContainer rootpane: info.rootpanes)
        {
            Component contentPane = rootpane.getContentPane();
            InvisibleGlassPane glasspane = new InvisibleGlassPane(contentPane,info);
            thread.addModal(glasspane);
            //rootpane.setGlassPane(glasspane);
            //glasspane.setVisible(true);
            //rootpane.getGlassPane().setVisible(true);
            rootpane.setGlassPane(glasspane);
            //glasspane.revalidate();
            //rootpane.getRootPane().revalidate();
            //javax.swing.JFrame jframe = (javax.swing.JFrame)rootpane;
            //jframe.repaint();
            //java.awt.Component co = jframe.getGlassPane();
            //co.setVisible(true);
            glasspane.setVisible(true);
            //co.repaint();
        }

        thread.start();
    }
    private static void runModal(GuiThreadInfo info, Runnable command)
    {
        final InvisibleModal modal = new InvisibleModal();
        InvisibleThread thread = new InvisibleThread(command,info);
        thread.addModal(modal);

        //show the form:
        Thread modalthread = new Thread(new java.lang.Runnable() {
            public void run() {
                modal.setVisible(true);
            }
        });
        modalthread.start();

        thread.start();
        //will be hidden when the thread is done.
    }

    private static class InvisibleThread extends Thread
    {
        Runnable command;
        Vector<Component> modal = new Vector();
        GuiThreadInfo info;

        public InvisibleThread(Runnable command2, GuiThreadInfo info2)
        {
            command = command2;
            info = info2;
           // modal = modal2;
        }
        public void addModal(Component modal2)
        {
            modal.add(modal2);
        }
        @Override public void run()
        {
            try{
                command.run();
            }catch(Exception e){
                m.err("Unexpected error in GuiThread:");
                e.printStackTrace();
            }
            for(Component comp: modal){
                comp.setVisible(false);
            }
            if(info.setWorkingProgressBar) m.setWorking(false);
        }
    }

    private static class InvisibleModal extends javax.swing.JDialog
    {
        public InvisibleModal()
        {
            super();
            //super((java.awt.Frame)null,true);

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setModal(true);
            setResizable(false);
            setUndecorated(true);

            /*javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE)
            );*/

            //pack(); //not needed with setSize

            setSize(0,0);
        }
    }

    private static class InvisibleGlassPane extends javax.swing.JPanel implements java.awt.event.MouseListener, java.awt.event.FocusListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener, java.awt.event.ComponentListener
    {
        java.awt.Component contentPane;

        public InvisibleGlassPane(java.awt.Component contentPane, GuiThreadInfo info)
        {
            super();
            this.contentPane = contentPane;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addFocusListener(this);
            this.addKeyListener(this);
            this.addComponentListener(this);
            //this.setSize(500, 500);
            this.setLayout(new java.awt.BorderLayout());
            this.setOpaque(false);

            if(info.setWorkingText)
            {
                JLabel label = new JLabel(translation.translate("Working..."),JLabel.CENTER);
                label.setFont(label.getFont().deriveFont(60.0f));
                label.setForeground(java.awt.Color.DARK_GRAY);
                this.add(label,java.awt.BorderLayout.CENTER);
                label.setNextFocusableComponent(label); //don't lost focus.
                //boolean likelytosucceed = this.requestFocusInWindow();
                //int dummy=0;
            }
        }

        public void componentResized(ComponentEvent e) {
        }
        public void componentMoved(ComponentEvent e) {
        }
        public void componentShown(ComponentEvent e) {
            boolean likelytosucceed = this.requestFocusInWindow();
            int dummy=0;
        }
        public void componentHidden(ComponentEvent e) {
        }

        public void keyTyped(KeyEvent e) {
            int dummy=0;
        }

        public void keyPressed(KeyEvent e) {
            int dummy=0;
        }

        public void keyReleased(KeyEvent e) {
            int dummy=0;
        }

        public void mouseClicked(MouseEvent e) {
            redispatch(e);
        }

        public void mousePressed(MouseEvent e) {
            redispatch(e);
        }

        public void mouseReleased(MouseEvent e) {
            redispatch(e);
        }

        public void mouseEntered(MouseEvent e) {
            redispatch(e);
        }

        public void mouseExited(MouseEvent e) {
            redispatch(e);
        }

        public void focusLost(FocusEvent fe) {
            //if (isVisible())
            //    requestFocus();
        }

        public void focusGained(FocusEvent fe) {
        }

        public void mouseDragged(MouseEvent e) {
            redispatch(e);
        }

        public void mouseMoved(MouseEvent e) {
            //redispatch(e);
        }

        public void redispatch(MouseEvent e)
        {
            boolean sendclick = false;

            Point glassPanePoint = e.getPoint();
            Point containerPoint = SwingUtilities.convertPoint(this, glassPanePoint, contentPane);
            Component comp = SwingUtilities.getDeepestComponentAt(contentPane, containerPoint.x, containerPoint.y);
            if(comp!=null)
            {
                //Container anc = javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JScrollPane.class, comp);
                //if(anc!=null)
                //{
                //    m.status("Ancestor of JScrollPane!");
                //    sendclick = true;
                //}
                if(//comp instanceof javax.swing.JTextArea ||
                        comp instanceof javax.swing.JTabbedPane ||
                        comp instanceof javax.swing.JScrollBar ||
                        comp instanceof com.sun.java.swing.plaf.motif.MotifScrollBarButton)
                {
                    sendclick = true;
                }
                else
                {
                    //m.status(comp.getClass().toString());
                    //if (comp instanceof com.sun.java.swing.plaf.motif.MotifScrollBarButton)
                    //{
                        //javax.swing.SwingUtilities.
                    //    shared.reflect.printClassHierarchy(comp);
                        //java.awt.Container o = comp.getParent();
                        ///java.awt.Container o2 = o.getParent();
                        //java.awt.Container o3 = o2.getParent();
                        //java.awt.Container o4 = o3.getParent();
                        //java.awt.Container o5 = o4.getParent();
                        //java.awt.Container o6 = o5.getParent();
                    //    int i = 0;
                    //}
                }
            }
            if(sendclick)
            {
                Point componentPoint = SwingUtilities.convertPoint(this, glassPanePoint, comp);
                comp.dispatchEvent(new java.awt.event.MouseEvent(comp,
                                                     e.getID(),
                                                     e.getWhen(),
                                                     e.getModifiers(),
                                                     componentPoint.x,
                                                     componentPoint.y,
                                                     e.getClickCount(),
                                                     e.isPopupTrigger()));
            }
        }


    }
}

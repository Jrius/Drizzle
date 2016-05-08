/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.text.JTextComponent;
import javax.swing.JFileChooser;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.InputEvent;
import javax.swing.text.Keymap;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
//import javax.swing.text.Keymap;
import javax.swing.ActionMap;
import java.io.File;
import java.awt.TrayIcon;
import java.awt.SystemTray;
import javax.swing.JOptionPane;

import java.awt.Component;
import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.UIManager;

public class GuiUtils
{
    public static final boolean onlyUseASingleJFileChooser = true;
    //public static final boolean useNativeLAFFileChooser = true;
    private static JFileChooser _fc;
    private static TrayIcon _trayicon;

    public static void DisplayMessage(String caption, String message)
    {
        //javax.swing.JOptionPane.showMessageDialog(null, message, caption, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        GuiUtils_TextDialog.showMessageDialog(caption,message);
    }
    public static void DisplayTrayMessage(String caption, String message)
    {
        if(_trayicon==null)
        {
            m.err("You must have shown the tray icon first.");
        }
        else
        {
            _trayicon.displayMessage(caption, message, java.awt.TrayIcon.MessageType.NONE);
        }
    }
    
    public static void showTrayIcon(String imgpath)
    {
        try{
            if(_trayicon!=null)
            {
                //m.err("A tray icon has already been shown; doing nothing.");
                return;
            }
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            //java.awt.Image img = shared.GetResource.getResourceAsImage("/gui/Pterosaur2b4-16.png");
            java.awt.Image img = shared.GetResource.getResourceAsImage(imgpath);
            //java.awt.PopupMenu menu = new java.awt.PopupMenu();
            //menu.add(new java.awt.MenuItem("test"));
            //java.awt.TrayIcon ti = new java.awt.TrayIcon(img,"Drizzle",menu);
            java.awt.TrayIcon ti = new java.awt.TrayIcon(img);
            _trayicon = ti;
            ti.setImageAutoSize(true);
            tray.add(ti);
            //ti.displayMessage("caption", "message", java.awt.TrayIcon.MessageType.NONE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static class CustomJFileChooser extends javax.swing.JFileChooser
    {

        //javax.swing.JComboBox drives;// = new javax.swing.JComboBox();
        JPanel drivePanel;

        static class Root
        {
            String display;
            java.io.File dir;

            public Root(String display, java.io.File dir)
            {
                this.display = display;
                this.dir = dir;
            }
            public String toString()
            {
                return display;
            }
        }
        private void addDriveButton(String label, final java.io.File file)
        {
            JButton jb = new javax.swing.JButton(label);
            jb.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateroot(file);
                }
            });
            drivePanel.add(jb);
        }
        public void rescanCurrentDirectory()
        {
            //m.msg("rescan");
            //java.awt.GridLayout gl = new java.awt.GridLayout(0, 1);
            drivePanel.removeAll();
            //drivePanel.setLayout(gl);
            addDriveButton("Desktop", javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory());
            for(java.io.File root: java.io.File.listRoots())
            {
                addDriveButton(root.toString(), root);
            }
            drivePanel.revalidate();

            super.rescanCurrentDirectory();
        }
        /*public void setCurrentDirectory(java.io.File dir)
        {
            super.setCurrentDirectory(dir);
            //if(dir!=null)
            //{
            //    m.msg("clear:"+dir.toString());
                //drives.setSelectedIndex(0);
            //}
        }*/
        static CustomJFileChooser getInstance()
        {
            //This list is the ones visible with a Motif look-and-feel.
            //We're forcing the strings to english so that our translator can catch them.

            UIManager.put("FileChooser.helpButtonText","Help");
            UIManager.put("FileChooser.openButtonText","OK");
            UIManager.put("FileChooser.updateButtonText", "Update");
            UIManager.put("FileChooser.cancelButtonText","Cancel");

            UIManager.put("FileChooser.pathLabelText","Enter path or folder name:");
            UIManager.put("FileChooser.filterLabelText","Filter");
            UIManager.put("FileChooser.acceptAllFileFilterText","*");

            UIManager.put("FileChooser.enterFileNameLabelText", "Enter file name:");
            UIManager.put("FileChooser.filesLabelText", "Files");
            UIManager.put("FileChooser.foldersLabelText","Folders");
            
            //com.sun.java.swing.plaf.motif.MotifFileChooserUI b;

            return new CustomJFileChooser();
        }
        private CustomJFileChooser()
        {
            super();
            this.setPreferredSize(new java.awt.Dimension(700,500));
            this.setDialogTitle("Select...");
            //java.util.Locale l = new java.util.Locale("en","ca");
            //this.setLocale(l);
            //setDefaultLocale(java.util.Locale.ENGLISH);

            java.awt.LayoutManager lm = this.getLayout();
            //java.awt.BorderLayout.SOUTH
            //this.add(new javax.swing.JLabel("hi"));
            //this.addimp
            //this.addImpl(new javax.swing.JLabel("hi"), java.awt.BorderLayout.WEST, -1);

            /*drives = new javax.swing.JComboBox(roots);
            drives.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    updateroot();
                }
            });*/
            //drives.setBounds(0, 0, 50, 50);
            java.awt.GridLayout gl = new java.awt.GridLayout(0, 1);
            drivePanel = new javax.swing.JPanel(gl);
            //pan.setBounds(0, 0, 100, 100);
            //pan.add(drives);
            this.addImpl(drivePanel, java.awt.BorderLayout.WEST, -1);

            JPanel pan2 = new JPanel();
            JButton createfolderbutton = new JButton("Create Folder");
            final CustomJFileChooser ths = this;
            createfolderbutton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String newfol = GuiUtils.getStringFromUser("Folder Name:", "Create Folder");
                    if(newfol.equals("")) return;
                    String fullnew = ths.getCurrentDirectory().getAbsolutePath()+"/"+newfol;
                    FileUtils.CreateFolder(fullnew);
                    ths.rescanCurrentDirectory();
                }
            });
            BoxLayout gl2 = new javax.swing.BoxLayout(pan2, BoxLayout.Y_AXIS);
            pan2.add(Box.createGlue());
            pan2.add(createfolderbutton);
            this.addImpl(pan2, java.awt.BorderLayout.EAST, -1);


            shared.translation.registerGUIForm(this);
            
            /*this.addMouseListener(new javax.swing.event.MouseInputListener() {
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton()==MouseEvent.BUTTON3)
                    {
                        javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                        javax.swing.JComponent parent = (javax.swing.JComponent)e.getSource();
                        javax.swing.JMenuItem mi = new javax.swing.JMenuItem("create folder");
                        mi.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String newfol = GuiUtils.getStringFromUser("Folder Name:", "Create Folder");
                                if(newfol.equals("")) return;
                                String fullnew = ths.getCurrentDirectory().getAbsolutePath()+"/"+newfol;
                                FileUtils.CreateFolder(fullnew);
                                ths.rescanCurrentDirectory();
                            }
                        });
                        java.awt.Point p = e.getPoint();
                        popup.add(mi);
                        popup.show(parent, p.x , p.y);
                    }
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
                public void mouseDragged(MouseEvent e) {}
                public void mouseMoved(MouseEvent e) {}
            });*/
            //roots = javax.swing.filechooser.FileSystemView.getFileSystemView().getRoots();
            //roots = sun.awt.shell.ShellFolder.
            /*javax.swing.JButton createfolderbutton = new javax.swing.JButton("Create Folder");
            java.awt.LayoutManager lm3 = this.getLayout();
            java.awt.BorderLayout bl3 = (java.awt.BorderLayout)lm3;
            java.awt.Component c5 = bl3.getLayoutComponent(java.awt.BorderLayout.SOUTH);
            javax.swing.JPanel c6 = (javax.swing.JPanel)c5;
            java.awt.LayoutManager lm6 = c6.getLayout();
            java.awt.BorderLayout bl6 = (java.awt.BorderLayout)lm6;
            java.awt.Component c7 = bl6.getLayoutComponent(java.awt.BorderLayout.SOUTH);
            JPanel jp7 = (JPanel)c7;
            LayoutManager lm7 = jp7.getLayout();
            jp7.add(new javax.swing.Box.Filler(new java.awt.Dimension(0,0), new java.awt.Dimension(0,0), new java.awt.Dimension(10000,10000)),javax.swing.BoxLayout.X_AXIS,-1);
            jp7.add(createfolderbutton,javax.swing.BoxLayout.X_AXIS,-1);
            for(Component c: jp7.getComponents())
            {
                int dummy=0;
            }
            //for(Component c: c6.getComponents())
            //{
            //    Class cl = c.getClass();
            //}
            //c6.add(createfolderbutton, java.awt.BorderLayout.EAST);
            if(true)return;
            for(java.awt.Component c: this.getComponents())
            {
                if (c instanceof javax.swing.JPanel)
                {
                    javax.swing.JPanel c2 = (javax.swing.JPanel)c;
                    c2.add(createfolderbutton);
                    java.awt.LayoutManager lm2 = c2.getLayout();
                    //break;
                }
                Class cl = c.getClass();
                String name = cl.getCanonicalName();
                String name2=cl.getName();
                Class cl2 = cl.getSuperclass();
                int dummy=0;
            }

            //this.addImpl(createfolderbutton, java.awt.BorderLayout., -1);
            int dummy=0;*/
        }

        public void updateroot(java.io.File dir)
        {
            //java.io.File root = ((Root)drives.getSelectedItem()).dir;
            //if(root!=null)
            //{
                //m.msg("changing root."+dir.toString());
                this.setCurrentDirectory(dir);
            //}
        }
        /*public void setUI(javax.swing.plaf.ComponentUI ui)
        {
            super.setUI(com.sun.java.swing.plaf.windows.WindowsFileChooserUI.createUI(this));
            //com.sun.java.swing.plaf.motif.MotifFileChooserUI
            int dummy=0;
            for(java.awt.Component c: this.getComponents())
            {
                javax.swing.SwingUtilities.updateComponentTreeUI(c);
            }
        }*/
    }
    public static void printUIManagerResources()
    {
        for (Object key: UIManager.getDefaults().keySet())
        {
            m.msg(key.toString());
        }

    }
    public static void setBackgroundColour(java.awt.Color colour)
    {
        javax.swing.plaf.ColorUIResource res = new javax.swing.plaf.ColorUIResource(colour);
        //java.awt.Color res = colour;
        //If you want to add the others, see the font list below.
        UIManager.put("Panel.background", res);
        UIManager.put("Button.background", res);
        UIManager.put("TextArea.background", res);
        UIManager.put("RadioButton.background", res);
        UIManager.put("CheckBox.background", res);
        UIManager.put("TextField.background", res);
        UIManager.put("TabbedPane.background", res);
        UIManager.put("TextPane.background", res);
        //UIManager.put("ScrollPane.background", res);
        //UIManager.put("ScrollPane.foreground", res);

        //UIManager.put("background", res);
        //UIManager.put("foreground", res);
        //UIManager.put("nimbusBase", res);
        //UIManager.put("nimbusBlueGrey", res);
        //UIManager.put("control", res);

    }
    /*public static void setBackgroundColour(java.awt.Color colour, java.awt.Component c)
    {
        javax.swing.plaf.ColorUIResource res = new javax.swing.plaf.ColorUIResource(colour);
        //If you want to add the others, see the font list below.
        m.msg("pb"+UIManager.get("Panel.background").toString());
        UIManager.put("Panel.background", res);
        m.msg("pb"+UIManager.get("Panel.background").toString());
        UIManager.put("Button.background", res);
        UIManager.put("TextArea.background", res);
        UIManager.put("RadioButton.background", res);
        UIManager.put("CheckBox.background", res);
        UIManager.put("TextField.background", res);
        UIManager.put("TabbedPane.background", res);
        updateGui(c);
        m.msg("pb"+UIManager.get("Panel.background").toString());

    }*/
    public static void updateGui(final java.awt.Component c, boolean immediate)
    {
//m.msg("hi");

        if(immediate)
        {
//m.msg("hi2");
            javax.swing.SwingUtilities.updateComponentTreeUI(c);
        }
        else
        {
            javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {

                    public void run() {
//m.msg("hi3");
                        javax.swing.SwingUtilities.updateComponentTreeUI(c);
                    }
                });
        }
    }
    public static void setCrossPlatformFonts(boolean tryToUseFixedWidth)
    {
        //The regular JRE only comes with the one font, without bold, italics, or fixedwidth.  The JDK JRE has bold, italics, and fixedwidth as well.
        //If tryToUseFixedWidth is set, it will try to load that fixedwidth font, otherwise it will use the default fixedwidth font.
        //If tryToUseFixedWidth is not set, it will simply use the same font for things that probably should be fixedwidth, i.e. JTextArea.

        //if dynamically loaded, needs to close stream afterwards.
        //java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, shared.GetResource.getResourceAsStream("/files/LiberationSans-Regular.ttf"));
        //java.awt.Font font2 = font.deriveFont(12.0f);
        //javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(font2);

        //long time = java.util.Calendar.getInstance().getTimeInMillis();
        //new java.awt.Font
        javax.swing.plaf.FontUIResource plain = new javax.swing.plaf.FontUIResource("Lucida Sans",java.awt.Font.PLAIN,12);

        javax.swing.plaf.FontUIResource fixed;
        if(tryToUseFixedWidth)
        {
            //Use the Lucida fixedwidth font if possible, else use the default:
            String fixedstr = "Lucida Sans Typewriter";
            boolean hasfixed = false;
            for(String s: java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            {
                if(s.equals(fixedstr))
                {
                    hasfixed = true;
                    break;
                }
            }
            if(hasfixed)
            {
                //use the Lucida monospace font
                fixed = new javax.swing.plaf.FontUIResource(fixedstr,java.awt.Font.PLAIN,12);
            }
            else
            {
                //just use the default monospace font.
                fixed = new javax.swing.plaf.FontUIResource(java.awt.Font.MONOSPACED,java.awt.Font.PLAIN,12);
            }
        }
        else
        {
            fixed = plain;
        }
        //com.sun.java.swing.plaf.windows.resources.windows.

        //Object[] objs = new Object[]{"Lucida",java.awt.Font.PLAIN,12};
        //javax.swing.UIDefaults.ProxyLazyValue f2 = new javax.swing.UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource",null,objs);

        //java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
        //while (keys.hasMoreElements())
        //{
        //    Object key = keys.nextElement();
        //    Object value = javax.swing.UIManager.get(key);
        //    if (value instanceof javax.swing.plaf.FontUIResource)
        //    {
        //        javax.swing.UIManager.put (key, f);
        //    }
        //}

        UIManager.put("Button.font", plain);
        UIManager.put("ToggleButton.font", plain);
        UIManager.put("RadioButton.font", plain);
        UIManager.put("CheckBox.font", plain);
        UIManager.put("ColorChooser.font", plain);
        UIManager.put("ComboBox.font", plain);
        UIManager.put("Label.font", plain);
        UIManager.put("MenuBar.font", plain);
        UIManager.put("MenuItem.font", plain);
        UIManager.put("RadioButtonMenuItem.font", plain);
        UIManager.put("CheckBoxMenuItem.font", plain);
        UIManager.put("Menu.font", plain);
        UIManager.put("PopupMenu.font", plain);
        UIManager.put("OptionPane.font", plain);
        UIManager.put("Panel.font", plain);
        UIManager.put("ProgressBar.font", plain);
        UIManager.put("ScrollPane.font", plain);
        UIManager.put("Viewport.font", plain);
        UIManager.put("TabbedPane.font", plain);
        UIManager.put("Table.font", plain);
        UIManager.put("TableHeader.font", plain);
        UIManager.put("TitledBorder.font", plain);
        UIManager.put("ToolBar.font", plain);
        UIManager.put("ToolTip.font", plain);
        UIManager.put("Tree.font", plain);
        UIManager.put("TextField.font", plain);
        UIManager.put("PasswordField.font", plain);
        UIManager.put("TextArea.font", fixed);
        UIManager.put("TextPane.font", fixed); //changed to fixed
        UIManager.put("EditorPane.font", plain);

        //time = java.util.Calendar.getInstance().getTimeInMillis() - time;
        //int dummy=0;
    }

    public static JFileChooser getJFileChooser()
    {
        if(onlyUseASingleJFileChooser)
        {
            if(_fc==null)
            {
                //if(useNativeLAFFileChooser)
                //{
                    //try{
                        //javax.swing.UIManager.put("FileChooser.cancelButtonText", "wha");
                        //javax.swing.UIManager.put("FileChooser.listViewWindowsStyle", (Boolean)false);
                        //javax.swing.UIManager.put("FileChooser.usesSingleFilePane", (Boolean)false);
                        //m.msg("a"+Long.toString(System.currentTimeMillis()));
                        //javax.swing.LookAndFeel origin = javax.swing.UIManager.getLookAndFeel();
                        //javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                        //m.msg(Long.toString(System.currentTimeMillis()));
                        //_fc = new JFileChooser();
                        //_fc = new CustomJFileChooser();
                        //String abc = javax.swing.UIManager.getSystemLookAndFeelClassName();
                        //com.sun.java.swing.plaf.windows.WindowsFileChooserUI
                        //javax.swing.SwingUtilities.updateComponentTreeUI(_fc);
                        //javax.swing.UIManager.setLookAndFeel(origin);
                        //m.msg(Long.toString(System.currentTimeMillis()));
                    //}catch(Exception e){
                    //    int dummy=0;
                    //}
                //}
                //else
                //{
                    _fc = CustomJFileChooser.getInstance();
                //}
                //m.msg(Long.toString(System.currentTimeMillis()));
                //javax.swing.UIManager.getUI(_fc).
                //javax.swing.UIManager.
                //_fc.putClientProperty("FileChooser.cancelButtonText", "yo");
                //javax.swing.plaf.FileChooserUI a;a.
            }
            /*try{
                javax.swing.LookAndFeel origin = javax.swing.UIManager.getLookAndFeel();
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                javax.swing.SwingUtilities.updateComponentTreeUI(_fc);
                javax.swing.UIManager.setLookAndFeel(origin);
            }catch(Exception e){}*/
            return _fc;
        }
        else
        {
            return CustomJFileChooser.getInstance();
        }
    }
    public static boolean getOKorCancelFromUserDos(String message, String title)
    {
        javax.swing.JFrame fr = new javax.swing.JFrame();
        fr.setState(javax.swing.JFrame.ICONIFIED);
        fr.show();
        int r = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        fr.hide();
        if(r==JOptionPane.OK_OPTION) return true;
        else return false;
    }
    public static boolean getOKorCancelFromUser(String message, String title)
    {
        int r = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        //r=-1->xed out. r=2->cancel. r=0->okay.
        if(r==JOptionPane.OK_OPTION) return true;
        else return false;
    }
    public static void getStringFromUser(JTextComponent field, String message, String title)
    {
        String result = JOptionPane.showInputDialog(null, shared.translation.translate(message), shared.translation.translate(title), JOptionPane.PLAIN_MESSAGE);
        if(result!=null) field.setText(result);
    }
    public static String getStringFromUser(String message, String title)
    {
        String result = JOptionPane.showInputDialog(null, shared.translation.translate(message), shared.translation.translate(title), JOptionPane.PLAIN_MESSAGE);
        if(result==null) result = "";
        return result;
    }
    public static void getUserSelectedFolder(JTextComponent field)
    {
        JFileChooser fc = getJFileChooser();

        File f = new File(field.getText());
        File cwd = f.isDirectory()?f:f.getParentFile();
        fc.setCurrentDirectory(cwd);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int wasFileChosen = fc.showOpenDialog(null);
        if(wasFileChosen==0)
        {
            String file = fc.getSelectedFile().getAbsolutePath();
            field.setText(file);
        }
        //Main.
        /*java.awt.FileDialog fd = new java.awt.FileDialog((java.awt.Frame)null);
        fd.setMode(java.awt.FileDialog.LOAD);
        java.awt.
        File cwd = new File(field.getText()).getParentFile();
        fd.setDirectory(cwd.getAbsolutePath());
        //fd.setFilenameFilter(new java.io.FilenameFilter() {
        //    public boolean accept(File dir, String name) {
        //        int dummy=0;
        //        return false;
        //    }
        //});
        fd.setVisible(true);
        if(fd.getFile()!=null)
        {
            String file = fd.getDirectory()+"/"+fd.getFile();
            m.msg(file);
        }*/

    }
    public static void getUserSelectedFile(JTextComponent field)
    {
        String filename = getUserSelectedFile(field.getText());
        if(filename!=null)
        {
            field.setText(filename);
        }
    }
    public static String getUserSelectedFile(String defaultFolder)
    {
        return getUserSelected(defaultFolder,JFileChooser.FILES_ONLY);
    }
    private static String getUserSelected(String defaultFolder, int selectionMode)
    {
        JFileChooser fc = getJFileChooser();
        File f = new File(defaultFolder);
        File cwd = f.isDirectory()?f:f.getParentFile();
        fc.setCurrentDirectory(cwd);
        fc.setFileSelectionMode(selectionMode);
        int wasFileChosen = fc.showOpenDialog(null);
        if(wasFileChosen==0)
        {
            String file = fc.getSelectedFile().getAbsolutePath();
            return file;
        }
        else
        {
            return null;
        }
    }
    public static void getUserSelectedFileWithNoPath(JTextComponent field)
    {
        JFileChooser fc = getJFileChooser();
        File f = new File(field.getText());
        File cwd = f.isDirectory()?f:f.getParentFile();
        fc.setCurrentDirectory(cwd);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int wasFileChosen = fc.showOpenDialog(null);
        if(wasFileChosen==0)
        {
            String file = fc.getSelectedFile().getName();
            field.setText(file);
        }
    }
    public static void getUserSelectedFileOrFolder(JTextComponent field)
    {
        JFileChooser fc = getJFileChooser();
        File f = new File(field.getText());
        File cwd = f.isDirectory()?f:f.getParentFile();
        fc.setCurrentDirectory(cwd);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int wasFileChosen = fc.showOpenDialog(null);
        if(wasFileChosen==0)
        {
            String file = fc.getSelectedFile().getAbsolutePath();
            field.setText(file);
        }
    }
    
    public static void SetKeymaps()
    {
        SetKeymap(new JTextField());
        SetKeymap(new JTextArea());
        SetKeymap(new JFormattedTextField()); //todo: is this necessary?
    }
    
    public static void SetKeymap(JTextComponent componentExample)
    {
        Keymap keymap = componentExample.getKeymap();
        ActionMap actionmap = componentExample.getActionMap();
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),new DefaultEditorKit.CopyAction());
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),new DefaultEditorKit.PasteAction());
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK),new DefaultEditorKit.CutAction());
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.SHIFT_DOWN_MASK),new DefaultEditorKit.PasteAction());
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK),actionmap.get(DefaultEditorKit.selectAllAction));

    }

}

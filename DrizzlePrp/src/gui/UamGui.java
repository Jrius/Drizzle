/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.AttributeSet;
import shared.m;
import java.io.File;
import java.util.HashMap;
import shared.FileUtils;
import uam.Uam.InstallStatus;
import uam.Uam;
import java.awt.Color;
import javax.swing.JButton;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import shared.b;
import javax.swing.JLabel;
import uam.UamConfigNew;
import java.util.ArrayDeque;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;

public class UamGui
{
    public static JList agelist;
    public static JList verlist;
    public static JList mirlist;
    public static gui.Gui guiform;
    public static JButton downloadbutton;
    public static JButton deletebutton;
    public static JButton versionbutton;
    public static JLabel AgeLabel;
    public static shared.State.ButtongroupState startup;
    
    final static boolean updateWhileAdjusting = false;

    //static Vector<AgeListItem> ages;

    public static class AgeListItemInfo
    {
        String agename;
        String FriendlyAgename;
        uam.Uam.InstallStatus status;

        public AgeListItemInfo(String friendlyAgename, InstallStatus status, String agename)
        {
            this.agename = agename;
            this.status = status;
            this.FriendlyAgename = friendlyAgename;
        }

        public String toString()
        {
            return FriendlyAgename;
        }

        public int hashCode()
        {
            return FriendlyAgename.hashCode();
        }
        public boolean equals(Object obj)
        {
            if(obj==null) return false;
            if(!(obj instanceof AgeListItemInfo)) return false;
            AgeListItemInfo o2 = (AgeListItemInfo)obj;
            if(!this.agename.equals(o2.agename)) return false;
            if(!this.FriendlyAgename.equals(o2.FriendlyAgename)) return false;
            return true;
        }
    }

    public static class AgeListItem extends javax.swing.JPanel
    {
        //JLabel label;
        String agename2;
        String FriendlyAgename;
        InstallStatus status;
        static java.awt.Image check = shared.GetResource.getResourceAsImage("/gui/check.png");
        //static java.awt.Image ex = shared.GetResource.getResourceAsImage("/gui/ex.png");
        static java.awt.Image up = shared.GetResource.getResourceAsImage("/gui/up.png");
        static java.awt.Image unknown = shared.GetResource.getResourceAsImage("/gui/unknown.png");
        //static java.awt.Image dash = shared.GetResource.getResourceAsImage("/gui/dash.png");
        static java.awt.Image dashred = shared.GetResource.getResourceAsImage("/gui/dashred.png");
        java.awt.Image img;
        public boolean showicon = true;
        int width;

        public String toString()
        {
            return FriendlyAgename;
        }

        public boolean equals(Object o)
        {
            if(!(o instanceof AgeListItem)) return false;
            AgeListItem o2 = (AgeListItem)o;
            boolean result = FriendlyAgename.equals(o2.FriendlyAgename);
            return result;
        }
        public int hashCode()
        {
            return FriendlyAgename.hashCode();
        }

        public AgeListItem(String friendlyAgename, InstallStatus status, String agename)
        {
            this.agename2 = agename;
            this.FriendlyAgename = friendlyAgename;
            this.status = status;
            this.setOpaque(true);
            //this.setPreferredSize(new java.awt.Dimension(0, 16));
            //label = new JLabel("hi");
            //this.add(label);
            //this.getGraphics().drawString(agename, 0, 0);
            if(status==null)
            {
                //int dummy=0;
                //String s = shared.debug.getStackTrace();
                //I think we can ignore this, since it gets replaced right away anyway.
                //m.msg("Minor multithread bug: null: agename=",agename);
                return;
            }
            switch(status)
            {
                case noVersionsExist:
                    this.setForeground(new Color(0x0000AA));
                    img = null;
                    break;
                case notInstalled:
                    //label.setForeground(Color.red);

                    //this.setForeground(new Color(0x770000));
                    //img = ex;

                    //this.setForeground(new Color(0xd45600));
                    //img = dash;

                    this.setForeground(new Color(0x770000));
                    img = dashred;
                    break;
                case latestVersionInCache:
                    //label.setForeground(Color.green);
                    this.setForeground(new Color(0x007700));
                    img = check;
                    break;
                case nonLatestVersionInCache:
                    //label.setForeground(Color.yellow);//new Color(0x00aa00));
                    //label.setForeground(Color.orange);
                    this.setForeground(new Color(0x777700));
                    img = up;
                    break;
                case notInCache:
                    this.setForeground(Color.black);
                    img = unknown;
                    break;
                default:
                    this.setForeground(Color.black);
                    img = unknown;
                    break;
            }
            
            /*int h = this.getHeight();
            String str = agename;
            int offset = 20;
            
            int w = this.getFontMetrics(this.getFont()).stringWidth(str) + offset;
            this.setSize(w,h);
            this.setMinimumSize(new java.awt.Dimension(w, h));*/
            
        }
        /*@Override public void repaint()
        {
            java.awt.Graphics g = this.getGraphics();
            g.drawString("hi", 0, 0);
        }*/
        @Override public void paintComponent(java.awt.Graphics g)
        {
            super.paintComponent(g);
            //this.setForeground(Color.BLUE);
            //this.setBackground(Color.GREEN);
            int h = this.getHeight();
            String str = FriendlyAgename;
            int offset = showicon?20:3;
            
            //int w = g.getFontMetrics().stringWidth(str) + offset;
            //this.setSize(w,h);
            //this.setMinimumSize(new java.awt.Dimension(w, h));
            
            g.drawString(str, offset, h-3);
            if(showicon && img!=null) g.drawImage(img, 0, 0, null);
            //javax.swing.JList a;
            
        }
        /*@Override public java.awt.Dimension getSize()
        {
            int h = this.getHeight();
            String str = agename;
            int offset = 20;
            
            int w = 300;
            return new java.awt.Dimension(h,w);
            this.
        }*/
        
        public java.awt.Dimension getPreferredSize()
        {
            this.setFont(this.getFont().deriveFont(12.0f));
            java.awt.Graphics g = this.getGraphics();
            
            int w;
            if(g!=null)
            {
                java.awt.FontMetrics fm = g.getFontMetrics();
                w = (showicon?20:3) + fm.stringWidth(FriendlyAgename) + 3;
            }
            else
            {
                w = 40; //hack
            }
            //int w = 400;
            //int w = showicon?20:3;
            //w+= this.getGraphics().getFontMetrics().stringWidth(FriendlyAgename);
            //w+= 3;
            //m.msg(FriendlyAgename+" : "+Integer.toString(w));
            int h = 16;
            java.awt.Dimension result = new java.awt.Dimension(w,h);
            //m.msg("gps:"+agename);
            return result;
        }
    }
    public static void init() //called by swing thread
    {
        //agelist.set
        agelist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if(!updateWhileAdjusting && e.getValueIsAdjusting()) return;
                if(((javax.swing.JList)e.getSource()).getSelectedValue()==null)
                {
                    //m.msg("null");
                    ((javax.swing.JList)e.getSource()).setSelectedIndex(0);
                }
                String age = ((AgeListItemInfo)((javax.swing.JList)e.getSource()).getSelectedValue()).agename;
                GetVersionListGui(age);
            }
            public String toString()
            {
                return "test";
            }
        });
        agelist.setCellRenderer(new javax.swing.ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String agename = ((AgeListItemInfo)value).agename;
                //InstallStatus status = Uam.ageInstallStatus.get(agename).installationStatus;
                InstallStatus status = Uam.installInfo.ages.get(agename).installationStatus;

                //AgeListItem ali = new AgeListItem(agename, status);
                String properagename = Uam.ageList.getAgeProperName(agename);
                //properagename = properagename + " ("+agename+")"; //adds the filename in parentheses.
                AgeListItem ali = new AgeListItem(properagename, status, agename);
                if(isSelected) ali.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                return ali;

                /*AgeListItem ali = (AgeListItem)value;
                if(isSelected) ali.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                else ali.setBorder(javax.swing.BorderFactory.createEmptyBorder());
                return ali;*/

                /*javax.swing.JLabel label = new javax.swing.JLabel(agename);
                label.setOpaque(true); //allows us to set the background.
                switch(status)
                {
                    case notInstalled:
                        //label.setForeground(Color.red);
                        label.setForeground(new Color(0x770000));
                        break;
                    case latestVersionInCache:
                        //label.setForeground(Color.green);
                        label.setForeground(new Color(0x007700));
                        break;
                    case nonLatestVersionInCache:
                        //label.setForeground(Color.yellow);//new Color(0x00aa00));
                        //label.setForeground(Color.orange);
                        label.setForeground(new Color(0x777700));
                        break;
                    case notInCache:
                        label.setForeground(Color.black);
                        break;
                    default:
                        label.setForeground(Color.black);
                        break;
                }
                //label.setBackground(Color.white);
                if(isSelected) label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                //else label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.pink));
                //if(cellHasFocus) label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.pink));
                return label;*/

            }
        });
        verlist.setCellRenderer(new javax.swing.ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                //Agename is really version here.
                Object o = agelist.getSelectedValue();
                String agename = ((AgeListItemInfo)o).agename;
                String version = (String)value;
                InstallStatus status = Uam.installInfo.ages.get(agename).versions.get(version);
                AgeListItem ali = new AgeListItem(version, status, null);
                if(isSelected) ali.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                return ali;
            }
        });
        verlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if(!updateWhileAdjusting && e.getValueIsAdjusting()) return;
                String age2 = ((AgeListItemInfo)agelist.getSelectedValue()).agename;
                String ver = (String)verlist.getSelectedValue();
                //String ver = (String)((javax.swing.JList)e.getSource()).getSelectedValue();
                GetMirrorListGui(age2,ver);
            }
        });
        mirlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if(!updateWhileAdjusting && e.getValueIsAdjusting()) return;
                String age2 = ((AgeListItemInfo)agelist.getSelectedValue()).agename;
                String ver2 = (String)verlist.getSelectedValue();
                String mir = (String)mirlist.getSelectedValue();
                //String mir = (String)((javax.swing.JList)e.getSource()).getSelectedValue();
                OnMirrorSelected(age2,ver2,mir);
            }
        });
        mirlist.setCellRenderer(new javax.swing.ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                //Agename is really version here.
                //Object o = agelist.getSelectedValue();
                //String agename = (String)o;
                String version = (String)value;
                InstallStatus status = InstallStatus.notInCache;

                AgeListItem ali = new AgeListItem(version, status, null);
                ali.showicon = false;
                if(isSelected) ali.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                return ali;
            }
        });



        //load list if called for...
        shared.State.AllStates.addCallbackAfterInit(new shared.delegate() {
            public void callback(Object arg) {
                int startuptype = shared.State.AllStates.getStateAsInt("uamStartup");
                switch(startuptype)
                {
                    case 0:
                        //m.msg("0");
                        break;
                    case 1:
                        //m.msg("1");
                        String potsfolder = shared.State.AllStates.getStateAsString("uamRoot");
                        //m.msg(potsfolder);
                        GetAgeListGuiOffline(potsfolder);
                        break;
                    case 2:
                        //m.msg("2");
                        //String uamserver = shared.State.AllStates.getStateAsString("uamServer");
                        String uamserver = shared.State.AllStates.getStateAsString("uamServer2");
                        //m.msg(uamserver);
                        String potsfolder2 = shared.State.AllStates.getStateAsString("uamRoot");
                        //m.msg(potsfolder2);
                        //GetAgeListGui(uamserver, potsfolder2);
                        ThreadedTasks.uamDownloadAgeList(uamserver, potsfolder2);
                        break;
                    default:
                        m.err("Unhandled UAM startup type.");
                        break;
                }
            }
        });
    }
    public static String GetCurrentlySelectedAge()
    {
        Object selobj = UamGui.agelist.getSelectedValue();
        AgeListItemInfo item = (AgeListItemInfo)selobj;
        return item.agename;
    }
    public static void ReportVersion()
    {
        String agename = gui.UamGui.GetCurrentlySelectedAge();
        if(uam.Uam.installInfo.getAge(agename).installationStatus.isInstalled())
        {
            String installedVersion = uam.Uam.TryToFindInstalledVersionOfAge(agename);
            if(installedVersion!=null)
            {
                m.msg("Installed Version: ",installedVersion);
                //UamGui.verlist.invalidate();
                //UamGui.verlist.repaint();
            }
            else
            {
                m.warn("Unable to figure out which version is installed for Age: ",agename);
            }
        }
        else
        {
            m.msg("This Age isn't installed: ",agename);
        }
    }
    public static void GetLocalInfo(String potsfolder)
    {
        //boolean versionTooLowForSomeAges = false;
        
        //This function gets info about installed Ages.
        //We care about these facts: which (if any) version of an Age is deployed?
        //In practise, we'll gather this: is any version of an Age installed, and if so we'll assume it's the most recent one in the cache(if any).
        //Because going through each 7zip file and checking hashes would mean pretty heavy waiting.
        //possibilities for each Age on the server list:
        //1)not installed
        //2)installed & has latest version in cache
        //3)installed & doesn't have latest version in cache and has some version
        //4)installed & doesn't have latest version in cache and has no version.
        
        /*Vector<String> installedAges = new Vector();
        for(File f: filesearcher.search.getallfiles(potsfolder+"/dat/", false))
        {
            String agefile = f.getName();
            if(agefile.endsWith(".age"))
            {
                String agename = f.getName().substring(0, agefile.length());
                installedAges.add(agename);
            }
        }*/
        
        //String welcomeMessage = Uam.ageList.getWelcomeMessage();
        //m.msg(welcomeMessage);
        
        //Vector<String> availableAges = Uam.ageList.getAllAgeNames();
        
        //HashMap<String,Vector<String>> ageversions = Uam.ageList.getConfigObject().ageversions;
        //Uam.ageInstallStatus = new HashMap();
        //Uam.installInfo = new Uam.InstallInfo();
        Uam.InstallInfo ii = new Uam.InstallInfo();
        
        
        /*for(String age: availableAges)
        {
            if(FileUtils.Exists(potsfolder+"/dat/"+age+".age"))
            {
                Vector<String> versions = ageversions.get(age);
                if(versions==null)
                {
                    //not in database; ignore
                    m.warn("Age has no available versions.");
                }
                else
                {
                    boolean isInCache = false;
                    for(int i=0;i<versions.size();i++)
                    {
                        String version = versions.get(i);
                        //check if we have this version cached...
                        if(FileUtils.Exists(potsfolder+Uam.ageArchivesFolder+age+uam.Uam.versionSep+version+".7z"))
                        {
                            if(i==0)
                            {
                                Uam.ageInstallStatus.put(age, InstallStatus.latestVersionInCache);
                            }
                            else
                            {
                                Uam.ageInstallStatus.put(age, InstallStatus.nonLatestVersionInCache);
                            }
                            isInCache = true;
                            break; //found the newest one, so break.
                        }
                    }
                    if(!isInCache)
                    {
                        Uam.ageInstallStatus.put(age, InstallStatus.notInCache);
                    }
                }
            }
            else
            {
                Uam.ageInstallStatus.put(age, InstallStatus.notInstalled);
            }
        }*/
        
        //Uam.installInfo.fullyUpToDate = true; //may be set to false below
        int numAgesNotInstalled = 0;
        int numAgesNeedUpdate = 0;
        
        //find cached versions.
        //for(String age: availableAges)
        for(UamConfigNew.UamConfigData.Age ageobj: Uam.ageList.data.ages)
        {
            /*if(ageobj.minver>Uam.version)
            {
                //this age can't be handled.
                versionTooLowForSomeAges = true;
                continue;
            }*/

            String age = ageobj.filename;
            
            /*if(age.equals("offlineki"))
            {
                int dum=0;
            }*/
            
            //boolean hasagefile = FileUtils.Exists(potsfolder+"/dat/"+age+".age");
            boolean hasagefile = FileUtils.Exists(potsfolder+"/"+ageobj.getMainfile());
            boolean isInCache = false;
            boolean haslatest = false;
            boolean someversionexists = false;
            
            Uam.AgeInstallInfo ageinfo = ii.getOrCreateAge(age);
            
            //Vector<String> versions = ageversions.get(age);
            //if(versions!=null)
            //{
                //for(int i=0;i<versions.size();i++)
                boolean first = true;
                for(UamConfigNew.UamConfigData.Age.Version verobj: ageobj.versions)
                {
                    someversionexists = true;
                    //String version = versions.get(i);
                    String version = verobj.name;
                    boolean hasversion = FileUtils.Exists(potsfolder+Uam.ageArchivesFolder+age+uam.Uam.versionSep+version+".7z");
                    ageinfo.versions.put(version, hasversion?InstallStatus.latestVersionInCache:InstallStatus.notInstalled);
                    if(hasversion)
                    {
                        isInCache = true;
                        //if(i==0) haslatest = true;
                        if(first) haslatest = true;
                    }
                    first = false;
                }
            //}
            //else
            //{
            //    m.warn("Age has no available versions.");
            //}
            
            //assign age installation info.
            //boolean ageIsFullyUpToDate = false;
            if(someversionexists)
            {
                if(hasagefile)
                {
                    if(isInCache)
                    {
                        if(haslatest)
                        {
                            //Uam.ageInstallStatus.put(age, InstallStatus.latestVersionInCache);
                            ii.getOrCreateAge(age).installationStatus = InstallStatus.latestVersionInCache;
                            //ageIsFullyUpToDate = true;
                        }
                        else
                        {
                            //Uam.ageInstallStatus.put(age, InstallStatus.nonLatestVersionInCache);
                            ii.getOrCreateAge(age).installationStatus = InstallStatus.nonLatestVersionInCache;
                            //numAgesNeedUpdate++;
                        }
                    }
                    else
                    {
                        //Uam.ageInstallStatus.put(age, InstallStatus.notInCache);
                        ii.getOrCreateAge(age).installationStatus = InstallStatus.notInCache;
                    }
                }
                else
                {
                    //Uam.ageInstallStatus.put(age, InstallStatus.notInstalled);
                    ii.getOrCreateAge(age).installationStatus = InstallStatus.notInstalled;
                }
            }
            else
            {
                ii.getOrCreateAge(age).installationStatus = InstallStatus.noVersionsExist;
                //ageIsFullyUpToDate = true;
            }
            
            //Uam.installInfo.countStats();
            
            /*if(!ageIsFullyUpToDate)
            {
                Uam.installInfo.fullyUpToDate = false;
            }*/
        }
        
        //Uam.AgeInstallInfo a = Uam.installInfo.getAge("offlineki");
        //int a2 = 0;
        /*if(versionTooLowForSomeAges)
        {
            m.warn("There are some Ages that you need a newer version of Drizzle for. (They won't be listed.) Sorry for the inconvenience!");
        }*/
        Uam.installInfo = ii;
    }
    public static void RefreshInfo()
    {
        RefreshInfo(Uam.getPotsFolder());
    }
    public static void RefreshInfo(String potsfolder2)
    {
        //m.msg("refresh");
        //list Ages...
        //final Vector<String> ages = uam.Uam.ageList.getAllAgeNames();

        String welcomeMessage = Uam.ageList.getWelcomeMessage();
        m.msg(welcomeMessage);

        GetLocalInfo(potsfolder2);
        Vector<AgeListItemInfo> ages = new Vector();
        for(UamConfigNew.UamConfigData.Age age: Uam.ageList.data.ages)
        {
            uam.Uam.InstallStatus is = Uam.installInfo.ages.get(age.filename).installationStatus;
            ages.add(new AgeListItemInfo(age.propername,is,age.filename));
        }
        //final Vector<UamConfigNew.UamConfigData.Age> ages = Uam.ageList.data.ages;
        final Vector<AgeListItemInfo> ages2 = ages;
        Object selection = agelist.getSelectedValue();
        agelist.setValueIsAdjusting(true);
        verlist.setValueIsAdjusting(true);
        mirlist.setValueIsAdjusting(true);
        mirlist.setModel(new javax.swing.DefaultListModel());
        verlist.setModel(new javax.swing.DefaultListModel());
        agelist.setModel(new javax.swing.ListModel() {
            public int getSize() {
                return ages2.size();
            }
            public Object getElementAt(int index) {
                //return ages.get(index);
                //return ages.get(index).filename;
                return ages2.get(index);
            }
            public void addListDataListener(ListDataListener l) {}
            public void removeListDataListener(ListDataListener l) {}
        });
        
        //try to re-select the item that was selected before.
        if(selection==null)
        {
            //m.msg("null2");
            if(agelist.getModel().getSize()>0) agelist.setSelectedIndex(0);
        }
        else
        {
            //m.msg("reinst");
            agelist.setSelectedValue(selection, true); //doesn't die even if selection is no longer there.
        }
        agelist.setValueIsAdjusting(false);
        verlist.setValueIsAdjusting(false);
        mirlist.setValueIsAdjusting(false);
        
        if(uam.Uam.ageList.getDrizzleVersion()>gui.Version.version)
        {
            m.msg("(","A new version of Drizzle is available: ","Drizzle"+Integer.toString(uam.Uam.ageList.getDrizzleVersion()),")");
        }

        uam.Uam.installInfo.printStatsMessage();

        uam.Uam.CheckForProblems(false);

        //check if we've installed a new Drizzle, and if so, restart.
        
    }
    public static void GetAgeListGuiOffline(String potsfolder)
    {
        if(!auto.AllGames.getPots().isFolderX(potsfolder))
        {
            return;
        }
        
        FileInputStream in;
        try
        {
            in = new FileInputStream(potsfolder+uam.Uam.ageArchivesFolder+uam.Uam.statusFilename);
        }
        catch(FileNotFoundException e)
        {
            m.err("Couldn't find cached list.");
            return;
        }
        uam.UamConfigNew ageList = new uam.UamConfigNew(in);
        uam.Uam.ageList = ageList;
        RefreshInfo(potsfolder);
        //if(!uam.Uam.installInfo.fullyUpToDate) m.msg("There are Ages available to be installed/upgraded.");
        //uam.Uam.installInfo.printStatsMessage();
    }
    public static void GetAgeListGui(String server, String potsfolder)
    {
        //m.msg("Updating Age list...");
        if(!auto.AllGames.getPots().isFolderX(potsfolder))
        {
            return;
        }
        if(!uam.Uam.HasPermissions(potsfolder)) return;

        
        final String potsfolder2 = potsfolder;
        
        //final Vector<String> ages = uam.Uam.GetAgeList(server,gui,new shared.delegate() {
        //uam.Uam.GetAgeList(server,gui,new shared.delegate() {
        uam.ThreadDownloadAndProcess.downloadConfig(server, potsfolder, new shared.delegate(){
            public void callback(Object arg) {
                //parse config file.
                byte[] result = (byte[])arg;
                ByteArrayInputStream in = new ByteArrayInputStream(result);
                //uam.UamConfigNew wha = new uam.UamConfigNew(in);
                uam.UamConfigNew ageList = new uam.UamConfigNew(in);
                //list Ages...
                //return ageList.getAllAgeNames();
                //uam.UamConfig config = (uam.UamConfig)arg;
                //uam.Uam.ageList = config;
                uam.Uam.ageList = ageList;
                
                RefreshInfo(potsfolder2);
                //if(!uam.Uam.installInfo.fullyUpToDate) m.msg("There are Ages available to be installed/upgraded.");
                //uam.Uam.installInfo.printStatsMessage();
                
                /*final Vector<String> ages = uam.Uam.ageList.getAllAgeNames();
                GetLocalInfo(potsfolder2);
                
                agelist.setModel(new javax.swing.ListModel() {
                    public int getSize() {
                        return ages.size();
                    }
                    public Object getElementAt(int index) {
                        return ages.get(index);
                    }
                    public void addListDataListener(ListDataListener l) {}
                    public void removeListDataListener(ListDataListener l) {}
                });
                verlist.setModel(new javax.swing.DefaultListModel());
                mirlist.setModel(new javax.swing.DefaultListModel());*/
                
                /*agelist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        String age = (String)((javax.swing.JList)e.getSource()).getSelectedValue();
                        GetVersionListGui(age);
                    }
                });
                agelist.setCellRenderer(new javax.swing.ListCellRenderer() {
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        String agename = (String)value;
                        InstallStatus status = Uam.ageInstallStatus.get(agename);
                        
                        javax.swing.JLabel label = new javax.swing.JLabel(agename);
                        label.setOpaque(true); //allows us to set the background.
                        switch(status)
                        {
                            case notInstalled:
                                label.setForeground(Color.orange);
                                break;
                            case latestVersionInCache:
                                label.setForeground(Color.green);
                                break;
                            case nonLatestVersionInCache:
                                label.setForeground(new Color(0x00aa00));
                                break;
                            case notInCache:
                                label.setForeground(Color.gray);
                                break;
                            default:
                                //label.setForeground(Color.BLUE);
                                break;
                        }
                        label.setBackground(Color.gray);
                        if(isSelected) label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
                        //else label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.pink));
                        //if(cellHasFocus) label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.pink));
                        return label;
                    }
                });*/
                /*if(agelist.getModel().getSize()>0) agelist.setSelectedIndex(0);
                */
            }
            
        });
        //m.msg("getgu done.");
    }
    public static void GetVersionListGui(String age)
    {

        //m.msg("updating version list.");
        final String age2 = age;
        if(age!=null)
        {
            boolean deletable = Uam.ageList.getDeletable(age) && Uam.installInfo.getAge(age).installationStatus.isInstalled();
            deletebutton.setEnabled(deletable);
            String info = uam.Uam.ageList.getAgeInfo(age);
            AgeLabel.setText("<html>"+info+"</html>");

            boolean doversion=Uam.installInfo.getAge(age).installationStatus.isInstalled();
            versionbutton.setEnabled(doversion);
            /*try{
            javax.swing.text.Document doc = AgeLabel.getDocument();
            doc.remove(0, doc.getLength());
            javax.swing.text.SimpleAttributeSet attbs = new javax.swing.text.SimpleAttributeSet();
            javax.swing.text.StyleConstants.setFontFamily(attbs, "Lucida");
            doc.insertString(0, info, attbs);
            String s = doc.getText(0, doc.getLength());
            String s2 = AgeLabel.getText();
            int dummy=0;
            }catch(Exception e){
                m.err("Unable to set text box.");
            }*/
           // AgeLabel.setText("<html><font face='Lucida' size='12'>"+info+"</font></html>");
        }
        else
        {
            deletebutton.setEnabled(false);
            AgeLabel.setText(shared.translation.translate("<html>","(Select an Age, or click \"Get Latest List\" to get the latest list of Ages.)","</html>"));
            //AgeLabel.setText("<html><font face='Lucida' size='12'>(Select an Age, or click \"Get Latest List\" to get the latest list of Ages.)</font></html>");
        }
        //final Vector<String> vers = uam.Uam.ageList.getAllVersionsOfAge(age);
        //final Vector<UamConfigNew.UamConfigData.Age.Version> vers = Uam.ageList.data.getAge(age).versions;
        final Vector<UamConfigNew.UamConfigData.Age.Version> vers = Uam.ageList.getAllVersions(age);
        //m.msg(Integer.toString(vers.size()));
        verlist.setModel(new javax.swing.ListModel() {
            public int getSize() {
                return vers.size();
            }
            public Object getElementAt(int index) {
                //return vers.get(index);
                return vers.get(index).name;
            }
            public void addListDataListener(ListDataListener l) {}
            public void removeListDataListener(ListDataListener l) {}
        });
        mirlist.setModel(new javax.swing.DefaultListModel());
        /*verlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String ver = (String)((javax.swing.JList)e.getSource()).getSelectedValue();
                GetMirrorListGui(age2,ver);
            }
        });*/
        if(verlist.getModel().getSize()>0) verlist.setSelectedIndex(0);
        //verlist.repaint();
        
    }
    public static void GetMirrorListGui(String age, String ver)
    {
        //m.msg("updating mirror list.");
        final String age2 = age;
        final String ver2 = ver;
        //final Vector<String> mirs = uam.Uam.ageList.getAllUrlsOfAgeVersion(age, ver);
        final Vector<UamConfigNew.UamConfigData.Age.Version.Mirror> mirs = Uam.ageList.getAllMirrors(age, ver);
        mirlist.setModel(new javax.swing.ListModel() {
            public int getSize() {
                return mirs.size();
            }
            public Object getElementAt(int index) {
                //return mirs.get(index);
                return mirs.get(index).url;
            }
            public void addListDataListener(ListDataListener l) {}
            public void removeListDataListener(ListDataListener l) {}
        });
        /*mirlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String mir = (String)((javax.swing.JList)e.getSource()).getSelectedValue();
                OnMirrorSelected(age2,ver2,mir);
            }
        });*/
        if(mirlist.getModel().getSize()>0) mirlist.setSelectedIndex(0);
    }
    public static void OnMirrorSelected(String age, String ver, String mir)
    {
        if(mir!=null)
        {
            downloadbutton.setEnabled(true);
        }
        else
        {
            downloadbutton.setEnabled(false);
        }
        //do nothing
    }
    public static void PerformDeletionAction()
    {
        String potsfolder = shared.State.AllStates.getStateAsString("uamRoot");
        if(PerformDeletion(potsfolder))
        {
            //refresh
            RefreshInfo(potsfolder);
        }
        
    }
    public static boolean PerformDeletion(String potsfolder)
    {
        String age = ((AgeListItemInfo)agelist.getSelectedValue()).agename;
        return PerformDeletion(potsfolder, age);
    }
    public static boolean PerformDeletion(String potsfolder, String age)
    {
        //m.msg("Deletions aren't supported yet.");
        //if(true)return;
        
        //ensure pots folder.
        if(!auto.AllGames.getPots().isFolderX(potsfolder))
        {
            return false;
        }
        if(!uam.Uam.HasPermissions(potsfolder)) return false;

        
        //String age = (String)agelist.getSelectedValue();
        if(age==null)
        {
            m.msg("You must select an Age.");
            return false;
        }
        
        //delete special files.
        for(String del: Uam.ageList.getDels(age))
        {
            String absfilename = potsfolder+"/"+del;
            FileUtils.DeleteFile2(absfilename);
        }
        
        //String deletable = Uam.ageList.getDeletable(age);
        //if(deletable.equals("true"))
        if(Uam.ageList.getDeletable(age))
        {
            //for each .7z file from that Age, delete its entries in Pots.
            Vector<File> files = filesearcher.search.getAllFilesWithExtension(potsfolder+uam.Uam.ageArchivesFolder, false, ".7z");
            for(File f: files)
            {
                if(f.getName().startsWith(age+uam.Uam.versionSep))
                {
                    if(f.length()!=0) //it may be zero if it has been nulled out.
                    {
                        shared.sevenzip.delete(f.getAbsolutePath(), potsfolder);
                    }
                }
            }
        }
        else
        {
            m.msg("This Age is listed as not being safely deletable; performing upgrade only.");
        }
        
        
        return true;
        
    }
    public static boolean PerformDownload()
    {
        String potsfolder = shared.State.AllStates.getStateAsString("uamRoot");
        Object[] ages = agelist.getSelectedValues();
        if(ages.length>1)
        //if(false)
        {
            boolean success = true;
            for(Object age2: ages)
            {
                String age = ((AgeListItemInfo)age2).agename;
                UamConfigNew.UamConfigData.Age a = uam.Uam.ageList.data.getAge(age);
                if(a.versions.size()==0) continue; //skip Ages without versions.
                UamConfigNew.UamConfigData.Age.Version ver2 = a.versions.get(0);
                String ver = ver2.name;
                if(ver2.mirrors.size()==0) continue; //skip Ages without mirrors for the most recent version.
                String mir = ver2.mirrors.get(0).url;
                boolean result = PerformDownload(potsfolder, age, ver, mir);
                //try{
                //Thread.sleep(20000);
                //}catch(Exception e){}
                if(!result) success = false;
            }
            return success;
        }
        else
        {
            String age = ((AgeListItemInfo)agelist.getSelectedValue()).agename;
            String ver = (String)verlist.getSelectedValue();
            String mir = (String)mirlist.getSelectedValue();
            return PerformDownload(potsfolder, age, ver, mir);
        }
    }
    public static boolean PerformDownload(String potsfolder, String age, String ver, String mir)
    {
        //String potsfolder = shared.State.AllStates.getStateAsString("uamRoot");
        
        //ensure pots folder.
        if(!auto.AllGames.getPots().isFolderX(potsfolder))
        {
            return false;
        }
        if(!uam.Uam.HasPermissions(potsfolder)) return false;


        //String age = (String)agelist.getSelectedValue();
        //String ver = (String)verlist.getSelectedValue();
        //String mir = (String)mirlist.getSelectedValue();
        if(age==null || ver==null || mir==null)
        {
            m.msg("You must select an Age, Version, and Mirror.");
            return false;
        }
        
        String archiveType = uam.Uam.ageList.getArchiveType(age, ver);
        if(!archiveType.equals("7z"))
        {
            m.err("This version is in a archive type not currently supported: ",archiveType);
            return false;
        }
        

        m.msg("Cleaning up any previous version...");
        if(!PerformDeletion(potsfolder, age)) return false;
        
        m.status("Checking to see if we already have this version in the cache...");
        if(FileUtils.Exists(potsfolder+Uam.ageArchivesFolder+age+Uam.versionSep+ver+".7z"))
        {
            m.status("Using cached version of Age: ",age,", Version: ",ver," ...");
            
            //String hash = Uam.ageList.getWhirlpool(age, ver);
            String hash = Uam.ageList.getSha1(age, ver);
            uam.ThreadDownloadAndProcess.extractAge(age, ver, potsfolder, hash);

            
            
            /*//prepare output file.
            String outputfolder = potsfolder+Uam.ageArchivesFolder;
            FileUtils.CreateFolder(outputfolder);
            String outputfile = outputfolder+age+uam.Uam.versionSep+ver+".7z";


            //check integrity.
            m.status("Checking integrity...");
            byte[] hash = shared.CryptHashes.GetWhirlpool(outputfile);
            String hashstr = b.BytesToHexString(hash);
            boolean isgood = whirlpool.equals(hashstr);
            if(!isgood)
            {
                m.err("Bad file integrity. The Age downloaded wasn't what was expected, perhaps because the version on the server is corrupted.");
                FileUtils.DeleteFile(outputfile);
                return false;
            }
            m.status("File integrity is good!");
            
            //extract.
            shared.sevenzip.extract(outputfile, potsfolder);
            
            //callback
            //callback.callback(null);
            m.status("Age installed!");

            RefreshInfo(potsfolder);*/
        }
        else
        {
            m.msg("Attempting to download Age: ",age,", Version: ",ver,", Mirror: ",mir);
            //uam.Uam.DownloadAge7Zip(age,ver,mir,potsfolder);

            //ensure pots folder. We don't need this here.
            //if(!automation.detectinstallation.isFolderPots(potsfolder))
            //{
            //    //return false;
            //    return;
            //}

            //get hash
            //String hash = ageList.getWhirlpool(age, ver);
            String hash = uam.Uam.ageList.getSha1(age, ver);

            //start work in another thread.
            uam.ThreadDownloadAndProcess.downloadAge(age,ver,mir,potsfolder,hash);

            //return true;
        }

        //refresh
        //RefreshInfo(potsfolder);
        return true;
    }
}

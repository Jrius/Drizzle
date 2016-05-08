/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import shared.*;
import javax.swing.JPanel;
import java.util.Vector;

public class Plugins
{
    //list of plugin classes to be initialised.
    private static final String[] plugins = {
        "drizzleadjunct.AdjPlugin",
    };

    private static Vector<JPanel> panels = new Vector<JPanel>();
    private static String guititle = "";
    private static Vector<Runnable> guicallbacks = new Vector<Runnable>();

    public static void initialise()
    {
        for(String plugin: plugins)
        {
            try{
                Class klass = Class.forName(plugin); //loads the class if it isn't already.
                m.msg("Loaded plugin: ",plugin);
            }catch(ClassNotFoundException e){
                //plugin not loaded.
                //m.msg("Unable to load plugin: ",plugin);
            }
            catch(Exception e){
                m.msg("Unexpected exception while loading plugin: ",plugin);
            }
        }
    }

    public static void initialiseGui() //at start of Gui constructor
    {
        //do panels
        for(JPanel panel: panels)
        {
            Main.guiform.tabs.addTab(panel.getName(), panel);
        }

        //do title
        String newtitle = Main.guiform.getTitle() + guititle;
        Main.guiform.setTitle(newtitle);

    }

    public static void initialiseGui2() //at end of Gui constructor
    {
        //do gui callbacks
        for(Runnable r: guicallbacks)
        {
            try{
                r.run();
            }catch(Exception e){
                m.err("Error while loading plugin.");
                e.printStackTrace();
            }
        }

    }

    public static void addGuiPanel(JPanel panel, String panelname)
    {
        panel.setName(panelname);
        panels.add(panel);
    }

    public static void appendTitle(String title)
    {
        guititle = guititle + " " + title;
    }

    public static void addGuiInitialisedCallback(Runnable r)
    {
        guicallbacks.add(r);
    }
}

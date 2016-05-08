/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import java.util.Vector;
import java.awt.Component;
import java.awt.Container;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import shared.m;
import shared.b;
import shared.FileUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.io.File;
import java.util.LinkedHashSet;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.WeakHashMap;
import shared.Pair;

/*
 * General idea is to do nothing unless changing to a non-default language, in which case:
 * -load and parse the new language file.
 * -store the english version of each gui component. (only the first time the language is changed)
 * -set the gui elements
 * -intercept m calls
*/

public class translation
{
    private static final String sep = "{->}";
    private static final String defaultlanguage = "en";
    private static final boolean updateEvenWhenNewLanguageIsSameAsPrevious = true;
    private static String curlanguage = defaultlanguage;
    private static boolean enabled = false;
    private static String pathToStringFiles = null;

    //missing translation recorder
    private static boolean doRecordMissingTranslations = true;
    private static LinkedHashSet<String> missingTranslations = new LinkedHashSet(); //linked to maintain order
    private static HashSet<String> allstrings;

    //string->string  translations
    private static Map<String,String> translator;// = new HashMap();

    //auto-registered components tied to their string.
    //static Map<Object,String> defaults;
    private static WeakHashMap<Component, String> defaults = new WeakHashMap();
    
    //registered components tied to their resource-name:
    private static WeakHashMap<Component, String> resourcecomponents = new WeakHashMap();

    //registered Gui Forms
    //private static HashSet<java.lang.ref.WeakReference<Container>> guiforms = new HashSet();
    private static WeakHashMap<Container, String> guiforms = new WeakHashMap();

    public static void registerGUIForm(Container c)
    {
        if(!enabled) return;
        //guiforms.add(new java.lang.ref.WeakReference(c));
        guiforms.put(c, null);
        if(updateEvenWhenNewLanguageIsSameAsPrevious || !curlanguage.equals(defaultlanguage))
        {
            traverseGuiForm(c/*, true, true*/,null);
        }
    }
    public static void registerResourceString(String path, JTextComponent textbox)
    {
        if(!enabled) return;
        resourcecomponents.put(textbox, path);
        if(updateEvenWhenNewLanguageIsSameAsPrevious || !curlanguage.equals(defaultlanguage))
        {
            traverseResource(textbox);
        }
    }

    public static void enable(String pathToStringFiles2)
    {
        if(pathToStringFiles2.endsWith("/")) pathToStringFiles2 = pathToStringFiles2.substring(0, pathToStringFiles2.length()-1);
        pathToStringFiles = pathToStringFiles2;
        enabled = true;
    }
    public static void disable()
    {
        enabled = false;
    }
    public static String getCurLanguage()
    {
        return curlanguage;
    }
    public static String getDefaultLanguage()
    {
        return defaultlanguage;
    }
    public static void test()
    {
        //saveCurrentStrings();

        //setLanguage("de");
    }
    public static void testCurrentLanguage()
    {
        testLanguage(curlanguage);
    }
    public static void testLanguage(String language)
    {
        //translator = java.util.Collections.synchronizedMap(new HashMap());
        Vector<shared.Pair<String,String>> lang = new Vector();
        loadLanguage(language,null,lang);

        HashSet<String> strings = getAllLiteralStrings();

        //check for keys that are not string literals:
        for(Pair<String,String> t: lang)
        {
            if(!strings.contains(t.left))
            {
                m.warn("Translation key is not a string literal(may not be a problem): ",t.left);
            }
        }

        //check for empty translations:
        for(Pair<String,String> t: lang)
        {
            if(t.right.equals("") && !t.left.equals("")) m.warn("Right hand side is empty: ",t.left);
        }

        //check for duplicate keys:
        for(Pair<String,String> t: lang)
        {
            int freq = 0;
            for(Pair<String,String> s: lang)
            {
                if(t.left.equals(s.left)) freq++;
            }
            if(freq>1) m.warn("Key is present more than once: ",t.left);
        }

        m.status("Done testing language!");
    }
    public static void setLanguage(String language)
    {
        if(!enabled) return;

        String prevlanguage = curlanguage;
        curlanguage = language;

        //things that are only done when going to the non-default language. (we could just do it, but it's less efficient for a lot of things.)
        if(updateEvenWhenNewLanguageIsSameAsPrevious || !prevlanguage.equals(language))
        {
            //load string->string translations from file.
            translator = java.util.Collections.synchronizedMap(new HashMap());
            loadLanguage(language,translator,null);

            //auto-find gui elements, setting new values, and saving the defaults if we haven't already.
            /*for(WeakReference<Container> form: guiforms)
            {
                Container c = form.get();
                if(c!=null)
                {
                    traverseGuiForm(c,null);
                }
            }*/
            for(Container c: guiforms.keySet())
            {
                traverseGuiForm(c,null);
            }
            //boolean storeDefaults = (defaults==null);
            //if(storeDefaults) defaults = java.util.Collections.synchronizedMap(new IdentityHashMap());
            //traverseGuiForm(gui.Main.gui, storeDefaults, true);

            //enable translations.
            m.state.curstate.translate = true;
            shared.GetResource.enableTranslations = true;

            //m.msg("Setting language to ",language);
        }

        //set manually registered components from their resource.
        for(Component textbox: resourcecomponents.keySet())
        {
            traverseResource(textbox);
        }

    }
    private static void traverseResource(Component c)
    {
        if(c instanceof JTextComponent)
        {
            JTextComponent tc = (JTextComponent)c;
            tc.setText(shared.GetResource.getResourceAsString(resourcecomponents.get(tc)));

            //hack to scroll the help window down.
            if(tc instanceof JTextArea)
            {
                final JTextComponent textbox2 = tc;
                javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {

                    public void run() {
                        ((JTextArea)textbox2).scrollRectToVisible(new java.awt.Rectangle(0,0,1,1));
                    }
                });
            }
        }
    }
    //loads the string translator from file.
    private static void loadLanguage(String language, Map<String,String> trans, Vector<shared.Pair<String,String>> output)
    {
        //'language' is the 2-letter language code.
        boolean asresource = true;

        //translator = java.util.Collections.synchronizedMap(new HashMap());
        //defaults = java.util.Collections.synchronizedMap(new HashMap());

        String langFile;
        if(asresource)
        {
            langFile = shared.GetResource.getResourceAsString(pathToStringFiles+"/"+language+".txt");
        }
        else
        {
            langFile = FileUtils.ReadFileAsString(FileUtils.GetInitialWorkingDirectory()+"/"+language+".txt");
        }

        langFile = langFile.replace("\r", ""); //hack for windows eol
        String[] lines = langFile.split("\n");
        for(int i=0;i<lines.length;i++)
        {
            String line = lines[i];
            int index = line.indexOf(sep);
            if(index==-1 && (line.startsWith("//")||line.equals(""))) continue;  //skip comments and empty lines.
            String left = line.substring(0, index);
            String right = line.substring(index+sep.length());
            if(left.length()==0 || right.length()==0)
            {
                throw new shared.uncaughtexception("Invalid language file at line "+Integer.toString(i+1));
            }

            if(trans!=null) trans.put(left, right);
            if(output!=null) output.add(new shared.Pair(left, right));
        }
    }
    public static void recordMissingTranslations()
    {
        doRecordMissingTranslations = true;
        missingTranslations = new LinkedHashSet();
    }
    public static void saveMissingTranslations()
    {
        allstrings = getAllLiteralStrings();
        
        StringBuilder result = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        for(String str: missingTranslations)
        {
            if(allstrings.contains(str))
            {
                result.append(str+sep+"\n");
                result2.append(str+sep+str+"\n");
            }
        }
        byte[] resultdata = b.StringToBytes(result.toString());
        byte[] result2data = b.StringToBytes(result2.toString());
        FileUtils.WriteFile(FileUtils.GetInitialWorkingDirectory()+"/DrizzleNeededTranslations.txt", resultdata);
        FileUtils.WriteFile(FileUtils.GetInitialWorkingDirectory()+"/DrizzleNeededTranslationsB.txt", result2data);
    }
    public static String translate(String... strs)
    {
        StringBuilder result = new StringBuilder();
        for(String str: strs)
        {
            result.append(translate(str));
        }
        return result.toString();
    }
    public static String translate(String str)
    {
        if(!enabled) return str;

        String result = null;
        if(translator!=null)
        {
            result = translator.get(str);
            if(result==null)
            {
                //if(shared.State.AllStates.getStateAsBoolean("recordtrans"))
                if(!curlanguage.equals(defaultlanguage))
                {
                    if(doRecordMissingTranslations && str!=null && !str.equals(""))
                    {
                        //FileUtils.AppendText(FileUtils.GetInitialWorkingDirectory()+"DrizzleNeededTranslations.txt", str+sep+str+"\n");
                        //missingTranslations.
                        boolean didnthave = missingTranslations.add(str);
                        if(didnthave)
                        {
                            //if(str.equals("Save settings now"))
                            //{
                            //    int d2 = 0;
                            //}
                            //int dummy=0;
                        }
                    }
                }
                //if not in translator, leave it as is.
                result = str;
            }
        }
        else
        {
            result = str;
        }

        return result;
    }
    public static void printStringsForAllGuiForms()
    {
        LinkedHashSet<String> vals = new LinkedHashSet();
        getAllCurrentGuiStrings(vals);
        StringBuilder output = new StringBuilder();
        for(String s: vals)
        {
            output.append(s+sep+"\n");
        }
        FileUtils.WriteFile(FileUtils.GetInitialWorkingDirectory()+"/drizzleGuiformsStrings.txt", b.StringToBytes(output.toString()));
    }
    private static void getAllCurrentGuiStrings(LinkedHashSet<String> vals)
    {
        /*for(WeakReference<Container> formref: guiforms)
        {
            Container form = formref.get();
            if(form!=null)
            {
                //traverseGuiForm(form,true,false,vals);
            }
        }*/
        for(Container form: guiforms.keySet())
        {
            traverseGuiForm(form,vals);
        }
        /*if(defaults==null)
        {
            defaults = java.util.Collections.synchronizedMap(new IdentityHashMap());
            //JFrame g = gui.Main.gui;
            //Vector<JLabel> labels = new Vector();
            for(WeakReference<Container> formref: guiforms)
            {
                Container form = formref.get();
                if(form!=null)
                {
                    traverseGuiForm(form,true,false);
                }
            }
        }
        for(String val: defaults.values())
        {
            vals.add(val);
        }*/
    }
    public static LinkedHashSet<String> getAllLiteralStrings()
    {
        LinkedHashSet<String> vals = new LinkedHashSet();

        //get the gui strings.
        //getAllCurrentGuiStrings(vals);


        //get the string literals
        String[] ignorePackages = {"/org/bouncycastle","/org/apache","/org/mortbay","/javax","/ie/wombat","/SevenZip","/automation/fileLists"};
        for(String res: shared.GetResource.listAllResources())
        {
            boolean skip = false;
            for(String ig: ignorePackages)
            {
                if(res.startsWith(ig))
                {
                    skip = true;
                }
            }
            if(skip) continue;
            if(res.endsWith(".class"))
            {
                shared.IBytestream c = shared.ByteArrayBytestream.createFromByteArray(shared.GetResource.getResourceAsByteArray(res));
                classfiles.classfile cf = new classfiles.classfile(c);
                for(String s: cf.getAllConstantStrings())
                {
                    //m.msg(s);
                    if(s.contains("\n") || s.contains("\r") ||s.contains(sep))
                    {
                        //m.warn("String literal contains newline or separator: ",s);
                        //m.warn("Ignoring: ",s);
                    }
                    else
                    {
                        vals.add(s);
                    }
                }
            }
        }

        return vals;
    }
    public static void saveCurrentStrings()
    {
        //translator = java.util.Collections.synchronizedMap(new HashMap());
        HashSet<String> vals = getAllLiteralStrings();

        StringBuilder result = new StringBuilder();
        for(String val: vals)
        {
            //String val = defaults.get(key);
            if(val.equals("")) continue;
            result.append(/*"//"+*/val+sep+"\n");//+" : "+val+"\n");
        }
        String result2 = result.toString();
        FileUtils.WriteFile(FileUtils.GetInitialWorkingDirectory()+"/drizzle.allstrings.txt", b.StringToBytes(result2));
    }

    private static void traverseGuiForm(Container c/*, boolean storeDefaults, boolean setNewValues*/,Set vals)
    {
        for(Component child: c.getComponents())
        {
            //recurse:
            if (child instanceof Container)
            {
                traverseGuiForm((Container)child/*,storeDefaults,setNewValues*/,vals);
            }
        }

        //Get text from component if applicable.
        //String text = null;

        //if(!(c instanceof JTabbedPane)) return;
        
        //skip components marked as not-to-be-translated.
        if (c instanceof JComponent)
        {
            JComponent c2 = (JComponent)c;
            Boolean trans = (Boolean)c2.getClientProperty("trans");
            if(trans==null) trans = true; //default
            
            if(!trans) return;
        }
        
        if (c instanceof JLabel)
        {
            JLabel label = (JLabel)c;
            //m.msg(label.getText());
            //if(label.getText().startsWith("(Select an Age"))
            //{
            //    int dummy=0;
            //}
            //if(storeDefaults) defaults.put(c, label.getText());
            //if(setNewValues) label.setText(translate(defaults.get(c)));
            //if(storeDefaults) label.putClientProperty("def", label.getText());
            //if(setNewValues) label.setText(translate((String)label.getClientProperty("def")));
            //String def = (String)label.getClientProperty("def");
            //if(def==null)
            //{
            //    def = label.getText();
            //    label.putClientProperty("def", def);
            //}
            //label.setText(translate(def));
            //if(vals!=null) vals.add(def);
            String def = defaults.get(c);
            if(def==null)
            {
                def = label.getText();
                defaults.put(c, def);
            }
            label.setText(translate(def));
            if(vals!=null) vals.add(def);
            //translator.put(text, text);
            //m.msg("jlabel found! "+text);
        }
        else if (c instanceof JButton)
        {
            JButton button = (JButton)c;
            //if(storeDefaults) defaults.put(c, button.getText());
            //if(setNewValues) button.setText(translate(defaults.get(c)));
            //if(storeDefaults) button.putClientProperty("def", button.getText());
            //if(setNewValues) button.setText(translate((String)button.getClientProperty("def")));
            String def = defaults.get(c);
            if(def==null)
            {
                def = button.getText();
                defaults.put(c, def);
            }
            button.setText(translate(def));
            if(vals!=null) vals.add(def);
            //translator.put(text, text);
            //m.msg("jbutton found! "+text);
        }
        else if (c instanceof JPanel)
        {
            JPanel panel = (JPanel)c;
            Border border = panel.getBorder();
            if(border instanceof TitledBorder)
            {
                TitledBorder tborder = (TitledBorder)border;
                //if(storeDefaults) defaults.put(c, tborder.getTitle());
                //if(setNewValues) tborder.setTitle(translate(defaults.get(c)));
                //if(storeDefaults) panel.putClientProperty("def", tborder.getTitle());
                //if(setNewValues) tborder.setTitle(translate((String)panel.getClientProperty("def")));
                String def = defaults.get(c);
                if(def==null)
                {
                    def = tborder.getTitle();
                    defaults.put(c, def);
                }
                tborder.setTitle(translate(def));
                if(vals!=null) vals.add(def);
                //translator.put(text, text);
                //m.msg("titled border found! "+text);
            }
        }
        else if (c instanceof JCheckBox)
        {
            JCheckBox checkbox = (JCheckBox)c;
            //if(storeDefaults) defaults.put(c, checkbox.getText());
            //if(setNewValues) checkbox.setText(translate(defaults.get(c)));
            //if(storeDefaults) checkbox.putClientProperty("def", checkbox.getText());
            //if(setNewValues) checkbox.setText(translate((String)checkbox.getClientProperty("def")));
            String def = defaults.get(c);
            if(def==null)
            {
                def = checkbox.getText();
                defaults.put(c, def);
            }
            checkbox.setText(translate(def));
            if(vals!=null) vals.add(def);
            //translator.put(text, text);
            //m.msg("checkbox: "+text);
        }
        else if (c instanceof JRadioButton)
        {
            JRadioButton radiobutton = (JRadioButton)c;
            //radiobutton.put
            //if(storeDefaults) defaults.put(c, radiobutton.getText());
            //if(setNewValues) radiobutton.setText(translate(defaults.get(c)));
            //if(storeDefaults) radiobutton.putClientProperty("def", radiobutton.getText());
            //if(setNewValues) radiobutton.setText(translate((String)radiobutton.getClientProperty("def")));
            String def = defaults.get(c);
            if(def==null)
            {
                def = radiobutton.getText();
                defaults.put(c, def);
            }
            radiobutton.setText(translate(def));
            if(vals!=null) vals.add(def);
            //translator.put(text, text);
            //m.msg("radiobutton: "+text);
        }
        else if (c instanceof JTabbedPane)
        {
            JTabbedPane tabs = (JTabbedPane)c;
            for(int i=0;i<tabs.getTabCount();i++)
            {
                Component tab = tabs.getComponentAt(i);
                JComponent ptab = (JComponent)tab;
                //if(storeDefaults) ptab.putClientProperty("def", tabs.getTitleAt(i));
                //if(setNewValues) tabs.setTitleAt(i, translate((String)ptab.getClientProperty("def")));
                String def = defaults.get(tab);
                if(def==null)
                {
                    def = tabs.getTitleAt(i);
                    defaults.put(tab, def);
                }
                tabs.setTitleAt(i,translate(def));
                if(vals!=null) vals.add(def);
            }
        }
        else if (c instanceof JDialog)
        {
            JDialog frame = (JDialog)c;
            String def = defaults.get(c);
            if(def==null)
            {
                def = frame.getTitle();
                defaults.put(c, def);
            }
            frame.setTitle(translate(def));
            if(vals!=null) vals.add(def);

            //c.invalidate();
            //c.validate();
            c.repaint();
        }
        else if (c instanceof JFrame)
        {
            JFrame frame = (JFrame)c;
            String def = defaults.get(c);
            if(def==null)
            {
                def = frame.getTitle();
                defaults.put(c, def);
            }
            frame.setTitle(translate(def));
            if(vals!=null) vals.add(def);

            //c.invalidate();
            //c.validate();
            c.repaint();
        }
        else if (c instanceof JFileChooser)
        {
            JFileChooser frame = (JFileChooser)c;
            String def = defaults.get(c);
            if(def==null)
            {
                def = frame.getDialogTitle();
                defaults.put(c, def);
            }
            frame.setDialogTitle(translate(def));
            if(vals!=null) vals.add(def);

            //c.invalidate();
            //c.validate();
            c.repaint();
        }
        //add text to translation strings.
        /*if(text!=null)
        {
            //translator.put(text, text);
            if(storeDefaults)
            {
                defaults.put(c, text);
                //translator.put(text,text);
            }
        }*/
    }
}

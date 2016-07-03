package auto.mod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.*;
import shared.m;
import prpobjects.*;
import shared.Flt;


public class AutoMod_MakeWidescreen {
    public static float deg(float x)
    { return (float) (x/PI*180.f); }
    
    public static float rad(float x)
    { return (float) (x*PI/180.f); }
    
    /**
     * Makes all cameras and PostEffectMods widescreen in the given PRP.
     * Saves the file to the output folder even if there weren't any modifications.
     * @param file the name of the file to modify
     * @param ratio the aspect ratio (eg: 1.333, 1.78)
     * @param outfolder the folder to write the output file
     * @return true if the file was modified, false otherwise.
     */
    public static boolean makePrpWidescreen(String file, float ratio, String outfolder)
    {
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(file, true);

        boolean modif = AutoMod_MakeWidescreen.makePrpWidescreen(prp, ratio);

        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
        
        return modif;
    }
    
    /**
     * Makes all cameras and PostEffectMods widescreen in the given PRP.
     * @param prp the file to modify
     * @param ratio the aspect ratio (eg: 1.333, 1.78)
     * @return true if the file was modified, false otherwise.
     */
    public static boolean makePrpWidescreen(prpfile prp, float ratio)
    {
        boolean modified = false;
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plCameraModifier1))
        {
            ro.hasChanged = true;
            modified = true;
            //m.status("Changing " + ro.header.desc.objectname.toString());
            x009BCameraModifier1 cam = ro.castTo();
            cam.FOVw = new Flt(deg((float)(2*atan(ratio*tan(rad(cam.FOVh.toJavaFloat())/2f)))));
        }
        
        for (PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plPostEffectMod))
        {
            ro.hasChanged = true;
            modified = true;
            //m.status("Changing " + ro.header.desc.objectname.toString());
            plPostEffectMod pem = ro.castTo();
            pem.fovX = new Flt(deg((float)(2*atan(ratio*tan(rad(pem.fovY.toJavaFloat())/2f)))));
        }
        
        return modified;
    }
    
    /**
     * Makes all PRP files in the given folder widescreen.
     * Files are overwritten with the new version automatically, but only if there were any modifications applied to them.
     * Read: for GUIs which were made specifically for 4:3 ratio (such as the telescope), the new ratio is not applied,
     * instead a 1.333 ratio is used (which does fix the tan ratio bug, though).
     * Some PRPs are also converted to use the new ratio, but as a safeguard some modifications (font size) are applied
     * to keep these interfaces usable.
     * @param prpfolder the name of the folder containing PRPs
     * @param ratio the aspect ratio (eg: 1.333, 1.78)
     */
    public static void makeAllPrpsWidescreen(String prpfolder, float ratio)
    {
        File folder = new File(prpfolder);
        File[] folderchildren = folder.listFiles();
        
        for(File child: folderchildren)
        {
            if (child.getAbsolutePath().endsWith(".prp"))
            {
                float usedRatio = ratio;
                if (
                        // These files must be kept at the original ratio - otherwise the left and right borders
                        // end up showing the camera render behind it (think the telescope, for instance)
                        // GUIs
                        child.getName().equals("GUI_District_telescope.prp") ||
                        child.getName().equals("GUI_District_AvatarCustomization.prp") ||
                        child.getName().equals("GUI_District_CalibrationGUI.prp") ||
                        // GUIs embedded in Ages files
                        child.getName().equals("Teledahn_District_tldnVaporScope.prp") ||
                        child.getName().equals("Ercana_District_BakingRmGUI.prp") ||
                        child.getName().equals("Ercana_District_BakingRmGUIBad.prp") ||
                        child.getName().equals("GreatZero_District_CalibrationMarkerGameGUI.prp") ||
                        child.getName().equals("Kadish_District_kdshScope01.prp") ||
                        child.getName().equals("Kadish_District_kdshScope02.prp") ||
                        child.getName().equals("Kadish_District_kdshScope03.prp") ||
                        // EoA
                        child.getName().equals("GUI_District_xSpecialEffectGlare.prp") ||
                        child.getName().equals("Todelmer_District_MiniScope.prp") ||
                        // BUG: widescreen often breaks rendering of text
                        // Force these to render in 4:3, as it doesn't really break hummerjun
                        // (could maybe disable the kScaleWithResolution flag instead - for now we won't bother with it)
                        child.getName().equals("GUI_District_OptionsHelpGUI.prp") ||
                        child.getName().equals("GUI_District_AdvancedGameSettingsDialog.prp") ||
                        child.getName().equals("GUI_District_GameSettingsDialog.prp") ||
                        child.getName().equals("GUI_District_KeyMap2Dialog.prp") ||
                        child.getName().equals("GUI_District_KeyMapDialog.prp") ||
                        child.getName().equals("GUI_District_OptionsHelpGUI.prp") ||
                        child.getName().equals("GUI_District_OptionsMenuGUI.prp") ||
                        child.getName().equals("GUI_District_StartupHelpGUI.prp")
                    )
                    usedRatio = 1.33333f;
                m.status("Processing " + child.getAbsolutePath() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child.getAbsoluteFile(), true);

                if (makePrpWidescreen(prp, usedRatio))
                {
                    // BUG: widescreen often breaks rendering of text
                    // If the GUI must really be rendered in 16:9, fix
                    // it in the following if statements
                    if (child.getName().equals("GUI_District_KIMini.prp") || child.getName().equals("GUI_District_KIMain.prp"))
                    {
                        // GPS coordinates are clipped, which makes the GZ quest unsolvable
                        // Fix it by using a smaller font
                        PrpRootObject[] objs = {
                            prp.findObject("GUITextBoxGPS01", Typeid.pfGUITextBoxMod),
                            prp.findObject("GUITextBoxGPS02", Typeid.pfGUITextBoxMod),
                            prp.findObject("GUITextBoxGPS03", Typeid.pfGUITextBoxMod),
                        };
                        for (PrpRootObject obj: objs)
                        {
                            // first, check if the object actually exists
                            // the GPS is in KIMini in the PotS KI, and in KIMain in the online version
                            if (obj != null)
                            {
                                pfGUITextBoxMod textObj = obj.castTo();
                                textObj.parent.colorScheme.fontsize = 8;
                                obj.markAsChanged();
                            }
                        }
                    }
                    String outputfilename = prpfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                    prp.saveAsFile(outputfilename);
                }
            }
        }
    }

    public static void makeUruExplorerWidescreen(String urufolder, float aspectratio)
    {
        RandomAccessFile raf;
        
        try {
            raf = new RandomAccessFile(urufolder+"/UruExplorer.exe", "rw");
        } catch (FileNotFoundException ex) {
            m.err("Couldn't open UruExplorer.exe");
            return;
        }
        
        try
        {
            // executable code seems little endian - we must reverse byte order
            int bits = Float.floatToIntBits(deg((float)(2*atan(aspectratio*tan(rad(66.7f)/2f)))));
            
            byte[] bytes = new byte[4];
            // reverse those nasssty little ho-bytes !
            bytes[0] = (byte)(bits & 0xff);
            bytes[1] = (byte)((bits >> 8) & 0xff);
            bytes[2] = (byte)((bits >> 16) & 0xff);
            bytes[3] = (byte)((bits >> 24) & 0xff);
            
            raf.seek(0x146957);
            raf.write(bytes);
            raf.seek(0x146ba9);
            raf.write(bytes);
            raf.close();
            
            m.msg("Done hacking UruExplorer.exe. Please remember I'm just writing stuff blindly into it, there is no guarantee it'll work.");
        }
        catch (IOException ex)
        {
            m.err("Somehow failed to modify UruExplorer.exe !");
        }
    }

    public static void increateUruExplorerPitch(String urufolder)
    {
        RandomAccessFile raf;
        
        try {
            raf = new RandomAccessFile(urufolder+"/UruExplorer.exe", "rw");
        } catch (FileNotFoundException ex) {
            m.err("Couldn't open UruExplorer.exe");
            return;
        }
        
        try
        {
            int bits = Float.floatToIntBits(1.56f); // roughly pi/2. Should be enough while avoiding gimbal lock or whatever.
            
            byte[] bytes = new byte[4];
            bytes[0] = (byte)(bits & 0xff);
            bytes[1] = (byte)((bits >> 8) & 0xff);
            bytes[2] = (byte)((bits >> 16) & 0xff);
            bytes[3] = (byte)((bits >> 24) & 0xff);
            
            //raf.seek(0x1469cb);
            //raf.seek(0x146c23);
            raf.seek(0x65014c);
            raf.write(bytes);
            raf.close();
            
            m.msg("Done hacking UruExplorer.exe. Please remember I'm just writing stuff blindly into it, there is no guarantee it'll work.");
        }
        catch (IOException ex)
        {
            m.err("Somehow failed to modify UruExplorer.exe !");
        }
    }
    
    public static void makeResolution(String urufolder, int resX, int resY)
    {
        m.warn("dev_mode.dat hacking not yet implemented");
        
        // there is a lot of stuff to read before getting to the resolution -
        // I'm not writing this code yet.
    }
    
    public static void autoWidescreenAll(String urufolder, float aspectratio)
    {
        m.status("Checking the folder you gave..." + urufolder);
        
        if (urufolder.endsWith("/") || urufolder.endsWith("\\"))
            urufolder = urufolder.substring(0, urufolder.length()-1);
        
        // make sure this is a PotS folder !
        // TODO: make other things sure: that it's not a Shard folder,
        // that it has the correct official no-cd, etc.
        if(!auto.AllGames.getPots().isFolderX(urufolder)) return;
        if(!uam.Uam.HasPermissions(urufolder)) return;
        
        makeAllPrpsWidescreen(urufolder + "/dat", aspectratio);
        makeUruExplorerWidescreen(urufolder, aspectratio);
        
        m.status("Done making all widescreen !");
    }
    
    public static void autoSpecialPRP(String file, float ratio)
    {
        File fileobj = new File(file);
        prpfile prp = prpobjects.prpfile.createFromFile(fileobj.getAbsoluteFile(), true);
        makePrpWidescreen(prp, ratio);
        prp.saveAsFile(file);
    }
}

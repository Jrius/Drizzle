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
    
    public static boolean makePrpWidescreen(String file, float ratio, String outfolder)
    {
        prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(file, true);

        boolean modif = AutoMod_MakeWidescreen.makePrpWidescreen(prp, ratio);

        String outputfilename = outfolder + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
        prp.saveAsFile(outputfilename);
        
        return modif;
    }
    
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
    
    public static void makeAllPrpsWidescreen(String prpfolder, float ratio)
    {
        File folder = new File(prpfolder);
        File[] folderchildren = folder.listFiles();
        
        for(File child: folderchildren)
        {
            if (child.getAbsolutePath().endsWith(".prp"))
            {
                if (
                        child.getName().equals("GUI_District_telescope.prp") ||
                        child.getName().equals("GUI_District_AvatarCustomization.prp")
                    )
                    continue;
                m.status("Processing " + child.getAbsolutePath() + "...");
                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child.getAbsoluteFile(), true);

                if (makePrpWidescreen(prp, ratio))
                {
                    String outputfilename = prpfolder + "/dat/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
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
        
        // make sure this is a PotS folder !
        // Will have to make other things sure: that it's not a Shard folder,
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

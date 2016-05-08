/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import auto.conversion.FileInfo;
import auto.conversion.Info;
import java.io.File;
import shared.m;
import java.util.ArrayList;
import shared.FileUtils;
import java.util.Vector;
import prpobjects.Typeid;
import prpobjects.prpfile;
import prpobjects.PrpRootObject;

public class Max
{
    static final boolean deleteTextureFileAfterConvert = true;

    public static void convert3dsmaxToPots(String maxfolder, String potsfolder, String agenames, boolean partialAge)
    {
        File datfolder = new File(maxfolder+"/dat/");
        ArrayList<String> agenamelist = new ArrayList();
        boolean isempty = true;
        for(String item: agenames.split(","))
        {
            String agename = item.trim();
            if(!agename.equals(""))
            {
                isempty = false;
                boolean found = false;
                for(File f: datfolder.listFiles())
                {
                    if(f.isFile() && f.getName().toLowerCase().equals(agename.toLowerCase()+".age"))
                    {
                        String realagename = f.getName().replace(".age", "");
                        agenamelist.add(realagename);
                        found = true;
                        break;
                        //m.msg("Found Age: "+agename);
                    }
                }
                //agenamelist.add(agename);
                if(!found) m.warn("Unable to find Age: ",agename);
            }
        }

        if(isempty)
        {
            //m.msg("You must specify an Age to conver");
            m.msg("No Ages specified; converting all Ages in 3dsmax's export folder...");
            convert3dsmaxToPots(maxfolder,potsfolder,partialAge);
            return;
        }
        else
        {
            convert3dsmaxToPots(maxfolder,potsfolder,agenamelist,partialAge);
        }
    }
    public static void convert3dsmaxToPots(String maxfolder, String potsfolder, ArrayList<String> ages, final boolean partialAge)
    {
        File datfolder = new File(maxfolder+"/dat/");

        if(ages.size()==0)
        {
            m.warn("None of the Ages were found in the 3dsmax export folder.");
            return;
        }

        //convert each found Age:
        for(String agename: ages)
        {
            if(!partialAge)
            {
                if(agename.toLowerCase().equals("personal") || agename.toLowerCase().equals("pahts"))
                {
                    m.err("Woah there!  You probably meant to have the 'Partial Age' option set when making a Relto page or AhraPahts shell.");
                    return;
                }
            }

            m.status("Converting Age: ",agename);
            m.increaseindentation();

            //convert .age file:
            String agefilename = maxfolder + "/dat/"+agename+".age";
            byte[] agefilebs_enc = FileUtils.ReadFile(agefilename);
            uru.UruFileTypes type = uru.UruCrypt.DetectType(agefilebs_enc, auto.AllGames.getPots().g);
            if(type!=uru.UruFileTypes.unencrypted)
            {
                //bug with PlasmaPlugin
                m.warn(agefilename," should be plain text, *not* encrypted.  Create it with a regular text editor.");
            }
            byte[] agefilebs = uru.UruCrypt.DecryptAny(agefilebs_enc, type);
            //byte[] agefilebs = uru.UruCrypt.DecryptAny(agefilename, auto.AllGames.getPots().g);
            prpobjects.textfile agefile = prpobjects.textfile.createFromBytes(agefilebs);
            String prefixstr = agefile.getVariable("SequencePrefix");
            int prefix = Integer.parseInt(prefixstr); //bugfix for PlasmaPlugin
            byte[] agefileoutbs = agefile.saveToByteArray();
            agefileoutbs = uru.UruCrypt.EncryptWhatdoyousee(agefileoutbs);
            String agefileout = potsfolder+"/dat/"+agename+".age";
            if(!partialAge)
            {
                FileUtils.WriteFile(agefileout, agefileoutbs, true, true);
            }

            //convert .sum file:
            String sumfileout = potsfolder+"/dat/"+agename+".sum";
            FileUtils.WriteFile(sumfileout, prpobjects.sumfile.createEmptySumfile().getByteArray(),true,true);

            //convert .fni file:
            String fnifilename = maxfolder + "/dat/"+agename+".fni";
            File fnifile = new File(fnifilename);
            prpobjects.textfile fnifilet;
            if(fnifile.exists())
            {
                byte[] fnifilebs = uru.UruCrypt.DecryptAny(fnifilename, auto.AllGames.getPots().g);
                fnifilet = prpobjects.textfile.createFromBytes(fnifilebs);
            }
            else
            {
                //create a default
                fnifilet = new prpobjects.textfile();
                fnifilet.appendLine("Graphics.Renderer.SetYon 100000");
                //fnifilet.appendLine("Graphics.Renderer.Fog.SetDefLinear 1 1000 1");
                fnifilet.appendLine("Graphics.Renderer.Fog.SetDefLinear 0 0 0");
                fnifilet.appendLine("Graphics.Renderer.Fog.SetDefColor 0 0 0");
                fnifilet.appendLine("Graphics.Renderer.SetClearColor 0 0 0");
            }
            byte[] fnifileoutbs = uru.UruCrypt.EncryptWhatdoyousee(fnifilet.saveToByteArray());
            String fnifileout = potsfolder+"/dat/"+agename+".fni";
            if(!partialAge)
            {
                FileUtils.WriteFile(fnifileout, fnifileoutbs, true, true);
            }

            //convert .prp files:
            ArrayList<String> prpfiles = new ArrayList();
            for(File f: datfolder.listFiles())
            {
                if(f.isFile() && f.getName().startsWith(agename+"_District_") && f.getName().endsWith(".prp"))
                {
                    prpfiles.add(f.getName());
                }
            }

            final MaxInfo maxinfo = new MaxInfo();
            //find textures prp
            String texturesPrp = null;
            for(String prp: prpfiles)
            {
                if(prp.endsWith("_District_Textures.prp"))
                {
                    texturesPrp = prp;
                    break;
                }
            }

            if(partialAge && texturesPrp==null)
            {
                //warn the user that this might not be right
                m.warn("Conversion is set to 'Partial Age', but no Textures prp was found.  If your Age has no textures then this is normal, otherwise you should re-export with 3dsmax.");
            }

            for(String prp: prpfiles)
            {
                if(partialAge) //skip BuiltIn and Textures prps for patial ages.
                {
                    if(prp.toLowerCase().endsWith("_district_builtin.prp") || prp.toLowerCase().endsWith("_district_textures.prp"))
                    {
                        m.status("Skipping Prp: ",prp);
                        continue;
                    }
                }

                m.status("Converting Prp: ",prp);
                m.increaseindentation();

                Vector<String> files = new Vector();
                files.add(prp);
                auto.AllGames.GameInfo moulinfo = auto.moul.getGameInfo();
                moulinfo.renameinfo.prefices.put(agename, prefix); //bugfix for PlasmaPlugin
                final auto.conversion.PostConversionModifier pcm = moulinfo.prpmodifier;
                moulinfo.prpmodifier = new auto.conversion.PostConversionModifier() {
                    public void ModifyPrp(Info info, FileInfo file, prpfile prp) {
                        //do the original Moul post conversion modifier:
                        pcm.ModifyPrp(info, file, prp);

                        //new stuff:
                        maxinfo.numSpawnPoints += prp.FindAllObjectsOfType(Typeid.plSpawnModifier).length;

                        maxinfo.numLights += prp.FindAllObjectsOfType(Typeid.plDirectionalLightInfo).length;
                        maxinfo.numLights += prp.FindAllObjectsOfType(Typeid.plOmniLightInfo).length;
                        maxinfo.numLights += prp.FindAllObjectsOfType(Typeid.plLimitedDirLightInfo).length;
                        maxinfo.numLights += prp.FindAllObjectsOfType(Typeid.plSpotLightInfo).length;

                        maxinfo.numPhysics += prp.FindAllObjectsOfType(Typeid.plSimulationInterface).length;

                        //make sounds streaming
                        Max.FixStreamingSounds(prp);

                        //fix object order
                        //moved into prpfile.java, since it will change the order one way or another.
                        //Max.FixObjectOrder(prp);
                    }
                };
                moulinfo.renameinfo.agenames.remove("Personal"); //don't want this renamed as PersonalMoul.
                auto.AllGames.GameConversionSub maxconv = new auto.AllGames.GameConversionSub(moulinfo);
                maxconv.ConvertFiles(maxfolder, potsfolder, files);

                if(partialAge) //pull in the textures for partial Ages
                {
                    if(texturesPrp!=null)
                    {
                        String infile = potsfolder+"/dat/"+prp;
                        String textfile = maxfolder+"/dat/"+texturesPrp;
                        String outfolder = potsfolder+"/dat/";
                        auto.Distiller.DistillTextures(infile,textfile,outfolder);
                    }
                }

                m.decreaseindentation();
            }

            //Bugfix for Cyan's plugin.  It makes duplicate textures and can even produce a corrupt prp when doing so.
            if(Max.deleteTextureFileAfterConvert)
            {
                //for(String prp: prpfiles)
                if(texturesPrp!=null)
                {
                    //if(prp.endsWith("_District_Textures.prp"))
                    //{
                    //m.msg("(Removing Textures prp to fix a Plasma bug.)");
                    String filetodelete = maxfolder+"/dat/"+texturesPrp;
                    FileUtils.DeleteFile(filetodelete, false);
                    //}
                }
            }

            if(!partialAge && maxinfo.numSpawnPoints==0)
            {
                m.warn("Your Age doesn't have a spawnpoint; you won't be able to link into it correctly.");
            }
            if(maxinfo.numLights==0)
            {
                m.warn("Your Age doesn't have any lights; everything may appear black.");
            }
            if(!partialAge && maxinfo.numPhysics==0)
            {
                m.warn("Your Age doesn't have any colliders; you will fall right through the ground.");
            }

            m.decreaseindentation();
            m.status("Done converting Age!: ",agename);
        }
    }
    public static void FixStreamingSounds(prpfile prp)
    {
        //makes all the sounds streaming.
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
        {
            prpobjects.plSoundBuffer sb = ro.castTo();
            sb.flags |= prpobjects.plSoundBuffer.kStreamCompressed;
        }
    }
    private static boolean ensureFolders(String maxfolder, String potsfolder)
    {
        if(!auto.AllGames.getPots().isFolderX(potsfolder))
            return false;
        File datfolder = new File(maxfolder+"/dat/");
        if(!datfolder.exists() || !datfolder.isDirectory())
        {
            m.err("The 3dsmax folder must be set to the folder that the 3dsmax plugin exports to, and so should contain a 'dat' folder.");
            return false;
        }
        File test = new File(maxfolder+"/UruExplorer.exe");
        if(test.exists())
        {
            m.err("The 3dsmax export folder should not be Uru's folder.  Create a folder just for 3dsmax to export to.");
            return false;
        }
        return true;
    }
    public static void convert3dsmaxToPots(String maxfolder, String potsfolder, boolean partialAge)
    {
        //check folders:
        if(!ensureFolders(maxfolder,potsfolder)) return;
        if(!uam.Uam.HasPermissions(potsfolder)) return;
        File datfolder = new File(maxfolder+"/dat/");

        //find ages in export folder:
        ArrayList<String> ages = new ArrayList();
        for(File f: datfolder.listFiles())
        {
            if(f.isFile() && f.getName().endsWith(".age"))
            {
                String agename = f.getName().replace(".age", "");
                ages.add(agename);
                //m.msg("Found Age: "+agename);
            }
        }

        convert3dsmaxToPots(maxfolder,potsfolder,ages,partialAge);

        
    }
    private static class MaxInfo
    {
        int numSpawnPoints = 0;
        int numLights = 0;
        int numPhysics = 0;
    }

    
}

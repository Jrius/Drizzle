/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import auto.hexisle;
import auto.mod.AutoMod_FixFanAge;
import auto.mod.AutoMod_MakeWidescreen;
import auto.mod.AutoMod_Light;
import auto.postmod.PostMod_MystV_WhiteBox;
import auto.prps;
import shared.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Vector;
import prpobjects.Pageid;
import prpobjects.Pagetype;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;
import prpobjects.Uruobjectdesc;
import prpobjects.Uruobjectref;
import prpobjects.plArmatureEffectFootSound;
import prpobjects.plPythonFileMod;
import prpobjects.plSceneObject;
import prpobjects.plSynchedObject;
import realmyst.Idx;
import realmyst.Sdb;
import static shared.m.gettime;

public class CommandLine
{
    public static void HandleCommand(String[] args)
    {
        if(args[0].equals("-help"))
        {
            m.msg("Welcome to Drizzle!");
            m.msg("  (Any of these commands can be compounded into a list, e.g. '-version -prpdiff c:/source.prp c:/dest.prp -help')");
            m.msg("  Information:");
            m.msg("    -version    ->Shows Drizzle's version.");
            m.msg("    -help    ->This help screen.");
            m.msg("    -listgamenames    ->Lists the possible options for 'gamename' arguments in other commands.");
            //m.msg("(gamename is one of: "+auto.Game.getAllGamenames()+" )");
            m.msg("  Server:");
            m.msg("    -mirrordataserver exampleserver.com c:/outfolder    ->Mirrors an Alcugs dataserver at the given address, saving to the given output folder.");
            m.msg("    -generatedataserver c:/infolder c:/outfolder    ->Generates an Alcugs dataserver using the given input Uru installation, saving to the given output folder.");
            m.msg("    -downloadmoulagainfiles c:/outputfolder");
            m.msg("    -downloadsecuremoulagainfiles username password c:/outputfolder");
            m.msg("    -startproxyserver c:/ProxyUruFolder     ->(Please note that there is no cleanup after this runs, so you should restart Drizzle.)");
            m.msg("    -patchmoulbinary c:/path/to/binary.exe server.com");
            //m.msg("    -encryptmoulfile c:/inputfile c:/outputfolder    ->Encrypts the input file with the notthedroids key Talcum uses.");
            m.msg("    -startmoulserver MainPassword server.com c:/DataFolder     ->(Please note that there is no cleanup after this runs, so you should restart Drizzle.)");
            m.msg("    -drizzleintermediary   ->acts as go-between for UruSetup and UruExplorer for Alcugs");
            m.msg("  Prp:");
            //m.msg("    -prpdiff c:/source.prp c:/dest.prp c:/generated.diff.txt");
            m.msg("    -prpdiff c:/version1.prp c:/version2.prp       ->Compares the two PRPs, listing the differences.");
            m.msg("    -folderprpdiff c:/folder1 c:/folder2       ->Same, but operates on all PRPs that are present in both folders.");
            //m.msg("    -changeagename c:/inputfile.prp c:/outputfolder NewAgeName");
            m.msg("    -changeagename c:/inputfile.prp c:/outputfolder NewAgeName       ->Does not change python/ogg files.");
            m.msg("    -changeprefix c:/inputfile.prp c:/outputfolder NewSequencePrefix");
            m.msg("    -changepagename c:/inputfile.prp c:/outputfolder NewPageName");
            m.msg("    -changepagenumber c:/inputfile.prp c:/outputfolder NewPageNumber");
            m.msg("    -changeagenameandprefix c:/inputfile.prp c:/outputfolder NewAgeName NewSequencePrefix    ->Changes python/ogg files.");
            m.msg("    -inplacemod c:/potsfolder dat/inputfile.prp ModName");
            m.msg("    -listinplacemods    ->Displays all the available InplaceMods.");
            m.msg("    -convert3dsmaxtopots c:/3dsmaxexportfolder c:/potsfolder agename1,agename2,etc     ->Converts the files exported by the 3dsmax plugin to Pots.");
            m.msg("    -translateagedown c:/inputfile.prp c:/outputfolder 200.0   ->Translates everying in the prp down so many units.");
            m.msg("    -pullintextures c:/inputfile.prp c:/texturefile.prp c:/outfolder  ->Pulls in the textures a prp file uses.");
            m.msg("    -listobjects c:/inputfile.prp  ->Lists the objects in a prp file.");
            m.msg("    -simpledistill c:/inputfile.prp c:/texturefile.prp c:/outfolder SceneObject1,SceneObject2   ->Experimental!! List as many scene objects as you want.");
            m.msg("    -makewidescreen c:/inputfile.prp c:/outfolder ratio    -> changes the aspect ratio for cameras and GUI objects in this prp");
            m.msg("    -makeallwidescreen c:/inputfolder c:/outfolder ratio    -> sames, except it applies to the whole folder");
            m.msg("    -sunlight c:/inputfile c:/outfolder listOfExcludedObjects    -> bakes sunlight using current stored settings. You'd better use the GUI for this.");
            m.msg("    -agesunlight c:/inputfolder c:/outfolder listOfExcludedObjects ageName    -> bakes sunlight using current stored settings. You'd better use the GUI for this.");
            m.msg("    -ao c:/inputfile c:/outfolder listOfExcludedObjects    -> bakes ao using current stored settings. You'd better use the GUI for this.");
            m.msg("    -ageao c:/inputfolder c:/outfolder listOfExcludedObjects ageName    -> bakes ao using current stored settings. You'd better use the GUI for this.");
            m.msg("    -ageimproveall c:/inputfolder c:/outfolder listOfExcludedObjects ageName    -> bakes ao and sunlight. You'd better use the GUI for this.");
            m.msg("    -striptex c:/inputfile c:/outfolder    -> removes any texture from the PRP");
            m.msg("    -agestriptex c:/inputfolder c:/outfolder ageName    -> removes any texture from the Age");
            m.msg("    -ageopti c:/inputfolder c:/outfolder ageName    -> removes shadows, disable specularity and fix collisions");
            m.msg("    -exactcollisions c:/inputfolder ageName    -> adds exact collisions to this Age. May be heavy ingame.");
            m.msg("    -excludepersistentstates c:/inputfile c:/outfolder    -> marks any animated object with animation not referenced by another object as non-persistent, this can help with Ages "
                    + "using a lot of animation which result in clients being kicked out online. Untested, use at your own risk.");
            m.msg("    -usehigherrestextures c:/inputfolder agename    -> changes some texture references to use a higher res version");
            m.msg("    -squashtextures c:/infolder c:/outfolder ageName -> moves all textures to a single PRP. Useful to reduce video memory consumption on older GPUs in ages like Elodea");
            m.msg("  Subtools:");
            m.msg("    -deepview c:/inputfile.prp    ->Starts DrizzleDeepview.  The inputfile is optional.");
            m.msg("    -folderdiff c:/folder1 c:/folder2    ->Diffs all the files and subfolders between two folders.");
            m.msg("    -makeuruexplorerwidescreen c:/urufolder ratio    -> hacks UruExplorer to use this ratio for first and default third cameras.");
            m.msg("    -increaseviewpitch c:/urufolder    -> hacks UruExplorer to increase pitch angle, thus enabling you to look directly above or below you.");
            m.msg("  Python:");
            m.msg("    -unpackpak c:/pakfile.pak c:/outputfolder gamename    ->Extracts all the .pyc files from a Python22 .pak file.");
            m.msg("    -decompilepyc c:/pycfile.pyc c:/outputfolder    ->Decompiles a .pyc file using DrizzleDecompile.");
            m.msg("    -decompilepak c:/pakfile.pak c:/outputfolder gamename    ->Decompiles all .pyc files within a Python22 .pak file, using DrizzleDecompile.");
            m.msg("    -removepythonoverrides c:/pakfolder c:/outputfolder overriddenpakname.pak gamename    ->Removes the entries from a .pak file, which have overrides in other files.");
            m.msg("    -listpak c:/pakfile.pak gamename         ->Lists the pyfiles that are present in the given .pak file.");
            m.msg("    -diffpaks c:/origpakfile.pak gamename c:/newpakfile.pak gamename   ->Compares two .pak files.");
            m.msg("  Misc:");
            m.msg("    -findsdl c:/dumpfile.dat c:/outputfolder");
        }
        else if(args[0].equals("-findsdl"))
        {
            auto.FindSdlInDump.FindSdlInDump(args[1], args[2]);
        }
        else if(args[0].equals("-simpledistill"))
        {
            auto.Distiller.SimpleDistill(args[1],args[2],args[3],args[4]);
        }
        else if(args[0].equals("-folderdiff"))
        {
            auto.diffs.FolderDiff(args[1],args[2]);
        }
        else if(args[0].equals("-listobjects"))
        {
            auto.prps.ListObjects(args[1]);
        }
        else if(args[0].equals("-drizzleintermediary"))
        {
            auto.DrizzleIntermediary.DoWork(args);
        }
        else if(args[0].equals("-pullintextures"))
        {
            auto.Distiller.DistillTextures(args[1],args[2],args[3]);
        }
        else if(args[0].equals("-diffpaks"))
        {
            onliner.Python.ComparePaks(args[1], args[2], args[3], args[4]);
        }
        else if(args[0].equals("-listpak"))
        {
            auto.Python.ListPak(args[1],args[2]);
        }
        else if(args[0].equals("-encryptmoulfile"))
        {
            File f = new File(args[1]);
            byte[] unencrypted = FileUtils.ReadFile(f);
            byte[] encrypted = uru.UruCrypt.EncryptNotthedroids(unencrypted, moulserver.SuperManager.GetTalcumNotthedroids());
            FileUtils.WriteFile(args[2]+"/"+f.getName(), encrypted, true, true);
        }
        else if(args[0].equals("-translateagedown"))
        {
            File f = new File(args[1]);
            prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(args[1], true);
            float z = Float.parseFloat(args[3]);
            auto.mod.AutoMod_Translate.translateAllObjects(prp, 0.0f, 0.0f, -z);
            prp.saveAsFile(args[2]+"/"+f.getName());
        }
        else if(args[0].equals("-startmoulserver"))
        {
            moulserver.Manager.StartServers(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-patchmoulbinary"))
        {
            moulserver.Patcher.PatchFile(args[1], args[2]);
        }
        else if(args[0].equals("-startproxyserver"))
        {
            moulserver.Proxy.start(args[1]);
        }
        else if(args[0].equals("-downloadsecuremoulagainfiles"))
        {
            moulserver.Client_SaveSecureDownloadFiles.SaveSecureDownloadQueue(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-downloadmoulagainfiles"))
        {
            moulserver.Client_SaveFileServerFiles.SaveSecureDownloadQueue(args[1]);
        }
        else if(args[0].equals("-removepythonoverrides"))
        {
            auto.Python.RemovePythonOverrides(args[1], args[2], args[3], args[4]);
        }
        else if(args[0].equals("-listgamenames"))
        {
            m.msg(auto.Game.getAllGamenames());
        }
        else if(args[0].equals("-convert3dsmaxtopots"))
        {
            auto.Max.convert3dsmaxToPots(args[1], args[2], args[3], false);
        }
        else if(args[0].equals("-decompilepak"))
        {
            auto.Python.DecompilePak(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-decompilepyc"))
        {
            auto.Python.DecompilePyc(args[1], args[2]);
        }
        else if(args[0].equals("-unpackpak"))
        {
            auto.Python.UnpackPak(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-mirrordataserver"))
        {
            uru.server.Dataserver.MirrorServer(args[1], args[2], true, true, true, true, true, "");
        }
        else if(args[0].equals("-generatedataserver"))
        {
            uru.server.Dataserver.CreateFiles(args[1], args[2], true);
        }
        else if(args[0].equals("-input"))
        {
            java.util.Scanner s = new java.util.Scanner(System.in);
            String a = s.nextLine();
            System.out.print(a);
        }
        else if(args[0].equals("-prpdiff"))
        {
            auto.PrpDiff.FindDiff(args[1], args[2]);
        }
        else if(args[0].equals("-changeagename"))
        {
            auto.ChangeNameAndPrefix.ChangeName(args[1], args[2], args[3], false);
        }
        else if(args[0].equals("-changeprefix"))
        {
            auto.ChangeNameAndPrefix.ChangePrefix(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-changeagenameandprefix"))
        {
            auto.ChangeNameAndPrefix.ChangeNameAndPrefix(args[1],args[2],args[3],args[4],true);
        }
        else if(args[0].equals("-changepagename"))
        {
            auto.ChangeNameAndPrefix.ChangePagename(args[1],args[2],args[3]);
        }
        else if(args[0].equals("-changepagenumber"))
        {
            auto.ChangeNameAndPrefix.ChangePagenumber(args[1],args[2],args[3]);
        }
        else if(args[0].equals("-deepview"))
        {
            String filename = (args.length>=2)?args[1]:null;
            deepview2.dvGUI.open(filename);
        }
        else if(args[0].equals("-inplacemod"))
        {
            auto.inplace.Inplace.InplaceMod(args[1],args[2],args[3]);
        }
        else if(args[0].equals("-listinplacemods"))
        {
            auto.inplace.Inplace.printAllModNames();
        }
        else if(args[0].equals("-makewidescreen"))
        {
            AutoMod_MakeWidescreen.makePrpWidescreen(args[1], Float.parseFloat(args[3]), args[2]);
        }
        else if(args[0].equals("-makeallwidescreen"))
        {
            AutoMod_MakeWidescreen.makeAllPrpsWidescreen(args[1], Float.parseFloat(args[3]));
        }
        else if(args[0].equals("-makeuruexplorerwidescreen"))
        {
            AutoMod_MakeWidescreen.makeUruExplorerWidescreen(args[1], Float.parseFloat(args[2]));
        }
        else if(args[0].equals("-increaseviewpitch"))
        {
            AutoMod_MakeWidescreen.increateUruExplorerPitch(args[1]);
        }
        else if(args[0].equals("-rmdump")) // not listed in help command. Only useful for reverse engineering
        {
            auto.realmyst.dumpAllModifiers();
        }
        else if(args[0].equals("-listrmrooms")) // not listed in help command. Only useful for reverse engineering
        {
            auto.realmyst.path = args[1];
            Idx sdbidx = auto.realmyst.S_LoadSceneDatabase();
            for (String roomname: auto.realmyst.S_GetEntryNamesFromBlock(sdbidx.RoomIndex))
                m.msg(roomname);
        }
        else if(args[0].equals("-loadrmroom")) // not listed in help command. Only useful for reverse engineering
        {
            auto.realmyst.S_LoadRMRooms(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-loadrmage")) // not listed in help command. Only useful for reverse engineering
        {
            auto.realmyst.S_TestRun3(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-sunlight"))
        {
            AutoMod_Light.bakeSunSimple(args[1], args[3].split(",|;"), args[2]);
        }
        else if(args[0].equals("-agesunlight"))
        {
            AutoMod_Light.bakeSunSimpleAge(args[1], args[2], args[3].split(",|;"), args[4]);
        }
        else if(args[0].equals("-ao"))
        {
            AutoMod_Light.bakeAOSimple(args[1], args[3].split(",|;"), args[2]);
        }
        else if(args[0].equals("-ageao"))
        {
            AutoMod_Light.bakeAOSimpleAge(args[1], args[2], args[3].split(",|;"), args[4]);
        }
        else if(args[0].equals("-ageimproveall"))
        {
            AutoMod_Light.bakeAllSimpleAge(args[1], args[2], args[3].split(",|;"), args[4]);
        }
        else if(args[0].equals("-striptex"))
        {
            AutoMod_Light.stripTexturesSimple(args[1], args[2]);
        }
        else if(args[0].equals("-agestriptex"))
        {
            AutoMod_Light.stripAgeTexturesSimple(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-ageopti"))
        {
            AutoMod_FixFanAge.simpleOpti(args[1], args[2], args[3]);
        }
        else if(args[0].equals("-exactcollisions"))
        {
            File folder1 = new File(args[1]);
            
            File[] folder1children = folder1.listFiles();
            m.msg("This will take a while, please wait...");
            for(File child1: folder1children)
            {
                if (child1.getName().startsWith(args[2]+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
                {
                    m.msg("  Loading " + child1.getName() + "...");
                    prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                    m.msg("  Editing...");
                    hexisle.createStaticCollidersForAllDrawables(prp);

                    m.msg("  Saving...");
                    String outputfilename = args[1] + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                    prp.saveAsFile(outputfilename);
                }
            }
            m.msg("Done !");
        }
        else if(args[0].equals("-excludepersistentstates"))
        {
            m.warn("Untested command ! Use it at your own risk !");
            AutoMod_FixFanAge.setExcludeFlags(args[1], args[2]);
        }
        else if (args[0].equals("-squashtextures"))
        {
            AutoMod_FixFanAge.squashDuplicateTextures(args[1], args[2], args[3]);
        }
        else if (args[0].equals("-folderprpdiff"))
        {
            File folderA = new File(args[1]);
            File folderB = new File(args[2]);
            
            File[] bFiles = folderB.listFiles();
            for (File file: folderA.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".prp");
                }
            }))
            {
                for (File bFile: bFiles)
                {
                    if (bFile.getName().equals(file.getName()))
                    {
                        // found two of those
                        m.msg("File: " + file.getName());
                        auto.PrpDiff.FindDiff(bFile.getAbsolutePath(), file.getAbsolutePath());
                    }
                }
            }
        }
        else if (args[0].equals("-usehigherrestextures"))
        {
            if (!args[1].endsWith("/") && !args[1].endsWith("\\"))
                args[1] += "/";
            File folder1 = new File(args[1]);
            File texfile = new File(args[1] + args[2] + "_District_Textures.prp");
            prpobjects.prpfile tex = prpobjects.prpfile.createFromFile(texfile, true);
            
            File[] folder1children = folder1.listFiles();
            for(File child1: folder1children)
            {
                if (child1.getName().startsWith(args[2]+"_District_") && child1.getAbsolutePath().endsWith(".prp"))
                {
                    m.msg("  Loading " + child1.getName() + "...");
                    prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(child1.getAbsoluteFile(), true);

                    m.msg("  Editing...");
                    auto.pots.UseHighResTextures(prp, tex);

                    if (prp.hasChanged())
                    {
                        m.msg("  Saving...");
                        String outputfilename = args[1] + "/" + prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
                        prp.saveAsFile(outputfilename);
                    }
                    else
                    {
                        m.msg("  No change required.");
                    }
                }
            }
            m.msg("Done !");
        }
        else if(args[0].equals("-audiotojson"))
        {
            prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(new File(args[1]), true);
            prps.ListSoundsAsJSON(prp, args[2]);
            m.msg("Done !");
        }
        else if(args[0].equals("-version"))
        {
            m.msg("This version of Drizzle is: ",Integer.toString(gui.Version.version));
        }
        else
        {
            m.err("Unknown command.  Use -help for some of the options.");
        }
    }
    public static void HandleArguments(String[] args)
    {
        ArrayList<ArrayList<String>> commands = CommandLine.SplitArgumentsIntoCommands(args);
        if(commands==null) return;

        for(ArrayList<String> command: commands)
        {
            String[] cargs = command.toArray(new String[]{});
            HandleCommand(cargs);
        }

    }

    public static ArrayList<ArrayList<String>> SplitArgumentsIntoCommands(String[] args)
    {
        //split into commands:
        ArrayList<ArrayList<String>> commands = new ArrayList();
        ArrayList<String> command = null;
        for(String arg: args)
        {
            if(arg.startsWith("--"))
            {
                //command within a command; treat as arg.
                if(command==null)
                {
                    m.err("Commands must start with a -, e.g. '-help'.");
                    return null;
                }
                command.add(arg);
            }
            else if(arg.startsWith("-"))
            {
                //new command.
                if(command!=null) commands.add(command); //save the current one
                command = new ArrayList(); //start a new one
                command.add(arg);
            }
            else
            {
                //regular arg.
                if(command==null)
                {
                    m.err("Commands must start with a -, e.g. '-help'.");
                    return null;
                }
                command.add(arg);
            }
        }
        if(command!=null) commands.add(command); //save the last one

        return commands;

    }
}

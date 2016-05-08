/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import shared.m;
import shared.FileUtils;
import java.io.File;
import shared.zip;
import java.text.SimpleDateFormat;
import java.util.Date;
import shared.b;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.ArrayDeque;
import prpobjects.Typeid;
import shared.HttpUtils;
import shared.*;
import prpobjects.PrpRootObject;
import prpobjects.prpfile;
import prpobjects.plLayerBink;
import java.util.ArrayList;

//TODO:
//The client.mfs needs UruExplorer.exe as the top item, and those two items from base have to go to others or be removed. (otherwise UruExplorer.exe won't be autostarted from UruSetup.)

//Sound files:
//scan through prps and check soundbuffer objects flag.
//1)If flag 0x4 or 0x8 is used, decompress to two wav files
//2)if flag 0x10 is used, it shouldn't be decompressed at all
//3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
//if situation 1 and 2 occur, warn
//if situation 1 and 3 occur, do both!

public class Dataserver
{
    boolean writedatafiles = false;
    boolean writemanifestfiles = true;

    enum DataserverType
    {
        alcugs,
        moul,
    }
    DataserverType type;

    String root;
    String outroot;
    //fileusers usage = new fileusers();
    DataserverInfo info;
    DataserverInfo oldinfo;

    ArrayList<String> prpfiles;
    //HashMap<String, String> pages; //maps page to age
    //HashMap<String, String> anims;
    //maps page to set of ages
    HashMap<String, HashSet<String>> pages; //maps pagename to list of ages with that pagename.

    //********* WARNING: modifying this class will invalidate previously saved data ***********
    public static class DataserverInfo implements java.io.Serializable
    {
        HashMap<String,DataserverFileInfo> map = new HashMap<String,DataserverFileInfo>(); //filename->fileinfo
        //(the manifestname ends in .mfs, even if it will be saved as .mfs_moul in the end.)
        public DataserverInfo()
        {
        }
        /*public static enum DataserverFileType
        {

        }*/
        public static class DataserverFileInfo implements java.io.Serializable
        {
            String relpath;
            long filesize;
            long lastmodified;

            byte[] md5;
            HashSet<String> avifiles = new HashSet<String>(); //only prpfiles
            HashMap<String,SfxInfo> sfxfiles = new HashMap<String,SfxInfo>(); //only prpfiles
            HashSet<String> oneshotmods = new HashSet<String>(); //only prpfiles
            byte[] compressedmd5;
            long compressedfilesize;

            public DataserverFileInfo()
            {
            }
        }
        public static class SfxInfo implements java.io.Serializable
        {
            String relpath;
            int flags;
        }
    }

    public static void MirrorServer(String serverdns, String outfolder, boolean includeGeneralFiles, boolean includePotsAges, boolean includeAgelistAges, boolean includeAvailableLinksAges, boolean includeCustomAges, String customAges)
    {
        LinkedHashSet<String> manifests = new LinkedHashSet();
        if(includeGeneralFiles)
        {
            SaveFile(serverdns,outfolder,"/status/lobbylist.txt");
            TryToSaveFile(serverdns,outfolder,"/status/index_eng.html");
            TryToSaveFile(serverdns,outfolder,"/status/index_fra.html");
            TryToSaveFile(serverdns,outfolder,"/status/index_ger.html");
            SaveAndParseAndSaveMfs(serverdns,outfolder,"/install/Expanded/ClientSetupNew.mfs","/install/Expanded/");
            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_clients/drcExplorer/client.mfs","/game_clients/drcExplorer/");
        }
        if(includePotsAges)
        {
            for(String age: auto.ageLists.potsages)
            {
                if(!manifests.contains(age))
                {
                    try{
                        SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
                        manifests.add(age);
                    }catch(Exception e){
                        m.warn("Unable to download all of Age: ",age);
                    }
                }
            }
        }
        if(includeAgelistAges)
        {
            byte[] data = SaveFile(serverdns,outfolder,"/game_data/dat/agelist.txt");
            String data2 = b.BytesToString(data);
            for(String age: data2.split("\n"))
            {
                age = age.trim();
                if(!age.equals(""))
                {
                    if(!manifests.contains(age))
                    {
                        try{
                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
                            manifests.add(age);
                        }catch(Exception e){
                            m.warn("Unable to download all of Age: ",age);
                        }
                    }
                }
            }
        }
        if(includeAvailableLinksAges)
        {
            //actually may have already downloaded this, so let's just read it instead of
            byte[] data = HttpUtils.geturl("http://"+serverdns+"/game_clients/drcExplorer/AvailableLinks.inf");
            String data2 = b.BytesToString(data);
            for(String line: data2.split("\n"))
            {
                line = line.trim();
                String age = null;
                if(line.startsWith("restorationlink:")) age = line.substring("restorationlink:".length(), line.indexOf(","));
                else if(line.startsWith("publiclink:")) age = line.substring("publiclink:".length(), line.indexOf(","));
                else if(line.startsWith("link:")) age = line.substring("link:".length(), line.indexOf(","));
                if(age!=null)
                {
                    if(!manifests.contains(age))
                    {
                        try{
                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
                            manifests.add(age);
                        }catch(Exception e){
                            m.warn("Unable to download all of Age: ",age);
                        }
                    }
                }
            }

        }
        if(includeCustomAges)
        {
            for(String line: customAges.split("\n"))
            {
                line = line.trim();
                if(!line.startsWith("#") && !line.equals(""))
                {
                    String age = line;
                    if(!manifests.contains(age))
                    {
                        try{
                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
                            manifests.add(age);
                        }catch(Exception e){
                            m.warn("Unable to download all of Age: ",age);
                        }
                    }
                }
            }
        }
        m.status("Done mirroring the dataserver!");
    }

    private static boolean HasFile(String filename, byte[] md5, boolean md5OnDecompressed)
    {
        File f = new File(filename);
        if(f.isDirectory()) throw new shared.uncaughtexception("A folder exists which should be a file: "+filename);
        if(!f.exists()) return false;
        if(!md5OnDecompressed)
        {
            byte[] actualmd5 = shared.CryptHashes.GetHash(filename, shared.CryptHashes.Hashtype.md5);
            return b.isEqual(md5, actualmd5);
        }
        else
        {
            byte[] actualmd5 = shared.CryptHashes.GetHash(shared.zip.decompressGzip(FileUtils.ReadFile(f)), shared.CryptHashes.Hashtype.md5);
            return b.isEqual(md5, actualmd5);
        }
    }
    private static void SaveIfNeeded(String serverdns, String outfolder, String serverpath, String basepath, Mfs.MfsEntry entry, String relpath)
    {
        boolean compressed = entry.flag==8;
        String path = basepath+relpath+entry.filename+(compressed?".gz":"");
        String source = "http://"+serverdns+path;
        String destin = outfolder+path;
        if(!HasFile(destin,entry.md5,compressed))
        {
            byte[] data = HttpUtils.geturl(source);
            FileUtils.WriteFile(destin, data, true);
        }
    }
    private static void SaveAndParseAndSaveMfs(String serverdns, String outfolder, String serverpath, String basepath)
    {
        Mfs mfs = SaveAndParseMfs(serverdns, outfolder, serverpath);
        for(Mfs.MfsEntry entry: mfs.base)
        {
            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "");
        }
        for(Mfs.MfsEntry entry: mfs.pages) //note that this one adds the dat folder by default.
        {
            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "/dat/");
        }
        for(Mfs.MfsEntry entry: mfs.other)
        {
            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "");
        }
    }
    private static byte[] TryToSaveFile(String serverdns, String outfolder, String serverpath)
    {
        try{
            byte[] result = SaveFile(serverdns, outfolder, serverpath);
            return result;
        }catch(Exception e){
            return null;
        }
    }
    private static byte[] SaveFile(String serverdns, String outfolder, String serverpath)
    {
        byte[] data = HttpUtils.geturl("http://"+serverdns+serverpath);
        if(data==null) throw new shared.uncaughtexception("Unable to read file from server: "+serverpath);
        FileUtils.WriteFile(outfolder+serverpath, data, true);
        return data;
    }
    private static Mfs SaveAndParseMfs(String serverdns, String outfolder, String serverpath)
    {
        byte[] data = SaveFile(serverdns,outfolder,serverpath);
        Mfs result = Mfs.readFromString(b.BytesToString(data));
        return result;
    }
    public static void CreateFiles(String root2, String outfolder2, boolean writeDataFiles)
    {
        Dataserver ds = new Dataserver(root2,outfolder2);
        ds.writedatafiles = writeDataFiles;
        ds.type = DataserverType.moul;
        ds.generate();
    }
    public static void CreateFilesMoul(String root2, String outfolder2, boolean writeDataFiles)
    {
        Dataserver ds = new Dataserver(root2,outfolder2);
        ds.writedatafiles = writeDataFiles;
        ds.type = DataserverType.moul;
        ds.generate();
    }
    
    public Dataserver(String root2, String outfolder2)
    {
        root = root2 + "/";
        outroot = outfolder2 + "/";

        //list all prp files
        prpfiles = new ArrayList<String>();
        File datdir = new File(root+"/dat/");
        for(File f: datdir.listFiles())
        {
            if(f.isFile())
            {
                String filename = f.getName();
                if(filename.endsWith(".prp"))
                {
                    prpfiles.add(f.getName());
                }
            }
        }

        //list all oneshotmod anims
        pages = new HashMap();
        for(String prpfile: prpfiles)
        {
            int idx = prpfile.indexOf("_District_");
            String agename = prpfile.substring(0, idx);
            String pagename = prpfile.substring(idx+"_District_".length(),prpfile.length()-".prp".length());
            /*if(pagename.startsWith("Female"))
            {
                String anim = pagename.substring("Female".length());
                String prevagename = anims.put(anim, agename);
                if(prevagename!=null)
                {
                    m.warn("There are two prps for the animation: ",anim);
                }
            }*/
            HashSet<String> ages = pages.get(pagename);
            if(ages==null)
            {
                ages = new HashSet<String>();
                pages.put(pagename, ages);
            }
            ages.add(agename);
        }
    }
    public void generate()
    {
        generateMoul();
//        if(type==DataserverType.alcugs)
//        {
//            generateAlcugs();
//            //generateMoul(); //should be fine for both.
//        }
//        else if(type==DataserverType.moul)
//        {
//            generateMoul();
//        }
//        else
//        {
//            m.throwUncaughtException("unhandled");
//        }
    }
    //private static final String[] installfiles = {"/UruSetup.exe","/UruLauncher.exe","/Uru.exe",/*"/ReleaseNotes.html"*/};
    private static final String[] installfiles = {"/UruSetup.exe","/UruLauncher.exe","/Uru.exe"/*,"/ReleaseNotes.html"*/ /*,"/serverconfig.ini"*/};
    private static final String[] agepaths = {"/dat/","/sfx/","/avi/"};
    public String GetPrefixPath(String relpath)
    {
        if(IsAgeFile(relpath)) return "/game_data/";
        if(IsInstallFile(relpath)) return "/install/Expanded/";
        return "/game_clients/drcExplorer/"; //everything else falls through to the client download
    }
    private boolean IsAgeFile(String relpath)
    {
        if(relpath.endsWith(".txt")) return false; //grr... buggy moulagain
        for(String s: agepaths) if(relpath.startsWith(s)) return true;
        return false;
    }
    private boolean IsInstallFile(String relpath)
    {
        for(String s: installfiles) if(relpath.equals(s)) return true;
        return false;
    }
    private boolean IsClientFile(String relpath)
    {
        return !IsAgeFile(relpath) && !IsInstallFile(relpath);
    }
    private void ReadFileInfo(String relpath)
    {

        String abspath = root+"/"+relpath;
        File f = new File(abspath);
        if(!f.exists())
            m.throwUncaughtException("woops!");

        if(f.isDirectory())
        {
            for(File child: f.listFiles())
            {
                ReadFileInfo(relpath+"/"+child.getName());
            }
        }
        else
        {

            //ignore these things(none of these should be present in Moul anyway:
            String[] ignorestarts = {
                "/KIimages/",
                "/log/",
                "/MyJournals/",
                "/sfx/streamingCache/",
                "/vnode_cache/", //in fact, causes some problems, because the nodes don't match up.
                "/vnodes/",
                "/init/", //in fact, causes update problems, because Uru changes them immediately.
                "/DrizzleDataserver", //DrizzleDataserverOverrides.txt,DrizzleDataserverInfo.dat
                "/originalfiles/", //Pots conversion original files
            };
            for(String i: ignorestarts) if(relpath.startsWith(i)) return;
            String[] ignoreends = {
                ".sum",
            };
            for(String i: ignoreends) if(relpath.endsWith(i)) return;
            String[] ignoreequals = {
                "/plClientSetup.cfg", //includes account info, so should be skipped or blanked.(blanking would cause people to loose their settings.)
                "/dev_mode.dat", //should we skip this? video card info, so don't download.
                //"/serverconfig.ini",
            };
            for(String i: ignoreequals) if(relpath.equals(i)) return;

            long filesize = f.length();
            long moddate = f.lastModified();
            DataserverInfo.DataserverFileInfo curinfo = oldinfo.map.get(relpath);
            if(curinfo!=null && curinfo.filesize==filesize && curinfo.lastmodified==moddate)
            {
                //re-use our old info, rather than rescanning the file.
                info.map.put(relpath, curinfo);
            }
            else
            {
                //scan the file now.
                curinfo = new DataserverInfo.DataserverFileInfo();

                //standard info
                curinfo.relpath = relpath;
                curinfo.filesize = filesize;
                curinfo.lastmodified = moddate;

                byte[] filedata = FileUtils.ReadFile(abspath);
                curinfo.md5 = shared.CryptHashes.GetMd5(filedata);
                byte[] compressedfiledata = shared.zip.compressGzip(filedata);
                curinfo.compressedfilesize = b.Int32ToInt64(compressedfiledata.length);
                curinfo.compressedmd5 = shared.CryptHashes.GetMd5(filedata);

                //save the compressed version now
                //String outfilename = outroot+GetPrefixPath(relpath)+relpath+".gz";
                //FileUtils.WriteFile(outfilename, compressedfiledata, true, true);
                //Save everything in the /game_data/ folder, even if it won't be used.
                FileUtils.WriteFile(new File(outroot+"/game_data/"+relpath+".gz"), compressedfiledata, true, true, false, true);
                //Save it in another folder if it is wanted there too:
                if(IsAgeFile(relpath))
                {
                    //nothing else to do
                }
                else if(IsInstallFile(relpath))
                {
                    FileUtils.WriteFile(new File(outroot+"/install/Expanded/"+relpath+".gz"), compressedfiledata, true, true, false, true);
                }
                else
                {
                    FileUtils.WriteFile(new File(outroot+"/game_clients/drcExplorer/"+relpath+".gz"), compressedfiledata, true, true, false, true);
                }

                if(relpath.toLowerCase().endsWith(".prp"))
                {
                    prpfile prp = prpfile.createFromFile(abspath, true);
                    for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plLayerBink))
                    {
                        plLayerBink bink = obj.castTo();
                        String binkfilename = bink.parent.filename.toString();
                        //turn it into a relpath:
                        if(binkfilename.startsWith("avi/"))
                        {
                            binkfilename = "/"+binkfilename;
                        }
                        else if(binkfilename.startsWith("/avi/"))
                        {
                            //it's good already!
                        }
                        else
                        {
                            m.throwUncaughtException("Weird bik filename: "+binkfilename);
                        }
                        curinfo.avifiles.add(binkfilename);
                    }
                    for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
                    {
                        prpobjects.plSoundBuffer sb = obj.castTo();
                        String oggfilename = sb.oggfile.toString();
                        int flags = sb.flags;
                        DataserverInfo.SfxInfo sfxinfo = new DataserverInfo.SfxInfo();
                        sfxinfo.relpath = "/sfx/"+oggfilename; //make it into a relpath.
                        sfxinfo.flags = flags;
                        curinfo.sfxfiles.put(oggfilename, sfxinfo);
                    }
                    for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plOneShotMod))
                    {
                        //do we care about plOneShotMsg?
                        prpobjects.plOneShotMod osm = obj.castTo();
                        String animname = osm.animobjectname.toString();
                        //find the prp file for this animation:
                        /*File dir = new File(root+"/dat/");
                        String prpname = null;
                        for(String prpfile: prpfiles)
                        {
                            if(prpfile.endsWith("_District_Female"+animname+".prp"))
                            {
                                if(prpname!=null)
                                {
                                    m.err("There are two prps for the animation: ",animname);
                                }
                                prpname = prpfile;
                            }
                        }
                        //register it if it isn't Global nor belongs to this Age.
                        if(prpname==null)
                        {
                            m.warn("Animation not found: ",animname);
                        }
                        else
                        {
                            if(prpname.startsWith("GlobalAnimations_District_") || prpname.startsWith(prp.header.agename.toString()+"_District_"))
                            {
                                //It's okay, it's a GlobalAnimation or belongs to this Age.
                            }
                            else
                            {
                                //The Age is doing the bad habit of using an animation from someone else's Age.
                                curinfo.oneshotmods.add(prpname);
                            }
                        }*/
                        curinfo.oneshotmods.add(animname);
                    }
                }

                info.map.put(relpath, curinfo);

                MemUtils.GarbageCollect();

            }


        }
    }
    public void generateMoul()
    {
        //Todo:
        //  pots manifests should also point to whatever.ogg.gz instead of whatever.ogg
        //  make avi files per-age too.
        //  save completed info, and use next time, just updating.

        //m.throwUncaughtException("unhandled");
        
        //by default, everything should be downloaded.
        //some Ages may have some of their things moved to other folders
        //every file gets marked as ignore, ClientSetupNew,client, a particular age, or

        m.status("Starting dataserver file creation...");

        //read previous info, if any.
        info = new DataserverInfo();
        final String dataserverfileinfo = outroot+"/DrizzleDataserverInfo.dat";
        oldinfo = (DataserverInfo)SerializationUtils.ReadFromFile(dataserverfileinfo,false);
        if(oldinfo==null)
        {
            m.status("Starting from scratch...");
            oldinfo = new DataserverInfo();
        }
        else
        {
            m.status("Updating previous info...");
        }

        //read all the new files, filling in this.info and saving compressed files.
        ReadFileInfo("");

        //So, at this point, all we have to do is create the manifest files.

        //Here's the plan:
        //  go through all infos,
        //    collect the sound flags, with a mapping oggfilename->newflags
        //    assign each info to one or more manifests

        //2 passes:
        //assign the prps, and as many oggs and avis as can be done through them. Do the sound flags during this pass.
        //assign the fnis, ages, csvs, and unclaimed oggs and avis and others.

        HashMap<String,Integer> soundflags = new HashMap(); //oggfilename->dataserverFlags
        Concurrent.HashSetMap<String,String> files = new Concurrent.HashSetMap(); //relpath->set of manifests
        //HashMap<String,HashSet<String>> manifests = new HashMap(); //manifestname->set of filenames
        //agefiles and sdlfiles just remember those files, so we can conveniently copy them later.
        ArrayList<String> agefiles = new ArrayList();
        ArrayList<String> sdlfiles = new ArrayList();

        //0th pass:
        //read overrides and assign those.
        File overridefile = new File(root+"/DrizzleDataserverOverrides.txt");
        if(overridefile.exists())
        {
            String of_contents = FileUtils.ReadFileAsString(overridefile);
            TextMapFile overrides = new TextMapFile(of_contents);
            for(TextMapFile.Item item: overrides.entries)
            {
                String relpath = item.left;
                String manifest = item.right;
                m.msg("Override: ",relpath," ",manifest);
                files.addToSet(relpath, manifest);
            }
        }

        //1st pass:
        //assign all p2f,loc,sum,age,fni,csv,prp
        for(DataserverInfo.DataserverFileInfo fileinfo: info.map.values())
        {
            String relpath = fileinfo.relpath;
            //if(relpath.startsWith("/dat/"))
            if(IsAgeFile(relpath))
            {
                if(relpath.endsWith(".p2f"))
                {
                    files.addToSet(relpath, "GUI.mfs");
                }
                else if(relpath.endsWith(".loc"))
                {
                    files.addToSet(relpath, "GUI.mfs");
                }
                //else if(relpath.endsWith(".sum"))
                //{
                    //ignore
                //}
                else if(relpath.endsWith(".age"))
                {
                    String age = relpath.substring(relpath.lastIndexOf("/")+1, relpath.length()-(".age".length()) );
                    files.addToSet(relpath, age+".mfs");
                    agefiles.add(relpath);
                }
                else if(relpath.endsWith(".fni"))
                {
                    String age = relpath.substring(relpath.lastIndexOf("/")+1, relpath.length()-(".fni".length()) );
                    files.addToSet(relpath, age+".mfs");
                }
                else if(relpath.endsWith(".csv"))
                {
                    String age = relpath.substring(relpath.lastIndexOf("/")+1, relpath.length()-(".csv".length()) );
                    files.addToSet(relpath, age+".mfs");
                }
                else if(relpath.endsWith(".prp"))
                {
                    String age = relpath.substring(relpath.lastIndexOf("/")+1, relpath.indexOf("_District_"));
                    files.addToSet(relpath, age+".mfs");

                    //register the sfx files we can and note their flags
                    for(DataserverInfo.SfxInfo sfxinfo: fileinfo.sfxfiles.values())
                    {
                        //note the flags
                        Integer dsflags = soundflags.get(sfxinfo.relpath);
                        if(dsflags==null) dsflags = 0;
                        //1)If flag 0x4 or 0x8 is used, decompress to two wav files
                        //2)if flag 0x10 is used, it shouldn't be decompressed at all
                        //3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
                        //if situation 1 and 2 occur, warn
                        //if situation 1 and 3 occur, do both!
                        boolean case1 = ((sfxinfo.flags&0x4)!=0)||(((sfxinfo.flags&0x8)!=0));
                        boolean case2 = ((sfxinfo.flags&0x10)!=0);
                        boolean case3 = !case1 && !case2;
                        if(case1) dsflags |= 1;
                        if(case2) dsflags |= 2;
                        if(case3) dsflags |= 4;
                        soundflags.put(sfxinfo.relpath, dsflags);

                        //register
                        //String sfxrelpath = "/sfx/"+sfxinfo.filename;
                        files.addToSet(sfxinfo.relpath, age+".mfs");
                    }

                    //register the bik files we can
                    for(String bikfile: fileinfo.avifiles)
                    {
                        files.addToSet(bikfile, age+".mfs");
                    }

                    //register the OneShotMod animations we can
                    for(String anim: fileinfo.oneshotmods)
                    {
                        HashSet<String> ages = pages.get("Female"+anim);
                        if(ages==null)
                        {
                            m.warn("Animation not found: ",anim);
                        }
                        else
                        {
                            if(ages.contains("GlobalAnimations") || ages.contains(age))
                            {
                                //do nothing, we're good!
                            }
                            else
                            {
                                if(ages.size()>1)
                                {
                                    m.err("Age uses animation from another Age, but there is more than 1 possibility!");
                                }
                                for(String curage: ages)
                                {
                                    String prp1 = "/dat/"+curage+"_District_Female"+anim+".prp";
                                    String prp2 = "/dat/"+curage+"_District_Male"+anim+".prp";
                                    files.addToSet(prp1, age+".mfs");
                                    files.addToSet(prp2, age+".mfs");
                                    m.warn("Age uses animation from another Age: ",age," uses animation from ",curage);
                                }
                            }
                        }
                        //if(anim==null)
                        //{
                        //    m.warn("Animation not found: ",anim);
                        //}
                        //else
                        //{
                        //    String animAge = anims.get(anim);
                        //    if(animAge.equals("GlobalAnimations") || animAge.equals(age))
                        //    {
                        //        //do nothing
                        //    }
                        //    else
                        //    {
                        //        String prp1 = animAge+"_District_Female"+anim+".prp";
                        //        String prp2 = animAge+"_District_Male"+anim+".prp";
                        //        files.addToSet(prp1, age+".mfs");
                        //        files.addToSet(prp2, age+".mfs");
                        //        m.warn("Age uses animation from another Age: ",age," uses animation from ",animAge);
                        //    }
                        //}
                    }

                }
                else if(relpath.endsWith(".ogg"))
                {
                    //handle in 2nd pass
                }
                else if(relpath.endsWith(".bik"))
                {
                    //handle in 2nd pass
                }
                else
                {
                    //a misc file
                    files.addToSet(relpath, "GUI.mfs");
                }
            }
            else if(IsInstallFile(relpath))
            {
                //main downloader manifest
                files.addToSet(relpath, "ClientSetupNew.mfs");
            }
            else
            {
                //rest of the client manifest
                if(relpath.endsWith(".sdl"))
                {
                    files.addToSet(relpath, "client.mfs");
                    sdlfiles.add(relpath);
                }
                else
                {
                    files.addToSet(relpath, "client.mfs");
                }
            }
        }

        //2nd pass:
        //Assign unassigned files (i.e. ogg and bik files)
        for(DataserverInfo.DataserverFileInfo fileinfo: info.map.values())
        {
            String relpath = fileinfo.relpath;
            java.util.Set<String> manifestsBelongedTo = files.get(relpath);
            if(manifestsBelongedTo==null)
            {
                //unassigned, so assign it
                files.addToSet(relpath, "GUI.mfs");
            }
        }


        //add .age files for crowthistle/myst5/hexisle/mqo if present
        final boolean forceConvertedAges = false;
        if(forceConvertedAges)
        {
            String[] forced = {
                "/dat/DescentMystV.age","/dat/Direbo.age","/dat/KveerMystV.age","/dat/Laki.age","/dat/MystMystV.age","/dat/Siralehn.age","/dat/Tahgira.age","/dat/Todelmer.age",
                "/dat/MarshScene.age","/dat/MountainScene.age",
                "/dat/CatfishCanyon.age","/dat/DessertDesert.age","/dat/LouderSpace.age","/dat/MoldyDungeon.age","/dat/PlasmaMiasma.age","/dat/PumpkinJungle.age",
                "/dat/PortalWell.age","/dat/ForestMQ.age","/dat/Courtyard.age",
            };
            for(String relpath: forced)
            {
                if(files.get(relpath)!=null)
                {
                    files.addToSet(relpath, "GUI.mfs");
                }
            }
        }


        //organize by manifest
        Concurrent.HashSetMap<String,String> manifests = new Concurrent.HashSetMap(); //manifestname->set of filenames
        for(String relpath: files.keySet())
        {
            for(String manifest: files.get(relpath))
            {
                manifests.addToSet(manifest, relpath);
            }
        }

        



        //create the mfs files.
        if(writemanifestfiles)
        {
            for(String mfs: manifests.keySet())
            {
                m.msg("Doing manifest file: ",mfs);

                //get path info for this manifest type:
                String outrelpath;
                //String outfolder;
                String outmfs;
                if(mfs.equals("ClientSetupNew.mfs"))
                {
                    outrelpath = "/install/Expanded/";
                    //outfolder = outroot+"/"+outrelpath;
                    outmfs = outroot+outrelpath;
                }
                else if(mfs.equals("client.mfs"))
                {
                    outrelpath = "/game_clients/drcExplorer/";
                    //outfolder = outroot+"/"+outrelpath;
                    outmfs = outroot+outrelpath;
                }
                else
                {
                    outrelpath = "/game_data/";
                    //outfolder = outroot+"/"+outrelpath;
                    outmfs = outroot+outrelpath+"dat/";
                }

                
                mfsfile mfsf = new mfsfile(); //Alcugs manifest
                Manifest moulmanifest = new Manifest(); //Moul manifest
                for(String relpath: manifests.get(mfs))
                {
                    DataserverInfo.DataserverFileInfo dfi = info.map.get(relpath);
                    if(dfi==null)
                    {
                        m.msg("Missing file: ",relpath);
                        continue;
                    }

                    //get entry text
                    StringBuilder entry = new StringBuilder();

                        //write filename:
                        String label;
                        if(dfi.relpath.endsWith(".prp"))
                        {
                            label = dfi.relpath.substring(dfi.relpath.lastIndexOf("/")+1); //skip the folder name, e.g. "hi_District_builtin.prp"
                        }
                        else
                        {
                            label = dfi.relpath.substring(1); //skip the '/', e.g. "sfx/hi.ogg"
                        }
                        entry.append(label);
                        entry.append(",");

                        //write filesize:
                        entry.append(Long.toString(dfi.filesize));
                        entry.append(",");

                        //write date:
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
                        Date d = new Date(dfi.lastmodified);
                        String dresult = format.format(d);
                        entry.append(dresult);
                        entry.append(",");

                        //write md5:
                        String md5str = b.BytesToHexString(dfi.md5);
                        md5str = md5str.toLowerCase();
                        entry.append(md5str);
                        entry.append(",");

                        //write flag:
                        Integer flag = soundflags.get(dfi.relpath); //only ogg files will have this set
                        if(flag==null) flag = 0;
                        flag |= 8; //every file is compressed.
                        entry.append(Integer.toString(flag));
                        if((flag&0x8)!=0) //this should always be true, since we just set it :P
                        {
                            entry.append(",");
                            entry.append(Long.toString(dfi.compressedfilesize));
                        }


                    //add to the correct section
                    if(dfi.relpath.endsWith(".age")) mfsf.base.add(entry.toString());
                    else if(dfi.relpath.endsWith(".fni")) mfsf.base.add(entry.toString());
                    else if(dfi.relpath.endsWith(".csv")) mfsf.base.add(entry.toString());
                    else if(dfi.relpath.endsWith(".p2f")) mfsf.base.add(entry.toString());
                    else if(dfi.relpath.endsWith(".prp")) mfsf.pages.add(entry.toString());
                    else mfsf.other.add(entry.toString());



                    //moul writing.
                    MoulFileInfo mi = new MoulFileInfo();
                    //mi.filename = new Str(dfi.relpath.replace("/", "\\")); //should we skip the first slash?
                    //mi.Downloadname = new Str((outrelpath+dfi.relpath+".gz").replace("/", "\\"));
                    mi.SetFilename(dfi.relpath);
                    mi.SetDownloadname(outrelpath+dfi.relpath+".gz");
                    //mi.CleanNames(); //Get rid of starting \ and double \\
                    mi.Hash = new Str(b.BytesToHexString(dfi.md5));
                    mi.CompressedHash = new Str(b.BytesToHexString(dfi.compressedmd5));
                    mi.Filesize = (int)dfi.filesize;
                    mi.Compressedsize = (int)dfi.compressedfilesize;
                    Integer sndflag = soundflags.get(dfi.relpath); //only ogg files will have this set
                    if(sndflag==null) sndflag = 0;
                    mi.Flags = sndflag;
                    moulmanifest.getFiles().add(mi);
                }


                //hack to move UruExplorer.exe to the top (not needed for Moul, I believe.)
                if(mfs.equals("client.mfs"))
                {
                    for(int i=0;i<mfsf.other.size();i++)
                    {
                        String curs = mfsf.other.get(i);
                        if(curs.startsWith("UruExplorer.exe,"))
                        {
                            mfsf.other.set(i, mfsf.other.get(0));
                            mfsf.other.set(0, curs);
                        }
                    }
                }

                //Create the Alcugs version of the manifest:
                //FileUtils.WriteFile(outmfs+"/"+mfs, mfsf.getBytes(),true,true);
                FileUtils.SaveFileIfChanged(outmfs+"/"+mfs, mfsf.getBytes(),true,true);

                //Create the Moul version of the manifest:
                //FileUtils.WriteFile(outmfs+"/"+mfs+"_moul", moulmanifest.compileAlone(shared.Format.moul) ,true,true);
                FileUtils.SaveFileIfChanged(outmfs+"/"+mfs+"_moul", moulmanifest.compileAlone(shared.Format.moul) ,true,true);

            }
        }


        //write a few extra files (Do we want this in Moul?):
        //String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nNeighborhood\nNexus\nPersonal\nCustomAvatars\nGlobalAvatars\nAvatarCustomization\n";
        String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nCustomAvatars\nGlobalAvatars\n";
        //read as GlobalAnimations, GlobalAvatars,GlobalClothing,GUI,CustomAvatars
        //FileUtils.WriteFile(outroot+"/game_data/dat/agelist.txt", b.StringToBytes(agelist_txt),true);
        FileUtils.SaveFileIfChanged(outroot+"/game_data/dat/agelist.txt", b.StringToBytes(agelist_txt),true,true);


        //copy uncompressed .age and .sdl files for convenience (they can be encrypted; Talcum and Alcugs are okay with that.)
        for(String relpath: agefiles)
        {
            String filename = relpath.substring(relpath.lastIndexOf("/")+1);
            if(relpath.toLowerCase().equals("/dat/"+filename.toLowerCase())) //don't do dummy .age files
            {
                String infile = root + "/" + relpath;
                String outfile = outroot + "/agefiles/"+filename;
                byte[] infiledata = FileUtils.ReadFile(infile);
                FileUtils.SaveFileIfChanged(outfile, infiledata, true, true);
            }
        }
        for(String relpath: sdlfiles)
        {
            String filename = relpath.substring(relpath.lastIndexOf("/")+1);
            if(relpath.toLowerCase().equals("/sdl/"+filename.toLowerCase())) //don't do dummy .sdl files.  Not that they exist :P
            {
                String infile = root + "/" + relpath;
                String outfile = outroot + "/sdlfiles/"+filename;
                byte[] infiledata = FileUtils.ReadFile(infile);
                FileUtils.SaveFileIfChanged(outfile, infiledata, true, true);
            }
        }

        //save the current info
        //SerializationUtils.SaveToFile(dataserverfileinfo, info);
        //but this file is always changing, probably because of the unpredictable nature of the HashMaps.
        byte[] filebs = SerializationUtils.SaveToBytes(info);
        FileUtils.SaveFileIfChanged(dataserverfileinfo, filebs, true, true);

        m.status("Done generating the dataserver!");
    }
    public static class mfsfile
    {
        int format;
        Vector<String> base = new Vector();
        Vector<String> pages = new Vector();
        Vector<String> other = new Vector();

        public mfsfile(){}

        public String toString()
        {
            StringBuilder result = new StringBuilder();
            result.append("[version]\nformat=5\n\n");

            result.append("[base]\n");
            for(String s: base)
            {
                result.append(s);
                result.append("\n");
            }
            result.append("\n");

            result.append("[pages]\n");
            for(String s: pages)
            {
                result.append(s);
                result.append("\n");
            }
            result.append("\n");

            result.append("[other]\n");
            for(String s: other)
            {
                result.append(s);
                result.append("\n");
            }
            //result.append("\n");

            return result.toString();
        }
        public byte[] getBytes()
        {
            return b.StringToBytes(this.toString());
        }
    }

    /*public class file
    {
        byte[] data;
        int compress;
        long filesize;
        long modtime;
        byte[] md5;

        String path;
        String filename;
        String fullpath;

        public file(String path2, String filename2)
        {
            path = path2;
            filename = filename2;
            fullpath = root +"/"+ path + "/" + filename;

            if(filename.endsWith(".exe") || filename.endsWith(".prp"))
            {
                compress = 8;
            }
            else if(filename.endsWith(".ogg"))
            {
                compress = 2;
            }
            else
            {
                compress = 0;
            }

            //String fullpath = root+"/"+relFilename;
            filesize = FileUtils.GetFilesize(fullpath);
            modtime = FileUtils.GetModtime(fullpath);
            if(compress==8)
            {
                data = zip.compressGzip(FileUtils.ReadFile(fullpath));
            }
            else
            {
                data = FileUtils.ReadFile(fullpath);
            }
            md5 = shared.CryptHashes.GetMd5(data);
        }
        public String toString()
        {
            StringBuilder s = new StringBuilder();

            //write filename:
            if(!path.equals("")) s.append(path+"/");
            s.append(filename);
            s.append(",");

            //write filesize:
            s.append(Long.toString(filesize));
            s.append(",");

            //write date:
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date d = new Date(modtime);
            String dresult = format.format(d);
            s.append(dresult);
            s.append(",");

            //write md5:
            String md5str = b.BytesToHexString(md5);
            md5str = md5str.toLowerCase();
            s.append(md5str);
            s.append(",");

            //write flag:
            if(compress==8)
            {
                s.append("8,");
                int compsize = data.length;
                s.append(Integer.toString(compsize));
            }
            else if(compress==2)
            {
                s.append("2");
            }
            else
            {
                s.append("0");
            }

            String result = s.toString();
            return result;
        }
    }*/
}

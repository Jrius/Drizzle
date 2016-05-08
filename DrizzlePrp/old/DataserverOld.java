///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package uru.server;
//urus
//import shared.m;
//import shared.FileUtils;
//import java.io.File;
//import shared.zip;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import shared.b;
//import java.util.Vector;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashSet;
//import java.util.Map.Entry;
//import java.util.ArrayDeque;
//import shared.HttpUtils;
//import shared.*;
//
////TODO:
////The client.mfs needs UruExplorer.exe as the top item, and those two items from base have to go to others or be removed. (otherwise UruExplorer.exe won't be autostarted from UruSetup.)
//
////Sound files:
////scan through prps and check soundbuffer objects flag.
////1)If flag 0x4 or 0x8 is used, decompress to two wav files
////2)if flag 0x10 is used, it shouldn't be decompressed at all
////3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
////if situation 1 and 2 occur, warn
////if situation 1 and 3 occur, do both!
//
//public class Dataserver
//{
//    boolean writedatafiles = false;
//    boolean writemanifestfiles = true;
//
//    enum DataserverType
//    {
//        alcugs,
//        moul,
//    }
//    DataserverType type;
//
//    String root;
//    String outroot;
//    fileusers usage = new fileusers();
//
//    public static class DataserverInfo extends SavableObjectBase
//    {
//        public DataserverInfo()
//        {
//        }
//    }
//
//    public static void MirrorServer(String serverdns, String outfolder, boolean includeGeneralFiles, boolean includePotsAges, boolean includeAgelistAges, boolean includeAvailableLinksAges, boolean includeCustomAges, String customAges)
//    {
//        LinkedHashSet<String> manifests = new LinkedHashSet();
//        if(includeGeneralFiles)
//        {
//            SaveFile(serverdns,outfolder,"/status/lobbylist.txt");
//            TryToSaveFile(serverdns,outfolder,"/status/index_eng.html");
//            TryToSaveFile(serverdns,outfolder,"/status/index_fra.html");
//            TryToSaveFile(serverdns,outfolder,"/status/index_ger.html");
//            SaveAndParseAndSaveMfs(serverdns,outfolder,"/install/Expanded/ClientSetupNew.mfs","/install/Expanded/");
//            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_clients/drcExplorer/client.mfs","/game_clients/drcExplorer/");
//        }
//        if(includePotsAges)
//        {
//            for(String age: auto.ageLists.potsages)
//            {
//                if(!manifests.contains(age))
//                {
//                    try{
//                        SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
//                        manifests.add(age);
//                    }catch(Exception e){
//                        m.warn("Unable to download all of Age: ",age);
//                    }
//                }
//            }
//        }
//        if(includeAgelistAges)
//        {
//            byte[] data = SaveFile(serverdns,outfolder,"/game_data/dat/agelist.txt");
//            String data2 = b.BytesToString(data);
//            for(String age: data2.split("\n"))
//            {
//                age = age.trim();
//                if(!age.equals(""))
//                {
//                    if(!manifests.contains(age))
//                    {
//                        try{
//                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
//                            manifests.add(age);
//                        }catch(Exception e){
//                            m.warn("Unable to download all of Age: ",age);
//                        }
//                    }
//                }
//            }
//        }
//        if(includeAvailableLinksAges)
//        {
//            //actually may have already downloaded this, so let's just read it instead of
//            byte[] data = HttpUtils.geturl("http://"+serverdns+"/game_clients/drcExplorer/AvailableLinks.inf");
//            String data2 = b.BytesToString(data);
//            for(String line: data2.split("\n"))
//            {
//                line = line.trim();
//                String age = null;
//                if(line.startsWith("restorationlink:")) age = line.substring("restorationlink:".length(), line.indexOf(","));
//                else if(line.startsWith("publiclink:")) age = line.substring("publiclink:".length(), line.indexOf(","));
//                else if(line.startsWith("link:")) age = line.substring("link:".length(), line.indexOf(","));
//                if(age!=null)
//                {
//                    if(!manifests.contains(age))
//                    {
//                        try{
//                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
//                            manifests.add(age);
//                        }catch(Exception e){
//                            m.warn("Unable to download all of Age: ",age);
//                        }
//                    }
//                }
//            }
//
//        }
//        if(includeCustomAges)
//        {
//            for(String line: customAges.split("\n"))
//            {
//                line = line.trim();
//                if(!line.startsWith("#") && !line.equals(""))
//                {
//                    String age = line;
//                    if(!manifests.contains(age))
//                    {
//                        try{
//                            SaveAndParseAndSaveMfs(serverdns,outfolder,"/game_data/dat/"+age+".mfs","/game_data/");
//                            manifests.add(age);
//                        }catch(Exception e){
//                            m.warn("Unable to download all of Age: ",age);
//                        }
//                    }
//                }
//            }
//        }
//        m.status("Done mirroring the dataserver!");
//    }
//
//    private static boolean HasFile(String filename, byte[] md5, boolean md5OnDecompressed)
//    {
//        File f = new File(filename);
//        if(f.isDirectory()) throw new shared.uncaughtexception("A folder exists which should be a file: "+filename);
//        if(!f.exists()) return false;
//        if(!md5OnDecompressed)
//        {
//            byte[] actualmd5 = shared.CryptHashes.GetHash(filename, shared.CryptHashes.Hashtype.md5);
//            return b.isEqual(md5, actualmd5);
//        }
//        else
//        {
//            byte[] actualmd5 = shared.CryptHashes.GetHash(shared.zip.decompressGzip(FileUtils.ReadFile(f)), shared.CryptHashes.Hashtype.md5);
//            return b.isEqual(md5, actualmd5);
//        }
//    }
//    private static void SaveIfNeeded(String serverdns, String outfolder, String serverpath, String basepath, Mfs.MfsEntry entry, String relpath)
//    {
//        boolean compressed = entry.flag==8;
//        String path = basepath+relpath+entry.filename+(compressed?".gz":"");
//        String source = "http://"+serverdns+path;
//        String destin = outfolder+path;
//        if(!HasFile(destin,entry.md5,compressed))
//        {
//            byte[] data = HttpUtils.geturl(source);
//            FileUtils.WriteFile(destin, data, true);
//        }
//    }
//    private static void SaveAndParseAndSaveMfs(String serverdns, String outfolder, String serverpath, String basepath)
//    {
//        Mfs mfs = SaveAndParseMfs(serverdns, outfolder, serverpath);
//        for(Mfs.MfsEntry entry: mfs.base)
//        {
//            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "");
//        }
//        for(Mfs.MfsEntry entry: mfs.pages) //note that this one adds the dat folder by default.
//        {
//            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "/dat/");
//        }
//        for(Mfs.MfsEntry entry: mfs.other)
//        {
//            SaveIfNeeded(serverdns, outfolder, serverpath, basepath, entry, "");
//        }
//    }
//    private static byte[] TryToSaveFile(String serverdns, String outfolder, String serverpath)
//    {
//        try{
//            byte[] result = SaveFile(serverdns, outfolder, serverpath);
//            return result;
//        }catch(Exception e){
//            return null;
//        }
//    }
//    private static byte[] SaveFile(String serverdns, String outfolder, String serverpath)
//    {
//        byte[] data = HttpUtils.geturl("http://"+serverdns+serverpath);
//        if(data==null) throw new shared.uncaughtexception("Unable to read file from server: "+serverpath);
//        FileUtils.WriteFile(outfolder+serverpath, data, true);
//        return data;
//    }
//    private static Mfs SaveAndParseMfs(String serverdns, String outfolder, String serverpath)
//    {
//        byte[] data = SaveFile(serverdns,outfolder,serverpath);
//        Mfs result = Mfs.readFromString(b.BytesToString(data));
//        return result;
//    }
//    public static void CreateFiles(String root2, String outfolder2, boolean writeDataFiles)
//    {
//        Dataserver ds = new Dataserver(root2,outfolder2);
//        ds.writedatafiles = writeDataFiles;
//        ds.type = DataserverType.moul;
//        ds.generate();
//    }
//    public static void CreateFilesMoul(String root2, String outfolder2, boolean writeDataFiles)
//    {
//        Dataserver ds = new Dataserver(root2,outfolder2);
//        ds.writedatafiles = writeDataFiles;
//        ds.type = DataserverType.moul;
//        ds.generate();
//    }
//
//    public Dataserver(String root2, String outfolder2)
//    {
//        root = root2 + "/";
//        outroot = outfolder2 + "/";
//    }
//    void readfileinfo(String root, String path, String file)
//    {
//        String relpath = (path.equals(""))?file:(path+"/"+file);//path+"/"+file;
//        String abspath = root+"/"+relpath;
//        File f = new File(abspath);
//        if(f.isDirectory())
//        {
//            for(String child: f.list())
//            {
//                //String newpath = (path.equals(""))?file:(path+"/"+file);
//                readfileinfo(root,relpath,child);
//            }
//        }
//        else
//        {
//
//            //ignore these things(none of these should be present in Moul anyway:
//            String[] ignores = {
//                "KIimages/",
//                "log/",
//                "MyJournals/",
//                "sfx/streamingCache/",
//                "vnode_cache/", //in fact, causes some problems, because the nodes don't match up.
//                "vnodes/",
//                "init/", //in fact, causes update problems, because Uru changes them immediately.
//                "plClientSetup.cfg", //includes account info, so should be skipped or blanked.(blanking would cause people to loose their settings.)
//                "dev_mode.dat", //should we skip this? video card info, so don't download.
//            };
//            for(String i: ignores)
//            {
//                if(relpath.startsWith(i))
//                {
//                    return;
//                }
//            }
//
//            //urusetup gets its own file (and UruLauncher is from Moul, so Vista/Win7 don't complain):
//            if(relpath.equals("UruSetup.exe")||relpath.equals("UruLauncher.exe"))
//            {
//                usage.add(relpath, "ClientSetupNew.mfs");
//                return;
//            }
//
//            //always include these ones in client.mfs
//            String[] always = {
//                "avi/",
//                "img/",
//                "lib/",
//                "MyMusic/",
//                "Python/",
//                "Registration/",
//                "SDL/",
//                //"sfx/",
//                "Xtras/",
//                "extras/", //moul
//                "uninstall", //moul
//            };
//            for(String s: always)
//            {
//                if(relpath.startsWith(s))
//                {
//                    usage.add(relpath, "client.mfs");
//                    return;
//                }
//            }
//            if(!relpath.contains("/")) //include any root files.
//            {
//                usage.add(relpath, "client.mfs");
//                return;
//            }
//
//            if(path.equals("sfx"))
//            {
//                //usage.add(relpath,"GUI.mfs");
//                usage.add(relpath,"client.mfs");
//                return;
//            }
//
//            if(path.equals("dat"))
//            {
//                if(file.endsWith(".p2f"))
//                {
//                    usage.add(relpath, "GUI.mfs");
//                    return;
//                }
//
//                if(file.endsWith(".loc"))
//                {
//                    usage.add(relpath, "GUI.mfs");
//                    return;
//                }
//
//                if(file.endsWith(".sum"))
//                {
//                    return; //ignore
//                }
//
//                if(file.endsWith(".age"))
//                {
//                    String age = file.substring(0,file.length()-4);
//                    usage.add(relpath, age+".mfs");
//                    return;
//                }
//
//                if(file.endsWith(".fni"))
//                {
//                    String age = file.substring(0,file.length()-4);
//                    usage.add(relpath, age+".mfs");
//                    return;
//                }
//
//                if(file.endsWith(".csv"))
//                {
//                    String age = file.substring(0,file.length()-4);
//                    usage.add(relpath, age+".mfs");
//                    return;
//                }
//
//                if(file.endsWith(".prp"))
//                {
//                    String age = file.substring(0,file.indexOf("_District_"));
//                    usage.add(relpath, age+".mfs");
//
//                    //check soundbuffers for ogg file usage...
//                    /*uru.moulprp.prpfile prp = uru.moulprp.prpfile.createFromFile(abspath, true);
//                    for(uru.moulprp.PrpRootObject ro: prp.FindAllObjectsOfType(uru.moulprp.Typeid.plSoundBuffer))
//                    {
//                        uru.moulprp.x0029SoundBuffer sb = ro.castTo();
//                        String ogg = "sfx/"+sb.oggfile;
//                        //1)If flag 0x4 or 0x8 is used, decompress to two wav files
//                        //2)if flag 0x10 is used, it shouldn't be decompressed at all
//                        //3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
//                        //if situation 1 and 2 occur, warn
//                        //if situation 1 and 3 occur, do both!
//                        boolean case1 = ((sb.flags&0x4)!=0)||(((sb.flags&0x8)!=0));
//                        boolean case2 = ((sb.flags&0x10)!=0);
//                        boolean case3 = !case1;
//                        //if(case1) usage.get(ogg).soundusecases.add(1);
//                        //if(case2) usage.get(ogg).soundusecases.add(2);
//                        //if(case3) usage.get(ogg).soundusecases.add(4);
//                        if(case1) usage.addButNotFound(ogg).sounduse |= 1;
//                        if(case2) usage.addButNotFound(ogg).sounduse |= 2;
//                        if(case3) usage.addButNotFound(ogg).sounduse |= 4;
//                    }*/
//
//                    return;
//                }
//            }
//
//            m.warn("Unhandled file: ",abspath);
//
//        }
//    }
//    public void generate()
//    {
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
//    }
//    public void generateMoul()
//    {
//        //Todo:
//        //  pots manifests should also point to whatever.ogg.gz instead of whatever.ogg
//        //  make avi files per-age too.
//        //  save completed info, and use next time, just updating.
//
//        //m.throwUncaughtException("unhandled");
//
//        //by default, everything should be downloaded.
//        //some Ages may have some of their things moved to other folders
//        //every file gets marked as ignore, ClientSetupNew,client, a particular age, or
//
//        m.status("Starting dataserver file creation...");
//
//        //read previous info, if any.
//        DataserverInfo info = (DataserverInfo)DataserverInfo.ReadFromFile(outroot+"/DrizzleDataserverInfo.dat");
//        if(info==null) info = new DataserverInfo();
//
//        //categorise which mfs files each file goes in.
//        readfileinfo(root, "", "");
//
//        //find ogg flags
//        fileusage[] fuses = usage.map.values().toArray(new fileusage[]{});
//        for(fileusage fus: fuses)
//        {
//            if(fus.name.endsWith(".prp"))
//            {
//                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(root+fus.name, true);
//                String age = fus.name.substring(0,fus.name.indexOf("_District_"));
//                age = age.substring(age.lastIndexOf("/")+1);
//                for(prpobjects.PrpRootObject ro: prp.FindAllObjectsOfType(prpobjects.Typeid.plSoundBuffer))
//                {
//                    prpobjects.x0029SoundBuffer sb = ro.castTo();
//                    String ogg = "sfx/"+sb.oggfile;
//                    if(usage.has(ogg)) //we don't want to add any ogg files we don't actually have.
//                    {
//                        usage.add(ogg, age+".mfs");
//                        usage.remove(ogg, "client.mfs"); //we've got a home, remove it from the client.mfs
//                    }
//                    //1)If flag 0x4 or 0x8 is used, decompress to two wav files
//                    //2)if flag 0x10 is used, it shouldn't be decompressed at all
//                    //3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
//                    //if situation 1 and 2 occur, warn
//                    //if situation 1 and 3 occur, do both!
//                    boolean case1 = ((sb.flags&0x4)!=0)||(((sb.flags&0x8)!=0));
//                    boolean case2 = ((sb.flags&0x10)!=0);
//                    boolean case3 = !case1 && !case2;
//                    //if(case1) usage.get(ogg).soundusecases.add(1);
//                    //if(case2) usage.get(ogg).soundusecases.add(2);
//                    //if(case3) usage.get(ogg).soundusecases.add(4);
//                    fileusage ogf = usage.map.get(ogg);
//                    if(ogf==null)
//                    {
//                        m.warn("ogg file not found.");
//                    }
//                    else
//                    {
//                        fileusage fus2 = usage.addButNotFound(ogg);
//                        if(case1) fus2.sounduse |= 1;
//                        if(case2) fus2.sounduse |= 2;
//                        if(case3) fus2.sounduse |= 4;
//                    }
//                }
//            }
//        }
//
//        //move ogg files from client.mfs to GUI.mfs (includes MyMusic, but that should be okay.)
//        for(fileusage fus: usage.getFilesUsedByUser("client.mfs"))
//        {
//            if(fus.name.startsWith("sfx/") && fus.name.endsWith(".ogg"))
//            {
//                //usage.move(fus,"client.mfs","GUI.mfs");
//                fus.users.add("GUI.mfs");
//                fus.users.remove("client.mfs");
//            }
//        }
//
//
//        //add .age files for crowthistle/myst5
//        String[] forced = {
//            "dat/DescentMystV.age","dat/Direbo.age","dat/KveerMystV.age","dat/Laki.age","dat/MystMystV.age","dat/Siralehn.age","dat/Tahgira.age","dat/Todelmer.age",
//            "dat/MarshScene.age","dat/MountainScene.age",
//        };
//        for(String name: forced)
//        {
//            if(usage.has(name)) usage.add(name,"GUI.mfs");
//        }
//
//        //move GlobalMarkers into client.
//
//        //create the mfs files.
//        if(writemanifestfiles)
//        {
//            for(String mfs: usage.getAllUsers())
//            {
//                m.msg("Doing manifest file: ",mfs);
//                String outrelpath;
//                String outfolder;
//                String outmfs;
//                if(mfs.equals("ClientSetupNew.mfs"))
//                {
//                    outrelpath = "install/Expanded/";
//                    outfolder = outroot+"/"+outrelpath;
//                    outmfs = outroot+"/"+outrelpath;
//                }
//                else if(mfs.equals("client.mfs"))
//                {
//                    outrelpath = "game_clients/drcExplorer/";
//                    outfolder = outroot+"/"+outrelpath;
//                    outmfs = outroot+"/"+outrelpath;
//                }
//                else
//                {
//                    outrelpath = "game_data/";
//                    outfolder = outroot+"/"+outrelpath;
//                    outmfs = outroot+"/"+outrelpath+"dat/";
//                }
//                mfsfile mfsf = new mfsfile();
//                Vector<fileusage> fs = usage.getFilesUsedByUser(mfs);
//                /*if(false) //move GlobalMarkers.mfs items into client.mfs since client doesn't try to load them automatically.
//                {
//                    if(mfs.equals("GUI.mfs"))
//                    {
//                        for(fileusage f: usage.getFilesUsedByUser("GlobalMarkers.mfs"))
//                        {
//                            fs.add(f);
//                        }
//                    }
//                    else if(mfs.equals("GlobalMarkers.mfs"))
//                    {
//                        //continue; //don't create GlobalMarkers.mfs
//                    }
//                }*/
//
//                //uru.Bytedeque moulmanifest = new uru.Bytedeque();
//                Manifest moulmanifest = new Manifest();
//
//                for(fileusage f: fs)
//                {
//                    f.readinfo();
//                    String entry = f.toString();
//                    if(f.name.endsWith(".age")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".fni")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".csv")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".p2f")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".prp")) mfsf.pages.add(entry);
//                    else mfsf.other.add(entry);
//
//                    String outname;
//                    if(type==DataserverType.alcugs && f.compress!=8) outname = f.name;
//                    else outname = f.name+".gz";
//
//                    if(writedatafiles) FileUtils.WriteFile(outfolder+"/"+outname, f.data,true);
//                    f.data = null; //otherwise we leak memory.
//
//                    //moul writing.
//                    MoulFileInfo mi = new MoulFileInfo();
//                    mi.filename = new Str(f.name.replace("/", "\\"));
//                    mi.Downloadname = new Str((outrelpath+outname).replace("/", "\\"));
//                    mi.Hash = new Str(b.BytesToHexString(f.md5));
//                    mi.CompressedHash = new Str(b.BytesToHexString(f.compressedmd5));
//                    mi.Filesize = (int)f.filesize;
//                    mi.Compressedsize = (int)f.compressedfilesize;
//                    mi.Flags = (f.compress==8)?0:f.compress; //everything is compressed in Moul.  The value 8 therefore is no longer used.
//                    //mi.write(moulmanifest);
//                    moulmanifest.getFiles().add(mi);
//
//                }
//
//                //hack to move UruExplorer.exe to the top (not needed for Moul, I believe.)
//                if(mfs.equals("client.mfs"))
//                {
//                    for(int i=0;i<mfsf.other.size();i++)
//                    {
//                        String curs = mfsf.other.get(i);
//                        if(curs.startsWith("UruExplorer.exe,"))
//                        {
//                            mfsf.other.set(i, mfsf.other.get(0));
//                            mfsf.other.set(0, curs);
//                        }
//                    }
//                }
//
//                FileUtils.WriteFile(outmfs+"/"+mfs, mfsf.getBytes(),true);
//
//                //Create the Moul version of the manifest:
//                FileUtils.WriteFile(outmfs+"/"+mfs+"_moul", moulmanifest.compileAlone(shared.Format.moul) ,true);
//
//            }
//        }
//
//
//        //write a few extra files (Do we want this in Moul?):
//        //String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nNeighborhood\nNexus\nPersonal\nCustomAvatars\nGlobalAvatars\nAvatarCustomization\n";
//        String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nCustomAvatars\nGlobalAvatars\n";
//        //read as GlobalAnimations, GlobalAvatars,GlobalClothing,GUI,CustomAvatars
//        FileUtils.WriteFile(outroot+"/game_data/dat/agelist.txt", b.StringToBytes(agelist_txt),true);
//
//        /*if(true)return;
//
//        FileUtils.CreateFolder(outfolder+"install/Expanded/");
//        //StringBuilder s = new StringBuilder();
//        //s.append("[version]\nformat=5\n\n[base]\n\n[pages]\n\n[other]\n");
//        mfsfile mfs = new mfsfile();
//        file f = new file("","UruSetup.exe");
//        mfs.other.add(f.toString());
//        //s.append(f.getEntry());
//        //s.append("\n");
//        FileUtils.WriteFile(outfolder+"install/Expanded/ClientSetupNew.mfs", mfs.getBytes());
//        FileUtils.WriteFile(outfolder+"install/Expanded/UruSetup.exe.gz", f.data);
//
//        String[] clientfiles = {
//            "UruExplorer.exe",
//            "SoundDecompress.exe"
//        };
//
//        FileUtils.CreateFolder(outfolder+"game_clients/drcExplorer/");
//        mfs = new mfsfile();
//        f = new file("","UruExplorer.exe");*/
//
//        m.status("Done generating the dataserver!");
//    }
//    public void generateAlcugs()
//    {
//        //by default, everything should be downloaded.
//        //some Ages may have some of their things moved to other folders
//        //every file gets marked as ignore, ClientSetupNew,client, a particular age, or
//
//        m.status("Starting dataserver file creation...");
//
//        //categorise which mfs files each file goes in.
//        readfileinfo(root, "", "");
//
//        //find ogg flags
//        fileusage[] fuses = usage.map.values().toArray(new fileusage[]{});
//        for(fileusage fus: fuses)
//        {
//            if(fus.name.endsWith(".prp"))
//            {
//                prpobjects.prpfile prp = prpobjects.prpfile.createFromFile(root+fus.name, true);
//                String age = fus.name.substring(0,fus.name.indexOf("_District_"));
//                age = age.substring(age.lastIndexOf("/")+1);
//                for(prpobjects.PrpRootObject ro: prp.FindAllObjectsOfType(prpobjects.Typeid.plSoundBuffer))
//                {
//                    prpobjects.x0029SoundBuffer sb = ro.castTo();
//                    String ogg = "sfx/"+sb.oggfile;
//                    if(usage.has(ogg)) //we don't want to add any ogg files we don't actually have.
//                    {
//                        usage.add(ogg, age+".mfs");
//                        usage.remove(ogg, "client.mfs"); //we've got a home, remove it from the client.mfs
//                    }
//                    //1)If flag 0x4 or 0x8 is used, decompress to two wav files
//                    //2)if flag 0x10 is used, it shouldn't be decompressed at all
//                    //3)if neither flag 0x4 nor 0x8 is used, decompress to one wav file
//                    //if situation 1 and 2 occur, warn
//                    //if situation 1 and 3 occur, do both!
//                    boolean case1 = ((sb.flags&0x4)!=0)||(((sb.flags&0x8)!=0));
//                    boolean case2 = ((sb.flags&0x10)!=0);
//                    boolean case3 = !case1 && !case2;
//                    //if(case1) usage.get(ogg).soundusecases.add(1);
//                    //if(case2) usage.get(ogg).soundusecases.add(2);
//                    //if(case3) usage.get(ogg).soundusecases.add(4);
//                    fileusage ogf = usage.map.get(ogg);
//                    if(ogf==null)
//                    {
//                        m.warn("ogg file not found.");
//                    }
//                    else
//                    {
//                        fileusage fus2 = usage.addButNotFound(ogg);
//                        if(case1) fus2.sounduse |= 1;
//                        if(case2) fus2.sounduse |= 2;
//                        if(case3) fus2.sounduse |= 4;
//                    }
//                }
//            }
//        }
//
//        //move ogg files from client.mfs to GUI.mfs (includes MyMusic, but that should be okay.)
//        for(fileusage fus: usage.getFilesUsedByUser("client.mfs"))
//        {
//            if(fus.name.startsWith("sfx/") && fus.name.endsWith(".ogg"))
//            {
//                //usage.move(fus,"client.mfs","GUI.mfs");
//                fus.users.add("GUI.mfs");
//                fus.users.remove("client.mfs");
//            }
//        }
//
//
//        //add .age files for crowthistle/myst5
//        String[] forced = {
//            "dat/DescentMystV.age","dat/Direbo.age","dat/KveerMystV.age","dat/Laki.age","dat/MystMystV.age","dat/Siralehn.age","dat/Tahgira.age","dat/Todelmer.age",
//            "dat/MarshScene.age","dat/MountainScene.age",
//        };
//        for(String name: forced)
//        {
//            if(usage.has(name)) usage.add(name,"GUI.mfs");
//        }
//
//        //move GlobalMarkers into client.
//
//        //create the mfs files.
//        if(writemanifestfiles)
//        {
//            for(String mfs: usage.getAllUsers())
//            {
//                m.msg("Doing manifest file: ",mfs);
//                String outfolder;
//                String outmfs;
//                if(mfs.equals("ClientSetupNew.mfs"))
//                {
//                    outfolder = outroot+"/install/Expanded/";
//                    outmfs = outroot+"/install/Expanded/";
//                }
//                else if(mfs.equals("client.mfs"))
//                {
//                    outfolder = outroot+"/game_clients/drcExplorer/";
//                    outmfs = outroot+"/game_clients/drcExplorer/";
//                }
//                else
//                {
//                    outfolder = outroot+"/game_data/";
//                    outmfs = outroot+"/game_data/dat/";
//                }
//                mfsfile mfsf = new mfsfile();
//                Vector<fileusage> fs = usage.getFilesUsedByUser(mfs);
//                /*if(false) //move GlobalMarkers.mfs items into client.mfs since client doesn't try to load them automatically.
//                {
//                    if(mfs.equals("GUI.mfs"))
//                    {
//                        for(fileusage f: usage.getFilesUsedByUser("GlobalMarkers.mfs"))
//                        {
//                            fs.add(f);
//                        }
//                    }
//                    else if(mfs.equals("GlobalMarkers.mfs"))
//                    {
//                        //continue; //don't create GlobalMarkers.mfs
//                    }
//                }*/
//                for(fileusage f: fs)
//                {
//                    f.readinfo();
//                    String entry = f.toString();
//                    if(f.name.endsWith(".age")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".fni")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".csv")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".p2f")) mfsf.base.add(entry);
//                    else if(f.name.endsWith(".prp")) mfsf.pages.add(entry);
//                    else mfsf.other.add(entry);
//
//                    String outname;
//                    if(f.compress==8) outname = f.name+".gz";
//                    else outname = f.name;
//
//                    if(writedatafiles) FileUtils.WriteFile(outfolder+"/"+outname, f.data,true);
//                    f.data = null; //otherwise we leak memory.
//                }
//
//                //hack to move UruExplorer.exe to the top
//                if(mfs.equals("client.mfs"))
//                {
//                    for(int i=0;i<mfsf.other.size();i++)
//                    {
//                        String curs = mfsf.other.get(i);
//                        if(curs.startsWith("UruExplorer.exe,"))
//                        {
//                            mfsf.other.set(i, mfsf.other.get(0));
//                            mfsf.other.set(0, curs);
//                        }
//                    }
//                }
//
//                FileUtils.WriteFile(outmfs+"/"+mfs, mfsf.getBytes(),true);
//            }
//        }
//
//
//        //write a few extra files:
//        //String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nNeighborhood\nNexus\nPersonal\nCustomAvatars\nGlobalAvatars\nAvatarCustomization\n";
//        String agelist_txt = "GlobalAnimations\nGlobalClothing\nGlobalMarkers\nGUI\nCustomAvatars\nGlobalAvatars\n";
//        //read as GlobalAnimations, GlobalAvatars,GlobalClothing,GUI,CustomAvatars
//        FileUtils.WriteFile(outroot+"/game_data/dat/agelist.txt", b.StringToBytes(agelist_txt),true);
//
//        /*if(true)return;
//
//        FileUtils.CreateFolder(outfolder+"install/Expanded/");
//        //StringBuilder s = new StringBuilder();
//        //s.append("[version]\nformat=5\n\n[base]\n\n[pages]\n\n[other]\n");
//        mfsfile mfs = new mfsfile();
//        file f = new file("","UruSetup.exe");
//        mfs.other.add(f.toString());
//        //s.append(f.getEntry());
//        //s.append("\n");
//        FileUtils.WriteFile(outfolder+"install/Expanded/ClientSetupNew.mfs", mfs.getBytes());
//        FileUtils.WriteFile(outfolder+"install/Expanded/UruSetup.exe.gz", f.data);
//
//        String[] clientfiles = {
//            "UruExplorer.exe",
//            "SoundDecompress.exe"
//        };
//
//        FileUtils.CreateFolder(outfolder+"game_clients/drcExplorer/");
//        mfs = new mfsfile();
//        f = new file("","UruExplorer.exe");*/
//
//        m.status("Done generating the dataserver!");
//    }
//    public class fileusage
//    {
//        boolean ignore;
//        boolean found = false;
//        String name;
//        HashSet<String> users = new HashSet();
//        HashSet<Integer> soundusecases = new HashSet();
//        int sounduse = 0;
//
//        byte[] data;
//        int compress;
//        long filesize;
//        long modtime;
//        byte[] md5;
//        byte[] compressedmd5; //used by Moul manifests
//        long compressedfilesize; //used by Moul manifests
//
//        //String path;
//        //String filename;
//        String fullpath;
//
//        public fileusage(String name)
//        {
//            this.name = name;
//        }
//
//        public void readinfo()
//        {
//            //path = path2;
//            //filename = filename2;
//            fullpath = root +"/"+ name;
//
//            //if(name.endsWith(".exe") || name.endsWith(".prp"))
//            if(name.endsWith(".exe") || name.endsWith(".prp") || name.endsWith(".dll") || name.endsWith(".p2f"))
//            {
//                compress = 8;
//            }
//            else if(name.startsWith("sfx/") && name.endsWith(".ogg"))
//            {
//                //compress = 2;
//                m.msg(name+":"+Integer.toString(sounduse));
//                if(name.endsWith("pradWindAmb.ogg"))
//                {
//                    int dummy=0;
//                }
//                switch(sounduse)
//                {
//                    case 1:
//                        compress = 1;
//                        break;
//                    case 2:
//                        compress = 2;
//                        break;
//                    case 4:
//                        compress = 4;
//                        break;
//                    case 3:
//                        m.warn("Unsure case 3.");
//                        compress = 1;
//                        break;
//                    case 5:
//                        m.warn("Unsure case 5.");
//                        compress = 5;
//                        break;
//                    case 6:
//                        m.warn("Unsure case 6.");
//                        compress = 4;
//                        break;
//                    case 7:
//                        m.warn("Unsure case 7.");
//                        //compress = 7;
//                        compress = 5;
//                        break;
//                    case 0:
//                        m.warn("no users, not decompressing: ",name);
//                        compress = 2;
//                        break;
//                    default:
//                        m.err("Unsure case default.");
//                        break;
//                }
//            }
//            else
//            {
//                if(type==DataserverType.alcugs)
//                    compress = 0;
//                else
//                    compress = 8; //Moul forces everything to be compressed.
//            }
//
//            //String fullpath = root+"/"+relFilename;
//            filesize = FileUtils.GetFilesize(fullpath);
//            modtime = FileUtils.GetModtime(fullpath);
//            byte[] rawdata = FileUtils.ReadFile(fullpath);
//            md5 = shared.CryptHashes.GetMd5(rawdata);
//            if(compress==8 || type==type.moul) //moul is always compressed
//            {
//                data = zip.compressGzip(rawdata);
//                compressedmd5 = shared.CryptHashes.GetMd5(data); //needed for Moul manifests.
//                compressedfilesize = data.length; //needed for Moul manifests
//            }
//            else
//            {
//                data = rawdata;
//                compressedmd5 = b.CopyBytes(md5);
//                compressedfilesize = filesize;
//            }
//        }
//        public String toString()
//        {
//            StringBuilder s = new StringBuilder();
//
//            //write filename:
//            String label;
//            if(name.endsWith(".prp"))
//            {
//                label = name.substring(name.lastIndexOf("/")+1);
//            }
//            else
//            {
//                label = name;
//            }
//            //if(!path.equals("")) s.append(path+"/");
//            //s.append(filename);
//            s.append(label);
//            s.append(",");
//
//            //write filesize:
//            s.append(Long.toString(filesize));
//            s.append(",");
//
//            //write date:
//            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//            Date d = new Date(modtime);
//            String dresult = format.format(d);
//            s.append(dresult);
//            s.append(",");
//
//            //write md5:
//            String md5str = b.BytesToHexString(md5);
//            md5str = md5str.toLowerCase();
//            s.append(md5str);
//            s.append(",");
//
//            //write flag:
//            s.append(Integer.toString(compress));
//            if(compress==8)
//            {
//                //s.append("8,");
//                s.append(",");
//                int compsize = data.length;
//                s.append(Integer.toString(compsize));
//            }
//            /*else if(compress==2)
//            {
//                s.append("2");
//            }
//            else
//            {
//                s.append("0");
//            }*/
//
//            String result = s.toString();
//            return result;
//        }
//    }
//    public class fileusers
//    {
//        HashMap<String,fileusage> map = new HashMap();
//
//        public fileusers()
//        {
//        }
//
//        /*public fileusage get(String file)
//        {
//            fileusage result = map.get(file);
//            if(result==null) result = add(file);
//            return result;
//        }*/
//        public boolean has(String file)
//        {
//            return map.containsKey(file);
//        }
//        public fileusage add(String file)
//        {
//            fileusage users = addButNotFound(file);
//            users.found = true;
//            return users;
//        }
//        public fileusage addButNotFound(String file)
//        {
//            fileusage users = map.get(file);
//            if(users==null)
//            {
//                users = new fileusage(file);
//                map.put(file, users);
//            }
//            return users;
//        }
//        public fileusage add(String file, String age)
//        {
//            fileusage result = add(file);
//            result.users.add(age);
//            return result;
//        }
//        public void remove(String file, String age)
//        {
//            fileusage users = map.get(file);
//            if(users!=null)
//            {
//                users.users.remove(age);
//            }
//        }
//
//        public HashSet<String> getAllUsers()
//        {
//            HashSet<String> result = new HashSet();
//
//            for(fileusage fuse: map.values())
//            {
//                for(String user: fuse.users)
//                {
//                    result.add(user);
//                }
//            }
//
//            return result;
//        }
//
//        public Vector<fileusage> getFilesUsedByUser(String mfsuser)
//        {
//            Vector<fileusage> result = new Vector();
//
//            for(fileusage fuse: map.values())
//            {
//                for(String user: fuse.users)
//                {
//                    if(mfsuser.equals(user))
//                    {
//                        result.add(fuse);
//                    }
//                }
//            }
//
//            return result;
//        }
//
//        /*public Vector<fileusage> getFilesUsedByAge(String age)
//        {
//            Vector<String> result = new Vector();
//            for(Entry<String,HashSet<String>> e: map.entrySet())
//            {
//                for(String curage: e.getValue())
//                {
//                    if(curage.equals(age))
//                    {
//                        result.add(e.getKey());
//                    }
//                }
//            }
//            return result;
//        }*/
//    }
//    public static class mfsfile
//    {
//        int format;
//        Vector<String> base = new Vector();
//        Vector<String> pages = new Vector();
//        Vector<String> other = new Vector();
//
//        public mfsfile(){}
//
//        public String toString()
//        {
//            StringBuilder result = new StringBuilder();
//            result.append("[version]\nformat=5\n\n");
//
//            result.append("[base]\n");
//            for(String s: base)
//            {
//                result.append(s);
//                result.append("\n");
//            }
//            result.append("\n");
//
//            result.append("[pages]\n");
//            for(String s: pages)
//            {
//                result.append(s);
//                result.append("\n");
//            }
//            result.append("\n");
//
//            result.append("[other]\n");
//            for(String s: other)
//            {
//                result.append(s);
//                result.append("\n");
//            }
//            //result.append("\n");
//
//            return result.toString();
//        }
//        public byte[] getBytes()
//        {
//            return b.StringToBytes(this.toString());
//        }
//    }
//
//    /*public class file
//    {
//        byte[] data;
//        int compress;
//        long filesize;
//        long modtime;
//        byte[] md5;
//
//        String path;
//        String filename;
//        String fullpath;
//
//        public file(String path2, String filename2)
//        {
//            path = path2;
//            filename = filename2;
//            fullpath = root +"/"+ path + "/" + filename;
//
//            if(filename.endsWith(".exe") || filename.endsWith(".prp"))
//            {
//                compress = 8;
//            }
//            else if(filename.endsWith(".ogg"))
//            {
//                compress = 2;
//            }
//            else
//            {
//                compress = 0;
//            }
//
//            //String fullpath = root+"/"+relFilename;
//            filesize = FileUtils.GetFilesize(fullpath);
//            modtime = FileUtils.GetModtime(fullpath);
//            if(compress==8)
//            {
//                data = zip.compressGzip(FileUtils.ReadFile(fullpath));
//            }
//            else
//            {
//                data = FileUtils.ReadFile(fullpath);
//            }
//            md5 = shared.CryptHashes.GetMd5(data);
//        }
//        public String toString()
//        {
//            StringBuilder s = new StringBuilder();
//
//            //write filename:
//            if(!path.equals("")) s.append(path+"/");
//            s.append(filename);
//            s.append(",");
//
//            //write filesize:
//            s.append(Long.toString(filesize));
//            s.append(",");
//
//            //write date:
//            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//            Date d = new Date(modtime);
//            String dresult = format.format(d);
//            s.append(dresult);
//            s.append(",");
//
//            //write md5:
//            String md5str = b.BytesToHexString(md5);
//            md5str = md5str.toLowerCase();
//            s.append(md5str);
//            s.append(",");
//
//            //write flag:
//            if(compress==8)
//            {
//                s.append("8,");
//                int compsize = data.length;
//                s.append(Integer.toString(compsize));
//            }
//            else if(compress==2)
//            {
//                s.append("2");
//            }
//            else
//            {
//                s.append("0");
//            }
//
//            String result = s.toString();
//            return result;
//        }
//    }*/
//}

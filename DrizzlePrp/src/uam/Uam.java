/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

import javax.swing.event.ListSelectionEvent;
import shared.b;
import shared.m;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Vector;
import shared.*;
import java.util.HashMap;
import java.io.File;

import prpobjects.prpfile;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;

public class Uam
{
    public static UamConfigNew ageList;
    //public static HashMap<String,InstallStatus> ageInstallStatus;
    public static InstallInfo installInfo;
    public static final String ageArchivesFolder = "/agearchives/";
    public static final String versionSep = "--";
    public static final String statusFilename = "uam.status.xml";
    //public static final int version = 16;
    
    public static class InstallInfo
    {
        public HashMap<String, AgeInstallInfo> ages = new HashMap();
        public int numNotInstalled;
        public int numUnknown;
        public int numUpdatable;
        public boolean fullyUpToDate;
        
        public AgeInstallInfo getOrCreateAge(String age)
        {
            AgeInstallInfo result = ages.get(age);
            if(result==null)
            {
                result = new AgeInstallInfo();
                ages.put(age, result);
            }
            return result;
        }
        
        public AgeInstallInfo getAge(String age)
        {
            return ages.get(age);
        }
        
        public void countStats()
        {
            this.numNotInstalled = 0;
            this.numUnknown = 0;
            this.numUpdatable = 0;
            for(AgeInstallInfo ageinfo: ages.values())
            {
                switch(ageinfo.installationStatus)
                {
                    case latestVersionInCache:
                        break;
                    case noVersionsExist:
                        break;
                    case nonLatestVersionInCache:
                        this.numUpdatable++;
                        break;
                    case notInCache:
                        this.numUnknown++;
                        break;
                    case notInstalled:
                        this.numNotInstalled++;
                        break;
                    default:
                        m.err("Unhandled installation type.");
                        break;
                }
            }
            this.fullyUpToDate = (this.numUnknown==0 && this.numNotInstalled==0 && this.numUpdatable==0);
        }
        
        public void printStatsMessage()
        {
            this.countStats();
            //String result;
            if(this.fullyUpToDate)
            {
                m.msg("You have everything installed!");
            }
            else
            {
                //StringBuilder result2 = new StringBuilder();
                Vector<String> result2 = new Vector();
                if(this.numNotInstalled!=0)
                {
                    result2.add("Ages not installed: ");
                    result2.add(Integer.toString(this.numNotInstalled)+"    ");
                }
                if(this.numUpdatable!=0)
                {
                    result2.add("Ages updatable: ");
                    result2.add(Integer.toString(this.numUpdatable)+"    ");
                }
                if(this.numUnknown!=0)
                {
                    result2.add("Ages with unknown installation status: ");
                    result2.add(Integer.toString(this.numUnknown)+"    ");
                }
                //result = result2.toString();
                m.msg(shared.generic.convertVectorToArray(result2, String.class));
            }


            //m.msgsafe(result);
        }
    }
    public static class AgeInstallInfo
    {
        public InstallStatus installationStatus = InstallStatus.notInstalled;
        public HashMap<String, InstallStatus> versions = new HashMap();
        public String installedVersion = null;
        
        /*public AgeInstallInfo(InstallStatus installationStatus)
        {
            this.installationStatus = installationStatus;
        }*/
        
    }
    public static enum InstallStatus
    {
        noVersionsExist,
        notInstalled,
        latestVersionInCache,
        nonLatestVersionInCache,
        notInCache,
        ;
        
        public boolean isInstalled()
        {
            return !(this==notInstalled || this==noVersionsExist);
        }
    }

    public static boolean HasPermissions(String foldername)
    {
        boolean hasperms = FileUtils.HasPermissions2(foldername,false);
        if(!hasperms)
        {
            m.err("You appear to be running Windows Vista or Windows Seven.  Uru has a bug that will require a workaround, see http://myst.dustbird.net/wiki/Drizzle#Vista_and_above for details.");
        }
        return hasperms;
    }


    /*public static void DeleteArchives()
    {
        if(!auto.AllGames.getPots().isFolderX(Uam.getPotsFolder())) return;

        if(shared.GuiUtils.getOKorCancelFromUser(m.trans("Are you sure you want to delete the archives?"),m.trans("Delete the archives?")))
        {
            for(String filename: GetAllAgeArchives())
            {
                FileUtils.ZeroFile(filename);
            }
        }
    }*/
    public static void CheckForProblems(boolean doDetailedCheck)
    {
        //if doDetailedCheck is false, we'll just do quick checks.

        //check for _socket.pyd
        String pathToFile = Uam.getPotsFolder()+"/Python/system/_socket.pyd";
        if(FileUtils.Exists(pathToFile))
        {
            m.warn("There is a privacy/security/persistency risk.  It is strongly recommended that you either: a) install the 'Network Access' with version '(disabled)', or b) manually delete the following file: ",pathToFile," Either way, this warning should not be displayed afterwards.");
        }
        
    }
    public static void ClearSumFiles()
    {
        if(!auto.AllGames.getPots().isFolderX(Uam.getPotsFolder())) return;
        if(!uam.Uam.HasPermissions(Uam.getPotsFolder())) return;

        m.status("Clearing all .sum files...");
        byte[] sumdata = prpobjects.sumfile.createEmptySumfile().getByteArray();
        for(File sumfile: FileUtils.FindAllFiles(Uam.getPotsFolder()+"/dat/", ".sum", false))
        {
            FileUtils.WriteFile(sumfile, sumdata);
        }
        m.status("Done!");
    }
    public static void TryToFindInstalledVersionsOfAllAges()
    {
        for(UamConfigNew.UamConfigData.Age age: Uam.ageList.getAllAges())
        {
            String ver = TryToFindInstalledVersionOfAge(age);
        }
    }
    public static String TryToFindInstalledVersionOfAge(String agename)
    {
        return TryToFindInstalledVersionOfAge(Uam.ageList.getAge(agename));
    }
    private static String TryToFindInstalledVersionOfAge(UamConfigNew.UamConfigData.Age age)
    {
        AgeInstallInfo info = Uam.installInfo.getAge(age.filename);
        if(info.installedVersion!=null) return info.installedVersion;

        for(UamConfigNew.UamConfigData.Age.Version ver: age.versions)
        {
            String archivefilename = Uam.getPotsFolder()+Uam.ageArchivesFolder+age.filename+Uam.versionSep+ver.name+".7z";
            if(FileUtils.Exists(archivefilename))
            {
                boolean isinstalled = shared.sevenzip.check(archivefilename,Uam.getPotsFolder(), new shared.sevenzip.FileIncluder() {
                    public boolean includeFile(String filename) {
                        if(filename.endsWith(".sum")) return false;
                        return true;
                    }
                });
                if(isinstalled)
                {
                    info.installedVersion = ver.name;
                    return info.installedVersion;
                }
            }

            //m.warn("Unable to find version of installed Age: ",age.filename);
            //return null; //problem!
        }
        return null; //no problem.
    }

    public static void DeleteOldArchives()
    {
        if(!auto.AllGames.getPots().isFolderX(Uam.getPotsFolder())) return;
        if(!uam.Uam.HasPermissions(Uam.getPotsFolder())) return;

        if(Uam.ageList==null)
        {
            m.err("You need to load the Age List in the UAM tab first.");
            return;
        }

        if(!shared.GuiUtils.getOKorCancelFromUser(m.trans("Are you sure you want to delete the old archives?"),m.trans("Delete the archives?")))
        {
            return;
        }


        m.status("Detecting which versions are installed...");
        TryToFindInstalledVersionsOfAllAges();


        for(UamConfigNew.UamConfigData.Age age: Uam.ageList.getAllAges())
        {
            AgeInstallInfo info = Uam.installInfo.getAge(age.filename);
            if(info.installationStatus.isInstalled())
            {
                if(info.installedVersion!=null)
                {
                    //delete all other archives
                    for(UamConfigNew.UamConfigData.Age.Version ver: age.versions)
                    {
                        if(!ver.name.equals(info.installedVersion))
                        {
                            DeleteArchive(age.filename,ver.name);
                        }
                    }
                }
                else
                {
                    //unknown, don't do anything!
                    m.warn("Unable to detect installed version, so keeping archives for Age: ",age.filename);
                }
            }
            else
            {
                //delete all archives.
                for(UamConfigNew.UamConfigData.Age.Version ver: age.versions)
                {
                    DeleteArchive(age.filename,ver.name);
                }
            }
        }
        m.status("Done!");
        //gui.UamGui.RefreshInfo(Uam.getPotsFolder());

        gui.UamGui.RefreshInfo();

    }
    private static void DeleteArchive(String agename, String version)
    {
        String f = Uam.getPotsFolder()+Uam.ageArchivesFolder+agename+Uam.versionSep+version+".7z";
        if(FileUtils.Exists(f))
        {
            FileUtils.DeleteFile2(f);
        }
    }
    /*public static void DeleteArchives()
    {
        if(!auto.AllGames.getPots().isFolderX(Uam.getPotsFolder())) return;

        if(shared.GuiUtils.getOKorCancelFromUser(m.trans("Are you sure you want to delete the archives?"),m.trans("Delete the archives?")))
        {
            for(String filename: GetAllAgeArchives())
            {
                FileUtils.ZeroFile(filename);
            }
        }
    }*/
    public static Vector<String> GetAllAgeArchives()
    {
        if(!auto.AllGames.getPots().isFolderX(Uam.getPotsFolder())) return null;

        Vector<String> r = new Vector();

        File archivefolder = new File(Uam.getPotsFolder()+Uam.ageArchivesFolder);
        for(File file: archivefolder.listFiles())
        {
            if(file.isFile() && file.getName().endsWith(".7z"))
            {
                r.add(file.getAbsolutePath());
            }
        }

        return r;
    }

    public static String GetRandomAge()
    {
        //int num = ageList.data.ages.size();
        //int randnum = (new java.util.Random()).nextInt(num);
        //String result = ageList.data.ages.get(randnum).propername;
        //return result;
        if(ageList==null)
        {
            m.err("You need to load the Age List in the UAM tab first.");
            return "";
        }

        String[] fanages = new String[ageList.data.ages.size()];
        for(int i=0;i<ageList.data.ages.size();i++)
        {
            fanages[i] = ageList.data.ages.get(i).propername;
        }
        String item = shared.RandomUtils.GetRandomItem(auto.ageLists.potsages,auto.ageLists.convertedages, fanages);
        return item;
    }

    public static void RunTests(String potsfolder)
    {
        if(!auto.AllGames.getPots().isFolderX(potsfolder)) return;
        boolean ignoreKnownOverrides = shared.State.AllStates.getStateAsBoolean("uamig");
        uam.Uam.HasPermissions(potsfolder); //print message if no perms.

        m.msg("Checking for python file duplicates...");
        java.util.HashMap<String, Vector<String>> pyfiles = new java.util.HashMap();
        Vector<File> pakfiles = shared.FileUtils.FindAllFiles(potsfolder+"/Python/", ".pak", false);
        for(File f: pakfiles)
        {
            //m.msg(f.getName());
            prpobjects.pakfile pak = new prpobjects.pakfile(f.getAbsolutePath(), auto.AllGames.getPots().g, true);
            for(prpobjects.pakfile.IndexEntry ind: pak.indices)
            {
                String pyname = ind.objectname.toString();
                Vector<String> paklist = pyfiles.get(pyname);
                if(paklist==null)
                {
                    pyfiles.put(pyname, new Vector());
                    pyfiles.get(pyname).add(f.getName());
                }
                else pyfiles.get(pyname).add(f.getName());
            }
        }
        for(String pyfile: pyfiles.keySet())
        {
            Vector<String> paklist = pyfiles.get(pyfile);
            boolean complain = false;
            if(paklist.size()==2)
            {
                if(ignoreKnownOverrides && (paklist.contains("moul.pak")||paklist.contains("offlineki.pak")||paklist.contains("UruLibraryManager.pak")
                        ||paklist.contains("tpots-fixes.pak")||paklist.contains("tpots-addons.pak")))
                {
                    //ignore
                }
                else
                {
                    complain = true;
                }
            }
            else if(paklist.size()>2)
            {
                complain = true;
            }

            if(complain)
            {
                String complaint = "  The file "+pyfile+" was found in these files: ";
                for(String pakfile: paklist)
                {
                    complaint += pakfile+", ";
                }
                m.msg(complaint);
            }
        }
        m.msg("Done checking for python file duplicates.");


        m.msg("Checking for sequence prefix duplicates...");
        java.util.HashMap<String, String> seqprefs = new java.util.HashMap();
        Vector<File> agefiles = shared.FileUtils.FindAllFiles(potsfolder+"/dat/", ".age", false);
        for(File f: agefiles)
        {
            prpobjects.textfile agefile = prpobjects.textfile.createFromBytes(uru.UruCrypt.DecryptWhatdoyousee(FileUtils.ReadFile(f)));
            String seq = agefile.getVariable("SequencePrefix");
            if(seqprefs.containsKey(seq))
            {
                m.msg("  Sequence Prefix from ",f.getName()," already used in ",seqprefs.get(seq));
            }
            else
            {
                seqprefs.put(seq, f.getName());
            }
        }
        m.msg("Done checking for sequence prefix duplicates.");


        //This following block works, but it takes a long time, and fails on a Neighborhood prp (I guess the prpfile.createFromFile chokes, even though we read as raw.)
        /*m.msg("Checking for missing ogg files...");
        for(File f: shared.FileUtils.FindAllFiles(potsfolder+"/dat/", ".prp", false))
        {
            prpfile prp = prpfile.createFromFile(f, true);
            for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plSoundBuffer))
            {
                uru.moulprp.x0029SoundBuffer sb = ro.castTo();
                String oggfile = sb.oggfile.toString();
                if(!shared.FileUtils.Exists(potsfolder+"/sfx/"+oggfile))
                {
                    m.msg("  Oggfile: "+oggfile+" not present, but used in: "+f.getName());
                }
            }
        }
        m.msg("Done checking for missing ogg files.");*/

        m.msg("Checking for prps with duplicate page IDs...");
        java.util.HashMap<prpobjects.Pageid,String> pageids = new java.util.HashMap();
        Vector<File> prpfiles = shared.FileUtils.FindAllFiles(potsfolder+"/dat/", ".prp", false);
        for(File f: prpfiles)
        {
            IBytestream c = shared.SerialBytestream.createFromFile(f);
            prpobjects.PrpHeader header = new prpobjects.PrpHeader(c);
            if(pageids.containsKey(header.pageid))
            {
                m.msg("  Pageid ",header.pageid.toString2()," used in both ",f.getName()," and ",pageids.get(header.pageid));
            }
            else
            {
                pageids.put(header.pageid, f.getName());
            }
        }
        m.msg("Done checking for duplicate page IDs.");


        m.msg("Checking .sum files...");
        for(File f: shared.FileUtils.FindAllFiles(potsfolder+"/dat/", ".sum", false))
        {
            prpobjects.sumfile sf = prpobjects.sumfile.readFromFile(f, 3);
            for(prpobjects.sumfile.sumfileFileinfo sfi: sf.files)
            {
                String filename = potsfolder+"/"+sfi.filename.toString();
                if(FileUtils.Exists(filename))
                {
                    byte[] expectedhash = sfi.md5;
                    byte[] actualhash = shared.CryptHashes.GetHash(filename, shared.CryptHashes.Hashtype.md5);
                    if(b.isEqual(actualhash, expectedhash))
                    {
                        //is good!
                    }
                    else
                    {
                        m.msg("  File listed in ",f.getAbsolutePath()," has the wrong hash: ",filename);
                    }
                }
                else
                {
                    m.msg("  File listed in ",f.getAbsolutePath()," was not found: ",filename);
                }
            }
        }
        m.msg("Done checking .sum files.");
    }
    public static void launchUru()
    {
        //shared.Exec.LaunchProgram(getPotsFolder()+"/"+"UruSetup.exe", "Uru");

        //Vista/Win7 can't start UruSetup.exe directly, because of the name.
        if(gui.Main.isVistaPlus())
        {
            shared.Exec.LaunchProgram(getPotsFolder()+"/"+"Uru.exe", "Uru");
        }
        else
        {
            shared.Exec.LaunchProgram(getPotsFolder()+"/"+"UruSetup.exe", "Uru");
        }

        /*String potsfolder = getPotsFolder()+"/";
        if(!automation.detectinstallation.isFolderPots(potsfolder)) return;
        String[] command = new String[]{
            potsfolder+"UruSetup.exe",
        };
        try
        {
            java.lang.Process p = Runtime.getRuntime().exec(command, null, new File(potsfolder));
            //Process proc = Runtime.getRuntime().exec(command);
            m.status("Uru launched!");
        }
        catch(java.io.IOException e)
        {
            m.err("Unable to launch Uru.");
        }*/

    }
    public static void launchSoundDecompress()
    {
        shared.Exec.LaunchProgram(getPotsFolder()+"/"+"SoundDecompress.exe", "SoundDecompress");
    }
    public static String getPotsFolder()
    {
        return shared.State.AllStates.getStateAsString("uamRoot");
    }
    /*public static void DownloadAge7Zip(String age,String ver,String mir,String potsfolder)
    {
    }*/
    public static void listAvailableAges()
    {
        if(ageList==null)
        {
            m.msg("You have to get the list of available Ages first.");
            return;
        }
        /*for(String s: ageList.getAllAgeNames())
        {
            m.msg("Age: "+s);
            for(String ver: ageList.getAllVersionsOfAge(s))
            {
                m.msg("  Ver: "+ver);
                for(String mirror: ageList.getAllUrlsOfAgeVersion(s, ver))
                {
                    m.msg("    Mir: "+mirror);
                }
            }
        }*/
        for(UamConfigNew.UamConfigData.Age age: ageList.data.ages)
        {
            m.msg("Age: ",age.filename);
            for(UamConfigNew.UamConfigData.Age.Version ver: age.versions)
            {
                m.msg("  Ver: ",ver.name);
                for(UamConfigNew.UamConfigData.Age.Version.Mirror mir: ver.mirrors)
                {
                    m.msg("    Mir: ",mir.url);
                }
            }
        }
    }
    
    /*public static Vector<String> GetAgeList(String server, gui.Gui gui)
    {
        //GuiModal modal = new GuiModal(null,true);
        //modal.setVisible(true);
        //modal.showButDontBlock();
        
        //SevenZip.J7zip 
        
        //String file = "http://www.the-ancient-city.de/uru-ages/Abysos.rar";
        //String file = "http://www.the-ancient-city.de/uru-ages/Dustin2.rar";
        //ThreadDownloadAndProcess.downloadConfig(server,gui);
        //String file = server+"/uam.status.txt";
        //byte[] result = ThreadDownloader.downloadAsBytes(file);
        //return result;
        //if(true)return;
        
        //parse config file.
        //ByteArrayInputStream in = new ByteArrayInputStream(result);
        //ageList = new UamConfig(in);
        
        //list Ages...
        //return ageList.getAllAgeNames();
        
        
        //String statusfile = server+"/uam.status.txt";
        //String statusfile = "http://www.the-ancient-city.de/uru-ages/Abysos.rar";
        //byte[] data = uam.Downloader.DownloadFile(statusfile);
        //String datastr = b.BytesToString(data);
        //m.msg(datastr);
    }*/
}

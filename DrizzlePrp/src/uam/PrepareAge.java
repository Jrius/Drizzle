/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

import shared.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.HashSet;

public class PrepareAge
{
    public static void DoAllWork(String basefolder/*, boolean isDryRun*/)
    {
        //The base folder is expected to have a certain layout.
        String tempfolder = basefolder + "/temp/"; //temp folder whose contents can be safely deleted afterwards. Will be created if doesn't exist.
        String archivefolder = basefolder + "/files/http/roots/myst.dustbird.net/uru-ages/"; //"/files/http/www/uru-ages/"; //where to find the .7z files, and to place the uam.status.xml file.
        String urufilesfolder = basefolder + "/UruFiles/"; //The location of Uru files.  This one has numbered subfolders, 0 is overriden by the contents of 1 which is overriden by the contents of 2, etc.  These subfolders are the ones that have Uru's directory structure.
        //String dataserverfolder = basefolder + "/files/alcugs/dataserver/"; //The output folder for the generated dataserver files.
        String dataserverfolder = basefolder + "/files/wwwuamsharddataserver/http/"; //The output folder for the generated dataserver files.
          //dataserverfolder was "/files/http/dataserver/"
        String sdlfolder = basefolder + "/files/alcugs/sdl/"; //Folder Alcugs will look in for .sdl files.
        String agefolder = basefolder + "/files/alcugs/age/"; //Folder Alcugs will look in for .age files.
        String statusfolder = basefolder + "/files/http/roots/myst.dustbird.net/status/";  //"/files/http/www/status/";

        gui.Interactor interactor = new gui.InteractorGui(); //this is a gui one, but we could have a commandline one passed in instead.
        StringBuilder summary = new StringBuilder();
        summary.append("****************** Summary *******************\n");

        boolean doAgeArchives = new File(archivefolder).exists();
        boolean doDataserver = new File(dataserverfolder).exists();


        //get info on archive updates
        uam.UamConfigNew.ArchivesInfo info = uam.UamConfigNew.generateStatusFile(archivefolder,interactor);

        //create the merged view:
        MergeFile urufiles = MergeFile.CreateWithRoot(urufilesfolder);

        //try to prepare the age for the shard
        final String[] manualages = {"offlineki","Drizzle","AgeInformation","officialnocd","UamKiPlugin"}; //these Ages will not be automatically prepared.
        m.msg();
        m.msg("Preparing shard files...");
        //if(isDryRun) m.status("(Dry run)");
        boolean noproblems = true;
        for(String archive: info.newFilenames)
        {
            //find if this is an Age we wish to do manually.
            File archf = new File(archive);
            boolean ignore = false;
            for(String manualage: manualages)
            {
                if(archf.getName().startsWith(manualage+"--")) ignore = true;
            }
            
            //check the date
            String ver = PrepareAge.GetVersionFromFilename(archf.getName()) ;
            java.util.Date verdate = PrepareAge.GetDateFromVersion(ver);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(Calendar.DATE, -20); //get the day 20 days ago
            if(verdate==null)
            {
                if(!interactor.AskOkCancel("Unable to parse the version's date: "+ver+"  Continue?")) return;
            }
            else if(verdate.before(cal.getTime())) //if before 20 days ago
            {
                if(!interactor.AskOkCancel("Version's date is very old: "+ver+"  Continue?")) return;
            }
            

            //do this age if appropriate
            if(ignore)
            {
                m.warn("Skipping Ages because it should be done manually: ",archive);
            }
            else
            {
                boolean success2 = DoShardAgeWork(archive, urufiles, tempfolder);
                if(!success2) noproblems = false;
            }
        }

        
        if(!noproblems)
        {
            summary.append("There were python problems.\n");
            //ask if we want to continue.
            boolean docontinue = interactor.AskOkCancel("There were problems.  Should we continue?");
            if(!docontinue) return;
        }

        
        //order shard list to match Uam
        //well, let's just warn about .age files without an entry for now.
        final boolean donexuslist = false;
        if(donexuslist)
        {
            boolean agesmissing = false;
            //get cyan ages
            Set<String> cyanages = auto.ageLists.AllCyanAges();
            //read current inf file
            String nexusfilename = urufiles.GetAbsPath("/AvailableFanLinks.inf");
            String nexusdata = FileUtils.ReadFileAsString(nexusfilename);
            OfflineKIInfFile inf = new OfflineKIInfFile(nexusdata);
            Set<String> nexusages = inf.GetAllAgeFilenames();
            //read current inf file
            OfflineKIInfFile inf2 = new OfflineKIInfFile(FileUtils.ReadFileAsString(urufiles.GetAbsPath("/AvailableExtraLinks.inf")));
            Set<String> nexusages2 = inf2.GetAllAgeFilenames();

            for(String relpath: urufiles.listChildrenRelpaths("/dat/"))
            {
                if(relpath.endsWith(".age"))
                {
                    String agename = new File(urufiles.GetAbsPath(relpath)).getName();
                    agename = agename.substring(0, agename.indexOf("."));

                    //check if Cyan Age.
                    if(cyanages.contains(agename)) continue; //just a Cyan Age; continue.

                    //check if already in list.
                    if(nexusages.contains(agename)) continue; //already in the nexus list.
                    if(nexusages2.contains(agename)) continue; //already in the nexus list.
                    
                    //try to add it
                    boolean tryadd = interactor.AskOkCancel("The Age was not found in the list, shall we try to add it to AvailableFanLinks.inf?");
                    if(tryadd)
                    {
                        String propername = info.config.getAgeProperName(agename);
                        inf.AddRestorationLinkLineAlphabetically(agename, propername);
                        String infdata = inf.ToRawString();
                        //FileUtils.CopyFile(nexusfilename, nexusfilename+"date", false, false, true); //don't make copy, or it will be put on dataserver :P
                        FileUtils.WriteFile(nexusfilename, infdata, false, true);
                    }
                    else
                    {
                        //we could add it to the list here, but let's just warn for now.
                        agesmissing = true;
                        m.warn("Add this age to AvailableFanLinks.inf: ",agename);
                    }
                }
            }
            if(agesmissing) summary.append("There are Ages that need to be added to AvailableFanLinks.inf\n");
        }

        // Check for Ages with missing info
        final boolean checkageinfo = true;
        if(checkageinfo)
        {
            boolean agesmissing = false;
            //get cyan ages
            Set<String> cyanages = auto.ageLists.AllCyanAges();
            //read current inf file
            String nexusfilename = urufiles.GetAbsPath("/AvailableFanLinks.inf");
            String nexusdata = FileUtils.ReadFileAsString(nexusfilename);
            OfflineKIInfFile inf = new OfflineKIInfFile(nexusdata);
            Set<String> nexusages = inf.GetAllAgeFilenames();
            //read current inf file
            OfflineKIInfFile inf2 = new OfflineKIInfFile(FileUtils.ReadFileAsString(urufiles.GetAbsPath("/AvailableExtraLinks.inf")));
            Set<String> nexusages2 = inf2.GetAllAgeFilenames();
            //read UamAgeInfo files
            Set<String> ageinfoages = new java.util.HashSet<String>();
            for(String relpath: urufiles.listChildrenRelpaths("/img/UamAgeInfo/"))
            {
                String filename = new File(urufiles.GetAbsPath(relpath)).getName();
                if (filename.startsWith("UamAgeInfo--") && filename.endsWith(".txt"))
                {
                    String agename = filename.substring("UamAgeInfo--".length(), filename.length()-".txt".length());
                    ageinfoages.add(agename);
                }
            }

            for(String relpath: urufiles.listChildrenRelpaths("/dat/"))
            {
                if(relpath.endsWith(".age"))
                {
                    String agename = new File(urufiles.GetAbsPath(relpath)).getName();
                    agename = agename.substring(0, agename.indexOf("."));

                    //check if Cyan Age.
                    if(cyanages.contains(agename)) continue; //just a Cyan Age; continue.

                    //check if already in list.
                    if(nexusages.contains(agename)) continue; //already in the nexus list.
                    if(nexusages2.contains(agename)) continue; //already in the nexus list.
                    
                    //check if already in ageinfo folder.
                    if(ageinfoages.contains(agename)) continue;
                    
                    //try to add it
                    agesmissing = true;
                    boolean keepgoing = interactor.AskOkCancel("The Age was not found in the list, you should make a UamAgeInfo file for it. Continue anyway?");
                    if (keepgoing)
                    {
                        summary.append("You should add the age info file to the archive and try again:  /img/UamAgeInfo/UamAgeInfo--"+agename+".txt");
                    }
                    else
                    {
                        return;
                    }
                    
                }
            }
            //if(agesmissing) summary.append("There are Ages that need to be added to AvailableFanLinks.inf\n");
        }
        
        
        //merge files, so that we can just pass the merged files on to the dataserver generator.  I'm too lazy to rewrite the dataserver generator, and these merged files aren't on the server anyway, so who cares.
        String absMergedPath = urufiles.MergeAllVersions();

        //generate the dataserver
        m.status("Beginning to generate dataserver from ",absMergedPath," to ",dataserverfolder);
        uru.server.Dataserver.CreateFiles(absMergedPath, dataserverfolder, true);

        //copy .sdl and .age files
        m.status("Copying .sdl and .age files...");
        for(String relpath: urufiles.listChildrenRelpaths("/SDL/"))
        {
            if(relpath.endsWith(".sdl"))
            {
                String abspath = urufiles.GetAbsPath(relpath);
                File abspath2 = new File(abspath);
                String dest = sdlfolder+"/"+abspath2.getName();
                FileUtils.CopyFile(abspath, dest, true, true, true);
                FileUtils.CopyModTime(abspath, dest);
            }
        }
        for(String relpath: urufiles.listChildrenRelpaths("/dat/"))
        {
            if(relpath.endsWith(".age"))
            {
                String abspath = urufiles.GetAbsPath(relpath);
                File abspath2 = new File(abspath);
                String dest = agefolder+"/"+abspath2.getName();
                FileUtils.CopyFile(abspath, dest, true, true, true);
                FileUtils.CopyModTime(abspath, dest);
            }
        }

        //copy other files
        String[][] copies = {
            {absMergedPath+"/ReleaseNotes.html" , statusfolder+"/ReleaseNotes.html"} //we might use the ReleaseNotes as the welcome message!
        };
        for(String[] copy: copies)
        {
            String from = copy[0];
            String to = copy[1];
            if(FileUtils.Exists(from)) //just ignore ones that don't exist.
            {
                byte[] data = FileUtils.ReadFile(from);
                FileUtils.SaveFileIfChanged(to, data, true, true);
            }
        }

        //create uam.status.xml and save backup, if changed.
        //if(!isDryRun)
        //{
        String newStatusFileContents = info.config.data.generateXml();
        String statusfile = archivefolder+"/"+uam.Uam.statusFilename;
        String oldstatustext = FileUtils.ReadFileAsString(statusfile);
        if(!newStatusFileContents.equals(oldstatustext))
        {
            //do new status message
            String curmessage = info.config.data.welcome;
            String newmessage = interactor.AskQuestion("Do you want a new status message? (blank to keep the old one)");
            if(!newmessage.equals(""))
            {
                info.config.data.comments.add(curmessage);
                info.config.data.welcome = newmessage;
                newStatusFileContents = info.config.data.generateXml(); //regenerate now that we've changed the message.
            }


            shared.FileUtils.CopyFile(statusfile, statusfile+shared.DateTimeUtils.GetSortableCurrentDate()+".xml", false, false);
            shared.FileUtils.WriteFile(statusfile, b.StringToBytes(newStatusFileContents));
            m.status("Finished creating new status file!");
        }
        //}
        summary.append("(All done!)\n");
        
        //print summary
        m.msg();
        m.msg(summary.toString());

    }
    
    public static interface DustFile
    {
        //use this if we want to have differt implementations of a File-like object.
    }

    public static class MergeFile implements DustFile
    {
        private File root;
        private String rootstr;
        private int maxversion;

        private MergeFile(){}
        public static MergeFile CreateWithRoot(String absroot)
        {
            MergeFile r = new MergeFile();
            r.root = new File(absroot);
            r.rootstr = r.root.getAbsolutePath();
            r.UpdateInfo();
            return r;
        }
        public int GetMaxVersion(){return maxversion;}
        public void UpdateInfo()
        {
            //find the highest version number.
            maxversion = -1;
            for(File child: root.listFiles())
            {
                if(child.exists() && child.isDirectory())
                {
                    try{
                        int curver = Integer.parseInt(child.getName());
                        if(curver>maxversion) maxversion = curver;
                    }catch(java.lang.NumberFormatException e){
                        //not a numbered folder
                    }
                }
            }
            if(maxversion==-1) m.throwUncaughtException("Expected numbered subfoler in: "+root.getAbsolutePath());

            //verify that they all exist
            for(int i=0;i<=maxversion;i++)
            {
                String verpath = root.getAbsolutePath()+"/"+Integer.toString(i);
                File verfolder = new File(verpath);
                if(!verfolder.exists()) m.throwUncaughtException("Folder was expected to exist: "+verpath);
                if(!verfolder.isDirectory()) m.throwUncaughtException("This should be a folder: "+verpath);
            }

        }
        //public MergeFile(String relpath)
        //{
        //}
        public void WriteFileToHighestVersion(String relpath, byte[] contents, boolean createdirs, boolean throwexception)
        {
            String fullpath = GetPathToVersion(maxversion)+"/"+relpath;
            FileUtils.WriteFile(fullpath, contents, createdirs, throwexception);
        }
        private String GetPathToVersion(int version)
        {
            return rootstr+"/"+Integer.toString(version)+"/";
        }
        public void CopyFileIntoHighestVersion(String fromAbsFilename, String relpath, boolean overwrite, boolean createdirs, boolean throwexception)
        {
            String fullpath = GetPathToVersion(maxversion)+"/"+relpath;
            File fullpath2 = new File(fullpath);
            FileUtils.CopyFile(fromAbsFilename, fullpath, overwrite, createdirs, throwexception);

            //set modtime to match original.  I think this is the only way to do this in java.
            FileUtils.CopyModTime(fromAbsFilename,fullpath);
            //long modtime = fullpath2.lastModified();
            //SetModificationTime(relpath, modtime);
        }
        public void SetModificationTime(String relpath, long modtime)
        {
            String abspath = GetAbsPath(relpath);
            File abspath2 = new File(abspath);
            abspath2.setLastModified(modtime);
        }
        public String GetAbsPath(String relpath)
        {
            //finds the highest versioned example.
            for(int i=maxversion;i>=0;i--)
            {
                String testpath = GetPathToVersion(i)+"/"+relpath;
                File testpath2 = new File(testpath);
                if(testpath2.exists()) return testpath2.getAbsolutePath();
            }

            //otherwise the file doesn't exist in any version.
            return null;
        }
        public LinkedHashSet<String> listChildrenRelpaths(String relpath) //e.g. "/dat/"
        {
            LinkedHashSet<String> r = new LinkedHashSet();

            //get children from all versions together in one set
            for(int i=maxversion;i>=0;i--)
            {
                String curverFolder = GetPathToVersion(i)+"/"+relpath;
                File curverFolder2 = new File(curverFolder);
                if(curverFolder2.exists())
                {
                    for(File child: curverFolder2.listFiles())
                    {
                        r.add(relpath+"/"+child.getName());
                    }
                }
            }

            return r;
        }
        public boolean isFile(String relpath)
        {
            String abspath = GetAbsPath(relpath);
            if(abspath==null) return false;
            File abspath2 = new File(abspath);
            return abspath2.isFile();
        }
        public boolean exists(String relpath)
        {
            String abspath = GetAbsPath(relpath);
            if(abspath==null) return false;
            else return true;
        }
        public boolean isDirectory(String relpath)
        {
            String abspath = GetAbsPath(relpath);
            if(abspath==null) return false;
            File abspath2 = new File(abspath);
            return abspath2.isDirectory();
        }
        public String MergeAllVersions()
        {
            //this relies on moddates/filesizes as ensuring a file has not changed.

            //let's go through all the ones currently in the merged folder, and remove ones that are in no version.
            cleanMerged("/");

            merge("/");

            return this.getMergedAbsPath("/");
        }
        private void merge(String relpath)
        {
            if(isDirectory(relpath))
            {
                LinkedHashSet<String> children = this.listChildrenRelpaths(relpath);
                for(String childRelpath: children)
                {
                    merge(childRelpath);
                }
            }
            else if(isFile(relpath))
            {
                //get most recent version's info
                String abspath = GetAbsPath(relpath);
                File abspath2 = new File(abspath);
                long filesize = abspath2.length();
                long moddate = abspath2.lastModified();

                //get current merged info
                String absMergedPath = getMergedAbsPath(relpath);
                File absMergedPath2 = new File(absMergedPath);
                if( !absMergedPath2.exists() || absMergedPath2.length()!=filesize || absMergedPath2.lastModified()!=moddate )
                {
                    //it's changed, so copy the new one over
                    FileUtils.CopyFile(abspath, absMergedPath, true, true, true);
                    FileUtils.CopyModTime(abspath,absMergedPath);
                }
            }
            else
            {
                m.throwUncaughtException("unexpected");
            }
            //String fullpath = rootstr+"/merged/"+relpath;
            //File fullpath2 = new File(fullpath);
        }
        private String getMergedAbsPath(String relpath)
        {
            return rootstr+"/merged/"+relpath;
        }
        private void cleanMerged(String relpath)
        {
            String fullpath = getMergedAbsPath(relpath);
            File fullpath2 = new File(fullpath);
            if(fullpath2.isDirectory())
            {
                for(File child: fullpath2.listFiles())
                {
                    cleanMerged(relpath+"/"+child.getName());
                }
            }
            else if(fullpath2.isFile())
            {
                String abspath = GetAbsPath(relpath);
                if(abspath==null)
                {
                    //no version of it, so remove it.
                    FileUtils.DeleteFile(fullpath,true);
                }
            }
        }
    }

    /*public static void DoAllWork(String archivefolder, String shardfolder, String tempfolder)
    {
        final boolean testing = false;

        //set the temp folder.
        if(tempfolder==null) tempfolder = archivefolder+"/temp/";

        //update the xml file for UAM.
        ArrayList<String> newarchives;
        if(!testing)
        {
            newarchives = uam.UamConfigNew.generateStatusFile(archivefolder);
        }
        else
        {
            m.msg("disabled xml update, change before release!");
            newarchives = new ArrayList();
            newarchives.add("H:/a/uam/ages/Vogokh_Oglahn--2010Aug20.7z");
        }

        if(!shardfolder.equals(""))
        {
            final String[] manualages = {"offlineki",};

            m.msg();
            m.msg("Preparing shard files...");
            for(String archive: newarchives)
            {
                //find if this is an Age we wish to do manually.
                File archf = new File(archive);
                boolean ignore = false;
                for(String manualage: manualages)
                {
                    if(archf.getName().startsWith(manualage+"--")) ignore = true;
                }
                if(!ignore)
                {
                    DoShardAgeWork(archive, shardfolder, tempfolder);
                }
            }
        }

    }*/
    public static boolean DoShardAgeWork(String archivestr, MergeFile shardfolder, String tempfolder)
    {
        File archive = new File(archivestr);
        String name = archive.getName();
        m.msg("Processing ", name);

        //extract the archive to the tempfolder
        String extractpath = tempfolder+"/"+name;
        FileUtils.CreateFolder(extractpath);
        boolean success = shared.sevenzip.extract(archivestr, extractpath);
        if(!success) m.throwUncaughtException("error extracting archive.");

        return PrepareFileForShard(extractpath, "/", shardfolder);
    }
    private static boolean PrepareFileForShard(String basepath, String relfilename, MergeFile shardfolder)
    {

        File f = new File(basepath+relfilename);
        String name = f.getName();
        if(f.isDirectory())
        {
            boolean success = true;
            for(File child: f.listFiles())
            {
                boolean success2 = PrepareFileForShard(basepath, relfilename+"/"+child.getName(), shardfolder);
                if(!success2) success = false;
            }
            return success;
        }
        else if(f.isFile())
        {
            //regular files
            if(name.endsWith(".prp")||name.endsWith(".age")||name.endsWith(".fni")||name.endsWith(".sum")||name.endsWith(".ogg")||name.endsWith(".p2f")||name.endsWith(".bik"))
            {
                //FileUtils.CopyFile(f.getAbsolutePath(), shardfolder+relfilename, true, true, true);
                shardfolder.CopyFileIntoHighestVersion(f.getAbsolutePath(), relfilename, true, true, true);
                return true;
            }

            //misc files
            if(name.endsWith(".txt")||name.endsWith(".jpg"))
            {
                //FileUtils.CopyFile(f.getAbsolutePath(), shardfolder+relfilename, true, true, true);
                shardfolder.CopyFileIntoHighestVersion(f.getAbsolutePath(), relfilename, true, true, true);
                return true;
            }

            //sdl
            if(name.endsWith(".sdl"))
            {
                //FileUtils.CopyFile(f.getAbsolutePath(), shardfolder+relfilename, true, true, true);
                shardfolder.CopyFileIntoHighestVersion(f.getAbsolutePath(), relfilename, true, true, true);
                return true;
            }

            //pak
            if(name.endsWith(".pak"))
            {
                //patch it
                //onliner.Python.PatchPak(byte[] pakdata);
                byte[] patched = onliner.Python.PatchPak(f.getAbsolutePath());
                if(patched!=null)
                {
                    //FileUtils.WriteFile(shardfolder+relfilename, patched, true, true);
                    shardfolder.WriteFileToHighestVersion(relfilename, patched, true, true);
                }
                else
                {
                    //unchanged so just copy
                    shardfolder.CopyFileIntoHighestVersion(f.getAbsolutePath(), relfilename, true, true, true);
                }

                //check for remaining problems
                String absPathToPakFile = shardfolder.GetAbsPath(relfilename);
                //onliner.Python.CheckForProblems(shardfolder+relfilename);
                boolean success2 = onliner.Python.CheckForProblems(absPathToPakFile);

                return success2;
            }

            //otherwise it was unhandled
            m.err("Unhandled file: ", relfilename);
            return false;
        }
        else //not file nor folder?
        {
            throw new shared.uncaughtexception("Neither file nor folder?: "+relfilename);
        }
    }

    public static class OfflineKIInfFile
    {
        ArrayList<Line> Lines = new ArrayList();
        ArrayList<String> RawLines = new ArrayList();

        public OfflineKIInfFile(String data)
        {
            data = data.replace("\r\n", "\n");
            String[] lines = data.split("\n");
            for(int i=0;i<lines.length;i++)
            {
                String line = lines[i];
                RawLines.add(line);
                String[] lineparts = line.split(":");
                if(lineparts.length>1) //test if a non-comment line
                {
                    Line _line = new Line();
                    _line.type = lineparts[0];
                    String[] infoparts = lineparts[1].split(",");
                    _line.filename = infoparts[0];
                    _line.propername = infoparts[1];
                    _line.linenum = i;
                    Lines.add(_line);
                }
            }
        }
        public String ToRawString()
        {
            StringBuilder r = new StringBuilder();
            for(String line: RawLines)
            {
                r.append(line);
                r.append("\n");
            }
            return r.toString();
        }
        public Set<String> GetAllAgeFilenames()
        {
            HashSet<String> r = new HashSet<String>();
            for(Line line: Lines) r.add(line.filename);
            return r;
        }
        public void AddRestorationLinkLineAlphabetically(String AgeFilename, String AgePropername)
        {
            boolean posfound = false;
            Line newline = new Line();
            newline.type = "restorationlink";
            newline.filename = AgeFilename;
            newline.propername = AgePropername;
            //find position
            for(int i=0;i<Lines.size();i++)
            {
                Line line = Lines.get(i);
                //find first propername bigger than this one
                if(!posfound)
                {
                    if(!line.type.equals("restorationlink")) continue;
                    if(line.propername.toLowerCase().compareTo(AgePropername.toLowerCase())>0)
                    {
                        //we'll take this i, and shift the rest.
                        newline.linenum = i;
                        posfound = true;
                    }
                }
                //shift later lines
                if(posfound)
                {
                    line.linenum += 1; 
                }
            }
            
            //insert new line
            RawLines.add(newline.linenum, newline.toRawLine());
        }

        public static class Line
        {
            String type; //e.g. restorationlink, link
            String filename;
            String propername;
            int linenum;
            
            public String toRawLine()
            {
                return type + ":" + filename + "," + propername;
            }
        }
    }
    
    public static String GetVersionFromFilename(String filename)
    {
        return filename.substring(filename.indexOf("--")+2, filename.length()-".7z".length());
    }
    
    /*
     * Tries to get the date from the version, returning null if failing.
     */
    public static java.util.Date GetDateFromVersion(String ver)
    {
        try
        {
            int leftbracket = ver.indexOf("(");
            if(leftbracket!=-1) ver = ver.substring(0,leftbracket);
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMMd");
            java.util.Date d = df.parse(ver);
            return d;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}

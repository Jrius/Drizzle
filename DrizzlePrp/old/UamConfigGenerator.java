/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

import java.io.File;
import shared.m;
import shared.b;
import java.util.Vector;
import java.util.ArrayDeque;

/*public class UamConfigGenerator
{
    
    private static String filenameToAgename(String filename)
    {
        if(false) return "";
        else if(filename.equals("Ahra Pahts v08E(with 232 and updated python)")) return "Pahts";
        else if(filename.equals("Andys Nexus")) return "Andy_Nexus";
        else if(filename.equals("Atlantis Outpost")) return "outpost";
        else if(filename.equals("Bowling Age")) return "bowling";
        else if(filename.equals("Boxage")) return "BoxAge";
        else if(filename.equals("Camp Bravo Daytime")) return "CampBravoDayTime";
        else if(filename.equals("Camp Bravo Nighttime")) return "Campbravo";
        else if(filename.equals("DkSkyClub")) return "DKSkyClub";
        else if(filename.equals("Dragons Tooth")) return "Dragons_tooth";
        else if(filename.equals("Eder Bahvahnin")) return "Bahvahnin";
        else if(filename.equals("Eder Licinius")) return "EderLicinius";
        else if(filename.equals("Eder Rilteh Inaltahv")) return "ederriltehinaltahv";
        else if(filename.equals("Gairdin Grianmhar")) return "Gairdin";
        else if(filename.equals("Galamay")) return "galamay";
        else if(filename.equals("Janga im Sommer")) return "Janga";
        else if(filename.equals("Janga im Winter")) return "Janga";
        else if(filename.equals("Jonae final")) return "Jonae";
        else if(filename.equals("Jonae Halloween final")) return "Jonae";
        else if(filename.equals("Jonae Halloween")) return "Jonae";
        else if(filename.equals("JonaeHood final")) return "JonaeHood";
        else if(filename.equals("Katos Lab")) return "katoslab";
        else if(filename.equals("Setal Gahmarin")) return "SetalGahmarin";
        else if(filename.equals("Tahm Hehvo")) return "Tahmhehvo";
        else if(filename.equals("TaklaMakan final")) return "TaklaMakan";
        else if(filename.equals("Terrati Lab")) return "ter";
        else if(filename.equals("TINA Testing Age")) return "TINA_Testing";
        else if(filename.equals("Trebivdil")) return "trebivdil";
        else if(filename.equals("Tsoidahl Sheegahtee")) return "jamey_study";
        else if(filename.equals("Vaiskor2")) return "vas2";
        else if(filename.equals("Zephyr Cove day")) return "Zephyr_Cove";
        else if(filename.equals("Zephyr Cove night")) return "Zephyr_Cove";
        else if(filename.equals("SuitUp")) return "suitup";
        else if(filename.equals("The Writers Niche")) return "WNiche";
        //else if(filename.equals("")) return "";
        //else if(filename.equals("")) return "";
        //else if(filename.equals("")) return "";
        //else return filename.substring(0,filename.length()-4);
        else return filename;
    }
    public static String filenameToVersionname(String filename, String version)
    {
        //fix alternate versions.
        if(false) return "";
        else if(filename.equals("Janga im Winter")) return version+"(Winter)";
        else if(filename.equals("Jonae Halloween final")) return version+"(Halloween)";
        else if(filename.equals("Jonae Halloween")) return version+"(Halloween)";
        else if(filename.equals("Zephyr Cove night")) return version+"(Night)";
        else return version;
    }
    public static void generateStatusFile(String folderWith7zs)
    {
        String sep = uam.Uam.versionSep;
        //String sep = "--";
        String suffix = ".7z";
        //StringBuilder result = new StringBuilder();
        UamConfigData data = new UamConfigData();
        data.comments.add("age.version.num can be a string, but it must be able to be part of a filename.");
        data.comments.add("The name of the file on the server isn't used; it's just renamed anyway.");
        data.comments.add("The aequalsatransposeequalsainverse tag is just used to mark the end, so we know we got the whole file.");
        data.welcome = "Welcome to the UruAgeManager sub-project of Drizzle!";
        UamConfig config;
        try{
            config = new UamConfig(new java.io.FileInputStream(folderWith7zs+"/"+uam.Uam.statusFilename));
        }catch(Exception e){
            m.err("Error reading config file.");
            return;
        }
        data.loadFromUamConfig(config);
        for(File f: filesearcher.search.getAllFilesWithExtension(folderWith7zs, false, suffix))
        {
            String filename = f.getName();
            //m.msg("Handling "+filename);
            int ind = filename.indexOf(sep);
            if(ind==-1)
            {
                m.msg("Skipping "+filename+" because it contains no version in the name.");
                break;
            }
            String name = filename.substring(0,ind);
            String agename = filenameToAgename(name);
            String versionstr = filename.substring(ind+sep.length(),filename.length()-suffix.length());
            versionstr = filenameToVersionname(name,versionstr);
            //byte[] hash = shared.CryptHashes.GetWhirlpool(f.getAbsolutePath());
            byte[] hash = shared.CryptHashes.GetHash(f.getAbsolutePath(), shared.CryptHashes.Hashtype.sha1);
            String hashstr = b.BytesToHexString(hash);
            //String server = "http://dustin.homeunix.net:88/uam/ages/";
            String server = "http://www.the-ancient-city.de/uru-ages/";
            String mirurl = server+filename;
            
            UamConfigData.Age age = data.getAgeOrCreate(agename);
            //age.deletable = "true";
            //if we haven't already created this age, add the default name.
            if(age.propername.equals(""))
            {
                age.propername = age.filename;
            }
            
            UamConfigData.Age.Version version = age.getVersionOrCreate(versionstr,true);
            
            //If we haven't already created this version, add the default mirror.
            if(version.sha1.equals(""))
            {
                UamConfigData.Age.Version.Mirror mirror = version.getMirrorOrCreate(mirurl);
            }
            
            //version.whirlpool = hashstr;
            version.sha1 = hashstr;
            version.archive = "7z";
            
            //result.append("<age>\n");
            //result.append("    <filename>"+agename+"</filename>\n");
            //result.append("    <version>\n");
            //result.append("        <num>"+versionstr+"</num>\n");
            //result.append("        <whirlpool>"+hashstr+"</whirlpool>\n");
            //result.append("        <mirror>\n");
            //result.append("            <url>"+mirurl+"</url>\n");
            //result.append("        </mirror>\n");
            //result.append("    </version>\n");
            //result.append("</age>\n");
            //result.append("\n");
        }
        //String finalresult = result.toString();
        String finalresult = data.generateXml();
        m.msg(finalresult);
    }
    public static class UamConfigData
    {
        Vector<String> comments = new Vector();
        String welcome = "";
        Vector<Age> ages = new Vector();
        
        public void loadFromUamConfig(UamConfig config)
        {
            welcome = config.getWelcomeMessage();
            for(String agename: config.getAllAgeNames())
            {
                Age age = this.getAgeOrCreate(agename);
                age.deletable = config.getDeletable(agename);
                age.info = config.getAgeInfo(agename);
                age.propername = config.getAgeProperName(agename);
                for(String version: config.getAllVersionsOfAge(agename))
                {
                    Age.Version ver = age.getVersionOrCreate(version,false);
                    ver.archive = config.getArchiveType(agename, version);
                    ver.sha1 = config.getSha1(agename, version);
                    for(String mirurl: config.getAllUrlsOfAgeVersion(agename, version))
                    {
                        Age.Version.Mirror mir = ver.getMirrorOrCreate(mirurl);
                    }
                }
            }
        }
        
        public String generateXml()
        {
            StringBuilder result = new StringBuilder();
            generateXml(result);
            return result.toString();
        }
        
        void generateXml(StringBuilder s)
        {
            s.append("<uam>\n");
            for(String comment: comments) s.append("\t<!-- "+comment+" -->\n");
            s.append("\t<welcome>"+welcome+"</welcome>\n");
            for(Age age: ages) age.generateXml(s);
            s.append("\t<aequalsatransposeequalsainverse />\n");
            s.append("</uam>\n");
        }
        
        public Age getAgeOrCreate(String filename)
        {
            for(Age age: ages)
            {
                if(age.filename.equals(filename)) return age;
            }
            Age age = new Age();
            age.filename = filename;
            ages.add(age);
            return age;
        }
        
        public static class Age
        {
            String filename = "";
            String deletable = "true";
            String info = "";
            String propername = "";
            ArrayDeque<Version> versions = new ArrayDeque();
            
            void generateXml(StringBuilder s)
            {
                s.append("\t<age>\n");
                s.append("\t\t<filename>"+filename+"</filename>\n");
                s.append("\t\t<name>"+propername+"</name>\n");
                s.append("\t\t<deletable>"+deletable+"</deletable>\n");
                s.append("\t\t<info>"+info+"</info>\n");
                for(Version version: versions) version.generateXml(s);
                s.append("\t</age>\n");
                //s.append("\n");
            }
            public Version getVersionOrCreate(String name, boolean prepend)
            {
                for(Version version: versions)
                {
                    if(version.name.equals(name)) return version;
                }
                Version version = new Version();
                version.name = name;
                if(prepend) versions.addFirst(version);
                else versions.addLast(version);
                return version;
            }
            
            public static class Version
            {
                String name = "";
                String archive = "";
                //String whirlpool = "";
                String sha1 = "";
                Vector<Mirror> mirrors = new Vector();
                
                void generateXml(StringBuilder s)
                {
                    s.append("\t\t<version>\n");
                    s.append("\t\t\t<name>"+name+"</name>\n");
                    s.append("\t\t\t<archive>"+archive+"</archive>\n");
                    //s.append("            <whirlpool>"+whirlpool+"</whirlpool>\n");
                    s.append("\t\t\t<sha1>"+sha1+"</sha1>\n");
                    for(Mirror mirror: mirrors) mirror.generateXml(s);
                    s.append("\t\t</version>\n");
                }
                public Mirror getMirrorOrCreate(String url)
                {
                    for(Mirror mirror: mirrors)
                    {
                        if(mirror.url.equals(url)) return mirror;
                    }
                    Mirror mirror = new Mirror();
                    mirror.url = url;
                    mirrors.add(mirror);
                    return mirror;
                }
                public static class Mirror
                {
                    String url = "";
                    void generateXml(StringBuilder s)
                    {
                        s.append("\t\t\t<mirror>\n");
                        s.append("\t\t\t\t<url>"+url+"</url>\n");
                        s.append("\t\t\t</mirror>\n");
                    }
                }
            }
        }
    }
}*/

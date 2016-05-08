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
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.InputSource;
import java.io.InputStream;
import org.xml.sax.XMLReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.Vector;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import shared.m;
import java.util.HashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import org.w3c.dom.Comment;

public class UamConfigNew
{
    static final boolean doValidate = false;

    Document doc;
    Element root;
    XPath xpath;
    
    public UamConfigData data;
    
    public UamConfigNew(InputStream in)
    {
        DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
        docfact.setValidating(doValidate);
        DocumentBuilder builder;
        try
        {
            builder = docfact.newDocumentBuilder();
            builder.setErrorHandler(new xmlErrorHandler());
        }
        catch(ParserConfigurationException e)
        {
            throw new ConfigErrorException("Error creating parser.");
        }
        try
        {
            doc = builder.parse(in);
        }
        catch(SAXException e)
        {
            throw new ConfigErrorException("Error parsing config. (SAXException)  The XML is probably invalid.");
        }
        catch(IOException e)
        {
            throw new ConfigErrorException("Error parsing config. (IOException)  The XML is probably invalid.");
        }
        root = doc.getDocumentElement();
        
        XPathFactory xpathfact = XPathFactory.newInstance();
        xpath = xpathfact.newXPath();
        
        data = new UamConfigData(root);
        //data.loadFromNode(root);
        
        //make sure we got the end of the file...
        //boolean hasend = this.hasItem("/uam/aequalsatransposeequalsainverse");
        if(!data.aequalsatransposeequalsainverse)
        {
            throw new ConfigErrorException("Config file doesn't seem to have the correct end; it's probably corrupt.");
        }
    }
    
    public static class xmlErrorHandler implements ErrorHandler
    {
	public void error(SAXParseException exception)
        {
            //recoverable.
            throw new ConfigErrorException("Error validating. Server's file may have a problem.");
        }
        public void fatalError(SAXParseException exception)
        {
            //non-recoverable.
            throw new ConfigErrorException("Error validating; Server's file is probably corrupt.");
        }
        public void warning(SAXParseException exception)
        {
            //warning.
            m.warn(exception.getMessage());
        }
    }
    public UamConfigData.Age getAge(String agename)
    {
        return data.getAge(agename);
    }
    public Vector<UamConfigData.Age> getAllAges()
    {
        return data.ages;
    }
    public Vector<UamConfigData.Age.Version> getAllVersions(String age)
    {
        UamConfigData.Age ageobj = data.getAge(age);
        if(ageobj==null)
        {
            return new Vector();
        }
        else
        {
            return ageobj.versions;
        }
    }
    public Vector<UamConfigData.Age.Version.Mirror> getAllMirrors(String age, String ver)
    {
        UamConfigData.Age ageobj = data.getAge(age);
        if(ageobj==null)
        {
            return new Vector();
        }
        else
        {
            UamConfigData.Age.Version verobj = ageobj.getVersion(ver);
            if(verobj==null)
            {
                return new Vector();
            }
            else
            {
                return verobj.mirrors;
            }
        }
    }
    public Vector<String> getDels(String agename)
    {
        return data.getAge(agename).dels;
    }
    public String getArchiveType(String agename, String version)
    {
        return data.getAge(agename).getVersion(version).archive;
    }
    public boolean getDeletable(String agename)
    {
        String result = data.getAge(agename).deletable;
        return result.equals("true");
    }
    public String getAgeInfo(String agename)
    {
        String curlang = shared.translation.getCurLanguage();
        String result = data.getAge(agename).infos.get(curlang);
        if(result==null) result = data.getAge(agename).info;
        return result;
    }
    public String getWelcomeMessage()
    {
        String curlang = shared.translation.getCurLanguage();
        String result = data.welcomes.get(curlang);
        if(result==null) result = data.welcome;
        return result;
    }
    public int getDrizzleVersion()
    {
        int result = Integer.parseInt(data.drizzle);
        return result;
    }
    public String getAgeProperName(String agename)
    {
        return data.getAge(agename).propername;
    }
    public String getSha1(String agename, String version)
    {
        return data.getAge(agename).getVersion(version).sha1;
    }
    
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

    public static class ArchivesInfo
    {
        ArrayList<String> newFilenames;
        //String newStatusFileContents;
        UamConfigNew config;
    }

    //besides updating the xml file, it returns a list of new archives.
    public static ArchivesInfo generateStatusFile(String folderWith7zs, gui.Interactor interactor)
    {
        String sep = uam.Uam.versionSep;
        //String sep = "--";
        String suffix = ".7z";
        //StringBuilder result = new StringBuilder();
        /*UamConfigData data = new UamConfigData();
        data.comments.add("age.version.num can be a string, but it must be able to be part of a filename.");
        data.comments.add("The name of the file on the server isn't used; it's just renamed anyway.");
        data.comments.add("The aequalsatransposeequalsainverse tag is just used to mark the end, so we know we got the whole file.");
        data.welcome = "Welcome to the UruAgeManager sub-project of Drizzle!";*/
        ArrayList<String> newarchives = new ArrayList();
        UamConfigNew config;
        String statusfile = folderWith7zs+"/"+uam.Uam.statusFilename;
        try{
            config = new UamConfigNew(new java.io.FileInputStream(statusfile));
        }catch(Exception e){
            //m.err("Error reading config file.");
            //return newarchives;
            throw new shared.uncaughtexception("Error reading config file.");
        }
        //UamConfigData data = new UamConfigData
        //data.loadFromUamConfig(config);

        //do new status message
        /*final boolean changemessage = true;
        if(changemessage)
        {
            String curmessage = config.data.welcome;
            String newmessage = interactor.AskQuestion("Do you want a new status message? (blank to keep the old one)");
            if(!newmessage.equals(""))
            {
                config.data.comments.add(curmessage);
                config.data.welcome = newmessage;
            }
        }*/

        //set latest drizzle version.
        //if(gui.Version.version > Integer.parseInt(config.data.drizzle))
        //{
        //    config.data.drizzle = Integer.toString(gui.Version.version);
        //}

        for(File f: filesearcher.search.getAllFilesWithExtension(folderWith7zs, false, suffix))
        {
            String filename = f.getName();
            //m.msg("Handling "+filename);
            int ind = filename.indexOf(sep);
            if(ind==-1)
            {
                m.msg("Skipping ",filename," because it contains no version in the name.");
                break;
            }
            String name = filename.substring(0,ind);
            String agename = filenameToAgename(name);
            String versionstr = filename.substring(ind+sep.length(),filename.length()-suffix.length());
            versionstr = filenameToVersionname(name,versionstr);
            //byte[] hash = shared.CryptHashes.GetWhirlpool(f.getAbsolutePath());
            //String server = "http://dustin.homeunix.net:88/uam/ages/";
            //String server = "http://www.the-ancient-city.de/uru-ages/";
            //String server = "http://dusty.homeunix.net/uru-ages/";
            String server = "http://myst.dustbird.net/uru-ages/";
            String mirurl = server+filename;
            
            UamConfigData.Age age = config.data.getAgeOrCreate(agename);
            if(age.filename==null)
            {
                m.msg("New age: ",agename);

                age.filename = agename;
                //age.propername = age.filename;
                age.propername = interactor.AskQuestionDefault("There is a new Age.  What is the proper name for "+agename+"? (blank for default)", age.filename);
                age.deletable = "true";
                age.info = "";
            }
            
            UamConfigData.Age.Version version = age.getVersionOrCreate(versionstr,true);
            if(version.name==null)
            {
                m.msg("New version: ",agename,": ",versionstr);

                version.name = versionstr;
                version.archive = "7z";
                
                //calculate hash:
                byte[] hash = shared.CryptHashes.GetHash(f.getAbsolutePath(), shared.CryptHashes.Hashtype.sha1);
                String hashstr = b.BytesToHexString(hash);
                version.sha1 = hashstr;
                
                //add mirror:
                UamConfigData.Age.Version.Mirror mirror = version.getMirrorOrCreate(mirurl);
                mirror.url = mirurl;

                //add to list
                newarchives.add(f.getAbsolutePath());
            }
        }
        
        //sort by minver and proper name:
        //config.data.sort();

        //alphabetize everything past -Ages but before -Whatever
        config.data.alphabetize("-Ages");
        
        //String finalresult = config.data.generateXml();
        //m.msg(finalresult);
        //shared.FileUtils.CopyFile(statusfile, statusfile+shared.DateTimeUtils.GetSortableCurrentDate()+".xml", false, false);
        //shared.FileUtils.WriteFile(statusfile, b.StringToBytes(finalresult));
        
        //m.status("Finished creating new status file!");
        ArchivesInfo r = new ArchivesInfo();
        r.newFilenames = newarchives;
        //r.newStatusFileContents = finalresult;
        r.config = config;
        return r;
    }
    public static class UamConfigData
    {
        public Vector<String> comments = new Vector();
        public String welcome = "";
        public HashMap<String,String> welcomes = new HashMap();
        public String drizzle = "1";
        public Vector<Age> ages = new Vector();
        public boolean aequalsatransposeequalsainverse = false;
        
        public UamConfigData(){}
        
        public UamConfigData(Element n)
        {
            String s = n.getTagName();
            if(!s.equals("uam")) throw new shared.uncaughtexception("Doesn't have uam node");
            
            //NodeList ns = n.getChildNodes();
            //int nodecount = ns.getLength();
            //Node child = n.getFirstChild();
            //for(int i=0;i<nodecount;i++)
            for(Node child=n.getFirstChild();child!=null;child=child.getNextSibling())
            {
                //Node child = ns.item(i);
                switch(child.getNodeType())
                {
                    case Node.COMMENT_NODE:
                        Comment c = (Comment)child;
                        String comment = c.getTextContent();
                        comments.add(comment);
                        break;
                    case Node.ELEMENT_NODE:
                        Element e = (Element)child;
                        String tag = e.getTagName();
                        if(tag.equals("welcome")) welcome = e.getTextContent();
                        else if(tag.startsWith("welcome--")) welcomes.put(tag.substring("welcome--".length()), e.getTextContent());
                        else if(tag.equals("drizzle")) drizzle = e.getTextContent();
                        else if(tag.equals("age")) ages.add(new Age(e));
                        else if(tag.equals("age2")) ages.add(new Age(e));
                        else if(tag.equals("aequalsatransposeequalsainverse")) aequalsatransposeequalsainverse = true;
                        break;
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
            s.append("<?xml version='1.0' encoding='ISO-8859-1'?>\n");
            s.append("<?xml-stylesheet type='text/xsl' href='uam.status.xsl'?>\n");
            s.append("<uam>\n");
            for(String comment: comments) s.append("\t<!--"+comment+"-->\n");
            s.append("\t<welcome>"+welcome+"</welcome>\n");
            for(String lang: welcomes.keySet()) s.append("\t<welcome--"+lang+">"+welcomes.get(lang)+"</welcome--"+lang+">\n");
            s.append("\t<drizzle>"+drizzle+"</drizzle>\n");
            for(Age age: ages) age.generateXml(s);
            s.append("\t<aequalsatransposeequalsainverse />\n");
            s.append("</uam>\n");
        }
        
        public Age getAge(String filename)
        {
            for(Age age: ages)
            {
                if(age.filename.equals(filename)) return age;
            }
            return null;
        }
        public Age getAgeOrCreate(String filename)
        {
            Age age = getAge(filename);
            if(age==null)
            {
                age = new Age();
                //age.filename = filename;
                ages.add(age);
            }
            return age;
        }
        
        public void sort()
        {
            java.util.Collections.sort(ages, new java.util.Comparator() {
                public int compare(Object o1, Object o2) {
                    Age a1 = (Age)o1;
                    Age a2 = (Age)o2;
                    if(a1.minver>=16 && a2.minver<16) return -1;
                    return a1.propername.compareTo(a2.propername);
                }
            });
        }

        public void alphabetize(String startage) //I.e. should be passed "-Ages"
        {
            //find start and end
            boolean foundstart = false;
            int start = -1;
            int end = -1;
            for(int i=0;i<ages.size();i++)
            {
                Age age = ages.get(i);

                if(foundstart)
                {
                    if(age.filename.startsWith("-"))
                    {
                        end = i;
                        break; //done
                    }


                }

                if(age.filename.equals(startage))
                {
                    foundstart = true;
                    start = i+1;
                }
            }

            if(start==-1) m.throwUncaughtException("unexpected");
            if(end==-1) end = ages.size();

            alphabetize(start,end);
        }
        public void alphabetize(int start, int end)
        {
            java.util.List<Age> sublist = ages.subList(start, end);
            java.util.Collections.sort(sublist, new java.util.Comparator<Age>(){
                public int compare(Age a1, Age a2)
                {
                    //Should be lowercase and ignore apostrophes.
                    //String a1name = a1.propername.toLowerCase();
                    //String a2name = a2.propername.toLowerCase();
                    String a1name = a1.propername.toLowerCase().replace("'", "");
                    String a2name = a2.propername.toLowerCase().replace("'", "");
                    int r = a1name.compareTo(a2name);
                    return r;
                }
            });
        }

        public static class Age
        {
            public String filename = null;
            private String mainfile = null;
            public String deletable = null;
            public String info = null;
            public HashMap<String,String> infos = new HashMap();
            public String propername = null;
            public int minver = 0;
            //public ArrayDeque<Version> versions = new ArrayDeque();
            public Vector<String> dels = new Vector();
            public Vector<Version> versions = new Vector();
            
            public Age(){}
            
            public Age(Element n)
            {
                for(Node child=n.getFirstChild();child!=null;child=child.getNextSibling())
                {
                    switch(child.getNodeType())
                    {
                        case Node.ELEMENT_NODE:
                            Element e = (Element)child;
                            String tag = e.getTagName();
                            if(tag.equals("filename")) filename = e.getTextContent();
                            else if(tag.equals("deletable")) deletable = e.getTextContent();
                            else if(tag.equals("info")) info = e.getTextContent();
                            else if(tag.startsWith("info--")) infos.put(tag.substring("info--".length()), e.getTextContent());
                            else if(tag.equals("name")) propername = e.getTextContent();
                            else if(tag.equals("minver")) minver = Integer.parseInt(e.getTextContent());
                            else if(tag.equals("version")) versions.add(new Version(e));
                            else if(tag.equals("mainfile")) mainfile = e.getTextContent();
                            else if(tag.equals("del")) dels.add(e.getTextContent());
                            break;
                    }
                }
                
                if(minver>gui.Version.version)
                {
                    info = "(You need a newer version of Drizzle to install this Age.) " + info;
                    for(Version v: versions)
                    {
                        v.mirrors.clear();
                    }
                }
            }
            
            void generateXml(StringBuilder s)
            {
                String tag = (minver<16)?"age":"age2";
                s.append("\t<"+tag+">\n");
                s.append("\t\t<filename>"+filename+"</filename>\n");
                if(mainfile!=null) s.append("\t\t<mainfile>"+mainfile+"</mainfile>\n");
                s.append("\t\t<name>"+propername+"</name>\n");
                s.append("\t\t<deletable>"+deletable+"</deletable>\n");
                s.append("\t\t<info>"+info+"</info>\n");
                for(String lang: infos.keySet()) s.append("\t\t<info--"+lang+">"+infos.get(lang)+"</info--"+lang+">\n");
                if(minver!=0) s.append("\t\t<minver>"+Integer.toString(minver)+"</minver>\n");
                for(String del: dels) s.append("\t\t<del>"+del+"</del>\n");
                for(Version version: versions) version.generateXml(s);
                s.append("\t</"+tag+">\n");
                //s.append("\n");
            }
            public String getMainfile()
            {
                String result = mainfile;
                if(result==null) result = "dat/"+filename+".age";
                return result;
            }
            public Version getVersion(String name)
            {
                for(Version version: versions)
                {
                    if(version.name.equals(name)) return version;
                }
                return null;
            }
            public Version getVersionOrCreate(String name, boolean prepend)
            {
                Version version = getVersion(name);
                if(version==null)
                {
                    version = new Version();
                    //version.name = name;
                    if(prepend)
                    {
                        //versions.addFirst(version);
                        Vector<Version> newversions = new Vector();
                        newversions.add(version);
                        for(Version v: versions) newversions.add(v);
                        versions = newversions;
                    }
                    else
                    {
                        //versions.addLast(version);
                        versions.add(version);
                    }
                }
                return version;
            }
            
            public static class Version
            {
                public String name = null;
                public String archive = null;
                //public String whirlpool = "";
                public String sha1 = null;
                public Vector<Mirror> mirrors = new Vector();
                
                public Version(){}
                
                public Version(Element n)
                {
                    for(Node child=n.getFirstChild();child!=null;child=child.getNextSibling())
                    {
                        switch(child.getNodeType())
                        {
                            case Node.ELEMENT_NODE:
                                Element e = (Element)child;
                                String tag = e.getTagName();
                                if(tag.equals("name")) name = e.getTextContent();
                                else if(tag.equals("archive")) archive = e.getTextContent();
                                else if(tag.equals("sha1")) sha1 = e.getTextContent();
                                else if(tag.equals("mirror")) mirrors.add(new Mirror(e));
                                break;
                        }
                    }
                }
                
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
                public Mirror getMirror(String url)
                {
                    for(Mirror mirror: mirrors)
                    {
                        if(mirror.url.equals(url)) return mirror;
                    }
                    return null;
                }
                public Mirror getMirrorOrCreate(String url)
                {
                    Mirror mirror = getMirror(url);
                    if(mirror==null)
                    {
                        mirror = new Mirror();
                        //mirror.url = url;
                        mirrors.add(mirror);
                    }
                    return mirror;
                }
                public static class Mirror
                {
                    public String url = null;

                    public Mirror(){}
                    
                    public Mirror(Element n)
                    {
                        for(Node child=n.getFirstChild();child!=null;child=child.getNextSibling())
                        {
                            switch(child.getNodeType())
                            {
                                case Node.ELEMENT_NODE:
                                    Element e = (Element)child;
                                    String tag = e.getTagName();
                                    if(tag.equals("url")) url = e.getTextContent();
                                    break;
                            }
                        }
                    }
                    
                    
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
}

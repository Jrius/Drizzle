/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.io.File;
import java.util.Vector;
import uru.vault.*;
import shared.*;

public class vaultAutomation
{
    public static String xmlize(String s)
    {
        String result = s==null?"":s;
        result = result.replace("&", "&amp;");
        result = result.replace("<", "&lt;");
        result = result.replace(">", "&gt;");
        result = result.replace("\"", "&quot;");
        result = result.replace("'", "&apos;");
        return result;
    }
    private static void saveFile(String outfolder, StringBuilder xml)
    {
        String xmls = xml.toString();
        byte[] data = b.StringToBytes(xmls);
        String sha1 = b.BytesToHexString(shared.CryptHashes.GetHash(data, shared.CryptHashes.Hashtype.sha1));
        String filename = sha1+".mem.xml";
        FileUtils.WriteFile(outfolder+"/"+filename, data);
    }
    public static void saveImages(String infolder, String outfolder)
    {
        //detect vault type:
        String vaultDotDat = infolder+"/vault.dat";
        Nodes nodes;
        if(FileUtils.Exists(vaultDotDat))
        {
            nodes = Nodes.createFromNodeVector(uru.vault.vaultdatfile.createFromFilename(vaultDotDat).nodes);
        }
        else
        {
            if(FileUtils.FindAllFiles(infolder, ".v", false).size()!=0)
            {
                nodes = readFolder(infolder);
            }
            else
            {
                m.err("The input folder must have either 'vault.dat' or some number of '.v' files.");
                return;
            }
        }

        File of = new File(outfolder);
        if(!of.exists())
        {
            m.err("You must select an output folder that already exists.");
            return;
        }
        else
        {
            if(!of.isDirectory())
            {
                m.err("You must select an Output folder.");
                return;
            }
        }

        //nodes.sortByCreationDate();
        m.status("Getting data...");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
        xml.append("<?xml-stylesheet type='text/xsl' href='memories.xsl'?>");
        xml.append("<memories>");

        java.util.HashSet<Long> dates = new java.util.HashSet();
        
        for(Node n: nodes.nodes)
        {
            Timestamp ts = n.age_time;

            if(n.type==nodetype.ImageNode)
            {
                byte[] data = n.ImageNode_GetImageData();
                String sha1 = b.BytesToHexString(shared.CryptHashes.GetHash(data, shared.CryptHashes.Hashtype.sha1));
                String filename = sha1+".jpg";
                FileUtils.WriteFile(outfolder+"/"+filename, data);

                StringBuilder xml2 = new StringBuilder();
                xml2.append("<imagenode>");
                xml2.append("<creationtime>"+xmlize(n.crt_time.toLongString())+"</creationtime>");
                xml2.append("<agetime>"+xmlize(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+xmlize(n.mod_time.toLongString())+"</modtime>");
                xml2.append("<owner>"+xmlize(nodes.getAvatarName(n.owner))+"</owner>");
                xml2.append("<agename>"+xmlize(n.ImageNode_GetAgeName())+"</agename>");
                xml2.append("<caption>"+xmlize(n.ImageNode_GetCaption())+"</caption>");
                xml2.append("<imagesha1>"+sha1+"</imagesha1>");
                xml2.append("</imagenode>");
                saveFile(outfolder, xml2);
            }
            else if(n.type==nodetype.TextNoteNode)
            {
                StringBuilder xml2 = new StringBuilder();
                xml2.append("<textnotenode>");
                xml2.append("<creationtime>"+xmlize(n.crt_time.toLongString())+"</creationtime>");
                xml2.append("<agetime>"+xmlize(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+xmlize(n.mod_time.toLongString())+"</modtime>");
                xml2.append("<owner>"+xmlize(nodes.getAvatarName(n.owner))+"</owner>");
                xml2.append("<agename>"+xmlize(n.ImageNode_GetAgeName())+"</agename>");
                xml2.append("<title>"+xmlize(n.TextNoteNode_GetTitle())+"</title>");
                xml2.append("<text>"+xmlize(n.TextNoteNode_GetText())+"</text>");
                xml2.append("</textnotenode>");
                saveFile(outfolder, xml2);
            }
            else if(n.type==nodetype.MarkerListNode)
            {
                StringBuilder xml2 = new StringBuilder();
                xml2.append("<markerlistnode>");
                xml2.append("<creationtime>"+xmlize(n.crt_time.toLongString())+"</creationtime>");
                xml2.append("<agetime>"+xmlize(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+xmlize(n.mod_time.toLongString())+"</modtime>");
                xml2.append("<owner>"+xmlize(nodes.getAvatarName(n.owner))+"</owner>");
                xml2.append("<agename>"+xmlize(n.ImageNode_GetAgeName())+"</agename>");
                xml2.append("<gamename>"+xmlize(n.xu20.toString())+"</gamename>");
                for(Node mn: nodes.getMarkers(n.owner, n.age_name.toString(), n.blob1))
                {
                    xml2.append("<marker>");
                    Flt f16 = Flt.createFromData(mn.xu16);
                    Flt f17 = Flt.createFromData(mn.xu17);
                    Flt f18 = Flt.createFromData(mn.xu18);
                    xml2.append("<text>"+mn.xu28.toString()+"</text>");
                    xml2.append("<x>"+f16.toString()+"</x>");
                    xml2.append("<y>"+f16.toString()+"</y>");
                    xml2.append("<z>"+f16.toString()+"</z>");
                    xml2.append("</marker>");
                }
                xml2.append("</markerlistnode>");
                saveFile(outfolder, xml2);
            }
        }

        for(File f: FileUtils.FindAllFiles(outfolder, ".mem.xml", false))
        {
            xml.append(b.BytesToString(FileUtils.ReadFile(f)));
        }
        
        xml.append("</memories>");
        FileUtils.WriteFile(outfolder+"/memories.xml", b.StringToBytes(xml.toString()));
        FileUtils.WriteFile(outfolder+"/memories.xsl", shared.GetResource.getResourceAsBytes("/files/memories.xsl"));
        FileUtils.WriteFile(outfolder+"/jquery.js", shared.GetResource.getResourceAsBytes("/files/jquery-1.3.2.min.js"));
        m.status("In the output folder, there will be a file called 'memories.xml'; open it with your web-browser to view the memories.");
        m.status("Finished creating Memories files!");
    }
    
    /*public static void saveImages2(String infolder, String outfolder)
    {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Memories</title>");
        html.append("<style type='text/css'>");
        html.append("div.textnotenode, div.imagenode, div.markerlistnode {");
        html.append("color: green;");
        html.append("border: thin solid blue;");
        html.append("margin: 16px 4px 16px 4px;");
        html.append("} </style>");
        html.append("</head><body>");
        
        Nodes nodes = readFolder(infolder);
        nodes.sortByCreationDate();

        for(Node n: nodes.nodes)
        {
            if(n.type==nodetype.ImageNode)
            {
                byte[] data = n.ImageNode_GetImageData();
                String sha1 = b.BytesToHexString(shared.CryptHashes.GetHash(data, shared.CryptHashes.Hashtype.sha1));
                //String filename = Integer.toString(i)+".jpg";
                String filename = sha1+".jpg";
                //html.append("Image:<a href='"+filename+"'>here</a><br/>");
                FileUtils.WriteFile(outfolder+"/"+filename, data);
                //String outfilename = outfolder+"/"+Sanitise.SanitiseFilename(image.ImageNode_GetAgeName()+"-("+Integer.toString(i)+")-"+image.ImageNode_GetCaption())+".jpg";
                //i++;
                //html.append("Agename:"+n.ImageNode_GetAgeName()+"<br/>");
                //html.append("Caption:"+n.ImageNode_GetCaption()+"<br/>");
                //html.append("Owner:"+nodes.getAvatarName(n.owner)+"<br/>");
                //html.append("Date:"+n.crt_time.toString()+"<br/>");
                //html.append("<img src='"+filename+"'/><br/>");
                html.append("<div class='imagenode'>");
                html.append("<b>"+htmlize(n.crt_time.toString())+"</b><br/>");
                html.append(htmlize(nodes.getAvatarName(n.owner))+", "+htmlize(n.ImageNode_GetAgeName())+"<br/>");
                html.append(htmlize(n.ImageNode_GetCaption())+"<br/>");
                html.append("<img src='"+filename+"'/><br/>");
                html.append("</div>\n");
            }
            else if(n.type==nodetype.TextNoteNode)
            {
                //html.append("Agename:"+n.TextNoteNode_GetAgename()+"<br/>");
                //html.append("Title:"+n.TextNoteNode_GetTitle()+"<br/>");
                //html.append("Text:"+n.TextNoteNode_GetText()+"<br/>");
                //html.append("Owner:"+nodes.getAvatarName(n.owner)+"<br/>");
                //html.append("Date:"+n.crt_time.toString()+"<br/>");
                html.append("<div class='textnotenode'>");
                html.append("<b>"+htmlize(n.crt_time.toString())+"</b><br/>");
                html.append(htmlize(nodes.getAvatarName(n.owner))+", "+htmlize(n.TextNoteNode_GetAgename())+"<br/>");
                html.append(htmlize(n.TextNoteNode_GetTitle())+"<br/>");
                html.append("<pre>"+htmlize(n.TextNoteNode_GetText())+"</pre><br/>");
                html.append("</div>\n");
                
            }
            else if(n.type==nodetype.MarkerListNode)
            {
                html.append("<div class='markerlistnode'>");
                html.append("<b>"+htmlize(n.crt_time.toString())+"</b><br/>");
                html.append(htmlize(nodes.getAvatarName(n.owner))+", "+htmlize(n.age_name.toString())+"<br/>");
                html.append(htmlize(n.xu20.toString())+"<br/>");
                html.append("<pre>");
                for(Node mn: nodes.getMarkers(n.owner, n.age_name.toString(), n.blob1))
                {
                    Flt f16 = Flt.createFromData(mn.xu16);
                    Flt f17 = Flt.createFromData(mn.xu17);
                    Flt f18 = Flt.createFromData(mn.xu18);
                    html.append(mn.xu28.toString()+" ("+f16.toString()+","+f17.toString()+","+f18.toString()+")"+"<br/>");
                }
                html.append("</pre>");
                html.append("</div>\n");
            }
        }
        
        html.append("</body></html>");
        //m.msg(html.toString());
        FileUtils.WriteFile(outfolder+"/index.htm", b.StringToBytes(html.toString()));
    }*/
    /*public static String htmlize(String text)
    {
        String result = text;
        if(result==null) result = "";
        //String result = "<pre>"+text.replace("<", "&lt;")+"</pre>";
        result = result.replace("<", "&lt;");
        return result;
    }*/
    public static Nodes readFolder(String infolder)
    {
        //Vector<vfile> vfiles = new Vector();
        Nodes result = new Nodes();
        
        File folder = new File(infolder);
        for(File child: folder.listFiles())
        {
            if(child.getName().endsWith(".v"))
            {
                vfile vf = vfile.createFromFilename(child.getAbsolutePath());
                //vfiles.add(vf);
                result.add(vf.node);
            }
        }
        //return vfiles;
        return result;
    }
    
    /*public static <T> Vector<T> findRecordsOfType(Vector<vfile> vfiles, Class<T> cls)
    {
        Vector<T> result = new Vector();
        
        for(vfile vf: vfiles)
        {
            T t = vf.node.castTo(cls);
            if(t!=null) result.add(t);
        }
        
        return result;
    }*/
}

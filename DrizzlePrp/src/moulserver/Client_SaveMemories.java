/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import shared.Concurrent;
import moulserver.GateServer.*;
import shared.*;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.Map;
import moulserver.ClientNodesHelper.*;

public class Client_SaveMemories extends Client
{
    public static void SaveMemories(String username, String password, String playername, String outputpath)
    {
        m.state.push();
        m.state.curstate.showNormalMessages = false;
        Client_SaveMemories client = new Client_SaveMemories();
        client.work(username,password,playername,outputpath);
        m.state.pop();
    }

    public void work(String username, String password, String playername, String outfolder)
    {
        //the shard version
        Version ver = Version.currentMoulagain;

        //test folders
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

        //connect to the auth server
        Client.AuthConnection authconn = new Client.AuthConnection(ver, ver.authserver);
        try{

        //login
        boolean loginsucceeded = authconn.Login(username,password);
        if(!loginsucceeded)
        {
            m.err("Login did not succeed.  The username or password was probably incorrect.");
            return;
        }

        AuthServer.AcctPlayerInfo playermsg = authconn.GetPlayer(playername);
        if(playermsg==null)
        {
            m.err("Avatar name was not found.");
            return;
        }

        //NodesInfo nodes = authconn.GetNodes(playermsg.playerId);
        ArrayList<byte[]> rawnodes = authconn.GetRawNodes(playermsg.playerId);
        ArrayList<Node> nodes = new ArrayList();
        for(byte[] rawnode: rawnodes) nodes.add(Node.getNode(rawnode));




        //process the nodes.


        //nodes.sortByCreationDate();
        //m.status("Getting data...");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version='1.0' encoding='ISO-8859-1'?>");
        xml.append("<?xml-stylesheet type='text/xsl' href='memories.xsl'?>");
        xml.append("<memories>");
        xml.append("<version>2</version>"); //the old pots/alcugs one have no <version> tags.

        java.util.HashSet<Long> dates = new java.util.HashSet();

        for(Node n2: nodes)
        {
            m.msg(n2.getClass().getName() + " : " + n2.toString());
            //Node n2 = Node.getNode(rawnode);
            if(n2 instanceof Node.ImageNode)
            {
                Node.ImageNode n = (Node.ImageNode)n2;
                byte[] data = n.getImageData();
                data = b.substr(data, 4, data.length-4); //fix for moul jpeg bug.

                String sha1 = b.BytesToHexString(shared.CryptHashes.GetHash(data, shared.CryptHashes.Hashtype.sha1));
                String filename = sha1+".jpg";
                FileUtils.WriteFile(outfolder+"/"+filename, data);

                StringBuilder xml2 = new StringBuilder();
                xml2.append("<imagenode>");
                xml2.append("<source>moulagain</source>");
                xml2.append("<creationtime>"+CleanXml(Long.toString(1000L*n.n.createTime))+"</creationtime>");
                //xml2.append("<agetime>"+CleanXml(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+CleanXml(Long.toString(1000L*n.n.modifyTime))+"</modtime>");
                xml2.append("<owner>"+CleanXml(getAvatarName(n.n.creatorIdx,nodes))+"</owner>");
                //xml2.append("<agename>"+CleanXml(n.n.createAgeName.toString())+"</agename>");
                xml2.append("<caption>"+CleanXml(n.getTitle())+"</caption>");
                xml2.append("<imagesha1>"+sha1+"</imagesha1>");
                xml2.append("</imagenode>");
                saveFile(outfolder, xml2);
            }
            else if(n2 instanceof Node.TextNoteNode)
            {
                Node.TextNoteNode n = (Node.TextNoteNode)n2;
                StringBuilder xml2 = new StringBuilder();
                xml2.append("<textnotenode>");
                xml2.append("<source>moulagain</source>");
                xml2.append("<creationtime>"+CleanXml(Long.toString(1000L*n.n.createTime))+"</creationtime>");
                //xml2.append("<agetime>"+CleanXml(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+CleanXml(Long.toString(1000L*n.n.modifyTime))+"</modtime>");
                xml2.append("<owner>"+CleanXml(getAvatarName(n.n.creatorIdx,nodes))+"</owner>");
                //xml2.append("<agename>"+CleanXml(n.n.createAgeName.toString())+"</agename>");
                //Str title = n.getTitle2();
                xml2.append("<title>"+CleanXml(n.getTitle())+"</title>");
                xml2.append("<text>"+CleanXml(n.getContents())+"</text>");
                xml2.append("</textnotenode>");
                saveFile(outfolder, xml2);
            }
            else if(n2 instanceof Node.MarkerListNode)
            {
                Node.MarkerListNode n = (Node.MarkerListNode)n2;
                StringBuilder xml2 = new StringBuilder();
                xml2.append("<markerlistnode>");
                xml2.append("<source>moulagain</source>");
                xml2.append("<creationtime>"+CleanXml(Long.toString(1000L*n.n.createTime))+"</creationtime>");
                //xml2.append("<agetime>"+CleanXml(n.age_time.toLongString())+"</agetime>");
                xml2.append("<modtime>"+CleanXml(Long.toString(1000L*n.n.modifyTime))+"</modtime>");
                xml2.append("<owner>"+CleanXml(getAvatarName(n.n.creatorIdx,nodes))+"</owner>");
                //xml2.append("<agename>"+CleanXml(n.n.createAgeName.toString())+"</agename>");
                xml2.append("<gamename>"+CleanXml(n.getGamename())+"</gamename>");

                //the markers are stored as a chronicle now.
                /*for(Node mn: nodes.getMarkers(n.owner, n.age_name.toString(), n.blob1))
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
                saveFile(outfolder, xml2);*/

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


        //all done!
        //m.msg("All done!");
        }finally{
            authconn.Disconnect();
        }

        m.status("In the output folder, there will be a file called 'memories.xml'; open it with your web-browser to view the memories.");
        m.status("Finished creating Memories files!");

    }

    public static String getAvatarName(int KInumber, ArrayList<Node> nodes)
    {
        for(Node n: nodes)
        {
            if(n instanceof Node.PlayerInfoNode)
            {
                Node.PlayerInfoNode n2 = (Node.PlayerInfoNode)n;
                if(n2.getPlayerId()==KInumber)
                {
                    return n2.getPlayerName();
                }
            }
        }
        return null;
    }

    private static void saveFile(String outfolder, StringBuilder xml)
    {
        String xmls = xml.toString();
        byte[] data = b.StringToBytes(xmls);
        String sha1 = b.BytesToHexString(shared.CryptHashes.GetHash(data, shared.CryptHashes.Hashtype.sha1));
        String filename = sha1+".mem.xml";
        FileUtils.WriteFile(outfolder+"/"+filename, data);
    }

    private static String CleanXml(String s)
    {
        if(s==null) return "";
        s = s.replace("\u001b", ""); //replace escape char, which occur sometimes at the end of textnote titles.
        s = EscapeUtils.escapeXmlString(s);
        return s;
    }

    /*private static ArrayList<Node> getMarkers(int KInumber, String age, int blob1, ArrayList<Node> nodes)
    {
        ArrayList<Node> result = new ArrayList();
        for(Node n: nodes)
        {
            if(n instanceof Node.MarkerListNode.type==nodetype.MarkerNode)
            {
                if(n.owner==KInumber)
                {
                    if(n.blob1==blob1)
                    {
                        if(n.age_name.toString().equals(age))
                        {
                            result.add(n);
                        }
                    }
                }
            }
        }
        return result;
    }*/

}

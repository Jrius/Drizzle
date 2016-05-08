/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uam;

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

/*public class UamConfig
{
    Document doc;
    //Element root;
    XPath xpath;
    boolean doValidate = false;
    
    UamConfigObject uco = null;
    Vector<String> allAgeNames = null;
    
    //public String getWhirlpool(String agename, String version)
    //{
    //    return this.getString("/uam/age[filename='"+agename+"']/version[name='"+version+"']/whirlpool");
    //}
    public String getAgeInfo(String agename)
    {
        return this.getString("/uam/age[filename='"+agename+"']/info");
    }
    public String getAgeProperName(String agename)
    {
        return this.getString("/uam/age[filename='"+agename+"']/name");
    }
    public String getSha1(String agename, String version)
    {
        return this.getString("/uam/age[filename='"+agename+"']/version[name='"+version+"']/sha1");
    }
    public String getDeletable(String agename)
    {
        return this.getString("/uam/age[filename='"+agename+"']/deletable");
    }
    public Vector<String> getAllAgeNames()
    {
        if(allAgeNames==null)
        {
            allAgeNames = this.getStrings("/uam/age/filename");
        }
        return allAgeNames;
    }
    public String getArchiveType(String agename, String version)
    {
        return this.getString("/uam/age[filename='"+agename+"']/version[name='"+version+"']/archive");
    }
    public String getWelcomeMessage()
    {
        return this.getString("/uam/welcome");
    }
    public Vector<String> getAllVersionsOfAge(String agename)
    {
        return this.getStrings("/uam/age[filename='"+agename+"']/version/name");
    }
    public Vector<String> getAllUrlsOfAgeVersion(String agename, String version)
    {
        return this.getStrings("/uam/age[filename='"+agename+"']/version[name='"+version+"']/mirror/url");
    }
    public UamConfig(InputStream in)
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
        //root = doc.getDocumentElement();
        
        XPathFactory xpathfact = XPathFactory.newInstance();
        xpath = xpathfact.newXPath();
        
        //make sure we got the end of the file...
        boolean hasend = this.hasItem("/uam/aequalsatransposeequalsainverse");
        if(!hasend)
        {
            throw new ConfigErrorException("Config file doesn't seem to have the correct end; it's probably corrupt.");
        }
        
        //InputSource is = new InputSource(in);
        //XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
        //SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        //reader.parse(is);
        //parser.getParser().
        //SAXParser parser = new SAXParser();
        //parser.
        //javax.xml.parsers.SAXParser
    }
    boolean hasItem(String xpathquery)
    {
        Node n = this.findNode(xpathquery);
        return n!=null;
    }
    Vector<String> getStrings(String xpathquery)
    {
        Vector<String> result = new Vector();
        for(Node n: this.findNodes(xpathquery))
        {
            result.add(n.getTextContent());
        }
        return result;
    }
    String getString(String xpathquery)
    {
        Node queryresult = findNode(xpathquery);
        if(queryresult==null)
        {
            m.err("Unable to find property in xml list: "+xpathquery);
            return "";
        }
        else
        {
            String result = queryresult.getTextContent();
            return result;
        }
    }
    Node findNode(String xpathquery)
    {
        Object result2;
        try
        {
            result2 = xpath.compile(xpathquery).evaluate(doc,XPathConstants.NODESET);
        }
        catch(XPathExpressionException e)
        {
            throw new ConfigErrorException("Error finding something in the config file.");
        }
        NodeList result = (NodeList)result2; //won't cast to Node directly.
        Node result3 = (result.getLength()>0)?result.item(0):null;
        return result3;
    }
    Vector<Node> findNodes(String xpathquery)
    {
        Object result2;
        try
        {
            result2 = xpath.compile(xpathquery).evaluate(doc,XPathConstants.NODESET);
        }
        catch(XPathExpressionException e)
        {
            throw new ConfigErrorException("Error finding something in the config file.");
        }
        NodeList result3 = (NodeList)result2;
        Vector<Node> result = new Vector();
        int count = result3.getLength();
        for(int i=0;i<count;i++)
        {
            result.add(result3.item(i));
        }
        return result;
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

    public UamConfigObject getConfigObject()
    {
        //cache it.
        if(uco==null)
        {
            uco = new UamConfigObject(this);
        }
        return uco;
    }
    
    public static class UamConfigObject
    {
        public HashMap<String,Vector<String>> ageversions;
        
        private UamConfigObject(UamConfig source)
        {
            //TODO: make this more efficient:
            
            ageversions = new HashMap();
            for(String agename: source.getAllAgeNames())
            {
                Vector<String> versions = source.getAllVersionsOfAge(agename);
                ageversions.put(agename, versions);
            }
        }
    }
    
    
}*/

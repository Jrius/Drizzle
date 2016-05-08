/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

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

import org.w3c.dom.Comment;

public class xml
{


    static final boolean doValidate = false;
    
    static DocumentBuilder builder;

    Document doc;
    Element root;
    XPath xpath;
    
    //public UamConfigData data; //removed
    
    public static String sanitise(String in)
    {
        in = in.replace("&", "&amp;");
        in = in.replace("\"", "&quot;");
        in = in.replace("'", "&apos;");
        in = in.replace("<", "&lt;");
        in = in.replace(">", "&gt;");
        return in;
    }

    public xml(byte[] data)
    {
        this(new java.io.ByteArrayInputStream(data));
    }
    public xml(InputStream in)
    {
        if(builder==null)
        {
            DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
            docfact.setValidating(doValidate);
            //DocumentBuilder builder;
            try
            {
                builder = docfact.newDocumentBuilder();
                builder.setErrorHandler(new xmlErrorHandler());
            }
            catch(ParserConfigurationException e)
            {
                throw new ConfigErrorException("Error creating parser.");
            }
        }
        if(xpath==null)
        {
            XPathFactory xpathfact = XPathFactory.newInstance();
            xpath = xpathfact.newXPath();
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
        
        
        //data = new UamConfigData(root); //removed
        //data.loadFromNode(root);
        
        //make sure we got the end of the file...
        //boolean hasend = this.hasItem("/uam/aequalsatransposeequalsainverse");
        //removed
        /*if(!data.aequalsatransposeequalsainverse)
        {
            throw new ConfigErrorException("Config file doesn't seem to have the correct end; it's probably corrupt.");
        }*/
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
    
    public Vector<String> getStrings(String xpathquery)
    {
        Vector<String> result = new Vector();
        for(Node n: this.findNodes(xpathquery))
        {
            result.add(n.getTextContent());
        }
        return result;
    }
    public String getString(String xpathquery)
    {
        Node queryresult = findNode(xpathquery);
        if(queryresult==null)
        {
            m.err("Unable to find property in xml list: ",xpathquery);
            return "";
        }
        else
        {
            String result = queryresult.getTextContent();
            return result;
        }
    }
    public Node findNode(String xpathquery)
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
    public Vector<Node> findNodes(String xpathquery)
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

    public static class ConfigErrorException extends RuntimeException
    {
        public ConfigErrorException(String msg)
        {
            super(msg);
            m.err(msg);
        }
    }

}

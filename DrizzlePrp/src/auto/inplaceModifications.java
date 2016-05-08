/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.Vertex;
import shared.Flt;
import prpobjects.*;
import prpobjects.plPythonFileMod.Pythonlisting;
import shared.FileUtils;
import uru.context;
import uru.Bytestream;
import shared.Bytes;
import java.io.File;
import shared.m;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import java.util.Vector;

public class inplaceModifications
{
    /*public static void addDynamicTextMapAndMiscToFile(String filename, String outfolder)
    {
        //open
        prpfile prp = prpfile.createFromFile(filename, true);
        
        //the images written in python must be no greater than this size, or it won't draw it.
        //the width and height are each rounded up to the nearest power of 2.
        //the image can be stretched to fit with python.
        //int width = 350; int height = 604;
        //int width = 512; int height = 1024;
        
        if(filename.toLowerCase().endsWith("nexus_district_nxusbookmachine.prp"))
        {
        }
        if(filename.toLowerCase().endsWith("gui_district_kiblackbar.prp"))
        {
        }
        
        //save
        String filename2 = outfolder+"/dat/"+new File(filename).getName();
        prp.saveAsFile(filename2);
    }*/
    /*public static void atranslateAllObjects(String filename, String outfolder, float x, float y, float z)
    {
        
        //read file
        byte[] filedata = FileUtils.ReadFile(filename);
        File f = new File(filename);
        context c = context.createFromBytestream(new Bytestream(filedata));
        c.curFile = filename;
        prpfile prp = uru.moulprp.prpprocess.ProcessAllObjects(c,true); //read raw
        prp.filename = filename;
        
        //hacks
        if(f.getName().toLowerCase().equals("ahnysphere01_district_sphere01.prp"))
        {
        }
        if(f.getName().toLowerCase().equals("ahnysphere01_district_maintroom01.prp"))
        {
        }
        
        //translate...
        translateAllObjects(prp, x, y ,z);
        
        //save changes.
        String filename2 = outfolder+"/dat/"+new File(filename).getName();
        //Bytes result = prp.saveAsBytes();
        //FileUtils.WriteFile(filename2, result);
        prp.saveAsFile(filename2);
        
    }*/

    
}

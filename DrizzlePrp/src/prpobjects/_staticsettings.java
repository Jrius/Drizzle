/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package prpobjects;

import shared.m;

/**
 *
 * @author user
 */
public class _staticsettings
{
    /*static void onHeaderLoaded(PrpHeader header)
    {
        
    }
    static void onObjectIndexLoaded(PrpObjectIndex objectindex)
    {
        
    }
    static void onObjectLoaded(_uruobj object)
    {
        
    }*/
    //public static String outputdir = "c:/documents and settings/user/desktop/output/";
    
    //set this to a value other than 0x00 to force the compiler to use this sequence prefix in Pageid.
    //todo: replace this with a member in an outputcontext.
    //0x63 is the one I'm using for Payiferen.
    //public static byte sequencePrefix = 0x00;
    
    //set breakpoint to -1 to disable it.
    public static int breakpoint = -1;
    //public static int breakpoint = 0x0065-0x3F;
    
    static Uruobjectdesc currentRootObj = null;
    
    //reportNewRootObj
    
    static boolean reportReferences = false; //should encountered uruobjectrefs be reported?
    static StringBuilder referenceReport = new StringBuilder();
    static void reportReference(Uruobjectdesc desc)
    {
        if(_staticsettings.currentRootObj!=null && reportReferences==true)
        {
            //referenceReport.append("    ref from:"+_staticsettings.currentRootObj.objectname.toString()+" number="+Integer.toString(currentRootObj.objectnumber)+" type="+currentRootObj.objecttype.toString()+"    ******ref to:"+desc.objectname.toString()+" (page="+desc.pageid.toString()+")(type="+desc.objecttype.toString()+")(number="+Integer.toString(desc.objectnumber)+")\n");
            referenceReport.append("ref;"+_staticsettings.currentRootObj.objectname.toString()+";"+currentRootObj.objecttype.toString()+";"+Integer.toString(currentRootObj.objectnumber)+";"+desc.objectname.toString()+";"+desc.objecttype.toString()+";"+Integer.toString(desc.objectnumber)+";"+desc.pageid.toString()+"\n");
        }
    }

    static StringBuilder scannedReferenceReport = new StringBuilder();
    static boolean tryToFindReferencesInUnknownObjects = false; //should we scan objects of unknown type for references?
    static void reportFoundUnknownReference(byte[] msg)
    {
        if(_staticsettings.currentRootObj!=null)
        {
            //scannedReferenceReport.append("Scanned reference found:"+new String(msg)+" in the rootobj:"+currentRootObj.objectname.toString()+" number="+Integer.toString(currentRootObj.objectnumber)+" type="+currentRootObj.objecttype.toString()+"\n");
            scannedReferenceReport.append("scan;"+currentRootObj.objectname.toString()+";"+currentRootObj.objecttype.toString()+";"+Integer.toString(currentRootObj.objectnumber)+";"+new String(msg)+";;;\n");
        }
    }
    
    static boolean doVisit(Uruobjectdesc desc)
    {
        boolean checkVisits = false; //should we use the doVisit check before visiting ages?
        
        if(desc.objectname.toString().equals("RTStatueLightOnly01"))
        {
            int i = 0; //dummy breakpoint.
        }
        
        if(!checkVisits) return true; //leave this uncommented to proceed as usual.
        
        if(desc.objectname.toString().equals("LinkInPointDefault"))
        {
            return true;
        }
        if(desc.objectname.toString().equals("StartPointDefault"))
        {
            return true;
        }
        if(desc.objecttype==Typeid.plDrawInterface)
        {
            return true;
        }
        return false;
        
        /*if(desc.objecttype==Typeid.plSpawnModifier)
        {
            return true;
        }
        if(desc.objecttype==Typeid.plCoordinateInterface || desc.objecttype==Typeid.plSceneObject)
        {
            if(desc.objectname.toString().equals("LinkInPointDefault"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        if (desc.objecttype==Typeid.plSceneNode)
        {
            return false;
        }
        return false;*/
    }
}

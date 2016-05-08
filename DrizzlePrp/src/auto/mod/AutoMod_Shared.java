/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.mod;

import prpobjects.prpfile;
import shared.m;
import prpobjects.Urustring;
import prpobjects.Bstr;
import prpobjects.PrpRootObject;
import prpobjects.Typeid;
import prpobjects.Uruobjectdesc;
import prpobjects.Uruobjectref;

import prpobjects.plSceneObject;
import prpobjects.plPythonFileMod.Pythonlisting;

public class AutoMod_Shared
{
    public static void AddXDustChronicleShowHideToObject(prpfile prp, plSceneObject sceneobject, String sdlvar, String valToMatch, boolean reverse)
    {
        //create pfm
        //add the pfm to the prp if it doesn't have it already:
        Uruobjectref pfmref;
        String pfmname = "PythXDustChronicleShowHide"+sdlvar+(reverse?"Rev":"");
        PrpRootObject pfmobj = prp.findObject(pfmname, Typeid.plPythonFileMod);
        if(pfmobj==null)
        {
            prpobjects.plPythonFileMod pfm = prpobjects.plPythonFileMod.createDefault();
            pfm.pyfile = Urustring.createFromString("xDustChronicleShowHide");
            pfm.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString(sdlvar)));
            pfm.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithString(2, Bstr.createFromString(valToMatch)));
            pfm.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithBoolean(3, !reverse));
            //no pythonlistings here...
            pfmref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plPythonFileMod, pfmname, prp);
            pfmobj = PrpRootObject.createFromDescAndObject(pfmref.xdesc, pfm);
            prp.addObject(pfmobj);
        }
        else
        {
            pfmref = pfmobj.header.desc.toRef();
        }
        sceneobject.addToObjectrefs2(pfmref);

    }

    public static void AddXAgeSDLBoolShowHideToObject(prpfile prp, plSceneObject sceneobject, String sdlvar, boolean reverse)
    {
        //create pfm
        //add the pfm to the prp if it doesn't have it already:
        Uruobjectref pfmref;
        String pfmname = "PythXAgeSDLBoolShowHide"+sdlvar+(reverse?"Rev":"");
        PrpRootObject pfmobj = prp.findObject(pfmname, Typeid.plPythonFileMod);
        if(pfmobj==null)
        {
            prpobjects.plPythonFileMod pfm = prpobjects.plPythonFileMod.createDefault();
            pfm.pyfile = Urustring.createFromString("xAgeSDLBoolShowHide");
            pfm.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithString(1, Bstr.createFromString(sdlvar)));
            pfm.addListing(prpobjects.plPythonFileMod.Pythonlisting.createWithBoolean(2, !reverse));
            //no pythonlistings here...
            pfmref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plPythonFileMod, pfmname, prp);
            pfmobj = PrpRootObject.createFromDescAndObject(pfmref.xdesc, pfm);
            prp.addObject(pfmobj);
        }
        else
        {
            pfmref = pfmobj.header.desc.toRef();
        }
        sceneobject.addToObjectrefs2(pfmref);

    }
    public static void AddXDustShowToObject(prpfile prp, plSceneObject sceneobject)
    {
        //create pfm
        //add the pfm to the prp if it doesn't have it already:
        Uruobjectref pfmref;
        PrpRootObject pfmobj = prp.findObject("PythXDustShow", Typeid.plPythonFileMod);
        if(pfmobj==null)
        {
            prpobjects.plPythonFileMod pfm = prpobjects.plPythonFileMod.createDefault();
            pfm.pyfile = Urustring.createFromString("xDustShow");
            //no pythonlistings here...
            pfmref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plPythonFileMod, "PythXDustShow", prp);
            pfmobj = PrpRootObject.createFromDescAndObject(pfmref.xdesc, pfm);
            prp.addObject(pfmobj);
        }
        else
        {
            pfmref = pfmobj.header.desc.toRef();
        }
        sceneobject.addToObjectrefs2(pfmref);

    }

    public static void CreatePythonfilemodReference(prpfile prp, String pyfilenameWithoutExtension, Uruobjectref objref, int type, String arbitraryPfmName)
    {
        //create pfm
        prpobjects.plPythonFileMod pfm = prpobjects.plPythonFileMod.createDefault();
        pfm.pyfile = Urustring.createFromString(pyfilenameWithoutExtension);
        pfm.addListing(Pythonlisting.createWithString(4, 1, Bstr.createFromString(objref.xdesc.objectname.toString())));
        pfm.addListing(Pythonlisting.createWithRef(type, 2, objref));
        //pfm.addListing(Pythonlisting.createWithString(4, 3, Bstr.createFromString(objref.xdesc.objecttype.toString())));
        Uruobjectref pfmref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plPythonFileMod, arbitraryPfmName, prp);
        PrpRootObject pfmroot = PrpRootObject.createFromDescAndObject(pfmref.xdesc, pfm);

        //find scenenode.
        PrpRootObject sn = null;
        for(PrpRootObject obj: prp.objects2)
            if(obj.header.objecttype==Typeid.plSceneNode)
                sn = obj;

        //create sceneobject
        plSceneObject so = plSceneObject.createDefaultWithScenenode(sn.header.desc.toRef());
        so.addToObjectrefs2(pfmref);
        Uruobjectref soref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plSceneObject, arbitraryPfmName+"_so", prp);
        PrpRootObject soroot = PrpRootObject.createFromDescAndObject(soref.xdesc, so);

        //add scenenode and sceneobject
        sn.markAsChanged();
        soroot.markAsChanged();
        pfmroot.markAsChanged();
        //prp.extraobjects.add(soroot);
        //prp.extraobjects.add(pfmroot);
        prp.addObject(soroot);
        prp.addObject(pfmroot);

    }

}

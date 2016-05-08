/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.prpfile;
import prpobjects.PrpRootObject;
import prpobjects.plPythonFileMod;
import prpobjects.plSynchedObject;
import prpobjects.plMultiModifier;
import prpobjects.Urustring;
import prpobjects.Uruobjectref;
import prpobjects.Uruobjectdesc;
import prpobjects.Typeid;
import prpobjects.Pageid;
import prpobjects.Pagetype;
import prpobjects.prpfile;
import shared.Bytes;
import prpobjects.plSceneObject;
import prpobjects.x0000Scenenode;
import prpobjects.*;
import java.util.Vector;

public class hackFactory
{
    public static PrpRootObject createAndAddDynamicTextMap(prpfile prp, String name, int visWidth, int visHeight)
    {
        plDynamicTextMap map = plDynamicTextMap.createBlank(visWidth,visHeight);//1024,1024);
        Uruobjectref mapref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plDynamicTextMap, name, prp.header.pageid, prp.header.pagetype);
        PrpRootObject maproot = PrpRootObject.createFromDescAndObject(mapref.xdesc, map);
        //prp.extraobjects.add(maproot);
        prp.addObject(maproot);
        
        return maproot;
    }
    
    public static void createAndAddCoordinateInterface(prpfile prp, PrpRootObject sceneobject)
    {
        plCoordinateInterface ci = plCoordinateInterface.createDefault(sceneobject.header.desc.toRef());
        Uruobjectref ciref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plCoordinateInterface, sceneobject.header.desc.objectname.toString()+"_ci", prp.header.pageid, prp.header.pagetype);
        PrpRootObject ciroot = PrpRootObject.createFromDescAndObject(ciref.xdesc, ci);
        ci.parent.bv = HsBitVector.createWithValues(0);
        
        plSceneObject so = prp.findObjectWithRef(sceneobject.header.desc.toRef()).castTo();
        so.coordinateinterface = ciref;
        sceneobject.hasChanged = true;
        
        //prp.extraobjects.add(ciroot);
        prp.addObject(ciroot);
    }
    
    public static void createBuiltInPrpFile(String agename, String outfolder)
    {
        //String agename = "Direbo";
        Pageid pid = Pageid.createFromPrefixPagenum(93, -2);
        
        //create
        prpfile prp = createBuiltInPrp(agename,pid);
        
        //compile
        //Bytes prpoutputbytes = prp.saveAsBytes();
        //prpoutputbytes.saveAsFile(outfolder+"/dat/"+agename+"_District_"+prp.header.pagename.toString()+".prp");
        prp.saveAsFile(outfolder+"/dat/"+agename+"_District_"+prp.header.pagename.toString()+".prp");
    }
    
    public static prpfile createBuiltInPrp(String agename, Pageid pid)
    {        
        //create pythonfilemod
        plPythonFileMod pm = plPythonFileMod.createDefault();
        Uruobjectref pmref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plPythonFileMod, "VeryVerySpecialPythonFileMod", pid, Pagetype.createWithType(8));
        PrpRootObject pmroot = PrpRootObject.createFromDescAndObject(pmref.xdesc, pm);
        pm.pyfile = Urustring.createFromString(agename);
        
        //create sceneobject
        plSceneObject so = plSceneObject.createDefaultWithScenenode(Uruobjectref.none());
        Uruobjectref soref = Uruobjectref.createDefaultWithTypeNamePagePagetype(Typeid.plSceneObject, "AgeSDLHook", pid, Pagetype.createWithType(8));
        PrpRootObject soroot = PrpRootObject.createFromDescAndObject(soref.xdesc, so);
        so.addToObjectrefs2(pmref);
        
        //create the prpfile
        PrpRootObject[] objects = new PrpRootObject[]{ pmroot, soroot };
        prpfile prp = prpfile.createFromObjectsAndInfo(objects, agename, "BuiltIn", pid, Pagetype.createWithType(8));
        return prp;
    }

    public static PrpRootObject createSceneNode(String name, Pageid pid)
    {
        x0000Scenenode sn = x0000Scenenode.createDefault();
        Uruobjectref snref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneNode, name, pid);
        PrpRootObject snroot = PrpRootObject.createFromDescAndObject(snref.xdesc, sn);
        return snroot;
    }
    
    public static PrpRootObject createSceneObject(PrpRootObject scenenode, String name, Pageid pid)
    {
        plSceneObject so = plSceneObject.createDefaultWithScenenode(Uruobjectref.createFromUruobjectdesc(scenenode.header.desc));
        Uruobjectref soref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, name, pid);
        PrpRootObject soroot = PrpRootObject.createFromDescAndObject(soref.xdesc, so);
        return soroot;
    }
    
    public static void create3SpawnpointObjects(PrpRootObject scenenode, String name, Pageid pid, Vector<PrpRootObject> objects)
    {
        PrpRootObject sceneobject = createSceneObject(scenenode,name+"_sceneobject", pid);

        x003DSpawnModifier spawnmod = x003DSpawnModifier.createDefault();
        Uruobjectref spawnref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSpawnModifier, name+"_spawnmod", pid);
        PrpRootObject spawnmodro = PrpRootObject.createFromDescAndObject(spawnref.xdesc, spawnmod);
        
        plCoordinateInterface coordinter = plCoordinateInterface.createDefault(sceneobject.header.desc.toRef());
        Uruobjectref coordref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plCoordinateInterface, name+"_coordint", pid);
        PrpRootObject coordro = PrpRootObject.createFromDescAndObject(coordref.xdesc, coordinter);

        coordinter.parent.bv = new HsBitVector(6);
        sceneobject.castToSceneObject().coordinateinterface = coordref;
        sceneobject.castToSceneObject().addToObjectrefs2(spawnref);
        
        objects.add(sceneobject);
        objects.add(spawnmodro);
        objects.add(coordro);
    }
    
    public static void createMinkataClusterGroupPythonMod(String outfolder)
    {
        //#01_0, 01_1,02_4,02_5,03_2,03_3
        //#minkExteriorDay, minkExteriorNight
        
        //create the pythonfilemod
        plPythonFileMod pm = plPythonFileMod.createDefault();
        pm.pyfile = Urustring.createFromString("minkDustdummy");
        //pm.listcount = 1;
        
        Pageid daypid = Pageid.createFromPrefixPagenum(42, 11); //we're moving it to page 11
        Pageid ngtpid = Pageid.createFromPrefixPagenum(42, 12); //we're moving it to page 12
        Pageid thispid = Pageid.createFromPrefixPagenum(42, 10);
        
        Uruobjectref ref01 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater01_0_minkDusttestDay", daypid);
        Uruobjectref ref02 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater01_1_minkDusttestDay", daypid);
        Uruobjectref ref03 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater02_4_minkDusttestDay", daypid);
        Uruobjectref ref04 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater02_5_minkDusttestDay", daypid);
        Uruobjectref ref05 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater03_2_minkDusttestDay", daypid);
        Uruobjectref ref06 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater03_3_minkDusttestDay", daypid);
        Uruobjectref ref07 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater01_0_minkDusttestNight", ngtpid);
        Uruobjectref ref08 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater01_1_minkDusttestNight", ngtpid);
        Uruobjectref ref09 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater02_4_minkDusttestNight", ngtpid);
        Uruobjectref ref10 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater02_5_minkDusttestNight", ngtpid);
        Uruobjectref ref11 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater03_2_minkDusttestNight", ngtpid);
        Uruobjectref ref12 = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plClusterGroup, "cCluster_Crater03_3_minkDusttestNight", ngtpid);
        //pm.listings = new x00A2Pythonfilemod.Pythonlisting[1];
        //pm.listings[0] = pl;
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  1, ref01));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  2, ref02));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  3, ref03));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  4, ref04));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  5, ref05));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  6, ref06));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  7, ref07));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  8, ref08));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20,  9, ref09));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20, 10, ref10));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20, 11, ref11));
        pm.addListing(plPythonFileMod.Pythonlisting.createWithRef(20, 12, ref12));
        
        Uruobjectdesc desc = Uruobjectdesc.createDefaultWithTypeNamePage(Typeid.plPythonFileMod, "Dustin_craters_pfm", thispid);
        PrpRootObject ro = PrpRootObject.createFromDescAndObject(desc, pm);
        
        //create the scenenode
        //x0000Scenenode sn = x0000Scenenode.createDefault();
        //Uruobjectref snref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneNode, "Minkata_minkDusttest", pid2);
        //PrpRootObject snroot = PrpRootObject.createFromDescAndObject(snref.xdesc, sn);
        PrpRootObject snroot = createSceneNode("Minkata_minkDusttest", thispid);
        
        //create the sceneobject
        //x0001Sceneobject so = x0001Sceneobject.createDefaultWithScenenode(snref);
        //Uruobjectref soref = Uruobjectref.createDefaultWithTypeNamePage(Typeid.plSceneObject, "cCluster_Crater01_0_pfm_so", pid2);
        //PrpRootObject soroot = PrpRootObject.createFromDescAndObject(soref.xdesc, so);
        PrpRootObject soroot = createSceneObject(snroot, "Dustin_craters_so", thispid);
        //so.count2 = 1;
        //so.objectrefs2 = new Uruobjectref[]{ Uruobjectref.createFromUruobjectdesc(desc) };
        soroot.castToSceneObject().addToObjectrefs2(desc.toRef());
        
        //populate the scenenode with the sceneobjects
        //sn.objectrefs1 = new Uruobjectref[]{ soref };
        //sn.count1 = sn.objectrefs1.length;
        snroot.castToSceneNode().addToObjectrefs1(soroot.header.desc.toRef());
        
        //create the prpfile
        PrpRootObject[] objects = new PrpRootObject[]{ ro, soroot, snroot };
        prpfile prp = prpfile.createFromObjectsAndInfo(objects, "Minkata", "minkDusttest", thispid, Pagetype.createDefault());
        
        //compile
        //Bytes prpoutputbytes = prp.saveAsBytes();
        //prpoutputbytes.saveAsFile(outfolder+"/dat/Minkata_District_minkDusttest.prp");
        prp.saveAsFile(outfolder+"/dat/Minkata_District_minkDusttest.prp");
        
    }
}

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

import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.m;
import shared.b;
import shared.e;
import java.util.Vector;
import shared.FileUtils;
import shared.readexception;
import java.io.File;
import shared.Bytes;

/**
 *
 * @author user
 */
public class prputils
{

    
    public static void ThisIsJustATemplate(context c)
    {
        //Bytestream data = c.in;

        PrpHeader header = new PrpHeader(c);
        
        //do header work.
        
        //process the object index, which is *not* a part of this struct.
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(new Bytestream(c.in,header.offsetToObjectIndex)));
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(header.offsetToObjectIndex)));
        context c2 = c.Fork(header.offsetToObjectIndex);
        PrpObjectIndex objectindex = new PrpObjectIndex(c2);
        c2.close();
        int numobjecttypes = objectindex.indexCount;
        for(int i_type=0;i_type<numobjecttypes;i_type++)
        {
            Typeid type = objectindex.types[i_type].type;
            int numObjects = objectindex.types[i_type].objectcount;
            
            //do per-type work.
            
            for(int j_obj=0;j_obj<numObjects;j_obj++)
            {
                Uruobjectdesc desc = objectindex.types[i_type].descs[j_obj].desc;
                int offset = objectindex.types[i_type].descs[j_obj].offset;
                int size = objectindex.types[i_type].descs[j_obj].size;
                
                //do per-object work.
                
            }
        }
    }

    public static String MakeObjectIndexReport(byte[] data)
    {
        context c = context.createFromBytestream(new Bytestream(data));
        
        StringBuilder report = new StringBuilder();
        
        PrpHeader header = new PrpHeader(c);
        
        //do header work.
        report.append("header:\n");
        report.append("    age:"+header.agename.toString()+"\n");
        report.append("    pagename:"+header.pagename.toString()+"\n");
        report.append("    pagetype:"+header.pagetype.toString()+"\n");
        report.append("    pageid:"+header.pageid.toString()+"\n");
        report.append("Object types:\n");
        
        //process the object index, which is *not* a part of this struct.
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(new Bytestream(c.in,header.offsetToObjectIndex)));
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(header.offsetToObjectIndex)));
        context c2 = c.Fork(header.offsetToObjectIndex);
        PrpObjectIndex objectindex = new PrpObjectIndex(c2);
        c2.close();
        int numobjecttypes = objectindex.indexCount;
        for(int i=0;i<numobjecttypes;i++)
        {
            Typeid type = objectindex.types[i].type;
            int numObjects = objectindex.types[i].objectcount;
            
            //do per-type work.
            report.append("    typeid:"+type.toString()+"\n");
            report.append("    Num objects:"+Integer.toString(numObjects)+"\n");
            
            for(int j=0;j<numObjects;j++)
            {
                Uruobjectdesc desc = objectindex.types[i].descs[j].desc;
                int offset = objectindex.types[i].descs[j].offset;
                int size = objectindex.types[i].descs[j].size;
                
                //do per-object work.
                report.append("        desc:"+desc.toString()+"\n");
                report.append("        offset:0x"+Integer.toHexString(offset)+"\n");
                report.append("        length:0x"+Integer.toHexString(size)+"\n");
                
            }
        }
        
        return report.toString();
    }
    
    public static prpfile ProcessAllMoul(byte[] data, Typeid[] typesToRead)
    {
        context c = context.createFromBytestream(new Bytestream(data));
        return ProcessAllMoul(c, false, typesToRead);
    }
    /*public static prpfile ProcessAll(context c)
    {
        if(c.readversion==6)
        {
            return ProcessAllMoul(c,false);
        }
        else if(c.readversion==3)
        {
            return ProcessPotsPrp(c);
        }
        m.err("processall: Unknown readversion.");
        return null;
    }*/
    public static prpfile ProcessAllMoul(context c, boolean reportProgress, Typeid[] typesToRead)
    {
        shared.IBytestream data = c.in;

        PrpHeader header = new PrpHeader(c);
        
        //todo: this should be in the proper place, not here.
            //fix payiferen pageid conflict problem.
            /*if (header.agename.toString().toLowerCase().equals("payiferen"))
            {
                //_staticsettings.sequencePrefix = 0x63;
                c.sequencePrefix = 0x63;
            }
            else if (header.agename.toString().toLowerCase().equals("kveer"))
            {
                //_staticsettings.sequencePrefix = 0x62;
                if(c.readversion==6) c.sequencePrefix = 0x62; //only if reading from moul.
            }
            else if (header.agename.toString().toLowerCase().equals("edertsogal"))
            {
                //_staticsettings.sequencePrefix = 0x61;
                c.sequencePrefix = 0x61;
            }*/

        //do header work.
        //_staticsettings.onHeaderLoaded(header);
        prpfile result = new prpfile();
        Vector<PrpRootObject> objects = new Vector<PrpRootObject>();
        result.header = header;
        
        //process the object index, which is *not* a part of this struct.
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(new Bytestream(data,header.offsetToObjectIndex)));
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(header.offsetToObjectIndex)));
        context c2 = c.Fork(header.offsetToObjectIndex);
        PrpObjectIndex objectindex = new PrpObjectIndex(c2);
        c2.close();
        //_staticsettings.onObjectIndexLoaded(objectindex);
        result.objectindex = objectindex;
        
        int numobjecttypes = objectindex.indexCount;
        for(int i=0;i<numobjecttypes;i++)
        {
            Typeid type = objectindex.types[i].type;
            //if(type==Typeid.plSceneObject) continue;
            int numObjects = objectindex.types[i].objectcount;
            
            //do per-type work.
            boolean parseThisType = false;
            if(typesToRead==null)
            {
                parseThisType = true;
            }
            else
            {
                for(int k=0;k<typesToRead.length;k++)
                {
                    if (typesToRead[k]==type)
                    {
                        parseThisType = true;
                    }
                }
            }
            if(reportProgress) m.msg("type=",type.toString());
            for(int j=0;j<numObjects;j++)
            {
                Uruobjectdesc desc = objectindex.types[i].descs[j].desc;
                int offset = objectindex.types[i].descs[j].offset;
                int size = objectindex.types[i].descs[j].size;
                if(shared.State.AllStates.getStateAsBoolean("reportObjects")) m.msg("ObjectReport: ",desc.toString());
                
                //do per-object work.
                if(!_staticsettings.doVisit(desc)) continue; //should we process this object?
                //rootobj object = null;
                PrpRootObject object = null;
                //context stream = c.Fork(new Bytestream(data,offset));
                //context stream = c.Fork(c.in.Fork(offset));
                context stream = c.Fork(offset);

                stream.curRootObject = desc;
                stream.curRootObjectOffset = offset;
                stream.curRootObjectSize = size;
                stream.curRootObjectEnd = offset+size;

                //process the object, which is *not* a part of this struct.
                //to disable an object type, simply comment out its "object=" line.
                _staticsettings.currentRootObj = desc; //used for reporting.
                boolean handled = true;
                if(desc.objectname.toString().toLowerCase().equals("envmap02"))
                {
                    int dummy=0;
                }
                if(parseThisType)
                {
                /*switch(desc.objecttype)
                {
                    case plSceneNode:
                    case plSceneObject:
                    case plMipMap:
                    case plCubicEnvironMap:
                    case plLayer:
                    case hsGMaterial:
                    case plParticleSystem:
                    case plBoundInterface:
                    case plAudioInterface:
                    case plWinAudio:
                    case plCoordinateInterface:
                    case plDrawInterface:
                    case plSpawnModifier:
                    case plDrawableSpans:
                    case plDirectionalLightInfo:
                    case plOmniLightInfo:
                    case plPythonFileMod:
                    case plPointShadowMaster:
                    case plSimulationInterface:
                    case plViewFaceModifier:
                    case plSittingModifier:
                    case plStereizer:
                    case plSoundBuffer:
                    case plRandomSoundMod:
                    case plWin32StreamingSound:
                    case plWin32StaticSound:
                    case plParticleLocalWind:
                    case plParticleCollisionEffectDie:
                    case plExcludeRegionModifier:
                    case plCameraBrain1:
                    case plCameraBrain1_Avatar:
                    case plCameraBrain1_Fixed:
                    case plCameraBrain1_Circle:
                    case plCameraModifier1:
                    case plAGModifier:
                    case plOccluder:
                    case plDynamicTextMap:
                    case plParticleCollisionEffectBounce:
                    case plSpotLightInfo:
                    case plShadowCaster:
                    case plDirectShadowMaster:
                    case plRelevanceRegion:
                    case plSoftVolumeSimple:
                    case plResponderModifier:
                    case plParticleFlockEffect:
                    case plFadeOpacityMod:
                    case plClusterGroup:
                    case plVisRegion:
                    case plSoftVolumeUnion:
                    case plObjectInVolumeDetector:
                    case plObjectInBoxConditionalObject:
                    case plInterfaceInfoModifier:
                    case plVolumeSensorConditionalObject:
                    case plLogicModifier:
                    case plActivatorConditionalObject:
                    case plFacingConditionalObject:
                    case plOneShotMod:
                    case plAvLadderMod:
                    case plDynaFootMgr:
                    case plPickingDetector:
                    case plCameraRegionDetector:
                    case plHKPhysical:
                    case plSoftVolumeIntersect:
                    case plEAXListenerMod:
                    case plPhysicalSndGroup:
                    case plSeekPointMod:
                    case plRailCameraMod:
                    case plLayerAnimation:
                    case plATCAnim:
                    case plAGMasterMod:
                    case plPanicLinkRegion:
                    case plLineFollowMod:
                    case plMsgForwarder:
                    case plAnimEventModifier:
                    case plMultiStageBehMod:
                    case plImageLibMod:
                    case plLimitedDirLightInfo:
                    case plAgeGlobalAnim:
                    case plDynaPuddleMgr:
                    case plWaveSet7:
                    case plDynamicEnvMap:
                    case plRidingAnimatedPhysicalDetector:
                    case plGrassShaderMod:
                    case plDynamicCamMap:
                    case plSoftVolumeInvert:*/
                    try
                    {
                        object = new PrpRootObject(stream, false, size);
                    }catch(readexception e){}
                    catch(Exception e)
                    {
                        m.err("Unexpected exception: ",e.getMessage());
                        e.printStackTrace();
                        //e.printStackTrace();
                    }
                    //break;
                }
                else
                {
                    //TODO: restore this line, it's just kind of annoying.
                    m.msg("unhandled object type:",desc.objecttype.toString());
                    handled = false;

                    //scan for string references in unparsed object:
                    if(_staticsettings.tryToFindReferencesInUnknownObjects)
                    {
                        //Bytestream bs = new Bytestream(data,offset);
                        shared.IBytestream bs = data.Fork(offset);
                        byte[] bytes = bs.readBytes(size);
                        //Urustring.attemptRecoveryScan(bytes);
                        for(int curbyte=0;curbyte<bytes.length;curbyte++)
                        {
                            if(b.ByteToInt32(bytes[curbyte])==0xF0)
                            {
                                if (curbyte==0) continue;
                                int l = b.ByteToInt32(bytes[curbyte-1]);
                                if (curbyte+l>=bytes.length) continue;
                                byte[] str = new byte[l];
                                for(int i2=0;i2<l;i2++)
                                {
                                    str[i2] = b.not(bytes[curbyte+i2+1]);
                                }
                                if(e.isGoodString(str))
                                {
                                    //_staticsettings.reportFoundUnknownReference(str);
                                    m.msg("Found unknown reference:",b.BytesToString(str));
                                }
                            }
                        }
                    }
                    //break;
                }
                
                if(object==null)
                {
                    handled = false;
                    //m.warn("Prp: object is null");
                }
                if(handled)
                {
                    objects.add(object);
                    
                    //if(object!=null && result!=null) result.objects.add(object);
                    int shortby = offset+size-stream.in.getAbsoluteOffset();
                    if(shortby!=0)
                    {
                        if(desc.objecttype!=Typeid.plHKPhysical)
                            m.msg("Prp: Object was not the expected size. It was off by:",Integer.toString(shortby)+" type="+desc.objecttype.toString());
                    }
                }
                //_staticsettings.onObjectLoaded(object);

                stream.close();
                
            }
        }
        
        //result.objects = uru.generics.convertVectorToArray(objects,PrpRootObject.class);
        result.objects2 = objects;

        //mark the objects that are present in the scenenode:
        result._markScenenodeSceneobjects();

        m.msg("Process All was successful!");
        return result;
        
    }

    public static void DumpObjects(byte[] data, Typeid typetodump, String outfolder)
    {
        context c = context.createFromBytestream(new Bytestream(data));
        
        PrpHeader header = new PrpHeader(c);
        
        //do header work.
        
        //process the object index, which is *not* a part of this struct.
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(new Bytestream(c.in,header.offsetToObjectIndex)));
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(header.offsetToObjectIndex)));
        context c2 = c.Fork(header.offsetToObjectIndex);
        PrpObjectIndex objectindex = new PrpObjectIndex(c2);
        c2.close();
        int numobjecttypes = objectindex.indexCount;
        for(int i=0;i<numobjecttypes;i++)
        {
            Typeid type = objectindex.types[i].type;
            int numObjects = objectindex.types[i].objectcount;
            
            //do per-type work.
            
            for(int j=0;j<numObjects;j++)
            {
                Uruobjectdesc desc = objectindex.types[i].descs[j].desc;
                int offset = objectindex.types[i].descs[j].offset;
                int size = objectindex.types[i].descs[j].size;
                
                //do per-object work.
                if(typetodump==null || desc.objecttype==typetodump)
                {
                    //Bytestream bs = new Bytestream(c.in,offset);
                    shared.IBytestream bs = c.in.Fork(offset);
                    byte[] bytes = bs.readBytes(size);
                    shared.FileUtils.WriteFile(outfolder+"/"+desc.toString()+".dat", bytes);
                }
                
            }
        }
    }

    public static void ReportCrossLinks(byte[] data, String outfolder)
    {
        context c = context.createFromBytestream(new Bytestream(data));
        
        _staticsettings.reportReferences = true;
        _staticsettings.tryToFindReferencesInUnknownObjects = true;
        ProcessAllMoul(c,false, auto.mystAutomation.moulReadable);
        //String report = "Cross-Reference report:\n\n" + _staticsettings.referenceReport.toString() + "\n\nScanned Reference Report:\n\n" + _staticsettings.scannedReferenceReport.toString();
        String report = "howfound;fromname;fromtype;fromnumber;toname;totype;tonumber;topageid\n" + _staticsettings.referenceReport.toString() + _staticsettings.scannedReferenceReport.toString();
        FileUtils.WriteFile(outfolder+"/crosslinkreport.csv", report.getBytes());
        
    }
    
    //doesn't quite work right!
    public static void ReportDeep(byte[] data, String outfolder)
    {
        context c = context.createFromBytestream(new Bytestream(data));
        //_staticsettings.reportReferences = true;
        //_staticsettings.tryToFindReferencesInUnknownObjects = true;
        prpfile prp = prpprocess.ProcessAllObjects(c, false);
        uru.reflection.reflectionReportToFile(prp, outfolder);
        //String report = "Cross-Reference report:\n\n" + _staticsettings.referenceReport.toString() + "\n\nScanned Reference Report:\n\n" + _staticsettings.scannedReferenceReport.toString();
        //FileUtils.WriteFile(_staticsettings.outputdir+"deepreport.txt", report.getBytes());
        
    }
    
    public static class Compiler
    {
        public interface Decider
        {
            boolean isObjectToBeIncluded(Uruobjectdesc desc);
        }
        
        public static Decider getDefaultDecider()
        {
            return new prputils.Compiler.Decider() {
                public boolean isObjectToBeIncluded(Uruobjectdesc desc) {
                    return true;
                }
            };
        }
                
        public static void RecompilePrp(byte[] data, Decider decider, String outfolder)
        {
            context c = context.createFromBytestream(new Bytestream(data));
            //c.outputVertices = true; //works but not used.
            //c.vertices = new Vector<java.lang.Float>(); //works but not used.
            
            
            //prpfile prp = ProcessAllMoul(c,false);
            prpfile prp = prpfile.createFromContext(c, auto.mystAutomation.moulReadable);
            
            Bytedeque fullbyte = RecompilePrp(prp, decider);
            String filename = prp.header.agename.toString()+"_District_"+prp.header.pagename.toString()+".prp";
            //FileUtils.WriteFile(outfolder+"/"+filename, fullbyte);
            fullbyte.writeAllBytesToFile(outfolder+"/"+filename);
        }
        
        public static Bytedeque RecompilePrp(prpfile prp, prpobjects.prputils.Compiler.Decider decider)
        {
            //fix payiferen pageid conflict problem.
            /*if (prp.header.agename.toString().toLowerCase().equals("payiferen"))
            {
                _staticsettings.sequencePrefix = 0x63;
            }
            else if (prp.header.agename.toString().toLowerCase().equals("kveer"))
            {
                _staticsettings.sequencePrefix = 0x62;
            }
            else if (prp.header.agename.toString().toLowerCase().equals("edertsogal"))
            {
                _staticsettings.sequencePrefix = 0x61;
            }*/
            //else if (prp.header.agename.toString().toLowerCase().equals("marshscene"))
            //{
            //    _staticsettings.sequencePrefix = 96;
            //}
            //else if (prp.header.agename.toString().toLowerCase().equals("mountainscene"))
            //{
            //    _staticsettings.sequencePrefix = 95;
            //}

            //fix problem with materials(referenced from plDrawableSpans) that point to LayerAnimations.
            //fixMaterial(prp);
            

            ////count # of object types //we don't need to do this, since the old version doesn't have the count in the header.

            Bytedeque headerdeque = new Bytedeque(shared.Format.pots);
            Bytedeque oideque = new Bytedeque(shared.Format.pots);

            //calculate header size and create first part of header deque. in=prp out=headerdeque
            PrpHeader header = prp.header;
            headerdeque.writeInt(5);
            header.pageid.compile(headerdeque);
            //headerdeque.writeShort(header.pagetype);
            header.pagetype.compile(headerdeque);
            prp.header.agename.compile(headerdeque);
            byte[] districtbytes = {'D','i','s','t','r','i','c','t'};
            Urustring district = new Urustring(districtbytes);
            district.compile(headerdeque);
            prp.header.pagename.compile(headerdeque);
            headerdeque.writeShort((short)63);
            headerdeque.writeShort((short)12);
            headerdeque.writeInt(0);
            headerdeque.writeInt(8);
            //byte[] header1 = headerdeque.getAllBytes();
            //int headersize = header1.length + 12;
            //int curpos = headersize; //put our pointer to the end of the header.

            //filter and compile objects: in=prp out=compiledObjects,uncompiledObjects
            Vector<byte[]> compiledObjects = new Vector<byte[]>();
            Vector<PrpRootObject> uncompiledObjects = new Vector<PrpRootObject>();
            //java.util.Iterator<rootobj> iter = prp.objects.iterator();
            boolean haveEncounteredSceneNode = false;
            //while(iter.hasNext())
            int numobjs = prp.objects2.size();
            for(int i=0;i<numobjs;i++)
            {
                //rootobj curobj = iter.next();
                PrpRootObject curobj = prp.objects2.get(i);
                
                //skip this object if it's tagged as deleted.
                if(curobj.tagDeleted)
                {
                    m.msg("Skipping RootObject tagged as deleted.");
                    continue;
                }
                
                //if(i==494)
                //{
                //    i = 494;
                //}
                //Typeid type = null;
                //try{
                //Objheader alk = curobj.getHeader();
                //Uruobjectdesc dlk = alk.desc;
                //type = dlk.objecttype;
                Typeid type = curobj.header.desc.objecttype;
                //}catch(Exception e)
                //{
                //    int ai=0;
                //}
                //e.equals(null);}
                //int number = curobj.getHeader().desc.objectnumber;
                //String name = curobj.getHeader().desc.objectname.toString();
                //Pageid pageid = curobj.getHeader().desc.pageid;

                //handle normal objects
                //if(isNormalObjectToBeIncluded(curobj.header.desc) || type==type.plSceneNode)
                if(decider.isObjectToBeIncluded(curobj.header.desc) || type==type.plSceneNode)
                {
                    uncompiledObjects.add(curobj);
                    
                    Bytedeque deque = new Bytedeque(shared.Format.pots);
                    deque.curRootObject = curobj.header.desc;
                    deque.prp = prp;
                    if(type==type.plSceneNode) //handle scene node; there should only be one of these per prp.
                    {
                        e.ensure(haveEncounteredSceneNode==false);
                        haveEncounteredSceneNode = true;
                        curobj.header.compile(deque);
                        //curobj.castTo(x0000Scenenode.class).regenerateAllSceneobjectsFromPrpRootObjects(uncompiledObjects);
                        //curobj.castTo(x0000Scenenode.class).compile(deque);
                        //((x0000Scenenode)curobj.prpobject.object).compileSpecial(deque, decider); //uses the isNormalObjectToBeIncluded function.
                        curobj.castTo(x0000Scenenode.class).compileSpecial(deque, prp.objects2, decider);
                    }
                    else
                    {
                        curobj.header.compile(deque);
                        curobj.compile(deque);
                    }
                    byte[] dequedata = deque.getAllBytes();
                    compiledObjects.add(dequedata);
                }

                    
            }
            //e.ensure(haveEncounteredSceneNode==true); //only relevant to prps with a scene node, not BuiltIn and Textures.
            
            //calculate size of all objects, type count, count for each type. in=compiledObjects, uncompiledObjects out=sizeOfAllObjects,typeInfo.
            int sizeOfAllObjects = 0;
            int numObjects = uncompiledObjects.size(); e.ensure(numObjects==compiledObjects.size());
            Typeid lasttype = Typeid.nil; //since there should be nil rootobj we this will change on the first loop.
            int numTypes = 0;
            //Vector<Integer> typeCounts = new Vector<Integer>();
            //java.util.EnumMap<Typeid,Integer> typeInfo = new java.util.EnumMap<Typeid,Integer>();
            java.util.HashMap<Typeid,Integer> typeInfo = new java.util.HashMap<Typeid,Integer>();
            for(int i3=0;i3<numObjects;i3++)
            {
                sizeOfAllObjects += compiledObjects.get(i3).length;
                PrpRootObject curobj = uncompiledObjects.get(i3);
                if(curobj.header.objecttype!=lasttype) //if new type encountered...
                {
                    lasttype = curobj.header.objecttype;
                    numTypes++;
                    
                    //make a count entry for this new type
                    e.ensure(!typeInfo.containsKey(lasttype)); //we shouldn't already have this key.
                    typeInfo.put(lasttype, 1);
                }
                else
                {
                    //increment count for this type
                    //int oldcount = typeCounts.get(numTypes);
                    //typeCounts.set(numTypes, oldcount+1);
                    int oldcount = typeInfo.get(lasttype);
                    typeInfo.put(lasttype, oldcount + 1);
                }
            }
            
            //compile object index. in=headerdeque,oideque,uncompiledObjects,compiledObjects out=headersize,oideque
            int headersize = headerdeque.getAllBytes().length + 12;
            int offset = headersize; //put our pointer to the end of the header.
            Typeid lasttype2 = Typeid.nil;
            oideque.writeInt(numTypes);
            for(int i=0;i<numObjects;i++)
            {
                PrpRootObject curobj = uncompiledObjects.get(i);
                if(curobj.header.objecttype!=lasttype2) //if new type encountered...
                {
                    lasttype2 = curobj.header.objecttype;
                    lasttype2.compile(oideque); //add typeid
                    //java.util.
                    int objcount = typeInfo.get(lasttype2);
                    oideque.writeInt(objcount); //add typecount
                }
                curobj.header.desc.compile(oideque); //add desc
                oideque.writeInt(offset); //add offset.
                int objectsize = compiledObjects.get(i).length;
                oideque.writeInt(objectsize); //add size.
                
                offset += objectsize; //move forward the size of the object.
            }
            
            //finish compiling header. in=headerdeque,oideque,sizeofallobjects,headersize. out=headerdeque
            int filedatasize = sizeOfAllObjects + oideque.getAllBytes().length;
            int firstoffset = headersize;
            int indexoffset = headersize + sizeOfAllObjects;// + oideque.getAllBytes().length;
            headerdeque.writeInt(filedatasize);
            headerdeque.writeInt(firstoffset);
            headerdeque.writeInt(indexoffset);
            
            //put it all together. in=headerdeque, compiledObjects, oideque. out=fulldeque
            Bytedeque fulldeque = new Bytedeque(shared.Format.pots);
            fulldeque.writeBytedeque(headerdeque);
            for(int i=0;i<numObjects;i++)
            {
                fulldeque.writeBytes(compiledObjects.get(i));
            }
            fulldeque.writeBytedeque(oideque);
            
            //save to file. in=fulldeque out=!!!
            //byte[] fullbyte = fulldeque.getAllBytes();
            //String filename = header.agename.toString()+"_District_"+header.pagename.toString()+".prp";
            //FileUtils.WriteFile(_staticsettings.outputdir+filename, fullbyte);

            //cleanup.
            //fix payiferen pageid conflict problem.
            //if (prp.header.agename.toString().toLowerCase().equals("payiferen"))
            //{
                //_staticsettings.sequencePrefix = 0x00;
            //}
            m.msg("Recompilated completed!");
            //return new Bytes(fullbyte);
            return fulldeque;
        }
        
        //a bit of a hack, to replace LayerAnimations with other materials.
        public static void fixMaterial(prpfile prp)
        {
            int numobjs = prp.objects2.size();
            Uruobjectdesc stableMaterial = null; //the material we can use instead of LayerAnimations.
            Vector<Uruobjectref> badMaterials = new Vector<Uruobjectref>();
            
            for(int i=0;i<numobjs;i++)
            {
                if(prp.objects2.get(i).header.desc.objecttype==Typeid.plDrawableSpans)
                {
                    plDrawableSpans curDrawableSpan = (plDrawableSpans)prp.objects2.get(i).prpobject.object;
                    
                    int numMaterials = curDrawableSpan.materialsCount;
                    for(int j=0;j<numMaterials;j++) //for each material...
                    {
                        Uruobjectref materialRef = curDrawableSpan.materials.get(j);
                        if(materialRef.hasRef!=0)
                        {
                            PrpRootObject curMaterial1 = prp.findObjectWithDesc(materialRef.xdesc);
                            if(curMaterial1!=null) //if null, then it should be in another page,
                            {
                                x0007Material curMaterial = (x0007Material)curMaterial1.prpobject.object;
                                boolean badmaterial = false;
                                //for(int k=0;k<curMaterial.layercount;k++)
                                for(Uruobjectref layerref: curMaterial.layerrefs)
                                {
                                    //Typeid layerType = curMaterial.layerrefs[k].xdesc.objecttype;
                                    Typeid layerType = layerref.xdesc.objecttype;
                                    if(layerType == Typeid.plLayerAnimation)
                                    {
                                        //replace this material;
                                        /*curDrawableSpan.materials[j].xdesc = stableMaterial;
                                        if(stableMaterial==null) m.err("No stable material given.");*/
                                        badMaterials.add(curDrawableSpan.materials.get(j));
                                        badmaterial = true;
                                    }
                                }
                                //if this material is the first good one, make it the stable material.
                                if(!badmaterial && stableMaterial==null) stableMaterial = curDrawableSpan.materials.get(j).xdesc;
                            }
                            else
                            {
                                m.warn("Material is not present in this page. I don't know if it uses a plLayerAnimation.");
                            }
                        }
                    }
                }
            }
            
            if( (badMaterials.size()!=0) && (stableMaterial==null) )
            {
                m.err("Unable to find *any* stable materials.");
            }
            else
            {
                for(int i=0;i<badMaterials.size();i++)
                {
                    badMaterials.get(i).xdesc = stableMaterial;
                }
            }
        }
        
    }
    

    //seems to no longer be used:
    /*public static prpfile ProcessPotsPrp(context c)//(byte[] data)
    {
        //context c = context.createDefault(new Bytestream(data));
        //c.readversion = 3; //read as pots
        
        //Bytestream data = c.in;

        PrpHeader header = new PrpHeader(c);
        
        //do header work.
        prpfile result = new prpfile();
        Vector<PrpRootObject> objects = new Vector<PrpRootObject>();
        result.header = header;
        
        //process the object index, which is *not* a part of this struct.
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(new Bytestream(c.in,header.offsetToObjectIndex)));
        //PrpObjectIndex objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(header.offsetToObjectIndex)));
        context c2 = c.Fork(header.offsetToObjectIndex);
        PrpObjectIndex objectindex = new PrpObjectIndex(c2);
        c2.close();
        result.objectindex = objectindex;
        
        int numobjecttypes = objectindex.indexCount;
        for(int i=0;i<numobjecttypes;i++)
        {
            Typeid type = objectindex.types[i].type;
            int numObjects = objectindex.types[i].objectcount;
            
            //do per-type work.
            for(int j=0;j<numObjects;j++)
            {
                Uruobjectdesc desc = objectindex.types[i].descs[j].desc;
                int offset = objectindex.types[i].descs[j].offset;
                int size = objectindex.types[i].descs[j].size;
                
                //do per-object work.
                PrpRootObject object = null;
                //context stream = c.Fork(new Bytestream(c.in,offset));
                //context stream = c.Fork(c.in.Fork(offset));
                context stream = c.Fork(offset);
                stream.curRootObject = desc;
                stream.curRootObjectOffset = offset;
                stream.curRootObjectSize = size;
                stream.curRootObjectEnd = offset+size;

                //process the object, which is *not* a part of this struct.
                //to disable an object type, simply comment out its "object=" line.
                
                //This next line can be used to skip all but a single type.
                //if(desc.objecttype!=Typeid.plBoundInterface) continue;

                boolean handled = true;
                switch(desc.objecttype)
                {
                    case plBoundInterface:
                    case plSceneNode:
                    case plSceneObject:
                    case plMipMap:
                    case plCubicEnvironMap:
                    case plLayer:
                    case hsGMaterial:
                    case plPointShadowMaster:
                    case plParticleSystem:
                    case plAudioInterface:
                    case plWinAudio:
                    case plCoordinateInterface:
                    case plDrawInterface:
                    case plSpawnModifier:
                    case plDrawableSpans:
                    case plDirectionalLightInfo:
                    case plOmniLightInfo:
                    case plViewFaceModifier:
                    case plPythonFileMod:
                    case plLayerAnimation:
                    case plHKPhysical:
                    case plStereizer:
                    case plSoundBuffer:
                    case plRandomSoundMod:
                    case plWin32StreamingSound:
                    case plWin32StaticSound:
                    case plParticleCollisionEffectDie:
                    case plParticleLocalWind:
                    case plSimulationInterface:
                    case plExcludeRegionModifier:
                    case plCameraBrain1:
                    case plCameraBrain1_Avatar:
                    case plCameraBrain1_Fixed:
                    case plCameraBrain1_Circle:
                    case plCameraModifier1:
                    case plAGModifier:
                    case plOccluder:
                    case plDynamicTextMap:
                    case plParticleCollisionEffectBounce:
                    case plSpotLightInfo:
                    case plShadowCaster:
                    case plDirectShadowMaster:
                    case plRelevanceRegion:
                    case plSoftVolumeSimple:
                    case plResponderModifier:
                    case plParticleFlockEffect:
                    case plFadeOpacityMod:
                    case plClusterGroup:
                    case plVisRegion:
                    case plSoftVolumeUnion:
                    case plObjectInVolumeDetector:
                    case plObjectInBoxConditionalObject:
                    case plInterfaceInfoModifier:
                    case plVolumeSensorConditionalObject:
                    case plLogicModifier:
                    case plActivatorConditionalObject:
                    case plFacingConditionalObject:
                    case plOneShotMod:
                    case plAvLadderMod:
                    case plDynaFootMgr:
                    case plPickingDetector:
                    case plCameraRegionDetector:
                    case plSoftVolumeIntersect:
                    case plEAXListenerMod:
                    case plPhysicalSndGroup:
                    case plSeekPointMod:
                    case plRailCameraMod:
                    case plATCAnim:
                    case plAGMasterMod:
                    case plPanicLinkRegion:
                    case plLineFollowMod:
                    case plMsgForwarder:
                    case plAnimEventModifier:
                    case plMultiStageBehMod:
                    case plSittingModifier:
                    case plImageLibMod:
                    case plLimitedDirLightInfo:
                    case plAgeGlobalAnim:
                    case plDynaPuddleMgr:
                    case plWaveSet7:
                    case plDynamicEnvMap:
                    case plRidingAnimatedPhysicalDetector:
                    case plGrassShaderMod:
                    case plDynamicCamMap:
                    case plSoftVolumeInvert:
                        try
                        {
                            object = new PrpRootObject(stream, false, size);
                        }
                        catch(readexception e)
                        {
                            
                        }
                        break;
                    default:
                        m.msg("unhandled object type:",desc.objecttype.toString());
                        handled = false;
                        break;
                }
                if(object==null) handled = false;
                if(handled)
                {
                    objects.add(object);
                    
                    int shortby = offset+size-stream.in.getAbsoluteOffset();
                    if(shortby!=0)
                    {
                        //if(desc.objecttype!=Typeid.plHKPhysical)
                            m.msg("Prp: Object was not the expected size. It was off by:",Integer.toString(shortby));
                    }
                }
                stream.close();
            }
        }
        
        m.msg("Process All was successful!");
        //result.objects = uru.generics.convertVectorToArray(objects,PrpRootObject.class);
        result.objects2 = objects;
        return result;
        
    }*/
    public static void findAllObjectsOfType(String prpdir, Typeid type)
    {
        class callback implements prpobjects.allprpfiles.RootobjCallbackInterface
        {
            Typeid type;
            public callback(Typeid type2)
            {
                type = type2;
            }
            
            public void handleRootobj(prpfile prp, PrpObjectIndex.ObjectindexObjecttypeObjectdesc obj)
            {
                if(obj.desc.objecttype==type)
                {
                    int dummybreakpoint=0;
                }
            }
        }
        
        prpobjects.allprpfiles.parseAllRootobjs(new callback(type),prpdir);
        
    }
    public static void FindDrawInterfacesThatUseLayerAnimations(byte[] data)
    {
        StringBuilder report = new StringBuilder();
        
        context c = context.createFromBytestream(new Bytestream(data));
        prpfile prp = ProcessAllMoul(c,false,auto.mystAutomation.moulReadable);
        for(int i=0;i<prp.objects2.size();i++)
        {
            PrpRootObject curobj = prp.objects2.get(i);
            if(curobj.header.desc.objecttype==Typeid.plDrawInterface)
            {
                plDrawInterface di = (plDrawInterface)curobj.prpobject.object;
                for(int j=0;j<di.subsetgroupcount;j++)
                {
                    int subsetgroup = di.subsetgroups[j].subsetgroupindex;
                    if(subsetgroup==-1) continue;
                    Uruobjectdesc spandesc = di.subsetgroups[j].span.xdesc;
                    plDrawableSpans span = prp.findObjectWithDesc(spandesc).castTo();//x004CDrawableSpans.class);
                    int numsubsets = span.DIIndices[subsetgroup].indicesCount;
                    for(int k=0;k<numsubsets;k++)
                    {
                        int subset = span.DIIndices[subsetgroup].indices[k];
                        if(subset>=span.icicles.length)
                        {
                            m.warn("Subset is not present.");
                        }
                        else
                        {
                            int material = span.icicles[subset].parent.parent.materialindex;
                            Uruobjectdesc matdesc = span.materials.get(material).xdesc;
                            if(matdesc.objectname.toString().toLowerCase().startsWith("crater"))
                            {
                                int dummy=0;
                            }
                            x0007Material mat = prp.findObjectWithDesc(matdesc).castTo();//x0007Material.class);
                            //for(int l=0;l<mat.layercount;l++)
                            for(Uruobjectref layerref: mat.layerrefs)
                            {
                                //Typeid mattype = mat.layerrefs[l].xdesc.objecttype;
                                Typeid mattype = layerref.xdesc.objecttype;
                                if(mattype==Typeid.plLayerAnimation)
                                {
                                    report.append("LayerAnimation found in: "+curobj.header.toString()+"\n");
                                }
                                else
                                {
                                    /*x0006Layer layer = prputils.findObjectWithDesc(prp, mat.layerrefs[l].xdesc).castTo();
                                    if(layer.texture.xdesc.objectname.toString().toLowerCase().startsWith("flatcraterinterior"))
                                    {
                                        int dummy=0;
                                    }*/
                                }
                            }
                        }
                    }
                    
                }
            }
        }
        //FileUtils.WriteFile(_staticsettings.outputdir+"LayerAnimationReport.txt", report.toString().getBytes());
    }
    public static PrpRootObject[] FindAllObjectsOfType(prpfile prp, Typeid type)
    {
        Vector<PrpRootObject> result = new Vector<PrpRootObject>();
        
        int numobjs = prp.objects2.size();
        for(int i=0;i<numobjs;i++)
        {
            PrpRootObject curobj = prp.objects2.get(i);
            if(curobj!=null && curobj.header.desc.objecttype==type)
            {
                result.add(curobj);
            }
        }
        
        PrpRootObject[] result2 = new PrpRootObject[0];
        result2 = result.toArray(result2);
        return result2;
    }
    
    public static PrpRootObject[] FindAllObjectsWithName(prpfile prp, String name)
    {
        Vector<PrpRootObject> result = new Vector<PrpRootObject>();
        String name2 = name.toLowerCase();
        
        int numobjs = prp.objects2.size();
        for(int i=0;i<numobjs;i++)
        {
            PrpRootObject curobj = prp.objects2.get(i);
            if(curobj!=null && curobj.header.desc.objectname.toString().toLowerCase().equals(name2))
            {
                result.add(curobj);
            }
        }
        
        PrpRootObject[] result2 = new PrpRootObject[0];
        result2 = result.toArray(result2);
        return result2;
    }
    
    
}

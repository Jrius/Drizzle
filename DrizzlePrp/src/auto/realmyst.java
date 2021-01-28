/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import shared.*;
import realmyst.*;
import export3ds.*;
import java.util.Vector;
import java.io.File;
import java.util.HashMap;

public class realmyst
{
    public static void testrun2(Vector<Sdb> sdbs, Vector<Mdb> mdbs)
    {
        Primary main = Primary.createNull();
        HashMap<Integer, Count3Undone> materialmap = new HashMap();
        for(Sdb sdb: sdbs)
        {
            if(!sdb.sourceName.endsWith("70919002.vdb")) continue;
            
            for(int matindex=0;matindex<sdb.count3s.length;matindex++)
            {
                Count3Undone c3 = sdb.count3s[matindex];
                
                if(c3.possibilities.length!=1) continue;
                int textureIndex = c3.possibilities[0];
                if(textureIndex==-1) continue;

                //create material.
                String textureFilename = sdb.count2s[textureIndex].textureFilename.toString();
                String matname = c3.sb2.str.toString();
                //String matname = "mat"+Integer.toString(matindex);
                Material mat = Material.create(matname);
                mat.texturemap = TextureMap.create(textureFilename+".dds");
                //apply uv transform while we're here.
                Matrix matrix = null;
                if(c3.subs!=null && c3.subs.length>0) matrix = c3.subs[0].m400;
                if(matrix!=null)
                {
                    Flt uscale = matrix.values[0][0];
                    Flt vscale = matrix.values[1][1];
                    Flt uoffset = matrix.values[0][3];
                    Flt voffset = matrix.values[1][3];
                    voffset.multModify(-1.0f);
                    mat.texturemap.uoffset = UOffset.create(uoffset.toJavaFloat());
                    mat.texturemap.voffset = VOffset.create(voffset.toJavaFloat());
                    mat.texturemap.uscale = UScale.create(uscale.toJavaFloat());
                    mat.texturemap.vscale = VScale.create(vscale.toJavaFloat());
                }

                main.meshdata.mats.add(mat);
                    
                materialmap.put(matindex, c3);
            }
            
        }
        
        for(Mdb mdb: mdbs)
        {
            if(!mdb.name.toString().equals("myst..portstairstep")) continue;
            
            NamedObj namedobj = createnamedobj(mdb,materialmap);
            main.meshdata.objs.add(namedobj);
        }
        
        //save 3ds file.
        IBytedeque out = new Bytedeque2(shared.Format.realmyst);
        main.compile(out);
        byte[] filedata = out.getAllBytes();
        FileUtils.WriteFile("c:/hsmout/000test.3ds", filedata);
    }
    public static NamedObj createnamedobj(Mdb mdb, HashMap<Integer, Count3Undone> materialmap)
    {
        String ignorereason = "";
        if(mdb.fs==null) ignorereason += "Skipping NamedObj because it has no fs. ";
        if(mdb.bunch==null) ignorereason += "Skipping NamedObj because it has no bunch. ";
        if(mdb.whas==null) ignorereason += "Skipping NamedObj because it has no whas. ";
        if(mdb.trips==null) ignorereason += "NamedObj has no trips.";
        if(!ignorereason.equals("")) throw new ignore(ignorereason);
        
        Vertex[] verts = new Vertex[mdb.fs.length];
        for(int i=0;i<mdb.fs.length;i++)
        {
            int to = mdb.fs[i].v2;
            int from = mdb.fs[i].v3;
            Flt x= mdb.bunch[to].f1;
            Flt y= mdb.bunch[to].f2;
            Flt z= mdb.bunch[to].f3;
            verts[i] = Vertex.createFromFlts(x, y, z);
        }

        FltPair[] uvcoords = new FltPair[mdb.trips.length];
        for(int i=0;i<mdb.trips.length;i++)
        {
            Flt u = mdb.trips[i].x;
            Flt v = mdb.trips[i].y;
            Flt w = mdb.trips[i].z;
            Flt u2 = Flt.createFromJavaFloat(1.0f - u.toJavaFloat()); //v = 1 - v;
            Flt v2 = Flt.createFromJavaFloat(1.0f - v.toJavaFloat()); //v = 1 - v;
            Flt w2 = Flt.createFromJavaFloat(1.0f - w.toJavaFloat()); //v = 1 - v;
            uvcoords[i] = FltPair.createFromFlts(u, v2);
        }

        ShortTriplet[] faces = new ShortTriplet[mdb.whas.length];
        for(int i=0;i<mdb.whas.length;i++)
        {
            int v1 = mdb.whas[i].u6;
            int v2 = mdb.whas[i].u7;
            int v3 = mdb.whas[i].u8;
            faces[i] = ShortTriplet.createFromShorts((short)v1, (short)v2, (short)v3);
            Flt uscale = mdb.whas[i].f12;
            //uvcoords[v1].u.multModify(uscale);
            //uvcoords[v2].u.multModify(uscale);
            //uvcoords[v3].u.multModify(uscale);
        }

        String objname = mdb.name.toString();
        
        NamedObj newobj = NamedObj.createNull(objname);
        newobj.namedTriangleObject = NamedTriangleObject.createNull();
        newobj.namedTriangleObject.points = PointArray.create(verts);
        //newobj.namedTriangleObject.faces = FaceArray.create(faces, "defaultmat");
        newobj.namedTriangleObject.faces = FaceArray.createNull();
        newobj.namedTriangleObject.faces.facecount = check.intToShort(faces.length);
        newobj.namedTriangleObject.faces.faces = new FaceArray.tdsface[faces.length];
        for(int i=0;i<faces.length;i++)
        {
            short v1 = faces[i].p;
            short v2 = faces[i].q;
            short v3 = faces[i].r;
            short flags = 0;
            newobj.namedTriangleObject.faces.faces[i] = new FaceArray.tdsface(v1,v2,v3,flags);
        }
        for(int curmat=0;curmat<mdb.ii.count;curmat++)
        {
            int curmatindex = mdb.ii.indices[curmat];
            Count3Undone c3= materialmap.get(curmatindex);
            String curmatname = c3.sb2.str.toString();
            int curmatvertcount = 0;
            for(int i=0;i<mdb.ifi.values.length;i++)
            {
                if(mdb.ifi.values[i]==curmatindex) curmatvertcount++;
            }
            short[] facesToApplyTo = new short[curmatvertcount];
            int curface = 0;
            for(int i=0;i<mdb.ifi.values.length;i++)
            {
                if(mdb.ifi.values[i]==curmatindex)
                {
                    facesToApplyTo[curface] = (short)i;
                    curface++;
                    
                    //apply uv transform while we're here.
                    //Matrix matrix = c3.subs[0].m400;
                    //Flt uscale = matrix.values[0][0];
                    //Flt vscale = matrix.values[1][1];
                    //Flt uoffset = matrix.values[0][3];
                    //Flt voffset = matrix.values[1][3];
                    //int v1 = mdb.whas[i].u6;
                    //int v2 = mdb.whas[i].u7;
                    //int v3 = mdb.whas[i].u8;
                    //uvcoords[v1].u.multModify(uscale);
                    //uvcoords[v1].u.addModify(uoffset);
                    //uvcoords[v2].u.multModify(uscale);
                    //uvcoords[v2].u.addModify(uoffset);
                    //uvcoords[v3].u.multModify(uscale);
                    //uvcoords[v3].u.addModify(uoffset);
                }
            }
            MeshMatGroup mmg = MeshMatGroup.create(curmatname, facesToApplyTo);
            newobj.namedTriangleObject.faces.mats.add(mmg);
        }
        
        
        for(ShortTriplet face: faces)
        {
            if(face.p >= verts.length || face.q>= verts.length || face.r>=verts.length)
            {
                int dummy=0;
            }
        }
        if(uvcoords.length!=0)
        {
            if(uvcoords.length!=verts.length)
            {
                int dummy=0;
            }
            else
            {
                if(uvcoords.length!=0) newobj.namedTriangleObject.uvcoords = UvVerts.create(uvcoords);
                
            }
        }

        return newobj;
    }
    public static void testrun(Vector<Sdb> sdbs, Vector<Mdb> mdbs)
    {
        Primary main = Primary.createNull();
        //main.meshdata.mat = Material.create("defaultmat");
        //add texture filename...
        //main.meshdata.mat.texturemap = TextureMap.create("active.hsm.dds");

        String[] mystrooms = auto.realmyst.findRoomInfo(sdbs,"Myst");
        //Vector<realmyst.Mdb> mystmdbs = automation.realmyst.filterMdbsByRoom(mdbs, mystrooms);
        //automation.realmyst.save3dsFile(mystmdbs);
        int matnum = 0;
        for(Sdb sdb: sdbs)
        {
            if(sdb.sourceName.endsWith("70919002.vdb"))
            {
                int i=0;
                for(Count2 c2: sdb.count2s)
                {
                    String texsrch = "stonestep.hsm";//"GBdock.hsm";
                    if(c2.textureFilename.toString().equals(texsrch))
                    {
                        int dummy=0;
                    }
                    i++;
                }
                int j=0;
                for(Count3Undone c3: sdb.count3s)
                {
                    int textureIndex = c3.possibility;
                    if(textureIndex!=-1)
                    {
                        String textureFilename = sdb.count2s[textureIndex].textureFilename.toString();
                        String name = c3.sb2.str.toString();
                        if(textureIndex==185)
                        {
                            int dummy=0;
                        }
                        if(name.startsWith("myst..portstairstep"))//"myst..portfloor"))
                        {
                            int dummy=0;
                        }
                        for(Mdb mdb: mdbs)
                        {
                            String mdbname = mdb.name.toString();
                            if(name.equals(mdbname))
                            {
                                matnum++;
                                String matname = "mat"+Integer.toString(matnum);
                                Material mat = Material.create(matname);
                                mat.texturemap = TextureMap.create(textureFilename+".dds");
                                main.meshdata.mats.add(mat);
                                
                                try
                                {
                                    NamedObj obj = createNamedObj(mdb);//, main, sdb);
                                    obj.namedTriangleObject.faces.mats.get(0).name = Ntstring.createFromString(matname);
                                    main.meshdata.objs.add(obj);
                                }
                                catch(ignore e)
                                {
                                    int dummy=0;
                                }
                                int dummy=0;
                            }
                        }
                    }
                    j++;
                }
            }
        }
        
        IBytedeque out = new Bytedeque2(shared.Format.realmyst);
        main.compile(out);
        byte[] filedata = out.getAllBytes();
        FileUtils.WriteFile("c:/hsmout/000test.3ds", filedata);
        
    }
    public static Vector<Mdb> filterMdbsByRoom(Vector<Mdb> mdbs, String[] rooms)
    {
        Vector<Mdb> result = new Vector();
        for(Mdb mdb: mdbs)
        {
            String objname = mdb.name.toString();
            //String oname = mdb.name.toString().toLowerCase();
            int ind = objname.indexOf("..");
            if(ind==-1)
            {
                m.msg("objectname has no .. : "+objname);
                break;
            }
            
            String curroom = objname.substring(0, ind);
            m.msg("objectname: "+curroom);
            for(String room: rooms)
            {
                if(room.equals(curroom))
                {
                    result.add(mdb);
                }
            }
            //if(mdb.name.toString().toLowerCase().startsWith("myst.."))
            //{
            //    mdbs.add(mdb);
            //}
        }
        return result;
    }
    public static Vector<Hsm> readAllHsms(String folder)
    {
        File f = new File(folder+"/scn/maps");
        //realmyst.rmcontext.get().curnum=0;
        Vector<Hsm> hsms = new Vector<Hsm>();
        int count = 0;
        for(File child: f.listFiles())
        {
            if(child.getName().toLowerCase().endsWith(".hsm"))
            {
                //realmyst.rmcontext.get().curnum++;
                count++;
                //if(count>400) break;
                try
                {
                    int fs = (int)child.length();
                    shared.IBytestream bs = shared.SerialBytestream.createFromFile(child);
                    Hsm hsm = new Hsm(bs,child.getName());
                    int offset = bs.getAbsoluteOffset();
                    int bytesleft = bs.getBytesRemaining();

                    //if (mdb.filesizeMinusHeader!=fs-offset)
                    //{
                    //    int dummy=0;
                    //}
                    if(bytesleft!=0)
                    {
                        int dummy=0;
                    }

                    //String oname = hsm.name.toString().toLowerCase();
                    //int ind = oname.indexOf("..");
                    //if(ind==-1) m.msg("objectname has no ..");
                    //else m.msg("objectname: "+oname.substring(0, ind));
                    //if(mdb.name.toString().toLowerCase().startsWith("myst.."))
                    //{
                    //    mdbs.add(mdb);
                    //}

                    hsms.add(hsm);

                    int dummy=0;
                }
                catch(shared.ignore e)
                {
                    m.warn("Error so skipping file.");
                }
            }
        }
        return hsms;
    }
    public static Vector<Mdb> readAllMdbs(String folder)
    {
        File f = new File(folder+"/mdb");
        rmcontext.get().curnum=0;
        Vector<Mdb> mdbs = new Vector<Mdb>();
        int count = 0;
        for(File child: f.listFiles())
        {
            if(child.getName().toLowerCase().endsWith(".vdb"))
            {
                rmcontext.get().curnum++;
                count++;
                //if(count>400) break;
                try
                {
                    int fs = (int)child.length();
                    shared.IBytestream bs = shared.SerialBytestream.createFromFile(child);
                    Mdb mdb = new Mdb(bs,"102445243.vdb");
                    int offset = bs.getAbsoluteOffset();
                    int bytesleft = bs.getBytesRemaining();

                    //if (mdb.filesizeMinusHeader!=fs-offset)
                    //{
                    //    int dummy=0;
                    //}
                    if(bytesleft!=0)
                    {
                        int dummy=0;
                    }

                    //String oname = mdb.name.toString().toLowerCase();
                    //int ind = oname.indexOf("..");
                    //if(ind==-1) m.msg("objectname has no ..");
                    //else m.msg("objectname: "+oname.substring(0, ind));
                    //if(mdb.name.toString().toLowerCase().startsWith("myst.."))
                    //{
                    mdbs.add(mdb);
                    //}

                    int dummy=0;
                }
                catch(shared.ignore e)
                {
                    m.warn("Error so skipping file.");
                }
            }
        }
        return mdbs;
    }
    public static Vector<Sdb> readAllSdbs(String folder)
    {
        File f = new File(folder+"/sdb");
        Vector<Sdb> sdbs = new Vector();
        for(File child: f.listFiles())
        {
            if(child.getName().toLowerCase().endsWith(".vdb"))
            {
                try
                {
                    int fs = (int)child.length();
                    shared.IBytestream bs = shared.SerialBytestream.createFromFile(child);
                    Sdb sdb = new Sdb(bs);
                    int offset = bs.getAbsoluteOffset();
                    int bytesleft = bs.getBytesRemaining();

                    if (sdb.filesizeMinusHeader!=fs-offset)
                    {
                        int dummy=0;
                    }
                    if(bytesleft!=0)
                    {
                        int dummy=0;
                    }
                    int dummy=0;

                    sdbs.add(sdb);
                }
                catch(shared.ignore e)
                {
                    m.warn("Error so skipping file.");
                }
            }
        }
        return sdbs;
    }

    public static String[] findRoomInfo(Vector<Sdb> sdbs, String agecode)
    {
        //Valid agecodes are: Channel, Dni, Mech, Rime, Sel, Stone, Myst
        String soughtObject;
        if(agecode.equals("Myst"))
            soughtObject = "HoldingPen_Channel";
        else if(agecode.equals("Channel")||agecode.equals("Channel")||agecode.equals("Channel")||agecode.equals("Channel")||agecode.equals("Channel")||agecode.equals("Channel")||agecode.equals("Channel"))
            soughtObject = "HoldingPen_"+agecode+"ToMyst";
        else throw new uncaughtexception("realMyst Agename wasn't known, it was probably a typo.:"+agecode);
        
        for(Sdb sdb: sdbs)
        {
            String objname = sdb.name.toString();
            if(objname.startsWith("HoldingPen_"))
            {
                String room = sdb.strs[0].toString();
                if(room.equals("global"))
                {
                    if(objname.equals(soughtObject))
                    {
                        Vector<String> list2500=new Vector();
                        Vector<String> list2501=new Vector();
                        for(Count10.occref occ: sdb.count10s[0].occrefs1)
                        {
                            if(occ.u3==2500) list2500.add(occ.subs[0].xstr.toString());
                            if(occ.u3==2501) list2501.add(occ.subs[0].xstr.toString());
                        }
                        //String list1 = sdb.count10s[0].occrefs1[2].subs[0].xstr.toString();
                        //String list2 = sdb.count10s[0].occrefs1[3].subs[0].xstr.toString();
                        
                        String[] result = list2501.get(0).split(",");
                        return result;
                    }                    
                    
                    int dummy=0;
                }
                int dummy=0;
            }
            int dummy=0;
        }
        return null;
    }
    
    public static void saveDdsFiles(Vector<Hsm> hsms, String outfolder)
    {
        for(Hsm hsm: hsms)
        {
            int compressionType = hsm.getCompressionType();
            if(compressionType==0)
            {
                IBytedeque out = new Bytedeque2(shared.Format.realmyst);
                images.dds.createFromUncompressed(out, hsm.xagrb, hsm.widthMaybe, hsm.heightMaybe);
                byte[] outdata = out.getAllBytes();
                FileUtils.WriteFile(outfolder+"/"+hsm.name+".dds", outdata);
            }
            else if(compressionType==1 || compressionType==5)
            {
                IBytedeque out = new Bytedeque2(shared.Format.realmyst);
                images.dds.createFromDxt(out, hsm.dxt);
                byte[] outdata = out.getAllBytes();
                FileUtils.WriteFile(outfolder+"/"+hsm.name+".dds", outdata);
            }
        }
    }
    public static void save3dsFile(Vector<Mdb> mdbs)
    {
        Primary main = Primary.createNull();
        Material mat = Material.create("defaultmat");
        
        //add texture filename...
        mat.texturemap = TextureMap.create("active.hsm.dds");
        
        main.meshdata.mats.add(mat);
        
        for(Mdb mdb: mdbs)
        {
            try
            {
                NamedObj obj = createNamedObj(mdb);
                main.meshdata.objs.add(obj);
            }
            catch(ignore e)
            {
                
            }
        }

        IBytedeque out = new Bytedeque2(shared.Format.realmyst);
        main.compile(out);
        byte[] filedata = out.getAllBytes();
        FileUtils.WriteFile("c:/test.3ds", filedata);
    }
    
    public static NamedObj createNamedObj(Mdb mdb)//, Primary main, Sdb sdb)
    {
        /*Vertex[] verts = new Vertex[bunch.length];
        for(int i=0;i<bunch.length;i++)
        {
            Flt x = bunch[i].f1;
            Flt y = bunch[i].f2;
            Flt z = bunch[i].f3;
            verts[i] = Vertex.createFromFlts(x, y, z);
        }*/
        
        String ignorereason = "";
        if(mdb.fs==null) ignorereason += "Skipping NamedObj because it has no fs. ";
        if(mdb.bunch==null) ignorereason += "Skipping NamedObj because it has no bunch. ";
        if(mdb.whas==null) ignorereason += "Skipping NamedObj because it has no whas. ";
        if(mdb.trips==null) ignorereason += "NamedObj has no trips.";
        if(!ignorereason.equals("")) throw new ignore(ignorereason);
        
        Vertex[] verts = new Vertex[mdb.fs.length];
        for(int i=0;i<mdb.fs.length;i++)
        {
            int to = mdb.fs[i].v2;
            int from = mdb.fs[i].v3;
            Flt x= mdb.bunch[to].f1;
            Flt y= mdb.bunch[to].f2;
            Flt z= mdb.bunch[to].f3;
            verts[i] = Vertex.createFromFlts(x, y, z);
        }

        ShortTriplet[] faces = new ShortTriplet[mdb.whas.length];
        for(int i=0;i<mdb.whas.length;i++)
        {
            int v1 = mdb.whas[i].u6;
            int v2 = mdb.whas[i].u7;
            int v3 = mdb.whas[i].u8;
            faces[i] = ShortTriplet.createFromShorts((short)v1, (short)v2, (short)v3);
        }

        String objname = mdb.name.toString();
        
        FltPair[] uvcoords = new FltPair[mdb.trips.length];
        for(int i=0;i<mdb.trips.length;i++)
        {
            Flt u = mdb.trips[i].x;
            Flt v = mdb.trips[i].y;
            Flt w = mdb.trips[i].z;
            Flt u2 = Flt.createFromJavaFloat(1.0f - u.toJavaFloat()); //v = 1 - v;
            Flt v2 = Flt.createFromJavaFloat(1.0f - v.toJavaFloat()); //v = 1 - v;
            Flt w2 = Flt.createFromJavaFloat(1.0f - w.toJavaFloat()); //v = 1 - v;
            uvcoords[i] = FltPair.createFromFlts(u, v2);
        }

        //if(main==null)
        //{
        //    main = Primary.createNull();
        //    main.meshdata.mat = Material.create("defaultmat");
        //}
        NamedObj newobj = NamedObj.createNull(objname);
        newobj.namedTriangleObject = NamedTriangleObject.createNull();
        newobj.namedTriangleObject.points = PointArray.create(verts);
        newobj.namedTriangleObject.faces = FaceArray.create(faces, "defaultmat");
        //if(uvcoords.length!=0) newobj.namedTriangleObject.uvcoords = UvVerts.create(uvcoords);
        //main.meshdata.objs.add(newobj);
        
        for(ShortTriplet face: faces)
        {
            if(face.p >= verts.length || face.q>= verts.length || face.r>=verts.length)
            {
                int dummy=0;
            }
        }
        if(uvcoords.length!=0)
        {
            if(uvcoords.length!=verts.length)
            {
                int dummy=0;
            }
            else
            {
                if(uvcoords.length!=0) newobj.namedTriangleObject.uvcoords = UvVerts.create(uvcoords);
                
            }
        }

        //IBytedeque out = new Bytedeque2();
        //main.compile(out);
        //byte[] filedata = out.getAllBytes();
        //FileUtils.WriteFile("c:/test.3ds", filedata);
        
        return newobj;
    }
}

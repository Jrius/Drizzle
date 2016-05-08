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
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class realmyst
{
    public static void dumpAllModifiers()
    {
        // writes every sdb modifier into its own .dat
        Vector<Sdb> sdbs = readAllSdbs("realmystdata/sho");
        
        int j=0;
        for (Sdb sdb: sdbs)
        {
            for (int i=0; i<sdb.count6; i++)
            {
                // dump this count6
                String outname = "realmystdata/sho/drizzletests/MODIFDUMP/c6/"
                        + sdb.count6s[i].size + "_" + sdb.name.toString() + ".dat";
                
                while (FileUtils.Exists(outname))
                    outname = outname.replace(".dat", "_.dat");
                
//                FileUtils.WriteFile(outname, sdb.count6s[i].data);
                
                //m.msg(sdb.name.toString() + ": " + sdb.count6s[i].name);
            }
            
            for (int i=0; i<sdb.count10; i++)
            {
                Count10 ct = sdb.count10s[i];
                
//                m.msg(ct.s1.toString());
            }
            
            j++;
//            if (j>0)
//                return;
        }
        
        
    }

    /*public static void readMdbAloud() {
        Vector<Mdb> mdbs = readAllMdbs("realmystdata/sho");
        
        int i=0;
        for (Mdb mdb: mdbs)
        {
            m.msg(mdb.name.toString());
            // matrices to find:
            // 16 values of 4 bytes each -> 64 bytes, 512 bits
            // *2 or 4 depending of number of matrices used in PlasmaV1
            
            // not ii, nor ifi, nor quats, nor whas, nor extras... I guess it's really in Count6 then.
            
            //if (mdb.ifi != null) m.msg("\tifi: "+Arrays.toString(mdb.ifi.values));
            //if (mdb.ifi != null) m.msg("\tifi: " + mdb.ifi.values.length);
            //if (mdb.ii != null) m.msg("\tii: " + mdb.ii.count);
            //if (mdb.quats != null) m.msg("\tquats: " + mdb.quats.length);
            //if (mdb.whas != null) m.msg("\twhas: " + mdb.whas.length);
            //if (mdb.extra != null) m.msg("\textras: " + mdb.extra.length);
            
            i++;
            if (i > 30)
                return;
        }
    } //*/
    
    public static void testrun2(Vector<Sdb> objinfos, Vector<Mdb> meshes, String outfldr)
    {
        Primary outfile = Primary.createNull();
        HashMap<Integer, Count3Undone> materialmap = new HashMap();
        for(Sdb obj: objinfos)
        {
            //if(!sdb.sourceName.endsWith("70919002.vdb")) continue;
            
            if (obj.count3s == null)
            {
                m.warn("No materials for object " + obj.sourceName);
                continue;
            }
            
            for(int matindex=0;matindex<obj.count3s.length;matindex++)
            {
                Count3Undone mat = obj.count3s[matindex];
                
                if(mat.possibilities.length!=1) continue; // hmmm. Does that mean we ignore multilayering ?
                int textureIndex = mat.possibilities[0];
                if(textureIndex==-1) continue;

                //create material.
                String textureFilename = obj.count2s[textureIndex].textureFilename.toString();
                String matname = mat.sb2.str.toString();
                //String matname = "mat"+Integer.toString(matindex);
                Material mat3ds = Material.create(matname);
                mat3ds.texturemap = TextureMap.create(textureFilename+".dds");

                //apply uv transform while we're here.
                Matrix matrix = null;
                if(mat.subs!=null && mat.subs.length>0) 
                    matrix = mat.subs[0].m400;
                if(matrix!=null)
                {
                    Flt uscale = matrix.values[0][0];
                    Flt vscale = matrix.values[1][1];
                    Flt uoffset = matrix.values[0][3];
                    Flt voffset = matrix.values[1][3];
                    voffset.multModify(-1.0f);
                    mat3ds.texturemap.uoffset = UOffset.create(uoffset.toJavaFloat());
                    mat3ds.texturemap.voffset = VOffset.create(voffset.toJavaFloat());
                    mat3ds.texturemap.uscale = UScale.create(uscale.toJavaFloat());
                    mat3ds.texturemap.vscale = VScale.create(vscale.toJavaFloat());
                }

                outfile.meshdata.mats.add(mat3ds);
                    
                materialmap.put(matindex, mat);
            }
            
        }
        
        for(Mdb mesh: meshes)
        {
            //if(!mdb.name.toString().equals("myst..portstairstep")) continue;
            
            try
            {
                NamedObj namedobj = createNamedObjWithMaterial(mesh,materialmap);
                outfile.meshdata.objs.add(namedobj);
            }
            catch (ignore i)
            {
                m.warn("Not parsing " + mesh.sourceName + "\nReason: " + i.getMessage());
            }
        }
        
        //save 3ds file.
        IBytedeque out = new Bytedeque2(shared.Format.realmyst);
        outfile.compile(out);
        byte[] filedata = out.getAllBytes();
        FileUtils.WriteFile(outfldr+"/test.3ds", filedata);
    }
    public static NamedObj createNamedObjWithMaterial(Mdb mdb, HashMap<Integer, Count3Undone> materialmap)
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

        ShortTriplet[] faces3ds = new ShortTriplet[mdb.whas.length];
        for(int i=0;i<mdb.whas.length;i++)
        {
            int v1 = mdb.whas[i].u6;
            int v2 = mdb.whas[i].u7;
            int v3 = mdb.whas[i].u8;
            faces3ds[i] = ShortTriplet.createFromShorts((short)v1, (short)v2, (short)v3);
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
        newobj.namedTriangleObject.faces.facecount = check.intToShort(faces3ds.length);
        newobj.namedTriangleObject.faces.faces = new FaceArray.tdsface[faces3ds.length];
        for(int i=0;i<faces3ds.length;i++)
        {
            short v1 = faces3ds[i].p;
            short v2 = faces3ds[i].q;
            short v3 = faces3ds[i].r;
            short flags = 0;
            newobj.namedTriangleObject.faces.faces[i] = new FaceArray.tdsface(v1,v2,v3,flags);
        }
        for(int curmat=0;curmat<mdb.ii.count;curmat++)
        {
            int curmatindex = mdb.ii.indices[curmat];
            Count3Undone c3= materialmap.get(curmatindex);
            String curmatname = "UNDEFINED_" + mdb.name.toString();
            if (c3 != null && c3.sb2 != null && c3.sb2.str != null)
                curmatname = c3.sb2.str.toString();
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
        
        
        for(ShortTriplet face: faces3ds)
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
                if(count>400) break;
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
                        m.warn("Didn't read the correct amount of data from " + mdb.name.toString());
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
//                        m.warn("Didn't read the correct amount of data from " + sdb.name.toString());
                    }
                    if(bytesleft!=0)
                    {
//                        m.warn("Remaining bytes isn't 0");
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
    public static void save3dsFile(Vector<Mdb> mdbs, String outfolder)
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
        FileUtils.WriteFile(outfolder+"/realMystTest.3ds", filedata);
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
    
    
    
    
    
    // new vain attempts by Sirius at understanding how RM works.
    // mainly focused on loading assets the way RM does, from the scene database to SDBs to MDBs
    
    public static String path="RealMYST/sho"; // put your own filepath here...
    public static Idx sceneDatabase;
    public static ArrayList<Mdb> mdb=new ArrayList();
    
    public static Idx S_LoadSceneDatabase()
    {
        if (sceneDatabase == null)
        {
            String filename = path + "/scn/myst.idx";
            shared.IBytestream bs = shared.SerialBytestream.createFromFilename(filename);
            sceneDatabase = new Idx(bs);
        }
        return sceneDatabase;
    }
    
    public static ArrayList<String> S_GetEntryNamesFromBlock(Idx.IdxBlock idxb)
    {
        ArrayList<String> names = new ArrayList();
        for (Idx.IdxEntry entry: idxb.entries)
            names.add(entry.name.toString());
        return names;
    }
    
    public static ArrayList<Sdb> S_GetSDBListFromIdxEntry(Idx.IdxEntry idxe)
    {
        ArrayList<Sdb> allSdb = new ArrayList();
        for (int index: idxe.indexes)
        {
            String filename = path + "/sdb/"+index+".vdb";
            shared.IBytestream bs = shared.SerialBytestream.createFromFilename(filename);
            allSdb.add(new Sdb(bs));
        }
        return allSdb;
    }
    
    public static ArrayList<Mdb> S_LoadMDB()
    {
        if (mdb.isEmpty())
        {
            Vector<File> files = FileUtils.FindAllFiles(path + "/mdb/", "vdb", false);
            for (File file: files)
            {
                shared.IBytestream bs = shared.SerialBytestream.createFromFile(file);
                mdb.add(new Mdb(bs));
            }
        }
        return mdb;
    }
    
    public static void S_TestRun3(String path, String age, String outfile)
    {
        auto.realmyst.path = path;
        Idx sdbidx = auto.realmyst.S_LoadSceneDatabase();
        ArrayList<Sdb> soughtsdb=new ArrayList();
        ArrayList<String> rooms=new ArrayList();


        // taken directly from holdingpen, since that's really a small amount of data.
        // funny, when linking, the holdingpen doesn't load the full Age - only visible parts.
        // Does that mean PlasmaV1 supports threaded loading ?!if (args[2].startsWith("channel"))
        if (age.startsWith("channel"))
        {
            rooms.add("channelwood");
            rooms.add("windmill");
            rooms.add("ch_sirrus01");
            rooms.add("ch_cylinder_f");
            rooms.add("ch_waterhut01");
            rooms.add("channel_temple");
            rooms.add("ch_bookroom");
            rooms.add("toy_veranda");
            rooms.add("ch_waterhut01");
        }
        else if (age.equals("dni"))
        {
            // missing rooms (only required mov as uppercase)
            rooms.add("DNY_room");
            rooms.add("Atrus");
            //rooms.add("mov"); // actually not used, since that's the intro cinematic
        }
        else if (age.startsWith("mech"))
        {
            rooms.add("mech_outdoor");
            rooms.add("mech_indoor");
            rooms.add("me_bookroom");
            rooms.add("me_pass02");
            rooms.add("me_pass03");
            rooms.add("me_achenar01");
            rooms.add("me_achenar02");
            rooms.add("msr01");
            rooms.add("msr02");
            rooms.add("99");
            rooms.add("cogwheel");
            rooms.add("me_lift01");
            rooms.add("me_lift02");
            rooms.add("me_conpane");
        }
        else if (age.equals("rime"))
        {
            // missing rooms
            rooms.add("snowage");
            rooms.add("sn_island_pit");
            //rooms.add("sn_isl_tunnel05");
            rooms.add("sn_isl_study02");
            rooms.add("page");
            rooms.add("pazzlestars");
            rooms.add("aurora");
            rooms.add("sn_island_nook");
            rooms.add("satunnel01");
            rooms.add("satunnel02");
            rooms.add("sn_island_laboratory");
        }
        else if (age.startsWith("sel"))
        {
            // missing rooms
            rooms.add("tube");
            rooms.add("sl_rocket");
            rooms.add("selenitic");
            rooms.add("se_hut_inside");
            rooms.add("slcave");
            //rooms.add("se_base");
            rooms.add("se_stair01");
            rooms.add("se_stair02");
            rooms.add("se_stair03");
            rooms.add("se_stair04");
            rooms.add("tika");
            rooms.add("se_bookhall");
        }
        else if (age.startsWith("stone"))
        {
            rooms.add("stoneship01");
            rooms.add("stoneship02");
            rooms.add("lighthouse");
            rooms.add("lighthouse2");
            rooms.add("bf1f");
            rooms.add("ss_sik01");
            rooms.add("ss_sik02");
            rooms.add("ss_sik03");
            rooms.add("ss_sik04");
            rooms.add("ss_akk01");
            rooms.add("ss_akk02");
            rooms.add("ss_akk03");
            rooms.add("ss_akk04");
            rooms.add("sship_aku_room");
            rooms.add("sirrus_room");
            rooms.add("sship_tunnel");
            rooms.add("sship_tunnel02");
            rooms.add("compus_room");
            rooms.add("cabin_kaidan");
            rooms.add("ss_deep_sea");
            rooms.add("book_room");
        }
        else if (age.equals("myst"))
        {
            rooms.add("myst");
            rooms.add("imgpassage");
            rooms.add("stairs");
            rooms.add("room");
            rooms.add("imager");
            rooms.add("cabin");
            rooms.add("planet");
            rooms.add("star_room");
            rooms.add("library");
            rooms.add("lose");
            rooms.add("map");
            rooms.add("lib_fireplace");
            rooms.add("libpassage");
            rooms.add("my_tower");
            rooms.add("rocket");
            rooms.add("hut01");
            rooms.add("genetunnel01");
            rooms.add("genetunnel02");
            rooms.add("genetunnel03");
            rooms.add("control");
            rooms.add("generoom");
            rooms.add("wood");
            rooms.add("treegate");
            rooms.add("clock");
        }
        else
        {
            m.err("Don't know the rooms for this age. Try myst, rime, dni, channelwood, mechanical, selenictic, or stoneship");
            return;
        }


        int found=0;
        for (Idx.IdxEntry entry: sdbidx.RoomIndex.entries)
        {
            for (String room: rooms)
                if (room.toLowerCase().equals(entry.name.toString().toLowerCase())) // it seems Plasma itself is case insensitive when it comes to these...
                {
                    found++;
                    soughtsdb.addAll(auto.realmyst.S_GetSDBListFromIdxEntry(entry));
                }
        }
        if (found != rooms.size())
            m.warn("Did not find all the required rooms for this Age !");
        if (!soughtsdb.isEmpty())
        {
            m.msg("Found required objects");
            Vector<Mdb> tosave = new Vector();
            for (Sdb sdb: soughtsdb)
            {
                if (sdb.count6 != 0)
                    for (Count6Undone c6: sdb.count6s)
                    {
                        c6.name.toString();
                        for (Mdb mdb2: auto.realmyst.S_LoadMDB())
                        {
                            if (mdb2.name.toString().equals(c6.name.toString()))
                            {
                                if (mdb2.bunch != null && false)
                                    for (Mdb.Sixlet vtx: mdb2.bunch)
                                    {
                                        // multiply matrix by vertex pos as vertex pos
                                        // this modifies the mesh so that it takes rotation, translation, scale, etc into account
                                        // (because I have no idea how to modify Dustin's 3ds code to take these into accounts)
                                        // unfortunately, doesn't work so fine for some meshes, resulting in huge geometry
                                        // Probably has some control flag somewhere.

                                        float realvtx[] = {vtx.f1.toJavaFloat(), vtx.f2.toJavaFloat(), vtx.f3.toJavaFloat()};
                                        float outvtx[] = {0f,0f,0f};
                                        outvtx[0] = c6.position.values[0][0].toJavaFloat() * realvtx[0]
                                                  + c6.position.values[0][1].toJavaFloat() * realvtx[1]
                                                  + c6.position.values[0][2].toJavaFloat() * realvtx[2]
                                                  + c6.position.values[0][3].toJavaFloat();
                                        outvtx[1] = c6.position.values[1][0].toJavaFloat() * realvtx[0]
                                                  + c6.position.values[1][1].toJavaFloat() * realvtx[1]
                                                  + c6.position.values[1][2].toJavaFloat() * realvtx[2]
                                                  + c6.position.values[1][3].toJavaFloat();
                                        outvtx[2] = c6.position.values[2][0].toJavaFloat() * realvtx[0]
                                                  + c6.position.values[2][1].toJavaFloat() * realvtx[1]
                                                  + c6.position.values[2][2].toJavaFloat() * realvtx[2]
                                                  + c6.position.values[2][3].toJavaFloat();
                                        vtx.f1 = new Flt(outvtx[0]);
                                        vtx.f2 = new Flt(outvtx[1]);
                                        vtx.f3 = new Flt(outvtx[2]);
                                    }
                                tosave.add(mdb2);
                            }
                        }
                    }
            }
            m.msg("Saving file to " + outfile);
            auto.realmyst.save3dsFile(tosave, outfile);
            m.msg("Done.");
        }
        else
            m.msg("Can't find this room.");
    }
    
    public static void S_LoadRMRooms(String path, String entryname, String outfile)
    {
        auto.realmyst.path = path;
        Idx sdbidx = auto.realmyst.S_LoadSceneDatabase();
        ArrayList<Sdb> soughtsdb=new ArrayList();
        for (Idx.IdxEntry entry: sdbidx.RoomIndex.entries)
        {
            if (!(entry.name.toString().equals(entryname)))
                continue;
            soughtsdb = auto.realmyst.S_GetSDBListFromIdxEntry(entry);
        }
        if (!soughtsdb.isEmpty())
        {
            m.msg("Found required objects");
            Vector<Mdb> tosave = new Vector();
            for (Sdb sdb: soughtsdb)
            {
                if (sdb.count6 != 0)
                    for (Count6Undone c6: sdb.count6s)
                    {
                        c6.name.toString();
                        for (Mdb mdbobj: auto.realmyst.S_LoadMDB())
                        {
                            if (mdbobj.name.toString().equals(c6.name.toString()))
                                tosave.add(mdbobj);
                        }
                    }
            }
            m.msg("Saving file to " + outfile);
            auto.realmyst.save3dsFile(tosave, outfile);
            m.msg("Done.");
        }
        else
            m.msg("Can't find this room.");
    }
}

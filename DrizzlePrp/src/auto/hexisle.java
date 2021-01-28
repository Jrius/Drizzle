/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import auto.conversion.FileInfo;
import auto.conversion.Info;
import shared.*;
import uru.Bytestream;
import uru.context;
import prpobjects.Uruobjectdesc;
import prpobjects.prpfile;
import prpobjects.Typeid;
import java.util.HashMap;
import prpobjects.Typeid;
import prpobjects.PrpRootObject;
import prpobjects.Uruobjectref;
import shared.Flt;
import prpobjects.textfile;


public class hexisle
{
    public static AllGames.GameInfo getGameInfo()
    {
        //if(true) m.throwUncaughtException("todo: re-enable main conversion.");

        AllGames.GameInfo r = new AllGames.GameInfo();
        r.GameName = "HexIsle";
        r.DetectionFile = "HexisleGDF.dll";
        r.prpMarkerForAgename = "_";
        r.format = shared.Format.hexisle;
        r.PythonVersion = 23;
        r.game = Game.hexisle;
        r.MusicFiles = new String[] {
            "CatFishCanyonMx.ogg","DessertDesertMx.ogg","LouderSpaceMx.ogg","MoldyDungeonMx.ogg","PlasmaMiasmaMx.ogg","PumpkinJungleMx.ogg",
        };
        //r.info = new conversion.Info();
        r.renameinfo.prefices.put("CatfishCanyon", 1162);
        r.renameinfo.prefices.put("DessertDesert", 1163);
        r.renameinfo.prefices.put("LouderSpace", 1164);
        r.renameinfo.prefices.put("MoldyDungeon", 1165);
        r.renameinfo.prefices.put("PlasmaMiasma", 1166);
        r.renameinfo.prefices.put("PumpkinJungle", 1167);
        //r.info.infolder = infolder;
        //r.info.outfolder = outfolder;
        /*r.decider = new uru.moulprp.prputils.Compiler.Decider() {
            public boolean isObjectToBeIncluded(Uruobjectdesc desc) {
                Typeid tid = desc.objecttype;
                //if(tid==Typeid.plPanicLinkRegion) return false;
                //if(tid==Typeid.plVisRegion) return false;
                return true;
            }
        };*/
        r.decider = prpobjects.prputils.Compiler.getDefaultDecider();
        r.prpmodifier = new conversion.PostConversionModifier() {
            public void ModifyPrp(conversion.Info info, conversion.FileInfo file, prpfile prp) {
                String age = prp.header.agename.toString();
                String page = prp.header.pagename.toString();
                String f = file.filename;

                //remove old physics and create new drawable-based physics for every main prp.
                if(f.equals("DessertDesert_Desert.prp")||f.equals("MoldyDungeon_Dungeon.prp")||f.equals("PlasmaMiasma_PlasmaTube.prp")||f.equals("LouderSpace_Space.prp")||f.equals("PumpkinJungle_JungleExterior.prp")||f.equals("CatfishCanyon_Canyon.prp"))
                {
                    removeAllPhysics(prp); // this also effectively disables all kinds of regions
                    createStaticCollidersForAllDrawables(prp);
                    //fixStartAndLoopForAudio(prp);
                }


                //make the collider good for each permanent hexagon prp.
                if(page.equals("TilePermanent"))
                {
                    makePhysicalIntoStaticCollider(prp,"Hex01");
                }
            }
        };
        r.fnimodifier = new conversion.FniModifier() {
            public void ModifyFni(Info info, FileInfo file, textfile tf) {
                for(textfile.textline line: tf.getLines())
                {
                    String l = line.getString();
                    if(l.startsWith("Keyboard.BindKey")) line.setString("#"+l);
                    if(l.startsWith("Keyboard.BindConsoleCmd")) line.setString("#"+l);
                    if(l.startsWith("Graphics.PostProcess")) line.setString("#"+l);
                }
            }
        };
        r.agemodifier = new conversion.AgeModifier() {
            public void ModifyAge(Info info, FileInfo file, textfile tf) {
                for(textfile.textline line: tf.getLines())
                {
                    String l = line.getString();
                    if(l.startsWith("Page=TileGoal,")) line.setString("#"+l);
                    if(l.startsWith("Page=TileProp01,")) line.setString("#"+l);
                    if(l.startsWith("Page=TileRegular,")) line.setString("#"+l);
                    if(l.startsWith("Page=TileTarget,")) line.setString("#"+l);
                    //if(l.startsWith("Page=TilePermanent,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoPumpkinEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoCatfishEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoPlasmaEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoLouderEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoMoldyEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoDessertEnd,")) line.setString("#"+l);
                    if(l.startsWith("Page=CreditsDialog,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoPlanet,")) line.setString("#"+l);
                    if(l.startsWith("Page=OsmoTub,")) line.setString("#"+l);

                }
            }
        };
        boolean debug = false;
        if(debug)
        {
            m.err("Turn off debug mode in hexisle!!!");
            r.addAgeFiles("CatfishCanyon", new String[]{
                //"CatfishCanyon.age",
                //"CatfishCanyon.fni",
                //"CatfishCanyon.sum",
                "CatfishCanyon_Canyon.prp",
                //"CatfishCanyon_OsmoCatfishEnd.prp",
                //"CatfishCanyon_Textures.prp",
                //"CatfishCanyon_TileGoal.prp",
                //"CatfishCanyon_TilePermanent.prp",
                //"CatfishCanyon_TileProp01.prp",
                //"CatfishCanyon_TileRegular.prp",
                //"CatfishCanyon_TileTarget.prp",
            });
            //r.info.addSoundFiles(HexisleSounds);
        }
        else
        {
        r.addAgeFiles("DessertDesert",new String[]{
            "DessertDesert.age",
            "DessertDesert.fni",
            "DessertDesert.sum",
            "DessertDesert_Desert.prp",
            "DessertDesert_OsmoDessertEnd.prp",
            "DessertDesert_Textures.prp",
            "DessertDesert_TileGoal.prp",
            "DessertDesert_TilePermanent.prp",
            "DessertDesert_TileProp01.prp",
            "DessertDesert_TileRegular.prp",
            "DessertDesert_TileTarget.prp",
        });
        r.addAgeFiles("MoldyDungeon", new String[]{
            "MoldyDungeon.age",
            "MoldyDungeon.fni",
            "MoldyDungeon.sum",
            "MoldyDungeon_Dungeon.prp",
            "MoldyDungeon_OsmoMoldyEnd.prp",
            "MoldyDungeon_Textures.prp",
            "MoldyDungeon_TileGoal.prp",
            "MoldyDungeon_TilePermanent.prp",
            "MoldyDungeon_TileProp01.prp",
            "MoldyDungeon_TileRegular.prp",
            "MoldyDungeon_TileTarget.prp",
        });
        r.addAgeFiles("PlasmaMiasma", new String[]{
            "PlasmaMiasma.age",
            "PlasmaMiasma.fni",
            "PlasmaMiasma.sum",
            "PlasmaMiasma_OsmoPlasmaEnd.prp",
            "PlasmaMiasma_PlasmaTube.prp",
            "PlasmaMiasma_Textures.prp",
            "PlasmaMiasma_TileGoal.prp",
            "PlasmaMiasma_TilePermanent.prp",
            "PlasmaMiasma_TileProp01.prp",
            "PlasmaMiasma_TileRegular.prp",
            "PlasmaMiasma_TileTarget.prp",
        });
        r.addAgeFiles("LouderSpace", new String[]{
            "LouderSpace.age",
            "LouderSpace.fni",
            "LouderSpace.sum",
            "LouderSpace_CreditsDialog.prp",
            "LouderSpace_OsmoLouderEnd.prp",
            "LouderSpace_OsmoPlanet.prp",
            "LouderSpace_OsmoTub.prp",
            "LouderSpace_Space.prp",
            "LouderSpace_Textures.prp",
            "LouderSpace_TileGoal.prp",
            "LouderSpace_TilePermanent.prp",
            "LouderSpace_TileProp01.prp",
            "LouderSpace_TileRegular.prp",
            "LouderSpace_TileTarget.prp",
        });
        r.addAgeFiles("PumpkinJungle", new String[]{
            "PumpkinJungle.age",
            "PumpkinJungle.fni",
            "PumpkinJungle.sum",
            "PumpkinJungle_JungleExterior.prp",
            "PumpkinJungle_OsmoPumpkinEnd.prp",
            "PumpkinJungle_Textures.prp",
            "PumpkinJungle_TileGoal.prp",
            "PumpkinJungle_TilePermanent.prp",
            "PumpkinJungle_TileProp01.prp",
            "PumpkinJungle_TileRegular.prp",
            "PumpkinJungle_TileTarget.prp",
        });
        r.addAgeFiles("CatfishCanyon", new String[]{
            "CatfishCanyon.age",
            "CatfishCanyon.fni",
            "CatfishCanyon.sum",
            "CatfishCanyon_Canyon.prp",
            "CatfishCanyon_OsmoCatfishEnd.prp",
            "CatfishCanyon_Textures.prp",
            "CatfishCanyon_TileGoal.prp",
            "CatfishCanyon_TilePermanent.prp",
            "CatfishCanyon_TileProp01.prp",
            "CatfishCanyon_TileRegular.prp",
            "CatfishCanyon_TileTarget.prp",
        });
        r.addSoundFiles(new String[]{
            "CatfishCanyon_Amb_Wind_loop.ogg","CatFishCanyonMx.ogg","CC_RiverLoopStereo.ogg",
            "CC_RandomBird01.ogg","CC_RandomBird02.ogg","CC_RandomBird03.ogg","CC_RandomBird04.ogg","CC_RandomBird05.ogg","CC_RandomBird06.ogg","CC_RandomBird07.ogg","CC_RandomBird08.ogg","CC_RandomBird09.ogg","CC_RandomBird10.ogg","CC_RandomBird11.ogg","CC_RandomBird12.ogg",
            "DessertDesert_Amb.ogg","DessertDesert_Wind_Loop.ogg","DessertDesertMx.ogg",
            "LouderSpaceAmb_loop.ogg","LouderSpaceCraters.ogg","LouderSpaceMx.ogg","LS_ShootingStar_loop.ogg","LS_SpeakerStomp.ogg","LS-RotatingSpeaker-Loop.ogg",
            "LS_Random01.ogg","LS_Random02.ogg","LS_Random03.ogg","LS_Random04.ogg","LS_Random05.ogg","LS_Random06.ogg","LS_Random07.ogg",
            "MoldyDungeon_Amb01.ogg","MoldyDungeon_Amb02.ogg","MoldyDungeonMx.ogg",
            "MD_Random01_scurry.ogg","MD_Random02_Squeak.ogg","MD_Random03_Buzz.ogg","MD_Random04_Buzz.ogg","MD_Random05_Buzz.ogg","MD_Random06_Squeak.ogg","MD_Random07_Squeak.ogg","MD_Random08_Squeak.ogg","MD_Random09_Squeak.ogg","MD_Random10_Debris.ogg","MD_Random11_Debris.ogg","MD_Random12_Debris.ogg","MD_Random13_Squeak.ogg",
            "PlasmaMiasma_Amb.ogg","PlasmaMiasmaMx.ogg","PM_MovingLoop.ogg",
            "PumkinJungle_Amb.ogg","PumpkinJungleMx.ogg",
            "PJBirdRandom01.ogg","PJBirdRandom02.ogg","PJBirdRandom03.ogg","PJBirdRandom04.ogg","PJBirdRandom05.ogg","PJBirdRandom06.ogg",
        });
        }
        return r;
    }

    
    /*public static void convert(String infolder, String outfolder)
    {

        }
        conversion.convertFiles(info);
        
        //handle prps...*/
        /*for(String filename: prpfiles)
        {
            String agename = common.getAgenameFromFilename(filename);
            String infile = infolder + "/dat/" + filename;
            String outfilename = filename.replaceFirst("_", "_District_");
            String outfile = outfolder + "/dat/" + outfilename;
            
            Bytes prpdata = Bytes.createFromFile(infile);
            Bytestream bytestream = Bytestream.createFromBytes(prpdata);
            context c = context.createFromBytestream(bytestream);
            c.curFile = filename; //helpful for debugging.
            
            //modify sequence prefix if Age is in list.
            Integer prefix = r.info.renamer.info.prefices.get(agename);
            if(prefix!=null)
            {
                c.sequencePrefix = prefix;
            }
            
            //modify agename if Age is in list.
            String newAgename = r.info.renamer.info.agenames.get(agename);
            if(newAgename!=null)
            {
                c.ageName = newAgename;
            }

            prpfile prp = prpfile.createFromContext(c, hexisleReadable);

            //processPrp(prp,agename,agenames,outfolder);

            //Bytes prpoutputbytes = prp.saveAsBytes();
            //prpoutputbytes.saveAsFile(outfile);
            prp.saveAsFile(outfile);
        }*/
    //}

    public static void createStaticCollidersForAllDrawables(prpfile prp)
    {
        PrpRootObject sn_ro = prp.findFirstScenenode();
        prpobjects.x0000Scenenode sn = sn_ro.castTo();
        Uruobjectref sn_ref = sn_ro.getref();

        //change this to be:
        //for each scene object with a draw interface
        //  use the coordinateInterface's localToWorld transform on all coordinates
        PrpRootObject[] objs = prp.FindAllObjectsOfType(Typeid.plSceneObject);
        for(int i=0;i<objs.length;i++)
        {
            PrpRootObject sod_ro = objs[i];
            prpobjects.plSceneObject sod = sod_ro.castTo();
            //if(sod_ro.header.desc.objectname.toString().toLowerCase().equals("capsule02"))
            //{
            //    int dummy=0;
            //}
            if(sod.drawinterface.hasref()) //if it has a draw interface
            {
                //try to get a coordinateInterface.
                prpobjects.Transmatrix transform = null;
                if(sod.coordinateinterface.hasref())
                {
                    prpobjects.plCoordinateInterface ci = prp.findObjectWithRef(sod.coordinateinterface).castTo();
                    transform = ci.localToWorld;
                }

                prpobjects.plDrawInterface di = prp.findObjectWithRef(sod.drawinterface).castTo();
                for(int j=0;j<di.subsetgroups.length;j++)
                {
                    if(di.subsetgroups[j].subsetgroupindex!=-1)
                    {
                        prpobjects.plDrawableSpans ds = prp.findObjectWithRef(di.subsetgroups[j].span).castTo();
                        prpobjects.plDrawableSpans.PlDISpanIndex dispi = ds.DIIndices[di.subsetgroups[j].subsetgroupindex];
                        for(int k=0;k<dispi.indices.length;k++)
                        {
                            int subsetind = dispi.indices[k];
                            prpobjects.plDrawableSpans.PlIcicle ice = ds.icicles[subsetind];
                            prpobjects.plDrawableSpans.PlGBufferGroup group = ds.groups[ice.parent.groupIdx];
                            String name = "HexisleDrizzleColliderIcicle-"+Integer.toString(i)+"-"+Integer.toString(j)+"-"+Integer.toString(k);

                            prpobjects.plDrawableSpans.PlGBufferGroup.SubMesh mesh = group.submeshes[ice.parent.VBufferIdx];
                            float[] vertices = mesh.getVertices(group.fformat);
                            Vertex[] vertices2 = new Vertex[vertices.length/3];
                            for(int l=0;l<vertices.length/3;l++)
                            {
                                Vertex v = Vertex.createFromFloats(vertices[l*3+0],vertices[l*3+1],vertices[l*3+2]);
                                vertices2[l] = v;
                            }

                            //get surfaces
                            prpobjects.plDrawableSpans.PlGBufferGroup.Surface surface = group.surfaces[ice.IBufferIdx];
                            shared.ShortTriplet[] faces = surface.faces;

                            //just get the part we want.
                            Vertex[] verticesX = new Vertex[ice.parent.VLength];
                            for(int l=0;l<ice.parent.VLength;l++)
                            {
                                Vertex v = vertices2[ice.parent.VStartIdx+l];
                                if(transform!=null)
                                {
                                    Vertex v2 = transform.mult(v);
                                    v = v2;
                                }
                                verticesX[l] = v;
                            }
                            int ilength = ice.ILength/3;
                            int istart = ice.IStartIdx/3;
                            ShortTriplet[] facesXb = new ShortTriplet[ilength];
                            for(int l=0;l<ilength;l++)
                            {
                                if(istart+l>=faces.length)
                                {
                                    int dummy=0;
                                }
                                else
                                {
                                    facesXb[l] = faces[istart+l];
                                }
                            }

                            //reassign indices
                            ShortTriplet[] facesX = new ShortTriplet[ilength];
                            for(int l=0;l<ilength;l++)
                            {
                                ShortTriplet tripb = facesXb[l];
                                short p = (short)(b.Int16ToInt32(tripb.p) - ice.parent.VStartIdx);
                                short q = (short)(b.Int16ToInt32(tripb.q) - ice.parent.VStartIdx);
                                short r = (short)(b.Int16ToInt32(tripb.r) - ice.parent.VStartIdx);
                                ShortTriplet trip = ShortTriplet.createFromShorts(p, q, r);
                                facesX[l] = trip;
                            }

                            //prepare new objects
                            prpobjects.plSceneObject so = prpobjects.plSceneObject.createDefaultWithScenenode(sn_ro.getref());
                            Uruobjectref so_ref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plSceneObject, name, prp);
                            prpobjects.plHKPhysical phys = prpobjects.plHKPhysical.createStaticTriangleMeshFromVerticesAndFaces(verticesX, facesX, sn_ref, so_ref);
                            Uruobjectref phys_ref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plHKPhysical, name, prp);
                            prpobjects.plSimulationInterface sim = prpobjects.plSimulationInterface.createWithPlHKPhysical(so_ref, phys_ref);
                            Uruobjectref sim_ref = Uruobjectref.createDefaultWithTypeNamePrp(Typeid.plSimulationInterface, name, prp);
                            so.simulationinterface = sim_ref;
                            //sn.addToObjectrefs1(so_ref);
                            //m.msg(so_ref.toString());

                            //put new objects in prp
                            prp.addObject(so_ref, so);
                            prp.addObject(phys_ref, phys);
                            prp.addObject(sim_ref, sim);
                        }
                    }
                }
            }

        }
    }

    /*
    public static void fixStartAndLoopForAudio(prpfile prp)
    {
        for(PrpRootObject obj: prp.objects2)
        {
            Typeid t = obj.header.objecttype;
            uru.moulprp.PlWin32Sound s = null;
            if(t==Typeid.plWin32LinkSound) s = obj.castTo(uru.moulprp.plWin32LinkSound.class).parent;
            else if(t==Typeid.plWin32StaticSound) s = obj.castTo(uru.moulprp.x0096Win32StaticSound.class).parent;
            else if(t==Typeid.plWin32StreamingSound) s = obj.castTo(uru.moulprp.x0084Win32StreamingSound.class).parent;
            if(s!=null)
            {
                //todo: allow background music sounds too!
                if( (s.parent.properties & uru.moulprp.PlWin32Sound.PlSound.kPropAutoStart) != 0 )
                {
                    //if it is set to autostart, then it should be set to play and loop too, at least in hexisle land.
                    //the play part is probably a difference between pots and hexisle, but the loop thing is because the background music doesn't need to loop, since you have a limited time to win ;)
                    s.parent.playing = 1;
                    s.parent.properties |= uru.moulprp.PlWin32Sound.PlSound.kPropLooping;
                    int dummy=0;
                }
            }
        }
    }
    */

    public static void removeAllPhysics(prpfile prp)
    {
        for(PrpRootObject ro: prp.FindAllObjectsOfType(Typeid.plHKPhysical))
        {
            ro.tagDeleted = true;
        }
    }

    public static void makePhysicalIntoStaticCollider(prpfile prp, String objname)
    {
        prpobjects.plHKPhysical phys = prp.findObject(objname, Typeid.plHKPhysical).castTo();
        phys.convertODEtoHK(prp);
        phys.havok.RC = Flt.createFromJavaFloat(0.5f);
        phys.havok.EL = Flt.createFromJavaFloat(0.0f);
        phys.havok.mass = Flt.zero();
        phys.havok.u1 = 0x0;
        phys.havok.coltype = 0x200;
        phys.havok.flagsdetect = 0x0;
        phys.havok.flagsrespond = 0x0;
        phys.havok.u2 = 0x0;
        phys.havok.u3 = 0x0;
        phys.havok.LOSDB = 0x44; //avatarWalkable and collidesWithCamera.
        phys.havok.group = new prpobjects.HsBitVector(0x104); //or should this be 0?
    }

    /*private static String[] HexisleSounds = {
        "CatfishCanyon_Amb_Wind_loop.ogg","CatFishCanyonMx.ogg","CC_RiverLoopStereo.ogg",
        "CC_RandomBird01.ogg","CC_RandomBird02.ogg","CC_RandomBird03.ogg","CC_RandomBird04.ogg","CC_RandomBird05.ogg","CC_RandomBird06.ogg","CC_RandomBird07.ogg","CC_RandomBird08.ogg","CC_RandomBird09.ogg","CC_RandomBird10.ogg","CC_RandomBird11.ogg","CC_RandomBird12.ogg",
        "DessertDesert_Amb.ogg","DessertDesert_Wind_Loop.ogg","DessertDesertMx.ogg",
        "LouderSpaceAmb_loop.ogg","LouderSpaceCraters.ogg","LouderSpaceMx.ogg","LS_ShootingStar_loop.ogg","LS_SpeakerStomp.ogg","LS-RotatingSpeaker-Loop.ogg",
        "LS_Random01.ogg","LS_Random02.ogg","LS_Random03.ogg","LS_Random04.ogg","LS_Random05.ogg","LS_Random06.ogg","LS_Random07.ogg",
        "MoldyDungeon_Amb01.ogg","MoldyDungeon_Amb02.ogg","MoldyDungeonMx.ogg",
        "MD_Random01_scurry.ogg","MD_Random02_Squeak.ogg","MD_Random03_Buzz.ogg","MD_Random04_Buzz.ogg","MD_Random05_Buzz.ogg","MD_Random06_Squeak.ogg","MD_Random07_Squeak.ogg","MD_Random08_Squeak.ogg","MD_Random09_Squeak.ogg","MD_Random10_Debris.ogg","MD_Random11_Debris.ogg","MD_Random12_Debris.ogg","MD_Random13_Squeak.ogg",
        "PlasmaMiasma_Amb.ogg","PlasmaMiasmaMx.ogg","PM_MovingLoop.ogg",
        "PumkinJungle_Amb.ogg","PumpkinJungleMx.ogg",
        "PJBirdRandom01.ogg","PJBirdRandom02.ogg","PJBirdRandom03.ogg","PJBirdRandom04.ogg","PJBirdRandom05.ogg","PJBirdRandom06.ogg",
    };*/

}

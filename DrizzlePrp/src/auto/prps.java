/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import prpobjects.*;
import prpobjects.plWin32Sound.PlSound;
import shared.m;

public class prps
{
    public static void ListObjects(String prpfilename)
    {
        prpfile prp = prpfile.createFromFile(prpfilename, true);
        for(PrpObjectIndex.ObjectindexObjecttype ind1: prp.objectindex.types)
        {
            for(PrpObjectIndex.ObjectindexObjecttypeObjectdesc ind2: ind1.descs)
            {
                m.msg(ind2.desc.toString());
            }
        }
    }
    
    /**
     * Finds all sound objects into the PRP, and returns a JSON containing simplified infos about them.
     * Should be enough to batch import them into Unity...
     * @param prp the file to read audio emitters from
     * @param outFolder the folder in which to write the json
     */
    public static void ListSoundsAsJSON(prpfile prp, String outFolder)
    {
        String root = "{'audio':[";
        for (PrpRootObject soro: prp.FindAllObjectsOfType(Typeid.plSceneObject))
        {
            plSceneObject so = soro.castTo();
            if (so.audiointerface != null && so.audiointerface.xdesc != null)
            {
                // got one
                PrpRootObject airo = prp.findObjectWithRef(so.audiointerface);
                if (airo == null) continue;
                x0011AudioInterface ai = airo.castTo();
                if (ai.soundlink == null || ai.soundlink.xdesc == null) continue;
                PrpRootObject waro = prp.findObjectWithRef(ai.soundlink);
                if (waro == null) continue;
                plWinAudible wa = waro.castTo();
                String emitterJS = "{'name': '" + soro.header.desc.objectname + "',";
                if (so.coordinateinterface != null && so.coordinateinterface.xdesc != null)
                {
                    PrpRootObject ciro = prp.findObjectWithRef(so.coordinateinterface);
                    if (ciro != null)
                    {
                        plCoordinateInterface ci = ciro.castTo();
                        float[] pos = {0,0,0};
                        int x = ci.localToWorld.xmatrix[3];
                        int y = ci.localToWorld.xmatrix[7];
                        int z = ci.localToWorld.xmatrix[11];
                        pos[0] = Float.intBitsToFloat(x);
                        pos[1] = Float.intBitsToFloat(y);
                        pos[2] = Float.intBitsToFloat(z);
                        
                        emitterJS += "'coord':[" + pos[0] + "," + pos[1] + "," + pos[2] + "],";
                    }
                }
                
                boolean hasRandomSounds = false;
                boolean rndStopped = false;
                int rndFlags = 0;
                float rndMinDelay = 1;
                float rndMaxDelay = 10;
                for (Uruobjectref ref: so.modifiers)
                {
                    if (ref == null || ref.xdesc == null)
                        continue;
                    switch (ref.xdesc.objecttype)
                    {
                        case plRandomSoundMod:
                            PrpRootObject rsro = prp.findObjectWithRef(ref);
                            if (rsro != null)
                            {
                                hasRandomSounds = true;
                                plRandomSoundMod rs = rsro.castTo();
                                rndMinDelay = rs.parent.minDelay.toJavaFloat();
                                rndMaxDelay = rs.parent.maxDelay.toJavaFloat();
                                rndFlags = rs.parent.mode;
                                rndStopped = (rs.parent.state != 0);
                            }
                            break;
                    }
                }
                if (hasRandomSounds)
                {
                    emitterJS += "'random':{";
                    emitterJS += "'stopped':" + (rndStopped ? "true,":"false,");
                    emitterJS += "'minDelay':" + rndMinDelay + ",";
                    emitterJS += "'maxDelay':" + rndMaxDelay + ",";
                    emitterJS += "'flags':" + rndFlags + ",";
                    emitterJS += "},";
                }
                else
                    emitterJS += "'random': null,";
                emitterJS += "'sounds':[";
                for (Uruobjectref wsoundref: wa.objectrefs)
                {
                    if (wsoundref == null || wsoundref.xdesc == null) continue;
                    PrpRootObject wssro = prp.findObjectWithRef(wsoundref);
                    if (wssro == null) continue;
                    plWin32Sound wss;
                    boolean streaming = (wssro.header.objecttype == Typeid.plWin32StreamingSound);
                    if (wssro.header.objecttype == Typeid.plWin32StaticSound)
                        wss = ((x0096Win32StaticSound) wssro.castTo()).parent;
                    else
                        wss = ((x0084Win32StreamingSound) wssro.castTo()).parent;
                    if (wss.channel != 0) continue; // ignore this left channel/right channel mess.
                    PlSound wsss = wss.parent;
                    
                    /* things unity handles:
                    V priority
                    V volume
                    V autostart
                    V loop
                    V min dist
                    V max dist
                    V is 3d
                    V sound resource (obviously)
                    V streaming or static. While it's not really a concern nowadays, let's keep this good practice
                    
                    things unity don't handle, and must be reimplemented via script:
                    V fadein, fadeout (can write a simple audio script to handle that)
                    V sound type (simple to fix, add enum in sound properties - it's better to have the possibility to turn some of the audio off selectively...)
                    V random sound (can write simple audio script to handle that)
                    soft region (ugh, ugly)
                    soft occlusion (ugh, ugly)
                    line follow (this is an animation, so it's NOT going to be converted automatically. Ever. Hopefully.)
                    stereizer (ignore)
                    logic (obviously). Will have to do it by hand
                    eax (uses reverb region only). Will have to do it by hand.
                    
                    Stereizer:
                        actually "Stereo-er" or "Stereo-izer". Allows splitting the audio file in two channels when running SoundDecompress, then resync these channels at run time
                        Still have no idea why that would be required - maybe because plasma only handles mono 3D sounds ?
                        Whatever, no need to bother with it as the original WAV is still single-file-stereo.
                        (line follow mode also uses stereizer)
                    // */
                    String sound = "{";
                    sound += "'name':'" + wssro.header.desc.objectname + "',";
                    sound += "'volume':" + wsss.desiredvolume + ",";
                    sound += "'priority':" + wsss.priority + ",";
                    sound += "'type':" + wsss.type + ",";
                    sound += "'mindist':" + wsss.minfalloff + ",";
                    sound += "'maxdist':" + wsss.maxfalloff + ",";
                    sound += "'autostart':" + (((wsss.properties & PlSound.kPropAutoStart) != 0) ? 1:0) + ",";
                    sound += "'streaming':" + (streaming ? 1:0) + ",";
                    sound += "'3d':" + (((wsss.properties & PlSound.kProp3D) != 0) ? 1:0) + ",";
                    sound += "'loop':" + (((wsss.properties & PlSound.kPropLooping) != 0) ? 1:0) + ",";
                    if (wsss.dataBuffer != null && wsss.dataBuffer.xdesc != null)
                    {
                        PrpRootObject sbro = prp.findObjectWithRef(wsss.dataBuffer);
                        if (sbro != null)
                        {
                            plSoundBuffer sb = sbro.castTo();
                            sound += "'src':'" + sb.oggfile.toString() + "',";
                        }
                    }
                    
                    sound += "'fadeintype':" + wsss.fadeInParams.type + ",";
                    sound += "'fadeinlength':" + wsss.fadeInParams.lengthInSecs + ",";
                    sound += "'fadeinvolstart':" + wsss.fadeInParams.volStart + ",";
                    sound += "'fadeinvolend':" + wsss.fadeInParams.volEnd + ",";
                    sound += "'fadeouttype':" + wsss.fadeOutParams.type + ",";
                    sound += "'fadeoutlength':" + wsss.fadeOutParams.lengthInSecs + ",";
                    sound += "'fadeoutvolstart':" + wsss.fadeOutParams.volStart + ",";
                    sound += "'fadeoutvolend':" + wsss.fadeOutParams.volEnd + ",";
                    
                    // add additional data here
                    
                    sound += "},";
                    emitterJS += sound;
                }
                emitterJS += "]},";
                root += emitterJS;
            }
        }
        root += "]}";
        root = root.replace('\'', '"');
        root = root.replace(",]", "]"); // remove extra commas
        root = root.replace(",}", "}");
        
        try{
            if (!outFolder.endsWith("/") && !outFolder.endsWith("\\"))
                outFolder += "/";
            String outFile = outFolder + prp.header.agename + "_" + prp.header.pagename + ".json";
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            writer.print(root);
            writer.close();
        } catch (IOException e) {
            m.err("Couldn't write to file !");
        }
    }
}

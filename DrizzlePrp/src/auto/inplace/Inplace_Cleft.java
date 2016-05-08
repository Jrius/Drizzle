/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.inplace;

import prpobjects.*;

public class Inplace_Cleft
{
    public static void RemoveFence(Inplace.InplaceModInfo info, prpfile prp)
    {
        //Dustin's removal of colliders for the Cleft fence so that avatars can jump over it (and eventually jump into the volcano.)
        //InplaceFile f = potsfolder.File(info.relpath);
        //byte[] data = f.ReadAsBytes();
        //prpfile prp = prpfile.createFromBytes(data, true);

        prp.markObjectDeleted(Typeid.plSceneObject, "PlayerBlockerTemp",false);

        //f.SaveFile(prp.saveAsBytes());
        //UpdateSumfile(potsfolder, info);
    }
}

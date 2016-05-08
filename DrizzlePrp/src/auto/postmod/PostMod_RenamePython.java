/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto.postmod;

import prpobjects.*;

public class PostMod_RenamePython
{
    public static void RenamePython(prpfile prp, String oldPythonFile, String newPythonFile)
    {
        for(PrpRootObject obj: prp.FindAllObjectsOfType(Typeid.plPythonFileMod))
        {
            plPythonFileMod pfm = obj.castTo();
            if(pfm.pyfile.toString().equals(oldPythonFile))
            {
                pfm.pyfile = Urustring.createFromString(newPythonFile);
            }
        }
    }
}

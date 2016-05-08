/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.*;
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
}

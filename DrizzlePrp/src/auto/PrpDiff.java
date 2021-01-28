/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package auto;

import prpobjects.prpfile;
import prpobjects.PrpRootObject;
import shared.m;
import shared.b;

public class PrpDiff
{
    static final boolean reportIdenticals = false;

    public static void FindDiff(String sourceprp, String destprp)
    {
        prpfile prp1 = prpfile.createFromFile(sourceprp, true);
        prpfile prp2 = prpfile.createFromFile(destprp, true);

        for(PrpRootObject obj1: prp1.objects2)
        {
            PrpRootObject obj2 = prp2.findObjectWithDesc(obj1.header.desc);
            if(obj2==null)
            {
                //prp1 has but prp2 does not.
                m.msg("Remove object: ",obj1.toString());
            }
            else
            {
                //in both, comparing
                if(obj1.getRawSize()!=obj2.getRawSize())
                {
                    //different sizes.
                    m.msg("Object changed: ",obj1.toString(), " old size: ",Integer.toString(obj1.getRawSize())," new size: ",Integer.toString(obj2.getRawSize()));
                }
                else if(!b.isEqual(obj1.getRawMd5(), obj2.getRawMd5()))
                {
                    //same size, different content.
                    m.msg("Object changed: ",obj1.toString()," (stayed the same size.)");
                }
                else
                {
                    //nothing changed!
                    if(reportIdenticals)
                    {
                        m.msg("Object is identical: ",obj1.toString());
                    }
                }
            }
        }
        for(PrpRootObject obj2: prp2.objects2)
        {
            PrpRootObject obj1 = prp1.findObjectWithDesc(obj2.header.desc);
            if(obj1==null)
            {
                //prp2 has but prp1 does not.
                m.msg("Add object: ",obj2.toString());
            }
        }
    }
}

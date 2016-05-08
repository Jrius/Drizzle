/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import prpobjects.PrpRootObject;
import prpobjects.Uruobjectref;
        
public class dvPrpRootObject extends dvPanel
{
    PrpRootObject prprootobject;
    deepview parent;
    
    public dvPrpRootObject(PrpRootObject prprootobject, deepview parent)
    {
        this.prprootobject = prprootobject;
        this.parent = parent;
        reload();
    }
    
    private void reload()
    {
        this.removeAll();
        this.add(dvWidgets.jlabel("PrpRootObject "+prprootobject.header.toString()));
        for(refLinks.refLink rl: parent.allreflinks.reflinks)
        {
            if(prprootobject.header.desc.equals(rl.to))
            {
                //this.add(dvWidgets.jlabel("found a ref:"+rl.from.toString()));
                this.add(new dvUruobjectref(Uruobjectref.createFromUruobjectdesc(rl.from),"referrer",parent,false));
            }
        }
        //this.revalidate();
    }
    
}

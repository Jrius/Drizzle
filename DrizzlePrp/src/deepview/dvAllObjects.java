/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import prpobjects.Uruobjectref;
import prpobjects.Uruobjectdesc;

public class dvAllObjects extends dvPanel
{
    deepview parent;
    
    public dvAllObjects(deepview parent)
    {
        this.parent = parent;
        
        reload();
    }
    
    public void reload()
    {
        this.removeAll();
        this.add(dvWidgets.jlabel("All Objects:"));
        for(Uruobjectref ref: parent.allrefs)
        {
            this.add(new dvUruobjectref(ref,"object",parent,false));
        }
        
    }
    
}

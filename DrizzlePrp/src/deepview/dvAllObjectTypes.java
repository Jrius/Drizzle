/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview;

import prpobjects.Uruobjectref;
import prpobjects.Uruobjectdesc;
import prpobjects.Typeid;

public class dvAllObjectTypes extends dvPanel
{
    deepview parent;
    
    public dvAllObjectTypes(deepview parent)
    {
        this.parent = parent;
        
        reload();
    }
    
    public void reload()
    {
        this.removeAll();
        this.add(dvWidgets.jlabel("All Object Types:"));
        for(Typeid typeid: parent.alltypes)
        {
            this.add(new dvTypeid(typeid,parent));
        }
        
    }
    
}

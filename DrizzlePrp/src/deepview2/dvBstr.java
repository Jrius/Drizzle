/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import prpobjects.*;

public class dvBstr extends dvNode
{


    public dvBstr(nodeinfo info2)
    {
        info = info2;
    }

    public String toString()
    {
        Bstr str = (Bstr)info.obj;
        return info.name+": "+str.toString();
    }

    public void onDoubleClick(guiTree tree)
    {
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export3ds;

import shared.*;
import java.util.Vector;

public class Meshdata extends tdsobj
{
    //public Material mat;
    public Vector<Material> mats = new Vector();
    //public NamedObj obj;
    public Vector<NamedObj> objs = new Vector();
    
    private Meshdata(){}
    
    public static Meshdata createNull()
    {
        Meshdata result = new Meshdata();
        return result;
    }
    public Typeid type(){return Typeid.meshdata;}
    public void innercompile(IBytedeque c)
    {
        //mat.compile(c);
        c.writeVector(mats);
        //obj.compile(c);
        c.writeVector(objs);
    }
}

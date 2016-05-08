/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

/**
 *
 * @author user
 */
public class nodeinfo
{
    String name;
    Class cls;
    Object obj;

    nodeinfo parent;

    java.lang.reflect.Field field;
    prpobjects.PrpRootObject rootobject;
    prpobjects.prpfile prp;
    dvAges root;

    public nodeinfo copyinherited()
    {
        nodeinfo r = new nodeinfo();
        r.parent = this;
        r.rootobject = rootobject;
        r.prp = prp;
        r.root = root;
        return r;
    }
}

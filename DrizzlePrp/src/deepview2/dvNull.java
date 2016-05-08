/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

/**
 *
 * @author user
 */
public class dvNull extends dvNode
{
    Class cls;
    String name;

    public dvNull(Class c, String name)
    {
        this.cls = c;
        this.name = name;
    }
    public String toString()
    {
        return name+" ("+cls.getName()+"){null}";
    }
}

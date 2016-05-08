/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

/**
 *
 * @author user
 */
public class MemUtils
{

    //this function should never change program behavior; it's just a suggestion to garbage collect.
    public static void GarbageCollect()
    {
        System.gc();
    }
}

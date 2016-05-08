/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Random;

public class RandomUtils
{
    public static final Random rng = new Random();

    public static <T> T GetRandomItem(T[] items)
    {
        int pos = (int)(Math.random()*items.length);
        T result = items[pos];
        return result;
    }

    public static <T> T GetRandomItem(T[]... itemlists)
    {
        int total=0;
        for(T[] list: itemlists)
        {
            total += list.length;
        }
        int pos = (int)(Math.random()*total);

        total = 0;
        for(T[] list: itemlists)
        {
            if(pos<total+list.length)
            {
                //it's from this list.
                T result = list[pos-total];
                return result;
            }

            total += list.length;
        }
        return null; //should never get here.
    }
}

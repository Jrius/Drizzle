/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeUtils
{
    public static String GetSortableDate(Date d)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String result = format.format(d);
        return result;
    }

    public static String GetSortableCurrentDate()
    {
        return GetSortableDate(new Date());
    }

    public static int getCurrentTimeInSeconds()
    {
        long msSince1970 = getCurrentTimeinMilliseconds();
        int sSince1970 = (int)(msSince1970/1000L);
        return sSince1970;
    }

    public static long getCurrentTimeinMilliseconds()
    {
        Date date = new Date();
        long msSince1970 = date.getTime();
        return msSince1970;
    }

}

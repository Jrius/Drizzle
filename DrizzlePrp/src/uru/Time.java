/*
    Drizzle - A general Myst tool.
    Copyright (C) 2008  Dustin Bernard.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package uru;

import java.util.Date;

//All times herein use milliseconds, whereas Uru uses seconds.
public class Time
{
    public static class agetimeinfo
    {
        public double hrsAgefileDayLength;
        public long msAgefileStartDate;
        public long msDayLength;
    }
    public static class agetimeinfo2 extends agetimeinfo
    {
        public long msServerTime;
        public long msDniTime;
        public long msAgeTime;
        public long numDay;
        public long msTimeOfDay;
        public double percentTimeOfDay;

        public long msCurDayStart;
        public long msNextDayStart;
    }
    public static class podtime extends agetimeinfo2
    {
        public long msTimeForSymbol;
        
        public long msCurDaySymbol;
        public long msNextDaySymbol;
    }
    public static agetimeinfo GetAgeTimeInfo(long msDayLength, long msAgefileStartDate)
    {
        agetimeinfo result = new agetimeinfo();
        
        result.msDayLength = msDayLength;
        result.msAgefileStartDate = msAgefileStartDate;
        result.hrsAgefileDayLength = (double)msDayLength/3600.0;
        
        return result;
    }
    public static agetimeinfo2 GetAgeTimeInfo2(long msDayLength, long msAgefileStartDate, long msServerTime)
    {
        agetimeinfo2 result = new agetimeinfo2();
        
        result.msDayLength = msDayLength;
        result.msAgefileStartDate = msAgefileStartDate;
        result.hrsAgefileDayLength = (double)msDayLength/3600.0;
        
        result.msServerTime = msServerTime;
        result.msDniTime = ServerTimeToDniTime(result.msServerTime);
        result.msAgeTime = result.msServerTime - result.msAgefileStartDate;
        result.numDay = result.msAgeTime / result.msDayLength;
        result.msTimeOfDay = result.msAgeTime % result.msDayLength;
        result.percentTimeOfDay = (double)result.msTimeOfDay / (double)result.msDayLength;
        result.msCurDayStart = result.msAgefileStartDate + result.numDay * result.msDayLength;
        result.msNextDayStart = result.msCurDayStart + result.msDayLength;
        
        return result;
    }
    public static podtime GetPodAgeTimeInfo(long msDayLength, long msAgefileStartDate, long msServerTime, long msTimeForSymbol)
    {
        podtime result = new podtime();
        
        result.msDayLength = msDayLength;
        result.msAgefileStartDate = msAgefileStartDate;
        result.hrsAgefileDayLength = (double)msDayLength/3600.0;
        
        result.msServerTime = msServerTime;
        result.msDniTime = ServerTimeToDniTime(result.msServerTime);
        result.msAgeTime = result.msServerTime - result.msAgefileStartDate;
        result.numDay = result.msAgeTime / result.msDayLength;
        result.msTimeOfDay = result.msAgeTime % result.msDayLength;
        result.percentTimeOfDay = (double)result.msTimeOfDay / (double)result.msDayLength;
        result.msCurDayStart = result.msAgefileStartDate + result.numDay * result.msDayLength;
        result.msNextDayStart = result.msCurDayStart + result.msDayLength;
        
        result.msTimeForSymbol = msTimeForSymbol;
        result.msCurDaySymbol = result.msCurDayStart + result.msTimeForSymbol;
        result.msNextDaySymbol = result.msNextDayStart + result.msTimeForSymbol;
        
        return result;
    }
    public static long ServerTimeToDniTime(long servertime)
    {
        return servertime - 7*60*60*1000; //new mexico time zone.
    }
    public static long DniTimeToServerTime(long dnitime)
    {
        return dnitime + 7*60*60*1000; //new mexico time zone.
    }

    public static long PtGetServerTime()
    {
        //really just the UTC time in ms since epoch.
        long servertime = new Date().getTime();
        return servertime;
    }
    
    public static long PtGetDniTime()
    {
        long servertime = PtGetServerTime();
        long dnitime = ServerTimeToDniTime(servertime);
        return dnitime;
    }

    public static String TimeToString(long time)
    {
        //long time = PtGetServerTime();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm:ss", java.util.Locale.CANADA);
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String result = sdf.format(new Date(time));
        return result;
    }

}

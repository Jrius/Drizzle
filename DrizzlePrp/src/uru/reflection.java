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

import shared.FileUtils;
import shared.m;
import java.lang.reflect.Field;
import prpobjects._staticsettings;

public class reflection
{
    
    public static String reflectionReportText(Object obj)
    {
        StringBuilder result = new StringBuilder();
        deepReflectionReportText(obj, obj.getClass(), result, 0);
        return result.toString();
    }
    
    static void depthString(StringBuilder result, int depth)
    {
        for(int i=0;i<depth;i++)
        {
            result.append("  ");
        }
    }
    
    public static void deepReflectionReportText(Object obj, Class objclass, StringBuilder result, int depth)
    {
        
        try
        {
            if(depth==42)
            {
                int ii=3;
            }

            //if simple, just output
            if(objclass.isPrimitive()||objclass==String.class||objclass.isEnum())
            {
                depthString(result, depth);
                result.append("type:"+objclass.getName()+" value:"+obj.toString()+"\n");
            }
            //if array, do each element
            else if(objclass.isArray())
            {
                depthString(result,depth);
                result.append("type:"+objclass.getName()+" array elements:\n");
                int length = java.lang.reflect.Array.getLength(obj);
                for(int i=0;i<length;i++)
                {
                    Object arrayitem = java.lang.reflect.Array.get(obj, i);
                    deepReflectionReportText(arrayitem,arrayitem.getClass(),result, depth+1);
                }
            }
            //otherwise, it's a class?
            else
            {
                if(objclass==Class.class) return;
                
                depthString(result,depth);
                result.append("type:"+objclass.getName()+" class members:\n");

                //if inherited, do parent
                if(objclass.getSuperclass()!=Object.class) //get ancestor's info
                {
                    //we don't actually use inheritance this way.
                    //deepReflectionReport(obj,objclass.getSuperclass(),result, depth+0);
                }

                Field[] fields = objclass.getDeclaredFields(); //get all fields
                java.lang.reflect.AccessibleObject.setAccessible(fields, true);
                for(int i=0;i<fields.length;i++)
                {
                    Class curclass = fields[i].getType();
                    Object curfield = fields[i].get(obj);
                    deepReflectionReportText(curfield,curclass,result,depth+1);
                }
            }
        }
        catch(Exception e)
        {
            //result.append("Error creating report.\n");
            m.err("Error creating report.");
        }
        
    }

    public static void reflectionReportToFile(Object obj, String outfolder)
    {
        String report = reflectionReportXml(obj);
        byte[] report2 = report.getBytes();
        FileUtils.WriteFile(outfolder+"/reflectionreport.xml", report2);
    }
    
    public static String reflectionReportXml(Object obj)
    {
        StringBuilder result = new StringBuilder();
        deepReflectionReportXml(obj, result, 0);
        return result.toString();
    }
    
    public static Class[] blacklist = new Class[]{Class.class};
    
    public static String sane(Class s)
    {
        String result = s.getSimpleName();
        result = result.replace(';', '_');
        result = result.replace('$','_');
        result = result.replace('.','_');
        result = result.replace('[','_');
        result = result.replace(']','_');
        return result;
    }
    public static String sane2(String s)
    {
        String result = s;
        result = result.replace('<','_' );
        result = result.replace('>','_' );
        result = result.replace('&','_' );
        return result;
    }
    public static void deepReflectionReportXml(Object obj, StringBuilder result, int depth)
    {
        
        try
        {
            if(obj==null)
            {
                result.append("<null />");
                return;
            }
            
            Class objclass = obj.getClass();
            String classname = sane(objclass);
            
            if(depth>=8)
            {
                return;
            }
            
            for(int i=0;i<blacklist.length;i++)
            {
                if(blacklist[i]==objclass) return;
            }

            //if simple, just output
            if(objclass.isPrimitive()||objclass==String.class||objclass.isEnum()||objclass==Integer.class||objclass==Short.class||objclass==Byte.class)
            {
                //depthString(result, depth);
                result.append("<"+classname+">"+sane2(obj.toString())+"</"+classname+">");
            }
            //if array, do each element
            else if(objclass.isArray())
            {
                //depthString(result,depth);
                result.append("<array"+classname+">");
                int length = java.lang.reflect.Array.getLength(obj);
                for(int i=0;i<length;i++)
                {
                    Object arrayitem = java.lang.reflect.Array.get(obj, i);
                    //java.lang.reflect.Array.
                    deepReflectionReportXml(arrayitem,result, depth+1);
                }
                result.append("</array"+classname+">");
            }
            //otherwise, it's a class?
            else
            {
                //if(objclass==Class.class) return;
                
                //depthString(result,depth);
                result.append("<"+classname+">");

                //if inherited, do parent
                if(objclass.getSuperclass()!=Object.class) //get ancestor's info
                {
                    //we don't actually use inheritance this way.
                    //deepReflectionReport(obj,objclass.getSuperclass(),result, depth+0);
                }

                Field[] fields = objclass.getDeclaredFields(); //get all fields
                java.lang.reflect.AccessibleObject.setAccessible(fields, true);
                for(int i=0;i<fields.length;i++)
                {
                    Class curclass = fields[i].getType();
                    Object curfield = fields[i].get(obj);
                    deepReflectionReportXml(curfield,result,depth+1);
                }
                result.append("</"+classname+">");
            }
        }
        catch(Exception e)
        {
            //result.append("Error creating report.\n");
            m.err("Error creating report.");
            e.printStackTrace();
        }
        
    }
}

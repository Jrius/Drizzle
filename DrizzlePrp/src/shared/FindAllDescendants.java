/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shared;

import java.util.Vector;
import java.lang.reflect.Field;
import shared.m;
import java.util.IdentityHashMap;

public class FindAllDescendants
{
    public static <T> Vector<T> FindAllDescendantsByClass(Class<T> classToFind, Object rootobj)
    {
        Vector<T> foundObjs = new Vector<T>();
        IdentityHashMap<Object,Object> seenObjects = new IdentityHashMap<Object,Object>(); //just using it as a vector.
        FindAllDescendantsByClass2(classToFind, rootobj, null, foundObjs, seenObjects);
        return foundObjs;
    }
    
    private static <T> void FindAllDescendantsByClass2(Class<T> classToFind, Object obj, Class objclass, Vector<T> foundObjs, IdentityHashMap<Object,Object> seenObjects)
    {
        if(obj==null) return;
        
        if(objclass==null) objclass = obj.getClass();
        
       
        if(objclass.isPrimitive()) return;
        if(objclass==String.class) return;
        if(objclass==Byte.class) return;
        if(objclass==Integer.class) return;
        if(objclass==Short.class) return;
        if(objclass==Boolean.class) return;
        if(objclass==Long.class) return;
        if(objclass==Float.class) return;
        if(objclass==Double.class) return;
        if(objclass==Character.class) return;

        if(seenObjects.containsKey(obj)) return;
        seenObjects.put(obj, null);
        
        //m.msg(obj.toString());
        
        if(objclass.equals(classToFind))
        {
            T typedobj = (T)obj;
            foundObjs.add(typedobj);
            //return; this will stop us from finding members of this class that are also an instance.
        }
        
        
        if(objclass.isEnum()) return;
        
        if(objclass==Class.class) return;
        if(objclass==Object.class) return;

        //if(objclass==String.class) return;
        //if(objclass==Byte.class) return;
        //if(objclass==Integer.class) return;
        //if(objclass==Short.class) return;
        //if(objclass==Boolean.class) return;
        //if(objclass==Long.class) return;
        //if(objclass==Float.class) return;
        //if(objclass==Double.class) return;
        //if(objclass==Character.class) return;
        
        //if array, do each element
        else if(objclass.isArray())
        {
            //for efficiency, skip primitive arrays.
            Class arrayclass = objclass.getComponentType();
            if(!arrayclass.isPrimitive())
            {
                int length = java.lang.reflect.Array.getLength(obj);
                for(int i=0;i<length;i++)
                {
                    Object arrayitem = java.lang.reflect.Array.get(obj, i);
                    FindAllDescendantsByClass2(classToFind,arrayitem,null,foundObjs,seenObjects);
                }
            }
            return;
        }
        
        
        //if inherited, do parent (because the parent fields will not be included in objclass.getDeclaredFields().)
        Class superclass = objclass.getSuperclass();
        if(superclass!=Object.class)
        {
            FindAllDescendantsByClass2(classToFind,obj,superclass,foundObjs,seenObjects);
        }

        //get all member fields.
        Field[] fields = objclass.getDeclaredFields(); //get all fields
        java.lang.reflect.AccessibleObject.setAccessible(fields, true);
        for(int i=0;i<fields.length;i++)
        {
            try
            {
                Object curfield = fields[i].get(obj);
                FindAllDescendantsByClass2(classToFind,curfield,null,foundObjs,seenObjects);
            }
            catch(Exception e)
            {
                m.err("FindAllDescendants: unable to get field.");
            }
        }
        
    }
}

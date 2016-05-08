/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import java.util.Vector;
import shared.m;
import prpobjects.*;
import java.lang.reflect.Field;
import shared.Flt;

public abstract class dvNode
{
    Vector<dvNode> children = new Vector();
    nodeinfo info;

    public dvNode getChild(int index)
    {
        return children.get(index);
    }
    public int getChildCount()
    {
        return children.size();
    }
    public int getChildIndex(dvNode n)
    {
        return children.indexOf(n);
    }
    public boolean isLeaf()
    {
        return false;
    }
    public void loadChildren()
    {
        //should be overridden for lazy loading.
    }
    public void startEdit()
    {
        //should be overridden for editable items.
    }
    public void onSelect(guiTree tree)
    {

    }
    public void onDoubleClick(guiTree tree)
    {

    }
    public void onSave() throws Exception
    {
        m.throwUncaughtException("Unimplemented save: "+this.getClass().getName());
    }
    protected void save(Object newval) throws Exception
    {
        info.field.set(info.parent.obj, newval);
        info.rootobject.markAsChanged();
        info.prp.markAsChanged();
    }
    public static dvNode load(nodeinfo info)//Object obj, Class c, String name, Field field, PrpRootObject rootobject, prpfile prp)
    {
        //nodeinfo info = new nodeinfo();
        //info.cls = c;
        //info.obj = obj;
        //info.name = name;
        //info.field = field;
        //info.rootobject = rootobject;
        //info.prp = prp;

        if(info.obj==null)
        {
            //m.throwUncaughtException("unhandled.");
            return new dvNull(info.cls,info.name);
        }

        //Class c = obj.getClass();

        if(info.cls.isPrimitive())
        {
            return new dvNull(info.cls,info.name);
        }
        else if(info.cls.isArray())
        {
            //return new dvNull(info.cls,info.name);
            return new dvArray(info);
        }
        else
        {
            if(info.cls==Integer.class) return new dvInteger(info);
            if(info.cls==Bstr.class) return new dvBstr(info);
            if(info.cls==Urustring.class) return new dvUrustring(info);
            if(info.cls==Uruobjectref.class) return new dvUruobjectref(info);
            if(info.cls==Byte.class) return new dvByte(info);
            if(info.cls==Flt.class) return new dvFlt(info);
            if(info.cls==Short.class) return new dvShort(info);
            if(info.cls==Typeid.class) return new dvTypeid(info);
            if(info.cls==Float.class) return new dvFloat(info);
            if(info.cls==Transmatrix.class) return new dvTransmatrix(info);

            return new dvObject(info);
        }
    }
    public static dvNode[] findChildren(nodeinfo info)
    {
        //Class c = obj.getClass();

        if(info.cls.isPrimitive())
        {
            m.throwUncaughtException("Cannot load children of primitive.");
        }
        if(info.cls.isArray())
        {
            m.throwUncaughtException("Cannot load children of array.");
        }

        Field[] fields = info.cls.getDeclaredFields(); //get all fields
        java.lang.reflect.AccessibleObject.setAccessible(fields, true);
        dvNode[] r = new dvNode[fields.length];
        for(int i=0;i<fields.length;i++)
        {
            try{
                Class curclass;
                Field curfield2 = fields[i];
                Object curfield = curfield2.get(info.obj);
                if(curfield!=null)
                {
                    curclass = curfield.getClass(); //bugfix: get the actual class, not some ancestor or interface.
                }
                else
                {
                    curclass = curfield2.getType();
                }
                //deepReflectionReportText(curfield,curclass,result,depth+1);
                String curname = curfield2.getName();
                nodeinfo childinfo = new nodeinfo();
                childinfo.cls = curclass;
                childinfo.field = curfield2;
                childinfo.name = curname;
                childinfo.obj = curfield;
                childinfo.parent = info;
                childinfo.rootobject = info.rootobject;
                childinfo.prp = info.prp;
                childinfo.root = info.root;
                r[i] = dvNode.load(childinfo);
            }catch(IllegalAccessException e){
                m.throwUncaughtException("Security problem.");
            }

        }
        return r;
    }
}

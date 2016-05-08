/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package deepview2;

import shared.m;
import prpobjects.*;
import javax.swing.*;
import java.util.Vector;

public class dvArray extends dvNode
{
    int length;

    public dvArray(nodeinfo info2)
    {
        info = info2;
        length = java.lang.reflect.Array.getLength(info.obj);
    }

    public String toString()
    {
        return info.name+": "+super.toString();
    }
    /*public static class Org
    {
        java.util.Stack<Object> stack = new java.util.Stack();
        Vector<Org> children = new Vector();
        Vector<Object> objs = new Vector();
        boolean childrenAreOrg = false;
        public void add(Object obj)
        {
            stack.push(obj);
            if(stack.size())
            int size = objs.size();
            if(size==10)
            {
                //too big to fit another; push it back.
            }
        }
    }*/
    public void loadChildren()
    {
        children.clear();

        /*Org cur = new Org();

        Object[][][] items;
        int thousands = (length)/1000;;
        items = new Object[thousands][][];
        for(int i=0;i<thousands;i++)*/

        /*int numTenthousands = length/10000; //could be more than 10.
        int numThousands = (length%10000)/1000;
        int numHundreds = (length%1000)/100;
        int numTens = (length%100)/10;
        int numOnes = (length%10)/1;*/



        int depth;
        if(length>10000) depth = 10000;
        else if(length>1000) depth = 1000;
        else if(length>100) depth = 100;
        else if(length>10) depth = 10;
        else depth = 1;

        //Integer curpos = 0;
        createLevels(this,depth,length,0);

        /*if(depth==3)
        {
            int childcount = (length+1)/1000 + 1;
            for(int i=0;i<childcount;i++)
            {
                nodeinfo childinfo = new nodeinfo();
                int start = 1000*i;
                int count = length-start;
                if(count>1000) count = 1000;
                dvArrayCollapse child = new dvArrayCollapse(childinfo,info.obj,start,count);
                children.add(child);
            }
        }
        if(depth==2)
        {
            int childcount = (length+1)/100 + 1;
            for(int i=0;i<childcount;i++)
            {
                nodeinfo childinfo = new nodeinfo();
                int start = 100*i;
                int count = length-start;
                if(count>100) count = 100;
                dvArrayCollapse child = new dvArrayCollapse(childinfo,info.obj,start,count);
                children.add(child);
            }
        }
        if(depth==1)
        {
            int childcount = length;
            for(int i=0;i<childcount;i++)
            {
                nodeinfo childinfo = new nodeinfo();
                dvNode child = dvNode.load(info);
                children.add(child);
            }
        }*/
    }
    public Class getClass(Object obj)
    {
        if(obj==null)
        {
            return info.cls;
        }
        else
        {
            return obj.getClass();
        }
    }
    public void createLevels(dvNode cur, int curdepth, int curlength, int curpos)
    {
        if(curdepth!=1)
        {
            int fullsets = curlength/curdepth;
            int residue = curlength%curdepth;
            int sets = fullsets + ((residue==0)?0:1);
            for(int i=0;i<fullsets;i++)
            {
                int start = curpos+ i*curdepth;
                int count = curdepth;
                nodeinfo childinfo = info.copyinherited();
                childinfo.name = Integer.toString(start)+".."+Integer.toString(start+count-1);
                dvNode newnode = new dvArrayCollapse(childinfo,info.obj,start,count);
                cur.children.add(newnode);
                createLevels(newnode,curdepth/10,count,start);
            }
            if(residue!=0)
            {
                int start = curpos+fullsets*curdepth;
                int count = residue;
                nodeinfo childinfo = info.copyinherited();
                childinfo.name = Integer.toString(start)+".."+Integer.toString(start+count-1);
                dvNode newnode = new dvArrayCollapse(childinfo,info.obj,start,count);
                cur.children.add(newnode);
                createLevels(newnode,curdepth/10,count,start);
            }
        }
        else
        {
            for(int i=0;i<curlength;i++)
            {
                int start = curpos +i;
                nodeinfo childinfo = info.copyinherited();
                childinfo.name = Integer.toString(start);
                childinfo.obj = java.lang.reflect.Array.get(info.obj, start);
                childinfo.cls = getClass(childinfo.obj);
                dvNode newnode = dvNode.load(childinfo);
                cur.children.add(newnode);
            }
        }
    }
    public void onDoubleClick(guiTree tree)
    {
        //String curval = val.toString();

        //guiEditor ge = new guiEditor(this);
        //_editor = new JTextField();
        //_editor.setPreferredSize(new java.awt.Dimension(100, 20));
        //ge.panel.add(_editor);
        //_editor.invalidate();
        //tree.createNewEditorWindow(ge);
        //_editor.setText(curval);
    }
    /*public void onSave() throws Exception
    {
        //String str = _editor.getText();
        //Urustring newval = Urustring.createFromString(str);
        //save(newval);
    }*/
    public static class dvArrayCollapse extends dvNode
    {

        public dvArrayCollapse(nodeinfo info2, Object array, int start, int count)
        {
            info = info2;
        }
        public String toString()
        {
            return info.name;
        }
    }
}

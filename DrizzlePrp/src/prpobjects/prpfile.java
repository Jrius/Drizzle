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

package prpobjects;

//import java.util.Vector;
import java.lang.reflect.Field;
import shared.m;
import uru.context;
import shared.Bytes;
import prpobjects.Typeid;
import prpobjects.prputils.Compiler.Decider;
import java.util.Vector;
import shared.FileUtils;
import java.io.File;
import uru.Bytestream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
/**
 *
 * @author user
 */
public class prpfile
{
    public PrpHeader header;
    //public Vector<PrpRootObject> extraobjects;
    //public PrpRootObject[] objects;
    public Vector<PrpRootObject> objects2 = new Vector();
    public PrpObjectIndex objectindex;
    
    public String filename;
    private boolean markAsChanged = false;
    
    public prpfile()
    {
        //extraobjects = new Vector<PrpRootObject>();
    }
    /*public void renameObject(String oldname, Typeid type, String newname)
    {
        PrpRootObject obj = findObject(oldname, type);
        obj.
    }*/
    public boolean hasChanged()
    {
        return markAsChanged;
    }
    public void markAsChanged()
    {
        markAsChanged = true;
    }
    public Vector<Uruobjectref> getAllRootObjectRefs()
    {
        Vector<Uruobjectref> refs = new Vector();
        for(PrpRootObject ro: objects2)
        {
            refs.add(ro.getref());
        }
        return refs;
    }
    public void sort()
    {
        Vector<PrpRootObject> newobjects = getSortedObjects();
        objects2 = newobjects;
    }
    public Vector<PrpRootObject> getSortedObjects()
    {
        Vector<PrpRootObject> newobjects = (Vector)objects2.clone();
        //PrpRootObject[] newobjects = java.util.Arrays.copyOf(objects, objects.length);
        java.util.Collections.sort(newobjects, new java.util.Comparator() {

            public int compare(Object o1, Object o2) {
                PrpRootObject r1 = (PrpRootObject)o1;
                PrpRootObject r2 = (PrpRootObject)o2;
                String t1 = r1.header.objecttype.toString();
                String t2 = r2.header.objecttype.toString();
                int comp = t1.compareToIgnoreCase(t2);
                //if(comp==-1||comp==1) return comp;
                //otherwise they are the same type
                return comp;
            }
        });
        /*java.util.Arrays.sort(newobjects, new java.util.Comparator() {

            public int compare(Object o1, Object o2) {
                PrpRootObject r1 = (PrpRootObject)o1;
                PrpRootObject r2 = (PrpRootObject)o2;
                String t1 = r1.header.objecttype.toString();
                String t2 = r2.header.objecttype.toString();
                int comp = t1.compareToIgnoreCase(t2);
                //if(comp==-1||comp==1) return comp;
                //otherwise they are the same type
                return comp;
            }
        });*/
        return newobjects;
    }
    public Vector<Vector<PrpRootObject>> getOrderedObjects() //changes the order of objects
    {
        Vector<Vector<PrpRootObject>> r = new Vector();
        Vector<PrpRootObject> newobjects = getSortedObjects();
        Typeid lasttype = null;
        Vector<PrpRootObject> lastvec = null;
        for(PrpRootObject ro: newobjects)
        {
            if(ro.header.objecttype!=lasttype)
            {
                lastvec = new Vector();
                r.add(lastvec);
                lasttype = ro.header.objecttype;
            }
            lastvec.add(ro);
        }
        return r;
    }
    public <T> Vector<T> FindAllInstances(Class<T> klass)
    {
        Vector<T> r = shared.FindAllDescendants.FindAllDescendantsByClass(klass, objects2);
        return r;
    }
    public PrpRootObject findFirstScenenode()
    {
        for(PrpRootObject obj: objects2)
        {
            if(obj.header.objecttype==Typeid.plSceneNode)
            {
                return obj;
            }
        }
        return null;
    }
    public void addObject(Uruobjectref ref, uruobj obj)
    {
        PrpRootObject ro = PrpRootObject.createFromDescAndObject(ref.xdesc, obj);
        addObject(ro);
    }
    public PrpRootObject addObject(Typeid type, String name, uruobj obj)
    {
        Uruobjectdesc desc = Uruobjectdesc.createDefaultWithTypeNamePagePagetype(type, name, this.header.pageid, this.header.pagetype);
        PrpRootObject ro = PrpRootObject.createFromDescAndObject(desc, obj);
        this.addObject(ro);
        return ro;
    }
    public void addObject(PrpRootObject obj)
    {
        //extraobjects.add(obj);
        objects2.add(obj);
        //this.mergeExtras();
    }
    /*public PrpRootObject readSingleObject(String name, Typeid type, String filename)
    {
        for(PrpObjectIndex.ObjectindexObjecttype oiot: objectindex.types)
        {
            if(oiot.type==type)
            {
                for(PrpObjectIndex.ObjectindexObjecttypeObjectdesc oiotod: oiot.descs)
                {
                    if(oiotod.desc.objectname.toString().equals(name))
                    {
                        context c = context.createFromBytestream(Bytestream.)
                        oiotod.offset;
                    }
                }
            }
        }
    }*/
    public Vector<String> findAllSceneobjectsThatStartWith(String startString)
    {
        Vector<String> result = new Vector();
        for(PrpRootObject obj: objects2)
        {
            if(obj.header.objecttype==Typeid.plSceneObject)
            {
                if(obj.header.desc.objectname.toString().startsWith(startString))
                {
                    result.add(obj.header.desc.objectname.toString());
                }
            }
        }
        return result;
    }
    public Vector<String> findAllSceneobjectsThatReferencePythonfilemod(String pfmname)
    {
        Vector<String> result = new Vector();
        for(PrpRootObject obj: objects2)
        {
            if(obj.header.objecttype==Typeid.plSceneObject)
            {
                prpobjects.plSceneObject so = obj.castTo();
                for(Uruobjectref ref: so.modifiers)
                {
                    if(ref.hasref() && ref.xdesc.objecttype==Typeid.plPythonFileMod && ref.xdesc.objectname.toString().equals(pfmname))
                    {
                        result.add(obj.header.desc.objectname.toString());
                    }
                }
            }
        }
        return result;
    }
    public boolean contains(Uruobjectdesc desc)
    {
        PrpRootObject result = this.findObjectWithDesc(desc);
        return (result!=null);
    }
    public boolean contains(String name, Typeid type)
    {
        Uruobjectdesc desc = findDescInIndex(name,type);
        return (desc!=null);
    }
    public Uruobjectdesc findDescInIndex(String name, Typeid type)
    {
        for(PrpObjectIndex.ObjectindexObjecttype oiot: objectindex.types)
        {
            if(oiot.type==type)
            {
                for(PrpObjectIndex.ObjectindexObjecttypeObjectdesc oiotod: oiot.descs)
                {
                    m.msg(oiotod.desc.toString());
                    if(oiotod.desc.objectname.toString().equals(name))
                    {
                        return oiotod.desc;
                    }
                }
            }
        }
        return null;
    }
    public static prpfile readHeaderAndIndexFromFile(String filename)
    {
        prpfile result = new prpfile();
        //read file
        byte[] filedata = shared.FileUtils.ReadFile(filename);
        File f = new File(filename);
        context c = context.createFromBytestream(new Bytestream(filedata));
        c.curFile = filename;
        result.header = new PrpHeader(c);
        //result.objectindex = new PrpObjectIndex(c.Fork(new Bytestream(c.in,result.header.offsetToObjectIndex)));
        //result.objectindex = new PrpObjectIndex(c.Fork(c.in.Fork(result.header.offsetToObjectIndex)));
        context c2 = c.Fork(result.header.offsetToObjectIndex);
        result.objectindex = new PrpObjectIndex(c2);
        c2.close();
        result.filename = filename;
        
        return result;
    }
    public void markObjectDeleted(Uruobjectref ref, boolean warnIfNotPresent)
    {
        if(ref.hasref())
        {
            markObjectDeleted(ref.xdesc.objecttype,ref.xdesc.objectname.toString(),warnIfNotPresent);
        }
    }
    public void markObjectDeleted(Typeid type, String name)
    {
        markObjectDeleted(type,name,true);
    }
    public void markObjectDeleted(Typeid type, String name, boolean warnIfNotPresent)
    {
        for(PrpRootObject obj: objects2)
        {
            if(obj.header.desc.objecttype==type && obj.header.desc.objectname.toString().equals(name))
            {
                obj.tagDeleted = true;
                return;
            }
        }
        if(warnIfNotPresent) m.msg("Could not find object to remove.");
    }
    public void deleteObject(Typeid type, String name)
    {
        PrpRootObject ro = this.findObject(name, type);
        objects2.remove(ro);
    }
    public void orderObjects()
    {
        //java.util.Arrays.sort(objects);
        java.util.Collections.sort(objects2);
    }
    /*public void mergeExtras()
    {
        int newsize = objects.length + extraobjects.size();
        PrpRootObject[] newobjects = new PrpRootObject[newsize];
        
        for(int i=0;i<objects.length;i++)
        {
            newobjects[i] = objects[i];
        }
        for(int i=0;i<extraobjects.size();i++)
        {
            newobjects[objects.length+i] = extraobjects.get(i);
        }
        
        extraobjects.clear();
        objects = newobjects;
    }*/
    /*public PrpRootObject[] getObjects()
    {
        int size = objects.size();
        PrpRootObject[] result = new PrpRootObject[size];
        for(int i=0;i<size;i++)
        {
            result[i] = objects.get(i);
        }
        return result;
    }*/
    public static prpfile createFromContext(context c, Typeid[] typesToRead)
    {
        prpfile result = prputils.ProcessAllMoul(c, false, typesToRead);
        return result;
    }
    public static prpfile createFromObjectsAndInfo(Vector<PrpRootObject> objs, String agename, String pagename, Pageid pageid, Pagetype pagetype)
    {
        //PrpRootObject[] objs2 = uru.generics.convertVectorToArray(objs, PrpRootObject.class);
        //return createFromObjectsAndInfo(objs2,agename,pagename,pageid,pagetype);

        prpfile result = new prpfile();
        result.objects2 = objs;
        //result.extraobjects = new Vector<PrpRootObject>();
        /*result.objects = new Vector<PrpRootObject>();
        for(PrpRootObject obj: objs)
        {
            result.objects.add(obj);
        }*/
        result.header = PrpHeader.createFromInfo(agename, pageid, pagetype, pagename);

        //these don't seem to be needed for compilation.  We may need to regenerate the ObjectIndex if we want to merge objects in and parse them.
        //result.objectindex = PrpObjectIndex.
        //result.filename

        return result;

    }
    
    public static prpfile createFromObjectsAndInfo(PrpRootObject[] objs, String agename, String pagename, Pageid pageid, Pagetype pagetype)
    {
        Vector<PrpRootObject> objs2 = uru.generics.convertArrayToVector(objs);
        return createFromObjectsAndInfo(objs2,agename,pagename,pageid,pagetype);
    }
    public PrpRootObject addScenenode()
    {
        prpobjects.x0000Scenenode sn = prpobjects.x0000Scenenode.createDefault();

        String nodename = this.header.agename.toString() + "_" + this.header.pagename.toString();
        Uruobjectdesc sndesc = Uruobjectdesc.createDefaultWithTypeNamePagePagetype(Typeid.plSceneNode, nodename, this.header.pageid, this.header.pagetype);
        PrpRootObject snro = PrpRootObject.createFromDescAndObject(sndesc, sn);
        this.addObject(snro);
        return snro;
    }
    public static prpfile create(String agename, String pagename, Pageid pageid, Pagetype pagetype)
    {
        //create the prpfile
        PrpRootObject[] objects = new PrpRootObject[]{ };
        prpfile prp = prpfile.createFromObjectsAndInfo(objects, agename, pagename, pageid, pagetype);
        return prp;
    }
    public static prpfile createFromFile(String filename, boolean readRaw)
    {
        File f = new File(filename);
        return createFromFile(f,readRaw);
    }
    public static prpfile createFromFile(File f, boolean readRaw)
    {
        //read file
        //Test removal:
        //byte[] filedata = shared.FileUtils.ReadFile(f);
        //context c = context.createFromBytestream(new Bytestream(filedata));
        context c = context.createFromBytestream(shared.SerialBytestream.createFromFile(f)); //test add
        c.curFile = f.getAbsolutePath();
        prpfile prp = prpobjects.prpprocess.ProcessAllObjects(c,readRaw); //read raw
        prp.filename = f.getAbsolutePath();
        c.close(); //test add
        
        return prp;
    }
    public static prpfile createFromBytes(byte[] data, boolean readRaw)
    {
        context c = context.createFromBytestream(shared.ByteArrayBytestream.createFromByteArray(data));
        prpfile prp = prpobjects.prpprocess.ProcessAllObjects(c, readRaw);
        c.close();
        return prp;
    }
    public void saveAsFile(String filename)
    {
        //this.mergeExtras();
        shared.IBytedeque result = this.saveAsBytes();
        //FileUtils.WriteFile(filename, result);
        result.writeAllBytesToFile(filename);
        
    }
    public shared.IBytedeque saveAsBytes()
    {
        //use the decider that always returns true by default.
        /*class compileDecider implements uru.moulprp.prputils.Compiler.Decider{
            public boolean isObjectToBeIncluded(Uruobjectdesc desc){
                return true;
            }
        }*/
        return saveAsBytes(prpobjects.prputils.Compiler.getDefaultDecider());
    }
    public shared.IBytedeque saveAsBytes(Decider decider)
    {
        if(decider==null) decider = prpobjects.prputils.Compiler.getDefaultDecider();
        //mergeExtras();
        //changed for Drizzle29:
        //this.sort();
        //orderObjects();
        FixObjectOrder(); //better version that sorts and orders, but also makes sure plSceneObject trees are in the correct order.
        //end Drizzle29 change
        shared.IBytedeque result = prputils.Compiler.RecompilePrp(this, decider);
        return result;
    }
    public PrpRootObject findObject(String name, Typeid type)
    {
        for(PrpRootObject obj: objects2)
        {
            /*if(obj==null||obj.header==null||obj.header.desc==null||obj.header.objecttype==null||obj.header.desc.objectname==null||obj.header.desc.objecttype==null)
            {
                int dummy=0;
            }*/
            if(obj!=null &&obj.header.desc.objectname.toString().equals(name) && obj.header.desc.objecttype.equals(type))
            {
                return obj;
            }
        }
        return null;
    }
    /**
     * Return all root objects of the given type whose name contains the given string (case invariant).
     */
    public ArrayList<PrpRootObject> findObjectsContaining(String nameContent, Typeid type)
    {
        String nameContentLower = nameContent.toLowerCase();
        ArrayList<PrpRootObject> result = new ArrayList();
        for(PrpRootObject obj: objects2)
        {
            if(obj!=null && obj.header.desc.objectname.toString().toLowerCase().contains(nameContentLower) && obj.header.desc.objecttype.equals(type))
            {
                result.add(obj);
            }
        }
        return result;
    }
    public PrpRootObject findObjectWithRef(Uruobjectref ref)
    {
        if(!ref.hasref())
        {
            m.warn("Tried to remove object, but the ref given has no desc.");
            return null;
        }
        PrpRootObject result = findObjectWithDesc(ref.xdesc);
        return result;
    }
    public PrpRootObject findObjectWithDesc(Uruobjectdesc desc)
    {
        int numobjects = this.objects2.size();
        for(int i=0;i<numobjects;i++)
        {
            PrpRootObject curobj = this.objects2.get(i);
            Uruobjectdesc curdesc = curobj.header.desc;
            /*if(curdesc==null || curdesc.objectname==null || curdesc.objectname.toString()==null||desc==null||desc.objectname==null||desc.objectname.toString()==null)
            {
                int dummy=0;
            }*/
            if(curdesc.objectname.toString().equals(desc.objectname.toString())
                    &&(curdesc.objecttype==desc.objecttype)
                    &&(curdesc.pageid.equals(desc.pageid)))
            {
                return curobj;
            }
        }
        return null;
    }

    public void tagRootObjectAsDeleted(PrpRootObject obj)
    {
        if(obj==null) return;
        /*PrpRootObject[] newobjects = new PrpRootObject[objects.length-1];
        int pos = 0;
        for(int i=0;i<objects.length;i++)
        {
            PrpRootObject curobj = objects[i];
            if(obj!=curobj)
            {
                newobjects[pos] = curobj;
                pos++;
            }
            else
            {
                int dummy=0;
            }
        }
        objects = newobjects;*/
        for(PrpRootObject curobj: objects2)
        {
            if(obj==curobj)
            {
                curobj.tagDeleted = true;
            }
        }
    }
    public void tagRootObjectAsDeleted(Uruobjectref ref)
    {
        PrpRootObject obj = findObjectWithRef(ref);
        tagRootObjectAsDeleted(obj);
    }
    
    public PrpRootObject[] FindAllObjectsOfType(Typeid type)
    {
        return prputils.FindAllObjectsOfType(this, type);
    }
    
    public Vector<PrpRootObject> FindObjectsThatReferenceAnother(Uruobjectdesc desc)
    {
        Vector<PrpRootObject> r = new Vector();
        for(PrpRootObject ro: objects2)
        {
            prpobjects.uruobj obj = ro.getObject();
            for(Uruobjectdesc curdesc: shared.FindAllDescendants.FindAllDescendantsByClass(Uruobjectdesc.class, obj))
            {
                if(curdesc.equals(desc))
                {
                    if(!r.contains(ro))
                    {
                        r.add(ro);
                    }
                }
            }
        }
        return r;
    }

    public void _markScenenodeSceneobjects()
    {
        PrpRootObject[] sns = FindAllObjectsOfType(Typeid.plSceneNode);
        if(sns.length==0)
        {
            //e.g. texture files.
        }
        else if(sns.length==1)
        {
            prpobjects.x0000Scenenode sn = sns[0].castTo();
            for(PrpRootObject ro: objects2)
            {
                if(ro.header.objecttype==Typeid.plSceneObject)
                {
                    prpobjects.plSceneObject so = ro.castTo();
                    if(sn.objectrefs1.contains(ro.getref()))
                    {
                        so.includeInScenenode = true;
                    }
                    else
                    {
                        so.includeInScenenode = false;
                    }
                }
            }
        }
        else
        {
            m.throwUncaughtException("unexpected");
        }
    }

    public Vector<PrpRootObject> getAllChildren(Uruobjectref ref)
    {
        Vector<PrpRootObject> r = new Vector();
        getAllChildren(ref,r);
        return r;
    }
    private void getAllChildren(Uruobjectref ref, Vector<PrpRootObject> r)
    {
        PrpRootObject so_ro = findObjectWithRef(ref);
        if(so_ro!=null)
        {
            r.add(so_ro);

            plSceneObject so = so_ro.castTo();
            PrpRootObject ci_ro = findObjectWithRef(so.coordinateinterface);
            if(ci_ro!=null)
            {
                plCoordinateInterface ci = ci_ro.castTo();

                //do children
                for(Uruobjectref childref: ci.children)
                {
                    getAllChildren(childref,r);
                }
            }
        }
    }

    public static class ChildrenTree
    {
        PrpRootObject ro; //null iff root
        ArrayList<ChildrenTree> children = new ArrayList();
        ChildrenTree parent; //null iff root

        public ChildrenTree(PrpRootObject ro2, ChildrenTree parent2)
        {
            ro = ro2;
            parent = parent2;
        }

        public boolean isRoot()
        {
            return (parent==null);
        }

        public ChildrenTree RemoveChild(Uruobjectdesc desc)
        {
            for(int i=0;i<children.size();i++)
            {
                ChildrenTree child = children.get(i);
                if(child.ro.header.desc.equals(desc))
                {
                    ChildrenTree r = children.remove(i);
                    return r;
                }
            }
            return null;
        }

        public void OrderElements() //put it into canonical form
        {
            //sort first by type, then by name
            java.util.Collections.sort(children, new java.util.Comparator<ChildrenTree>(){
                public int compare(ChildrenTree a, ChildrenTree b)
                {
                    //- means a<b, 0 means a=b, + means a>b
                    //Hmm... we're using the order of the constants in Typeid.  This is okay, because we just have plCoordinateInterface and plFilterCoordInterface, which have the right order in Typeid.java.
                    int r = a.ro.header.desc.objecttype.compareTo(b.ro.header.desc.objecttype);
                    if(r!=0) return r;
                    r = a.ro.header.desc.objectname.toString().compareTo(b.ro.header.desc.objectname.toString());
                    if(r!=0) return r;
                    return 0;
                }
            });

            //do children
            for(ChildrenTree child: children)
            {
                child.OrderElements();
            }
        }

        public String toString()
        {
            if(this.isRoot()) return "(root)";
            return ro.toString();
        }

        public Vector<PrpRootObject> SerializeElements()
        {
            Vector<PrpRootObject> r = new Vector();
            SerializeElements(r);
            return r;
        }
        private void SerializeElements(Vector<PrpRootObject> r)
        {
            /*//first these children, *then* the grandchildren.
            for(ChildrenTree child: children)
            {
                r.add(child.ro);
            }

            for(ChildrenTree child: children)
            {
                child.SerializeElements(r);
            }*/

            //do depth-first
            for(ChildrenTree child: children)
            {
                r.add(child.ro);
                child.SerializeElements(r);
            }
        }

    }
    /*public ChildrenTree GetChildrenTree2(boolean includeRegularElements)
    {
        ChildrenTree root = new ChildrenTree(null, null); //root
        HashMap<Uruobjectdesc,ChildrenTree> parents = new HashMap(); //maps a desc to its parent
        //HashMap<Uruobjectdesc,ChildrenTree> entries = new HashMap(); //maps a desc to its tree

        for(PrpRootObject ro: objects2)
        {
            //check if it's a coord interface and get the children:
            //Uruobjectref[] ci_children = null;
            plCoordinateInterface ci = null;
            if(ro.header.objecttype==Typeid.plCoordinateInterface)
            {
                ci = ro.castTo();
            }
            else if(ro.header.objecttype == Typeid.plFilterCoordInterface)
            {
                plFilterCoordInterface fci = ro.castTo();
                ci = fci.parent;
            }

            //it's one of the two types of coord interfaces:
            if(ci!=null)
            {
                //check if it is a child of one already handled:
                //ChildrenTree parent = parents.get(ci.parent.sceneobject.xdesc);
                ChildrenTree parent = parents.get(ro.header.desc);
                if(parent==null) parent = root;  //it doesn't have a parent (yet) so let's add it to the root

                ChildrenTree node = new ChildrenTree(ro, parent);
                parent.children.add(node);
                //entries.put(ro.header.desc, node);
                //parents.put(ci.parent.sceneobject.xdesc, parent);
                parents.put(ro.header.desc, parent);

                //let children know we're their parent:
                for(Uruobjectref ref: ci.children)
                {
                    //get the coord interface desc
                    //Uruobjectdesc desc = ref.xdesc;
                    Uruobjectdesc desc = this.findObjectWithDesc(ref.xdesc).castToSceneObject().coordinateinterface.xdesc;

                    //check if this child has been attached yet
                    ChildrenTree curparent = parents.get(desc); //this should always be either null, or the root, (or after this point, us).
                    if(curparent==null)
                    {
                        //this child hasn't been encountered yet
                        parents.put(desc, node);
                    }
                    else
                    {
                        //this child is hooked up to the root, so move it to us:
                        ChildrenTree child = parent.RemoveChild(desc);
                        node.children.add(child);
                    }
                }
            }
            else
            {
                if(includeRegularElements)
                {
                    ChildrenTree parent = root;

                    ChildrenTree node = new ChildrenTree(ro, parent);
                    parent.children.add(node);
                }
            }
        }
        
        return root;

    }*/
    public ChildrenTree GetChildrenTree(boolean includeRegularElements)
    {
        ChildrenTree root = new ChildrenTree(null, null); //root
        HashMap<Uruobjectdesc,ChildrenTree> parents = new HashMap(); //maps a desc to its parent
        //HashMap<Uruobjectdesc,ChildrenTree> entries = new HashMap(); //maps a desc to its tree

        for(PrpRootObject ro: objects2)
        {
            boolean handled = false;
            if(ro.header.objecttype==Typeid.plSceneObject)
            {
                plSceneObject so = ro.castToSceneObject();
                if(so.coordinateinterface.hasref())
                {
                    //check if it's a coord interface and get it:
                    PrpRootObject ci_ro = this.findObjectWithDesc(so.coordinateinterface.xdesc);
                    //if(ci_ro==null || ci_ro.header==null || ci_ro.header.objecttype==null)
                    //{
                    //    int dummy=0;
                    //}
                    if(ci_ro!=null) //it might be null, if it's a deleted object or something.
                    {
                        plCoordinateInterface ci;
                        if(ci_ro.header.objecttype==Typeid.plCoordinateInterface)
                        {
                            ci = ci_ro.castTo();
                        }
                        else if(ci_ro.header.objecttype==Typeid.plFilterCoordInterface)
                        {
                            plFilterCoordInterface fci = ci_ro.castTo();
                            ci = fci.parent;
                        }
                        else throw new shared.uncaughtexception("unexpected");

                        //check if it is a child of one already handled:
                        ChildrenTree parent = parents.get(ro.header.desc);
                        if(parent==null) parent = root;  //it doesn't have a parent (yet) so let's add it to the root

                        ChildrenTree node = new ChildrenTree(ro, parent);
                        handled = true;
                        parent.children.add(node);
                        //entries.put(ro.header.desc, node);
                        //parents.put(ci.parent.sceneobject.xdesc, parent);
                        parents.put(ro.header.desc, parent);

                        //let children know we're their parent:
                        for(Uruobjectref ref: ci.children)
                        {
                            //get the coord interface desc
                            Uruobjectdesc desc = ref.xdesc;
                            //Uruobjectdesc desc = this.findObjectWithDesc(ref.xdesc).castToSceneObject().coordinateinterface.xdesc;

                            //check if this child has been attached yet
                            ChildrenTree curparent = parents.get(desc); //this should always be either null, or the root, (or after this point, us).
                            if(curparent==null)
                            {
                                //this child hasn't been encountered yet
                                parents.put(desc, node);
                            }
                            else
                            {
                                //this child is hooked up to the root, so move it to us:
                                ChildrenTree child = curparent.RemoveChild(desc);
                                if(child==null)
                                {
                                    int dummy=0;
                                }
                                node.children.add(child);
                            }
                        }
                    }
                }
            }

            if(!handled)
            {
                if(includeRegularElements)
                {
                    ChildrenTree parent = root;

                    ChildrenTree node = new ChildrenTree(ro, parent);
                    parent.children.add(node);
                }
            }
        }

        return root;

    }
    public void FixObjectOrder()
    {
        //attempt to reorder the coordinate interfaces and filtercoordinterfaces so that there is no crash

        //create tree
        prpfile.ChildrenTree root = this.GetChildrenTree(true);

        //alphabetize tree to achieve canonical form
        root.OrderElements();

        //order them
        Vector<PrpRootObject> neworder = root.SerializeElements();

        //assign this new ordering
        this.objects2 = neworder;

    }

}

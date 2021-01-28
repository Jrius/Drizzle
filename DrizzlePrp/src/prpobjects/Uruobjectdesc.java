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

import uru.context; import shared.readexception;
import uru.Bytestream;
import shared.e;
import shared.m;
import uru.Bytedeque;
import shared.IBytestream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

/**
 *
 * @author user
 */
public class Uruobjectdesc extends uruobj implements java.io.Serializable
{
    byte flag;
    //int pageid;
    public Pageid pageid;
    //short pagetype;
    public Pagetype pagetype;
    byte loadMask;
    //short objecttype;
    public Typeid objecttype;
    public int objectnumber;
    public Urustring objectname;
    //int xsomeid;
    //int xclientid;
    //byte xu1; //was really just loadMask.
    //short xm5unknown;
    public int cloneId;
    public int clonePlayerId;

    transient public Uruobjectdesc rootobj;

    public static final int kHasCloneIDs = 0x01;
    public static final int kHasLoadMask = 0x02;

    public boolean hasCloneIds()
    {
        return ((flag&kHasCloneIDs)!=0);
    }
    public boolean hasLoadMask()
    {
        return ((flag&kHasLoadMask)!=0);
    }
    public Uruobjectdesc(context c)
    {

        IBytestream data = c.in;

        this.rootobj = c.curRootObject;

        ////flag seems to have different meaninsg in Myst5/Crow/Hex, as 0x02(kHasLoadMask) was removed because the loadMask was removed.
        ////so we should probably actually remove the 0x02 and 0x04 flags when reading Myst5/Crow/Hex.
        //nevermind the above 2 lines.  Value 0x1 only occurs online, 0x2 marks the presence of loadMask, and 0x4 seems to only be in Myst5, but also marks the presence of loadMask.
        flag = data.readByte(); //e.ensureflags(flag,0x00,0x02,0x04);//should be 0 normally,1 and 2 also happen, but we need to study them. 0x04 occurs in Myst5 for example.
        //m.status("flag="+Integer.toHexString(b.ByteToInt32(flag)));
        //pageid = data.readInt();
        pageid = new Pageid(c);
        //if(pageid.prefix==97)
        //{
        //    int dummy=0;
        //}
        //pagetype = data.readShort(); e.ensure(pagetype,0,4,8,16,20); //should this be a byte? //0=page, 4=global, 8=texture/builtin.
        
        if(c.readversion==3)
        {
            pagetype = new Pagetype(c);
            if((flag&kHasLoadMask)!=0)
            {
                loadMask = data.readByte();
                //m.status("loadMask="+Integer.toHexString(b.ByteToInt32(loadMask)));
            }
            objecttype = Typeid.Read(c);
            objectname = new Urustring(c);
            if((flag&kHasCloneIDs)!=0)
            {
                cloneId = c.readInt();
                clonePlayerId = c.readInt();
            }
        }
        else if(c.readversion==4||c.readversion==7)
        {
            //in crowthistle/myst5/hexisle, we have this other flag.
            //xm5unknown = c.readShort(); //only in crowthistle?(actually a part of the Pageid.)
            pagetype = new Pagetype(c);
            //if((flag&kHasLoadMask)!=0)
            //{
                //int dummy=0;
            //}
            objecttype = Typeid.Read(c);
            objectnumber = data.readInt(); //this objects unique number in the list.(the numbering starts anew for each objecttype in each page).
            objectname = new Urustring(c);
            //if(flag==0x02 || flag==0x04)
            if((flag&0x06)!=0) //if either or both.
            {
                ////is this byte the loadFlag?  No, loadFlag is always 0, and this can have many values, such as cc,ee,e,cf,ef,f,1,cc,c,e,ee,ef,f,f1,f3 in crow/hex/myst5.
                //oops, yes it is loadFloag; my test had a type :P
                //xu1 = data.readByte();
                loadMask = data.readByte();
                //m.status("xu1="+Integer.toHexString(b.ByteToInt32(xu1)));
            }
            
            //flag &= 0x1; //unset any other bits, because they are associated with something new.
        }
        else if(c.readversion==6)
        {
            pagetype = new Pagetype(c);
            if((flag&kHasLoadMask)!=0)
            {
                loadMask = data.readByte();
                //m.status("loadMask="+Integer.toHexString(b.ByteToInt32(loadMask)));
            }
            objecttype = Typeid.Read(c);
            objectnumber = data.readInt(); //this objects unique number in the list.(the numbering starts anew for each objecttype in each page).
            objectname = new Urustring(c);
            if((flag&kHasCloneIDs)!=0)
            {
                cloneId = c.readInt();
                clonePlayerId = c.readInt();
            }
        }
        
        //_staticsettings.reportReference(this);
        //deepview.deepview.allreflinks.add(c.curRootObject, this);

        //shared.reporter.reportEvent(new uru.reporterReports.refEncountered(c.curRootObject, this), "refEncountered");
    }
    private Uruobjectdesc(){}
    public static Uruobjectdesc createDefaultWithTypeNamePrp(Typeid type, String name, prpfile prp)
    {
        return createDefaultWithTypeNamePagePagetype(type,name,prp.header.pageid,prp.header.pagetype);
    }

    public static Uruobjectdesc createDefaultWithTypeNamePage(Typeid type, String name, Pageid page)
    {
        return createDefaultWithTypeNamePagePagetype(type,name,page,Pagetype.createDefault());
    }
    public static Uruobjectdesc createDefaultWithTypeNamePagePagetype(Typeid type, String name, Pageid page, Pagetype pagetype)
    {
        Uruobjectdesc result = new Uruobjectdesc();
        result.flag = 0;
        result.objectname = Urustring.createFromString(name);
        result.objecttype = type;
        result.pageid = page;
        result.pagetype = pagetype;
        return result;
    }
    public Uruobjectref toRef()
    {
        Uruobjectref result = Uruobjectref.createFromUruobjectdesc(this);
        return result;
    }
    //public static Uruobjectdesc create(Bytestream data)
    //{
    //    return new Uruobjectdesc(data);
    //}
    public void compile(Bytedeque c)
    {
        if(c.format==Format.pots)
        {
            c.writeByte(flag);
            pageid.compile(c);
            pagetype.compile(c);
            if((flag&kHasLoadMask)!=0)
            {
                c.writeByte(loadMask);
            }
            objecttype.compile(c);
            //deque.appendInt(objectnumber); //not in pots
            objectname.compile(c);
            if((flag&kHasCloneIDs)!=0) //this should be in the other versions that have xu1, too.
            {
                c.writeInt(cloneId);
                c.writeInt(clonePlayerId);
            }
        }
        else if(c.format==Format.moul)
        {
            c.writeByte(flag);
            pageid.compile(c);
            pagetype.compile(c);
            if((flag&kHasLoadMask)!=0)
            {
                c.writeByte(loadMask);
            }
            objecttype.compile(c);
            c.writeInt(objectnumber); //has this been set correctly?  In Talcum yes, in Pots->Moul no.
            objectname.compile(c);
            if((flag&kHasCloneIDs)!=0) //this should be in the other versions that have xu1, too.
            {
                c.writeInt(cloneId);
                c.writeInt(clonePlayerId);
            }

        }
        else m.throwUncaughtException("unimplemented");

    }
    public String toString()
    {
        return objectname.toString()+"("+objecttype.toString()+")("+pageid.toString()+")";
    }
    public PrpRootObject getObjectDescribed(prpfile prp)
    {
        PrpRootObject result = prp.findObjectWithDesc(this);
        return result;
    }
    public boolean equals(Object obj)
    {
        if(obj==null) return false;
        if(!(obj instanceof Uruobjectdesc)) return false;
        Uruobjectdesc o=(Uruobjectdesc)obj;
        //if(this.flag!=o.flag) return false;
        if(!this.objectname.equals(o.objectname)) return false;
        if(!this.objecttype.equals(o.objecttype)) return false;
        if(!this.pageid.equals(o.pageid)) return false;
        if(!this.pagetype.equals(o.pagetype)) return false;
        if(this.cloneId!=o.cloneId) return false;
        if(this.clonePlayerId!=o.clonePlayerId) return false;
        return true;
    }
    /*public void addXml(StringBuilder s)
    {
        s.append("<desc>");
        s.append("<name>");objectname.addXml(s);s.append("</name>");
        s.append("<type>");objecttype.addXml(s);s.append("</type>");
        s.append("<pageid>");pageid.addXml(s);s.append("</pageid>");
        s.append("<pagetype>");pagetype.addXml(s);s.append("</pagetype>");
        s.append("<flag>");s.append(Byte.toString(flag));s.append("</flag>");
        s.append("<xu1>");s.append(Byte.toString(loadMask));s.append("</xu1>");
        s.append("</desc>");
    }
    public static Uruobjectdesc createFromXml(Element e1)
    {
        Uruobjectdesc result = new Uruobjectdesc();
        for(Node child=e1.getFirstChild();child!=null;child=child.getNextSibling())
        {
            if(child.getNodeType()==Node.ELEMENT_NODE)
            {
                Element e2 = (Element)child;
                String tag = e2.getTagName();
                if(tag.equals("name")) result.objectname = Urustring.createFromXml(e2);
                else if(tag.equals("type")) result.objecttype = Typeid.createFromXml(e2);
                else if(tag.equals("pageid")) result.pageid = Pageid.createFromXml(e2);
                else if(tag.equals("pagetype")) result.pagetype = Pagetype.createFromXml(e2);
                else if(tag.equals("flag")) result.flag = Byte.parseByte(e2.getTextContent());
                else if(tag.equals("xu1")) result.loadMask = Byte.parseByte(e2.getTextContent());
            }
        }
        return result;
    }*/
    public int hashCode()
    {
        int a = this.objectname.hashCode();
        int b = this.pageid.hashCode();
        int c = this.objecttype.hashCode();
        return this.objectname.hashCode() + this.pageid.hashCode() + this.objecttype.hashCode() + this.cloneId + this.clonePlayerId*16;
    }
    public Uruobjectdesc deepClone()
    {
        Uruobjectdesc result = new Uruobjectdesc();
        this.copyInto(result);
        return result;
    }
    public void copyInto(Uruobjectdesc result)
    {
        result.flag = flag;
        result.objectname = objectname.deepClone();
        result.objectnumber = objectnumber;
        result.objecttype = objecttype;
        result.pageid = pageid.deepClone();
        result.pagetype = pagetype.deepClone();
        //result.xm5unknown = xm5unknown;
        result.loadMask = loadMask;
        //result.xu1 = xu1;
        result.cloneId = cloneId;
        result.clonePlayerId = clonePlayerId;
    }
}

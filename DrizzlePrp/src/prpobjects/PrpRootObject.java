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

import shared.readexception;
import uru.context; import shared.readexception;
import shared.m;
import uru.Bytedeque;
import uru.Bytestream;
import java.lang.Comparable;

/**
 *
 * @author user
 */
public class PrpRootObject extends uruobj implements Comparable
{
    public Objheader header;
    public PrpObject prpobject;
    //public boolean isRaw = false;
    //public boolean saveRaw = false;
    public boolean hasChanged; //does it have changes that need to be written
    public boolean hasRaw; //does it have raw byte[] data.
    public boolean hasParsed; //does it have a real prpobject
    byte[] rawdata;
    int readversion;
    
    public boolean tagDeleted = false;
    //public boolean wasread; //was this object read rather than created from scratch
    
    public PrpRootObject(context c, boolean readRaw, int length) throws readexception
    {
        //wasread = true;

        int headerStart = c.in.getAbsoluteOffset();
        header = new Objheader(c);
        int headerEnd = c.in.getAbsoluteOffset();
        //this.isRaw = isRaw;
        //if(isRaw) saveRaw = true;
        this.hasChanged = false;
        this.hasRaw = readRaw;
        this.hasParsed = true;
        this.readversion = c.readversion;
        if(readRaw)
        {
            //rawdata = c.Fork().readBytes(length-(headerEnd-headerStart));
            rawdata = c.readBytes(length-(headerEnd-headerStart));
            this.hasParsed = false;
        }
        else
        {
            prpobject = new PrpObject(c, header.objecttype);
        }
    }
    public Uruobjectref getref()
    {
        return this.header.desc.toRef();
    }
    public int compareTo(Object o)
    {
        if(!(o instanceof PrpRootObject)) return -1;
        PrpRootObject obj = (PrpRootObject)o;
        int a = this.header.desc.objecttype.compareTo(obj.header.desc.objecttype);
        if(a<0) return -1;
        if(a>0) return 1;
        return this.header.desc.objectname.toString().compareTo(obj.header.desc.objectname.toString());
    }
    private PrpRootObject(){}
    public static PrpRootObject createFromDescAndObject(Uruobjectdesc desc, uruobj object)
    {
        PrpRootObject result = new PrpRootObject();
        result.prpobject = PrpObject.createFromUruobj(object);
        result.header = Objheader.createFromDesc(desc);
        
        result.readversion = 3;
        //result.hasChanged = false;
        result.hasChanged = true;
        //m.status("altered hasChanged flag.(remove this)");
        result.hasRaw = false;
        result.hasParsed = true;
        
        return result;
    }
    public static PrpRootObject createFromTypeNamePrpObject(Typeid type, String name, prpfile prp, uruobj obj)
    {
        PrpRootObject r = new PrpRootObject();
        r.prpobject = PrpObject.createFromUruobj(obj);
        r.header = Objheader.createFromDesc(Uruobjectdesc.createDefaultWithTypeNamePrp(type, name, prp));

        r.hasChanged = true;
        r.hasRaw = false;
        r.hasParsed = true;

        return r;
    }
    public void ensureParsed()
    {
        try
        {
            parseRawDataNow();
        }
        catch(readexception e)
        {
            m.err("Unable to parse rawdata: ",e.getMessage());
        }
    }
    public void parseRawDataNow() throws readexception
    {
        //if(isRaw)
        if(!hasParsed)
        {
            context c = context.createFromBytestream(new Bytestream(rawdata));
            c.readversion = this.readversion;
            c.curRootObject = this.header.desc;
            prpobject = new PrpObject(c, header.objecttype);
            //isRaw = false;
            hasParsed = true;
        }
    }
    
    public void markAsChanged()
    {
        //saveRaw = false;
        hasChanged = true;
    }
    
    /*public static PrpRootObject createAsRawData(context c, int length) throws readexception
    {
        PrpRootObject result = new PrpRootObject();
        result.header = new Objheader(c);
        result.rawdata = c.readBytes(length);
        return result;
    }*/
    
    public void compile(Bytedeque c)
    {
        //header.compile(c); //should we have this here?
        //if(!isRaw)
        //if(!saveRaw)
        if(hasRaw && !hasChanged)
        {
            //m.warn("Untested compilation in PrpRootObject.");
            c.writeBytes(rawdata);
        }
        else
        {
            prpobject.compile(c);
        }
    }
    
    public String toString()
    {
        /*if(prpobject!=null)
            return prpobject.toString();
        else
            return "(raw)";*/
        return "PrpRootObject: "+this.header.desc.toString();
    }
    
    public <T> T castTo() //java.lang.Class<T> objclass)
    {
        this.ensureParsed();
        T result = (T)this.prpobject.object;
        return result;
    }
    
    public <T> T castTo(T dummy)
    {
        this.ensureParsed();
        //PrpRootObject a = c.cast(this.prpobject.object);
        T result = (T)this.prpobject.object;
        return result;
    }
    
    public <T> T castTo(Class<T> cls)
    {
        this.ensureParsed();
        T result = (T)this.prpobject.object;
        return result;
    }
    
    public x0000Scenenode castToSceneNode()
    {
        this.ensureParsed();
        x0000Scenenode result = this.castTo();
        return result;
    }
    
    public plSceneObject castToSceneObject()
    {
        this.ensureParsed();
        plSceneObject result = this.castTo();
        return result;
    }
    
    public uruobj getObject()
    {
        this.ensureParsed();
        return prpobject.object;
    }
    public int getRawSize()
    {
        if(!this.hasRaw) throw new shared.uncaughtexception("Cannot get rawsize on non-raw object.");
        int result = rawdata.length;
        return result;
    }
    public byte[] getRawMd5()
    {
        if(!this.hasRaw) throw new shared.uncaughtexception("Cannot get md5 on non-raw object.");
        byte[] result = shared.CryptHashes.GetHash(rawdata, shared.CryptHashes.Hashtype.md5);
        return result;
    }
    
}

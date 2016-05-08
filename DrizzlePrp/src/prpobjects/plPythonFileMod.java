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
import uru.Bytedeque;
import shared.e;
import shared.m;
import java.util.Vector;

//was x00A2Pythonfilemod
public class plPythonFileMod extends uruobj
{
    //Objheader xheader;
    public plMultiModifier parent;
    public Urustring pyfile;
    public int refcount;
    public Uruobjectref[] pythonrefs;
    public int listcount;
    public Vector<Pythonlisting> listings;
    
    public plPythonFileMod(context c) throws readexception//,boolean hasHeader)
    {
        shared.IBytestream data = c.in;
        //if(hasHeader) xheader = new Objheader(c);
        parent = new plMultiModifier(c);//,false);
        pyfile = new Urustring(c);
        if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod: file="+pyfile.toString()+"  name="+c.curRootObject.toString());
        refcount = data.readInt();
        pythonrefs = new Uruobjectref[refcount];
        for(int i=0;i<refcount;i++)
        {
            pythonrefs[i] = new Uruobjectref(c);
        }
        listcount = data.readInt();
        /*listings = new Pythonlisting[listcount];
        for(int i=0;i<listcount;i++)
        {
            listings[i] = new Pythonlisting(c,pyfile);
        }*/
        listings = c.readVector(Pythonlisting.class, listcount);
    }
    private plPythonFileMod(){}
    public Pythonlisting getListingByIndex(int index)
    {
        for(Pythonlisting listing: listings)
        {
            if(listing.index==index) return listing;
        }
        return null;
    }
    public void removeListingByIndex(int index)
    {
        Vector<Pythonlisting> listingsToRemove = new Vector();
        for(Pythonlisting listing: listings)
        {
            if(listing.index==index) listingsToRemove.add(listing);
        }
        for(Pythonlisting listing: listingsToRemove)
        {
            listings.remove(listing);
        }
    }
    public static plPythonFileMod createEmpty()
    {
        return new plPythonFileMod();
    }
    public static plPythonFileMod createDefault()
    {
        plPythonFileMod result = plPythonFileMod.createEmpty();
        result.parent = plMultiModifier.createDefault();
        result.pyfile = Urustring.createFromString("changeme");
        result.refcount = 0;
        result.pythonrefs = new Uruobjectref[0];
        result.listcount = 0;
        //result.listings = new Pythonlisting[0];
        result.listings = new Vector<Pythonlisting>();
        return result;
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
        pyfile.compile(deque);
        deque.writeInt(refcount);
        for(int i=0;i<refcount;i++)
        {
            pythonrefs[i].compile(deque);
        }
        //deque.writeInt(listcount);
        /*for(int i=0;i<listcount;i++)
        {
            listings[i].compile(deque);
        }*/
        deque.writeInt(listings.size());
        deque.writeVector2(listings);
    }
    public void addListing(Pythonlisting listing)
    {
        listcount++;
        listings.add(listing);
    }
    public void clearListings()
    {
        listcount = 0;
        listings.clear();
    }
    static public class Pythonlisting extends uruobj
    {
        public int index;
        public int type;
        public int xInteger;
        public int xFloat; //this is really a floating point number, but why bother to convert it?
        public int xBoolean;
        public Bstr xString;
        public Uruobjectref xRef;
        
        public Pythonlisting()
        {
        }
        
        public static Pythonlisting createWithRef(int type, int index, Uruobjectref ref)
        {
            Pythonlisting result = new Pythonlisting();
            result.type = type;
            result.index = index;
            result.xRef = ref;
            return result;
        }
        public static Pythonlisting createWithString(int type, int index, Bstr str)
        {
            Pythonlisting result = new Pythonlisting();
            result.type = type;
            result.index = index;
            result.xString = str;
            return result;
        }
        public static Pythonlisting createWithString(int index, Bstr str)
        {
            Pythonlisting result = new Pythonlisting();
            result.type = 4;
            result.index = index;
            result.xString = str;
            return result;
        }
        public static Pythonlisting createWithBoolean(int index, boolean booleanval)
        {
            Pythonlisting result = new Pythonlisting();
            result.type = 3;
            result.index = index;
            result.xBoolean = booleanval?1:0;
            return result;
        }
        public static Pythonlisting createWithInteger(int index, int val)
        {
            Pythonlisting r = new Pythonlisting();
            r.type = 1;
            r.index = index;
            r.xInteger = val;
            return r;
        }
        public Pythonlisting(context c) throws readexception
        {
            this(c,Urustring.createFromString("(this constructor doesn't show the pyfile)"));
        }
        public Pythonlisting(context c, Urustring pyfile) throws readexception
        {
            shared.IBytestream data = c.in;

            index = data.readInt();
            type = data.readInt();
            if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:   type="+Integer.toString(type)+" index="+Integer.toString(index)+" pyfile="+pyfile.toString());
            switch(type)
            {
                case 1:
                    xInteger = data.readInt();
                    if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     int="+Integer.toString(xInteger));
                    break;
                case 2:
                    xFloat = data.readInt();
                    if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     float="+Integer.toString(xFloat));
                    break;
                case 3:
                    xBoolean = data.readInt();
                    if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     bool="+Integer.toString(xBoolean));
                    break;
                case 4:
                case 13:
//                    if(type==4 && c.readversion==4)
//                    {
//                        m.warn("          Changing ptAttribDropDownList to ptAttribString, it needs to be accounted for in the .py file as well.");
//                    }
                    xString = new Bstr(c);
                    if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     string="+xString.toString());
                    break;
                case 20:
                    if(c.readversion==4)
                    {
                        type = 4; //change to ptAttribString
                        xString = new Bstr(c);
                        if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     string="+xString.toString());
                        //m.warn("PythonFileMod: usinng a case that differs between versions.");
//                        m.warn("          Changing ptAttribGlobalSDLVar to ptAttribString, it needs to be accounted for in the .py file as well.");
                        //throw new shared.readwarningexception("PythonFileMod: can read okay, but throwing error to ignore.");
                    }
                    else if(c.readversion==6||c.readversion==3)
                    {
                        e.ensure(type!=23); //type 23 shouldn't occur, according to my stats.
                        xRef = new Uruobjectref(c);
                        if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     ref="+xRef.toString());
                    }
                    else
                    {
                        m.throwUncaughtException("Unhandled.");
                    }
                    break;
                case 23:
                    if(true)
                        throw new shared.readexception("PythonFileMod: case 23 shouldn't occur anywhere.");
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 21:
                    xRef = new Uruobjectref(c);
                    if(shared.State.AllStates.getStateAsBoolean("reportPythonFileMod")) m.msg("PythonFileMod:     ref="+xRef.toString());
                    break;
                default:
                    m.msg("unknown pythonfilemod type."); //we *may* encounter 22 in moul; if so, it has no match in pots.
                    break;
            }
        }
        public void compile(Bytedeque deque)
        {
            deque.writeInt(index);
            deque.writeInt(type);
            switch(type)
            {
                case 1:
                    deque.writeInt(xInteger);
                    break;
                case 2:
                    deque.writeInt(xFloat);
                    break;
                case 3:
                    deque.writeInt(xBoolean);
                    break;
                case 4:
                case 13:
                    xString.compile(deque);
                    break;
                case 20:
                //case 23:
                    //if(xString!=null)
                    //{
                    //    xString.compile(deque);
                    //    m.warn("PythonFileMod: usinng a case that differs between versions.");
                    //}
                    //else
                    //{
                        e.ensure(xRef!=null); //if it's not a string it should be a ref.
                        xRef.compile(deque);
                    //}
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                //case 20:
                case 21:
                    xRef.compile(deque);
                    break;
                default:
                    m.msg("unknown pythonfilemod type.");
                    break;
            }
        }
    }
}

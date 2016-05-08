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
import shared.b;
import shared.m;
import shared.Bytes;
import shared.e;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

/**
 *
 * @author user
 */
//this is a class I made myself, to encapsulate changing page ids.

//In pots GlobalAvatars, the pages wrap around so that MaleFall,6 has the same Pageid as MalePelletBookLeft,262

//This is a new version, which just has the prefix and pagenum for modifyable values.

public class Pageid extends uruobj implements java.io.Serializable
{
    private static final long serialVersionUID = -4399754547712612873L; //change/remove this if we want to force a new version to not read an old version.

    public static final int kStateInvalid = 0;
    public static final int kStateNormal = 1;
    public static final int kStateVirtual = 2;

    private int type;
    private int prefix;
    private int pagenum;

    public Pageid(context c)
    {
        int shift;
        if(c.readversion==6)
        {
            shift = 16;
        }
        else if(c.readversion==3||c.readversion==4||c.readversion==7)
        {
            shift = 8;
        }
        else
        {
            throw new shared.uncaughtexception("unhandled"); //note that mqo can perhaps be handled by moul.
        }

        int rawdata = c.in.readInt();
        if(rawdata==0xFFFFFFFF)
        {
            type = kStateInvalid;
        }
        else if(rawdata==0x00000000)
        {
            type = kStateVirtual;
        }
        else
        {
            type = kStateNormal;
            if((rawdata&0x80000000)!=0)
            {
                int cleandata = rawdata - (shift==16?0xFF000001:0xFFFF0001);
                prefix = cleandata >>> shift;
                pagenum = cleandata - (prefix << shift);
                prefix = -prefix;
            }
            else
            {
                int cleandata = rawdata - 33;
                prefix = cleandata >>> shift;
                pagenum = cleandata - (prefix << shift);
            }
        }
        //sign extend:
        if(shift==16)
            pagenum = ((pagenum&0x00008000)!=0)?(pagenum|0xFFFF0000):pagenum;
        else
            pagenum = ((pagenum&0x00000080)!=0)?(pagenum|0xFFFFFF00):pagenum;

        if(c.sequencePrefix!=null)
        {
            prefix = c.sequencePrefix;
            //--We don't seem to need this now, I think.
            // I have no clue why, but this seems to be necessary
            // BultIn and Textures have a prefix which is one HIGHER than the rest of the pages for that age
            // found for both Cyan and fan-created ages, it is what both PRPTool and PlasmaExplorer show
            // so I'll just believe it
            //if (suffix <= 0x20) {
            //    ++prefix;
            //}
        }

        //change suffix
        if(c.pagenumMap!=null)
        {
            Integer newpagenum = c.pagenumMap.get(this.getPageNumber());
            if(newpagenum!=null)
            {
                //if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Suffix: Replacing Pagenum ",Integer.toString(pagenum)+" with "+Integer.toString(newpagenum));
                this.setPagenum(newpagenum);
            }
        }
    }

    public int getPageNumber(){return pagenum;}
    public void setPagenum(int val){pagenum = val;}

    private Pageid(){}
    public static Pageid createFromPrefixPagenum(int prefix, int pagenum)
    {
        if(pagenum<-2) m.err("Unhandled pagenum: investigate now!"); //could be lower.

        Pageid r = new Pageid();
        r.type = kStateNormal;
        r.prefix = prefix;
        r.pagenum = pagenum;
        return r;
    }

    public int getSequencePrefix(){return prefix;}
    public void setSequencePrefix(int val){prefix = val;}

    public void compile(Bytedeque deque)
    {
        if(type==kStateInvalid)
        {
            deque.writeInt(0xFFFFFFFF);
        }
        else if(type==kStateVirtual)
        {
            deque.writeInt(0x00000000);
        }
        else
        {
            int shift;
            if(deque.format==Format.moul)
            {
                shift = 16;
            }
            else if(deque.format==Format.pots||deque.format==Format.crowthistle||deque.format==Format.hexisle)
            {
                shift = 8;
            }
            else
            {
                throw new shared.uncaughtexception("unhandled"); //note that mqo can perhaps be handled by moul.
            }

            //we may have to modify these values if they are out of range, so use copies
            int _pagenum = pagenum;
            int _prefix = prefix;
            //if there is overflow in the shift8 case, reproduce it here:
            if(shift==8)
            {
                if(_prefix<0)
                {
                    while(_pagenum>=256)
                    {
                        _prefix -= 1;
                        _pagenum -= 256;
                    }
                }
                else
                {
                    while(_pagenum>=256)
                    {
                        _prefix += 1;
                        _pagenum -= 256;
                    }
                }
            }


            if(shift==8 && _pagenum<-10)
            {
                m.err("unsure what to do with a shift of 8 and a pagenum below -10");
                m.err("3dsMax users converting to PotS: make SURE your sequence prefix is above 100 and under 32000...");
            }
            //if(shift==8 && _pagenum>200)
            //{
            //    if(_pagenum>500) m.throwUncaughtException("handle this");
            //}

            //sign
            int smallpagenum = (shift==16)?(_pagenum&0x0000FFFF):(_pagenum&0x000000FF);
            //if(shift==16)
            //    pagenum = ((pagenum&0x00008000)!=0)?(pagenum|0xFFFF0000):pagenum;
            //else
            //    pagenum = ((pagenum&0x00000080)!=0)?(pagenum|0xFFFFFF00):pagenum;


            if(_prefix<0)
            {
                int val = smallpagenum - (_prefix<<shift) + (shift==16?0xFF000001:0xFFFF0001);
                deque.writeInt(val);
            }
            else
            {
                int val = smallpagenum + (_prefix<<shift) + 33;
                deque.writeInt(val);
            }

        }

        //int rawdata = getRawData();
        // convert it to TPOTS format
        //if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Suffix: Writing ",toString());
        //int newdata = (rawdata & 0x000000FF) | ((rawdata & 0x00FF0000) >>> 8);
        //int newprefix = prefix<0 ? (0xFFFF00|(0xFF & (-prefix))) : prefix;
        //int newdata = (suffix & 0x000000FF) | ((newprefix & 0x00FFFFFF)<<8);
        //deque.writeInt(newdata);
    }

    public String toString()
    {
        //return "Prefix=0x"+Integer.toHexString(prefix)+", Suffix=0x"+Integer.toHexString(suffix);
        return Integer.toString(prefix) + ":" + Integer.toString(pagenum);
    }
    public String toString2()
    {
        return "Prefix="+Integer.toString(prefix) + ", Pagenum="+Integer.toString(pagenum);
    }

    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Pageid)) return false;
        Pageid o2 = (Pageid)o;
        if(this.type!=o2.type) return false;
        if(this.prefix!=o2.prefix) return false;
        if(this.pagenum!=o2.pagenum) return false;
        return true;
    }
    public int hashCode()
    {
        return this.prefix + this.pagenum<<16;
    }
    public Pageid deepClone()
    {
        Pageid result = new Pageid();
        result.type = type;
        result.prefix = prefix;
        result.pagenum = pagenum;
        return result;
    }

}

/*public class Pageid extends uruobj implements java.io.Serializable
{
    private static final long serialVersionUID = -4399754547712612873L; //change/remove this if we want to force a new version to not read an old version.

    public static final int kStateInvalid = 0;
    public static final int kStateNormal = 1;
    public static final int kStateVirtual = 2;

    public int prefix;
    public int suffix;
    
    //context ctx;
    //Integer xOverridePrefix;

    //public int sequenceprefix;
    //public int pagenum;
    
    public Pageid(context c)
    {
        //int rawdata;
        if(c.readversion==6) // moul
        {
            //first 16 bits is sequence prefix, next is pageid?
            int rawdata = c.in.readInt();
            prefix = rawdata >>> 16;
            suffix = rawdata & 0x0000FFFF;
            if((prefix&0xFF00)==0xFF00)
            {
                prefix = -(prefix&0xFF);
                m.msg("Encountered negative sequence prefix.");
            }
            if((suffix&0xFF00)==0xFF00)
            {
                m.warn("Pageid: untested case.");
                suffix = -(suffix&0xFF);
            }
        }
        else if(c.readversion==3) // TPOTS 
        {
            if(c.curFile.toLowerCase().startsWith("gui"))
            {
                int dummy=0;
            }
            int fixme = c.in.readInt();
            //fixme = (fixme & 0x000000FF) | ((fixme & 0x0000FF00) << 8);
            //rawdata = fixme;
            suffix = fixme & 0x000000FF;
            if((fixme & 0xFFFF0000)==0xFFFF0000) prefix = -((fixme & 0x0000FF00)>>>8);
            else prefix = (fixme & 0xFFFFFF00)>>>8;
        }
        else if(c.readversion==4||c.readversion==7) // Crowthwistle
        {
            //int fixme = Bytes.Int16ToInt32(c.readShort());
            //fixme = (fixme & 0x000000FF) | ((fixme & 0x0000FF00) << 8);
            //rawdata = fixme;
            short rawdata = c.readShort();
            short xm5unknown = c.readShort(); e.ensure(xm5unknown==0);
            prefix = (rawdata & 0x0000FF00) >>> 8;
            suffix = (rawdata & 0x000000FF);
            
        }
        e.ensure(suffix>=0 && suffix<=512); //was suffix<=255 but Moul GlobalAnimations has ones above 255
        e.ensure(prefix>=-255 && prefix<=8388607);
        //else rawdata = 0;
        //prefix = rawdata >> 16;
        //suffix = rawdata << 16 >> 16;
         
        //ctx = c;
        //xOverridePrefix = c.sequencePrefix;
        //if(xOverridePrefix!=null) //was ctx.sequencePrefix
        if(c.sequencePrefix!=null)
        {
            //prefix = xOverridePrefix; //was ctx.sequencePrefix
            prefix = c.sequencePrefix;
            if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Suffix: Using forced sequence prefix 0x",Integer.toHexString(prefix));
            // I have no clue why, but this seems to be necessary
            // BultIn and Textures have a prefix which is one HIGHER than the rest of the pages for that age
            // found for both Cyan and fan-created ages, it is what both PRPTool and PlasmaExplorer show
            // so I'll just believe it
            if (suffix <= 0x20) {
                ++prefix;
            }
        }
        
        //change suffix
        if(c.pagenumMap!=null)
        {
            //Integer newsuffix = c.sequenceSuffixMap.get(suffix);
            Integer newpagenum = c.pagenumMap.get(this.getPageNumber());
            if(newpagenum!=null)
            {
                if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Suffix: Replacing sequence suffix ",Integer.toString(suffix)+" with "+Integer.toString(newpagenum));
                //suffix = newsuffix;
                this.setPagenum(newpagenum);
            }
            else
            {
                int dummy=0;
            }
        }
    }
    private Pageid(){}
    public static Pageid createFromPrefixSuffix(int prefix, int suffix)
    {
        Pageid result = new Pageid();
        result.prefix = prefix;
        result.suffix = suffix;
        return result;
    }
    public static Pageid createFromPrefixPagenum(int prefix, int pagenum)
    {
        if(pagenum<-2) m.err("Unhandled pagenum: investigate now!"); //could be lower.
        return createFromPrefixSuffix(prefix,pagenum+33);
        //Pageid r = new Pageid();
        //r.setSequencePrefix(prefix);
        //r.setPagenum(pagenum);
        //return r;
    }
    public void setPagenum(int pagenum)
    {
        if(prefix<0)
        {
            if(pagenum>511)
            {
                throw new shared.uncaughtexception("Unhandled pagenum in setPagenum; investigate now!");
            }
            if(pagenum>254)
            {
                prefix--; //observed in Pots GlobalAnimations.age.  MaleClap and FemalePelletBookRight only differ in prefix, because their page numbers differ by 256. 
            }
            suffix = pagenum+1;
        }
        else
        {
            if(pagenum>222 || pagenum<-2)
            {
                throw new shared.uncaughtexception("Unhandled pagenum: investigate now!"); //could be lower
            }
            suffix = pagenum+33;
        }
    }
    public int getPageNumber()
    {
        if(prefix<0)
        {
            return suffix - 1;
        }
        else
        {
            if(suffix<33)
            {
                //not really a problem; 32 happens often with Moul Textures, e.g.
                //m.warn("Unhandled pageid suffix: investigate now!");
            }
            return suffix - 33;
        }
    }
    public int getSequencePrefix()
    {
        if(suffix<33)
        {
            return prefix-1;
        }
        else
        {
            return prefix;
        }
    }
    public void setSequencePrefix(int newval)
    {
        if(suffix<33)
        {
            prefix = newval + 1;
        }
        else
        {
            prefix = newval;
        }
    }
    //public int getRawData()
    //{
    //    return prefix << 16 | suffix;
    //}
    public void compile(Bytedeque deque)
    {
        if(suffix>255 || suffix < 0)
        {
            //this can validly happen, see note at top of this page.
            m.warn("Suffix is out of range to be written.  May be a loop-around suffix.");
        }
        
        //int rawdata = getRawData();
        // convert it to TPOTS format
        if(shared.State.AllStates.getStateAsBoolean("reportSuffixes")) m.msg("Suffix: Writing ",toString());
        //int newdata = (rawdata & 0x000000FF) | ((rawdata & 0x00FF0000) >>> 8);
        int newprefix = prefix<0 ? (0xFFFF00|(0xFF & (-prefix))) : prefix;
        int newdata = (suffix & 0x000000FF) | ((newprefix & 0x00FFFFFF)<<8);
        deque.writeInt(newdata);
    }
    
    public String toString()
    {
        //return "Prefix=0x"+Integer.toHexString(prefix)+", Suffix=0x"+Integer.toHexString(suffix);
        return Integer.toString(prefix) + ":" + Integer.toString(suffix);
    }
    public String toString2()
    {
        return "Prefix="+Integer.toString(prefix) + ", Suffix=" + Integer.toString(suffix)+", Pagenum="+Integer.toString(this.getPageNumber());
    }

    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Pageid)) return false;
        Pageid o2 = (Pageid)o;
        if(this.prefix!=o2.prefix) return false;
        if(this.suffix!=o2.suffix) return false;
        return true;
    }
    public void addXml(StringBuilder s)
    {
        s.append("<prefix>"+Integer.toString(prefix)+"</prefix>");
        s.append("<suffix>"+Integer.toString(suffix)+"</suffix>");
    }
    public static Pageid createFromXml(Element e1)
    {
        Pageid result = new Pageid();
        for(Node child=e1.getFirstChild();child!=null;child=child.getNextSibling())
        {
            if(child.getNodeType()==Node.ELEMENT_NODE)
            {
                Element e2 = (Element)child;
                String tag = e2.getTagName();
                if(tag.equals("prefix")) result.prefix = Integer.parseInt(e2.getTextContent());
                else if(tag.equals("suffix")) result.suffix = Integer.parseInt(e2.getTextContent());
            }
        }
        return result;
    }
    public int hashCode()
    {
        return this.prefix + this.suffix;
    }
    public Pageid deepClone()
    {
        Pageid result = new Pageid();
        result.prefix = prefix;
        result.suffix = suffix;
        return result;
    }

}*/

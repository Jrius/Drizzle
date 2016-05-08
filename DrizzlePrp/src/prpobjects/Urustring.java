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
import shared.m;
import shared.e;
import shared.b;
import uru.Bytedeque;
import shared.IBytestream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.*;

/**
 *
 * @author user
 */
public class Urustring extends uruobj implements java.io.Serializable
{
    byte[] unencryptedString; //unencrypted form of this string.

    public static Urustring createFromString(String s)
    {
        Urustring result = new Urustring();
        result.unencryptedString = b.StringToBytes(s);
        return result;
    }
    
    private Urustring(){}
    
    public void shallowCopyFrom(Urustring s2)
    {
        this.unencryptedString = s2.unencryptedString;
    }
    public Urustring(IBytestream c, int readversion)
    {
        //Urustring res = new Urustring();
        if(readversion==6 || readversion==3 || readversion==8)
        {
            //Bytestream data = c.in;
            short lengthbytes = c.readShort();
            if ((lengthbytes & 0xF000)==0) c.readShort();//data.skipBytes(2); //skip 2 bytes, if first half-byte is set. These bytes are apparently irrelevant, anyway.
            int actuallength = lengthbytes & 0xFFF;
            if((lengthbytes & 0xF000)!=0xF000)
            {
                m.msg("urustring doesn't start with F.");
            }
            if (actuallength > 255)
            {
                m.msg("urustring over 255 bytes.");
            }
            unencryptedString = new byte[actuallength];
            //if((actuallength > 0) && (urustring[startpos]>0x7F))
            //if((actuallength > 0) && ((c.data.peekByte() & 0x80) != 0))
            //{
            if(actuallength > 0) //make sure we've got at least 1 byte.
            {
                byte b0 = c.readByte();
                if((b0 & 0x80)!=0)
                {
                    //encrypted...
                    unencryptedString[0] = (byte)~b0;
                    for (int i = 1; i<actuallength; i++)
                    {
                        unencryptedString[i] = (byte)~c.readByte();
                    }
                }
                else
                {
                    //unencrypted...
                    //if(actuallength==0) m.msg("string is empty."); //not a problem.
                    if(actuallength!=0) m.msg("urustring is not encrypted.");
                    unencryptedString[0] = b0;
                    for (int i = 1; i<actuallength; i++)
                    {
                        unencryptedString[i] = c.readByte();
                    }
                }
            }
        }
        else if(readversion==4||readversion==7)
        {
            byte[] key = { 109, 121, 115, 116, 110, 101, 114, 100 }; //ascii for "mystnerd"
            short len = c.readShort();
            byte[] result = new byte[len];
            for(int i=0;i<len;i++)
            {
                result[i] = (byte)(c.readByte() ^ key[i%8]);
            }
            unencryptedString = result;
        }
        e.ensureString(unencryptedString); //make sure its a text string.
        
        //return res;
    }
    public Urustring(context c)
    {
        this(c.in,c.readversion);
    }
    public void compile(Bytedeque deque)
    {
        if(deque.format==Format.pots || deque.format==Format.moul)
        {
            int actuallength = unencryptedString.length;
            //byte[] result = new byte[actuallength+2];

            //write header
            short startint = (short)(0xF000 | actuallength);
            byte[] startbytes = b.Int16ToBytes(startint);
            //b.CopyBytes(startbytes,result,0);
            deque.writeBytes(startbytes);

            //encode bytes
            for(int i=0;i<actuallength;i++)
            {
                //result[i+2] = (byte)~string[i];
                deque.writeByte((byte)~unencryptedString[i]);
            }
        }
        else
        {
            m.throwUncaughtException("unimplemented");
        }
        //return result;
    }
    
    public String toString()
    {
        return new String(unencryptedString);
    }
    
    public Urustring(byte[] unencrypted)
    {
        unencryptedString = unencrypted;
    }
    public void addXml(StringBuilder s)
    {
        s.append(shared.xml.sanitise(this.toString()));
    }
    public static Urustring createFromXml(Element e1)
    {
        return Urustring.createFromString(e1.getTextContent());
    }
    public boolean equals(Object o)
    {
        if(o==null) return false;
        if(!(o instanceof Urustring)) return false;
        return o.toString().equals(this.toString());
    }
    
    public int hashCode()
    {
        return this.toString().hashCode();
    }
    
    public Urustring deepClone()
    {
        Urustring result = new Urustring();
        result.unencryptedString = b.CopyBytes(unencryptedString);
        return result;
    }
}


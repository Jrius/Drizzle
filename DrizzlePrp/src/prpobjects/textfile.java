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

import shared.Bytes;
import shared.m;
import uru.Bytedeque;
import java.util.Vector;

public class textfile
{
    //Bytes text;
    protected Bytes[] lines;

    public textfile()
    {
        lines = new Bytes[0];
    }
    public void removeVariables(String varname)
    {
        Vector<Bytes> result = new Vector();
        Bytes start = new Bytes(varname+"=");
        for(int i=0;i<lines.length;i++)
        {
            if(!lines[i].startsWith(start))
            {
                result.add(lines[i]);
            }
        }
        lines = shared.generic.convertVectorToArray(result, Bytes.class);
    }
    public static class textline
    {
        Bytes line;
        
        public textline(Bytes[] lines, int index)
        {
            line = lines[index];
        }
        public String getString()
        {
            return line.toString();
        }
        
        public void setString(String newvalue)
        {
            line.setWithString(newvalue);
        }
        
    }
    /*public static textfile loadFromFile(String filename, boolean isencrypted)
    {
        textfile result = new textfile();
        byte[] filecontents = shared.FileUtils.ReadFile(filename);
        Bytes text;
        if(isencrypted)
        {
            text = Bytes.create(uru.UruCrypt.DecryptWhatdoyousee(filecontents));
        }
        else
        {
            text = Bytes.create(filecontents);
        }
        //text.split((byte)0x0d);
        text = text.remove((byte)0x0a);
        result.lines = text.split((byte)0x0d);
        
        return result;
    }*/
    public textline[] getLines()
    {
        textline[] result = new textline[lines.length];
        for(int i=0;i<lines.length;i++)
        {
            result[i] = new textline(lines,i);
        }
        return result;
    }
    public static textfile createFromBytes(byte[] bytes)
    {
        return createFromBytes(new Bytes(bytes));
    }
    public static textfile createFromBytes(Bytes bytes)
    {
        textfile result = new textfile();
        //byte[] filecontents = shared.FileUtils.ReadFile(filename);
        Bytes text = bytes;
        //if(isencrypted)
        //{
        //    text = Bytes.create(uru.UruCrypt.DecryptWhatdoyousee(filecontents));
        //}
        //else
        //{
        //    text = Bytes.create(filecontents);
        //}
        //text.split((byte)0x0d);
        text = text.remove((byte)0x0a);
        result.lines = text.split((byte)0x0d);
        
        return result;
    }
    public void dump()
    {
        for(int i=0;i<lines.length;i++)
        {
            m.msg(lines[i].toString());
        }
    }
    /*public void saveToFile(String filename, boolean encrypt)
    {
        Bytedeque result = new Bytedeque();
        for(int i=0;i<lines.length;i++)
        {
            result.writeBytes(lines[i]);
            result.writeByte((byte)0x0d);
            result.writeByte((byte)0x0a);
        }
        
        byte[] result2 = result.getAllBytes();
        if(encrypt)
        {
            result2 = uru.UruCrypt.EncryptWhatdoyousee(result2);
        }
        
        shared.FileUtils.WriteFile(filename, result2);
    }*/
    
    public void appendLine(String line)
    {
        Bytes newline = Bytes.create(line);
        Bytes[] newlines = new Bytes[lines.length+1];
        for(int i=0;i<lines.length;i++)
        {
            newlines[i] = lines[i];
        }
        newlines[lines.length] = newline;
        lines = newlines;
    }
    
    public Bytes saveToBytes()
    {
        Bytedeque result = new Bytedeque(shared.Format.none);
        for(Bytes line: lines)
        {
            result.writeBytes(line);
            result.writeByte((byte)0x0d);
            result.writeByte((byte)0x0a);
        }
        
        return result.getBytes();
    }
    public byte[] saveToByteArray()
    {
        Bytedeque result = new Bytedeque(shared.Format.none);
        for(Bytes line: lines)
        {
            result.writeBytes(line);
            result.writeByte((byte)0x0d);
            result.writeByte((byte)0x0a);
        }
        
        return result.getAllBytes();
    }
    public boolean hasVariable(String varname)
    {
        for(int i=0;i<lines.length;i++)
        {
            if(lines[i].startsWith(new Bytes(varname+"=")))
            {
                return true;
            }
        }
        return false;
    }
    public String getVariable(String varname)
    {
        for(int i=0;i<lines.length;i++)
        {
            if(lines[i].startsWith(new Bytes(varname+"=")))
            {
                int pos = varname.length()+1;
                String result = lines[i].substr(pos).toString();
                return result;
            }
        }
        return null;
    }
    public void setVariable(String varname, String value)
    {
        Bytes start = new Bytes(varname+"=");
        for(int i=0;i<lines.length;i++)
        {
            if(lines[i].startsWith(start))
            {
                lines[i] = start.append(new Bytes(value));
                return;
            }
        }
        m.err("textfile: setVariable: Variable not found.");
    }
    
}

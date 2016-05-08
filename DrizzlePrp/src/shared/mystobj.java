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

package shared;

import uru.*;
import prpobjects.*;
import uru.Bytedeque;
import uru.Bytestream;
import shared.m;

/**
 *
 * @author user
 */
abstract public class mystobj //implements compilable
{
    public mystobj()
    {
    }
    //public uruobj(Bytestream data)
    //{
        
    //}
    
    public static mystobj create(Bytestream data)
    {
        m.err("Mystobj doesn't implement create.");
        return null;
    }
    
    public void compile(Bytedeque data)
    {
        //m.err("Mystobj doesn't implement compile. ",this.toString());
        write(data); //fallback to write, if available.
    }

    public void write(IBytedeque c)
    {
        m.throwUncaughtException("Mystobj doesn't implement write: ",this.toString());
    }
    
    /*void compile(Bytedeque data)
    {
        m.err("Uruobj doesn't implement compile.");
    }*/
    
    
    //int getNumBytesProcessed();

    public <T> T serialClone(Class<T> klass, Format format)
    {
        //Bytedeque c = new Bytedeque();
        //this.compile(c);
        byte[] serialdata = compileAlone(format);
        IBytestream c2 = shared.ByteArrayBytestream.createFromByteArray(serialdata);
        T result = c2.readObj(klass);
        return result;
    }

    /*public byte[] compileAlone()
    {
        Bytedeque c = new Bytedeque();
        this.compile(c);
        byte[] r = c.getAllBytes();
        return r;
    }*/

    public byte[] compileAlone(Format format)
    {
        Bytedeque c = new Bytedeque(format);
        this.compile(c);
        byte[] r = c.getAllBytes();
        return r;
    }
    
}

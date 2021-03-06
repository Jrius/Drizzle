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

package realmyst;

import shared.*;

public class Vdb
{
    //int tag;
    Typeid tag; //gets read again in the Sdb and Mdb classes.
    Sdb xsdb;
    Mdb xmdb;
    
    public Vdb(IBytestream c)
    {
        IBytestream lookahead = c.Fork();
        tag = Typeid.read(lookahead);
        
        if(tag==Typeid.sdbstart)
        {
            xsdb = new Sdb(c);
        }
        else if(tag==Typeid.mdb)
        {
            xmdb = new Mdb(c);
        }
        else
        {
            throw new uncaughtexception("Vdb file appears to be neither Sdb nor Mdb.");
        }
    }
}

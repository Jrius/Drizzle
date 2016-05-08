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


package riven;

import shared.MystInStream;
import shared.readexception;
import shared.Bytes;
import shared.mystobj;
//import shared.NullTerminatedString;
import shared.Ntstring;
import shared.m;

public class mhk
{
    Bytes header;
    int remainingByteCount;
    Bytes rsrc;
    int u2;
    int filesize;
    int offsetToTypeinfo;
    short sizeOfFileTable;
    short offsetToFileTable;
    short offsetToNames;
    short countOfTypeinfos;
    mhktype[] types;
    int countOfFileTableEntries;
    FileTableEntry[] FileTable;
    
    public mhk(String filename) throws readexception
    {
        MystInStream c = MystInStream.createFromFile(filename);
        
        header = c.readbytes(4);
        if(!header.isequal(Bytes.create(new byte[]{'M','H','W','K'})))
        {
            throw new readexception("MHK header not found.");
        }
        remainingByteCount = c.readintBigendian(); //bytes after this
        rsrc = c.readbytes(4);
        if(!rsrc.isequal(Bytes.create(new byte[]{'R','S','R','C'})))
        {
            throw new readexception("RSRC header not found.");
        }
        u2 = c.readintBigendian(); //flags?
        filesize = c.readintBigendian(); //filesize, =u1+8
        offsetToTypeinfo = c.readintBigendian(); //offset to type list. should be 28.
        offsetToFileTable = c.readshortBigendian(); //offset to something on top
        sizeOfFileTable = c.readshortBigendian(); //size of file table in bytes
        //u3 == u1+8
        
        //type list
        MystInStream c2 = c.fork(offsetToTypeinfo,false);
        offsetToNames = c2.readshortBigendian(); //offset to names
        countOfTypeinfos = c2.readshortBigendian();
        int u8b = Bytes.Int16ToInt32(countOfTypeinfos);
        types = new mhktype[countOfTypeinfos];
        for(int i=0;i<u8b;i++)
        {
            types[i] = new mhktype(c2);
        }
        //types = c2.readVector(mhktype.class, Bytes.Int16ToInt32(u8));
        
        MystInStream c3 = c.fork(offsetToTypeinfo+offsetToFileTable,false);
        countOfFileTableEntries = c3.readintBigendian();
        FileTable = new FileTableEntry[countOfFileTableEntries];
        for(int i=0;i<countOfFileTableEntries;i++)
        {
            FileTable[i] = new FileTableEntry(c3);
        }
        //u10 = c3.readVector(unknownclass1.class, u9);
        
        int pos = 7;
        int size = FileTable[pos].getFilelength();
        int foffset = FileTable[pos].filedataOffset;
        MystInStream c4 = c.fork(foffset, false);
        Bytes test = c4.readbytes(size);
        shared.FileUtils.WriteFile("/shared/test.dat", test.getByteArray());
            
        
    }
    public static class FileTableEntry extends mystobj
    {
        int filedataOffset;
        short fileLength1;
        byte fileLength2;
        byte u4;
        short u5;
        
        public FileTableEntry(MystInStream c) throws readexception
        {
            filedataOffset = c.readintBigendian(); //offset
            fileLength1 = c.readshortBigendian(); //length in bytes
            fileLength2 = c.readbyte();
            //int filelength = (Bytes.ByteToInt32(fileLength2) << 16)|(Bytes.Int16ToInt32(fileLength1));
            u4 = c.readbyte(); //resource flags
            u5 = c.readshortBigendian();
        }
        public int getFilelength()
        {
            return (Bytes.ByteToInt32(fileLength2) << 16)|(Bytes.Int16ToInt32(fileLength1));
        }
    }
    public class mhktype extends mystobj
    {
        Bytes name; //4 bytes long.
        short u1; //offset from start of typelist.
        short u2; //offset from start of typelist.
        
        mhktype1 u3;
        mhktype2 u4;
        
        public mhktype(MystInStream c) throws readexception
        {
            name = c.readbytes(4);
            u1 = c.readshortBigendian();
            u2 = c.readshortBigendian();
            
            u3 = new mhktype1(c.fork(u1+offsetToTypeinfo,false));
            u4 = new mhktype2(c.fork(u2+offsetToTypeinfo,false));
        }
        
        public class mhktype1
        {
            short count;
            mhktype1a[] members;
            
            public mhktype1(MystInStream c) throws readexception
            {
                count = c.readshortBigendian();
                int count2 = Bytes.Int16ToInt32(count);
                members = new mhktype1a[count2];
                for(int i=0;i<count2;i++)
                {
                    members[i] = new mhktype1a(c);
                }
                //members = c.readVector(mhktype1a.class, count);
            }
            
            public class mhktype1a extends mystobj
            {
                short u1; //local resource id number
                short u2; //index
                
                public mhktype1a(MystInStream c) throws readexception
                {
                    u1 = c.readshortBigendian();
                    u2 = c.readshortBigendian();
                }
            }
        }
        public class mhktype2
        {
            short count;
            mhktype2a[] members;
            
            public mhktype2(MystInStream c) throws readexception
            {
                count = c.readshortBigendian();
                //members = c.readVector(mhktype2a.class, count);
                int count2 = Bytes.Int16ToInt32(count);
                members = new mhktype2a[count2];
                for(int i=0;i<count2;i++)
                {
                    members[i] = new mhktype2a(c);
                }
            }
            
            public class mhktype2a extends mystobj
            {
                short u1;
                short u2;
                //NullTerminatedString u3;
                Ntstring u3;
                
                public mhktype2a(MystInStream c) throws readexception
                {
                    u1 = c.readshortBigendian();
                    u2 = c.readshortBigendian();

                    //u3 = new NullTerminatedString(c.fork(offsetToNames+offsetToTypeinfo+u1,false));
                    //u3 = new Ntstring(c.fork(offsetToNames+offsetToTypeinfo+u1,false));
                    m.err("Fixthis: needs to use Ntstring.");
                }
                
            }
        }
    }
}

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

import shared.Ntstring;
import shared.e;
import shared.b;
import java.util.Vector;
import shared.*;
import java.io.File;

//pera.dni seems to contain only .wav files.

public class dirtfile
{
    Header header;
    //DirectoryTable dirtable;
    
    dirttree tree;
    
    public dirtfile(IBytestream c)
    {
        header = new Header(c);
        //dirtable = new DirectoryTable(c.Fork(header.dirOffset));
        tree = new dirttree(c.Fork(header.dirOffset), header.FTOffset, true);
    }
    public void saveAllFiles(String outputdir)
    {
        for(dirttree child: tree.xchildren)
        {
            saveAllFiles(child,outputdir);
        }
    }
    private void saveAllFiles(dirttree node, String outputdir)
    {
        String filename = outputdir+"/"+node.name.toString();
        if(node.isdir)
        {
            FileUtils.CreateFolder(filename);
            for(dirttree child: node.xchildren)
            {
                saveAllFiles(child,filename);
            }
        }
        else
        {
            FileUtils.WriteFile(filename, node.xfile.rawdata);
        }
    }
    public static class dirttree
    {
        Ntstring name;
        ObjFile xfile;
        DirectoryTable xdirtable;
        FileStruct xfilestruct;
        
        boolean isdir;
        dirttree[] xchildren;
        
        public dirttree(IBytestream c, int FTOffset, boolean isdirectory)
        {
            isdir = isdirectory;
            if(isdir)
            {
                xdirtable = new DirectoryTable(c);
                name = new Ntstring(c.Fork(xdirtable.nameOffset));
                int childcount = xdirtable.numFiles;
                xchildren = new dirttree[childcount];
                for(int i=0;i<childcount;i++)
                {
                    int childoffset = xdirtable.fileOffsets[i];
                    boolean isChildDir = ( childoffset < FTOffset );
                    xchildren[i] = new dirttree(c.Fork(childoffset), FTOffset, isChildDir);
                }
            }
            else
            {
                xfilestruct = new FileStruct(c);
                name = new Ntstring(c.Fork(xfilestruct.nameOffset));
                //shared.FileUtils.AppendText("c:/log.txt", name.toString()+"\n");
                xfile = new ObjFile(c.Fork(xfilestruct.offset), xfilestruct.fileLength, xfilestruct.isEmpty);
            }
        }
        
        public String toString()
        {
            return name.toString();
        }
    }
    
    public static class Header
    {
        byte[] header;
        int version;
        int dirOffset;
        int FTOffset;
        int NLOffset;
        int dataOffset;
        int FTOffset2; //equals FTOffset
        
        public Header(IBytestream c)
        {
            header = c.readBytes(4); e.ensure(b.isEqual(header, new byte[]{'D','i','r','t'}));
            version = c.readInt(); e.ensure(version==0x10000);
            dirOffset = c.readInt();
            FTOffset = c.readInt();
            NLOffset = c.readInt();
            dataOffset = c.readInt();
            FTOffset2 = c.readInt(); e.ensure(FTOffset2==FTOffset);
        }
                
    }
    
    public static class DirectoryTable
    {
        int nameOffset;
        int numFiles;
        int[] fileOffsets;
        
        public DirectoryTable(IBytestream c)
        {
            nameOffset = c.readInt();
            numFiles = c.readInt();
            fileOffsets = c.readInts(numFiles);
        }
    }
    
    public static class FileStruct
    {
        int nameOffset;
        int extOffset; //the offset to this structure, this var is redundent.
        int fileLength;
        int offset;
        int isEmpty; //is this what it is?
        
        static int sum = 0;
        static int all = 0;
        static int diff = 0;
        static int test = 0;
        static int type1 = 0;
        static int type2 = 0;
        
        public FileStruct(IBytestream c)
        {
            nameOffset = c.readInt();
            extOffset = c.readInt();
            fileLength = c.readInt();
            offset = c.readInt();
            isEmpty = c.readInt();
            all += fileLength;
            diff += fileLength-isEmpty;
            test += isEmpty==0?fileLength:isEmpty;
            if(isEmpty!=0)
            {
                int dummy=0;
                sum += isEmpty;
                type2++;
                //m.msg("fileLength="+Integer.toString(fileLength)+" isEmpty="+Integer.toString(isEmpty)+" sum="+Integer.toString(sum)+" all="+Integer.toString(all)+" diff="+Integer.toString(diff));
                //m.msg("offsetstart="+Integer.toString(offset)+" offsetend"+Integer.toString(offset+fileLength));
                //m.msg("test="+Integer.toString(test));
            }
            else
            {
                type1++;
            }
            m.msg("type1="+Integer.toString(type1)+" type2="+Integer.toString(type2));
            //m.msg(Integer.toString(extOffset));
        }
    }
}

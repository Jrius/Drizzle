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
import shared.FileUtils;
import shared.m;
import shared.b;
import java.io.File;
import shared.IBytestream;
import java.util.ArrayList;
import java.util.List;
import shared.Format;
import uru.Bytedeque;

/**
 *
 * @author user
 */
//untested and incomplete.
public class pakfile extends uruobj
{
    //public int objectcount;
    //public IndexEntry[] indices;
    //public PythonObject[] objects;
    private int objectcount;
    public ArrayList<IndexEntry> indices = new ArrayList();
    public ArrayList<PythonObject> objects = new ArrayList();

    int pythonversion;

    public void packPakFile(String inputFolder)
    {
        File inputDir = new File(inputFolder);
        File[] files = inputDir.listFiles();
        for(int i=0;i<files.length;i++)
        {
            File curfile = files[i];
            String curfilename = curfile.getName();
            if(curfilename.endsWith(".pyc"))
            {
                String internalName = curfilename.substring(0,curfilename.length()-5);
                
            }
        }
    }
    public PythonObject findByFilename(String filename)
    {
        for(int i=0;i<indices.size();i++)
        {
            if(indices.get(i).objectname.toString().equals(filename))
            {
                return objects.get(i);
            }
        }
        return null;
    }
    public List<pythondec.pycfile> extractPakFile(boolean prependPYCHeader)
    {
        List<pythondec.pycfile> r = new ArrayList();

        for(int i=0;i<indices.size();i++)
        {
            //int size = objects.get(i).objectsize;
            byte[] rawdata = objects.get(i).rawCompiledPythonObjectData;
            String name = indices.get(i).objectname.toString();
            if(prependPYCHeader)
            {
                //The first 4 bytes are the magic number for that version of python.  The next 4 are a timestamp that doesn't matter, so I just set it to 0.
                byte[] header = null;
                if(pythonversion==22)
                {
                    header = new byte[]{(byte)0x2D,(byte)0xED,(byte)0x0D,(byte)0x0A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                }
                else if(pythonversion==23)
                {
                    header = new byte[]{(byte)0x3B,(byte)0xF2,(byte)0x0D,(byte)0x0A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                }
                else
                {
                    m.err("unhandled python version in extractPakFile");
                }
                rawdata = b.appendBytes(header,rawdata);
            }

            shared.IBytestream c = shared.ByteArrayBytestream.createFromByteArray(rawdata);
            pythondec.pycfile pyc = new pythondec.pycfile(c);
            pyc.filename = name;

            r.add(pyc);
        }
        
        return r;
    }
    public void extractPakFile(boolean prependPYCHeader, String outfolder)
    {
        for(int i=0;i<indices.size();i++)
        {
            //int size = objects.get(i).objectsize;
            byte[] rawdata = objects.get(i).rawCompiledPythonObjectData;
            String name = indices.get(i).objectname.toString();
            if(prependPYCHeader)
            {
                //The first 4 bytes are the magic number for that version of python.  The next 4 are a timestamp that doesn't matter, so I just set it to 0.
                byte[] header = null;
                if(pythonversion==22)
                {
                    header = new byte[]{(byte)0x2D,(byte)0xED,(byte)0x0D,(byte)0x0A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                }
                else if(pythonversion==23)
                {
                    header = new byte[]{(byte)0x3B,(byte)0xF2,(byte)0x0D,(byte)0x0A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
                }
                else
                {
                    m.err("unhandled python version in extractPakFile");
                }
                rawdata = b.appendBytes(header,rawdata);
            }
            
            //String filename = _staticsettings.outputdir+name+".pyc";
            String filename = outfolder+"/"+name;
            if(filename.endsWith(".py")) filename += "c";
            else filename+=".pyc";
            FileUtils.WriteFile(filename, rawdata,true,true);
        }
    }

    public static pakfile create(String filename, auto.AllGames.GameConversionSub g)
    {
        pakfile r = new pakfile(filename,g.g,true);
        return r;
    }
    public pakfile(String f, auto.AllGames.GameInfo g, boolean readPythonObjects)
    {
        this(new File(f),g,readPythonObjects);
    }
    public pakfile(File f, auto.AllGames.GameInfo g, boolean readPythonObjects)
    {
        //byte[] data = shared.FileUtils.ReadFile(f);
        //data = uru.UruCrypt.DecryptWhatdoyousee(data);
        this.pythonversion = g.PythonVersion;
        byte[] data = uru.UruCrypt.DecryptAny(f.getAbsolutePath(), g);
        IBytestream c = shared.ByteArrayBytestream.createFromByteArray(data);

        objectcount = c.readInt();
        //indices = new IndexEntry[objectcount];
        for(int i=0;i<objectcount;i++)
        {
            //indices[i] = new IndexEntry(c, g.game.readversion);
            indices.add(new IndexEntry(c, g.game.readversion));
        }
        if(readPythonObjects)
        {
            //objects = new PythonObject[objectcount];
            for(int i=0;i<objectcount;i++)
            {
                int offset = indices.get(i).offset;
                IBytestream c2 = c.Fork(offset);
                //objects[i] = new PythonObject(c2, g.game.readversion);
                objects.add(new PythonObject(c2, g.game.readversion));
            }
        }
    }
    public byte[] compileEncrypted(Format format)
    {
        byte[] unencbs = this.compileAlone(format);
        return uru.UruCrypt.EncryptAny(unencbs,format);
    }
    public void compile(Bytedeque c)
    {
        if(indices.size()!=objects.size()) m.throwUncaughtException("unexpected");
        //Bytedeque c = new Bytedeque(format);

        //this isn't very efficient!

        //test run to set offsets
        int offset = 0;
        //c.writeInt(indices.size());
        offset += 4;
        for(int i=0;i<indices.size();i++)
        {
            pakfile.IndexEntry ind = indices.get(i);
            byte[] bs = ind.compileAlone(c.format);
            offset += bs.length;
        }
        for(int i=0;i<objects.size();i++)
        {
            indices.get(i).offset = offset;
            pakfile.PythonObject obj = objects.get(i);
            byte[] bs = obj.compileAlone(c.format);
            offset += bs.length;
        }

        //real run
        c.writeInt(indices.size());
        for(int i=0;i<indices.size();i++)
        {
            indices.get(i).compile(c);
        }
        for(int i=0;i<objects.size();i++)
        {
            objects.get(i).compile(c);
        }
    }
    public void remove(int idx)
    {
        indices.remove(idx);
        objects.remove(idx);
    }
    
    public static class IndexEntry extends uruobj
    {
        public Urustring objectname;
        public int offset;
        
        public IndexEntry(IBytestream c, int readversion)
        {
            objectname = new Urustring(c, readversion);
            offset = c.readInt();
        }
        public void compile(Bytedeque c)
        {
            objectname.compile(c);
            c.writeInt(offset);
        }
        
        //public static IndexEntry
    }
    
    public static class PythonObject extends uruobj
    {
        int objectsize;
        public byte[] rawCompiledPythonObjectData;
        
        public PythonObject(IBytestream c, int readversion)
        {
            objectsize = c.readInt();
            //account for bug in some Python creation utils: (Cyan paks don't have this bug.)
            if(objectsize+c.getAbsoluteOffset()>c.getFilelength()) objectsize-=4;
            rawCompiledPythonObjectData = c.readBytes(objectsize);
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(rawCompiledPythonObjectData.length); //this will repair the bug too.
            c.writeBytes(rawCompiledPythonObjectData);
        }
        
        private PythonObject(){}
        
        public static PythonObject create(byte[] rawCompiledHeaderlessPythonObjectData)
        {
            PythonObject result = new PythonObject();
            int length = rawCompiledHeaderlessPythonObjectData.length;
            result.objectsize = length;
            result.rawCompiledPythonObjectData = rawCompiledHeaderlessPythonObjectData;
            return result;
        }
    }
}

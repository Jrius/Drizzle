/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import shared.*;
import uru.server.MoulFileInfo;
import java.util.ArrayList;
import uru.Bytedeque;

//This is a custom class representing the .mfs_moul manifest files created by the Drizzle dataserver generator, and does not represent any Uru classes.
public class Manifest extends mystobj
{
    private boolean israw;
    private byte[] rawdata;

    public int count; //total number when all manifest parts are put together.
    private ArrayList<MoulFileInfo> files = new ArrayList<MoulFileInfo>();
    short uk; //always 0?  Might just be a null terminator for the entire thing.

    /*public Manifest(String filename, boolean readraw)
    {
        israw = readraw;

        if(readraw)
        {
            rawdata = FileUtils.ReadFile(filename);
        }
        else
        {
            IBytestream c = shared.SerialBytestream.createFromFilename(filename);
            while(c.getBytesRemaining()!=0)
            {
                MoulFileInfo mfi = new MoulFileInfo(c);
                files.add(mfi);
            }

        }
    }*/

    public ArrayList<MoulFileInfo> getFiles()
    {
        parse();
        return files;
    }

    /*public void setFiles(ArrayList<MoulFileInfo> newfiles)
    {
        israw = false;
        files = newfiles;
    }*/

    /*public void removeEntry(MoulFileInfo mfi)
    {
        boolean hadit = files.remove(mfi);
        if(!hadit) m.throwUncaughtException("Mfi entry not found.");
        this.
    }*/

    private void parse()
    {
        if(israw)
        {
            israw = false;
            IBytestream c = shared.ByteArrayBytestream.createFromByteArray(rawdata);
            for(int i=0;i<count;i++)
            {
                files.add(new MoulFileInfo(c));
            }
            uk = c.readShort(); e.ensure(uk==0);
        }
    }

    public Manifest(IBytestream c, boolean readraw)
    {
        count = c.readInt();
        int charcount = c.readInt();
        if(readraw)
        {
            israw = true;
            rawdata = c.readBytes(charcount*2);
        }
        else
        {
            israw = false;
            //rawdata = c.readBytes(charcount*2);
            //IBytestream c2 = shared.ByteArrayBytestream.createFromByteArray(rawdata);
            IBytestream c2 = c;

            boolean readall = true; //will likely be set to false.
            for(int i=0;i<count;i++)
            {
                MoulFileInfo mfi = new MoulFileInfo(c2);
                if(mfi.filename.toString().equals(""))
                {
                    readall = false;
                    break;
                }
                files.add(mfi);
            }
            if(readall)
            {
                uk = c.readShort(); //only on the final manifest part.
            }
            //uk = c.readShort(); e.ensure(uk==0); //this is now read as the breaker MoulFileInfo
        }
    }

    public Manifest(){}

    public void write(IBytedeque c)
    {

        if(israw)
        {
            c.writeInt(count);
            c.writeInt(rawdata.length/2);
            c.writeBytes(rawdata);
        }
        else
        {
            c.writeInt(files.size());

            //find the size of the rest, so that we can find the length.
            Bytedeque c2 = new Bytedeque(shared.Format.moul);
            for(int i=0;i<files.size();i++)
            {
                files.get(i).write(c2);
            }
            c2.writeShort(uk);
            byte[] data = c2.getAllBytes();

            c.writeInt(data.length/2);
            c.writeBytes(data);
        }
    }

}

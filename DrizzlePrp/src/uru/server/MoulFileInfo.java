/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import shared.*;

public class MoulFileInfo
{
    public Str filename; //was Utf16 //name to save as, e.g. "dat\whatever.fni"
    public Str Downloadname; //special name to be sent to and understood by the file server. e.g. "Data\dat\whatever.fni"
    public Str Hash;
    public Str CompressedHash;
    public int Filesize; //two shorts, reversed order.  the other two ints are like this too.
    private short u1; //null to terminate xFilesize
    public int Compressedsize;
    private short u2; //null to terminate xCompressedsize
    public int Flags;
    private short u3; //null to terminate xFlags

    //boolean hasfields = true;

    public MoulFileInfo(IBytestream c)
    {
        filename = Str.readAsUtf16NT(c);
        if(filename.toString().equals("")) return; //not actually a MoulFileInfo item :P
        Downloadname = Str.readAsUtf16NT(c);
        Hash = Str.readAsUtf16NT(c);
        CompressedHash = Str.readAsUtf16NT(c);
        Filesize = c.readIntAsTwoShorts();
        u1 = c.readShort(); //0
        Compressedsize = c.readIntAsTwoShorts();
        u2 = c.readShort(); //0
        Flags = c.readIntAsTwoShorts();
        u3 = c.readShort(); //0
    }
    public MoulFileInfo(){}

    public void write(IBytedeque c)
    {
        filename.writeAsUtf16NT(c);
        if(filename.toString().equals("")) return; //not actually a MoulFileInfo item :P
        Downloadname.writeAsUtf16NT(c);
        Hash.writeAsUtf16NT(c);
        CompressedHash.writeAsUtf16NT(c);
        c.writeIntAsTwoShorts(Filesize);
        c.writeShort(u1);
        c.writeIntAsTwoShorts(Compressedsize);
        c.writeShort(u2);
        c.writeIntAsTwoShorts(Flags);
        c.writeShort(u3);
    }

    public String toString()
    {
        return "filename="+filename.toString()+" downname="+Downloadname.toString()
            +" hash="+Hash.toString()+" comphash="+CompressedHash.toString()
            +" filesize="+Integer.toString(Filesize)+" compsize="+Integer.toString(Compressedsize)
            +" flags="+Integer.toString(Flags);
        //return "filename="+filename.toString();
    }

    /*public void CleanNames()
    {
        String str;
        //Get rid of double slashes and a slash at the start.

        str = filename.toString();
        str = str.replace("/", "\\");
        str = str.replace("\\\\", "\\");
        if(str.startsWith("\\")) str = str.substring(1);
        filename = new Str(str);

        str = Downloadname.toString();
        str = str.replace("/", "\\");
        str = str.replace("\\\\", "\\");
        if(str.startsWith("\\")) str = str.substring(1);
        Downloadname = new Str(str);
    }*/
    public void SetFilename(String str)
    {
        str = str.replace("/", "\\");
        str = str.replace("\\\\", "\\");
        if(str.startsWith("\\")) str = str.substring(1);
        filename = new Str(str);
    }
    public void SetDownloadname(String str)
    {
        str = str.replace("/", "\\");
        str = str.replace("\\\\", "\\");
        if(str.startsWith("\\")) str = str.substring(1);
        Downloadname = new Str(str);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import shared.e;
import java.util.Vector;

//this file contains 3 blocks, each one of which maps a name onto a list of indexes.  The names don't seem to be files, but the indexes seem to be present as the file names in /sdb (and maybe /mdb).
//Perhaps this gives object names to the numbered files; kind-of like how Plasma2 uses the same name for multiple kinds of objects.
//block reader at 541010.
//aka hsSceneDatabase?

public class Idx
{
    //byte b1; //0
    //byte b2; //0
    //byte b3; //0
    //byte blockcount; //hard to tell since there's only 1 idx file, but I think this is the block count.
    ReverseInt three;
    //IdxBlock[] idxblocks;
    IdxBlock SaveGroupIndex; //refs to /sdb
    IdxBlock RoomIndex; //refs to /sdb
    IdxBlock NamedGroupIndex; //refs to /sdb
    //int u1;
    //int u2;
    //IdxEntry[] entries;
    //Vector<IdxEntry> entries;
    //int u3;
    //IdxEntry[] entries2;
    //int u4;
    //IdxEntry[] entries3;
    
    
    public Idx(IBytestream c)
    {
        //b1 = c.readByte(); e.ensure(b1==0);
        //b2 = c.readByte(); e.ensure(b2==0);
        //b3 = c.readByte(); e.ensure(b3==0);
        //blockcount = c.readByte();
        three = new ReverseInt(c);
        SaveGroupIndex = new IdxBlock(c);
        RoomIndex = new IdxBlock(c);
        NamedGroupIndex = new IdxBlock(c);
        int saveGroupCount = 0;
        for(IdxEntry entry: SaveGroupIndex.entries)
        {
            saveGroupCount += entry.indexcount;
        }
        int roomCount = 0;
        for(IdxEntry entry: RoomIndex.entries)
        {
            roomCount += entry.indexcount;
        }
        int dummy=0;
        //idxblocks = c.readArray(IdxBlock.class, blockcount);
        //c.toString();
        //u1 = c.readInt(); //0x03000000 = 00 00 00 03
        //u2 = c.readInt();
        //entries = c.readArray(IdxEntry.class, u2);
        //u3 = c.readInt();
        //entries2 = c.readArray(IdxEntry.class, u3);
        //u4 = c.readInt();
        //entries3 = c.readArray(IdxEntry.class, u4);
        /*entries = new Vector<IdxEntry>();
        int count = 0;
        while(c.getBytesRemaining()>0)
        {
            try{
            entries.add(new IdxEntry(c));
            count++;
            if(count==85)
            {
                int dummy=0;
            }
            }catch(Exception e)
            {
                int dummy=0;
            }
        }*/
    }
    public static class IdxBlock
    {
        int idxcount;
        IdxEntry[] entries;
        
        public IdxBlock(IBytestream c)
        {
            idxcount = c.readInt();
            entries = c.readArray(IdxEntry.class, idxcount);
        }
    }
    public static class IdxEntry
    {
        Bstr name;
        int indexcount;
        int[] indexes;
        
        public IdxEntry(IBytestream c)
        {
            name = new Bstr(c);
            indexcount = c.readInt();
            indexes = c.readInts(indexcount);
        }
        
        public String toString()
        {
            return name.toString();
        }
    }
}

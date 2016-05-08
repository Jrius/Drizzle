/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import java.util.Random;
import shared.*;

public class Guid
{
    //we want to be able to identify a type by the guid.
    //bytes 0-3 are the type, 4-7 are playerId, 8-11 are ageSequencePrefix, 12-15 are unused.

    public byte[] guid;

    //private Guid(){}
    private static int player = 1;
    private static int ageinstance = 2;

    public Guid(uru.context c)
    {
        this(c.in);
    }
    public Guid(IBytestream c)
    {
        guid = c.readBytes(16);
    }
    public Guid(byte[] bytes)
    {
        if(bytes.length!=16) m.throwUncaughtException("Guid is the wrong size.");
        guid = bytes;
    }
    public void write(IBytedeque c)
    {
        c.writeBytes(guid);
    }
    public static Guid newAgeInstanceGuid(int prefix, int playerId)
    {
        byte[] guid = new byte[16];
        b.Int32IntoBytes(ageinstance, guid, 0);
        b.Int32IntoBytes(playerId, guid, 4);
        b.Int32IntoBytes(prefix, guid, 8);
        return new Guid(guid);
    }
    public static byte[] newRandomPlayer()
    {
        Random rng = new Random();
        //Guid r = new Guid();
        byte[] guid = new byte[16];
        rng.nextBytes(guid);
        b.Int32IntoBytes(player, guid, 0);
        return guid;
    }
    public static Guid fullyRandom()
    {
        Random rng = new Random();
        byte[] guid = new byte[16];
        rng.nextBytes(guid);
        return new Guid(guid);
    }

    public static byte[] none()
    {
        byte[] guid = new byte[16];
        return guid;
    }
    public static Guid none2()
    {
        return new Guid(new byte[16]);
    }

    public static String GuidBytesToString(byte[] guid)
    {
        //00000000-0000-0000-0000-000000000000
        StringBuilder r = new StringBuilder();
        for(int i=0;i<4;i++)
        {
            b.ByteToHexString(r,guid[i]);
        }
        r.append("-");
        for(int i=4;i<6;i++)
        {
            b.ByteToHexString(r,guid[i]);
        }
        r.append("-");
        for(int i=6;i<8;i++)
        {
            b.ByteToHexString(r,guid[i]);
        }
        r.append("-");
        for(int i=8;i<10;i++)
        {
            b.ByteToHexString(r,guid[i]);
        }
        r.append("-");
        for(int i=10;i<16;i++)
        {
            b.ByteToHexString(r,guid[i]);
        }
        return r.toString();
    }

    public static byte[] GuidStringToBytes(String guid)
    {
        //byte[] r = new byte[16];
        guid.replace("-", "");
        byte[] r = b.HexStringToBytes(guid);
        return r;
    }

    public String toString()
    {
        return b.BytesToHexString(guid);
    }
    public byte[] toBytes()
    {
        return guid;
    }

    public int hashCode()
    {
        int hashcode = java.util.Arrays.hashCode(guid);
        return hashcode;
    }

    public boolean equals(Object o2)
    {
        if(!(o2 instanceof Guid)) return false;
        Guid g2 = (Guid)o2;
        return b.isEqual(guid, g2.guid);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

import java.util.Vector;
import shared.*;

public class Patcher
{

    public static void PatchFile(String filename, String newdomainname)
    {
        byte[] data = FileUtils.ReadFile(filename);
        byte[] md5 = CryptHashes.GetMd5(data);
        String md5str = b.BytesToHexString(md5).toUpperCase();

        if(md5str.equals("6F1BBD558DBC4B8963AACA6CCCECC1B3"))
        {
            //Moulagain 1.893 UruLauncher.exe

            //patch servers
            //PatchUtf16(data, 0x5A740, newdomainname);
            //PatchUtf16(data, 0x5C9FC, newdomainname);
            //PatchUtf16(data, 0x5CA18, newdomainname);
            PatchUtf16(data, "support.cyanworlds.com", newdomainname);
            PatchUtf16(data, "67.202.54.141", newdomainname);
            PatchUtf16(data, "184.73.198.22", newdomainname);

            //patch server keys
            PatchHex(data,Version.moulagain_1_893.gateserver_mod,Version.talcum.gateserver_mod);
            PatchHex(data,Version.moulagain_1_893.gateserver_B,Version.talcum.gateserver_B);
            PatchHex(data,Version.moulagain_1_893.authserver_mod,Version.talcum.authserver_mod);
            PatchHex(data,Version.moulagain_1_893.authserver_B,Version.talcum.authserver_B);
            PatchHex(data,Version.moulagain_1_893.csrserver_mod,Version.talcum.csrserver_mod);
            PatchHex(data,Version.moulagain_1_893.csrserver_B,Version.talcum.csrserver_B);
            PatchHex(data,Version.moulagain_1_893.gameserver_mod,Version.talcum.gameserver_mod);
            PatchHex(data,Version.moulagain_1_893.gameserver_B,Version.talcum.gameserver_B);
        }
        else if(md5str.equals("6B299C93B10DF1F351C0D92DF7115C64"))
        {
            //Moulagain 1.893 UruExplorer.exe

            //patch the shard status text
            PatchUtf16(data, "support.cyanworlds.com", newdomainname);
            //patch the "Need an Account?" button
            PatchUtf8(data,"http://www.mystonline.com/signup.html","http://"+newdomainname+"/signup.html");
            //patch the server ips
            PatchUtf16(data, "67.202.54.141", newdomainname);
            PatchUtf16(data, "184.73.198.22", newdomainname);

            //patch server keys
            PatchHex(data,Version.moulagain_1_893.gateserver_mod,Version.talcum.gateserver_mod);
            PatchHex(data,Version.moulagain_1_893.gateserver_B,Version.talcum.gateserver_B);
            PatchHex(data,Version.moulagain_1_893.authserver_mod,Version.talcum.authserver_mod);
            PatchHex(data,Version.moulagain_1_893.authserver_B,Version.talcum.authserver_B);
            PatchHex(data,Version.moulagain_1_893.csrserver_mod,Version.talcum.csrserver_mod);
            PatchHex(data,Version.moulagain_1_893.csrserver_B,Version.talcum.csrserver_B);
            PatchHex(data,Version.moulagain_1_893.gameserver_mod,Version.talcum.gameserver_mod);
            PatchHex(data,Version.moulagain_1_893.gameserver_B,Version.talcum.gameserver_B);
        }
        else
        {
            m.err("Cannot find a patch for this file's md5.");
            return;
        }

        //save
        FileUtils.CopyFile(filename, filename+".orig", true, false);
        FileUtils.WriteFile(filename, data, false, true);

        m.msg("Done patching!");

    }
    public static void PatchUtf8(byte[] data, String oldstr, String newstr)
    {
        //todo: like patchutf16
        if(newstr.length()>oldstr.length())
        {
            throw new uncaughtexception("Newhexstr *probably* shouldn't be longer than Oldhexstr.");
        }
        byte[] oldstrbs = b.StringToBytes(oldstr);
        byte[] newstrbs = b.StringToBytes(newstr);
        Vector<Integer> locs = b.findBytes_All(data, oldstrbs);
        for(int loc: locs)
        {
            b.writeNullTerminatedBytes(data, loc, newstrbs);
            m.msg("Patched "+oldstr+" to "+newstr+" at position "+Integer.toString(loc));
        }
    }
    public static void PatchHex(byte[] data, String oldhexstr, String newhexstr)
    {
        if(newhexstr.length()>oldhexstr.length())
        {
            throw new uncaughtexception("Newhexstr *probably* shouldn't be longer than Oldhexstr.");
        }

        byte[] oldbytes = b.HexStringToBytes(oldhexstr);
        byte[] newbytes = b.HexStringToBytes(newhexstr);
        Vector<Integer> locs = b.findBytes_All(data, oldbytes);
        for(int loc: locs)
        {
            b.CopyBytes(newbytes, data, loc);
            m.msg("Patched "+oldhexstr+" to "+newhexstr+" at position "+Integer.toString(loc));
        }
    }
    public static void PatchUtf16(byte[] data, String oldstr, String str)
    {
        byte[] oldstrbs = b.StringToUtf16Bytes(oldstr);
        Vector<Integer> positions = b.findBytes_All(data, oldstrbs);
        for(int loc: positions)
        {
            PatchUtf16(data,loc,str);
        }
    }
    public static void PatchUtf16(byte[] data, int loc, String str)
    {
        String curstr = b.readNullTerminatedUtf16FromBytes(data, loc);
        if(str.length()>curstr.length())
        {
            throw new uncaughtexception("String "+str+" is too long to fit in "+curstr+" so make it shorter.");
        }
        b.writeNullTerminatedUtf16FromString(data,loc,str);
        m.msg("Patched "+curstr+" at location "+Integer.toString(loc)+" to "+str);
    }

    
//    private static PatchInfo[] patches = /*getPatches();*/ new PatchInfo[]{
//        new PatchInfo("6F1BBD558DBC4B8963AACA6CCCECC1B3").addDnsLocation(0x5A740).addDnsLocation(0x5C9FC).addDnsLocation(0x5CA18), //Moulagain 1.893
//        //0x5E5E8 n
//        //0x5E628 x
//    };


//    private static class PatchInfo
//    {
//        String expectedmd5hash;
//        //String newdomainname;
//        Vector<Integer> locations = new Vector();
//        Vector<String> dnsnames = new Vector();
//        //Vector
//        public PatchInfo(String md5hash)
//        {
//            this.expectedmd5hash = md5hash;
//        }
//
//        public PatchInfo addDnsLocation(int loc)
//        {
//            locations.add(loc);
//            return this;
//        }
//        public PatchInfo addDnsName(String dnsname)
//        {
//            dnsnames.add(dnsname);
//            return this;
//        }
//    }
//    public static void PatchFile2(String filename, String newdomainname)
//    {
//
//        byte[] data = FileUtils.ReadFile(filename);
//        byte[] md5 = CryptHashes.GetMd5(data);
//        String md5str = b.BytesToHexString(md5);
//        //IBytestream c = shared.ByteArrayBytestream.createFromByteArray(data);
//
//        //find patch
//        PatchInfo patch = FindPatch(md5str);
//        if(patch==null)
//        {
//            m.err("Cannot find a patch for this file's md5.");
//            return;
//        }
//
//        //check size of newdomainname
//        int maxlength = 1000;
//        for(int loc: patch.locations)
//        {
//            String curdns = b.readNullTerminatedUtf16FromBytes(data, loc);
//            if(curdns.length()<maxlength) maxlength = curdns.length();
//            m.msg("Address found: ",curdns);
//        }
//        if(newdomainname.length()>maxlength)
//        {
//            m.err("New address length must be no more than: ",Integer.toString(maxlength));
//            return;
//        }
//
//        //patch
//        for(int loc: patch.locations)
//        {
//            b.writeNullTerminatedUtf16FromString(data,loc,newdomainname);
//        }
//
//        //save
//        FileUtils.CopyFile(filename, filename+".orig", true, false);
//        FileUtils.WriteFile(filename, data, false, true);
//
//        m.msg("Done patching!");
//    }
//
//    static PatchInfo FindPatch(String md5str)
//    {
//        for(PatchInfo patch: patches)
//        {
//            if(patch.expectedmd5hash.toUpperCase().equals(md5str.toUpperCase()))
//            {
//                return patch;
//            }
//        }
//        return null;
//    }
}

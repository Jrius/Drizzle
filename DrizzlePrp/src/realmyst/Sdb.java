/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package realmyst;

import shared.*;
import shared.e;

public class Sdb
{
    public int tag;
    public int u1;
    public int filesizeMinusHeader;
    public Bstr name;
    //public int u3;
    public Bstr u3;
    public int strCount; //number of Bstrs that follow
    public Bstr[] strs;
    
    public Count2[] count2s;
    public Count3Undone[] count3s;
    public Count10[] count10s;
    
    public String sourceName;
    
    public Sdb(IBytestream c)
    {
        sourceName = c.sourceName;
        String filename = new java.io.File(c.sourceName).getName();
        String trapfilename=
                //"113499986.vdb"
                //"14341445.vdb"
                //"45043.vdb"
                //"6910138.vdb"
                "85655908.vdb"
                ;
        if(c.sourceName.toLowerCase().endsWith(trapfilename.toLowerCase()))
        {
            int dummy=0;
        }
        else
        {
            //return;
        }
        
        boolean ignore = true;
        
        tag = c.readInt();
        if(tag!=0x02000000) throw new uncaughtexception("Sdb didn't have the correct magic number.");
        
        //sdb header
        u1 = c.readInt(); //filesize (including header) Sometimes not, though.
        filesizeMinusHeader = c.readInt(); //filesize minus header
        name = new Bstr(c);
        //m.msg(name.toString()+" u1="+Integer.toString(u1)+" u2="+Integer.toString(filesizeMinusHeader));
        m.msg(name.toString());
        if(name.toString().toLowerCase().startsWith("holdingpen_"))
        {
            int dummy=0;
        }
        else
        {
            //return;
        }
        //u3 = c.readInt();
        u3 = new Bstr(c);
        strCount = c.readInt();
        //s2 = new Bstr(c);
        if(strCount!=1)
        {
            int dummy=0;
        }
        strs = c.readArray(Bstr.class, strCount);

        //if(true)return;

        if(c.getBytesRemaining()<40)
        {
            int dummy=0;
        }

        //if(true)return;

        //hsObjectGroup (sub_46c140)
        int tag2 = c.readInt(); //should actually be a reverse int.
        if(tag2!=0x9469DF4E)
        {
            //ignore = false;
            throw new uncaughtexception("Unhandled tag.");
        }
        ReverseInt two = new ReverseInt(c); e.ensure(two.convertToInt()==2);
        int six = c.readInt();  e.ensure(six==6);
        //int u8 = c.readInt(); //v6
        //if(u8!=0)
        //{
        //    //ignore = false;
        //    throw new ignore("Unhandled u8.");
        //}
        Bstr u8 = new Bstr(c);
        if(u8.strlen>0)
        {
            int dummy=0;
        }
        int count = c.readInt(); //v65
        Bstr[] morestrs = c.readArray(Bstr.class, count);
        int count2 = c.readInt(); //v69
        if(count2!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count2.");
            count2s = c.readArray(Count2.class, count2);
        }
        int count3 = c.readInt(); //v72
        if(count3!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count3.");
            count3s = c.readArray(Count3Undone.class, count3);
        }

        //new try:
        int hasThing = c.readInt();
        if(hasThing!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled hasThing.");
            CountAUndone cau = new CountAUndone(c);
        }
        int count4 = c.readInt(); //v78
        if(count4!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count4.");
            Count4Undone[] count4s = c.readArray(Count4Undone.class, count4);
        }
        int u10 = c.readInt(); //stored at offset 148
        int u11 = c.readInt(); //v15
        if(u11!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled u11.");
        }
        int count5 = c.readInt(); //v84
        if(count5!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count5.");
            Count5Undone[] count5s = c.readArray(Count5Undone.class, count5);
        }
        int count6 = c.readInt(); //v86
        if(count6!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count6.");
            Count6Undone[] count6s = c.readArray(Count6Undone.class, count6);
        }
        int count7 = c.readInt(); //v91
        if(count7!=0)
        {
            //ignore = false;
            //throw new ignore("Unhandled count7.");
            Count7Undone[] count7s = c.readArray(Count7Undone.class, count7);
        }
        int count8 = c.readInt(); //v96
        if(count8!=0)
        {
            //ignore = false;
            //throw new ignore("unhandled count8.");
            Count8Undone[] count8s = c.readArray(Count8Undone.class, count8);
        }
        int count9 = c.readInt(); //v101
        if(count9!=0)
        {
            //ignore = false;
            //if(ignore) throw new ignore("unhandled.");
            Count9[] c9s = c.readArray(Count9.class, count9);
        }
        int count10 = c.readInt(); //v107
        if(count10!=0) //used by age objects, for example.
        {
            //m.err("unhandled.");
            //ignore = false;
            //if(ignore) throw new ignore("unhandled.");
            count10s = c.readArray(Count10.class, count10);
        }
        int count11 = c.readInt(); //result
        if(count11!=0)
        {
            //if(ignore) throw new ignore("unhandled.");
            //m.msg("count11:"+c.sourceName);
            //throw new uncaughtexception("unhandled.");
            Count11[] c11s = c.readArray(Count11.class, count11);
        }
        int dummy=0;

        //old try:
        /*else
        {
            int hasThing = c.readInt();
            if(hasThing!=0)
            {
                throw new uncaughtexception("Unhandled.");
            }
            int count4 = c.readInt(); //v78
            if(count4!=0)
            {
                throw new uncaughtexception("Unhandled.");
            }
            else
            {
                int u10 = c.readInt(); //stored at offset 148
                int u11 = c.readInt(); //v15
                if(u11!=0)
                {
                    throw new uncaughtexception("Unhandled.");
                }
                int count5 = c.readInt(); //v84
                if(count5!=0)
                {
                    throw new uncaughtexception("Unhandled.");
                }
                else
                {
                    int count6 = c.readInt(); //v86
                    if(count6!=0)
                    {
                        throw new uncaughtexception("Unhandled.");
                    }
                    else
                    {
                        int count7 = c.readInt(); //v91
                        if(count7!=0)
                        {
                            throw new uncaughtexception("Unhandled.");
                        }
                        else
                        {
                            int count8 = c.readInt(); //v96
                            if(count8!=0)
                            {
                                throw new uncaughtexception("unhandled.");
                            }
                            else
                            {
                                int count9 = c.readInt(); //v101
                                if(count9!=0)
                                {
                                    throw new uncaughtexception("unhandled.");
                                }
                                else
                                {
                                    int count10 = c.readInt(); //v107
                                    if(count10!=0)
                                    {
                                        //m.err("unhandled.");
                                        //if(true) throw new uncaughtexception("unhandled.");
                                        Count10[] u1s = c.readArray(Count10.class, count10);
                                    }
                                    else
                                    {
                                        int count11 = c.readInt(); //result
                                        if(count11!=0)
                                        {
                                            m.msg("count11:"+c.sourceName);
                                            //throw new uncaughtexception("unhandled.");
                                            Count11[] c11s = c.readArray(Count11.class, count11);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }*/

        //Sceneobject so = new Sceneobject(c);

        /*else if(tag==0x529A6D54)
        {
            //mdb
            u1 = c.readInt(); //filesize (including header)
            filesizeMinusHeader = c.readInt(); //=1?
            if(filesizeMinusHeader!=1)
            {
                int dummy=0;
            }
            name = new Bstr(c);
            
            int u3 = c.readInt();
            byte b4 = c.readByte();
            int u5 = c.readInt();
            int u6 = c.readInt();
            int u7 = c.readInt();
            int u8 = c.readInt();
            int u9 = c.readInt();
            int u10 = c.readInt();
            m.msg(name.toString());
        }
        else
        {
            m.err("Unhandled tag: 0x"+Integer.toHexString(tag));
        }
        int dummy=0;*/
    }
}

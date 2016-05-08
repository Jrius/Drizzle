package classfiles;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import shared.IBytestream;
import shared.m;
import shared.b;
import java.util.Vector;

public class classfile
{
    byte[] magic;
    short minver;
    short maxver;
    short poolcountbad;
    Vector<cp_info> constant_pool = new Vector();
    //there is more here, but we don't need it now.

    public classfile(IBytestream c)
    {

        try
        {
            //IBytestream c = shared.ByteArrayBytestream.createFromByteArray(shared.GetResource.getResourceAsByteArray("/translation/translation.class"));
            //java.io.DataInputStream dis = new java.io.DataInputStream(shared.GetResource.getResourceAsStream("/translation/translation.class"));
            magic = c.readBytes(4);
            minver = c.readShortBigEndian();
            maxver = c.readShortBigEndian();
            poolcountbad = c.readShortBigEndian();
            for(int i=1;i<poolcountbad;i++)
            {
                cp_info cpinfo = new cp_info(c);
                constant_pool.add(cpinfo);
                if(cpinfo.tag==0x06 || cpinfo.tag==0x05)
                {
                    //weird thing with java where longs and doubles take up two index entries, so lets just put it in twice.
                    constant_pool.add(cpinfo);
                    i++;
                }
            }
            int dummy=0;
        }
        catch(Exception e)
        {
            int dummy=0;
        }
    }

    public Vector<String> getAllConstantStrings()
    {
        Vector<String> result = new Vector();
        for(cp_info cpinfo: constant_pool)
        {
            if(cpinfo.tag==0x08)
            {
                constant_string_info csi = (constant_string_info)cpinfo.info;
                short index = csi.string_index;
                cp_info strinfo = constant_pool.get(index-1); //1-indexed instead of 0-indexed.
                constant_utf8_info utf8 = (constant_utf8_info)strinfo.info;
                String str = utf8.toString();
                result.add(str);
            }
        }
        return result;
    }

    public static class cp_info
    {
        byte tag;
        info2 info;

        public cp_info(IBytestream c)
        {
            tag = c.readByte();
            switch(tag)
            {
                case 0x01:
                    info = new constant_utf8_info(c);
                    break;
                case 0x03:
                    info = new constant_integer_info(c);
                    break;
                case 0x04:
                    info = new constant_float_info(c);
                    break;
                case 0x05:
                    info = new constant_long_info(c);
                    break;
                case 0x06:
                    info = new constant_double_info(c);
                    break;
                case 0x07:
                    info = new constant_class_info(c);
                    break;
                case 0x08:
                    info = new constant_string_info(c);
                    break;
                case 0x09:
                    info = new constant_fieldref_info(c);
                    break;
                case 0x0A:
                    info = new constant_methodref_info(c);
                    break;
                case 0x0B:
                    info = new constant_interfacemethodref_info(c);
                    break;
                case 0x0C:
                    info = new constant_nameandtype_info(c);
                    break;
                default:
                    m.err("Unhandled tag in class file.");
                    break;
            }
        }

    }

    public abstract static class info2
    {
    }
    public static class constant_double_info extends info2
    {
        byte[] high_bytes;
        byte[] low_bytes;
        public constant_double_info(IBytestream c)
        {
            high_bytes = c.readBytes(4);
            low_bytes = c.readBytes(4);
        }
    }
    public static class constant_long_info extends info2
    {
        byte[] high_bytes;
        byte[] low_bytes;
        public constant_long_info(IBytestream c)
        {
            high_bytes = c.readBytes(4);
            low_bytes = c.readBytes(4);
        }
    }
    public static class constant_float_info extends info2
    {
        byte[] bytes;
        public constant_float_info(IBytestream c)
        {
            bytes = c.readBytes(4);
        }
    }
    public static class constant_integer_info extends info2
    {
        byte[] bytes;
        public constant_integer_info(IBytestream c)
        {
            bytes = c.readBytes(4);
        }
    }
    public static class constant_nameandtype_info extends info2
    {
        short name_index;
        short descriptor_index;
        public constant_nameandtype_info(IBytestream c)
        {
            name_index = c.readShortBigEndian();
            descriptor_index = c.readShortBigEndian();
        }
    }
    public static class constant_utf8_info extends info2
    {
        short length;
        byte[] bytes;

        public constant_utf8_info(IBytestream c)
        {
            length = c.readShortBigEndian();
            bytes = c.readBytes(length);
        }

        public String toString()
        {
            //this isn't correct, because it's actually a weird variation of UTF-8.  I belive that java.io.DataInput can correctly read this.
            return b.BytesToString(bytes);
        }
    }
    public static class constant_interfacemethodref_info extends info2
    {
        short class_index;
        short name_and_type_index;

        public constant_interfacemethodref_info(IBytestream c)
        {
            class_index = c.readShortBigEndian();
            name_and_type_index = c.readShortBigEndian();
        }
    }
    public static class constant_fieldref_info extends info2
    {
        short class_index;
        short name_and_type_index;

        public constant_fieldref_info(IBytestream c)
        {
            class_index = c.readShortBigEndian();
            name_and_type_index = c.readShortBigEndian();
        }
    }
    public static class constant_class_info extends info2
    {
        short name_index;
        public constant_class_info(IBytestream c)
        {
            name_index = c.readShortBigEndian();
        }
    }
    public static class constant_string_info extends info2
    {
        short string_index;

        public constant_string_info(IBytestream c)
        {
            string_index = c.readShortBigEndian();
        }
    }
    public static class constant_methodref_info extends info2
    {
        //byte tag;
        short class_index;
        short name_and_type_index;

        public constant_methodref_info(IBytestream c)
        {
            //tag = c.readByte();
            class_index = c.readShortBigEndian();
            name_and_type_index = c.readShortBigEndian();
        }
    }

}

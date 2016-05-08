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

import shared.*;

public class Count6Undone
{
    public Typeid type;
    public int size;
    public byte[] data;
    
    
    // attempt at reading
    public int tag;
    public Bstr name;
    public Matrix position;
    
    // following: more raw data, sometimes scene names, more raw data, and sometimes a list of objects
    
    public Count6Undone(IBytestream c)
    {
        type = Typeid.read(c);
        size = c.readInt();
        
        IBytestream f = c.Fork();
        
        //skip the rest.
        data = c.readBytes(size-8);
        
        
        // attempt at reading
        tag = f.readInt(); e.ensure(tag,1);
        name = new Bstr(f);
        
        if (data.length < 157)
        {
            position = new Matrix();
            return;
        }
        
        // unknown ints and floats
        // incorrect for objects which don't use an axis system (as children objects).
        // However, should be valid for a lot other.
        byte u_1 = f.readByte(); //e.ensure(u_1, 4);  // 04
        int u_2 = f.readInt(); //e.ensure(u_2, 0);    // 00000000
        int u_3 = f.readInt(); //e.ensure(u_3, 3);    // 03000000
        int u_4 = f.readInt(); //e.ensure(u_4, 3);    // 0d7b7476
        int u_5 = f.readInt(); ////e.ensure(u_5, 152);  // 98000000
        int u_6 = f.readInt(); ////e.ensure(u_6, 1);    // 01000000
        float u_7 = f.readFloat();//e.ensure(u_7,100);// 0000c842
        int u_8 = f.readInt(); //e.ensure(u_8, 0);    // 00000000
        int u_9 = f.readInt(); //e.ensure(u_9, 0);    // 00000000
        float u15 = f.readFloat();////e.ensure(u15, -2);// c0000000
        int u14 = f.readInt(); //e.ensure(u14, 0);    // 00000000
        int u13 = f.readInt(); ////e.ensure(u13, 32);   // 20000000
        int u12 = f.readInt(); ////e.ensure(u12, 8192); // 00200000
        int u11 = f.readInt(); //e.ensure(u11, 1);    // 01000000
        int u10 = f.readInt(); ////e.ensure(u10, 9);    // 09000000
        int u9 = f.readInt(); //e.ensure(u9, 0);      // 00000000
        int u8 = f.readInt(); ////e.ensure(u8, 0);      // 00000000
        float u7 = f.readFloat(); //e.ensure(u7, .5); // 0000003f
        float u6 = f.readFloat(); //e.ensure(u6, 1);  // 803f0000
        float u5 = f.readFloat(); //e.ensure(u5, 1);  // 803f0000
        int u4 = f.readInt(); //e.ensure(u4, -1);     // ffffffff
        int u3 = f.readInt(); ////e.ensure(u3, 8);      // 08000000
        int u2 = f.readInt(); //e.ensure(u2, 0);      // 00000000
        int u1 = f.readInt(); //e.ensure(u1, 1);      // 01000000
        position = new Matrix(f);
        
        if (
                false && (
                position.values[3][0].toJavaFloat() != 0 ||
                position.values[3][1].toJavaFloat() != 0 ||
                position.values[3][2].toJavaFloat() != 0 ||
                position.values[3][3].toJavaFloat() != 1
                
                // very moar dirty hacxxxxx ! dude, that's not even /coded/ correctly.
                ||
                position.values[0][0].toJavaFloat() > 80 ||
                position.values[0][0].toJavaFloat() < -80 ||
                position.values[0][1].toJavaFloat() > 80 ||
                position.values[0][1].toJavaFloat() < -80 ||
                position.values[0][2].toJavaFloat() > 80 ||
                position.values[0][2].toJavaFloat() < -80 ||
                
                position.values[1][0].toJavaFloat() > 80 ||
                position.values[1][0].toJavaFloat() < -80 ||
                position.values[1][1].toJavaFloat() > 80 ||
                position.values[1][1].toJavaFloat() < -80 ||
                position.values[1][2].toJavaFloat() > 80 ||
                position.values[1][2].toJavaFloat() < -80 ||
                
                position.values[2][0].toJavaFloat() > 80 ||
                position.values[2][0].toJavaFloat() < -80 ||
                position.values[2][1].toJavaFloat() > 80 ||
                position.values[2][1].toJavaFloat() < -80 ||
                position.values[2][2].toJavaFloat() > 80 ||
                position.values[2][2].toJavaFloat() < -80
                ))
            // hardcode line 4 to 0001 or else reset matrix (very dirty hack, but well... working)
            position = new Matrix(f);
        /* position matrix:
        xx xy xz tx
        yx yy yz ty
        zx zy zz tz
         0  0  0  1
        * /
        int u0 = f.readInt(); e.ensure(u0, 0);      // 00000000
        int ua = f.readInt(); e.ensure(ua, 0);      // 00000000
        //*/
    }
    public void wout(String scename, int num)
    {
        String objname = name.toString().substring(name.toString().indexOf("..")+2);
        String agename = name.toString().substring(0, name.toString().indexOf(".."));
        String dirname = "tmp/c6/";
        
        IBytedeque out = new Bytedeque2(shared.Format.realmyst);
        out.writeBytes(data);
        byte[] filedata = out.getAllBytes();
        
        /*
        String fullname = dirname+agename+"/"+scename+"/"+objname+".dat";
        
        if (FileUtils.Exists(fullname))
            m.warn("File "+fullname+" already exists !");
        FileUtils.CreateFolder(dirname+agename+"/"+scename+"/");
        FileUtils.WriteFile(fullname, filedata);
                */
        
        String fullname = dirname+scename+"/"+agename+"/"+objname+".dat";
        
        if (FileUtils.Exists(fullname))
            m.warn("File "+fullname+" already exists !");
        FileUtils.CreateFolder(dirname+scename+"/"+agename+"/");
        FileUtils.WriteFile(fullname, filedata);
    }
}

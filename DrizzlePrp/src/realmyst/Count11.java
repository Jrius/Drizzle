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

//wav files
public class Count11
{
    Typeid type;
    
    public Count11(IBytestream c)
    {
        int startpos = c.getAbsoluteOffset();
        type = Typeid.read(c); e.ensure(type==Typeid.count11);
        int u1 = c.readInt(); //total length of this object.
        int endpos = startpos+u1;
        //m.msg("u1(binary)="+Integer.toBinaryString(u1));
        int u2 = c.readInt(); e.ensure(u2,0,1); //1
        int u3 = c.readInt(); e.ensure(u3,-1);
        int u4 = c.readInt(); e.ensure(u4,0,1,2); //1,2
        Bstr s1 = new Bstr(c);
        Bstr s2 = new Bstr(c);
        if(u2==0)
        {
            Bstr s3 = new Bstr(c); //sometimes "0" or ""
            Bstr s4 = new Bstr(c); //sometimes "100" or "74"
            int u5 = c.readInt(); e.ensure(u5,0,1);
            //if(u5==1)
            //if((u1&32)!=0)
            //if(c.getAbsoluteOffset()!=endpos)
            if(u4==1)
            {
                //if(u4!=1)
                //{
                //    int dummy=0;
                //}
                //m.msg("reading extra int and flt");
                int u6 = c.readInt(); e.ensure(u6,4,10);
                Flt f1 = new Flt(c); //0.5
                int dummy=0;
            }
            //else
            //{
                //if(u4==1)
                //{
                //    int dummy=0;
                //}
            //}
        }
        else if(u2==1)
        {
            Flt f1 = new Flt(c);
            Bstr s6 = new Bstr(c);
            int dummy=0;
        }
        int dummy=0;
    }
}

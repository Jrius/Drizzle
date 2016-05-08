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
import uru.Bytestream;
import uru.Bytedeque;
import shared.m;

/**
 *
 * @author user
 */
public class PrpObjectIndex extends uruobj
{

    public int indexCount;
    //Vector<ObjectindexObjecttype> types = new Vector<ObjectindexObjecttype>();
    public ObjectindexObjecttype[] types;

    public PrpObjectIndex(context c)
    {
        indexCount = c.in.readInt();
        types = new ObjectindexObjecttype[indexCount];
        for(int i=0; i<indexCount; i++)
        {
            types[i] = new ObjectindexObjecttype(c);
        }
    }
    public void compile(Bytedeque deque)
    {
        m.msg("compile not implemented");

    }


    public static class ObjectindexObjecttype extends uruobj
    {
        //short type;
        Typeid type;
        int datasize;
        byte u1;
        public int objectcount;
        //Vector<ObjectindexObjecttypeObjectdesc> descs = new Vector<ObjectindexObjecttypeObjectdesc>();
        public ObjectindexObjecttypeObjectdesc[] descs;

        public ObjectindexObjecttype(context c)
        {
            //type = data.readShort();
            if(c.readversion==6||c.readversion==4||c.readversion==7)
            {
                type = Typeid.ReadEvenIfUnknown(c);
                datasize = c.in.readInt(); //relative offset to next objecttype.
                u1 = c.in.readByte(); //should be 0 or 1, apparently.
                objectcount = c.in.readInt();
                descs = new ObjectindexObjecttypeObjectdesc[objectcount];
                for(int i=0; i<objectcount; i++)
                {
                    descs[i] = new ObjectindexObjecttypeObjectdesc(c);
                }
            }
            else if(c.readversion==3)
            {
                type = Typeid.ReadEvenIfUnknown(c);
                objectcount = c.in.readInt();
                descs = new ObjectindexObjecttypeObjectdesc[objectcount];
                for(int i=0; i<objectcount; i++)
                {
                    descs[i] = new ObjectindexObjecttypeObjectdesc(c);
                }
            }
        }
        public void compile(Bytedeque deque)
        {
            m.msg("compile not implemented");

        }
        public String toString()
        {
            return "ObjectIndexObjectType: "+type.toString();
        }
    }
    
    public static class ObjectindexObjecttypeObjectdesc extends uruobj
    {
        public Uruobjectdesc desc;
        public int offset;
        public int size;

        //_uruobj object;

        public ObjectindexObjecttypeObjectdesc(context c)
        {
            desc = new Uruobjectdesc(c);
            offset = c.in.readInt(); //absolute offset to object
            size = c.in.readInt(); //size of the object(including its header)

        }
        public void compile(Bytedeque deque)
        {
            //desc.saveAsUruobj(deque);
            m.msg("compile not implemented");
        }
        public String toString()
        {
            return "Objectindexdesc:"+desc.toString();
        }
    }
}

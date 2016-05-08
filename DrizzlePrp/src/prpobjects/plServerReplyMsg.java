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
//import java.util.Vector;
import shared.*;


public class plServerReplyMsg extends uruobj
{
    public static final int kUnInit = -1;
    public static final int kDeny = 0;
    public static final int kAffirm = 1;

    public plMessage parent;
    public int type;
    
    public plServerReplyMsg(context c) throws readexception
    {
        if(c.readversion!=6) m.throwUncaughtException("untested");
        parent = new plMessage(c);
        type = c.readInt();
    }
    
    public void compile(Bytedeque c)
    {
        //moul format, not necessarily pots.
        if(c.format!=Format.moul) m.throwUncaughtException("unhandled");
        parent.compile(c);
        c.writeInt(type);
    }

    private plServerReplyMsg(){}
    public static plServerReplyMsg createDefault()
    {
        plServerReplyMsg r = new plServerReplyMsg();
        r.parent = plMessage.createDefault();
        return r;

    }
    
}

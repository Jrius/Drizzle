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
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;
import shared.*;


public class pfGUITextBoxMod extends uruobj
{
    public pfGUIControlMod parent;
    public Bstr str1;
    public byte xb2;
    public Urustring16 xstr3; //e.g. ACA.GUI.Age.  Used for internationalization?
    
    public pfGUITextBoxMod(context c) throws readexception
    {
        parent = new pfGUIControlMod(c);
        str1 = new Bstr(c);
        if(c.readversion==6)
        {
            xb2 = c.readByte();
            if(xb2!=0)
            {
                //read an Urustring?
                xstr3 = new Urustring16(c);
                //m.throwUncaughtException("Unhandled.");
            }
        }
    }
    
    public void compile(Bytedeque c)
    {
        //m.warn("compile not implemented.",this.toString());
        //m.warn("not tested with pots.",this.toString());
        //m.warn("untested pfGUITextBoxMod");
        parent.compile(c);
        str1.compile(c);
    }
    
}

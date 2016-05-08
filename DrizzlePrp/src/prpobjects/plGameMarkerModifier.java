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


public class plGameMarkerModifier extends uruobj
{
    plSingleModifier parent;
    Uruobjectref greenAnim;
    Uruobjectref redAnim;
    Uruobjectref openAnim;
    Uruobjectref bounceAnim;
    short placeSoundIdx;
    short hitSoundIdx;
    
    public plGameMarkerModifier(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        greenAnim = new Uruobjectref(c);
        redAnim = new Uruobjectref(c);
        openAnim = new Uruobjectref(c);
        bounceAnim = new Uruobjectref(c);
        placeSoundIdx = c.readShort();
        hitSoundIdx = c.readShort();
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        greenAnim.compile(c);
        redAnim.compile(c);
        openAnim.compile(c);
        bounceAnim.compile(c);
        c.writeShort(placeSoundIdx);
        c.writeShort(hitSoundIdx);
    }
    
}

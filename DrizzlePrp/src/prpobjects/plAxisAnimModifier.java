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

import shared.Flt;
import uru.context; import shared.readexception;
import uru.Bytestream;
import uru.Bytedeque;
import shared.e;
import shared.m;
import shared.b;
import shared.readexception;
//import java.util.Vector;


public class plAxisAnimModifier extends uruobj
{
    public plSingleModifier parent;
    public Uruobjectref xAnim;
    public Uruobjectref yAnim;
    public Uruobjectref notificationKey;
    public byte allOrNothing;
    public PrpTaggedObject message;
    public Wpstr animLabel;
    
    /* Typical problem: PotS only allows the item to be dragged, MV flags didn't exist at that time
     * Things missing:
     *  - speed factor
     *  - sens (could be negative speed factor)
     *  - stop values (think of Siralehn's turnable rock)
     *  - release position (when releasing, animation will go back to closest "node")
     * Oh, and notification doesn't seem to work anyway. I'll resort to add some AnimEventModifiers here and there (tested on Srln turnable rocks, is perfect).
     * To fix speed factor and sens: edit the animations (might lose precision, will be hard, but could work).
     * To fix release pos: use anim.playToTime(). Stop values: if we have release pos, it won't be necessary. Might be even easier to drag Siralehn's rocks.
     * Best solution would be to fire those axis animations, and put typical Uru anims, but might be more work.
     */
    
    public plAxisAnimModifier(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        if(c.readversion==4)
        {
            byte b1 = c.readByte();
            byte b2 = c.readByte();
        }
        xAnim = new Uruobjectref(c);
        yAnim = new Uruobjectref(c);
        notificationKey = new Uruobjectref(c);
        allOrNothing = c.readByte();
        message = new PrpTaggedObject(c);
        animLabel = new Wpstr(c);
        if(c.readversion==4)
        {
            int count1 = c.readInt();
            Flt[] flts1 = c.readArray(Flt.class, count1);
            int count2 = c.readInt();
            Flt[] flts2 = c.readArray(Flt.class, count2);
            byte b4 = c.readByte();
            Flt f1 = new Flt(c);
            Flt f2 = new Flt(c);
            Flt f3 = new Flt(c);
            
            Flt f4 = new Flt(c);
            Flt f5 = new Flt(c);
            Flt f6 = new Flt(c);
            Flt f7 = new Flt(c);
            Flt f8 = new Flt(c);
            byte b5 = c.readByte();
            byte b6 = c.readByte();
            byte b7 = c.readByte();
            byte b8 = c.readByte();
        }
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        xAnim.compile(c);
        yAnim.compile(c);
        notificationKey.compile(c);
        c.writeByte(allOrNothing);
        message.compile(c);
        animLabel.compile(c);
    }
    
}

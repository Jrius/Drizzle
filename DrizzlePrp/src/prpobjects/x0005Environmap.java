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
//import java.util.Vector;

/**
 *
 * @author user
 */
public class x0005Environmap extends uruobj
{
    //Objheader xheader;
    x0003Bitmap parent;
    public x0004MipMap[] sides = new x0004MipMap[6];
    
    Uruobjectref[] unused = new Uruobjectref[6];
    
    public x0005Environmap(context c) throws readexception //,boolean hasHeader)
    {
        //if(hasHeader) xheader = new Objheader(c);
        parent = new x0003Bitmap(c);//,false);
        for(int i=0;i<6;i++)
        {
            if(c.readversion==4||c.readversion==7)
            {
                //c.readByte(); //0
                //c.readInt(); //-1
                //c.readBytes(9); //0
                unused[i] = new Uruobjectref(c);
            }
            sides[i] = new x0004MipMap(c);//,false);
        }
        //Moved to mystv hacks:
        /*if(c.readversion==4||c.readversion==7) //guessing about hexisle, but it is correct above, so probably correct here?
        {
            //fix up total inversion.
            if(c.readversion==7)
            {
                m.warn("Flipping CubicEnvironMaps, but perhaps shouldn't be.");
            }
            x0004MipMap temp = sides[0];
            sides[0] = sides[1];
            sides[1] = temp;
            temp = sides[2];
            sides[2] = sides[3];
            sides[3] = temp;
            temp = sides[4];
            sides[4] = sides[5];
            sides[5] = temp;
            sides[0].invert();
            sides[1].invert();
            sides[2].invert();
            sides[3].invert();
            sides[4].invert();
            sides[5].invert();
            sides[5].rotate90clockwise(); //rotate the top
            sides[5].rotate90clockwise();
            sides[4].rotate90clockwise(); //is this right? Yes, PrpExplorer displays it correctly, along with original Pots Ages.
            sides[4].rotate90clockwise();
        }*/
    }
    public void invert()
    {
        x0004MipMap temp = sides[0];
        sides[0] = sides[1];
        sides[1] = temp;
        temp = sides[2];
        sides[2] = sides[3];
        sides[3] = temp;
        temp = sides[4];
        sides[4] = sides[5];
        sides[5] = temp;
        sides[0].invert();
        sides[1].invert();
        sides[2].invert();
        sides[3].invert();
        sides[4].invert();
        sides[5].invert();
        sides[5].rotate90clockwise(); //rotate the top
        sides[5].rotate90clockwise();
        sides[4].rotate90clockwise(); //is this right? Yes, PrpExplorer displays it correctly, along with original Pots Ages.
        sides[4].rotate90clockwise();
    }
    public void compile(Bytedeque deque)
    {
        parent.compile(deque);
        for(int i=0;i<6;i++)
        {
            sides[i].compile(deque);
        }
    }
}

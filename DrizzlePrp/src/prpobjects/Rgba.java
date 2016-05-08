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

import uru.Bytestream;
import uru.Bytedeque;
import shared.Flt;
import uru.context;
import shared.*;

/**
 *
 * @author user
 */
public class Rgba extends uruobj
{
    //int r;
    //int g;
    //int b;
    //int a;
    public Flt r;
    public Flt g;
    public Flt b;
    public Flt a;
    
    public Rgba(context c)
    {
        //r = data.readInt(); //red //float from 0.0 to 1.0
        //g = data.readInt(); //green //float from 0.0 to 1.0
        //b = data.readInt(); //blue //float from 0.0 to 1.0
        //a = data.readInt(); //alpha //float from 0.0 to 1.0
        r = new Flt(c);
        g = new Flt(c);
        b = new Flt(c);
        a = new Flt(c);
    }
    /*public Rgba(int r, int g, int b, int a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }*/
    public Rgba(Flt r, Flt g, Flt b, Flt a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public void compile(Bytedeque deque)
    {
        //deque.writeInt(r);
        //deque.writeInt(g);
        //deque.writeInt(b);
        //deque.writeInt(a);
        r.compile(deque);
        g.compile(deque);
        b.compile(deque);
        a.compile(deque);
    }

    public String toString()
    {
        /*return Flt.toString(r)
                +":"+Flt.toString(g)
                +":"+Flt.toString(b)
                +":"+Flt.toString(a);*/
        return r.toString()
                +":"+g.toString()
                +":"+b.toString()
                +":"+a.toString();
    }
    private Rgba(){}
    public Rgba(Rgba d)
    {
        this.a = d.a.deepClone();
        this.b = d.b.deepClone();
        this.g = d.g.deepClone();
        this.r = d.r.deepClone();
    }
    public static Rgba createFromRGBA(RGBA data)
    {
        Rgba r = new Rgba();
        r.r = Flt.createFromJavaFloat(data.r);
        r.g = Flt.createFromJavaFloat(data.g);
        r.b = Flt.createFromJavaFloat(data.b);
        r.a = Flt.createFromJavaFloat(data.a);
        return r;
    }
    public static Rgba createFromVals(float r, float g, float b, float a)
    {
        Rgba r2 = new Rgba();
        r2.r = Flt.createFromJavaFloat(r);
        r2.g = Flt.createFromJavaFloat(g);
        r2.b = Flt.createFromJavaFloat(b);
        r2.a = Flt.createFromJavaFloat(a);
        return r2;
    }
    public Rgba deepClone()
    {
        return new Rgba(this);
    }
}

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

import uru.context;
import uru.Bytedeque;
import shared.readexception;
import shared.e;

public class EmbeddedClasses
{
    public static class PlSoundVolumeApplicator extends uruobj
    {
        byte u1;
        Urustring u2;
        int u3;
        
        public PlSoundVolumeApplicator(context c) throws readexception
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
            u3 = c.readInt();
        }
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
            c.writeInt(u3);
        }
    }
    public static class PlOmniSqApplicator extends uruobj
    {
        byte u1;
        Urustring u2;
        
        public PlOmniSqApplicator(context c) throws readexception
        {
            e.ensure(c.readversion==6);
            
            u1 = c.readByte();
            u2 = new Urustring(c);

            throw new shared.readwarningexception("plOmniSqApplicator: can read okay, but failing in order to ignore.");
        }
    }
    public static class PlLightSpecularApplicator extends uruobj
    {
        byte u1;
        Urustring u2;
        
        public PlLightSpecularApplicator(context c)
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
        }
    }
    public static class PlLightDiffuseApplicator extends uruobj
    {
        byte u1;
        Urustring u2;
        
        public PlLightDiffuseApplicator(context c)
        {
            u1 = c.readByte();
            u2 = new Urustring(c);
        }
        
        public void compile(Bytedeque c)
        {
            c.writeByte(u1);
            u2.compile(c);
        }
    }
    
    public static class PlPointControllerChannel extends uruobj
    {
        public PlMatrixChannel matrixchannel;
        public PrpTaggedObject emb;
        
        public PlPointControllerChannel(context c) throws readexception
        {
            matrixchannel = new PlMatrixChannel(c);
            emb = new PrpTaggedObject(c);
        }
        
        public void compile(Bytedeque c)
        {
            matrixchannel.compile(c);
            emb.compile(c);
        }
    }
    
    public static class PlMatrixChannel extends uruobj
    {
        Urustring u1;
        
        public PlMatrixChannel(context c)
        {
            u1 = new Urustring(c);
        }
        
        public void compile(Bytedeque c)
        {
            u1.compile(c);
        }
    }
}

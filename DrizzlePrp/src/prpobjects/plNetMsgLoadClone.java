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


public class plNetMsgLoadClone extends uruobj
{

    public static final int kCompressionNone = 0;
    public static final int kCompressionFailed = 1;
    public static final int kCompressionZlib = 2;
    public static final int kCompressionDont = 3;

    public plNetMsgGameMessage parent;
    public Uruobjectdesc desc;
    public byte isPlayer;
    public byte isLoading;
    public byte isInitialState;

    //sub6b6b60
    public plNetMsgLoadClone(context c) throws readexception
    {
        parent = new plNetMsgGameMessage(c);
        desc = new Uruobjectdesc(c);
        isPlayer = c.readByte();
        isLoading = c.readByte();
        isInitialState = c.readByte();
    }
    
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        desc.compile(c);
        c.writeByte(isPlayer);
        c.writeByte(isLoading);
        c.writeByte(isInitialState);
    }

    private plNetMsgLoadClone(){}
    public static plNetMsgLoadClone createWithInfo(int playerIdx, plLoadCloneMsg clonemsg, boolean isInitialState)
    {
        plNetMsgLoadClone r = new plNetMsgLoadClone();

        r.parent = plNetMsgGameMessage.createWithObjPlayeridx(PrpTaggedObject.create(clonemsg), playerIdx);
        r.desc = clonemsg.cloneKey.xdesc; //is this right?  I think so.
        r.isLoading = clonemsg.isLoading;
        r.isInitialState = isInitialState?(byte)1:(byte)0;
        r.isPlayer = 0; //false
        if(clonemsg instanceof plLoadAvatarMsg)
        {
            plLoadAvatarMsg clonemsg2 = (plLoadAvatarMsg)clonemsg;
            r.isPlayer = clonemsg2.isPlayer;
        }
        
        return r;
    }

    public plLoadCloneMsg tryToGetLoadCloneMsg()
    {
        uruobj obj = this.parent.parent.obj.prpobject.object;
        if(obj instanceof plLoadCloneMsg) return (plLoadCloneMsg)obj;
        return null;
    }


    public static class plNetMsgStreamHelper
    {

        int uncompressedSize;
        byte compressionType;
        int streamLength;

        Typeid type;
        byte[] data;
        byte[] compressedData;
        byte[] uncompressedData;

        //PrpObject obj;
        uruobj obj;

        public <T extends uruobj>  plNetMsgStreamHelper(context c, Class<T> klassToRead, boolean parse/*, int uncompressedOffset*/) throws readexception
        {
            uncompressedSize = c.readInt();
            compressionType = c.readByte();
            streamLength = c.readInt();
            //compressedData = c.readBytes(streamLength);

            //the stream looks like this:
            //if uncompressed: either the typeid then the object, or just the object
            //if compressed: an uncompressed typeid, then the compressed object.  (if not a PrpTaggedObject, the typeid=0x8000
            //oops, no, I guess I thought that because 0x8000 is a common flag for SdlBinary :P
            //so if compressed: the first two bytes are uncompressed, then the rest of the object is compressed.

            //type = Typeid.ReadEvenIfUnknown(c);
            //compressedData = c.readBytes(streamLength-2); //we've already read two bytes for the typeid.
            if(compressionType!=kCompressionNone && compressionType!=kCompressionDont && compressionType!=kCompressionZlib)
            {
                m.warn("untested");
            }
            if(compressionType==kCompressionZlib)
            {
                //untested

                //old
                /*type = Typeid.ReadEvenIfUnknown(c); //uncompressed
                //compressedData = c.readBytes(streamLength);
                //java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                //out.write(compressedData,0,uncompressedOffset); //write the first two bytes
                compressedData = c.readBytes(streamLength-2);
                //uncompressedData = shared.zip.decompressZlib(compressedData, uncompressedOffset, compressedData.length-uncompressedOffset,out); //skip the first two bytes
                uncompressedData = shared.zip.decompressZlib(compressedData);
                e.ensure(uncompressedData.length==uncompressedSize-2);*/

                //new
                final int uncompressedOffset = 2;
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                compressedData = c.readBytes(streamLength);
                out.write(compressedData, 0, uncompressedOffset);
                uncompressedData = shared.zip.decompressZlib(compressedData, uncompressedOffset, compressedData.length-uncompressedOffset, out); //skip the first two bytes

                if(!parse)
                {
                    data = uncompressedData;
                }
                else
                {
                    try{
                        IBytestream c2 = shared.ByteArrayBytestream.createFromByteArray(uncompressedData);
                        context c2c = context.createFromBytestream(c2);
                        c2c.readversion = 6;
                        obj = c2c.readObj(klassToRead);
                    }catch(Exception e){
                        throw new shared.nested(e);
                    }
                }

                //new
                //shared.zip.d
                //uncompressedData = shared.zip.decompressZlib(compressedData);
                //e.ensure(uncompressedData.length==uncompressedSize-2); //should the -2 be here?
            }
            else
            {
                uncompressedData = compressedData;

                //this assumes no compression, so fix that sometime.
                if(!parse)
                {
                    data = c.readBytes(streamLength);
                }
                else
                {
                    //T object = c.readObj(klassToRead);
                    //obj = object;
                    try{
                        obj = c.readObj(klassToRead);
                    }catch(Exception e){
                        throw new shared.nested(e);
                    }
                }
            }


            //if(parse) parse(c.readversion);
        }

        public void parse(int readversion) throws readexception
        {
            shared.ByteArrayBytestream bs = shared.ByteArrayBytestream.createFromByteArray(uncompressedData);
            uru.context bsc = uru.context.createFromBytestream(bs);
            bsc.readversion = readversion;
            //PrpTaggedObject obj2 = new PrpTaggedObject(bsc);
            obj = new PrpObject(bsc,type);
            if(bsc.in.getBytesRemaining()!=0)
                m.throwUncaughtException("did not read all");
        }
        public plNetMsgStreamHelper(){}
        public static plNetMsgStreamHelper createFromObj(Bytedeque c, uruobj obj)
        {
            //compile obj first
            byte[] objbs = obj.compileAlone(c.format);

            //create plNetMsgStreamHelper
            plNetMsgStreamHelper r = new plNetMsgStreamHelper();
            r.uncompressedSize = 0;
            r.compressionType = 0;
            r.streamLength = objbs.length;
            r.data = objbs;
            return r;
        }
        public void compile(Bytedeque c)
        {
            c.writeInt(uncompressedSize);
            c.writeByte(compressionType);
            c.writeInt(streamLength);
            if(compressionType!=0) m.throwUncaughtException("unhandled plNetMsgStreamHelper compile.");
            c.writeBytes(data);
        }
    }

}

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

package uru;

import shared.FileUtils;
//import gui.Main;
import java.util.Arrays;
import java.util.Vector;
import java.lang.Byte;
import shared.b;
import shared.m;
import shared.Bytes;
import shared.*;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;
//import org.bouncycastle.crypto.engines.XTEAEngine;

/**
 *
 * @author user
 */

public class UruCrypt {
    
    private static byte[] whatdoyouseeHeader = { 'w','h','a','t','d','o','y','o','u','s','e','e' };
    private static byte[] notthedroidsHeader = { 'n','o','t','t','h','e','d','r','o','i','d','s' };
    private static byte[] eoaHeader = {(byte)0x88,(byte)0x42,(byte)0x87,(byte)0x0D};
    private static byte[] briceissmartHeader = { 'B','r','i','c','e','I','s','S','m','a','r','t' };

    private static final int[] notthedroidsKey = {0x9a17342d,0xe40bc816,0x7b2ef65d,0xaa9d5539}; //moul
    //private static final int[] notthedroidsKey = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};
    //private static final int[] notthedroidsKey = {0x16db7fc2,0x3a170b92,0x03827d0f,0x6c0a5452};

    //private static final int[] notthedroidsKeyMqo = {0x75b2eb65,0x66c2cd85,0x5fc6bf8d,0x2e7a5cf4}; //mqo
    //private static final int[] notthedroidsKeyMqo =   {0xdb776285,0x0f775e2a,0x0aff40e0,0x3a2d830f}; //mqo
    private static final int[] notthedroidsKeyMqo = {0x0cb41968,0xc3e387c6,0x0eec1dd8,0x466c8cd8}; //mqo
    //private static final int[] notthedroidsKey = {0x2e7a5cf4,0x5fc6bf8d,0x66c2cd85,0x75b2eb65,};
    //private static final int[] notthedroidsKey = {0xf45c7a2e,0x8dbfc65f,0x85cdc266,0x65ebb275,};
    //private static final int[] notthedroidsKey =   {0x65ebb275,0x85cdc266,0x8dbfc65f,0xf45c7a2e,};

    public static byte[] DecryptAny(String filename, auto.AllGames.GameInfo game)
    {
        byte[] encdata = FileUtils.ReadFile(filename);
        return DecryptAny(encdata,game);
    }
    public static byte[] DecryptAny(byte[] encdata, auto.AllGames.GameInfo game)
    {
        UruFileTypes type = DetectType(encdata,game);
        return DecryptAny(encdata,type);
    }
    public static byte[] EncryptAny(byte[] unencdata, Format format)
    {
        if(format==Format.pots)
        {
            return UruCrypt.EncryptWhatdoyousee(unencdata);
        }
        else
        {
            throw new shared.uncaughtexception("unimplemented");
        }
    }
    public static byte[] DecryptAny(byte[] encdata, Format format)
    {
        if(format==Format.pots)
        {
            return UruCrypt.DecryptWhatdoyousee(encdata);
        }
        else
        {
            throw new shared.uncaughtexception("unimplemented");
        }
    }
    public static byte[] DecryptAny(byte[] encdata, UruFileTypes type)
    {
        /*if(b.startswith(encdata, whatdoyouseeHeader)) return DecryptWhatdoyousee(encdata);
        else if(b.startswith(encdata, notthedroidsHeader))
        {
            if(game.game==auto.Game.mqo) return DecryptNotthedroids(encdata,true); ///mqo
            else return DecryptNotthedroids(encdata,false); //moul
        }
        else if(b.startswith(encdata, eoaHeader)) return DecryptEoa(encdata);
        else
        {
            //m.throwUncaughtException("Unknown encryption type.");
            m.msg("Unknown encryption type; assuming it is unencrypted.");
            return encdata;
        }
        //return null;*/

        switch(type)
        {
            case whatdoyousee:
                return DecryptWhatdoyousee(encdata);
            case notthedroids:
                return DecryptNotthedroids(encdata,false);
            case notthedroids_mqo:
                return DecryptNotthedroids(encdata,true);
            case eoaenc:
                return DecryptEoa(encdata);
            case unencrypted:
                return encdata;
            case unknown:
                m.msg("Unknown encryption type; assuming it is unencrypted.");
                return encdata;
            default:
                throw new shared.uncaughtexception("unexpected");
        }
    }
    public static UruFileTypes DetectType(byte[] encdata, auto.AllGames.GameInfo game)
    {
        if(b.startswith(encdata, whatdoyouseeHeader)) return UruFileTypes.whatdoyousee;
        else if(b.startswith(encdata, notthedroidsHeader))
        {
            if(game.game==auto.Game.mqo) return UruFileTypes.notthedroids_mqo; //mqo
            else return UruFileTypes.notthedroids; //moul
        }
        else if(b.startswith(encdata, eoaHeader)) return UruFileTypes.eoaenc;
        else
        {
            //return UruFileTypes.unknown;

            //m.msg("Unknown encryption type; assuming it is unencrypted."); //let's not annoy the user
            return UruFileTypes.unencrypted;
        }
        //return null;
    }
    public static UruFileTypes DetectType(String filename)
    {
        String filestring = FileUtils.ReadFileAsString(filename);
        //String filestring = new String(filecontents);
        
        if(filestring.startsWith(new String(whatdoyouseeHeader))) return UruFileTypes.whatdoyousee;
        if(filestring.startsWith(new String(notthedroidsHeader))) return UruFileTypes.notthedroids;
        if(filestring.startsWith(new String(eoaHeader))) return UruFileTypes.eoaenc;
        if(filestring.startsWith(new String(briceissmartHeader)))
        {
            m.warn("briceissmart encountered(it needs to be implemented):",filename);
            return UruFileTypes.briceissmart;
        }
        
        return UruFileTypes.unknown;
    }
    
    //interprets LF bytes as line separators
    public static byte[] EncryptElf(byte[] input)
    {
        Vector<Vector<Byte>> lines = new Vector<Vector<Byte>>();
        
        //split up input into lines.
        Vector<Byte> curline = new Vector<Byte>();
        for(int i=0; i<input.length; i++)
        {
            
            if(input[i]==0x0A)
            {
                //curline.add(input[i]); //cut out the LF
                lines.add(curline);
                curline = new Vector<Byte>();
            }
            else if(input[i]==0x0D)
            {
                //curline.add(input[i]); //cut out the CR
            }
            else
            {
                curline.add(input[i]);
            }
        }
        //add the last line, even if it doesn't have a LF at the end.
        if(curline.size()!=0)
        {
            lines.add(curline);
        }
        
        Vector<Byte> results = new Vector<Byte>();
        int file_pos = 0;
        
        //add each line
        for(int i=0; i<lines.size(); i++)
        {
            curline = lines.get(i);
            int seg_size = curline.size();
            
            //calculate and store head.
            byte key = (byte)(file_pos & 0xff);
            int seg_head32 = seg_size ^ b.ByteToInt32(key);
            short seg_head = (short)seg_head32;
            byte[] seg_head_bytes = b.Int16ToBytes(seg_head);
            results.add(seg_head_bytes[0]);
            results.add(seg_head_bytes[1]);
            
            //encrypt block
            byte[] inblock = new byte[seg_size];
            byte[] outblock = new byte[seg_size];
            for(int j=0; j<seg_size; j++)
            {
                inblock[j] = curline.get(j);
            }
            EncryptElfBlock(inblock, outblock, key);
            
            //write outblock to result
            for(int j=0; j<seg_size; j++)
            {
                results.add(outblock[j]);
            }
            
            //move file cursor forward.
            file_pos += 2 + seg_size;
        }
        
        
        //create output bytes
        Byte[] results2 = new Byte[results.size()];
        results.toArray(results2);
        byte[] result = new byte[results.size()];
        for(int i=0;i<result.length;i++)
        {
            result[i] = results2[i];
        }
        return result;
    }
    
    public static void EncryptElfBlock(byte[] v, byte[] encoded, byte key)
    {
        int len = v.length;
        byte a, c, d;
        
        c = v[len-1];
        c = b.and(c, 0xFC);
        c = b.shl(c, 3);

        for(int i=0; i<len; i++)
        {
            d = v[i];
            a = d;
            a = b.shl(a, 6);
            d = b.shr(d, 2);
            a = b.or(a, d);
            d = a;
            a = b.shr(a, 3);
            a = b.or(a, c);
            c = key;
            a = b.or(a, c);
            d = b.shl(d, 5);
            encoded[i] = a;
            c = d;
            
            /*
            d = (v[i] << 6) | (v[i] >> 2);
            a = (d >> 3) | c;
            v[i] = a ^ key;
            c = d << 5;
            */
        }
    }

    //seperates lines with LF
    public static byte[] DecryptElf(byte[] input)
    {
        //byte[][] results;
        Vector<Byte> results = new Vector<Byte>();
        
        int file_pos = 0;
        int length = input.length;
        for(int i=0; true; i++)
        {
            if(file_pos+2 > length) break;
            int seg_head = b.Int16ToInt32(b.BytesToInt16(input,file_pos));
            byte key = (byte)(file_pos & 0xff);
            int seg_size = seg_head ^ b.ByteToInt32(key);
            file_pos += 2;

            if(file_pos+seg_size > length) break;
            byte[] block = Arrays.copyOfRange(input, file_pos, file_pos+seg_size);
            file_pos += seg_size;
            
            //decrypt
            byte[] outblock = new byte[block.length];
            DecryptElfBlock(block, outblock, key);
            
            //copy outblock.
            for(int j=0;j<outblock.length;j++)
            {
                results.add(outblock[j]);
            }
            results.add((byte)0x0A); //line-feed
            //results.add((byte)0x0D); //carriage-return
            
        }
        
        //create output bytes
        Byte[] results2 = new Byte[results.size()];
        results.toArray(results2);
        byte[] result = new byte[results.size()];
        for(int i=0;i<result.length;i++)
        {
            result[i] = results2[i];
        }
        return result;
    }
    public static void DecryptElfBlock(byte[] v, byte[] unencoded, byte key)
    {
        int length = v.length;
        byte a, c, d;

        /*byte v8;
        int loopCounter = length - 1;
        for ( byte i = b.shr(b.xor(key, v[0]), 5); loopCounter >= 0; i = b.shr(v8, 5))
        {
            v8 = b.xor(key, v[loopCounter]);
            //v[loopCounter] = or(b.shr(or(i, b.shl(b.xor(key, v[loopCounter]), 3), 6), (4 * or(i, (8 * b.xor(key, v[loopCounter])))));
            //v[loopCounter] = ((i | (8 * (key ^ v[loopCounter]))) >> 6) | (4 * (i | (8 * (key ^ v[loopCounter]))));
            v[loopCounter] = or(b.shr(or(i, b.shl(b.xor(key, v[loopCounter]), 3)), 6), b.shl(or(i, b.shl(b.xor(key, v[loopCounter]), 3)), 2));
            --loopCounter;
        }*/

        //b = key;
        d = v[0];
        d = b.xor(d, key);
        d = b.shr(d, 5);
        
        for (int i = length-1; i >= 0; i--)
        {
            a = v[i];
            a = b.or(a, key);
            c = a;
            a = b.shl(a, 3);
            a = b.or(a, d);
            d = a;
            d = b.shr(d, 6);
            a = b.shl(a, 2);
            d = b.or(d, a);
            c = b.shr(c, 5);
            unencoded[i] = d;
            d = c;
        }
    }
    
    public static byte[] EncryptWhatdoyousee(byte[] data)
    {
        /*byte[] ke2 = {(byte)0x52,(byte)0x54,(byte)0x0A,(byte)0x6C,
                      (byte)0x0F,(byte)0x7D,(byte)0x82,(byte)0x03,
                      (byte)0x92,(byte)0x0B,(byte)0x17,(byte)0x3A,
                      (byte)0xC2,(byte)0x7F,(byte)0xDB,(byte)0x16};  //key*/
        /*byte[] key = {(byte)0x6C,(byte)0x0A,(byte)0x54,(byte)0x52,
                      (byte)0x03,(byte)0x82,(byte)0x7D,(byte)0x0F,
                      (byte)0x3A,(byte)0x17,(byte)0x0B,(byte)0x92,
                      (byte)0x16,(byte)0xDB,(byte)0x7F,(byte)0xC2};  //key*/
        int blockLength = 8; //crypto's bytes per block.
        int prefixSize = 12 + 4; //num bytes used with header, length, etc.

        int length = data.length;
        int numblocks = length / blockLength;
        if (numblocks*blockLength < length) numblocks++; //take care of partial block.
        byte[] result = new byte[numblocks*blockLength+prefixSize];

        //header
        b.CopyBytes(whatdoyouseeHeader,result,0);
        
        //length
        byte[] l = b.Int32ToBytes(length);
        b.CopyBytes(l,result,12);
        
        //iterate through blocks
        byte[] encodedblock = new byte[blockLength];
        byte[] unencodedblock = new byte[blockLength];
        //XTEAEngine crypto = new XTEAEngine();
        //crypto.init(true, new KeyParameter(key));
        for(int i = 0; i<length; i+= blockLength)
        {
            int remainingbytes = length-i;
            if (remainingbytes > blockLength) remainingbytes = blockLength;
            b.CopyBytes(data,i,unencodedblock,0,remainingbytes);
            for(int j=remainingbytes;j<blockLength;j++)
            {
                unencodedblock[j] = 0x00; //supposed to be random, but we don't care, since we don't chain cipher anyway(we just ECB).
            }
            //crypto.processBlock(unencodedblock, 0, encodedblock, 0); //encrypt block.
            EncryptWhatdoyouseeBlock(unencodedblock, encodedblock);
            b.CopyBytes(encodedblock,result,prefixSize+i);
        }
        
        return result;
        
    }
    public static Bytes EncryptWhatdoyousee(Bytes data)
    {
        return new Bytes(EncryptWhatdoyousee(data.getByteArray()));
    }
    private static void EncryptWhatdoyouseeBlock(byte[] unencodedblock, byte[] encodedblock)
    {
        int[] k = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};  //key

        int v0 = b.BytesToInt32(unencodedblock,0);
        int v1 = b.BytesToInt32(unencodedblock,4);
        
        int sum = 0;
        int delta = 0x9E3779B9;
        for(int i=0; i<32; i++)
        {
            v0 += (((v1 << 4)^(v1 >>> 5)) + v1) ^ (sum + k[sum & 3]);
            sum += delta;
            v1 += (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + k[(sum >>> 11) & 3]);
        }
        
        b.loadInt32IntoBytes(v0, encodedblock, 0);
        b.loadInt32IntoBytes(v1, encodedblock, 4);
        
    }
    
    public static byte[] EncryptEoa(byte[] data)
    {
        byte[] key = {(byte)0xF0,(byte)0x4D,(byte)0x25,(byte)0x33,(byte)0xAC,(byte)0x5D,(byte)0x27,(byte)0x5A,(byte)0x9E,(byte)0x18,(byte)0x78,(byte)0x3E,(byte)0x65,(byte)0x2C,(byte)0x48,(byte)0x08};

        int length = data.length;
        int numblocks = length / 16;
        if (numblocks*16 < length) numblocks++; //take care of partial block.
        byte[] result = new byte[numblocks*16+4+4];

        //header
        b.CopyBytes(eoaHeader,result,0);
        
        //length
        byte[] l = b.Int32ToBytes(length);
        b.CopyBytes(l,result,4);
        
        //iterate through blocks
        byte[] encodedblock = new byte[16];
        byte[] unencodedblock = new byte[16];
        AESEngine aes = new AESEngine();
        aes.init(true, new KeyParameter(key));
        for(int i = 0; i<length; i+= 16)
        {
            int remainingbytes = length-i;
            if (remainingbytes > 16) remainingbytes = 16;
            b.CopyBytes(data,i,unencodedblock,0,remainingbytes);
            for(int j=remainingbytes;j<16;j++)
            {
                unencodedblock[j] = 0x00; //supposed to be random, but we don't care, since we don't chain cipher anyway(we just ECB).
            }
            aes.processBlock(unencodedblock, 0, encodedblock, 0); //encrypt block.
            b.CopyBytes(encodedblock,result,8+i);
        }
        
        return result;
    }
    public static byte[] DecryptEoa(byte[] encryptedData)
    {
        //byte[] key = {240, 77, 37, 51, 172, 93, 39, 90, 158, 24, 120, 62, 101, 44, 72, 8};
        byte[] key = {(byte)0xF0,(byte)0x4D,(byte)0x25,(byte)0x33,(byte)0xAC,(byte)0x5D,(byte)0x27,(byte)0x5A,(byte)0x9E,(byte)0x18,(byte)0x78,(byte)0x3E,(byte)0x65,(byte)0x2C,(byte)0x48,(byte)0x08};
        
        byte[] header = Arrays.copyOfRange(encryptedData, 0, 4); //should be header.
        byte[] lbytes = Arrays.copyOfRange(encryptedData, 4, 8);
        int length = b.BytesToInt32(lbytes,0);
        
        byte[] result = new byte[length];
        KeyParameter keyparam = new KeyParameter(key);
        AESEngine aes = new AESEngine();
        aes.init(false, keyparam);
        
        byte[] decrypted = new byte[16];
        for(int i=0;i<length;i+=16)
        {
            aes.processBlock(encryptedData, i+8, decrypted, 0);
            int remaining = length - i;
            if (remaining > 16) remaining = 16;
            b.CopyBytes(decrypted,0,result,i,remaining);
        }
        
        return result;
    }
    public static Bytes DecryptEoa(Bytes encryptedData)
    {
        return new Bytes(DecryptEoa(encryptedData.getByteArray()));
    }
    
    public static byte[] DecryptEoastring(byte[] eoastring)
    {
        byte[] key = { 109, 121, 115, 116, 110, 101, 114, 100 }; //ascii for "mystnerd"
        short len = b.BytesToInt16(eoastring,0);
        byte[] result = new byte[len];
        for(int i=0;i<len;i++)
        {
            result[i] = (byte)(eoastring[i+2] ^ key[i%8]);
        }
        return result;
    }
    
    //only handles string under 2^16 bytes long.
    public static byte[] EncryptEoastring(byte[] string)
    {
        byte[] key = { 109, 121, 115, 116, 110, 101, 114, 100 }; //ascii for "mystnerd"
        
        short len = (short)string.length;
        byte[] result = new byte[len+2];
        byte[] lenbytes = b.Int16ToBytes(len);
        b.CopyBytes(lenbytes,result,0);
        for(int i=0;i<len;i++)
        {
            result[i+2] = (byte)(string[i] ^ key[i%8]);
        }
        return result;
    }

    public static byte[] EncryptNotthedroids(byte[] unencrypted)
    {
        return EncryptNotthedroids(unencrypted,UruCrypt.notthedroidsKey);
    }
    public static byte[] EncryptNotthedroids(byte[] unencrypted, int[] key)
    {
        int length = unencrypted.length;
        int numblocks = length / 8;
        if (numblocks*8 < length) numblocks++; //take care of partial block.
        byte[] result = new byte[numblocks*8+12+4];

        //header
        b.CopyBytes(notthedroidsHeader,result,0);
        
        //length
        byte[] l = b.Int32ToBytes(length);
        b.CopyBytes(l,result,12);
        
        //iterate through blocks
        byte[] encodedblock = new byte[8];
        byte[] unencodedblock = new byte[8];
        for(int i = 0; i<length; i+= 8)
        {
            int remainingbytes = length-i;
            if (remainingbytes > 8) remainingbytes = 8;
            b.CopyBytes(unencrypted,i,unencodedblock,0,remainingbytes);
            for(int j=remainingbytes;j<8;j++)
            {
                unencodedblock[j] = 0x00; //supposed to be random, but we don't care, since we don't chain cipher anyway.
            }
            EncryptNotthedroidsBlock(unencodedblock, encodedblock, key);
            b.CopyBytes(encodedblock,result,16+i);
        }
        
        return result;
    }
    public static void EncryptNotthedroidsBlock(byte[] unencodedblock, byte[] encodedblock, int[] k)
    {
        int v0 = b.BytesToInt32(unencodedblock, 0);
        int v1 = b.BytesToInt32(unencodedblock, 4);
        
        //int[] k = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};  //key
        //int[] k = {0x9a17342d,0xe40bc816,0x7b2ef65d,0xaa9d5539};  //key
        //int[] k = notthedroidsKey;
        int sum = 0;
        int e;
        int DELTA = 0x9e3779b9;
        int y = v0;
        int z = v1;
        int q = 6+(52/2); //=32
        int p;
        while(q-- > 0)
        {
            sum += DELTA;
            e = (sum >>> 2) & 3;
            
            p = 0;
            y = v1;
            v0 += ( (((v1>>>5)^(y<<2))+((y>>>3)^(v1<<4)))^((sum^y)+(k[(p&3)^e]^v1)) );
            z = v0;
            
            p = 1;
            y = v0;
            v1 += ( (((v0>>>5)^(y<<2))+((y>>>3)^(v0<<4)))^((sum^y)+(k[(p&3)^e]^v0)) );
            z = v1;
            
        }
        
        
        b.loadInt32IntoBytes(v0, encodedblock, 0);
        b.loadInt32IntoBytes(v1, encodedblock, 4);
        
    }
    public static byte[] DecryptNotthedroids(byte[] filecontents)
    {
        return DecryptNotthedroids(filecontents,false);
    }
    public static byte[] DecryptNotthedroids(byte[] filecontents, boolean isMqo)
    {
        int[] key = isMqo?UruCrypt.notthedroidsKeyMqo:UruCrypt.notthedroidsKey;
        return DecryptNotthedroids(filecontents,key);
    }
    public static byte[] DecryptNotthedroids(byte[] filecontents, int[] key)
    {
        //int[] key = isMqo?UruCrypt.notthedroidsKeyMqo:UruCrypt.notthedroidsKey;

        int filelength = filecontents.length;
        byte[] header = Arrays.copyOfRange(filecontents, 0, 12);
        byte[] lbytes = Arrays.copyOfRange(filecontents, 12, 16);
        //int innerlength = (lbytes[0]<<0) | (lbytes[1]<<8) | (lbytes[2]<<16) | (lbytes[3]<<24);
        int innerlength = b.BytesToInt32(lbytes,0);
        if(shared.State.AllStates.getStateAsBoolean("reportDecryption"))
        {
            m.msg("decrypting notthedroids...");
            m.msg("header:",new String(header));
            m.msg("payload length:",Integer.toString(innerlength));
        }
        
        byte[] result = new byte[innerlength];
        byte[] decodedBlock = new byte[8];
        for( int i = 0; i < innerlength; i+=8 ) //for each block of 8
        {
            DecodeNotthedroidsBlock(filecontents,decodedBlock,i+16,0,key);
            int remainingbytes = innerlength-i;

            if(remainingbytes > 8) remainingbytes = 8;
            //DecodeWhatdoyouseeBlock(filecontents,result,i+16,i);
            for(int j=0;j<remainingbytes;j++)
            {
                result[i+j] = decodedBlock[j];
            }
        }
        //int i =4;
        return result;
        
    }
    public static byte[] DecryptWhatdoyousee(byte[] filecontents)
    {
        int filelength = filecontents.length;
        byte[] header = Arrays.copyOfRange(filecontents, 0, 12);
        byte[] lbytes = Arrays.copyOfRange(filecontents, 12, 16);
        //int innerlength = (lbytes[0]<<0) | (lbytes[1]<<8) | (lbytes[2]<<16) | (lbytes[3]<<24);
        int innerlength = b.BytesToInt32(lbytes,0);
        if(shared.State.AllStates.getStateAsBoolean("reportDecryption"))
        {
            m.msg("decrypting whatdoyousee...");
            m.msg("header:",new String(header));
            m.msg("payload length:",Integer.toString(innerlength));
        }
        
        byte[] result = new byte[innerlength];
        byte[] decodedBlock = new byte[8];
        for( int i = 0; i < innerlength; i+=8 ) //for each block of 8
        {
            DecodeWhatdoyouseeBlock(filecontents,decodedBlock,i+16,0);
            int remainingbytes = innerlength-i;

            if(remainingbytes > 8) remainingbytes = 8;
            //DecodeWhatdoyouseeBlock(filecontents,result,i+16,i);
            for(int j=0;j<remainingbytes;j++)
            {
                result[i+j] = decodedBlock[j];
            }
        }
        //int i =4;
        return result;
    }

    private static void DecodeNotthedroidsBlock(byte[] input, byte[] output, int inputpos, int outputpos, int[] key)
    {
        //XXTEA
        //#define MX  ( (((z>>5)^(y<<2))+((y>>3)^(z<<4)))^((sum^y)+(k[(p&3)^e]^z)) )
        //long btea(long* v, long n, long* k) {
        //    unsigned long z /* = v[n-1] */, y=v[0], sum=0, e, DELTA=0x9e3779b9;
        //    n = -n ;
        //    q = 6+52/n ;
        //    sum = q*DELTA ;
        //    while (sum != 0) {
        //        e = sum>>2 & 3;
        //        for (p=n-1; p>0; p--) z = v[p-1], y = v[p] -= MX;
        //        z = v[n-1];
        //        y = v[0] -= MX;
        //        sum -= DELTA;
        //    }
        //}
        //int n = 2; //uru only uses 2. Diff from standard: -2?
        //int[] k = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};  //key
        //int[] k = {0x9a17342d,0xe40bc816,0x7b2ef65d,0xaa9d5539};  //key
        //int[] k = {0x16db7fc2,0x3a170b92,0x03827d0f,0x6c0a5452};  //key
        //int[] k = notthedroidsKey;
        
        int v0 = b.BytesToInt32(input, inputpos);
        int v1 = b.BytesToInt32(input, inputpos+4);

        int z; // = loadBytesIntoInt32(input,inputpos+4*(n-1));
        int y = v0;
        int sum; //=0;
        int e;
        int p;
        int DELTA = 0x9e3779b9;
        //n = -n; //Diff from standard: shouldn't be commented out.
        //int q = 32; //integer division.
        sum = 32 * DELTA;
        while(sum != 0)
        {
            e = (sum >>> 2) & 3;

            //int MX = ( (((v0>>>5)^(y<<2))+((y>>>3)^(v0<<4)))^((sum^y)+(k[1^e]^v0)) );
            //y = v1 -= MX;
            p = 1;
            y = v1 - ( (((v0>>>5)^(y<<2))+((y>>>3)^(v0<<4)))^((sum^y)+(key[(p&3)^e]^v0)) );
            v1 = y;
            
            p = 0;
            y = v0 - ( (((v1>>>5)^(y<<2))+((y>>>3)^(v1<<4)))^((sum^y)+(key[(p&3)^e]^v1)) ); //p=0
            v0 = y;
            //y = v0 -= MX;
            
            sum -= DELTA;
        }
        
        //write out
        b.loadInt32IntoBytes(v0,output,outputpos);
        b.loadInt32IntoBytes(v1,output,outputpos+4);
        
    }
    private static void DecodeWhatdoyouseeBlock(byte[] input, byte[] output, int inputpos, int outputpos)
    {
        //XTEA
        //void decipher(unsigned int num_rounds, unsigned long* v, unsigned long* k) {
        //    unsigned long v0=v[0], v1=v[1], i;
        //    unsigned long delta=0x9E3779B9, sum=delta*num_rounds;
        //    for(i=0; i<num_rounds; i++) {
        //        v1 -= ((v0 << 4 ^ v0 >> 5) + v0) ^ (sum + k[sum>>11 & 3]);
        //        sum -= delta;
        //        v0 -= ((v1 << 4 ^ v1 >> 5) + v1) ^ (sum + k[sum & 3]);
        //    }
        //    v[0]=v0; v[1]=v1;
        //}
        int num_rounds = 32;  //uru only uses 32 rounds
        int[] k = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};  //key

        //int v0=input[inputpos];
        //int v1=input[inputpos+1];
        int v0 = b.BytesToInt32(input, inputpos);
        int v1 = b.BytesToInt32(input, inputpos+4);
        int delta=0x9E3779B9;
        int sum = delta*num_rounds;
        for(int i=0; i<num_rounds; i++)
        {
            v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + k[(sum >>> 11) & 3]);
            sum -= delta;
            v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + k[sum & 3]);
        }
        b.loadInt32IntoBytes(v0,output,outputpos);
        b.loadInt32IntoBytes(v1,output,outputpos+4*(1));
    }
    public static void EncryptUruMessageInPlace(byte[] string)//, int offset)
    {
        byte[] result = new byte[string.length];
        
        for(int i=0;i<string.length;i++)
        {
            int k = (i /*+ offset*/) % 8;
            int j = b.ByteToInt32(string[i]) << k;
            int l = b.ByteToInt32(string[i]) >>> (8 - k);
            //result[i] = (byte)(j | l);
            string[i] = (byte)(j | l);
        }
        
        //return result;
    }
    public static void DecryptUruMessageInPlace(byte[] string)//, int offset)
    {
        byte[] result = new byte[string.length];
        
        for(int i=0;i<string.length;i++)
        {
            int k = (i /*+ offset*/) % 8;
            int j = b.ByteToInt32(string[i]) >>> k;
            int l = b.ByteToInt32(string[i]) << (8 - k);
            //result[i] = (byte)(j | l);
            string[i] = (byte)(j | l);
        }
        
        //return result;
    }
    
    //allows for sizes under 2^12, but perhaps it should be 2^8.
    public static byte[] EncryptUrustring(byte[] string)
    {
        int actuallength = string.length;
        byte[] result = new byte[actuallength+2];
        
        //write header
        short startint = (short)(0xF000 | actuallength);
        byte[] startbytes = b.Int16ToBytes(startint);
        b.CopyBytes(startbytes,result,0);
        
        //encode bytes
        for(int i=0;i<actuallength;i++)
        {
            result[i+2] = (byte)~string[i];
        }
        
        return result;
    }
    
    //allows for sizes under 2^12, but perhaps it should be 2^8.
    public static byte[] DecryptUrustring(byte[] urustring)
    {
        int startpos;

        short lengthbytes = b.BytesToInt16(urustring,0);
        if ((lengthbytes & 0xF000)==0) startpos = 4; //skip 2 bytes, if first half-byte is set. These bytes are apparently irrelevant, anyway.
        else startpos = 2;
        int actuallength = lengthbytes & 0xFFF;
        if (actuallength > 255)
        {
            m.warn("urustring over 255 bytes:", new String(urustring));
        }
        byte[] result = new byte[actuallength];
        //if((actuallength > 0) && (urustring[startpos]>0x7F))
        if((actuallength > 0) && ((urustring[startpos] & 0x80) != 0))
        {
            //encrypted...
            for (int i = 0; i<actuallength; i++)
            {
                result[i] = (byte)~urustring[startpos+i];
            }
        }
        else
        {
            //unencrypted...
            for (int i = 0; i<actuallength; i++)
            {
                result[i] = urustring[startpos+i];
            }
        }
        return result;
    }
    
    /*private static void DecodeNotthedroidsBlock2(byte[] input, byte[] output, int inputpos, int outputpos)
    {
        //XXTEA
        //#define MX  ( (((z>>5)^(y<<2))+((y>>3)^(z<<4)))^((sum^y)+(k[(p&3)^e]^z)) )
        //long btea(long* v, long n, long* k) {
        //    unsigned long z , y=v[0], sum=0, e, DELTA=0x9e3779b9;
        //    n = -n ;
        //    q = 6+52/n ;
        //    sum = q*DELTA ;
        //    while (sum != 0) {
        //        e = sum>>2 & 3;
        //        for (p=n-1; p>0; p--) z = v[p-1], y = v[p] -= MX;
        //        z = v[n-1];
        //        y = v[0] -= MX;
        //        sum -= DELTA;
        //    }
        //}
        int n = 2; //uru only uses 2. Diff from standard: -2?
        int[] k = {0x6c0a5452,0x03827d0f,0x3a170b92,0x16db7fc2};  //key
        
        int z; // = loadBytesIntoInt32(input,inputpos+4*(n-1));
        int y = b.BytesToInt32(input, inputpos);
        int sum=0;
        int e;
        int DELTA = 0x9e3779b9;
        //n = -n; //Diff from standard: shouldn't be commented out.
        int q = 6 + ( 52 / n ); //integer division.
        sum = q * DELTA;
        while(sum != 0)
        {
            e = (sum >>> 2) & 3;
            for (int p=n-1; p>0; p--)
            {
                z = b.BytesToInt32(input, inputpos+4*(p-1));
                int MX = ( (((z>>>5)^(y<<2))+((y>>>3)^(z<<4)))^((sum^y)+(k[(p&3)^e]^z)) );
                int vp = b.BytesToInt32(input,inputpos+4*(p));
                b.loadInt32IntoBytes(vp - MX, input, inputpos+4*(p));
                y = vp - MX;
            }
            z = b.BytesToInt32(input, inputpos+4*(n-1));
            int MX = ( (((z>>>5)^(y<<2))+((y>>>3)^(z<<4)))^((sum^y)+(k[(0&3)^e]^z)) ); //p=0
            int vp = b.BytesToInt32(input, inputpos); //p=0
            b.loadInt32IntoBytes(vp - MX, input, inputpos); //p=0
            y = vp - MX;
            
            sum -= DELTA;
        }
        
        //write out
        int v0 = b.BytesToInt32(input, inputpos);
        int v1 = b.BytesToInt32(input, inputpos+4*(1));
        b.loadInt32IntoBytes(v0,output,outputpos);
        b.loadInt32IntoBytes(v1,output,outputpos+4*(1));
        
    }*/
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uru.server;

import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.Date;
import shared.m;
import shared.b;

public class Mfs
{
    int format;
    Vector<MfsEntry> base = new Vector();
    Vector<MfsEntry> pages = new Vector();
    Vector<MfsEntry> other = new Vector();

    private static SimpleDateFormat dateformatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");


    public Mfs(){}

    public static enum BlockType
    {
        version,base,pages,other,none,
    }

    public static class MfsEntry
    {
        String filename;
        int filesize;
        Date moddate;
        byte[] md5;
        int flag;
        int compressedSize;

        public MfsEntry(String line)
        {
            String[] parts = line.split(",");
            filename = parts[0];
            filesize = Integer.parseInt(parts[1]);
            try{
                moddate = dateformatter.parse(parts[2]);
            }catch(Exception e){
                m.err("Unable to parse date in Manifest file.");
            }
            md5 = b.HexStringToBytes(parts[3]);
            flag = Integer.parseInt(parts[4]);
            if(flag==8)
            {
                compressedSize = Integer.parseInt(parts[5]);
            }
        }

        public String generateLine()
        {
            StringBuilder s = new StringBuilder();
            s.append(filename);
            s.append(",");
            s.append(Integer.toString(filesize));
            s.append(",");
            s.append(dateformatter.format(moddate));
            s.append(",");
            s.append(b.BytesToHexString(md5));
            s.append(",");
            s.append(Integer.toString(flag));
            if(flag==8)
            {
                s.append(",");
                s.append(Integer.toString(compressedSize));
            }
            return s.toString();
        }
    }

    public static Mfs readFromString(String data)
    {
        Mfs result = new Mfs();
        String[] lines = data.split("\n");
        BlockType currentBlock = BlockType.none;
        for(String line: lines)
        {
            line = line.trim();
            if(line.equals("")) {}
            else if(line.equals("[version]")) currentBlock = BlockType.version;
            else if(line.equals("[base]")) currentBlock = BlockType.base;
            else if(line.equals("[pages]")) currentBlock = BlockType.pages;
            else if(line.equals("[other]")) currentBlock = BlockType.other;
            else
            {
                if(currentBlock==BlockType.version)
                {
                    if(line.startsWith("format="))
                    {
                        result.format = Integer.parseInt(line.substring("format=".length()));
                    }
                    else
                    {
                        m.warn("Unhandled line in mfs file: ",line);
                    }
                }
                else if(currentBlock==BlockType.base)
                {
                    result.base.add(new MfsEntry(line));
                }
                else if(currentBlock==BlockType.pages)
                {
                    result.pages.add(new MfsEntry(line));
                }
                else if(currentBlock==BlockType.other)
                {
                    result.other.add(new MfsEntry(line));
                }
                else
                {
                    m.warn("Unhandled line in mfs file: ",line);
                }
            }
        }
        return result;
    }
    
}

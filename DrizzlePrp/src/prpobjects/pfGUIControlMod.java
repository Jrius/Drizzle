/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package prpobjects;

import uru.*;
import shared.*;

public class pfGUIControlMod extends uruobj
{
    plSingleModifier parent;
    int u1;
    byte b1;
    //int u2; //something irrelevant done with this.
    pfGUICtrlProcWriteableObject wo;
    byte b2;
    Uruobjectref xref1;
    Uruobjectref xref2;
    byte b3;
     //Flt[] xflts;
     //int xu3;
     //Urustring xstr1;
     //byte xb4;
     //byte xb5;
    public pfGUIColorScheme colorScheme;
    byte b4;
    int[] ints1;
    //HsBitVector bv1;
    Uruobjectref ref1;
    Uruobjectref ref2;

    public pfGUIControlMod(context c) throws readexception
    {
        parent = new plSingleModifier(c);
        u1 = c.readInt();
        b1 = c.readByte();

        wo = new pfGUICtrlProcWriteableObject(c);
        //pfGUICtrlProcWriteableObject
        //u2 = c.readInt(); //case 0:nothing, case 1: read int x, and x bytes, case2,3: nothing?

        b2 = c.readByte();
        if(b2!=0)
        {
            xref1 = new Uruobjectref(c);
            xref2 = new Uruobjectref(c);
        }
        b3 = c.readByte();
        if(b3!=0)
        {
            colorScheme = new pfGUIColorScheme(c);
        }
        b4 = c.readByte();
        //if(b4!=0)
        //{
        ints1 = c.readInts(b.ByteToInt32(b4));
        //}

        //bv1 = new HsBitVector(c); //if it has more than 255 entries, this is broken.
        if(parent.flagvector.count!=0 && (parent.flagvector.get(0)&0x40)!=0 )
        {
            ref1 = new Uruobjectref(c); //92 is bitvector count, 88 is ref to bitvector vals
        }
        ref2 = new Uruobjectref(c);
    }
    public void compile(Bytedeque c)
    {
        parent.compile(c);
        c.writeInt(u1);
        c.writeByte(b1);
        //c.writeInt(u2);
        wo.compile(c);

        c.writeByte(b2);
        if(b2!=0)
        {
            xref1.compile(c);
            xref2.compile(c);
        }
        c.writeByte(b3);
        if(b3!=0)
        {
            colorScheme.compile(c);
        }
        c.writeByte((byte)ints1.length);
        //if(b4!=0)
        //{
        c.writeInts(ints1);
        //}
        
        if(parent.flagvector.count!=0 && (parent.flagvector.get(0)&0x40)!=0 )
        {
            ref1.compile(c);
        }
        ref2.compile(c);
    }
    public static class pfGUIColorScheme //was whattheheck
    {
        //Flt[] xflts;
        public RGBA foregroundColor;
        public RGBA backgroundColor;
        public RGBA selectedForegroundColor;
        public RGBA selectedBackgroundColor;
        public int transparent;
        public Urustring font;
        public byte fontsize;
        public byte fontflags;

        public pfGUIColorScheme(context c) throws readexception
        {
            //16 flt, 1 int, 1 urustring, 2 bytes
            //xflts = c.readArray(Flt.class, 16);
            foregroundColor = new RGBA(c.in);
            backgroundColor = new RGBA(c.in);
            selectedForegroundColor = new RGBA(c.in);
            selectedBackgroundColor = new RGBA(c.in);
            transparent = c.readInt();
            font = new Urustring(c);
            fontsize = c.readByte();
            fontflags = c.readByte();
        }
        public void compile(Bytedeque c)
        {
            //c.writeArray(xflts);
            foregroundColor.compile(c);
            backgroundColor.compile(c);
            selectedForegroundColor.compile(c);
            selectedBackgroundColor.compile(c);
            c.writeInt(transparent);
            font.compile(c);
            c.writeByte(fontsize);
            c.writeByte(fontflags);
        }
    }

    public static class pfGUICtrlProcWriteableObject
    {
        public static final int kNull = 0;
        public static final int kConsoleCmd = 1;
        public static final int kPythonScript = 2;
        public static final int kCloseDlg = 3;

        int type;
        byte[] xConsoleCommand;

        public pfGUICtrlProcWriteableObject(context c)
        {
            type = c.readInt();
            if(type==kConsoleCmd)
            {
                int numbytes = c.readInt();
                xConsoleCommand = c.readBytes(numbytes);
            }
            else if(type==kPythonScript)
            {
                //nothing?
            }
            else if(type==kCloseDlg)
            {
                //nothing?
            }
        }

        public void compile(Bytedeque c)
        {
            c.writeInt(type);
            if(type==kConsoleCmd)
            {
                c.writeInt(xConsoleCommand.length);
                c.writeBytes(xConsoleCommand);
            }
            else if(type==kPythonScript)
            {
                //nothing?
            }
            else if(type==kCloseDlg)
            {
                //nothing?
            }
        }
    }
}

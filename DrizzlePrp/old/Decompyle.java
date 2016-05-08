///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package pythondec;
//
//import shared.ByteArrayBytestream;
//import shared.FileUtils;
//import shared.IBytestream;
//import shared.e;
//import shared.uncaughtexception;
//import shared.b;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Vector;
//import shared.m;
//import pythondec.Disassemble.OpInfo;
//
//public class Decompyle
//{
//    //static final int Python22Magic = 0x0A0DED2D;
//
//
//    public static void Decompyle(String infile, String outfolder, boolean justDisassemble)
//    {
//        //PythonXX p = new Python22();
//        IBytestream c = ByteArrayBytestream.createFromByteArray(FileUtils.ReadFile(infile));
//        Demarshal demarshalledCode = new Demarshal(c);
//        Disassemble disassembledCode = new Disassemble(demarshalledCode.code,demarshalledCode.p);
//        for(OpInfo t: disassembledCode.rv)
//        {
//            String msg = "type: "+t.o.toString();
//            msg += "  offset: "+Integer.toString(t.offset);
//            msg += "  pattr: "+((t.pattr==null)?"(null)":t.pattr.toString().replace("\n", ""));
//            msg += "  oparg: "+((t.oparg==null)?"(null)":Integer.toString(t.oparg));
//            //if(t.pointerSource!=null) msg += "  psource: "+Integer.toString(t.pointerSource);
//            if(t.pointerDest!=null) msg += "  pdest: "+Integer.toString(t.pointerDest);
//            m.msg(msg);
//        }
//        if(justDisassemble) return;
//        Deflattener2 deflattenedCode = new Deflattener2(disassembledCode,demarshalledCode.p);
//
//        int dummy=0;
//    }
//}

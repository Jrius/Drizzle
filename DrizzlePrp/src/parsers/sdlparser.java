/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package parsers;

import parsers.gen.SDLgrammarParser;
import parsers.gen.SDLgrammarLexer;
import shared.*;
//import lpg.runtime.*;
import org.antlr.runtime.*;
//import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;

//Be careful about editing this class, as it might then not jive with the grammar, which would mean the grammar would have to be edited if it is to be updated.

public class sdlparser
{
    public static Sdlfile parse(byte[] data)
    {
        //lpg stuff:
        /*//lpg.runtime.LexStream lexstream = new lpg.runtime.LexStream();
        //lpg.runtime.Utf8LexStream lexstream = new lpg.runtime.Utf8LexStream(data, "filename goes here");
        lpg.runtime.LexStream lexstream = new lpg.runtime.LexStream(data, "filenamegoeshere");
        SDL sdlparser = new SDL(lexstream);
        lpg.runtime.IPrsStream prsstream = lexstream.getIPrsStream();

        //prsstream.

        parsers.ast.Ast tree = sdlparser.parser();

        int dummy=0;

        //create tokens.*/

        //antlr stuff:

        try{
            java.io.ByteArrayInputStream in1 = new java.io.ByteArrayInputStream(data);
            org.antlr.runtime.ANTLRInputStream in2 = new org.antlr.runtime.ANTLRInputStream(in1);
            //parsers.gen.SDLgrammarLexer lexer = new parsers.gen.SDLgrammarLexer(in2);
            CustomLexer lexer = new CustomLexer(in2);
            org.antlr.runtime.CommonTokenStream tokenstream = new org.antlr.runtime.CommonTokenStream(lexer);
            //parsers.gen.SDLgrammarParser parser = new parsers.gen.SDLgrammarParser(tokenstream);
            CustomParser parser = new CustomParser(tokenstream);

            //SDLgrammarParser.sdlfile_return root = parser.sdlfile();
            Sdlfile root = parser.sdlfile();
            //root.
            //int numerrors = parser.getNumberOfSyntaxErrors();
            //if(numerrors!=0) m.msg("Error found!");
            //int numlexerrors = lexer.getNumberOfSyntaxErrors();
            //if(numlexerrors!=0) m.msg("Lex Error found!");
            //int dummy=0;
            return root;
        }catch(ParseException e){
            m.err("Unable to correctly parse file.");
            return null;
        }catch(Exception e){
            throw new shared.nested(e);
        }



    }


    //SDl classes:
    public static class ValueTuple extends Value
    {
        public ArrayList<Value> values = new ArrayList();
        public void add(Value value)
        {
            this.values.add(value);
        }
        public Object getValue(){return values;}
    }
    public static class ValueName extends Value
    {
        public String name;
        public ValueName(String name)
        {
            this.name = name;
        }
        public Object getValue(){return name;}
    }
    public static class ValueInt extends Value
    {
        public int value;
        public ValueInt(int value)
        {
            this.value = value;
        }
        public Object getValue(){return value;}
    }
    public static class ValueFloat extends Value
    {
        public float value;
        public ValueFloat(float value)
        {
            this.value = value;
        }
        public Object getValue(){return value;}
    }
    public static class ValueString extends Value
    {
        public String value;
        public ValueString(String escapedvalue)
        {
            this.value = shared.EscapeUtils.unescapeJavaString(escapedvalue);
        }
        public Object getValue(){return value;}
    }
    public static class TypeStd extends Type
    {
        public String typestr;
        public TypeStd(String typestr)
        {
            this.typestr = typestr;
            String t = typestr.toLowerCase();
            if(t.equals("int"))
                type = Type.kInt;
            else if(t.equals("float"))
                type = Type.kFloat;
            else if(t.equals("bool"))
                type = Type.kBool;
            else if(t.equals("byte"))
                type = Type.kByte;
            else if(t.equals("creatable"))
                type = Type.kCreatable;
            else if(t.equals("time"))
                type = Type.kTime;
            else if(t.equals("string32"))
                type = Type.kString;
            else if(t.equals("short"))
                type = Type.kShort;
            else if(t.equals("plkey"))
                type = Type.kKey;
            else if(t.equals("point3"))
                type = Type.kPoint3;
            else if(t.equals("rgb8"))
                type = Type.kRGB8;
            else if(t.equals("agetimeofday"))
                type = Type.kAgeTimeOfDay;
            else if(t.equals("quaternion"))
                type = Type.kQuaternion;
            else if(t.equals("vector3"))
                type = Type.kVector3;
            else
                m.throwUncaughtException("unhandled");
        }
    }
    public static class TypeStatedesc extends Type
    {
        public String typestr;
        public TypeStatedesc(String typestr)
        {
            this.typestr = typestr;
            this.type = Type.kStateDescriptor;
        }
    }
    public abstract static class Type
    {
        public static final byte kNone = (byte)0xFF;
        public static final byte kInt = (byte)0;
        public static final byte kFloat = (byte)1;
        public static final byte kBool = (byte)2;
        public static final byte kString = (byte)3;
        public static final byte kKey = (byte)4;
        public static final byte kStateDescriptor = (byte)5;
        public static final byte kCreatable = (byte)6;
        public static final byte kDouble = (byte)7;
        public static final byte kTime = (byte)8;
        public static final byte kByte = (byte)9;
        public static final byte kShort = (byte)10;
        public static final byte kAgeTimeOfDay = (byte)11;
        public static final byte kVector3 = (byte)50;
        public static final byte kPoint3 = (byte)51;
        public static final byte kRGB = (byte)52;
        public static final byte kRGBA = (byte)53;
        public static final byte kQuaternion = (byte)54;
        public static final byte kRGB8 = (byte)55;
        public static final byte kRGBA8 = (byte)56;

        public byte type;
        public boolean isStatedesc()
        {
            if(this instanceof TypeStatedesc) return true;
            return false;
        }
    }
    public abstract static class Value
    {
        public abstract Object getValue();

        public float AsFloat()
        {
            Class klass = this.getClass();
            if(klass==ValueFloat.class)
            {
                return ((ValueFloat)this).value;
            }
            else if(klass==ValueInt.class)
            {
                return (float)(((ValueInt)this).value);
            }
            else
            {
                throw new shared.uncaughtexception("wasn't a float");
            }
        }
    }
    public abstract static class Option
    {
    }
    public static class OptionDefault extends Option
    {
        public Value value;
        public OptionDefault(Value value)
        {
            this.value = value;
        }
    }
    public static class OptionDefaultoption extends Option
    {
        public Value value;
        public OptionDefaultoption(Value value)
        {
            this.value = value;
        }
    }
    public static class OptionDisplayoption extends Option
    {
        public Value value;
        public OptionDisplayoption(Value value)
        {
            this.value = value;
        }
    }
    public static class Varline
    {
        public Type type;
        public String name;
        public Integer index;
        public ArrayList<Option> options = new ArrayList();
        public OptionDefault defaultOption;
        public Varline(Type type, String name)
        {
            this.type = type;
            this.name = name;
        }
        public void setIndex(int index)
        {
            this.index = index;
        }
        public void add(Option option)
        {
            options.add(option);
            if(option instanceof OptionDefault) defaultOption = (OptionDefault)option;
        }
        public boolean isVariableLength()
        {
            if(index==null) return true;
            else return false;
        }
        public int getCount()
        {
            return index;
        }
        public Value getDefault()
        {
            if(defaultOption==null) return null;
            return defaultOption.value;
        }
    }
    public static class Sdlfile
    {
        public ArrayList<Statedesc> statedescs = new ArrayList();
        public void add(Statedesc statedesc)
        {
            statedescs.add(statedesc);
        }
    }
    public static class Statedesc
    {
        public String name;
        public int version;
        public ArrayList<Varline> varlines = new ArrayList();
        public ArrayList<Varline> varlinessimple = new ArrayList();
        public ArrayList<Varline> varlinesstatedescs = new ArrayList();
        public Statedesc(String name, int version)
        {
            this.name = name;
            this.version = version;
        }
        public void add(Varline varline)
        {
            varlines.add(varline);
            if(varline.type.isStatedesc()) varlinesstatedescs.add(varline);
            else varlinessimple.add(varline);
        }
        public int getVarCount()
        {
            return varlines.size();
        }
        public int getSimpleVarCount()
        {
            return varlinessimple.size();
        }
        public int getStatedescVarCount()
        {
            return varlinesstatedescs.size();
        }
        public Varline getVar(int index)
        {
            return varlines.get(index);
        }
        public Varline getSimpleVar(int index)
        {
            return varlinessimple.get(index);
        }
        public Varline getStatedescVar(int index)
        {
            return varlinesstatedescs.get(index);
        }
        public String toString()
        {
            return name.toString();
        }
    }


    public static class ParseException extends java.lang.RuntimeException
    {

    }
    public static class CustomParser extends SDLgrammarParser
    {
        public CustomParser(TokenStream input)
        {
            super(input);
        }
        public void reportError(RecognitionException re)
        {
            //m.msg("err1");
            super.reportError(re);
            throw new ParseException();
        }
        public void recover(IntStream input, RecognitionException re)
        {
            //m.msg("err2");
            //throw new shared.uncaughtnestedexception(re);
            super.recover(input, re);
        }
    }
    public static class CustomLexer extends SDLgrammarLexer
    {
        public CustomLexer(CharStream input)
        {
            super(input);
        }
        public void reportError(RecognitionException re)
        {
            //m.msg("err3");
            super.reportError(re);
            throw new ParseException();
        }
        public void recover(IntStream input, RecognitionException re)
        {
            //m.msg("err4");
            //throw new shared.uncaughtnestedexception(re);
            super.recover(input, re);
        }
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3.ast;

import lpg.runtime.IToken;
import lpg.runtime.IPrsStream;
import pythondec.Disassemble.OpInfo;
import pythondec3.PythonDec22sym;
import lpg.runtime.ILexStream;
import pythondec.*;
import pythondec3.helpers;

public class Tok extends pythondec3.ast.Ast implements IToken
{
    int _kind;
    int _startoffset;
    int _endoffset;
    int _tokenindex;
    int _adjunctindex;
    IPrsStream _prsstream;

    int tokennum;
    public OpInfo oi;
    String opname;

    public String debugstr = "";

    public Tok(OpInfo oi2, int number, String opname2, IPrsStream prsstream)
    {
        _prsstream = prsstream;
        oi = oi2;
        tokennum = number;
        opname = opname2;
        _kind = getToken();
        _startoffset = number;
        _endoffset = number;
        _tokenindex = 0;
        _adjunctindex = 0;
    }
    public Tok(OpInfo oi2, int number, IPrsStream prsstream)
    {
        this(oi2,number,oi2.o.toString(),prsstream);
        //oi = oi2;
        //tokennum = number;
        //opname = oi.o.toString();
        //_kind = getToken();
    }
    /*public DisToken(IPrsStream prsstream)
    {
        //eof
        this(null,-1,"EOF_TOKEN",prsstream);
        //oi = null;
        //tokennum = -1;
        //opname = "EOF_TOKEN";
        //_kind = getToken();
    }*/
    public static Tok fakeToken(op o, IPrsStream prsstream)
    {
        OpInfo oi = OpInfo.create(o);
        Tok r = new Tok(oi,-1,prsstream);
        return r;
    }
    public static Tok fakeToken(op o, IPrsStream prsstream, int offset, Object pattr)
    {
        OpInfo oi = OpInfo.create(o);
        oi.pattr = pattr;
        oi.offset = offset;
        Tok r = new Tok(oi,-1,prsstream);
        return r;
    }
    public static Tok customToken(String tokenname, IPrsStream prsstream)
    {
        Tok r = new Tok(OpInfo.create(op.CUSTOM),-1,tokenname,prsstream);
        return r;
    }
    public static Tok customToken(String tokenname, IPrsStream prsstream, Object pattr)
    {
        OpInfo oi = OpInfo.create(op.CUSTOM);
        oi.pattr = pattr;
        Tok r = new Tok(oi,-1,tokenname,prsstream);
        return r;
    }
    public static Tok eofToken(IPrsStream prsstream)
    {
        Tok r = new Tok(OpInfo.create(op.EOF),-1,"EOF_TOKEN",prsstream);
        return r;
    }
    public static Tok sofToken(IPrsStream prsstream)
    {
        Tok r = new Tok(OpInfo.create(op.SOF),-1,"SOF_TOKEN",prsstream);
        return r;
    }
    public void gen2(sgen s)
    {
        //this is not at all reliable!!!
        if(oi==null)
            s.out("DrizzleError:"+this.toString());
        else
        {
            String name = getName(s);
            if(name==null)
                s.out("DrizzleError:"+this.toString());
            else
                s.out(name);
        }
    }
    public String getName(sgen s)
    {
        if(oi.pattr instanceof PyString)
        {
            PyString r = (PyString)oi.pattr;
            switch(oi.o)
            {
                //case LOAD_CONST:
                //    return "'"+helpers.escapePythonString(r)+"'";
                case STORE_ATTR:
                case LOAD_ATTR:
                case STORE_NAME:
                case LOAD_NAME:
                case STORE_FAST:
                case LOAD_FAST:
                    String demangledname = helpers.demangleName(s, r.toJavaString());
                    return demangledname;
                default:
                    return r.toJavaString();
            }
        }
        if(oi.pattr instanceof PyInt)
        {
            PyInt r = (PyInt)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        if(oi.pattr instanceof PyNone)
        {
            PyNone r = (PyNone)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        if(oi.pattr instanceof PyFloat)
        {
            PyFloat r = (PyFloat)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        if(oi.pattr instanceof PyLong)
        {
            PyLong r = (PyLong)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        if(oi.pattr instanceof PyComplex)
        {
            PyComplex r = (PyComplex)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        if(oi.pattr instanceof PyUnicode)
        {
            PyUnicode r = (PyUnicode)oi.pattr;
            String r2 = r.toJavaString(); //this is placing quotes around it, which should be the only
            return r2;
        }
        if(oi.pattr instanceof PyEllipsis)
        {
            PyEllipsis r = (PyEllipsis)oi.pattr;
            String r2 = r.toJavaString();
            return r2;
        }
        return null;
    }
    public String getNameQuoted(sgen s)
    {
        if(oi.pattr instanceof PyString)
        {
            String r = "'"+helpers.escapePythonString((PyString)oi.pattr)+"'";
            return r;
        }
        if(oi.pattr instanceof PyUnicode)
        {
            String r = "u'"+helpers.escapeUnicodeString(((PyUnicode)oi.pattr).toJavaString())+"'";
            return r;
        }
        return getName(s);
    }
    private int getToken()
    {
        boolean doefficient = false;
        if(doefficient)
        {
            switch(oi.o)
            {
                case POP_TOP: return PythonDec22sym.TK_POP_TOP;
                case ROT_TWO: return PythonDec22sym.TK_ROT_TWO;
                default:
                    throw new shared.uncaughtexception("Unknown token.");
            }
        }
        else
        {
            if(this.opname.equals("EOF_TOKEN")) return PythonDec22sym.TK_EOF_TOKEN;
            if(this.opname.equals("SOF_TOKEN")) return 0; //this shouldn't conflict, as generated source uses 0 for a bad token.
            try{
                //String opname2 = (opname!=null)?(opname):(oi.o.toString());
                java.lang.reflect.Field f = PythonDec22sym.class.getField("TK_"+opname);
                int r = f.getInt(null);
                return r;
            }catch(Exception e){
                e.printStackTrace();
                throw new shared.uncaughtexception("wha");
            }
        }
    }

    public int getKind()
    {
        return _kind;
    }
    public void setKind(int kind)
    {
        _kind = kind;
    }
    public int getStartOffset()
    {
        return _startoffset;
    }
    public void setStartOffset(int startOffset)
    {
        _startoffset = startOffset;
    }
    public int getEndOffset()
    {
        return _endoffset;
    }
    public void setEndOffset(int endOffset)
    {
        _endoffset = endOffset;
    }
    public int getTokenIndex()
    {
        return _tokenindex;
    }
    public void setTokenIndex(int i)
    {
        _tokenindex = i;
    }
    public int getAdjunctIndex()
    {
        return _adjunctindex;
    }
    public void setAdjunctIndex(int i)
    {
        _adjunctindex = i;
    }
    public IToken[] getPrecedingAdjuncts()
    {
        throw new shared.uncaughtexception("unhandled");
    }
    public IToken[] getFollowingAdjuncts()
    {
        throw new shared.uncaughtexception("unhandled");
    }
    public ILexStream getILexStream()
    {
        return _prsstream.getILexStream();
    }
    public ILexStream getLexStream()
    {
        return getILexStream();
    }
    public IPrsStream getIPrsStream()
    {
        return _prsstream;
    }
    public IPrsStream getPrsStream()
    {
        return getIPrsStream();
    }

    public int getLine()
    {
        return tokennum;
    }
    public int getColumn()
    {
        return 1;
    }
    public int getEndLine()
    {
        return tokennum;
    }
    public int getEndColumn()
    {
        return 1;
    }
    public String getValue(char[] inputChars)
    {
        return toString();
    }

    public String toString()
    {
        //return "fixthistostring";
        //return opname;
        //String r = oi.;
        //PythonDec22sym.
        String r = opname;
        r += " : ";
        if(oi!=null) r+= oi.toDebugString();
        r += " tokennum: "+Integer.toString(_tokenindex);
        if(!debugstr.equals("")) r+= " debug: "+debugstr;
        return r;
    }

    public boolean compare(Tok t2)
    {
        //if(this.tokennum!=t2.tokennum) return false; //ignore tokennum
        if(!this.opname.equals(t2.opname)) return false;
        if(this.oi==null)
        {
            if(t2.oi==null)
            {
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(t2.oi==null)
            {
                return false;
            }
            else
            {
                if(!this.oi.compare(t2.oi)) return false;
            }
        }
        return true;
    }
}

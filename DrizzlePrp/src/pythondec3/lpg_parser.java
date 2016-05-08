/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pythondec3;

import lpg.runtime.IMessageHandler;
import lpg.runtime.IPrsStream;
import lpg.runtime.IToken;
import lpg.runtime.ILexStream;
import pythondec.Disassemble.OpInfo;
import pythondec.op;
import shared.m;
import pythondec3.PythonDec22sym;
import java.util.ArrayList;
import pythondec3.ast.Tok;
import pythondec3.ast.Ast;
import pythondec.PyCode;
import java.util.LinkedList;

/**
 *
 * @author user
 */
public class lpg_parser
{
    public static void works()
    {
        //work2();
    }
    public static ArrayList<Tok> MakeTokens(pythondec.Disassemble dis, IPrsStream prsstream)
    {
        //ArrayList<Tok> tokens = new ArrayList();
        LinkedList<Tok> tokens = new LinkedList();
        Tok sof = Tok.sofToken(prsstream);
        tokens.add(sof);
        for(int i=0;i<dis.rv.size();i++)
        {
            OpInfo opinfo = dis.rv.get(i);

            if(opinfo.pointerSources!=null)
            {
                for(Integer j: opinfo.pointerSources)
                {
                    //tokens.add(Tok.customToken("LAND", prsstream));
                    Tok t = Tok.fakeToken(op.LAND, prsstream);
                    t.oi.pattr = j;
                    tokens.add(t);
                }
            }

            if(opinfo.o==op.SET_LINENO)
            {
                continue;  //skip SET_LINEO
            }
            else if(opinfo.o==op.BINARY_TRUE_DIVIDE||opinfo.o==op.INPLACE_TRUE_DIVIDE)
            {
                //if a call is made to "from __future__ import division", then it uses INPLACE_TRUE_DIVIDE, ortherwise the compiler uses the class INPLACE_DIVIDE.
                //the difference between DIVIDE and TRUE_DIVIDE is that DIVIDE may do integer division, but TRUE_DIVIDE never does.
                //They also introduced a new operation for integer divison for when you want that.
                //so we could easily include these if we want, but we may have to ensure the futures statement is included (we'll probably get this for free, so no worries.)

                //m.throwUncaughtException("BINARY_TRUE_DIVIDE is a futures thing."); //perhaps we could properly handle this, or we might get away with changing the token.
                tokens.add(new Tok(opinfo,i,prsstream));
            }
            else if(opinfo.o==op.BUILD_LIST || opinfo.o==op.BUILD_TUPLE || opinfo.o==op.MAKE_FUNCTION)
            {
                /*int count = opinfo.oparg;
                String opname = opinfo.o.toString()+"_"+Integer.toString(count);
                Tok r = new Tok(opinfo,i,opname,prsstream);
                //prsstream.addToken(r);
                tokens.add(r);*/
                //add EAT tokens:
                int count = opinfo.oparg;
                //String eatername;
                //if(opinfo.o==op.BUILD_LIST) eatername = "EAT_BL";
                //else if(opinfo.o==op.BUILD_TUPLE) eatername = "EAT_BT";
                //else if(opinfo.o==op.MAKE_FUNCTION) eatername = "EAT_MF";
                //else throw new shared.uncaughtexception("unhandled");

                if(options.simpleLists)
                {
                    if(opinfo.o==op.BUILD_TUPLE || opinfo.o==op.BUILD_LIST)
                    {

                        //extra help for simple lists
                        int size = tokens.size();
                        boolean issimple = true;
                        for(int j=0;j<count;j++)
                        {
                            Tok t = tokens.get(size-1-j);
                            if(t.oi.o!=op.LOAD_CONST && t.oi.o!=op.LOAD_FAST && t.oi.o!=op.LOAD_GLOBAL && t.oi.o!=op.LOAD_NAME)
                            {
                                issimple = false;
                                break;
                            }
                        }

                        if(issimple && count!=0)
                        {
                            LinkedList<Ast> simplelist = new LinkedList();
                            int offset = -1; //will be the last item added to the list.
                            for(int j=0;j<count;j++)
                            {
                                Tok t = tokens.remove(size-1-j);
                                offset = t.oi.offset;
                                switch(t.oi.o)
                                {
                                    case LOAD_CONST:
                                        simplelist.add(new pythondec3.ast.Expr.Loadconst(t));
                                        break;
                                    case LOAD_FAST:
                                        simplelist.add(new pythondec3.ast.Expr.Loadfast(t));
                                        break;
                                    case LOAD_GLOBAL:
                                        simplelist.add(new pythondec3.ast.Expr.Loadglobal(t));
                                        break;
                                    case LOAD_NAME:
                                        simplelist.add(new pythondec3.ast.Expr.Loadname(t));
                                        break;
                                    default:
                                        throw new shared.uncaughtexception("unhandled");
                                }
                                //simplelist.add(t);
                            }
                            tokens.add(Tok.fakeToken(op.EXPR_LIST, prsstream, offset, simplelist));
                            tokens.add(new Tok(opinfo,i,prsstream));
                        }
                        else
                        {
                            tokens.add(new Tok(opinfo,i,prsstream));
                            for(int j=0;j<count;j++)
                            {
                                //tokens.add(Tok.customToken(eatername, prsstream));
                                tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                            }
                        }
                        
                        continue;
                    }
                }

                if(true)
                {
                    if(opinfo.o==op.MAKE_FUNCTION)
                    {
                        Tok lctok = tokens.remove(tokens.size()-1);
                        if(lctok.oi.o!=op.LOAD_CONST) m.throwUncaughtException("unhandled"); //this shouldn't happen.
                        tokens.add(new Tok(lctok.oi,i,"LOAD_CONST_MAKE_FUNCTION",prsstream));
                        for(int j=0;j<count;j++)
                        {
                            //tokens.add(Tok.customToken(eatername, prsstream));
                            tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                        }
                        continue;
                    }
                }

                boolean oldway = false;
                if(opinfo.o!=op.BUILD_LIST) oldway = false;
                if(oldway)
                {
                    tokens.add(Tok.customToken("BUILD_LIST_"+Integer.toString(count), prsstream));
                }
                else
                {
                    tokens.add(new Tok(opinfo,i,prsstream));
                    for(int j=0;j<count;j++)
                    {
                        //tokens.add(Tok.customToken(eatername, prsstream));
                        tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                    }
                }
            }
            else if(opinfo.o==op.MAKE_CLOSURE)
            {
                int exprcount = opinfo.oparg;
                Tok lctok = tokens.get(tokens.size()-1);
                if(lctok.oi.o!=op.LOAD_CONST) m.throwUncaughtException("unhandled"); //this shouldn't happen.
                PyCode code = (PyCode)lctok.oi.pattr;
                //tokens.add(new Tok(lctok.oi,i,"LOAD_CONST_MAKE_CLOSURE",prsstream));
                tokens.add(new Tok(opinfo,i,prsstream));
                for(int j=0;j<code.freevars.items.length;j++)
                {
                    tokens.add(Tok.customToken("EAT_LOADCLOSURE", prsstream));
                }
                for(int j=0;j<exprcount;j++)
                {
                    //tokens.add(Tok.customToken(eatername, prsstream));
                    tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                }
            }
            else if(opinfo.o==op.CALL_FUNCTION || opinfo.o==op.CALL_FUNCTION_KW || opinfo.o==op.CALL_FUNCTION_VAR || opinfo.o==op.CALL_FUNCTION_VAR_KW)
            {
                final int style = 1;

                int positional = opinfo.i0;
                int keywords = opinfo.i1;
                //if(positional==0 && keywords==0)
                //{
                //    String opname = opinfo.o.toString()+"_"+Integer.toString(positional)+"_"+Integer.toString(keywords);
                //    tokens.add(new Tok(opinfo,i,opname,prsstream));
                //}
                //else
                //{
                if(style==1)
                {
                    tokens.add(new Tok(opinfo,i,prsstream));
                    for(int j=0;j<keywords;j++)
                    {
                        tokens.add(Tok.customToken("EAT_KWARG", prsstream));
                    }
                    for(int j=0;j<positional;j++)
                    {
                        tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                    }
                }
                else if(style==2)
                {
                    tokens.add(new Tok(opinfo,i,prsstream));
                    if(opinfo.o==op.CALL_FUNCTION_KW || opinfo.o==op.CALL_FUNCTION_VAR)
                    {
                        //tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                    }
                    else if(opinfo.o==op.CALL_FUNCTION_VAR_KW)
                    {
                        //tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                        //tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                    }
                    for(int j=0;j<keywords;j++)
                    {
                        //tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                        //tokens.add(Tok.customToken("EAT_CONST", prsstream));

                        //tokens.add(Tok.customToken("EAT_EXPR", prsstream));

                        tokens.add(Tok.customToken("EAT_KWARG", prsstream));
                    }
                    for(int j=0;j<positional;j++)
                    {
                        tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                    }
                    //tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                }
                //}
            }
            else if(opinfo.o==op.DUP_TOPX)
            {
                int numtodup = opinfo.oparg;
                tokens.add(new Tok(opinfo,i,prsstream));
                for(int j=0;j<numtodup;j++)
                {
                    tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                }
            }
            else if(opinfo.o==op.UNPACK_SEQUENCE)
            {
                int num = opinfo.oparg;
                for(int j=0;j<num;j++)
                {
                    tokens.add(Tok.customToken("ANTIEAT_DESIG", prsstream));
                }
                tokens.add(new Tok(opinfo,i,prsstream));
            }
            else if(opinfo.o==op.RAISE_VARARGS)
            {
                int num = opinfo.oparg;
                tokens.add(new Tok(opinfo,i,prsstream));
                for(int j=0;j<num;j++)
                {
                    tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                }
            }
            else if(opinfo.o==op.BUILD_SLICE)
            {
                int num = opinfo.oparg;
                if(num!=2 && num!=3) m.throwUncaughtException("unexpected"); //only 2 and 3 should occur.
                tokens.add(new Tok(opinfo,i,prsstream));
                for(int j=0;j<num;j++)
                {
                    tokens.add(Tok.customToken("EAT_EXPR", prsstream));
                }
            }
            else if(opinfo.o==op.JUMP_ABSOLUTE || opinfo.o==op.JUMP_FORWARD)
            {
                if(options.jumpFalls)
                {
                    tokens.add(Tok.customToken("FALL", prsstream));
                }
                tokens.add(new Tok(opinfo,i,prsstream));
            }
            else
            {
                Tok r = new Tok(opinfo,i,prsstream);
                //prsstream.addToken(r);
                tokens.add(r);

            }
        }
        Tok eof = Tok.eofToken(prsstream);
        //prsstream.addToken(eof);
        tokens.add(eof);

        //renumber tokens:
        for(int i=0;i<tokens.size();i++)
        {
            Tok token = tokens.get(i);
            token.setTokenIndex(i);
        }

        /*//add tokens to stream
        if(pythondec3.stats.printTiming) shared.m.marktime("Starting adding tokens");
        for(Tok token: tokens)
        {
            prsstream.addToken(token);
        }

        //update the streamlength
        prsstream.setStreamLength();*/

        //return tokekns;
        ArrayList r = new ArrayList(tokens);
        return r;
    }
    public static Ast Deflatten(/*pythondec.Disassemble dis,*/ ArrayList<Tok> tokens, pythondec3.PythonDec22 pd22, PyCode pycode)
    {

        //prsstream.makeToken(startLoc, endLoc, kind);
        //for(pythondec.Disassemble.OpInfo oi: disassembledCode.rv)
        //{
        //    DisToken token = new DisToken(oi);
        //    prsstream.addToken(token);
        //}

        //pythondec3.ast.Ast root;
        //try{
        pythondec3.ast.Ast root = pd22.parser();
        if(root==null)
        {
            for(Tok token: tokens) m.msg(token.toString());
            m.msg();
            stats.printCurtokens();
            m.msg("Class/function name: ",pycode.name.toString());
            m.cancel("Root Ast is null, so the parsing must not have worked. ");
        }
        //pythondec3.ast.stmt_plus rt = (pythondec3.ast.stmt_plus)root;
        //}catch(Exception e){
//            e.printStackTrace();
  //      }
        //lpg.runtime.IAst curast = root;
        //while(curast!=null)
        //{
            //do stuff with current AST.
        //    curast = curast.getNextAst();
        //}
        int dummy=0;
        return root;
    }
//    public static void work1()
//    {
//        pythondec3.PythonDec22prs a;
//        pythondec3.PythonDec22sym b;
//        pythondec3.PythonDec22 c;
//        //lpg.runtime.
//        lpg.runtime.ILexStream lexstream = new lpg.runtime.ILexStream() {
//
//            private IPrsStream prsstream;
//
//            public IPrsStream getIPrsStream() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public IPrsStream getPrsStream() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void setPrsStream(IPrsStream stream) {
//                prsstream = stream;
//                //if(prsstream!=null) prsstream.setLexStream(this);
//                prsstream.setLexStream(this);
//            }
//
//            public int getLineCount() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public String[] orderedExportedSymbols() {
//                //return null;  //this is was LexStream does.
//                return pythondec3.PythonDec22sym.orderedTerminalSymbols;
//            }
//
//            public int getLineOffset(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getLineNumberOfCharAt(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getColumnOfCharAt(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public char getCharValue(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getIntValue(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void makeToken(int startLoc, int endLoc, int kind) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void setMessageHandler(IMessageHandler errMsg) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public IMessageHandler getMessageHandler() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int[] getLocation(int left_loc, int right_loc) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportLexicalError(int left, int right) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportLexicalError(int errorCode, int left_loc, int right_loc, int error_left_loc, int error_right_loc, String[] errorInfo) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public String toString(int startOffset, int endOffset) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getToken() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getToken(int end_token) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getKind(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getNext(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getPrevious(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public String getName(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int peek() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reset(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reset() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int badToken() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getLine(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getColumn(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getEndLine(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getEndColumn(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public boolean afterEol(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public String getFileName() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getStreamLength() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getFirstRealToken(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public int getLastRealToken(int i) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportError(int errorCode, int leftToken, int rightToken, String errorInfo) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportError(int errorCode, int leftToken, int errorToken, int rightToken, String errorInfo) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportError(int errorCode, int leftToken, int rightToken, String[] errorInfo) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void reportError(int errorCode, int leftToken, int errorToken, int rightToken, String[] errorInfo) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//
//
//        pythondec3.PythonDec22 pd22 = new pythondec3.PythonDec22(lexstream);
//        //pd22.
//        //lexstream.
//        pythondec3.ast.Ast root = pd22.parser();
//        int dummy=0;
//    }
    public static String getDynamicRules()
    {
        int build_list = 30;
        int build_tuple = 5;
        int mkfunc = 3;
        int callfunc_positionalargs = 3; int callfunc_keywordargs = 3;
        int callfuncvar_positionalargs = 3; int callfuncvar_keywordargs = 3;
        int callfunckw_positionalargs = 3; int callfunckw_keywordargs = 3;
        int callfuncvarkw_positionalargs = 3; int callfuncvarkw_keywordargs = 3;
        String tab = "    ";

        StringBuilder r = new StringBuilder();
        StringBuilder t = new StringBuilder();

        for(int i=0;i<=build_list;i++)
        {
            int e = 1;
            t.append(tab+"BUILD_LIST_"+Integer.toString(i)+"\n");
            r.append(tab+"dyn_list_expr ::= ");
            for(int j=0;j<i;j++) r.append("expr$e"+Integer.toString(e++)+" ");
            r.append("BUILD_LIST_"+Integer.toString(i));
            r.append("    /. set(new Expr.Buildlist(");
                for(int p=0;p<i;p++)
                {
                    if(p!=0) r.append(",");
                    r.append("s("+Integer.toString(p+1)+")");
                }
                r.append(")); ./");
            r.append("\n");
        }
        for(int i=0;i<=build_tuple;i++)
        {
            int e = 1;
            t.append(tab+"BUILD_TUPLE_"+Integer.toString(i)+"\n");
            r.append(tab+"dyn_tuple_expr ::= ");
            for(int j=0;j<i;j++) r.append("expr$e"+Integer.toString(e++)+" ");
            r.append("BUILD_TUPLE_"+Integer.toString(i));
            r.append("    /. set(new Expr.Buildtuple(");
                for(int p=0;p<i;p++)
                {
                    if(p!=0) r.append(",");
                    r.append("s("+Integer.toString(p+1)+")");
                }
                r.append(")); ./");
            r.append("\n");
        }
        for(int i=0;i<=mkfunc;i++)
        {
            int e = 1;
            t.append(tab+"MAKE_FUNCTION_"+Integer.toString(i)+"\n");
            r.append(tab+"dyn_mkfunc ::= ");
            for(int j=0;j<i;j++) r.append("expr$e"+Integer.toString(e++)+" ");
            r.append("LOAD_CONST MAKE_FUNCTION_"+Integer.toString(i));
            r.append("    /. set(new Expr.Makefunction(");
                r.append("t("+Integer.toString(i+1)+")");
                for(int p=0;p<i;p++)
                {
                    r.append(",");
                    r.append("s("+Integer.toString(p+1)+")");
                }
                r.append(")); ./");
            r.append("\n");
        }
        for(int i=0;i<=callfunc_positionalargs;i++)
        {
            for(int j=0;j<=callfunc_keywordargs;j++)
            {
                int e = 1;
                t.append(tab+"CALL_FUNCTION_"+Integer.toString(i)+"_"+Integer.toString(j)+"\n");
                r.append(tab+"dyn_callfunc ::= ");
                r.append("expr$en ");
                for(int k=0;k<i;k++) r.append("expr$e"+Integer.toString(e++)+" ");
                for(int l=0;l<j;l++) r.append("kwarg$e"+Integer.toString(e++)+" ");
                r.append("");
                r.append("CALL_FUNCTION_"+Integer.toString(i)+"_"+Integer.toString(j));
                r.append("    /. set(new Expr.Callfunction(");
                    r.append("s(1),new Ast[]{");
                    for(int p=0;p<i;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+1+1)+")");
                    }
                    r.append("},new Ast[]{");
                    for(int p=0;p<j;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+i+1+1)+")");
                    }
                    r.append("},null,null");
                    r.append(")); ./");
                r.append("\n");
            }
        }
        for(int i=0;i<=callfuncvar_positionalargs;i++)
        {
            for(int j=0;j<=callfuncvar_keywordargs;j++)
            {
                int e = 1;
                t.append(tab+"CALL_FUNCTION_VAR_"+Integer.toString(i)+"_"+Integer.toString(j)+"\n");
                r.append(tab+"dyn_callfunc ::= ");
                r.append("expr$en ");
                for(int k=0;k<i;k++) r.append("expr$e"+Integer.toString(e++)+" ");
                for(int l=0;l<j;l++) r.append("kwarg$e"+Integer.toString(e++)+" ");
                r.append("expr$ev ");
                r.append("CALL_FUNCTION_VAR_"+Integer.toString(i)+"_"+Integer.toString(j));
                r.append("    /. set(new Expr.Callfunction(");
                    r.append("s(1),new Ast[]{");
                    for(int p=0;p<i;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+1+1)+")");
                    }
                    r.append("},new Ast[]{");
                    for(int p=0;p<j;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+i+1+1)+")");
                    }
                    r.append("},s("+Integer.toString(i+j+1+1)+"),null");
                    r.append(")); ./");
                r.append("\n");
            }
        }
        for(int i=0;i<=callfunckw_positionalargs;i++)
        {
            for(int j=0;j<=callfunckw_keywordargs;j++)
            {
                int e = 1;
                t.append(tab+"CALL_FUNCTION_KW_"+Integer.toString(i)+"_"+Integer.toString(j)+"\n");
                r.append(tab+"dyn_callfunc ::= ");
                r.append("expr$en ");
                for(int k=0;k<i;k++) r.append("expr$e"+Integer.toString(e++)+" ");
                for(int l=0;l<j;l++) r.append("kwarg$e"+Integer.toString(e++)+" ");
                r.append("expr$ek ");
                r.append("CALL_FUNCTION_KW_"+Integer.toString(i)+"_"+Integer.toString(j));
                r.append("    /. set(new Expr.Callfunction(");
                    r.append("s(1),new Ast[]{");
                    for(int p=0;p<i;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+1+1)+")");
                    }
                    r.append("},new Ast[]{");
                    for(int p=0;p<j;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+i+1+1)+")");
                    }
                    r.append("},null,s("+Integer.toString(i+j+1+1)+")");
                    r.append(")); ./");
                r.append("\n");
            }
        }
        for(int i=0;i<=callfuncvarkw_positionalargs;i++)
        {
            for(int j=0;j<=callfuncvarkw_keywordargs;j++)
            {
                int e = 1;
                t.append(tab+"CALL_FUNCTION_VAR_KW_"+Integer.toString(i)+"_"+Integer.toString(j)+"\n");
                r.append(tab+"dyn_callfunc ::= ");
                r.append("expr$en ");
                for(int k=0;k<i;k++) r.append("expr$e"+Integer.toString(e++)+" ");
                for(int l=0;l<j;l++) r.append("kwarg$e"+Integer.toString(e++)+" ");
                r.append("expr$ev expr$ek ");
                r.append("CALL_FUNCTION_VAR_KW_"+Integer.toString(i)+"_"+Integer.toString(j));
                r.append("    /. set(new Expr.Callfunction(");
                    r.append("s(1),new Ast[]{");
                    for(int p=0;p<i;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+1+1)+")");
                    }
                    r.append("},new Ast[]{");
                    for(int p=0;p<j;p++)
                    {
                        if(p!=0) r.append(",");
                        r.append("s("+Integer.toString(p+i+1+1)+")");
                    }
                    r.append("},s("+Integer.toString(i+j+1+1)+"),s("+Integer.toString(i+j+1+1+1)+")");
                    r.append(")); ./");
                r.append("\n");
            }
        }


        //String result = tab+"--terminals\n" + t.toString() + "\n\n" + tab+"--rules\n" + r.toString();
        String result =
                "--Dynamically generated stuff:\n"+
                "%Terminals\n"+
                t.toString()+
                "%End\n"+
                "%Rules\n"+
                r.toString()+
                "%End\n";
        return result;
    }
}

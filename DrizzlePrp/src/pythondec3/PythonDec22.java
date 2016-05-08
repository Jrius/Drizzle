package pythondec3;


    import lpg.runtime.*;
    //Dustin added
    //import pythondec3.ast.*;
    //Dustin added end

    //import lpg.runtime.*;
    //Dustin added
    import pythondec3.ast.*;
    //Dustin added end

public class PythonDec22 implements RuleAction
{
    private PrsStream prsStream = null;
    
    private boolean unimplementedSymbolsWarning = false;

    private static ParseTable prsTable = new PythonDec22prs();
    public ParseTable getParseTable() { return prsTable; }

    private BacktrackingParser btParser = null;
    public BacktrackingParser getParser() { return btParser; }

    private void setResult(Object object) { btParser.setSym1(object); }
    public Object getRhsSym(int i) { return btParser.getSym(i); }

    public int getRhsTokenIndex(int i) { return btParser.getToken(i); }
    public IToken getRhsIToken(int i) { return prsStream.getIToken(getRhsTokenIndex(i)); }
    
    public int getRhsFirstTokenIndex(int i) { return btParser.getFirstToken(i); }
    public IToken getRhsFirstIToken(int i) { return prsStream.getIToken(getRhsFirstTokenIndex(i)); }

    public int getRhsLastTokenIndex(int i) { return btParser.getLastToken(i); }
    public IToken getRhsLastIToken(int i) { return prsStream.getIToken(getRhsLastTokenIndex(i)); }

    public int getLeftSpan() { return btParser.getFirstToken(); }
    public IToken getLeftIToken()  { return prsStream.getIToken(getLeftSpan()); }

    public int getRightSpan() { return btParser.getLastToken(); }
    public IToken getRightIToken() { return prsStream.getIToken(getRightSpan()); }

    public int getRhsErrorTokenIndex(int i)
    {
        int index = btParser.getToken(i);
        IToken err = prsStream.getIToken(index);
        return (err instanceof ErrorToken ? index : 0);
    }
    public ErrorToken getRhsErrorIToken(int i)
    {
        int index = btParser.getToken(i);
        IToken err = prsStream.getIToken(index);
        return (ErrorToken) (err instanceof ErrorToken ? err : null);
    }

    public void reset(ILexStream lexStream)
    {
        prsStream = new PrsStream(lexStream);
        btParser.reset(prsStream);

        try
        {
            prsStream.remapTerminalSymbols(orderedTerminalSymbols(), prsTable.getEoftSymbol());
        }
        catch(NullExportedSymbolsException e) {
        }
        catch(NullTerminalSymbolsException e) {
        }
        catch(UnimplementedTerminalsException e)
        {
            if (unimplementedSymbolsWarning) {
                java.util.ArrayList unimplemented_symbols = e.getSymbols();
                System.out.println("The Lexer will not scan the following token(s):");
                for (int i = 0; i < unimplemented_symbols.size(); i++)
                {
                    Integer id = (Integer) unimplemented_symbols.get(i);
                    System.out.println("    " + PythonDec22sym.orderedTerminalSymbols[id.intValue()]);               
                }
                System.out.println();
            }
        }
        catch(UndefinedEofSymbolException e)
        {
            throw new Error(new UndefinedEofSymbolException
                                ("The Lexer does not implement the Eof symbol " +
                                 PythonDec22sym.orderedTerminalSymbols[prsTable.getEoftSymbol()]));
        } 
    }
    
    public PythonDec22()
    {
        try
        {
            btParser = new BacktrackingParser(prsStream, prsTable, (RuleAction) this);
        }
        catch (NotBacktrackParseTableException e)
        {
            throw new Error(new NotBacktrackParseTableException
                                ("Regenerate PythonDec22prs.java with -BACKTRACK option"));
        }
        catch (BadParseSymFileException e)
        {
            throw new Error(new BadParseSymFileException("Bad Parser Symbol File -- PythonDec22sym.java"));
        }
    }
    
    public PythonDec22(ILexStream lexStream)
    {
        this();
        reset(lexStream);
    }
    
    public int numTokenKinds() { return PythonDec22sym.numTokenKinds; }
    public String[] orderedTerminalSymbols() { return PythonDec22sym.orderedTerminalSymbols; }
    public String getTokenKindName(int kind) { return PythonDec22sym.orderedTerminalSymbols[kind]; }
    public int getEOFTokenKind() { return prsTable.getEoftSymbol(); }
    public IPrsStream getIPrsStream() { return prsStream; }

    /**
     * @deprecated replaced by {@link #getIPrsStream()}
     *
     */
    public PrsStream getPrsStream() { return prsStream; }

    /**
     * @deprecated replaced by {@link #getIPrsStream()}
     *
     */
    public PrsStream getParseStream() { return prsStream; }

    public Ast parser()
    {
        return parser(null, 0);
    }
    
    public Ast parser(Monitor monitor)
    {
        return parser(monitor, 0);
    }
    
    public Ast parser(int error_repair_count)
    {
        return parser(null, error_repair_count);
    }

    public Ast parser(Monitor monitor, int error_repair_count)
    {
        btParser.setMonitor(monitor);
        
        try
        {
            return (Ast) btParser.fuzzyParse(error_repair_count);
        }
        catch (BadParseException e)
        {
            //Dustin added
            if(true)
            {
                e.printStackTrace();
                return null;
            }
            //Dustin added end
        
            prsStream.reset(e.error_token); // point to error token

            DiagnoseParser diagnoseParser = new DiagnoseParser(prsStream, prsTable);
            diagnoseParser.diagnose(e.error_token);
        }

        return null;
    }

    //
    // Additional entry points, if any
    //
    
    
    //Dustin added
    private void set(Ast object)
    {
        btParser.setSym1(object);
    }
    private Ast s(int i)
    {
        Object sym = btParser.getSym(i);
        return (Ast)sym;
    }
    private Tok t(int i)
    {
        int token = btParser.getToken(i);
        Object sym = prsStream.getIToken(token); //esle returns the token.
        return (Tok)sym;
    }
    //private Ast get(int i)
    //{
    //    Object sym = btParser.getSym(i);
    //    if(sym!=null)
    //    {
    //        return (Ast)btParser.getSym(i); //return symbol if exists.
    //    }
    //    else
    //    {
    //        int token = btParser.getToken(i);
    //        return (Ast)prsStream.getIToken(token); //esle returns the token.
    //    }
    //}
    //Dustin added end
    

    public void ruleAction(int ruleNumber)
    {
        switch (ruleNumber)
        {

            //
            // Rule 1:  stmt_plus ::= stmt
            //
            case 1: {
               //#line2 194 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtList(s(1))); 
                break;
            }
            //
            // Rule 2:  stmt_plus ::= stmt_plus stmt
            //
            case 2: {
               //#line2 195 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 3:  stmt_star ::= $Empty
            //
            case 3: {
               //#line2 196 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtList()); 
                break;
            }
            //
            // Rule 4:  stmt_star ::= stmt_star stmt
            //
            case 4: {
               //#line2 197 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 5:  designator ::= STORE_NAME
            //
            case 5: {
               //#line2 201 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Storename(t(1))); 
                break;
            }
            //
            // Rule 6:  designator ::= expr STORE_ATTR
            //
            case 6: {
               //#line2 202 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Storeattr(s(1),t(2))); 
                break;
            }
            //
            // Rule 7:  designator ::= STORE_GLOBAL
            //
            case 7: {
               //#line2 203 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Storeglobal(t(1))); 
                break;
            }
            //
            // Rule 8:  designator ::= STORE_FAST
            //
            case 8: {
               //#line2 204 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Storefast(t(1))); 
                break;
            }
            //
            // Rule 9:  designator ::= expr expr$e2 STORE_SUBSCR
            //
            case 9: {
               //#line2 205 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Subscr(s(1),s(2))); 
                break;
            }
            //
            // Rule 10:  designator ::= expr STORE_SLICE_0
            //
            case 10: {
               //#line2 206 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Slice(s(1),null,null)); 
                break;
            }
            //
            // Rule 11:  designator ::= expr expr$e2 STORE_SLICE_1
            //
            case 11: {
               //#line2 207 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Slice(s(1),s(2),null)); 
                break;
            }
            //
            // Rule 12:  designator ::= expr expr$e2 STORE_SLICE_2
            //
            case 12: {
               //#line2 208 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Slice(s(1),null,s(2))); 
                break;
            }
            //
            // Rule 13:  designator ::= expr expr$e2 expr$e3 STORE_SLICE_3
            //
            case 13: {
               //#line2 209 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Slice(s(1),s(2),s(3))); 
                break;
            }
            //
            // Rule 14:  designator ::= STORE_DEREF
            //
            case 14: {
               //#line2 210 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.Name(t(1))); 
                break;
            }
            //
            // Rule 15:  expr ::= LOAD_NAME
            //
            case 15: {
               //#line2 212 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadname(t(1))); 
                break;
            }
            //
            // Rule 16:  expr ::= LOAD_CONST
            //
            case 16: {
               //#line2 213 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadconst(t(1))); 
                break;
            }
            //
            // Rule 17:  expr ::= LOAD_GLOBAL
            //
            case 17: {
               //#line2 214 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadglobal(t(1))); 
                break;
            }
            //
            // Rule 18:  expr ::= LOAD_LOCALS
            //
            case 18: {
               //#line2 215 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadlocals(t(1))); 
                break;
            }
            //
            // Rule 19:  expr ::= LOAD_FAST
            //
            case 19: {
               //#line2 216 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadfast(t(1))); 
                break;
            }
            //
            // Rule 20:  expr ::= expr LOAD_ATTR
            //
            case 20: {
               //#line2 217 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Loadattr(s(1),t(2))); 
                break;
            }
            //
            // Rule 21:  expr ::= expr expr$e2 BINARY_SUBSCR
            //
            case 21: {
               //#line2 218 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Binarysubscript(s(1),s(2))); 
                break;
            }
            //
            // Rule 22:  expr ::= LOAD_DEREF
            //
            case 22: {
               //#line2 219 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Name(t(1))); 
                break;
            }
            //
            // Rule 23:  stmt ::= expr expr$e2 inplace_op designator
            //
            case 23: {
               //#line2 223 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(s(1),s(2),s(3),s(4))); 
                break;
            }
            //
            // Rule 24:  stmt ::= expr DUP_TOP SLICE_0 expr$e2 inplace_op ROT_TWO STORE_SLICE_0
            //
            case 24: {
               //#line2 224 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,null),s(4),s(5))); 
                break;
            }
            //
            // Rule 25:  stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_1 expr$e7 inplace_op ROT_THREE STORE_SLICE_1
            //
            case 25: {
               //#line2 225 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),null),s(7),s(8))); 
                break;
            }
            //
            // Rule 26:  stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 SLICE_2 expr$e7 inplace_op ROT_THREE STORE_SLICE_2
            //
            case 26: {
               //#line2 226 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Slice(s(1),null,s(2)),s(7),s(8))); 
                break;
            }
            //
            // Rule 27:  stmt ::= expr expr$e2 expr$e3 DUP_TOPX EAT_EXPR EAT_EXPR$e6 EAT_EXPR$e7 SLICE_3 expr$e9 inplace_op ROT_FOUR STORE_SLICE_3
            //
            case 27: {
               //#line2 227 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Slice(s(1),s(2),s(3)),s(9),s(10))); 
                break;
            }
            //
            // Rule 28:  stmt ::= expr DUP_TOP LOAD_ATTR expr$e4 inplace_op ROT_TWO STORE_ATTR
            //
            case 28: {
               //#line2 228 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Attr(s(1),t(7)),s(4),s(5))); 
                break;
            }
            //
            // Rule 29:  stmt ::= expr expr$e2 DUP_TOPX EAT_EXPR EAT_EXPR$e5 BINARY_SUBSCR expr$e7 inplace_op ROT_THREE STORE_SUBSCR
            //
            case 29: {
               //#line2 229 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Inplace(new Dsgn.Subscr(s(1),s(2)),s(7),s(8))); 
                break;
            }
            //
            // Rule 30:  inplace_op ::= INPLACE_ADD
            //
            case 30: {
               //#line2 230 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 31:  inplace_op ::= INPLACE_SUBTRACT
            //
            case 31: {
               //#line2 231 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 32:  inplace_op ::= INPLACE_MULTIPLY
            //
            case 32: {
               //#line2 232 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 33:  inplace_op ::= INPLACE_DIVIDE
            //
            case 33: {
               //#line2 233 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 34:  inplace_op ::= INPLACE_TRUE_DIVIDE
            //
            case 34: {
               //#line2 234 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 35:  inplace_op ::= INPLACE_FLOOR_DIVIDE
            //
            case 35: {
               //#line2 235 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 36:  inplace_op ::= INPLACE_MODULO
            //
            case 36: {
               //#line2 236 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 37:  inplace_op ::= INPLACE_POWER
            //
            case 37: {
               //#line2 237 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 38:  inplace_op ::= INPLACE_LSHIFT
            //
            case 38: {
               //#line2 238 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 39:  inplace_op ::= INPLACE_RSHIFT
            //
            case 39: {
               //#line2 239 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 40:  inplace_op ::= INPLACE_AND
            //
            case 40: {
               //#line2 240 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 41:  inplace_op ::= INPLACE_XOR
            //
            case 41: {
               //#line2 241 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 42:  inplace_op ::= INPLACE_OR
            //
            case 42: {
               //#line2 242 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 43:  stmt ::= LOAD_CONST expr dyn_mkfunc CALL_FUNCTION BUILD_CLASS designator
            //
            case 43: {
               //#line2 246 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Classdef(t(1),s(2),s(3),s(6))); 
                break;
            }
            //
            // Rule 44:  stmt ::= LOAD_CONST expr closure CALL_FUNCTION BUILD_CLASS designator
            //
            case 44: {
               //#line2 247 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Classdef(t(1),s(2),s(3),s(6))); 
                break;
            }
            //
            // Rule 45:  expr ::= dyn_mkfunc
            //
            case 45: {
               //#line2 251 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 46:  expr ::= expr SLICE_0
            //
            case 46: {
               //#line2 255 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Slice(s(1),null,null)); 
                break;
            }
            //
            // Rule 47:  expr ::= expr expr$e2 SLICE_1
            //
            case 47: {
               //#line2 256 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Slice(s(1),s(2),null)); 
                break;
            }
            //
            // Rule 48:  expr ::= expr expr$e2 SLICE_2
            //
            case 48: {
               //#line2 257 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Slice(s(1),null,s(2))); 
                break;
            }
            //
            // Rule 49:  expr ::= expr expr$e2 expr$e3 SLICE_3
            //
            case 49: {
               //#line2 258 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Slice(s(1),s(2),s(3))); 
                break;
            }
            //
            // Rule 50:  expr ::= expr expr$e2 COMPARE_OP
            //
            case 50: {
               //#line2 261 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Compare(s(1),s(2),t(3))); 
                break;
            }
            //
            // Rule 51:  expr ::= expr cmp_list1 ROT_TWO POP_TOP LAND
            //
            case 51: {
               //#line2 262 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(new Expr.Complist.Comp(s(1),null))); 
                break;
            }
            //
            // Rule 52:  cmp_list1 ::= expr DUP_TOP ROT_THREE COMPARE_OP JUMP_IF_FALSE POP_TOP cmp_list1 LAND
            //
            case 52: {
               //#line2 263 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(7).add(new Expr.Complist.Comp(s(1),t(4)))); 
                break;
            }
            //
            // Rule 53:  cmp_list1 ::= expr DUP_TOP ROT_THREE COMPARE_OP JUMP_IF_FALSE POP_TOP cmp_list2 LAND
            //
            case 53: {
               //#line2 264 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(7).add(new Expr.Complist.Comp(s(1),t(4)))); 
                break;
            }
            //
            // Rule 54:  cmp_list2 ::= expr COMPARE_OP JUMP_FORWARD
            //
            case 54: {
               //#line2 265 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set((new Expr.Complist()).add(new Expr.Complist.Comp(s(1),t(2)))); 
                break;
            }
            //
            // Rule 55:  ifcmd ::= JUMP_IF_FALSE
            //
            case 55: {
               //#line2 268 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 56:  ifcmd ::= JUMP_IF_TRUE
            //
            case 56: {
               //#line2 269 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 57:  stmt ::= expr$a1 ifcmd$a2 POP_TOP stmt_star$a3 jump LAND POP_TOP$e7 stmt_star$a4 LAND$e9
            //
            case 57: {
               //#line2 271 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Ifelse(s(1),s(2),s(4),s(8))); 
                break;
            }
            //
            // Rule 58:  stmt ::= SETUP_LOOP land_star$e2 expr$a1 JUMP_IF_FALSE POP_TOP stmt_star$a2 JUMP_ABSOLUTE LAND POP_TOP$e8 POP_BLOCK stmt_star$a3 LAND$e11
            //
            case 58: {
               //#line2 277 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Whileelse(s(3),s(6),s(11))); 
                break;
            }
            //
            // Rule 59:  stmt ::= SETUP_LOOP JUMP_FORWARD JUMP_IF_FALSE POP_TOP land_plus$e5 stmt_star$a2 JUMP_ABSOLUTE LAND POP_TOP$e8 POP_BLOCK stmt_star$a3 LAND$e11
            //
            case 59: {
               //#line2 278 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Whileelse(null,s(6),s(11))); 
                break;
            }
            //
            // Rule 60:  stmt ::= BREAK_LOOP
            //
            case 60: {
               //#line2 281 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Break()); 
                break;
            }
            //
            // Rule 61:  stmt ::= continue_stmt
            //
            case 61: {
               //#line2 284 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 62:  continue_stmt ::= JUMP_ABSOLUTE
            //
            case 62: {
               //#line2 285 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Continue()); 
                break;
            }
            //
            // Rule 63:  continue_stmt ::= CONTINUE_LOOP
            //
            case 63: {
               //#line2 286 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Continue()); 
                break;
            }
            //
            // Rule 64:  land_plus ::= LAND
            //
            case 64: {
               //#line2 288 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List(t(1))); 
                break;
            }
            //
            // Rule 65:  land_plus ::= land_plus LAND
            //
            case 65: {
               //#line2 289 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(t(2))); 
                break;
            }
            //
            // Rule 66:  land_star ::= $Empty
            //
            case 66: {
               //#line2 290 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List()); 
                break;
            }
            //
            // Rule 67:  land_star ::= land_star LAND
            //
            case 67: {
               //#line2 291 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(t(2))); 
                break;
            }
            //
            // Rule 68:  h_looper ::= GET_ITER land_plus FOR_ITER
            //
            case 68: {
               //#line2 294 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Forelse(t(3))); 
                break;
            }
            //
            // Rule 69:  h_looper ::= LOAD_CONST FOR_LOOP
            //
            case 69: {
               //#line2 295 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Forelse(t(2))); 
                break;
            }
            //
            // Rule 70:  stmt ::= SETUP_LOOP expr h_looper designator stmt_star JUMP_ABSOLUTE LAND$e1 POP_BLOCK stmt_star$e2 LAND$e3
            //
            case 70: {
               //#line2 296 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Stmt.Forelse)s(3)).setvals(s(2),s(4),s(5),s(9))); 
                break;
            }
            //
            // Rule 71:  remover ::= designator
            //
            case 71: {
               //#line2 300 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 72:  remover ::= POP_TOP
            //
            case 72: {
               //#line2 301 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(null); 
                break;
            }
            //
            // Rule 73:  stmt ::= try1_stmt
            //
            case 73: {
               //#line2 302 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 74:  stmt ::= try2_stmt
            //
            case 74: {
               //#line2 303 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 75:  try2_stmt ::= SETUP_FINALLY stmt_star POP_BLOCK LOAD_CONST LAND stmt_star$e1 END_FINALLY
            //
            case 75: {
               //#line2 304 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Try.Tryfinally(s(2),t(4),s(6))); 
                break;
            }
            //
            // Rule 76:  except_cond ::= DUP_TOP expr$a1 COMPARE_OP$a2 JUMP_IF_FALSE POP_TOP POP_TOP$e1 remover$a3 POP_TOP$e2 stmt_star$a4 jump LAND POP_TOP$e3
            //
            case 76: {
               //#line2 305 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Try.Exceptcond(s(2),t(3),s(7),s(9))); 
                break;
            }
            //
            // Rule 77:  except ::= POP_TOP$a POP_TOP$b POP_TOP stmt_star jump
            //
            case 77: {
               //#line2 306 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Try.Except(s(4))); 
                break;
            }
            //
            // Rule 78:  try1_stmt ::= SETUP_EXCEPT stmt_star POP_BLOCK jump LAND$e5 exceptlist
            //
            case 78: {
               //#line2 308 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Stmt.Try.Tryexcept2)s(6)).setStmts(s(2))); 
                break;
            }
            //
            // Rule 79:  exceptlist ::= except exceptlist LAND
            //
            case 79: {
               //#line2 309 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 80:  exceptlist ::= except_cond exceptlist LAND
            //
            case 80: {
               //#line2 310 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 81:  exceptlist ::= END_FINALLY LAND stmt_star
            //
            case 81: {
               //#line2 311 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Try.Tryexcept2(s(3))); 
                break;
            }
            //
            // Rule 82:  stmt ::= LOAD_CONST importer
            //
            case 82: {
               //#line2 315 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import(t(1),null,s(2),null,1)); 
                break;
            }
            //
            // Rule 83:  stmt ::= LOAD_CONST IMPORT_NAME IMPORT_STAR
            //
            case 83: {
               //#line2 316 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import(t(1),t(2),null,null,2)); 
                break;
            }
            //
            // Rule 84:  stmt ::= LOAD_CONST IMPORT_NAME importer_plus POP_TOP
            //
            case 84: {
               //#line2 317 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import(t(1),t(2),null,s(3),3)); 
                break;
            }
            //
            // Rule 85:  importer_plus ::= importer
            //
            case 85: {
               //#line2 318 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List(s(1))); 
                break;
            }
            //
            // Rule 86:  importer_plus ::= importer_plus importer
            //
            case 86: {
               //#line2 319 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 87:  importer ::= IMPORT_NAME designator
            //
            case 87: {
               //#line2 320 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import.Importer(t(1),null,s(2))); 
                break;
            }
            //
            // Rule 88:  importer ::= IMPORT_NAME LOAD_ATTR designator
            //
            case 88: {
               //#line2 321 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import.Importer(t(1),t(2),s(3))); 
                break;
            }
            //
            // Rule 89:  importer ::= IMPORT_FROM designator
            //
            case 89: {
               //#line2 322 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Import.Importer2(t(1),s(2))); 
                break;
            }
            //
            // Rule 90:  expr ::= expr UNARY_CONVERT
            //
            case 90: {
               //#line2 325 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Unary(s(1),t(2))); 
                break;
            }
            //
            // Rule 91:  expr ::= expr UNARY_INVERT
            //
            case 91: {
               //#line2 326 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Unary(s(1),t(2))); 
                break;
            }
            //
            // Rule 92:  expr ::= expr UNARY_NEGATIVE
            //
            case 92: {
               //#line2 327 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Unary(s(1),t(2))); 
                break;
            }
            //
            // Rule 93:  expr ::= expr UNARY_NOT
            //
            case 93: {
               //#line2 328 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Unary(s(1),t(2))); 
                break;
            }
            //
            // Rule 94:  expr ::= expr UNARY_POSITIVE
            //
            case 94: {
               //#line2 329 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Unary(s(1),t(2))); 
                break;
            }
            //
            // Rule 96:  del_stmt ::= DELETE_GLOBAL
            //
            case 96: {
               //#line2 333 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Name(t(1)))); 
                break;
            }
            //
            // Rule 97:  del_stmt ::= expr expr$e2 DELETE_SUBSCR
            //
            case 97: {
               //#line2 334 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Subscr(s(1),s(2)))); 
                break;
            }
            //
            // Rule 98:  del_stmt ::= DELETE_NAME
            //
            case 98: {
               //#line2 335 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Name(t(1)))); 
                break;
            }
            //
            // Rule 99:  del_stmt ::= expr DELETE_ATTR
            //
            case 99: {
               //#line2 336 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Attr(s(1),t(2)))); 
                break;
            }
            //
            // Rule 100:  del_stmt ::= DELETE_FAST
            //
            case 100: {
               //#line2 337 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Name(t(1)))); 
                break;
            }
            //
            // Rule 101:  del_stmt ::= expr DELETE_SLICE_0
            //
            case 101: {
               //#line2 338 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Slice(s(1),null,null))); 
                break;
            }
            //
            // Rule 102:  del_stmt ::= expr expr$e2 DELETE_SLICE_1
            //
            case 102: {
               //#line2 339 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Slice(s(1),s(2),null))); 
                break;
            }
            //
            // Rule 103:  del_stmt ::= expr expr$e2 DELETE_SLICE_2
            //
            case 103: {
               //#line2 340 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Slice(s(1),null,s(2)))); 
                break;
            }
            //
            // Rule 104:  del_stmt ::= expr expr$e2 expr$e3 DELETE_SLICE_3
            //
            case 104: {
               //#line2 341 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Del(new Dsgn.Slice(s(1),s(2),s(3)))); 
                break;
            }
            //
            // Rule 105:  expr ::= BUILD_MAP dictentry_star
            //
            case 105: {
               //#line2 344 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Dictionary(s(2))); 
                break;
            }
            //
            // Rule 106:  dictentry_star ::= $Empty
            //
            case 106: {
               //#line2 345 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List()); 
                break;
            }
            //
            // Rule 107:  dictentry_star ::= dictentry_star dictentry
            //
            case 107: {
               //#line2 346 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 108:  dictentry ::= DUP_TOP expr ROT_TWO expr$e2 STORE_SUBSCR
            //
            case 108: {
               //#line2 347 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Dictionary.Entry(s(2),s(4))); 
                break;
            }
            //
            // Rule 109:  dictentry ::= DUP_TOP expr expr$e2 ROT_THREE STORE_SUBSCR
            //
            case 109: {
               //#line2 348 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Dictionary.Entry(s(3),s(2))); 
                break;
            }
            //
            // Rule 110:  stmt ::= expr POP_TOP
            //
            case 110: {
               //#line2 351 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Exprstmt(s(1))); 
                break;
            }
            //
            // Rule 111:  expr ::= expr JUMP_IF_TRUE POP_TOP expr$e2 LAND
            //
            case 111: {
               //#line2 354 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.ShortcircuitOr(s(1),s(4))); 
                break;
            }
            //
            // Rule 112:  expr ::= expr JUMP_IF_FALSE POP_TOP expr$e2 LAND
            //
            case 112: {
               //#line2 355 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.ShortcircuitAnd(s(1),s(4))); 
                break;
            }
            //
            // Rule 113:  expr ::= jump JUMP_IF_FALSE POP_TOP LAND expr$a2 LAND$e6
            //
            case 113: {
               //#line2 356 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.ShortcircuitAnd(null,s(5))); 
                break;
            }
            //
            // Rule 114:  stmt ::= expr expr$e2 expr$e3 EXEC_STMT
            //
            case 114: {
               //#line2 360 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Exec(s(1),s(2),s(3))); 
                break;
            }
            //
            // Rule 115:  stmt ::= expr expr$e2 DUP_TOP EXEC_STMT
            //
            case 115: {
               //#line2 361 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Exec(s(1),s(2),null)); 
                break;
            }
            //
            // Rule 116:  stmt ::= expr YIELD_STMT
            //
            case 116: {
               //#line2 364 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Yield(s(1))); 
                break;
            }
            //
            // Rule 117:  stmt ::= expr JUMP_IF_FALSE POP_TOP expr$e4 JUMP_IF_TRUE POP_TOP$e6 LOAD_GLOBAL RAISE_VARARGS EAT_EXPR LAND LAND$e11 POP_TOP$e12
            //
            case 117: {
               //#line2 367 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Assert(s(1),s(4),t(7),null)); 
                break;
            }
            //
            // Rule 118:  stmt ::= expr JUMP_IF_FALSE POP_TOP expr$e4 JUMP_IF_TRUE POP_TOP$e6 LOAD_GLOBAL expr$e8 RAISE_VARARGS EAT_EXPR EAT_EXPR$e12 LAND LAND$e13 POP_TOP$e14
            //
            case 118: {
               //#line2 368 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Assert(s(1),s(4),t(7),s(8))); 
                break;
            }
            //
            // Rule 119:  expr ::= closure
            //
            case 119: {
               //#line2 371 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 120:  closure ::= LOAD_CLOSURE LOAD_CONST MAKE_CLOSURE EAT_LOADCLOSURE
            //
            case 120: {
               //#line2 372 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set((new ExprClosure(t(2))).addClosure(t(1))); 
                break;
            }
            //
            // Rule 121:  closure ::= LOAD_CLOSURE closure EAT_LOADCLOSURE
            //
            case 121: {
               //#line2 373 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((ExprClosure)s(2)).addClosure(t(1))); 
                break;
            }
            //
            // Rule 122:  closure ::= expr closure EAT_EXPR
            //
            case 122: {
               //#line2 374 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((ExprClosure)s(2)).addExpr(s(1))); 
                break;
            }
            //
            // Rule 123:  expr ::= expr expr$e2 BUILD_SLICE EAT_EXPR EAT_EXPR$e5
            //
            case 123: {
               //#line2 377 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildslice(s(1),s(2),null)); 
                break;
            }
            //
            // Rule 124:  expr ::= expr expr$e2 expr$e3 BUILD_SLICE EAT_EXPR EAT_EXPR$e6 EAT_EXPR$e7
            //
            case 124: {
               //#line2 378 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildslice(s(1),s(2),s(3))); 
                break;
            }
            //
            // Rule 125:  stmt ::= expr$val h_assign_star$targets designator$var
            //
            case 125: {
               //#line2 383 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtAssign(s(1),s(2).add(s(3)))); 
                break;
            }
            //
            // Rule 126:  h_assign_star ::= $Empty
            //
            case 126: {
               //#line2 384 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List()); 
                break;
            }
            //
            // Rule 127:  h_assign_star ::= h_assign_star$ls h_assign$it
            //
            case 127: {
               //#line2 385 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 128:  h_assign ::= DUP_TOP designator$des
            //
            case 128: {
               //#line2 386 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2)); 
                break;
            }
            //
            // Rule 129:  stmt ::= expr RETURN_VALUE
            //
            case 129: {
               //#line2 389 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtReturn(s(1))); 
                break;
            }
            //
            // Rule 130:  expr ::= expr expr$e2 binary_op
            //
            case 130: {
               //#line2 392 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Binary(s(1),s(2),s(3))); 
                break;
            }
            //
            // Rule 131:  binary_op ::= BINARY_ADD
            //
            case 131: {
               //#line2 393 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 132:  binary_op ::= BINARY_SUBTRACT
            //
            case 132: {
               //#line2 394 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 133:  binary_op ::= BINARY_MULTIPLY
            //
            case 133: {
               //#line2 395 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 134:  binary_op ::= BINARY_DIVIDE
            //
            case 134: {
               //#line2 396 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 135:  binary_op ::= BINARY_TRUE_DIVIDE
            //
            case 135: {
               //#line2 397 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 136:  binary_op ::= BINARY_FLOOR_DIVIDE
            //
            case 136: {
               //#line2 398 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 137:  binary_op ::= BINARY_MODULO
            //
            case 137: {
               //#line2 399 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 138:  binary_op ::= BINARY_LSHIFT
            //
            case 138: {
               //#line2 400 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 139:  binary_op ::= BINARY_RSHIFT
            //
            case 139: {
               //#line2 401 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 140:  binary_op ::= BINARY_AND
            //
            case 140: {
               //#line2 402 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 141:  binary_op ::= BINARY_OR
            //
            case 141: {
               //#line2 403 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 142:  binary_op ::= BINARY_XOR
            //
            case 142: {
               //#line2 404 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 143:  binary_op ::= BINARY_POWER
            //
            case 143: {
               //#line2 405 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 144:  binary_op ::= BINARY_VALUE
            //
            case 144: {
               //#line2 406 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 145:  stmt ::= PRINT_NEWLINE
            //
            case 145: {
               //#line2 410 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(null,null,true)); 
                break;
            }
            //
            // Rule 146:  stmt ::= expr$a PRINT_ITEM PRINT_NEWLINE
            //
            case 146: {
               //#line2 411 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(s(1),null,true)); 
                break;
            }
            //
            // Rule 147:  stmt ::= expr$a PRINT_ITEM
            //
            case 147: {
               //#line2 412 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(s(1),null,false)); 
                break;
            }
            //
            // Rule 148:  stmt ::= expr$a h_pt_plus$b PRINT_NEWLINE_TO
            //
            case 148: {
               //#line2 413 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(s(2),s(1),true)); 
                break;
            }
            //
            // Rule 149:  stmt ::= expr$a h_pt_plus$b POP_TOP
            //
            case 149: {
               //#line2 414 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(s(2),s(1),false)); 
                break;
            }
            //
            // Rule 150:  stmt ::= expr$b PRINT_NEWLINE_TO
            //
            case 150: {
               //#line2 415 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new StmtPrint(null,new List(s(1)),true)); 
                break;
            }
            //
            // Rule 151:  h_pt_plus ::= h_pt$it
            //
            case 151: {
               //#line2 416 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new List(s(1))); 
                break;
            }
            //
            // Rule 152:  h_pt_plus ::= h_pt_plus$ls h_pt$it
            //
            case 152: {
               //#line2 417 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1).add(s(2))); 
                break;
            }
            //
            // Rule 153:  h_pt ::= DUP_TOP expr$ex ROT_TWO PRINT_ITEM_TO
            //
            case 153: {
               //#line2 418 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2)); 
                break;
            }
            //
            // Rule 154:  stmt ::= dyn_raiseva
            //
            case 154: {
               //#line2 421 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 155:  dyn_raiseva ::= RAISE_VARARGS
            //
            case 155: {
               //#line2 422 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Stmt.Raisevarargs()); 
                break;
            }
            //
            // Rule 156:  dyn_raiseva ::= expr dyn_raiseva EAT_EXPR
            //
            case 156: {
               //#line2 423 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 157:  designator ::= dyn_unpack
            //
            case 157: {
               //#line2 426 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 158:  dyn_unpack ::= UNPACK_SEQUENCE
            //
            case 158: {
               //#line2 427 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Dsgn.UnpackSequence()); 
                break;
            }
            //
            // Rule 159:  dyn_unpack ::= ANTIEAT_DESIG dyn_unpack designator
            //
            case 159: {
               //#line2 428 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(3))); 
                break;
            }
            //
            // Rule 160:  expr ::= dyn_list_expr
            //
            case 160: {
               //#line2 430 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 161:  dyn_list_expr ::= BUILD_LIST
            //
            case 161: {
               //#line2 431 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildlist()); 
                break;
            }
            //
            // Rule 162:  dyn_list_expr ::= expr dyn_list_expr EAT_EXPR
            //
            case 162: {
               //#line2 432 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 163:  expr ::= dyn_tuple_expr
            //
            case 163: {
               //#line2 433 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 164:  dyn_tuple_expr ::= BUILD_TUPLE
            //
            case 164: {
               //#line2 434 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildtuple()); 
                break;
            }
            //
            // Rule 165:  dyn_tuple_expr ::= expr dyn_tuple_expr EAT_EXPR
            //
            case 165: {
               //#line2 435 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 166:  dyn_list_expr ::= EXPR_LIST BUILD_LIST
            //
            case 166: {
               //#line2 436 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildlist((new ExprList(t(1))).getvals())); 
                break;
            }
            //
            // Rule 167:  dyn_tuple_expr ::= EXPR_LIST BUILD_TUPLE
            //
            case 167: {
               //#line2 437 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Buildtuple((new ExprList(t(1))).getvals())); 
                break;
            }
            //
            // Rule 168:  dyn_mkfunc ::= LOAD_CONST_MAKE_FUNCTION
            //
            case 168: {
               //#line2 445 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Makefunction(t(1))); 
                break;
            }
            //
            // Rule 169:  dyn_mkfunc ::= expr dyn_mkfunc EAT_EXPR
            //
            case 169: {
               //#line2 446 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(2).add(s(1))); 
                break;
            }
            //
            // Rule 170:  expr ::= dyn_callfunc
            //
            case 170: {
               //#line2 458 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 171:  dyn_callfunc2 ::= CALL_FUNCTION
            //
            case 171: {
               //#line2 459 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Callfunction(null,null)); 
                break;
            }
            //
            // Rule 172:  dyn_callfunc2 ::= expr dyn_callfunc2 EAT_EXPR
            //
            case 172: {
               //#line2 460 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Expr.Callfunction)s(2)).addPoarg(s(1))); 
                break;
            }
            //
            // Rule 173:  dyn_callfunc ::= expr dyn_callfunc2
            //
            case 173: {
               //#line2 461 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Expr.Callfunction)s(2)).setName(s(1))); 
                break;
            }
            //
            // Rule 174:  dyn_callfunc2 ::= LOAD_CONST expr dyn_callfunc2 EAT_KWARG
            //
            case 174: {
               //#line2 463 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Expr.Callfunction)s(3)).addKwarg(new Helpers.Kwarg(t(1),s(2)))); 
                break;
            }
            //
            // Rule 175:  dyn_callfunc2 ::= expr CALL_FUNCTION_VAR
            //
            case 175: {
               //#line2 464 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Callfunction(s(1),null)); 
                break;
            }
            //
            // Rule 176:  dyn_callfunc2 ::= expr CALL_FUNCTION_KW
            //
            case 176: {
               //#line2 465 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Callfunction(null,s(1))); 
                break;
            }
            //
            // Rule 177:  dyn_callfunc2 ::= expr expr$e2 CALL_FUNCTION_VAR_KW
            //
            case 177: {
               //#line2 466 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Callfunction(s(1),s(2))); 
                break;
            }
            //
            // Rule 178:  expr ::= BUILD_LIST DUP_TOP LOAD_ATTR designator list_for del_stmt
            //
            case 178: {
               //#line2 482 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(((Expr.Listfor)s(5)).setInfo(t(3),s(4),s(6))); 
                break;
            }
            //
            // Rule 179:  list_iter ::= list_for
            //
            case 179: {
               //#line2 483 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 180:  list_iter ::= list_if
            //
            case 180: {
               //#line2 484 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 181:  list_iter ::= lc_body
            //
            case 181: {
               //#line2 485 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(1)); 
                break;
            }
            //
            // Rule 182:  list_for ::= expr h_looper designator list_iter JUMP_ABSOLUTE LAND
            //
            case 182: {
               //#line2 486 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(4).add(new Expr.Listfor.For(s(1),s(2),s(3)))); 
                break;
            }
            //
            // Rule 183:  list_if ::= expr ifcmd POP_TOP list_iter jump LAND POP_TOP$e7 LAND$e8
            //
            case 183: {
               //#line2 487 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(s(4).add(new Expr.Listfor.If(s(1),s(2)))); 
                break;
            }
            //
            // Rule 184:  lc_body ::= LOAD_NAME expr CALL_FUNCTION EAT_EXPR POP_TOP
            //
            case 184: {
               //#line2 488 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Listfor(t(1),s(2))); 
                break;
            }
            //
            // Rule 185:  lc_body ::= LOAD_FAST expr CALL_FUNCTION EAT_EXPR POP_TOP
            //
            case 185: {
               //#line2 489 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(new Expr.Listfor(t(1),s(2))); 
                break;
            }
            //
            // Rule 186:  jump ::= JUMP_FORWARD
            //
            case 186: {
               //#line2 491 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
            //
            // Rule 187:  jump ::= JUMP_ABSOLUTE
            //
            case 187: {
               //#line2 492 "C:/Documents and Settings/user/Desktop/drizzle/DrizzlePrp/grammars/./btParserTemplateDust.gi"
                
                 set(t(1)); 
                break;
            }
    
            default:
                break;
        }
        return;
    }
}


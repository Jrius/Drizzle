package parsers.gen;


    import lpg.runtime.*;
    //Dustin added
    //import pythondec3.ast.*;
    //Dustin added end

    import parsers.ast.*;

public class SDL implements RuleAction
{
    private PrsStream prsStream = null;
    
    private boolean unimplementedSymbolsWarning = false;

    private static ParseTable prsTable = new SDLprs();
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
                    System.out.println("    " + SDLsym.orderedTerminalSymbols[id.intValue()]);               
                }
                System.out.println();
            }
        }
        catch(UndefinedEofSymbolException e)
        {
            throw new Error(new UndefinedEofSymbolException
                                ("The Lexer does not implement the Eof symbol " +
                                 SDLsym.orderedTerminalSymbols[prsTable.getEoftSymbol()]));
        } 
    }
    
    public SDL()
    {
        try
        {
            btParser = new BacktrackingParser(prsStream, prsTable, (RuleAction) this);
        }
        catch (NotBacktrackParseTableException e)
        {
            throw new Error(new NotBacktrackParseTableException
                                ("Regenerate SDLprs.java with -BACKTRACK option"));
        }
        catch (BadParseSymFileException e)
        {
            throw new Error(new BadParseSymFileException("Bad Parser Symbol File -- SDLsym.java"));
        }
    }
    
    public SDL(ILexStream lexStream)
    {
        this();
        reset(lexStream);
    }
    
    public int numTokenKinds() { return SDLsym.numTokenKinds; }
    public String[] orderedTerminalSymbols() { return SDLsym.orderedTerminalSymbols; }
    public String getTokenKindName(int kind) { return SDLsym.orderedTerminalSymbols[kind]; }
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

    
            default:
                break;
        }
        return;
    }
}


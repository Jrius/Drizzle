// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g 2010-05-31 12:36:44

    import parsers.sdlparser.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class SDLgrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "STATEDESC", "LEFTCURLY", "RIGHTCURLY", "LEFTPARENS", "RIGHTPARENS", "LEFTSQUARE", "RIGHTSQUARE", "EQUALS", "DOLLAR", "COMMA", "SEMICOLON", "VERSION", "VAR", "DEFAULT", "DISPLAYOPTION", "DEFAULTOPTION", "NAME", "NATNUM", "COMMENT", "WS", "STRING", "'-'", "'.'"
    };
    public static final int DOLLAR=38;
    public static final int NATNUM=47;
    public static final int LEFTPARENS=33;
    public static final int EQUALS=37;
    public static final int VERSION=41;
    public static final int EOF=-1;
    public static final int STATEDESC=30;
    public static final int NAME=46;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int LEFTCURLY=31;
    public static final int COMMA=39;
    public static final int RIGHTCURLY=32;
    public static final int VAR=42;
    public static final int COMMENT=48;
    public static final int D=7;
    public static final int E=8;
    public static final int F=9;
    public static final int G=10;
    public static final int A=4;
    public static final int B=5;
    public static final int C=6;
    public static final int L=15;
    public static final int M=16;
    public static final int N=17;
    public static final int RIGHTSQUARE=36;
    public static final int O=18;
    public static final int H=11;
    public static final int I=12;
    public static final int DEFAULT=43;
    public static final int J=13;
    public static final int K=14;
    public static final int U=24;
    public static final int T=23;
    public static final int W=26;
    public static final int V=25;
    public static final int Q=20;
    public static final int SEMICOLON=40;
    public static final int P=19;
    public static final int S=22;
    public static final int R=21;
    public static final int Y=28;
    public static final int X=27;
    public static final int Z=29;
    public static final int WS=49;
    public static final int DISPLAYOPTION=44;
    public static final int LEFTSQUARE=35;
    public static final int RIGHTPARENS=34;
    public static final int DEFAULTOPTION=45;
    public static final int STRING=50;

    // delegates
    // delegators


        public SDLgrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SDLgrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return SDLgrammarParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g"; }


    public static class floatnum_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "floatnum"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:60:1: floatnum : ( '-' )? NATNUM '.' NATNUM ;
    public final SDLgrammarParser.floatnum_return floatnum() throws RecognitionException {
        SDLgrammarParser.floatnum_return retval = new SDLgrammarParser.floatnum_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token char_literal1=null;
        Token NATNUM2=null;
        Token char_literal3=null;
        Token NATNUM4=null;

        Tree char_literal1_tree=null;
        Tree NATNUM2_tree=null;
        Tree char_literal3_tree=null;
        Tree NATNUM4_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:60:9: ( ( '-' )? NATNUM '.' NATNUM )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:60:11: ( '-' )? NATNUM '.' NATNUM
            {
            root_0 = (Tree)adaptor.nil();

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:60:11: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==51) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:60:11: '-'
                    {
                    char_literal1=(Token)match(input,51,FOLLOW_51_in_floatnum565); 
                    char_literal1_tree = (Tree)adaptor.create(char_literal1);
                    adaptor.addChild(root_0, char_literal1_tree);


                    }
                    break;

            }

            NATNUM2=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_floatnum568); 
            NATNUM2_tree = (Tree)adaptor.create(NATNUM2);
            adaptor.addChild(root_0, NATNUM2_tree);

            char_literal3=(Token)match(input,52,FOLLOW_52_in_floatnum570); 
            char_literal3_tree = (Tree)adaptor.create(char_literal3);
            adaptor.addChild(root_0, char_literal3_tree);

            NATNUM4=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_floatnum572); 
            NATNUM4_tree = (Tree)adaptor.create(NATNUM4);
            adaptor.addChild(root_0, NATNUM4_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "floatnum"

    public static class sdlfile_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sdlfile"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:86:1: sdlfile : ( statedesc )* ->;
    public final SDLgrammarParser.sdlfile_return sdlfile() throws RecognitionException {
        SDLgrammarParser.sdlfile_return retval = new SDLgrammarParser.sdlfile_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        SDLgrammarParser.statedesc_return statedesc5 = null;


        RewriteRuleSubtreeStream stream_statedesc=new RewriteRuleSubtreeStream(adaptor,"rule statedesc");
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:86:8: ( ( statedesc )* ->)
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:86:10: ( statedesc )*
            {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:86:10: ( statedesc )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==STATEDESC) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:86:10: statedesc
            	    {
            	    pushFollow(FOLLOW_statedesc_in_sdlfile742);
            	    statedesc5=statedesc();

            	    state._fsp--;

            	    stream_statedesc.add(statedesc5.getTree());

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);



            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 86:21: ->
            {
                adaptor.addChild(root_0,  new Integer(5) );

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sdlfile"

    public static class statedesc_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statedesc"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:1: statedesc : STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY -> ^( STATEDESC NAME NATNUM ( varline )* ) ;
    public final SDLgrammarParser.statedesc_return statedesc() throws RecognitionException {
        SDLgrammarParser.statedesc_return retval = new SDLgrammarParser.statedesc_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token STATEDESC6=null;
        Token NAME7=null;
        Token LEFTCURLY8=null;
        Token VERSION9=null;
        Token NATNUM10=null;
        Token RIGHTCURLY12=null;
        SDLgrammarParser.varline_return varline11 = null;


        Tree STATEDESC6_tree=null;
        Tree NAME7_tree=null;
        Tree LEFTCURLY8_tree=null;
        Tree VERSION9_tree=null;
        Tree NATNUM10_tree=null;
        Tree RIGHTCURLY12_tree=null;
        RewriteRuleTokenStream stream_RIGHTCURLY=new RewriteRuleTokenStream(adaptor,"token RIGHTCURLY");
        RewriteRuleTokenStream stream_NAME=new RewriteRuleTokenStream(adaptor,"token NAME");
        RewriteRuleTokenStream stream_VERSION=new RewriteRuleTokenStream(adaptor,"token VERSION");
        RewriteRuleTokenStream stream_LEFTCURLY=new RewriteRuleTokenStream(adaptor,"token LEFTCURLY");
        RewriteRuleTokenStream stream_NATNUM=new RewriteRuleTokenStream(adaptor,"token NATNUM");
        RewriteRuleTokenStream stream_STATEDESC=new RewriteRuleTokenStream(adaptor,"token STATEDESC");
        RewriteRuleSubtreeStream stream_varline=new RewriteRuleSubtreeStream(adaptor,"rule varline");
        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:10: ( STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY -> ^( STATEDESC NAME NATNUM ( varline )* ) )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:12: STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY
            {
            STATEDESC6=(Token)match(input,STATEDESC,FOLLOW_STATEDESC_in_statedesc753);  
            stream_STATEDESC.add(STATEDESC6);

            NAME7=(Token)match(input,NAME,FOLLOW_NAME_in_statedesc755);  
            stream_NAME.add(NAME7);

            LEFTCURLY8=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_statedesc757);  
            stream_LEFTCURLY.add(LEFTCURLY8);

            VERSION9=(Token)match(input,VERSION,FOLLOW_VERSION_in_statedesc759);  
            stream_VERSION.add(VERSION9);

            NATNUM10=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_statedesc761);  
            stream_NATNUM.add(NATNUM10);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:52: ( varline )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==VAR) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:52: varline
            	    {
            	    pushFollow(FOLLOW_varline_in_statedesc763);
            	    varline11=varline();

            	    state._fsp--;

            	    stream_varline.add(varline11.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            RIGHTCURLY12=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_statedesc766);  
            stream_RIGHTCURLY.add(RIGHTCURLY12);



            // AST REWRITE
            // elements: STATEDESC, NATNUM, NAME, varline
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Tree)adaptor.nil();
            // 87:72: -> ^( STATEDESC NAME NATNUM ( varline )* )
            {
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:75: ^( STATEDESC NAME NATNUM ( varline )* )
                {
                Tree root_1 = (Tree)adaptor.nil();
                root_1 = (Tree)adaptor.becomeRoot(stream_STATEDESC.nextNode(), root_1);

                adaptor.addChild(root_1, stream_NAME.nextNode());
                adaptor.addChild(root_1, stream_NATNUM.nextNode());
                // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:87:99: ( varline )*
                while ( stream_varline.hasNext() ) {
                    adaptor.addChild(root_1, stream_varline.nextTree());

                }
                stream_varline.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statedesc"

    public static class varline_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "varline"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:1: varline : VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )? ;
    public final SDLgrammarParser.varline_return varline() throws RecognitionException {
        SDLgrammarParser.varline_return retval = new SDLgrammarParser.varline_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token VAR13=null;
        Token NAME15=null;
        Token LEFTSQUARE16=null;
        Token NATNUM17=null;
        Token RIGHTSQUARE18=null;
        Token SEMICOLON20=null;
        SDLgrammarParser.type_return type14 = null;

        SDLgrammarParser.option_return option19 = null;


        Tree VAR13_tree=null;
        Tree NAME15_tree=null;
        Tree LEFTSQUARE16_tree=null;
        Tree NATNUM17_tree=null;
        Tree RIGHTSQUARE18_tree=null;
        Tree SEMICOLON20_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:8: ( VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )? )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:10: VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )?
            {
            root_0 = (Tree)adaptor.nil();

            VAR13=(Token)match(input,VAR,FOLLOW_VAR_in_varline787); 
            VAR13_tree = (Tree)adaptor.create(VAR13);
            adaptor.addChild(root_0, VAR13_tree);

            pushFollow(FOLLOW_type_in_varline789);
            type14=type();

            state._fsp--;

            adaptor.addChild(root_0, type14.getTree());
            NAME15=(Token)match(input,NAME,FOLLOW_NAME_in_varline791); 
            NAME15_tree = (Tree)adaptor.create(NAME15);
            adaptor.addChild(root_0, NAME15_tree);

            LEFTSQUARE16=(Token)match(input,LEFTSQUARE,FOLLOW_LEFTSQUARE_in_varline793); 
            LEFTSQUARE16_tree = (Tree)adaptor.create(LEFTSQUARE16);
            adaptor.addChild(root_0, LEFTSQUARE16_tree);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:35: ( NATNUM | )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==NATNUM) ) {
                alt4=1;
            }
            else if ( (LA4_0==RIGHTSQUARE) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:36: NATNUM
                    {
                    NATNUM17=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_varline796); 
                    NATNUM17_tree = (Tree)adaptor.create(NATNUM17);
                    adaptor.addChild(root_0, NATNUM17_tree);


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:43: 
                    {
                    }
                    break;

            }

            RIGHTSQUARE18=(Token)match(input,RIGHTSQUARE,FOLLOW_RIGHTSQUARE_in_varline800); 
            RIGHTSQUARE18_tree = (Tree)adaptor.create(RIGHTSQUARE18);
            adaptor.addChild(root_0, RIGHTSQUARE18_tree);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:57: ( option )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=DEFAULT && LA5_0<=DEFAULTOPTION)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:57: option
            	    {
            	    pushFollow(FOLLOW_option_in_varline802);
            	    option19=option();

            	    state._fsp--;

            	    adaptor.addChild(root_0, option19.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:65: ( SEMICOLON )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==SEMICOLON) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:89:65: SEMICOLON
                    {
                    SEMICOLON20=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_varline805); 
                    SEMICOLON20_tree = (Tree)adaptor.create(SEMICOLON20);
                    adaptor.addChild(root_0, SEMICOLON20_tree);


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "varline"

    public static class type_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "type"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:90:1: type : ( NAME | DOLLAR NAME );
    public final SDLgrammarParser.type_return type() throws RecognitionException {
        SDLgrammarParser.type_return retval = new SDLgrammarParser.type_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token NAME21=null;
        Token DOLLAR22=null;
        Token NAME23=null;

        Tree NAME21_tree=null;
        Tree DOLLAR22_tree=null;
        Tree NAME23_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:90:5: ( NAME | DOLLAR NAME )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==NAME) ) {
                alt7=1;
            }
            else if ( (LA7_0==DOLLAR) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:90:7: NAME
                    {
                    root_0 = (Tree)adaptor.nil();

                    NAME21=(Token)match(input,NAME,FOLLOW_NAME_in_type812); 
                    NAME21_tree = (Tree)adaptor.create(NAME21);
                    adaptor.addChild(root_0, NAME21_tree);


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:90:14: DOLLAR NAME
                    {
                    root_0 = (Tree)adaptor.nil();

                    DOLLAR22=(Token)match(input,DOLLAR,FOLLOW_DOLLAR_in_type816); 
                    DOLLAR22_tree = (Tree)adaptor.create(DOLLAR22);
                    adaptor.addChild(root_0, DOLLAR22_tree);

                    NAME23=(Token)match(input,NAME,FOLLOW_NAME_in_type818); 
                    NAME23_tree = (Tree)adaptor.create(NAME23);
                    adaptor.addChild(root_0, NAME23_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "type"

    public static class option_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "option"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:92:1: option : ( DEFAULT EQUALS value | DEFAULTOPTION EQUALS value | DISPLAYOPTION EQUALS value );
    public final SDLgrammarParser.option_return option() throws RecognitionException {
        SDLgrammarParser.option_return retval = new SDLgrammarParser.option_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token DEFAULT24=null;
        Token EQUALS25=null;
        Token DEFAULTOPTION27=null;
        Token EQUALS28=null;
        Token DISPLAYOPTION30=null;
        Token EQUALS31=null;
        SDLgrammarParser.value_return value26 = null;

        SDLgrammarParser.value_return value29 = null;

        SDLgrammarParser.value_return value32 = null;


        Tree DEFAULT24_tree=null;
        Tree EQUALS25_tree=null;
        Tree DEFAULTOPTION27_tree=null;
        Tree EQUALS28_tree=null;
        Tree DISPLAYOPTION30_tree=null;
        Tree EQUALS31_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:92:7: ( DEFAULT EQUALS value | DEFAULTOPTION EQUALS value | DISPLAYOPTION EQUALS value )
            int alt8=3;
            switch ( input.LA(1) ) {
            case DEFAULT:
                {
                alt8=1;
                }
                break;
            case DEFAULTOPTION:
                {
                alt8=2;
                }
                break;
            case DISPLAYOPTION:
                {
                alt8=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:92:9: DEFAULT EQUALS value
                    {
                    root_0 = (Tree)adaptor.nil();

                    DEFAULT24=(Token)match(input,DEFAULT,FOLLOW_DEFAULT_in_option825); 
                    DEFAULT24_tree = (Tree)adaptor.create(DEFAULT24);
                    adaptor.addChild(root_0, DEFAULT24_tree);

                    EQUALS25=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_option827); 
                    EQUALS25_tree = (Tree)adaptor.create(EQUALS25);
                    adaptor.addChild(root_0, EQUALS25_tree);

                    pushFollow(FOLLOW_value_in_option829);
                    value26=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value26.getTree());

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:92:32: DEFAULTOPTION EQUALS value
                    {
                    root_0 = (Tree)adaptor.nil();

                    DEFAULTOPTION27=(Token)match(input,DEFAULTOPTION,FOLLOW_DEFAULTOPTION_in_option833); 
                    DEFAULTOPTION27_tree = (Tree)adaptor.create(DEFAULTOPTION27);
                    adaptor.addChild(root_0, DEFAULTOPTION27_tree);

                    EQUALS28=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_option835); 
                    EQUALS28_tree = (Tree)adaptor.create(EQUALS28);
                    adaptor.addChild(root_0, EQUALS28_tree);

                    pushFollow(FOLLOW_value_in_option837);
                    value29=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value29.getTree());

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:92:61: DISPLAYOPTION EQUALS value
                    {
                    root_0 = (Tree)adaptor.nil();

                    DISPLAYOPTION30=(Token)match(input,DISPLAYOPTION,FOLLOW_DISPLAYOPTION_in_option841); 
                    DISPLAYOPTION30_tree = (Tree)adaptor.create(DISPLAYOPTION30);
                    adaptor.addChild(root_0, DISPLAYOPTION30_tree);

                    EQUALS31=(Token)match(input,EQUALS,FOLLOW_EQUALS_in_option843); 
                    EQUALS31_tree = (Tree)adaptor.create(EQUALS31);
                    adaptor.addChild(root_0, EQUALS31_tree);

                    pushFollow(FOLLOW_value_in_option845);
                    value32=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value32.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "option"

    public static class value_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:1: value : ( NAME | NATNUM | floatnum | STRING | tuple );
    public final SDLgrammarParser.value_return value() throws RecognitionException {
        SDLgrammarParser.value_return retval = new SDLgrammarParser.value_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token NAME33=null;
        Token NATNUM34=null;
        Token STRING36=null;
        SDLgrammarParser.floatnum_return floatnum35 = null;

        SDLgrammarParser.tuple_return tuple37 = null;


        Tree NAME33_tree=null;
        Tree NATNUM34_tree=null;
        Tree STRING36_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:6: ( NAME | NATNUM | floatnum | STRING | tuple )
            int alt9=5;
            switch ( input.LA(1) ) {
            case NAME:
                {
                alt9=1;
                }
                break;
            case NATNUM:
                {
                int LA9_2 = input.LA(2);

                if ( (LA9_2==52) ) {
                    alt9=3;
                }
                else if ( (LA9_2==RIGHTCURLY||LA9_2==RIGHTPARENS||(LA9_2>=COMMA && LA9_2<=SEMICOLON)||(LA9_2>=VAR && LA9_2<=DEFAULTOPTION)) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 2, input);

                    throw nvae;
                }
                }
                break;
            case 51:
                {
                alt9=3;
                }
                break;
            case STRING:
                {
                alt9=4;
                }
                break;
            case LEFTPARENS:
                {
                alt9=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:8: NAME
                    {
                    root_0 = (Tree)adaptor.nil();

                    NAME33=(Token)match(input,NAME,FOLLOW_NAME_in_value851); 
                    NAME33_tree = (Tree)adaptor.create(NAME33);
                    adaptor.addChild(root_0, NAME33_tree);


                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:15: NATNUM
                    {
                    root_0 = (Tree)adaptor.nil();

                    NATNUM34=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_value855); 
                    NATNUM34_tree = (Tree)adaptor.create(NATNUM34);
                    adaptor.addChild(root_0, NATNUM34_tree);


                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:24: floatnum
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_floatnum_in_value859);
                    floatnum35=floatnum();

                    state._fsp--;

                    adaptor.addChild(root_0, floatnum35.getTree());

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:35: STRING
                    {
                    root_0 = (Tree)adaptor.nil();

                    STRING36=(Token)match(input,STRING,FOLLOW_STRING_in_value863); 
                    STRING36_tree = (Tree)adaptor.create(STRING36);
                    adaptor.addChild(root_0, STRING36_tree);


                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:93:44: tuple
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_tuple_in_value867);
                    tuple37=tuple();

                    state._fsp--;

                    adaptor.addChild(root_0, tuple37.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class tuple_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tuple"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:94:1: tuple : LEFTPARENS tuplelist RIGHTPARENS ;
    public final SDLgrammarParser.tuple_return tuple() throws RecognitionException {
        SDLgrammarParser.tuple_return retval = new SDLgrammarParser.tuple_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token LEFTPARENS38=null;
        Token RIGHTPARENS40=null;
        SDLgrammarParser.tuplelist_return tuplelist39 = null;


        Tree LEFTPARENS38_tree=null;
        Tree RIGHTPARENS40_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:94:6: ( LEFTPARENS tuplelist RIGHTPARENS )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:94:8: LEFTPARENS tuplelist RIGHTPARENS
            {
            root_0 = (Tree)adaptor.nil();

            LEFTPARENS38=(Token)match(input,LEFTPARENS,FOLLOW_LEFTPARENS_in_tuple873); 
            LEFTPARENS38_tree = (Tree)adaptor.create(LEFTPARENS38);
            adaptor.addChild(root_0, LEFTPARENS38_tree);

            pushFollow(FOLLOW_tuplelist_in_tuple875);
            tuplelist39=tuplelist();

            state._fsp--;

            adaptor.addChild(root_0, tuplelist39.getTree());
            RIGHTPARENS40=(Token)match(input,RIGHTPARENS,FOLLOW_RIGHTPARENS_in_tuple877); 
            RIGHTPARENS40_tree = (Tree)adaptor.create(RIGHTPARENS40);
            adaptor.addChild(root_0, RIGHTPARENS40_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "tuple"

    public static class tuplelist_return extends ParserRuleReturnScope {
        Tree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "tuplelist"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:1: tuplelist : ( | value ( COMMA value )* );
    public final SDLgrammarParser.tuplelist_return tuplelist() throws RecognitionException {
        SDLgrammarParser.tuplelist_return retval = new SDLgrammarParser.tuplelist_return();
        retval.start = input.LT(1);

        Tree root_0 = null;

        Token COMMA42=null;
        SDLgrammarParser.value_return value41 = null;

        SDLgrammarParser.value_return value43 = null;


        Tree COMMA42_tree=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:10: ( | value ( COMMA value )* )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RIGHTPARENS) ) {
                alt11=1;
            }
            else if ( (LA11_0==LEFTPARENS||(LA11_0>=NAME && LA11_0<=NATNUM)||(LA11_0>=STRING && LA11_0<=51)) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:12: 
                    {
                    root_0 = (Tree)adaptor.nil();

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:14: value ( COMMA value )*
                    {
                    root_0 = (Tree)adaptor.nil();

                    pushFollow(FOLLOW_value_in_tuplelist885);
                    value41=value();

                    state._fsp--;

                    adaptor.addChild(root_0, value41.getTree());
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:20: ( COMMA value )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==COMMA) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:21: COMMA value
                    	    {
                    	    COMMA42=(Token)match(input,COMMA,FOLLOW_COMMA_in_tuplelist888); 
                    	    COMMA42_tree = (Tree)adaptor.create(COMMA42);
                    	    adaptor.addChild(root_0, COMMA42_tree);

                    	    pushFollow(FOLLOW_value_in_tuplelist890);
                    	    value43=value();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, value43.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Tree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Tree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "tuplelist"

    // Delegated rules


 

    public static final BitSet FOLLOW_51_in_floatnum565 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_floatnum568 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_floatnum570 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_floatnum572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statedesc_in_sdlfile742 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_STATEDESC_in_statedesc753 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_statedesc755 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_statedesc757 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_VERSION_in_statedesc759 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_statedesc761 = new BitSet(new long[]{0x0000040100000000L});
    public static final BitSet FOLLOW_varline_in_statedesc763 = new BitSet(new long[]{0x0000040100000000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_statedesc766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_varline787 = new BitSet(new long[]{0x0000404000000000L});
    public static final BitSet FOLLOW_type_in_varline789 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_varline791 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_LEFTSQUARE_in_varline793 = new BitSet(new long[]{0x0000801000000000L});
    public static final BitSet FOLLOW_NATNUM_in_varline796 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_RIGHTSQUARE_in_varline800 = new BitSet(new long[]{0x0000390000000002L});
    public static final BitSet FOLLOW_option_in_varline802 = new BitSet(new long[]{0x0000390000000002L});
    public static final BitSet FOLLOW_SEMICOLON_in_varline805 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_type812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_type816 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_type818 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_option825 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option827 = new BitSet(new long[]{0x000CC00200000000L});
    public static final BitSet FOLLOW_value_in_option829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULTOPTION_in_option833 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option835 = new BitSet(new long[]{0x000CC00200000000L});
    public static final BitSet FOLLOW_value_in_option837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISPLAYOPTION_in_option841 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option843 = new BitSet(new long[]{0x000CC00200000000L});
    public static final BitSet FOLLOW_value_in_option845 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_value851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NATNUM_in_value855 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_floatnum_in_value859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_value863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_value867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTPARENS_in_tuple873 = new BitSet(new long[]{0x000CC00600000000L});
    public static final BitSet FOLLOW_tuplelist_in_tuple875 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_RIGHTPARENS_in_tuple877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_tuplelist885 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_COMMA_in_tuplelist888 = new BitSet(new long[]{0x000CC00200000000L});
    public static final BitSet FOLLOW_value_in_tuplelist890 = new BitSet(new long[]{0x0000008000000002L});

}
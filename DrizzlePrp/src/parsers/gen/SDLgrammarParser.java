// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g 2010-05-31 20:46:25

//code to be placed at the start of the parser file.
package parsers.gen;
import parsers.sdlparser.*;
import java.util.Vector;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SDLgrammarParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "STATEDESC", "LEFTCURLY", "RIGHTCURLY", "LEFTPARENS", "RIGHTPARENS", "LEFTSQUARE", "RIGHTSQUARE", "EQUALS", "DOLLAR", "COMMA", "SEMICOLON", "VERSION", "VAR", "DEFAULT", "DISPLAYOPTION", "DEFAULTOPTION", "NAME", "NATNUM", "COMMENT", "WS", "ESC_SEQ", "STRING", "HEX_DIGIT", "UNICODE_ESC", "OCTAL_ESC", "'-'", "'.'"
    };
    public static final int DOLLAR=38;
    public static final int NATNUM=47;
    public static final int OCTAL_ESC=54;
    public static final int LEFTPARENS=33;
    public static final int EQUALS=37;
    public static final int VERSION=41;
    public static final int EOF=-1;
    public static final int STATEDESC=30;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int NAME=46;
    public static final int ESC_SEQ=50;
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
    public static final int UNICODE_ESC=53;
    public static final int I=12;
    public static final int DEFAULT=43;
    public static final int J=13;
    public static final int K=14;
    public static final int U=24;
    public static final int T=23;
    public static final int HEX_DIGIT=52;
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
    public static final int STRING=51;

    // delegates
    // delegators


        public SDLgrammarParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SDLgrammarParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return SDLgrammarParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g"; }


    public static class floatnum_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "floatnum"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:68:1: floatnum : ( '-' )? NATNUM '.' NATNUM ;
    public final SDLgrammarParser.floatnum_return floatnum() throws RecognitionException {
        SDLgrammarParser.floatnum_return retval = new SDLgrammarParser.floatnum_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:68:9: ( ( '-' )? NATNUM '.' NATNUM )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:68:11: ( '-' )? NATNUM '.' NATNUM
            {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:68:11: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==55) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:68:11: '-'
                    {
                    match(input,55,FOLLOW_55_in_floatnum564); 

                    }
                    break;

            }

            match(input,NATNUM,FOLLOW_NATNUM_in_floatnum567); 
            match(input,56,FOLLOW_56_in_floatnum569); 
            match(input,NATNUM,FOLLOW_NATNUM_in_floatnum571); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "floatnum"


    // $ANTLR start "sdlfile"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:1: sdlfile returns [Sdlfile val] : ( statedesc )* ;
    public final Sdlfile sdlfile() throws RecognitionException {
        Sdlfile val = null;

        Statedesc statedesc1 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:30: ( ( statedesc )* )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:32: ( statedesc )*
            {
            val = new Sdlfile();
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:56: ( statedesc )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==STATEDESC) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:95:57: statedesc
            	    {
            	    pushFollow(FOLLOW_statedesc_in_sdlfile761);
            	    statedesc1=statedesc();

            	    state._fsp--;

            	    val.add(statedesc1);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "sdlfile"


    // $ANTLR start "statedesc"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:97:1: statedesc returns [Statedesc val] : STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY ;
    public final Statedesc statedesc() throws RecognitionException {
        Statedesc val = null;

        Token NAME2=null;
        Token NATNUM3=null;
        Varline varline4 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:97:34: ( STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:97:36: STATEDESC NAME LEFTCURLY VERSION NATNUM ( varline )* RIGHTCURLY
            {
            match(input,STATEDESC,FOLLOW_STATEDESC_in_statedesc777); 
            NAME2=(Token)match(input,NAME,FOLLOW_NAME_in_statedesc779); 
            match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_statedesc781); 
            match(input,VERSION,FOLLOW_VERSION_in_statedesc783); 
            NATNUM3=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_statedesc785); 
            val = new Statedesc((NAME2!=null?NAME2.getText():null),Integer.parseInt((NATNUM3!=null?NATNUM3.getText():null)));
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:97:143: ( varline )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==VAR) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:97:144: varline
            	    {
            	    pushFollow(FOLLOW_varline_in_statedesc790);
            	    varline4=varline();

            	    state._fsp--;

            	    val.add(varline4);

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_statedesc796); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "statedesc"


    // $ANTLR start "varline"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:1: varline returns [Varline val] : VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )? ;
    public final Varline varline() throws RecognitionException {
        Varline val = null;

        Token NAME6=null;
        Token NATNUM7=null;
        Type type5 = null;

        Option option8 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:30: ( VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )? )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:32: VAR type NAME LEFTSQUARE ( NATNUM | ) RIGHTSQUARE ( option )* ( SEMICOLON )?
            {
            match(input,VAR,FOLLOW_VAR_in_varline808); 
            pushFollow(FOLLOW_type_in_varline810);
            type5=type();

            state._fsp--;

            NAME6=(Token)match(input,NAME,FOLLOW_NAME_in_varline812); 
            val = new Varline(type5,(NAME6!=null?NAME6.getText():null));
            match(input,LEFTSQUARE,FOLLOW_LEFTSQUARE_in_varline816); 
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:101: ( NATNUM | )
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
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:102: NATNUM
                    {
                    NATNUM7=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_varline819); 
                    val.setIndex(Integer.parseInt((NATNUM7!=null?NATNUM7.getText():null)));

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:157: 
                    {
                    }
                    break;

            }

            match(input,RIGHTSQUARE,FOLLOW_RIGHTSQUARE_in_varline824); 
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:171: ( option )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=DEFAULT && LA5_0<=DEFAULTOPTION)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:172: option
            	    {
            	    pushFollow(FOLLOW_option_in_varline827);
            	    option8=option();

            	    state._fsp--;

            	    val.add(option8);

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:206: ( SEMICOLON )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==SEMICOLON) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:99:206: SEMICOLON
                    {
                    match(input,SEMICOLON,FOLLOW_SEMICOLON_in_varline833); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "varline"


    // $ANTLR start "type"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:100:1: type returns [Type val] : ( NAME | DOLLAR NAME );
    public final Type type() throws RecognitionException {
        Type val = null;

        Token NAME9=null;
        Token NAME10=null;

        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:100:24: ( NAME | DOLLAR NAME )
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
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:100:26: NAME
                    {
                    NAME9=(Token)match(input,NAME,FOLLOW_NAME_in_type844); 
                    val = new TypeStd((NAME9!=null?NAME9.getText():null));

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:100:66: DOLLAR NAME
                    {
                    match(input,DOLLAR,FOLLOW_DOLLAR_in_type849); 
                    NAME10=(Token)match(input,NAME,FOLLOW_NAME_in_type851); 
                    val = new TypeStatedesc((NAME10!=null?NAME10.getText():null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "type"


    // $ANTLR start "option"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:101:1: option returns [Option val] : ( DEFAULT EQUALS value | DEFAULTOPTION EQUALS value | DISPLAYOPTION EQUALS value );
    public final Option option() throws RecognitionException {
        Option val = null;

        Value value11 = null;

        Value value12 = null;

        Value value13 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:101:28: ( DEFAULT EQUALS value | DEFAULTOPTION EQUALS value | DISPLAYOPTION EQUALS value )
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
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:101:30: DEFAULT EQUALS value
                    {
                    match(input,DEFAULT,FOLLOW_DEFAULT_in_option863); 
                    match(input,EQUALS,FOLLOW_EQUALS_in_option865); 
                    pushFollow(FOLLOW_value_in_option867);
                    value11=value();

                    state._fsp--;

                    val = new OptionDefault(value11);

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:101:93: DEFAULTOPTION EQUALS value
                    {
                    match(input,DEFAULTOPTION,FOLLOW_DEFAULTOPTION_in_option873); 
                    match(input,EQUALS,FOLLOW_EQUALS_in_option875); 
                    pushFollow(FOLLOW_value_in_option877);
                    value12=value();

                    state._fsp--;

                    val = new OptionDefaultoption(value12);

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:101:168: DISPLAYOPTION EQUALS value
                    {
                    match(input,DISPLAYOPTION,FOLLOW_DISPLAYOPTION_in_option883); 
                    match(input,EQUALS,FOLLOW_EQUALS_in_option885); 
                    pushFollow(FOLLOW_value_in_option887);
                    value13=value();

                    state._fsp--;

                    val = new OptionDisplayoption(value13);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "option"


    // $ANTLR start "value"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:1: value returns [Value val] : ( NAME | NATNUM | floatnum | STRING | tuple );
    public final Value value() throws RecognitionException {
        Value val = null;

        Token NAME14=null;
        Token NATNUM15=null;
        Token STRING17=null;
        SDLgrammarParser.floatnum_return floatnum16 = null;

        ValueTuple tuple18 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:26: ( NAME | NATNUM | floatnum | STRING | tuple )
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

                if ( (LA9_2==56) ) {
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
            case 55:
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
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:28: NAME
                    {
                    NAME14=(Token)match(input,NAME,FOLLOW_NAME_in_value899); 
                    val = new ValueName((NAME14!=null?NAME14.getText():null));

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:71: NATNUM
                    {
                    NATNUM15=(Token)match(input,NATNUM,FOLLOW_NATNUM_in_value905); 
                    val = new ValueInt(Integer.parseInt((NATNUM15!=null?NATNUM15.getText():null)));

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:135: floatnum
                    {
                    pushFollow(FOLLOW_floatnum_in_value911);
                    floatnum16=floatnum();

                    state._fsp--;

                    val = new ValueFloat(Float.parseFloat((floatnum16!=null?input.toString(floatnum16.start,floatnum16.stop):null)));

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:205: STRING
                    {
                    STRING17=(Token)match(input,STRING,FOLLOW_STRING_in_value917); 
                    val = new ValueString((STRING17!=null?STRING17.getText():null));

                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:102:255: tuple
                    {
                    pushFollow(FOLLOW_tuple_in_value924);
                    tuple18=tuple();

                    state._fsp--;

                    val = tuple18;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "value"


    // $ANTLR start "tuple"
    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:1: tuple returns [ValueTuple val] : LEFTPARENS ( | v1= value ( COMMA v2= value )* ) RIGHTPARENS ;
    public final ValueTuple tuple() throws RecognitionException {
        ValueTuple val = null;

        Value v1 = null;

        Value v2 = null;


        try {
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:31: ( LEFTPARENS ( | v1= value ( COMMA v2= value )* ) RIGHTPARENS )
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:33: LEFTPARENS ( | v1= value ( COMMA v2= value )* ) RIGHTPARENS
            {
            match(input,LEFTPARENS,FOLLOW_LEFTPARENS_in_tuple938); 
            val = new ValueTuple();
            // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:71: ( | v1= value ( COMMA v2= value )* )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RIGHTPARENS) ) {
                alt11=1;
            }
            else if ( (LA11_0==LEFTPARENS||(LA11_0>=NAME && LA11_0<=NATNUM)||LA11_0==STRING||LA11_0==55) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:73: 
                    {
                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:75: v1= value ( COMMA v2= value )*
                    {
                    pushFollow(FOLLOW_value_in_tuple948);
                    v1=value();

                    state._fsp--;

                    val.add(v1);
                    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:105: ( COMMA v2= value )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==COMMA) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // C:\\Documents and Settings\\user\\Desktop\\drizzle\\DrizzlePrp\\grammars\\SDLgrammar.g:105:106: COMMA v2= value
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_tuple953); 
                    	    pushFollow(FOLLOW_value_in_tuple957);
                    	    v2=value();

                    	    state._fsp--;

                    	    val.add(v2);

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RIGHTPARENS,FOLLOW_RIGHTPARENS_in_tuple965); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return val;
    }
    // $ANTLR end "tuple"

    // Delegated rules


 

    public static final BitSet FOLLOW_55_in_floatnum564 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_floatnum567 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_floatnum569 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_floatnum571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statedesc_in_sdlfile761 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_STATEDESC_in_statedesc777 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_statedesc779 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LEFTCURLY_in_statedesc781 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_VERSION_in_statedesc783 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_NATNUM_in_statedesc785 = new BitSet(new long[]{0x0000040100000000L});
    public static final BitSet FOLLOW_varline_in_statedesc790 = new BitSet(new long[]{0x0000040100000000L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_statedesc796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_varline808 = new BitSet(new long[]{0x0000404000000000L});
    public static final BitSet FOLLOW_type_in_varline810 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_varline812 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_LEFTSQUARE_in_varline816 = new BitSet(new long[]{0x0000801000000000L});
    public static final BitSet FOLLOW_NATNUM_in_varline819 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_RIGHTSQUARE_in_varline824 = new BitSet(new long[]{0x0000390000000002L});
    public static final BitSet FOLLOW_option_in_varline827 = new BitSet(new long[]{0x0000390000000002L});
    public static final BitSet FOLLOW_SEMICOLON_in_varline833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_type844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOLLAR_in_type849 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_NAME_in_type851 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_option863 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option865 = new BitSet(new long[]{0x0088C00200000000L});
    public static final BitSet FOLLOW_value_in_option867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULTOPTION_in_option873 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option875 = new BitSet(new long[]{0x0088C00200000000L});
    public static final BitSet FOLLOW_value_in_option877 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DISPLAYOPTION_in_option883 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_EQUALS_in_option885 = new BitSet(new long[]{0x0088C00200000000L});
    public static final BitSet FOLLOW_value_in_option887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_value899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NATNUM_in_value905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_floatnum_in_value911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_value917 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_value924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTPARENS_in_tuple938 = new BitSet(new long[]{0x0088C00600000000L});
    public static final BitSet FOLLOW_value_in_tuple948 = new BitSet(new long[]{0x0000008400000000L});
    public static final BitSet FOLLOW_COMMA_in_tuple953 = new BitSet(new long[]{0x0088C00200000000L});
    public static final BitSet FOLLOW_value_in_tuple957 = new BitSet(new long[]{0x0000008400000000L});
    public static final BitSet FOLLOW_RIGHTPARENS_in_tuple965 = new BitSet(new long[]{0x0000000000000002L});

}